package sqlsentinel.gui;

/*

 SQLSentinel v 0.4

 Copyright (C) 2012-2013  Luca Magistrelli <blackstorm010[at]gmail[dot]com>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JOptionPane;
import javax.swing.text.Document;

import sqlsentinel.core.SQLSentinelUtils;
import sqlsentinel.report.PDFGenerator;
import sqlsentinel.spider.SpiderThread;
import sqlsentinel.core.userAgent;
import sqlsentinel.core.ProxyManager;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import sqlsentinel.core.cookieManager;

/**
 *
 * @author s3ntinel
 */
public class SQLMainGui extends javax.swing.JFrame implements ComponentListener, ItemListener {

    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private SpiderThread sqlspider;
    private String url = null;
    private Thread t;
    private SQLGuiManager sqlgui;
    private PDFGenerator pdfGen;

    /**
     * Creates new form SQLMainGui
     */
    public SQLMainGui() {
        initComponents();

        //disclaimer issue
        String Disclaimer = " Legal Disclaimer\n\n"
                + " usage of SQLSentinel for attacking web servers without prior mutual "
                + "consent\n can be considered as an illegal activity. it is the final user's "
                + "responsibility\n to obey all applicable local, state and federal laws. author"
                + "assume no liability\n and are not responsible for any misuse or damage caused by this program.\n";
        MainPanelTXT.append(Disclaimer);
        PDFGenButton.setEnabled(false);
        StopThreadButton.setEnabled(false);


    }

