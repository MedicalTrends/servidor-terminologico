package ws.minsal.semantikos.ws.utils;

import cl.minsal.semantikos.modelweb.Pair;
import org.apache.commons.codec.binary.Base64;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by des01c7 on 28-07-17.
 */
public class UtilsWS {

    public static Pair<String, String> getCredentialsFromWSContext(MessageContext mctx) throws Exception {
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


}
