/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import java.awt.BorderLayout;
import java.util.Locale;
import javax.swing.JApplet;

public class KHPlannerApplet extends JApplet {
    private KHPlanner _KHPlanner;
    private KHPlannerMenuBar _KHPlannerMenuBar;
    private Locale _actualLanguage;

    @Override
    public void init() {
        setLanguage(getParameter("language") != null ? getParameter("language") : "de");
        _KHPlanner = new KHPlanner(false,_actualLanguage);
        _KHPlannerMenuBar = new KHPlannerMenuBar(_KHPlanner, _actualLanguage);
        _KHPlanner.setMenuBar(_KHPlannerMenuBar);

        setJMenuBar(_KHPlannerMenuBar);
        getContentPane().add(_KHPlanner, BorderLayout.CENTER);
    }

    @Override
    public void start() {
        setVisible(true);
    }

    @Override
    public void stop() {
        _KHPlanner.exit();
    }

    private void setLanguage(String language) {
        if (language.equalsIgnoreCase("en")) {
            _actualLanguage = Locale.ENGLISH;
        } else {
            _actualLanguage = Locale.GERMAN;
        }
    }
}