/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.awt.Color;
import java.awt.Dialog;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.JPanel;
import kp.cbs.creature.Creature;
import kp.cbs.creature.CreatureViewer;

/**
 *
 * @author mpasc
 */
public class CreatureTeamViewer extends JPanel
{
    private final Dialog parent;
    private final Creature creature;
    private final boolean isCurrent;
    private final Consumer<CreatureTeamViewer> action;
    
    public CreatureTeamViewer(Dialog parent, Creature creature, boolean isCurrent, Consumer<CreatureTeamViewer> action)
    {
        initComponents();
        this.parent = Objects.requireNonNull(parent);
        this.creature = Objects.requireNonNull(creature);
        this.isCurrent = isCurrent;
        this.action = Objects.requireNonNull(action);
        init();
    }
    
    private void init()
    {
        name.setText("Nombre: " + creature.getName());
        race.setText("Raza: " + creature.getRace().getName());
        level.setText("Nv: " + creature.getLevel());
        altered.setText(creature.getAlterationManager().getAbbreviatedInfo());
        
        var health = creature.getHealthPoints();
        psCount.setText(health.getCurrentHealthPoints() + "/" + health.getMaxHealthPoints());
        
        ps.setMinimum(0);
        ps.setMaximum(health.getMaxHealthPoints());
        ps.setValue(health.getCurrentHealthPoints());
        ps.setForeground(computeHealthColor(health.getCurrentHealthPoints(),health.getMaxHealthPoints()));
        
        exp.setMinimum(0);
        exp.setMaximum(100);
        exp.setValue((int)creature.getExperienceManager().getCurrentPercentage() * 100);
        exp.setForeground(computeExpColor(creature.getLevel()));
        
        fight.setEnabled(!isCurrent && creature.isAlive());
    }
    
    public final Creature getCreature() { return creature; }
    public final boolean isCurrent() { return isCurrent; }
    
    private static Color computeHealthColor(float current, float max)
    {
        float per = current / max;
        if(per >= 0.5f)
        {
            int r = (int) (-(per - 1f) * 510);
            return new Color(r,255,0);
        }
        int g = (int) (per * 510);
        return new Color(255, g, 0);
    }
    
    private static Color computeExpColor(int level)
    {
        if(level < 50)
        {
            int g = (50 - level) * 4;
            return new Color(0, g, 250);
        }
        int r = (level - 50) * 3;
        int b = 200 + (level - 50);
        return new Color(r, 0, b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ps = new javax.swing.JProgressBar();
        name = new javax.swing.JLabel();
        race = new javax.swing.JLabel();
        psCount = new javax.swing.JLabel();
        exp = new javax.swing.JProgressBar();
        level = new javax.swing.JLabel();
        altered = new javax.swing.JLabel();
        show = new javax.swing.JButton();
        fight = new javax.swing.JButton();

        name.setText("jLabel1");

        race.setText("jLabel2");

        psCount.setText("9999/9999");

        level.setText("jLabel4");

        altered.setText("jLabel5");

        show.setText("Ver");
        show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });

        fight.setText("¡Luchar!");
        fight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fightActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(exp, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(psCount))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(race))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(level)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(altered))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(fight, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(show, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name)
                    .addComponent(race))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(level)
                    .addComponent(altered))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ps, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(psCount)
                    .addComponent(exp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(show)
                    .addComponent(fight))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fightActionPerformed
        action.accept(this);
    }//GEN-LAST:event_fightActionPerformed

    private void showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionPerformed
        CreatureViewer.open(parent, creature, false);
    }//GEN-LAST:event_showActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel altered;
    private javax.swing.JProgressBar exp;
    private javax.swing.JButton fight;
    private javax.swing.JLabel level;
    private javax.swing.JLabel name;
    private javax.swing.JProgressBar ps;
    private javax.swing.JLabel psCount;
    private javax.swing.JLabel race;
    private javax.swing.JButton show;
    // End of variables declaration//GEN-END:variables
}
