/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import kp.cbs.editor.utils.BattleSelector;
import kp.cbs.editor.utils.ListBattleSelector;
import kp.cbs.editor.utils.ListRequireds;
import kp.cbs.place.Challenge;
import kp.cbs.place.Place;
import kp.cbs.utils.Pair;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class PlaceEditor extends JDialog
{
    private BattleSelector wildBattle;
    private BattleSelector trainerBattle;
    private ListBattleSelector challengeBattles;
    private ListRequireds requireds;
    
    public PlaceEditor(MainMenuEditor parent)
    {
        super(parent, false);
        initComponents();
        init();
    }
    
    public static final void open(MainMenuEditor parent)
    {
        var editor = new PlaceEditor(parent);
        editor.setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        wildBattle = new BattleSelector(this, "Batalla Salvajes");
        jPanel1.add(wildBattle);
        
        trainerBattle = new BattleSelector(this, "Batalla Entrenadores");
        jPanel1.add(trainerBattle);
        
        challengeBattles = new ListBattleSelector(this, this::updateChallengeBattles);
        jPanel6.add(challengeBattles);
        
        challenges.setModel(new DefaultListModel<>());
        links.setModel(new DefaultListModel<>());
        
        requireds = new ListRequireds(this, this::updateRequireds);
        jPanel9.add(requireds);
        
        showChallenge();
        showPlace();
    }
    
    private void updateChallengeBattles(String[] battles)
    {
        var sel = challenges.getSelectedValue();
        if(sel == null)
            return;
        
        sel.setBattles(battles);
    }
    
    private void updateRequireds(String[] reqs)
    {
        var sel = links.getSelectedValue();
        if(sel == null)
            return;
        
        sel.setRequireds(reqs);
    }
    
    private DefaultListModel<Challenge> challengeModel() { return (DefaultListModel<Challenge>) challenges.getModel(); }
    private DefaultListModel<PlaceEntry> placeModel() { return (DefaultListModel<PlaceEntry>) links.getModel(); }
    
    private void showChallenge()
    {
        var sel = challenges.getSelectedValue();
        if(sel == null)
        {
            c_id.setText("");
            c_name.setText("");
            c_desc.setText("");
            c_unique.setSelected(false);
            challengeBattles.clear();
            
            c_up.setEnabled(false);
            c_down.setEnabled(false);
            c_remove.setEnabled(false);
            c_id.setEnabled(false);
            c_name.setEnabled(false);
            c_desc.setEnabled(false);
            c_unique.setEnabled(false);
            challengeBattles.setVisible(false);
        }
        else
        {
            c_id.setText(sel.getId());
            c_name.setText(sel.getName());
            c_desc.setText(sel.getDescription());
            c_unique.setSelected(sel.isUnique());
            challengeBattles.fillBattles(sel.getBattles());

            var index = challenges.getSelectedIndex();
            var model = challengeModel();

            c_up.setEnabled(index > 0 && model.size() > 1);
            c_down.setEnabled(index < model.size() - 1 && model.size() > 1);
            c_remove.setEnabled(true);
            c_id.setEnabled(true);
            c_name.setEnabled(true);
            c_desc.setEnabled(true);
            c_unique.setEnabled(true);
            challengeBattles.setVisible(true);
        }
    }
    
    private void showPlace()
    {
        var sel = links.getSelectedValue();
        if(sel == null)
        {
            l_name.setText("");
            requireds.clear();
            
            l_up.setEnabled(false);
            l_down.setEnabled(false);
            l_remove.setEnabled(false);
            l_name.setEnabled(false);
            requireds.setVisible(false);
        }
        else
        {
            l_name.setText(sel.getText());

            var index = links.getSelectedIndex();
            var model = placeModel();

            l_up.setEnabled(index > 0 && model.size() > 1);
            l_down.setEnabled(index < model.size() - 1 && model.size() > 1);
            l_remove.setEnabled(true);
            l_name.setEnabled(true);
            requireds.setVisible(true);
        }
    }
    
    private void modChallenge(BiAction<Challenge> action)
    {
        var sel = challenges.getSelectedValue();
        if(sel == null)
            return;
        action.accept(challenges.getSelectedIndex(), sel);
    }
    private void modPlace(BiAction<PlaceEntry> action)
    {
        var sel = links.getSelectedValue();
        if(sel == null)
            return;
        action.accept(links.getSelectedIndex(), sel);
    }
    
    private void load()
    {
        var allPlaces = Place.getAllSavedNames();
        if(allPlaces.length < 1)
            return;
        var sel = JOptionPane.showInputDialog(this, "¿Que lugar quieres cargar?",
                "Cargar Lugar", JOptionPane.QUESTION_MESSAGE, null,
                allPlaces, allPlaces[0]);
        if(sel == null)
            return;
        
        var place = Place.load((String) sel);
        if(place == null)
            return;
        createNew();
        expandPlace(place);
    }
    
    private void store()
    {
        if(name.getText().isBlank())
        {
            JOptionPane.showMessageDialog(this, "El nombre del lugar no puede estar vacío.",
                "Guardar Lugar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        var place = generatePlace();
        if(Place.save(place))
        {
            JOptionPane.showMessageDialog(this, "¡El lugar ha sido guardada con éxito!",
                    "Guardar Lugar", JOptionPane.INFORMATION_MESSAGE);
        }
        else JOptionPane.showMessageDialog(this, "Ha habido un fallo al guardar el lugar.",
                "Guardar Lugar", JOptionPane.ERROR_MESSAGE);
    }
    
    private void createNew()
    {
        name.setText("");
        wildBattle.setText("");
        trainerBattle.setText("");
        
        challengeModel().clear();
        placeModel().clear();
        
        showChallenge();
        showPlace();
    }
    
    private Place generatePlace()
    {
        var place = new Place();
        
        place.setName(name.getText());
        place.setWildBattle(wildBattle.getText());
        place.setTrainerBattle(trainerBattle.getText());
        
        var cModel = challengeModel();
        place.setChallenges(Utils.listModelToStream(cModel).collect(Collectors.toList()));
        
        var pModel = placeModel();
        place.setTravels(Utils.listModelToStream(pModel).map(p -> new Pair<>(p.place, p.requireds)).toArray(Pair[]::new));
        
        return place;
    }
    
    private void expandPlace(Place place)
    {
        name.setText(place.getName());
        wildBattle.setText(place.getWildBattle());
        trainerBattle.setText(place.getTrainerBattle());
        
        var cModel = challengeModel();
        cModel.clear();
        cModel.addAll(place.getAllChallenges());
        
        var pModel = placeModel();
        pModel.clear();
        pModel.addAll(place.getAllTravels().stream().map(PlaceEntry::new).collect(Collectors.toList()));
    }
    
    private static void setCheckBoxState(JCheckBox box, boolean state)
    {
        box.setSelected(!state);
        box.doClick();
    }
    
    
    
    
    @FunctionalInterface
    private static interface BiAction<T> { void accept(int index, T object); }
    
    private static final class PlaceEntry
    {
        private String place = "";
        private String[] requireds = {};
        
        private PlaceEntry() {}
        private PlaceEntry(String place) { setText(place); }
        private PlaceEntry(Pair<String, String[]> pair)
        {
            setText(pair.left);
            setRequireds(pair.right);
        }
        
        public final void setText(String battle) { this.place = battle == null ? "" : battle; }
        public final String getText() { return place; }
        
        public final void setRequireds(String[] requireds) { this.requireds = Objects.requireNonNullElse(requireds, new String[]{}); }
        public final String[] getRequireds() { return requireds; }
        
        @Override
        public final String toString() { return place == null || place.isBlank() ? "<Unnamed Place>" : place; }
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        challenges = new javax.swing.JList<>();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        c_id = new javax.swing.JTextField();
        c_unique = new javax.swing.JCheckBox();
        c_name = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        c_desc = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        c_add = new javax.swing.JButton();
        c_up = new javax.swing.JButton();
        c_down = new javax.swing.JButton();
        c_remove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        links = new javax.swing.JList<>();
        jPanel8 = new javax.swing.JPanel();
        l_add = new javax.swing.JButton();
        l_up = new javax.swing.JButton();
        l_link = new javax.swing.JButton();
        l_down = new javax.swing.JButton();
        l_remove = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        l_name = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(4, 1));

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        jPanel1.add(name);

        jTabbedPane1.addTab("Básico", jPanel1);

        challenges.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        challenges.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                challengesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(challenges);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        c_id.setBorder(javax.swing.BorderFactory.createTitledBorder("Id"));
        c_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_idKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(c_id, gridBagConstraints);

        c_unique.setText("Único");
        c_unique.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_uniqueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(c_unique, gridBagConstraints);

        c_name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        c_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_nameKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(c_name, gridBagConstraints);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));

        c_desc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_descKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(c_desc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jScrollPane2, gridBagConstraints);

        jTabbedPane2.addTab("Básico", jPanel5);

        jPanel6.setLayout(new java.awt.GridLayout(1, 1));
        jTabbedPane2.addTab("Batallas", jPanel6);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        c_add.setText("Añadir");
        c_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_addActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(c_add, gridBagConstraints);

        c_up.setText("^");
        c_up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_upActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(c_up, gridBagConstraints);

        c_down.setText("v");
        c_down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_downActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(c_down, gridBagConstraints);

        c_remove.setText("Quitar");
        c_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_removeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(c_remove, gridBagConstraints);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Desafios", jPanel2);

        links.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        links.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                linksValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(links);

        jPanel8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        l_add.setText("Añadir");
        l_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_addActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        jPanel8.add(l_add, gridBagConstraints);

        l_up.setText("^");
        l_up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_upActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel8.add(l_up, gridBagConstraints);

        l_link.setText("Enlazar");
        l_link.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_linkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        jPanel8.add(l_link, gridBagConstraints);

        l_down.setText("v");
        l_down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_downActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel8.add(l_down, gridBagConstraints);

        l_remove.setText("Quitar");
        l_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_removeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel8.add(l_remove, gridBagConstraints);

        l_name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        l_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                l_nameKeyReleased(evt);
            }
        });

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_name, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_name, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Enlaces", jPanel3);

        jButton1.setText("Nuevo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Guardar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Cargar");
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void challengesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_challengesValueChanged
        showChallenge();
    }//GEN-LAST:event_challengesValueChanged

    private void c_idKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_idKeyReleased
        modChallenge((index, sel) -> sel.setId(c_id.getText()));
    }//GEN-LAST:event_c_idKeyReleased

    private void c_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_nameKeyReleased
        modChallenge((index, sel) -> { sel.setName(c_name.getText()); challenges.repaint(); });
    }//GEN-LAST:event_c_nameKeyReleased

    private void c_descKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_descKeyReleased
        modChallenge((index, sel) -> sel.setDescription(c_desc.getText()));
    }//GEN-LAST:event_c_descKeyReleased

    private void c_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_addActionPerformed
        var model = challengeModel();
        var sel = new Challenge();
        
        model.addElement(sel);
        challenges.setSelectedValue(sel, true);
    }//GEN-LAST:event_c_addActionPerformed

    private void c_upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_upActionPerformed
        modChallenge((index, sel) -> {
            var model = challengeModel();
            
            if(index > 0 && model.size() > 1)
            {
                var old = model.remove(index);
                model.add(index - 1, old);
                challenges.setSelectedValue(old, true);
            }
        });
    }//GEN-LAST:event_c_upActionPerformed

    private void c_downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_downActionPerformed
        modChallenge((index, sel) -> {
            var model = challengeModel();
            
            if(index < model.size() - 1 && model.size() > 1)
            {
                var old = model.remove(index);
                model.add(index + 1, old);
                challenges.setSelectedValue(old, true);
            }
        });
    }//GEN-LAST:event_c_downActionPerformed

    private void c_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_removeActionPerformed
        modChallenge((index, sel) -> {
            challengeModel().remove(index);
            showChallenge();
        });
    }//GEN-LAST:event_c_removeActionPerformed

    private void linksValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_linksValueChanged
        showPlace();
    }//GEN-LAST:event_linksValueChanged

    private void l_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_addActionPerformed
        var model = placeModel();
        var sel = new PlaceEntry();
        
        model.addElement(sel);
        links.setSelectedValue(sel, true);
    }//GEN-LAST:event_l_addActionPerformed

    private void l_upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_upActionPerformed
        modPlace((index, sel) -> {
            var model = placeModel();
            
            if(index > 0 && model.size() > 1)
            {
                var old = model.remove(index);
                model.add(index - 1, old);
                links.setSelectedValue(old, true);
            }
        });
    }//GEN-LAST:event_l_upActionPerformed

    private void l_downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_downActionPerformed
        modPlace((index, sel) -> {
            var model = placeModel();
            
            if(index < model.size() - 1 && model.size() > 1)
            {
                var old = model.remove(index);
                model.add(index + 1, old);
                links.setSelectedValue(old, true);
            }
        });
    }//GEN-LAST:event_l_downActionPerformed

    private void l_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_nameKeyReleased
        modPlace((index, sel) -> { sel.setText(l_name.getText()); links.repaint(); });
    }//GEN-LAST:event_l_nameKeyReleased

    private void l_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_removeActionPerformed
        modPlace((index, sel) -> {
            placeModel().remove(index);
            showPlace();
        });
    }//GEN-LAST:event_l_removeActionPerformed

    private void l_linkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_linkActionPerformed
        var model = placeModel();
        var sel = new PlaceEntry();
        
        var all = Place.getAllSavedNames();
        var obj = JOptionPane.showInputDialog(this, "¿Que lugar quieres enlazar?", "Enlazar lugares", JOptionPane.QUESTION_MESSAGE, null, all, all[0]);
        if(obj == null || !(obj instanceof String))
            return;
        
        var place = Place.load(obj.toString());
        if(place == null)
            return;
        
        place.addTravel(name.getText());
        
        sel.place = place.getName();
        model.addElement(sel);
        links.setSelectedValue(sel, true);
    }//GEN-LAST:event_l_linkActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        createNew();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        load();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        store();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void c_uniqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_uniqueActionPerformed
        modChallenge((index, sel) -> sel.setUnique(c_unique.isSelected()));
    }//GEN-LAST:event_c_uniqueActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton c_add;
    private javax.swing.JTextPane c_desc;
    private javax.swing.JButton c_down;
    private javax.swing.JTextField c_id;
    private javax.swing.JTextField c_name;
    private javax.swing.JButton c_remove;
    private javax.swing.JCheckBox c_unique;
    private javax.swing.JButton c_up;
    private javax.swing.JList<Challenge> challenges;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton l_add;
    private javax.swing.JButton l_down;
    private javax.swing.JButton l_link;
    private javax.swing.JTextField l_name;
    private javax.swing.JButton l_remove;
    private javax.swing.JButton l_up;
    private javax.swing.JList<PlaceEntry> links;
    private javax.swing.JTextField name;
    // End of variables declaration//GEN-END:variables
}
