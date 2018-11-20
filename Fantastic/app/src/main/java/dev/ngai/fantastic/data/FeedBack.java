package dev.ngai.fantastic.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import cn.bmob.v3.BmobObject;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Ngai
 * @since 2017/9/5
 * Des:
 */
@Entity
public class FeedBack extends BmobObject {

    @Id(autoincrement = true)
    Long id;
    String loginUnique; // email
    String questionName;
    String questionContent;
    String answerContent;

    @Generated(hash = 501311359)
    public FeedBack(Long id, String loginUnique, String questionName,
            String questionContent, String answerContent) {
        this.id = id;
        this.loginUnique = loginUnique;
        this.questionName = questionName;
        this.questionContent = questionContent;
        this.answerContent = answerContent;
    }
    @Generated(hash = 1712720019)
    public FeedBack() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLoginUnique() {
        return this.loginUnique;
    }
    public void setLoginUnique(String loginUnique) {
        this.loginUnique = loginUnique;
    }
    public String getQuestionName() {
        return this.questionName;
    }
    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }
    public String getQuestionContent() {
        return this.questionContent;
    }
    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
    public String getAnswerContent() {
        return this.answerContent;
    }
    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "answerContent='" + answerContent + '\'' +
                ", id=" + id +
                ", loginUnique='" + loginUnique + '\'' +
                ", questionName='" + questionName + '\'' +
                ", questionContent='" + questionContent + '\'' +
                '}';
    }
}
