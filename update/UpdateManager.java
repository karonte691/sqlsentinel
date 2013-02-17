package sqlsentinel.update;

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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import sqlsentinel.core.ConfigManager;

public class UpdateManager {

    private ConfigManager cfg = null;
    private String updateUrl = new String();
    private URL url = null;
    private String actualVersion = new String();
    private HttpURLConnection connection = null;
    private String postData = new String();
    private String respXml = null;
    public String newVersionUpdateAvailable = null;
    private SAXBuilder builder = new SAXBuilder();
    private Reader in = null;
    private Document doc = null;
    private Element root = null;
    private int updateFlag = 0;
    

    public UpdateManager(String updateUrl, String actualVersion) {
        this.updateUrl = updateUrl;
        this.actualVersion = actualVersion;
    }

    /*
     *  check if there is an update available by doing a http post request
     *  to web services defined in config.xml
     * 
     *  @return boolean true if there is an update
     */
    public boolean isUpdateAvailable() {
        if (actualVersion.length() == 0 || updateUrl.length() == 0) {
            return false;
        }

        try {
            respXml = this.doUpdatePostRequest();
            if (respXml == null || respXml.length() == 0) {
                return false;
            }
            in = new StringReader(respXml);          
            doc = builder.build(in);
            root = doc.getRootElement();
            updateFlag = Integer.parseInt(root.getChild("updateFlag").getText());
            if (updateFlag == 1) {
                newVersionUpdateAvailable = root.getChild("versionAv").getText();
                return true;
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /*
     *  submit the post request to the webservices
     *  version is taken from config.xml
     * 
     *  @return response String the webservice xml response
     */
    private String doUpdatePostRequest() {
        try {
            url = new URL(updateUrl);
            postData = "version=" + URLEncoder.encode(actualVersion, "UTF-8");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            //Get Response	
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }
    }
}
