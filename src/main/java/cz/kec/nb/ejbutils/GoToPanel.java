/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * markiewb@netbeans.org
 *
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package cz.kec.nb.ejbutils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.openide.util.ImageUtilities;

/**
 *
 * @author  Petr Hrebejk
 */
public class GoToPanel extends javax.swing.JPanel {
            
    private static Icon WAIT_ICON = ImageUtilities.loadImageIcon("org/netbeans/modules/jumpto/resources/wait.gif", false); // NOI18N
    private static Icon WARN_ICON = ImageUtilities.loadImageIcon("org/netbeans/modules/jumpto/resources/warning.png", false); // NOI18N
        
    private static final int BRIGHTER_COLOR_COMPONENT = 10;
    private ContentProvider contentProvider;
    private boolean containsScrollPane;
    JLabel messageLabel;
    private Iterable<? extends TypeDescriptor> selectedTypes = Collections.emptyList();
    private volatile int textId = 0;
    private String oldText;
    private String oldMessage;
    
    // Time when the serach stared (for debugging purposes)
    long time = -1;


    // handling http://netbeans.org/bugzilla/show_bug.cgi?id=203512
    // if the whole search argument (in the name JTextField) is selected and something is pasted in it's place,
    // notify the DocumentListener because it will first call removeUpdate() and then inserteUpdate().
    // When removeUpdate() is called we should not call update() because it messes the messageLabel's text.
    private boolean pastedFromClipboard = false;
    public EJBInfoPanel ejbInfoPanel = new EJBInfoPanel();
    
