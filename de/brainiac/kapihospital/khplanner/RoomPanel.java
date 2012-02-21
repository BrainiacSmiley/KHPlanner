/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import de.brainiac.kapihospital.khvalues.KHValues;
import de.brainiac.kapihospital.khvalues.Room;
import de.brainiac.kapihospital.khvalues.Blueprint;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RoomPanel extends JPanel implements MouseListener, MouseMotionListener {
    private Image _Background;
    private Image _BuildingPlaceWrong;
    private Image _BuildingPlaceOK;
    private Image _Notepad;
    private Image _noNotepad;
    private Image _PAIcon;
    private Image _PACleaner;
    private Image _Cleaner;
    private Image _PowerNurse;
    private Image _Next, _Prev;
    private Image _BlueprintSelection;
    private Image[] _SelectedVehicle;
    private Image _StinkyPeters;
    private Image _PASecretary;
    private Image _Secretary;
    private Image _Doorman;
    private Image _Worker;
    private Image _Patientnurse;
    private Image[] _Patients;
    private Image _blueprintPremiumLock;
    private Image _blueprintRoomLock;
    private KHValues _KHValues;
    private Room _SelectedRoom;
    private Blueprint[] _BlueprintsToPaint;
    private int _mouseX, _mouseY;
    private int _actualGrid;
    private int _actualBlueprint, _selectedBlueprint;
    private int _actualLevel;
    private int _actualCarLevel;
    private int _RoomOffsetX, _RoomOffsetY;
    private int _BlueprintOffsetX, _BlueprintOffsetY;
    private Room[] _InstalledBeds;
    private int[][][] _InstalledGrid;
    private Room[][] _InstalledRooms;
    private boolean _IsPA, _UseUsedRooms, _UseJunk, _hasUnsavedChanges;
    private int _Index;
    private int[] _countedRooms;

    @SuppressWarnings("LeakingThisInConstructor")
    public RoomPanel(Locale language) {
        String path = "/de/brainiac/kapihospital/khplanner/images/roomPanel/";
        
        try {
            _Background = ImageIO.read(getClass().getResource(path+"background.jpg"));
            _BuildingPlaceWrong = new ImageIcon(getClass().getResource(path+"building_place_wrong.gif")).getImage();
            _BuildingPlaceOK = ImageIO.read(getClass().getResource(path+"building_place_ok.jpg"));
            _Notepad = new ImageIcon(getClass().getResource(path+"nurse2.1.gif")).getImage();
            _noNotepad = new ImageIcon(getClass().getResource(path+"nurse1.1.gif")).getImage();
            _PAIcon = new ImageIcon(getClass().getResource(path+"menu_premium.gif")).getImage();
            _PACleaner = new ImageIcon(getClass().getResource(path+"cleaner2.gif")).getImage();
            _Cleaner = new ImageIcon(getClass().getResource(path+"cleaner1.gif")).getImage();
            _PowerNurse = new ImageIcon(getClass().getResource(path+"powernurse.gif")).getImage();
            _Worker = new ImageIcon(getClass().getResource(path+"workerunit.gif")).getImage();
            _Next = ImageIO.read(getClass().getResource(path+"next.jpg"));
            _Prev = ImageIO.read(getClass().getResource(path+"prev.jpg"));
            _BlueprintSelection = ImageIO.read(getClass().getResource(path+"BlueprintSelection.png"));
            _SelectedVehicle = new Image[7];
            _SelectedVehicle[0] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle1.jpg"));
            _SelectedVehicle[1] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle2.jpg"));
            _SelectedVehicle[2] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle3.jpg"));
            _SelectedVehicle[3] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle4.jpg"));
            _SelectedVehicle[4] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle5.jpg"));
            _SelectedVehicle[5] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle6.jpg"));
            _SelectedVehicle[6] = ImageIO.read(getClass().getResource(path+"vehicle/vehicle7.jpg"));
            _StinkyPeters = new ImageIcon(getClass().getResource(path+"stinkypeters.gif")).getImage();
            _PASecretary = new ImageIcon(getClass().getResource(path+"medicalsecretary_1.gif")).getImage();
            _Secretary = new ImageIcon(getClass().getResource(path+"medicalsecretary_2.gif")).getImage();
            _Doorman = new ImageIcon(getClass().getResource(path+"doorman.gif")).getImage();
            _Patientnurse = new ImageIcon(getClass().getResource(path+"patientnurse.gif")).getImage();
            _Patients = new Image[6];
            _Patients[0] = new ImageIcon(getClass().getResource(path+"patients/patient1_u.gif")).getImage();
            _Patients[1] = new ImageIcon(getClass().getResource(path+"patients/patient2_u.gif")).getImage();
            _Patients[2] = new ImageIcon(getClass().getResource(path+"patients/patient3_u.gif")).getImage();
            _Patients[3] = new ImageIcon(getClass().getResource(path+"patients/patient4_u.gif")).getImage();
            _Patients[4] = new ImageIcon(getClass().getResource(path+"patients/patient5_u.gif")).getImage();
            _Patients[5] = new ImageIcon(getClass().getResource(path+"patients/patient6_u.gif")).getImage();
            _blueprintPremiumLock = ImageIO.read(getClass().getResource(path+"premiumlock.png"));
            _blueprintRoomLock = ImageIO.read(getClass().getResource(path+"roomlock.png"));
        } catch (IOException ex) {
            Logger.getLogger(AnalysisPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        setPreferredSize(new Dimension(_Background.getWidth(this), _Background.getHeight(this)));

        _KHValues = new KHValues(language);
        _Index = 0;
        getBluePrints();
        addMouseMotionListener(this);
        addMouseListener(this);
        _selectedBlueprint = -1;
        _RoomOffsetX = 39;
        _RoomOffsetY = 24;
        _BlueprintOffsetX = 765;
        _BlueprintOffsetY = 2;
        _IsPA = true;
        _UseUsedRooms = true;
        _UseJunk = false;
        _hasUnsavedChanges = false;
        newHospital();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(_Background, 0, 0, this);
        //PA Options
        if (_IsPA) {
            g.drawImage(_PAIcon, 2, 2, this);
            g.drawImage(_PACleaner, 320, 103, this);
            g.drawImage(_Notepad, 322, 0, this);
            g.drawImage(_PowerNurse, 322, 0, this);
            g.drawImage(_PASecretary, 26, 520, this);
            g.drawImage(_Doorman, 499, 520, this);
            if (_actualCarLevel > 0) {
                g.drawImage(_SelectedVehicle[_actualCarLevel-1], 557, 520, this);
            }
            g.drawImage(_Patients[5], 178, 588, this);
            g.drawImage(_Patients[5], 204, 588, this);
            g.drawImage(_Patients[5], 231, 588, this);
            g.drawImage(_Patients[5], 256, 588, this);
            g.drawImage(_Patients[5], 282, 588, this);
            g.drawImage(_Patients[5], 309, 588, this);
            g.drawImage(_Patients[5], 335, 588, this);
            g.drawImage(_Patients[5], 359, 588, this);
            g.drawImage(_Patients[5], 384, 588, this);
            g.drawImage(_Patients[5], 410, 588, this);
            g.drawImage(_Patients[5], 436, 588, this);
            g.drawImage(_Patients[5], 463, 588, this);
            g.drawImage(_Patients[5], 489, 588, this);
            g.drawImage(_Patients[5], 515, 588, this);
        } else {
            g.drawImage(_noNotepad, 322, 0, this);
            g.drawImage(_Cleaner, 320, 103, this);
            g.drawImage(_StinkyPeters, 304, 540, this);
            g.drawImage(_Secretary, 26, 520, this);
            g.drawImage(_Patients[0], 178, 588, this);
            g.drawImage(_Patients[1], 204, 588, this);
            g.drawImage(_Patients[2], 231, 588, this);
            g.drawImage(_Patients[3], 256, 588, this);
            /*g.drawImage(_Patients[4], 282, 588, this);
            g.drawImage(_Patients[0], 309, 588, this);
            g.drawImage(_Patients[1], 335, 588, this);
            g.drawImage(_Patients[2], 359, 588, this);*/
            g.drawImage(_Patients[3], 384, 588, this);
            g.drawImage(_Patients[4], 410, 588, this);
            g.drawImage(_Patients[0], 436, 588, this);
            g.drawImage(_Patients[1], 463, 588, this);
            g.drawImage(_Patients[2], 489, 588, this);
            g.drawImage(_Patients[3], 515, 588, this);
        }
        //Worker
        g.drawImage(_Worker, 379, 92, this);

        //levelSelector
        int availableFloors = 0;
        if (_IsPA) {
            availableFloors = KHValues.MAXFLOORS;
        } else {
            availableFloors = KHValues.MAXFLOORS-1;
        }
        for (int x = availableFloors; x > 0; x--) {
            g.drawImage(_KHValues.getFloorImage(x, _actualLevel == x-1), 2, 138+(KHValues.MAXFLOORS-x)*16, this);
        }

        //Blueprint selected
        if (_selectedBlueprint != -1) {
            g.drawImage(_BlueprintSelection, _BlueprintOffsetX + 97 * (_selectedBlueprint / 8), _BlueprintOffsetY + 70 * (_selectedBlueprint % 8), this);
        }
        //Blueprints
        for (int x = 0; x < _BlueprintsToPaint.length; x++) {
            g.drawImage(_BlueprintsToPaint[x].getImage(), _BlueprintOffsetX + 97 * (x / 8), _BlueprintOffsetY + 70 * (x % 8), this);
            int roomID = _BlueprintsToPaint[x].getID();
            //roomLock
            if (_KHValues.getNumberOfRoomsAllowed(roomID) == _countedRooms[roomID]) {
                g.drawImage(_blueprintRoomLock, _BlueprintOffsetX + 20 + 97 * (x / 8), _BlueprintOffsetY + 10 + 70 * (x % 8), this);
            }
            //premiumLock
            if (_KHValues.hasRoomPremiumLock(roomID) && !_IsPA) {
                g.drawImage(_blueprintPremiumLock, _BlueprintOffsetX + 97 * (x / 8), _BlueprintOffsetY + 70 * (x % 8), this);
            }
        }
        //Arrows
        if (_Index > 0) {
            g.drawImage(_Prev, 800, 612, this);
        }
        if (_KHValues.getNumberOfBlueprints() > _Index + 16) {
            g.drawImage(_Next, 897, 612, this);
        }

        g.drawImage(_Patientnurse, 319, 521, this);

        //Betten
        for (int x = 0; x < 6; x++) {
            if (_InstalledBeds[x] != null) {
                g.drawImage(_KHValues.getRoomImage(_InstalledBeds[x].getID(), _InstalledBeds[x].getUpgradeLevel(), _UseUsedRooms, false, false), _RoomOffsetX + 40 * x, _RoomOffsetY, this);
            }
        }
        for (int x = 6; x < 12; x++) {
            if (_InstalledBeds[x] != null) {
                g.drawImage(_KHValues.getRoomImage(_InstalledBeds[x].getID(), _InstalledBeds[x].getUpgradeLevel(), _UseUsedRooms, false, false), _RoomOffsetX + 40 * x + 200, _RoomOffsetY, this);
            }
        }

        //Räume
        for (int x = 0; x < 153; x++) {
            if (_InstalledRooms[_actualLevel][x] != null) {
                if (_InstalledRooms[_actualLevel][x].getID() < -1 && _UseJunk) {
                    g.drawImage(_KHValues.getRoomImage(_InstalledRooms[_actualLevel][x].getID(), _InstalledRooms[_actualLevel][x].getUpgradeLevel(), _UseUsedRooms, false, false), _RoomOffsetX + x % 17 * 40, _RoomOffsetY + 120 + x / 17 * 40, this);
                } else if (_InstalledRooms[_actualLevel][x].getID() >= 0) {
                    g.drawImage(_KHValues.getRoomImage(_InstalledRooms[_actualLevel][x].getID(), _InstalledRooms[_actualLevel][x].getUpgradeLevel(), _UseUsedRooms, false, false), _RoomOffsetX + x % 17 * 40, _RoomOffsetY + 120 + x / 17 * 40, this);
                }
            }
        }

        if (_SelectedRoom != null && _actualGrid != -1) {
            for (int x = 0; x < _SelectedRoom.getSize().width; x++) {
                for (int y = 0; y < _SelectedRoom.getSize().height; y++) {
                    g.drawImage(getBuildingPositionImage(isBuildingPositionOK()), _RoomOffsetX + _actualGrid % 17 * 40 + x * 40, _RoomOffsetY + _actualGrid / 17 * 40 + y * 40, this);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
            //Left Click
            //LevelSelection
            if (_mouseX >= 1 && _mouseX <= 17 && _mouseY >= 138 && _mouseY <= 138+(16*KHValues.MAXFLOORS)-1) {
                int clickedLevel = KHValues.MAXFLOORS-(_mouseY-138)/16;
                if (clickedLevel == KHValues.MAXFLOORS) {
                    if (_IsPA) {
                        changeActualFloor(clickedLevel);
                    }
                } else {
                    changeActualFloor(clickedLevel);
                }
            } //Worker
            else if (_mouseX >= 379 && _mouseX <= 463 && _mouseY >= 92 && _mouseY <= 133) {
                String message, title;
                if (_UseJunk && !this.isLevelEmpty(_UseJunk)) {
                    message = "Möchten sie diese Etage komplett entrümpeln?";
                    title = "Entrümpelung";
                } else {
                    message = "Möchten sie diese Etage komplett abreißen?";
                    title = "Abriß";
                }
                if (!isLevelEmpty(_UseJunk) || !isLevelEmpty(!_UseJunk)) {
                    int returnVal = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
                    if (returnVal == 0) {
                        cleanLevel(_UseJunk);
                    }
                }
            } //BlueprintSelection
            else if (_mouseX >= _BlueprintOffsetX && _mouseX <= _BlueprintOffsetX + 2 * 91 + 6 && _mouseY >= _BlueprintOffsetY && _mouseY <= _BlueprintOffsetY + 8 * 70) {
                if ((_actualBlueprint != _selectedBlueprint) && (_BlueprintsToPaint.length >= _actualBlueprint)) {
                    int roomID = _BlueprintsToPaint[_actualBlueprint].getID();
                    if (_countedRooms[roomID] == _KHValues.getNumberOfRoomsAllowed(roomID)) {
                        JOptionPane.showMessageDialog(this, "Sie können diesen Raum nicht mehr bauen. Sie haben schon die maximale Anzahl.");
                    } else if (_KHValues.hasRoomPremiumLock(roomID) && !_IsPA) {
                        JOptionPane.showMessageDialog(this, "Sie brauchen einen Premium Account für diesen Raum.");
                    }
                    else {
                        _selectedBlueprint = _actualBlueprint;
                        _SelectedRoom = _KHValues.getRoom(_BlueprintsToPaint[_selectedBlueprint].getID());
                        _actualGrid = -1;
                    }
                } else {
                    _selectedBlueprint = -1;
                    _SelectedRoom = null;
                }
                repaint();
            } //Left Arrows
            else if (_mouseX >= 800 && _mouseX <= 824 && _mouseY >= 612 && _mouseY <= 639) {
                if (_Index > 0) {
                    _Index -= 16;
                    _selectedBlueprint = -1;
                    _SelectedRoom = null;
                    getBluePrints();
                }
            } //Right Arrow
            else if (_mouseX >= 897 && _mouseX <= 921 && _mouseY >= 612 && _mouseY <= 639) {
                if (_KHValues.getNumberOfBlueprints() > _Index + 16) {
                    _Index += 16;
                    _selectedBlueprint = -1;
                    _SelectedRoom = null;
                    getBluePrints();
                }
            } //Grid
            else if (_mouseX >= 39 && _mouseX <= 718 && _mouseY >= 24 && _mouseY <= 503) {
                if (_SelectedRoom != null) {
                    if (isBuildingPositionOK()) {
                        if (_actualGrid > 50) {
                            installRoom(_actualGrid - 51, _SelectedRoom.getID());
                        } else {
                            installBed(_actualGrid);
                        }
                        repaint();
                    }
                }
            }
        } //Doppelklick
        else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            if (_mouseX >= 39 && _mouseX <= 718 && _mouseY >= 24 && _mouseY <= 503) {
                //Upgrade
                if (_actualGrid > 50) {
                    int roomFound = getRoomFromClick(_actualGrid - 51);
                    if ((roomFound != -1 && _KHValues.isFloorUpgradeable(_actualLevel)) || (!_KHValues.isFloorUpgradeable(_actualLevel) && _InstalledRooms[_actualLevel][roomFound].getID() == 30)) {
                        _InstalledRooms[_actualLevel][roomFound].upgradeRoom();
                        _hasUnsavedChanges = true;
                        repaint();
                    } else if (!_KHValues.isFloorUpgradeable(_actualLevel) && _InstalledRooms[_actualLevel][roomFound].getID() != 30) {
                        JOptionPane.showMessageDialog(this, "Floor not upgradeable");
                    }
                } else {
                    int bedFound = getBedFromClick(_actualGrid);
                    if (bedFound != -1) {
                        _InstalledBeds[bedFound].upgradeRoom();
                        _hasUnsavedChanges = true;
                        repaint();
                    }
                }
            } else if (_mouseX >= 557 && _mouseX <= 738 && _mouseY >= 520 && _mouseY <= 639) {
                    //vehicle
                    if (_actualCarLevel < 7) {
                        _actualCarLevel ++;
                        _hasUnsavedChanges = true;
                        repaint();
                    }
                }
        } //rechte MouseTaste
        else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
            if (_SelectedRoom != null) {
                _SelectedRoom = null;
                _selectedBlueprint = -1;
                repaint();
            } else {
                if (_mouseX >= 39 && _mouseX <= 718 && _mouseY >= 24 && _mouseY <= 503) {
                    //Delete
                    if (_actualGrid > 50) {
                        int roomFound = getRoomFromClick(_actualGrid - 51);
                        if (roomFound != -1) {
                            if (isRoomDowngradeable(roomFound)) {
                                _InstalledRooms[_actualLevel][roomFound].downgradeRoom();
                            } else {
                                removeRoom(roomFound);
                            }
                            _hasUnsavedChanges = true;
                            repaint();
                        }
                    } else {
                        int bedFound = getBedFromClick(_actualGrid);
                        if (bedFound != -1) {
                            if (isBedDowngradeable(bedFound)) {
                                _InstalledBeds[bedFound].downgradeRoom();
                            } else {
                                removeBed(bedFound);
                            }
                            _hasUnsavedChanges = true;
                            repaint();
                        }
                    }
                } else if (_mouseX >= 557 && _mouseX <= 738 && _mouseY >= 520 && _mouseY <= 639) {
                    //vehicle
                    if (_actualCarLevel > 0) {
                        _actualCarLevel --;
                        _hasUnsavedChanges = true;
                        repaint();
                    }
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        _mouseX = e.getX();
        _mouseY = e.getY();
        //blueprintGrid
        if (_mouseX >= _BlueprintOffsetX && _mouseX <= _BlueprintOffsetX + 2 * 91 + 6 && _mouseY >= _BlueprintOffsetY && _mouseY <= _BlueprintOffsetY + 8 * 70) {
            if (_BlueprintsToPaint.length > (_mouseY - _BlueprintOffsetY) / 70 + (_mouseX - _BlueprintOffsetX) / 94 * 8) {
                setActualBlueprint((_mouseY - _BlueprintOffsetY) / 70 + (_mouseX - _BlueprintOffsetX) / 94 * 8);
            }
        } else {
            setToolTipText("");
        }

        if (_mouseX >= _RoomOffsetX && _mouseX <= _RoomOffsetX + 17 * 40 - 1 && _mouseY >= _RoomOffsetY && _mouseY <= _RoomOffsetY + 12 * 40 - 1) {
            setActualGrid(((_mouseY - _RoomOffsetY) / 40) * 17 + (_mouseX - _RoomOffsetX) / 40);
        }
    }

    public void setIsPA(boolean b, boolean changed) {
        if (changed) {
            _hasUnsavedChanges = true;
        }
        _IsPA = b;
        repaint();
    }

    public void setUseUsedRooms(boolean b, boolean changed) {
        if (changed) {
            _hasUnsavedChanges = true;
        }
        _UseUsedRooms = b;
        for (int x = 0; x < _InstalledBeds.length; x++) {
            if (_InstalledBeds[x] != null) {
                _InstalledBeds[x].isUsed(b);
            }
        }
        for (int x = 0; x < _InstalledRooms.length; x++) {
            for (int y = 0; y < _InstalledRooms[x].length; y++) {
                if (_InstalledRooms[x][y] != null) {
                    _InstalledRooms[x][y].isUsed(b);
                }
            }
        }
        repaint();
    }

    public void setUseJunk(boolean b, boolean changed) {
        if (changed) {
            _UseJunk = b;
        }
        repaint();
    }

    public final void newHospital() {
        _countedRooms = new int[KHValues.NUMBEROFROOMS];
        initGrid();
        _actualLevel = 0;
        _actualCarLevel = 0;
        installBed(0);
        installRoom(105, 0);
        _hasUnsavedChanges = false;
        _KHValues.clearRoomImageList();
    }

    public int[][] save(boolean saved) {
        if (saved) {
            _hasUnsavedChanges = false;
        }
        int[][] saveValues = new int[12+KHValues.MAXFLOORS*153+1][2];

        for (int x = 0; x < 12; x++) {
            if (_InstalledBeds[x] != null) {
                if (x < 6) {
                    saveValues[0+x][0] = 4;
                } else {
                    saveValues[0+x][0] = 499999;                    
                }
                saveValues[0+x][1] = _InstalledBeds[x].getUpgradeLevel();
            } else {
                saveValues[0+x][0] = -1;
                saveValues[0+x][1] = -1;
            }
        }
        for (int x = 0; x < _InstalledRooms.length; x++) {
            for (int y = 0; y < _InstalledRooms[x].length; y++) {
                if (_InstalledRooms[x][y] != null) {
                    saveValues[12+x*153+y][0] = _InstalledRooms[x][y].getID();
                    saveValues[12+x*153+y][1] = _InstalledRooms[x][y].getUpgradeLevel();
                } else {
                    saveValues[12+x*153+y][0] = -1;
                    saveValues[12+x*153+y][1] = -1;
                }
            }
        }
        saveValues[12+KHValues.MAXFLOORS*153][0] = _actualCarLevel;

        return saveValues;
    }

    public void load(int[][] loadValues) {
        _countedRooms = new int[KHValues.NUMBEROFROOMS];
        int oldActualLevel = _actualLevel;
        int numberOfFloorsInSaveFile = (loadValues.length-13)/153;
        if (numberOfFloorsInSaveFile < KHValues.MAXFLOORS) {
            int[][] tempLoadValues = new int[12+KHValues.MAXFLOORS*153+1][2];
            for (int x = 0; x < tempLoadValues.length; x++) {
                for (int y = 0; y < tempLoadValues[x].length; y++) {
                    tempLoadValues[x][y] = -1;
                }
            }
            for (int x = 0; x < loadValues.length-1; x++) {
                System.arraycopy(loadValues[x], 0, tempLoadValues[x], 0, loadValues[x].length);
            }
            tempLoadValues[tempLoadValues.length-1][0] = loadValues[loadValues.length-1][0];
            tempLoadValues[tempLoadValues.length-1][1] = loadValues[loadValues.length-1][1];
            loadValues = tempLoadValues;
        }
        initGrid();
        //install beds from savefile
        for (int x = 0; x < 12; x++) {
            if (loadValues[x][0] != -1) {
                if (x < 6) {
                    installBed(x);
                } else {
                    installBed(x+5);
                }
                for (int y = 0; y < loadValues[x][1]; y++) {
                    _InstalledBeds[x].upgradeRoom();
                }
            }
        }
        //install Rooms from savefile
        for (int x = 12; x < KHValues.MAXFLOORS*153+12; x++) {
            _actualLevel = (x-12)/153;
            int grid = (x-12)%153;
            installRoom(grid, loadValues[x][0]);
            for (int z = 0; z < loadValues[x][1]; z++) {
                _InstalledRooms[_actualLevel][grid].upgradeRoom();
            }
        }
        //remove all old trash
        for (int grid = 12; grid < KHValues.MAXFLOORS*153+12; grid++) {
            int actualLevel = (grid-12)/153;
            int actualGrid = (grid-12)%153;
            if (_InstalledRooms[actualLevel][actualGrid] != null) {
                if (_InstalledRooms[actualLevel][actualGrid].getID() > -1) {
                    for (int x = 0; x < _InstalledRooms[actualLevel][actualGrid].getSize().height; x++) {
                        for (int y = 0; y < _InstalledRooms[actualLevel][actualGrid].getSize().width; y++) {
                            if (x > 0 || y > 0) {
                                _InstalledRooms[actualLevel][actualGrid+x*17+y] = null;
                                _InstalledGrid[actualLevel][(actualGrid / 17) + x][(actualGrid % 17) + y] = actualGrid;
                            }
                        }
                    }
                }
            }
        }

        _actualCarLevel = loadValues[loadValues.length-1][0];
        _hasUnsavedChanges = false;
        _KHValues.clearRoomImageList();
        _actualLevel = oldActualLevel;
        repaint();
    }

    public boolean hasUnsavedChanges() {
        return _hasUnsavedChanges;
    }

    public void installAllBeds() {
        for (int x = 0; x < 12; x++) {
            if(_InstalledBeds[x] == null) {
                int grid = x;
                if (x > 5)
                    grid += 5;
                installBed(grid);
            }
        }
        repaint();
    }

    public void upgradeAllBeds() {
        for (int x = 0; x < 12; x++) {
            if(_InstalledBeds[x] != null) {
                while (_InstalledBeds[x].isUpgradable()) {
                    _InstalledBeds[x].upgradeRoom();
                    _hasUnsavedChanges = true;
                }
            }
        }
        repaint();
    }

    public void upgradeAllRooms() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 153; y++) {
                if (_InstalledRooms[x][y] != null) {
                    while(_InstalledRooms[x][y].isUpgradable()) {
                        _InstalledRooms[x][y].upgradeRoom();
                        _hasUnsavedChanges = true;
                    }
                }
            }
        }
        repaint();
    }

    private void setActualBlueprint(int blueprint) {
        if (_actualBlueprint != blueprint) {
            _actualBlueprint = blueprint;
        }
        setToolTipText(_KHValues.getRoom(_BlueprintsToPaint[_actualBlueprint].getID()).getName());
        repaint();
    }

    private void setActualGrid(int grid) {
        if (_actualGrid != grid) {
            _actualGrid = grid;
            repaint();
        }
    }

    private boolean isBuildingPositionOK() {
        boolean positionOK = true;

        if (_SelectedRoom.getID() == 4) {
            if ((_actualGrid >= 0 && _actualGrid <= 5) || (_actualGrid >= 11 && _actualGrid <= 16)) {
                if (_actualGrid < 6) {
                    if (_InstalledBeds[_actualGrid] != null) {
                        positionOK = false;
                    }
                } else {
                    if (_InstalledBeds[_actualGrid - 5] != null) {
                        positionOK = false;
                    }
                }
            } else {
                positionOK = false;
            }
        } else {
            if ((_actualGrid < 51)
                    || ((_actualGrid % 17) + _SelectedRoom.getSize().width > 17)
                    || ((_actualGrid / 17) + _SelectedRoom.getSize().height > 12)) {
                positionOK = false;
            } else {
                int installedGrid = _actualGrid - 51;
                if (_UseJunk) {
                    //InstalledRoom
                    if (_InstalledRooms[_actualLevel][installedGrid] != null) {
                        positionOK = false;
                    } else {
                        for (int x = 0; x < _SelectedRoom.getSize().height; x++) {
                            for (int y = 0; y < _SelectedRoom.getSize().width; y++) {
                                if (_InstalledGrid[_actualLevel][(installedGrid / 17) + x][(installedGrid % 17) + y] != -1) {
                                    positionOK = false;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if (_InstalledRooms[_actualLevel][installedGrid] != null &&
                        _InstalledRooms[_actualLevel][installedGrid].getID() > -1) {
                        positionOK = false;
                    } else {
                        for (int x = 0; x < _SelectedRoom.getSize().height; x++) {
                            for (int y = 0; y < _SelectedRoom.getSize().width; y++) {
                                if (_InstalledGrid[_actualLevel][(installedGrid / 17) + x][(installedGrid % 17) + y] != ((installedGrid / 17) + x)*17+(installedGrid % 17) + y && _InstalledGrid[_actualLevel][(installedGrid / 17) + x][(installedGrid % 17) + y] != -1) {
                                    positionOK = false;
                                    break;
                                } else {
                                    if (_InstalledRooms[_actualLevel][((installedGrid / 17) + x)*17+(installedGrid % 17) + y] != null) {
                                        if (_InstalledRooms[_actualLevel][((installedGrid / 17) + x)*17+(installedGrid % 17) + y].getID() > -1) {
                                            positionOK = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return positionOK;
    }

    private Image getBuildingPositionImage(boolean b) {
        if (b) {
            return _BuildingPlaceOK;
        } else {
            return _BuildingPlaceWrong;
        }
    }

    private void getBluePrints() {
        _BlueprintsToPaint = _KHValues.getBluprints(_Index);
        repaint();
    }

    private void installRoom(int grid, int roomID) {
        _hasUnsavedChanges = true;
        _InstalledRooms[_actualLevel][grid] = _KHValues.getRoom(roomID);
        for (int x = 0; x < _InstalledRooms[_actualLevel][grid].getSize().height; x++) {
            for (int y = 0; y < _InstalledRooms[_actualLevel][grid].getSize().width; y++) {
                if (x > 0 || y > 0) {
                    _InstalledRooms[_actualLevel][grid+x*17+y] = null;
                }
            }
        }
        if (_UseUsedRooms) {
            _InstalledRooms[_actualLevel][grid].isUsed(true);
        }

        for (int x = 0; x < _InstalledRooms[_actualLevel][grid].getSize().height; x++) {
            for (int y = 0; y < _InstalledRooms[_actualLevel][grid].getSize().width; y++) {
                _InstalledGrid[_actualLevel][(grid / 17) + x][(grid % 17) + y] = grid;
            }
        }
        countRooms(roomID, 1);
    }

    private void removeRoom(int grid) {
        _hasUnsavedChanges = true;
        for (int x = 0; x < _InstalledRooms[_actualLevel][grid].getSize().height; x++) {
            for (int y = 0; y < _InstalledRooms[_actualLevel][grid].getSize().width; y++) {
                _InstalledGrid[_actualLevel][(grid / 17) + x][(grid % 17) + y] = -1;
                if (x > 0 || y > 0) {
                    _InstalledRooms[_actualLevel][(((grid / 17) + x)*17)+((grid % 17) + y)] = null;
                }
            }
        }
        countRooms(_InstalledRooms[_actualLevel][grid].getID(), -1);
        _InstalledRooms[_actualLevel][grid] = null;
    }

    private void installBed(int grid) {
        _hasUnsavedChanges = true;
        if (grid > 5) {
            grid = grid-5;
        }
        if (grid < 6) {
            _InstalledBeds[grid] = _KHValues.getRoom(4);
        } else {
            //499999 for right Beds
            _InstalledBeds[grid] = _KHValues.getRoom(499999);
        }
        if (_UseUsedRooms) {
            _InstalledBeds[grid].isUsed(true);
        }
        countRooms(4, 1);
    }

    private void removeBed(int grid) {
        _hasUnsavedChanges = true;
        if (grid == 0) {
            _InstalledBeds[grid] = _KHValues.getRoom(4);
        } else {
            _InstalledBeds[grid] = null;
            countRooms(4, -1);
        }
    }

    private int getRoomFromClick(int grid) {
        int roomFound = -1;
        if (_InstalledGrid[_actualLevel][grid/17][grid%17] != -1) {
            roomFound = _InstalledGrid[_actualLevel][grid/17][grid%17];
        }
        return roomFound;
    }

    private int getBedFromClick(int grid) {
        int bedFound = -1;
        if ((grid >= 0 && grid <= 5) || (grid >= 11 && grid <= 16)) {
            if (grid > 10) {
                grid = grid - 5;
            }

            if (_InstalledBeds[grid] != null) {
                bedFound = grid;
            }
        }
        return bedFound;
    }

    private boolean isRoomDowngradeable(int grid) {
        if (_InstalledRooms[_actualLevel][grid].getUpgradeLevel() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isBedDowngradeable(int bed) {
        if (_InstalledBeds[bed].getUpgradeLevel() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private void changeActualFloor(int clickedFloor) {
        if (_actualLevel != clickedFloor-1) {
            _actualLevel = clickedFloor-1;
            repaint();
        }
    }

    private void initGrid() {
        _InstalledBeds = new Room[12];
        _InstalledRooms = new Room[KHValues.MAXFLOORS][153];
        _InstalledGrid = new int[KHValues.MAXFLOORS][9][17];
        for (int x = 0; x < _InstalledGrid.length; x++) {
            for (int y = 0; y < _InstalledGrid[x].length; y++) {
                for (int z = 0; z < _InstalledGrid[x][y].length; z++) {
                    _InstalledGrid[x][y][z] = -1;
                }
            }
        }
        int[][][] junk = _KHValues.getJunk();
        for (int x = 0; x < 3; x++) {
            _actualLevel = x;
            for (int y = 0; y < 9; y++) {
                for (int z = 0; z < 17; z++) {
                    if (junk[x][y][z] != -1) {
                        installRoom(y*17+z, junk[x][y][z]);
                    }
                }
            }
        }        
    }

    public BufferedImage getFloorImage(int floor) {
        BufferedImage background = null;
        try {
            background = KHValues.toBufferedImage(ImageIO.read(getClass().getResource("/de/brainiac/kapihospital/khplanner/images/roomPanel/background.jpg")));
        } catch (IOException ex) {
            Logger.getLogger(RoomPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage floorImage = background.getSubimage(19, 136, 720, 381);
        int roomOffsetX = 20, roomOffsetY = 8;
        
        //Räume
        for (int x = 0; x < 153; x++) {
            if (_InstalledRooms[floor][x] != null) {
                if (_InstalledRooms[floor][x].getID() < -1 && _UseJunk) {
                    floorImage.getGraphics().drawImage(_KHValues.getRoomImage(_InstalledRooms[floor][x].getID(), _InstalledRooms[floor][x].getUpgradeLevel(), _UseUsedRooms, false, false), roomOffsetX + x % 17 * 40, roomOffsetY + x / 17 * 40, this);
                } else if (_InstalledRooms[floor][x].getID() >= 0) {
                    floorImage.getGraphics().drawImage(_KHValues.getRoomImage(_InstalledRooms[floor][x].getID(), _InstalledRooms[floor][x].getUpgradeLevel(), _UseUsedRooms, false, false), roomOffsetX + x % 17 * 40, roomOffsetY + x / 17 * 40, this);
                }
            }
        }
        return floorImage;
    }

    private void countRooms(int roomID, int direction) {
        if (roomID > -1) {
            _countedRooms[roomID] += 1*direction;
            if (_countedRooms[roomID] == _KHValues.getNumberOfRoomsAllowed(roomID)) {
                _selectedBlueprint = -1;
                _SelectedRoom = null;
            }
        }
    }

    private void cleanLevel(boolean useJunk) {
        for (int x = 0; x < _InstalledRooms[_actualLevel].length; x++) {
            if (_InstalledRooms[_actualLevel][x] != null) {
                if ((useJunk && _InstalledRooms[_actualLevel][x].getID() < -1) || (!useJunk && _InstalledRooms[_actualLevel][x].getID() > -1)) {
                    removeRoom(x);
                }
            }
        }
    }

    private boolean isLevelEmpty(boolean useJunk) {
        boolean isEmpty = true;
        for (int x = 0; x < _InstalledRooms[_actualLevel].length; x++) {
            if (_InstalledRooms[_actualLevel][x] != null) {
                if ((useJunk && _InstalledRooms[_actualLevel][x].getID() < -1) || (!useJunk && _InstalledRooms[_actualLevel][x].getID() > -1)) {
                    isEmpty = false;
                    break;
                }
            }
        }        
        return isEmpty;
    }
}