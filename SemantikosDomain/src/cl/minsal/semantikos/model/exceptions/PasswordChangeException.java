package cl.minsal.semantikos.model.exceptions;

import java.io.Serializable;

/**
 * Created by BluePrints Developer on 23-08-2016.
 */
public class PasswordChangeException extends Exception implements Serializable {


    public PasswordChangeException(String message){
        super(message);
    }

}
