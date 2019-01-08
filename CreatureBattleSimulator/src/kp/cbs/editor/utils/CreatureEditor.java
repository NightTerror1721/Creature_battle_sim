/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import kp.cbs.battle.prop.CreatureProperties;
import kp.cbs.creature.Nature;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackSlot;
import kp.cbs.creature.attack.RaceAttackPool.RaceAttack;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.race.Race;
import kp.cbs.creature.race.RacePool;
import static kp.cbs.editor.AttackEditor.generateIntegerRange;
import kp.cbs.editor.BattleEditor;
import kp.cbs.utils.Formula;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class CreatureEditor extends JDialog
{
    private CreatureProperties creature;
    private boolean computeStatistics;
    
    public CreatureEditor(BattleEditor parent)
    {
        super(parent, true);
        initComponents();
        init();
    }
    
    public final void open(CreatureProperties creature)
    {
        this.creature = Objects.requireNonNull(creature);
        reset();
        Utils.focus(this);
        setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        
        final var levels = generateIntegerRange(1, 100, 1);
        final var abs = generateIntegerRange(0, 512, 1);
        final var gens = generateIntegerRange(0, 32, 1);
        
        race.setModel(new DefaultComboBoxModel<>(getRaces()));
        nature.setModel(new DefaultComboBoxModel<>(Nature.values()));
        levelMin.setModel(new DefaultComboBoxModel<>(levels));
        levelMax.setModel(new DefaultComboBoxModel<>(levels));
        
        maxHidden.setModel(new DefaultComboBoxModel<>(generateIntegerRange(0, 4, 1)));
        att1.setModel(new DefaultComboBoxModel<>());
        att2.setModel(new DefaultComboBoxModel<>());
        att3.setModel(new DefaultComboBoxModel<>());
        att4.setModel(new DefaultComboBoxModel<>());
        
        uniqueAbValue.setModel(new DefaultComboBoxModel<>(abs));
        hpAb.setModel(new DefaultComboBoxModel<>(abs));
        attackAb.setModel(new DefaultComboBoxModel<>(abs));
        defenseAb.setModel(new DefaultComboBoxModel<>(abs));
        spAttackAb.setModel(new DefaultComboBoxModel<>(abs));
        spDefenseAb.setModel(new DefaultComboBoxModel<>(abs));
        speedAb.setModel(new DefaultComboBoxModel<>(abs));
        
        hpGen.setModel(new DefaultComboBoxModel<>(gens));
        attackGen.setModel(new DefaultComboBoxModel<>(gens));
        defenseGen.setModel(new DefaultComboBoxModel<>(gens));
        spAttackGen.setModel(new DefaultComboBoxModel<>(gens));
        spDefenseGen.setModel(new DefaultComboBoxModel<>(gens));
        speedGen.setModel(new DefaultComboBoxModel<>(gens));
    }
    
    private void clear()
    {
        computeStatistics = false;
        
        race.setSelectedIndex(0);
        name.setText("");
        nature.setSelectedItem(Nature.UNDEFINED);
        levelMin.setSelectedItem(1);
        levelMax.setSelectedItem(1);
        
        setCheckBoxState(levelAtts, true);
        
        setCheckBoxState(uniqueAb, true);
        hpAb.setSelectedItem(0);
        attackAb.setSelectedItem(0);
        defenseAb.setSelectedItem(0);
        spAttackAb.setSelectedItem(0);
        spDefenseAb.setSelectedItem(0);
        speedAb.setSelectedItem(0);
        uniqueAbValue.setSelectedItem(0);
        
        setCheckBoxState(randomGen, true);
        hpGen.setSelectedItem(0);
        attackGen.setSelectedItem(0);
        defenseGen.setSelectedItem(0);
        spAttackGen.setSelectedItem(0);
        spDefenseGen.setSelectedItem(0);
        speedGen.setSelectedItem(0);
        
        computeStatistics = true;
        computeStatistics();
        
    }
    private static void setCheckBoxState(JCheckBox box, boolean state)
    {
        box.setSelected(!state);
        box.doClick();
    }
    
    private void reset()
    {
        clear();
        if(creature.getRace() == null)
            return;
        
        computeStatistics = false;
        
        race.setSelectedItem(creature.getRace());
        name.setText(creature.getName());
        nature.setSelectedItem(creature.getNature());
        levelMin.setSelectedItem(creature.getMinLevel());
        levelMax.setSelectedItem(creature.getMaxLevel());
        
        setCheckBoxState(levelAtts, creature.isLevelAttacks());
        maxHidden.setSelectedItem(creature.getMaxHiddenAttacks());
        att1.setSelectedItem(creature.getRaceAttack(AttackSlot.SLOT_1));
        att2.setSelectedItem(creature.getRaceAttack(AttackSlot.SLOT_2));
        att3.setSelectedItem(creature.getRaceAttack(AttackSlot.SLOT_3));
        att4.setSelectedItem(creature.getRaceAttack(AttackSlot.SLOT_4));
        
        setCheckBoxState(uniqueAb, creature.hasUniqueAbility());
        uniqueAbValue.setSelectedItem(creature.getUniqueAbilityValue());
        hpAb.setSelectedItem(creature.getHpAbility());
        attackAb.setSelectedItem(creature.getAttackAbility());
        defenseAb.setSelectedItem(creature.getDefenseAbility());
        spAttackAb.setSelectedItem(creature.getSpecialAttackAbility());
        spDefenseAb.setSelectedItem(creature.getSpecialDefenseAbility());
        speedAb.setSelectedItem(creature.getSpeedAbility());
        
        setCheckBoxState(randomGen, creature.isRandomGenetic());
        hpGen.setSelectedItem(creature.getHpGenetic());
        attackGen.setSelectedItem(creature.getAttackGenetic());
        defenseGen.setSelectedItem(creature.getDefenseGenetic());
        spAttackGen.setSelectedItem(creature.getSpecialAttackGenetic());
        spDefenseGen.setSelectedItem(creature.getSpecialDefenseGenetic());
        speedGen.setSelectedItem(creature.getSpeedGenetic());
        
        computeStatistics = true;
        computeStatistics();
    }
    
    
    private void findAttacks()
    {
        att1.removeAllItems();
        att2.removeAllItems();
        att3.removeAllItems();
        att4.removeAllItems();
        
        var sel = race.getSelectedIndex();
        if(sel < 0)
            return;
        
        final var selRace = (Race) race.getSelectedItem();
        final var atts = selRace.getAttackPool().getAllAttacksInArray();
        
        att1.setModel(new DefaultComboBoxModel<>(atts));
        att2.setModel(new DefaultComboBoxModel<>(atts));
        att3.setModel(new DefaultComboBoxModel<>(atts));
        att4.setModel(new DefaultComboBoxModel<>(atts));
    }
    
    private void computeAbilityPoints()
    {
        int points;
        if(uniqueAb.isSelected())
            points = value(uniqueAbValue) * 6;
        else points = value(hpAb) + value(attackAb) + value(defenseAb) +
                value(spAttackAb) + value(spDefenseAb) + value(speedAb);
        jTextField1.setText(Integer.toString(points));
        
        computeStatistics();
    }
    
    private void computeStatistics()
    {
        if(!computeStatistics)
            return;
        
        var abs = testAb.isSelected();
        var gens = testGen.isSelected() && !randomGen.isSelected();
        var level = testLevel.isSelected() ? value(levelMax, 1) : value(levelMin, 1);
        
        computeStat(StatId.HEALTH_POINTS, testHp, abs ? hpAb : null, gens ? hpGen : null, level);
        computeStat(StatId.ATTACK, testAttack, abs ? attackAb : null, gens ? attackGen : null, level);
        computeStat(StatId.DEFENSE, testDefense, abs ? defenseAb : null, gens ? defenseGen : null, level);
        computeStat(StatId.SPECIAL_ATTACK, testSpAttack, abs ? spAttackAb : null, gens ? spAttackGen : null, level);
        computeStat(StatId.SPECIAL_DEFENSE, testSpDefense, abs ? spDefenseAb : null, gens ? spDefenseGen : null, level);
        computeStat(StatId.SPEED, testSpeed, abs ? speedAb : null, gens ? speedGen : null, level);
    }
    private void computeStat(StatId stat, JTextField field, JComboBox<Integer> abs, JComboBox<Integer> gens, int level)
    {
        var selRace = (Race) race.getSelectedItem();
        var base = selRace.getBase(stat);
        var gen = gens == null ? 0 : value(gens);
        var ab = abs == null ? 0 : uniqueAb.isSelected() ? value(uniqueAbValue) : value(abs);
        var nat = (Nature) nature.getSelectedItem();
        var value = stat == StatId.HEALTH_POINTS
                ? Formula.hpValue(level, base, gen, ab)
                : Formula.statValue(stat, level, base, gen, ab, nat, 0);
        field.setText(Integer.toString(value));
    }
    
    private void store()
    {
        if(creature == null)
            return;
        try
        {
            creature.setRace((Race) race.getSelectedItem());
            creature.setName(name.getText());
            creature.setNature((Nature) nature.getSelectedItem());
            creature.setMinLevel(value(levelMin, 1));
            creature.setMaxLevel(value(levelMax, 1));
            
            var flag = levelAtts.isSelected();
            creature.setLevelAttacks(flag);
            creature.setMaxHiddenAttacks(flag ? value(maxHidden) : 0);
            creature.setAttack(AttackSlot.SLOT_1, attack(att1));
            creature.setAttack(AttackSlot.SLOT_2, attack(att2));
            creature.setAttack(AttackSlot.SLOT_3, attack(att3));
            creature.setAttack(AttackSlot.SLOT_4, attack(att4));
            
            flag = uniqueAb.isSelected();
            if(flag)
                creature.setUniqueAbilityValue(value(uniqueAbValue));
            else creature.dissableUniqueAbility();
            creature.setHpAbility(flag ? 0 : value(hpAb));
            creature.setAttackAbility(flag ? 0 : value(attackAb));
            creature.setDefenseAbility(flag ? 0 : value(defenseAb));
            creature.setSpecialAttackAbility(flag ? 0 : value(spAttackAb));
            creature.setSpecialDefenseAbility(flag ? 0 : value(spDefenseAb));
            creature.setSpeedAbility(flag ? 0 : value(speedAb));
            
            flag = randomGen.isSelected();
            creature.setRandomGenetic(flag);
            creature.setHpGenetic(flag ? 0 : value(hpGen));
            creature.setAttackGenetic(flag ? 0 : value(attackGen));
            creature.setDefenseGenetic(flag ? 0 : value(defenseGen));
            creature.setSpecialAttackGenetic(flag ? 0 : value(spAttackGen));
            creature.setSpecialDefenseGenetic(flag ? 0 : value(spDefenseGen));
            creature.setSpeedGenetic(flag ? 0 : value(speedGen));
            
        }
        catch(Exception ex) { ex.printStackTrace(System.err); }
    }
    
    private static int value(JComboBox<Integer> box, int defaultValue)
    {
        try { return ((Number) box.getSelectedItem()).intValue(); }
        catch(Throwable ex) { return defaultValue; }
    }
    private static int value(JComboBox<Integer> box) { return value(box, 0); }
    
    private static AttackModel attack(JComboBox<RaceAttack> box)
    {
        try { return ((RaceAttack) box.getSelectedItem()).getAttackModel(); }
        catch(Throwable ex) { return null; }
    }
    
    private static Race[] getRaces()
    {
        var races = RacePool.getAllRaces(true);
        races.sort((r0, r1) -> Integer.compare(r0.getId(), r1.getId()));
        return races.toArray(Race[]::new);
    }
    
    @Override
    public final void dispose()
    {
        store();
        creature = null;
        super.dispose();
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
        race = new javax.swing.JComboBox<>();
        name = new javax.swing.JTextField();
        nature = new javax.swing.JComboBox<>();
        levelMin = new javax.swing.JComboBox<>();
        levelMax = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        uniqueAb = new javax.swing.JCheckBox();
        uniqueAbValue = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        hpAb = new javax.swing.JComboBox<>();
        attackAb = new javax.swing.JComboBox<>();
        defenseAb = new javax.swing.JComboBox<>();
        speedAb = new javax.swing.JComboBox<>();
        spAttackAb = new javax.swing.JComboBox<>();
        spDefenseAb = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        levelAtts = new javax.swing.JCheckBox();
        maxHidden = new javax.swing.JComboBox<>();
        att1 = new javax.swing.JComboBox<>();
        att2 = new javax.swing.JComboBox<>();
        att3 = new javax.swing.JComboBox<>();
        att4 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        randomGen = new javax.swing.JCheckBox();
        hpGen = new javax.swing.JComboBox<>();
        attackGen = new javax.swing.JComboBox<>();
        defenseGen = new javax.swing.JComboBox<>();
        speedGen = new javax.swing.JComboBox<>();
        spAttackGen = new javax.swing.JComboBox<>();
        spDefenseGen = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        testAb = new javax.swing.JCheckBox();
        testGen = new javax.swing.JCheckBox();
        testLevel = new javax.swing.JCheckBox();
        testHp = new javax.swing.JTextField();
        testAttack = new javax.swing.JTextField();
        testDefense = new javax.swing.JTextField();
        testSpeed = new javax.swing.JTextField();
        testSpAttack = new javax.swing.JTextField();
        testSpDefense = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor de Criaturas");
        getContentPane().setLayout(new java.awt.GridLayout(3, 2));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Basico"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        race.setBorder(javax.swing.BorderFactory.createTitledBorder("Raza"));
        race.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                raceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(race, gridBagConstraints);

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(name, gridBagConstraints);

        nature.setBorder(javax.swing.BorderFactory.createTitledBorder("Naturaleza"));
        nature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                natureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(nature, gridBagConstraints);

        levelMin.setBorder(javax.swing.BorderFactory.createTitledBorder("Nivel Min"));
        levelMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelMinActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(levelMin, gridBagConstraints);

        levelMax.setToolTipText("");
        levelMax.setBorder(javax.swing.BorderFactory.createTitledBorder("Nivel Max"));
        levelMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelMaxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(levelMax, gridBagConstraints);

        getContentPane().add(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Esfuerzo"));
        jPanel2.setLayout(new java.awt.GridLayout(3, 3));

        uniqueAb.setText("Valor Único");
        uniqueAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniqueAbActionPerformed(evt);
            }
        });
        jPanel2.add(uniqueAb);

        uniqueAbValue.setBorder(javax.swing.BorderFactory.createTitledBorder("Esfuerzo"));
        uniqueAbValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniqueAbValueActionPerformed(evt);
            }
        });
        jPanel2.add(uniqueAbValue);

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Esfuerzo Total", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.add(jTextField1);

        hpAb.setBorder(javax.swing.BorderFactory.createTitledBorder("Puntos Salud"));
        hpAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hpAbActionPerformed(evt);
            }
        });
        jPanel2.add(hpAb);

        attackAb.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque"));
        attackAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackAbActionPerformed(evt);
            }
        });
        jPanel2.add(attackAb);

        defenseAb.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa"));
        defenseAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defenseAbActionPerformed(evt);
            }
        });
        jPanel2.add(defenseAb);

        speedAb.setBorder(javax.swing.BorderFactory.createTitledBorder("Velocidad"));
        speedAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedAbActionPerformed(evt);
            }
        });
        jPanel2.add(speedAb);

        spAttackAb.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque Esp."));
        spAttackAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spAttackAbActionPerformed(evt);
            }
        });
        jPanel2.add(spAttackAb);

        spDefenseAb.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa Esp."));
        spDefenseAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spDefenseAbActionPerformed(evt);
            }
        });
        jPanel2.add(spDefenseAb);

        getContentPane().add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataques"));
        jPanel3.setLayout(new java.awt.GridLayout(3, 2));

        levelAtts.setText("Usar Ataques por Nivel");
        levelAtts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelAttsActionPerformed(evt);
            }
        });
        jPanel3.add(levelAtts);

        maxHidden.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataques Ocultos Máx"));
        jPanel3.add(maxHidden);

        att1.setBorder(javax.swing.BorderFactory.createTitledBorder("Slot 1"));
        jPanel3.add(att1);

        att2.setBorder(javax.swing.BorderFactory.createTitledBorder("Slot 2"));
        jPanel3.add(att2);

        att3.setBorder(javax.swing.BorderFactory.createTitledBorder("Slot 3"));
        jPanel3.add(att3);

        att4.setBorder(javax.swing.BorderFactory.createTitledBorder("Slot 4"));
        jPanel3.add(att4);

        getContentPane().add(jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Genética"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        randomGen.setText("Genética Aleatória");
        randomGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(randomGen, gridBagConstraints);

        hpGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Puntos Salud"));
        hpGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hpGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(hpGen, gridBagConstraints);

        attackGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque"));
        attackGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(attackGen, gridBagConstraints);

        defenseGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa"));
        defenseGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defenseGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(defenseGen, gridBagConstraints);

        speedGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Velocidad"));
        speedGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(speedGen, gridBagConstraints);

        spAttackGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque Esp."));
        spAttackGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spAttackGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(spAttackGen, gridBagConstraints);

        spDefenseGen.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa Esp."));
        spDefenseGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spDefenseGenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(spDefenseGen, gridBagConstraints);

        getContentPane().add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Estadísticas"));
        jPanel5.setLayout(new java.awt.GridLayout(3, 3));

        testAb.setSelected(true);
        testAb.setText("Esfuerzo");
        testAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testAbActionPerformed(evt);
            }
        });
        jPanel5.add(testAb);

        testGen.setSelected(true);
        testGen.setText("Genética");
        testGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testGenActionPerformed(evt);
            }
        });
        jPanel5.add(testGen);

        testLevel.setText("Usar nivel max");
        testLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testLevelActionPerformed(evt);
            }
        });
        jPanel5.add(testLevel);

        testHp.setEditable(false);
        testHp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        testHp.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Puntos Salud", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.add(testHp);

        testAttack.setEditable(false);
        testAttack.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        testAttack.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ataque", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.add(testAttack);

        testDefense.setEditable(false);
        testDefense.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        testDefense.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Defensa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.add(testDefense);

        testSpeed.setEditable(false);
        testSpeed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        testSpeed.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Velocidad", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.add(testSpeed);

        testSpAttack.setEditable(false);
        testSpAttack.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        testSpAttack.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ataque Esp.", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.add(testSpAttack);

        testSpDefense.setEditable(false);
        testSpDefense.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        testSpDefense.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Defensa Esp.", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.add(testSpDefense);

        getContentPane().add(jPanel5);

        jButton1.setText("Guardar y Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Resetear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Limpiar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        getContentPane().add(jPanel6);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void raceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_raceActionPerformed
        findAttacks();
        computeStatistics();
    }//GEN-LAST:event_raceActionPerformed

    private void levelAttsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelAttsActionPerformed
        var sel = levelAtts.isSelected();
        maxHidden.setEnabled(sel);
        att1.setEnabled(!sel);
        att2.setEnabled(!sel);
        att3.setEnabled(!sel);
        att4.setEnabled(!sel);
    }//GEN-LAST:event_levelAttsActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        reset();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void uniqueAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniqueAbActionPerformed
        var sel = uniqueAb.isSelected();
        hpAb.setEnabled(!sel);
        attackAb.setEnabled(!sel);
        defenseAb.setEnabled(!sel);
        spAttackAb.setEnabled(!sel);
        spDefenseAb.setEnabled(!sel);
        speedAb.setEnabled(!sel);
        uniqueAbValue.setEnabled(sel);
        
        computeAbilityPoints();
    }//GEN-LAST:event_uniqueAbActionPerformed

    private void uniqueAbValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniqueAbValueActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_uniqueAbValueActionPerformed

    private void hpAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hpAbActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_hpAbActionPerformed

    private void attackAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackAbActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_attackAbActionPerformed

    private void defenseAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defenseAbActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_defenseAbActionPerformed

    private void speedAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedAbActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_speedAbActionPerformed

    private void spAttackAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spAttackAbActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_spAttackAbActionPerformed

    private void spDefenseAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spDefenseAbActionPerformed
        computeAbilityPoints();
    }//GEN-LAST:event_spDefenseAbActionPerformed

    private void randomGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomGenActionPerformed
        var sel = randomGen.isSelected();
        hpGen.setEnabled(!sel);
        attackGen.setEnabled(!sel);
        defenseGen.setEnabled(!sel);
        spAttackGen.setEnabled(!sel);
        spDefenseGen.setEnabled(!sel);
        speedGen.setEnabled(!sel);
        
        computeStatistics();
    }//GEN-LAST:event_randomGenActionPerformed

    private void hpGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hpGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_hpGenActionPerformed

    private void attackGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_attackGenActionPerformed

    private void defenseGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defenseGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_defenseGenActionPerformed

    private void speedGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_speedGenActionPerformed

    private void spAttackGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spAttackGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_spAttackGenActionPerformed

    private void spDefenseGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spDefenseGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_spDefenseGenActionPerformed

    private void testAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testAbActionPerformed
        computeStatistics();
    }//GEN-LAST:event_testAbActionPerformed

    private void testGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testGenActionPerformed
        computeStatistics();
    }//GEN-LAST:event_testGenActionPerformed

    private void testLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testLevelActionPerformed
        computeStatistics();
    }//GEN-LAST:event_testLevelActionPerformed

    private void natureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_natureActionPerformed
        computeStatistics();
    }//GEN-LAST:event_natureActionPerformed

    private void levelMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelMinActionPerformed
        computeStatistics();
    }//GEN-LAST:event_levelMinActionPerformed

    private void levelMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelMaxActionPerformed
        computeStatistics();
    }//GEN-LAST:event_levelMaxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clear();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<RaceAttack> att1;
    private javax.swing.JComboBox<RaceAttack> att2;
    private javax.swing.JComboBox<RaceAttack> att3;
    private javax.swing.JComboBox<RaceAttack> att4;
    private javax.swing.JComboBox<Integer> attackAb;
    private javax.swing.JComboBox<Integer> attackGen;
    private javax.swing.JComboBox<Integer> defenseAb;
    private javax.swing.JComboBox<Integer> defenseGen;
    private javax.swing.JComboBox<Integer> hpAb;
    private javax.swing.JComboBox<Integer> hpGen;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JCheckBox levelAtts;
    private javax.swing.JComboBox<Integer> levelMax;
    private javax.swing.JComboBox<Integer> levelMin;
    private javax.swing.JComboBox<Integer> maxHidden;
    private javax.swing.JTextField name;
    private javax.swing.JComboBox<Nature> nature;
    private javax.swing.JComboBox<Race> race;
    private javax.swing.JCheckBox randomGen;
    private javax.swing.JComboBox<Integer> spAttackAb;
    private javax.swing.JComboBox<Integer> spAttackGen;
    private javax.swing.JComboBox<Integer> spDefenseAb;
    private javax.swing.JComboBox<Integer> spDefenseGen;
    private javax.swing.JComboBox<Integer> speedAb;
    private javax.swing.JComboBox<Integer> speedGen;
    private javax.swing.JCheckBox testAb;
    private javax.swing.JTextField testAttack;
    private javax.swing.JTextField testDefense;
    private javax.swing.JCheckBox testGen;
    private javax.swing.JTextField testHp;
    private javax.swing.JCheckBox testLevel;
    private javax.swing.JTextField testSpAttack;
    private javax.swing.JTextField testSpDefense;
    private javax.swing.JTextField testSpeed;
    private javax.swing.JCheckBox uniqueAb;
    private javax.swing.JComboBox<Integer> uniqueAbValue;
    // End of variables declaration//GEN-END:variables
}
