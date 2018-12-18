/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.text.ParseException;
import java.util.Objects;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import kp.cbs.creature.attack.effects.DamageEffect;
import kp.cbs.creature.attack.effects.NormalDamageEffect;
import kp.cbs.editor.AttackEditor;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class NormalDamageEffectEditor extends JDialog
{
    private final NormalDamageEffect effect;
    
    public NormalDamageEffectEditor(AttackEditor parent, NormalDamageEffect effect)
    {
        super(parent, true);
        this.effect = Objects.requireNonNull(effect);
        initComponents();
        init();
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        if(effect.isPowerValueCustom())
        {
            powerSel.setSelected(true);
            power.setValue(effect.getCustomPowerValue());
        }
        else
        {
            powerSel.setSelected(false);
            power.setValue(0);
        }
        
        if(effect.isAttackValueCustom())
        {
            attackSel.setSelectedIndex(2);
            attack.setValue(effect.getCustomAttackValue());
        }
        else
        {
            attackSel.setSelectedIndex(effect.isAttackValueToSelf() ? 0 : 1);
            attack.setValue(0);
        }
        
        if(effect.isDefenseValueCustom())
        {
            defenseSel.setSelectedIndex(2);
            defense.setValue(effect.getCustomDefenseValue());
        }
        else
        {
            defenseSel.setSelectedIndex(effect.isDefenseValueToSelf() ? 0 : 1);
            defense.setValue(0);
        }
        
        if(effect.hasDamageAbsorbtion())
        {
            vampRetSel.setSelectedIndex(1);
            vampRet.setValue(effect.getBackwardDamageOrAbsordb());
        }
        else if(effect.hasBackwardDamage())
        {
            vampRetSel.setSelectedIndex(2);
            vampRet.setValue(-effect.getBackwardDamageOrAbsordb());
        }
        else
        {
            vampRetSel.setSelectedIndex(0);
            vampRet.setValue(0);
        }
        
        special.setSelected(effect.isSpecialEnabled());
        selfTarget.setSelected(effect.isSelfTargetEnabled());
        primaryType.setSelected(effect.isUseSelfElementalTypeEnabled());
        criticalHit.setSelected(effect.isProbableCriticalHitEnabled());
    }
    
    public static final void execute(AttackEditor parent, DamageEffect effect)
    {
        if(effect instanceof NormalDamageEffect)
        {
            NormalDamageEffectEditor editor = new NormalDamageEffectEditor(parent, (NormalDamageEffect) effect);
            editor.setVisible(true);
        }
    }
    
    private void store()
    {
        if(powerSel.isSelected())
            effect.setPowerValueCustom(value(power, 1, 255));
        else effect.setPowerValueDefault();
        
        switch(attackSel.getSelectedIndex())
        {
            case 0: effect.setAttackValueToSelf(); break;
            case 1: effect.setAttackValueToEnemy(); break;
            default: case 2: effect.setAttackValueCustom(value(attack, 1, 9999)); break;
        }
        
        switch(defenseSel.getSelectedIndex())
        {
            case 0: effect.setDefenseValueToSelf(); break;
            case 1: effect.setDefenseValueToEnemy(); break;
            default: case 2: effect.setDefenseValueCustom(value(defense, 1, 9999)); break;
        }
        
        switch(vampRetSel.getSelectedIndex())
        {
            default: case 0: effect.setBackwardDamageOrAbsordb(0); break;
            case 1: effect.setBackwardDamageOrAbsordb(value(vampRet, 1, 256)); break;
            case 2: effect.setBackwardDamageOrAbsordb(-value(vampRet, 1, 256));
        }
        
        effect.setSpecialEnabled(special.isSelected());
        effect.setSelfTargetEnabled(selfTarget.isSelected());
        effect.setUseSelfElementalTypeEnabled(primaryType.isSelected());
        effect.setProbableCriticalHitEnabled(criticalHit.isSelected());
    }
    
    private void commit()
    {
        try
        {
            power.commitEdit();
            vampRet.commitEdit();
            attack.commitEdit();
            defense.commitEdit();
        }
        catch(ParseException ex) {}
    }
    
    @Override
    public void dispose()
    {
        commit();
        store();
        super.dispose();
    }
    
    private static int value(JFormattedTextField field, int min, int max)
    {
        return Utils.range(min, max, ((Number) field.getValue()).intValue());
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
        jPanel2 = new javax.swing.JPanel();
        powerSel = new javax.swing.JCheckBox();
        power = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        vampRetSel = new javax.swing.JComboBox<>();
        vampRet = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        attackSel = new javax.swing.JComboBox<>();
        attack = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        defenseSel = new javax.swing.JComboBox<>();
        defense = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        special = new javax.swing.JCheckBox();
        selfTarget = new javax.swing.JCheckBox();
        primaryType = new javax.swing.JCheckBox();
        criticalHit = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Propiedades de Daño Normal");

        jPanel1.setLayout(new java.awt.GridLayout(2, 2));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Poder Personalizado"));

        powerSel.setText("Activar poder personalizado");

        power.setBorder(javax.swing.BorderFactory.createTitledBorder("Poder"));
        power.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(powerSel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(power, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(powerSel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(power, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Efecto Vampiro o Daño retroceso"));

        vampRetSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ninguno", "Efecto Vampiro", "Daño Retroceso" }));

        vampRet.setBorder(javax.swing.BorderFactory.createTitledBorder("Porcentaje (X / 256)"));
        vampRet.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vampRet)
                    .addComponent(vampRetSel, 0, 187, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(vampRetSel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vampRet, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque Personalizado"));

        attackSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Valor del Usuario", "Valor del Enemigo", "Valor Personalizado" }));

        attack.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor del Ataque"));
        attack.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(attackSel, 0, 187, Short.MAX_VALUE)
                    .addComponent(attack))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(attackSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(attack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa Personalizada"));

        defenseSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Valor del Usuario", "Valor del Enemigo", "Valor Personalizado" }));

        defense.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor de la Defensa"));
        defense.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defenseSel, 0, 187, Short.MAX_VALUE)
                    .addComponent(defense))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(defenseSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(defense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.GridLayout(2, 2));

        special.setText("Usar Ataque/Defensa Especial");
        jPanel6.add(special);

        selfTarget.setText("Apuntar al Usuario");
        jPanel6.add(selfTarget);

        primaryType.setText("Usar Tipo primario del Usuario");
        jPanel6.add(primaryType);

        criticalHit.setText("Alta probabilidad Golpe Crítico");
        jPanel6.add(criticalHit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField attack;
    private javax.swing.JComboBox<String> attackSel;
    private javax.swing.JCheckBox criticalHit;
    private javax.swing.JFormattedTextField defense;
    private javax.swing.JComboBox<String> defenseSel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JFormattedTextField power;
    private javax.swing.JCheckBox powerSel;
    private javax.swing.JCheckBox primaryType;
    private javax.swing.JCheckBox selfTarget;
    private javax.swing.JCheckBox special;
    private javax.swing.JFormattedTextField vampRet;
    private javax.swing.JComboBox<String> vampRetSel;
    // End of variables declaration//GEN-END:variables
}