/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Arrays;
import java.util.stream.IntStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.attack.AttackPool;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.editor.utils.AttackTurnEditor;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class AttackEditor extends JDialog
{
    private AttackModel loaded;
    
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
        loaded = null;
        name.setText("");
        power.setSelectedItem(0);
        pps.setSelectedItem(5);
        precision.setSelectedItem(100);
        priority.setSelectedItem(0);
        type.setSelectedItem(ElementalType.NORMAL);
        
        turnsPanel.removeAll();
        createNewTurnTab();
        
        updateTitle(-1);
    }
    
    private void load()
    {
        loaded = null;
        var allModels = AttackPool.getAllModels(true);
        if(allModels.isEmpty())
            return;
        allModels.sort((m0, m1) -> m0.toString().compareTo(m1.toString()));
        var sel = JOptionPane.showInputDialog(this, "¿Que ataque quieres cargar?",
                "Cargar Ataque", JOptionPane.QUESTION_MESSAGE, null,
                allModels.toArray(), allModels.get(0));
        if(sel == null)
            return;
        
        var model = (AttackModel) sel;
        createNew();
        expandModel(loaded = model);
        updateTitle(model.getId());
    }
    
    private void store()
    {
        var model = generateModel();
        if(loaded != null)
            model.setId(loaded.getId());
        if(AttackPool.registerNewOrUpdateModel(model, loaded == null))
        {
            loaded = model;
            updateTitle(model.getId());
            JOptionPane.showMessageDialog(this, "¡El ataque ha sido guardado con éxito!",
                    "Guardar Ataque", JOptionPane.INFORMATION_MESSAGE);
        }
        else JOptionPane.showMessageDialog(this, "Ha habido un fallo al guardar el ataque.",
                "Guardar Ataque", JOptionPane.ERROR_MESSAGE);
    }
    
    private AttackModel generateModel()
    {
        AttackModel model = new AttackModel();
        
        model.setName(name.getText().isBlank() ? "UNKNOWN" : name.getText());
        model.setPower(intValue(power));
        model.setMaxPP(intValue(pps));
        model.setPrecision(intValue(precision));
        model.setPriority(intValue(priority));
        model.setElementalType(value(type, ElementalType.UNKNOWN));
        
        int tabs = turnsPanel.getTabCount();
        model.setTurns(tabs);
        for(int i=0;i<tabs;i++)
        {
            AttackTurn turn = model.getTurn(i);
            ((AttackTurnEditor) turnsPanel.getComponentAt(i)).fillTurn(turn);
        }
        
        return model;
    }
    
    private void expandModel(AttackModel model)
    {
        name.setText(model.getName());
        power.setSelectedItem(model.getPower());
        pps.setSelectedItem(model.getMaxPP());
        precision.setSelectedItem(model.getPrecision());
        priority.setSelectedItem(model.getPriority());
        type.setSelectedItem(model.getElementalType());
        
        turnsPanel.removeAll();
        int len = model.getTurnCount();
        for(int i=0;i<len;i++)
        {
            AttackTurn turn = model.getTurn(i);
            AttackTurnEditor tab = createNewTurnTab();
            tab.expandTurn(turn);
        }
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
    
    private static int intValue(JComboBox<Integer> box)
    {
        try { return ((Number) box.getSelectedItem()).intValue(); }
        catch(Throwable ex) { return 0; }
    }
    
    private static <E> E value(JComboBox<E> box, E defaultVavlue)
    {
        try { return (E) box.getSelectedItem(); }
        catch(Throwable ex) { return defaultVavlue; }
    }
    
    private AttackTurnEditor createNewTurnTab()
    {
        int len = turnsPanel.getTabCount();
        AttackTurnEditor tab = new AttackTurnEditor(this);
        turnsPanel.add("Turno " + (len + 1), tab);
        return tab;
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
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cargar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Guardar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

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
        createNewTurnTab();
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        createNew();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        load();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        store();
    }//GEN-LAST:event_jButton5ActionPerformed


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
