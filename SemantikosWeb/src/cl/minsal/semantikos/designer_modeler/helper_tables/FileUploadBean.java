/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cl.minsal.semantikos.designer_modeler.helper_tables;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableImportReport;
import cl.minsal.semantikos.model.helpertables.LoadMode;
import cl.minsal.semantikos.model.helpertables.LoadStatus;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@ManagedBean(name = "fileUploadBean")
@ViewScoped
public class FileUploadBean {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadBean.class);

    @EJB
    private HelperTablesManager helperTablesManager;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    /** El archivo cargado */
    private UploadedFile file;

    /** Helper Table seleccionada */
    private HelperTable helperTable;

    /** Lista de Helper tables */
    private List<HelperTable> helperTableList;

    private LoadMode loadModeSelected;

    private LoadMode[] loadModes;


    public HelperTable getHelperTable() {
        return helperTable;
    }

    public void setHelperTable(HelperTable helperTable) {
        if(helperTable!=null)this.helperTable = helperTable;
    }

    public List<HelperTable> getHelperTableList() {
        return helperTableList;
    }

    public void setHelperTableList(List<HelperTable> helperTableList) {
       this.helperTableList = helperTableList;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public LoadMode getLoadModeSelected() {
        return loadModeSelected;
    }

    public void setLoadModeSelected(LoadMode loadModeSelected) {
        if(loadModeSelected!=null)this.loadModeSelected = loadModeSelected;
    }

    public LoadMode[] getLoadModes() {
        return loadModes;
    }

    public void setLoadModes(LoadMode[] loadModes) {
        this.loadModes = loadModes;
    }

    @PostConstruct
    public void init(){
        helperTableList= (List<HelperTable>) helperTablesManager.findAll();
        loadModes= LoadMode.values();
    }

    /**
     * Este método es utilizado como acción para cargar el archivo CVS con una tabla.
     */
    public void upload(FileUploadEvent event) {
        logger.info("FileUpload");
        this.file=event.getFile();
        if (file != null) {
            logger.info("Archivo cargado:" + file.getFileName());

            Reader in;
            try {
                in = new InputStreamReader(file.getInputstream());
            } catch (IOException e) {
                logger.error("Error al cargar el streaming.");
                return;
            }

            /* Se invoca la función de negocio para cargar el archivo */
            HelperTableImportReport helperTableImportReport = helperTablesManager.loadFromFile(helperTable, loadModeSelected, in, authenticationBean.getLoggedUser());
            if (helperTableImportReport.getStatus().equals(LoadStatus.CANCELED)){
                FacesMessage msg = new FacesMessage("Carga cancelada! " + helperTableImportReport.getExceptions());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            else {
                FacesMessage msg = new FacesMessage("Carga exitosa! " + helperTableImportReport.getInsertedRecords() + " registros cargados.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            logger.info("Archivo NO cargado!");
            FacesMessage msg = new FacesMessage("Carga cancelada! Archivo no cargado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        logger.info("Archivo cargado!");
        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }



}
