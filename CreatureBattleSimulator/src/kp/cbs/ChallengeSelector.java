/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import kp.cbs.place.Challenge;
import kp.cbs.place.Challenge.ChallengeStage;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class ChallengeSelector extends JDialog
{
    private final Consumer<ChallengeStage> action;
    private final PlayerGame game;
    private final TeamManager team;
    
    private ChallengeSelector(MainGameInterface parent, List<Challenge> challenges, Consumer<ChallengeStage> action)
    {
        super(parent, true);
        this.action = Objects.requireNonNull(action);
        this.game = parent.getPlayerGame();
        this.team = parent.getTeam();
        initComponents();
        init(challenges);
    }
    
    public static final void open(MainGameInterface parent, List<Challenge> challenges, Consumer<ChallengeStage> action)
    {
        var sel = new ChallengeSelector(parent, challenges, action);
        sel.setVisible(true);
    }
    
    private void init(List<Challenge> all)
    {
        setResizable(false);
        Utils.focus(this);
        
        var model = new DefaultListModel<Challenge>();
        model.addAll(all);
        challenges.setModel(model);
        
        showChallenge();
    }
    
    public final PlayerGame getPlayerGame() { return game; }
    public final TeamManager getTeam() { return team; }
    
    private void showChallenge()
    {
        var sel = challenges.getSelectedValue();
        if(sel == null)
        {
            name.setText("");
            description.setText("");
            
            name.setEnabled(false);
            description.setEnabled(false);
            playChallenge.setEnabled(false);
            return;
        }
        
        name.setText(sel.getName() + (sel.isCompleted(game) ? " (Completado)" : ""));
        description.setText(sel.getDescription() + (sel.isUnique() ? " (Solo se puede completar una vez)." : ""));
        
        name.setEnabled(true);
        description.setEnabled(true);
        playChallenge.setEnabled(sel.isEnabled(game));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        challenges = new javax.swing.JList<>();
        playChallenge = new javax.swing.JButton();
        name = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Desafios"));

        challenges.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        challenges.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                challengesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(challenges);

        playChallenge.setText("Comenzar Desafio");
        playChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playChallengeActionPerformed(evt);
            }
        });

        name.setEditable(false);
        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));

        description.setEditable(false);
        jScrollPane2.setViewportView(description);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name)
                    .addComponent(jScrollPane2)
                    .addComponent(playChallenge, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playChallenge))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playChallengeActionPerformed
        var sel = challenges.getSelectedValue();
        if(sel == null)
            return;
        
        var stage = sel.createStage();
        SequencedBattleMenu.execute(this, stage);
        team.clearAll();
        
        action.accept(stage);
        if(stage.allWinned())
            game.addPassedId(sel.getId());
        
        showChallenge();
    }//GEN-LAST:event_playChallengeActionPerformed

    private void challengesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_challengesValueChanged
        showChallenge();
    }//GEN-LAST:event_challengesValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<Challenge> challenges;
    private javax.swing.JTextPane description;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField name;
    private javax.swing.JButton playChallenge;
    // End of variables declaration//GEN-END:variables
}
