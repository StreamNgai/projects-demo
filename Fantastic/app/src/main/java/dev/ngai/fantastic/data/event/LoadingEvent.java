package dev.ngai.fantastic.data.event;

/**
 * Des:
 * Created by Weihl
 * 2017/8/24
 */
public class LoadingEvent {


    public boolean hasShow;
    public String title;
    public String content;

    public LoadingEvent(boolean hasShow, String title, String content) {
        this.content = content;
        this.hasShow = hasShow;
        this.title = title;
    }
}
