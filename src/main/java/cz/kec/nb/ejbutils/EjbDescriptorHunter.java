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
import java.util.Enumeration;
import javax.xml.parsers.ParserConfigurationException;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel Kec
 */
public class EjbDescriptorHunter {
    private final FileObject beanFo;
    private final Document ejbJarXml;
    private String beanName;
    private String description;

    public Document getEjbJarXmlDom() {
        return ejbJarXml;
    }

    private EjbDescriptorHunter(FileObject beanFo) {
        this.beanFo = beanFo;
        ejbJarXml = huntEjbJarXml(beanFo);
        if(ejbJarXml==null)return;
        this.beanName = getBeanProp("ejb-name");
        this.description = getBeanProp("description");
    }
    
    public static EjbDescriptorHunter getDefault(FileObject fo){
        return new EjbDescriptorHunter(fo);
    }
    
    public boolean hasEjbJarXml(){
        return ejbJarXml!=null;
    }
    
    private String getBeanProp(String elemName){
        return XmlUtils.xpath(
/**/                
"//ns:"+elemName+"[substring(../ns:ejb-class, string-length(../ns:ejb-class) - string-length('"+beanFo.getName()+"') + 1)='"+beanFo.getName()+"']/text()", 
                this.getEjbJarXmlDom(),
                new String[]{"ns=http://java.sun.com/xml/ns/j2ee"});
    }
    
    public static Document huntEjbJarXml(FileObject fo){
        try {
            ProjectManager pm = ProjectManager.getDefault();
            Project project = pm.findProject(fo.getParent());
            if(project==null)return null;
            Enumeration<? extends FileObject> files = project.getProjectDirectory().getData(true);
            while (files.hasMoreElements()) {
                FileObject file = files.nextElement();
                if(!file.hasExt("xml"))continue;
                if(!file.getName().equals("ejb-jar.xml"))continue;
                return XmlUtils.parseXmlFile(fo,true);
            }
            
            return null;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ParserConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SAXException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;        
    }

    public String getBeanName() {
        return this.beanName;
    }

    public String getDescription() {
        return description;
    }
    
    
}
