/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlsentinel.sqlchecker;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s3ntinel
 */
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
