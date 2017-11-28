package cl.minsal.semantikos.gmdn;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.GmdnManager;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

import static java.util.Collections.emptyList;

/**
 * @author Gustavo Punucura
 * @created 26-07-16.
 */

@ManagedBean(name = "gmdnBean")
@ViewScoped
public class GmdnTypeBean implements Serializable {

    //@EJB
    private GmdnManager gmdnManager = (GmdnManager) RemoteEJBClientFactory.getInstance().getManager(GmdnManager.class);

    private String pattern;

    private transient TreeNode root;

    /**
     * Constructor por defecto para la inicialización de componentes.

     */
    public GmdnTypeBean() {

    }

    public GmdnManager getGmdnManager() {
        return gmdnManager;
    }

    public void setGmdnManager(GmdnManager gmdnManager) {
        this.gmdnManager = gmdnManager;
    }

    /**
     * Este método realiza la búsqueda del auto-complete, recuperando todos los conceptos (mostrando su toString()) SCT
     * cuyas descripciones coinciden con el patrón buscado.
     *
     * @param patron El patrón de búsqueda.
     *
     * @return Una lista con los conceptos a desplegar.
     */
    public List<DeviceType> getDeviceTypeSearchInput(String patron) {

        pattern = patron;

        List<DeviceType> deviceTypes = new ArrayList<>();

        deviceTypes = gmdnManager.findDeviceTypesByPattern(patron);

        return deviceTypes;
    }

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode(new CollectiveTerm(0,"root", "root element"), null);
    }

    public TreeNode getRoot() {
        return root;
    }


    public TreeNode mapCollectiveTerms(List<CollectiveTerm> collectiveTerms, TreeNode treeNode, boolean expanded) {

        //CollectiveTerm collectiveTermData = (CollectiveTerm) treeNode.getData();

        /*
        if(conceptData.equals(conceptSelected)) {
            expanded = false;
        }
        */

        treeNode.setExpanded(expanded);

        for (CollectiveTerm collectiveTerm : collectiveTerms) {

            TreeNode childTreeNode = new DefaultTreeNode(collectiveTerm, treeNode);

            List<CollectiveTerm> children = new ArrayList<>();

            for (CollectiveTerm child : collectiveTerm.getChildren()) {
                children.add(child);
            }

            mapCollectiveTerms(children, childTreeNode, expanded);
        }

        return root;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


}
