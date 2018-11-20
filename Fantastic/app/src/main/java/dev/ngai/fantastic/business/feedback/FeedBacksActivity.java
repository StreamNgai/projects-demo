package dev.ngai.fantastic.business.feedback;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.data.FeedBack;
import dev.ngai.fantastic.utils.AnimUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class FeedBacksActivity extends BaseActivity implements FeedBackContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    FeedBackContract.Presenter mPresenter;
    @BindView(R.id.feedback_recode_layout)
    LinearLayout feedbackRecodeLayout;
    @BindView(R.id.contentEdit)
    EditText contentEdit;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.feedBackInputLayout)
    View feedBackInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_backs);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("意见反馈");
        toolbar.setNavigationIcon(R.drawable.ic_aleft);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBackInputLayout.setVisibility(feedBackInputLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if(feedBackInputLayout.getVisibility() == View.VISIBLE){
                    AnimUtils.shakeView(feedBackInputLayout);
                }
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                if (contentEdit.getText() != null) {
                    String content = contentEdit.getText().toString();
                    mPresenter.commitNewFeedBack(content);
                    contentEdit.setText("");
                    hideKeyboard(v);
                }
            }
        });

        new FeedBackPresenter(this).start();
    }

    private MaterialDialog mLoadingDialog;

    private void showLoadingDialog() {
        mLoadingDialog = new MaterialDialog.Builder(this)
                .title("提交")
                .content("请稍后...")
                .progress(true, 0).show();
    }

    private void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void setPresenter(FeedBackContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void displayFeedBacks(List<FeedBack> feedBackList) {
        checkNotNull(feedBackList);
        feedbackRecodeLayout.removeAllViews();
        for (FeedBack feedBack : feedBackList) {
            displayFeedBacks(feedBack);
        }
    }

    @Override
    public void displayFeedBacks(FeedBack feedBack) {
        hideLoadingDialog();
        feedBackInputLayout.setVisibility(View.GONE);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.topMargin = 10;

        feedbackRecodeLayout.addView(createFeedBackItemView(feedBack),0, llp);
    }

    @Override
    public void commitFailure() {
        hideLoadingDialog();
        new MaterialDialog.Builder(this)
                .title("提示!")
                .content("提交失败，请尝试重新提交!")
                .positiveText("了解情况")
                .show();
    }

    private View createFeedBackItemView(FeedBack feedBack) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_feedback, null);
        TextView qName = (TextView) view.findViewById(R.id.questionName);
        TextView qContent = (TextView) view.findViewById(R.id.questionContent);
        TextView aContent = (TextView) view.findViewById(R.id.answerContent);

        qName.setText(feedBack.getQuestionName() + " : ");
        qContent.setText(feedBack.getQuestionContent());
        if (!TextUtils.isEmpty(feedBack.getAnswerContent())) {
            aContent.setText(feedBack.getAnswerContent());
        }
        return view;
    }
}
