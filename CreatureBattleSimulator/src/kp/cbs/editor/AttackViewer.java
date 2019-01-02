/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackPool;
import kp.cbs.creature.attack.effects.DamageEffect.DamageType;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class AttackViewer extends JDialog
{
    private final DefaultListModel<AttackModel> models;
    
    private AttackViewer(MainMenuEditor parent)
    {
        super(parent, false);
        initComponents();
        this.attackList.setModel(models = new DefaultListModel<>());
        init();
    }
    public static final void open(MainMenuEditor parent)
    {
        AttackViewer viewer = new AttackViewer(parent);
        viewer.setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        
        f_type.setModel(newModel(ElementalType.getAllElementalTypes()));
        f_damType.setModel(newModel(DamageType.values()));
        f_minPower.setModel(newModel(0, 255));
        f_maxPower.setModel(newModel(0, 255));
        f_minPrecision.setModel(newModel(0, 255));
        f_maxPrecision.setModel(newModel(0, 255));
        
        
        fill();
    }
    
    private void fill()
    {
        var all = AttackPool.getAllModels(false).stream()
                .filter(this::applyFilters)
                .sorted((m0, m1) -> m0.toString().compareTo(m1.toString()))
                .collect(Collectors.toList());
        models.removeAllElements();
        models.addAll(all);
        
        setTitle("Visor de Ataques - Número de ataques encontrados: " + all.size());
    }
    
    private boolean applyFilters(AttackModel model)
    {
        return typeFilter(model) &&
               damTypeFilter(model) &&
               intFilter(f_minPower, model::getPower, true) &&
               intFilter(f_maxPower, model::getPower, false) &&
               intFilter(f_minPrecision, model::getPower, true) &&
               intFilter(f_maxPrecision, model::getPower, false);
    }
    
    private void showModel()
    {
        int sel = attackList.getSelectedIndex();
        if(sel < 0)
        {
            name.setText("");
            id.setText("");
            type.setText("");
            damType.setText("");
            power.setText("");
            precision.setText("");
            pps.setText("");
            priority.setText("");
            description.setText("");
            return;
        }
        
        AttackModel model = models.getElementAt(sel);
        name.setText(model.getName());
        id.setText(Integer.toString(model.getId()));
        type.setText(model.getElementalType().toString());
        damType.setText(model.getGeneralDamageType().toString());
        power.setText(Integer.toString(model.getPower()));
        precision.setText(model.getPrecision() <= 0 ? "Nunca falla" : Integer.toString(model.getPrecision()));
        pps.setText(Integer.toString(model.getMaxPP()));
        priority.setText(Integer.toString(model.getPriority()));
        description.setText(model.generateDescription());
    }
    
    private boolean typeFilter(AttackModel model)
    {
        int sel = f_type.getSelectedIndex();
        if(sel < 0)
            return true;
        
        var e = f_type.getItemAt(sel);
        return e.isAll() || model.getElementalType().equals(e.element);
    }
    
    private boolean damTypeFilter(AttackModel model)
    {
        int sel = f_damType.getSelectedIndex();
        if(sel < 0)
            return true;
        
        var e = f_damType.getItemAt(sel);
        return e.isAll() || model.getGeneralDamageType().equals(e.element);
    }
    
    private boolean intFilter(JComboBox<FilterElement<Integer>> box, IntSupplier getter, boolean min)
    {
        int sel = box.getSelectedIndex();
        if(sel < 0)
            return true;
        
        var e = box.getItemAt(sel);
        return e.isAll() || (min ? getter.getAsInt() >= e.element : getter.getAsInt() <= e.element);
    }
    
    private static <E> DefaultComboBoxModel<FilterElement<E>> newModel(E... base)
    {
        var list = Arrays.stream(base)
                .map(FilterElement::new)
                .collect(Collectors.toList());
        list.add(0, new FilterElement<>(null));
        return new DefaultComboBoxModel<>(list.toArray(FilterElement[]::new));
    }
    
    private static DefaultComboBoxModel<FilterElement<Integer>> newModel(int min, int max)
    {
        var list = IntStream.rangeClosed(min, max)
                .mapToObj(e -> new FilterElement<>(e))
                .collect(Collectors.toList());
        list.add(0, new FilterElement<>(null));
        return new DefaultComboBoxModel<>(list.toArray(FilterElement[]::new));
    }
    
    private static final class FilterElement<E>
    {
        private final E element;
        
        private FilterElement(E element) { this.element = element; }
        
        public final boolean isAll() { return element == null; }
        
        @Override
        public final String toString() { return element == null ? "Sin Filtro" : element.toString(); }
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
        attackList = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        id = new javax.swing.JTextField();
        type = new javax.swing.JTextField();
        power = new javax.swing.JTextField();
        precision = new javax.swing.JTextField();
        pps = new javax.swing.JTextField();
        priority = new javax.swing.JTextField();
        damType = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        f_type = new javax.swing.JComboBox<>();
        f_minPower = new javax.swing.JComboBox<>();
        f_minPrecision = new javax.swing.JComboBox<>();
        f_damType = new javax.swing.JComboBox<>();
        f_maxPower = new javax.swing.JComboBox<>();
        f_maxPrecision = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visor de Ataques");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Ataques"));
        jScrollPane1.setToolTipText("");

        attackList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        attackList.setToolTipText("");
        attackList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                attackListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(attackList);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Características del Ataque"));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        name.setEditable(false);
        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(name, gridBagConstraints);

        id.setEditable(false);
        id.setBorder(javax.swing.BorderFactory.createTitledBorder("ID"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(id, gridBagConstraints);

        type.setEditable(false);
        type.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(type, gridBagConstraints);

        power.setEditable(false);
        power.setBorder(javax.swing.BorderFactory.createTitledBorder("Poder"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(power, gridBagConstraints);

        precision.setEditable(false);
        precision.setBorder(javax.swing.BorderFactory.createTitledBorder("Precisión"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(precision, gridBagConstraints);

        pps.setEditable(false);
        pps.setBorder(javax.swing.BorderFactory.createTitledBorder("PPs"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(pps, gridBagConstraints);

        priority.setEditable(false);
        priority.setBorder(javax.swing.BorderFactory.createTitledBorder("Prioridad"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(priority, gridBagConstraints);

        damType.setEditable(false);
        damType.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Daño"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(damType, gridBagConstraints);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));

        description.setEditable(false);
        jScrollPane2.setViewportView(description);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setText("Actualizar Lista");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));
        jPanel3.setLayout(new java.awt.GridLayout(2, 3));

        f_type.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));
        f_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_typeActionPerformed(evt);
            }
        });
        jPanel3.add(f_type);

        f_minPower.setBorder(javax.swing.BorderFactory.createTitledBorder("Poder Mínimo"));
        f_minPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_minPowerActionPerformed(evt);
            }
        });
        jPanel3.add(f_minPower);

        f_minPrecision.setBorder(javax.swing.BorderFactory.createTitledBorder("Precisión Mínima"));
        f_minPrecision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_minPrecisionActionPerformed(evt);
            }
        });
        jPanel3.add(f_minPrecision);

        f_damType.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Daño"));
        f_damType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_damTypeActionPerformed(evt);
            }
        });
        jPanel3.add(f_damType);

        f_maxPower.setBorder(javax.swing.BorderFactory.createTitledBorder("Poder Máximo"));
        f_maxPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_maxPowerActionPerformed(evt);
            }
        });
        jPanel3.add(f_maxPower);

        f_maxPrecision.setBorder(javax.swing.BorderFactory.createTitledBorder("Precisión Máxima"));
        f_maxPrecision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_maxPrecisionActionPerformed(evt);
            }
        });
        jPanel3.add(f_maxPrecision);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void attackListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_attackListValueChanged
        showModel();
    }//GEN-LAST:event_attackListValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        fill();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void f_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_typeActionPerformed
        fill();
    }//GEN-LAST:event_f_typeActionPerformed

    private void f_damTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_damTypeActionPerformed
        fill();
    }//GEN-LAST:event_f_damTypeActionPerformed

    private void f_minPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_minPowerActionPerformed
        fill();
    }//GEN-LAST:event_f_minPowerActionPerformed

    private void f_maxPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_maxPowerActionPerformed
        fill();
    }//GEN-LAST:event_f_maxPowerActionPerformed

    private void f_minPrecisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_minPrecisionActionPerformed
        fill();
    }//GEN-LAST:event_f_minPrecisionActionPerformed

    private void f_maxPrecisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_maxPrecisionActionPerformed
        fill();
    }//GEN-LAST:event_f_maxPrecisionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<AttackModel> attackList;
    private javax.swing.JTextField damType;
    private javax.swing.JTextPane description;
    private javax.swing.JComboBox<FilterElement<DamageType>> f_damType;
    private javax.swing.JComboBox<FilterElement<Integer>> f_maxPower;
    private javax.swing.JComboBox<FilterElement<Integer>> f_maxPrecision;
    private javax.swing.JComboBox<FilterElement<Integer>> f_minPower;
    private javax.swing.JComboBox<FilterElement<Integer>> f_minPrecision;
    private javax.swing.JComboBox<FilterElement<ElementalType>> f_type;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField name;
    private javax.swing.JTextField power;
    private javax.swing.JTextField pps;
    private javax.swing.JTextField precision;
    private javax.swing.JTextField priority;
    private javax.swing.JTextField type;
    // End of variables declaration//GEN-END:variables
}
