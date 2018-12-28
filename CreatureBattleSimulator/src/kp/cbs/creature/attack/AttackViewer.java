/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Objects;
import javax.swing.JDialog;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class AttackViewer extends JDialog
{
    private Attack attack;
    
    private AttackViewer(JDialog parent, Attack attack)
    {
        super(parent, true);
        this.attack = Objects.requireNonNull(attack);
        initComponents();
        init();
    }
    
    public static final void open(JDialog parent, Attack attack)
    {
        new AttackViewer(parent, attack).setVisible(true);
    }
    
    private void init()
    {
        setResizable(true);
        Utils.focus(this);
        
        setTitle("Visor de Ataque - " + attack.getName());
        
        name.setText(attack.getName());
        type.setText(attack.getElementalType().getName());
        power.setText(Integer.toString(attack.getPower()));
        damType.setText(attack.getModel().getGeneralDamageType().toString());
        pps.setText(attack.getCurrentPP() + " / " + attack.getMaxPP());
        precision.setText(precision());
        priority.setText(Integer.toString(attack.getPriority()));
        
        description.setText(attack.getDescription());
    }
    
    private String precision()
    {
        int value = attack.getPrecision();
        return value < 1 ? "Nunca Falla" : value + "%";
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

        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        type = new javax.swing.JTextField();
        power = new javax.swing.JTextField();
        damType = new javax.swing.JTextField();
        pps = new javax.swing.JTextField();
        precision = new javax.swing.JTextField();
        priority = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        name.setEditable(false);
        name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        name.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nombre", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(name, gridBagConstraints);

        type.setEditable(false);
        type.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        type.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(type, gridBagConstraints);

        power.setEditable(false);
        power.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        power.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Poder", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(power, gridBagConstraints);

        damType.setEditable(false);
        damType.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        damType.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo de Daño", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(damType, gridBagConstraints);

        pps.setEditable(false);
        pps.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pps.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Puntos de Poder", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(pps, gridBagConstraints);

        precision.setEditable(false);
        precision.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        precision.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Precisión", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(precision, gridBagConstraints);

        priority.setEditable(false);
        priority.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        priority.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prioridad", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(priority, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));

        description.setEditable(false);
        jScrollPane1.setViewportView(description);

        jButton1.setText("Atrás");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField damType;
    private javax.swing.JTextPane description;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField name;
    private javax.swing.JTextField power;
    private javax.swing.JTextField pps;
    private javax.swing.JTextField precision;
    private javax.swing.JTextField priority;
    private javax.swing.JTextField type;
    // End of variables declaration//GEN-END:variables
}
