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
package sqlsentinel.spider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sqlsentinel.core.SQLSentinelUtils;

import sqlsentinel.gui.SQLGuiManager;
import sqlsentinel.core.userAgent;
import sqlsentinel.core.ProxyManager;
import sqlsentinel.core.cookieManager;

public class Crawler {

    private HashSet<String> urlSearched = null;
    private String baseDomain;
    private SQLGuiManager sqlgui;
    private cookieManager cookieM;
    private Connection conn;

    /*
     *  Constructor
     * 
     *  @param url String url of domain
     *  @param sqlgui SQLGuiManager instance of SQLGuiManager class
     *  @param pdfGen PDFGenerator instance of PDFGenerator class
     * 
     */
    public Crawler(String url, SQLGuiManager sqlgui) {
        this.baseDomain = url;
        urlSearched = new HashSet<String>();
        this.sqlgui = sqlgui;
    }

    /*
     *  This recursive function crawl all the page of the site 
     * 
     *  @param none
     * 
     *  @return vectorSearched Vector contains all the pages crawled
     * 
     */
    public void doScan(String urltocrawl) {
        urltocrawl = SQLSentinelUtils.addHttp(SQLSentinelUtils.cleanStr(urltocrawl));
        if (urltocrawl.length() <= 1) {
            return;
        }

        try {
            Document doc = null;
            conn = null;

            if (ProxyManager.useProxy) {
                System.setProperty("http.proxyHost", ProxyManager.proxyHost);
                System.setProperty("http.proxyPort", ProxyManager.proxyPort);
            }

            conn = Jsoup.connect(urltocrawl);

            if (userAgent.useRandomUserAgent) {
                conn.userAgent(new userAgent().getRandomUserAgent());
            }

            //check cookie
            if (cookieM.useCookie && cookieM.cookie.size() > 0) {
                for (Map.Entry<String, String> e : cookieM.cookie.entrySet()) {
                    conn.cookie(e.getKey(), e.getValue());
                }
            }

            doc = conn.get();

            //System.out.println(doc.body().toString());

            if (urlSearched.add(urltocrawl)) {
                sqlgui.addRow(urltocrawl + " <-- added", "SpiderPanel");
            }

            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String urlfound = link.attr("abs:href").toString();

                if (sameDomain(baseDomain, urlfound) && !urlSearched.contains(urlfound)) {
                    if (urlSearched.add(urlfound)) {
                        sqlgui.addRow(urlfound + " <-- added", "SpiderPanel");
                        this.doScan(urlfound);
                    }
                }

            }
        } catch (IOException ex) {
            System.err.println(urltocrawl + " not valid");
        }
    }

    public HashSet<String> getUrlSearched() {
        return urlSearched;
    }

    /*
     *  pageExists if the site is online
     * 
     *  @param szSite String the url of the domain
     * 
     *  @return int 1 if the site is online and 0 if it isn't online
     * 
     */
    private boolean isOnline(String szSite) {
        try {
            InetAddress.getByName(szSite);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /*
     *  pageExists if page exist
     * 
     *  @param urldomain String the page to pageExists
     * 
     *  @return boolean true if page exists, false if it doesn't exist
     * 
     *  @note taken from http://stackoverflow.com/questions/1378199/how-to-pageExists-if-a-url-exists-or-returns-404-with-java
     */
    private boolean pageExists(String urldomain) {
        try {
            URL u = new URL(urldomain);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int response = huc.getResponseCode();
            return response == 200;
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    /*
     * 
     *   Compares if the url have the same top-level domain
     * 
     *   @param url1 String first url
     *   @param url2 String second url
     * 
     *   @return boolean true if the top-level domain is the same in the two urls, false if not
     */

    private static boolean sameDomain(String url1, String url2) {
        url1 = SQLSentinelUtils.getDomain(url1);
        url2 = SQLSentinelUtils.getDomain(url2);

        return url1.equals(url2);
    }
}