    public void showGui() {
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        szInputUrl = new javax.swing.JTextField();
        StartThreadButton = new javax.swing.JButton();
        StopThreadButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        MainPanelTXT = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        SpiderPanelTXT = new javax.swing.JTextArea();
        PDFGenButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        proxyHostnameInput = new javax.swing.JTextField();
        proxyHostnameText = new javax.swing.JLabel();
        proxyPortText = new javax.swing.JLabel();
        proxyPortInput = new javax.swing.JTextField();
        proxyTor = new javax.swing.JCheckBox();
        userAgentCheckBox = new javax.swing.JCheckBox();
        proxyCheckButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cookieLabel = new javax.swing.JLabel();
        cookieText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SQLSentinel ");
        setResizable(false);

        jLabel1.setText("Url:");

        StartThreadButton.setText("Start");
        StartThreadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AsyncWorker.post(new AsyncTask() {
                    @Override
                    public Object run() throws Exception {
                        StartThreadButtonActionPerformed(null);
                        return "Slept!";
                    }

                    @Override
                    public void success(Object result) {
                    }

                    @Override
                    public void failure(Throwable x) {
                    }
                });
            }
        });

        StopThreadButton.setText("Stop");
        StopThreadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AsyncWorker.post(new AsyncTask() {
                    @Override
                    public Object run() throws Exception {
                        StopThreadButtonActionPerformed(null);
                        return "Slept!";
                    }

                    @Override
                    public void success(Object result) {
                    }

                    @Override
                    public void failure(Throwable x) {
                    }
                });
            }
        });

        MainPanelTXT.setColumns(20);
        MainPanelTXT.setRows(5);
        MainPanelTXT.setMaximumSize(new java.awt.Dimension(100, 100));
        jScrollPane1.setViewportView(MainPanelTXT);

        jTabbedPane1.addTab("Working Logs", jScrollPane1);

        SpiderPanelTXT.setColumns(20);
        SpiderPanelTXT.setRows(5);
        jScrollPane2.setViewportView(SpiderPanelTXT);

        cookieLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cookieLabel.setText("Cookie(cookie1=value1;cookie2=value2)");

        jTabbedPane1.addTab("Spider logs", jScrollPane2);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Proxy");

        proxyHostnameText.setText("Hostname:");

        proxyPortText.setText("Port:");

        proxyTor.setText("Use Tor?");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Various");

        userAgentCheckBox.setText("Use random user-agents");

        userAgentCheckBox.addItemListener(this);
        proxyTor.addItemListener(this);

        
        
        proxyCheckButton.setText("Check Proxy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cookieText)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cookieLabel)
                .addComponent(proxyTor)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(proxyHostnameText)
                .addComponent(proxyPortText))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(proxyHostnameInput)
                .addComponent(proxyPortInput, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))))
                .addGap(44, 44, 44)
                .addComponent(userAgentCheckBox)
                .addGap(0, 11, Short.MAX_VALUE)))
                .addContainerGap())
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(280, 280, 280)))));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cookieLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cookieText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(proxyHostnameText)
                .addComponent(proxyHostnameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(userAgentCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(proxyPortText)
                .addComponent(proxyPortInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(proxyTor)
                .addGap(24, 24, 24)));

        jTabbedPane1.addTab("Settings", jPanel1);

        PDFGenButton.setText("Generate PDF report");
        PDFGenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PDFGenButtonActionPerformed(evt);
            }
        });

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        jMenuItem1.setText("Disclaimer");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem1);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(szInputUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StartThreadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(StopThreadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PDFGenButton)
                .addContainerGap()))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(szInputUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(StartThreadButton)
                .addComponent(StopThreadButton))
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PDFGenButton)
                .addContainerGap(17, Short.MAX_VALUE)));



        pack();
    }// </editor-fold>

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void StartThreadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (szInputUrl.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Error: you must insert an url!", "Error", WIDTH);
        } else {
            try {
                //clean the main text area
                Document doc = MainPanelTXT.getDocument();
                try {
                    doc.remove(0, doc.getLength());
                } catch (BadLocationException ex) {
                    Logger.getLogger(SQLMainGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                //and clean the spider text area
                Document doc2 = SpiderPanelTXT.getDocument();
                try {
                    doc2.remove(0, doc2.getLength());
                } catch (BadLocationException ex) {
                    Logger.getLogger(SQLMainGui.class.getName()).log(Level.SEVERE, null, ex);
                }


                //proxy stuff
                if ((proxyHostnameInput.getText().length() > 0 && proxyPortInput.getText().length() > 0) || proxyTor.isSelected()) {
                    try {
                        boolean proxy = new ProxyManager().setProxy(proxyHostnameInput.getText(), proxyPortInput.getText(), proxyTor.isSelected());
                        if (!proxy) {
                            JOptionPane.showMessageDialog(this, "Could not connect to proxy! Session will be aborted", "Proxy Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //cookie stuff
                if(cookieText.getText().length() > 0){
                    cookieManager.useCookie = true;
                    new cookieManager().loadCookie(cookieText.getText());
                }
                
                //ok start the job
                url = sqlutils.cleanStr(szInputUrl.getText());


                //init panel stuff
                sqlgui = new SQLGuiManager(MainPanelTXT, SpiderPanelTXT);
                sqlgui.Init();
                //init pdf generator "daemon"
                pdfGen = new PDFGenerator(url);

                sqlspider = new SpiderThread(url, sqlgui, pdfGen);
                t = new Thread(sqlspider);
                t.start();
                //block start button & report pdf button
                StartThreadButton.setEnabled(false);
                //and enable stop button
                StopThreadButton.setEnabled(true);
                try {
                    t.join();
                    JOptionPane.showMessageDialog(rootPane, "Job finished!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SQLMainGui.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(rootPane, "Job interrupted!", "Warning", JOptionPane.WARNING_MESSAGE);
                } finally {
                    StartThreadButton.setEnabled(true);
                    StopThreadButton.setEnabled(false);
                    PDFGenButton.setEnabled(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (userAgentCheckBox.isSelected()) {
            userAgent.useRandomUserAgent = true;
        } else {
            userAgent.useRandomUserAgent = false;
        }

        if (proxyTor.isSelected()) {
            proxyHostnameInput.setEditable(false);
            proxyPortInput.setEditable(false);
        } else {
            proxyHostnameInput.setEditable(true);
            proxyPortInput.setEditable(true);
        }

    }

    private void StopThreadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            sqlspider.killThread = true;
            t.interrupt();
            StartThreadButton.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PDFGenButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (pdfGen.makePdf()) {
            String pdfpath = pdfGen.GetPDFPath();
            JOptionPane.showMessageDialog(rootPane, "PDF Report saved on " + pdfpath);
        }
    }

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

        String aboutMsg = "SQLSentinel v 0.3\n\n"
                + "Author: Luca Magistrelli <blackstorm010[at]gmail[dot]com>, Michele Mildoni\n\n"
                + "Distribuited under GPL V2 software license.\n\n"
                + "https://sourceforge.net/projects/sqlsentinel/";
        JOptionPane.showMessageDialog(rootPane, aboutMsg, "about", 1);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        String legalDisclaimer = "Legal Disclaimer\n\n"
                + " usage of SQLSentinel for attacking web servers without prior mutual "
                + "consent\n can be considered as an illegal activity. it is the final user's "
                + "responsibility\n to obey all applicable local, state and federal laws. author"
                + "assume no liability\n and are not responsible for any misuse or damage caused by this program.";
        JOptionPane.showMessageDialog(rootPane, legalDisclaimer, "Disclaimer", 1);
    }
    // Variables declaration - do not modify
    private javax.swing.JTextArea MainPanelTXT;
    private javax.swing.JButton PDFGenButton;
    private javax.swing.JTextArea SpiderPanelTXT;
    private javax.swing.JButton StartThreadButton;
    private javax.swing.JButton StopThreadButton;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextField szInputUrl;
    private javax.swing.JTextField proxyHostnameInput;
    private javax.swing.JLabel proxyHostnameText;
    private javax.swing.JTextField proxyPortInput;
    private javax.swing.JLabel proxyPortText;
    private javax.swing.JCheckBox proxyTor;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JCheckBox userAgentCheckBox;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton proxyCheckButton;
    private javax.swing.JLabel cookieLabel;
    private javax.swing.JTextField cookieText;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration

    @Override
    public void componentResized(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void componentShown(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
