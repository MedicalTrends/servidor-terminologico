package cl.minsal.semantikos.components;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    //match address bar in mobile browser
    /*
    private Map<String,String> themeColors;
    
    //theme name
    private String theme = "blue";
    
    //menu mode    
    private String menuLayout = "static";
        
    @PostConstruct
    public void init() {
        themeColors = new HashMap<String,String>();
        themeColors.put("green", "#00acac");
        themeColors.put("blue", "#2f8ee5");
        themeColors.put("orange", "#efa64c");
        themeColors.put("purple", "#6c76af");
        themeColors.put("pink", "#f16383");
        themeColors.put("light-blue", "#63c9f1");
    }
        
    public String getMenuLayout() {
        if(this.menuLayout.equals("static"))
            return "menu-layout-static";
        else if(this.menuLayout.equals("overlay"))
            return "menu-layout-overlay";
        else if(this.menuLayout.equals("horizontal"))
            return "menu-layout-static menu-layout-horizontal";
        else
            return "menu-layout-static";
    }
    
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
    	this.theme = theme;
    }
    
    public void setMenuLayout(String menuLayout) {
        this.menuLayout = menuLayout;
    }
    
    public Map getThemeColors() {
        return this.themeColors;
    }
    */

    private Map<String,String> themeColors;

    private String theme = "cyan";

    private String menuClass = null;

    private String profileMode = "inline";

    private String menuLayout = "static";

    private boolean compact = true;

    private boolean orientationRTL;

    @PostConstruct
    public void init() {
        themeColors = new HashMap<String,String>();
        themeColors.put("indigo", "#3F51B5");
        themeColors.put("blue", "#03A9F4");
        themeColors.put("blue-grey", "#607D8B");
        themeColors.put("brown", "#795548");
        themeColors.put("cyan", "#00bcd4");
        themeColors.put("green", "#4CAF50");
        themeColors.put("purple-amber", "#673AB7");
        themeColors.put("purple-cyan", "#673AB7");
        themeColors.put("teal", "#009688");
    }

    public String getMenuClass() {
        return this.menuClass;
    }

    public String getProfileMode() {
        return this.profileMode;
    }

    public String getTheme() {
        return theme;
    }

    public String getMenuLayout() {
        if(this.menuLayout.equals("static"))
            return "menu-layout-static";
        else if(this.menuLayout.equals("overlay"))
            return "menu-layout-overlay";
        else if(this.menuLayout.equals("horizontal"))
            return "menu-layout-static menu-layout-horizontal";
        else if(this.menuLayout.equals("slim"))
            return "menu-layout-static layout-menu-slim";
        else
            return "menu-layout-static";
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setLightMenu() {
        this.menuClass = null;
    }

    public void setDarkMenu() {
        this.menuClass = "layout-menu-dark";
    }

    public void setProfileMode(String profileMode) {
        this.profileMode = profileMode;
    }

    public void setMenuLayout(String menuLayout) {
        this.menuLayout = menuLayout;
    }

    public Map getThemeColors() {
        return this.themeColors;
    }

    public void setCompact(boolean value) {
        this.compact = value;
    }

    public boolean isCompact() {
        return this.compact;
    }

    public boolean isOrientationRTL() {
        return orientationRTL;
    }

    public void setOrientationRTL(boolean orientationRTL) {
        this.orientationRTL = orientationRTL;
    }
}