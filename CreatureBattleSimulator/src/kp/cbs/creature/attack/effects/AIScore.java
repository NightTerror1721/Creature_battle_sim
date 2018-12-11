/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.utils.RNG;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public final class AIScore implements Comparable<AIScore>
{
    private static final int I_MAX_SCORE = 65535;
    private static final int I_MIN_SCORE = -I_MAX_SCORE;
    
    private int score;
    
    public AIScore() {}
    private AIScore(int score) { this.score = Utils.range(I_MIN_SCORE, I_MAX_SCORE, score); }
    
    public final int getScore() { return score; }
    
    public final AIScore add(int value) { this.score = Math.min(I_MAX_SCORE, score + (value < 0 ? 0 : value)); return this; }
    public final AIScore subtract(int value) { this.score = Math.max(I_MIN_SCORE, score - (value < 0 ? 0 : value)); return this; }
    public final AIScore multiply(float value) { this.score = Utils.range(I_MIN_SCORE, I_MAX_SCORE, (int) (score * (value < 0 ? -value : value))); return this; }
    public final AIScore divide(float value) { this.score = Utils.range(I_MIN_SCORE, I_MAX_SCORE, (int) (score / (value < 0 ? -value : value))); return this; }
    
    public final AIScore invert() { this.score = Utils.range(I_MIN_SCORE, I_MAX_SCORE, -score); return this; }
    
    public final AIScore maximize() { this.score = I_MAX_SCORE; return this; }
    public final AIScore nullify() { this.score = 0; return this; }
    public final AIScore minimize() { this.score = I_MIN_SCORE; return this; }
    
    public final boolean equals(AIScore score) { return this.score == score.score; }
    
    @Override
    public final boolean equals(Object o) { return o instanceof AIScore && score == ((AIScore) o).score; }

    @Override
    public final int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + this.score;
        return hash;
    }

    @Override
    public final int compareTo(AIScore o)  { return Integer.compare(score, o.score); }
    
    public static final AIScore concat(AIScore s0, AIScore s1)
    {
        return new AIScore(s0.score + s1.score);
    }
    
    public static final AIScore concat(AIScore sBase, AIScore... scores)
    {
        switch(scores.length)
        {
            case 0: return new AIScore(sBase.score);
            case 1: return concat(sBase, scores[0]);
            default: {
                long value = sBase.score;
                for(AIScore score : scores)
                    value += score.score;
                return new AIScore((int) value);
            }
        }
    }
    
    public static final AIScore combine(AIScore... scores)
    {
        switch(scores.length)
        {
            case 0: return zero();
            case 1: return new AIScore(scores[0].score);
            default: {
                int min = I_MIN_SCORE + I_MAX_SCORE;
                int max = I_MAX_SCORE + I_MAX_SCORE;
                long lmid = 0;
                for(AIScore score : scores)
                {
                    min = Math.min(min, score.score + I_MAX_SCORE);
                    max = Math.max(max, score.score + I_MAX_SCORE);
                    lmid += score.score;
                }
                
                int dif = max - min;
                if(dif <= 0)
                    return new AIScore(max);
                float fmid = lmid / scores.length - min;
                
                return new AIScore((int) (max - (dif / ((fmid / dif * 6f) + 1f))) - I_MAX_SCORE);
            }
        }
    }
    
    
    public static final AIScore maximum() { return new AIScore(I_MAX_SCORE); }
    public static final AIScore zero() { return new AIScore(0); }
    public static final AIScore minimum() { return new AIScore(I_MIN_SCORE); }
    
    public static final AIScore medium(boolean negative) { return new AIScore((negative ? -I_MAX_SCORE : I_MAX_SCORE) / 2); }
    public static final AIScore third(boolean negative) { return new AIScore((negative ? -I_MAX_SCORE : I_MAX_SCORE) / 3); }
    public static final AIScore fourth(boolean negative) { return new AIScore((negative ? -I_MAX_SCORE : I_MAX_SCORE) / 4); }
    public static final AIScore eighth(boolean negative) { return new AIScore((negative ? -I_MAX_SCORE : I_MAX_SCORE) / 8); }
    
    public static final AIScore random(RNG rng, boolean negative)
    {
        int value = rng.d(I_MAX_SCORE + 1);
        return new AIScore(negative ? -value : value);
    }
    public static final AIScore random(RNG rng) { return new AIScore(rng.d((I_MAX_SCORE + 1) * 2) - I_MAX_SCORE); }
}
