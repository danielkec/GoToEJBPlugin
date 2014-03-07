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

import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;

/**
 *
 * @author daniel
 */
public class EJBTypeProvider implements TypeProvider{
    
private static final Logger LOGGER = Logger.getLogger(EJBTypeProvider.class.getName());
//    private static final ClassPath EMPTY_CLASSPATH = ClassPathSupport.createClassPath( new FileObject[0] );
//    private Set<CacheItem> cache;
      private volatile boolean isCanceled = false;
//    private final TypeElementFinder.Customizer customizer;
  //  private ClasspathInfo cpInfo;
//    private GlobalPathRegistryListener pathListener;

    public String name() {
        return "java"; // NOI18N
    }

    public String getDisplayName() {
        // TODO - i18n
        return "Java Classes";
    }
    
    public void cleanup() {

    }

    public void cancel() {
        isCanceled = true;
    }
    
    public EJBTypeProvider() {
//        pathListener = new GlobalPathRegistryListener() {
//
//            public void pathsAdded(GlobalPathRegistryEvent event) {
//                cache = null; cpInfo = null;
//            }
//
//            public void pathsRemoved(GlobalPathRegistryEvent event) {
//                cache = null; cpInfo = null;
//            }
//        };
//        GlobalPathRegistry.getDefault().addGlobalPathRegistryListener(pathListener);
    }
   
    
//    // This is essentially the code from OpenDeclAction
//    // TODO: Was OpenDeclAction used for anything else?
//    public void gotoType(TypeDescriptor type) {
//    //public void actionPerformed(ActionEvent e) {
//        Lookup lkp = WindowManager.getDefault().getRegistry().getActivated().getLookup();
//        DataObject activeFile = (DataObject) lkp.lookup(DataObject.class);
//        Element value = (Element) lkp.lookup(Element.class);
//        if (activeFile != null && value != null) {
//            JavaSource js = JavaSource.forFileObject(activeFile.getPrimaryFile());
//            if (js != null) {
//                ClasspathInfo cpInfo = js.getClasspathInfo();
//                assert cpInfo != null;
//                UiUtils.open(cpInfo,value);
//            }
//        }
//    }

    public void computeTypeNames(Context context, Result res) {
        String text = context.getText();
        SearchType searchType = context.getSearchType();
        Project[] openProjects = OpenProjects.getDefault().getOpenProjects();
        //ProjectUtils.
        
//        boolean hasBinaryOpen = Lookup.getDefault().lookup(BinaryElementOpen.class) != null;
//        final ClassIndex.NameKind nameKind;
//        switch (searchType) {
//        case EXACT_NAME: nameKind = ClassIndex.NameKind.SIMPLE_NAME; break;
//        case CASE_INSENSITIVE_EXACT_NAME: nameKind = ClassIndex.NameKind.CASE_INSENSITIVE_REGEXP; break;        
//        case PREFIX: nameKind = ClassIndex.NameKind.PREFIX; break;
//        case CASE_INSENSITIVE_PREFIX: nameKind = ClassIndex.NameKind.CASE_INSENSITIVE_PREFIX; break;
//        case REGEXP: nameKind = ClassIndex.NameKind.REGEXP; break;
//        case CASE_INSENSITIVE_REGEXP: nameKind = ClassIndex.NameKind.CASE_INSENSITIVE_REGEXP; break;
//        case CAMEL_CASE: nameKind = ClassIndex.NameKind.CAMEL_CASE; break;
//        default: throw new RuntimeException("Unexpected search type: " + searchType);
//        }
        int maxItemCount = 0;
        long time;
        time = System.currentTimeMillis();
        for (Project project : openProjects) {
            Sources src = ProjectUtils.getSources(project);
            SourceGroup[] javaGroups = src.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
            for (SourceGroup javaGroup : javaGroups) {
                // javaGroup.getRootFolder().
               // ClassPath classPath = ClassPath.getClassPath(javaGroup.getRootFolder(), ClassPath.COMPILE);
                Enumeration<? extends FileObject> children = javaGroup.getRootFolder().getChildren(true);
             //   if(classPath==null)continue;
              //  List<ClassPath.Entry> entries = classPath.entries();
                while(children.hasMoreElements()) {   
                    Object o = children.nextElement();
                  //  LOGGER.info(">>>>>>>>>>>>"+o.getClass().getName());
                    FileObject fo = (FileObject) o;
                    //if(fo==null)continue;
                    if(fo.isFolder())continue;
                    if(!fo.hasExt("java"))continue;
                    SessionBeanValidator.EJBInfo ejbInfo = SessionBeanValidator.validate(fo);
                    if(ejbInfo==null)continue;
                    if(fo.getName().toUpperCase().contains(text.toUpperCase())||text.equals("*")){
                        res.addResult(new EJBTypeDescriptor(ejbInfo));
                        if(maxItemCount++==70)return;
                    }
                }
            }
            
        }
        
                
            //    ClassPath scp = //RepositoryUpdater.getDefault().getScannedSources();
                
       
    }
    
    private static boolean isAllUpper( String text ) {
        for( int i = 0; i < text.length(); i++ ) {
            if ( !Character.isUpperCase( text.charAt( i ) ) ) {
                return false;
            }
        }
        
        return true;
    }
   
    private static String removeNonJavaChars(String text) {
       StringBuilder sb = new StringBuilder();

       for( int i = 0; i < text.length(); i++) {
           char c = text.charAt(i);
           if( Character.isJavaIdentifierPart(c) || c == '*' || c == '?') {
               sb.append(c);
           }
       }
       return sb.toString();
    }
   

    
}
