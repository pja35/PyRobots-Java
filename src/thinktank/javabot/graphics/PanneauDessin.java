package thinktank.javabot.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import thinktank.javabot.intelligences.Action;
import thinktank.javabot.intelligences.Intelligence;
import thinktank.javabot.physics.ObjetTT;
import thinktank.javabot.physics.Physique;
import thinktank.javabot.physics.Tank;

@SuppressWarnings("serial")
public class PanneauDessin extends JPanel implements MouseListener {

	public static int tailleCase = 20;
	public static int pixelsavantx=0;
	public static int pixelsavanty=0;
	Physique physique;
	ObjetTT contenu;
	SpriteSet sp = new SpriteSet();
	Sprite mur = new Sprite("src/ressources/wall1.png");
	Sprite sol = new Sprite("src/ressources/sand3.png");
	Sprite projectile = new Sprite("src/ressources/shot.png");
	Point lastRegisteredMousePosition = new Point();
	AffineTransformOp op;
	private int jauney;
	private int jaunex;
	public static int lx = 30;
	public static int ly = 22;
	public static ArrayList<String> script = new ArrayList<>();
	private static Image img = new ImageIcon("src/ressources/warning.png").getImage();
	private JPopupMenu menu=new JPopupMenu("Menu");

	public PanneauDessin(Physique physique) {
		super();
		this.physique = physique;
		this.addMouseListener(this);
		/*
		 * Cr�ation des divers MenuItem et leurs action pour les ajouter au JPopupMenu
		 */
		 JMenuItem item = new JMenuItem("Supprimer le tank");
		    item.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        MainWindow.getPanneauDessin().getPhysique().destroyTank(MainWindow.getInterface().getSelectedTank());
		        MainWindow.getInterface().setSelectedTank(null);
		      }
		    });
		  menu.add(item);
		  JMenuItem item1 = new JMenuItem("Orienter vers la gauche");
		    item1.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  if((MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)){
		    		  System.out.println("Here");
		  			GraphicInterface.getSelectedTank().getDirection().setDx(-1);
		  			GraphicInterface.getSelectedTank().getDirection().setDy(0);
		  	}
		      }
		    });
		   menu.add(item1);
		   JMenuItem item2 = new JMenuItem("Orienter vers la droite");
		    item2.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  if((GraphicInterface.stoped==1 || GraphicInterface.stoped==2)){
		    		  GraphicInterface.getSelectedTank().getDirection().setDx(1);
		          	  GraphicInterface.getSelectedTank().getDirection().setDy(0);
		  	}
		      }
		    });
		   menu.add(item2);
		   JMenuItem item3 = new JMenuItem("Orienter vers le haut");
		    item3.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  if((GraphicInterface.stoped==1 || GraphicInterface.stoped==2)){
		    		  GraphicInterface.getSelectedTank().getDirection().setDx(0);
		          	  GraphicInterface.getSelectedTank().getDirection().setDy(-1);
		  	}
		      }
		    });
		   menu.add(item3);
		   JMenuItem item4 = new JMenuItem("Orienter vers le bas");
		    item4.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  if((GraphicInterface.stoped==1 || GraphicInterface.stoped==2)){
		    		  GraphicInterface.getSelectedTank().getDirection().setDx(0);
		          	  GraphicInterface.getSelectedTank().getDirection().setDy(1);
		  	}
		      }
		    });
		   menu.add(item4);
	}

	public int getTailleCase() {
		return tailleCase;
	}

	public void setJauneXY(int x, int y) {

		this.jaunex = x;
		this.jauney = y;

	}

	private void paintLifeStick(Graphics g, int x, int y, int vie) {
		g.setColor(Color.CYAN);
		g.fillRect(x, y - 10, 24 * vie / 100, 5);
		g.setColor(Color.WHITE);
		g.drawRect(x, y - 10, 24, 5);
	}
	public static void paintTriangle(Graphics g, int x, int y) {
		g.drawImage(img, x + 14 , y + 14, null);

	}

	public static void paintSelectedArea(Graphics g, int x, int y) {
		g.setColor(Color.blue);
		g.drawRect(x - 15, y - 15, 50, 50);
	}

	private AffineTransformOp computeRotation(Image image, Action action,
			int avancement) {
		double rotationRequired;
		if (action == Action.turnClockwise) {
			rotationRequired = Math.toRadians(-90
					+ ((100 - avancement) / 100.0) * 90);
		} else if (action == Action.turnCounterClockwise) {
			rotationRequired = Math
					.toRadians(90 - ((100 - avancement) / 100.0) * 90);
		} else {
			rotationRequired = Math.toRadians(0);
		}

		double locationX = image.getWidth(null) / 2;
		double locationY = image.getHeight(null) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(
				rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BILINEAR);
		return op;
	}

	private int getPositionXFluide(Tank contenu, int x, int ni) {

		int posx = x;

		if (((Tank) contenu).getDeplacementStatus() == Action.moveForward
				|| ((Tank) contenu).getDeplacementStatus() == Action.moveBackward) {
			switch (ni) {

			case 2: // GAUCHE
				posx = (int) (x + (((Tank) contenu).getAvancement() / 100.0)
						* tailleCase);

				break;

			case 3: // DROITE

				posx = (int) (x - (((Tank) contenu).getAvancement() / 100.0)
						* tailleCase);

				break;

			default: // BAS ET HAUT
				posx = x;
				break;
			}
		}
		return posx;
	}

	private int getPositionYFluide(Tank contenu, int y, int ni) {

		int posy = y;
		if (((Tank) contenu).getDeplacementStatus() == Action.moveForward
				|| ((Tank) contenu).getDeplacementStatus() == Action.moveBackward) {
			switch (ni) {
			case 0: // HAUT

				posy = (int) (y + (((Tank) contenu).getAvancement() / 100.0)
						* tailleCase);

				break;

			case 1: // BAS

				posy = (int) (y - (((Tank) contenu).getAvancement() / 100.0)
						* tailleCase);

				break;

			default: // GAUCHE ET DROITE

				posy = y;

				break;

			}
		}
		return posy;
	}

	/**
	 * Fonctions de dessins pour les divers éléments du jeu
	 * 
	 * @param g
	 *            Outil de dessin permettant de définir l'affichage à l'écran
	 **/
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, jaunex, jauney);
		setBorders(g);

		for (int i = 0; i < lx; i++) {
			for (int j = 0; j < ly; j++) {

				int x = i * tailleCase;
				int y = j * tailleCase;
							
				/*if(GraphicInterface.jaune.getWidth()>PanneauDessin.lx*PanneauDessin.tailleCase || GraphicInterface.jaune.getHeight()>PanneauDessin.ly*PanneauDessin.tailleCase){
					pixelsavantx=(GraphicInterface.jaune.getWidth()-PanneauDessin.lx*PanneauDessin.tailleCase)/2;
					pixelsavanty=(GraphicInterface.jaune.getHeight()-PanneauDessin.ly*PanneauDessin.tailleCase)/2;
					y+=pixelsavanty;
					x+=pixelsavantx;				
				}*/

				contenu = physique.detail(i, j);

				if (contenu.getType() == Physique.type.vide) {
					// g.drawImage(sol.getImg(),x,y,tailleCase,tailleCase,null);

				} else if (contenu.getType() == Physique.type.mur) {
					g.drawImage(mur.getImg(), x, y, tailleCase, tailleCase,
							null);
				} else if (contenu.getType() == Physique.type.tank) {
					int dy = ((Tank) contenu).getDirection().getDy();
					int dx = ((Tank) contenu).getDirection().getDx();
					int ni = 0; 

					if (dy > 0)
						ni = 1;
					else if (dx != 0)
						ni = dx < 0 ? 2 : 3;

					int vie = Integer
							.valueOf(((Tank) contenu).getPointsDeVie());

					try {

						op = computeRotation(
								sp.getImg(ni, ((Tank) contenu).tc),
								(((Tank) contenu).getDeplacementStatus()),
								(((Tank) contenu).getAvancement()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int posx = getPositionXFluide((Tank) contenu, x, ni);
					int posy = getPositionYFluide((Tank) contenu, y, ni);
					try {
						g.drawImage(op.filter(
								sp.getImg(ni, ((Tank) contenu).tc), null),
								posx, posy, /* tailleCase, tailleCase, */null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					paintLifeStick(g, posx, posy, vie);
					ArrayList<Tank> Tanks = MainWindow.phy.getTanks();
					for (Tank t : Tanks)
					{
						for (String fichier : script)
						{
							if(t.ia.getScript().getTmpFileName().equals(fichier))
							{
								int dyy = t.getDirection().getDy();
								int dxx = t.getDirection().getDx();
								int nii = 0;
								if (dyy > 0)
									nii = 1;
								else if (dxx != 0)
									nii = dxx < 0 ? 2 : 3;
								int posXX = getPositionXFluide(t, t.getCoordX() * tailleCase, nii);
								int posYY = getPositionYFluide(t, t.getCoordY() * tailleCase, nii);
								System.out.println("TANK POSX: " + posXX + " POSY: " + posYY);
								System.out.println("coord " + posXX + " " + posYY);
								paintTriangle(g, posXX, posYY);
							}
						}
					}

					if (GraphicInterface.getSelectedTank() == (Tank) contenu) {
						if(GraphicInterface.stoped==1||GraphicInterface.stoped==2){
						GraphicInterface.slcTank=null;
						paintSelectedArea(g, posx, posy);
						GraphicInterface.SetBorder();}
					}
				} else if (contenu.getType() == Physique.type.projectile) {
					g.drawImage(projectile.getImg(), x, y, tailleCase,
							tailleCase, null);
				}
				
			}
			
			if (GraphicInterface.TankChoice != "")
			{
				try {
					g.drawImage(sp.getImg(1,GraphicInterface.TankChoice), (int)(lastRegisteredMousePosition.getX())/tailleCase * tailleCase,(int)(lastRegisteredMousePosition.getY())/tailleCase * tailleCase,  null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
//		if(e.getClickCount()==2 && (MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)){
//			MainWindow.getInterface().getSelectedTank().getDirection().tournerDroite();
//			System.out.println(MainWindow.getInterface().getSelectedTank().getCoordX()*tailleCase+" "+MainWindow.getInterface().getSelectedTank().getCoordY()*tailleCase);
//	}
		if (e.getButton() == 1) {
			System.out.println("x: " + e.getX() + "y: " + e.getY());
			for (Tank t : physique.getTanks()) {
				int dy = t.getDirection().getDy();
				int dx = t.getDirection().getDx();
				int ni = 0;
				if (dy > 0)
					ni = 1;
				else if (dx != 0)
					ni = dx < 0 ? 2 : 3;
				int posX = getPositionXFluide(t, t.getCoordX() * tailleCase, ni);
				int posY = getPositionYFluide(t, t.getCoordY() * tailleCase, ni);
				System.out.println("TANK POSX: " + posX + " POSY: " + posY);
				if (e.getX() >= posX - tailleCase
						&& e.getX() <= posX + tailleCase
						&& e.getY() <= posY + tailleCase
						&& e.getY() >= posY - tailleCase) {
					GraphicInterface.slcTank=null;
					GraphicInterface.setSelectedTank(t);
					if (t != null) {
						GraphicInterface.textAreaCode.setText(t.getIntel().getScript()
								.getInstructions());

						}

					return;
				}

			}
			GraphicInterface.checkCodeUpdates();
			GraphicInterface.setSelectedTank(null);
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// System.out.println("x: "+e.getX()+"y: "+e.getY());

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("x: "+e.getX()+"y: "+e.getY());

	}

	@Override
	/*
	 * Evenement click droit sur le panneau de dessin
	 * l'evement permet d'afficher un menu contextuel si l'objet cliqu� est un tank
	 * le menu permet entre autre de supprimer le tank du panneau de jeu
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			for (Tank t : physique.getTanks()) {
				int dy = t.getDirection().getDy();
				int dx = t.getDirection().getDx();
				int ni = 0;
				if (dy > 0)
					ni = 1;
				else if (dx != 0)
					ni = dx < 0 ? 2 : 3;
				int posX = getPositionXFluide(t, t.getCoordX() * tailleCase, ni);
				int posY = getPositionYFluide(t, t.getCoordY() * tailleCase, ni);
				System.out.println("TANK POSX: " + posX + " POSY: " + posY);
				if (e.getX() >= posX - tailleCase
						&& e.getX() <= posX + tailleCase
						&& e.getY() <= posY + tailleCase
						&& e.getY() >= posY - tailleCase) {

					GraphicInterface.setSelectedTank(t);
					if (t != null && (MainWindow.getInterface().stoped==1 || MainWindow.getInterface().stoped==2)) {
				          menu.show(e.getComponent(), e.getX(), e.getY());
				          GraphicInterface.textAreaCode.setText(t.getIntel().getScript()
									.getInstructions());
				          

						}

					return;
				}

			}
       }
	}

	public Physique getPhysique() {
		return physique;
	}

	public void set(Graphics g, int i) {
		g.drawImage(mur.getImg(), i, 5, 25, 25, null);

	}

	public void setTaillecase(int i) {
		// TODO Auto-generated method stub

	}
	public static void tankBugge(String nomScript) {
		// TODO Auto-generated method stub
		script.add(nomScript);

	}
	
	public void setBorders(Graphics g)
	{
		int x;
		int y;
		for (Tank tank : physique.getTanks()) {
			if(GraphicInterface.slcTank!=null){
			if(tank.filep.equals(GraphicInterface.slcTank.getText()))
			{
				x=tank.getCoordX()*tailleCase;
				y=tank.getCoordY()*tailleCase;
				paintSelectedArea(g, x, y);
			}
		}
			}
	}

}
