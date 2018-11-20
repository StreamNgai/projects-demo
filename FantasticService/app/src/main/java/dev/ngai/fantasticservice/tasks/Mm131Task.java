package dev.ngai.fantasticservice.tasks;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
import dev.ngai.fantasticservice.bean.Detail;
import dev.ngai.fantasticservice.bean.Discover;

/**
 * @author Ngai
 * @since 2018/1/17
 * Des:
 */
public class Mm131Task extends TaskScheduler.BaseTask {


    int page = 1;
    String URL = "http://www.mm131.com/chemo/";
    int index = 401;
//    String URL = "http://www.mm131.com/qingchun/list_1_2.html";  完成
//    String URL = "http://www.mm131.com/chemo/list_3_2.html";完成


    public Mm131Task() {
    }

    public Mm131Task(CallBack callBack) {
        super(callBack);
    }

    @Override
    void onRun() {


        while (page < 11) {

            ArrayList<Discover> pageDiscover = getPageDiscovers(page);

            ArrayList<BmobObject> discoverList = new ArrayList<>();
            for (Discover discover : pageDiscover) {
                String datas = getPageDiscoversItemPages(discover.tag);
                discover.id = index;
                discover.tag = "";
                discover.details = datas;
                discover.tab = "chemo";

                if(!TextUtils.isEmpty(datas)){
                    discoverList.add(discover);
                    ++ index;
                }
            }

            if (!discoverList.isEmpty()) {
                doBmobInsert(discoverList);
            }

            ++page;
        }

        Log.d("Mm131Task", "完成资料录入！");

    }


    private void doBmobInsert(ArrayList<BmobObject> datas) {

        new BmobBatch().insertBatch(datas).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < o.size(); i++) {
                        BatchResult result = o.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.d("Mm131Task", "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());

                        } else {
                            Log.d("Mm131Task", "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    private String getPageDiscoversItemPages(String itemPageUrl) {

        try {
            String URLL = itemPageUrl.replace(".html", "");
            Document document = Jsoup.connect(itemPageUrl).get();
            String firstImg = document.getElementsByClass("content-pic").get(0).getElementsByTag("img").get(0).attr("src");
            Elements elements = document.getElementsByClass("content-page");
            String pageNum = elements.get(0).getElementsByClass("page-ch").get(0).text().replace("共", "").replace("页", "");
            int pageNumInt = Integer.valueOf(pageNum);
            List<Detail> array = new ArrayList<>();
            array.add(new Detail(URLL,firstImg));
            for (int i = 2; i <= pageNumInt; i++) {
                String pageUrl = URLL + "_" + i + ".html";
                String imgUrl = findImgUlr(pageUrl);
                if(!TextUtils.isEmpty(imgUrl)){
                    array.add(new Detail(pageUrl,imgUrl));
                }
            }

            String result = new Gson().toJson(array);
            Log.d("Mm131Task", "Result = " + result);
            return result;
        } catch (Exception e) {
        }

        return "";
    }

    private String findImgUlr(String url) {
        try {

            Document document = Jsoup.connect(url).get();
            String firstImg = document.getElementsByClass("content-pic").get(0).getElementsByTag("img").get(0).attr("src");
            return firstImg;
        } catch (Exception e) {
        }
        return null;
    }

    private ArrayList<Discover> getPageDiscovers(int page) {
        ArrayList<Discover> tempDis = new ArrayList<>();

        try {

            String URLL = URL;
            if (page >= 2)
                URLL = URL + "list_3_" + page + ".html";

            Document document = Jsoup.connect(URLL).get();
            Elements elements = document.getElementsByClass("list-left public-box");
            Elements dd_elements = elements.get(0).getElementsByTag("dd");
            Gson gson = new Gson();
            for (Element element : dd_elements) {

                Elements aEls = element.getElementsByTag("a");
                String href = aEls.get(0).attr("href");


                Elements imgEls = element.getElementsByTag("img");
                String src = imgEls.get(0).attr("src");
                String alt = imgEls.get(0).attr("alt");
                String width = imgEls.get(0).attr("width");
                String height = imgEls.get(0).attr("height");

                Log.d("Mm131Task", "href = " + href +
                        " ; src = " + src +
                        " ; alt = " + alt +
                        " ; height = " + height +
                        " ; width = " + width
                );

                Discover discover = new Discover();
                discover.desc = alt;
                discover.title = alt.substring(0, 4);
                discover.thumb = gson.toJson(new Detail(href,src));
                discover.thumbWh = width + "_" + height;
                discover.tag = href;
                tempDis.add(discover);
            }

        } catch (Exception e) {
            Log.d("Mm131Task", "href = " + e.getMessage());
        }

        return tempDis;
    }



    @Override
   public String uniqueTag() {
        return "Mm131Task";
    }

    @Override
    void onCancel() {

    }

}
