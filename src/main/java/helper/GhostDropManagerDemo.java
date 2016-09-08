package helper;

import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import thinktank.javabot.graphics.MainWindow;

public class GhostDropManagerDemo extends AbstractGhostDropManager {
    private JComponent target;

    public GhostDropManagerDemo(JComponent target) {   	
        super(target);
        this.target=target;
    }

	public void ghostDropped(GhostDropEvent e) {
//		String TankChoice;
//	   String action = e.getAction();
//	   Point p = getTranslatedPoint(e.getDropLocation());
//	   p.setLocation(p.getX()+target.getBounds().getX(), p.getY()+target.getBounds().getY());
//	   System.out.println(p);
//	   System.out.println(target.getBounds());
//	   if (isInTarget(p)) {
//	       JOptionPane.showMessageDialog(this.component, "Action: " + action);
//	       TankChoice="ressources/"+action+".jpg";
//	       //MainWindow.getInterface().
//	   }
	}
}