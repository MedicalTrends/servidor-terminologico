package cl.minsal.semantikos.main;

import cl.minsal.semantikos.model.SMTKLoader;

/**
 * Created by des01c7 on 21-12-17.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        SMTKLoader smtkLoader = new SMTKLoader();

        if(args.length < 2) {
            throw new Exception("Debe especificar archivo de entrada");
        }

        if(args.length > 2) {
            throw new Exception("Debe especificar solo un parametro. Si ha especificado solo un parametro, asegurese de que no contenga espacios");
        }

        // Capturar parametros de PARAMILS
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-file")) {
                smtkLoader.setIspPath(args[++i].toString());
            }
            else {
                throw new Exception("Parametro no valido. Especifique un parametro valido");
            }
        }

        try {
            smtkLoader.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
