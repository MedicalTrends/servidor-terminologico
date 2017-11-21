package cl.minsal.semantikos.model.gmdn;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

/**
 * Created by des01c7 on 20-11-17.
 */
public class DeviceType extends PersistentEntity implements Target {

    private String brand;

    private String model;

    private String tradeName;

    private GenericDeviceGroup genericDeviceGroup;

    public DeviceType(String brand, String model, String tradeName) {
        this.brand = brand;
        this.model = model;
        this.tradeName = tradeName;
    }

    public DeviceType(String brand, String model, String tradeName, GenericDeviceGroup genericDeviceGroup) {
        this.brand = brand;
        this.model = model;
        this.tradeName = tradeName;
        this.genericDeviceGroup = genericDeviceGroup;
    }

    public DeviceType(long id, String brand, String model, String tradeName, GenericDeviceGroup genericDeviceGroup) {
        super(id);
        this.brand = brand;
        this.model = model;
        this.tradeName = tradeName;
        this.genericDeviceGroup = genericDeviceGroup;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public GenericDeviceGroup getGenericDeviceGroup() {
        return genericDeviceGroup;
    }

    public void setGenericDeviceGroup(GenericDeviceGroup genericDeviceGroup) {
        this.genericDeviceGroup = genericDeviceGroup;
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.GMDN;
    }

    @Override
    public String getRepresentation() {
        return this.getGenericDeviceGroup().getTermName()+" - Marca: "+this.getBrand() +" Modelo: "+this.getModel();
    }

    @Override
    public Target copy() {
        return new DeviceType(this.getBrand(), this.getModel(), this.getTradeName(), this.genericDeviceGroup);
    }
}
