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

import javax.swing.Icon;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 *
 * @author daniel
 */
public class EJBTypeDescriptor extends TypeDescriptor{
    public static final int EJB_VERSION_2X = 2;
    public static final int EJB_VERSION_3X = 3;

    private static final Icon beanIcon3 = ImageUtilities.loadImageIcon("cz/kec/nb/ejbutils/Ejb3.png", false); // NOI18N
    private static final Icon beanIcon2 = ImageUtilities.loadImageIcon("cz/kec/nb/ejbutils/Ejb2.png", false); // NOI18N
    
    private final String simpleName;
    private final int ejbVersion;
    private final FileObject fileObject;
    private String projectName;
    private Icon projectIcon;
    private String description;
    private String mappedName;

    public EJBTypeDescriptor(String simpleName,int ejbVersion, FileObject fileObject) {
        this.simpleName = simpleName;
        this.ejbVersion = ejbVersion;
        this.fileObject = fileObject;
    }
    public EJBTypeDescriptor(SessionBeanValidator.EJBInfo ejbInfo) {
        this.simpleName  = ejbInfo.getBeanName();
        this.ejbVersion  = ejbInfo.getVersion();
        this.description = ejbInfo.getDescription();
        this.mappedName  = ejbInfo.getJndiName();
        this.fileObject  = ejbInfo.getFo();
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getDescription() {
        return description;
    }

    public String getMappedName() {
        return mappedName;
    }

    public synchronized String getProjectName() {
        if (projectName == null) {
            initProjectInfo();
        }

        return projectName;
    }

    @Override
    public String getContextName() {
        return null;
    }

    public synchronized Icon getProjectIcon() {
        if (projectIcon == null) {
            initProjectInfo();
        }

        return projectIcon;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    @Override
    public int getOffset() {
        return -1;
    }

    @Override
    public String getOuterName() {
        return null;
    }

    @Override
    public String getTypeName() {
        return getSimpleName();
    }

    public int getEjbVersion() {
        return ejbVersion;
    }

    @Override
    public Icon getIcon() {
        if(this.ejbVersion==EJB_VERSION_2X){
            return beanIcon2;
        }
        return beanIcon3;
    }

    private void initProjectInfo() {
        Project p = FileOwnerQuery.getOwner(fileObject);
        if (p != null) {
            ProjectInformation pi = ProjectUtils.getInformation(p);
            projectName = pi.getDisplayName();
            projectIcon = pi.getIcon();
        }
    }

    @Override
    public void open() {
        OpenCookie open = fileObject.getLookup().lookup(OpenCookie.class);
        open.open();
    }
    
}
