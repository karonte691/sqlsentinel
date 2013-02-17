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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



public class cookieManager {
    public static boolean useCookie = false;
    public static Map<String, String> cookie = new HashMap<String, String>();
    public static String cookie_s = new String();
    
    /*
     *  Load cookie from the gui cookie string, split it and
     *  put the array results in the cookie map var.
     * 
     *  @param cookieList String, the string taken from the gui, that contains the
     *                             original cookie string(cookie1=value1;cookie2=value2)
     * 
     *  @return: none
     * 
     */
    public void loadCookie(String cookieList){
       cookie_s = cookieList;
       
       try {
            //remove blank space
            cookieList = cookieList.replaceAll("\\s", "");
            
            String[] cookie_split = cookieList.split(";");
            if (cookie_split.length > 0) {
                for (int i = 0; i < cookie_split.length; i++) {
                    String[] cookie_part = cookie_split[i].split("=");
                    cookie.put(cookie_part[0].toString(), cookie_part[1].toString());                    
                }
            }
        } catch (Exception ex) {
             Logger.getLogger(cookieManager.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }

}
