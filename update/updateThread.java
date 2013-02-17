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

import java.util.logging.Level;
import java.util.logging.Logger;
import sqlsentinel.core.ConfigManager;

public class updateThread implements Runnable {

    private ConfigManager cfg = null;
    private String updateUrl = null;
    private boolean isUpAv = false;
    private UpdateManager upMan = null;
    private String actual_version = null;
    private updateDialog upDialog = null;

    public updateThread(ConfigManager cfg) {
        this.cfg = cfg;
    }

    @Override
    public void run() {
        try {
            if (!cfg.canUpdate()) {
                return;
            }

            //get update url
            updateUrl = cfg.getUpdateUrl();
            //get actual version
            actual_version = cfg.getActualVersion();
            upMan = new UpdateManager(updateUrl, actual_version);
            isUpAv = upMan.isUpdateAvailable();
            if (isUpAv) {
                upDialog = new updateDialog(upMan.newVersionUpdateAvailable);
                upDialog.showUpdateDialogBox();
            }
        } catch (Exception ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
