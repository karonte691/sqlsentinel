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

import java.util.Random;


public class userAgent {
    public static boolean useRandomUserAgent = false;
    private static String[] userAgent ={
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)",
            "Googlebot/2.1 ( http://www.googlebot.com/bot.html)",
            "msnbot-Products/1.0 (+http://search.msn.com/msnbot.htm)",
            "Opera/9.20 (Windows NT 6.0; U; en)",
            "Opera/9.00 (Windows NT 5.1; U; en)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)",
            "AnzwersCrawl/2.0 (anzwerscrawl@anzwers.com.au;Engine)",
            "Avant Browser (http://www.avantbrowser.com)",
            "Dumbot(version 0.1 beta - dumbfind.com)",
            "Feed Seeker Bot (RSS Feed Seeker http://www.MyNewFavoriteThing.com/fsb.php)",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; BOLT/2.800) AppleWebKit/534.6 (KHTML, like Gecko) Version/5.0 Safari/534.6.3",
    };
            
    /*
     * return random user agent string
     */
    public  String getRandomUserAgent(){
        Random rand = new Random();
        int rand_i = rand.nextInt(userAgent.length);
        
        return userAgent[rand_i].toString();
    }
}
