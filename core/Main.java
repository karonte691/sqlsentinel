package sqlsentinel.core;

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


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import sqlsentinel.gui.SQLMainGui;
import sqlsentinel.core.ConfigManager;
import sqlsentinel.update.updateThread;

public class Main extends javax.swing.JFrame {

    private static SQLSentinelUtils sqlutils;
    private static ConfigManager cfg;
    private static updateThread upThread = null;

    public static void main(String[] args) {
        try {
            cfg = new ConfigManager();
            if(!cfg.loadConfigFile())
                return;
            
            upThread = new updateThread(cfg);
            
            Thread upT = new Thread(upThread);
            upT.start();
            
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
            
            sqlutils = new SQLSentinelUtils();

            //check if report folders exists. If not, we must create it
            if (!sqlutils.folderExist("Report")) {
                sqlutils.makeReportFolder();
            }

            //invoke the main gui
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new SQLMainGui().showGui();
                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
