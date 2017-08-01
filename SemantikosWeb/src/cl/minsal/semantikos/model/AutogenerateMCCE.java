package cl.minsal.semantikos.model;

/**
 * Created by des01c7 on 17-10-16.
 */
public class AutogenerateMCCE {

    private String MC;
    private String cantidad;
    private String unidadMedidaCantidad;
    private String volumen;
    private String volumenUnidad;
    private String pack;
    private String packUnidad;

    public AutogenerateMCCE() {
        this.MC = "";
        this.cantidad = "";
        this.unidadMedidaCantidad = "";
        this.volumen = "";
        this.volumenUnidad = "";
        this.pack = "";
        this.packUnidad = "";
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getCantidad() {
        return (cantidad==null)?"":cantidad;

    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedidaCantidad() {
        return (unidadMedidaCantidad==null)?"":unidadMedidaCantidad;
    }

    public void setUnidadMedidaCantidad(String unidadMedidaCantidad) {
        this.unidadMedidaCantidad = unidadMedidaCantidad;
    }

    public String getVolumen() {
        return (volumen==null)? "": volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getVolumenUnidad() {
        return (volumenUnidad==null)? "": volumenUnidad;
    }

    public void setVolumenUnidad(String volumenUnidad) {
        this.volumenUnidad = volumenUnidad;
    }

    public String getPack() {
        return (pack==null)? "":pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPackUnidad() {
        return (packUnidad==null)? "": packUnidad;
    }

    public void setPackUnidad(String packUnidad) {
        this.packUnidad = packUnidad;
    }

    @Override
    public String toString() {
        return  MC + ' '  + getCantidad() + ' ' + getUnidadMedidaCantidad() + ' ' +  getVolumen() + ' ' + getVolumenUnidad() + ' ' + getPack() + ' '+ getPackUnidad();
    }
}
