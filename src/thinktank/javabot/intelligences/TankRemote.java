package thinktank.javabot.intelligences;

import org.python.antlr.ast.Slice.Slice___init___exposer;

import thinktank.javabot.graphics.MainWindow;
import thinktank.javabot.physics.Physique;
import thinktank.javabot.physics.Tank;
import thinktank.javabot.sensors.DetectionLigneDroite;

// TODO: Auto-generated Javadoc
/**
 * The Class TankRemote.
 */
public class TankRemote {

	/** The tank phy. */
	Tank tankPhy;

	/** The lock. */
	boolean lock = true; // Verrou de la t�l�commande

	/** The ia. */
	Intelligence ia;

	/**
	 * Instantiates a new tank remote.
	 * 
	 * @param ia
	 *            the ia
	 * @param tankPhy
	 *            the tank phy
	 */
	TankRemote(Intelligence ia, Tank tankPhy) {
		this.ia = ia;
		this.tankPhy = tankPhy;
	}

	/**
	 * V�rrouille la t�l�commande, l'IA associ�e s'endors.
	 */
	public synchronized void lock()

	{
		lock = true;
	}

	/**
	 * d�verouille la t�l�commande, l'IA associ�e est solicit�e pour un calcul
	 * d'action.
	 */
	public synchronized void unlock() {
		lock = false;
		notifyAll();
	}

	/**
	 * Renvoie l'�tat du v�rrou de la t�l�commande.
	 * 
	 * @return true, if is locked
	 */
	public synchronized boolean isLocked() {
		return lock;
	}

	/**
	 * Endors l'IA (et donc le script python) jusqu'� la prochaine demande
	 * d'action.
	 */
	public synchronized void bePrepared() {
		try {
			this.lock();
			ia.noMoreRunning();
			while (isLocked())
				wait();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/* Commandes accessibles pour les scripts IA (jython/python). */

	/**
	 * Do nothing.
	 */
	public void doNothing(int lineno) {
		ia.setAction(Action.noAction);
		ia.getScript().setCurrentLine(lineno);
		bePrepared();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Move forward.
	 */
	public synchronized void moveForward(int lineno) {
		while(MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ia.setAction(Action.moveForward);
		ia.getScript().setCurrentLine(lineno);
		bePrepared();
	}

	/**
	 * Move backward.
	 */
	public synchronized void moveBackward(int lineno) {
		while(MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ia.setAction(Action.moveBackward);
		ia.getScript().setCurrentLine(lineno);
		bePrepared();
	}

	/**
	 * Turn clockwise.
	 */
	public synchronized void turnClockwise(int lineno) {
		while(MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ia.setAction(Action.turnClockwise);
		ia.getScript().setCurrentLine(lineno);
		bePrepared();
		
	}

	/**
	 * Turn counter clockwise.
	 */
	public synchronized void turnCounterClockwise(int lineno) {
		while(MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ia.setAction(Action.turnCounterClockwise);
		ia.getScript().setCurrentLine(lineno);
		bePrepared();
	}

	/**
	 * Shoot.
	 */
	public synchronized void shoot(int lineno) {
		ia.setAction(Action.shoot);
		ia.getScript().setCurrentLine(lineno);
		bePrepared();
	}

	/**
	 * Radar.
	 * 
	 * @return the int
	 */
	public synchronized int radar(int lineno) {
		ia.getScript().setCurrentLine(lineno);
		return 42;
	}

	/**
	 * Look forward.
	 * 
	 * @return the intregex
	 */
	public synchronized int distanceOfForwardObstacle(int lineno) {
		DetectionLigneDroite dld = ((DetectionLigneDroite) tankPhy.getSensor());
		ia.getScript().setCurrentLine(lineno);
		return dld.detection().getDistance();
	}

	public synchronized Physique.type typeOfForwardObstacle(int lineno) {
		DetectionLigneDroite dld = ((DetectionLigneDroite) tankPhy.getSensor());
		ia.getScript().setCurrentLine(lineno);
		return dld.detection().getType();
	}

}