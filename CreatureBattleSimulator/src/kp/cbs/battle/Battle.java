/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import static kp.cbs.battle.TeamId.ENEMY;
import static kp.cbs.battle.TeamId.SELF;
import kp.cbs.creature.Creature;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackPool;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.StatId;
import kp.cbs.utils.Formula;
import kp.cbs.utils.LifebarUtils;
import kp.cbs.utils.Semaphore;
import kp.cbs.utils.SoundManager;
import kp.cbs.utils.Utils;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;

/**
 *
 * @author Asus
 */
public class Battle extends JDialog
{
    private final BattleCore core;
    
    private final Semaphore sem = new Semaphore(1);
    private final Semaphore round = new Semaphore(1);
    private final Semaphore damC = new Semaphore(1);
    private final Semaphore healC = new Semaphore(1);
    
    private final Sound expGet = SoundManager.getSound("expget");
    
    private float expBonus = 1f;
    
    private boolean wildBattle;
    
    private ButtonsMenuState butState = ButtonsMenuState.DISABLED;
    
    private Music music;
    
    private boolean gameOver;
    
    private BattleResult result;
    
    private Battle(Frame parent, BattleCore core)
    {
        super(parent, true);
        this.core = Objects.requireNonNull(core);
        initComponents();
        init();
    }
    
    private Battle(Dialog parent, BattleCore core)
    {
        super(parent, true);
        this.core = Objects.requireNonNull(core);
        initComponents();
        init();
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        core.setBattle(this);
    }
    
    public static final void initiate(Window parent, Encounter encounter)
    {
        var bcore = new BattleCore(encounter.getSelfTeam(), encounter.getEnemyTeam());
        
        Battle battle;
        if(parent == null || parent instanceof Frame)
            battle = new Battle((Frame) parent, bcore);
        else if(parent instanceof Dialog)
            battle = new Battle((Dialog) parent, bcore);
        else throw new IllegalStateException();
        
        battle.expBonus = encounter.getExperienceBonus();
        
        battle.music = SoundManager.loadMusic(encounter.getMusic());
        
        battle.start();
    }
    
    
    private void start()
    {
        var mainThread = new Thread(() -> {
            core.start();
            selectActionState();
        });
        mainThread.start();
        if(music != null)
                music.play(true);
        setVisible(true);
    }
    
    final void modifBarlifePlayer(TeamId team, final int goal, boolean isHeal)
    {
        final JProgressBar bar = team == SELF ? self_lifebar : enemy_lifebar;
        //final JProgressBar bar = team == USER ? user_ps_bar : enemy_ps_bar;
        final int mod = isHeal ? 1 : -1;
        final boolean flag = team == SELF;
        final int goalLive = goal;
        final int lastHealth = bar.getValue();
        //bar.setValue(cr.getFeatures().getLife().getLastHealth());
        
        
        Runnable RunnInitial = () -> {
            //int ratio = pl.getFeatures().getLife().getMaxHeal();
            //ratio = ratio <= 400 ? 4 : (int)( 4f * ((400f / ratio) * 1.5f));
            int ratio = 2;
            float rest = isHeal ?  goalLive - lastHealth :
                    lastHealth - goalLive;
            final float max = rest;
            final float life = bar.getMaximum();
            //float mr = rest / 400f * 2f * 0.1f;
            //mr = mr < 0.15f ? 0.15f : mr > 1f ? 1f : mr;
            float pp;
            boolean slow = rest >= (life * 0.13f);
            final float mid = rest / 2f;
            final float midSpeed = mid / 400f * 3.5f;
            final float slowTime = rest / 10f;
            final float min = rest / 400f * 3.5f * 0.1f;
            final float maxSpeed = life > 2000 ? 2000f / 400f * 3.5f * 0.25f :
                    life / 400f * 3.5f * 0.25f;
            if(ratio < 1) ratio = 1;
            while(mod < 0
                    ? (bar.getValue() > goalLive &&
                    bar.getValue() > 0)
                    : (bar.getValue() < goalLive &&
                    bar.getValue() < bar.getMaximum()))
            {
                if(slow)
                {
                    if(rest >= mid)
                    {
                        float sslow = (max - rest) / slowTime;
                        if(sslow > 1f) sslow = 1f;
                        pp = midSpeed * mod * sslow;
                        //if(Math.abs(pp) > midSpeed)
                        //pp = midSpeed * mod;
                        if(Math.abs(pp) < min)
                            pp = min * mod;
                    }
                    else
                    {
                        pp = rest / 400f * 3.5f * mod;
                    }
                }
                else pp = (life * 0.03f) / 400f * 3.5f * mod;
                //pp = rest / 400f * 3.5f * mod;
                if(Math.abs(pp) < 0.15f) pp = 0.15f * mod;
                if(Math.abs(pp) > maxSpeed) pp = maxSpeed * mod;
                rest += isHeal ? -pp : pp;
                if(isHeal)
                    bar.setValue((int) (goalLive - rest));
                else
                    bar.setValue((int) (goalLive + rest));
                if(flag)
                    updateSelfHpPoints(bar.getValue());
                bar.repaint();
                sleep(ratio);
            }
            sem.toGreen(0);
        };
        Thread tInitial = new Thread(RunnInitial);
        sem.toRed(0);
        tInitial.start();
        if(lastHealth != goalLive)
        {
            if(mod < 0) damageColor(team);
            else healColor(team);
        }
        sem.pass(0);
        damC.pass(0);
        healC.pass(0);
        //colorLife(isSelf);
    }
    
