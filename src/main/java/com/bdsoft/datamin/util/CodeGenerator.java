package com.bdsoft.datamin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 自动生成dao、service辅助类
 * 
 * @author bdceo
 * 
 */
public class CodeGenerator {

	/**
	 * 工具类使用说明：
	 * 
	 * 1，生成entity，通过jpa工程生成，建立同当前工程一致的包结构，生成后，拷贝过来
	 * 
	 * ***2，将新生成的entity放到一个临时目录里，比如..\\entity\\tmp\\
	 * 
	 * 3，通过指定的临时entity目录，生成一套dao+service，一般dao可以覆盖，service不建议覆盖
	 * 
	 * 4，dao相关生成：接口+实现、daofactory、persitence.xml、application-manager.xml
	 * 
	 * 5，service相关生成：接口+实现、servicefactory
	 * 
	 */

	public static String FILE_TYPE = ".java";
	public static String BASE = "D:\\workspaceserver\\bd_data_min\\src\\";
	public static String ENTITY_PATH = BASE + "com\\bdsoft\\datamin\\entity\\";
	// dao
	public static String DAO_PATH = BASE + "com\\bdsoft\\datamin\\dao\\";
	public static String DAO_FACTORY = BASE
			+ "com\\bdsoft\\datamin\\LogicFactory.java";
	public static String DAO_TMP = BASE + "tmp\\DaoTemplate.txt";
	public static String DAO_PERSIS = BASE + "META-INF\\persistence.xml";
	public static String DAO_SPRING = BASE + "applicationContext-manager.xml";
	// service
	public static String SERVICE_PATH = BASE
			+ "com\\bdsoft\\datamin\\service\\";
	public static String SERVICE_FACTORY = BASE
			+ "com\\bdsoft\\datamin\\service\\ServiceFactory.java";
	public static String SERVICE_TMP = BASE + "tmp\\ServiceTemplate.txt";

	public static void main(String[] args) throws Exception {
		File[] fs = new File(ENTITY_PATH).listFiles();
		List<String> fns = new ArrayList<String>();
		for (File f : fs) {
			if (f.isFile()) {
				String fn = f.getName();
				fn = fn.substring(0, fn.indexOf("."));
				fns.add(fn);
			}
		}
		if (fns.size() == 0) {
			System.out.println("没有新entity需要生成");
		}

		genDao(fns, true);
		// genService(fns, false);
	}

