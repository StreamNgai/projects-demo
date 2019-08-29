package dev.ngai.fantasticservice.task;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Des:
 * Created by Weihl
 * 2017/9/28
 */
public class FengNiaoTask implements Runnable {


    @Override
    public void run() {
        try {

            String URLL = "http://image.fengniao.com/slide/534/5346921_1.html#p=4";
            Document document = Jsoup.connect(URLL).get();
            Elements elements = document.getElementsByTag("script");
            String jsonDataScript = (elements.get(3).data().toString().split("var"))[1];
            String jsonData = jsonDataScript.substring(jsonDataScript.indexOf("["), jsonDataScript.lastIndexOf("]") + 1);


            JSONArray arrayList = new JSONArray(jsonData);
            for (int i = 0; i < arrayList.length(); i++) {
                JSONObject object = arrayList.getJSONObject(i);
                String pic_url = object.getString("pic_url");
                String brief_json = object.getString("brief_json");
                String itemJson = pic_url + "@@@" + brief_json;
                Log.d("FengNiaoTask", "itemJson = " + itemJson);
            }
        } catch (Exception e) {
            Log.d("FengNiaoTask", "href = " + e.getMessage());
        }
    }
}
