package sqlsentinel.update;

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

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class updateDialog {
    private String newVersionAv = null;
    private String sqlsentinelsiteAdd = "http://www.sqlsentinel.org";
    private int goUpdate = 0;
    private Desktop desktop = null;
    private URL sqlsentinelSite = null;
    

    public updateDialog(String newVer) {
        this.newVersionAv = newVer;
    }

    /*
     *  Display alert box for updating sqlsentinel
     *  If user choose yes, the function will open system default
     *  web browser to surf on sqlsentinel.org website.
     * 
     */
    public void showUpdateDialogBox() {
        goUpdate = JOptionPane.showConfirmDialog(null,
                "New version of SQLSentinel is available: " + newVersionAv + "\n"
                + "Would you like to download it now?",
                "SQLSentinel update",
                JOptionPane.YES_NO_OPTION);
        if(goUpdate == 0){
            try
            {
                sqlsentinelSite = new URL(this.sqlsentinelsiteAdd);
                this.openSQLSentinelSite(sqlsentinelSite.toURI());
            }catch (Exception e) {
                Logger.getLogger(updateDialog.class.getName()).log(Level.SEVERE, null, e);
            }
        }

    }

    /*
     *  Open the default browser and surf on a website given in input
     * 
     *  @param uri URI uri of website
     */
    public  void openSQLSentinelSite(URI uri) {
        desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                Logger.getLogger(updateDialog.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
