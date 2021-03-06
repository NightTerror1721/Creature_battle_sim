/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.awt.Window;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import kp.cbs.creature.Creature;
import kp.cbs.creature.CreatureViewer;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class CreatureSelector extends JDialog
{
    private final PlayerGame game;
    private final boolean selection;
    private Creature option;
    
    private CreatureSelector(JFrame parent, PlayerGame game, List<Creature> creatures, boolean selection)
    {
        super(parent, true);
        this.game = Objects.requireNonNull(game);
        this.selection = selection;
        initComponents();
        init(creatures);
    }
    
    private CreatureSelector(JDialog parent, PlayerGame game, List<Creature> creatures, boolean selection)
    {
        super(parent, true);
        this.game = Objects.requireNonNull(game);
        this.selection = selection;
        initComponents();
        init(creatures);
    }
    
    private static CreatureSelector selector(Window parent, PlayerGame game, List<Creature> creatures, boolean selection)
    {
        if(parent instanceof JFrame)
            return new CreatureSelector((JFrame) parent, game, creatures, selection);
        else if(parent instanceof JDialog)
            return new CreatureSelector((JDialog) parent, game, creatures, selection);
        else return new CreatureSelector((JFrame) null, game, creatures, selection);
    }
    
    public static final Creature selection(Window parent, PlayerGame game, List<Creature> creatures)
    {
        if(creatures == null || creatures.isEmpty())
            return null;
        
        var selector = selector(parent, game, creatures, true);
        selector.setVisible(true);
        return selector.option;
    }
    public static final void viewer(Window parent, PlayerGame game)
    {
        var creatures = game.getAllCreatures();
        if(creatures == null || creatures.isEmpty())
            return;
        
        var selector = selector(parent, game, game.getAllCreatures(), false);
        selector.setVisible(true);
    }
    
    
    
    private void init(List<Creature> creatures)
    {
        setResizable(false);
        Utils.focus(this);
        
        select.setEnabled(selection);
        
        var model = new DefaultListModel<Creature>();
        model.addAll(creatures);
        all.setModel(model);
        
        showCreature();
    }
    
    private void showCreature()
    {
        var sel = all.getSelectedValue();
        boolean valid = sel != null;
        
        name.setEnabled(valid);
        race.setEnabled(valid);
        clazz.setEnabled(valid);
        level.setEnabled(valid);
        see.setEnabled(valid);
        learnAtts.setEnabled(valid);
        select.setEnabled(valid && selection);
        
        name.setText(valid ? sel.getName() : "");
        race.setText(valid ? sel.getRace().getName() : "");
        clazz.setText(valid ? sel.getCreatureClass().getName() : "");
        level.setText(valid ? Integer.toString(sel.getLevel()) : "");
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

        jScrollPane1 = new javax.swing.JScrollPane();
        all = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        race = new javax.swing.JTextField();
        clazz = new javax.swing.JTextField();
        level = new javax.swing.JTextField();
        see = new javax.swing.JButton();
        select = new javax.swing.JButton();
        learnAtts = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 2));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Luchadores"));

        all.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        all.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allMouseClicked(evt);
            }
        });
        all.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                allValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(all);

        getContentPane().add(jScrollPane1);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Propiedades del Luchador"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        name.setEditable(false);
        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(name, gridBagConstraints);

        race.setEditable(false);
        race.setBorder(javax.swing.BorderFactory.createTitledBorder("Raza"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(race, gridBagConstraints);

        clazz.setEditable(false);
        clazz.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clazz.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clase", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(clazz, gridBagConstraints);

        level.setEditable(false);
        level.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        level.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nivel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(level, gridBagConstraints);

        see.setText("Ver");
        see.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(see, gridBagConstraints);

        select.setText("Seleccionar");
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(select, gridBagConstraints);

        learnAtts.setText("Aprender Ataques");
        learnAtts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                learnAttsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(learnAtts, gridBagConstraints);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void allValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_allValueChanged
        showCreature();
    }//GEN-LAST:event_allValueChanged

    private void seeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seeActionPerformed
        var sel = all.getSelectedValue();
        if(sel == null)
            return;
        CreatureViewer.open(this, sel, true);
    }//GEN-LAST:event_seeActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        var sel = all.getSelectedValue();
        if(sel == null || !selection)
            return;
        option = sel;
        dispose();
    }//GEN-LAST:event_selectActionPerformed

    private void allMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allMouseClicked
        if(evt.getClickCount() != 2)
            return;
        
        var sel = all.getSelectedValue();
        if(sel == null || !selection)
            return;
        option = sel;
        dispose();
    }//GEN-LAST:event_allMouseClicked

    private void learnAttsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_learnAttsActionPerformed
        var sel = all.getSelectedValue();
        if(sel == null)
            return;
        ArtificialAttackLearn.open(this, game, sel);
    }//GEN-LAST:event_learnAttsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<Creature> all;
    private javax.swing.JTextField clazz;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton learnAtts;
    private javax.swing.JTextField level;
    private javax.swing.JTextField name;
    private javax.swing.JTextField race;
    private javax.swing.JButton see;
    private javax.swing.JButton select;
    // End of variables declaration//GEN-END:variables
}
