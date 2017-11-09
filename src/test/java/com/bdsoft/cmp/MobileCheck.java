package com.bdsoft.cmp;

import com.alibaba.fastjson.JSONObject;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;

import java.util.Arrays;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/9 16:28
 */
public class MobileCheck {

    public static void main(String[] args) throws Exception {
        BDLogUtil.init();
        // 数据源
        String csv = "d:/download/mobile.csv";

        // 提取手机号
        String content = BDFileUtil.readerToString(csv, "utf-8");
        String[] mobiles = content.split("\r\n");
        sop("待检测手机号：", mobiles.length, "个");

        // 调用生产api检测手机号是否重复
        int regs = 0;
        String api = "http://api.shejijia.com/cas-proxy/guest_user_account/api/v1/users/validate?execute=user.account.validate.has";
        for (String mobile : mobiles) {
            BDHttpParam param = BDHttpParam.init().setContentType(BDHttpParam.HEADER_CONTENT_TYPE_JSON).addCommon("userName", mobile);
            String res = BDHttpUtil.sendPost(api, param);
            // sop(res);
            boolean reg = isReg(res);
            sop("手机号：", mobile, (reg ? " 已注册" : " 未注册"));
            if (reg) {
                regs++;
            }
        }

        sop("共检测", mobiles.length, "个手机号，", regs, "个已注册，", (mobiles.length - regs), "个未注册");
    }

    public static boolean isReg(String json) {
        JSONObject obj = JSONObject.parseObject(json);
        return obj.containsKey("error_code") && obj.getString("error_code").equalsIgnoreCase("100110203");
    }

    public static void sop(Object... args) {
        StringBuilder str = new StringBuilder();
        Arrays.stream(args).forEach(s -> {
            str.append(s);
        });
        System.out.println(str);
    }
}
