package cl.minsal.semantikos;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.utils.ObjectSizeFetcher;
//import org.ehcache.sizeof.SizeOf;

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

    //private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    //private RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    static ConceptSMTK conceptSMTK;

    SMTKLoader smtkLoader;

    public LoaderView() throws InterruptedException {

        //conceptSMTK = conceptManager.getConceptByID(306551);
        //conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));

        smtkLoader = new SMTKLoader(textArea1, textArea2, textField1, textField2, textField3, textField4, progressBar1);

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

    public static void main(String[] args) throws InterruptedException {

        JFrame jFrame = new JFrame("Semantikos - Carga Inicial");

        double offset=0.595;
        jFrame.setSize(new Dimension((int)(0.3* Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int)(offset* Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        jFrame.setMinimumSize(new Dimension((int)(0.3* Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int)(offset* Toolkit.getDefaultToolkit().getScreenSize().getHeight())));

        LoaderView loaderView = new LoaderView();

        //SizeOf sizeOf = SizeOf.newInstance();

        //long deepSize = sizeOf.deepSizeOf(conceptSMTK);

        jFrame.setContentPane(loaderView.panelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}
