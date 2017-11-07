package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class HelperTableRowDTO implements TargetDTO, Serializable {

    private long id;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="America/Buenos_Aires")
    private Timestamp creationDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="America/Buenos_Aires")
    private Timestamp lastEditDate;
    private boolean valid;
    private Timestamp validityUntil;
    private String description;
    private String creationUsername;
    private String lastEditUsername;

    private List<HelperTableData> cells = new ArrayList<>();
    private long helperTableId;


    public HelperTableRowDTO(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Timestamp lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationUsername() {
        return creationUsername;
    }

    public void setCreationUsername(String creationUsername) {
        this.creationUsername = creationUsername;
    }

    public long getHelperTableId() {
        return helperTableId;
    }

    public void setHelperTableId(long helperTableId) {
        this.helperTableId = helperTableId;
    }

    public String getLastEditUsername() {
        return lastEditUsername;
    }

    public void setLastEditUsername(String lastEditUsername) {
        this.lastEditUsername = lastEditUsername;
    }

    public List<HelperTableData> getCells() {
        return cells;
    }

    public void setCells(List<HelperTableData> cells) {
        if(cells != null) {
            this.cells = cells;
        }
    }
}
