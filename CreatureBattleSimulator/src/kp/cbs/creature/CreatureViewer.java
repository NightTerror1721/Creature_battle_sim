/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackSlot;
import kp.cbs.creature.attack.AttackViewer;
import kp.cbs.creature.feat.NormalStat;
import kp.cbs.creature.feat.Stat;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.race.Race;
import kp.cbs.utils.Utils;

/**
 *
 * @author mpasc
 */
public class CreatureViewer extends JDialog
{
    private final Creature creature;
    private final boolean modifyAbilities;
    
    private CreatureViewer(Frame parent, Creature creature, boolean canModifyAbilities)
    {
        super(parent,true);
        initComponents();
        this.creature = Objects.requireNonNull(creature);
        this.modifyAbilities = canModifyAbilities;
        init();
    }
    
    private CreatureViewer(Dialog parent, Creature creature, boolean canModifyAbilities)
    {
        super(parent,true);
        initComponents();
        this.creature = Objects.requireNonNull(creature);
        this.modifyAbilities = canModifyAbilities;
        init();
    }
    
    public static final void open(Window parent, Creature creature, boolean canModifyAbilities)
    {
        if(parent == null)
            new CreatureViewer((Frame) parent, creature, canModifyAbilities).setVisible(true);
        if(parent instanceof Frame)
            new CreatureViewer((Frame) parent, creature, canModifyAbilities).setVisible(true);
        if(parent instanceof Dialog)
            new CreatureViewer((Dialog) parent, creature, canModifyAbilities).setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        restart();
    }
    private void restart()
    {
        setTitle("Visor de Criatura - " + creature.getName());
        
        name.setText("Nombre: " + creature.getName());
        race.setText("Raza: " + creature.getRace().getName());
        types.setText("Tipo: " + creature.getElementalTypeNames());
        cclass.setText("Clase: " + creature.getCreatureClass());
        
        change_name.setEnabled(modifyAbilities);
        change_name.setVisible(modifyAbilities);
        
        psBar.setMinimum(0);
        psBar.setMaximum(creature.getHealthPoints().getMaxHealthPoints());
        psBar.setValue(creature.getHealthPoints().getCurrentHealthPoints());
        psBar.setForeground(computeHealthColor(psBar.getValue(),psBar.getMaximum()));
        psCount.setText(psBar.getValue() + "/" + psBar.getMaximum());
        
        exp.setText(Integer.toString(creature.getExperience()));
        level.setText("Nivel: " + creature.getLevel());
        expBar.setMinimum(0);
        expBar.setMaximum(100);
        expBar.setValue((int) (creature.getExperienceManager().getCurrentPercentage() * 100));
        expBar.setForeground(computeExpColor(creature.getLevel()));
        nextLevelExp.setText("Siguiente nivel: " + creature.getNextLevelExperience());
        
        nature.setText("Naturaleza: " + creature.getNature().getName());
        altered.setText(creature.getAlterationManager().getAbbreviatedInfo());
        
        setStat(hp, creature.getStat(StatId.HEALTH_POINTS));
        setStat(attack, creature.getStat(StatId.ATTACK));
        setStat(defense, creature.getStat(StatId.DEFENSE));
        setStat(spAttack, creature.getStat(StatId.SPECIAL_ATTACK));
        setStat(spDefense, creature.getStat(StatId.SPECIAL_DEFENSE));
        setStat(speed, creature.getStat(StatId.SPEED));
        
        fillAttacks();
        fillAbilityPoints();
        fillRaceBases();
        fillEvos();
    }
    
    private void setStat(JTextField label, Stat stat)
    {
        var modifiable = stat instanceof NormalStat ? (NormalStat) stat : null;
        int value = stat.getValue();
        int modif = modifiable != null ? modifiable.getAlterationLevels() : 0;
        String sufix = modif == 0 || modifiable == null ? "" : " (x" + modifiable.getAlterationRatio() + ")";
        label.setText(value + sufix);
        if(stat.getStatId() != StatId.HEALTH_POINTS)
        {
            float nat = creature.getNature().getStatModificator(stat.getStatId());
            if(nat < 1f)
                label.setForeground(Color.BLUE);
            else if(nat > 1f)
                label.setForeground(Color.RED);
        }
    }
    
