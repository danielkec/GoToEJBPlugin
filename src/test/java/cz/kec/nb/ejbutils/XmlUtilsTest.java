/*
 * The MIT License
 *
 * Copyright 2014 Daniel Kec.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package cz.kec.nb.ejbutils;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel Kec <daniel at kecovi.cz>
 */
public class XmlUtilsTest {
    
    public XmlUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseXml method, of class XmlUtils.
     */
    @Test
    public void testParseXml() throws Exception {
        System.out.println("parseXml");
        String content = xml;
        XmlUtils.parseXml(content,true);
    }


    /**
     * Test of xpath method, of class XmlUtils.
     */
    @Test
    public void testXpath_String_Document() throws ParserConfigurationException, SAXException, IOException {
        System.out.println("xpath");
        String xpathExpr = "//ns:description[../ns:ejb-name='DNScheduler']/text()";
        Document dom = XmlUtils.parseXml(xml,true);
        String expResult = "Timed objekt pro planovane batchove ulohy";
        String result = XmlUtils.xpath(xpathExpr, dom,new String[]{"ns=http://java.sun.com/xml/ns/j2ee"});
        assertEquals(expResult, result);
    }
    String xml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<ejb-jar id=\"ejb-jar_ID\" version=\"2.1\" xmlns=\"http://java.sun.com/xml/ns/j2ee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd\">\n" +
"    <description>Obsluha online aplikaci a scheduleru</description>\n" +
"    <display-name>dnBusinessLogic</display-name>\n" +
"	<enterprise-beans>\n" +
"		<session id=\"Lustrace\">\n" +
"			<ejb-name>Lustrace</ejb-name>\n" +
"			<local-home>cz.syntea.dn.business.lustrace.LustraceLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.business.lustrace.LustraceLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.business.lustrace.impl.LustraceBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"			<ejb-local-ref id=\"EJBLocalRef_1261575917970\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<ejb-ref-name>dn/ejb/AamServiceSoapBindingImpl</ejb-ref-name>\n" +
"				<ejb-ref-type>Session</ejb-ref-type>\n" +
"				<local-home>sk.regobsa.aam.applogic.ejb.AamServiceLocalHome</local-home>\n" +
"				<local>sk.regobsa.aam.applogic.ejb.AamServiceLocal</local>\n" +
"				<ejb-link>dnAAM.jar#AamServiceSoapBindingImpl</ejb-link>\n" +
"			</ejb-local-ref>\n" +
"			<resource-ref id=\"ResourceRef_1256563406571\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>jdbc/dsDN</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"		<session id=\"HlasnaSluzba\">\n" +
"			<ejb-name>HlasnaSluzba</ejb-name>\n" +
"			<local-home>cz.syntea.dn.business.hs.HlasnaSluzbaLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.business.hs.HlasnaSluzbaLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.business.hs.impl.HlasnaSluzbaBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"			<ejb-local-ref id=\"EJBLocalRef_1263830086856\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<ejb-ref-name>dn/ejb/AamServiceSoapBindingImpl</ejb-ref-name>\n" +
"				<ejb-ref-type>Session</ejb-ref-type>\n" +
"				<local-home>sk.regobsa.aam.applogic.ejb.AamServiceLocalHome</local-home>\n" +
"				<local>sk.regobsa.aam.applogic.ejb.AamServiceLocal</local>\n" +
"				<ejb-link>dnAAM.jar#AamServiceSoapBindingImpl</ejb-link>\n" +
"			</ejb-local-ref>\n" +
"			<resource-ref id=\"ResourceRef_1263830086842\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>jdbc/dsDN</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"		<session id=\"DNScheduler\">\n" +
"            <description>Timed objekt pro planovane batchove ulohy</description>\n" +
"            <ejb-name>DNScheduler</ejb-name>\n" +
"			<local-home>cz.syntea.dn.centrum.schedule.DNSchedulerLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.centrum.schedule.DNSchedulerLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.centrum.schedule.impl.DNSchedulerBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"<!--			<ejb-local-ref id=\"EJBLocalRef_1273492675081\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<ejb-ref-name>dn/ejb/D2ExportSedn</ejb-ref-name>\n" +
"				<ejb-ref-type>Session</ejb-ref-type>\n" +
"				<local-home>cz.syntea.dn.centrum.d2.exportsedn.D2ExportSednLocalHome</local-home>\n" +
"				<local>cz.syntea.dn.centrum.d2.exportsedn.D2ExportSednLocal</local>\n" +
"				<ejb-link>dnCentrumBackend.jar#D2ExportSedn</ejb-link>\n" +
"			</ejb-local-ref>\n" +
"			<ejb-local-ref id=\"EJBLocalRef_1275909138502\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<ejb-ref-name>dn/ejb/DataExport</ejb-ref-name>\n" +
"				<ejb-ref-type>Session</ejb-ref-type>\n" +
"				<local-home>cz.syntea.dn.centrum.dataexport.DataExportLocalHome</local-home>\n" +
"				<local>cz.syntea.dn.centrum.dataexport.DataExportLocal</local>\n" +
"				<ejb-link>dnCentrumBackend.jar#DataExport</ejb-link>\n" +
"			</ejb-local-ref>-->\n" +
"<!--			<ejb-local-ref id=\"EJBLocalRef_1277190062284\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<ejb-ref-name>dn/ejb/Statistika</ejb-ref-name>\n" +
"				<ejb-ref-type>Session</ejb-ref-type>\n" +
"				<local-home>cz.syntea.dn.centrum.statistika.StatistikaLocalHome</local-home>\n" +
"				<local>cz.syntea.dn.centrum.statistika.StatistikaLocal</local>\n" +
"				<ejb-link>dnCentrumBackend.jar#Statistika</ejb-link>\n" +
"			</ejb-local-ref>-->\n" +
"			<resource-ref id=\"ResourceRef_1271066737761\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>jdbc/dsDNWAS</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"			<resource-ref id=\"ResourceRef_1282811249611\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>fileDNAppConfig</res-ref-name>\n" +
"				<res-type>java.net.URL</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"		<session id=\"RychleHlaseni\">\n" +
"			<ejb-name>RychleHlaseni</ejb-name>\n" +
"			<local-home>cz.syntea.dn.business.rh.RychleHlaseniLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.business.rh.RychleHlaseniLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.business.rh.impl.RychleHlaseniBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"			<resource-ref id=\"ResourceRef_1273581925152\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>jdbc/dsDN</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"		<session id=\"SystemovaLustrace\">\n" +
"			<ejb-name>SystemovaLustrace</ejb-name>\n" +
"			<local-home>cz.syntea.dn.business.sldn.SystemovaLustraceLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.business.sldn.SystemovaLustraceLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.business.sldn.impl.SystemovaLustraceBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"			<resource-ref id=\"ResourceRef_1273582467428\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>jdbc/dsDN</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"		<session id=\"Statistika\">\n" +
"			<ejb-name>Statistika</ejb-name>\n" +
"			<local-home>cz.syntea.dn.business.statistika.StatistikaLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.business.statistika.StatistikaLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.business.statistika.impl.StatistikaBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"			<ejb-local-ref id=\"EJBLocalRef_1325856076341\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<ejb-ref-name>dn/ejb/AamAuxTransactionService</ejb-ref-name>\n" +
"				<ejb-ref-type>Session</ejb-ref-type>\n" +
"				<local-home>cz.syntea.dn.business.aam.AamAuxTransactionServiceLocalHome</local-home>\n" +
"				<local>cz.syntea.dn.business.aam.AamAuxTransactionServiceLocal</local>\n" +
"				<ejb-link>dnAAM.jar#AamAuxTransactionService</ejb-link>\n" +
"			</ejb-local-ref>\n" +
"			<resource-ref id=\"ResourceRef_1281513395775\">\n" +
"				<res-ref-name>jdbc/dsDN</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"			<resource-ref id=\"ResourceRef_1325774476320\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>fileDNAppConfig</res-ref-name>\n" +
"				<res-type>java.net.URL</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"			<resource-ref id=\"ResourceRef_1325836374778\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>fileDNReportsDir</res-ref-name>\n" +
"				<res-type>java.net.URL</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"			<resource-ref id=\"ResourceRef_1328020318817\">\n" +
"				<description>Databazove pripojeni pro stazku na st schema</description>\n" +
"				<res-ref-name>jdbc/dsDNCentrum</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"		<session id=\"TableOfKeysManager\">\n" +
"			<ejb-name>TableOfKeysManager</ejb-name>\n" +
"			<local-home>cz.syntea.dn.business.tableofkeys.TableOfKeysManagerLocalHome</local-home>\n" +
"			<local>cz.syntea.dn.business.tableofkeys.TableOfKeysManagerLocal</local>\n" +
"			<ejb-class>cz.syntea.dn.business.tableofkeys.impl.TableOfKeysManagerBean</ejb-class>\n" +
"			<session-type>Stateless</session-type>\n" +
"			<transaction-type>Container</transaction-type>\n" +
"			<resource-ref id=\"ResourceRef_1295617886231\">\n" +
"				<description>\n" +
"				</description>\n" +
"				<res-ref-name>jdbc/dsDN</res-ref-name>\n" +
"				<res-type>javax.sql.DataSource</res-type>\n" +
"				<res-auth>Container</res-auth>\n" +
"				<res-sharing-scope>Shareable</res-sharing-scope>\n" +
"			</resource-ref>\n" +
"		</session>\n" +
"	</enterprise-beans>\n" +
"	<assembly-descriptor id=\"AssemblyDescriptor_1368616604232\">\n" +
"		<container-transaction>\n" +
"			<method>\n" +
"				<ejb-name>DNScheduler</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>cancelTimers_Required</method-name>\n" +
"				<method-params>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>DNScheduler</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>initializeTimer_Required</method-name>\n" +
"				<method-params>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>HlasnaSluzba</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getListDn_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>HlasnaSluzba</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getSvodkaDn_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"					<method-param>cz.syntea.dn.activitylog.ActivityWriter</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Lustrace</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getBasicLustraceDN_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"					<method-param>cz.syntea.dn.activitylog.ActivityWriter</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Lustrace</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getDnList_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Lustrace</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getFullLustraceDN_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"					<method-param>cz.syntea.dn.activitylog.ActivityWriter</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>RychleHlaseni</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>ISDN_RH_GetNasledky_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>createStatStandard_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getReportList_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getReport_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getSODSnimky</method-name>\n" +
"				<method-params>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getStatKalendar_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getStatStandard_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getTemplateList_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>updateStatStandard_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>SystemovaLustrace</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getEntityActivityList_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"					<method-param>cz.syntea.dn.activitylog.ActivityWriter</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>SystemovaLustrace</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getSubjectActivityList_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>SystemovaLustrace</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getSubjectList_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>TableOfKeysManager</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getChybaContent_Required</method-name>\n" +
"				<method-params>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>TableOfKeysManager</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>getTableOfKeysContent_Required</method-name>\n" +
"				<method-params>\n" +
"					<method-param>java.lang.String</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<trans-attribute>Required</trans-attribute>\n" +
"		</container-transaction>\n" +
"		<container-transaction>\n" +
"			<method>\n" +
"				<ejb-name>DNScheduler</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>ejbTimeout</method-name>\n" +
"				<method-params>\n" +
"					<method-param>javax.ejb.Timer</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>createStatStandard_RequiresNew</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>updateStatStandard_RequiresNew</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>executeQuery_RequiresNew</method-name>\n" +
"				<method-params>\n" +
"					<method-param>org.w3c.dom.Document</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>isTemplateWithSameName_RequiresNew</method-name>\n" +
"				<method-params>\n" +
"					<method-param>java.lang.String</method-param>\n" +
"					<method-param>int</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<trans-attribute>RequiresNew</trans-attribute>\n" +
"		</container-transaction>\n" +
"		<container-transaction>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>isStandardUpToDate_Mandatory</method-name>\n" +
"				<method-params>\n" +
"					<method-param>int</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<method>\n" +
"				<ejb-name>Statistika</ejb-name>\n" +
"				<method-intf>Local</method-intf>\n" +
"				<method-name>updateStandardToTransferedState_Mandatory</method-name>\n" +
"				<method-params>\n" +
"					<method-param>int</method-param>\n" +
"				</method-params>\n" +
"			</method>\n" +
"			<trans-attribute>Mandatory</trans-attribute>\n" +
"		</container-transaction>\n" +
"	</assembly-descriptor>\n" +
"	<ejb-client-jar>dnBusinessLogicClient.jar</ejb-client-jar>\n" +
"</ejb-jar>\n" +
"";
}
