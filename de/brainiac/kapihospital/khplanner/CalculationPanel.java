/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class CalculationPanel extends JPanel {
    private Image _Background;
    protected int[][][] _rawValues;

    public CalculationPanel() {
        try {
            _Background = ImageIO.read(getClass().getResource("/de/brainiac/kapihospital/khplanner/images/analysisPanel/background.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(AnalysisPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(_Background, 0, 0, this);
    }

    public void setCalculationValues(int[][] activeValues, int[][] planningValues) {
        _rawValues = new int[2][][];
        _rawValues[0] = activeValues;
        _rawValues[1] = planningValues;
        calculateValues();
    }

    public abstract void calculateValues();
}