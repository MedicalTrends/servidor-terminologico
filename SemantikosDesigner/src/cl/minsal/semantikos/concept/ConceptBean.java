package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.MainMenuBean;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.description.AutogenerateBeans;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.messages.MessageBean;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetRecord;
import cl.minsal.semantikos.relationship.RelationshipBeans;
import cl.minsal.semantikos.session.ProfilePermissionsBeans;
import cl.minsal.semantikos.snomed.SCTTypeBean;
import cl.minsal.semantikos.snomed.SnomedBeans;
import cl.minsal.semantikos.CompoundSpecialty;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.browser.PendingBrowserBean;
import cl.minsal.semantikos.designer.*;

import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.kernel.businessrules.ConceptDefinitionalGradeBR;
import cl.minsal.semantikos.kernel.businessrules.RelationshipBindingBR;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.descriptions.*;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.DescriptionWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipWeb;
import cl.minsal.semantikos.modelweb.Pair;
import org.primefaces.event.ReorderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static org.primefaces.util.Constants.EMPTY_STRING;

/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "conceptBean",eager = true)
@ViewScoped
public class ConceptBean implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ConceptBean.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    //@EJB
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);;

    //@EJB
    CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    HelperTablesManager helperTablesManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    //@EJB
    TagSMTKManager tagSMTKManager = (TagSMTKManager) ServiceLocator.getInstance().getService(TagSMTKManager.class);

    //@EJB
    AuditManager auditManager = (AuditManager) ServiceLocator.getInstance().getService(AuditManager.class);

    //@EJB
    ViewAugmenter viewAugmenter = (ViewAugmenter) ServiceLocator.getInstance().getService(ViewAugmenter.class);

    //@EJB
    RelationshipBindingBR relationshipBindingBR = (RelationshipBindingBR) ServiceLocator.getInstance().getService(RelationshipBindingBR.class);

    //@EJB
    ConceptDefinitionalGradeBR conceptDefinitionalGradeBR = (ConceptDefinitionalGradeBR) ServiceLocator.getInstance().getService(ConceptDefinitionalGradeBR.class);

    @ManagedProperty(value = "#{smtkBean}")
    private SMTKTypeBean smtkTypeBean;

    @ManagedProperty(value = "#{compositeAditionalBean}")
    private CompositeAditional compositeAditionalBean;

    @ManagedProperty(value = "#{conceptExport}")
    private ConceptExportMBean conceptBeanExport;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{changeMarketedBean}")
    private ChangeMarketedBean changeMarketedBean;

    @ManagedProperty(value = "#{crossmapBean}")
    private CrossmapBean crossmapBean;

    @ManagedProperty(value = "#{messageBean}")
    MessageBean messageBean;

    @ManagedProperty( value = "#{autogenerateBeans}")
    private AutogenerateBeans autogenerateBeans;

    @ManagedProperty( value = "#{snomedBean}")
    private SnomedBeans snomedBeans;

    @ManagedProperty( value = "#{sctBean}")
    private SCTTypeBean sctTypeBean;

    @ManagedProperty( value = "#{sensibilityBean}")
    private SensibilityDescriptionDefaultBean sensibilityDescriptionDefaultBean;

    @ManagedProperty( value = "#{pendingBrowserBean}")
    private PendingBrowserBean pendingBrowserBean;

    @ManagedProperty( value="#{mainMenuBean}")
    MainMenuBean mainMenuBean;

    public void setPendingBrowserBean(PendingBrowserBean pendingBrowserBean) {
        this.pendingBrowserBean = pendingBrowserBean;
    }

    public void setSensibilityDescriptionDefaultBean(SensibilityDescriptionDefaultBean sensibilityDescriptionDefaultBean) {
        this.sensibilityDescriptionDefaultBean = sensibilityDescriptionDefaultBean;
    }

    public void setSctTypeBean(SCTTypeBean sctTypeBean) {
        this.sctTypeBean = sctTypeBean;
    }

    public void setSnomedBeans(SnomedBeans snomedBeans) {
        this.snomedBeans = snomedBeans;
    }

    public void setAutogenerateBeans(AutogenerateBeans autogenerateBeans) {
        this.autogenerateBeans = autogenerateBeans;
    }

    private List<Category> categoryList;

    private List<DescriptionWeb> descriptionsToTraslate = new ArrayList<>();

    private List<DescriptionType> descriptionTypes = new ArrayList<>();

    private List<DescriptionType> descriptionTypesEdit = new ArrayList<DescriptionType>();

    private List<Description> selectedDescriptions = new ArrayList<Description>();

    private List<TagSMTK> tagSMTKs = new ArrayList<TagSMTK>();

    private List<RelationshipDefinitionWeb> orderedRelationshipDefinitionsList = new ArrayList<>();

    private List<ConceptAuditAction> auditAction;

    private List<String> autoGenerateList = new ArrayList<>();

    private List<NoValidDescription> noValidDescriptions = new ArrayList<>();

    private List<ConceptSMTK> conceptSuggestedList;

    private List<ObservationNoValid> observationNoValids;

    public List<ObservationNoValid> getObservationNoValids() {
        return observationNoValids;
    }

    private Category categorySelected;

    DescriptionTypeFactory descriptionTypeFactory = DescriptionTypeFactory.getInstance();

    public User user;

    private boolean editable;

    private int idCategory;

    /** id del concepto sobre la cual se esta editando. Usado como enlace entre la petición desde el ConceptBrowser y el
     * concepto */
    private int idConcept;

    private ConceptSMTKWeb concept;

    /** El concepto original */
    private ConceptSMTKWeb _concept;

    /** La categoría asociada a la vista, de la cual se crea un nuevo concepto */
    private Category category;

    // Placeholder para las descripciones
    private String otherTermino;

    private boolean otherSensibilidad;

    private DescriptionType otherDescriptionType;

    private ConceptSMTK conceptSMTKTranslateDes;

    private Description descriptionToTranslate;

    // Placeholders para los target de las relaciones
    private BasicTypeValue basicTypeValue = new BasicTypeValue(null);

    private HelperTableRow selectedHelperTableRecord = null;

    private ConceptSMTK conceptSMTK;

    private ConceptSMTK conceptSMTKAttributeSelected;

    private ConceptSMTK conceptSelected;

    private ConceptSCT conceptSCTSelected;

    private CrossmapSetRecord crossmapSetMemberSelected;

    private Map<Long, ConceptSMTK> targetSelected;

    // Placeholders para los atributos de relacion

    private Map<Long, Relationship> relationshipPlaceholders = new HashMap<Long, Relationship>();

    private Map<RelationshipDefinition, List<RelationshipAttribute>> relationshipAttributesPlaceholder = new HashMap<RelationshipDefinition, List<RelationshipAttribute>>();

    //Parametros del formulario

    private String FSN;

    private boolean fullyDefined;

    private String favoriteDescription;

    private int categorySelect;

    //para tipo helpertable
    private int helperTableValuePlaceholder;

    private long idTermPending;

    private Description descriptionPending;

    private boolean refsetEditConcept;

    public Description getDescriptionPending() {
        return descriptionPending;
    }

    public void setDescriptionPending(Description descriptionPending) {
        this.descriptionPending = descriptionPending;
    }

    private AutogenerateMCCE autogenerateMCCE = new AutogenerateMCCE();

    private AutogenerateMC autogenerateMC;

    private AutogeneratePCCE autogeneratePCCE;

    /**
     * Un map para almacenar localmente las relaciones aumentadas
     */
    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public void setConceptBeanExport(ConceptExportMBean conceptBean) {
        this.conceptBeanExport = conceptBean;
    }

    private ObservationNoValid observationNoValid;

    private ConceptSMTK conceptSMTKNotValid;

    private ConceptSMTK conceptSuggested;

    public void setObservationNoValids(List<ObservationNoValid> observationNoValids) {
        this.observationNoValids = observationNoValids;
    }

    public DescriptionManager getDescriptionManager() {
        return descriptionManager;
    }

    public List<NoValidDescription> getNoValidDescriptions() {
        return noValidDescriptions;
    }

    public List<DescriptionWeb> getDescriptionsToTraslate() {
        return descriptionsToTraslate;
    }
    public ObservationNoValid getObservationNoValid() {
        return observationNoValid;
    }

    public void setObservationNoValid(ObservationNoValid observationNoValid) {
        this.observationNoValid = observationNoValid;
    }

    public void setCrossmapBean(CrossmapBean crossmapBean) {
        this.crossmapBean = crossmapBean;
    }

    public Map<RelationshipDefinition, List<RelationshipAttribute>> getRelationshipAttributesPlaceholder() {
        return relationshipAttributesPlaceholder;
    }

    public void setRelationshipAttributesPlaceholder(Map<RelationshipDefinition, List<RelationshipAttribute>> relationshipAttributesPlaceholder) {
        this.relationshipAttributesPlaceholder = relationshipAttributesPlaceholder;
    }

    public boolean pendingTerms;

    public boolean isPendingTerms() {
        return pendingTerms;
    }

    //Inicializacion del Bean
    @PostConstruct
    public void initialize() {
        user = authenticationBean.getLoggedUser();
        autogenerateMCCE = new AutogenerateMCCE();
        autogenerateMC = new AutogenerateMC();
        autogeneratePCCE = new AutogeneratePCCE();
        noValidDescriptions = new ArrayList<>();
        observationNoValids = descriptionManager.getObservationsNoValid();
        categoryList = categoryManager.getCategories();
        descriptionTypes = descriptionTypeFactory.getDescriptionTypesButFSNandFavorite();
        descriptionTypesEdit = descriptionTypeFactory.getDescriptionTypesButFSN();
        tagSMTKs = tagSMTKManager.getAllTagSMTKs();
        orderedRelationshipDefinitionsList = new ArrayList<>();
        descriptionsToTraslate = new ArrayList<>();
        conceptSMTKNotValid = conceptManager.getNoValidConcept();
        conceptSuggestedList = new ArrayList<>();
    }

    public void addSuggest() {
        conceptSuggestedList.add(conceptSuggested);
        conceptSuggested = null;
    }

    public void removeConceptSuggest(ConceptSMTK conceptSMTKSuggestSel) {
        conceptSuggestedList.remove(conceptSMTKSuggestSel);
    }

    public MainMenuBean getMainMenuBean() {
        return mainMenuBean;
    }

    public void setMainMenuBean(MainMenuBean mainMenuBean) {
        this.mainMenuBean = mainMenuBean;
    }

    /**
     * Este método se encarga de inicializar un concepto si es que se va a crear un concepto nuevo. Y si se va a editar
     * invoca al getConceptByID.
     */
    public void createConcept() {

        if (idConcept == 0) {

            setCategory(categoryManager.getCategoryById(idCategory));

            if (category.getId() == 34) {
                changeMultiplicityToRequiredRelationshipDefinitionMC();
            }

            ConceptSMTK aConcept = categoryManager.categoryContains(category, favoriteDescription);

            /* Se valida que el término propuesto no exista previamente */
            if (aConcept != null) {
                messageBean.messageError("La descripción " + favoriteDescription + " ya existe dentro de la categoría " + category.getName() +". Descripción perteneciente a concepto: "+aConcept);
                return;
            } else {
                newConcept(category, favoriteDescription);
            }
        } else {
            getConceptById(idConcept);
            if (category.getId() == 34) {
                changeMCSpecial();
            }
        }

        // Una vez que se ha inicializado el concepto, inicializar los placeholders para las relaciones
        mainMenuBean.augmentRelationshipPlaceholders(category, concept, relationshipPlaceholders);

        changeMCSpecial();

    }

    //Este método es responsable de a partir de un concepto SMTK y un término, devolver un concepto WEB con su FSN y su Preferida
    public ConceptSMTKWeb initConcept(ConceptSMTK concept, String term) {
        ConceptSMTKWeb conceptWeb = new ConceptSMTKWeb(concept, term);

        fullyDefined = concept.isFullyDefined();

        return viewAugmenter.augmentConcept(category, conceptWeb);
    }

    //Este método es responsable de pasarle a la vista un concepto plantilla
    //(llamado desde la vista cuando se desea crear un nuevo concepto)
    public void newConcept(Category category, String term) {
        /* Valores iniciales para el concepto */
        String observation = "";

        TagSMTK tagSMTK = new TagSMTK(category.getTagSemantikos().getId(), category.getTagSemantikos().getName());
        ConceptSMTK conceptSMTK = new ConceptSMTK("", category, false, false, false, false, false, false, observation, tagSMTK);

        // Se crea el concepto WEB a partir del concepto SMTK
        concept = initConcept(conceptSMTK, term);
        concept.setEditable(editable);

        concept.getDescriptionFSN().setCaseSensitive(sensibilityDescriptionDefaultBean.sensibility(category.getId()));
        concept.getDescriptionFavorite().setCaseSensitive(sensibilityDescriptionDefaultBean.sensibility(category.getId()));
        // Se crea una copia con la imagen original del concepto
        _concept = initConcept(conceptSMTK, term);

        conceptBeanExport.setConceptSMTK(concept);
        conceptBeanExport.loadConcept();
    }

    /**
     * Este método se encarga de setear el idCategory. En ejecución este metodo es invocado al realizar el request
     * desde el conceptBrowser cuando se desea crear un nuevo concepto dentro de una categoria
     */
    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    /**
     * Este método se encarga de setear el idConcept. En ejecución este metodo es invocado al realizar el request
     * desde el conceptBrowser cuando se desea ver o editar un concepto existente
     */
    public void setIdConcept(int idConcept) {
        this.idConcept = idConcept;
        if (idConcept != 0 && concept == null) {
            createConcept();
        }
    }

    public int getIdConcept() {
        return idConcept;
    }

    //Este método es responsable de pasarle a la vista un concepto, dado el id del concepto
    //(llamado desde la vista cuando se desea editar un concepto)
    public void getConceptById(long conceptId) {
        ConceptSMTK conceptSMTK = conceptManager.getConceptByID(conceptId);
        try {
            conceptSMTK.setRelationships(conceptManager.loadRelationships(conceptSMTK));
        } catch (Exception e) {
            e.printStackTrace();
        }
        crossmapBean.refreshCrossmapIndirect(conceptSMTK);
        this.conceptSMTK = conceptSMTK;
        // Se crea el concepto WEB a partir del concepto SMTK
        concept = new ConceptSMTKWeb(conceptSMTK);
        // Se crea una copia con la imagen original del concepto
        _concept = new ConceptSMTKWeb(conceptSMTK);

        fullyDefined=concept.isFullyDefined();
        concept.setEditable(editable);
        auditAction = auditManager.getConceptAuditActions(concept, true);
        category = concept.getCategory();
        conceptBeanExport.setConceptSMTK(conceptSMTK);
        conceptBeanExport.loadConcept();
    }


    public ConceptSMTK getTargetForRD(RelationshipDefinition relationshipDefinition, ConceptSMTK conceptSel) {
        if (targetSelected == null) {
            targetSelected = new HashMap<Long, ConceptSMTK>();
        }
        if (!targetSelected.containsKey(relationshipDefinition.getId())) {
            targetSelected.put(relationshipDefinition.getId(), conceptSel);
        }
        return targetSelected.get(relationshipDefinition.getId());
    }

    /**
     * Este método es el encargado de agregar relaciones al concepto recibiendo como parámetro un Relationship
     * Definition. Este método es utilizado por el componente BasicType, el cual agrega relaciones con target sin valor
     */
    public void addRelationshipWithAttributes(RelationshipDefinition relationshipDefinition) {
        if (snomedBeans.existRelationshipISAMapping(concept,relationshipDefinition) ) {
            messageBean.messageError("Cuando existe una relación 'Es un mapeo de', no se pueden agregar más relaciones.");
            return;
        }

        Relationship relationship = relationshipPlaceholders.get(relationshipDefinition.getId());

        try {
            if(concept.getRelationships().contains(relationship)){
                messageBean.messageError("No se puede agregar dos veces el mismo registro");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            relationshipBindingBR.brSCT001(concept, relationship);
        }catch (EJBException e) {
            messageBean.messageError(e.getMessage());
            resetPlaceHolders();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(CompoundSpecialty.existCompounSpeciality(concept.getRelationships(),relationship)){
                messageBean.messageError("Solo puede existir una especialidad compuesta con este nombre");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(relationshipDefinition.isSNOMEDCT()) {
            BasicTypeValue<Integer> targetGroup = new BasicTypeValue<Integer>(sctTypeBean.getRelationshipGroup());
            relationship.getRelationshipAttributes().add(new RelationshipAttribute( relationshipDefinition.getGroupAttributeDefinition(),relationship,targetGroup));
            //Collections.reverse(relationship.getRelationshipAttributes());
        }

        // Validar placeholders de targets de relacion
        if (relationship.getTarget() == null) {
            messageBean.messageError("Debe seleccionar un valor para el atributo " + relationshipDefinition.getName());
            mainMenuBean.augmentRelationshipPlaceholders(category, concept, relationshipPlaceholders);
            resetPlaceHolders();
            return;
        }
        if (snomedBeans.existRelationshipToSCT(concept)) crossmapBean.refreshCrossmapIndirect(concept);
        if (snomedBeans.isMapping(relationship)) {
            ConceptSCT conceptSCT = (ConceptSCT) relationship.getTarget();
            fullyDefined = (conceptSCT.isCompletelyDefined()) ? true : false;
            concept.setFullyDefined(fullyDefined);
            concept.setInherited(true);
        } else {
            concept.setInherited(false);
        }
        if (relationshipDefinition.getOrderAttributeDefinition() != null) {
            RelationshipAttribute attribute = new RelationshipAttribute(relationshipDefinition.getOrderAttributeDefinition(), relationship, new BasicTypeValue(concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).size() + 1));
            relationship.getRelationshipAttributes().add(attribute);
        }
        for (RelationshipAttributeDefinition attributeDefinition : relationshipDefinition.getRelationshipAttributeDefinitions()) {
            if ((!attributeDefinition.isOrderAttribute() && !relationship.isMultiplicitySatisfied(attributeDefinition)) || changeIndirectMultiplicity(relationship, relationshipDefinition, attributeDefinition)) {
                messageBean.messageError("Información incompleta para agregar " + relationshipDefinition.getName());
                mainMenuBean.augmentRelationshipPlaceholders(category, concept, relationshipPlaceholders);
                resetPlaceHolders();
                return;
            }
        }

        if(!isMCSpecialThisConcept() && concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0 && !relationshipDefinition.isSNOMEDCT())autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

        // Se setea la definición pasada por parámetro, para rescatar posibles modificaciones a nivel de vista
        relationship.setRelationshipDefinition(relationshipDefinition);

       // Se utiliza el constructor mínimo (sin id)
        this.concept.addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
        if(!isMCSpecialThisConcept() && !relationshipDefinition.isSNOMEDCT()) {
            autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
        }

        // Resetear placeholder relacion
        mainMenuBean.augmentRelationshipPlaceholders(category, concept, relationshipPlaceholders);
        // Resetear placeholder targets
        resetPlaceHolders();
    }

    public void resetPlaceHolders() {
        basicTypeValue = new BasicTypeValue(null);
        selectedHelperTableRecord = null;
        conceptSelected = null;
        conceptSCTSelected = null;
        crossmapSetMemberSelected = null;
        conceptSMTKAttributeSelected=null;
    }

    /**
     * Este método es el encargado de agregar una nuva relacion con los parémetros que se indican.
     */
    public void addRelationship(RelationshipDefinition relationshipDefinition, Target target) {

        Relationship relationship = new Relationship(this.concept, target, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);
        try {
            if(concept.getRelationships().contains(relationship)) {
                messageBean.messageError("No se puede agregar dos veces el mismo registro");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            relationshipBindingBR.verifyPreConditions(concept, relationship, user);
        } catch (EJBException e) {
            messageBean.messageError(e.getMessage());
            resetPlaceHolders();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Se utiliza el constructor mínimo (sin id)
        this.concept.addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
        // Resetear placeholder targets
        resetPlaceHolders();
    }

    /**
     * Este método se encarga de agregar o cambiar la relación para el caso de multiplicidad 1.
     */
    public void addOrChangeRelationship(RelationshipDefinition relationshipDefinition, Target target) {
        Relationship relationship = null;
        boolean isRelationshipFound = false;

        if(target == null || target.getRepresentation().equals("null")) {
            return;
        }

        if (relationshipDefinition.getTargetDefinition().isSMTKType() && target.getId() == concept.getId()) {
            messageBean.messageError("No puede seleccionar el mismo concepto que está editando");
            conceptSelected = null;
            return;
        }

        // Se busca la relación
        for (Relationship relationshipWeb : concept.getRelationshipsWeb()) {
            if (relationshipWeb.getRelationshipDefinition().equals(relationshipDefinition)) {
                relationshipWeb.setTarget(target);

                isRelationshipFound = true;

                try {
                    relationshipBindingBR.verifyPreConditions(concept, relationshipWeb, user);
                } catch (EJBException e) {
                    messageBean.messageError(e.getMessage());
                    resetPlaceHolders();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0)
                    autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

                if(relationshipDefinition.isComercializado())
                    changeMarketedBean.changeMarketedEvent(concept, relationshipDefinition, target);

                autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

                if (relationshipDefinition.getId() == 74 && ((BasicTypeValue<Boolean>) target).getValue())
                    changeMultiplicityNotRequiredRelationshipDefinitionMC();
                if (relationshipDefinition.getId() == 74 && !((BasicTypeValue<Boolean>) target).getValue())
                    changeMultiplicityToRequiredRelationshipDefinitionMC();
                break;
            }
        }
        // Si no se encuentra la relación, se crea una nueva
        if (!isRelationshipFound) {
            relationship = new Relationship(this.concept, target, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

            try {
                relationshipBindingBR.verifyPreConditions(concept, relationship, user);
            } catch (EJBException e) {
                messageBean.messageError(e.getMessage());
                resetPlaceHolders();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.concept.addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
            if (relationshipDefinition.getId() == 74 && ((BasicTypeValue<Boolean>) target).getValue())
                changeMultiplicityNotRequiredRelationshipDefinitionMC();
            if (relationshipDefinition.getId() == 74 && !((BasicTypeValue<Boolean>) target).getValue())
                changeMultiplicityToRequiredRelationshipDefinitionMC();
        }
        //Autogenerado
        if(concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0) {
            autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
        }

        autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
        // Se resetean los placeholder para los target de las relaciones
        resetPlaceHolders();
    }

    /**
     * Este método se encarga de agregar o cambiar el atributo para el caso de multiplicidad 1.
     */
    public void addOrChangeRelationshipAttribute(RelationshipDefinition relationshipDefinition, RelationshipAttributeDefinition relationshipAttributeDefinition, Target target) {

        if(target == null || target.getRepresentation().equals("null")) {
            return;
        }

        boolean isRelationshipFound = false;
        boolean isAttributeFound = false;

        // Se busca la relación y el atributo
        for (Relationship relationship : concept.getRelationshipsWeb()) {
            if (relationship.getRelationshipDefinition().equals(relationshipDefinition)) {
                isRelationshipFound = true;
                for (RelationshipAttribute attribute : relationship.getRelationshipAttributes()) {
                    if (attribute.getRelationAttributeDefinition().equals(relationshipAttributeDefinition)) {
                        attribute.setTarget(target);
                        isAttributeFound = true;
                        if(concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0)autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

                        autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
                        break;
                    }
                }
                // Si se encuentra la relación, pero no el atributo, se crea un nuevo atributo
                if (!isAttributeFound) {
                    RelationshipAttribute attribute = new RelationshipAttribute(relationshipAttributeDefinition, relationship, target);
                    relationship.getRelationshipAttributes().add(attribute);
                    if(concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0)autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

                    autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
                }
            }
        }

        // Si no se encuentra la relación, se crea una nueva relación con el atributo y target vacio
        if (!isRelationshipFound) {
            messageBean.messageError("No existe una relación a la cual añadir este atributo. Primero agregue la relación");
        }

        // Se resetean los placeholder para los target de las relaciones
        resetPlaceHolders();
    }

    public void setTarget(RelationshipDefinition relationshipDefinition, Target target) {
        relationshipPlaceholders.get(relationshipDefinition.getId()).setTarget(target);
    }

    /**
     * Este método se encarga de agregar o cambiar el atributo para el caso de multiplicidad 1.
     */
    public void setTargetAttribute(RelationshipDefinition relationshipDefinition, RelationshipAttributeDefinition relationshipAttributeDefinition, Target target) {
        //relationshipWeb.getRelationshipAttributes().add()
        Relationship relationship = relationshipPlaceholders.get(relationshipDefinition.getId());
        boolean isAttributeFound = false;

        // Se busca el atributo
        for (RelationshipAttribute attribute : relationship.getRelationshipAttributes()) {
            if (attribute.getRelationAttributeDefinition().equals(relationshipAttributeDefinition)) {
                attribute.setTarget(target);
                isAttributeFound = true;
                break;
            }
        }
        // Si no se encuentra el atributo, se crea uno nuevo
        if (!isAttributeFound) {
            relationship.getRelationshipAttributes().add(new RelationshipAttribute(relationshipAttributeDefinition, relationship, target));
        }
        // Se resetean los placeholder para los target de las relaciones
        resetPlaceHolders();
    }

    /**
     * Este método es el encargado de remover una relación específica del concepto.
     */
    public void removeRelationship(RelationshipDefinition rd, Relationship r) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            concept.removeRelationshipWeb(r);
            concept.removeRelationship(r);
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", e.getMessage()));
        }

        if (rd.getOrderAttributeDefinition() != null) {
            int order = 1;
            for (RelationshipWeb rw : concept.getValidRelationshipsWebByRelationDefinition(rd)) {
                rw.setOrder(order);
                order++;
            }
        }

        autogenerateMCCE = new AutogenerateMCCE();

        if(concept.isPersistent() && !concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0) {
            autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
        }

        if(!isMCSpecialThisConcept()) {
            autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);
        }

        crossmapBean.refreshCrossmapIndirect(concept);

    }

    /**
     * Este método es el encargado de remover un atributo de relación específico de una relación
     */
    public void removeRelationshipAttribute(Relationship r, RelationshipAttribute ra) {
        r.getRelationshipAttributes().remove(ra);
        if(concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0)autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

        autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

    }

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public boolean validateRelationships() {
        for (RelationshipDefinitionWeb relationshipDefinition : getOrderedRelationshipDefinitions()) {
            boolean isMultiplicitySatisfied = concept.isMultiplicitySatisfied(relationshipDefinition);
            relationshipDefinition.setMultiplicitySatisfied(isMultiplicitySatisfied);

            if (!isMultiplicitySatisfied) {
                messageBean.messageError("El atributo " + relationshipDefinition.getName() + " no cumple con el minimo requerido");
                return false;
            }
            if (changeDirectMultiplicity(relationshipDefinition)) {
                messageBean.messageError("Información incompleta Cantidad y Unidad en " + relationshipDefinition.getName());
                return false;
            }
        }
        return true;
    }

    /**
     * Este metodo revisa que las relaciones cumplan el lower_boundary del
     * relationship definition, en caso de no cumplir la condicion se retorna falso.
     *
     * @return
     */
    public boolean validateDescriptions() {
        for (DescriptionWeb descriptionWeb : concept.getDescriptionsWeb()) {
            if(descriptionWeb.getDescriptionType().equals(DescriptionType.FSN) &&
                TagSMTKFactory.getInstance().findTagSMTKByName(descriptionWeb.getTerm().trim()) != null) {
                descriptionWeb.setUiValid(false);
                messageBean.messageError("Debe especificar una descripción FSN");
                return false;
            }

            if(descriptionWeb.getDescriptionType().equals(DescriptionType.PREFERIDA) &&
                descriptionWeb.getTerm().trim().isEmpty()) {
                descriptionWeb.setUiValid(false);
                messageBean.messageError("Debe especificar una descripción Preferida");
                return false;
            }
        }
        return true;
    }

    public boolean containDescription(DescriptionWeb descriptionWeb) {
        for (DescriptionWeb description : concept.getDescriptionsWeb()) {
            if (description.getTerm().trim().equals(descriptionWeb.getTerm().trim())) {
                return true;
            }
        }
        return false;
    }

    public void saveConcept() {
        FacesContext context = FacesContext.getCurrentInstance();
        resetPlaceHolders();
        if(pendingTerms && concept.getRelationshipsSnomedCT().isEmpty()){
            messageBean.messageError("Cuando se crea un concepto desde pendientes, este puede ser guardado, sólo si cumple las condiciones para ser un concepto Modelado.");
            return;
        }
        if (validateRelationships() && validateDescriptions()) {
            try{
                if (concept.isModeled()) {
                    relationshipBindingBR.brSCT005(concept);
                }
            }catch (EJBException e) {
                messageBean.messageError(e.getMessage());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                context.addMessage(null, new FacesMessage("Error", e.getMessage()));
            }
            // Si el concepto está persistido, actualizarlo. Si no, persistirlo
            if (concept.isPersistent()) {
                try {
                    updateConcept(context);
                } catch (EJBException e) {
                    messageBean.messageError(e.getMessage());
                }
            } else {
                if (!containDescriptionCategory(concept)) {
                    persistConcept(context);
                }
            }
        }
    }

    private boolean containDescriptionCategory(ConceptSMTKWeb conceptSMTK) {
        for (Description description : conceptSMTK.getDescriptionsWeb()) {

            ConceptSMTK aConcept = categoryManager.categoryContains(conceptSMTK.getCategory(), description.getTerm());

            if (aConcept != null) {
                messageBean.messageError("Ya existe la descripción " + description.getTerm() + " en la categoría " + category.getName() +". Descripción perteneciente a concepto: "+aConcept);
                return true;
            }
        }
        return false;
    }

    private void persistConcept(FacesContext context) {

        long id;

        try {
            id = conceptManager.persist(concept, user);
            concept.setId(id);
            if (pendingTerms) {
                for (DescriptionWeb descriptionWeb : concept.getDescriptionsWeb()) {
                    if(descriptionWeb.getConceptSMTK().equals(conceptManager.getPendingConcept())) {
                        ConceptSMTK conceptSource = descriptionWeb.getConceptSMTK();
                        descriptionWeb.setConceptSMTK(concept);
                        descriptionManager.moveDescriptionToConcept(conceptSource, descriptionWeb, user);
                    }
                }
            }
            context.addMessage(null, new FacesMessage("Acción Exitosa", "Concepto guardado "));
            // Se resetea el concepto, como el concepto está persistido, se le pasa su id
            getConceptById(id);
        } catch (BusinessRuleException bre) {
            context.addMessage(null, new FacesMessage("Error", bre.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Error", e.getMessage()));
        }
    }

    /**
     * @param context
     */
    private void updateConcept(FacesContext context) {

        /* Se actualizan los campos básicos */
        int changes = updateConceptFields();
        /* Se actualizan las descripciones */
        changes += updateConceptDescriptions();
        /* Se actualizan las relaciones */
        changes += updateConceptRelationships();

        changes = (refsetEditConcept) ? changes + 1 : changes;

        if (changes == 0)
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se ha realizado ningún cambio al concepto!!"));
        else {
            context.addMessage(null, new FacesMessage("Acción Exitosa", "Se han registrado " + changes + " cambios en el concepto."));
            // Se restablece el concepto, como el concepto está persistido, se le pasa su id
            getConceptById(concept.getId());
        }
    }

    /**
     * @return
     */
    private int updateConceptRelationships() {
        List<RelationshipWeb> relationshipsForPersist = concept.getUnpersistedRelationshipsWeb();

        /* Se persisten las nuevas relaciones */
        for (RelationshipWeb relationshipWeb : relationshipsForPersist) {
            relationshipWeb.setSourceConcept(concept);
            try {
                relationshipManager.bindRelationshipToConcept(concept, relationshipWeb.toRelationship(), user);
            } catch (EJBException EJB) {
                messageBean.messageError(EJB.getMessage());
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (RelationshipWeb relationshipWeb : relationshipsForPersist) {
            relationshipWeb.setSourceConcept(concept);
            if (snomedBeans.existRelationshipToSCT(concept)) {
                try {
                    relationshipBindingBR.postActions(relationshipWeb.toRelationship(), user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* Se elimina las relaciones eliminadas */
        List<RelationshipWeb> relationshipsForDelete = concept.getRemovedRelationshipsWeb(_concept);
        for (RelationshipWeb relationshipWeb : relationshipsForDelete) {
            try {
                relationshipManager.removeRelationship(concept,relationshipWeb, user);
                _concept.removeRelationship(relationshipWeb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* Se actualizan las relaciones actualizadas */
        List<Pair<RelationshipWeb, RelationshipWeb>> relationshipsForUpdate = concept.getModifiedRelationships(_concept);
        for (Pair<RelationshipWeb, RelationshipWeb> relationship : relationshipsForUpdate) {
            try {
                relationshipManager.updateRelationship(concept, relationship.getFirst(), relationship.getSecond(), user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        changeMarketedBean.changeMarketed();

        return relationshipsForPersist.size() + relationshipsForDelete.size() + relationshipsForUpdate.size();
    }

    /**
     * Este método es responsable de actualizar las descripciones del concepto.
     *
     * @return La cantidad de cambios realizados: agregadas, eliminadas y actualizadas.
     */
    private int updateConceptDescriptions() {

        /* Se persisten las nuevas descripciones */
        List<DescriptionWeb> unpersistedDescriptions = concept.getUnpersistedDescriptions();
        for (Description description : unpersistedDescriptions) {
            descriptionManager.bindDescriptionToConcept(concept, description, true, user);
        }
        /* Se invalidan las descripciones eliminadas */
        List<DescriptionWeb> descriptionsForDelete = concept.getRemovedDescriptionsWeb(_concept);
        for (Description description : descriptionsForDelete) {
            if (!(containDescriptionToTranslate(description) || containDescriptionNoValidToTranslate(description))) {
                descriptionManager.deleteDescription(description, user);
            }
            _concept.removeDescription(description);
        }
        /* Se actualizan las que tienen cambios */
        List<Pair<DescriptionWeb, DescriptionWeb>> descriptionsForUpdate = concept.getModifiedDescriptionsWeb(_concept);
        for (Pair<DescriptionWeb, DescriptionWeb> description : descriptionsForUpdate) {
            descriptionManager.updateDescription(concept, description.getFirst(), description.getSecond(), user);
        }
        for (NoValidDescription noValidDescription : noValidDescriptions) {
            descriptionManager.invalidateDescription(concept, noValidDescription, user);
        }
        for (DescriptionWeb description : descriptionsToTraslate) {
            descriptionManager.moveDescriptionToConcept(concept, description, user);
        }
        descriptionsToTraslate.clear();
        noValidDescriptions.clear();
        return unpersistedDescriptions.size() + descriptionsForDelete.size() + descriptionsForUpdate.size() + descriptionsToTraslate.size() + noValidDescriptions.size();
    }

    private int updateConceptFields() {
        int changes = 0;
        if (attributeChanges()) {
            conceptManager.updateFields(_concept, concept, user);
            changes++;
        }
        return changes;
    }

    private boolean attributeChanges() {
        return _concept.isToBeReviewed() != concept.isToBeReviewed()
                || _concept.isToBeConsulted() != concept.isToBeConsulted()
                || !concept.getObservation().equalsIgnoreCase(_concept.getObservation())
                || !_concept.getTagSMTK().equals(concept.getTagSMTK())
                || !_concept.isFullyDefined().equals(concept.isFullyDefined());
    }


    public void deleteConcept() throws IOException {
        // Si el concepto está persistido, invalidarlo
        if (concept.isPersistent() && !concept.isModeled()) {
            conceptManager.delete(concept, user);
            messageBean.messageSuccess("Acción exitosa", "Concepto eliminado");
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            eContext.redirect(eContext.getRequestContextPath() + "/views/browser/generalBrowser.xhtml?idCategory=" + category.getId());
        } else {
            conceptManager.invalidate(concept, user);
            messageBean.messageSuccess("Acción exitosa", "Concepto invalidado");
        }
    }

    public void cancelConcept() {
        try {
            concept.restore(_concept);
        } catch (Exception e) {
            e.printStackTrace();
        }
        descriptionsToTraslate.clear();
        noValidDescriptions.clear();
        changeMarketedBean.conceptSelected.clear();
        resetPlaceHolders();
        messageBean.messageSuccess("Acción exitosa", "Los cambios se han descartado");
    }

    public void updateFSN(Description d) {
        concept.getValidDescriptionFSN().setTerm(d.getTerm());
    }

    public boolean containDescriptionToTranslate(Description description) {
        for (Description descriptionTraslate : descriptionsToTraslate) {
            if (descriptionTraslate.getId() == description.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean containDescriptionNoValidToTranslate(Description description) {
        for (NoValidDescription descriptionTraslate : noValidDescriptions) {
            if (descriptionTraslate.getNoValidDescription().getId() == description.getId()) {
                return true;
            }
        }
        return false;
    }

    public void onRowReorder(ReorderEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        if(concept.isPersistent() &&! concept.isModeled() && autoGenerateList.isEmpty() && autogenerateMC.toString().trim().length()==0)autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) UIComponent.getCurrentComponent(context).getAttributes().get("relationshipDefinition");

        int sourceOrder = event.getFromIndex() + 1;
        int targetOrder = event.getToIndex() + 1;

        RelationshipAttribute att1 = concept.getAttributeOrder(relationshipDefinition, sourceOrder);
        RelationshipAttribute att2 = concept.getAttributeOrder(relationshipDefinition, targetOrder);

        att1.setTarget(new BasicTypeValue(targetOrder));
        att2.setTarget(new BasicTypeValue(sourceOrder));


        RelationshipDefinition relationshipDefinitionRowEdit = (RelationshipDefinition) UIComponent.getCurrentComponent(context).getAttributes().get("relationshipDefinitionRowEdit");
        if(relationshipDefinitionRowEdit==null)relationshipDefinitionRowEdit=relationshipDefinition;

        autogenerateBeans.loadAutogenerate(concept,autogenerateMC,autogenerateMCCE,autogeneratePCCE,autoGenerateList);

    }

    // Getter and Setter

    public long getIdTermPending() {
        return idTermPending;
    }

    public void setIdTermPending(long idTermPending) {
        this.idTermPending = idTermPending;
        descriptionPending = descriptionManager.getDescriptionByID(idTermPending);
        if (descriptionPending != null && !containDescription(new DescriptionWeb(descriptionPending))) {
            concept.addDescriptionWeb(new DescriptionWeb(descriptionPending));
        }
    }

    public void setPendingTerms(boolean pendingTerms) {
        if(pendingTerms){
            for (PendingTerm pendingTerm : pendingBrowserBean.getTermsSelected()) {
                concept.addDescriptionWeb(new DescriptionWeb(pendingTerm.getRelatedDescription()));
            }
        }
        pendingBrowserBean.setTermsSelected(new ArrayList<PendingTerm>());
        this.pendingTerms = pendingTerms;
    }

    public String getFSN() {
        return FSN;
    }

    public void setFSN(String FSN) {
        this.FSN = FSN;
    }

    public ConceptSMTKWeb getConcept() {
        return concept;
    }

    public void setConcept(ConceptSMTKWeb concept) {
        this.concept = concept;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getOtherTermino() {
        return otherTermino;
    }

    public void setOtherTermino(String otherTermino) {
        this.otherTermino = otherTermino;
    }

    public boolean getOtherSensibilidad() {
        return otherSensibilidad;
    }

    public void setOtherSensibilidad(boolean otherSensibilidad) {
        this.otherSensibilidad = otherSensibilidad;
    }

    public DescriptionType getOtherDescriptionType() {
        return otherDescriptionType;
    }

    public void setOtherDescriptionType(DescriptionType otherDescriptionType) {
        this.otherDescriptionType = otherDescriptionType;
    }

    public List<DescriptionType> getDescriptionTypes() {
        return descriptionTypes;
    }

    public void setDescriptionTypes(List<DescriptionType> descriptionTypes) {
        this.descriptionTypes = descriptionTypes;
    }

    public List<Description> getSelectedDescriptions() {
        return selectedDescriptions;
    }

    public void setSelectedDescriptions(List<Description> selectedDescriptions) {
        this.selectedDescriptions = selectedDescriptions;
    }

    public ConceptSMTK getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSMTK conceptSelected) {
        this.conceptSelected = conceptSelected;
    }

    public ConceptSCT getConceptSCTSelected() {
        return conceptSCTSelected;
    }

    public void setConceptSCTSelected(ConceptSCT conceptSCTSelected) {
        this.conceptSCTSelected = conceptSCTSelected;
    }

    public void setSmtkTypeBean(SMTKTypeBean smtkTypeBean) {
        this.smtkTypeBean = smtkTypeBean;
    }

    public String getFavoriteDescription() {
        return favoriteDescription;
    }

    public void setFavoriteDescription(String favoriteDescription) {
        this.favoriteDescription = favoriteDescription;
        if(concept==null) {
            createConcept();
        }
    }

    public HelperTablesManager getHelperTablesManager() {
        return helperTablesManager;
    }

    public void setHelperTablesManager(HelperTablesManager helperTableManager) {
        this.helperTablesManager = helperTableManager;
    }

    public BasicTypeValue getBasicTypeValue() {
        return basicTypeValue;
    }

    public void setBasicTypeValue(BasicTypeValue basicTypeValue) {
        this.basicTypeValue = basicTypeValue;
    }

    public HelperTableRow getSelectedHelperTableRecord() {
        return selectedHelperTableRecord;
    }

    public void setSelectedHelperTableRecord(HelperTableRow selectedHelperTableRecord) {
        this.selectedHelperTableRecord = selectedHelperTableRecord;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {
        this.conceptSMTK = conceptSMTK;
    }

    public List<TagSMTK> getTagSMTKs(String query) {
        List<TagSMTK> results = new ArrayList<TagSMTK>();
        for (TagSMTK tagSMTK : tagSMTKs) {
            if (tagSMTK.getName().toLowerCase().contains(query.toLowerCase()))
                results.add(tagSMTK);
        }
        return results;
    }

    public List<TagSMTK> getTagSMTKs() {
        return tagSMTKs;
    }

    public int getCategorySelect() {
        return categorySelect;
    }

    public void setCategorySelect(int categorySelect) {
        this.categorySelect = categorySelect;
    }

    public List<ConceptAuditAction> getAuditAction() {
        return auditAction;
    }

    public void setAuditAction(List<ConceptAuditAction> auditAction) {
        this.auditAction = auditAction;
    }

    public Description getDescriptionToTranslate() {
        return descriptionToTranslate;
    }

    public void setDescriptionToTranslate(Description descriptionToTranslate) {
        this.descriptionToTranslate = descriptionToTranslate;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public ConceptSMTK getConceptSMTKTranslateDes() {
        return conceptSMTKTranslateDes;
    }

    public void setConceptSMTKTranslateDes(ConceptSMTK conceptSMTKTranslateDes) {
        this.conceptSMTKTranslateDes = conceptSMTKTranslateDes;
    }

    public boolean isRefsetEditConcept() {
        return refsetEditConcept;
    }

    public void setRefsetEditConcept(boolean refsetEditConcept) {
        this.refsetEditConcept = refsetEditConcept;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @ManagedProperty(value = "#{profilePermissionsBeans}")
    private ProfilePermissionsBeans profilePermissionsBeans;

    public void setProfilePermissionsBeans(ProfilePermissionsBeans profilePermissionsBeans) {
        this.profilePermissionsBeans = profilePermissionsBeans;
    }

    //TODO: editar concepto

    private long idconceptselect;

    public long getIdconceptselect() {
        return idconceptselect;
    }

    public void setIdconceptselect(long idconceptselect) {
        this.idconceptselect = idconceptselect;
    }

    public int getHelperTableValuePlaceholder() {
        return helperTableValuePlaceholder;
    }

    public void setHelperTableValuePlaceholder(int helperTableValuePlaceholder) {
        this.helperTableValuePlaceholder = helperTableValuePlaceholder;
    }

    public ConceptSMTK getConceptSMTKAttributeSelected() {
        return conceptSMTKAttributeSelected;
    }

    public void setConceptSMTKAttributeSelected(ConceptSMTK conceptSMTKAttributeSelected) {
        this.conceptSMTKAttributeSelected = conceptSMTKAttributeSelected;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public List<DescriptionType> getDescriptionTypesEdit() {
        return descriptionTypesEdit;
    }

    public void setDescriptionTypesEdit(List<DescriptionType> descriptionTypesEdit) {
        this.descriptionTypesEdit = descriptionTypesEdit;
    }

    public ConceptSMTK getConceptSMTKNotValid() {
        return conceptSMTKNotValid;
    }

    public void setConceptSMTKNotValid(ConceptSMTK conceptSMTKNotValid) {
        this.conceptSMTKNotValid = conceptSMTKNotValid;
    }

    public void setCompositeAditionalBean(CompositeAditional compositeAditionalBean) {
        this.compositeAditionalBean = compositeAditionalBean;
    }

    public Map<Long, Relationship> getRelationshipPlaceholders() {
        return relationshipPlaceholders;
    }

    public ConceptSMTK getConceptSuggested() {
        return conceptSuggested;
    }

    public void setConceptSuggested(ConceptSMTK conceptSuggested) {
        this.conceptSuggested = conceptSuggested;
    }

    public List<ConceptSMTK> getConceptSuggestedList() {
        return conceptSuggestedList;
    }

    public void setConceptSuggestedList(List<ConceptSMTK> conceptSuggestedList) {
        this.conceptSuggestedList = conceptSuggestedList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setChangeMarketedBean(ChangeMarketedBean changeMarketedBean) {
        this.changeMarketedBean = changeMarketedBean;
    }

    public CrossmapSetRecord getCrossmapSetMemberSelected() {
        return crossmapSetMemberSelected;
    }

    public void setCrossmapSetMemberSelected(CrossmapSetRecord crossmapSetMemberSelected) {
        this.crossmapSetMemberSelected = crossmapSetMemberSelected;
    }

    /**
     * Este método retorna una lista ordenada de relaciones.
     *
     * @return Una lista ordenada de las relaciones de la categoría.
     */
    public List<RelationshipDefinitionWeb> getOrderedRelationshipDefinitions() {
        if (orderedRelationshipDefinitionsList.isEmpty()) {
            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {
                RelationshipDefinitionWeb relationshipDefinitionWeb =
                        viewAugmenter.augmentRelationshipDefinition(category, relationshipDefinition);
                orderedRelationshipDefinitionsList.add(relationshipDefinitionWeb);
            }
            Collections.sort(orderedRelationshipDefinitionsList);
        }
        return orderedRelationshipDefinitionsList;
    }

    /**
     * Este método retorna una lista ordenada de definiciones propias de semantikos.
     *
     * @return Una lista ordenada de las relaciones de la categoría.
     */
    public List<RelationshipDefinitionWeb> getOrderedSMTKRelationshipDefinitions() {
        if (orderedRelationshipDefinitionsList.isEmpty() && category != null) {
            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {
                RelationshipDefinitionWeb relationshipDefinitionWeb = viewAugmenter.augmentRelationshipDefinition(category, relationshipDefinition);
                orderedRelationshipDefinitionsList.add(relationshipDefinitionWeb);
            }
            Collections.sort(orderedRelationshipDefinitionsList);
        }

        List<RelationshipDefinitionWeb> smtkRelationshipDefinitions = new ArrayList<>();

        for (RelationshipDefinitionWeb relationshipDefinition : orderedRelationshipDefinitionsList) {
            if(!relationshipDefinition.getTargetDefinition().isSnomedCTType() && !relationshipDefinition.getTargetDefinition().isCrossMapType()) {
                smtkRelationshipDefinitions.add(relationshipDefinition);
            }
        }
        return smtkRelationshipDefinitions;
    }

    public void changeMultiplicityNotRequiredRelationshipDefinitionMC() {
        for (RelationshipDefinition relationshipDefinition : orderedRelationshipDefinitionsList) {
            if (relationshipDefinition.getId() == 46) relationshipDefinition.getMultiplicity().setLowerBoundary(0);
            if (relationshipDefinition.getId() == 58) relationshipDefinition.getMultiplicity().setLowerBoundary(0);
            if (relationshipDefinition.getId() == 47) relationshipDefinition.getMultiplicity().setLowerBoundary(0);
        }
        if(concept != null) {
            for (RelationshipDefinition relationshipDefinition : concept.getCategory().getRelationshipDefinitions()) {
                if (relationshipDefinition.getId() == 46) relationshipDefinition.getMultiplicity().setLowerBoundary(0);
                if (relationshipDefinition.getId() == 58) relationshipDefinition.getMultiplicity().setLowerBoundary(0);
                if (relationshipDefinition.getId() == 47) relationshipDefinition.getMultiplicity().setLowerBoundary(0);
            }
        }
    }

    public void changeMultiplicityToRequiredRelationshipDefinitionMC() {
        for (RelationshipDefinition relationshipDefinition : orderedRelationshipDefinitionsList) {
            if (relationshipDefinition.getId() == 46) relationshipDefinition.getMultiplicity().setLowerBoundary(1);
            if (relationshipDefinition.getId() == 58) relationshipDefinition.getMultiplicity().setLowerBoundary(1);
            if (relationshipDefinition.getId() == 47) relationshipDefinition.getMultiplicity().setLowerBoundary(1);
        }
        if(concept != null) {
            for (RelationshipDefinition relationshipDefinition : concept.getCategory().getRelationshipDefinitions()) {
                if (relationshipDefinition.getId() == 46) relationshipDefinition.getMultiplicity().setLowerBoundary(1);
                if (relationshipDefinition.getId() == 58) relationshipDefinition.getMultiplicity().setLowerBoundary(1);
                if (relationshipDefinition.getId() == 47) relationshipDefinition.getMultiplicity().setLowerBoundary(1);
            }
        }
    }

    public boolean isMCSpecial(RelationshipDefinition relationshipDefinition) {
        if(relationshipDefinition.getId()==74){
            return true;
        }return false;
    }

    public boolean isMCSpecialRendered(RelationshipDefinition relationshipDefinition) {
        if(relationshipDefinition.getId()==74 && concept.isModeled()){
            return true;
        }return false;
    }
    public boolean isMCSpecialThisConcept() {
        for (Relationship relationship : concept.getValidRelationships()) {
            if (relationship.getRelationshipDefinition().getId() == 74) {
                if (((BasicTypeValue<Boolean>) relationship.getTarget()).getValue()){
                   return true;
                }
            }
        }
        return false;
    }

    public void changeMCSpecial() {
        for (Relationship relationship : concept.getValidRelationships()) {
            if (relationship.getRelationshipDefinition().getId() == 74) {
                if (!((BasicTypeValue<Boolean>) relationship.getTarget()).getValue()){
                    changeMultiplicityToRequiredRelationshipDefinitionMC();
                }
                else{
                    changeMultiplicityNotRequiredRelationshipDefinitionMC();
                }
            }
        }
    }

    public boolean changeDirectMultiplicity(RelationshipDefinition relationshipDefinition) {
        //MCCE Pack Multi
        if (relationshipDefinition.getId() == 77) return changeDirectMultiplicity(relationshipDefinition, 16L);
        //MCCE Volumen total
        if (relationshipDefinition.getId() == 93) return changeDirectMultiplicity(relationshipDefinition, 17L);
        //MC Cantidad Volumen total
        if (relationshipDefinition.getId() == 69) return changeDirectMultiplicity(relationshipDefinition, 12L);
        return false;
    }

    public boolean changeDirectMultiplicity(RelationshipDefinition relationshipDefinition, Long idAttributeDefinition) {
        for (RelationshipAttributeDefinition relationshipAttributeDefinition : relationshipDefinition.getRelationshipAttributeDefinitions()) {
            if (relationshipAttributeDefinition.getId() == idAttributeDefinition) {
                if (!concept.getRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {
                    for (Relationship relationship : concept.getRelationshipsByRelationDefinition(relationshipDefinition)) {
                        return relationship.getAttributesByAttributeDefinition(relationshipAttributeDefinition).isEmpty();
                    }
                }
            }
        }
        return false;
    }

    public boolean changeIndirectMultiplicity(Relationship relation, RelationshipDefinition relationshipDefinition, RelationshipAttributeDefinition relationshipAttributeDefinition) {
        if (relationshipAttributeDefinition.getId() == 8 && relation.getAttributesByAttributeDefinition(relationshipAttributeDefinition).size() > 0) {
            return isEmpty(relation, relationshipDefinition, relationshipAttributeDefinition, 9L);
        }
        if (relationshipAttributeDefinition.getId() == 10 && relation.getAttributesByAttributeDefinition(relationshipAttributeDefinition).size() > 0) {
            return isEmpty(relation, relationshipDefinition, relationshipAttributeDefinition, 11L);
        }
        return false;
    }

    public boolean isEmpty(Relationship relation, RelationshipDefinition relationshipDefinition, RelationshipAttributeDefinition relationshipAttributeDefinition, Long idAttributeDefinition) {
        if (relation.getAttributesByAttributeDefinition(relationshipAttributeDefinition).size() != 0) {
            for (RelationshipAttributeDefinition rAD : relationshipDefinition.getRelationshipAttributeDefinitions()) {
                if (rAD.getId() == idAttributeDefinition) {
                    return relation.getAttributesByAttributeDefinition(rAD).isEmpty();
                }
            }
        }
        return true;
    }

    public boolean isFullyDefined() {
        if (concept != null) {
            concept.setFullyDefined(fullyDefined);
            return (concept.isFullyDefined()) ? true : false;
        }
        return this.fullyDefined;
    }

    public void changeFullyDefined(ConceptSMTKWeb concept) {
        try {
            concept.setFullyDefined((fullyDefined) ? true : false);
            if (concept.isFullyDefined()) {
                conceptDefinitionalGradeBR.apply(concept);
            }
        } catch (EJBException e) {
            concept.setFullyDefined(false);
            fullyDefined=false;
            messageBean.messageError("No es posible establecer este grado de definición, porque existen otros conceptos con las relaciones a SNOMED CT");
        }
    }

    public void setFullyDefined(boolean fullyDefined) {
        this.fullyDefined = fullyDefined;
    }

    public MessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

}