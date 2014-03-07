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

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author Daniel Kec
 */
public class SessionBeanValidator {
    /*^[^\"]*\@\bStateless\b\s*(\([^\)]*[^)]*\))?*/
    private static final Pattern SESSION_ANNOTATION_PATTERN = Pattern.compile("^[^\\\"]*\\@\\bStateless\\b\\s*(\\([^\\)]*[^)]*\\))?");
    /*.*\bclass\b.*\bimplements\b.*\bSessionBean\b.*\{*/
    private static final Pattern SESSION_INTERFACE_PATTERN  = Pattern.compile("^[^\\{]*\\bimplements\\b[^\\{]*\\bSessionBean\\b[^\\{]*\\{");
    
    private static final Logger LOG = Logger.getLogger(SessionBeanValidator.class.getName());
    
    public static EJBInfo validate(FileObject fo){
        try {
            String asText = fo.asText();
            Matcher ma = SESSION_ANNOTATION_PATTERN.matcher(asText);
            EjbDescriptorHunter descriptorHunter = EjbDescriptorHunter.getDefault(fo);
            String beanName    = fo.getName();
            String description = "-";
            String mappedName  = "-";
            
            if(descriptorHunter.hasEjbJarXml()){
               beanName    = descriptorHunter.getBeanName();
               description = descriptorHunter.getDescription();
               //mappedName  = descriptorHunter.get
            }
                    
            if(ma.find()){
                AnnotationParser annotaionParams = new AnnotationParser(ma.group(1));    
                beanName    = annotaionParams.getValue("name")       !=null?annotaionParams.getValue("name"):beanName;
                description = annotaionParams.getValue("description")!=null?annotaionParams.getValue("description"):description;
                mappedName  = annotaionParams.getValue("mappedName") !=null?annotaionParams.getValue("mappedName"):mappedName;
                LOG.info("EJBInfo "+beanName+", "+description+", "+mappedName);
                return new EJBInfo(beanName,EJBInfo.EJB_VERSION_3X, mappedName,description, fo);                
            }
            
            if(!asText.contains("SessionBean"))return null;
            Matcher mi = SESSION_INTERFACE_PATTERN.matcher(asText);
            if(mi.find())return new EJBInfo(fo.getName(),EJBInfo.EJB_VERSION_2X,mappedName,description, fo);
            return null;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static class EJBInfo {
    public static final int EJB_VERSION_2X = 2;
    public static final int EJB_VERSION_3X = 3;
        private final String beanName;
        private int version=3;
        private final String jndiName;
        private final String description;
        private final FileObject fo;

        public String getBeanName() {
            return beanName;
        }

        public int getVersion() {
            return version;
        }

        public String getJndiName() {
            return jndiName;
        }

        public FileObject getFo() {
            return fo;
        }

        public String getDescription() {
            return description;
        }
        public EJBInfo(String beanName, int version,String jndiName,String description,FileObject fo) {
            this.beanName = beanName;
            this.version = version;
            this.jndiName = jndiName;
            this.description = description;
            this.fo = fo;
        }
    }
}
