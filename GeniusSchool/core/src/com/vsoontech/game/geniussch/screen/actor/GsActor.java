package com.vsoontech.game.geniussch.screen.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vsoontech.game.geniussch.Logc;

/**
 * 抽离所有 Actor 共用方法
 *
 * @author Ngai
 */
class GsActor extends Actor {

    boolean allowLog() {
        return Logc.allowPrint();
    }

    void doLog(String log) {
        Logc.d("[" + getClass().getSimpleName() + "] " + log);
    }
}
