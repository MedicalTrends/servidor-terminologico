import org.primefaces.extensions.component.layout.LayoutPane;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by root on 05-01-17.
 */
@ManagedBean
@ViewScoped
public class LayoutBean implements Serializable {

    private static final long serialVersionUID = 20120925L;

    private LayoutOptions layoutOptions;

    @PostConstruct
    protected void initialize() {
        layoutOptions = new LayoutOptions();

        // options for all panes
        LayoutOptions panes = new LayoutOptions();
        panes.addOption("slidable", false);
        panes.addOption("resizeWhileDragging", false);
        layoutOptions.setPanesOptions(panes);

        // options for north pane
        LayoutOptions north = new LayoutOptions();
        north.addOption("size",60);
        north.addOption("resizable", false);
        north.addOption("closable", false);
        layoutOptions.setNorthOptions(north);
        //closable="false" resizeWhileDragging="false"


        // options for center pane
        LayoutOptions center = new LayoutOptions();
        center.addOption("size","50%");
        layoutOptions.setCenterOptions(center);

        // options for nested center layout

        LayoutOptions childCenterOptions = new LayoutOptions();
        childCenterOptions.addOption("size","50%");
        center.setChildOptions(childCenterOptions);

        // options for center-north pane
        LayoutOptions centerNorth = new LayoutOptions();
        centerNorth.addOption("size", "50%");
        childCenterOptions.setNorthOptions(centerNorth);


        // options for center-center pane
        LayoutOptions centerCenter = new LayoutOptions();
        centerCenter.addOption("minHeight", 60);
        childCenterOptions.setCenterOptions(centerCenter);

        // options for west pane
        LayoutOptions west = new LayoutOptions();
        west.addOption("size", 200);
        layoutOptions.setWestOptions(west);

        // options for nested west layout
        LayoutOptions childWestOptions = new LayoutOptions();
        west.setChildOptions(childWestOptions);

        // options for west-north pane
        LayoutOptions westNorth = new LayoutOptions();
        westNorth.addOption("size", "33%");
        childWestOptions.setNorthOptions(westNorth);

        // options for west-center pane
        LayoutOptions westCenter = new LayoutOptions();
        westCenter.addOption("minHeight", "60");
        childWestOptions.setCenterOptions(westCenter);

        // options for west-south pane
        LayoutOptions westSouth = new LayoutOptions();
        westSouth.addOption("size", "33%");
        childWestOptions.setSouthOptions(westSouth);

        // options for east pane
        LayoutOptions east = new LayoutOptions();
        east.addOption("size", 300);
        east.addOption("resizable", false);
        //north.addOption("closable", false);
        layoutOptions.setEastOptions(east);

        // options for south pane
        LayoutOptions south = new LayoutOptions();
        south.addOption("size", 85);
        south.addOption("resizable", false);
        //south.addOption("closable", false);
        layoutOptions.setSouthOptions(south);

    }

    public LayoutOptions getLayoutOptions() {
        return layoutOptions;
    }

    public void handleClose(CloseEvent event) {
        FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane closed",
                        "Position:" + ((LayoutPane) event.getComponent()).getPosition());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleOpen(OpenEvent event) {
        FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane opened",
                        "Position:" + ((LayoutPane) event.getComponent()).getPosition());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleResize(ResizeEvent event) {
        FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane resized",
                        "Position:" + ((LayoutPane) event.getComponent()).getPosition() + ", new width = "
                                + event.getWidth() + "px, new height = " + event.getHeight() + "px");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
