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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlsentinel.core.ProxyManager;
import sqlsentinel.core.SQLSentinelUtils;
import sqlsentinel.gui.SQLGuiManager;
import sqlsentinel.report.PDFGenerator;


public class SQLApexVulnFinder implements Runnable {

    private String sUrl = null;
    private ArrayList<String> urlParams = null;
    private String urlApexVuln = null;
    private String downloadedPage = null;
    private String db_type = null;
    private SQLGuiManager sqlgui;
    private PDFGenerator pdfGen;
    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private SQLCommonErrors sqlErrorFind = null;
    
    //attack test mysql
    private SQLMySQLBlindFinder mysqlblind = null;
    private SQLMySQLErrorBasedFinder mysqlerrorb = null;
    private SQLMySQLUnionFinder mysqlunion = null; 
    
    //attack test PostgreSQL
    private SQLPostgresqlBlindFinder pgBlindTest = null;
    private SQLPostgresqlErrorBasedFinder pgErrorBasedTest = null;
    private SQLPostgresqlUnionFinder pgUnionTest = null;
    

    public SQLApexVulnFinder(String url, SQLGuiManager sqlgui, PDFGenerator pdfGen) {
        this.sUrl = url;
        this.sqlgui = sqlgui;
        this.pdfGen = pdfGen;
    }

