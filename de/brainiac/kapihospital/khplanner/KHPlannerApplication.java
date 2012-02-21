/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khplanner;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JFrame;

public class KHPlannerApplication extends JFrame implements WindowListener {
    private KHPlanner _KHPlanner;
    private KHPlannerMenuBar _KHPlannerMenuBar;
    private ResourceBundle _Captions;
    private static Locale _actualLanguage;

    public KHPlannerApplication() {
        initComponents();
    }

    public static void main(String[] args) {
        setLanguage(args.length >= 2 ? args[1] : "de");
        KHPlannerApplication khplannerApplication = new KHPlannerApplication();
        khplannerApplication.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        _KHPlanner.exit();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private void initComponents() {
        _KHPlanner = new KHPlanner(false, _actualLanguage);
        _KHPlannerMenuBar = new KHPlannerMenuBar(_KHPlanner, _actualLanguage);
        _KHPlanner.setMenuBar(_KHPlannerMenuBar);
        _Captions = ResourceBundle.getBundle("de.brainiac.kapihospital.khplanner.prop.GUILabeling", Locale.getDefault());
        setTitle(_Captions.getString("title"));
        setJMenuBar(_KHPlannerMenuBar);
        getContentPane().add(_KHPlanner, BorderLayout.CENTER);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        pack();
    }

    private static void setLanguage(String language) {
        if (language.equalsIgnoreCase("en")) {
            _actualLanguage = Locale.ENGLISH;
        } else {
            _actualLanguage = Locale.GERMAN;
        }
    }
}