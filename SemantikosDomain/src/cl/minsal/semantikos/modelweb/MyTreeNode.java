package cl.minsal.semantikos.modelweb;

/**
 * Created by root on 30-09-21.
 */
public class MyTreeNode {

    String title;
    String name;
    String icon;
    String detail;

    String type = "default";

    public MyTreeNode(String title, String name, String icon) {
        this.title = title;
        this.name = name;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
