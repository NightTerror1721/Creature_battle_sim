/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import kp.cbs.battle.prop.BattlePropertiesPool;
import kp.cbs.editor.utils.EditorUtils;
import kp.cbs.editor.utils.FlagManager;
import kp.cbs.editor.utils.RequiredPanel;
import kp.cbs.place.Challenge;
import kp.cbs.place.Place;
import kp.cbs.utils.Utils;

/**
 *
 * @author Marc
 */
public class PlaceEditor extends JFrame
{
    private final RequiredPanel challengeRequireds = new RequiredPanel();
    private final RequiredPanel travelRequireds = new RequiredPanel();
    
    private final BattleComboBoxModel cb_wildBattleModel;
    private final BattleComboBoxModel cb_trainerBattleModel;
    
    private final DefaultListModel<Challenge> l_challengeModel;
    private final DefaultListModel<String> l_challengeBattlesModel;
    private final DefaultListModel<TravelEntry> l_travelsModel;
    
    private BattleEntry[] battlesCache;
    
    private final FlagManager<Object> locker = new FlagManager<>();
    
    /**
     * Creates new form PlaceCreator
     */
    private PlaceEditor()
    {
        initComponents();
        
        cb_wildBattleModel = new BattleComboBoxModel(cb_wildBattle);
        cb_trainerBattleModel = new BattleComboBoxModel(cb_trainerBattle);
        
        challengeRequireds.setCallback(this::callengeRequiredsCallback);
        travelRequireds.setCallback(this::travelRequiredsCallback);
        
        l_challenge.setModel(l_challengeModel = new DefaultListModel<>());
        l_challengeBattles.setModel(l_challengeBattlesModel = new DefaultListModel<>());
        l_travels.setModel(l_travelsModel = new DefaultListModel<>());
        
        init();
        pack();
    }
    
    public static final void open() { new PlaceEditor().setVisible(true); }
    
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        updateBattles();
        
        jPanel8.add(challengeRequireds);
        jPanel13.add(travelRequireds);
        
        installChallengeCallback(challengeId, (cmp, c) -> {
            if(!cmp.getText().isBlank())
                c.setId(cmp.getText());
        }, false);
        installChallengeCallback(challengeName, (cmp, c) -> c.setName(cmp.getText()), true);
        installChallengeCallback(challengeDesc, (cmp, c) -> c.setDescription(cmp.getText()), false);
        installChallengeCallback(challengeUnique, (cmp, c) -> c.setUnique(cmp.isSelected()));
        
