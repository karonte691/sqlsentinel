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
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlManager {

    private File xml = null;
    private SAXBuilder builder = null;
    private Document document = null;

    public XmlManager(File xmlFile) throws JDOMException, IOException {
        this.xml = xmlFile;
        builder = new SAXBuilder();
        document = builder.build(xml);
    }

    /*
     *  retrieve config's xml param value
     * 
     *  @param paramName String name of the param
     * 
     *  @return String value of the param given in input
     */
    public String readXmlParam(String paramName) {
        String returnValue = new String();

        try {
            Element root = document.getRootElement();
            returnValue = root.getChild(paramName).getText();
            return returnValue;

        } catch (Exception e) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /*
     *  Update xml param
     * 
     *  @param paramName String param that we will update
     *  @param paramValue String new value
     * 
     *  @return true if success
     */
    public boolean updateXmlParam(String paramName, String paramValue) {
        try {
            Element rootNode = document.getRootElement();
            rootNode.getChild(paramName).setText(paramValue);
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileWriter(xml.getName()));
            return true;
        } catch (Exception e) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
}
