/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import kp.cbs.creature.attack.effects.DamageEffect;
import kp.cbs.creature.attack.effects.DamageEffect.DamageEffectType;
import kp.cbs.creature.attack.effects.FixedDamageEffect;
import kp.cbs.creature.attack.effects.HealDamageEffect;
import kp.cbs.creature.attack.effects.NormalDamageEffect;
import kp.cbs.creature.attack.effects.SecondaryEffect;
import kp.cbs.editor.AttackEditor;

/**
 *
 * @author Asus
 */
public class AttackTurnEditor extends JPanel
{
    private final AttackEditor parent;
    private DamageEffect damageEffect;
    
    public AttackTurnEditor(AttackEditor parent)
    {
        this.parent = Objects.requireNonNull(parent);
        initComponents();
        init();
    }
    
    private void init()
    {
        minHits.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 10, 1)));
        maxHits.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 10, 1)));
        
        selectedDamEff.setModel(new DefaultComboBoxModel<>(DamageEffectType.values()));
        selectedDamEff.setSelectedItem(DamageEffectType.NORMAL);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        minHits = new javax.swing.JComboBox<>();
        maxHits = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        selectedDamEff = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();

        setLayout(new java.awt.GridLayout(4, 0));

        jTextField1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensaje Opcional"));
        add(jTextField1);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        minHits.setBorder(javax.swing.BorderFactory.createTitledBorder("Mínimo de golpes"));
        jPanel2.add(minHits);

        maxHits.setBorder(javax.swing.BorderFactory.createTitledBorder("Máximo de golpes"));
        jPanel2.add(maxHits);

        add(jPanel2);

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        selectedDamEff.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Efecto Daño"));
        selectedDamEff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedDamEffActionPerformed(evt);
            }
        });
        jPanel3.add(selectedDamEff);

        jButton2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton2.setText("Editar");
        jButton2.setPreferredSize(new java.awt.Dimension(120, 50));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        add(jPanel3);

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jButton3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton3.setText("Nuevo");
        jPanel4.add(jButton3);

        jButton4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton4.setText("Editar");
        jPanel4.add(jButton4);

        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder("Efectos secundarios"));
        jPanel4.add(jComboBox1);

        add(jPanel4);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(damageEffect == null)
            return;
        switch(damageEffect.getType())
        {
            case NORMAL: NormalDamageEffectEditor.execute(parent, damageEffect); break;
            case FIXED: FixedDamageEffectEditor.execute(parent, damageEffect); break;
            case HEAL: HealDamageEffectEditor.execute(parent, damageEffect); break;
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void selectedDamEffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedDamEffActionPerformed
        var type = (DamageEffectType) selectedDamEff.getSelectedItem();
        switch(type)
        {
            default:
            case NO_DAMAGE: damageEffect = DamageEffect.NO_DAMAGE; break;
            case NORMAL: damageEffect = new NormalDamageEffect(); break;
            case LEVEL: damageEffect = DamageEffect.LEVEL_DAMAGE; break;
            case FIXED: damageEffect = new FixedDamageEffect(); break;
            case HEAL: damageEffect = new HealDamageEffect(); break;
        }
        if(damageEffect == null)
            damageEffect = DamageEffect.NO_DAMAGE;
    }//GEN-LAST:event_selectedDamEffActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<SecondaryEffect> jComboBox1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox<Integer> maxHits;
    private javax.swing.JComboBox<Integer> minHits;
    private javax.swing.JComboBox<DamageEffectType> selectedDamEff;
    // End of variables declaration//GEN-END:variables
}
