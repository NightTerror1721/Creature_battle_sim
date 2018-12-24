/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.battle;

import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import static kp.cbs.battle.Battle.TeamId.SELF;
import kp.cbs.utils.Semaphore;

/**
 *
 * @author Asus
 */
public class Battle extends JDialog
{
    private final Semaphore sem = new Semaphore(1);
    private final Semaphore round = new Semaphore(1);
    private final Semaphore damC = new Semaphore(1);
    private final Semaphore healC = new Semaphore(1);
    
    public Battle(JFrame parent)
    {
        super(parent, true);
        initComponents();
        init();
    }
    
    private Battle(JDialog parent)
    {
        super(parent, true);
        initComponents();
        init();
    }
    
    private void init()
    {
        
    }
    
    private void modifBarlifePlayer(TeamId team, final int goal, boolean isHeal)
    {
        final JProgressBar bar = team == SELF ? self_lifebar : enemy_lifebar;
        //final JProgressBar bar = team == USER ? user_ps_bar : enemy_ps_bar;
        final int mod = isHeal ? 1 : -1;
        final boolean flag = team == SELF;
        final int goalLive = goal;
        final int lastHealth = bar.getValue();
        //bar.setValue(cr.getFeatures().getLife().getLastHealth());
        
        
        Runnable RunnInitial = () -> {
            //int ratio = pl.getFeatures().getLife().getMaxHeal();
            //ratio = ratio <= 400 ? 4 : (int)( 4f * ((400f / ratio) * 1.5f));
            int ratio = 2;
            float rest = isHeal ?  goalLive - lastHealth :
                    lastHealth - goalLive;
            final float max = rest;
            final float life = bar.getMaximum();
            //float mr = rest / 400f * 2f * 0.1f;
            //mr = mr < 0.15f ? 0.15f : mr > 1f ? 1f : mr;
            float pp;
            boolean slow = rest >= (life * 0.13f);
            final float mid = rest / 2f;
            final float midSpeed = mid / 400f * 3.5f;
            final float slowTime = rest / 10f;
            final float min = rest / 400f * 3.5f * 0.1f;
            final float maxSpeed = life > 2000 ? 2000f / 400f * 3.5f * 0.25f :
                    life / 400f * 3.5f * 0.25f;
            if(ratio < 1) ratio = 1;
            while(mod < 0
                    ? (bar.getValue() > goalLive &&
                    bar.getValue() > 0)
                    : (bar.getValue() < goalLive &&
                    bar.getValue() < bar.getMaximum()))
            {
                if(slow)
                {
                    if(rest >= mid)
                    {
                        float sslow = (max - rest) / slowTime;
                        if(sslow > 1f) sslow = 1f;
                        pp = midSpeed * mod * sslow;
                        //if(Math.abs(pp) > midSpeed)
                        //pp = midSpeed * mod;
                        if(Math.abs(pp) < min)
                            pp = min * mod;
                    }
                    else
                    {
                        pp = rest / 400f * 3.5f * mod;
                    }
                }
                else pp = (life * 0.03f) / 400f * 3.5f * mod;
                //pp = rest / 400f * 3.5f * mod;
                if(Math.abs(pp) < 0.15f) pp = 0.15f * mod;
                if(Math.abs(pp) > maxSpeed) pp = maxSpeed * mod;
                rest += isHeal ? -pp : pp;
                if(isHeal)
                    bar.setValue((int) (goalLive - rest));
                else
                    bar.setValue((int) (goalLive + rest));
                //if(flag) updatePSValue(bar.getValue());
                bar.repaint();
                sleep(ratio);
            }
            sem.toGreen(0);
        };
        Thread tInitial = new Thread(RunnInitial);
        sem.toRed(0);
        tInitial.start();
        if(lastHealth != goalLive)
        {
            if(mod < 0) damageColor(team);
            else healColor(team);
        }
        sem.pass(0);
        damC.pass(0);
        healC.pass(0);
        //colorLife(isSelf);
    }
    
    private void modifExpBar(final int goal)
    {
        final int speed = 20;
        
        sem.toRed(0);
        new Thread(() -> {
            expGet.play();
            while(user_exp.getValue() < goal)
            {
                user_exp.setValue(user_exp.getValue() + 1);
                user_exp.repaint();
                sleep(speed);
            }
            expGet.stop();
            sem.toGreen(0);
        }).start();
        sem.pass(0);
    }
    
    private void insertMessage(String message)
    {
        try
        {
            int offset = jTextPane1.getDocument().getLength();
            jTextPane1.getDocument().insertString(offset,message + "\n",null);
            jTextPane1.setCaretPosition(jTextPane1.getDocument().getLength());
        }
        catch(Exception ex) {}
    }
    
    private void clearText()
    {
        jTextPane1.setText("");
        jTextPane1.setCaretPosition(jTextPane1.getDocument().getLength());
    }
    
