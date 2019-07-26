package com.bdsoft.datamin.fetch.jd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.transaction.CannotCreateTransactionException;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.fetch.jd.product.FetchProduct;
import com.bdsoft.datamin.fetch.jd.review.FetchReview;
import com.bdsoft.datamin.mapper.JDProductMapper;
import com.bdsoft.datamin.mapper.JDQueueMapper;
import com.bdsoft.datamin.mapper.JDReviewsMapper;
import com.bdsoft.datamin.mapper.JDUserMapper;
import com.bdsoft.datamin.mapper.JDVenderMapper;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.DBUtils;
import com.bdsoft.datamin.util.DBUtils.DBProc;
import com.bdsoft.datamin.util.DateUtil;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.cmd.MySQLUtil;
import com.bdsoft.datamin.util.mail.BDMailer;

/**
 * 抓取京东商品/评论
 *
 * @author 丁辰叶
 * @date 2016-5-26
 * @version 1.0.0
 */
public class JDController {

	private static final int JD_PRO_FETCHER = 10; // 抓取商品-线程

	private static final int JD_REV_FETCHER = 9; // 抓取评论-线程

	private static final int JD_REV_SUB_FETCHER = 14;// 抓取评论-最大子线程数

	// 每次读取多少个URL进入商品队列
	private static int MAX_LOAD = 2000;

	public static final String LOAD_ORDER = "asc";

	// 待抓评论队列超过该阀值，商品抓取线程挂起等待
	private static final int maxREVqsize = JD_REV_FETCHER * 2;

	// 商品队列小于该阀值，触发加载新的URL入队列{TODO 可以指定公式，动态定义}
	private static final int minItem = JD_PRO_FETCHER * 2;

	// mysql服务检测相关
	public static AtomicBoolean MYSQL_OK = new AtomicBoolean(true);

	public static AtomicBoolean MYSQL_HANDING = new AtomicBoolean(false);

	public static final long MYSQL_START_WAIT = 1000L * 5;// 重启耗时，线程挂起时间

	// 系统刚启动初始化
	static {
		// 检测mysql服务状态
		MySQLUtil.start();

		// 初始化spring容器
		BDSpringUtil.init();
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) {
		// 从db读取商品url加入队列
		DB4jd db = new DB4jd();
		DBUtils du = DBUtils.getInstance();

		// --------------------------------------------------------------
		// 抓取队列核心控制类
		JdQueues queues = new JdQueues(db);

		// 商品队列-生产者线程
		JdProductFeeder jdpFeeder = new JdProductFeeder(db, queues, du);
		jdpFeeder.setName("#商品-队列线程#");
		jdpFeeder.start();

		// 商品队列-消费者线程 兼 评论队列-生产者线程
		for (int i = 0; i < JD_PRO_FETCHER; i++) {
			Thread jdpFetcher = new JdProductFetcher(db, queues);
			jdpFetcher.setName("#商品-抓取线程#>" + (i + 1));
			jdpFetcher.start();
		}

		// 评论队列-消费者
		for (int i = 0; i < JD_REV_FETCHER; i++) {
			Thread revFetcher = new JdReviewFetcher(queues);
			revFetcher.setName("#评论-抓取线程#>" + (i + 1));
			revFetcher.start();
		}
		// --------------------------------------------------------------

		// 数据库状态监控
		DBMoniter dbm = new DBMoniter();
		dbm.setDaemon(true);
		dbm.start();
	}

	// 数据库服务监控
	private static class DBMoniter extends Thread {

		private long checkDBInteval = 1000 * 3;// 秒

		private String title = "京东抓取-数据库异常";

		private String msg = "数据库异常检测时间：";

