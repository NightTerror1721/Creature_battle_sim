/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import kp.cbs.utils.Utils;

/**
 *
 * @author Asus
 */
public class ItemsViewer extends JDialog
{
    private final PlayerGame game;
    
    private ItemsViewer(MainGameInterface parent)
    {
        super(parent, true);
        this.game = parent.getPlayerGame();
        initComponents();
        init();
    }
    
    public static final void open(MainGameInterface parent)
    {
        var viewer = new ItemsViewer(parent);
        viewer.setVisible(true);
    }
    
    private void init()
    {
        setResizable(false);
        Utils.focus(this);
        
        var model = new DefaultListModel<ItemId>();
        items.setModel(model);
        model.addAll(List.of(ItemId.values()));
        
        updateMoney();
        showItem();
    }
    
    private void updateMoney()
    {
        money.setText(Integer.toString(game.getMoney()));
    }
    
    private void showItem()
    {
        var sel = items.getSelectedValue();
        if(sel == null)
        {
            amount.setText("");
            amount.setEnabled(false);
            
            price.setText("");
            price.setEnabled(false);
            
            buy.setEnabled(false);
            sale.setEnabled(false);
        }
        else
        {
            var am = game.getItemAmount(sel);
            var pr = sel.getPrice();

            amount.setText(Integer.toString(am));
            amount.setEnabled(true);

            price.setText(Integer.toString(pr));
            price.setEnabled(true);

            buy.setEnabled(game.hasEnoughMoney(pr));
            sale.setEnabled(am > 0);
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        items = new javax.swing.JList<>();
        money = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        amount = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        price = new javax.swing.JTextField();
        buy = new javax.swing.JButton();
        sale = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Objetos y Tienda");

        items.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        items.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                itemsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(items);

        money.setEditable(false);
        money.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        money.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dinero", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        amount.setEditable(false);
        amount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        amount.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cantidad Actual", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Transacciones"));

        price.setEditable(false);
        price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        price.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Precio", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        buy.setText("Comprar");
        buy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyActionPerformed(evt);
            }
        });

        sale.setText("Vender");
        sale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price)
                    .addComponent(buy, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(sale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sale)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(amount)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(money)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_itemsValueChanged
        showItem();
    }//GEN-LAST:event_itemsValueChanged

    private void buyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyActionPerformed
        var sel = items.getSelectedValue();
        if(sel == null)
            return;
        
        if(!game.hasEnoughMoney(sel.getPrice()))
            return;
        
        game.useMoney(sel.getPrice());
        game.addItemAmount(sel, 1);
        updateMoney();
        showItem();
    }//GEN-LAST:event_buyActionPerformed

    private void saleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleActionPerformed
        var sel = items.getSelectedValue();
        if(sel == null)
            return;
        
        if(game.getItemAmount(sel) < 1)
            return;
        
        game.addMoney(sel.getPrice() / 2);
        game.removeItemAmount(sel, 1);
        updateMoney();
        showItem();
    }//GEN-LAST:event_saleActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amount;
    private javax.swing.JButton buy;
    private javax.swing.JList<ItemId> items;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField money;
    private javax.swing.JTextField price;
    private javax.swing.JButton sale;
    // End of variables declaration//GEN-END:variables
}