    private void fillAttacks()
    {
        var atts = creature.getAttackManager();
        for(var slot : AttackSlot.iterable())
        {
            Attack a = atts.getAttack(slot);
            JButton but = attackButton(slot);
            if(a == null)
            {
                but.setEnabled(false);
                but.setText("----------");
                but.setBackground(Color.WHITE);
                but.setForeground(Color.BLACK);
                continue;
            }
            but.setEnabled(true);
            but.setText(a.getName() + " (" + a.getCurrentPP() + "/" + a.getMaxPP() + ")");
            but.setBackground(a.getElementalType().getColor());
            but.setForeground(a.getElementalType().getFontColor());
        }
    }
    
    private JButton attackButton(AttackSlot slot)
    {
        switch(slot)
        {
            default: throw new IllegalStateException();
            case SLOT_1: return att1;
            case SLOT_2: return att2;
            case SLOT_3: return att3;
            case SLOT_4: return att4;
        }
    }
    
    private void fillAbilityPoints()
    {
        ab_points.setText(Integer.toString(creature.getRemainingAbilityPoints()));
        
        ps_ab.setText(Integer.toString(creature.getHealthPoints().getAbilityPoints()));
        attack_ab.setText(Integer.toString(creature.getAttack().getAbilityPoints()));
        defense_ab.setText(Integer.toString(creature.getDefense().getAbilityPoints()));
        attackSp_ab.setText(Integer.toString(creature.getSpecialAttack().getAbilityPoints()));
        defenseSp_ab.setText(Integer.toString(creature.getSpecialDefense().getAbilityPoints()));
        speed_ab.setText(Integer.toString(creature.getSpeed().getAbilityPoints()));
        
        boolean enabled = modifyAbilities && creature.canGiveAbilityPoints() && creature.getRemainingAbilityPoints() > 0;
        ps_add.setEnabled(enabled);
        attack_add.setEnabled(enabled);
        defense_add.setEnabled(enabled);
        attackSp_add.setEnabled(enabled);
        defenseSp_add.setEnabled(enabled);
        speed_add.setEnabled(enabled);
    }
    
    private void addAbilityPoint(StatId stat)
    {
        creature.useAbilityPoint(stat);
        fillAbilityPoints();
    }
    
    private void fillRaceBases()
    {
        Race crace = creature.getRace();
        setStat(crace.getHealthPointsBase() ,base_ps, hp_p);
        setStat(crace.getAttackBase(), base_attack, attack_p);
        setStat(crace.getDefenseBase(), base_defense, defense_p);
        setStat(crace.getSpecialAttackBase(), base_attackSp, sp_attack_p);
        setStat(crace.getSpecialDefenseBase(), base_defenseSp, sp_defense_p);
        setStat(crace.getSpeedBase(), base_speed, speed_p);
    }
    
    private void fillEvos()
    {
        evosPanel.removeAll();
        if(!modifyAbilities)
        {
            evosPanel.setEnabled(false);
            return;
        }
        
        evosPanel.setEnabled(true);
        var evos = creature.getRace().getEvolutions();
        var len = evos.getEvolutionCount();
        for(var i = 0; i < len; i++)
        {
            final var evo = evos.getEvolution(i);
            var button = new JButton();
            if(evo.check(creature))
            {
                button.setText(evo.getRaceToEvolve().getName());
                button.addActionListener(e -> {
                    var targetRace = evo.getRaceToEvolve();
                    JOptionPane.showMessageDialog(CreatureViewer.this, creature.getName() + " ha evolucionado de la raza " +
                            creature.getRace().getName() + " a " + targetRace.getName());
                    creature.evolve(CreatureViewer.this, evo);
                    restart();
                });
            }
            else
            {
                final var text = evo.getRemainingConditionsToCheck(creature);
                button.setText("?????");
                button.addActionListener(e -> JOptionPane.showMessageDialog(CreatureViewer.this, text,
                        "Condiciones de evolución", JOptionPane.INFORMATION_MESSAGE));
            }
            evosPanel.add(button);
        }
    }
    