		public void run() {
			while (true) {
				try {
					if (MYSQL_OK.get()) {
						System.out.println("数据库服务正常 @" + new Date().toLocaleString());
						Thread.sleep(checkDBInteval);
						continue;
					}
					System.out.println("数据库服务异常，开始处理");
					MYSQL_HANDING.set(true);
					// 发邮件通知
					BDMailer.alarm(this.title, this.msg);

					boolean op = MySQLUtil.start();
					System.out.println("处理结束，恢复状态");
					MYSQL_OK.set(op);
					MYSQL_HANDING.set(false);

					if (op) {
						BDMailer.regain(this.title, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 京东抓取队列 - 主控类
	private static class JdQueues {

		// 访问DB支持
		private DB4jd db;

		// 商品队列
		List<JDQueue> pdtQueue = Collections.synchronizedList(new ArrayList<JDQueue>());

		// 评论队列
		List<JDProduct> revQueue = Collections.synchronizedList(new ArrayList<JDProduct>());

		// 服务监控
		List<BDListener> serviceLis = new ArrayList<BDListener>();

		// 失败抓取，最大尝试重新抓取次数
		private int maxRetry = 5;

		// 失败抓取队列，统计
		ConcurrentMap<String, AtomicInteger> retryJdqC = new ConcurrentHashMap<String, AtomicInteger>();

		AtomicLong pdtQcount = new AtomicLong(0);// 商品队列统计

		AtomicLong pdtQsuc = new AtomicLong(0);// 商品抓取成功统计

		AtomicLong revQcount = new AtomicLong(0);// 评论队列统计

		AtomicLong revQsuc = new AtomicLong(0);// 评论抓取成功统计

		public JdQueues(DB4jd db) {
			this.db = db;
			// 注册当前支持的监控事件
			serviceLis.add(new BDMySQLListener());
		}

		// 触发服务监听，遍历各服务状态，处理问题
		public synchronized void errReport() {
			System.out.println("接到错误事件上报...");
			for (BDListener lis : serviceLis) {
				System.out.println("处理错误事件");
				lis.handle();
			}
		}

		/**
		 * 入商品队列，刷新统计
		 * 
		 * @param jdqs
		 */
		public synchronized void enPDTqueue(List<JDQueue> jdqs) {
			pdtQueue.addAll(jdqs);
			pdtQcount.addAndGet(jdqs.size());
			this.dump();
		}

		/**
		 * 入商品队列，初始放入 或 出错放入以备重新抓
		 * 
		 * @param jdq
		 * @param reEnQ
		 *            是否出错重新放
		 */
		public synchronized void enPDTqueue(JDQueue jdq, boolean reEnQ) {
			if (reEnQ) {
				String url = jdq.getUrl();
				AtomicInteger c = retryJdqC.get(url);
				if (c == null) {
					retryJdqC.put(url, new AtomicInteger(1));
				} else {
					System.out.println("商品抓取失败：" + jdq.getUrl());
					if (c.get() >= maxRetry) {
						retryJdqC.remove(url);// 超过指定次数，移除
						ctPDTsuc();
						jdq.setQstatus(2);
						jdq.setFerr("商品抓取失败，超过" + maxRetry + "次");
						db.updateJdQueue(jdq);// 更新数据库
						return;
					}
					// 刷新尝试次数
					c.incrementAndGet();
					retryJdqC.put(url, c);
				}
			}
			pdtQueue.add(jdq);
			pdtQcount.incrementAndGet();
		}

		public synchronized void reEnPDTqueue(JDQueue jdq) {
			pdtQueue.add(jdq);
			pdtQcount.incrementAndGet();
		}

		/**
		 * 从商品队列提取一个URL
		 * 
		 * @return
		 */
		public synchronized JDQueue dePDTqueue() {
			if (pdtQueue.size() == 0) {
				return null;
			}
			JDQueue jdq = pdtQueue.remove(0);
			return jdq;
		}

		// 刷新商品队列成功数统计
		public void ctPDTsuc() {
			pdtQsuc.incrementAndGet();
		}

		// 抓取成功，从尝试抓取队列中移除统计
		public void rmvRetry(JDQueue jdq) {
			retryJdqC.remove(jdq.getUrl());
		}

		/**
		 * 入评论队列
		 * 
		 * @param jdp
		 */
		public synchronized void enREVqueue(JDProduct jdp) {
			revQueue.add(jdp);
			revQcount.incrementAndGet();
		}

		public synchronized void reEnREVqueue(JDProduct jdp) {
			revQueue.add(0, jdp);
			revQcount.incrementAndGet();
		}

		/**
		 * 从评论队列提取一个URL
		 * 
		 * @return
		 */
		public synchronized JDProduct deREVqueue() {
			if (revQueue.size() == 0) {
				return null;
			}
			return revQueue.remove(0);
		}

		// 刷新评论队列成功数统计
		public void ctREVsuc() {
			revQsuc.incrementAndGet();
		}

		public int getPDTQsize() {
			this.dump();
			return pdtQueue.size();
		}

		public int getREVQsize() {
			this.dump();
			return revQueue.size();
		}

		// 检查评论队列，以免待抓评论太多
		public synchronized boolean checkREVqueue() {
			if (getREVQsize() > maxREVqsize) {
				return false;
			}
			return true;
		}

		// 输出队列统计
		public String dump() {
			StringBuffer console = new StringBuffer();
			console.append("\n----------------------------------------");
			console.append("\n商品 >> 队列：");
			console.append("\n\t队列总数：" + pdtQcount.get());
			console.append("\n\t已处理：" + pdtQsuc.get());
			console.append("\n\t待处理：" + pdtQueue.size());
			console.append("\n");
			console.append("\n评论 >> 队列：");
			console.append("\n\t队列总数：" + revQcount.get());
			console.append("\n\t已处理：" + revQsuc.get());
			console.append("\n\t待处理：" + revQueue.size());
			console.append("\n");

			console.append(",[Pt-" + JD_PRO_FETCHER);
			console.append(",Rt-" + JD_REV_FETCHER);
			console.append("/" + JD_REV_SUB_FETCHER + "]");

			console.append("\n统计时间：" + new Date().toLocaleString());
			console.append("\n----------------------------------------");
			console.append("\n");
			String dump = console.toString();
			System.out.println(dump);
			return dump;
		}
	}

	// 评论队列-消费者线程
	private static class JdReviewFetcher extends Thread {

		private JdQueues queues; // 队列访问支持

		private long subSleep = 1000L * 5;// 秒，子线程抓取过程，检测时间间隔

		private long waitSleep = 1000L * 3;// 秒，队列空，等待时间

		private long subStartSleep = 1L * 100;// 毫秒，子线程启动延迟

		private boolean thisPause = false; // 子线程是否在执行抓取

		public JdReviewFetcher(JdQueues queues) {
			this.queues = queues;
		}

		public void setThisPause(boolean thisPause) {
			this.thisPause = thisPause;
		}

		// 抓取评论线程逻辑实现
		public void run() {
			while (true) {
				try {
					// 检测DB状态
					if (!MYSQL_OK.get()) {
						System.out.println(getName() + ",MySQL服务维护中，等待...");
						Thread.sleep(MYSQL_START_WAIT);
						continue;
					}

					// 如果子线程正在抓取，等待子线程执行完毕再开始下一个商品的评论抓取
					if (thisPause) {
						System.out.println(getName() + ",子线程正在抓取，等待...");
						Thread.sleep(subSleep);
						continue;
					}

					// 如果评论队列空，需要等待
					JDProduct jdp = queues.deREVqueue();
					if (jdp == null) {
						System.out.println(getName() + ",评论队列空，等待...");
						Thread.sleep(waitSleep);
						continue;
					}

					// 计算分页数，使得子线程可以平均分发处理抓取评论
					int count = jdp.getRvecount();// 商品总评论数
					if (count > 0) {
						int mod = count % FetchReview.RLIST_PAGESIZE;
						int shang = count / FetchReview.RLIST_PAGESIZE;
						int pages = (mod == 0) ? shang : shang + 1;// 商品评论总页数

						mod = pages % JD_REV_SUB_FETCHER;// 是否最大线程 可以正好平均处理
						int per = pages / JD_REV_SUB_FETCHER; // 平均每个线程处理多少页数据

						int thrds = (pages <= JD_REV_SUB_FETCHER) ? pages : JD_REV_SUB_FETCHER;// 计划分配执行抓取的线程数
						System.out.println("\t商品评论总页数：" + pages + "，计划启动子线程数：" + thrds + "，URL=" + jdp.getPurl());

						// 所有子线程任务结束处理逻辑
						JdEndReviewFetcher endTask = new JdEndReviewFetcher(queues, this, jdp);
						// 并发障碍器
						CyclicBarrier cb = new CyclicBarrier(thrds, endTask);
						// 启动子线程分发处理，分页抓取评论
						int s = 0, e = 0;
						if (pages <= JD_REV_SUB_FETCHER) {
							for (int i = 0; i < thrds; i++) {
								String n = getName() + "-#子线程#>" + (i + 1);
								e++;
								Runnable task = new JdSubReviewFetcher(jdp, s, e, cb, n);
								new Thread(task).start();
								s = e;
								Thread.sleep(subStartSleep);
							}
						} else {
							for (int i = 0; i < thrds; i++) {
								String n = getName() + "-#子线程#>" + (i + 1);
								e = (i + 1) * per;
								if ((i == (thrds - 1)) && (mod > 0)) {
									e += mod;
								}
								Runnable task = new JdSubReviewFetcher(jdp, s, e, cb, n);
								new Thread(task).start();
								s = e;
								Thread.sleep(subStartSleep);
							}
						}
					} else {
						queues.ctREVsuc();
						System.out.println("\t商品无评论：" + jdp.getPurl());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 评论抓取子线程，结束抓取逻辑
	private static class JdEndReviewFetcher implements Runnable {

		private JdQueues queues;

		private JdReviewFetcher holdThread;

		private JDProduct jdp;

		public JdEndReviewFetcher(JdQueues queues, JdReviewFetcher holdThrd, JDProduct jdp) {
			this.queues = queues;
			this.holdThread = holdThrd;
			this.holdThread.setThisPause(true);
			this.jdp = jdp;
		}

		// 结束抓取逻辑实现
		public void run() {
			try {
				// 刷新评论抓取统计
				queues.ctREVsuc();
				// 所有评论子线程抓取完毕，通知触发抓取下一个商品的评论
				this.holdThread.setThisPause(false);
				System.out.println(this.holdThread.getName() + ",所有子线程执行完毕.");

				// 数据库异常
				if (!MYSQL_OK.get()) {
					// 将正在抓取的商品重新放进评论队列
					queues.reEnREVqueue(this.jdp);
					System.out.println("数据库异常，导致抓取评论失败的商品，已重新放入队列");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 评论抓取子线程，具体负责抓取评论逻辑实现
	private static class JdSubReviewFetcher implements Runnable {

		private String name;

		// 并发-障碍器
		private CyclicBarrier cb;

		// 商品信息，及分页范围
		private JDProduct jdp;

		private int start;

		private int end;

		// 评论抓取
		private FetchReview fetcher;

		public JdSubReviewFetcher(JDProduct jdp, int s, int e, CyclicBarrier cb, String name) {
			this.jdp = jdp;
			this.start = s;
			this.end = e;
			this.cb = cb;
			this.name = name;
			this.fetcher = new FetchReview();
		}

		// 抓取子线程实现逻辑
		public void run() {
			try {
				// 执行抓取，指派抓取范围，从start-end页
				System.out.println(name + "，启动...");
				fetcher.fetch(jdp, start, end);
				System.out.println(name + "，执行完毕.");
			} catch (CannotCreateTransactionException pe) {
				System.out.println(name + "，检测到数据库异常：" + pe.getMessage());
				// 改变相关服务状态，在障碍器最终线程中执行错误上报
				MYSQL_OK.compareAndSet(!MYSQL_HANDING.get(), false);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					cb.await(); // 成功与否，都会通知障碍器，子线程抓取完毕
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	// 商品队列-消费者线程
	private static class JdProductFetcher extends Thread {

		private DB4jd db;

		private JdQueues queues; // 队列访问支持

		private FetchProduct fetcher;// 抓取商品实现类

		private long fetcherSleep = 1L * 100;// 毫秒，抓取成功休眠间隔时间

		private long waitSleep = 1000L * 3;// 秒，队列空等待时间

		private long reviewSleep = 1000L * 15;// 秒，评论队列过大时等待时间

		public JdProductFetcher(DB4jd db, JdQueues queues) {
			this.db = db;
			this.queues = queues;
			this.fetcher = new FetchProduct();
		}

		// 抓取商品线程逻辑实现
		public void run() {
			while (true) {
				JDQueue jdq = null;
				try {
					// 检测DB状态
					if (!MYSQL_OK.get()) {
						System.out.println(getName() + ",MySQL服务维护中，等待...");
						Thread.sleep(MYSQL_START_WAIT);
						continue;
					}

					// 检查评论队列，如果评论抓取慢，需要等待
					if (!queues.checkREVqueue()) {
						System.out.println(getName() + ",待抓评论总数超过:" + maxREVqsize + "，等待...");
						Thread.sleep(reviewSleep);
						continue;
					}

					// 如果商品队列空，需要等待
					jdq = queues.dePDTqueue();
					if (jdq == null) {
						System.out.println(getName() + ",商品队列空，等待...");
						Thread.sleep(waitSleep);
						continue;
					}

					// 执行抓取
					JDProduct jdp = fetcher.fetch(jdq);
					if (jdp == null) { // 抓取失败，重新放入队列，等待再次尝试抓取
						queues.enPDTqueue(jdq, true);
					} else {
						// 抓取成功，刷新统计数
						queues.ctPDTsuc();
						queues.rmvRetry(jdq);

						// 2013-12-09，取消抓取衣服类商品
						if (jdp.getRvecount() > 0) {
							if (StringUtil.isEmpty(jdp.getCat1Code())) {
								System.out.println("分类：一级分类未知，暂不抓取评论");
								// } else if
								// (!StringUtil.isEmpty(jdp.getCat1Code())
								// && jdp.getCat1Code().equals("1315")) {
								// System.out.println("分类：服饰鞋帽，暂不抓取评论");
							} else {
								// 触发评论抓取队列
								queues.enREVqueue(jdp);
								System.out.println(getName() + "，成功入评论队列：" + jdp.getPname());
							}
						}

						// 更新jd_queue表状态：抓取成功
						jdq.setQstatus(1);
						if (jdp.getPstat() == 3) {// 下架商品不再维护
							jdq.setQstatus(3);// 不再抓取
						}
						db.updateJdQueue(jdq);
					}
					Thread.sleep(fetcherSleep);
				} catch (CannotCreateTransactionException pe) {
					System.out.println(getName() + "，检测到数据库异常：" + pe.getMessage());
					// 将正在抓取的商品重新放进商品队列
					queues.reEnPDTqueue(jdq);
					// 改变相关服务状态，上报错误
					MYSQL_OK.compareAndSet(!MYSQL_HANDING.get(), false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 商品队列-生产者线程类
	 * 
	 * @author bdceo
	 * 
	 */
	private static class JdProductFeeder extends Thread {

		private DB4jd db; // db访问支持

		private JdQueues queues; // 访问队列支持

		private DBUtils du = null;

		private long products = 0l;

		private long users = 0l;

		private long reviews = 0l;

		private long nowProducts = 0l;

		private long nowUsers = 0l;

		private long nowReviews = 0l;

		private int loop = 0;

		private Date lastDump = null;

		private long queueCheck = 1000L * 5;// 检测商品队列时间间隔

		public JdProductFeeder(DB4jd db, JdQueues queues, DBUtils du) {
			this.db = db;
			this.queues = queues;
			this.du = du;
		}

		// 生产者线程执行逻辑
		public void run() {
			init();
			while (true) {
				try {
					// 检测DB状态
					if (!MYSQL_OK.get()) {
						System.out.println(getName() + ",MySQL服务维护中，等待...");
						Thread.sleep(MYSQL_START_WAIT);
						continue;
					}

					// 获取当前商品队列大小，小于检测阀值，触发加载新的URL入队列
					int size = queues.getPDTQsize();
					if (size <= minItem) {
						reset(); // 刷新抓取统计起始值
						dump();// 输出控制台，或发邮件

						load();// 读DB，加载URL，入商品队列

						// 新加载的数据，10分钟后才开始检测
						Thread.sleep(1000 * 60 * 10);
						continue;
					}
					Thread.sleep(queueCheck);
				} catch (CannotCreateTransactionException pe) {
					System.out.println(getName() + "，检测到数据库异常：" + pe.getMessage());
					// 改变相关服务状态，上报错误
					MYSQL_OK.compareAndSet(!MYSQL_HANDING.get(), false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void init() {
			// 读取数据库抓取统计
			Map<String, Integer> data = du.callProc(DBProc.JD_MAX);
			if (data.size() > 0) {
				this.products = this.nowProducts = data.get("product");
				this.users = this.nowUsers = data.get("user");
				this.reviews = this.nowReviews = data.get("review");
				System.out.println("最新统计：P-" + nowProducts + ",R-" + nowReviews + ",U-" + nowUsers);
			}
			// 第一次启动，初始化队列
			load();
		}

		private void load() {
			// 抓取周期统计
			loop++;
			lastDump = new Date();

			List<JDQueue> jdqs = db.readJdQueue(MAX_LOAD);
			DateUtil.cost("从DB提取URL队列", lastDump);

			System.out.println();
			if (jdqs != null && jdqs.size() > 0) {
				queues.enPDTqueue(jdqs);
				System.out.println(getName() + "，入商品队列=" + jdqs.size());
			}
		}

		public void reset() {
			try {
				Map<String, Integer> data = du.callProc(DBProc.JD_MAX);
				this.nowProducts = data.get("product");
				this.nowUsers = data.get("user");
				this.nowReviews = data.get("review");
				System.out.println("最新统计：P-" + nowProducts + ",R-" + nowReviews + ",U-" + nowUsers);
			} catch (CannotCreateTransactionException pe) {
				System.out.println(getName() + "，检测到数据库异常：" + pe.getMessage());
				// 改变相关服务状态，上报错误
				MYSQL_OK.compareAndSet(!MYSQL_HANDING.get(), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 输出到控制台并发送统计邮件
		public void dump() {
			// 统计每轮抓取量
			long newPs = this.nowProducts - this.products;
			long newRs = this.nowReviews - this.reviews;
			long newUs = this.nowUsers - this.users;

			// 控制台输出和邮件内容
			StringBuffer console = new StringBuffer();
			console.append("----------------京东-抓取统计----------------------");
			console.append("\n统计第 <").append(loop).append("> 轮抓取");
			console.append("\n开始时间：").append(lastDump.toLocaleString());
			console.append("\n结束时间：").append(new Date().toLocaleString());
			console.append("\n\t商品总数：").append(this.nowProducts);
			console.append("\t新抓取：").append(newPs);
			console.append("\n\t评论总数：").append(this.nowReviews);
			console.append("\t新抓取：").append(newRs);
			console.append("\n\t用户总数：").append(this.nowUsers);
			console.append("\t新抓取：").append(newUs);
			console.append("\n----------------京东-抓取统计----------------------");

			// 邮件标题
			StringBuffer cheaf = new StringBuffer();
			cheaf.append("<").append(loop).append(">");
			cheaf.append("[P-").append(newPs).append(",R-").append(newRs).append(",U-").append(newUs).append("]");
			cheaf.append(",[Pt-").append(JD_PRO_FETCHER).append(",Rt-").append(JD_REV_FETCHER).append("/");
			cheaf.append(JD_REV_SUB_FETCHER).append("]");

			// 刷新统计
			this.products = this.nowProducts;
			this.reviews = this.nowReviews;
			this.users = this.nowUsers;

			String content = console.toString();
			System.out.println(content);

			String title = cheaf.toString();
			BDMailer.jdFetchCount(title, content);

			// TODO 写db，统计抓取效率{线程数，抓取一轮时间，抓取商品，评论，用户等数统计……}
			// 可以写成MapReduce任务统计
		}
	}

	// 系统异常监控
	interface BDListener {
		void handle();
	}

	// 针对数据库服务的监控
	private static class BDMySQLListener implements BDListener {

		private boolean handling = false;

		private Object lock = new Object();

		public BDMySQLListener() {
		}

		public void handle() {
		}
	}

}

class DB4jd {

	private JDQueueMapper jdqDao;

	private JDUserMapper jduDao;

	private JDReviewsMapper jdrDao;

	private JDProductMapper jdpDao;

	private JDVenderMapper jdvDao;

	public DB4jd() {
		this.jdqDao = BDSpringUtil.getBean(JDQueueMapper.class);
		this.jduDao = BDSpringUtil.getBean(JDUserMapper.class);
		this.jdrDao = BDSpringUtil.getBean(JDReviewsMapper.class);
		this.jdpDao = BDSpringUtil.getBean(JDProductMapper.class);
		this.jdvDao = BDSpringUtil.getBean(JDVenderMapper.class);
	}

	// 从jd_queue中读取指定数目待抓URL
	public List<JDQueue> readJdQueue(int sum) {
		// String sql = "select * from jd_queue where qstatus=0 order by ctime "
		// + JDFetcher.LOAD_ORDER + " limit " + sum;
		List<JDQueue> jdqs = jdqDao.selectUnFetchQueue(JDController.LOAD_ORDER, sum);

		// 20131215：每次读取新队列数据时删除已抓商品，较耗时，独线程处理
		new Thread() {
			public void run() {
				System.out.println("单独线程，从DB中删除已抓商品URL");
				// jdqDao.excuteSQL("delete from jd_queue where qstatus>0");
				jdqDao.deleteFetchedQueue();
			}
		}.start();
		return jdqs;
	}

	// 更新抓取URL的状态
	public boolean updateJdQueue(JDQueue jdq) {
		if (jdq.getRtime() != null) {
			jdq.setFtime(jdq.getRtime());
			jdq.setRtime(new Date());
		} else {
			jdq.setFtime(new Date());
			jdq.setRtime(jdq.getFtime());
		}
		return jdqDao.updateSelectiveById(jdq) > 0;
	}

	// 删除URL
	public boolean deleteJdQueue(JDQueue jdq) {
		long id = jdq.getId();
		if (id != 0) {
			return jdqDao.deleteById(id * 1L) > 0;
		}
		return false;
	}

	// 统计用，读取各表数据量
	public int getJdQueueCount() {
		return jdqDao.selectCount();
	}

	public int getJdPdtCount() {
		return jdpDao.selectCount();
	}

	public int getJdUserCount() {
		return jduDao.selectCount();
	}

	public int getJdRevCount() {
		return jdrDao.selectCount();
	}

	public int getJdVndCount() {
		return jdvDao.selectCount();
	}
}