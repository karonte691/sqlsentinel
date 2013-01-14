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
package sqlsentinel.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sqlsentinel.sqlchecker.SQLApexVulnFinder;

public class SQLSentinelUtils {

    private String currentDir = System.getProperty("user.dir");
    private String rFolder = currentDir + osPath() + "Report" + osPath();

    /*
     * Remove http && www from domain string, thanks to Roberto D'Auria for the regex
     * 
     * @param sz String the string to clean
     * 
     * @return result String the string cleaned from http://www.
     */
    public static String cleanStr(String sz) {
        Pattern pattern1 = Pattern.compile("http:\\/\\/");
        Pattern pattern2 = Pattern.compile("www\\.");
        Matcher Mhttp = pattern1.matcher(sz);
        String firstSz = Mhttp.replaceAll("");
        Matcher Mwww = pattern2.matcher(firstSz);
        return Mwww.replaceAll("");
    }
    
    public static String getDomain(String url) {
        return cleanStr(url).split("/")[0];
    }

    /*
     * add http && www to the domain string
     * 
     * @param sz String the string without http://www.
     * 
     * @return String the input string with http//www.
     */
    public static String addHttp(String szString) {
        return "http://www.".concat(szString);
    }

    /*
     *  simple code for creating a folder
     * 
     *  @param Fname String the folder's name
     * 
     *  @return boolean true if the folder was created, false if it wasn't
     */
    public boolean createFolder(String fName) {
        return new File(fName).mkdir();
    }

    /*
     *  create the report folder
     *  
     *  @param none
     * 
     *  @return none
     */
    public void makeReportFolder() {
        new File(rFolder).mkdir();
    }

    /*
     *  Check if the folder exist
     * 
     *  @param foldername String the name of the folder to check
     * 
     *  @return boolean true if exists, false if it doesn't exist
     */
    public boolean folderExist(String foldername) {
        String path = currentDir + osPath() + foldername;
        File file = new File(path);
        return file.exists();
    }

    /*
     *  return the correct slashes because Windows and Linux have different FileSystem with
     *  different type of path. 
     * 
     *  @return returnPath String the correct slashes
     */
    public String osPath() {
        String OS = System.getProperty("os.name");
        if (OS.startsWith("Win")) { //windows os
            return "\\";
        } else { //unix, linux etc
            return "/";
        }
    }
    
    /*
     *  This function downloads the web page and returns the content
     * 
     *  @param page
     * 
     *  @return string contains the html of the page, null if the url is offline
     */
    public String getDownloadPage(String page) throws IOException {
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
}
