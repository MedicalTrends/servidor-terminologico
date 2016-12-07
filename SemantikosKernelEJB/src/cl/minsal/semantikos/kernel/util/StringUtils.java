package cl.minsal.semantikos.kernel.util;

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
}
