package dev.weihl.amazing.data.event;

/**
 * @author Ngai
 * @since 2017/7/20
 * Des:
 */
public class TickEvent {

    public Type mType;
    public int mPayGoldCoin;

    public TickEvent(Type type) {
        this.mType = type;
        mPayGoldCoin = -1;
    }

    public TickEvent onPyaGoldCoin(int goldCoin) {
        this.mPayGoldCoin = goldCoin;
        return this;
    }

    public enum Type {
        S,
        M,
        H
    }


}
