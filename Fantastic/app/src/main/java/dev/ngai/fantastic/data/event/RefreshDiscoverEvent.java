package dev.ngai.fantastic.data.event;

/**
 * Des:
 * Created by Weihl
 * 2017/8/1
 */
public class RefreshDiscoverEvent {

    // -1 ,表示全部刷新
    public int ItemPosition;

    public RefreshDiscoverEvent(int itemPosition) {
        this.ItemPosition = itemPosition;
    }
}
