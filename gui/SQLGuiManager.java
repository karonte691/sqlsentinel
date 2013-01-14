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
package sqlsentinel.gui;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.tree.DefaultMutableTreeNode;


public class SQLGuiManager extends JTextPane {

    public JTextArea mainPanelTXT, spiderPanelTXT;

    public SQLGuiManager(JTextArea mainPanelTXT, JTextArea spiderPanelTXT) {
        this.mainPanelTXT = mainPanelTXT;
        this.spiderPanelTXT = spiderPanelTXT;
    }

    /*
     *  add a row to jtextarea
     * 
     *  @param row String the text to add
     *  @param textarea String the name of the jtextarea
     */
    public void addRow(String row, String textarea) {
        if (textarea.length() > 0) {
            if (textarea.equals("MainPanel")) {
                addMainPanelRow(row);
            } else if (textarea.equals("SpiderPanel")) {
                addSpiderPanelRow(row);
            }
        }
    }

    /*
     *  add a row to MainPanelTXT jtextarea
     *  
     *  @param row String the line to add
     */
    private void addMainPanelRow(String row) {
        mainPanelTXT.append(row);
        mainPanelTXT.append("\n");
    }

    /*
     *  add a row to SpiderPanelTXT jtextarea
     *  
     *  @param spider_row String the line to add
     */
    private void addSpiderPanelRow(String spider_row) {
        spiderPanelTXT.append(spider_row);
        spiderPanelTXT.append("\n");

    }

    public void Init() {
        addRow("########################\n", "MainPanel");
        addRow("SQLSentinel V 0.2", "MainPanel");
        addRow("\n########################", "MainPanel");
    }

    public void Finish() {
        addRow("\nJob finished!", "MainPanel");
        addRow("\nJob finished!", "SpiderPanel");

    }

    
    
}
