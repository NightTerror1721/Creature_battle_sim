/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import kp.cbs.TeamManager.TeamSlot;
import kp.cbs.creature.Creature;
import kp.cbs.creature.race.Race;
import kp.cbs.creature.race.RacePool;
import kp.cbs.place.Leage;
import kp.cbs.place.Place;
import kp.cbs.utils.Config;
import kp.cbs.utils.RNG;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class MainGameInterface extends JFrame
{
    private final PlayerGame game;
    private final TeamManager team;
    private Place currentPlace;
    private Leage currentLeage;
    
    public MainGameInterface(PlayerGame game)
    {
        this.game = Objects.requireNonNull(game);
        this.team = new TeamManager(game);
        initComponents();
        init();
    }
    
    public final PlayerGame getPlayerGame() { return game; }
    public final TeamManager getTeam() { return team; }
    
    public static final void start()
    {
        initialMenu();
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        updateTitle();
        
        for(TeamSlot slot : TeamSlot.values())
        {
            var creaturePanel = new MainCreaturePanel(this, team, slot);
            creaturePanel.update();
            jPanel6.add(creaturePanel);
        }
        
        updateMoney();
        updateCurrentPlace();
        updateCurrentLeage();
    }
    
    private void updateTitle()
    {
        setTitle("Creature Battle Simulator - " + game.getName());
    }
    
    private void updateMoney()
    {
        money.setText(Integer.toString(game.getMoney()));
    }
    
    private void updateTeam()
    {
        var len = jPanel6.getComponentCount();
        for(var i = 0; i < len; i++)
        {
            var comp = jPanel6.getComponent(i);
            if(comp != null && comp instanceof MainCreaturePanel)
                ((MainCreaturePanel) comp).update();
        }
    }
    
    private void updateCurrentPlace()
    {
        currentPlace = Place.load(game.getCurrentPlace());
        if(currentPlace == null)
        {
            placeName.setText("");
            
            placeName.setEnabled(false);
            travel.setEnabled(false);
            challenges.setEnabled(false);
            trainerBattle.setEnabled(false);
            wildBattle.setEnabled(false);
        }
        else
        {
            placeName.setText(currentPlace.getName());
            
            placeName.setEnabled(true);
            travel.setEnabled(currentPlace.getAvailableTravelsCount(game) > 0);
            challenges.setEnabled(currentPlace.getChallengeCount() > 0);
            trainerBattle.setEnabled(currentPlace.hasTrainerBattle());
            wildBattle.setEnabled(currentPlace.hasWildBattle());
        }
    }
    
    private void updateCurrentLeage()
    {
        currentLeage = Leage.load(game.getCurrentLeage());
        updateLeageElements();
    }
    
    private void updateLeageElements()
    {
        if(currentLeage == null)
        {
            leageName.setText("");
            leageElo.setText("");
            
            leageName.setEnabled(false);
            leageElo.setEnabled(false);
            changeLeage.setEnabled(true);
            playLeage.setEnabled(false);
            playLeader.setEnabled(false);
        }
        else
        {
            var elo = game.getLeageElo(currentLeage.getId());
            
            leageName.setText(currentLeage.getName() + (game.isIdPassed(currentLeage.getId()) ? " (Completada)" : ""));
            leageElo.setText(Integer.toString(elo));
            
            leageName.setEnabled(true);
            leageElo.setEnabled(true);
            changeLeage.setEnabled(true);
            playLeage.setEnabled(true);
            playLeader.setEnabled(game.isIdPassed(currentLeage.getId()) || currentLeage.isFinalBattleEnabled(elo));
        }
    }
    
    
    
    
    
    
    
    private static void initialMenu()
    {
        EventQueue.invokeLater(() -> {
            final var frame = new JFrame("CBS - Inicio");
            var bnew = new JButton("Partida Nueva");
            var bload = new JButton("Cargar Partida");
            var bquit = new JButton("Salir");
            
            var saves = PlayerGame.getAllSavedNames();

            bnew.addActionListener(e -> newGame(frame));
            bload.addActionListener(e -> loadGame(frame));
            bload.setEnabled(saves.length > 0);
            bquit.addActionListener(e -> { frame.dispose(); System.exit(0); });

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(300, 200));
            frame.setLayout(new GridLayout(3, 1));
            frame.add(bnew);
            frame.add(bload);
            frame.add(bquit);
            frame.setResizable(false);
            frame.pack();
            Utils.focus(frame);

            frame.setVisible(true);
        });
    }
    
    private static void openMain(final PlayerGame game)
    {
        EventQueue.invokeLater(() -> {
            new MainGameInterface(game).setVisible(true);
        });
    }
    
    private static void newGame(JFrame frame)
    {
        final var races = RacePool.getInitialRaces();
        if(races.length < 1)
            return;
        var obj = JOptionPane.showInputDialog(frame, "Debes seleccionar la raza de tu criatura inicial", "Escoger Raza",
                JOptionPane.QUESTION_MESSAGE, null, races, races[0]);
        if(obj == null)
            return;
        
        var creature = Creature.createWild((Race) obj, 5, new RNG());
        
        var game = new PlayerGame();
        game.setName("Partida sin nombre");
        game.addCreature(creature);
        game.setMoney(3000);
        game.setCurrentPlace(Config.getString("initial_place", ""));
        
        frame.dispose();
        openMain(game);
    }
    
    private static void loadGame(JFrame frame)
    {
        var saves = PlayerGame.getAllSavedNames();
        if(saves.length < 1)
            return;
        
        var obj = JOptionPane.showInputDialog(frame, "¿Que partida quieres cargar?", "Cargar partida",
                JOptionPane.QUESTION_MESSAGE, null, saves, saves[0]);
        if(obj == null || !(obj instanceof String))
            return;
        
        var game = PlayerGame.load((String) obj);
        if(game == null)
            return;
        
        frame.dispose();
        openMain(game);
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
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        money = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        placeName = new javax.swing.JTextField();
        travel = new javax.swing.JButton();
        wildBattle = new javax.swing.JButton();
        trainerBattle = new javax.swing.JButton();
        challenges = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        leageName = new javax.swing.JTextField();
        leageElo = new javax.swing.JTextField();
        changeLeage = new javax.swing.JButton();
        playLeage = new javax.swing.JButton();
        playLeader = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Equipo"));
        jPanel6.setLayout(new java.awt.GridLayout(2, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.2;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(jPanel6, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Otros"));
        jPanel7.setLayout(new java.awt.GridLayout(4, 1));

        money.setEditable(false);
        money.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        money.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dinero", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel7.add(money);

        jButton5.setText("Ver Objetos");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton5);

        jButton10.setText("Ver Luchadores");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton10);

        jButton6.setText("Guardar Partida");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(jPanel7, gridBagConstraints);

        jPanel1.add(jPanel2);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Localización"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        placeName.setEditable(false);
        placeName.setBorder(javax.swing.BorderFactory.createTitledBorder("Localización Actual"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.5;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(placeName, gridBagConstraints);

        travel.setText("Viajar");
        travel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                travelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(travel, gridBagConstraints);

        wildBattle.setText("Explorar");
        wildBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wildBattleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(wildBattle, gridBagConstraints);

        trainerBattle.setText("Retar a un entreandor");
        trainerBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainerBattleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(trainerBattle, gridBagConstraints);

        challenges.setText("Ver Desafios");
        challenges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                challengesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(challenges, gridBagConstraints);

        jPanel3.add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Liga"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        leageName.setEditable(false);
        leageName.setBorder(javax.swing.BorderFactory.createTitledBorder("Liga Actual"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(leageName, gridBagConstraints);

        leageElo.setEditable(false);
        leageElo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        leageElo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ELO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(leageElo, gridBagConstraints);

        changeLeage.setText("Cambiar de liga");
        changeLeage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLeageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(changeLeage, gridBagConstraints);

        playLeage.setText("Participar");
        playLeage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playLeageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(playLeage, gridBagConstraints);

        playLeader.setText("Desafiar al Lider");
        playLeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playLeaderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(playLeader, gridBagConstraints);

        jPanel3.add(jPanel5);

        jPanel1.add(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        CreatureSelector.viewer(this, game);
        updateTeam();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ItemsViewer.open(this);
        updateMoney();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        for(;;)
        {
            var name = JOptionPane.showInputDialog(this, "¿Con que nombre quieres guardar la partida?", game.getName());
            if(name == null)
                break;
            
            if(PlayerGame.exists(name) && JOptionPane.showConfirmDialog(this, "La partida ya existe. ¿Quieres sobreescribirla?",
                    "Partida existente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
                continue;
            
            game.setName(name);
            if(PlayerGame.save(game))
                JOptionPane.showMessageDialog(this, "¡La partida se ha guardado con éxito!", "Partida Guardad", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "Ha habido un error y la partida no se ha guardado.", "Partida Guardad", JOptionPane.ERROR_MESSAGE);
            break;
        }
        updateTitle();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void travelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_travelActionPerformed
        if(currentPlace == null)
            return;
        
        var all = currentPlace.getAvailableTravels(game);
        if(all.length < 1)
            return;
        
        var obj = JOptionPane.showInputDialog(this, "¿Donde quieres viajar?", "Viajar", JOptionPane.QUESTION_MESSAGE, null, all, all[0]);
        if(obj == null || !(obj instanceof String))
            return;
        
        game.setCurrentPlace(obj.toString());
        updateCurrentPlace();
    }//GEN-LAST:event_travelActionPerformed

    private void trainerBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainerBattleActionPerformed
        if(currentPlace == null || !currentPlace.hasTrainerBattle())
            return;
        
        currentPlace.startTrainerBattle(this, game, team.getTeamCreatures());
        team.clearAll();
        
        updateMoney();
        updateTeam();
    }//GEN-LAST:event_trainerBattleActionPerformed

    private void wildBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wildBattleActionPerformed
        if(currentPlace == null || !currentPlace.hasWildBattle())
            return;
        
        var creature = currentPlace.startWildBattle(this, game, team.getTeamCreatures());
        team.clearAll();
        updateTeam();
        if(creature == null)
            return;
        
        creature.clearAll();
        game.addCreature(creature);
        JOptionPane.showMessageDialog(this, "¡Has capturado a un " + creature.getRace().getName() + "! Se guardará con el resto de luchadores.");
    }//GEN-LAST:event_wildBattleActionPerformed

    private void changeLeageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeLeageActionPerformed
        var all = Leage.getAvailableLeageNames(game);
        if(all.length < 1)
            return;
        
        var obj = JOptionPane.showInputDialog(this, "¿Que liga quieres escoger?", "Ligas", JOptionPane.QUESTION_MESSAGE, null, all, all[0]);
        if(obj == null || !(obj instanceof String))
            return;
        
        game.setCurrentLeage(obj.toString());
        updateCurrentLeage();
    }//GEN-LAST:event_changeLeageActionPerformed

    private void playLeageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playLeageActionPerformed
        if(currentLeage == null)
            return;
        
        var elo = game.getLeageElo(currentLeage.getId());
        var money = game.getMoney();
        var stage = currentLeage.createStage(elo);
        SequencedBattleMenu.execute(this, stage);
        team.clearAll();
        
        game.setLeageElo(currentLeage.getId(), elo + stage.getAccumulatedElo());
        game.setMoney(money + stage.getAccumulatedMoney());
        
        updateCurrentLeage();
        updateTeam();
    }//GEN-LAST:event_playLeageActionPerformed

    private void playLeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playLeaderActionPerformed
        if(currentLeage == null)
            return;
        
        var elo = game.getLeageElo(currentLeage.getId());
        if(!currentLeage.isFinalBattleEnabled(elo))
            return;
        
        var money = game.getMoney();
        var result = currentLeage.startFinalBattle(this, game, team.getTeamCreatures());
        team.clearAll();
        
        if(result.isSelfWinner())
            game.addPassedId(currentLeage.getId());
        
        game.setMoney(money + result.getMoney());
        
        updateCurrentLeage();
        updateTeam();
        updateMoney();
    }//GEN-LAST:event_playLeaderActionPerformed

    private void challengesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_challengesActionPerformed
        if(currentPlace == null)
            return;
        
        var list = currentPlace.getAllChallenges();
        if(list == null || list.isEmpty())
            return;
        
        ChallengeSelector.open(this, list, stage -> {
            var money = game.getMoney();
            game.setMoney(money + stage.getAccumulatedMoney());
            updateTeam();
            updateMoney();
        });
        updateTeam();
        updateMoney();
    }//GEN-LAST:event_challengesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton challenges;
    private javax.swing.JButton changeLeage;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField leageElo;
    private javax.swing.JTextField leageName;
    private javax.swing.JTextField money;
    private javax.swing.JTextField placeName;
    private javax.swing.JButton playLeader;
    private javax.swing.JButton playLeage;
    private javax.swing.JButton trainerBattle;
    private javax.swing.JButton travel;
    private javax.swing.JButton wildBattle;
    // End of variables declaration//GEN-END:variables
}
