/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle.cmd;

/**
 *
 * @author Asus
 */
public final class BattleCommand
{
    private final BattleCommandType type;
    private final Object[] parameters;
    
    BattleCommand(BattleCommandType type, Object... parameters)
    {
        this.type = type;
        this.parameters = parameters;
    }
    
    public final BattleCommandType getType() { return type; }
    
    public final int size() { return parameters.length; }
    
    public final <T> T get(int index) { return (T) parameters[index]; }
}
