package sqlsentinel.core;

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


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sqlsentinel.core.XmlManager;



public class ConfigManager {
    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private final String configXml = "config.xml";
    private File configFile = null;
    private XmlManager xmlManager = null;
    private String configPath = sqlutils.getSQLSentinelPath() + sqlutils.osPath() + configXml;         
    
    
    
    
    public ConfigManager(){ }
    
    
    /*
     *  load configuration's xml
     * 
     *  @return boolean success, false -> failed
     */
    public boolean loadConfigFile(){
        try
        {      
           // System.out.println(configPath);
            configFile = new File(this.configPath);
            if(!configFile.exists() || !configFile.canRead() || !configFile.canWrite()) {
                return false;
            }
            xmlManager = new XmlManager(configFile);  
            return true;
        }catch(Exception ex){
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /*
     *  get sqlsentinel icon param value
     * 
     *  @return String name of sqlsentinel icon
     */
    public String getSQLSentinelIcon(){
        try
        {
            return xmlManager.readXmlParam("icon").toString();
        }catch(Exception ex){
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /*
     *  get update url param value
     * 
     *  @return String update url
     */
    public String getUpdateUrl(){
        try
        {
            return xmlManager.readXmlParam("updateUrl").toString();
        }catch(Exception ex){
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /*
     *  Check if we can start the update thread by param value
     *  if canUpdate is not set to 0, we can check update
     *  
     *  @return boolean true if we can check update, false if not
     */
    public boolean canUpdate(){
        try
        {
            int updateStatus = Integer.parseInt(xmlManager.readXmlParam("canUpdate"));
            if(updateStatus == 0)
                return false;
            return true;           
        }catch(Exception ex){
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /*
     *  get actual version of sqlsentinel param value
     * 
     *  @return String sqlsentinel's version
     */
    public String getActualVersion(){
        try
        {
            return xmlManager.readXmlParam("version").toString();
        }catch(Exception ex){
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
    
    
    
    
    
}
