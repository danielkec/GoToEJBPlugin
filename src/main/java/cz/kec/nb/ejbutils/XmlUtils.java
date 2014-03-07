/*
 * The MIT License
 *
 * Copyright 2014 daniel.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author daniel
 */
public class XmlUtils {

    public static Document parseXml(String content,boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(namespaceAware);
        DocumentBuilder newDocumentBuilder = builderFactory.newDocumentBuilder();
        return newDocumentBuilder.parse(new ByteArrayInputStream(content.getBytes()));       
    }
    public static Document parseXmlFile(FileObject fo,boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(namespaceAware);
        DocumentBuilder newDocumentBuilder = builderFactory.newDocumentBuilder();
        return newDocumentBuilder.parse(Utilities.toFile(fo.toURI()));       
    }
    
    public static String xpath(String xpathExpr,Element e){
        return xpath(xpathExpr, e, null);
    }
    
    public static String xpath(String xpathExpr,Element e,String[] namespaces){
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            if(namespaces!=null&&namespaces.length>0){
                System.out.println(">>> pridavam ns");
                xPath.setNamespaceContext(new SimpleNamespaceContext(namespaces));
            }
            return xPath.evaluate(xpathExpr,e);
        } catch (XPathExpressionException ex) {
            Exceptions.printStackTrace(ex);
        }return null;
    }
     public static String xpath(String xpathExpr,Document dom,String[] namespaces){
         return xpath(xpathExpr, dom.getDocumentElement(),namespaces);
     }
     
     public static class SimpleNamespaceContext implements NamespaceContext{
        HashMap<String,String> prefixMap = new HashMap<String,String>();
        HashMap<String,String> nsMap = new HashMap<String,String>();
        public SimpleNamespaceContext(String prefix,String ns) {
            this.prefixMap.put(prefix, ns);
            this.nsMap.put(ns,prefix);
        }
        /**
         * pole stringu ve tvaru "prefix=http://namespace/1.0"
         * @param decl 
         */
        public SimpleNamespaceContext(String[] decl) {
            for (String string : decl) {
                String[] split = string.split("=");
                this.prefixMap.put(split[0],split[1]);
                this.nsMap    .put(split[1],split[0]);
            }           
        }

        @Override
        public String getNamespaceURI(String prefix) {
            System.out.println("Returning ns "+this.prefixMap.get(prefix));
            return this.prefixMap.get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) {
           return this.nsMap.get(namespaceURI);
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return prefixMap.keySet().iterator();
        }
         
     }
}