    private void damageColor(TeamId team)
    {
        final JProgressBar bar = team == SELF ? self_lifebar : enemy_lifebar;
        final int fr, fg, fb;
        
        Color c = bar.getForeground();
        fr = c.getRed();
        fg = c.getGreen();
        fb = c.getBlue();
        
        Runnable RunnInitial = () -> {
            int times = 0;
            int r, g, b, col;
            while(times < 2)
            {
                times++;
                r = fr;
                g = fg;
                b = fb;
                col = 0;
                while(col < 256)
                {
                    col+=2;
                    sleep(1);
                    r += 2;
                    g -= 2;
                    b -= 2;
                    if(r > 255) r = 255;
                    if(g < 0) g = 0;
                    if(b < 0) b = 0;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r, g, b));
                    }
                }
                col = 256;
                while(col > 0)
                {
                    col-=2;
                    sleep(1);
                    r -= 2;
                    g += 2;
                    b += 2;
                    if(r < fr) r = fr;
                    if(g > fg) g = fg;
                    if(b > fb) b = fb;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r, g, b));
                    }
                }
            }
            damC.toGreen(0);
        };
        Thread th = new Thread(RunnInitial);
        damC.toRed(0);
        th.start();
    }
    
    private void healColor(TeamId team)
    {
        final JProgressBar bar = team == SELF ? self_lifebar : enemy_lifebar;
        final int fr, fg, fb;
        
        Color c = bar.getForeground();
        fr = c.getRed();
        fg = c.getGreen();
        fb = c.getBlue();
        
        Runnable RunnInitial = () -> {
            int times = 0;
            int r, g, b, col;
            while(times < 1)
            {
                times++;
                r = fr;
                g = fg;
                b = fb;
                col = 0;
                while(col < 256)
                {
                    col += 1;
                    sleep(3);
                    r -= 1;
                    g += 1;
                    b += 1;
                    if(r < 0) r = 0;
                    if(g > 255) g = 255;
                    if(b > 255) b = 255;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r,g,b));
                    }
                }
                while(col > 0)
                {
                    col-=1;
                    sleep(3);
                    r += 1;
                    g -= 1;
                    b -= 1;
                    if(r > fr) r = fr;
                    if(g < fg) g = fg;
                    if(b < fb) b = fb;
                    synchronized(bar)
                    {
                        bar.setForeground(new Color(r, g, b));
                    }
                }
            }
            healC.toGreen(0);
        };
        Thread th = new Thread(RunnInitial);
        healC.toRed(0);
        th.start();
    }
    
    private static void sleep(int sleep)
    {
        try
        {
            Thread.sleep(sleep);
        }
        catch(InterruptedException ex)
        {
            
        }
    }
    
    enum TeamId
    {
        SELF, ENEMY;
        private TeamId invert() { return this == SELF ? ENEMY : SELF; }
    }
    public enum ButtonsMenuState { DISABLED, MAIN, ATTACKS }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        self_lifecount = new javax.swing.JLabel();
        self_lifebar = new javax.swing.JProgressBar();
        self_level = new javax.swing.JLabel();
        self_altered = new javax.swing.JLabel();
        self_race = new javax.swing.JLabel();
        self_name = new javax.swing.JLabel();
        self_expbar = new javax.swing.JProgressBar();
        self_powerup = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        enemy_lifebar = new javax.swing.JProgressBar();
        enemy_level = new javax.swing.JLabel();
        enemy_altered = new javax.swing.JLabel();
        enemy_race = new javax.swing.JLabel();
        enemy_name = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel4 = new javax.swing.JPanel();
        but1 = new javax.swing.JButton();
        but2 = new javax.swing.JButton();
        but3 = new javax.swing.JButton();
        but4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        self_lifecount.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        self_lifecount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        self_lifecount.setText("99999/99999");
        self_lifecount.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        self_lifebar.setValue(50);

        self_level.setText("jLabel2");

        self_altered.setText("jLabel3");

        self_race.setText("jLabel4");

        self_name.setText("jLabel5");

        self_expbar.setForeground(new java.awt.Color(51, 255, 255));
        self_expbar.setValue(50);
        self_expbar.setPreferredSize(new java.awt.Dimension(146, 8));

        self_powerup.setText("jLabel6");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(self_lifebar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(self_level)
                            .addComponent(self_name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(self_altered, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(self_race, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(self_expbar, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .addComponent(self_powerup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(self_lifecount, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(self_name)
                    .addComponent(self_race))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(self_level)
                    .addComponent(self_altered))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(self_lifebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(self_expbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(self_powerup)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(self_lifecount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        enemy_lifebar.setValue(50);

        enemy_level.setText("jLabel2");

        enemy_altered.setText("jLabel3");

        enemy_race.setText("jLabel4");

        enemy_name.setText("jLabel5");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enemy_lifebar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(enemy_level)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(enemy_altered))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(enemy_name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(enemy_race)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enemy_name)
                    .addComponent(enemy_race))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enemy_level)
                    .addComponent(enemy_altered))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enemy_lifebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jTextPane1);

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new java.awt.GridLayout(2, 2));

        but1.setText("jButton1");
        jPanel4.add(but1);

        but2.setText("jButton2");
        jPanel4.add(but2);

        but3.setText("jButton3");
        jPanel4.add(but3);

        but4.setText("jButton4");
        jPanel4.add(but4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton but1;
    private javax.swing.JButton but2;
    private javax.swing.JButton but3;
    private javax.swing.JButton but4;
    private javax.swing.JLabel enemy_altered;
    private javax.swing.JLabel enemy_level;
    private javax.swing.JProgressBar enemy_lifebar;
    private javax.swing.JLabel enemy_name;
    private javax.swing.JLabel enemy_race;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel self_altered;
    private javax.swing.JProgressBar self_expbar;
    private javax.swing.JLabel self_level;
    private javax.swing.JProgressBar self_lifebar;
    private javax.swing.JLabel self_lifecount;
    private javax.swing.JLabel self_name;
    private javax.swing.JLabel self_powerup;
    private javax.swing.JLabel self_race;
    // End of variables declaration//GEN-END:variables
}
