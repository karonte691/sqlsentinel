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
package sqlsentinel.sqlchecker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlsentinel.core.SQLSentinelUtils;


public class BlindTest {
    private String url_vuln = null;
    private String url_before = null;
    private String url_after = null;
    private String url_vuln_BlindFalse = null;
    private String url_vuln_BlindTrue = null;
    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private String blindCodeTrueFalseInj = null;
    private String blindCodeTrueTrueInj = null;
    private int check_true = 0;
    private int check_false = 0;
    
    public BlindTest(String url_vuln, String blindCodeTrueFalseInj, String blindCodeTrueTrueInj){
        this.url_vuln = url_vuln;
        this.blindCodeTrueFalseInj = blindCodeTrueFalseInj;
        this.blindCodeTrueTrueInj = blindCodeTrueTrueInj;
    }
    
    public boolean checkBlindInj(){
        int i = url_vuln.indexOf("'");
        if (i > -1) {
            try {
                url_before = url_vuln.substring(0, i);
                url_after = url_vuln.substring(i + 1, url_vuln.length());
                
                //ok first step
                url_vuln_BlindFalse = url_before + blindCodeTrueFalseInj + url_after;
                
                check_false = sqlutils.getDownloadPage(url_vuln_BlindFalse).length();
                //second step
                url_vuln_BlindTrue = url_before + blindCodeTrueTrueInj + url_after;
                check_true = sqlutils.getDownloadPage(url_vuln_BlindTrue).length();
                if(check_false == check_true || check_false > check_true)
                        return false;
                return true;
            } catch (IOException ex) {
                Logger.getLogger(SQLMySQLBlindFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public String getSQLBlindInjUrl() {
        return this.url_vuln_BlindTrue;
    }
    
    
}
