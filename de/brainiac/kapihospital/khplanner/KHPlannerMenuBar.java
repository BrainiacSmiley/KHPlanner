/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khplanner;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;

public class KHPlannerMenuBar extends JMenuBar {
    private JMenu jMenu1, jMenu2, jMenu3;
    private JMenuItem jMenuItem1, jMenuItem2, jMenuItem3, jMenuItem4, jMenuItem5, jMenuItem6, jMenuItem7, jMenuItem8, jMenuItem9, jMenuItem10, jMenuItem11;
    private JCheckBoxMenuItem jCheckBoxMenuItem1, jCheckBoxMenuItem2, jCheckBoxMenuItem3;
    private ResourceBundle _Captions;

    public KHPlannerMenuBar(KHPlanner gui, Locale language) {
        _Captions = ResourceBundle.getBundle("de.brainiac.kapihospital.khplanner.prop.GUILabeling", language);
        jMenu1 = new JMenu(_Captions.getString("jMenu1"));

        jMenuItem1 = new JMenuItem(_Captions.getString("jMenuItem1"));
        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setActionCommand("New");
        jMenuItem1.addActionListener(gui);
        jMenu1.add(jMenuItem1);

        jMenu1.add(new Separator());

        jMenuItem2 = new JMenuItem(_Captions.getString("jMenuItem2"));
        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setActionCommand("Load");
        jMenuItem2.addActionListener(gui);
        jMenu1.add(jMenuItem2);

        jMenuItem3 = new JMenuItem(_Captions.getString("jMenuItem3"));
        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setActionCommand("Save");
        jMenuItem3.addActionListener(gui);
        jMenu1.add(jMenuItem3);

        jMenuItem4 = new JMenuItem(_Captions.getString("jMenuItem4"));
        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jMenuItem4.setActionCommand("SaveAs");
        jMenuItem4.addActionListener(gui);
        jMenu1.add(jMenuItem4);

        jMenu1.add(new Separator());

        jMenuItem5 = new JMenuItem(_Captions.getString("jMenuItem5"));
        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setActionCommand("Exit");
        jMenuItem5.addActionListener(gui);
        jMenu1.add(jMenuItem5);

        add(jMenu1);

        jMenu2 = new JMenu(_Captions.getString("jMenu2"));

        jCheckBoxMenuItem1 = new JCheckBoxMenuItem(_Captions.getString("jCheckBoxMenuItem1"));
        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setActionCommand("PAToggle");
        jCheckBoxMenuItem1.addActionListener(gui);
        jMenu2.add(jCheckBoxMenuItem1);

        jCheckBoxMenuItem2 = new JCheckBoxMenuItem(_Captions.getString("jCheckBoxMenuItem2"));
        jCheckBoxMenuItem2.setSelected(true);
        jCheckBoxMenuItem2.setActionCommand("UsedRoomsToggle");
        jCheckBoxMenuItem2.addActionListener(gui);
        jMenu2.add(jCheckBoxMenuItem2);

        jCheckBoxMenuItem3 = new JCheckBoxMenuItem(_Captions.getString("jCheckBoxMenuItem3"));
        jCheckBoxMenuItem3.setActionCommand("UseJunkToggle");
        jCheckBoxMenuItem3.addActionListener(gui);
        jMenu2.add(jCheckBoxMenuItem3);

        jMenu2.add(new Separator());

        jMenuItem6 = new javax.swing.JMenuItem(_Captions.getString("jMenuItem6"));
        jMenuItem6.setActionCommand("Transfer");
        jMenuItem6.addActionListener(gui);
        jMenu2.add(jMenuItem6);

        jMenuItem7 = new javax.swing.JMenuItem(_Captions.getString("jMenuItem7"));
        jMenuItem7.setActionCommand("AllBeds");
        jMenuItem7.addActionListener(gui);
        jMenu2.add(jMenuItem7);

        jMenuItem8 = new javax.swing.JMenuItem(_Captions.getString("jMenuItem8"));
        jMenuItem8.setActionCommand("UpgradeBeds");
        jMenuItem8.addActionListener(gui);
        jMenu2.add(jMenuItem8);

        jMenuItem9 = new javax.swing.JMenuItem(_Captions.getString("jMenuItem9"));
        jMenuItem9.setActionCommand("UpgradeRooms");
        jMenuItem9.addActionListener(gui);
        jMenu2.add(jMenuItem9);

        add(jMenu2);
        
        jMenu3 = new JMenu(_Captions.getString("jMenu3"));
        
        jMenuItem10 = new JMenuItem(_Captions.getString("jMenuItem10"));
        jMenuItem10.setActionCommand("SaveFloorsToImages");
        jMenuItem10.addActionListener(gui);
        jMenu3.add(jMenuItem10);
        
        jMenuItem11 = new JMenuItem(_Captions.getString("jMenuItem11"));
        jMenuItem11.setActionCommand("SaveFloorsToImagesForum");
        jMenuItem11.addActionListener(gui);
        jMenu3.add(jMenuItem11);

        add(jMenu3);
    }

    public void setIsPA(boolean b) {
        jCheckBoxMenuItem1.setSelected(b);
    }

    public boolean getIsPA() {
        return jCheckBoxMenuItem1.isSelected();
    }

    public void setUseUsedRooms(boolean b) {
        jCheckBoxMenuItem2.setSelected(b);
    }

    public boolean getUseUsedRooms() {
        return jCheckBoxMenuItem2.isSelected();
    }

    public void setUseJunk(boolean b) {
        jCheckBoxMenuItem3.setSelected(b);
    }

    public boolean getUseJunk() {
        return jCheckBoxMenuItem3.isSelected();
    }
}