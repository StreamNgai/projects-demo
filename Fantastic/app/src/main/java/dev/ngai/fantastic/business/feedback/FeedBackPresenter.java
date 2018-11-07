package dev.ngai.fantastic.business.feedback;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.FeedBack;
import dev.ngai.fantastic.data.source.local.DaoHelper;

/**
 * @author Ngai
 * @since 2017/9/5
 * Des:
 */
public class FeedBackPresenter implements FeedBackContract.Presenter {

    FeedBackContract.View mView;
    final String TAG = "FeedBackPresenter";

    public FeedBackPresenter(FeedBackContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

        List<FeedBack> feedBackList = getFeedBackList();
        mView.displayFeedBacks(feedBackList);

        remoteFeedBackAns();

    }

    @Override
    public void stop() {

    }

    private List<FeedBack> getFeedBackList() {
        List<FeedBack> list = DaoHelper.getDaoSession().getFeedBackDao().loadAll();
        if (list != null) {
//            Collections.reverse(list);
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public void commitNewFeedBack(String feedBackContent) {
        if (!TextUtils.isEmpty(feedBackContent)) {
            final FeedBack feedBack = new FeedBack();
            feedBack.setLoginUnique(Session.User.getLoginUnique());
            feedBack.setQuestionContent(feedBackContent);
            feedBack.setQuestionName(Session.User.getUsername());

            feedBack.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        DaoHelper.getDaoSession().getFeedBackDao().insertOrReplace(feedBack);
                        mView.displayFeedBacks(feedBack);
                    }else {
                        mView.commitFailure();
                        Logc.d(TAG,e.getMessage());
                    }
                }
            });

        }
    }



    private void remoteFeedBackAns() {
        BmobQuery<FeedBack> query = new BmobQuery<>();
        query.addWhereEqualTo("loginUnique",Session.User.getLoginUnique());
        query.findObjects(new FindListener<FeedBack>() {
            @Override
            public void done(List<FeedBack> object, BmobException e) {
                DaoHelper.getDaoSession().getFeedBackDao().deleteAll();
                if (e == null) {
                    if(!object.isEmpty()){
                        DaoHelper.getDaoSession().getFeedBackDao().insertOrReplaceInTx(object);

                        List<FeedBack> feedBackList = getFeedBackList();
                        mView.displayFeedBacks(feedBackList);
                    }
                } else {
                    Logc.d(TAG, "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }
}
