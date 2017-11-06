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

public class HelperTableRowDTO implements Serializable {

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

    @JsonProperty("creation_date")
    public Timestamp getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("last_edit_date")
    public Timestamp getLastEditDate() {
        return lastEditDate;
    }

    @JsonProperty("last_edit_date")
    public void setLastEditDate(Timestamp lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @JsonProperty("valid")
    public boolean isValid() {
        return valid;
    }

    @JsonProperty("valid")
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @JsonProperty("validity_until")
    public Timestamp getValidityUntil() {
        return validityUntil;
    }


    @JsonProperty("validity_until")
    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @JsonProperty("creation_user")
    public String getCreationUsername() {
        return creationUsername;
    }


    @JsonProperty("creation_user")
    public void setCreationUsername(String creationUsername) {
        this.creationUsername = creationUsername;
    }

    @JsonProperty("helper_table_id")
    public long getHelperTableId() {

        return helperTableId;
    }

    @JsonProperty("helper_table_id")
    public void setHelperTableId(long helperTableId) {
        this.helperTableId = helperTableId;
    }

    @JsonProperty("last_edit_user")
    public String getLastEditUsername() {
        return lastEditUsername;
    }

    @JsonProperty("last_edit_user")
    public void setLastEditUsername(String lastEditUsername) {
        this.lastEditUsername = lastEditUsername;
    }

    @Override
    public String toString() {
        return description;
    }

    public List<HelperTableData> getCells() {
        return cells;
    }

    public void setCells(List<HelperTableData> cells) {
        this.cells = cells;
    }
}