    @Override
    public void run() {
        urlParams = parseGetParametersList(sUrl);
        if (urlParams != null) {
            try {
                for (int i = 0; i < urlParams.size(); i++) {

                    urlApexVuln = buildApexVulnAt(sUrl, urlParams, i);

                    //now in vulnQ we have a test url and we can send it to
                    //the remote target site
                    downloadedPage = getDownloadPage(urlApexVuln);
                    if (downloadedPage != null) {
                        if (findSQLError(downloadedPage)) {  
                            //ok we have found a sql error
                            db_type = sqlErrorFind.db_type;
                            
                            sqlgui.addRow("\n[FOUND] " + urlApexVuln + " --> is vulnerable [Database: " + db_type + "]", "MainPanel");
                            
                            if(db_type.compareToIgnoreCase("MYSQL") == 0){
                                /*
                                 * Check mysql injection
                                 */

                                //blind
                                mysqlblind = new SQLMySQLBlindFinder(urlApexVuln);
                                if(mysqlblind.checkVuln())
                                    sqlgui.addRow(" [*]MySQL blind sql injection: " + mysqlblind.getSQLInjUrl().toString(), "MainPanel");

                                //error based
                                mysqlerrorb = new SQLMySQLErrorBasedFinder(urlApexVuln);
                                if(mysqlerrorb.checkVuln())
                                    sqlgui.addRow(" [*]MySQL error based sql injection: " + mysqlerrorb.getSQLInjUrl().toString(), "MainPanel");

                                //union
                                mysqlunion = new SQLMySQLUnionFinder(urlApexVuln);
                                if(mysqlunion.checkVuln())
                                    sqlgui.addRow(" [*]MySQL union sql injection: " + mysqlunion.getSQLInjUrl().toString(), "MainPanel");

                                mysqlblind = null;
                                mysqlerrorb = null;
                                mysqlunion = null;
                            }
                            else if(db_type.compareToIgnoreCase("ORACLE") == 0)
                            {
                                //oracle check
                            }
                            else if(db_type.compareToIgnoreCase("MSSQL") == 0)
                            {
                                //mssql check
                            }
                            else if(db_type.compareToIgnoreCase("POSTGRESQL") == 0)
                            {
                                /*
                                 * Check postgresql injection
                                 */
                                
                                //blind
                                pgBlindTest = new SQLPostgresqlBlindFinder(urlApexVuln);
                                if(pgBlindTest.checkVuln())
                                    sqlgui.addRow(" [*]PostgreSQL blind sql injection: " + pgBlindTest.getSQLInjUrl().toString(), "MainPanel");
                                
                                //error based
                                pgErrorBasedTest = new SQLPostgresqlErrorBasedFinder(urlApexVuln);
                                if(pgErrorBasedTest.checkVuln())
                                    sqlgui.addRow(" [*]PostgreSQL error based sql injection: " + pgErrorBasedTest.getSQLInjUrl().toString(), "MainPanel");
                                
                                //union
                                pgUnionTest = new SQLPostgresqlUnionFinder(urlApexVuln);
                                if(pgUnionTest.checkVuln())
                                    sqlgui.addRow(" [*]PostgreSQL union sql injection: " + pgUnionTest.getSQLInjUrl().toString(), "MainPanel");
                                
                                pgBlindTest = null;
                                pgErrorBasedTest = null;
                                pgUnionTest = null;
                            }
                            else if(db_type.compareToIgnoreCase("OTHER") == 0) 
                            {
                                //other dbs checks
                            }
                            //and add to report
                            pdfGen.addUrlVuln(urlApexVuln, "Found SQL error");
                        }
                    } else {
                        //maybe it's blind
                        String blind = testBlind(urlApexVuln);
                        
                        if (blind != null && blind.length() > 0) {
                            sqlgui.addRow("\n[FOUND] " + urlApexVuln + " --> is blind vulnerable", "MainPanel");
                            sqlgui.addRow(" [*] " + blind, "MainPanel");
                            pdfGen.addUrlVuln(urlApexVuln, "Blind sql error");
                        }

                    }
                }
            } catch (IOException e) {
                Logger.getLogger(SQLApexVulnFinder.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    /*
     *  this method test if the blind url doesn't work because there is a ' in the get parameter
     *  @return boolean blind url, null if is not inj
     * 
     */

    private String testBlind(String url_vuln) throws IOException {
       try
       {    
            BlindTest t = new BlindTest(url_vuln, "+and+1=2--+", "+and+1=1--+");
            if(t.checkBlindInj())
                return t.getSQLBlindInjUrl();
            return null;
       }catch(Exception e){
           return null;
       }
       
    }
    /*
     *  This fuction check if there is a sql error string in the html code. The sqlerror.
     *  SQL_ERROR array contains a list of the most common sql error
     * 
     *  @param html String the html page buffer
     * 
     *  @return bool true if an error is present, false if not
     *  
     */

    private boolean findSQLError(String htmlPage) {
        sqlErrorFind = new SQLCommonErrors(htmlPage);
        
        return sqlErrorFind.searchSqlError();
    }

    /*
     *  This function downloads the web page and returns the content
     * 
     *  @param page
     * 
     *  @return string contains the html of the page, null if the url is offline
     */
    private String getDownloadPage(String page) throws IOException {
        String line = null;
        String content = null;
        URL htmlpage = null;

        if(ProxyManager.useProxy){
            System.setProperty("http.proxyHost", ProxyManager.proxyHost);
            System.setProperty("http.proxyPort", ProxyManager.proxyPort);
        }
        
        try {
            htmlpage = new URL(page);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SQLApexVulnFinder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        BufferedReader dis = new BufferedReader(new InputStreamReader(htmlpage.openStream()));
        while ((line = dis.readLine()) != null) {
            content += line;
        }
        dis.close();
        return content;
    }

    /*
     *  This function parse all params of the url and return a HasMap that cointains them
     * 
     *  @return HashMap that contains all the get parameters
     */
    private ArrayList<String> parseGetParametersList(String url) {
        ArrayList<String> parmsMap = new ArrayList<String>();
        int i = url.indexOf("?");
        if (i > -1) {
            String searchURL = url.substring(url.indexOf("?") + 1);
            String params[] = searchURL.split("&");

            for (String param : params) {
                try {
                    String temp[] = param.split("=");
                    parmsMap.add(temp[0] + "=" + java.net.URLDecoder.decode(temp[1], "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SQLApexVulnFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return parmsMap;
        }
        return parmsMap;
    }
    /*
     *  This function divides the parameters of the URL, add a ' at the position
     *  defined by pos, and reassembles them.
     *  Note: The location of the apex is defined by the loop that performs the operation in SQLFinder.java
     * 
     *  @param pos Int the position of the '
     * 
     *  @return AbsoluteUrl String vuln url (http://site.com/hi.php?id=1')
     */

    private String buildApexVulnAt(String url, ArrayList<String> params, int pos) {
        String newQuery = "";
        //return a simple top-level url without the get parameters
        String finalUrl = url.substring(0, url.indexOf("?"));

        int arraySize = params.size();
        for (int i = 0; i < arraySize; i++) {
            newQuery += params.get(i);
            //the parameters that will be checked
            if (i == pos) {
                newQuery += "'";
            }
            if (i < arraySize - 1) {
                newQuery += "&";
            }
        }
        //and add the parameters
        finalUrl += "?";
        finalUrl += newQuery;

        return finalUrl;
    }
}
