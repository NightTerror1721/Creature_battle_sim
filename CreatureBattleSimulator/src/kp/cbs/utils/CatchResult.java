/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

/**
 *
 * @author Asus
 */
public enum CatchResult
{
    FAIL_0,
    FAIL_1,
    FAIL_2,
    FAIL_3,
    CATCHED;
    
    public final boolean isCatched() { return this == CATCHED; }
    
    public final boolean isFullFailed() { return this == FAIL_0; }
    public final boolean hasOneAssert() { return ordinal() > 0; }
    public final boolean hasTwoAsserts() { return ordinal() > 1; }
    public final boolean hasThreeAsserts() { return ordinal() > 2; }
    
}
