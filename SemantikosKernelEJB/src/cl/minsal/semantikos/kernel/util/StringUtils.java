package cl.minsal.semantikos.kernel.util;

import org.apache.commons.lang.StringEscapeUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 11-07-16.
 *
 */
public class StringUtils {

    public static String underScoreToCamelCaseJSON(String json){

        Pattern p = Pattern.compile( "_([a-zA-Z])" );
        Matcher m = p.matcher( json );
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        return m.appendTail(sb).toString();
    }

    public static String camelCaseToUnderScore(String json){

        Pattern p = Pattern.compile( "([A-Z])" );
        Matcher m = p.matcher( json );
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "_"+m.group(1).toLowerCase());
        }
        return m.appendTail(sb).toString();
    }

    public static String[] camelCaseToUnderScore(String[] jsons){
        List<String> underScores = new ArrayList<>();

        for (String s : Arrays.asList(jsons)) {
            underScores.add(camelCaseToUnderScore(s));
        }

        String[] array = new String[underScores.size()];
        return underScores.toArray(array);
    }


    public static String toSQLLikePattern(String pattern) {
        StringBuilder res = new StringBuilder();

        for ( Integer i = 0; i < pattern.length(); ++i ) {
            Character c = pattern.charAt(i);
            if ( isAlphaNumeric(c) ) {
                res.append(c);
            } else {
                res.append("_");
            }
        }

        return res.toString();
    }

    private static Boolean isAlphaNumeric(Character c) {
        if ( c != null ) {
            Integer cVal = (int) c.charValue();
            if ((cVal >= 48 && cVal <= 57)
                    || (cVal >= 65 && cVal <= 90)
                    || (cVal >= 97 && cVal <= 122)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public static boolean validateRutFormat(String rut) {

        Pattern p = Pattern.compile( "^0*(\\d{1,3}(\\.?\\d{3})*)\\-?([\\dkK])$" );
        Matcher m = p.matcher( rut );

        if(!m.matches()) {
            return false;
        }

        return true;
    }

    public static boolean validateRutVerificationDigit(String rut) {

        rut = rut.replace("-","");
        rut = rut.replace(".","");
        rut = rut.toUpperCase();

        if(rut.length()<2) {
            return true;
        }

        if(rut.length()>10) {
            return false;
        }

        int num = Integer.parseInt(rut.substring(0,rut.length()-1));

        char dv = rut.charAt(rut.length()-1);

        if(dv == 'k') {
            dv = 'K';
        }

        if(!validarRut(num,dv)) {
            return false;
        }

        return true;
    }

    private static boolean validarRut(int rut, char dv)
    {
        int m = 0, s = 1;

        for (; rut != 0; rut /= 10)
        {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return dv == (char) (s != 0 ? s + 47 : 75);
    }

    public static String formatRut(String rut) {

        if(rut == null || rut.trim().isEmpty() || rut.trim().length() < 2) {
            return rut;
        }

        rut = rut.trim();
        rut = rut.replace("-","");
        rut = rut.replace(".","");

        long num = 0L;

        try {
            num = Long.parseLong(rut.substring(0,rut.length()-1));
        }
        catch (NumberFormatException e) {
            return rut;
        }

        DecimalFormat df = new DecimalFormat("###,###,###,###");

        //String fRut = String.format(new Locale("es-CL"),"%d", num);

        String fRut = df.format(num);

        fRut = fRut.concat("-");

        fRut = fRut.concat(rut.substring(rut.length()-1));

        return fRut;
    }

    public static String parseRut(String rut) {

        //rut = rut.replace("-","");
        rut = rut.replace(".","");
        rut = rut.toUpperCase();

        return rut;
    }

    public static boolean validatePasswordFormat(String password) {

        return password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$");

    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static String cleanJSON(String json){
        /*
        Pattern p = Pattern.compile( "([/]+)" );
        Matcher m = p.matcher( json );
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "/");
        }
        return m.appendTail(sb).toString();
        */
        //return json.replaceAll("\\\\[\\\\]+","\\\\").replaceAll("[a-zA-Z]]","]").replaceAll("\\\"}","}").replaceAll("\\\\}","}").replaceAll("\\\"\\[","[").replaceAll("\\\\\\[","[");


        //return json.replaceAll("\\\\[\\\\]*","").replaceAll("\\\"}","}").replaceAll("\\\\}","}").replaceAll("\\\"\\[","[").replaceAll("\\\\\\[","[").replaceAll("\\\"\\{","{").replaceAll("\\}\\\"","}");
        //"{,\"lastEditUsername\":\"Carga Inicial\",\"lastEditDate\":\"2017-04-07T11:06:24.984000\",\"valid\":1,\"validityUntil\":null,\"description\":\"es un[a],\"cells\":null},\"relationshipId\":122736}]}"
        //"{\"id\":276775,\"idSourceConcept\":67175,\"targetDTO\":{\"@type\":\"HelperTableRowDTO\",\"id\":14206,\"helperTableId\":14,\"creationUsername\":\"Carga Inicial\",\"creationDate\":\"2017-04-07T11:25:49.890000\",\"lastEditUsername\":\"Carga Inicial\",\"lastEditDate\":\"2017-04-07T11:25:49.890000\",\"valid\":1,\"validityUntil\":null,\"description\":\"clorfenamina\",\"cells\":[{\"id\":10795,\"rowId\":14206,\"columnId\":17,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"R06AB04\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":10796,\"rowId\":14206,\"columnId\":18,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"clorfenamina >> R06AB04 >> Alquilaminas substituidas >> ANTIHISTAMÍNICOS PARA USO SISTÉMICO >> ANTIHISTAMÍNICOS PARA USO SISTÉMICO >> SISTEMA RESPIRATORIO\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null}lu0000,u0000]\"},\"idRelationshipDefinition\":6,\"validityUntil\":null,\"creationDate\":\"2017-11-03T15:50:24.078000\",\"idRelationship\":null,\"relationshipAttributeDTOs\":null}"
        return StringEscapeUtils.unescapeJava(json).replaceAll("\\\\[\\\\]*","").replaceAll("\\\\}","}").replaceAll("\\\"\\[","[").replaceAll("\\\\\\[","[").replaceAll("\\}]\"","}]").replaceAll("\\\"\\{","{").replaceAll("\\}\\\"","}").replaceAll("[a-zA-Z|,]u0000","").replaceAll("}]\"}","}]}");
    }
}

