package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.TagSMTKManager;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.system.SystemFactory;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(eager = true)
@ApplicationScoped
public class ApplicationContainer implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(ApplicationContainer.class);

    private List<Category> categories;

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    private TagSMTKManager tagSMTKManager = (TagSMTKManager) ServiceLocator.getInstance().getService(TagSMTKManager.class);

    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    private TagSMTKFactory tagSMTKFactory;

    private DescriptionTypeFactory descriptionTypeFactory;

    //@EJB
    private TimeOutWeb timeOutWeb= (TimeOutWeb) ServiceLocator.getInstance().getService(TimeOutWeb.class);

    private static int timeOut;

    @PostConstruct
    public void init() {

        categories =  categoryManager.getCategories();

        tagSMTKFactory = tagSMTKManager.getTagSMTKFactory();

        descriptionTypeFactory = descriptionManager.getDescriptionTypeFactory();

        TagSMTKFactory.getInstance().setTagsSMTK(tagSMTKFactory.getTagsSMTK());
        TagSMTKFactory.getInstance().setTagsSMTKByName(tagSMTKFactory.getTagsSMTKByName());

        DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypeFactory.getDescriptionTypes());

        SystemFactory.getInstance().setTimeout(timeOutWeb.getTimeOut());
        //SystemFactory.getInstance().setTimeout(5000);

    }

    public TagSMTKFactory getTagSMTKFactory() {
        return tagSMTKFactory;
    }

    public void setTagSMTKFactory(TagSMTKFactory tagSMTKFactory) {
        this.tagSMTKFactory = tagSMTKFactory;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public static int getTimeOut() {
        return timeOut;
    }

    public static void setTimeOut(int timeOut) {
        ApplicationContainer.timeOut = timeOut;
    }

}

