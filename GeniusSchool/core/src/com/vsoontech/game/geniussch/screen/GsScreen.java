package com.vsoontech.game.geniussch.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.vsoontech.game.geniussch.Logc;
import java.util.ArrayList;

/**
 * 抽离所有 GsScreen 共用方法
 *
 * @author Ngai
 */
abstract class GsScreen implements Screen {

    private ArrayList<Disposable> mDisposableList;

    boolean allowLog() {
        return Logc.allowPrint();
    }

    void doLog(String log) {
        Logc.d("[" + getClass().getSimpleName() + "] " + log);
    }

    @Override
    public void resize(int width, int height) {
        if (mDisposableList == null) {
            mDisposableList = new ArrayList<Disposable>();
        }
        dispose();
    }

    // 辅助 dispose ;
    void autoDisposable(Disposable dispose) {
        if (dispose != null) {
            if (mDisposableList != null
                && !mDisposableList.contains(dispose)) {
                mDisposableList.add(dispose);
            } else {
                if (allowLog()) {
                    doLog("autoDisposable need [ super.resize(int width, int height) ]、[ super.dispose() ] !");
                }
            }
        }
    }

    @Override
    public void dispose() {
        try {
            if (mDisposableList != null
                && !mDisposableList.isEmpty()) {
                for (Disposable obj : mDisposableList) {
                    obj.dispose();
                }
                mDisposableList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
