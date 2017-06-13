package cl.minsal.semantikos;

import cl.minsal.semantikos.loaders.BasicConceptLoader;
import cl.minsal.semantikos.loaders.Initializer;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.users.User;
import javafx.scene.control.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 09-06-17.
 */
public class LoaderView {

    private JPanel panelMain;
    private JPanel panelHeader;
    private JPanel panelProgress;
    private JPanel panelLog;
    private JProgressBar progressBar1;
    private JTextArea textArea1;
    private JButton iniciarCargaButton;
    private JTextField textField1;
    private JTextField textField2;

    User user;
    SMTKLoader smtkLoader;
    Initializer initializer;
    BasicConceptLoader basicConceptLoader;

    public LoaderView() {
        user = new User();
        user.setEmail("carga_inicial@admin.cl");

        smtkLoader = new SMTKLoader();
        smtkLoader.setLogsComponent(textArea1);
        smtkLoader.setConceptsTotal(textField1);
        smtkLoader.setConceptsProcessed(textField2);

        iniciarCargaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    smtkLoader.execute();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //basicConceptLoader.processConcepts(smtkLoader);
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Semantikos - Carga Inicial");
        LoaderView loaderView = new LoaderView();
        double offset=0.895;
        jFrame.setSize(new Dimension((int)(0.7*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int)(offset*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        jFrame.setMinimumSize(new Dimension((int)(0.7*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int)(offset*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        jFrame.setContentPane(loaderView.panelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
