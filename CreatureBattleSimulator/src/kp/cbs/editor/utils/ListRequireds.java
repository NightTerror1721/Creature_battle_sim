/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Asus
 */
public class ListRequireds extends JPanel
{
    private final JDialog parent;
    private final Consumer<String[]> listener;
    
    public ListRequireds(JDialog parent, Consumer<String[]> listener)
    {
        this.parent = Objects.requireNonNull(parent);
        this.listener = Objects.requireNonNull(listener);
        initComponents();
        init();
    }
    
    private void init()
    {
        list.setModel(new DefaultListModel<>());
        
        showBattle();
    }
    
    private DefaultListModel<BattleEntry> listModel() { return (DefaultListModel<BattleEntry>) list.getModel(); }
    
    private Stream<BattleEntry> battlesStream()
    {
        var model = listModel();
        return StreamSupport.stream(Spliterators.spliterator(model.elements().asIterator(), model.size(), Spliterator.ORDERED), false);
    }
    
    public final String[] getRequiredIds()
    {
        return battlesStream().map(BattleEntry::getText).toArray(String[]::new);
    }
    
    public final void fillRequiredIds(String[] battles)
    {
        var model = listModel();
        model.removeAllElements();
        model.addAll(Stream.of(battles).map(BattleEntry::new).collect(Collectors.toList()));
        showBattle();
    }
    
    public final void fillRequiredIds(Collection<String> battles)
    {
        var model = listModel();
        model.removeAllElements();
        model.addAll(battles.stream().map(BattleEntry::new).collect(Collectors.toList()));
        showBattle();
    }
    
    public final void clear()
    {
        name.setText("");
        listModel().clear();
    }
    
    private void showBattle()
    {
        var sel = list.getSelectedValue();
        if(sel == null)
        {
            name.setText("");
            
            name.setEnabled(false);
            //find.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
            remove.setEnabled(false);
        }
        else
        {
            var index = list.getSelectedIndex();
            var len = listModel().size();
            
            name.setText(sel.getText());
            
            name.setEnabled(true);
            //find.setEnabled(true);
            up.setEnabled(index > 0 && len > 1);
            down.setEnabled(index < len - 1 && len > 1);
            remove.setEnabled(true);
        }
    }
    
    private void storeName()
    {
        var sel = list.getSelectedValue();
        if(sel == null)
            return;
        
        sel.setText(name.getText());
        list.repaint();
        fireListener();
    }
    
    /*private void findName(BattleEntry entry)
    {
        var all = BattlePropertiesPool.getAllNames();
        var sel = entry.getText();
        
        var obj = JOptionPane.showInputDialog(parent, "¿Que batalla quieres poner?", "Selector de batallas", JOptionPane.QUESTION_MESSAGE, null, all, sel);
        if(obj == null || !(obj instanceof String))
            return;
        
        entry.setText(obj.toString());
    }*/
    
    private void fireListener()
    {
        listener.accept(getRequiredIds());
    }
    
    private static final class BattleEntry
    {
        private String battle = "";
        
        private BattleEntry() {}
        private BattleEntry(String battle) { setText(battle); }
        
        public final void setText(String battle) { this.battle = battle == null ? "" : battle; }
        public final String getText() { return battle; }
        
        @Override
        public final String toString() { return battle == null || battle.isBlank() ? "<Unnamed Battle>" : battle; }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        up = new javax.swing.JButton();
        down = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre de la Id"));
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 0.1;
        add(name, gridBagConstraints);

        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.7;
        add(jScrollPane1, gridBagConstraints);

        jButton2.setText("Añadir");
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
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(jButton2, gridBagConstraints);

        remove.setText("Quitar");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(remove, gridBagConstraints);

        up.setText("^");
        up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(up, gridBagConstraints);

        down.setText("v");
        down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(down, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void listValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listValueChanged
        showBattle();
    }//GEN-LAST:event_listValueChanged

    private void nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyReleased
        storeName();
    }//GEN-LAST:event_nameKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        var sel = new BattleEntry();
        listModel().addElement(sel);
        list.setSelectedValue(sel, true);
        fireListener();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed
        var index = list.getSelectedIndex();
        if(index < 0)
            return;
        
        var model = listModel();
        if(index > 0 && model.size() > 1)
        {
            var old = model.remove(index);
            model.add(index - 1, old);
            list.setSelectedValue(old, true);
            fireListener();
        }
    }//GEN-LAST:event_upActionPerformed

    private void downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downActionPerformed
        var index = list.getSelectedIndex();
        if(index < 0)
            return;
        
        var model = listModel();
        if(index < model.size() - 1 && model.size() > 1)
        {
            var old = model.remove(index);
            model.add(index + 1, old);
            list.setSelectedValue(old, true);
            fireListener();
        }
    }//GEN-LAST:event_downActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        var index = list.getSelectedIndex();
        if(index < 0)
            return;
        
        listModel().remove(index);
        showBattle();
        fireListener();
    }//GEN-LAST:event_removeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton down;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<BattleEntry> list;
    private javax.swing.JTextField name;
    private javax.swing.JButton remove;
    private javax.swing.JButton up;
    // End of variables declaration//GEN-END:variables
}
