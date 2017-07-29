package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Francisco Mendez
 */
public class User extends PersistentEntity implements Serializable {

    private static User dummyUser = new User(NON_PERSISTED_ID, "dummy", "Usuario de Prueba", true);

    private String username = "";
    private String name;
    private String lastName;
    private String secondLastName = "";
    private String email = "";
    private String appointment;

    private boolean documentRut = true;
    private String documentNumber;

    private String password;
    private String passwordHash;
    private String passwordSalt;

    private List<Profile> profiles = new ArrayList<>();

    private Date lastLogin;
    private Date lastPasswordChange;
    private boolean locked = true;
    private boolean valid = true;

    private int failedLoginAttempts;
    private int failedAnswerAttempts;

    private String lastPasswordHash1;
    private String lastPasswordSalt1;
    private String lastPasswordHash2;
    private String lastPasswordSalt2;
    private String lastPasswordHash3;
    private String lastPasswordSalt3;
    private String lastPasswordHash4;
    private String lastPasswordSalt4;

    private String verificationCode;

    // TODO: Francisco. Actualizar esto en el modelo de datos.
    /** BR-RefSet-004: La institución en la que trabaja el usuario */
    private List<Institution> institutions = new ArrayList<>();

    private List<Answer> answers = new ArrayList<>();

    public User() {
    }

    private User(String username, String name, boolean locked) {
        this.username = username;
        this.name = name;
        this.locked = locked;
    }

    private User(long id, String username, String name, boolean locked) {
        super(id);
        this.username = username;
        this.name = name;
        this.locked = locked;
    }

    /**
     * Constructor extendido para crear una instancia de usuario básica real.
     *
     * @param id   Identificador único del Usuario.
     * @param username Nombre de Usuario
     * @param name     Nombre del Usuario
     * @param password Contraseña
     * @param locked   Bloqueado?
     */
    public User(long id, String username, String name, String password, boolean locked) {
        this(id, username, name, locked);
        this.setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Profile> getProfiles() {
        if (profiles == null)
            profiles = new ArrayList<Profile>();
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getFullName() {
        return name +
                ((lastName != null) ? " " + lastName : "") +
                ((secondLastName != null) ? " " + secondLastName : "");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public boolean isDocumentRut() {
        return documentRut;
    }

    public void setDocumentRut(boolean rutDocument) {
        this.documentRut = rutDocument;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getDocumentNumber() {
        if(isDocumentRut()) {
            return StringUtils.formatRut(documentNumber);
        }
        else {
            return documentNumber;
        }
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public int getFailedAnswerAttempts() {
        return failedAnswerAttempts;
    }

    public void setFailedAnswerAttempts(int failedAnswerAttempts) {
        this.failedAnswerAttempts = failedAnswerAttempts;
    }

    public String getLastPasswordHash1() {
        return lastPasswordHash1;
    }

    public void setLastPasswordHash1(String lastPasswordHash1) {
        this.lastPasswordHash1 = lastPasswordHash1;
    }

    public String getLastPasswordSalt1() {
        return lastPasswordSalt1;
    }

    public void setLastPasswordSalt1(String lastPasswordSalt1) {
        this.lastPasswordSalt1 = lastPasswordSalt1;
    }

    public String getLastPasswordHash2() {
        return lastPasswordHash2;
    }

    public void setLastPasswordHash2(String lastPasswordHash2) {
        this.lastPasswordHash2 = lastPasswordHash2;
    }

    public String getLastPasswordSalt2() {
        return lastPasswordSalt2;
    }

    public void setLastPasswordSalt2(String lastPasswordSalt2) {
        this.lastPasswordSalt2 = lastPasswordSalt2;
    }

    public String getLastPasswordHash3() {
        return lastPasswordHash3;
    }

    public void setLastPasswordHash3(String lastPasswordHash3) {
        this.lastPasswordHash3 = lastPasswordHash3;
    }

    public String getLastPasswordSalt3() {
        return lastPasswordSalt3;
    }

    public void setLastPasswordSalt3(String lastPasswordSalt3) {
        this.lastPasswordSalt3 = lastPasswordSalt3;
    }

    public String getLastPasswordHash4() {
        return lastPasswordHash4;
    }

    public void setLastPasswordHash4(String lastPasswordHash4) {
        this.lastPasswordHash4 = lastPasswordHash4;
    }

    public String getLastPasswordSalt4() {
        return lastPasswordSalt4;
    }

    public void setLastPasswordSalt4(String lastPasswordSalt4) {
        this.lastPasswordSalt4 = lastPasswordSalt4;
    }

    public List<Institution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(List<Institution> institutions) {
        this.institutions = institutions;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && (String.valueOf(this.getId()) != null)
                ? String.valueOf(this.getId()).equals(String.valueOf(((User) other).getId()))
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (String.valueOf(this.getId()) != null)
                ? (this.getClass().hashCode() + String.valueOf(this.getId()).hashCode())
                : super.hashCode();
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswersByQuestion(Question question) {
        List<Answer> someAnswers = new ArrayList<>();

        for (Answer answer : getAnswers()) {
            if(answer.getQuestion().equals(question)) {
                someAnswers.add(answer);
            }
        }
        return someAnswers;
    }

    @Override
    public String toString() {
        //return String.format("ExampleEntity[%d, %s]", idDescriptionType, glosa);
        return getUsername();
    }

    /**
     * Este método es responsable de agregar un perfil al usuario. No es buena práctica devolver el objeto de la
     * estructura interna para hacerlo directamente.
     *
     * @return <code>true</code> si se agregó el perfile y <code>false</code> sino.
     */
    public boolean addProfile(Profile aProfile) {
        return this.profiles.add(aProfile);
    }

    public boolean isNullUser() {
        return false;
    }

    /**
     * Este método es responsable de retornar la instancia del usuario de pruebas.
     *
     * @return El usuario de pruebas.
     */
    public static User getDummyUser() {
        return dummyUser;
    }
}
