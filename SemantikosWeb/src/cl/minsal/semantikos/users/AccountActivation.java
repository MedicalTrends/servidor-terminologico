package cl.minsal.semantikos.users;

import cl.minsal.semantikos.Constants;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.ISPFetcher;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.util.StringUtils;
import org.primefaces.context.RequestContext;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 22-03-17.
 */
@ManagedBean(name = "accountActivation")
@ViewScoped
public class AccountActivation {

    //@ManagedProperty(value="#{param.key}")
    private String key;

    private boolean valid;

    private boolean accountActive = false;

    private String password = null;

    private String newPassword1 = null;

    private String newPassword2 = null;

    private String passwordError;

    private String newPassword1Error;

    private String newPassword2Error;

    private List<Question> questionList;

    private List<Question> userQuestionList;

    private boolean passwordValid = false;

    private User user;

    //@EJB
    UserManager userManager;

    //@EJB
    AuthenticationManager authenticationManager;

    @PostConstruct
    public void init() {
        userManager = (UserManager) RemoteEJBClientFactory.getInstance().getManager(UserManager.class);
        authenticationManager = (AuthenticationManager) RemoteEJBClientFactory.getInstance().getManager(AuthenticationManager.class);

        key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
        questionList = userManager.getAllQuestions();
        valid = check(key); // And auto-login if valid?
    }

    public boolean check(String key) {
        //return userManager.activateAccount(key);
        user = userManager.getUserByVerificationCode(key);

        if(user != null) {
            return true;
        }
        else {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            try {
                context.redirect(context.getRequestContextPath() + "/" + Constants.VIEWS_FOLDER+ "/" + Constants.LOGIN_PAGE );
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
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

    public boolean isPasswordValid() {
        return passwordValid;
    }

    public void setPasswordValid(boolean passwordValid) {
        this.passwordValid = passwordValid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateAnswers(Question question) {

        if(user.getAnswersByQuestion(question).isEmpty()) {
            user.getAnswers().add(new Answer(question));
        }
        else {
            user.getAnswers().removeAll(user.getAnswersByQuestion(question));
        }
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }

    public void checkPassword() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rContext = RequestContext.getCurrentInstance();

        if(password.isEmpty()) {
            passwordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar su contraseña actual"));
        }
        else {
            passwordError = "";
        }

        if(newPassword1.isEmpty()) {
            newPassword1Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar su nueva contraseña"));
        }
        else {
            newPassword1Error = "";
        }

        if(newPassword2.isEmpty()) {
            newPassword2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar su nueva contraseña"));
        }
        else {
            newPassword2Error = "";
        }

        if(password.length() < 8) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe tener al menos 8 caracteres"));
            passwordError = "ui-state-error";
            passwordError = "ui-state-error";
        }
        else{
            passwordError = "";;
            passwordError = "";
        }

        if(newPassword1.length() < 8) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe tener al menos 8 caracteres"));
            newPassword1Error = "ui-state-error";
            newPassword2Error = "ui-state-error";
        }
        else{
            newPassword1Error = "";;
            newPassword2Error = "";
        }

        if(!StringUtils.validatePasswordFormat(newPassword1)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe contener números y letras"));
            newPassword1Error = "ui-state-error";
            newPassword2Error = "ui-state-error";
        }
        else{
            newPassword1Error = "";;
            newPassword2Error = "";
        }

        if(!passwordError.concat(newPassword1Error).concat(newPassword2Error).trim().isEmpty()) {
            return;
        }

        if(!newPassword1.equals(newPassword2)) {
            newPassword2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
            return;
        }
        else {
            newPassword2Error = "";
        }

        if(!authenticationManager.checkPassword(user, user.getEmail(), password)) {
            passwordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña proporcionada no es correcta"));
            return;
        }
        else {
            passwordError = "";
        }

        user.setPassword(newPassword1);

        setPasswordValid(true);
    }

    public void activate() {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rContext = RequestContext.getCurrentInstance();

        if(password.isEmpty()) {
            passwordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar su contraseña actual"));
        }
        else {
            passwordError = "";
        }

        if(newPassword1.isEmpty()) {
            newPassword1Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar su nueva contraseña"));
        }
        else {
            newPassword1Error = "";
        }

        if(newPassword2.isEmpty()) {
            newPassword2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar su nueva contraseña"));
        }
        else {
            newPassword2Error = "";
        }

        if(password.length() < 8) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe tener al menos 8 caracteres"));
            passwordError = "ui-state-error";
            passwordError = "ui-state-error";
        }
        else{
            passwordError = "";;
            passwordError = "";
        }

        if(newPassword1.length() < 8) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe tener al menos 8 caracteres"));
            newPassword1Error = "ui-state-error";
            newPassword2Error = "ui-state-error";
        }
        else{
            newPassword1Error = "";;
            newPassword2Error = "";
        }

        if(!StringUtils.validatePasswordFormat(newPassword1)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña debe contener números y letras"));
            newPassword1Error = "ui-state-error";
            newPassword2Error = "ui-state-error";
        }
        else{
            newPassword1Error = "";;
            newPassword2Error = "";
        }

        if(!passwordError.concat(newPassword1Error).concat(newPassword2Error).trim().isEmpty()) {
            return;
        }

        if(!newPassword1.equals(newPassword2)) {
            newPassword2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
            return;
        }
        else {
            newPassword2Error = "";
        }

        if(user.getAnswers().size() < 3) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar 3 preguntas de desbloqueo"));
            return;
        }

        for (Answer answer : user.getAnswers()) {
            if(answer.getAnswer() == null || answer.getAnswer().trim().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe responder todas las preguntas seleccionadas"));
                return;
            }
        }

        if(!authenticationManager.checkPassword(user, user.getEmail(), password)) {
            passwordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña proporcionada no es correcta"));
            return;
        }
        else {
            passwordError = "";
        }

        try {
            user.setPassword(newPassword1);
            userManager.activateAccount(user);
            accountActive = true;
        }
        catch (EJBException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }

    }

}
