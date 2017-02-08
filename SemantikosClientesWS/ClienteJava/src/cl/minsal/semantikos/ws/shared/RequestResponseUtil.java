package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;

/**
 * @author Andrés Farías on 16-01-17.
 */
public class RequestResponseUtil {

    public void addAllRefsets(RespuestaRefSets finalResponse, RespuestaRefSets response) {

        RespuestaRefSets.Refsets refsets = response.getRefsets();
        if (refsets != null) {
            if (finalResponse.refsets == null){
                finalResponse.refsets = new RespuestaRefSets.Refsets();
                if (finalResponse.refsets.getRefset() == null){
                    finalResponse.refsets.refset = new ArrayList<>();
                }
            }
            finalResponse.refsets.getRefset().addAll(refsets.getRefset());
        }
    }
}
