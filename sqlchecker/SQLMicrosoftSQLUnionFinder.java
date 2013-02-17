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

import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLMicrosoftSQLUnionFinder implements SQLCommonCheck {
    private final String numberColumnsMessage = "All queries combined using a UNION, INTERSECT or EXCEPT operator must have an equal number of expressions in their target lists";
    private final String unionCodeInj = "+union+select+";
    private String url_vuln = null;
    private UnionNullTester unionTest = null;
    private String union_url_vuln;
    
    public SQLMicrosoftSQLUnionFinder(String url_vuln){
        this.url_vuln = url_vuln;
    }
    
     /*
     * test vuln method
     */
    @Override
    public boolean checkVuln() {
        try
        {
            unionTest = new UnionNullTester(url_vuln, unionCodeInj, numberColumnsMessage);
            if(unionTest.checkNullUnionInj()){
                union_url_vuln = unionTest.getNullUnionSQLInjUrl();
                return true;
            }
            return false;
        } catch (Exception ex) {
             Logger.getLogger(SQLMicrosoftSQLUnionFinder.class.getName()).log(Level.SEVERE, null, ex);
        }       
        return false;
    }

    /*
     * return the url complete with sql injection
     */
    @Override
    public String getSQLInjUrl() {
        return this.union_url_vuln;
    }
    
}
