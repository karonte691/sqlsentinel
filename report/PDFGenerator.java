package sqlsentinel.report;

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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.JOptionPane;


import sqlsentinel.core.SQLSentinelUtils;


public class PDFGenerator {
    private SQLSentinelUtils sqlutils = new SQLSentinelUtils();
    private static Vector urlVuln, vulnType, urlCrawled;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    public static String FILE, url;
    private String pdfDir = System.getProperty("user.dir") + sqlutils.osPath() + "Report" + sqlutils.osPath();
    private Calendar c = new GregorianCalendar();

    /*
     * Constructor
     * 
     * @param url String the domain url
     */
    public PDFGenerator(String url) {
        this.url = url;
        this.urlVuln = new Vector();
        this.vulnType = new Vector();
        this.urlCrawled = new Vector();
    }
    
    /*
     *  We can add vuln url and vuln type to the local vector through this function.
     *  
     *  @param url String the vuln url(http://site.com/hi.php?id=5')
     *  @param Vuln String description of the vuln 
     * 
     */
    public void addUrlVuln(String url, String Vuln){
        this.urlVuln.add(url);
        this.vulnType.add(Vuln);
    }
    
    /*
     *  This fuctions add an url crawled by spider to the vector
     * 
     *  @param urlcrawled String the url crawled by the spider
     */
    public void addUrlCrawled(String urlcrawled){
        this.urlCrawled.add(urlcrawled);
    }
    
    /*
     *  Try to create the pdf and add the report information(like url crawled, url vuln etc)
     *  The pdf filename should be: url_of_the_site_tested +  current_day + current_month + current_year +
     *                              + current_hour + current_minute + current_second + ".pdf"
     * 
     *  Example: localhost_30_3_2012_19_5_4.pdf
     * 
     *  @param none
     * 
     *  @return true if the operation finished successfully
     */
    public boolean makePdf(){
        try
        {  
            if(urlVuln.size() == 0)
            {
                JOptionPane.showMessageDialog(null, "Error, cannot write report: there aren't any vuln link");
                return false;
            }
            Document document = new Document();
            FILE = pdfDir + url + "_" + c.get(Calendar.DAY_OF_MONTH) + "_" + c.get(Calendar.MONTH) + "_" + c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)
                          + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND) + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            createTable(document);
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /*
     *  This method build the pdf header.
     * 
     *  @param document Document the instance of pdf document
     */
    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("SQLSentinel Report", catFont));
        addEmptyLine(preface, 2);
        preface.add(new Paragraph("Report generated on: " + new Date(), smallBold));
        addEmptyLine(preface, 2);
        preface.add(new Paragraph("Domain: " + url, smallBold));
        addEmptyLine(preface, 2);
        preface.add(new Paragraph("SQL Errors  founds: ", catFont));
        addEmptyLine(preface, 1);
        document.add(preface);
    }
    
    /*
     *  Create and populate the table content with the vectors(urlVuln, vulnType, urlCrawled)
     * 
     *  @param document Document instance of pdf document
     */
    private static void createTable(Document document) throws BadElementException, DocumentException {
        float[] colsWidth = {2f, 1f}; // Code 1
        PdfPTable table = new PdfPTable(colsWidth);

        table.setWidthPercentage(100);
        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Url"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("type of vulnerability"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);


        table.setHeaderRows(1);

        for (int i = 0; i < urlVuln.size(); i++) {
            table.addCell((String) urlVuln.elementAt(i));
            table.addCell((String) vulnType.elementAt(i));
        }
        document.add(table);
        
        
        //url crawled table
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 2);
        preface.add(new Paragraph("Url Crawled:", catFont));
        addEmptyLine(preface, 1);
        
        document.add(preface);
        
        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100);
        
        table2.setHeaderRows(1);
        for(int j = 0; j < urlCrawled.size(); j++)
            table2.addCell((String)urlCrawled.elementAt(j));
        document.add(table2);
        
    }
   
    /*
     *  add empty lines 
     * 
     *  @param paragraph Paragraph The instance of Paragraph where we will add the empty lines
     *  @param number int the number of empty lines
     */
   private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

   /*
    *  Add meta data to pdf document
    * 
    *  @param document Document instance of pdf document
    */
    private static void addMetaData(Document document) {
        document.addTitle("SQLSentinel Report");
        document.addSubject("site: " + url);
        document.addKeywords("");
        document.addAuthor("SQLSentinel");
        document.addCreator("SQLSentinel");
    }
    
    /*
     *  return the path where pdf was created
     * 
     *  @return file String the path of pdf
     */
    public String GetPDFPath(){
        return FILE;
    }
    
}
