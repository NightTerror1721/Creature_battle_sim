/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import kp.cbs.creature.attack.AttackModel.AttackTurn;
import kp.cbs.creature.attack.effects.DamageEffect;
import kp.cbs.creature.attack.effects.DamageEffect.DamageEffectType;
import kp.cbs.creature.attack.effects.FixedDamageEffect;
import kp.cbs.creature.attack.effects.HealDamageEffect;
import kp.cbs.creature.attack.effects.NormalDamageEffect;
import kp.cbs.creature.attack.effects.SecondaryEffect;
import kp.cbs.creature.attack.effects.SecondaryEffect.SecondaryEffectType;
import kp.cbs.creature.attack.effects.SpecialSecondaryEffect;
import kp.cbs.creature.attack.effects.StatAlterationSecondaryEffect;
import kp.cbs.creature.attack.effects.StateAlterationSecondaryEffect;
import kp.cbs.creature.attack.effects.WeatherSecondaryEffect;
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
    
    public final void fillTurn(AttackTurn turn)
    {
        turn.setMessage(optMessage.getText().isBlank() ? "" : optMessage.getText());
        turn.setMinHits(intValue(minHits));
        turn.setMaxHits(intValue(maxHits));
        turn.setDamageEffect(damageEffect);
        
        int len = secEffects.getItemCount();
        for(int i=0;i<len;i++)
            turn.addSecondaryEffect(secEffects.getItemAt(i).effect);
    }
    
    public final void expandTurn(AttackTurn turn)
    {
        optMessage.setText(turn.getMessage());
        minHits.setSelectedItem(turn.getMinHits());
        maxHits.setSelectedItem(turn.getMaxHits());
        
        selectedDamEff.setSelectedItem(turn.getDamageEffect().getType());
        damageEffect = turn.getDamageEffect();
        
        secEffects.removeAllItems();
        int len = turn.getSecondaryEffectCount();
        for(int i=0;i<len;i++)
            secEffects.addItem(new SecondaryEffectObject(turn.getSecondaryEffect(i)));
    }
    
    private static int intValue(JComboBox<Integer> box)
    {
        try { return ((Number) box.getSelectedItem()).intValue(); }
        catch(Throwable ex) { return 0; }
    }
    
    private static final class SecondaryEffectObject
    {
        private final SecondaryEffect effect;
        
        private SecondaryEffectObject(SecondaryEffect effect)
        {
            this.effect = Objects.requireNonNull(effect);
        }
        
        @Override
        public final String toString() { return effect.getType().toString(); }
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

        optMessage = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        minHits = new javax.swing.JComboBox<>();
        maxHits = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        selectedDamEff = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        secEffects = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setLayout(new java.awt.GridLayout(4, 0));

        optMessage.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensaje Opcional"));
        add(optMessage);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        minHits.setBorder(javax.swing.BorderFactory.createTitledBorder("Mínimo de golpes"));
        jPanel2.add(minHits);

        maxHits.setBorder(javax.swing.BorderFactory.createTitledBorder("Máximo de golpes"));
        jPanel2.add(maxHits);

        add(jPanel2);

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        selectedDamEff.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Efecto Daño"));
        selectedDamEff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedDamEffActionPerformed(evt);
            }
        });
        jPanel3.add(selectedDamEff);

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
        jPanel4.setLayout(new java.awt.GridLayout(1, 2));

        secEffects.setBorder(javax.swing.BorderFactory.createTitledBorder("Efectos secundarios"));
        jPanel4.add(secEffects);

        jPanel1.setLayout(new java.awt.GridLayout(2, 2));

        jButton1.setText("Nuevo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton5.setText("Editar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setText("Eliminar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        jButton3.setText("^");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3);

        jButton4.setText("v");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4);

        jPanel1.add(jPanel5);

        jPanel4.add(jPanel1);

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Object val = JOptionPane.showInputDialog(parent, "Selecciona el tipo de effecto que quieres crear.",
                "Nuevo Efecto Secundario", JOptionPane.QUESTION_MESSAGE, null,
                SecondaryEffectType.values(), SecondaryEffectType.EMPTY_EFFECT);
        if(val == null)
            return;
        
        SecondaryEffect effect;
        switch((SecondaryEffectType) val)
        {
            default:
            case EMPTY_EFFECT: return;
            case STAT_ALTERATION: effect = new StatAlterationSecondaryEffect(); break;
            case WEATHER_ALTERATION: effect = new WeatherSecondaryEffect(); break;
            case STATE_ALTERATION: effect = new StateAlterationSecondaryEffect(); break;
            case SPECIAL_EFFECT: effect = new SpecialSecondaryEffect(); break;
        }
        
        secEffects.addItem(new SecondaryEffectObject(effect));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int sel = secEffects.getSelectedIndex();
        if(sel < 0)
            return;
        
        secEffects.removeItemAt(sel);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int sel = secEffects.getSelectedIndex();
        if(sel < 1)
            return;
        
        SecondaryEffectObject value = secEffects.getItemAt(sel);
        secEffects.removeItemAt(sel);
        secEffects.insertItemAt(value, sel - 1);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int sel = secEffects.getSelectedIndex();
        if(sel < 0 || sel >= secEffects.getItemCount() - 1)
            return;
        
        SecondaryEffectObject value = secEffects.getItemAt(sel);
        secEffects.removeItemAt(sel);
        secEffects.insertItemAt(value, sel + 1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int sel = secEffects.getSelectedIndex();
        if(sel < 0)
            return;
        
        SecondaryEffect effect = secEffects.getItemAt(sel).effect;
        switch(effect.getType())
        {
            case STAT_ALTERATION: StatAlterationSecondaryEffectEditor.execute(parent, effect); break;
            case WEATHER_ALTERATION: WeatherOrSpecialSecondaryEffectEditor.execute(parent, effect); break;
            case STATE_ALTERATION: StateAlterationSecondaryEffectEditor.execute(parent, effect); break;
            case SPECIAL_EFFECT: WeatherOrSpecialSecondaryEffectEditor.execute(parent, effect); break;
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JComboBox<Integer> maxHits;
    private javax.swing.JComboBox<Integer> minHits;
    private javax.swing.JTextField optMessage;
    private javax.swing.JComboBox<SecondaryEffectObject> secEffects;
    private javax.swing.JComboBox<DamageEffectType> selectedDamEff;
    // End of variables declaration//GEN-END:variables
}
