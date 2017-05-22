package cl.minsal.semantikos.snomed;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.SnomedCTSnapshotManager;
import cl.minsal.semantikos.model.snapshots.SnomedCTSnapshotUpdate;
import cl.minsal.semantikos.model.snomedct.*;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

@ManagedBean(name = "snapshotBean")
@ViewScoped
public class Snapshot {

    private String destination = "/home/des01c7/Documentos/temp/";

    private List<ConceptSCT> conceptSCTs;
    private List<DescriptionSCT> descriptionSCTs;
    private List<RelationshipSnapshotSCT> relationshipSCTs;
    private List<LanguageRefsetSCT> languageRefsetSCTs;
    private List<TransitiveSCT> transitiveSCTs;

    public SnomedCTSnapshotUpdate getSnomedCTSnapshotUpdate() {
        return snomedCTSnapshotUpdate;
    }

    public void setSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate) {
        this.snomedCTSnapshotUpdate = snomedCTSnapshotUpdate;
    }

    private SnomedCTSnapshotUpdate snomedCTSnapshotUpdate;
    private String conceptSnapshotPath;
    private String descriptionSnapshotPath;
    private String relationshipSnapshotPath;
    private String refsetSnapshotPath;
    private String transitiveSnapshotPath;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    //@EJB
    private SnomedCTSnapshotManager snomedCTSnapshotManager = (SnomedCTSnapshotManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTSnapshotManager.class);

    @PostConstruct
    public void init() {
        snomedCTSnapshotUpdate = new SnomedCTSnapshotUpdate();
        snomedCTSnapshotUpdate.setUser(authenticationBean.getLoggedUser());

    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public void uploadFileConcept(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            //conceptSnapshotPath = copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            snomedCTSnapshotUpdate.setConceptSnapshotPath(copyFile(event.getFile().getFileName(), event.getFile().getInputstream()));
            //chargeConcept(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileDescription(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            //descriptionSnapshotPath = copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            snomedCTSnapshotUpdate.setDescriptionSnapshotPath(copyFile(event.getFile().getFileName(), event.getFile().getInputstream()));
            //chargeDescription(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileRelationship(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            //relationshipSnapshotPath = copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            snomedCTSnapshotUpdate.setRelationshipSnapshotPath(copyFile(event.getFile().getFileName(), event.getFile().getInputstream()));
            //chargeRelationship(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileTransitive(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            //transitiveSnapshotPath = copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            snomedCTSnapshotUpdate.setTransitiveSnapshotPath(copyFile(event.getFile().getFileName(), event.getFile().getInputstream()));
            //chargeTransitive(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileLanguageRefSet(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            //refsetSnapshotPath = copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            snomedCTSnapshotUpdate.setRefsetSnapshotPath(copyFile(event.getFile().getFileName(), event.getFile().getInputstream()));
            //chargeLanguajeRefset(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String copyFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            return destination + fileName;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void chargeConcept(String dir) {
        String cadena;
        FileReader f = null;
        conceptSCTs = new ArrayList<>();
        try {
            f = new FileReader(dir);

            BufferedReader b = new BufferedReader(f);

            boolean first = true;
            while ((cadena = b.readLine()) != null) {

                if (!first) {
                    int i = 0;
                    long id = 0L;
                    String affectiveTime = "";
                    boolean active = false;
                    long moduleId = 0L;
                    long definitionStatusId = 0L;

                    for (String token : cadena.split("\t")) {
                        if (token.trim().length() != 0 && i == 0)
                            id = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 1)
                            affectiveTime = token;
                        if (token.trim().length() != 0 && i == 2)
                            active = (token.equals("1")) ? true : false;
                        if (token.trim().length() != 0 && i == 3)
                            moduleId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 4)
                            definitionStatusId = Long.valueOf(token);
                        ++i;
                    }
                    String time = affectiveTime.substring(0, 4) + "-" + affectiveTime.substring(4, 6) + "-" + affectiveTime.substring(6, 8) + " 00:00:00";
                    conceptSCTs.add(new ConceptSCT(id, Timestamp.valueOf(time), active, moduleId, definitionStatusId));
                } else {
                    first = false;
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void chargeDescription(String dir) {
        String cadena;
        FileReader f = null;
        descriptionSCTs = new ArrayList<>();
        try {
            f = new FileReader(dir);

            BufferedReader b = new BufferedReader(f);


            boolean first = true;
            while ((cadena = b.readLine()) != null) {

                if (!first) {
                    int i = 0;
                    long id = 0L;
                    String effectiveTime = null;
                    boolean active = false;
                    long moduleId = 0L;
                    long conceptId = 0L;
                    String languageCode = null;
                    long typeId = 0L;
                    String term = null;
                    long caseSignificanceId = 0L;

                    for (String token : cadena.split("\t")) {
                        if (token.trim().length() != 0 && i == 0)
                            id = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 1)
                            effectiveTime = token;
                        if (token.trim().length() != 0 && i == 2)
                            active = (token.equals("1")) ? true : false;
                        if (token.trim().length() != 0 && i == 3)
                            moduleId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 4)
                            conceptId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 5)
                            languageCode = token;
                        if (token.trim().length() != 0 && i == 6)
                            typeId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 7)
                            term = token;
                        if (token.trim().length() != 0 && i == 8)
                            caseSignificanceId = Long.valueOf(token);
                        ++i;
                    }
                    String time = effectiveTime.substring(0, 4) + "-" + effectiveTime.substring(4, 6) + "-" + effectiveTime.substring(6, 8) + " 00:00:00";

                    descriptionSCTs.add(new DescriptionSCT(id, DescriptionSCTType.valueOf(typeId), Timestamp.valueOf(time), active, moduleId, conceptId, languageCode, term, caseSignificanceId));

                } else {
                    first = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void chargeRelationship(String dir) {
        String cadena;
        FileReader f = null;
        relationshipSCTs = new ArrayList<>();
        try {
            f = new FileReader(dir);

            BufferedReader b = new BufferedReader(f);

            boolean first = true;
            while ((cadena = b.readLine()) != null) {

                if (!first) {
                    int i = 0;
                    long id = 0L;
                    String effectiveTime = null;
                    boolean active = false;
                    long moduleId = 0L;
                    long sourceId = 0L;
                    long destinationId = 0L;
                    long relationshipGroup = 0L;
                    long typeId = 0L;
                    long characteristicTypeId = 0L;
                    long modifierId = 0L;


                    for (String token : cadena.split("\t")) {
                        if (token.trim().length() != 0 && i == 0)
                            id = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 1)
                            effectiveTime = token;
                        if (token.trim().length() != 0 && i == 2)
                            active = (token.equals("1")) ? true : false;
                        if (token.trim().length() != 0 && i == 3)
                            moduleId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 4)
                            sourceId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 5)
                            destinationId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 6)
                            relationshipGroup = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 7)
                            typeId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 8)
                            characteristicTypeId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 8)
                            modifierId = Long.valueOf(token);
                        ++i;
                    }
                    String time = effectiveTime.substring(0, 4) + "-" + effectiveTime.substring(4, 6) + "-" + effectiveTime.substring(6, 8) + " 00:00:00";

                    relationshipSCTs.add(new RelationshipSnapshotSCT(id, Timestamp.valueOf(time), active, moduleId, sourceId, destinationId, relationshipGroup, typeId, characteristicTypeId, modifierId));
                } else {
                    first = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void chargeLanguajeRefset(String dir) {
        String cadena;
        FileReader f = null;
        languageRefsetSCTs = new ArrayList<>();
        try {
            f = new FileReader(dir);

            BufferedReader b = new BufferedReader(f);

            boolean first = true;
            while ((cadena = b.readLine()) != null) {

                if (!first) {
                    int i = 0;
                    String id = null;
                    String affectiveTime = null;
                    boolean active = false;
                    long moduleId = 0L;
                    long refsetId = 0L;
                    long referencedComponentId = 0L;
                    long acceptabilityId = 0L;

                    for (String token : cadena.split("\t")) {
                        if (token.trim().length() != 0 && i == 0)
                            id = token;
                        if (token.trim().length() != 0 && i == 1)
                            affectiveTime = token;
                        if (token.trim().length() != 0 && i == 2)
                            active = (token.equals("1")) ? true : false;
                        if (token.trim().length() != 0 && i == 3)
                            moduleId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 4)
                            refsetId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 5)
                            referencedComponentId = Long.valueOf(token);
                        if (token.trim().length() != 0 && i == 6)
                            acceptabilityId = Long.valueOf(token);

                        ++i;
                    }
                    String time = affectiveTime.substring(0, 4) + "-" + affectiveTime.substring(4, 6) + "-" + affectiveTime.substring(6, 8) + " 00:00:00";

                    //languageRefsetSCTs.add(new LanguageRefsetSCT(id, Timestamp.valueOf(time), active, moduleId, refsetId, referencedComponentId, acceptabilityId));
                } else {
                    first = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void chargeTransitive(String dir) {
        String cadena;
        FileReader f = null;
        transitiveSCTs = new ArrayList<>();
        try {
            f = new FileReader(dir);

            BufferedReader b = new BufferedReader(f);

            while ((cadena = b.readLine()) != null) {

                int i = 0;
                long parent = 0L;
                long child = 0L;

                for (String token : cadena.split(";")) {
                    if (token.trim().length() != 0 && i == 0)
                        parent = Long.valueOf(token.replace("\"", ""));
                    if (token.trim().length() != 0 && i == 1)
                        child = Long.valueOf(token.replace("\"", ""));

                    ++i;
                }
                transitiveSCTs.add(new TransitiveSCT(parent, child));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void chargeSNAPSHOT() {
        //if(!conceptSCTs.isEmpty() && !descriptionSCTs.isEmpty() && !relationshipSCTs.isEmpty() && !languageRefsetSCTs.isEmpty() && !transitiveSCTs.isEmpty()){
        //snomedCTManager.chargeSNAPSHOT(conceptSCTs,descriptionSCTs,relationshipSCTs,languageRefsetSCTs,transitiveSCTs);
            /*
for (ConceptSCT conceptSCT : conceptSCTs) {
            try {
                snomedCTManager.persistConceptSCT(conceptSCT);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
            for (DescriptionSCT descriptionSCT : descriptionSCTs) {
                try {
                    snomedCTManager.persistSnapshotDescriptionSCT(descriptionSCT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            for (DescriptionSCT descriptionSCT : descriptionSCTs) {
            try {
                snomedCTManager.persistSnapshotDescriptionSCT(descriptionSCT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

            for (RelationshipSnapshotSCT relationshipSCT : relationshipSCTs) {
                try {
                    snomedCTManager.persistSnapshotRelationshipSCT(relationshipSCT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (LanguageRefsetSCT languageRefsetSCT : languageRefsetSCTs) {

            try {
                snomedCTManager.persistSnapshotLanguageRefSetSCT(languageRefsetSCT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
            */

        /*boolean ok = true;
        List<RelationshipSnapshotSCT> relationshipSnapshotSCTListBlock = null;
        System.out.println( relationshipSCTs.size());
        int i=1;
        while (ok) {
            if (relationshipSnapshotSCTListBlock != null) {

                if (relationshipSCTs.size() >= (300000*i)) {
                    relationshipSnapshotSCTListBlock = relationshipSCTs.subList((300000*(i-1)), (300000*i));
                    snomedCTManager.persistSnapshotRelationshipSCT(relationshipSnapshotSCTListBlock);
                } else {
                    relationshipSnapshotSCTListBlock = relationshipSCTs.subList(relationshipSnapshotSCTListBlock.size(), relationshipSCTs.size());
                    snomedCTManager.persistSnapshotRelationshipSCT(relationshipSnapshotSCTListBlock);
                    ok = false;
                }
            } else {
                if (relationshipSCTs.size() >= 300000) {
                    relationshipSnapshotSCTListBlock = relationshipSCTs.subList(0, 300000);
                    snomedCTManager.persistSnapshotRelationshipSCT(relationshipSnapshotSCTListBlock);
                } else {
                    relationshipSnapshotSCTListBlock.subList(0, relationshipSCTs.size());
                    snomedCTManager.persistSnapshotRelationshipSCT(relationshipSnapshotSCTListBlock);
                    ok = false;
                }
            }
            i++;
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("bloque "+i);
        }

         boolean ok = true;
        List<ConceptSCT> conceptSnapshotSCTListBlock = null;
        int i=1;
        while (ok) {
            if (conceptSnapshotSCTListBlock != null) {

                if (conceptSCTs.size() >= (100000*i)) {
                    conceptSnapshotSCTListBlock = conceptSCTs.subList((100000*(i-1)), (100000*i));
                    snomedCTManager.persistConceptSCT(conceptSnapshotSCTListBlock);
                } else {
                    conceptSnapshotSCTListBlock = conceptSCTs.subList(conceptSnapshotSCTListBlock.size()+1, conceptSCTs.size());
                    snomedCTManager.persistConceptSCT(conceptSnapshotSCTListBlock);
                    ok = false;
                }
            } else {
                if (conceptSCTs.size() >= 100000) {
                    conceptSnapshotSCTListBlock = conceptSCTs.subList(0, 100000);
                    snomedCTManager.persistConceptSCT(conceptSnapshotSCTListBlock);
                } else {
                    conceptSnapshotSCTListBlock=conceptSCTs.subList(0, conceptSCTs.size());
                    snomedCTManager.persistConceptSCT(conceptSnapshotSCTListBlock);
                    ok = false;
                }
            }
            i++;

            System.out.println("bloque "+i);
        }
        */


        //snomedCTManager.persistConceptSCT(conceptSCTs);


        // }
    }

    public void updateSnapshotSCT() {

        RequestContext context = RequestContext.getCurrentInstance();

        long time_start, time_end;
        time_start = System.currentTimeMillis();
        snomedCTSnapshotUpdate.setDate(new Timestamp(currentTimeMillis()));
        snomedCTSnapshotUpdate.setRelease("1.0");
        snomedCTSnapshotManager.updateSnapshot(snomedCTSnapshotUpdate);
        time_end = System.currentTimeMillis();
        System.out.println("Tiempo: " + ((time_end - time_start) / 1000) + " segundos");
    }


}