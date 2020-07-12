package com.jimmy.utils;

import com.github.pagehelper.Page;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-12
 */
public class CommonUtils {
    public static <T,K extends Collection<? extends T>> List<T> sub(K a, K b) {
        List<T> result = new ArrayList<>();
        if (a != null) {
            result = new ArrayList<>(a);
            result.removeAll(b);
        }
        return result;
    }

    public static String formatDate(Date datime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(datime);
    }

    public static long dateSubToMinute(Date startDate,Date endDate){
        long total = endDate.getTime() - startDate.getTime();
        return total / (1000L * 60L);
    }



    public static boolean checkFileSize(Long len, int size, String unit) {
//        long len = file.length();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }

    public static List<Map<String,String>> formatESSearch(SearchResponse response){
//        Page<Map<String,String>> resList = new Page<>();
        ArrayList<Map<String, String>> resList = new ArrayList<>();
        System.out.println(response.getHits());
        response.getHits().forEach(
                hit->{
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    String defaultContent = (String) sourceAsMap.getOrDefault("content", "");
                    String defaultTitle = (String) sourceAsMap.getOrDefault("title", "");
                    String kind = (String) sourceAsMap.getOrDefault("kind", "1");

                    String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((long)sourceAsMap.get("createTime"));
                    String score = String.valueOf(hit.getScore());
                    String uid = hit.getId();
                    Map<String, String> resMap = new HashMap<String, String>() {
                        {
                            put("id",uid);
                            put("score",score);
                            put("title", defaultTitle);
                            put("content", defaultContent);
                            put("createTime",createTime);
                            put("type",kind);
                        }
                    };
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields.containsKey("content")){
                        String content = highlightFields.get("content").getFragments()[0].string();
                        resMap.put("content",content);
                    }

                    if(highlightFields.containsKey("title")){
                        String title = highlightFields.get("title").getFragments()[0].string();
                        resMap.put("title",title);
                    }
                    resList.add(resMap);
                }
        );
        return resList;
    }

    public static List<Map<String,String>> formatESVistorSearch(SearchResponse response){
//        Page<Map<String,String>> resList = new Page<>();
        ArrayList<Map<String, String>> resList = new ArrayList<>();
        response.getHits().forEach(
                hit->{
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    String defaultIP = (String) sourceAsMap.getOrDefault("iP", "");
                    String defaultURL = (String) sourceAsMap.getOrDefault("url", "");
                    String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((long)sourceAsMap.get("createTime"));
                    String score = String.valueOf(hit.getScore());
                    String uid = hit.getId();
                    Map<String, String> resMap = new HashMap<String, String>() {
                        {
                            put("id",uid);
                            put("score",score);
                            put("ip", defaultIP);
                            put("url", defaultURL);
                            put("createTime",createTime);
                        }
                    };
                    resList.add(resMap);
                }
        );
        return resList;
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
