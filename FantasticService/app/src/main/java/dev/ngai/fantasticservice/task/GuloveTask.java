package dev.ngai.fantasticservice.task;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import dev.ngai.fantasticservice.bean.Discover;

/**
 * Des:
 * Created by Weihl
 * 2017/9/29
 */
public class GuloveTask implements Runnable {

    @Override
    public void run() {

        int index = 2844;

        try {

            String URLL = "http://www.gulove.cn/index.php?m=content&c=index&a=lists&catid=7&t=18_A_GZ_YS_PC_SS_JP_5";
            Document document = Jsoup.connect(URLL).get();
            Elements elements = document.getElementsByClass("show showCase showCase-kp showCaseJs fl").get(0).getElementsByTag("li");

            ArrayList<BmobObject> discoverList = new ArrayList<>();
            for (Element element: elements) {
                String datasUrl = "http://www.gulove.cn"+element.getElementsByTag("a").attr("href");
                String title = element.getElementsByTag("h3").text();
                String thumb = element.getElementsByTag("img").get(0).attr("src");
                String thumbWh =  433 +"_"+ 291 ;
                String details = getDataImgs(datasUrl);
                String tab = "gulove";


                Discover tDis = new Discover();
                tDis.id = index;
                tDis.thumbWh = thumbWh;
                tDis.thumb = thumb;
                tDis.title = title;
                tDis.details = details;
                tDis.tab = tab;

                Log.d("GuloveTask",tDis.toString());
                discoverList.add(tDis);

                ++index;
            }

            if(!discoverList.isEmpty()){
                Log.d("GuloveTask","-----------------------------");
                Log.d("GuloveTask","discoverList.size = "+discoverList.size());
                doBmobInsert(discoverList.subList(0,20));
                doBmobInsert(discoverList.subList(20,40));
                doBmobInsert(discoverList.subList(40,60));
                doBmobInsert(discoverList.subList(60,80));
                doBmobInsert(discoverList.subList(80,100));
                doBmobInsert(discoverList.subList(100,120));
                doBmobInsert(discoverList.subList(120,140));
            }


        } catch (Exception e) {
            Log.d("GuloveTask", "error = " + e.getMessage());
        }

    }

    private void doBmobInsert(List<BmobObject> datas) {

        new BmobBatch().insertBatch(datas).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < o.size(); i++) {
                        BatchResult result = o.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.d("GuloveTask", "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());

                        } else {
                            Log.d("GuloveTask", "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }
                } else {
                    Log.i("GuloveTask", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    private String getDataImgs(String URLL) {
        try {
            Document document = Jsoup.connect(URLL).get();
            Elements elements = document.getElementsByClass("imgShow-img");

            ArrayList<String> thumbs = new ArrayList<>();
            for (Element element: elements) {
                String thumb = element.attr("src");
                thumbs.add(thumb);
            }
            return new Gson().toJson(thumbs);
        } catch (Exception e) {
            Log.d("GuloveTask", "href = " + e.getMessage());
        }
        return null;
    }
}
