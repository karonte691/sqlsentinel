package sqlsentinel.sqlchecker;

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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlsentinel.core.SQLSentinelUtils;


public class ErrorBasedTest {
    private String url_vuln = null, url_before = null, url_after = null, url_vuln_errorBased = null;
    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private String ErrorBasedCodeInj = null;
    private String[] errorCode;
    
    public ErrorBasedTest(String url_vuln, String ErrorBasedCodeInj, String[] errorCode){
        this.url_vuln = url_vuln;
        this.ErrorBasedCodeInj = ErrorBasedCodeInj;
        this.errorCode = errorCode;
    }
    
    public boolean checkErrorBasedInj(){
        int i = url_vuln.indexOf("'");
        if (i > -1) {
            url_before = url_vuln.substring(0, i);
            url_after = url_vuln.substring(i + 1, url_vuln.length());
            url_vuln_errorBased = url_before + ErrorBasedCodeInj + url_after;

            try {
                String page = sqlutils.getDownloadPage(url_vuln_errorBased);
                if (page == null) {
                    return false;
                } else {
                    for (int j = 0; j < errorCode.length; j++) {
                        if (page.indexOf(errorCode[j]) > -1) {
                            return true;
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SQLMySQLErrorBasedFinder.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
    
    public String getErrorBasedSqlInj(){
         return this.url_vuln_errorBased;
    }
}
