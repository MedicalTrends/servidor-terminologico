package cl.minsal.semantikos.model.descriptions;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.DAO;
import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías on 08-07-16.
 */
public class Description extends PersistentEntity implements AuditableEntity, Serializable {

    /** El término que representa esta descripción */
    private String term;

    /** El DESCRIPTION_ID (valor de negocio) de la descripción */
    private String descriptionID;

    private boolean isCaseSensitive;

    private boolean autogeneratedName;

    private boolean isPublished;

    /** El tipo de descriptor */
    private DescriptionType descriptionType;

    /** El estado del descriptor */
    private boolean modeled;

    /** La descripción es Vigente (válida) hasta la fecha... */
    private Timestamp validityUntil;

    /** La fecha de creación */
    private Timestamp creationDate;

    /** Usuario que creo la descripción */
    private User creatorUser;

    /** Cantidad de usos de la descripción */
    private long uses;

    /** El concepto al cuál está asociada la descripción */
    private ConceptSMTK conceptSMTK;

    /**
     * @param id              Identificador único de la base de datos.
     * @param descriptionID   El valor del DESCRIPTION_ID.
     * @param descriptionType El tipo de la descripción.
     * @param term            El término de la descripción.
     * @param isCaseSensitive Si es sensible a caracteres.
     * @param isAutoGenerated Si es auto-generado.
     * @param isPublished     Si está publicado.
     * @param validityUntil   Fecha tope de vigencia.
     */
    public Description(long id, ConceptSMTK conceptSMTK, String descriptionID, DescriptionType descriptionType, String term, long uses, boolean isCaseSensitive, boolean isAutoGenerated, boolean isPublished, Timestamp validityUntil, Timestamp creationDate, User creatorUser, boolean isModeled) {
        super(id);

        this.conceptSMTK = conceptSMTK;
        this.descriptionID = descriptionID;
        this.term = term;
        this.descriptionType = descriptionType;

        this.isCaseSensitive = isCaseSensitive;
        this.autogeneratedName = isAutoGenerated;
        this.isPublished = isPublished;
        this.validityUntil = validityUntil;
        this.creationDate = creationDate;
        this.creatorUser = creatorUser;
        this.modeled= isModeled;
        this.uses = uses;
    }

    /**
     * Este es el constructor para crear no Descripciones no persistentes.
     *
     * @param conceptSMTK     El concepto al cual está asociada la descripción.
     * @param descriptionID   El valor del DESCRIPTION_ID.
     * @param descriptionType El tipo de la descripción.
     * @param term            El término de la descripción.
     * @param isCaseSensitive Si es sensible a caracteres.
     * @param isAutoGenerated Si es auto-generado.
     * @param isPublished     Si está publicado.
     * @param validityUntil   Fecha tope de vigencia.
     */
    public Description(ConceptSMTK conceptSMTK, String descriptionID, DescriptionType descriptionType, String term, boolean isCaseSensitive, boolean isAutoGenerated, boolean isPublished, Timestamp validityUntil, Timestamp creationDate, User creatorUser, boolean isModeled) {
        this(DAO.NON_PERSISTED_ID, conceptSMTK, descriptionID, descriptionType, term, 0, isCaseSensitive, isAutoGenerated, isPublished, validityUntil, creationDate, creatorUser,isModeled);
    }

    /**
     * Un constructor minimalista para la Descripción.
     *
     * @param term            El término de la descripción.
     * @param descriptionType El tipo de la descripción.
     */
    public Description(ConceptSMTK conceptSMTK, String term, DescriptionType descriptionType) {
        this(-1, conceptSMTK, "NULL", descriptionType, term, 0,false, false, false, null, new Timestamp(currentTimeMillis()), User.getDummyUser(), false);
    }

    public String getDescriptionId() {
        return descriptionID;
    }

    public void setDescriptionId(String idDescription) {
        this.descriptionID = idDescription;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
        if (this.getDescriptionType().equals(DescriptionType.FSN)) {

            Matcher m = Pattern.compile("\\((.*?)\\)").matcher(term);

            while(m.find()) {
                if(TagSMTKFactory.getInstance().findTagSMTKByName(m.group(1)) != null) {
                    this.term = this.term.replace("("+m.group(1)+")","").trim();
                }
            }

            this.term = this.term + " (" + conceptSMTK.getTagSMTK() + ")";

        }
    }

    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.isCaseSensitive = caseSensitive;
    }

    public boolean isAutogeneratedName() {
        return autogeneratedName;
    }

    public void setAutogeneratedName(boolean autogeneratedName) {
        this.autogeneratedName = autogeneratedName;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public DescriptionType getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(DescriptionType descriptionType) {
        this.descriptionType = descriptionType;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }

    public long getUses() {
        return uses;
    }

    public void setUses(long uses) {
        this.uses = uses;
    }

    public boolean isModeled() {
        return modeled;
    }

    public void setModeled(boolean modeled) {
        this.modeled = modeled;
    }

    /**
     * Este método es responsable de determinar si esta descripción es válida
     *
     * @return <code>true</code> si es válida y <code>false</code> si no lo es.
     */
    public boolean isValid() {
        return (getValidityUntil() == null || getValidityUntil().after(new Timestamp(currentTimeMillis())));
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {
        this.conceptSMTK = conceptSMTK;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }

    /**
     * Este método es responsable de dar la representación de la descripción.
     * En particular, hay una BR que indica que para las descripciones FSN siempre se debe mostrar concatenado con el
     * Tag Semántikos.
     *
     * @return La representación literal de la descripción.
     */
    @Override
    public String toString() {

        if (this.getDescriptionType().equals(DescriptionType.FSN)) {
            String tagSMTKParenthesis = "(" + conceptSMTK.getTagSMTK().getName().toLowerCase() + ")";
            String descTerm = term.toLowerCase();
            if (!descTerm.endsWith(tagSMTKParenthesis)) {
                return this.term + (conceptSMTK == null ? "" : " (" + conceptSMTK.getTagSMTK() + ")");
            }
        }

        return this.term;
    }

    /**
     * Este método tiene como responsabilidad imprimir mayor detalle sobre la descripcion.
     * @return Un String con detalles.
     */
    public String fullToString(){
        return this.term + "[ID=" + this.getId() + ", DESCRIPTION_ID=" + this.descriptionID + " Type=" + this.descriptionType + "]";
    }
}