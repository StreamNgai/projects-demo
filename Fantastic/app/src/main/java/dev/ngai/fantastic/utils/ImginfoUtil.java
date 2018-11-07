package dev.ngai.fantastic.utils;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dev.ngai.fantastic.data.Imginfo;

/**
 * @author Ngai
 * @since 2018/1/22
 * Des:
 */
public class ImginfoUtil {

    public static GlideUrl createGlideUrl(Imginfo imginfo) {
        GlideUrl glideUrl = null;
        if (!TextUtils.isEmpty(imginfo.referer)) {
            glideUrl = new GlideUrl(imginfo.url, new LazyHeaders.Builder()
                    .addHeader("Referer", "http://www.mm131.com/xinggan/3627.html")
                    .build());
        } else {
            glideUrl = new GlideUrl(imginfo.url, new LazyHeaders.Builder()
                    .build());
        }
        return glideUrl;
    }

    public static List<Imginfo> parsingDetails(String details) {
        List<Imginfo> imgs = null;
        try {
            imgs = new Gson().fromJson(details, new com.google.common.reflect.TypeToken<ArrayList<Imginfo>>() {
            }.getType());
            return imgs;
        } catch (Exception e) {
            List<String> imgsS = new Gson().fromJson(details, new com.google.common.reflect.TypeToken<ArrayList<String>>() {
            }.getType());
            imgs = new ArrayList<>();
            for (String img : imgsS) {
                imgs.add(new Imginfo(img, ""));
            }
        }
        return imgs;
    }

    public static Imginfo parsingThumb(String thumb) {
        try{
            return new Gson().fromJson(thumb,Imginfo.class);
        }catch (Exception e){
            return new Imginfo(thumb,"");
        }
    }
}
