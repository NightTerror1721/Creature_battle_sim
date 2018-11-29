/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.flag;

import java.util.Objects;
import kp.cbs.creature.altered.AlteredStateId;
import kp.cbs.creature.elements.ElementalType;
import kp.cbs.creature.feat.StatId;
import kp.cbs.creature.state.StateId;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public final class Flag
{
    private final FlagModel model;
    private final int value;
    
    public Flag(FlagModel model, int value)
    {
        this.model = Objects.requireNonNull(model);
        this.value = parse(value);
    }
    
    public final FlagModel getModel() { return model; }
    public final FlagType getType() { return model.getType(); }
    
    public final <T> T getValue() { return (T) value(); }
    
    private int parse(int value)
    {
        switch(model.getType())
        {
            case DEC_PERCENT: return Utils.range(-1, 100, value);
            case BYTE_PERCENT: return Utils.range(-1, 256, value);
            case POWER: return Utils.range(1, 255, value);
            case TARGET: return Utils.range(0, 1, value);
            case ELEMENT: return Utils.range(0, ElementalType.getElementalTypeCount() - 1, value);
            case STAT: return Utils.range(1, 5, value);
            case PRECISION: return Utils.range(0, 1, value);
            case ALTERED: return Utils.range(0, AlteredStateId.count() - 1, value);
            default: throw new IllegalStateException();
        }
    }
    
    private Object value()
    {
        switch(model.getType())
        {
            case DEC_PERCENT: return value;
            case BYTE_PERCENT: return value;
            case POWER: return value;
            case TARGET: return value == 0;
            case ELEMENT: return ElementalType.getElementalType(value);
            case STAT: return StatId.decode(value);
            case PRECISION: return value == 0 ? StateId.ACCURACY : StateId.EVASION;
            case ALTERED: return AlteredStateId.decode(value);
            default: throw new IllegalStateException();
        }
    }
    
    @Override
    public final String toString()
    {
        switch(model.getType())
        {
            case DEC_PERCENT: {
                int per = getValue();
                return per < 0 ? "Probabilidad asegurada" : per + "%";
            }
            case BYTE_PERCENT: {
                float fper = this.<Integer>getValue().floatValue();
                fper = fper > 0 ? fper * 100f / 256f : fper;
                if(((float)((int) fper)) == fper)
                {
                    int per = (int) fper;
                    return per < 0 ? "Probabilidad asegurada" : per + "%";
                }
                return fper < 0 ? "Probabilidad asegurada" : fper + "%";
            }
            case POWER: return value().toString();
            case TARGET: return getValue() ? "Objetivo" : "Usuario";
            case ELEMENT: return this.<ElementalType>getValue().getName();
            case STAT: return this.<StatId>getValue().getName();
            case PRECISION: this.<StateId>getValue().getName();
            case ALTERED: return this.<AlteredStateId>getValue().getName();
            default: throw new IllegalStateException();
        }
    }
}
