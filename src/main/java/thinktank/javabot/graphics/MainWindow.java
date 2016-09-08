
package thinktank.javabot.graphics;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import helper.GhostGlassPane;
import thinktank.javabot.intelligences.Intelligence;
import thinktank.javabot.intelligences.Intelligences;
import thinktank.javabot.physics.Physique;
import thinktank.javabot.physics.Terrain;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	public static PanneauDessin game;

	// private static JFileChooser chooser = new JFileChooser();

	public static MainWindow window = new MainWindow();

	/* Coordonn√©es saisies par le user */
	// private static int setX;
	// private static int setY;

	public static GhostGlassPane container;/* = new JPanel(); */
	// private JPanel c2 = new JPanel();

	public static Physique phy;

	public static GhostGlassPane getContainer() {
		return container;
	}

	public static MainWindow getMainWindow() {

		return window;

	}

	public static GraphicInterface NewGame;

	/**
	 * Affichage principal de l'application
	 **/

	public MainWindow() {

		phy = new Physique(30, 22);
		game = new PanneauDessin(phy);

		this.setTitle("JavaBot");
		this.setSize(740, 560);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		container = new GhostGlassPane();

		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		container.add(game);

		this.setContentPane(container);
		this.setResizable(true);
		this.setVisible(false);
		//game.repaint();

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		NewGame = new GraphicInterface();

		// NewGame.lancement();

		// NewGame.jPanel5.add(container);

		// NewGame.setResizable(false);

		// NewGame.jPanel5.add(c2);
		GraphicInterface.jaune.add(container);

		NewGame.setSize(1368, 676);


		NewGame.setResizable(true);
		NewGame.setVisible(true);
		Point p = NewGame.getLocationOnScreen();
		NewGame.persistance();
		System.out.println(p.getX());
		System.out.println(p.getY());
		this.setVisible(false);

	}

	/**
	 * Graphics Fonction main principale
	 **/
	public static void main(String args[]) {
		Random alea = new Random();
		switch (alea.nextInt(4)) {
		case 0 :
			for (int i = 5; i < 10; i++) {
				MainWindow.phy.newMur(i, 5);

			}

			for (int i = 18; i < 25; i++) {
				MainWindow.phy.newMur(i, 15);

			}

			for (int i = 10; i < 15; i++) {
				MainWindow.phy.newMur(8, i);

			}
			break;
		case 1 :
			for (int i = 5; i < 15; i++) {
				MainWindow.phy.newMur(i, 5);

			}

			for (int i = 14; i < 25; i++) {
				MainWindow.phy.newMur(i, 15);

			}

			for (int i = 10; i < 15; i++) {
				MainWindow.phy.newMur(4, i);

			}
			break;
		case 2 :
			for (int i = 20; i < 25; i++) {
				MainWindow.phy.newMur(i, 5);

			}

			for (int i = 5; i < 12; i++) {
				MainWindow.phy.newMur(i, 15);

			}

			for (int i = 10; i < 15; i++) {
				MainWindow.phy.newMur(23, i);

			}
			break;
		case 3 :
			for (int i = 5; i < 10; i++) {
				MainWindow.phy.newMur(i, 12);

			}

			for (int i = 18; i < 25; i++) {
				MainWindow.phy.newMur(i, 5);

			}

			for (int i = 10; i < 18; i++) {
				MainWindow.phy.newMur(18, i);

			}
			break;
		case 4 :
			break;
		}


		while (true) {

			if (GraphicInterface.stoped != 1 || GraphicInterface.NextStepFlag) {
				MainWindow.phy.iter();
				/*
				 * if(GraphicInterface.NextStepFlag)
				 * GraphicInterface.NextStepFlag=false;
				 */
			}
			MainWindow.game.repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public static PanneauDessin getPanneauDessin() {
		return game;
	}

	public static GraphicInterface getInterface() {
		return NewGame;
	}

	private static void eraseAll()
	{
		for(int i=0;i<30;i++)
		{
			for(int j=0;j<22;j++)
			{
				phy.getMap().erase(i, j);
			}
		}
	}
	
	/*
	 * La fonction permet de gÈnÈrer une nouvelle carte du jeu si il n'y a aucun tanks sur le terrain
	 * La fonction pourrait Ítre modifier pour finalement changer de carte de jeu mÍme quand les tanks existent
	 * en prenant en considÈration leurs positions
	 */
	public static void generateNewMap()
	{
		if(phy.getTanks().isEmpty())
		{
			phy.setMap(new Terrain(30, 22));
			Random alea = new Random();
			switch (alea.nextInt(4)) {
			case 0 :
				for (int i = 5; i < 10; i++) {
					MainWindow.phy.newMur(i, 5);

				}

				for (int i = 18; i < 25; i++) {
					MainWindow.phy.newMur(i, 15);

				}

				for (int i = 10; i < 15; i++) {
					MainWindow.phy.newMur(8, i);

				}
				break;
			case 1 :
				for (int i = 5; i < 15; i++) {
					MainWindow.phy.newMur(i, 5);

				}

				for (int i = 14; i < 25; i++) {
					MainWindow.phy.newMur(i, 15);

				}

				for (int i = 10; i < 15; i++) {
					MainWindow.phy.newMur(4, i);

				}
				break;
			case 2 :
				for (int i = 20; i < 25; i++) {
					MainWindow.phy.newMur(i, 5);

				}

				for (int i = 5; i < 12; i++) {
					MainWindow.phy.newMur(i, 15);

				}

				for (int i = 10; i < 15; i++) {
					MainWindow.phy.newMur(23, i);

				}
				break;
			case 3 :
				for (int i = 5; i < 10; i++) {
					MainWindow.phy.newMur(i, 12);

				}

				for (int i = 18; i < 25; i++) {
					MainWindow.phy.newMur(i, 5);

				}

				for (int i = 10; i < 18; i++) {
					MainWindow.phy.newMur(18, i);

				}
				break;
			case 4 :
				break;
			}
			MainWindow.game.repaint();
		}
	}
}