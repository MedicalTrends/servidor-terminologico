package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.components.GuestPreferences;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.*;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "conceptTreeBean")
@SessionScoped
public class ConceptTreeBean implements Serializable {

    static private final Logger logger = LoggerFactory.getLogger(ConceptTreeBean.class);


    private transient TreeNode conceptRoot;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
    }


    public TreeNode getConceptTree(ConceptSMTK concept) {

        conceptRoot = new DefaultTreeNode(new Object(), null);

        DefaultTreeNode rootNode = new DefaultTreeNode(concept.getConceptID(), conceptRoot);

        DefaultTreeNode favouriteDescriptionNode = new DefaultTreeNode(concept.getDescriptionFavorite().getTerm(), rootNode);

        DefaultTreeNode fsnDescriptionNode = new DefaultTreeNode(concept.getDescriptionFSN().getTerm(), rootNode);

        return rootNode;

    }



}
