/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.util.LinkedList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import kp.cbs.creature.Creature;
import kp.cbs.creature.altered.AlteredStateId;
import kp.cbs.creature.attack.Attack;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.creature.attack.AttackSlot;
import kp.cbs.utils.Formula;

/**
 *
 * @author Asus
 */
public enum ItemId
{
    POTION("Poción", 200),
    SUPER_POTION("Super Poción", 400),
    HYPER_POTION("Hiper Poción", 1000),
    MAX_POTION("Poción Máxima", 2000),
    
    ANTIDOTE("Antídoto", 100),
    PARALYZ_HEAL("Antiparaliz", 100),
    BURN_HEAL("Antiquemar", 200),
    ICE_HEAL("Antihielo", 200),
    AWAKENING("Despertar", 200),
    FULL_HEAL("Cura Total", 500),
    
    REVIVE("Revivir", 1500),
    MAX_REVIVE("Revivir Máximo", 3000),
    
    FULL_RESTORE("Restaurar Todo", 2500),
    
    ETHER("Éter", 1500),
    MAX_ETHER("Éter Máximo", 2000),
    ELIXIR("Elixir", 2500),
    MAX_ELIXIR("Elixir Máximo", 3000),
    
    CATCHER("Capturador", 200),
    SUPER_CATCHER("Super Capturador", 500),
    ULTRA_CATCHER("Ultra Capturador", 1000),
    TIMER_CATCHER("Capturador por Tiempo", 800),
    FAST_CATCHER("Capturador Rápido", 800),
    MASTER_CATCHER("Capturador Maestro", 100_000);
    
    private final String name;
    private final int price;
    
    private ItemId(String name, int price)
    {
        this.name = name;
        this.price = Math.max(1, price);
    }
    
    public final String getName() { return name; }
    
    public final int getPrice() { return price; }
    
    @Override
    public final String toString() { return name; }
    
    public final boolean isCatcherItem()
    {
        switch(this)
        {
            case CATCHER:
            case SUPER_CATCHER:
            case ULTRA_CATCHER:
            case TIMER_CATCHER:
            case FAST_CATCHER:
            case MASTER_CATCHER:
                return true;
            default: return false;
        }
    }
    
    public final float getCatchMultiplier(int turn)
    {
        switch(this)
        {
            default:
            case CATCHER: return 1f;
            case SUPER_CATCHER: return 1.5f;
            case ULTRA_CATCHER: return 2f;
            case TIMER_CATCHER: return Formula.timerCatcherMultiplier(turn);
            case FAST_CATCHER: return Formula.rapidCatcherMultiplier(turn);
            case MASTER_CATCHER: return 65535f;
        }
    }
    
    public final String getDescription()
    {
        switch(this)
        {
            case POTION: return "Restaura 500 puntos de salud.";
            case SUPER_POTION: return "Restaura 1000 puntos de salud.";
            case HYPER_POTION: return "Restaura 3000 puntos de salud.";
            case MAX_POTION: return "Restaura toda la salud.";
            case ANTIDOTE: return "Cura el envenenamiento y el intoxicamiento.";
            case PARALYZ_HEAL: return "Cura la parálisis.";
            case BURN_HEAL: return "Cura las quemaduras.";
            case ICE_HEAL: return "Cura la congelación.";
            case AWAKENING: return "Despierta del sueño profundo.";
            case FULL_HEAL: return "Cura cualquier problema de estado.";
            case REVIVE: return "Revive a un luchador debilitado, otorgandole la mitad de su salud máxima.";
            case MAX_REVIVE: return "Revive a un luchador debilitado, otorgandole la salud completa.";
            case FULL_RESTORE: return "Restaura toda la salud y cura todos los problemas de estado.";
            case ETHER: return "Restaura 10 PP de un ataque.";
            case MAX_ETHER: return "Restaura todos los PP de un ataque.";
            case ELIXIR: return "Restaura 10 PP a todos los ataques.";
            case MAX_ELIXIR: return "Restaura todos los PP a todos los ataques.";
            case CATCHER: return "Captura a un luchador. Tiene ratio 1.";
            case SUPER_CATCHER: return "Captura a un luchador. Tiene ratio 1.5.";
            case ULTRA_CATCHER: return "Captura a un luchador. Tiene ratio 2.";
            case TIMER_CATCHER: return "Captura a un luchador. Su augmenta según avanza el combate. Ratio minimo 1, máximo 5.";
            case FAST_CATCHER: return "Captura a un luchador. Su ratio es 5 en el primer turno, el resto del combate es 1.";
            case MASTER_CATCHER: return "Captura a un luchador. Nunca falla.";
            default: return "<UNKNOWN ITEM>";
        }
    }
    
