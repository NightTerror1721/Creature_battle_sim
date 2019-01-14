/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kp.cbs.editor.MainMenuEditor;
import kp.cbs.utils.SoundManager;

/**
 *
 * @author Asus
 */
public final class Main
{
    public static void main(String[] args) throws IOException
    {
        if(args.length > 0 && args[0].equals("editors"))
            executeEditor();
        else executeGame();
    }
    
    private static void executeEditor()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace(System.err);
        }
        
        EventQueue.invokeLater(MainMenuEditor::start);
    }
    
    private static void executeGame()
    {
        SoundManager.loadSounds(true);
        
        try
        {
            for(var info : UIManager.getInstalledLookAndFeels())
            {
                if("CDE/Motif".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace(System.err);
        }
        
        MainGameInterface.start();
    }
}
