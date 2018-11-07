package dev.ngai.fantastic.business.feedback;

import java.util.List;

import dev.ngai.fantastic.BasePresenter;
import dev.ngai.fantastic.BaseView;
import dev.ngai.fantastic.business.login.LoginContract;
import dev.ngai.fantastic.data.FeedBack;

/**
 * @author Ngai
 * @since 2017/9/5
 * Des:
 */
public interface FeedBackContract {

    interface View extends BaseView<FeedBackContract.Presenter> {
        void displayFeedBacks(List<FeedBack> feedBackList);

        void displayFeedBacks(FeedBack feedBack);

        void commitFailure();
    }

    interface Presenter extends BasePresenter {
        void commitNewFeedBack(String feedBackContent);
    }

}