    public final boolean applyEffect(JDialog parent, Creature creature)
    {
        switch(this)
        {
            case POTION: return heal(creature, 500);
            case SUPER_POTION: return heal(creature, 1000);
            case HYPER_POTION: return heal(creature, 3000);
            case MAX_POTION: return heal(creature);
            case ANTIDOTE:
                cure(creature, AlteredStateId.POISONING);
                return cure(creature, AlteredStateId.INTOXICATION);
            case PARALYZ_HEAL: return cure(creature, AlteredStateId.PARALYSIS);
            case BURN_HEAL: return cure(creature, AlteredStateId.BURN);
            case ICE_HEAL: return cure(creature, AlteredStateId.FREEZING);
            case AWAKENING: return cure(creature, AlteredStateId.SLEEP);
            case FULL_HEAL: return cure(creature);
            case REVIVE: return revive(creature, false);
            case MAX_REVIVE: return revive(creature, true);
            case FULL_RESTORE: return fullRestore(creature);
            case ETHER: return ether(parent, creature, false);
            case MAX_ETHER: return ether(parent, creature, true);
            case ELIXIR: return elixir(creature, false);
            case MAX_ELIXIR: return elixir(creature, true);
            default: return false;
        }
    }
    
    private static boolean heal(Creature c, int points)
    {
        if(!c.isAlive() || c.isFullHealth())
            return false;
        c.getHealthPoints().heal(points);
        return true;
    }
    private static boolean heal(Creature c)
    {
        if(!c.isAlive() || c.isFullHealth())
            return false;
        c.getHealthPoints().fullHeal();
        return true;
    }
    
    private static boolean cure(Creature c, AlteredStateId state)
    {
        if(!c.isAlive())
            return false;
        return c.getAlterationManager().rawDeleteAlterationState(state);
    }
    private static boolean cure(Creature c)
    {
        if(!c.isAlive())
            return false;
        return c.getAlterationManager().rawDeleteAllAlterationStates();
    }
    
    private static boolean fullRestore(Creature c)
    {
        if(!c.isAlive())
            return false;
        var state = c.isFullHealth();
        c.getHealthPoints().fullHeal();
        if(c.getAlterationManager().rawDeleteAllAlterationStates())
            state = true;
        return state;
    }
    
    private static boolean revive(Creature c, boolean full)
    {
        if(c.isAlive())
            return false;
        if(full)
            c.getHealthPoints().fullHeal();
        else c.getHealthPoints().heal(c.getMaxHealthPoints() / 2);
        return true;
    }
    
    private static boolean ether(Creature c, AttackSlot slot, boolean full)
    {
        var attack = c.getAttack(slot);
        if(attack == null)
            return false;
        if(full)
            attack.restoreAllPP();
        else attack.restorePP(10);
        return true;
    }
    private static boolean ether(JDialog parent, Creature c, boolean full)
    {
        var list = new LinkedList<SelectedAttack>();
        for(var slot = AttackSlot.SLOT_1; slot != null; slot = slot.next())
        {
            var attack = c.getAttack(slot);
            if(attack != null)
            {
                var sel = new SelectedAttack(slot, attack);
                list.add(sel);
            }
        }
        if(list.isEmpty())
            return false;
        
        var array = list.toArray(SelectedAttack[]::new);
        var sel = JOptionPane.showInputDialog(parent, "¿Aque ataque quieres aplicarlo?", "Éter", JOptionPane.QUESTION_MESSAGE,
                null, array, array[0]);
        if(sel == null || !(sel instanceof SelectedAttack))
            return false;
        
        return ether(c, ((SelectedAttack) sel).slot, full);
    }
    
    private static boolean elixir(Creature c, boolean full)
    {
        int falses = 0;
        for(var slot = AttackSlot.SLOT_1; slot != null; slot = slot.next())
            if(!ether(c, slot, full))
                falses++;
        return falses < 4;
    }
    
    private static final class SelectedAttack
    {
        private final AttackModel attack;
        private final AttackSlot slot;
        
        private SelectedAttack(AttackSlot slot, Attack attack)
        {
            this.slot = slot;
            this.attack = attack.getModel();
        }
        
        @Override
        public final String toString() { return attack.getName(); }
    }
}
