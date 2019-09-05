package com.whltech.netres.prettypic;

import com.whltech.netres.prettypic.PageItem.Hd;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Grab {

    public static void main(String args[]) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.mm131.net/xinggan").get();
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
                            PageItem pageItem = new PageItem();
                            pageItem.href = href;
                            pageItem.alt = alt;
                            pageItem.count = pageCount;
                            pageItem.src = itemHeadline.absUrl("src");
                            pageItem.hds = hdData;
                            log(pageItem.toString());
                        }
                    }
                    break;
                }

                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
