/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature;

import java.util.Arrays;
import kp.cbs.creature.feat.StatId;
import static kp.cbs.creature.feat.StatId.*;
import kp.cbs.utils.RNG;

/**
 *
 * @author Asus
 */
public enum Nature
{
    HARDY("Fuerte", ATTACK, ATTACK),
    LONELY("Huraña", ATTACK, DEFENSE),
    BRAVE("Audaz", ATTACK, SPEED),
    ADAMANT("Firme", ATTACK, SPECIAL_ATTACK),
    NAUGHTY("Pícara", ATTACK, SPECIAL_DEFENSE),
    BOLD("Osada", DEFENSE, ATTACK),
    DOCILE("Dócil", DEFENSE, DEFENSE),
    RELAXED("Plácida", DEFENSE, SPEED),
    IMPISH("Agitada", DEFENSE, SPECIAL_ATTACK),
    LAX("Floja", DEFENSE, SPECIAL_DEFENSE),
    TIMID("Miedosa", SPEED, ATTACK),
    HASTY("Activa", SPEED, DEFENSE),
    SERIOUS("Seria", SPEED, SPEED),
    JOLLY("Alegre", SPEED, SPECIAL_ATTACK),
    NAIVE("Ingenua", SPEED, SPECIAL_DEFENSE),
    MODEST("Modesta", SPECIAL_ATTACK, ATTACK),
    MILD("Afable", SPECIAL_ATTACK, DEFENSE),
    QUIET("Mansa", SPECIAL_ATTACK, SPEED),
    BASHFUL("Tímida", SPECIAL_ATTACK, SPECIAL_ATTACK),
    RASH("Alocada", SPECIAL_ATTACK, SPECIAL_DEFENSE),
    CALM("Serena", SPECIAL_DEFENSE, ATTACK),
    GENTILE("Amable", SPECIAL_DEFENSE, DEFENSE),
    SASSY("Grosera", SPECIAL_DEFENSE, SPEED),
    CAREFUL("Cauta", SPECIAL_DEFENSE, SPECIAL_ATTACK),
    QUIRKY("Rara", SPECIAL_DEFENSE, SPECIAL_DEFENSE),
    UNDEFINED("No definida", null, null);
    
    private final String name;
    private final StatId up;
    private final StatId down;
    
    private Nature(String name, StatId up, StatId down)
    {
        this.name = name;
        this.up = up;
        this.down = down;
    }
    
    public final String getName() { return name; }
    
    public final float getStatModificator(StatId stat)
    {
        if(up == down || this == UNDEFINED)
            return 1f;
        return stat == up ? 1.1f : stat == down ? 0.9f : 1f;
    }
    
    @Override
    public final String toString() { return name; }
    
    private static final Nature[] VALUES = generateNormalNatures();
    
    public static final Nature random(RNG rng)
    {
        return VALUES[rng.d(VALUES.length)];
    }
    
    private static Nature[] generateNormalNatures()
    {
        var all = values();
        return Arrays.copyOf(all, all.length - 1);
    }
}
