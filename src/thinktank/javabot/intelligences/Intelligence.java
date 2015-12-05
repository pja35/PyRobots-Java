package thinktank.javabot.intelligences;

import java.io.IOException;

import org.python.util.PythonInterpreter;

import thinktank.javabot.graphics.GraphicInterface;
import thinktank.javabot.physics.Tank;

// TODO: Auto-generated Javadoc
/**
 * Classe exécutant sur un thread dédié, le script ia d'un utilisateur.
 * 
 * @author cedric
 * 
 */

public class Intelligence extends Thread {

	/** The tank r. */
	private TankRemote tankR;

	/** The filepath. */
	private String filepath;

	/** The action. */
	private Action action;

	/** The intelligences. */
	private Intelligences intelligences;

	/** The is initialized. */
	private boolean isInitialized = false; // Verrou d'initialisation.

	private Script script;

	private PythonInterpreter interp;

	/**
	 * Reprend le script python pour calculer une nouvelle action Ã  effectuer.
	 * (Fonction non bloquante.)
	 * */
	public void computeAction() {
		if (action == Action.scriptCompleted
				|| action == Action.scriptTerminated) {
			return;
		}
		this.setRunning();

		this.action = Action.noAction;
		tankR.unlock();
	}

	public Intelligences getIntels() {
		return intelligences;
	}

	public TankRemote getTankR() {
		return tankR;
	}

	/**
	 * Attend la réponse du script. (Fonction bloquante).
	 * 
	 * @return the action
	 * @deprecated
	 */
	@Deprecated
	public synchronized Action waitForAction() {
		while (this.action == Action.noAction) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		return action;
	}

	/**
	 * 
	 * Retourne l'action calculée par le script avec computeAction. Il faut
	 * penser  à laisser le temps au script de calculer (delay + timeout).
	 * 
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	public Script getScript() {
		return script;
	}

	/**
	 * Fonction interne du package. Permet au script python, via un objet
	 * TankRemote, de stoquer l'action à effectuer.
	 * 
	 * @param action
	 *            the new action
	 */
	synchronized void setAction(Action action) {
		this.action = action;
		notifyAll();
	}

	/**
	 * Constructeur. Demande le chemin du script utilisateur.
	 * 
	 * @param filepath
	 *            Chemin vers le script utilisateur (python/jython) codant l'IA
	 *            du tank.
	 * @param intelligences
	 *            Liste d'intlligences.
	 * @deprecated Utilisez {@Link #Intelligence(String, Intelligences,
	 *             Tank)} à la place.
	 * */
	@Deprecated
	Intelligence(String filepath, Intelligences intelligences) {
		this.tankR = new TankRemote(this, null);
		this.setFilepath(filepath);
		this.intelligences = intelligences;
	}

	/**
	 * Constructeur. Demande le chemin du script utilisateur.
	 * 
	 * @param filepath
	 *            Chemin vers le script utilisateur (python/jython) codant l'IA
	 *            du tank.
	 * @param intelligences
	 *            Liste d'intÃ©lligences.
	 * @param tankPhy
	 *            Tank physique, utilisé pour l'accés aux différents capteurs
	 *            par les scripts d'IA utilisateurs.
	 * */
	Intelligence(String filepath, Intelligences intelligences, Tank tankPhy) {
		this.tankR = new TankRemote(this, tankPhy);
		this.setFilepath(filepath);
		this.intelligences = intelligences;
		script = new Script(filepath, this);

	}

	Intelligence(String filepath, Intelligences intelligences, Tank tankPhy,
			Script script) {
		this.tankR = new TankRemote(this, tankPhy);
		this.setFilepath(filepath);
		this.intelligences = intelligences;
		this.script = script;
		System.out.println(script.getInstructions());
	}

	/**
	 * Leve le vérrou d'initialisation.
	 */
	synchronized void setInitialized() {
		isInitialized = true;
		notifyAll();
	}

	/**
	 * Initialise l'IA. IL FAUT effectuer obligatoirement l'initialisation avant
	 * d'utiliser les accesseurs d'actions.
	 */
	public synchronized void initialize() {
		this.start();

		while (!isInitialized) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Sets the running.
	 */
	void setRunning() {
		this.intelligences.addRunningIntelligence();
	}

	/**
	 * No more running.
	 */
	void noMoreRunning() {
		this.intelligences.removeRunningIntelligence();
	}

	/**
	 * Termine un script utilisateur. Ne marche que lorsque le script est en
	 * pause. Cela est du au fait que l'interprÃšte Jython cache les appels Ã 
	 * interrupt. A part bidouillages, il est impossible de terminer un script
	 * coincÃ© dans une boucle infinie.
	 * 
	 * @deprecated NE MARCHE PAS!!
	 */
	@Deprecated
	public void terminate() {
		this.interrupt();
		this.setAction(Action.scriptTerminated);
	}

	public void initInterpreter() {
		interp = new PythonInterpreter();

		interp.setOut(GraphicInterface.outPut);
		// interp.setOut(System.out);
		interp.exec("import sys");
		interp.exec("import inspect");
		interp.exec("def lineno():\n\treturn inspect.currentframe().f_back.f_lineno");
		// interp.exec("print sys");
		interp.set("tank", tankR);
		tankR.bePrepared();
	}

	public void execInterpreter() {
		interp.execfile("src/ressources/" + script.getTmpFileName());
		setAction(Action.scriptCompleted);
	}

	/**
	 * Thread dédié à l'interpréteur python pour l'IA utilisateur.
	 */
	public void run() {
		this.setRunning();
		initInterpreter();
		setInitialized();

		try {
			execInterpreter();
		} catch (Exception e) {
			try {
				GraphicInterface.outPut.write("Erreur dans le script\n");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.noMoreRunning();

		interp.close();
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}