    private void modifExpBar(final int goal)
    {
        final int speed = 20;
        
        sem.toRed(0);
        new Thread(() -> {
            expGet.play();
            while(self_expbar.getValue() < goal)
            {
                self_expbar.setValue(self_expbar.getValue() + 1);
                self_expbar.repaint();
                sleep(speed);
            }
            expGet.stop();
            sem.toGreen(0);
        }).start();
        sem.pass(0);
    }
    
    final void insertMessage(String message)
    {
        try
        {
            int offset = jTextPane1.getDocument().getLength();
            jTextPane1.getDocument().insertString(offset,message + "\n",null);
            jTextPane1.setCaretPosition(jTextPane1.getDocument().getLength());
        }
        catch(BadLocationException ex) {}
    }
    
    final void clearText()
    {
        jTextPane1.setText("");
        jTextPane1.setCaretPosition(jTextPane1.getDocument().getLength());
    }
    
    
    final void updateCreatureInterface(TeamId team)
    {
        JLabel name = team == SELF ? self_name : enemy_name;
        JLabel race = team == SELF ? self_race : enemy_race;
        JLabel level = team == SELF ? self_level : enemy_level;
        JLabel altered = team == SELF ? self_altered : enemy_altered;
        JPanel panel = team == SELF ? jPanel2 : jPanel3;
        
        var creature = core.getFighter(team);
        
        name.setEnabled(creature != null);
        race.setEnabled(creature != null);
        level.setEnabled(creature != null);
        altered.setEnabled(creature != null);
        
        if(creature == null)
        {
            name.setText("");
            race.setText("");
            level.setText("");
            altered.setText("");
            panel.setBorder(new LineBorder(Color.BLACK, 1));
            
            updateCreatureHp(creature, team);
            
            if(team == SELF)
            {
                self_expbar.setEnabled(false);
                self_powerup.setEnabled(false);
                
                self_expbar.setMaximum(100);
                self_expbar.setValue(0);
                self_powerup.setText("");

                self_lifecount.setForeground(Color.BLACK);
                self_powerup.setForeground(Color.BLACK);
            }
        }
        else
        {
            name.setText("Nombre: " + creature.getName());
            race.setText(creature.getRace() + " [Clase: " + creature.getCreatureClass() + "]");
            level.setText("Nivel " + creature.getLevel());
            altered.setText(creature.getAlterationManager().getAbbreviatedInfo());
            updateCreatureHp(creature, team);

            float hpPer = creature.getCurrentHealthPoints() / ((float) creature.getMaxHealthPoints());
            Color bcolor = hpPer <= 0.2f ? Color.RED : hpPer <= 0.5f ? Color.YELLOW : Color.BLACK;

            name.setForeground(bcolor);
            race.setForeground(bcolor);
            level.setForeground(bcolor);
            altered.setForeground(bcolor);
            panel.setBorder(new LineBorder(bcolor, 1));
            
            if(team == SELF)
            {
                self_expbar.setEnabled(true);
                self_powerup.setEnabled(true);
                
                self_expbar.setMaximum(100);
                self_expbar.setValue((int) (creature.getExperienceManager().getCurrentPercentage() * 100));
                self_powerup.setText(creature.getFeaturesManager().getPowerupAbbreviations());

                self_lifecount.setForeground(bcolor);
                self_powerup.setForeground(bcolor);
            }
        }
        
    }
    
