package dev.ngai.fantasticservice.task;

import android.util.Log;

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
import dev.ngai.fantasticservice.bean.GitDir;
import dev.ngai.fantasticservice.bean.GitDirContent;
import dev.ngai.fantasticservice.utils.RequestUtil;

/**
 * @author Ngai
 * @since 2017/12/14
 * Des:
 */
public class GitManualTask implements Runnable {

    final String TAG = "GitManualTask";
    final String GIT_DOMAIN = "https://git-scm.com";
    final String GIT_DIR_URL = GIT_DOMAIN + "/book/zh/v2";

    @Override
    public void run() {
        ArrayList<GitDir> gitDirs = gitDirectory();
        Log.d(TAG, gitDirs.toString());

        //  目录数据
        ArrayList<BmobObject> gitDirList = new ArrayList<>();
        int dirIndex = 0;
        for (GitDir dir : gitDirs) {
            dir.index = dirIndex++;
            gitDirList.add(dir);
            Log.d("AABCC",dir.toString());
            if (dir.chapter != null && !dir.chapter.isEmpty()) {
                for (GitDir dirC : dir.chapter) {
                    dirC.index = dirIndex++;
                    gitDirList.add(dirC);
                    Log.d("AABCC",dirC.toString());
                }
            }
        }
        doBmobInsertGitDir(gitDirList);

//        // 目录内容数据
//        ArrayList<GitDirContent> gitContents = new ArrayList<>();
//        int dirContentIndex = 0;
//        for (GitDir gitDir : gitDirs) {
//            String html = getChapterContent(gitDir);
//            GitDirContent tContent = new GitDirContent();
//            tContent.dirName = gitDir.name;
//            tContent.content = html;
//            tContent.index = dirContentIndex ++ ;
//            gitContents.add(tContent);
//            if (gitDir.chapter != null && !gitDir.chapter.isEmpty()) {
//                for (GitDir gitDirC : gitDir.chapter) {
//                    String htmlC = getChapterContent(gitDirC);
//                    GitDirContent tContentC = new GitDirContent();
//                    tContentC.dirName = gitDirC.name;
//                    tContentC.content = htmlC;
//                    tContentC.index = dirContentIndex ++ ;
//                    gitContents.add(tContentC);
//                }
//            }
//        }
////        Log.d(TAG, gitContents.toString());
//
//        ArrayList<BmobObject> gitDirContentList = new ArrayList<>();
//        gitDirContentList.addAll(gitContents);
//        doBmobInsertGitDir(gitDirContentList);
    }

    private void doBmobInsertGitDir(List<BmobObject> datas) {

        int count = datas.size() / 20;
        if (datas.size() % 20 > 0) {
            count++;
        }

        for (int i = 0; i < count; i++) {
            int start = i * 20;
            int end = start + 20;
            if (end > datas.size()) {
                end = datas.size();
            }

            new BmobBatch().insertBatch(datas.subList(start, end)).doBatch(new QueryListListener<BatchResult>() {

                @Override
                public void done(List<BatchResult> o, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < o.size(); i++) {
                            BatchResult result = o.get(i);
                            BmobException ex = result.getError();
                            if (ex == null) {
                                Log.d(TAG, "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());

                            } else {
                                Log.d(TAG, "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                            }
                        }
                    } else {
                        Log.i(TAG, "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }

    private String getChapterContent(GitDir gitDir) {
        String html = "";
        try {
            html = RequestUtil.requestGet(gitDir.href, null);
            Document document = Jsoup.parse(html);
            Elements imgEles = document.getElementsByClass("book edition2").get(0).getElementsByTag("img");
            for (Element imgE : imgEles) {
                imgE.attr("src", GIT_DOMAIN + imgE.attr("src"));
            }
            html = document.getElementsByClass("book edition2").html();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    private ArrayList<GitDir> gitDirectory() {
        ArrayList<GitDir> gitDirs = new ArrayList<>();

        try {
            Document document = Jsoup.connect(GIT_DIR_URL).get();
            Elements cElements = document.getElementsByClass("chapter");
            for (Element chapterEl : cElements) {
                Elements liElements = chapterEl.getElementsByTag("li");
                GitDir chapterFirst = new GitDir();
                chapterFirst.name = liElements.get(0).getElementsByTag("h2").text();
                chapterFirst.href = GIT_DOMAIN + liElements.get(0).getElementsByTag("h2").get(0).getElementsByTag("a").attr("href");
//                String html = getChapterContent(chapterFirst);
//                Log.d(TAG,"content : "+ html);
                chapterFirst.chapter = new ArrayList<>();
                for (int i = 1; i < liElements.size(); i++) {
                    GitDir tChapter = new GitDir();
                    tChapter.name = liElements.get(i).text();
                    tChapter.href = GIT_DOMAIN + liElements.get(i).getElementsByTag("a").attr("href");
                    chapterFirst.chapter.add(tChapter);
                }
                gitDirs.add(chapterFirst);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gitDirs;
    }
}
