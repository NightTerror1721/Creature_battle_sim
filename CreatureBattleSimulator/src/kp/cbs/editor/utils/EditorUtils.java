/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.editor.utils;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Marc
 */
public interface EditorUtils
{
    static <T extends JTextComponent> T installActionCallback(final T component, final Consumer<T> action)
    {
        component.addFocusListener(new FocusListener()
        {
            @Override public final void focusGained(FocusEvent e) {}
            @Override public final void focusLost(FocusEvent e) { action.accept(component); }
        });
        component.addKeyListener(new KeyListener()
        {
            @Override public final void keyTyped(KeyEvent e) {}
            @Override public final void keyPressed(KeyEvent e) {}
            @Override public final void keyReleased(KeyEvent e) { action.accept(component); }
        });
        return component;
    }
    
    static <T> JComboBox<T> installActionCallback(final JComboBox<T> component, final Consumer<JComboBox<T>> action)
    {
        component.addFocusListener(new FocusListener()
        {
            @Override public final void focusGained(FocusEvent e) {}
            @Override public final void focusLost(FocusEvent e) { action.accept(component); }
        });
        component.addActionListener(a -> action.accept(component));
        return component;
    }
    
    static JCheckBox installActionCallback(final JCheckBox component, final Consumer<JCheckBox> action)
    {
        component.addFocusListener(new FocusListener()
        {
            @Override public final void focusGained(FocusEvent e) {}
            @Override public final void focusLost(FocusEvent e) { action.accept(component); }
        });
        component.addActionListener(a -> action.accept(component));
        return component;
    }
    
    static <T> JList<T> installSelectionCallback(final JList<T> component, final BiConsumer<JList<T>, ListSelectionEvent> action)
    {
        component.addListSelectionListener(event -> action.accept(component, event));
        return component;
    }
    
    
    static <T> boolean containsInListModel(ListModel<T> model, T element)
    {
        final var len = model.getSize();
        for(int i = 0; i < len; ++i)
            if(element.equals(model.getElementAt(i)))
                return true;
        return false;
    }
    
    
    static <T> T selectItem(Component parent, String title, String message, T[] values, T defaultValue)
    {
        if(values == null || values.length < 1)
            return null;
        
        if(values.length == 1)
            return values[0];
        
        if(defaultValue == null)
            defaultValue = values[0];
        
        var sel = JOptionPane.showInputDialog(parent,
                message,
                title,
                JOptionPane.QUESTION_MESSAGE,
                null,
                values,
                defaultValue);
        return sel == null ? null : (T) sel;
    }
    
    
    static <T> Iterator<T> listModelIterator(final ListModel<T> model)
    {
        return new Iterator<>()
        {
            private final int len = model.getSize();
            private int it = 0;
            
            @Override public final boolean hasNext() { return it < len; }
            @Override public final T next() { return model.getElementAt(it++); }
        };
    }
    
    static <T> Stream<T> listModelStream(final ListModel<T> model)
    {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        listModelIterator(model),
                        model.getSize(),
                        Spliterator.SIZED | Spliterator.ORDERED),
                false);
    }
}