    private void updateCreatureHp(Creature creature, TeamId team)
    {
        var bar = team == SELF ? self_lifebar : enemy_lifebar;
        if(creature == null)
        {
            if(team == SELF)
            {
                self_lifecount.setEnabled(false);
                self_lifecount.setText("");
            }
            bar.setMaximum(1);
            bar.setValue(0);
            bar.setForeground(Color.BLACK);
            bar.setEnabled(false);
        }
        else
        {
            if(team == SELF)
            {
                self_lifecount.setEnabled(true);
                self_lifecount.setText(creature.getCurrentHealthPoints() + "/" + creature.getMaxHealthPoints());
            }
            bar.setMaximum(creature.getMaxHealthPoints());
            bar.setValue(creature.getCurrentHealthPoints());
            bar.setForeground(LifebarUtils.computeColor(creature.getMaxHealthPoints()));
            bar.setEnabled(false);
        }
    }
    
    private void updateSelfHpPoints(int value)
    {
        var creature = core.getFighter(SELF);
        self_lifecount.setText(value + "/" + creature.getMaxHealthPoints());
    }
    
    
    final void setButtonsState(ButtonsMenuState state)
    {
        but1.setEnabled(state != ButtonsMenuState.DISABLED);
        but2.setEnabled(state != ButtonsMenuState.DISABLED);
        but3.setEnabled(state != ButtonsMenuState.DISABLED);
        if(wildBattle)
        {
            but4.setEnabled(state != ButtonsMenuState.DISABLED);
            but5.setEnabled(state != ButtonsMenuState.DISABLED);
        }
        else
        {
            but4.setEnabled(state == ButtonsMenuState.ATTACKS);
            but5.setEnabled(state == ButtonsMenuState.ATTACKS);
        }
        switch(state)
        {
            case DISABLED:
            case MAIN:
                but1.setText("Usar Ataque");
                but2.setText("Golpear");
                but3.setText("Equipo");
                but4.setText("Capturar");
                but5.setText("Escapar");
                but1.setBackground(Color.WHITE);
                but2.setBackground(Color.WHITE);
                but3.setBackground(Color.WHITE);
                but4.setBackground(Color.WHITE);
                but5.setBackground(Color.WHITE);
                but1.setForeground(Color.BLACK);
                but2.setForeground(Color.BLACK);
                but3.setForeground(Color.BLACK);
                but4.setForeground(Color.BLACK);
                but5.setForeground(Color.BLACK);
            break;
            case ATTACKS: {
                var self = core.getFighter(SELF);
                setButtonText(but1, self.getAttack(0));
                setButtonText(but2, self.getAttack(1));
                setButtonText(but3, self.getAttack(2));
                setButtonText(but4, self.getAttack(3));
                but5.setText("Atrás");
            } break;
        }
        butState = state;
    }
    private static void setButtonText(JButton but, Attack att)
    {
        if(att == null)
        {
            but.setText("----------");
            but.setEnabled(false);
        }
        else
        {
            but.setText(att.getName() + " (" +
                    att.getCurrentPP()+ "/" + att.getMaxPP() + ")");
            ElementalType type = att.getElementalType();
            but.setBackground(type.getColor());
            but.setForeground(type.getFontColor());
        }
    }
    
    
    private void damageColor(TeamId team)
    {
        final JProgressBar bar = team == SELF ? self_lifebar : enemy_lifebar;
        final int fr, fg, fb;
        
        Color c = bar.getForeground();
        fr = c.getRed();
        fg = c.getGreen();
        fb = c.getBlue();
        
        Runnable RunnInitial = () -> {
            int times = 0;
            int r, g, b, col;
            while(times < 2)
            {
                times++;
                r = fr;
                g = fg;
                b = fb;
                col = 0;
                while(col < 256)
                {
                    col+=2;
                    sleep(1);
                    r += 2;
                    g -= 2;
                    b -= 2;
                    if(r > 255) r = 255;
                    if(g < 0) g = 0;
                    if(b < 0) b = 0;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r, g, b));
                    }
                }
                col = 256;
                while(col > 0)
                {
                    col-=2;
                    sleep(1);
                    r -= 2;
                    g += 2;
                    b += 2;
                    if(r < fr) r = fr;
                    if(g > fg) g = fg;
                    if(b > fb) b = fb;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r, g, b));
                    }
                }
            }
            damC.toGreen(0);
        };
        Thread th = new Thread(RunnInitial);
        damC.toRed(0);
        th.start();
    }
    
    private void healColor(TeamId team)
    {
        final JProgressBar bar = team == SELF ? self_lifebar : enemy_lifebar;
        final int fr, fg, fb;
        
        Color c = bar.getForeground();
        fr = c.getRed();
        fg = c.getGreen();
        fb = c.getBlue();
        
        Runnable RunnInitial = () -> {
            int times = 0;
            int r, g, b, col;
            while(times < 1)
            {
                times++;
                r = fr;
                g = fg;
                b = fb;
                col = 0;
                while(col < 256)
                {
                    col += 1;
                    sleep(3);
                    r -= 1;
                    g += 1;
                    b += 1;
                    if(r < 0) r = 0;
                    if(g > 255) g = 255;
                    if(b > 255) b = 255;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r,g,b));
                    }
                }
                while(col > 0)
                {
                    col-=1;
                    sleep(3);
                    r += 1;
                    g -= 1;
                    b -= 1;
                    if(r > fr) r = fr;
                    if(g < fg) g = fg;
                    if(b < fb) b = fb;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r, g, b));
                    }
                }
            }
            healC.toGreen(0);
        };
        Thread th = new Thread(RunnInitial);
        healC.toRed(0);
        th.start();
    }
    
    final void sleep(int sleep)
    {
        try { Thread.sleep(sleep); }
        catch(InterruptedException ex) {}
    }
    
    
    final void checkExperience()
    {
        if(!core.isCurrentAlive(ENEMY) || core.isCurrentAlive(SELF))
            return;
        int expGain = core.getExperieneGained(expBonus);
        if(expGain <= 0)
            return;
        var creature = core.getFighter(SELF);
        var cexp = creature.getExperienceManager();
        insertMessage(creature.getName() + " gana " + expGain + " puntos de experiencia.");
        sleep(1000);
        while(expGain > 0 && creature.getLevel() < 100)
        {
            if(expGain < cexp.getRemainingToNextLevel())
            {
                cexp.addExperience(expGain);
                modifExpBar((int) (cexp.getCurrentPercentage() * 100));
                expGain = 0;
                break;
            }
            int exp = cexp.getRemainingToNextLevel();
            expGain -= exp;
            modifExpBar(100);
            cexp.addExperience(exp);
            SoundManager.playSound("exp_max");
            sleep(650);
            var stats = new int[12];
            for(int i=0;i<6;i++)
                stats[i] = creature.getStat(StatId.decode(i)).getValue();
            creature.updateAll();
            for(int i=6;i<12;i++)
            {
                stats[i] = creature.getStat(StatId.decode(i - 6)).getValue();
                stats[i - 6] = stats[i] - stats[i - 6];
            }
            updateCreatureInterface(SELF);
            SoundManager.playSound("level_up");
            LevelUpStatComparison.open(this,stats);
            checkLevelAttacksToLearn(creature);
        }
    }
    
    private void checkLevelAttacksToLearn(Creature creature)
    {
        var latts = creature.getRace().getAttackPool().getNormalAttacksInLevel(creature.getLevel());
        if(latts == null || latts.isEmpty())
            return;
        var atts = creature.getAttackManager();
        for(var att : latts)
        {
            if(atts.containsAttack(att))
                continue;
            int slot = atts.getFirstEmptySlot();
            if(slot < 0)
            {
                slot = LearnAttack.open(this, creature, att);
                if(slot < 0)
                    continue;
            }
            atts.setAttack(slot, att);
            insertMessage("¡" + creature.getName() + " ha aprendido \"" + att.getName() + "\"!");
            SoundManager.playSound("level_up");
            sleep(1000);
        }
    }
    
    final void selectActionState()
    {
        clearText();
        insertMessage("¿Que quieres hacer?");
        setButtonsState(ButtonsMenuState.MAIN);
    }
    
    final void executeRound(final BattleAction selfAction)
    {
        setButtonsState(ButtonsMenuState.DISABLED);
        new Thread(() -> {
            var selfState = core.generateFighterState(SELF);
            selfState.action = Objects.requireNonNull(selfAction);
            
            while(!gameOver)
            {
                var enemyState = core.generateFighterState(ENEMY);
                if(core.hasAttackInProgress(enemyState))
                    enemyState.action = BattleAction.CONTINUE_ATTACK;
                else enemyState.selectAttackAction(core.getEnemyIntelligence());

                round(selfState, enemyState);
                
                if(!gameOver && core.hasAttackInProgress(selfState))
                {
                    selfState = core.generateFighterState(SELF);
                    selfState.action = BattleAction.CONTINUE_ATTACK;
                    
                    sleep(1000);
                }
                else break;
            }
            
            if(!gameOver)
                selectActionState();
            else applyGameOver();
        }).start();
    }
    
    private void round(FighterTurnState selfState, FighterTurnState enemyState)
    {
        updateCreatureInterface(SELF);
        updateCreatureInterface(ENEMY);
        
        var currentTeam = SELF;
        
        var selfSpeed = Formula.computeRealSpeed(selfState.self, core.getWeatherId());
        var enemySpeed = Formula.computeRealSpeed(enemyState.self, core.getWeatherId());
        
        if(!selfState.action.hasMorePriority(enemyState.action) && (
                enemyState.action.hasMorePriority(selfState.action) ||
                selfSpeed < enemySpeed ||
                (selfSpeed == enemySpeed && core.getCoreRng().d2(1))))
        {
            currentTeam = currentTeam.invert();
        }
        
        
        for(int i = 0; i < 2; i++)
        {
            var state = currentTeam == SELF ? selfState : enemyState;

            clearText();
            
            switch(state.action)
            {
                case USE_ATTACK1: useAttackAction(state, 0); break;
                case USE_ATTACK2: useAttackAction(state, 1); break;
                case USE_ATTACK3: useAttackAction(state, 2); break;
                case USE_ATTACK4: useAttackAction(state, 3); break;
                case USE_COMBAT: useAttackAction(state, -1); break;
                case CONTINUE_ATTACK: continueAttackAction(state); break;
                case CATCH: applyCatch(state); break;
                case CHANGE: applyChange(state); break;
                case RUN: applyRun(state); break;
            }
            
            updateCreatureInterface(currentTeam);
            updateCreatureInterface(currentTeam.invert());
            state.setTurnToEnd();
            if(!core.isCurrentAlive(SELF) || !core.isCurrentAlive(ENEMY))
            {
                selfState.setTurnToEnd();
                enemyState.setTurnToEnd();
                break;
            }
            currentTeam = currentTeam.invert();
        }
        
        if(core.isCurrentAlive(SELF))
        {
            core.updateAlterations(selfState);
            updateCreatureInterface(SELF);
        }
        if(core.isCurrentAlive(ENEMY))
        {
            core.updateAlterations(enemyState);
            updateCreatureInterface(ENEMY);
        }
        
        core.updateWeather();
        updateCreatureInterface(SELF);
        updateCreatureInterface(ENEMY);
        
        //sleep(2000);
        
        if(core.isGameOver())
        {
            gameOver = true;
        }
        else core.checkChanges();
    }
    
    private void useAttackAction(FighterTurnState state, int attackIndex)
    {
        core.updateAlterations(state);
        if(!state.canAttack())
            return;
        
        var attack = attackIndex < 0 ? AttackPool.createAttack(AttackPool.createCombatAttackModel(state.self))
                : state.self.getAttack(attackIndex);
        if(attack == null)
            attack = AttackPool.createAttack(AttackPool.createCombatAttackModel(state.self));
        
        
        
        core.applyAttack(state, attack);
    }
    
    private void continueAttackAction(FighterTurnState state)
    {
        core.updateAlterations(state);
        if(!state.canAttack())
            return;
        
        core.applyNextTurn(state);
    }
    
    private void applyCatch(FighterTurnState state)
    {
        
    }
    
    private void applyChange(FighterTurnState state)
    {
        core.applyChange(state.self.getFighterId());
    }
    
    private void applyRun(FighterTurnState state)
    {
        
    }
    

    public enum ButtonsMenuState { DISABLED, MAIN, ATTACKS }
    
    public enum BattleAction
    {
        USE_ATTACK1(0),
        USE_ATTACK2(0),
        USE_ATTACK3(0),
        USE_ATTACK4(0),
        USE_COMBAT(0),
        CONTINUE_ATTACK(0),
        CHANGE(1),
        CATCH(1),
        RUN(2);
        
        private final int priority;
        private BattleAction(int priority) { this.priority = priority; }
        
        public final boolean hasMorePriority(BattleAction other)
        {
            return priority > other.priority;
        }
    }
    
    private void applyGameOver()
    {
        clearText();
        var win = core.isSelfWinner();
        result = core.createResult();
        if(win)
        {
            insertMessage("¡Has ganado el combate!");
            sleep(1000);
            insertMessage("¡Has ganado " + result.getMoney() + " de dinero!");
            sleep(1000);
            insertMessage("¡Tu elo se ha incrementado " + result.getElo() + " puntos!");
        }
        else
        {
            insertMessage("Pierdes el combate...");
            sleep(1000);
            insertMessage("Has perdido " + result.getMoney() + " de dinero.");
            sleep(1000);
            insertMessage("Tu elo ha descendido " + result.getElo() + " puntos");
        }
        sleep(2000);
        dispose();
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
        self_lifecount = new javax.swing.JLabel();
        self_lifebar = new javax.swing.JProgressBar();
        self_level = new javax.swing.JLabel();
        self_altered = new javax.swing.JLabel();
        self_race = new javax.swing.JLabel();
        self_name = new javax.swing.JLabel();
        self_expbar = new javax.swing.JProgressBar();
        self_powerup = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        enemy_lifebar = new javax.swing.JProgressBar();
        enemy_level = new javax.swing.JLabel();
        enemy_altered = new javax.swing.JLabel();
        enemy_race = new javax.swing.JLabel();
        enemy_name = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel4 = new javax.swing.JPanel();
        but1 = new javax.swing.JButton();
        but2 = new javax.swing.JButton();
        but3 = new javax.swing.JButton();
        but4 = new javax.swing.JButton();
        but5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        self_lifecount.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        self_lifecount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        self_lifecount.setText("99999/99999");
        self_lifecount.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        self_lifebar.setValue(50);

        self_level.setText("jLabel2");

        self_altered.setText("jLabel3");

        self_race.setText("jLabel4");

        self_name.setText("jLabel5");

        self_expbar.setForeground(new java.awt.Color(51, 255, 255));
        self_expbar.setValue(50);
        self_expbar.setPreferredSize(new java.awt.Dimension(146, 8));

        self_powerup.setText("jLabel6");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(self_lifebar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(self_level)
                            .addComponent(self_name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(self_altered, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(self_race, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(self_expbar, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .addComponent(self_powerup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(self_lifecount, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(self_name)
                    .addComponent(self_race))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(self_level)
                    .addComponent(self_altered))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(self_lifebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(self_expbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(self_powerup)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(self_lifecount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        enemy_lifebar.setValue(50);

        enemy_level.setText("jLabel2");

        enemy_altered.setText("jLabel3");

        enemy_race.setText("jLabel4");

        enemy_name.setText("jLabel5");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enemy_lifebar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(enemy_level)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(enemy_altered))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(enemy_name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(enemy_race)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enemy_name)
                    .addComponent(enemy_race))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enemy_level)
                    .addComponent(enemy_altered))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enemy_lifebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jTextPane1.setFocusable(false);
        jScrollPane1.setViewportView(jTextPane1);

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        but1.setText("jButton1");
        but1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(but1, gridBagConstraints);

        but2.setText("jButton2");
        but2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(but2, gridBagConstraints);

        but3.setText("jButton3");
        but3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(but3, gridBagConstraints);

        but4.setText("jButton4");
        but4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(but4, gridBagConstraints);

        but5.setText("jButton1");
        but5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                but5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(but5, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void but1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but1ActionPerformed
        switch(butState)
        {
            case MAIN: setButtonsState(ButtonsMenuState.ATTACKS); break;
            case ATTACKS: executeRound(BattleAction.USE_ATTACK1); break;
        }
    }//GEN-LAST:event_but1ActionPerformed

    private void but2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but2ActionPerformed
        switch(butState)
        {
            case MAIN: executeRound(BattleAction.USE_COMBAT); break;
            case ATTACKS: executeRound(BattleAction.USE_ATTACK2); break;
        }
    }//GEN-LAST:event_but2ActionPerformed

    private void but3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but3ActionPerformed
        switch(butState)
        {
            case MAIN: {
                if(core.prepareChange(SELF))
                    executeRound(BattleAction.CHANGE);
            } break;
            case ATTACKS: executeRound(BattleAction.USE_ATTACK3); break;
        }
    }//GEN-LAST:event_but3ActionPerformed

    private void but4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but4ActionPerformed
        switch(butState)
        {
            case MAIN: executeRound(BattleAction.CATCH); break;
            case ATTACKS: executeRound(BattleAction.USE_ATTACK4); break;
        }
    }//GEN-LAST:event_but4ActionPerformed

    private void but5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_but5ActionPerformed
        switch(butState)
        {
            case MAIN: executeRound(BattleAction.RUN); break;
            case ATTACKS: setButtonsState(ButtonsMenuState.MAIN); break;
        }
    }//GEN-LAST:event_but5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton but1;
    private javax.swing.JButton but2;
    private javax.swing.JButton but3;
    private javax.swing.JButton but4;
    private javax.swing.JButton but5;
    private javax.swing.JLabel enemy_altered;
    private javax.swing.JLabel enemy_level;
    private javax.swing.JProgressBar enemy_lifebar;
    private javax.swing.JLabel enemy_name;
    private javax.swing.JLabel enemy_race;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel self_altered;
    private javax.swing.JProgressBar self_expbar;
    private javax.swing.JLabel self_level;
    private javax.swing.JProgressBar self_lifebar;
    private javax.swing.JLabel self_lifecount;
    private javax.swing.JLabel self_name;
    private javax.swing.JLabel self_powerup;
    private javax.swing.JLabel self_race;
    // End of variables declaration//GEN-END:variables
}
