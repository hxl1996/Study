package com.demo.test.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.test.demo.entity.DbModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TestDb {

    public static void main(String[] args) {
        try {
            testDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testDb() throws Exception {
        List<DbModel> datas = new ExcelData().redExcel("D:\\169.xlsx");
        //定义一个缓冲字符输入流
        BufferedReader in = null;
        for (DbModel d: datas
             ) {
            try {
                //访问链接
                String url = "https://www.douban.com/search?cat=1002&q="+d.getTitle();
                //存储网页内容
                String result = "";
                URL realUrl = new URL(url);
                //初始化一个链接到那个url的连接
                URLConnection connection = realUrl.openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 建立实际的连接
                connection.connect();
                //初始化BufferdReder输入流来读取内容
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //用来临时储存抓取到的数据
                String line;
                while ((line = in.readLine()) != null){
                    //遍历抓取 到的每一行并将其储存到result里面
                    result += line + "\n";
                }
                Document doc = Jsoup.parse(result);
                String rate = doc.getElementsByClass("rating_nums").get(0).text();
                System.out.println(d.getTitle() + "----" +rate);
                d.setRate(rate);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        if(datas.size()>0){
            new ExcleUtil2().exportExcelPaper(datas);
        }

        if (in!=null){
            in.close();
        }
    }

    public static void chooseVideos(){
        List<DbModel> allData = new ArrayList<DbModel>();
        //定义一个缓冲字符输入流
        BufferedReader in = null;
        try {
            //电影
            String[] tags = {"%E7%83%AD%E9%97%A8","%E6%9C%80%E6%96%B0","%E7%BB%8F%E5%85%B8","%E5%8F%AF%E6%92%AD%E6%94%BE","%E8%B1%86%E7%93%A3%E9%AB%98%E5%88%86",
                    "%E5%86%B7%E9%97%A8%E4%BD%B3%E7%89%87","%E5%8D%8E%E8%AF%AD","%E6%AC%A7%E7%BE%8E","%E9%9F%A9%E5%9B%BD","E6%97%A5%E6%9C%AC","%E5%8A%A8%E4%BD%9C",
                    "%E5%96%9C%E5%89%A7","%E7%88%B1%E6%83%85","%E7%A7%91%E5%B9%BB","%E6%82%AC%E7%96%91","%E6%81%90%E6%80%96","%E5%8A%A8%E7%94%BB"};
            for (String tag: tags
            ) {
                int page_start = 20;
                while (true){
                    //访问链接
                    String url = "https://movie.douban.com/j/search_subjects?type=movie&tag="+tag+"&sort=recommend&page_limit=20&page_start="+page_start;
                    //存储网页内容
                    String result = "";
                    URL realUrl = new URL(url);
                    //初始化一个链接到那个url的连接
                    URLConnection connection = realUrl.openConnection();
                    // 设置通用的请求属性
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    connection.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    // 建立实际的连接
                    connection.connect();
                    //初始化BufferdReder输入流来读取内容
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //用来临时储存抓取到的数据
                    String line;
                    while ((line = in.readLine()) != null){
                        //遍历抓取 到的每一行并将其储存到result里面
                        result += line + "\n";
                    }

                    JSONObject jsonx = JSON.parseObject(result);
                    String sample = jsonx.getString("subjects");
                    if("[]".equals(sample) || sample == null){
                        break;
                    }else{
                        System.out.println(sample);
                        List<DbModel> list = fromToJson(sample, new TypeToken<List<DbModel>>(){}.getType());
                        allData.addAll(list);
                        System.out.println("-------------------------"+allData.size());
                        page_start += 20;
                    }
                }
            }
            new ExcleUtil2().exportExcelPaper(allData);
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常" + e);
            e.printStackTrace();
        }finally {
            try {
                if (in != null){
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //根据泛型返回解析制定的类型
    public static  <T> T fromToJson(String json, Type listType){
        Gson gson = new Gson();
        T t = null;
        t = gson.fromJson(json,listType);
        return t;
    }

}
