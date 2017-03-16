package cl.minsal.semantikos.beans.snomed;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import java.io.*;
import java.nio.file.Files;

@ManagedBean(name = "snapshotBean")
public class Snapshot {

    private String destination="/home/des01c7/Documentos/temp/";

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");

        FacesContext.getCurrentInstance().addMessage(null, message);
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void copyFile(String fileName, InputStream in) {
        try {


            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}