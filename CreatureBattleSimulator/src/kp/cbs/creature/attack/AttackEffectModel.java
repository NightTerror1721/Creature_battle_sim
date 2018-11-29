/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import kp.cbs.creature.attack.flag.Flag;
import kp.cbs.creature.attack.flag.FlagModel;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "buildDescription")
public class AttackEffectModel
{
    @Property
    private int id;
    
    @Property
    private String name;
    
    @Property
    private FlagModel[] flags;
    
    @Property(name = "description")
    private String rawDescription;
    
    private final LinkedList<DescNode> desc = new LinkedList<>();
    
    
    public final int getId() { return id; }
    public final String getName() { return name; }
    public List<FlagModel> getFlags() { return Collections.unmodifiableList(Arrays.asList(flags)); }
    
    final Flag[] generateFlags(int[] flagValues)
    {
        if(flags.length != flagValues.length)
            throw new IllegalStateException();
        Flag[] f = new Flag[flags.length];
        for(int i=0;i<flags.length;i++)
            f[i] = new Flag(flags[i], flagValues[i]);
        return f;
    }
    
    final String generateDescription(AttackEffect effect)
        {
            if(desc.isEmpty())
                return "";
            
            StringBuilder sb = new StringBuilder();
            for(DescNode node : desc)
                sb.append(node.generateString(effect));
            return sb.toString();
        }
    
    private void buildDescription()
    {
        StringBuilder sb = new StringBuilder();
        char[] chars = rawDescription.toCharArray();
        for(int i=0;i<chars.length;i++)
        {
            char c;
            switch(c = chars[i])
            {
                case '$':
                    if(sb.length() > 0)
                    {
                        desc.add(new TextDescNode(sb.toString()));
                        sb.delete(0, sb.length());
                    }
                    if(i + 1 >= chars.length)
                        break;
                    for(i++; i < chars.length; i++)
                    {
                        if(!Character.isDigit(c = chars[i]))
                        {
                            i--;
                            break;
                        }
                        sb.append(c);
                    }
                    if(sb.length() > 0)
                    {
                        desc.add(new FlagDescNode(Integer.parseInt(sb.toString())));
                        sb.delete(0, sb.length());
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        if(sb.length() > 0)
            desc.add(new TextDescNode(sb.toString()));
    }
    
    private static AttackEffectModel injector() { return new AttackEffectModel(); }
    
    
    
    
    
    
    
    
    
    
    
    
    private abstract class DescNode
    {
        public abstract String generateString(AttackEffect effect);
    }
    
    private final class TextDescNode extends DescNode
    {
        private final String text;
        private TextDescNode(String text) { this.text = text == null ? "" : text; }
        @Override public final String generateString(AttackEffect effect) { return text; }
    }
    
    private final class FlagDescNode extends DescNode
    {
        private final int flagId;
        private FlagDescNode(int flagId) { this.flagId = flagId < 0 ? 0 : flagId; }
        @Override public final String generateString(AttackEffect effect)
        {
            if(flagId >= flags.length)
                return "";
            return effect.getFlag(flagId).toString();
        }
    }
}
