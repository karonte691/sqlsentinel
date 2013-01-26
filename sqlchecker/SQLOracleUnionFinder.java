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

import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLOracleUnionFinder implements SQLCommonCheck {
    private String url_vuln = null;
    private String union_url_vuln;
    private final String numberColumnsMessage = "ORA-01789: query block has incorrect number of result columns";
    private final String unionCodeInj = "+UNION+SELECT+";
    private UnionNullTester unionNullTest = null;
    
    public SQLOracleUnionFinder(String url_vuln){
        this.url_vuln = url_vuln;
    }

    @Override
    public boolean checkVuln() {
        try
        {
            unionNullTest = new UnionNullTester(url_vuln, unionCodeInj, numberColumnsMessage);
            if(unionNullTest.checkNullUnionInj()){
                union_url_vuln = unionNullTest.getNullUnionSQLInjUrl();
                return true;
            }
            return false;
        } catch (Exception ex) {
             Logger.getLogger(SQLOracleUnionFinder.class.getName()).log(Level.SEVERE, null, ex);
        }       
        return false;
    }

    @Override
    public String getSQLInjUrl() {
        return this.union_url_vuln;
    }
    
}
