/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import de.brainiac.kapihospital.khvalues.KHValues;
import de.brainiac.kapihospital.khvalues.Room;
import de.brainiac.kapihospital.khvalues.UpgradeEvaluation;
import de.brainiac.kapihospital.khvalues.UpgradeEvaluationComparator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

public class UpgradePanel extends JPanel implements MouseListener {
    private Image _background;
    private KHValues _KHValues;
    private int[][] _rawValues;
    private UpgradeEvaluation _nextRoomToUpgrade;
    private List<UpgradeEvaluation> _allUpgradeOptions;
    private JEditorPane upgradePane;
    private JScrollPane myScrollPane;
    private AlphaContainer scrollAlphaContainer;
    private ResourceBundle _Captions;

    public UpgradePanel(Locale language) {
        _Captions = ResourceBundle.getBundle("de.brainiac.kapihospital.khplanner.prop.UpgradeLabeling", language);
        try {
            _background = ImageIO.read(getClass().getResource("/de/brainiac/kapihospital/khplanner/images/analysisPanel/background.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(AnalysisPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        _KHValues = new KHValues(language);
        _allUpgradeOptions = new ArrayList<UpgradeEvaluation>();
        initComponents();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(_background, 0, 0, this);
    }

    public void setCalculationValues(int[][] activeValues) {
        _rawValues = activeValues;
        calculateValues();
    }

    private void calculateValues() {
        DecimalFormat hTFormat = new DecimalFormat("#,##0.00 hT");
        _allUpgradeOptions.clear();

        for (int x = 0; x < 12 + KHValues.MAXFLOORS * 153; x++) {
            if (_rawValues[x][0] != -1) {
                Room RoomToCount = new Room();
                RoomToCount = _KHValues.getRoom(_rawValues[x][0]);
                for (int y = 0; y < _rawValues[x][1]; y++) {
                    RoomToCount.upgradeRoom();
                }
                UpgradeEvaluation roomToCountUpgradeEvaluation;
                roomToCountUpgradeEvaluation = _KHValues.getBestUpgradeOption(RoomToCount.getID(), RoomToCount.getUpgradeLevel());
                if (_nextRoomToUpgrade == null) {
                    if (roomToCountUpgradeEvaluation != null) {
                        _nextRoomToUpgrade = roomToCountUpgradeEvaluation;
                    }
                } else {
                    if (roomToCountUpgradeEvaluation != null) {
                        if (_nextRoomToUpgrade.getCostsPerPoint() > roomToCountUpgradeEvaluation.getCostsPerPoint()) {
                            _nextRoomToUpgrade = roomToCountUpgradeEvaluation;
                        }
                    }
                }
                List<UpgradeEvaluation> roomToCountAllUpgradeEvaluations = new ArrayList<UpgradeEvaluation>();
                roomToCountAllUpgradeEvaluations = _KHValues.getAllUpgradeOptions(RoomToCount.getID(), RoomToCount.getUpgradeLevel());
                for (int y = 0; y < roomToCountAllUpgradeEvaluations.size(); y++) {
                    _allUpgradeOptions.add( roomToCountAllUpgradeEvaluations.get(y));
                }
            }
        }
        Comparator<UpgradeEvaluation> comparator = new UpgradeEvaluationComparator();
        Collections.sort(_allUpgradeOptions, comparator);
        String text = "";
        if (_allUpgradeOptions.size() > 0) {
            int totalUpgradePoints = 0;
            double totalUpgradeCosts = 0;
            text += "<center><table>"
                    + "<tr cellspacing=\"0\" cellpadding=\"0\">"
                    + "<td colspan=\"6\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">Upgrade</td>"
                    + "<td width=\"20\">&nbsp;</td>"
                    + "<td colspan=\"2\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("sum") + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">#</td>"
                    + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("room") + "</td>"
                    + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("fromLevel") + "</td>"
                    + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("toLevel") + "</td>"
                    + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("cost") + "</td>"
                    + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">AP</td>"
                    + "<td width=\"30\">&nbsp;</td>"
                    + "<td width=\"40\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">AP</td>"
                    + "<td width=\"100\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">hT</td>"
                    + "</tr>";
            for (int x = 0; x < _allUpgradeOptions.size(); x++) {
                UpgradeEvaluation actualUpgradeOption = _allUpgradeOptions.get(x);
                int actualUpgradePoints = actualUpgradeOption.getUpgradePoints();
                double actualUpgradeCosts = actualUpgradeOption.getCosts();
                totalUpgradePoints += actualUpgradePoints;
                totalUpgradeCosts += actualUpgradeCosts;
                int zaehler = x+1;
                text += "<tr>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + zaehler + ".</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + actualUpgradeOption.getRoom() + "</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + actualUpgradeOption.getForWichLevel() + "</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + actualUpgradeOption.getToWichLevel() + "</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + hTFormat.format(actualUpgradeCosts) + "</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + actualUpgradePoints + "</td>"
                        + "<td>&nbsp;</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + totalUpgradePoints + "</td>"
                        + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + hTFormat.format(totalUpgradeCosts) + "</td>"
                        + "</tr>";
            }
            text += "</table></center>";
        } else {
            text = "<p align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">Sie haben alle Räume maximal ausgebaut.</p>";
        }
        upgradePane.setText(text);
        upgradePane.setCaretPosition(0);
    }

    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initComponents() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
       
        upgradePane = new JEditorPane();
        upgradePane.setBackground(new Color(0,0,0,0));
        upgradePane.setOpaque(false);
        upgradePane.setEditable(false);
        upgradePane.setContentType("text/html");
        myScrollPane = new JScrollPane(new AlphaContainer(upgradePane));
        myScrollPane.setPreferredSize(new Dimension(700,600));
        myScrollPane.setBackground(new Color(0,0,0,0));
        myScrollPane.setOpaque(false);
        myScrollPane.getViewport().setBackground(new Color(0,0,0,0));
        myScrollPane.getViewport().setOpaque(false);
        myScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        myScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollAlphaContainer = new AlphaContainer(myScrollPane);
        //Adjust constraints to set Position.
        layout.putConstraint(SpringLayout.WEST, scrollAlphaContainer, 98, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, scrollAlphaContainer, 18, SpringLayout.NORTH, this);
        add(scrollAlphaContainer);
    }
}