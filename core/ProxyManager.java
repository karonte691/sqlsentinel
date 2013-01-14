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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;


public class ProxyManager {
    
    public static boolean useProxy = false;
    public static Proxy proxy = null;
    public static String proxyHost = null;
    public static String proxyPort = null;
    
    public  boolean setProxy(String proxy, String port, boolean tor){  
            try
            {
                if(tor == true)
                    new Socket("127.0.0.1", 8118).close();
                else
                    new Socket(proxy, Integer.parseInt(port)).close();
                useProxy = true;          
            }catch(Exception e){
                return false;             
            }
            if(useProxy)
                if(tor){
                    setTorConnection();
                    proxyHost = "127.0.0.1";
                    proxyPort = "8118";
                }
                    
                else{
                    setProxyConnection(proxy, port);
                    proxyHost = proxy;
                    proxyPort = port;
                }
            return true;
    }
    
     /*
     *  Setting up a proxy for the connection
     * 
     *  @param proxy String ip of the proxy
     *         port String proxy port
     * 
     *  @return: none
     */
    private static Proxy setProxyConnection(String proxy_ip, String port){
        try
        {
            if(proxy_ip.length() <= 0 || port.length() <= 0) {
                return null;
            }    
            
            if(proxy == null) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_ip, Integer.parseInt(port)));
            }
            
            return proxy;
             
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    
     /*
     *  Setting up tor proxy
     * 
     * 
     *  @return: none
     */
    
    private static void setTorConnection(){
        try
        {
            setProxyConnection("127.0.0.1", "8118");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
