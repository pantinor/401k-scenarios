/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.finance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.commons.io.IOUtils;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

/**
 *
 * @author pantinori
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scenarioPanel = new javax.swing.JPanel();
        addPortfolioBtn = new javax.swing.JButton();
        deletePortfolioBtn = new javax.swing.JButton();
        selectedStocksScrollPane = new javax.swing.JScrollPane();
        selectedStocksTable = new javax.swing.JTable();
        startDateField = new javax.swing.JTextField();
        startDateLabel = new javax.swing.JLabel();
        addStockBtn = new javax.swing.JButton();
        deleteStockBtn = new javax.swing.JButton();
        portfolioScrolPane = new javax.swing.JScrollPane();
        portfolioTable = new javax.swing.JTable();
        loadConfigBtn = new javax.swing.JButton();
        saveConfigBtn = new javax.swing.JButton();
        calculateBtn = new javax.swing.JButton();
        initBalFld = new javax.swing.JTextField();
        initBalLbl = new javax.swing.JLabel();
        percentIndFld = new javax.swing.JTextField();
        percentSumLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        scenarioPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Portfolio Scenario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        scenarioPanel.setPreferredSize(new java.awt.Dimension(400, 400));

        addPortfolioBtn.setText("Add Portfolio");
        addPortfolioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPortfolioBtnActionPerformed(evt);
            }
        });

        deletePortfolioBtn.setText("Delete Portfolio");
        deletePortfolioBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePortfolioBtnActionPerformed(evt);
            }
        });

        selectedStocksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Ticker", "Percentage of Investment", "Name", "Current Share Price", "Price at Initial Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        selectedStocksTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                selectedStocksTablePropertyChange(evt);
            }
        });
        selectedStocksScrollPane.setViewportView(selectedStocksTable);

        startDateLabel.setText("Inital Date");

        addStockBtn.setText("Add Stock");
        addStockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockBtnActionPerformed(evt);
            }
        });

        deleteStockBtn.setText("Delete Stock");
        deleteStockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStockBtnActionPerformed(evt);
            }
        });

        portfolioTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Portfolio Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        portfolioTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                portfolioTableMouseClicked(evt);
            }
        });
        portfolioScrolPane.setViewportView(portfolioTable);

        loadConfigBtn.setText("Load");
        loadConfigBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadConfigBtnActionPerformed(evt);
            }
        });

        saveConfigBtn.setText("Save");
        saveConfigBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveConfigBtnActionPerformed(evt);
            }
        });

        calculateBtn.setText("Calculate");
        calculateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateBtnActionPerformed(evt);
            }
        });

        initBalLbl.setText("Initial Balance");

        percentIndFld.setEnabled(false);

        percentSumLbl.setText("Percent Sum");

        javax.swing.GroupLayout scenarioPanelLayout = new javax.swing.GroupLayout(scenarioPanel);
        scenarioPanel.setLayout(scenarioPanelLayout);
        scenarioPanelLayout.setHorizontalGroup(
            scenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scenarioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(portfolioScrolPane)
                    .addComponent(selectedStocksScrollPane)
                    .addGroup(scenarioPanelLayout.createSequentialGroup()
                        .addComponent(addStockBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteStockBtn)
                        .addGap(97, 97, 97)
                        .addComponent(percentSumLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(percentIndFld, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveConfigBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadConfigBtn))
                    .addGroup(scenarioPanelLayout.createSequentialGroup()
                        .addComponent(addPortfolioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deletePortfolioBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calculateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                        .addComponent(initBalLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initBalFld, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        scenarioPanelLayout.setVerticalGroup(
            scenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scenarioPanelLayout.createSequentialGroup()
                .addComponent(portfolioScrolPane, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(scenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addPortfolioBtn)
                    .addComponent(deletePortfolioBtn)
                    .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDateLabel)
                    .addComponent(calculateBtn)
                    .addComponent(initBalFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initBalLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectedStocksScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(scenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStockBtn)
                    .addComponent(deleteStockBtn)
                    .addComponent(loadConfigBtn)
                    .addComponent(saveConfigBtn)
                    .addComponent(percentIndFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(percentSumLbl))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scenarioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scenarioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                .addContainerGap())
        );

        scenarioPanel.getAccessibleContext().setAccessibleName("Portfolio Scenarios");
        scenarioPanel.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPortfolioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPortfolioBtnActionPerformed

        DefaultTableModel model = (DefaultTableModel) portfolioTable.getModel();
        int row = portfolioTable.getSelectedRow() == -1 ? 0 : portfolioTable.getSelectedRow();

        DefaultTableModel smodel = newStockTableModel();

        this.stockTableModels.add(row, smodel);
        this.selectedStocksTable.setModel(smodel);

        model.insertRow(row, new Object[]{"MyPortfolio-" + this.stockTableModels.size(), (double) 0.0});

    }//GEN-LAST:event_addPortfolioBtnActionPerformed

    private void deletePortfolioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePortfolioBtnActionPerformed
        DefaultTableModel model = (DefaultTableModel) portfolioTable.getModel();
        int row = portfolioTable.getSelectedRow() == -1 ? 0 : portfolioTable.getSelectedRow();
        if (row >= 0) {
            model.removeRow(row);
            this.stockTableModels.remove(row);
        }
    }//GEN-LAST:event_deletePortfolioBtnActionPerformed

    private void addStockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockBtnActionPerformed
        DefaultTableModel model = (DefaultTableModel) selectedStocksTable.getModel();
        int row = selectedStocksTable.getSelectedRow() == -1 ? 0 : selectedStocksTable.getSelectedRow();
        model.insertRow(row, new Object[]{"TICK", 0, "", (double) 0.0, (double) 0.0});
    }//GEN-LAST:event_addStockBtnActionPerformed

    private void deleteStockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStockBtnActionPerformed
        DefaultTableModel model = (DefaultTableModel) selectedStocksTable.getModel();
        int row = selectedStocksTable.getSelectedRow() == -1 ? 0 : selectedStocksTable.getSelectedRow();
        if (row >= 0) {
            model.removeRow(row);
        }
    }//GEN-LAST:event_deleteStockBtnActionPerformed

    private void portfolioTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_portfolioTableMouseClicked
        int idx = portfolioTable.rowAtPoint(evt.getPoint());
        if (idx >= 0) {
            DefaultTableModel m = this.stockTableModels.get(idx);
            if (m != this.selectedStocksTable.getModel()) {
                this.selectedStocksTable.setModel(m);
            }
        }
    }//GEN-LAST:event_portfolioTableMouseClicked

    private void loadConfigBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadConfigBtnActionPerformed
        final JFileChooser fc = new JFileChooser("./");
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            try {
                File file = fc.getSelectedFile();
                FileInputStream fis = new FileInputStream(file);
                String json = IOUtils.toString(fis, StandardCharsets.UTF_8);
                fis.close();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                this.config = gson.fromJson(json, Config.class);

                this.startDateField.setText(this.config.initialDate);
                this.initBalFld.setText(this.config.initialBalance + "");

                this.stockTableModels.clear();

                DefaultTableModel portModel = (DefaultTableModel) portfolioTable.getModel();
                while (portModel.getRowCount() > 0) {
                    portModel.removeRow(0);
                }

                for (PortfolioConfig pcfg : this.config.portfolioConfig) {

                    portModel.addRow(new Object[]{pcfg.portfolioName, (double) 0.0});

                    DefaultTableModel smodel = newStockTableModel();

                    for (StockConfig scfg : pcfg.stockConfig) {
                        smodel.addRow(new Object[]{scfg.ticker, scfg.percentage, "", (double) 0.0, (double) 0.0});
                    }

                    this.stockTableModels.add(smodel);

                }

                if (!this.stockTableModels.isEmpty()) {
                    selectedStocksTable.setModel(this.stockTableModels.get(0));
                } else {
                    selectedStocksTable.setModel(new DefaultTableModel());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_loadConfigBtnActionPerformed

    private void saveConfigBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConfigBtnActionPerformed

        final JFileChooser fc = new JFileChooser("./");
        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            try {

                this.config = getConfig();

                File file = fc.getSelectedFile();

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.setPrettyPrinting().create();
                String json = gson.toJson(this.config);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(json.getBytes("UTF-8"));
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }//GEN-LAST:event_saveConfigBtnActionPerformed

    private void calculateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateBtnActionPerformed

        try {
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(SDF.parse(this.startDateField.getText()));

            for (DefaultTableModel model : this.stockTableModels) {
                for (int i = 0; i < model.getRowCount(); i++) {

                    try {
                        String ticker = (String) model.getValueAt(i, 0);

                        //Stock stock = YahooFinance.get(ticker);
                        Stock stock = YahooFinance.get(ticker, initialDate, Calendar.getInstance(), Interval.MONTHLY);

                        BigDecimal price = stock.getQuote().getPrice();
                        BigDecimal fromPrice = stock.getHistory().get(0).getClose();

                        model.setValueAt(stock.getName(), i, 2);
                        model.setValueAt(price.doubleValue(), i, 3);
                        model.setValueAt(fromPrice.doubleValue(), i, 4);

                    } catch (Exception e) {
                        e.printStackTrace();
                        model.setValueAt((double) 0.0, i, 3);
                        model.setValueAt((double) 0.0, i, 4);
                    }
                }

            }

            double initialBalance = Double.parseDouble(this.initBalFld.getText());

            for (int i = 0; i < this.portfolioTable.getRowCount(); i++) {
                double currentBalance = 0.0;

                DefaultTableModel smodel = this.stockTableModels.get(i);
                for (int j = 0; j < smodel.getRowCount(); j++) {
                    int percent = (int) smodel.getValueAt(j, 1);
                    double initialBalanceOfThisSecurity = initialBalance * ((double) percent * 0.01);

                    double initialSharePrice = (double) smodel.getValueAt(j, 4);
                    double sharesBoughtInitially = initialBalanceOfThisSecurity / initialSharePrice;

                    double currentSharePrice = (double) smodel.getValueAt(j, 3);
                    double currentBalanceOfThisSecurity = sharesBoughtInitially * currentSharePrice;

                    currentBalance = currentBalance + currentBalanceOfThisSecurity;
                }

                this.portfolioTable.setValueAt(currentBalance, i, 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_calculateBtnActionPerformed

    private void selectedStocksTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_selectedStocksTablePropertyChange
        int idx = this.selectedStocksTable.getEditingRow();
        if (idx >= 0) {
            DefaultTableModel smodel = (DefaultTableModel) this.selectedStocksTable.getModel();
            int total = 0;
            for (int j = 0; j < smodel.getRowCount(); j++) {
                int percent = (int) smodel.getValueAt(j, 1);
                total += percent;
            }
            this.percentIndFld.setText(total + "");
        }

    }//GEN-LAST:event_selectedStocksTablePropertyChange

    private DefaultTableModel newStockTableModel() {
        DefaultTableModel smodel = new DefaultTableModel(
                null,
                new String[]{"Stock Ticker", "Percentage of Investment", "Name", "Current Share Price", "Price at Initial Date"}
        ) {
            Class[] types = new Class[]{java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            boolean[] canEdit = new boolean[]{true, true, false, false, false};

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        return smodel;
    }

    private Config getConfig() throws Exception {

        Config config = new Config();

        Date initialDate = SDF.parse(this.startDateField.getText());

        config.initialDate = SDF.format(initialDate);
        config.initialBalance = Double.parseDouble(this.initBalFld.getText());

        TableModel portModel = this.portfolioTable.getModel();

        int rows = portModel.getRowCount();
        for (int x = 0; x < rows; x++) {
            PortfolioConfig pcfg = new PortfolioConfig();
            pcfg.portfolioName = portModel.getValueAt(x, 0) == null ? "" : (String) portModel.getValueAt(x, 0);
            config.portfolioConfig.add(pcfg);

            TableModel stockModel = this.stockTableModels.get(x);
            int srows = stockModel.getRowCount();

            for (int i = 0; i < srows; i++) {
                StockConfig scfg = new StockConfig();
                scfg.ticker = stockModel.getValueAt(i, 0) == null ? "" : (String) stockModel.getValueAt(i, 0);
                scfg.percentage = stockModel.getValueAt(i, 1) == null ? 0 : (int) stockModel.getValueAt(i, 1);
                pcfg.stockConfig.add(scfg);
            }
        }

        return config;
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
    private Config config = new Config();
    private final List<DefaultTableModel> stockTableModels = new ArrayList<>();

    private class Config {

        String initialDate;
        double initialBalance;
        List<PortfolioConfig> portfolioConfig = new ArrayList<>();
    }

    private class PortfolioConfig {

        String portfolioName;
        List<StockConfig> stockConfig = new ArrayList<>();
    ;

    }

    private class StockConfig {

        String ticker;
        int percentage;
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        final GUI gui = new GUI();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        String initialDate = SDF.format(c.getTime());
        gui.startDateField.setText(initialDate);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPortfolioBtn;
    private javax.swing.JButton addStockBtn;
    private javax.swing.JButton calculateBtn;
    private javax.swing.JButton deletePortfolioBtn;
    private javax.swing.JButton deleteStockBtn;
    private javax.swing.JTextField initBalFld;
    private javax.swing.JLabel initBalLbl;
    private javax.swing.JButton loadConfigBtn;
    private javax.swing.JTextField percentIndFld;
    private javax.swing.JLabel percentSumLbl;
    private javax.swing.JScrollPane portfolioScrolPane;
    private javax.swing.JTable portfolioTable;
    private javax.swing.JButton saveConfigBtn;
    private javax.swing.JPanel scenarioPanel;
    private javax.swing.JScrollPane selectedStocksScrollPane;
    private javax.swing.JTable selectedStocksTable;
    private javax.swing.JTextField startDateField;
    private javax.swing.JLabel startDateLabel;
    // End of variables declaration//GEN-END:variables
}
