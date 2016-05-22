package thinktank.javabot.intelligences;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.python.antlr.PythonParser.break_stmt_return;
import org.python.util.PythonInterpreter;

import thinktank.javabot.graphics.GraphicInterface;
import thinktank.javabot.graphics.PanneauDessin;
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

	private Script scripts;

	private PythonInterpreter interp;

	private String nomFichier = null;

	private String nomScript = null;

	public static Color color = Color.BLACK; // couleur par defaut

	public Document doc = GraphicInterface.textAreaOutput.getStyledDocument();	




	public static Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}


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
		return scripts;
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
		scripts = new Script(filepath, this);

	}

	Intelligence(String filepath, Intelligences intelligences, Tank tankPhy,
			Script script) {
		this.tankR = new TankRemote(this, tankPhy);
		this.setFilepath(filepath);
		this.intelligences = intelligences;
		this.scripts = script;
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
		interp.exec("import sys");
		interp.exec("import inspect");
		interp.exec("def lineno():\n\treturn inspect.currentframe().f_back.f_lineno");
		interp.set("tank", tankR);
		tankR.bePrepared();
	}

	public void execInterpreter() {
		System.out.println("path    :"+filepath.toString());
		interp.execfile("src/ressources/" + scripts.getTmpFileName());
		setAction(Action.scriptCompleted);
	}
	/**
	 * update Output Area
	 * cette fonction fait appel � la fonction 
	 * updateOutputArea(Style style) de la classe GraphicInterface
	 */
	public static void updateOutputArea()
	{
		Style style = GraphicInterface.textAreaOutput.addStyle("Color1", null);
		StyleConstants.setForeground(style, getColor());
		GraphicInterface.updateOutputArea(style);
	}


	/**
	 * Thread dédié à l'interpréteur python pour l'IA utilisateur.
	 */
	public synchronized void  run() {
		this.setRunning();
		initInterpreter();
		setInitialized();
		// changer la couleur du Thread avec celle du Tank
		this.setColor(this.tankR.tankPhy.color);
		// Creation d'un nouveau style en lui attribuant la couleur du Tank
		Style style = GraphicInterface.textAreaOutput.addStyle("Color", null);
		StyleConstants.setForeground(style, this.getColor());
		try {
			execInterpreter();
		} catch (Exception e) {
			StringTokenizer decouper = new StringTokenizer(filepath);
			while (decouper.hasMoreTokens()) {
				nomFichier = decouper.nextToken("/");
			}
			StringTokenizer decoupermot = new StringTokenizer(nomFichier);
			if (decoupermot.hasMoreTokens()) {
				nomScript = decoupermot.nextToken(".");
			}
			try {
				doc.insertString(doc.getLength(),"Erreur dans le " + nomScript + " : \n", style);
			} catch (BadLocationException e2) {
				e2.printStackTrace();
			}

			String mot;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			//R�cuperer le message d'erreur dans un String
			String exceptionAsString = sw.toString();
			System.out.println(exceptionAsString);
			//D�couper le message d'erreur et le rendre le plus court possible
			StringTokenizer decouperligne = new StringTokenizer(exceptionAsString);
			boolean line = false;
			while (decouperligne.hasMoreTokens()) {
				mot = decouperligne.nextToken();
				if(mot.equals("line") || line == true || mot.equals("SyntaxError:"))
				{
					line = true;
					if(mot.equals("at"))
						break;
					else
					{
						try {
							doc.insertString(doc.getLength(),mot + " ", style);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}

					}
				}
			}		
			try {
				// Retour � la ligne � la fin de chaque message d'erreur
				doc.insertString(doc.getLength(),"\n",style);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(25);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			//Ajouter le script du tank dans la liste des scripts bugg�s
			PanneauDessin.tankBugge(scripts.getTmpFileName());
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
	SimpleAttributeSet aset = new SimpleAttributeSet();


	public PythonInterpreter getInterp() {
		return interp;
	}

	public void setInterp(PythonInterpreter interp) {
		this.interp = interp;
	}

	public void setScript(Script script) {
		this.scripts = script;
	}


}