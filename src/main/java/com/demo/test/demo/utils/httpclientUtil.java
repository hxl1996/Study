package com.demo.test.demo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.test.demo.entity.VideoEntity;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class httpclientUtil {

    private static List<VideoEntity> list;
    private static String[] rows = {"20","50","100"};

    /**
     * 发送post请求
     * @param url  路径
     * @param encoding 编码格式
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static List<VideoEntity> send(String url, String encoding, String JSESSIONID) {
        List<VideoEntity> list = new ArrayList<>();
        try {
//        JSESSIONID=ED097224BEDF3F47D3FD75FE75FD74A4;
            BasicCookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", JSESSIONID);
            cookie.setVersion(0);
            cookie.setValue(JSESSIONID);
            cookie.setDomain("182.138.30.132");
            cookie.setPath("/cimp");
            cookieStore.addCookie(cookie);
            // 有了cookieStore
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            CloseableHttpClient client = httpClientBuilder.setDefaultCookieStore(cookieStore).build();
            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            //设置参数到请求对象中
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //----------判断是否重定向开始
    //        int code = response.getStatusLine().getStatusCode();
    //        String newuri="";
    //        if (code == 302) {
    //            Header header = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD 中的
    //            newuri = "http://182.138.30.132:8080"+header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
    //            httpPost = new HttpPost(newuri);
    //            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
    //            //s = new StringEntity(jsonObject1.toString(), "utf-8");
    //            s.setContentType("CONTENT_TYPE_TEXT_JSON");
    //            httpPost.setEntity(s);
    //
    //            response = client.execute(httpPost);
    //            code = response.getStatusLine().getStatusCode();
    //            System.out.println("login"+code);
    //        }
            //------------重定向结束
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                String body = EntityUtils.toString(entity, encoding);
                System.out.println(body);
                JSONObject jsonObj = JSONObject.parseObject(body);
                if(jsonObj!=null && !"".equals(jsonObj)){
                    String rows = jsonObj.getString("rows");
                    //转换为实体类对象
                    List<VideoEntity> videos = JSONArray.parseArray(rows,VideoEntity.class);
                    list.addAll(videos);
                }
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //解析html
    public static void resolverHtml() throws IOException {
        //        将post请求参数写到map中（map中可以存map）
        HashMap<String, Object> map = new HashMap<>();
        //map.put("program.elementType", "Series");
        map.put("page", "1");
        map.put("rows", "20");
//        将map转为json对象
        JSONObject json = new JSONObject(map);
        String  url = "http://182.138.30.132:8080/cimp/jsp/program/programraw_page?program.elementType=Series";
//        String html = send(url, "UTF-8");
        /**
         * 下面是Jsoup展现自我的平台
         */
        //6.Jsoup解析html
//        Document document = Jsoup.parse(html);
        //像js一样，通过标签获取title
//        System.out.println(document.getElementsByTag("title").first());
        //像js一样，通过id 获取文章列表元素对象
//        Element postList = document.getElementById("verifyCodeImg");
       // System.out.println(postList.text());
    }

    public static void main(String[] args) {
        //开始清空全局变量
        list = new ArrayList<>();
        Integer page = 1;
        String data = "data";
        while (data != null && !"".equals(data)){
            String url = "http://182.138.30.132:8080/cimp/jsp/program/programraw_page?program.elementType=Series&page="+page+"&rows="+rows[2];
            list = send(url, "UTF-8","21F0CC5F678F5BC5C73D5BA661666AB2");
            page ++;
        }
        list = new EntityUtil().turnInChinese(list);
        System.out.println("集合数：----------------"+list.size());
        new ExcleUtil().exportExcelPaper(list);
    }
}
