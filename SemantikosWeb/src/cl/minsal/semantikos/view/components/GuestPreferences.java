package cl.minsal.semantikos.view.components;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {
    
    //match address bar in mobile browser
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
}