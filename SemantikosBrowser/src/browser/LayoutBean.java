package browser;

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

    private static String SIZE = "size";
    private static String RESIZABLE = "resizable";
    private static String CLOSABLE = "closable";
    private static String SLIDABLE = "slidable";
    private static String RESIZE_WHILE_DRAGGING = "resizeWhileDragging";

    private static String MIN_HEIGHT = "minHeight";


    @PostConstruct
    protected void initialize() {
        layoutOptions = new LayoutOptions();

        // options for all panes
        LayoutOptions panes = new LayoutOptions();
        panes.addOption(RESIZABLE, false);
        panes.addOption(RESIZE_WHILE_DRAGGING, false);
        layoutOptions.setPanesOptions(panes);

        // options for north pane
        LayoutOptions north = new LayoutOptions();
        north.addOption(SIZE,60);
        north.addOption(RESIZABLE, false);
        north.addOption(CLOSABLE, false);
        layoutOptions.setNorthOptions(north);
        //closable="false" resizeWhileDragging="false"


        // options for center pane
        LayoutOptions center = new LayoutOptions();
        center.addOption(SIZE,"50%");
        layoutOptions.setCenterOptions(center);

        // options for nested center layout

        LayoutOptions childCenterOptions = new LayoutOptions();
        childCenterOptions.addOption(SIZE,"50%");
        center.setChildOptions(childCenterOptions);

        // options for center-north pane
        LayoutOptions centerNorth = new LayoutOptions();
        centerNorth.addOption(SIZE, "50%");
        childCenterOptions.setNorthOptions(centerNorth);


        // options for center-center pane
        LayoutOptions centerCenter = new LayoutOptions();
        centerCenter.addOption(MIN_HEIGHT, 60);
        childCenterOptions.setCenterOptions(centerCenter);

        // options for west pane
        LayoutOptions west = new LayoutOptions();
        west.addOption(SIZE, 200);
        layoutOptions.setWestOptions(west);

        // options for nested west layout
        LayoutOptions childWestOptions = new LayoutOptions();
        west.setChildOptions(childWestOptions);

        // options for west-north pane
        LayoutOptions westNorth = new LayoutOptions();
        westNorth.addOption(SIZE, "33%");
        childWestOptions.setNorthOptions(westNorth);

        // options for west-center pane
        LayoutOptions westCenter = new LayoutOptions();
        westCenter.addOption(MIN_HEIGHT, "60");
        childWestOptions.setCenterOptions(westCenter);

        // options for west-south pane
        LayoutOptions westSouth = new LayoutOptions();
        westSouth.addOption(SIZE, "33%");
        childWestOptions.setSouthOptions(westSouth);

        // options for east pane
        LayoutOptions east = new LayoutOptions();
        east.addOption(SIZE, 300);
        east.addOption(RESIZABLE, false);
        //north.addOption("closable", false);
        layoutOptions.setEastOptions(east);

        // options for south pane
        LayoutOptions south = new LayoutOptions();
        south.addOption(SIZE, 85);
        south.addOption(RESIZABLE, false);
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
