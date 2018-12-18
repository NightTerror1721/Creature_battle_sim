/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Arrays;
import java.util.stream.IntStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.editor.utils.AttackTurnEditor;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class AttackEditor extends JDialog
{
    
    
    private AttackEditor(MainMenuEditor parent)
    {
        super(parent, false);
        initComponents();
        init();
        createNew();
    }
    public static final void open(MainMenuEditor parent) { new AttackEditor(parent).setVisible(true); }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        power.setModel(new DefaultComboBoxModel<>(generateIntegerRange(0, 255, 1)));
        pps.setModel(new DefaultComboBoxModel<>(generateIntegerRange(5, 40, 5)));
        precision.setModel(new DefaultComboBoxModel<>(generateIntegerRange(0, 100, 5)));
        priority.setModel(new DefaultComboBoxModel<>(generateIntegerRange(-7, 7, 1)));
        type.setModel(new DefaultComboBoxModel<>(Arrays.stream(ElementalType.getAllElementalTypes())
                .sorted((e0, e1) -> e0.getName().compareTo(e1.getName())).toArray(ElementalType[]::new)));
    }
    
    private void updateTitle(int id)
    {
        if(id < 0)
            setTitle("Editor de Ataques - NUEVO");
        else setTitle("Editor de Ataques - [" + id + "]");
    }
    
    public final void createNew()
    {
        name.setText("");
        power.setSelectedItem(0);
        pps.setSelectedItem(5);
        precision.setSelectedItem(100);
        priority.setSelectedItem(0);
        type.setSelectedItem(ElementalType.NORMAL);
        
        turnsPanel.removeAll();
        turnsPanel.add("Turno 1", new AttackTurnEditor(this));
        
        updateTitle(-1);
    }
    
    public static final Integer[] generateIntegerRange(int first, int last, int interval)
    {
        if(interval == 1)
            return IntStream.rangeClosed(first, last).boxed().toArray(Integer[]::new);
        int len = (last - first) / interval + 1;
        Integer[] array = new Integer[len];
        for(int i = 0, current = first; i < len; i++, current += interval)
            array[i] = current;
        return array;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        power = new javax.swing.JComboBox<>();
        type = new javax.swing.JComboBox<>();
        pps = new javax.swing.JComboBox<>();
        precision = new javax.swing.JComboBox<>();
        priority = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        turnsPanel = new javax.swing.JTabbedPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor de Ataques");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Características"));

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));

        power.setBorder(javax.swing.BorderFactory.createTitledBorder("Poder"));

        type.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));

        pps.setBorder(javax.swing.BorderFactory.createTitledBorder("PPs"));

        precision.setBorder(javax.swing.BorderFactory.createTitledBorder("Precisión"));

        priority.setBorder(javax.swing.BorderFactory.createTitledBorder("Prioridad"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(name, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(type, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(power, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pps, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(precision, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(priority, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(name)
                    .addComponent(power)
                    .addComponent(pps))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(type, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(precision)
                    .addComponent(priority))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Turnos"));

        jButton1.setText("Añadir Turno");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Eliminar Turno Actual");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(turnsPanel)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(turnsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jButton3.setText("Nuevo");

        jButton4.setText("Cargar");

        jButton5.setText("Guardar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int len = turnsPanel.getTabCount();
        AttackTurnEditor tab = new AttackTurnEditor(this);
        turnsPanel.add("Turno " + (len + 1), tab);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int len = turnsPanel.getTabCount();
        if(len < 2)
            return;
        int sel = turnsPanel.getSelectedIndex();
        if(sel < 0)
            return;
        turnsPanel.removeTabAt(sel);
        len--;
        for(;sel < len; sel++)
            turnsPanel.setTitleAt(sel, "Turno " + (sel + 1));
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField name;
    private javax.swing.JComboBox<Integer> power;
    private javax.swing.JComboBox<Integer> pps;
    private javax.swing.JComboBox<Integer> precision;
    private javax.swing.JComboBox<Integer> priority;
    private javax.swing.JTabbedPane turnsPanel;
    private javax.swing.JComboBox<ElementalType> type;
    // End of variables declaration//GEN-END:variables
}