    /** Creates new form GoToPanel */
    public GoToPanel( ContentProvider contentProvider, boolean multiSelection ) throws IOException {
        this.contentProvider = contentProvider;
        initComponents();
        containsScrollPane = true;
                
        matchesList.setSelectionMode( multiSelection ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
        //matchesList.setPrototypeCellValue("12345678901234567890123456789012345678901234567890123456789012345678901234567890");        
        matchesList.addListSelectionListener(null);
        
        Color bgColorBrighter = new Color(
                                    Math.min(getBackground().getRed() + BRIGHTER_COLOR_COMPONENT, 255),
                                    Math.min(getBackground().getGreen() + BRIGHTER_COLOR_COMPONENT, 255),
                                    Math.min(getBackground().getBlue() + BRIGHTER_COLOR_COMPONENT, 255)
                            );
        
        messageLabel = new JLabel();
        messageLabel.setBackground(bgColorBrighter);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setEnabled(true);
        messageLabel.setText("No ejb found"); // NOI18N
        messageLabel.setFont(matchesList.getFont());
        
        // matchesList.setBackground( bgColorBrighter );
        // matchesScrollPane1.setBackground( bgColorBrighter );
        matchesList.setCellRenderer( contentProvider.getListCellRenderer(
                matchesList,
                caseSensitive.getModel()));
        contentProvider.setListModel( this, null );
        
        PatternListener pl = new PatternListener( this );
        nameField.getDocument().addDocumentListener(pl);
        caseSensitive.setSelected(UiOptions.GoToTypeDialog.getCaseSensitive());
        caseSensitive.addItemListener(pl);
        matchesList.addListSelectionListener(pl);                       

        //searchHistory = new SearchHistory(GoToPanel.class, nameField);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
    }

    /** Sets the model from different therad
     */
    public void setModel( final ListModel model, final int id ) { 
        // XXX measure time here
        final int fid;
        // -1 only from EDT
        fid = id == -1 ? textId : id;
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               if (fid != textId) {
                   return;
               }
               if (model.getSize() > 0 || getText() == null || getText().trim().length() == 0 ) {
                   matchesList.setModel(model);
                   matchesList.setSelectedIndex(0);
                   setListPanelContent(null,false);
                   if ( time != -1 ) {
                       GoToTypeAction.LOGGER.fine("Real search time " + (System.currentTimeMillis() - time) + " ms.");
                       time = -1;
                   }
               }
               else {
                   setListPanelContent("No EJB found",false ); // NOI18N
               }
           }
       });
    }
    
    /** Sets the initial text to find in case the user did not start typing yet. */
    public void setInitialText( final String text ) {
        oldText = text;
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                String textInField = nameField.getText();
                if ( textInField == null || textInField.trim().length() == 0 ) {
                    nameField.setText(text);
                    nameField.setCaretPosition(text.length());
                    nameField.setSelectionStart(0);
                    nameField.setSelectionEnd(text.length());
                }
            }
        });
    }
    
    public void setSelectedTypes() {
        final List<TypeDescriptor> types = new LinkedList<TypeDescriptor>();
        for (Object td : matchesList.getSelectedValues()) {
            types.add((TypeDescriptor)td);
        }
        selectedTypes = Collections.unmodifiableCollection(types);
    }
    
    public Iterable<? extends TypeDescriptor> getSelectedTypes() {
        return selectedTypes;
    }

    void setWarning(String warningMessage) {
        if (warningMessage != null) {
            jLabelWarning.setIcon(WARN_ICON);
            jLabelWarning.setBorder(BorderFactory.createEmptyBorder(3, 1, 1, 1));
        } else {
                jLabelWarning.setIcon(null);
                jLabelWarning.setBorder(null);
        }
        jLabelWarning.setText(warningMessage);
    }
    
    //handling http://netbeans.org/bugzilla/show_bug.cgi?id=178555
    public void setMouseListener(MouseListener warningMouseListener) {
        if (messageLabel.getMouseListeners().length == 0) {
            messageLabel.addMouseListener(warningMouseListener);
        }
    }
            
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("deprecation")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelText = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabelList = new javax.swing.JLabel();
        listPanel = new javax.swing.JPanel();
        matchesScrollPane1 = new javax.swing.JScrollPane();
        matchesList = new javax.swing.JList();
        jLabelWarning = new javax.swing.JLabel();
        caseSensitive = new javax.swing.JCheckBox();
        jLabelLocation = new javax.swing.JLabel();
        jTextFieldLocation = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setFocusable(false);
        setNextFocusableComponent(nameField);
        setLayout(new java.awt.GridBagLayout());

        jLabelText.setLabelFor(nameField);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelText,"Go to EJB"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(jLabelText, gridBagConstraints);
        jLabelText.getAccessibleContext().setAccessibleDescription(("GoToPanel.jLabelText.AccessibleContext.accessibleDescription")); // NOI18N

        nameField.setFont(new java.awt.Font("Monospaced", 0, getFontSize()));
        nameField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameFieldKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(nameField, gridBagConstraints);

        jLabelList.setLabelFor(matchesList);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelList, ("List of the Enterprise Java Beans found in opened projects")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(jLabelList, gridBagConstraints);

        listPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        listPanel.setName("dataPanel"); // NOI18N
        listPanel.setLayout(new java.awt.BorderLayout());

        matchesScrollPane1.setBorder(null);
        matchesScrollPane1.setFocusable(false);

        matchesList.setFont(new java.awt.Font("Monospaced", 0, getFontSize()));
        matchesList.setVisibleRowCount(15);
        matchesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                matchesListMouseReleased(evt);
            }
        });
        matchesScrollPane1.setViewportView(matchesList);
        matchesList.getAccessibleContext().setAccessibleName(("ACSD_GoToListName")); // NOI18N
        matchesList.getAccessibleContext().setAccessibleDescription(("GoToPanel.matchesList.AccessibleContext.accessibleDescription")); // NOI18N

        listPanel.add(matchesScrollPane1, java.awt.BorderLayout.CENTER);

        jLabelWarning.setFocusable(false);
        listPanel.add(jLabelWarning, java.awt.BorderLayout.PAGE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(listPanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(caseSensitive, ("TXT_GoToType_CaseSensitive")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        //add(caseSensitive, gridBagConstraints);
        //caseSensitive.getAccessibleContext().setAccessibleDescription(("GoToPanel.caseSensitive.AccessibleContext.accessibleDescription")); // NOI18N
        add(ejbInfoPanel, gridBagConstraints);
        
        jLabelLocation.setText(("EJB Location:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(jLabelLocation, gridBagConstraints);

        jTextFieldLocation.setEditable(false);
        jTextFieldLocation.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jTextFieldLocation, gridBagConstraints);
    }// </editor-fold>                        

    private void matchesListMouseReleased(java.awt.event.MouseEvent evt) {                                          
        if ( evt.getClickCount() == 2 ) {
            nameFieldActionPerformed( null );
        }
    }                                         

    private void nameFieldKeyTyped(java.awt.event.KeyEvent evt) {                                   
        if (boundScrollingKey(evt)) {
            delegateScrollingKey(evt);
        }
    }                                  

    private void nameFieldKeyReleased(java.awt.event.KeyEvent evt) {                                      
        if (boundScrollingKey(evt)) {
            delegateScrollingKey(evt);
        }
    }                                     

    private void nameFieldKeyPressed(java.awt.event.KeyEvent evt) {                                     
        if (boundScrollingKey(evt)) {
            delegateScrollingKey(evt);
        } else {
            //handling http://netbeans.org/bugzilla/show_bug.cgi?id=203512
            Object o = nameField.getInputMap().get(KeyStroke.getKeyStrokeForEvent(evt));
            if (o instanceof String) {
                String action = (String) o;
                if ("paste-from-clipboard".equals(action)) {
                    String selectedTxt = nameField.getSelectedText();
                    String txt = nameField.getText();
                    if (selectedTxt != null && txt != null) {
                        if (selectedTxt.length() == txt.length()) {
                            pastedFromClipboard = true;
                        }
                    }
                }
            }
        }
    }                                    

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (contentProvider.hasValidContent()) {
            contentProvider.closeDialog();
            setSelectedTypes();
        }
    }                                         
    
    
    // Variables declaration - do not modify                     
    private javax.swing.JCheckBox caseSensitive;
    private javax.swing.JLabel jLabelList;
    private javax.swing.JLabel jLabelLocation;
    private javax.swing.JLabel jLabelText;
    private javax.swing.JLabel jLabelWarning;
    private javax.swing.JTextField jTextFieldLocation;
    private javax.swing.JPanel listPanel;
    private javax.swing.JList matchesList;
    private javax.swing.JScrollPane matchesScrollPane1;
    javax.swing.JTextField nameField;
    // End of variables declaration                   
        
    public int getTextId() {
        return textId;
    }
    
    private String getText() {
        try {
            String text = nameField.getDocument().getText(0, nameField.getDocument().getLength());
            return text;
        }
        catch( BadLocationException ex ) {
            return null;
        }
    }
    
    private int getFontSize () {
        return this.jLabelList.getFont().getSize();
    }
    
    public boolean isCaseSensitive () {
        return this.caseSensitive.isSelected();
    }

    void updateMessage(@NullAllowed final String message) {
        final String text = getText();
        if (oldText == null || oldText.trim().length() == 0 || !text.startsWith(oldText) ||
        (message == null ? oldMessage != null : !message.equals(oldMessage))) {
            setListPanelContent(message,true); // NOI18N
        }
    }
    
    void setListPanelContent( String message ,boolean waitIcon ) {
        assert SwingUtilities.isEventDispatchThread();
        oldMessage = message;
        if (message == null) {            
            if (!containsScrollPane) {
               listPanel.remove( messageLabel );
               listPanel.add( matchesScrollPane1 );
               containsScrollPane = true;
               revalidate();
               repaint();
            }
        } else {
           jTextFieldLocation.setText(""); 
           //handling http://netbeans.org/bugzilla/show_bug.cgi?id=178555
            messageLabel.setText(waitIcon
                    ? "<html>" + message + "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://www.netbeans.org\">"+"Cancell search "+"</a></html>" //NOI18N
                    : message);
           messageLabel.setIcon( waitIcon ? WAIT_ICON : null);
           if ( containsScrollPane ) {
               listPanel.remove( matchesScrollPane1 );
               listPanel.add( messageLabel );
               containsScrollPane = false;
           }
           revalidate();
           repaint();
       }                
    }
    
    private String listActionFor(KeyEvent ev) {
        InputMap map = matchesList.getInputMap();
        Object o = map.get(KeyStroke.getKeyStrokeForEvent(ev));
        if (o instanceof String) {
            return (String)o;
        } else {
            return null;
        }
    }

    private boolean boundScrollingKey(KeyEvent ev) {
        String action = listActionFor(ev);
        // See BasicListUI, MetalLookAndFeel:
        return "selectPreviousRow".equals(action) || // NOI18N
        "selectNextRow".equals(action) || // NOI18N
        "selectPreviousRowExtendSelection".equals(action) ||    //NOI18N
        "selectNextRowExtendSelection".equals(action) || //NOI18N
        "scrollUp".equals(action) || // NOI18N
        "scrollDown".equals(action); // NOI18N
    }

    private void delegateScrollingKey(KeyEvent ev) {
        String action = listActionFor(ev);
        
        // Wrap around
        if ( "selectNextRow".equals(action) && 
            matchesList.getSelectedIndex() == matchesList.getModel().getSize() -1 ) {
            matchesList.setSelectedIndex(0);
            matchesList.ensureIndexIsVisible(0);
            return;
        }
        else if ( "selectPreviousRow".equals(action) &&
                  matchesList.getSelectedIndex() == 0 ) {
            int last = matchesList.getModel().getSize() - 1;
            matchesList.setSelectedIndex(last);
            matchesList.ensureIndexIsVisible(last);
            return;
        }        
        // Plain delegation        
        Action a = matchesList.getActionMap().get(action);
        if (a != null) {
            a.actionPerformed(new ActionEvent(matchesList, 0, action));
        }
    }
    
    private static class PatternListener implements DocumentListener, ItemListener, ListSelectionListener {
               
        private final GoToPanel dialog;
        
        
        PatternListener( GoToPanel dialog ) {
            this.dialog = dialog;
        }
        
        PatternListener( DocumentEvent e, GoToPanel dialog ) {
            this.dialog = dialog;
        }
        
        // DocumentListener ----------------------------------------------------
        
        public void changedUpdate( DocumentEvent e ) {            
            update();
        }

        public void removeUpdate( DocumentEvent e ) {
            // handling http://netbeans.org/bugzilla/show_bug.cgi?id=203512
            if (dialog.pastedFromClipboard) {
                dialog.pastedFromClipboard = false;
                dialog.textId++;
            } else {
                update();
            }
        }

        public void insertUpdate( DocumentEvent e ) {
            update();
        }
        
        // Item Listener -------------------------------------------------------
        
        public void itemStateChanged (final ItemEvent e) {
            UiOptions.GoToTypeDialog.setCaseSensitive(dialog.isCaseSensitive());
            update();
        }
        
        // ListSelectionListener -----------------------------------------------
        
        @Override
        public void valueChanged(@NonNull final ListSelectionEvent ev) {
            // got "Not computed yet" text sometimes
            final Object obj = dialog.matchesList.getSelectedValue();
            if (obj instanceof EJBTypeDescriptor) {
                final EJBTypeDescriptor selectedValue = (EJBTypeDescriptor) obj;
                final String fileName = selectedValue.getFileDisplayPath();             
                dialog.ejbInfoPanel.setEJBName(selectedValue.getSimpleName());
                dialog.ejbInfoPanel.setEJBVersion(selectedValue.getEjbVersion()+".x");
                dialog.ejbInfoPanel.setJavaFileName(selectedValue.getFileObject().getName()+".java");
                dialog.ejbInfoPanel.setEJBMappedName(selectedValue.getMappedName());
                dialog.ejbInfoPanel.setEJBDescription(selectedValue.getDescription());
                
                dialog.jTextFieldLocation.setText(fileName);
                
                
            } else {
                dialog.jTextFieldLocation.setText("");      //NOI18N
            }
        }
        
        private void update() {
            dialog.time = System.currentTimeMillis();
            dialog.updateMessage("Searching...");
            String text = dialog.getText();            
            dialog.oldText = text;
            dialog.textId++;
            dialog.contentProvider.setListModel(dialog,text);            
        }
                                         
    }
             
        
    public static interface ContentProvider {

        @NonNull
        public ListCellRenderer getListCellRenderer(
                @NonNull JList list,
                @NonNull ButtonModel caseSensitive);
        
        public void setListModel( GoToPanel panel, String text );
        
        public void closeDialog();
        
        public boolean hasValidContent ();
                
    }

}
