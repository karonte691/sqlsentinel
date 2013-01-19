/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlsentinel.sqlchecker;

import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLMicrosoftSQLBlindFinder implements SQLCommonCheck {
    private final String blindCodeTrueFalseInj = "+and+9775=9774--+";
    private final String blindCodeTrueTrueInj = "+and+9775=9775--+";
    private String url_vuln = null;
    private String url_sql_inj = new String();
    private BlindTest blindT = null;
    

    public SQLMicrosoftSQLBlindFinder(String url_vuln){
        this.url_vuln = url_vuln;
    }
    
    /*
     * test vuln method
    */
    @Override
    public boolean checkVuln() {
            try
            {
                blindT = new BlindTest(url_vuln, blindCodeTrueFalseInj, blindCodeTrueTrueInj);
                if(blindT.checkBlindInj()){
                    url_sql_inj = blindT.getSQLBlindInjUrl();
                    return true;
                }
                return false;
            } catch (Exception ex) {
                Logger.getLogger(SQLMicrosoftSQLBlindFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return false;
    }

    /*
     * return the url complete with sql injection
     */
    @Override
    public String getSQLInjUrl() {
        return this.url_sql_inj;
    }
}
