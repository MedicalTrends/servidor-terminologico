package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import org.primefaces.context.RequestContext;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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

    private String password;

    private String newPassword1;

    private String newPassword2;

    private String passwordError;

    private String newPassword1Error;

    private String newPassword2Error;

    private List<Question> questionList;

    private List<Question> userQuestionList;

    private User user;

    @EJB
    UserManager userManager;

    @EJB
    AuthenticationManager authenticationManager;

    @PostConstruct
    public void init() {
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
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar su nueva contraseña"));
        }
        else {
            newPassword2Error = "";
        }

        if(!newPassword1.equals(newPassword2)) {
            newPassword2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
        }
        else {
            newPassword2Error = "";
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
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            user.setPassword(newPassword1);
            userManager.activateAccount(user);

            rContext.execute("PF('editDialog').hide();");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuario creado de manera exitosa!!"));
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha enviado un correo de notificación al usuario para confirmar esta cuenta. Este usuario permanecerá inactivo hasta que confirme el correo"));
        }
        catch (EJBException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }

    }

}
