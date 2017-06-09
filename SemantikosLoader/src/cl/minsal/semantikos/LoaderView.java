package cl.minsal.semantikos;

import cl.minsal.semantikos.model.SMTKLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    SMTKLoader smtkLoader = new SMTKLoader();


    public LoaderView() {
        iniciarCargaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Semantikos - Carga Inicial");

        jFrame.setContentPane(new LoaderView().panelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
