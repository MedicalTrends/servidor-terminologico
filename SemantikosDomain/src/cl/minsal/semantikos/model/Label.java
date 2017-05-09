package cl.minsal.semantikos.model;

/**
 * @author Andrés Farías
 */
public class Label {

    /** El nombre de la etiqueta */
    private String name;

    /** Color de fondo */
    private String backgroundColor;

    /** Color del font */
    private String fontColor;

    public Label(String name, String backgroundColor, String fontColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
