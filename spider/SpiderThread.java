/*

 SQLSentinel v 0.3

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
package sqlsentinel.spider;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlsentinel.core.ProxyManager;
import sqlsentinel.gui.SQLGuiManager;
import sqlsentinel.report.PDFGenerator;
import sqlsentinel.sqlchecker.SQLApexVulnFinder;

public class SpiderThread implements Runnable {

    private SQLGuiManager sqlgui;
    private String szUrl = null;
    private Crawler sqlbot = null;
    private HashSet<String> list = null;
    private PDFGenerator pdfGen = null;
    public boolean isThreadFinished = false;
    public boolean killThread = false;

    public SpiderThread(String szUrl, SQLGuiManager sqlgui, PDFGenerator pdfGen) {
        this.szUrl = szUrl;
        this.sqlgui = sqlgui;
        this.pdfGen = pdfGen;
    }

    @Override
    public void run() {
        if (killThread) {
            return;
        }

        if (ProxyManager.useProxy) {
            sqlgui.addRow("Starting spider using proxy " + ProxyManager.proxyHost + " on port " + ProxyManager.proxyPort, "MainPanel");
        } else {
            sqlgui.addRow("Starting spider..", "MainPanel");
        }

        sqlbot = new Crawler(szUrl, sqlgui);
        sqlbot.doScan(szUrl);

        list = sqlbot.getUrlSearched();
        sqlgui.addRow("Spider task completed, starting sql injection checks", "MainPanel");
        for (String s : list) {
            if (killThread) {
                return;
            }

            Thread sqlApexThread = new Thread(new SQLApexVulnFinder(s, sqlgui, pdfGen));
            sqlApexThread.start();
            try {
                sqlApexThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SpiderThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        isThreadFinished = true;
    }
}
