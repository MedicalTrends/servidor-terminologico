package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.model.users.Question;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import java.util.List;

/**
 * Created by root on 22-03-17.
 */
@ManagedBean(name = "accountActivation")
@RequestScoped
public class AccountActivation {

    @ManagedProperty(value="#{param.key}")
    private String key;

    private boolean valid;

    private String password;

    private String newPassword1;

    private String newPassword2;

    private String passwordError;

    private String newPassword1Error;

    private String newPassword2Error;

    private List<Question> questionList;

    private List<Question> userQuestionList;

    @EJB
    UserManager userManager;

    @PostConstruct
    public void init() {
        questionList = userManager.getAllQuestions();
        valid = check(key); // And auto-login if valid?
    }

    public boolean check(String key) {
        //return userManager.activateAccount(key);
        return userManager.checkActivationCode(key);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public List<Question> getUserQuestionList() {
        return userQuestionList;
    }

    public void setUserQuestionList(List<Question> userQuestionList) {
        this.userQuestionList = userQuestionList;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getNewPassword1Error() {
        return newPassword1Error;
    }

    public void setNewPassword1Error(String newPassword1Error) {
        this.newPassword1Error = newPassword1Error;
    }

    public String getNewPassword2Error() {
        return newPassword2Error;
    }

    public void setNewPassword2Error(String newPassword2Error) {
        this.newPassword2Error = newPassword2Error;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public void updateQuestions(Question question) {

    }

}
