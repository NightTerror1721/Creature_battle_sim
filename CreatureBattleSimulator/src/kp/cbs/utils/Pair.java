/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.utils;

public class Pair<L, R>
{
    public final L left;
    public final R right;
    
    public Pair(L left, R right)
    {
        this.left = left;
        this.right = right;
    }
    
    public final boolean equals(Pair<?, ?> pair)
    {
        return left.equals(pair.left) &&
               right.equals(pair.right);
    }
    
    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return left.equals(pairo.left) &&
               right.equals(pairo.right);
    }

    @Override
    public final int hashCode() { return left.hashCode() ^ right.hashCode(); }
}
