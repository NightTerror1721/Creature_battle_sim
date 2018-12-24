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
public final class Semaphore
{
    private final boolean[] _mode;
    private final boolean lock;
    
    public Semaphore(int permisions)
    {
        _mode = new boolean[permisions];
        for(int i=0;i<permisions;i++) _mode[i] = true;
        lock = true;
    }
    
    private boolean mode(int idx)
    {
        return _mode[idx];
    }
    private void mode(boolean mode, int idx)
    {
        _mode[idx] = mode;
    }
    
    public synchronized final void toRed(int permision)
    {
        while(!mode(permision) || !lock)
        {
            try
            {
                wait();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace(System.err);
            }
        }
        mode(false,permision);
    }
    public synchronized final void toInterrumptedRed(int permision) throws InterruptedException
    {
        while(!mode(permision) || !lock)
        {
            wait();
        }
        mode(false,permision);
    }
    public final void toRed(int... permisions)
    {
        for(int p : permisions)
            toRed(p);
    }
    
    public synchronized final void toGreen(int permision)
    {
        mode(true,permision);
        notify();
    }
    
    public synchronized final void pass(int permision)
    {
        while(!mode(permision) || !lock)
        {
            try
            {
                wait();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace(System.err);
            }
        }
    }
}
