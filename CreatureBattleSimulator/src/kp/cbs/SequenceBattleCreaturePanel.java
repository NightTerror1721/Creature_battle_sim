/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.awt.Color;
import java.util.Objects;
import javax.swing.JPanel;
import kp.cbs.TeamManager.TeamSlot;
import kp.cbs.creature.CreatureViewer;

/**
 *
 * @author Asus
 */
public class SequenceBattleCreaturePanel extends JPanel
{
    private final SequencedBattleMenu parent;
    private final TeamManager team;
    private final TeamSlot slot;
    
    public SequenceBattleCreaturePanel(SequencedBattleMenu parent, TeamManager team, TeamSlot slot)
    {
        this.parent = Objects.requireNonNull(parent);
        this.team = Objects.requireNonNull(team);
        this.slot = Objects.requireNonNull(slot);
        initComponents();
        update();
    }
    
    public final void update()
    {
        var creature = team.getCreature(slot);
        if(creature == null)
        {
            name.setText("");
            level.setText("");
            race.setText("");
            clazz.setText("");
            lifebar.setMaximum(1);
            lifebar.setValue(0);
            lifebar.setForeground(Color.BLACK);
            lifePoints.setText("???");
            altered.setText("");
            
            name.setEnabled(false);
            level.setEnabled(false);
            race.setEnabled(false);
            clazz.setEnabled(false);
            lifebar.setEnabled(false);
            lifePoints.setEnabled(false);
            altered.setEnabled(false);
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
        }
        else
        {
            name.setText(creature.getName());
            level.setText(Integer.toString(creature.getLevel()));
            race.setText(creature.getRace().getName());
            clazz.setText(creature.getCreatureClass().getName());
            lifebar.setMaximum(creature.getMaxHealthPoints());
            lifebar.setValue(creature.getCurrentHealthPoints());
            lifebar.setForeground(computeHealthColor(lifebar.getValue(), lifebar.getMaximum()));
            lifePoints.setText(creature.getCurrentHealthPoints() + "/" + creature.getMaxHealthPoints());
            altered.setText(creature.getAlterationManager().getAbbreviatedInfo());
            
            name.setEnabled(true);
            level.setEnabled(true);
            race.setEnabled(true);
            clazz.setEnabled(true);
            lifebar.setEnabled(true);
            lifePoints.setEnabled(true);
            altered.setEnabled(true);
            jButton1.setEnabled(true);
            jButton2.setEnabled(true);
        }
    }
    
    private static Color computeHealthColor(float current, float max)
    {
        float per = current / max;
        if(per >= 0.5f)
        {
            int r = (int) (-(per - 1f) * 510);
            return new Color(r, 255, 0);
        }
        int g = (int) (per * 510);
        return new Color(255, g, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        name = new javax.swing.JTextField();
        level = new javax.swing.JTextField();
        race = new javax.swing.JTextField();
        clazz = new javax.swing.JTextField();
        lifePoints = new javax.swing.JLabel();
        altered = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lifebar = new javax.swing.JProgressBar();

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        setLayout(new java.awt.GridBagLayout());

        name.setEditable(false);
        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        add(name, gridBagConstraints);

        level.setEditable(false);
        level.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        level.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nivel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(level, gridBagConstraints);

        race.setEditable(false);
        race.setBorder(javax.swing.BorderFactory.createTitledBorder("Raza"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        add(race, gridBagConstraints);

        clazz.setEditable(false);
        clazz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clazz.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clase", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(clazz, gridBagConstraints);

        lifePoints.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lifePoints.setText("0/0");
        lifePoints.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        add(lifePoints, gridBagConstraints);

        altered.setText("???");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        add(altered, gridBagConstraints);

        jButton1.setText("Ver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        add(jButton1, gridBagConstraints);

        jButton2.setText("Usar Objeto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        add(jButton2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        add(lifebar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        var creature = team.getCreature(slot);
        if(creature == null)
            return;
        CreatureViewer.open(parent, creature, true);
        update();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        var creature = team.getCreature(slot);
        if(creature == null)
            return;
        
        var item = parent.getSelectedItem();
        if(item == null)
            return;
        
        item.use(creature);
        update();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel altered;
    private javax.swing.JTextField clazz;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField level;
    private javax.swing.JLabel lifePoints;
    private javax.swing.JProgressBar lifebar;
    private javax.swing.JTextField name;
    private javax.swing.JTextField race;
    // End of variables declaration//GEN-END:variables
}
