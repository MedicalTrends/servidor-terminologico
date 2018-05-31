package cl.minsal.semantikos.ws.utils;

import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.request.*;
import org.apache.commons.codec.binary.Base64;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by des01c7 on 28-07-17.
 */
public class UtilsWS {

    public static Pair<String, String> getCredentials(MessageContext mctx) throws Exception {

        Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);

        ArrayList list = (ArrayList) http_headers.get("Authorization");

        if (list == null || list.size() == 0) {
            throw new Exception("Error de autenticación: Este WS necesita Autenticación!");
        }

        String userpass = (String) list.get(0);
        userpass = userpass.substring(5);
        byte[] buf = new byte[0];

        buf = Base64.decodeBase64(userpass.getBytes());

        String credentials = new String(buf);

        String username = null;
        String password = null;
        int p = credentials.indexOf(":");

        if (p > -1) {
            username = credentials.substring(0, p);
            password = credentials.substring(p+1);
        }
        else {
            throw new Exception("Hubo un error al decodificar la autenticación");
        }

        return new Pair<>(username, password);
    }


    /**
     * Este método es responsable de validar que una petición posea al menos una categoría o un refset.
     *
     * @param request La petición enviada.
     * @throws cl.minsal.semantikos.modelws.fault.IllegalInputFault Se arroja si se viola la condición.
     */
    public static void validateAtLeastOneCategory(RequestableConceptsRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
    }

    public static void validateAtLeastOneCategoryOrOneRefSet(SimpleSearchTermRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames()) /*&& isEmpty(request.getRefSetNames())*/) {
            //throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet.");
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
        if (request.getTerm() == null || request.getTerm().trim().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un Termino a buscar");
        }
    }

    public static void validateAtLeastOneCategory(DescriptionsSuggestionsRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
    }

    public static boolean isEmpty(List<String> list) {
        return list.isEmpty() || (list.size() == 1 && list.contains(EMPTY_STRING));
    }

    public static void validate(SnomedSearchTermRequest request) throws IllegalInputFault {
        /* Se hace una validación de los parámetros */
        if(request.getTerm() == null || request.getTerm().isEmpty() || request.getTerm().length() < 3) {
            throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
        }
    }

    public static void validate(SnomedSuggestionsRequest request) throws IllegalInputFault {
        /* Se hace una validación de los parámetros */
        if(request.getTerm() == null || request.getTerm().isEmpty() || request.getTerm().length() < 3) {
            throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
        }
    }
}
