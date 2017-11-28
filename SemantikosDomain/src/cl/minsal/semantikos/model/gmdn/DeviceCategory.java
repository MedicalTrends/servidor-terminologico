package cl.minsal.semantikos.model.gmdn;

import java.io.Serializable;

/**
 * Created by des01c7 on 20-11-17.
 */
public class DeviceCategory implements Serializable {

    private long code;

    private String description;

    public DeviceCategory(long code, String description) {
        this.code = code;
        this.description = description;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
