
package cz.kec.nb.ejbutils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;


@ActionID(
        category = "J2EE",
        id = "cz.kec.nb.ejb.GoToEJB"
)
/*SystemFileSystem.icon*/
/*nbresloc:/org/netbeans/modules/j2ee/ejbcore/ui/logicalview/ejb/session/SessionNodeIcon.gif*/
@ActionRegistration(
        displayName = "Go to Enterprise JavaBean",iconInMenu = true,iconBase = "cz/kec/nb/ejbutils/Ejb3.png"
)
@ActionReferences({
    @ActionReference(path = "Menu/GoTo", position = 200),
    @ActionReference(path = "Shortcuts", name = "DS-E")
})
//@Messages("CTL_GoToEJB=Go to Enterprise JavaBean")
public final class GoToEJB implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
         GoToTypeAction goToTypeAction = new GoToTypeAction("Go to Enterprise JavaBean", null,false, new EJBTypeProvider());
         goToTypeAction.actionPerformed(e);
    }
    
}
