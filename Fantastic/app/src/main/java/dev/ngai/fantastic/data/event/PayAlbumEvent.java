package dev.ngai.fantastic.data.event;

/**
 * @author Ngai
 * @since 2017/7/31
 * Des:
 */
public class PayAlbumEvent {

    public boolean hasPaySuccess;
    public Type mType;

    public PayAlbumEvent(Type type) {
        this.hasPaySuccess = false;
        this.mType = type;
    }

    public PayAlbumEvent onSuccess() {
        hasPaySuccess = true;
        return this;
    }

    public PayAlbumEvent onFailure() {
        hasPaySuccess = false;
        return this;
    }

    public enum Type {
        Show,
        Pay
    }
}