    private static void setStat(int stat, JProgressBar bar, JLabel label)
    {
        Color bg = setColor(stat);
        bar.setForeground(bg);
        label.setText("(" + stat + ")");
        bar.setMaximum(255);
        bar.setMinimum(0);
        bar.setValue(stat);
    }
    
    private static Color setColor(int stat)
    {
        if(stat >= 180)
            return Color.RED;
        if(stat >= 140)
            return Color.ORANGE;
        if(stat >= 100)
            return Color.YELLOW;
        if(stat >= 80)
            return Color.GREEN;
        if(stat >= 50)
            return new Color(0,250,125);
        return Color.CYAN;
    }
    
    private static Color computeHealthColor(float current, float max)
    {
        float per = current / max;
        if(per >= 0.5f)
        {
            int r = (int) (-(per - 1f) * 510);
            return new Color(r,255,0);
        }
        int g = (int) (per * 510);
        return new Color(255,g,0);
    }
    
    private static Color computeExpColor(int level)
    {
        if(level < 50)
        {
            int g = (50 - level) * 4;
            return new Color(0,g,250);
        }
        int r = (level - 50) * 3;
        int b = 200 + (level - 50);
        return new Color(r,0,b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        race = new javax.swing.JLabel();
        types = new javax.swing.JLabel();
        nature = new javax.swing.JLabel();
        cclass = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        psBar = new javax.swing.JProgressBar();
        psCount = new javax.swing.JLabel();
        altered = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        exp = new javax.swing.JLabel();
        level = new javax.swing.JLabel();
        expBar = new javax.swing.JProgressBar();
        nextLevelExp = new javax.swing.JLabel();
        change_name = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        att1 = new javax.swing.JButton();
        att2 = new javax.swing.JButton();
        att3 = new javax.swing.JButton();
        att4 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        hp = new javax.swing.JTextField();
        speed = new javax.swing.JTextField();
        attack = new javax.swing.JTextField();
        spAttack = new javax.swing.JTextField();
        defense = new javax.swing.JTextField();
        spDefense = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        ab_points = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        ps_ab = new javax.swing.JLabel();
        ps_add = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        attack_ab = new javax.swing.JLabel();
        attack_add = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        defense_ab = new javax.swing.JLabel();
        defense_add = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        defenseSp_ab = new javax.swing.JLabel();
        defenseSp_add = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        attackSp_ab = new javax.swing.JLabel();
        attackSp_add = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        speed_ab = new javax.swing.JLabel();
        speed_add = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        base_defenseSp = new javax.swing.JProgressBar();
        base_attackSp = new javax.swing.JProgressBar();
        base_speed = new javax.swing.JProgressBar();
        base_defense = new javax.swing.JProgressBar();
        base_attack = new javax.swing.JProgressBar();
        base_ps = new javax.swing.JProgressBar();
        hp_p = new javax.swing.JLabel();
        attack_p = new javax.swing.JLabel();
        defense_p = new javax.swing.JLabel();
        speed_p = new javax.swing.JLabel();
        sp_attack_p = new javax.swing.JLabel();
        sp_defense_p = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        evosPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        name.setText("jLabel1");

        race.setText("jLabel2");

        types.setText("jLabel3");

        nature.setText("jLabel16");

        cclass.setText("jLabel18");

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Salud"));

        psCount.setText("jLabel5");

        altered.setText("jLabel9");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(psBar, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(altered)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(psCount)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(psBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(psCount)
                    .addComponent(altered))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Experiencia"));

        exp.setText("jLabel7");

        level.setText("jLabel6");

        nextLevelExp.setText("jLabel8");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(exp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(level))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(nextLevelExp)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exp)
                    .addComponent(level))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextLevelExp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        change_name.setText("Cambiar Nombre");
        change_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_nameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cclass))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(types)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nature))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(race)
                            .addComponent(change_name))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name)
                    .addComponent(cclass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(race)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(types)
                    .addComponent(nature))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(change_name)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Básico", jPanel1);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataques"));
        jPanel5.setLayout(new java.awt.GridLayout(2, 2));

        att1.setText("jButton2");
        att1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                att1ActionPerformed(evt);
            }
        });
        jPanel5.add(att1);

        att2.setText("jButton3");
        att2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                att2ActionPerformed(evt);
            }
        });
        jPanel5.add(att2);

        att3.setText("jButton4");
        att3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                att3ActionPerformed(evt);
            }
        });
        jPanel5.add(att3);

        att4.setText("jButton5");
        att4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                att4ActionPerformed(evt);
            }
        });
        jPanel5.add(att4);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Stats"));
        jPanel13.setLayout(new java.awt.GridLayout(3, 2));

        hp.setEditable(false);
        hp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hp.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Puntos de Salud", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel13.add(hp);

        speed.setEditable(false);
        speed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        speed.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Velocidad", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel13.add(speed);

        attack.setEditable(false);
        attack.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        attack.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ataque", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel13.add(attack);

        spAttack.setEditable(false);
        spAttack.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        spAttack.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ataque Especial", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel13.add(spAttack);

        defense.setEditable(false);
        defense.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        defense.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Defensa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel13.add(defense);

        spDefense.setEditable(false);
        spDefense.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        spDefense.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Defensa Especial", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel13.add(spDefense);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Características", jPanel2);

        jLabel14.setText("Puntos de habilidad restantes:");

        ab_points.setText("ab_points");

        jLabel15.setText("Salud:");

        ps_ab.setText("----");

        ps_add.setText("+");
        ps_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ps_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ps_ab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ps_add, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ps_add)
                    .addComponent(ps_ab)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setText("Ataque:");

        attack_ab.setText("----");

        attack_add.setText("+");
        attack_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attack_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(attack_ab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attack_add, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attack_add)
                    .addComponent(attack_ab)
                    .addComponent(jLabel17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel19.setText("Defensa:");

        defense_ab.setText("----");

        defense_add.setText("+");
        defense_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defense_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(defense_ab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defense_add, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defense_add)
                    .addComponent(defense_ab)
                    .addComponent(jLabel19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel25.setText("Def. Esp:");

        defenseSp_ab.setText("----");

        defenseSp_add.setText("+");
        defenseSp_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defenseSp_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(defenseSp_ab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defenseSp_add, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defenseSp_add)
                    .addComponent(defenseSp_ab)
                    .addComponent(jLabel25))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel23.setText("At. Esp:");

        attackSp_ab.setText("----");

        attackSp_add.setText("+");
        attackSp_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackSp_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(attackSp_ab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attackSp_add, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attackSp_add)
                    .addComponent(attackSp_ab)
                    .addComponent(jLabel23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel21.setText("Velocid:");

        speed_ab.setText("----");

        speed_add.setText("+");
        speed_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speed_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(speed_ab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(speed_add, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(speed_add)
                    .addComponent(speed_ab)
                    .addComponent(jLabel21))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ab_points)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(ab_points))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(110, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Habilidad", jPanel3);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Caracteristicas"));

        jLabel8.setText("Salud Base:");

        jLabel9.setText("Ataque Base:");

        jLabel10.setText("Defensa Base:");

        jLabel11.setText("Velocidad Base:");

        jLabel12.setText("Ataque Especial Base:");

        jLabel13.setText("Defensa Especial Base:");

        hp_p.setText("(9999)");

        attack_p.setText("(9999)");

        defense_p.setText("(9999)");

        speed_p.setText("(9999)");

        sp_attack_p.setText("(9999)");

        sp_defense_p.setText("(9999)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(base_attackSp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                    .addComponent(base_speed, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(base_defense, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(base_attack, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(base_defenseSp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(base_ps, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(hp_p, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(attack_p, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(defense_p, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(speed_p, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sp_attack_p, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sp_defense_p, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(base_ps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hp_p))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel9)
                                .addComponent(base_attack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(attack_p))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10)
                                .addComponent(base_defense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(defense_p))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11)
                                .addComponent(base_speed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(speed_p))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(base_attackSp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(sp_attack_p))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(base_defenseSp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sp_defense_p))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Evoluciones"));

        evosPanel.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane1.setViewportView(evosPanel);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Raza", jPanel4);

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
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ps_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ps_addActionPerformed
        addAbilityPoint(StatId.HEALTH_POINTS);
    }//GEN-LAST:event_ps_addActionPerformed

    private void attack_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attack_addActionPerformed
        addAbilityPoint(StatId.ATTACK);
    }//GEN-LAST:event_attack_addActionPerformed

    private void defense_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defense_addActionPerformed
        addAbilityPoint(StatId.DEFENSE);
    }//GEN-LAST:event_defense_addActionPerformed

    private void defenseSp_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defenseSp_addActionPerformed
        addAbilityPoint(StatId.SPECIAL_DEFENSE);
    }//GEN-LAST:event_defenseSp_addActionPerformed

    private void attackSp_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackSp_addActionPerformed
        addAbilityPoint(StatId.SPECIAL_ATTACK);
    }//GEN-LAST:event_attackSp_addActionPerformed

    private void speed_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speed_addActionPerformed
        addAbilityPoint(StatId.SPEED);
    }//GEN-LAST:event_speed_addActionPerformed

    private void att1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_att1ActionPerformed
        AttackViewer.open(this, creature.getAttack(AttackSlot.SLOT_1));
    }//GEN-LAST:event_att1ActionPerformed

    private void att2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_att2ActionPerformed
        AttackViewer.open(this, creature.getAttack(AttackSlot.SLOT_2));
    }//GEN-LAST:event_att2ActionPerformed

    private void att3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_att3ActionPerformed
        AttackViewer.open(this, creature.getAttack(AttackSlot.SLOT_3));
    }//GEN-LAST:event_att3ActionPerformed

    private void att4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_att4ActionPerformed
        AttackViewer.open(this, creature.getAttack(AttackSlot.SLOT_4));
    }//GEN-LAST:event_att4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void change_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_nameActionPerformed
        var obj = JOptionPane.showInputDialog(this, "Escribe un nombre para el luchador.", creature.getName());
        if(obj == null || obj.isBlank())
            return;
        creature.setName(obj);
        name.setText("Nombre: " + creature.getName());
    }//GEN-LAST:event_change_nameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ab_points;
    private javax.swing.JLabel altered;
    private javax.swing.JButton att1;
    private javax.swing.JButton att2;
    private javax.swing.JButton att3;
    private javax.swing.JButton att4;
    private javax.swing.JTextField attack;
    private javax.swing.JLabel attackSp_ab;
    private javax.swing.JButton attackSp_add;
    private javax.swing.JLabel attack_ab;
    private javax.swing.JButton attack_add;
    private javax.swing.JLabel attack_p;
    private javax.swing.JProgressBar base_attack;
    private javax.swing.JProgressBar base_attackSp;
    private javax.swing.JProgressBar base_defense;
    private javax.swing.JProgressBar base_defenseSp;
    private javax.swing.JProgressBar base_ps;
    private javax.swing.JProgressBar base_speed;
    private javax.swing.JLabel cclass;
    private javax.swing.JButton change_name;
    private javax.swing.JTextField defense;
    private javax.swing.JLabel defenseSp_ab;
    private javax.swing.JButton defenseSp_add;
    private javax.swing.JLabel defense_ab;
    private javax.swing.JButton defense_add;
    private javax.swing.JLabel defense_p;
    private javax.swing.JPanel evosPanel;
    private javax.swing.JLabel exp;
    private javax.swing.JProgressBar expBar;
    private javax.swing.JTextField hp;
    private javax.swing.JLabel hp_p;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel level;
    private javax.swing.JLabel name;
    private javax.swing.JLabel nature;
    private javax.swing.JLabel nextLevelExp;
    private javax.swing.JProgressBar psBar;
    private javax.swing.JLabel psCount;
    private javax.swing.JLabel ps_ab;
    private javax.swing.JButton ps_add;
    private javax.swing.JLabel race;
    private javax.swing.JTextField spAttack;
    private javax.swing.JTextField spDefense;
    private javax.swing.JLabel sp_attack_p;
    private javax.swing.JLabel sp_defense_p;
    private javax.swing.JTextField speed;
    private javax.swing.JLabel speed_ab;
    private javax.swing.JButton speed_add;
    private javax.swing.JLabel speed_p;
    private javax.swing.JLabel types;
    // End of variables declaration//GEN-END:variables
}
