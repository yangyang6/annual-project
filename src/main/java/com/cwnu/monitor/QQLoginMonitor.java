package com.cwnu.monitor;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Created by rey on 2018/4/9.
 */
public class QQLoginMonitor {


    /**
     * 这种感觉是很危险的，随便知道密码就可以登录的
     */
    public static void testLogin(){
        System.err.println("test Attention");
        try{
            //创建一个链接。
            HttpClient conn = new HttpClient();

            String response = "";
            String addUrl = "http://116.31.94.164:8090/gold-washing/login";
            PostMethod mine = new PostMethod(addUrl);
            NameValuePair[] params = {
                    new NameValuePair("username","15182971928"),
                    new NameValuePair("password","g1234567")
            };
            mine.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //mine.setRequestHeader("Cookie", cookies);
            mine.setRequestBody(params);
            conn.executeMethod(mine);
            response = mine.getResponseBodyAsString();
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }


    public static void main(String[] args) {
        testLogin();
    }

}
