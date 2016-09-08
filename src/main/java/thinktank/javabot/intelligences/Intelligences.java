package thinktank.javabot.intelligences;

import java.util.ArrayList;

import thinktank.javabot.physics.Tank;

// TODO: Auto-generated Javadoc
/**
 * Classe de gestions des intelligences artificielles.
 * 
 * @author cedric
 */
public class Intelligences {

	/** The running intelligences. */
	private int runningIntelligences = 0;

	/** The intelligences. */
	private ArrayList<Intelligence> intelligences = new ArrayList<Intelligence>();

	/**
	 * Incr�mente le compteur (s�maphore) d'intelligences s'ex�cutants.
	 */
	synchronized void addRunningIntelligence() {
		this.runningIntelligences++;
	}

	/**
	 * D�cr�mente le compteur (s�maphore) d'intelligences s'ex�cutants.
	 */
	synchronized void removeRunningIntelligence() {
		this.runningIntelligences--;
		if (runningIntelligences <= 0)
			this.notifyAll();
	}

	/**
	 * Renvoi le nombre d'ia qui sont toujours en train de calculer (non
	 * endormies).
	 * 
	 * @return nombre d'intelligences s'ex�cutants.
	 */
	public synchronized int runningIntelligences() {
		return runningIntelligences;
	}

	/**
	 * Cr�e une nouvelle intelligence et l'ajoute à la liste.
	 * 
	 * @param filepath
	 *            Chemin de fichier vers le script (python) de l'IA utilisateur.
	 * @return intelligence
	 * @deprecated Utilisez {@link #newIntelligence(String, Tank)} Ã  la
	 *             place.
	 */
	@Deprecated
	public Intelligence newIntelligence(String filepath) {
		Intelligence ia = new Intelligence(filepath, this);
		intelligences.add(ia);
		return ia;
	}

	/**
	 * New intelligence.
	 * 
	 * @param filepath
	 *            the filepath
	 * @param tankPhy
	 *            the tank phy
	 * @return the intelligence
	 */
	public Intelligence newIntelligence(String filepath, Tank tankPhy) {
		Intelligence ia = new Intelligence(filepath, this, tankPhy);
		intelligences.add(ia);
		return ia;
	}

	/**
	 * R�veille toutes les IA pour calculer leurs prochaines actions. La
	 * fonction est non bloquante.
	 */
	public synchronized void computeAllActions() {
		for (Intelligence ia : intelligences)
			ia.computeAction();
	}

	/**
	 * Attend la fin du calcul de toutes les IA. à ex�cuter APRES un
	 * computeAllActions().
	 * 
	 * @deprecated La fonction est BLOQUANTE. Ne l'utilisez QUE POUR DES TESTS.
	 */
	@Deprecated
	public synchronized void waitForAllActions() {

		while (runningIntelligences() > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Attend la fin du calcul de toutes les IA. à ex�cuter APRES un
	 * computeAllActions(). La fonction est SEMI-BLOQUANTE (elle dispose d'un
	 * timeout en millisecondes).
	 * 
	 * @param timeout
	 *            Timeout en ms.
	 * @deprecated Ne fonctionne pas correctement: l'intelligence peut ne pas se
	 *             r�veiller. NE PAS UTILISER! Ins�rer un sleep à la place.
	 */
	@Deprecated
	public synchronized void waitForAllActions(long timeout) {
		try {
			wait(timeout); // Note: il faudrait mettre ce wait dans une boucle
							// avec condition, mais comment savoir si le wait
							// termine à cause d'un timeout ou juste d'un notify
							// ?
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
