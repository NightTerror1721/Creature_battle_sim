/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import kp.cbs.creature.Growth;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackPool;
import kp.cbs.creature.attack.RaceAttackPool;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.race.Evolution;
import kp.cbs.creature.race.Evolution.EvolveCondition;
import kp.cbs.creature.race.Race;
import kp.cbs.creature.race.RaceEvolutions;
import kp.cbs.creature.race.RacePool;
import kp.cbs.editor.utils.EvolveStatSelection;
import kp.cbs.utils.Formula;
import kp.cbs.utils.SortableListModel;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class RaceEditor extends JDialog
{
    private Race loaded;
    
    private RaceEditor(MainMenuEditor parent)
    {
        super(parent, false);
        initComponents();
        init();
    }
    
    public static final void open(MainMenuEditor parent)
    {
        var editor = new RaceEditor(parent);
        editor.setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        type1.setModel(new DefaultComboBoxModel<>(ElementalType.getAllElementalTypes()));
        type2.setModel(new DefaultComboBoxModel<>(ElementalType.getAllElementalTypes()));
        
        hp.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 999, 1)));
        attack.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 999, 1)));
        defense.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 999, 1)));
        spAttack.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 999, 1)));
        spDefense.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 999, 1)));
        speed.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 999, 1)));
        
        growth.setModel(new DefaultComboBoxModel<>(Growth.values()));
        
        var atts = AttackPool.getAllModels(true).stream()
                .sorted((a0, a1) -> a0.getName().compareTo(a1.getName()))
                .toArray(AttackModel[]::new);
        innateAttack.setModel(new DefaultComboBoxModel<>(atts));
        add_attack.setModel(new DefaultComboBoxModel<>(atts));
        mod_attack.setModel(new DefaultComboBoxModel<>(atts));
        
        add_level.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 100, 1)));
        mod_level.setModel(new DefaultComboBoxModel<>(AttackEditor.generateIntegerRange(1, 100, 1)));
        
        attacks.setModel(new SortableListModel<>());
        evolves.setModel(new DefaultComboBoxModel<>());
        conditions.setModel(new DefaultListModel<>());
        
        createNew();
    }
    
    private void updateStatAmount()
    {
        int sum = value(hp) + value(attack) + value(defense) + value(spAttack) + value(spDefense) + value(speed);
        int ab = Formula.creatureStatAbilityBase(sum);
        int exp = Formula.creatureExpBase(sum);
        statAmount.setText("Exp. Base: [" + exp + "]; Puntos de Habilidad: [" + ab + "]; Suma de Stats: [" + sum + "]");
    }
    
    private void createCondition(Evolution.EvolutionConditionType type)
    {
        var sel = evolves.getSelectedIndex();
        if(sel < 0)
            return;
        var evo = (Evolution) evolves.getSelectedItem();
        
        EvolveCondition cond = null;
        switch(type)
        {
            case LEVEL: cond = new Evolution.LevelEvolveCondition(); break;
            case STAT_GREATER_THAN: cond = new Evolution.GreaterStatEvolveCondition(); break;
            case STAT_EQUALS_TO: cond = new Evolution.EqualsStatEvolveCondition(); break;
            case LEARNED_ATTACK: cond = new Evolution.LearnedAttackEvolveCondition(); break;
        }
        
        if(cond != null)
        {
            editCondition(cond);
            conditionsModel().addElement(cond);
            evo.addCondition(cond);
            conditions.repaint();
        }
    }
    
    private void editCondition(EvolveCondition cond)
    {
        switch(cond.getType())
        {
            case LEVEL: {
                var obj = JOptionPane.showInputDialog(this, "¿En qué nivel evolucionará?",
                        "Condición de Nivel", JOptionPane.QUESTION_MESSAGE, null,
                        AttackEditor.generateIntegerRange(1, 100, 1), 1);
                if(obj == null)
                    return;
                ((Evolution.LevelEvolveCondition) cond).setLevel(((Number) obj).intValue());
            } break;
            case LEARNED_ATTACK: {
                var atts = AttackPool.getAllModels(true).toArray(AttackModel[]::new);
                if(atts.length < 1)
                    return;
                var obj = JOptionPane.showInputDialog(this, "¿Qué ataque debe conocer?",
                        "Condición de Ataque", JOptionPane.QUESTION_MESSAGE, null,
                        atts, atts[0]);
                if(obj == null)
                    return;
                ((Evolution.LearnedAttackEvolveCondition) cond).setAttack((AttackModel) obj);
            }
            case STAT_GREATER_THAN:
            case STAT_EQUALS_TO:
                EvolveStatSelection.open(this, cond);
                break;
        }
    }
    
    private void updateTitle()
    {
        final String base = "Editor de Razas - ";
        if(loaded == null)
            setTitle(base + "Nueva Raza");
        else setTitle(base + "[" + loaded.getId() + "]: " + loaded.getName());
    }
    
    private void createNew()
    {
        name.setText("");
        type1.setSelectedItem(ElementalType.NORMAL);
        type2.setSelectedItem(ElementalType.NORMAL);
        hp.setSelectedItem(1);
        attack.setSelectedItem(1);
        defense.setSelectedItem(1);
        spAttack.setSelectedItem(1);
        spDefense.setSelectedItem(1);
        speed.setSelectedItem(1);
        growth.setSelectedItem(Growth.NORMAL);
        innateAttack.setSelectedItem(AttackPool.getModel(0));
        attacksModel().clear();
        conditionsModel().clear();
        evolves.removeAllItems();
        
        loaded = null;
        updateTitle();
    }
    
    private void load()
    {
        loaded = null;
        var allRaces = RacePool.getAllRaces(true);
        if(allRaces.isEmpty())
            return;
        allRaces.sort((r0, r1) -> r0.getName().compareTo(r1.getName()));
        var sel = JOptionPane.showInputDialog(this, "¿Que raza quieres cargar?",
                "Cargar Raza", JOptionPane.QUESTION_MESSAGE, null,
                allRaces.toArray(), allRaces.get(0));
        if(sel == null)
            return;
        
        var race = (Race) sel;
        createNew();
        expandRace(loaded = race);
        updateTitle();
    }
    
    private void store()
    {
        var race = generateRace();
        if(loaded != null)
            race.setId(loaded.getId());
        if(RacePool.registerNewOrUpdateRace(race, loaded == null))
        {
            loaded = race;
            updateTitle();
            JOptionPane.showMessageDialog(this, "¡La raza ha sido guardada con éxito!",
                    "Guardar Raza", JOptionPane.INFORMATION_MESSAGE);
        }
        else JOptionPane.showMessageDialog(this, "Ha habido un fallo al guardar la raza.",
                "Guardar Raza", JOptionPane.ERROR_MESSAGE);
    }
    
    private void createCloned()
    {
        if(loaded != null)
        {
            JOptionPane.showMessageDialog(this, "¡Se ha clonado la raza!",
                    "Clonar Raza", JOptionPane.INFORMATION_MESSAGE);
            loaded = null;
            updateTitle();
        }
    }
    
    private void expandRace(Race race)
    {
        name.setText(race.getName());
        type1.setSelectedItem(race.getPrimaryType());
        type2.setSelectedItem(race.getSecondaryType());
        
        hp.setSelectedItem(race.getHealthPointsBase());
        attack.setSelectedItem(race.getAttackBase());
        defense.setSelectedItem(race.getDefenseBase());
        spAttack.setSelectedItem(race.getSpecialAttackBase());
        spDefense.setSelectedItem(race.getSpecialDefenseBase());
        speed.setSelectedItem(race.getSpeedBase());
        
        growth.setSelectedItem(race.getGrowth());
        
        var atts = race.getAttackPool();
        innateAttack.setSelectedItem(Objects.requireNonNullElse(atts.getInnateAttack(), AttackPool.getModel(0)));
        atts.streamAllAttacks().forEach(att -> {
            attacksModel().addElement(new AttackObject(att.getAttackModel(), att.getLevel(), att.isHidden()));
        });
        attacksModel().sort();
        
        var evos = race.getEvolutions();
        int len = evos.getEvolutionCount();
        for(int i=0;i<len;i++)
            evolves.addItem(evos.getEvolution(i));
    }
    
    private Race generateRace()
    {
        Race race = new Race();
        
        race.setName(name.getText());
        race.setPrimaryType((ElementalType) type1.getSelectedItem());
        race.setSecondaryType((ElementalType) type2.getSelectedItem());
        
        race.setHpBase(value(hp));
        race.setAttBase(value(attack));
        race.setDefBase(value(defense));
        race.setSpAttBase(value(spAttack));
        race.setSpDefBase(value(spDefense));
        race.setSpdBase(value(speed));
        
        race.setGrowth((Growth) growth.getSelectedItem());
        
        var atts = RaceAttackPool.instance();
        race.setAttackPool(atts);
        atts.setInnateAttack((AttackModel) innateAttack.getSelectedItem());
        attacksModel().elements().forEach((att) -> {
            atts.registerAttack(att.getLevel(), att.getAttack(), att.isHidden());
        });
        
        var evos = new RaceEvolutions();
        race.setEvolutions(evos);
        
        int len = evolves.getItemCount();
        for(int i=0;i<len;i++)
        {
            var evo = evolves.getItemAt(i);
            evos.addEvolution(evo);
        }
        
        return race;
    }
    
    private static int value(JComboBox<Integer> box)
    {
        try { return ((Number) box.getSelectedItem()).intValue(); }
        catch(Throwable ex) { return 0; }
    }
    
    private static void rangeValue(JComboBox<Integer> box, int min, int max)
    {
        int value = value(box);
        box.setSelectedItem(Utils.range(min, max, value));
    }
    
    private SortableListModel<AttackObject> attacksModel()
    {
        return (SortableListModel<AttackObject>) attacks.getModel();
    }
    
    private DefaultListModel<EvolveCondition> conditionsModel()
    {
        return (DefaultListModel<EvolveCondition>) conditions.getModel();
    }
    
    private static final class AttackObject implements Comparable<AttackObject>
    {
        private AttackModel attack;
        private int level;
        private boolean hidden;
        
        public AttackObject(AttackModel attack, int level, boolean hidden)
        {
            setAttack(attack);
            setLevel(level);
            setHidden(hidden);
        }
        
        public final void setAttack(AttackModel attack) { this.attack = Objects.requireNonNull(attack); }
        public final AttackModel getAttack() { return attack; }
        
        public final void setLevel(int level) { this.level = Utils.range(1, 100, level); }
        public final int getLevel() { return level; }
        
        public final void setHidden(boolean flag) { this.hidden = flag; }
        public final boolean isHidden() { return hidden; }

        @Override
        public final int compareTo(AttackObject o)
        {
            int val = Boolean.compare(hidden, o.hidden);
            if(val != 0)
                return val;
            val = Integer.compare(level, o.level);
            if(val != 0)
                return val;
            return attack.compareTo(o.attack);
        }
        
        @Override
        public final String toString()
        {
            String str = getLevel() + ": " + getAttack().getName();
            return isHidden() ? ("[" + str + "]") : str;
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        hp = new javax.swing.JComboBox<>();
        speed = new javax.swing.JComboBox<>();
        attack = new javax.swing.JComboBox<>();
        spAttack = new javax.swing.JComboBox<>();
        defense = new javax.swing.JComboBox<>();
        spDefense = new javax.swing.JComboBox<>();
        statAmount = new javax.swing.JLabel();
        growth = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        type1 = new javax.swing.JComboBox<>();
        type2 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        attacks = new javax.swing.JList<>();
        innateAttack = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        add_attack = new javax.swing.JComboBox<>();
        add_level = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        add_hidden = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        mod_attack = new javax.swing.JComboBox<>();
        mod_level = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        mod_hidden = new javax.swing.JCheckBox();
        jButton15 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        evolves = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        conditions = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor de Razas");

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));

        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        hp.setEditable(true);
        hp.setBorder(javax.swing.BorderFactory.createTitledBorder("Puntos de Salud"));
        hp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hpActionPerformed(evt);
            }
        });
        jPanel2.add(hp);

        speed.setEditable(true);
        speed.setBorder(javax.swing.BorderFactory.createTitledBorder("Velocidad"));
        speed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedActionPerformed(evt);
            }
        });
        jPanel2.add(speed);

        attack.setEditable(true);
        attack.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque"));
        attack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackActionPerformed(evt);
            }
        });
        jPanel2.add(attack);

        spAttack.setEditable(true);
        spAttack.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque Especial"));
        spAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spAttackActionPerformed(evt);
            }
        });
        jPanel2.add(spAttack);

        defense.setEditable(true);
        defense.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa"));
        defense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defenseActionPerformed(evt);
            }
        });
        jPanel2.add(defense);

        spDefense.setEditable(true);
        spDefense.setBorder(javax.swing.BorderFactory.createTitledBorder("Defensa Especial"));
        spDefense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spDefenseActionPerformed(evt);
            }
        });
        jPanel2.add(spDefense);

        statAmount.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        growth.setBorder(javax.swing.BorderFactory.createTitledBorder("Crecimiento"));

        jPanel10.setLayout(new java.awt.GridLayout(1, 2));

        type1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo 1"));
        jPanel10.add(type1);

        type2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo 2"));
        jPanel10.add(type2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statAmount, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(growth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(name)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(growth, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Básico", jPanel1);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataques"));

        attacks.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        attacks.setToolTipText("");
        attacks.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                attacksValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(attacks);

        innateAttack.setToolTipText("");
        innateAttack.setBorder(javax.swing.BorderFactory.createTitledBorder("Ataque Innato"));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Añadir Ataque"));

        add_level.setBorder(javax.swing.BorderFactory.createTitledBorder("Nivel"));

        jButton4.setText("Añadir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        add_hidden.setText("Oculto");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(add_attack, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(add_level, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add_hidden, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(add_attack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add_level, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(add_hidden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Modificar Ataque"));

        mod_level.setBorder(javax.swing.BorderFactory.createTitledBorder("Nivel"));

        jButton5.setText("Modificar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        mod_hidden.setText("Oculto");

        jButton15.setText("Eliminar");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mod_attack, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(mod_level, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mod_hidden, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(mod_attack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mod_level, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(mod_hidden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(innateAttack, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(innateAttack, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ataques", jPanel3);

        jButton6.setText("Crear");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Eliminar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        evolves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evolvesActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Condiciones"));

        conditions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(conditions);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Añadir/Eliminar Condición"));
        jPanel7.setLayout(new java.awt.GridLayout(5, 1));

        jButton8.setText("Nivel Requerido");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton8);

        jButton9.setText("Stat mayor que otro");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton9);

        jButton10.setText("Stat igual a otro");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton10);

        jButton11.setText("Requiere ataque");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton11);

        jButton13.setText("Eliminar Condición");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton13);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Editar Condición"));

        jButton12.setText("Editar Condición");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(evolves, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(evolves, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Evoluciones", jPanel4);

        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cargar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Nueva Raza");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton14.setText("Clonar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton14))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hpActionPerformed
        rangeValue(hp, 1, 999);
        updateStatAmount();
    }//GEN-LAST:event_hpActionPerformed

    private void speedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedActionPerformed
        rangeValue(speed, 1, 999);
        updateStatAmount();
    }//GEN-LAST:event_speedActionPerformed

    private void attackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackActionPerformed
        rangeValue(attack, 1, 999);
        updateStatAmount();
    }//GEN-LAST:event_attackActionPerformed

    private void spAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spAttackActionPerformed
        rangeValue(spAttack, 1, 999);
        updateStatAmount();
    }//GEN-LAST:event_spAttackActionPerformed

    private void defenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defenseActionPerformed
        rangeValue(defense, 1, 999);
        updateStatAmount();
    }//GEN-LAST:event_defenseActionPerformed

    private void spDefenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spDefenseActionPerformed
        rangeValue(spDefense, 1, 999);
        updateStatAmount();
    }//GEN-LAST:event_spDefenseActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        var att = (AttackModel) add_attack.getSelectedItem();
        if(att == null)
            return;
        var level = Utils.range(1, 100, value(add_level));
        var hidden = add_hidden.isSelected();
        var obj = new AttackObject(att, level, hidden);
        attacksModel().addElement(obj);
        attacksModel().sort();
        attacks.repaint();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void attacksValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_attacksValueChanged
        var sel = attacks.getSelectedValue();
        if(sel == null)
            return;
        
        mod_attack.setSelectedItem(sel.getAttack());
        mod_level.setSelectedItem(sel.getLevel());
        mod_hidden.setSelected(sel.isHidden());
    }//GEN-LAST:event_attacksValueChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        var sel = attacks.getSelectedValue();
        if(sel == null)
            return;
        
        var att = (AttackModel) mod_attack.getSelectedItem();
        if(att == null)
            return;
        var level = Utils.range(1, 100, value(mod_level));
        var hidden = mod_hidden.isSelected();
        
        sel.setAttack(att);
        sel.setLevel(level);
        sel.setHidden(hidden);
        attacksModel().sort();
        attacks.repaint();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void evolvesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evolvesActionPerformed
        var sel = (Evolution) evolves.getSelectedItem();
        if(sel == null)
            return;
        
        var model = conditionsModel();
        model.clear();
        
        for(int i=0;i<sel.getConditionCount();i++)
            model.addElement(sel.getCondition(i));
    }//GEN-LAST:event_evolvesActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if(evolves.getSelectedItem() == null)
            return;
        var cond = conditions.getSelectedValue();
        if(cond == null)
            return;
        
        editCondition(cond);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        createCondition(Evolution.EvolutionConditionType.LEVEL);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        createCondition(Evolution.EvolutionConditionType.STAT_GREATER_THAN);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        createCondition(Evolution.EvolutionConditionType.STAT_EQUALS_TO);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        createCondition(Evolution.EvolutionConditionType.LEARNED_ATTACK);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        int sel = evolves.getSelectedIndex();
        if(sel < 0)
            return;
        var evo = (Evolution) evolves.getSelectedItem();
        
        sel = conditions.getSelectedIndex();
        if(sel < 0)
            return;
        
        conditionsModel().remove(sel);
        evo.removeCondition(sel);
        conditions.repaint();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        var races = RacePool.getAllRaces(false).toArray(Race[]::new);
        if(races.length < 1)
            return;
        var obj = JOptionPane.showInputDialog(this, "¿A que raza evolucionará?",
                "Nueva Evolución", JOptionPane.QUESTION_MESSAGE, null,
                races, races[0]);
        if(obj == null)
            return;
        
        var evo = new Evolution();
        evo.setRaceToEvolve((Race) obj);
        evolves.addItem(evo);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        var sel = evolves.getSelectedIndex();
        if(sel < 0)
            return;
        evolves.removeItemAt(sel);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        createNew();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        load();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        store();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        createCloned();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        var sel = attacks.getSelectedIndex();
        if(sel < 0)
            return;
        attacksModel().removeElementAt(sel);
    }//GEN-LAST:event_jButton15ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<AttackModel> add_attack;
    private javax.swing.JCheckBox add_hidden;
    private javax.swing.JComboBox<Integer> add_level;
    private javax.swing.JComboBox<Integer> attack;
    private javax.swing.JList<AttackObject> attacks;
    private javax.swing.JList<EvolveCondition> conditions;
    private javax.swing.JComboBox<Integer> defense;
    private javax.swing.JComboBox<Evolution> evolves;
    private javax.swing.JComboBox<Growth> growth;
    private javax.swing.JComboBox<Integer> hp;
    private javax.swing.JComboBox<AttackModel> innateAttack;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<AttackModel> mod_attack;
    private javax.swing.JCheckBox mod_hidden;
    private javax.swing.JComboBox<Integer> mod_level;
    private javax.swing.JTextField name;
    private javax.swing.JComboBox<Integer> spAttack;
    private javax.swing.JComboBox<Integer> spDefense;
    private javax.swing.JComboBox<Integer> speed;
    private javax.swing.JLabel statAmount;
    private javax.swing.JComboBox<ElementalType> type1;
    private javax.swing.JComboBox<ElementalType> type2;
    // End of variables declaration//GEN-END:variables
}
