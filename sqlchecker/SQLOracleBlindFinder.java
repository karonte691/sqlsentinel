/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlsentinel.sqlchecker;

import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLOracleBlindFinder implements SQLCommonCheck {
    private final String blindCodeTrueFalseInj = "+and+true=false--+";
    private final String blindCodeTrueTrueInj = "+and+true=true--+";
    private String url_vuln = null;
    private String url_sql_inj = new String();
    private BlindTest blindT = null;
    

    public SQLOracleBlindFinder(String url_vuln){
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
                Logger.getLogger(SQLOracleBlindFinder.class.getName()).log(Level.SEVERE, null, ex);
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
