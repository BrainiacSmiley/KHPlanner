/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import de.brainiac.kapihospital.khvalues.KHValues;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class KHPlanner extends JPanel implements ActionListener, ChangeListener {
    private RoomPanel active, planning;
    private AnalysisPanel analysis;
    private UpgradePanel upgrade;
    private JTabbedPane jTabbedPane1;
    private final JFileChooser jFileChooser;
    private final Object[] _DialogOptions;
    private File _ActualFile;
    private ResourceBundle _Captions;
    private boolean _jnlp;
    private FileContents fc;
    private KHPlannerMenuBar _KHPlannerMenuBar;
    private Locale _actualLanguage;

    public KHPlanner(boolean jnlp, Locale language) {
        _actualLanguage = language;
        _Captions = ResourceBundle.getBundle("de.brainiac.kapihospital.khplanner.prop.GUILabeling", _actualLanguage);
        initComponents();
        jFileChooser = new JFileChooser(java.lang.System.getProperties().getProperty("user.dir"));
        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setFileFilter(new FileNameExtensionFilter(_Captions.getString("description"), _Captions.getString("ending")));
        fc = null;
        _DialogOptions = new Object[]{_Captions.getString("yes"), _Captions.getString("no"), _Captions.getString("cancle")};
        _jnlp = jnlp;
        analysis.setCalculationValues(active.save(false), planning.save(false));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equalsIgnoreCase("PAToggle")) {
            JCheckBoxMenuItem eSource = (JCheckBoxMenuItem)e.getSource();
            active.setIsPA(eSource.isSelected(), true);
            planning.setIsPA(eSource.isSelected(), true);
            analysis.setIsPA(eSource.isSelected());
        } else if (actionCommand.equalsIgnoreCase("UsedRoomsToggle")) {
            JCheckBoxMenuItem eSource = (JCheckBoxMenuItem)e.getSource();
            active.setUseUsedRooms(eSource.isSelected(), true);
            planning.setUseUsedRooms(eSource.isSelected(), true);
        } else if (actionCommand.equalsIgnoreCase("UseJunkToggle")) {
            JCheckBoxMenuItem eSource = (JCheckBoxMenuItem)e.getSource();
            active.setUseJunk(eSource.isSelected(), true);
            planning.setUseJunk(eSource.isSelected(), true);
            analysis.setUseJunk(eSource.isSelected());
        } else if (actionCommand.equalsIgnoreCase("New")) {
            if (active.hasUnsavedChanges() || planning.hasUnsavedChanges()) {
                int returnVal = JOptionPane.showOptionDialog(this, "Möchten Sie Ihre Änderungen speichern?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, _DialogOptions, _DialogOptions[0]);
                if (returnVal == 0) {
                    saveFile(false);
                }
                if (returnVal == 0 || returnVal == 1) {
                    plannerReset();
                }
            } else {
                plannerReset();
            }
        } else if (actionCommand.equalsIgnoreCase("Load")) {
            if (active.hasUnsavedChanges() || planning.hasUnsavedChanges()) {
                int returnVal = JOptionPane.showOptionDialog(this, "Möchten Sie Ihre Änderungen speichern?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, _DialogOptions, _DialogOptions[0]);
                if (returnVal == 0) {
                    saveFile(false);
                }
                if (returnVal == 0 || returnVal == 1) {
                    openFile();
                }
            } else {
                openFile();
            }
        } else if (actionCommand.equalsIgnoreCase("Save")) {
            saveFile(false);
        } else if (actionCommand.equalsIgnoreCase("SaveAs")) {
            saveFile(true);
        } else if (actionCommand.equalsIgnoreCase("Exit")) {
            exit();
        } else if (actionCommand.equalsIgnoreCase("Transfer")) {
            planning.load(active.save(false));
            if (_KHPlannerMenuBar.getUseUsedRooms()) {
                planning.setUseUsedRooms(true, true);
            }
            jTabbedPane1.setSelectedIndex(1);
        } else if (actionCommand.equalsIgnoreCase("AllBeds")) {
            if (jTabbedPane1.getSelectedIndex() == 0) {
                active.installAllBeds();
            } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
                planning.installAllBeds();
            }
        } else if (actionCommand.equalsIgnoreCase("UpgradeBeds")) {
            if (jTabbedPane1.getSelectedIndex() == 0) {
                active.upgradeAllBeds();
            } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
                planning.upgradeAllBeds();
            }
        } else if (actionCommand.equalsIgnoreCase("UpgradeRooms")) {
            if (jTabbedPane1.getSelectedIndex() == 0) {
                active.upgradeAllRooms();
            } else if (this.jTabbedPane1.getSelectedIndex() == 1) {
                planning.upgradeAllRooms();
            }
        } else if (actionCommand.equalsIgnoreCase("SaveFloorsToImages")) {
            if (_ActualFile == null) {
                JOptionPane.showMessageDialog(this, "Bitte speichern sie ihr Krankenhaus erst ab.");
            } else {
                saveFloorsAsImages(false);
            }
        } else if (actionCommand.equalsIgnoreCase("SaveFloorsToImagesForum")) {
            if (_ActualFile == null) {
                JOptionPane.showMessageDialog(this, "Bitte speichern sie ihr Krankenhaus erst ab.");
            } else {
                saveFloorsAsImages(true);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (((JTabbedPane) e.getSource()).getSelectedIndex() == 2) {
            analysis.setCalculationValues(active.save(false), planning.save(false));
        }
        else if (((JTabbedPane) e.getSource()).getSelectedIndex() == 3) {
            upgrade.setCalculationValues(active.save(false));
        }
    }

    public void setMenuBar(KHPlannerMenuBar khplannerMenuBar) {
        _KHPlannerMenuBar = khplannerMenuBar;
    }

    public void exit() {
        if (active.hasUnsavedChanges() || planning.hasUnsavedChanges()) {
            int returnVal = JOptionPane.showOptionDialog(this, "Möchten Sie Ihre Änderungen speichern?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, _DialogOptions, _DialogOptions[0]);
            if (returnVal == 0) {
                saveFile(false);
            }
            if (returnVal == 0 || returnVal == 1) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private void initComponents() {
        jTabbedPane1 = new JTabbedPane();
        jTabbedPane1.addChangeListener(this);

        active = new RoomPanel(_actualLanguage);
        jTabbedPane1.addTab(_Captions.getString("tab1"), active);

        planning = new RoomPanel(_actualLanguage);
        jTabbedPane1.addTab(_Captions.getString("tab2"), planning);

        analysis = new AnalysisPanel(_actualLanguage);
        jTabbedPane1.addTab(_Captions.getString("tab3"), analysis);
        
        upgrade = new UpgradePanel(_actualLanguage);
        jTabbedPane1.addTab(_Captions.getString("tab4"), upgrade);

        add(jTabbedPane1);
    }

    private void openFile() {
        int returnVal;

        if (_jnlp) {
            FileOpenService fos = null;
            try {
                fos = (FileOpenService) ServiceManager.lookup("javax.jnlp.FileOpenService");
            } catch (UnavailableServiceException use) {
                fos = null;
                use.printStackTrace(System.out);
            }
            if (fos != null) {
                try {
                    fc = fos.openFileDialog(null, new String[]{"khp"});
                    if (fc != null) {
                        readFile(fc.getInputStream());
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.out);
                }
            }
        } else {
            returnVal = jFileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                _ActualFile = jFileChooser.getSelectedFile();
                try {
                    readFile(new FileInputStream(_ActualFile));
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace(System.out);
                }
            }
        }
    }

    private void readFile(InputStream fis) {
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof int[][]) {
                int[][] loadedPanelData = (int[][]) obj;
                active.load(loadedPanelData);
                analysis.setActiveCalculationValues(loadedPanelData);
            }
            obj = ois.readObject();
            if (obj instanceof int[][]) {
                int[][] loadedPanelData = (int[][]) obj;
                planning.load(loadedPanelData);
                analysis.setPlanningCalculationValues(loadedPanelData);
            }
            obj = ois.readObject();
            if (obj instanceof Boolean) {
                boolean paState = (Boolean) obj;
                _KHPlannerMenuBar.setIsPA(paState);
                active.setIsPA(paState, false);
                planning.setIsPA(paState, false);
            }
            obj = ois.readObject();
            if (obj instanceof Boolean) {
                boolean useUsedRoomsState = (Boolean) obj;
                _KHPlannerMenuBar.setUseUsedRooms(useUsedRoomsState);
                active.setUseUsedRooms(useUsedRoomsState, false);
                planning.setUseUsedRooms(useUsedRoomsState, false);
            }
            obj = ois.readObject();
            if (obj instanceof Boolean) {
                boolean useJunkState = (Boolean) obj;
                _KHPlannerMenuBar.setUseJunk(useJunkState);
                active.setUseJunk(useJunkState, false);
                planning.setUseJunk(useJunkState, false);
                analysis.setUseJunk(useJunkState);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.out);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.out);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.out);
                }
            }
        }
        jTabbedPane1.setSelectedIndex(0);
    }

    private void saveFile(boolean saveAs) {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        if (_ActualFile == null || saveAs) {
            int returnVal = jFileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                _ActualFile = jFileChooser.getSelectedFile();
                if (!_ActualFile.getName().contains(".khp")) {
                    _ActualFile = new File(_ActualFile.getParentFile(), _ActualFile.getName() + ".khp");
                }
            }
        }
        if (_ActualFile != null) {
            try {
                fos = new FileOutputStream(_ActualFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(active.save(true));
                oos.writeObject(planning.save(true));
                oos.writeObject(_KHPlannerMenuBar.getIsPA());
                oos.writeObject(_KHPlannerMenuBar.getUseUsedRooms());
                oos.writeObject(_KHPlannerMenuBar.getUseJunk());
            } catch (IOException e) {
            } finally {
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private void saveFloorsAsImages(boolean scaled) {
        for (int x = 0; x < KHValues.MAXFLOORS; x++) {
            int floor = x+1;
            File khpSaveFilePath = _ActualFile.getParentFile();
            String khpSaveFileName = _ActualFile.getName();
            String khpSaveFileNameWithoutEnding = khpSaveFileName.substring(0, khpSaveFileName.length()-4);
            String activeImageFileName = khpSaveFileNameWithoutEnding + "_Aktuell_Etage" + floor + ".jpg";
            String planningImageFileName = khpSaveFileNameWithoutEnding + "_Planung_Etage" + floor + ".jpg";
            String activeForumImageFileName = khpSaveFileNameWithoutEnding + "_Aktuell_Forum_Etage" + floor + ".jpg";
            String planningForumImageFileName = khpSaveFileNameWithoutEnding + "_Planung_Forum_Etage" + floor + ".jpg";
            
            try {
                if (scaled) {
                    ImageIO.write(getScaledInstance(active.getFloorImage(x), 227, 120, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true), "jpg", new File(khpSaveFilePath, activeForumImageFileName));
                    ImageIO.write(getScaledInstance(planning.getFloorImage(x), 227, 120, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true), "jpg", new File(khpSaveFilePath, planningForumImageFileName));
                } else {
                    ImageIO.write(active.getFloorImage(x), "png", new File(khpSaveFilePath, activeImageFileName));
                    ImageIO.write(planning.getFloorImage(x), "png", new File(khpSaveFilePath, planningImageFileName));
                }
            } catch (Throwable ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                Logger.getLogger(KHPlanner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void plannerReset() {
        _ActualFile = null;
        active.newHospital();
        planning.newHospital();
    }

    private BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage)img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }
        
        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}