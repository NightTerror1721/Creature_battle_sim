/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.util.Objects;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import kp.cbs.TeamManager.TeamSlot;
import kp.cbs.creature.Creature;
import kp.cbs.place.Challenge.ChallengeStage;
import kp.cbs.place.Leage.LeageStage;
import kp.cbs.place.Stage;
import kp.cbs.utils.SoundManager;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class SequencedBattleMenu extends JDialog
{
    private final PlayerGame game;
    private final TeamManager team;
    private final Stage stage;
    
    private SequencedBattleMenu(MainGameInterface parent, LeageStage stage)
    {
        super(parent, true);
        this.game = parent.getPlayerGame();
        this.team = parent.getTeam();
        this.stage = Objects.requireNonNull(stage);
        initComponents();
        init();
    }
    
    private SequencedBattleMenu(ChallengeSelector parent, ChallengeStage stage)
    {
        super(parent, true);
        this.game = parent.getPlayerGame();
        this.team = parent.getTeam();
        this.stage = Objects.requireNonNull(stage);
        initComponents();
        init();
    }
    
    public static final void execute(MainGameInterface parent, LeageStage stage)
    {
        var seq = new SequencedBattleMenu(parent, stage);
        seq.setVisible(true);
    }
    public static final void execute(ChallengeSelector parent, ChallengeStage stage)
    {
        var seq = new SequencedBattleMenu(parent, stage);
        seq.setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        for(TeamSlot slot : TeamSlot.values())
        {
            var creaturePanel = new SequenceBattleCreaturePanel(this, team, slot);
            creaturePanel.update();
            jPanel1.add(creaturePanel);
        }
        
        addItems();
        updateElements();
    }
    
    private void updateElements()
    {
        money.setText(Integer.toString(stage.getAccumulatedMoney()));
        if(stage.isLeage())
        {
            elo.setText(Integer.toString(stage.getAccumulatedElo()));
            elo.setEnabled(true);
        }
        else
        {
            elo.setText("");
            elo.setEnabled(false);
        }
        
        var batts = stage.getRemainingBattles();
        reaminingBattles.setText(Integer.toString(batts));
        jButton1.setEnabled(batts < 1 || team.isAnyAlive());
        jButton1.setText(batts > 0 ? "Siguiente lucha" : "Terminar");
        jButton2.setEnabled(batts > 0);
    }
    
    private void updateTeam()
    {
        var len = jPanel1.getComponentCount();
        for(var i = 0; i < len; i++)
        {
            var comp = jPanel1.getComponent(i);
            if(comp != null && comp instanceof SequenceBattleCreaturePanel)
                ((SequenceBattleCreaturePanel) comp).update();
        }
    }
    
    private void addItems()
    {
        final var model = new DefaultListModel<Item>();
        game.streamAvailableItems()
                .filter(i -> !i.left.isCatcherItem())
                .forEach(i -> model.addElement(new Item(i.left)));
        items.setModel(model);
    }
    
    final Item getSelectedItem()
    {
        return items.getSelectedValue();
    }
    
    final class Item
    {
        private final ItemId item;
        
        private Item(ItemId item) { this.item = item; }
        
        public final int getAmount() { return game.getItemAmount(item); }
        
        public final void use(Creature creature)
        {
            if(!item.applyEffect(SequencedBattleMenu.this, creature))
                return;
            
            game.removeItemAmount(item, 1);
            if(game.getItemAmount(item) < 1)
            {
                var model = (DefaultListModel<Item>) items.getModel();
                model.removeElement(this);
            }
            items.repaint();
            SoundManager.playSound("item_use");
            updateTeam();
            updateElements();
        }
        
        @Override
        public final String toString()
        {
            return getAmount() + "x " + item.getName();
        }
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
        items = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        money = new javax.swing.JTextField();
        elo = new javax.swing.JTextField();
        reaminingBattles = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Objetos"));

        items.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(items);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Equipo"));
        jPanel1.setLayout(new java.awt.GridLayout(2, 3));

        jButton1.setText("Siguiente lucha");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Abandonar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridLayout(1, 3));

        money.setEditable(false);
        money.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        money.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dinero Acumulado", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.add(money);

        elo.setEditable(false);
        elo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        elo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Elo Acumulado", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.add(elo);

        reaminingBattles.setEditable(false);
        reaminingBattles.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reaminingBattles.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Batallas Restantes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.add(reaminingBattles);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(stage.getRemainingBattles() < 1)
        {
            dispose();
            return;
        }
        
        stage.startNextBattle(this, game, team.getTeamCreatures());
        team.clearTransient();
        
        updateElements();
        updateTeam();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(stage != null)
            stage.forceLose();
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField elo;
    private javax.swing.JList<Item> items;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField money;
    private javax.swing.JTextField reaminingBattles;
    // End of variables declaration//GEN-END:variables
}
