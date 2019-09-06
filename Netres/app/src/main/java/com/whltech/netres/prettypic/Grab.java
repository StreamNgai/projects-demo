package com.whltech.netres.prettypic;

import com.google.gson.Gson;
import com.whltech.netres.prettypic.Pageres.Hd;
import com.whltech.netres.remote.BmobService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Grab {

    public static void main(String args[]) {

//        ArrayList<Pageres> tPageres = doMM131();
//        log(new Gson().toJson(tPageres));
//        downloadPageres(tPageres);
    }

    private static void downloadPageres(ArrayList<Pageres> list) {

        try {

            String webDir = "D:\\netres";
            for (Pageres page : list) {
                // 创建文件夹
                File pageDir = new File(webDir + "\\" + page.alt);
                if (!pageDir.exists()) {
                    pageDir.mkdirs();
                    log(page.alt + " <<< 正下载！");
                    File thumFile = downloadImage(page.href, "thum", page.src, pageDir.getAbsolutePath());
                    if (thumFile != null) {
                        log("ok -> " + thumFile.getAbsolutePath());
                    }

                    File hdDir = new File(pageDir.getAbsolutePath() + "\\high_definition");
                    hdDir.mkdirs();
                    int index = 0;
                    for (Pageres.Hd hd : page.hds) {
                        File tHD = downloadImage(hd.hdHref, String.valueOf(++index), hd.hdSrc, hdDir.getAbsolutePath());
                        if (tHD != null) {
                            log("ok -> " + tHD.getAbsolutePath());
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File downloadImage(String referer, String imgName, String imgUrl, String imgDir) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .url(imgUrl)
                .header("Referer", referer)
                .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody body = response.body();
            if (body != null) {
                // image/jpeg
                String sufftix = String.valueOf(body.contentType()).replaceAll("image/", "");
                byte[] picBytes = body.bytes();
                return bytesToImageFile(picBytes, imgDir + "\\" + imgName + "." + sufftix);

//                System.out.println("ContentType: " + body.contentType());
//                System.out.println("ContentLength: " + body.contentLength());
//                System.out.println("Server: " + response.header("Server"));
//                System.out.println("Server: " + response.header("Server"));
//                System.out.println("Date: " + response.header("Date"));
//                System.out.println("Vary: " + response.headers("Vary"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File bytesToImageFile(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            file.delete();
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


    private static ArrayList<Pageres> doMM131() {
        Document doc = null;
        ArrayList<Pageres> pageresList = new ArrayList<>();
        try {
            // https://www.mm131.net/xinggan/list_6_3.html
            doc = Jsoup.connect("https://www.mm131.net/xinggan/list_6_2.html").get();
            log(doc.title());
            Elements newsHeadlines = doc.select("a [href=https://www.mm131.net/xinggan],[target=_blank]");
            for (Element headline : newsHeadlines) {
                String href = headline.absUrl("href");
                Elements itemHeadLines = headline.select("img");
                for (Element itemHeadline : itemHeadLines) {
                    String alt = itemHeadline.attr("alt");
                    if (itemHeadline.select("img") != null && !isEmpty(alt)) {
                        int pageCount = getPageCount(href);
                        ArrayList<Hd> hdData = getHds(href, pageCount);
                        if (hdData != null && !hdData.isEmpty()) {
                            Pageres pageres = new Pageres();
                            pageres.href = href;
                            pageres.alt = alt;
                            pageres.count = pageCount;
                            pageres.src = itemHeadline.absUrl("src");
                            pageres.hds = hdData;
                            log(pageres.toString());
                            pageresList.add(pageres);
                        }
                    }
                    break;
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageresList;
    }

    private static int getPageCount(String href) {
        try {
            Document doc = Jsoup.connect(href).get();
            Elements newsHeadlines = doc.select("div [class^=content-page]");
            Elements elements = newsHeadlines.first().select("span");
            String count = elements.first().html().replaceAll("[^0-9]", "");
            log("count = " + count + "\n");
            return Integer.valueOf(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static ArrayList<Hd> getHds(String href, int pageCount) {
        ArrayList<Hd> hds = new ArrayList<>();
        try {
            for (int i = 1; i <= pageCount; i++) {
                String newHref = "";
                if (i > 1) {
                    newHref = href.replace(".html", "");
                    newHref = newHref + "_" + i + ".html";
                } else {
                    newHref = href;
                }
//                log(newHref);
                Document doc = Jsoup.connect(newHref).get();
                Elements newsHeadlines = doc.select("div [class^=content-pic]");
                for (Element headline : newsHeadlines) {
                    Elements itemHeadLines = headline.select("img");
                    String hdHref = headline.select("a").first().absUrl("href");
                    for (Element itemHeadline : itemHeadLines) {
                        String hdSrc = itemHeadline.attr("src");
                        if (!isEmpty(hdSrc)) {
                            Hd hd = new Hd();
                            hd.hdHref = hdHref;
                            hd.hdSrc = hdSrc;
                            hds.add(hd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hds;
    }

    private static void log(String s) {
        System.out.print("Grab >> " + s + "\n");
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
