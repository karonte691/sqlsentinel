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

import sqlsentinel.sqlchecker.BlindTest;

public class SQLPostgresqlBlindFinder implements SQLCommonCheck {

    private final String blindCodeTrueFalseInj = "+AND+333=332--";
    private final String blindCodeTrueTrueInj = "+AND+333=333--";
    private String url_vuln = null;
    private String url_sql_inj = new String();
    private BlindTest blindT = null;

    public SQLPostgresqlBlindFinder(String url_vuln) {
        this.url_vuln = url_vuln;
    }

    @Override
    public boolean checkVuln() {
        try {
            blindT = new BlindTest(url_vuln, blindCodeTrueFalseInj, blindCodeTrueTrueInj);
            if (blindT.checkBlindInj()) {
                url_sql_inj = blindT.getSQLBlindInjUrl();
                return true;
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(SQLMySQLBlindFinder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public String getSQLInjUrl() {
        return this.url_sql_inj;
    }
}
