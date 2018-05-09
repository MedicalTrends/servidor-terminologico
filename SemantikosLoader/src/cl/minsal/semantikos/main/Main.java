package cl.minsal.semantikos.main;

import cl.minsal.semantikos.model.SMTKLoader;

/**
 * Created by des01c7 on 21-12-17.
 */
public class Main {

    public static void main(String[] args) {

        SMTKLoader smtkLoader = new SMTKLoader();

        try {
            smtkLoader.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
