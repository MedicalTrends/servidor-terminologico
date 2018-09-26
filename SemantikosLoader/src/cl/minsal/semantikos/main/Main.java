package cl.minsal.semantikos.main;

import cl.minsal.semantikos.core.loaders.BaseLoader;
import cl.minsal.semantikos.model.SMTKLoader;

/**
 * Created by des01c7 on 21-12-17.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        if(args.length == 0) {
            throw new Exception("Debe especificar archivo de entrada");
        }

        if(args.length > 1) {
            throw new Exception("Debe especificar solo un parametro");
        }

        // Capturar parametros de PARAMILS
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-file")) {
                BaseLoader.setDataFile(args[++i].toString());
            }
            else {
                throw new Exception("Parametro no valido. Especifique un parametro valido");
            }
        }

        SMTKLoader smtkLoader = new SMTKLoader();

        try {
            smtkLoader.processMaster();
            //smtkLoader.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
