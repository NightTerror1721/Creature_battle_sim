/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack.effects;

import kp.cbs.utils.Utils;
import kp.udl.autowired.InjectOptions;
import kp.udl.autowired.Property;

/**
 *
 * @author Asus
 */
@InjectOptions(builder = "injector", afterBuild = "postInject")
public final class AIIntelligence
{
    public static final int DUMMY_RATIO = 0;
    public static final int GIFTED_RATIO = 255;
    
    public static final int MIN_LOW_RATIO = 1;
    public static final int MIN_NORMAL_RATIO = 64;
    public static final int MIN_HIGHT_RATIO = 128;
    public static final int MIN_VERY_HIGHT_RATIO = 192;
    
    public static final int MAX_LOW_RATIO = MIN_NORMAL_RATIO - 1;
    public static final int MAX_NORMAL_RATIO = MIN_HIGHT_RATIO - 1;
    public static final int MAX_HIGHT_RATIO = MIN_VERY_HIGHT_RATIO - 1;
    public static final int MAX_VERY_HIGHT_RATIO = GIFTED_RATIO - 1;
    
    
    @Property
    private int ratio;
    
    private AIIntelligence(int value)
    {
        this.ratio = Utils.range(DUMMY_RATIO, GIFTED_RATIO, value);
    }
    
    public static final AIIntelligence createDummy() { return new AIIntelligence(0); }
    public static final AIIntelligence createGifted() { return new AIIntelligence(255); }
    public static final AIIntelligence create(int intelligenceValue) { return new AIIntelligence(intelligenceValue); }
    
    public final boolean isDummy() { return ratio <= DUMMY_RATIO; }
    public final boolean isGifted() { return ratio >= GIFTED_RATIO; }
    
    public final boolean isLow() { return ratio > DUMMY_RATIO && ratio < MIN_NORMAL_RATIO; }
    public final boolean isLowOrLess() { return ratio < MIN_NORMAL_RATIO; }
    public final boolean isLowOrGreater() { return ratio > DUMMY_RATIO; }
    
    public final boolean isNormal() { return ratio >= MIN_NORMAL_RATIO && ratio < MIN_HIGHT_RATIO; }
    public final boolean isNormalOrLess() { return ratio < MIN_HIGHT_RATIO; }
    public final boolean isNormalOrGreater() { return ratio >= MIN_NORMAL_RATIO; }
    
    public final boolean isHight() { return ratio >= MIN_HIGHT_RATIO && ratio < MIN_VERY_HIGHT_RATIO; }
    public final boolean isHightOrLess() { return ratio < MIN_VERY_HIGHT_RATIO; }
    public final boolean isHightOrGreater() { return ratio >= MIN_HIGHT_RATIO; }
    
    public final boolean isVeryHight() { return ratio >= MIN_VERY_HIGHT_RATIO && ratio < GIFTED_RATIO; }
    public final boolean isVeryHightOrLess() { return ratio < GIFTED_RATIO; }
    public final boolean isVeryHightOrGreater() { return ratio >= MIN_VERY_HIGHT_RATIO; }
    
    public final boolean isLessThan(int ratio) { return this.ratio < ratio; }
    public final boolean isLessOrEqualsThan(int ratio) { return this.ratio <= ratio; }
    public final boolean isGreaterThan(int ratio) { return this.ratio > ratio; }
    public final boolean isGreaterOrEqualsThan(int ratio) { return this.ratio >= ratio; }
    public final boolean isEquals(int ratio) { return this.ratio == ratio; }
    
    public final int getRatio() { return ratio; }
    
    
    private static AIIntelligence injector() { return new AIIntelligence(0); }
    private void postInject() { this.ratio = Utils.range(0, 255, ratio); }
}
