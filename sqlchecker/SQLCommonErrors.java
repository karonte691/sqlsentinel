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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * 
 *  This class contains the most common sql error
 * 
 */
public class SQLCommonErrors {
    private final String[] MYSQL_ERRORS = {
        "SQL syntax.*MySQL",
        "valid MySQL result",
        "Warning.*mysql_.*"
    };
    private final String[] ORACLE_ERRORS = {
        "ORA-[0-9][0-9][0-9][0-9]",
        "Oracle error",
        "Oracle.*Driver",
        "Warning.*\\Woci_.*",
        "Warning.*\\Wora_.*"       
    };
    private final String[] POSTGRESQL_ERRORS = {
         "valid PostgreSQL result",
         "Warning.*\\Wpg_.*",
         "PostgreSQL.*ERROR",
         "Npgsql\\.",
         "unterminated quoted string at or near"
    };
    private final String[] MSSQL_ERRORS = {
         "Driver.* SQL[\\-\\_\\ ]*Server",
         "OLE DB.* SQL Server",
         "(\\W|\\A)SQL Server.*Driver",
         "Warning.*mssql_.*",
         "(\\W|\\A)SQL Server.*[0-9a-fA-F]{8}",
         "(?s)Exception.*\\WSystem\\.Data\\.SqlClient\\.",
         "Unclosed quotation mark after the character"
    };
    private final String[] OTHER_DB_ERRORS = {
          "Microsoft Access Driver",
          "SQLite/JDBCDriver",
          "Warning.*ibase_.*",
          "Exception.*Informix",
          "SQL error.*POS([0-9]+).*",
          "Warning.*sybase.*",
          "SQLite.Exception",    
          "JET Database Engine",
          "Access Database Engine"
    };
    private String page_to_check = null;
    private Pattern p = null;
    private Matcher m = null;
    public String db_type = null;
    
    public SQLCommonErrors(String page_to_check){
        this.page_to_check = page_to_check;
    }
    
    public boolean searchSqlError(){
        boolean check = false;
        
        try
        {
            this.db_type = new String();
            
            if(this.checkIfIsMySQL())
            {
                this.db_type = "MySQL";
                check = true;
            }
            else if(this.checkIfIsMSSQL())
            {
                this.db_type = "MSSQL";
                check = true;
            }
            else if(this.checkIfIsOracle())
            {
                this.db_type = "ORACLE";
                check = true;
            }
            else if(this.checkIfIsPostgreSQL())
            {
                this.db_type = "POSTGRESQL";
                check = true;
            }
            else if(this.checkIfIsOtherDB())
            {
                this.db_type = "OTHER";
                check = true;
            }
            else {
                check = false;
            }
            return check;
        } catch (Exception ex) {
             Logger.getLogger(SQLCommonErrors.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        }
    }
    
    private boolean checkIfIsMySQL(){
        boolean mysql_result = false;
        
        try
        {
            for(int i = 0; i < MYSQL_ERRORS.length; i++){
                p = null;
                m = null;
                
                p =  Pattern.compile(MYSQL_ERRORS[i]);
                m = p.matcher(page_to_check);

                if(m.find()) {
                    mysql_result = true;
                }
            }
            return mysql_result;
        } catch (Exception ex) {
             Logger.getLogger(SQLCommonErrors.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return false;
    }
    
    private boolean checkIfIsMSSQL(){
        boolean mssql_result = false;
        
        try
        {
            for(int i = 0; i < MSSQL_ERRORS.length; i++){
                p = null;
                m = null;
                
                p =  Pattern.compile(MSSQL_ERRORS[i]);
                m = p.matcher(page_to_check);

                if(m.find()) {
                    mssql_result = true;
                }
            }
            return mssql_result;
        } catch (Exception ex) {
             Logger.getLogger(SQLCommonErrors.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
    }
    
    private boolean checkIfIsOracle(){
        boolean oracle_result = false;
        
        try
        {
            for(int i = 0; i < ORACLE_ERRORS.length; i++){
                p = null;
                m = null;
                
                p =  Pattern.compile(ORACLE_ERRORS[i]);
                m = p.matcher(page_to_check);

                if(m.find()) {
                    oracle_result = true;
                }
            }
            return oracle_result;
        } catch (Exception ex) {
             Logger.getLogger(SQLCommonErrors.class.getName()).log(Level.SEVERE, null, ex);           
        }  
        return false;
    }
    
    private boolean checkIfIsPostgreSQL(){
        boolean pgsql_result = false;
        
        try
        {
            for(int i = 0; i < POSTGRESQL_ERRORS.length; i++){
                p = null;
                m = null;
                
                p =  Pattern.compile(POSTGRESQL_ERRORS[i]);
                m = p.matcher(page_to_check);

                if(m.find()) {
                    pgsql_result = true;
                }
            }
            return pgsql_result;
        } catch (Exception ex) {
             Logger.getLogger(SQLCommonErrors.class.getName()).log(Level.SEVERE, null, ex); 
        }     
        return false;
    }
    
    private boolean checkIfIsOtherDB(){
        boolean odb_result = false;
        
        try
        {
            for(int i = 0; i < OTHER_DB_ERRORS.length; i++){
                p = null;
                m = null;
            
                p =  Pattern.compile(OTHER_DB_ERRORS[i]);
                m = p.matcher(page_to_check);

                if(m.find()) {
                    odb_result = true;
                }
            }
            return odb_result;
        } catch (Exception ex) {
             Logger.getLogger(SQLCommonErrors.class.getName()).log(Level.SEVERE, null, ex);           
        }  
        return false;
    }
    

    
    
}
