package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.designer_modeler.Constants;
import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.auth.PasswordChangeException;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static cl.minsal.semantikos.kernel.auth.UserManager.MAX_FAILED_ANSWER_ATTEMPTS;
import static org.primefaces.util.Constants.EMPTY_STRING;

/**
 * Created by root on 22-03-17.
 */
@ManagedBean(name = "accountRecovery")
@ViewScoped
public class AccountRecovery {

    private boolean valid;

    private boolean accountActive = false;

    private String email = null;

    private String emailError = null;

    private User user;

    private boolean correctAnswers = false;

    private String newPassword1 = null;

    private String newPassword2 = null;

    private String newPassword1Error;

    private String newPassword2Error;

    private boolean passwordChanged = false;

    @EJB
    UserManager userManager;

    @EJB
    AuthenticationManager authenticationManager;

    @PostConstruct
    public void init() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public boolean isCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(boolean correctAnswers) {
        this.correctAnswers = correctAnswers;
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

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public void findUser() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        if(email.trim().equals("")) {
            emailError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar 'e-mail'"));
            return;
        }
        else {
            emailError = "";
        }

        if(!StringUtils.isValidEmailAddress(email)) {
            emailError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El formato del 'e-mail' no es válido"));
            return;
        }
        else {
            emailError = "";
        }

        user = userManager.getUserByEmail(email);

        if(user == null) {
            /**
             * Error no existe el usuario
             */
            emailError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No existe un usuario con este e-mail"));
            return;
        }

        if(user.getVerificationCode() != null) {
            /**
             * Redirect a activación de cuenta
             */
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            try {
                eContext.redirect(eContext.getRequestContextPath() + "/views/users/activateAccount.xhtml?key=" + user.getVerificationCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(user.isLocked()) {
            /**
             * Usuario bloqueado
             */
        }

        /**
         * Borrar las respuestas
         */
        for (Answer answer : user.getAnswers()) {
            answer.setAnswer(EMPTY_STRING);
        }
    }

    public void checkAnswers() {

        FacesContext context = FacesContext.getCurrentInstance();

        for (Answer answer : user.getAnswers()) {
            if(answer.getAnswer().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe responder las 3 preguntas de seguridad"));
                return;
            }

        }

        if(!userManager.checkAnswers(user)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Al menos una de las respuestas no es correcta"));
            return;
        }
        else {
            correctAnswers = true;
        }
    }

    public boolean maxAttemptsReached() {
        if(user == null) {
            return false;
        }
        else {
            if(user.getFailedAnswerAttempts() >= MAX_FAILED_ANSWER_ATTEMPTS) {
                return true;
            }
            return false;
        }
    }

    public void changePassword() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            if(newPassword1.trim().equals("")) {
                newPassword1Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar una nueva contraseña"));
            }
            else {
                newPassword1Error = "";
            }

            if(newPassword2.trim().equals("")) {
                newPassword2Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar la nueva contraseña"));
            }
            else {
                newPassword2Error = "";
            }

            if(!newPassword1Error.concat(newPassword2Error).trim().equals("")) {
                return;
            }

            if(!newPassword1.equals(newPassword2)) {
                newPassword2Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
            }
            else {
                newPassword2Error = "";
            }

            if(!newPassword1Error.concat(newPassword2Error).trim().equals("")) {
                return;
            }

            authenticationManager.setUserPassword(user.getEmail(),newPassword1);
            userManager.unlockUser(user.getEmail());
            passwordChanged = true;

        } catch (PasswordChangeException e) {
            newPassword1Error = "ui-state-error";
            newPassword2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            e.printStackTrace();
        }
    }


}
