package cl.minsal.semantikos.kernel.util;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringEscapeUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.DecimalFormat;
import java.text.Normalizer;
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
        //"{\"id\":197283,\"idSourceConcept\":80826,\"targetDTO\":{\"@type\":\"HelperTableRowDTO\",\"id\":23901,\"helperTableId\":16,\"creationUsername\":\"cargainicial@gmail.com\",\"creationDate\":\"2017-09-13T15:36:29.733000\",\"lastEditUsername\":\"cargainicial@gmail.com\",\"lastEditDate\":\"2017-09-13T15:36:29.733000\",\"valid\":1,\"validityUntil\":null,\"description\":\"F-20668/13\",\"cells\":[{\"id\":107981,\"rowId\":23901,\"columnId\":24,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"F-20668/13\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107982,\"rowId\":23901,\"columnId\":26,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"SANOFI-AVENTIS DE CHILE S.A.\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107983,\"rowId\":23901,\"columnId\":34,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"Establecimientos Tipo A Y Asistencial\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107984,\"rowId\":23901,\"columnId\":30,\"intValue\":null,\"floatValue\":null,\"stringValue\":null,\"dateValue\":\"2018-12-16T00:00:00\",\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107985,\"rowId\":23901,\"columnId\":51,\"intValue\":null,\"floatValue\":null,\"stringValue\":null,\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107986,\"rowId\":23901,\"columnId\":31,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"Importado Terminado con Reacondicionamiento Local\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107987,\"rowId\":23901,\"columnId\":27,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"Vigente con suspensión voluntaria de distribución\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null}nu0000,{\"id\":107988,\"rowId\":23901,\"columnId\":52,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"26418\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107989,\"rowId\":23901,\"columnId\":32,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"Oral\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107990,\"rowId\":23901,\"columnId\":35,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"Tratamiento de los síntomas de la enfermedad depresiva de todos los tipos, incluyendo depresión reactiva y severa y depresión acompañada por ansiedad. Trastorno obsesivo compulsivo, trastorno de pánico, trastorno de ansiedad social, trastorno de ansiedad generalizada, trastorno estrés postraumático. \",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null}u0000iu0000du0000iu0000,{\"id\":107991,\"rowId\":23901,\"columnId\":29,\"intValue\":null,\"floatValue\":null,\"stringValue\":null,\"dateValue\":\"2013-12-16T00:00:00\",\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107992,\"rowId\":23901,\"columnId\":53,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"ZEQUIVEC COMPRIMIDOS RECUBIERTOS 20 mg\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107993,\"rowId\":23901,\"columnId\":25,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"EQUIVALENTE TERAPÉUTICO\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107994,\"rowId\":23901,\"columnId\":54,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"RF474397\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null},{\"id\":107995,\"rowId\":23901,\"columnId\":33,\"intValue\":null,\"floatValue\":null,\"stringValue\":\"Receta Médica\",\"dateValue\":null,\"booleanValue\":null,\"foreignKeyValue\":null}]},\"idRelationshipDefinition\":84,\"validityUntil\":null,\"creationDate\":\"2017-09-13T15:36:54.469000\",\"idRelationship\":null,\"relationshipAttributeDTOs\":null}"
        //json = CharMatcher.javaIsoControl().removeFrom(StringEscapeUtils.unescapeJava(json.replace("\\\\[\\\\]*","\\")));
        return json.replaceAll("\\\\[\\\\]*","").replaceAll("\\\\}","}").replaceAll("\\\"\\[","[").replaceAll("\\\\\\[","[").replaceAll("\\}]\"","}]").replaceAll("\\\"\\{","{").replaceAll("\\}\\\"","}");
        //return json;
    }
}

