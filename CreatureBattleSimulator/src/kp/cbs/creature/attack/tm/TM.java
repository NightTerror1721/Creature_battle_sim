/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.tm;

import java.util.Objects;
import kp.cbs.creature.attack.AttackModel;
import kp.cbs.utils.IdentifierObject;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
public final class TM implements IdentifierObject, Comparable<TM>
{
    @Property
    private int id;
    
    @Property
    private AttackModel attack;
    
    @Override
    public final void setId(int id) { this.id = Math.max(0, id); }
    
    @Override
    public final int getId() { return id; }
    
    public final void setAttack(AttackModel model) { this.attack = Objects.requireNonNull(model); }
    public final AttackModel getAttack() { return attack == null ? new AttackModel() : attack; }

    @Override
    public final int compareTo(TM o) { return  }
}