	/**
	 * @param fns
	 * @param flag
	 *            是否覆盖
	 * @throws Exception
	 */
	public static void genDao(List<String> fns, boolean flag) throws Exception {
		String bean = ENTITY_PATH.substring(ENTITY_PATH.indexOf("com"));
		if (bean.endsWith("tmp\\")) {
			bean = bean.substring(0, bean.length() - 5);
		}
		bean = bean.replaceAll("\\\\", ".");
		String dao = bean.replaceAll("entity", "dao");

		// dao模板文件
		String tmp = BDFileUtil.readerToString(DAO_TMP, "utf-8");
		// daoFactory模板
		String facTmp = "\tpublic static #INTERFACE# get#ENTITY#Dao(){"
				+ " return (#INTERFACE#) context.getBean(\"#ENTITY#Dao\");}\n";
		StringBuffer sb = new StringBuffer();
		// persitence.xml
		Document pxml = new SAXReader().read(DAO_PERSIS);
		Element unit = pxml.getRootElement().element("persistence-unit");
		// application-manager.xml
		Document sxml = new SAXReader().read(DAO_SPRING);
		Element root = sxml.getRootElement();

		int size = fns.size();
		for (int i = 0; i < size; i++) {
			String fn = fns.get(i);
			// 接口
			String in = "I" + fn + "Dao";
			String inf = tmp.replaceAll("#ENTITY_NAME#", fn);
			inf = inf.replaceAll("#FILE_TYPE#", "interface");
			inf = inf.replaceAll("#FILE_NAME#", in);
			inf = inf.replaceAll("#EXTENDS#", "ICommonJPA");
			inf = inf.replaceAll("#IMPLEMENTS#", "");
			BDFileUtil.writeFile(DAO_PATH + in + FILE_TYPE, inf, flag);
			// 实现
			String cl = fn + "DaoImp";
			inf = tmp.replaceAll("#ENTITY_NAME#", fn);
			inf = inf.replaceAll("#FILE_TYPE#", "class");
			inf = inf.replaceAll("#FILE_NAME#", cl);
			inf = inf.replaceAll("#EXTENDS#", "CommonJPA");
			inf = inf.replaceAll("#IMPLEMENTS#", "implements " + in);
			BDFileUtil.writeFile(DAO_PATH + cl + FILE_TYPE, inf, flag);
			// factory
			inf = facTmp.replaceAll("#INTERFACE#", in);
			inf = inf.replaceAll("#ENTITY#", fn);
			sb.append(inf);
			// persistence.xml
			Element clsEle = unit.addElement("class");
			clsEle.setText(bean + fn);
			// application-manager.xml
			Element beanEle = root.addElement("bean");
			beanEle.addAttribute("id", fn + "Dao");
			beanEle.addAttribute("class", dao + cl);
			beanEle.addAttribute("scope", "singleton");
			beanEle.addAttribute("parent", "commonJPA");
			beanEle.addAttribute("lazy-init", "true");
		}
		// 修改DaoFactory
		tmp = BDFileUtil.readerToString(DAO_FACTORY, "utf-8");
		tmp = tmp.substring(0, tmp.lastIndexOf("}"));
		tmp += sb.append("\n}").toString();
		BDFileUtil.writeFile(DAO_FACTORY, tmp, true);
		// 修改persistence.xml
		XMLWriter xw = new XMLWriter(new FileOutputStream(new File(DAO_PERSIS)));
		xw.write(pxml);
		xw.flush();
		xw.close();
		// 修改application-manager.xml
		xw = new XMLWriter(new FileOutputStream(new File(DAO_SPRING)));
		xw.write(sxml);
		xw.flush();
		xw.close();
	}

	public static void genService(List<String> fns, boolean flag)
			throws Exception {
		String tmp = BDFileUtil.readerToString(SERVICE_TMP, "utf-8");
		String facTmp = "\tpublic static #INTERFACE# get#ENTITY#Service(){"
				+ " return new #IMPLEMENTS#();}\n";
		StringBuffer sb = new StringBuffer();
		int size = fns.size();
		for (int i = 0; i < size; i++) {
			String fn = fns.get(i);
			// 接口
			String in = "I" + fn + "Service";
			String inf = tmp.replaceAll("#FILE_TYPE#", "interface");
			inf = inf.replaceAll("#FILE_NAME#", in);
			inf = inf.replaceAll("#IMPLEMENTS#", "");
			BDFileUtil.writeFile(SERVICE_PATH + in + FILE_TYPE, inf, flag);
			// 实现
			String cl = fn + "ServiceImp";
			inf = tmp.replaceAll("#FILE_TYPE#", "class");
			inf = inf.replaceAll("#FILE_NAME#", cl);
			inf = inf.replaceAll("#IMPLEMENTS#", "implements " + in);
			BDFileUtil.writeFile(SERVICE_PATH + cl + FILE_TYPE, inf, flag);
			// factory
			inf = facTmp.replaceAll("#INTERFACE#", in);
			inf = inf.replaceAll("#ENTITY#", fn);
			inf = inf.replaceAll("#IMPLEMENTS#", cl);
			sb.append(inf);
		}
		// 修改ServiceFactory
		tmp = BDFileUtil.readerToString(SERVICE_FACTORY, "utf-8");
		tmp = tmp.substring(0, tmp.lastIndexOf("}"));
		tmp += sb.append("\n}").toString();
		BDFileUtil.writeFile(SERVICE_FACTORY, tmp, true);
	}

}