        newPlace();
    }
    
    private void updateBattles()
    {
        battlesCache = Stream.concat(Stream.of(BattleEntry.INVALID), 
                Stream.of(BattlePropertiesPool.getAllNames()).map(BattleEntry::new))
                .toArray(BattleEntry[]::new);
        
        cb_wildBattleModel.updateValues();
        cb_trainerBattleModel.updateValues();
    }
    
    
    private void showChallenge()
    {
        if(isLocked(l_challenge))
            return;
        
        lock(l_challenge);
        
        final var sel = getSelectedValue(l_challenge);
        if(sel == null)
        {
            challengeId.setEnabled(false);
            challengeName.setEnabled(false);
            challengeUnique.setEnabled(false);
            challengeDesc.setEnabled(false);
            l_challengeBattles.setEnabled(false);
            b_addChallengeBattle.setEnabled(false);
            b_removeChallengeBattle.setEnabled(false);
            b_upChallengeBattle.setEnabled(false);
            b_downChallengeBattle.setEnabled(false);
            jButton7.setEnabled(false);
            b_removeChallenge.setEnabled(false);
            b_upChallenge.setEnabled(false);
            b_downChallenge.setEnabled(false);
            
            challengeId.setText("");
            challengeName.setText("");
            challengeDesc.setText("");
            challengeUnique.setSelected(false);
            l_challengeBattlesModel.removeAllElements();
            
            challengeRequireds.deactivateAndRestart();
        }
        else
        {
            challengeId.setEnabled(true);
            challengeName.setEnabled(true);
            challengeUnique.setEnabled(true);
            challengeDesc.setEnabled(true);
            
            final var bidxsel = l_challengeBattles.getSelectedIndex();
            l_challengeBattles.setEnabled(true);
            b_addChallengeBattle.setEnabled(true);
            b_removeChallengeBattle.setEnabled(bidxsel >= 0);
            b_upChallengeBattle.setEnabled(bidxsel > 0);
            b_downChallengeBattle.setEnabled(bidxsel + 1 < l_challengeBattlesModel.size());
            jButton7.setEnabled(true);
            
            b_removeChallenge.setEnabled(false);
            b_upChallenge.setEnabled(l_challenge.getSelectedIndex() > 0);
            b_downChallenge.setEnabled(l_challenge.getSelectedIndex() + 1 < l_challengeModel.getSize());
            
            challengeRequireds.clear();
            challengeRequireds.activate();
            travelRequireds.clear();
            travelRequireds.activate();
            
            challengeId.setText(sel.getId());
            challengeName.setText(sel.getName());
            challengeDesc.setText(sel.getDescription());
            challengeUnique.setSelected(sel.isUnique());
            
            lock(l_challengeModel);
            var bsel = getSelectedValue(l_challengeBattles);
            l_challengeBattlesModel.removeAllElements();
            l_challengeBattlesModel.addAll(sel.streamBattles()
                    .filter(BattleEntry::isValid)
                    .collect(Collectors.toList()));
            if(bsel != null)
                l_challengeBattles.setSelectedValue(bsel, true);
            unlock(l_challengeModel);
            
            challengeRequireds.setRequiredIds(sel.getAllRequired());
        }
        l_challenge.repaint();
        
        unlock(l_challenge);
    }
    
    private void showTravel()
    {
        if(isLocked(l_travels))
            return;
        
        lock(l_travels);
        
        final var sel = getSelectedValue(l_travels);
        if(sel == null)
        {
            b_removeTravel.setEnabled(false);
            travelRequireds.deactivateAndRestart();
        }
        else
        {
            b_removeTravel.setEnabled(l_travels.getSelectedIndex() >= 0);
            
            travelRequireds.clear();
            travelRequireds.activate();
            travelRequireds.setRequiredIds(sel.getRequiredIds());
        }
        
        unlock(l_travels);
    }
    
    
    private void updateTitle()
    {
        final var base = "Editor de Lugares - ";
        final var pname = name.getText();
        if(pname == null || pname.isBlank())
            setTitle(base + "???");
        else setTitle(base + pname);
    }
    
    
    private static <T> T getSelectedValue(JList<T> list)
    {
        var sel = list.getSelectedValue();
        return sel == null ? null : (T) sel;
    }
    
    private void lock(Object obj)
    {
        locker.enable(obj);
    }
    
    private void unlock(Object obj)
    {
        locker.disable(obj);
    }
    
    private boolean isLocked(Object obj)
    {
        return locker.isEnabled(obj);
    }
    
    private void unlockAll()
    {
        locker.disableAll();
    }
    
    private void openBattleEditor()
    {
        BattleEditor.open(this);
        updateBattles();
    }
    
    private String generateChallengeName()
    {
        if(l_challengeModel.isEmpty())
            return "new_challenge";
        
        final var len = l_challengeModel.size();
        var count = 0;
        for(int i = 0; i < len; ++i)
        {
            var c = l_challengeModel.getElementAt(i);
            if(c.getId().contains("new_challenge"))
                ++count;
        }
        return count < 1 ? "new_challenge" : ("new_challenge" + count);
    }
    
    private void callengeRequiredsCallback(String[] requireds)
    {
        if(!isLocked(l_challenge))
        {
            var sel = getSelectedValue(l_challenge);
            if(sel != null)
                sel.setRequired(requireds);
        }
    }
    
    private void travelRequiredsCallback(String[] requireds)
    {
        if(!isLocked(l_travels))
        {
            var sel = getSelectedValue(l_travels);
            if(sel != null)
                sel.setRequiredIds(requireds);
        }
    }
    
    private String selectBattle()
    {
        var entry = EditorUtils.selectItem(this, "Batallas", "¿Qué batalla quieres añadir?", battlesCache, null);
        return entry == null || !entry.isValid() ? null : entry.toString();
    }
    
    private String selectTravel()
    {
        return EditorUtils.selectItem(this,
                "Lugares para enlazar",
                "¿Con que lugar quieres enlazarte?",
                Place.getAllSavedNames(), null);
    }
    
    private <T extends JTextComponent> void installChallengeCallback(T component, BiConsumer<T, Challenge> action, boolean repaint)
    {
        EditorUtils.installActionCallback(component, c -> {
            if(!isLocked(l_challenge))
            {
                final var sel = getSelectedValue(l_challenge);
                if(sel != null)
                {
                    action.accept(c, sel);
                    if(repaint)
                        l_challenge.repaint();
                }
            }
        });
    }
    
    private void installChallengeCallback(JCheckBox component, BiConsumer<JCheckBox, Challenge> action)
    {
        EditorUtils.installActionCallback(component, c -> {
            if(!isLocked(l_challenge))
            {
                final var sel = getSelectedValue(l_challenge);
                if(sel != null)
                    action.accept(c, sel);
            }
        });
    }
    
    private static <T> void extractFromComboBox(JComboBox<T> box, T defaultValue, Consumer<T> action)
    {
        var value = box.getSelectedItem();
        if(value == null)
            value = Objects.requireNonNull(defaultValue);
        
        try { action.accept((T) value); }
        catch(ClassCastException ex) { action.accept(Objects.requireNonNull(defaultValue)); }
    }
    
    
    private void newPlace()
    {
        name.setText("");
        cb_wildBattle.setSelectedIndex(0);
        cb_trainerBattle.setSelectedIndex(0);
        
        lock(l_challenge);
        l_challengeModel.removeAllElements();
        unlock(l_challenge);
        
        lock(l_travels);
        l_travelsModel.removeAllElements();
        unlock(l_travels);
        
        showChallenge();
        showTravel();
        updateTitle();
    }
    
    private Place buildPlace()
    {
        final var place = new Place();
        
        extractFromComboBox(cb_wildBattle, BattleEntry.INVALID, e -> place.setWildBattle(e.getBattleId()));
        extractFromComboBox(cb_trainerBattle, BattleEntry.INVALID, e -> place.setTrainerBattle(e.getBattleId()));
        
        place.setChallenges(EditorUtils.listModelStream(l_challengeModel).collect(Collectors.toList()));
        
        place.setTravels(EditorUtils.listModelStream(l_travelsModel).collect(Collectors.toMap(
                TravelEntry::toString,
                TravelEntry::getRequiredIds
        )));
        
        return place;
    }
    
    private void installPlace(Place place)
    {
        name.setText(place.getName());
        
        cb_wildBattleModel.setSelectedBattle(place.getWildBattle());
        cb_trainerBattleModel.setSelectedBattle(place.getTrainerBattle());
        
        lock(l_challenge);
        l_challengeModel.removeAllElements();
        l_challengeModel.addAll(place.getAllChallenges());
        if(!l_challengeModel.isEmpty())
            l_challenge.setSelectedIndex(0);
        unlock(l_challenge);
        
        lock(l_travels);
        l_travelsModel.removeAllElements();
        l_travelsModel.addAll(place.getAllTravels().entrySet().stream()
                .filter(e -> TravelEntry.isValid(e.getKey()))
                .map(TravelEntry::new)
                .collect(Collectors.toList()));
        unlock(l_travels);
        
        showChallenge();
        showTravel();
    }
    
    private void store()
    {
        final var placeName = name.getText();
        if(placeName == null || placeName.isBlank())
        {
            JOptionPane.showMessageDialog(this,
                    "Debe haber un nombre válido para el lugar, no vacío.",
                    "Error de guardado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        final var place = buildPlace();
        Place.save(place, placeName);
        updateTitle();
        JOptionPane.showMessageDialog(this,
                    "¡El lugar \"" + placeName + "\" ha sido guardado con éxito!",
                    "Lugar guardado",
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void load()
    {
        final var places = Place.getAllSavedNames();
        if(places == null || places.length < 1)
            return;
        
        final var placeName = JOptionPane.showInputDialog(this,
                "¿Qué lugar deseas cargar?",
                "Cargar lugar",
                JOptionPane.QUESTION_MESSAGE,
                null,
                places,
                places[0]);
        if(placeName == null || placeName.toString().isBlank())
            return;
        
        final var place = Place.load(placeName.toString());
        if(place == null)
            return;
        
        installPlace(place);
        updateTitle();
        JOptionPane.showMessageDialog(this,
                    "¡El lugar \"" + placeName + "\" ha sido cargado con éxito!",
                    "Lugar cargado",
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    
    private final class BattleComboBoxModel extends DefaultComboBoxModel<BattleEntry>
    {
        private final JComboBox<BattleEntry> box;
        
        private BattleComboBoxModel(JComboBox<BattleEntry> box)
        {
            this.box = box;
            init();
        }
        
        private void init()
        {
            box.setModel(this);
        }
        
        private void updateValues()
        {
            var sel = box.getSelectedItem();
            
            removeAllElements();
            addAll(List.of(battlesCache));
            
            if(sel == null)
                box.setSelectedIndex(0);
            else box.setSelectedItem((BattleEntry) sel);
        }
        
        private void setSelectedBattle(String battleId)
        {
            box.setSelectedItem(new BattleEntry(battleId));
        }
    }
    
    private static class AbstractEntry<E extends AbstractEntry<E>> implements Comparable<E>
    {
        protected final String id;
        
        private AbstractEntry(String id)
        {
            this.id = id == null || id.isBlank() ? null : id;
        }
        
        public final boolean isValid() { return id != null; }
        
        public final boolean equals(E e)
        {
            return e != null && (id == null ? e.id == null : id.equals(e.id));
        }
        
        @Override
        public final boolean equals(Object o)
        {
            if(o == this)
                return true;
            if(o == null)
                return false;
            
            if(getClass().isInstance(o))
            {
                return id == null
                        ? ((AbstractEntry) o).id == null
                        : id.equals(((AbstractEntry) o).id);
            }
            return false;
        }

        @Override
        public final int hashCode()
        {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.id);
            return hash;
        }
        
        @Override
        public final String toString() { return id == null ? "[Invalid]" : id; }

        @Override
        public int compareTo(E o)
        {
            return id == null ? o.id == null ? 0 : 1 : id.compareTo(o.id);
        }
        
        public static final boolean isValid(String battleId)
        {
            return battleId != null && !battleId.isBlank();
        }
    }
    
    private static final class BattleEntry extends AbstractEntry<BattleEntry>
    {
        public static final BattleEntry INVALID = new BattleEntry(null);
        
        private BattleEntry(String battleId) { super(battleId); }
        
        public final String getBattleId() { return isValid() ? id : ""; }
    }
    
    private static final class TravelEntry extends AbstractEntry<TravelEntry>
    {
        private String[] required = {};
        
        private TravelEntry(String travelId) { super(travelId); }
        
        private TravelEntry(Map.Entry<String, String[]> entry)
        {
            this(entry.getKey());
            required = Arrays.copyOf(entry.getValue(), entry.getValue().length);
        }
        
        public final String getPlaceName() { return isValid() ? id : ""; }
        
        public final void setRequiredIds(String[] ids)
        {
            if(ids == null || ids.length < 1)
                required = new String[]{};
            else required = Arrays.copyOf(ids, ids.length);
        }
        
        public final String[] getRequiredIds() { return required; }
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
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        challengeDesc = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        l_challengeBattles = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        b_addChallengeBattle = new javax.swing.JButton();
        b_removeChallengeBattle = new javax.swing.JButton();
        b_upChallengeBattle = new javax.swing.JButton();
        b_downChallengeBattle = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        challengeId = new javax.swing.JTextField();
        challengeName = new javax.swing.JTextField();
        challengeUnique = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        l_challenge = new javax.swing.JList<>();
        jPanel11 = new javax.swing.JPanel();
        b_createChallenge = new javax.swing.JButton();
        b_removeChallenge = new javax.swing.JButton();
        b_upChallenge = new javax.swing.JButton();
        b_downChallenge = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        l_travels = new javax.swing.JList<>();
        jPanel14 = new javax.swing.JPanel();
        b_addTravel = new javax.swing.JButton();
        b_removeTravel = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        cb_wildBattle = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cb_trainerBattle = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Desafios"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        challengeDesc.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));
        jScrollPane1.setViewportView(challengeDesc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 2.5;
        jPanel4.add(jScrollPane1, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Batallas"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        l_challengeBattles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        l_challengeBattles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                l_challengeBattlesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(l_challengeBattles);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(jScrollPane2, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        b_addChallengeBattle.setText("Añadir");
        b_addChallengeBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_addChallengeBattleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel7.add(b_addChallengeBattle, gridBagConstraints);

        b_removeChallengeBattle.setText("Quitar");
        b_removeChallengeBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_removeChallengeBattleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel7.add(b_removeChallengeBattle, gridBagConstraints);

        b_upChallengeBattle.setText("^");
        b_upChallengeBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_upChallengeBattleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel7.add(b_upChallengeBattle, gridBagConstraints);

        b_downChallengeBattle.setText("v");
        b_downChallengeBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_downChallengeBattleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel7.add(b_downChallengeBattle, gridBagConstraints);

        jButton7.setText("...");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel7.add(jButton7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel5.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jPanel5, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridLayout(1, 3));

        challengeId.setBorder(javax.swing.BorderFactory.createTitledBorder("ID"));
        jPanel6.add(challengeId);

        challengeName.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        jPanel6.add(challengeName);

        challengeUnique.setText("Único");
        challengeUnique.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(challengeUnique);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jPanel6, gridBagConstraints);

        jPanel8.setLayout(new java.awt.GridLayout(1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        jPanel10.add(jPanel4, gridBagConstraints);

        l_challenge.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        l_challenge.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                l_challengeValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(l_challenge);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jScrollPane3, gridBagConstraints);

        jPanel11.setLayout(new java.awt.GridBagLayout());

        b_createChallenge.setText("Crear Desafio");
        b_createChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_createChallengeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel11.add(b_createChallenge, gridBagConstraints);

        b_removeChallenge.setText("Eliminar Desafio");
        b_removeChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_removeChallengeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel11.add(b_removeChallenge, gridBagConstraints);

        b_upChallenge.setText("^");
        b_upChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_upChallengeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel11.add(b_upChallenge, gridBagConstraints);

        b_downChallenge.setText("v");
        b_downChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_downChallengeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel11.add(b_downChallenge, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel10.add(jPanel11, gridBagConstraints);

        jTabbedPane1.addTab("Desafios", jPanel10);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel12.setLayout(new java.awt.GridBagLayout());

        l_travels.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        l_travels.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                l_travelsValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(l_travels);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.5;
        jPanel12.add(jScrollPane4, gridBagConstraints);

        jPanel14.setLayout(new java.awt.GridLayout(2, 1));

        b_addTravel.setText("Añadir enlace");
        b_addTravel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_addTravelActionPerformed(evt);
            }
        });
        jPanel14.add(b_addTravel);

        b_removeTravel.setText("Quitar Enlace");
        b_removeTravel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_removeTravelActionPerformed(evt);
            }
        });
        jPanel14.add(b_removeTravel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel12.add(jPanel14, gridBagConstraints);

        jPanel1.add(jPanel12);

        jPanel13.setLayout(new java.awt.GridLayout(1, 1));
        jPanel1.add(jPanel13);

        jTabbedPane1.addTab("Enlaces", jPanel1);

        jPanel9.setLayout(new java.awt.GridLayout(1, 3));

        name.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));
        name.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFocusLost(evt);
            }
        });
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameKeyReleased(evt);
            }
        });
        jPanel9.add(name);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Batalla Salvaje"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(cb_wildBattle, gridBagConstraints);

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(jButton1, gridBagConstraints);

        jPanel9.add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Batalla Entrenadores"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 0.1;
        jPanel3.add(cb_trainerBattle, gridBagConstraints);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel3.add(jButton2, gridBagConstraints);

        jPanel9.add(jPanel3);

        jPanel15.setLayout(new java.awt.GridLayout(1, 3));

        jButton3.setText("Cargar Lugar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton3);

        jButton4.setText("Crear Lugar nuevo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton4);

        jButton5.setText("Guardar Lugar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void l_challengeValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_l_challengeValueChanged
        showChallenge();
    }//GEN-LAST:event_l_challengeValueChanged

    private void b_createChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_createChallengeActionPerformed
        lock(l_challenge);
        
        var c = new Challenge();
        c.setId(generateChallengeName());
        c.setName(c.getId());
        l_challengeModel.addElement(c);
        l_challenge.setSelectedValue(c, true);
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_createChallengeActionPerformed

    private void b_removeChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_removeChallengeActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        if(sel != null && JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar el desafío \"" + sel.getName() + "\"?",
                "Eliminar desafío",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            l_challengeModel.removeElement(sel);
            if(!l_challengeModel.isEmpty())
                l_challenge.setSelectedIndex(0);
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_removeChallengeActionPerformed

    private void b_addChallengeBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_addChallengeBattleActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        
        if(sel != null)
        {
            var battle = selectBattle();
            if(battle != null)
            {
                sel.setBattles(Stream.concat(sel.streamBattles(), Stream.of(battle))
                        .toArray(String[]::new));
                l_challengeBattles.setSelectedValue(battle, true);
            }
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_addChallengeBattleActionPerformed

    private void b_upChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_upChallengeActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        var idx = l_challenge.getSelectedIndex();
        
        if(sel != null && idx > 0)
        {
            l_challengeModel.removeElementAt(idx);
            l_challengeModel.add(idx - 1, sel);
            l_challenge.setSelectedValue(sel, true);
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_upChallengeActionPerformed

    private void b_downChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_downChallengeActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        var idx = l_challenge.getSelectedIndex();
        
        if(sel != null && idx + 1 < l_challengeModel.size())
        {
            l_challengeModel.removeElementAt(idx);
            l_challengeModel.add(idx + 1, sel);
            l_challenge.setSelectedValue(sel, true);
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_downChallengeActionPerformed

    private void b_removeChallengeBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_removeChallengeBattleActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        
        if(sel != null)
        {
            var battle = getSelectedValue(l_challengeBattles);
            if(battle != null)
            {
                sel.setBattles(sel.streamBattles()
                        .filter(b -> !b.equals(battle))
                        .toArray(String[]::new));
                l_challengeBattles.setSelectedValue(battle, true);
            }
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_removeChallengeBattleActionPerformed

    private void b_upChallengeBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_upChallengeBattleActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        
        if(sel != null)
        {
            var battle = getSelectedValue(l_challengeBattles);
            var idx = l_challengeBattles.getSelectedIndex();
            if(battle != null && idx > 0)
            {
                sel.setBattles(Utils.arraySwap(sel.getAllBattles(), idx, idx - 1));
                l_challengeBattles.setSelectedValue(battle, true);
            }
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_upChallengeBattleActionPerformed

    private void b_downChallengeBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_downChallengeBattleActionPerformed
        lock(l_challenge);
        
        var sel = getSelectedValue(l_challenge);
        
        if(sel != null)
        {
            var battle = getSelectedValue(l_challengeBattles);
            var idx = l_challengeBattles.getSelectedIndex();
            if(battle != null && idx + 1 < l_challengeBattlesModel.size())
            {
                sel.setBattles(Utils.arraySwap(sel.getAllBattles(), idx, idx + 1));
                l_challengeBattles.setSelectedValue(battle, true);
            }
        }
        
        unlock(l_challenge);
        showChallenge();
    }//GEN-LAST:event_b_downChallengeBattleActionPerformed

    private void l_challengeBattlesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_l_challengeBattlesValueChanged
        var sel = getSelectedValue(l_challenge);
        if(sel != null)
        {
            final var bidxsel = l_challengeBattles.getSelectedIndex();
            b_removeChallengeBattle.setEnabled(bidxsel >= 0);
            b_upChallengeBattle.setEnabled(bidxsel > 0);
            b_downChallengeBattle.setEnabled(bidxsel + 1 < l_challengeBattlesModel.size());
        }
    }//GEN-LAST:event_l_challengeBattlesValueChanged

    private void b_addTravelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_addTravelActionPerformed
        lock(l_travels);
        
        var place = selectTravel();
        var travel = new TravelEntry(place);
        if(!EditorUtils.containsInListModel(l_travelsModel, travel))
        {
            l_travelsModel.addElement(travel);
            l_travels.setSelectedValue(travel, true);
        }
        
        unlock(l_travels);
        showTravel();
    }//GEN-LAST:event_b_addTravelActionPerformed

    private void b_removeTravelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_removeTravelActionPerformed
        lock(l_travels);
        
        var sel = getSelectedValue(l_travels);
        if(sel != null)
        {
            l_travelsModel.removeElement(sel);
            if(!l_travelsModel.isEmpty())
                l_travels.setSelectedIndex(0);
        }
        
        unlock(l_travels);
        showTravel();
    }//GEN-LAST:event_b_removeTravelActionPerformed

    private void l_travelsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_l_travelsValueChanged
        showTravel();
    }//GEN-LAST:event_l_travelsValueChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        newPlace();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        store();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        load();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openBattleEditor();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        openBattleEditor();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        openBattleEditor();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyReleased
        updateTitle();
    }//GEN-LAST:event_nameKeyReleased

    private void nameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFocusLost
        updateTitle();
    }//GEN-LAST:event_nameFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_addChallengeBattle;
    private javax.swing.JButton b_addTravel;
    private javax.swing.JButton b_createChallenge;
    private javax.swing.JButton b_downChallenge;
    private javax.swing.JButton b_downChallengeBattle;
    private javax.swing.JButton b_removeChallenge;
    private javax.swing.JButton b_removeChallengeBattle;
    private javax.swing.JButton b_removeTravel;
    private javax.swing.JButton b_upChallenge;
    private javax.swing.JButton b_upChallengeBattle;
    private javax.swing.JComboBox<BattleEntry> cb_trainerBattle;
    private javax.swing.JComboBox<BattleEntry> cb_wildBattle;
    private javax.swing.JTextPane challengeDesc;
    private javax.swing.JTextField challengeId;
    private javax.swing.JTextField challengeName;
    private javax.swing.JCheckBox challengeUnique;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<Challenge> l_challenge;
    private javax.swing.JList<String> l_challengeBattles;
    private javax.swing.JList<TravelEntry> l_travels;
    private javax.swing.JTextField name;
    // End of variables declaration//GEN-END:variables
}
