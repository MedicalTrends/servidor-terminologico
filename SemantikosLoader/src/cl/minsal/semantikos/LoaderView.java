package cl.minsal.semantikos;

import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by root on 09-06-17.
 */
public class LoaderView {

    private JPanel panelMain;
    private JPanel panelHeader;
    private JPanel panelProgress;
    private JPanel panelLogInfo;
    private JPanel panelLogError;
    private JProgressBar progressBar1;
    private JTextArea textArea1;
    private JButton iniciarCargaButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textArea2;
    private JTextField textField3;
    private JTextField textField4;

    SMTKLoader smtkLoader;

    public LoaderView() {

        smtkLoader = new SMTKLoader(textArea1, textArea2, textField1, textField2, textField3, textField4);

        //textArea1.setRows(10);

        iniciarCargaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    smtkLoader.execute();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Semantikos - Carga Inicial");
        LoaderView loaderView = new LoaderView();

        double offset=0.595;
        jFrame.setSize(new Dimension((int)(0.3*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int)(offset*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        jFrame.setMinimumSize(new Dimension((int)(0.3*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int)(offset*java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight())));

        jFrame.setContentPane(loaderView.panelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}
