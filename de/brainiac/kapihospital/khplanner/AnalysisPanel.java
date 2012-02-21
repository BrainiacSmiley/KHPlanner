/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import de.brainiac.kapihospital.khvalues.Room;
import de.brainiac.kapihospital.khvalues.KHValues;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.text.DecimalFormat;
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

public class AnalysisPanel extends JPanel {
    private Image _Background;
    private KHValues _KHValues;
    private int[][][] _rawValues;
    private ResourceBundle _Captions;
    private ResourceBundle _RoomCaptions;
    private ResourceBundle _GUICaptions;
    private boolean _IsPA, _UseJunk;
    private JEditorPane columnOnePane, columnTwoPane, columnThreePane;
    private JScrollPane columnOneScrollPane, columnTwoScrollPane, columnThreeScrollPane;
    private AlphaContainer columnOneAlphaContainer, columnTwoAlphaContainer, columnThreeAlphaContainer;

    public AnalysisPanel(Locale language) {
        _Captions = ResourceBundle.getBundle("de.brainiac.kapihospital.khplanner.prop.AnalysisLabeling", language);
        _RoomCaptions = ResourceBundle.getBundle("de.brainiac.kapihospital.khvalues.prop.KHValues", language);
        _GUICaptions = ResourceBundle.getBundle("de.brainiac.kapihospital.khplanner.prop.GUILabeling", language);
        try {
            _Background = ImageIO.read(getClass().getResource("/de/brainiac/kapihospital/khplanner/images/analysisPanel/background.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(AnalysisPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        _KHValues = new KHValues(language);
        _IsPA = true;
        _UseJunk = false;
        _rawValues = new int[2][][];
        initComponents();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(_Background, 0, 0, this);
    }

    private void initComponents() {
        setPreferredSize(new Dimension(_Background.getWidth(this), _Background.getHeight(this)));

        SpringLayout layout = new SpringLayout();
        setLayout(layout);
       
        columnOnePane = new JEditorPane();
        columnOnePane.setBackground(new Color(0,0,0,0));
        columnOnePane.setOpaque(false);
        columnOnePane.setEditable(false);
        columnOnePane.setContentType("text/html");
        columnOneScrollPane = new JScrollPane(new AlphaContainer(columnOnePane));
        columnOneScrollPane.setPreferredSize(new Dimension(290,600));
        columnOneScrollPane.setBackground(new Color(0,0,0,0));
        columnOneScrollPane.setOpaque(false);
        columnOneScrollPane.getViewport().setBackground(new Color(0,0,0,0));
        columnOneScrollPane.getViewport().setOpaque(false);
        columnOneScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        columnOneScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        columnOneAlphaContainer = new AlphaContainer(columnOneScrollPane);
        //Adjust constraints to set Position.
        layout.putConstraint(SpringLayout.WEST, columnOneAlphaContainer, 18, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, columnOneAlphaContainer, 18, SpringLayout.NORTH, this);
        add(columnOneAlphaContainer);

        columnTwoPane = new JEditorPane();
        columnTwoPane.setBackground(new Color(0,0,0,0));
        columnTwoPane.setOpaque(false);
        columnTwoPane.setEditable(false);
        columnTwoPane.setContentType("text/html");
        columnTwoScrollPane = new JScrollPane(new AlphaContainer(columnTwoPane));
        columnTwoScrollPane.setPreferredSize(new Dimension(290,600));
        columnTwoScrollPane.setBackground(new Color(0,0,0,0));
        columnTwoScrollPane.setOpaque(false);
        columnTwoScrollPane.getViewport().setBackground(new Color(0,0,0,0));
        columnTwoScrollPane.getViewport().setOpaque(false);
        columnTwoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        columnTwoScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        columnTwoAlphaContainer = new AlphaContainer(columnTwoScrollPane);
        //Adjust constraints to set Position.
        layout.putConstraint(SpringLayout.WEST, columnTwoAlphaContainer, 313, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, columnTwoAlphaContainer, 18, SpringLayout.NORTH, this);
        add(columnTwoAlphaContainer);

        columnThreePane = new JEditorPane();
        columnThreePane.setBackground(new Color(0,0,0,0));
        columnThreePane.setOpaque(false);
        columnThreePane.setEditable(false);
        columnThreePane.setContentType("text/html");
        columnThreeScrollPane = new JScrollPane(new AlphaContainer(columnThreePane));
        columnThreeScrollPane.setPreferredSize(new Dimension(290,600));
        columnThreeScrollPane.setBackground(new Color(0,0,0,0));
        columnThreeScrollPane.setOpaque(false);
        columnThreeScrollPane.getViewport().setBackground(new Color(0,0,0,0));
        columnThreeScrollPane.getViewport().setOpaque(false);
        columnThreeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        columnThreeScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        columnThreeAlphaContainer = new AlphaContainer(columnThreeScrollPane);
        //Adjust constraints to set Position.
        layout.putConstraint(SpringLayout.WEST, columnThreeAlphaContainer, 608, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, columnThreeAlphaContainer, 18, SpringLayout.NORTH, this);
        add(columnThreeAlphaContainer);
    }

    public void setCalculationValues(int[][] activeValues, int[][] planningValues) {
        _rawValues = new int[2][][];
        _rawValues[0] = activeValues;
        _rawValues[1] = planningValues;
        calculateValues();
        repaint();
    }

    public void setActiveCalculationValues(int[][] activeValues) {
        _rawValues[0] = activeValues;
    }

    public void setPlanningCalculationValues(int[][] planningValues) {
        _rawValues[1] = planningValues;
    }

    public void setUseJunk(boolean b) {
        _UseJunk = b;
        calculateValues();
        repaint();
    }

    public void setIsPA(boolean b) {
        _IsPA = b;
        calculateValues();
        repaint();
    }

    private void calculateValues() {
        int[][][] countedBeds = new int[2][1][4];
        int[][][] countedTreatmentRooms = new int[2][KHValues.NUMBEROFTREATMENTROOMS][4];
        int[][][] countedCoinsRooms = new int[2][KHValues.NUMBEROFCOINROOMS][1];
        int[][][] countedDecoRooms = new int[2][KHValues.NUMBEROFDECOROOMS][1];
        int[][][] countedSpecialRooms = new int[2][KHValues.NUMBEROFSPECIALROOMS][4];
        int[] boughtRooms = new int[KHValues.NUMBEROFROOMS];
        double[][] invested = new double[1+KHValues.MAXFLOORS+1][2];
        double[][] needed = new double[1+KHValues.MAXFLOORS+1][2];
        double[] demolition = new double[KHValues.MAXFLOORS];
        int[] minLevelRequired = new int[] {0,0};
        int[] countedUpgradePoints = new int[2];
        int[] totalPatients = new int[2];
        DecimalFormat hTFormat = new DecimalFormat("#,##0.00 hT");
        DecimalFormat coinsFormat = new DecimalFormat("#,##0 Coins");
        
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 12 + KHValues.MAXFLOORS*153; y++) {
                int actualRoomID = _rawValues[x][y][0];
                int actualRoomUpgradeLevel = _rawValues[x][y][1];
                if (actualRoomID != -1) {
                    if (x == 0 && actualRoomID > -1) {
                        if (_KHValues.isRoomCoinsRoom(actualRoomID)) {
                            boughtRooms[actualRoomID]++;
                        }
                    }
                    Room actualRoom = _KHValues.getRoom(actualRoomID);
                    for (int i = 0; i < actualRoomUpgradeLevel; i++) {
                        actualRoom.upgradeRoom();
                    }
                    //LevelRquirement
                    if (minLevelRequired[x] < actualRoom.getLevelNeeded()) {
                        minLevelRequired[x] = actualRoom.getLevelNeeded();
                    }
                    //UpgradePoints
                    countedUpgradePoints[x] += actualRoom.getUpgradePoints();
                    //Invested
                    if (x == 0) {
                        if (y < 12) {
                            invested[0][0] += actualRoom.getBuildingCostsSumUpToLevel()[0];
                            invested[0][1] += actualRoom.getBuildingCostsSumUpToLevel()[1];
                        } else {
                            invested[((y-12)/153)+1][0] += actualRoom.getBuildingCostsSumUpToLevel()[0];
                            invested[((y-12)/153)+1][1] += actualRoom.getBuildingCostsSumUpToLevel()[1];
                        }
                    }
                    // <editor-fold defaultstate="collapsed" desc="switch">
                    switch (actualRoomID) {
                        case 4:
                        case 499999:
                            countedBeds[x][0][actualRoomUpgradeLevel]++;
                            break;
                        case 0:
                            countedTreatmentRooms[x][0][actualRoomUpgradeLevel]++;
                            break;
                        case 2:
                            countedTreatmentRooms[x][1][actualRoomUpgradeLevel]++;
                            break;
                        case 7:
                            countedTreatmentRooms[x][2][actualRoomUpgradeLevel]++;
                            break;
                        case 9:
                            countedTreatmentRooms[x][3][actualRoomUpgradeLevel]++;
                            break;
                        case 11:
                            countedTreatmentRooms[x][4][actualRoomUpgradeLevel]++;
                            break;
                        case 15:
                            countedTreatmentRooms[x][5][actualRoomUpgradeLevel]++;
                            break;
                        case 16:
                            countedTreatmentRooms[x][6][actualRoomUpgradeLevel]++;
                            break;
                        case 17:
                            countedTreatmentRooms[x][7][actualRoomUpgradeLevel]++;
                            break;
                        case 21:
                            countedTreatmentRooms[x][8][actualRoomUpgradeLevel]++;
                            break;
                        case 22:
                            countedTreatmentRooms[x][9][actualRoomUpgradeLevel]++;
                            break;
                        case 23:
                            countedTreatmentRooms[x][10][actualRoomUpgradeLevel]++;
                            break;
                        case 24:
                            countedTreatmentRooms[x][11][actualRoomUpgradeLevel]++;
                            break;
                        case 25:
                            countedTreatmentRooms[x][12][actualRoomUpgradeLevel]++;
                            break;
                        case 26:
                            countedTreatmentRooms[x][13][actualRoomUpgradeLevel]++;
                            break;
                        case 27:
                            countedCoinsRooms[x][0][0]++;
                            break;
                        case 10:
                            countedCoinsRooms[x][1][0]++;
                            break;
                        case 12:
                            countedCoinsRooms[x][2][0]++;
                            break;
                        case 14:
                            countedCoinsRooms[x][3][0]++;
                            break;
                        case 19:
                            countedCoinsRooms[x][4][0]++;
                            break;
                        case 29:
                            countedCoinsRooms[x][5][0]++;
                            break;
                        case 31:
                            countedCoinsRooms[x][6][0]++;
                            break;
                        case 33:
                            countedCoinsRooms[x][7][0]++;
                            break;
                        case 28:
                            countedDecoRooms[x][0][0]++;
                            break;
                        case 3:
                            countedDecoRooms[x][1][0]++;
                            break;
                        case 5:
                            countedDecoRooms[x][2][0]++;
                            break;
                        case 6:
                            countedDecoRooms[x][3][0]++;
                            break;
                        case 13:
                            countedDecoRooms[x][4][0]++;
                            break;
                        case 18:
                            countedDecoRooms[x][5][0]++;
                            break;
                        case 20:
                            countedDecoRooms[x][6][0]++;
                            break;
                        case 32:
                            countedDecoRooms[x][7][0]++;
                            break;
                        case 1:
                            countedSpecialRooms[x][0][0]++;
                            break;
                        case 8:
                            countedSpecialRooms[x][1][0]++;
                            break;
                        case 30:
                            countedSpecialRooms[x][2][actualRoomUpgradeLevel]++;
                            break;
                    }
                    // </editor-fold>
                }
                if (x == 1) {
                    int activeRoomID = _rawValues[0][y][0];
                    int activeRoomUpgradeLevel = _rawValues[0][y][1];
                    //actualRoom ist der gleiche wie activeRoom
                    if (actualRoomID == activeRoomID && actualRoomID > -1) {
                        Room activeRoom = _KHValues.getRoom(activeRoomID);
                        Room actualRoom = _KHValues.getRoom(actualRoomID);
                        for (int i = 0; i < actualRoomUpgradeLevel; i++) {
                            actualRoom.upgradeRoom();
                        }
                        for (int i = 0; i < activeRoomUpgradeLevel; i++) {
                            activeRoom.upgradeRoom();
                        }
                        if (actualRoomUpgradeLevel > activeRoomUpgradeLevel) {
                            if (y < 12) {
                                needed[0][0] += actualRoom.getBuildingCostsSumUpToLevel()[0]-activeRoom.getBuildingCostsSumUpToLevel()[0];
                                needed[0][1] += actualRoom.getBuildingCostsSumUpToLevel()[1]-activeRoom.getBuildingCostsSumUpToLevel()[1];
                            } else {
                                needed[((y-12)/153)+1][0] += actualRoom.getBuildingCostsSumUpToLevel()[0]-activeRoom.getBuildingCostsSumUpToLevel()[0];
                                needed[((y-12)/153)+1][1] += actualRoom.getBuildingCostsSumUpToLevel()[1]-activeRoom.getBuildingCostsSumUpToLevel()[1];
                            }
                        } else if (actualRoomUpgradeLevel < activeRoomUpgradeLevel) {
                            if (y > 11) {
                                //Demolition Costs
                                demolition[((y-12)/153)] += activeRoom.getDemolitionCosts();
                                needed[((y-12)/153)+1][0] += actualRoom.getBuildingCostsSumUpToLevel()[0];
                                needed[((y-12)/153)+1][1] += actualRoom.getBuildingCostsSumUpToLevel()[1];
                            }
                        } else {
                            if (_KHValues.isRoomCoinsRoom(actualRoomID)) {
                                if (boughtRooms[actualRoomID] > 0) {
                                    boughtRooms[actualRoomID]--;
                                } else {
                                    needed[((y-12)/153)+1][1] += actualRoom.getBuildingCostsSumUpToLevel()[1];
                                }
                            }
                        }
                    } else {
                        //Demolition Costs
                        if ((activeRoomID < -1 && _UseJunk) || activeRoomID > -1) {
                            Room activeRoom = _KHValues.getRoom(activeRoomID);
                            demolition[((y-12)/153)] += activeRoom.getDemolitionCosts();
                        }
                        //Building cost for actual Room
                        if (actualRoomID > -1) {
                            Room actualRoom = _KHValues.getRoom(actualRoomID);
                            for (int i = 0; i < actualRoomUpgradeLevel; i++) {
                                actualRoom.upgradeRoom();
                            }
                            if (y < 12) {
                                needed[0][0] += actualRoom.getBuildingCostsSumUpToLevel()[0];
                                needed[0][1] += actualRoom.getBuildingCostsSumUpToLevel()[1];
                            } else {
                                //CheckforAlreadyBought Coins Rooms
                                double buildingCostshT = actualRoom.getBuildingCostsSumUpToLevel()[0];
                                double buildingCostsCoins = actualRoom.getBuildingCostsSumUpToLevel()[1];
                                int id = actualRoom.getID();
                                if (_KHValues.isRoomCoinsRoom(id)) {
                                    if (boughtRooms[id] > 0) {
                                        boughtRooms[id]--;
                                    } else {
                                        needed[((y-12)/153)+1][1] += buildingCostsCoins;
                                    }
                                    
                                } else {
                                    needed[((y-12)/153)+1][0] += buildingCostshT;
                                    needed[((y-12)/153)+1][1] += buildingCostsCoins;
                                }
                            }
                        }
                    }
                }
            }

            //calculation of #Patients
            totalPatients[x] = _KHValues.getPatientsPerLevel(minLevelRequired[x]) + (countedUpgradePoints[x] / 12);
        }
        //Car Costs
        invested[invested.length-1][0] = _KHValues.getVehicleCosts(_rawValues[0][12 + KHValues.MAXFLOORS*153][0])[0];
        invested[invested.length-1][1] = _KHValues.getVehicleCosts(_rawValues[0][12 + KHValues.MAXFLOORS*153][0])[1];
        double neededHT = _KHValues.getVehicleCosts(_rawValues[1][12 + KHValues.MAXFLOORS*153][0])[0] - invested[invested.length-1][0];
        double neededCoins = _KHValues.getVehicleCosts(_rawValues[1][12 + KHValues.MAXFLOORS*153][0])[1] - invested[invested.length-1][1];
        if (neededHT < 0.0) {
            needed[needed.length-1][0] = 0;
        } else {
            needed[needed.length-1][0] = neededHT;
        }
        if (neededCoins < 0.0) {
            needed[needed.length-1][1] = 0;
        } else {
            needed[needed.length-1][1] = neededCoins;
        }
        
        //First Bed is free
        invested[0][0] -= 150;
        //First Behandlungsraum is free
        if (_rawValues[0][105+12][0] == 0) {
            invested[1][0] -= 750;
        }
        //First Röntengraum is free
        for (int x = 12; x < 165; x++) {
            if (_rawValues[0][x][0] == 2) {
                invested[1][0] -= 900;
                break;
            }
        }
        //totalValues
        double[] totalInvested = get2DimArraySum(invested);
        double[] totalNeeded = get2DimArraySum(needed);
        //Vehicel Costen wieder rausrechnen
        totalNeeded[0] -= needed[needed.length-1][0];
        totalNeeded[1] -= needed[needed.length-1][1];
        double totalDemolition = getArraySum(demolition);
        double[] totalCosts;
        if (_IsPA) {
            totalCosts = get2DimArraySum(needed);
        } else {
            //Vehicle rausrechnen
            totalInvested[0] -= invested[invested.length-1][0];
            totalInvested[1] -= invested[invested.length-1][1];
            //letzte Etage rausrechnen
            totalInvested[0] -= invested[invested.length-2][0];
            totalInvested[1] -= invested[invested.length-2][1];
            totalNeeded[0] -= needed[needed.length-2][0];
            totalNeeded[1] -= needed[needed.length-2][1];
            totalDemolition -= demolition[demolition.length-1];
            totalCosts = totalNeeded;
        }
        //Demolition Costs draufrechnen
        totalCosts[0] += totalDemolition;

        //Fill Text into Columns
        // <editor-fold defaultstate="collapsed" desc="Column One">
        columnOnePane.setText(
                "<table cellspacing=\"0\" cellpadding=\"0\">"
                + "<tr>"
                + "<td width=\"120\"></td>"
                + "<td width=\"70\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _GUICaptions.getString("tab1") + "</font></td>"
                + "<td width=\"70\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _GUICaptions.getString("tab2") + "</font></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room4") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedBeds[0][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedBeds[1][0]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room0") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][0]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room2") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][1]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room7") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][2]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][2]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room9") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][3]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][3]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room11") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][4]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][4]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room15") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][5]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][5]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room16") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][6]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][6]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room17") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][7]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][7]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room21") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][8]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][8]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room22") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][9]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][9]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room23") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][10]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][10]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room24") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][11]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][11]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room25") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][12]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][12]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room26") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[0][13]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedTreatmentRooms[1][13]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("numberOfRooms") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSumString(countedTreatmentRooms[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSumString(countedTreatmentRooms[1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room27") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][0][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][0][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room10") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][1][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][1][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room12") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][2][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][2][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room14") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][3][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][3][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room19") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][4][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][4][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room29") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][5][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][5][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room31") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][6][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][6][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room33") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[0][7][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedCoinsRooms[1][7][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("numberOfCoinRooms") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSum(countedCoinsRooms[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSum(countedCoinsRooms[1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room28") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][0][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][0][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room3") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][1][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][1][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room5") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][2][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][2][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room6") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][3][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][3][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room13") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][4][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][4][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room18") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][5][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][5][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room20") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][6][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][6][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room32") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[0][7][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedDecoRooms[1][7][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("numberOfDecoration") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSum(countedDecoRooms[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSum(countedDecoRooms[1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room1") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedSpecialRooms[0][0][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedSpecialRooms[1][0][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room8") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedSpecialRooms[0][1][0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedSpecialRooms[1][1][0] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _RoomCaptions.getString("room30") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedSpecialRooms[0][2]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomCountedString(countedSpecialRooms[1][2]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("numberOfSpecialRooms") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSumString(countedSpecialRooms[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + getRoomSumString(countedSpecialRooms[1]) + "</td>"
                + "</tr>"
                + "</table>"
        );
        columnOnePane.setCaretPosition(0);
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Column Two">
        int numberOfFloors = 0;
        if (_IsPA) {
            numberOfFloors = KHValues.MAXFLOORS;
        } else {
            numberOfFloors = KHValues.MAXFLOORS-1;
        }
        String text = "<table cellspacing=\"0\" cellpadding=\"0\">"
                + "<tr>"
                + "<td colspan=\"3\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("beds") + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"120\"></td>"
                + "<td width=\"70\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("ht") + "</font></td>"
                + "<td width=\"70\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("coins") + "</font></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("invest") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(invested[0][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(invested[0][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("neededTotal") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(needed[0][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(needed[0][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>";
        for (int x = 0; x < numberOfFloors; x++) {
            int floor = x+1;
            text += "<tr>"
                + "<td colspan=\"3\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("level") + " " + floor + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td></td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("ht") + "</font></td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("coins") + "</font></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("invest") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(invested[1+x][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(invested[1+x][1]) + "</td>"
                + "<tr>";
            if (_UseJunk) {
                text += "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("demolitionplus") + "</td>";
            } else {
                text += "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("demolition") + "</td>";
            }
            text += "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(demolition[x]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">&nbsp;</td>"
                + "</tr>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("needed") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(needed[1+x][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(needed[1+x][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("neededTotal") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(needed[1+x][0]+demolition[x]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(needed[1+x][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>";
        }
        if (_IsPA) {
            text += "<tr>"
                + "<td colspan=\"3\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("vehicle") + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td></td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("ht") + "</font></td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("coins") + "</font></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("invest") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(invested[invested.length-1][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(invested[invested.length-1][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("neededTotal") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(needed[invested.length-1][0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(needed[invested.length-1][1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\"><hr></td>"
                + "</tr>";
        }
        text += "<tr>"
                + "<td colspan=\"3\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("alllevel") + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td></td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("ht") + "</font></td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _Captions.getString("coins") + "</font></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("invest") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(totalInvested[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(totalInvested[1]) + "</td>"
                + "</tr>"
                + "<tr>";
            if (_UseJunk) {
                text += "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("demolitionplus") + "</td>";
            } else {
                text += "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("demolition") + "</td>";
            }
            text += "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(totalDemolition) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">&nbsp;</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("needed") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(totalNeeded[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(totalNeeded[1]) + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-face:Verdana\">" + _Captions.getString("neededTotal") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + hTFormat.format(totalCosts[0]) + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-face:Verdana\">" + coinsFormat.format(totalCosts[1]) + "</td>"
                + "</tr>"
                + "</table>";
        columnTwoPane.setText(text);
        columnTwoPane.setCaretPosition(0);
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Column Three">
        columnThreePane.setText(
                "<table cellspacing=\"0\" cellpadding=\"0\">"
                + "<tr>"
                + "<td width=\"140\"></td>"
                + "<td width=\"50\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _GUICaptions.getString("tab1") + "</font></td>"
                + "<td width=\"50\" align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">" + _GUICaptions.getString("tab2") + "</font></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("doctorlevel") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + minLevelRequired[0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + minLevelRequired[1] +  "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("upgradePoints") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedUpgradePoints[0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + countedUpgradePoints[1] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + _Captions.getString("patients") + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + totalPatients[0] + "</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">" + totalPatients[1] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\">&nbsp;</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">Bonuspatienten</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">Kosten</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:bold;font-face:Verdana\">Patienten</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">Quests</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(kostenlos)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+8</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">jede weitere</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(1 Coin)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+1</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\">&nbsp;</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">Showbühne</td>"
                + "<td></td>"
                + "<td></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">- Claus, der Klown</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(300hT)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+1-2</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">- Der große Hobolski</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(2 Coins)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+3</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">- Drei Engel für Flauschling</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(5 Coins)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+10</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan=\"3\">&nbsp;</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">Dr. Knevel</td>"
                + "<td></td>"
                + "<td></td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">- Ein klitzekleines Alienschiff</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(1 Coins)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+1</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">- Eine kleine Flotte Alienschiffe</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(2 Coins)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+5</td>"
                + "</tr>"
                + "<tr>"
                + "<td style=\"font-size:9px;font-weight:normal;font-face:Verdana\">- Eine riesige Armada an Alienschiffen</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">(5 Coins)</td>"
                + "<td align=\"center\" style=\"font-size:9px;font-weight:normal;font-face:Verdana\">+10</td>"
                + "</tr>"
                + "</table>"
        );
        // </editor-fold>
    }

    private String getRoomCountedString(int[] countedValues) {
        int sum = 0;
        for (int x = 0; x < countedValues.length; x++) {
                sum += countedValues[x];
        }

        return sum + " (" + countedValues[0] + "/" + countedValues[1] + "/" + countedValues[2] + "/" + countedValues[3] + ")";
    }

    private String getRoomSumString(int[][] countedValues) {
        int[] sum = new int[4];
        int totalsum = 0;
        for (int x = 0; x < countedValues.length; x++) {
            for (int y = 0; y < countedValues[x].length; y++) {
                sum[y] += countedValues[x][y];
                totalsum += countedValues[x][y];
            }
        }

        return totalsum + " (" + sum[0] + "/" + sum[1] + "/" + sum[2] + "/" + sum[3] + ")";
    }

    private String getRoomSum(int[][] countedValues) {
        int sum = 0;
        
        for (int x = 0; x < countedValues.length; x++) {
            for (int y = 0; y < countedValues[x].length; y++) {
                sum += countedValues[x][y];
            }
        }
        
        return "" + sum;
    }

    private double[] get2DimArraySum(double[][] arrayToSum) {
        double sum[] = new double[2];
        for (int x = 0; x < arrayToSum.length; x++) {
            sum[0] += arrayToSum[x][0];
            sum[1] += arrayToSum[x][1];
        }
        return sum;
    }

    private double getArraySum(double[] arrayToSum) {
        double sum = 0.0;
        for (int x = 0; x < arrayToSum.length; x++) {
            sum += arrayToSum[x];
        }
        return sum;
    }
}