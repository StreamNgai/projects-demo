package dev.weihl.amazing.data.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import cn.bmob.v3.BmobObject;

/**
 * @author Ngai
 * @since 2018/6/1
 * Des:
 */
@Entity
public class UserFeedback extends BmobObject {
    @Id(autoincrement = true)
    Long id;
    String userId;
    String userMail;
    String question;
    String answer;
    @Generated(hash = 523830417)
    public UserFeedback(Long id, String userId, String userMail, String question,
            String answer) {
        this.id = id;
        this.userId = userId;
        this.userMail = userMail;
        this.question = question;
        this.answer = answer;
    }
    @Generated(hash = 602428104)
    public UserFeedback() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserMail() {
        return this.userMail;
    }
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return this.answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
