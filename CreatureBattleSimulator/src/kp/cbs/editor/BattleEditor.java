/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import kp.cbs.battle.Team.SearchFirstBehabior;
import kp.cbs.battle.Team.SearchNextBehabior;
import kp.cbs.battle.prop.BattleProperties;
import kp.cbs.battle.prop.BattleProperties.CreatureEntry;
import kp.cbs.battle.prop.BattlePropertiesPool;
import kp.cbs.battle.prop.CreatureProperties;
import kp.cbs.creature.race.RacePool;
import static kp.cbs.editor.AttackEditor.generateIntegerRange;
import kp.cbs.editor.utils.CreatureEditor;
import kp.cbs.utils.MusicModel;
import kp.cbs.utils.SortableListModel;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public final class BattleEditor extends JDialog
{
    private final CreatureEditor creatureEditor = new CreatureEditor(this);
    
    public BattleEditor(JFrame parent)
    {
        super(parent, false);
        initComponents();
        init();
    }
    
    public static final void open(MainMenuEditor parent)
    {
        var editor = new BattleEditor(parent);
        editor.setVisible(true);
    }
    
    public static final void open(PlaceEditor parent)
    {
        var editor = new BattleEditor(parent);
        editor.setModal(true);
        editor.setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        minTeam.setModel(new DefaultComboBoxModel<>(generateIntegerRange(1, 12, 1)));
        maxTeam.setModel(new DefaultComboBoxModel<>(generateIntegerRange(1, 12, 1)));
        
        behabiorFirst.setModel(new DefaultComboBoxModel<>(SearchFirstBehabior.values()));
        behabiorNext.setModel(new DefaultComboBoxModel<>(SearchNextBehabior.values()));
        
        music.setModel(new DefaultComboBoxModel<>(MusicModel.getAllModelNames(true)));
        intelligence.setModel(new DefaultComboBoxModel<>(generateIntegerRange(0, 255, 1)));
        
        creatures.setModel(new SortableListModel<>());
        probability.setModel(new DefaultComboBoxModel<>(generateIntegerRange(1, 255, 1)));
        type.setModel(new DefaultComboBoxModel<>(TemplateType.values()));
        
        createNew();
    }
    
    private void showTemplate()
    {
        var sel = creatures.getSelectedValue();
        if(sel == null)
        {
            probability.setEnabled(false);
            type.setEnabled(false);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            return;
        }
        
        probability.setEnabled(true);
        type.setEnabled(true);
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
        
        probability.setSelectedItem(sel.getProbability());
        type.setSelectedItem(sel.getType());
    }
    
    private SortableListModel<CreatureTemplate> creaturesModel() { return (SortableListModel<CreatureTemplate>) creatures.getModel(); }
    
    private void load()
    {
        var allBattles = BattlePropertiesPool.getAllNames();
        if(allBattles.length < 1)
            return;
        var sel = JOptionPane.showInputDialog(this, "¿Que batalla quieres cargar?",
                "Cargar Batalla", JOptionPane.QUESTION_MESSAGE, null,
                allBattles, allBattles[0]);
        if(sel == null)
            return;
        
        var battle = BattlePropertiesPool.load((String) sel);
        if(battle == null)
            return;
        createNew();
        expandBattle((String) sel, battle);
    }
    
    private void store()
    {
        if(name.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "El nombre de la batalla no puede estar vacío.",
                "Guardar Batalla", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        var battle = generateBattle();
        if(BattlePropertiesPool.store(name.getText(), battle))
        {
            JOptionPane.showMessageDialog(this, "¡La batalla ha sido guardada con éxito!",
                    "Guardar Batalla", JOptionPane.INFORMATION_MESSAGE);
        }
        else JOptionPane.showMessageDialog(this, "Ha habido un fallo al guardar la batalla.",
                "Guardar Batalla", JOptionPane.ERROR_MESSAGE);
    }
    
    private void createNew()
    {
        name.setText("");
        minTeam.setSelectedIndex(0);
        maxTeam.setSelectedIndex(0);
        behabiorFirst.setSelectedIndex(0);
        behabiorNext.setSelectedIndex(0);
        music.setSelectedItem("trainer");
        intelligence.setSelectedItem(0);
        
        creaturesModel().removeAllElements();
        showTemplate();
    }
    
    private void expandBattle(String name, BattleProperties battle)
    {
        this.name.setText(name);
        minTeam.setSelectedItem(battle.getMinTeamLength());
        maxTeam.setSelectedItem(battle.getMaxTeamLength());
        behabiorFirst.setSelectedItem(battle.getSearchFirstBehabior());
        behabiorNext.setSelectedItem(battle.getSearchNextBehabior());
        music.setSelectedItem(battle.getMusic());
        intelligence.setSelectedItem(battle.getIntelligence());
        
        final var model = creaturesModel();
        model.removeAllElements();
        battle.streamNormalCreatures().forEach(e -> {
            var template = new CreatureTemplate(e);
            model.addElement(template);
        });
        battle.streamRequiredCreatures().forEach(c -> {
            var template = new CreatureTemplate(c);
            model.addElement(template);
        });
        model.sort();
    }
    
    private BattleProperties generateBattle()
    {
        var battle = new BattleProperties();
        battle.setMinTeamLength(value(minTeam, 1));
        battle.setMaxTeamLength(value(maxTeam, 1));
        battle.setSearchFirstBehabior((SearchFirstBehabior) behabiorFirst.getSelectedItem());
        battle.setSearchNextBehabior((SearchNextBehabior) behabiorNext.getSelectedItem());
        battle.setMusic((String) music.getSelectedItem());
        battle.setIntelligence(value(intelligence));
        
        var model = creaturesModel();
        var len = model.size();
        for(int i = 0; i < len; i++)
        {
            var template = model.getElementAt(i);
            if(template.getType() == TemplateType.REQUIRED)
                battle.addCreature(template.getProperties(), template.getProbability(), false, true);
            else battle.addCreature(template.getProperties(), template.getProbability(), template.getType() == TemplateType.UNIQUE, false);
        }
        
        return battle;
    }
    
    private static int value(JComboBox<Integer> box, int defaultValue)
    {
        try { return ((Number) box.getSelectedItem()).intValue(); }
        catch(Throwable ex) { return defaultValue; }
    }
    private static int value(JComboBox<Integer> box) { return value(box, 0); }
    
    
    public static final class CreatureTemplate implements Comparable<CreatureTemplate>
    {
        private final CreatureProperties props;
        private int probability = 1;
        private TemplateType type = TemplateType.NORMAL;
        
        private CreatureTemplate(CreatureEntry entry)
        {
            if(entry != null)
            {
                this.props = entry.getCreatureProperties();
                setProbability(entry.getProbability());
                setType(entry.isUnique() ? TemplateType.UNIQUE : TemplateType.NORMAL);
            }
            else this.props = new CreatureProperties();
        }
        private CreatureTemplate(CreatureProperties properties)
        {
            if(properties != null)
            {
                this.props = properties;
                setProbability(1);
                setType(TemplateType.REQUIRED);
            }
            else this.props = new CreatureProperties();
        }
        private CreatureTemplate()
        {
            this.props = new CreatureProperties();
        }
        
        public final CreatureProperties getProperties() { return props; }
        
        public final void setProbability(int probability) { this.probability = Math.max(1, probability); }
        public final int getProbability() { return probability; }
        
        public final void setType(TemplateType type) { this.type = Objects.requireNonNullElse(type, TemplateType.NORMAL); }
        public final TemplateType getType() { return type; }
        
        @Override
        public final String toString()
        {
            String t;
            switch(type)
            {
                default:
                case NORMAL: t = "[N] "; break;
                case UNIQUE: t = "[U] "; break;
                case REQUIRED: t = "[R] "; break;
            }
            return t + (props.getName().isBlank() ? props.getRace().getName() : props.getName());
        }

        @Override
        public final int compareTo(CreatureTemplate o)
        {
            return Integer.compare(type.ordinal(), o.type.ordinal());
        }
    }
    
    public enum TemplateType { NORMAL, UNIQUE, REQUIRED; }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        minTeam = new javax.swing.JComboBox<>();
        maxTeam = new javax.swing.JComboBox<>();
        behabiorFirst = new javax.swing.JComboBox<>();
        behabiorNext = new javax.swing.JComboBox<>();
        music = new javax.swing.JComboBox<>();
        intelligence = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        creatures = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        probability = new javax.swing.JComboBox<>();
        type = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor de Batallas");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(name, gridBagConstraints);

        minTeam.setBorder(javax.swing.BorderFactory.createTitledBorder("Tamaño mínimo equipo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(minTeam, gridBagConstraints);

        maxTeam.setBorder(javax.swing.BorderFactory.createTitledBorder("Tamaño máximo equipo"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(maxTeam, gridBagConstraints);

        behabiorFirst.setBorder(javax.swing.BorderFactory.createTitledBorder("Comportamiento primero"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(behabiorFirst, gridBagConstraints);

        behabiorNext.setBorder(javax.swing.BorderFactory.createTitledBorder("Comportamiento siguiente"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(behabiorNext, gridBagConstraints);

        music.setBorder(javax.swing.BorderFactory.createTitledBorder("BGM"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(music, gridBagConstraints);

        intelligence.setBorder(javax.swing.BorderFactory.createTitledBorder("Inteligencia"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(intelligence, gridBagConstraints);

        jTabbedPane1.addTab("Principal", jPanel1);

        creatures.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        creatures.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                creaturesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(creatures);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Propiedades"));
        jPanel3.setLayout(new java.awt.GridLayout(2, 2));

        probability.setBorder(javax.swing.BorderFactory.createTitledBorder("Probabilidad"));
        probability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                probabilityActionPerformed(evt);
            }
        });
        jPanel3.add(probability);

        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel3.add(type);

        jButton4.setText("Editar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4);

        jButton5.setText("Eliminar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButton6.setText("Crear");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jButton6, gridBagConstraints);

        jButton7.setText("Subir");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jButton7, gridBagConstraints);

        jButton8.setText("Bajar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jButton8, gridBagConstraints);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Luchadores", jPanel2);

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

        jButton3.setText("Nueva");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void creaturesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_creaturesValueChanged
        showTemplate();
    }//GEN-LAST:event_creaturesValueChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        var sel = creatures.getSelectedValue();
        if(sel == null)
            return;
        
        creatureEditor.open(sel.getProperties());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void probabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_probabilityActionPerformed
        var sel = creatures.getSelectedValue();
        if(sel == null)
            return;
        
        sel.setProbability(((Number) probability.getSelectedItem()).intValue());
    }//GEN-LAST:event_probabilityActionPerformed

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed
        var sel = creatures.getSelectedValue();
        if(sel == null)
            return;
        
        sel.setType((TemplateType) type.getSelectedItem());
    }//GEN-LAST:event_typeActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        var sel = creatures.getSelectedIndex();
        if(sel < 0)
            return;
        
        var model = creaturesModel();
        model.removeElementAt(sel);
        model.sort();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        var value = JOptionPane.showInputDialog(this, "¿Qué tipo de creatura será?", "Crear criatura",
                JOptionPane.QUESTION_MESSAGE, null, TemplateType.values(), TemplateType.NORMAL);
        if(value == null || !(value instanceof TemplateType))
            return;
        
        var template = new CreatureTemplate();
        template.setType((TemplateType) value);
        template.setProbability(1);
        template.getProperties().setRace(RacePool.getRace(0));
        
        var model = creaturesModel();
        model.addElement(template);
        model.sort();
        creatures.setSelectedValue(template, true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        var sel = creatures.getSelectedIndex();
        if(sel < 1)
            return;
        
        var model = creaturesModel();
        var current = model.getElementAt(sel);
        var upper = model.getElementAt(sel - 1);
        if(current.compareTo(upper) == 0)
        {
            model.removeElementAt(sel);
            model.add(sel - 1, current);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        var model = creaturesModel();
        var sel = creatures.getSelectedIndex();
        if(sel < 0 || sel >= model.size() - 1)
            return;
        
        var current = model.getElementAt(sel);
        var downer = model.getElementAt(sel + 1);
        if(current.compareTo(downer) == 0)
        {
            model.removeElementAt(sel);
            model.add(sel + 1, current);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        createNew();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        load();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        store();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<SearchFirstBehabior> behabiorFirst;
    private javax.swing.JComboBox<SearchNextBehabior> behabiorNext;
    private javax.swing.JList<CreatureTemplate> creatures;
    private javax.swing.JComboBox<Integer> intelligence;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<Integer> maxTeam;
    private javax.swing.JComboBox<Integer> minTeam;
    private javax.swing.JComboBox<String> music;
    private javax.swing.JTextField name;
    private javax.swing.JComboBox<Integer> probability;
    private javax.swing.JComboBox<TemplateType> type;
    // End of variables declaration//GEN-END:variables
}
