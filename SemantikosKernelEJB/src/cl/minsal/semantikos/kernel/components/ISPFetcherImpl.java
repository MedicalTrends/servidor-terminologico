package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.users.Roles;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BluePrints Developer on 15-11-2016.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class ISPFetcherImpl implements ISPFetcher {

    @Override
    @PermitAll
    public Map<String,String> getISPData(String registro){

        Map<String,String> ret = new HashMap<String, String>();
        //String registro = "123C-254/15";
        Document doc = null;
        try {
            doc = Jsoup.connect("http://registrosanitario.ispch.gob.cl/Ficha.aspx?RegistroISP=" + registro).get();

            Elements filas = doc.select("#ctl00_ContentPlaceHolder1_cpProducto table tbody tr");

            for (Element e: filas) {

                String title = "";
                try{

                    title=e.select("strong").get(0).text();
                }
                catch (Exception ex){}


                String valor = "";
                try{
                    valor=e.childNode(5).childNode(1).childNode(0).toString();
                }catch(Exception ex){}

                ret.put(title ,valor);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (validar(ret))
            return ret;

        return new HashMap<String, String>();

    }

    private boolean validar(Map<String, String> ret) {

        for (String key : ret.keySet()) {
            String value =  ret.get(key);
            if(key != null && value !=null && value.trim().length()>1)
                return true;
        }

        return false;
    }
}
