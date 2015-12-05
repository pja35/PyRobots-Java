/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinktank.javabot.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import thinktank.javabot.physics.Tank;

/**
 * 
 * @author Zayd
 */
@SuppressWarnings("serial")
public class GraphicInterface extends javax.swing.JFrame implements
		WindowListener, KeyListener {

	/**
	 * Creates new form MainGameWindow
	 * 
	 * @see MainWindow
	 */
	public static String TankChoice = "";
	public static int xp = 0;
	public static int yp = 0;
	public static JFileChooser chooser = new JFileChooser();
	private static Tank selectedTank;
	public String mode = "Debug";
	/*
	 * Plusieurs valeurs possibles pour stoped: 0: Le jeu est en exécution 1: Le
	 * jeu est arrêté 2: L'arrêt est en cours et sera effectif à la fin des
	 * itérations de fluidité
	 */
	public static int stoped = 1;
	public static boolean NextStepFlag = false;
	public static RSyntaxTextArea textAreaCode;
	public static JTextArea textAreaOutput;
	public static JTextArea textAreaHelp;
	public static Highlighter currentLineExecution;
	public static Writer outPut = new StringWriter();
	// public static JButton btnExport;
	// public static JButton btnImport;
	public JButton btnImport;
	public JLabel TankBBrun;
	public JLabel TankBCyan;
	public JLabel TankBJaune;
	public JLabel TankBRose;
	public JLabel TankBVert;
	private JLabel TankBViolet;
	public JLabel a;
	public GroupLayout gl_rouge;
	private MouseMotionListener mms;
	private MouseListener ms;
	private MouseListener pms;
	public static GraphicInterface gui;
	public static boolean devModeActivated = false;
	public JLabel slcTank, tankPressed;
	private String TankScript;
	private JLabel TankBRed;
	public JPanel[] tankPane;
	public JLabel[] scriptnom;
	public JLabel[] tanklist;
	public boolean dragging;

	// public String pathFile;

	/**
	 * constructeur GraphicInterface
	 */
	public GraphicInterface() {

		initComponents();
		GraphicInterface.gui = this;
		this.addWindowListener(this);
		this.addKeyListener(this);
		System.out.println("");
		this.setTitle("PyBotWar Pro");
		setResizable(true);

	}

	/**
	 * récupère le tank sélectionné
	 * 
	 * @return selectedTank
	 */
	public static Tank getSelectedTank() {
		return selectedTank;
	}

	/**
	 * met à jour le tank sélectionné
	 * 
	 * @param tank
	 */
	public static void setSelectedTank(Tank t) {
		selectedTank = t;

		if (selectedTank == null) {
			// btnImport.setVisible(false);
			// btnExport.setVisible(false);

		} else {

			textAreaCode.setEditable(true);

		}
		GraphicInterface.gui.setFocusable(true);
		GraphicInterface.gui.requestFocusInWindow();
	}

	/**
	 * check Code Updates
	 */
	public static void checkCodeUpdates() {
		/*
		 * On vérifie si il faut mettre à jour la liste d'instructions. Y-a-t-il
		 * eu des modifications sur le script Python ?
		 */
		if (selectedTank != null) {
			if (!textAreaCode.getText().equals(
					selectedTank.getIntel().getScript().getInstructions())) {
				System.out.println("maj");
				selectedTank.getIntel().getScript()
						.updateInstructions(textAreaCode.getText());
			}

		}
	}

	/**
	 * update Code Area
	 */
	public static void updateCodeArea() {
		if (selectedTank != null) {
			textAreaCode.setText(selectedTank.getIntel().getScript()
					.getInstructions());
		} else {
			// updateListTanksOnMap();
		}
	}

	/**
	 * update Output Area
	 */
	public static void updateOutputArea() {

		textAreaOutput.setText(outPut.toString());
	}

	/**
	 * updateHighlight
	 * 
	 * @param line
	 */
	public static void updateHighlight(int line) {

		currentLineExecution = textAreaCode.getHighlighter();
		currentLineExecution.removeAllHighlights();
		try {
			currentLineExecution.addHighlight(selectedTank.getIntel()
					.getScript().startPositionLine(line), selectedTank
					.getIntel().getScript().endPositionLine(line),
					new DefaultHighlighter.DefaultHighlightPainter(Color.cyan));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialise les compsants de la classe GraphicInterface initComponents
	 * 
	 * @param bleu
	 * @param blanc
	 * @param gris
	 */
	private void initComponents() {
		// sert a rien
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setText("jLabel1");

		panel = new javax.swing.JPanel();

		vert = new JPanel();
		vert.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), "Éditeur de code"));
		jaune = new JPanel();
		jaune.setBackground(Color.YELLOW);
		jaune.setLayout(new GridLayout());

		jaune.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub

				PanneauDessin.tailleCase = jaune.getHeight() / PanneauDessin.ly;
				if (jaune.getWidth() < PanneauDessin.lx
						* PanneauDessin.tailleCase)
					PanneauDessin.tailleCase = jaune.getWidth()
							/ PanneauDessin.lx;

			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		rouge = new JPanel();
		rouge.setLocation(new Point(2, 2));

		bleu = new JPanel();
		blanc = new JPanel();

		tankPane = new JPanel[100];
		scriptnom = new JLabel[100];
		tanklist = new JLabel[100];

		final JPanel gris = new JPanel();
		gris.setMaximumSize(new Dimension(200, 32767));
		gris.setAlignmentX(Component.LEFT_ALIGNMENT);
		//gris.setBackground(Color.gray);

		ComponentListener componentListener;
		componentListener = new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub

				MainWindow.getPanneauDessin().setJauneXY(
						MainWindow.NewGame.jaune.getWidth(),
						MainWindow.NewGame.jaune.getHeight());
				System.out.println(MainWindow.NewGame.getWidth()
						+ "Siiiiiiiiiize" + MainWindow.NewGame.getHeight());
				// MainWindow.getPanneauDessin().revalidate();
				// MainWindow.getPanneauDessin().repaint();
				// MainWindow.getPanneauDessin().tailleCase=0; //
				// System.out.println(MainWindow.NewGame.getHeight());
				/*
				 * //setJauneWidithHeight() if
				 * (MainWindow.NewGame.getWidth()==1301 &&
				 * MainWindow.NewGame.getHeight()==744){
				 * System.out.println("ougha");
				 * MainWindow.getPanneauDessin().tailleCase=28;
				 * MainWindow.getPanneauDessin().revalidate();
				 * MainWindow.getPanneauDessin().repaint(); }
				 */

			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}
		};
		this.addComponentListener(componentListener);

		final JButton btnAdd = new JButton("");
		btnAdd.setRequestFocusEnabled(false);
		btnAdd.setFocusable(false);
		btnAdd.setToolTipText("Ajouter un tank");
		btnAdd.setRolloverEnabled(false);
		btnAdd.setIcon(new ImageIcon("src/ressources/Plus Math-50.png"));
		btnAdd.setContentAreaFilled(false);
		btnAdd.setBorderPainted(false);
		btnAdd.setBorder(null);
		btnAdd.setIconTextGap(0);
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnAdd.setMargin(new Insets(2, 2, 2, 2));

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				GraphicCreateTank a = new GraphicCreateTank(MainWindow
						.getInterface());
			}
		});

		final JButton btnDelete = new JButton("");
		btnDelete.setRequestFocusEnabled(false);
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Enlever un tank");
		btnDelete.setRolloverEnabled(false);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBorder(null);
		btnDelete.setIcon(new ImageIcon("src/ressources/Delete.png"));
		btnDelete.setIconTextGap(0);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDelete.setMargin(new Insets(2, 2, 2, 2));
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				for (int i = 0; i < tanklist.length; i++) {

					if (tankPressed == tanklist[i]) {

						MainWindow.getInterface().rouge.remove(tankPane[i]);
						MainWindow.getInterface().rouge.repaint();
						MainWindow.getInterface().validate();
					}

				}

				ArrayList<Tank> Tanks = MainWindow.phy.getTanks();
				int a = Tanks.size();
				for (int i = 0; i < tanklist.length; i++) {
					if (tankPressed == tanklist[i]) {

						while (a >= 0) {
							a--;
							System.out.println("dsffffffffffffffffff" + a);
							if (Tanks.get(a).filep.equals(scriptnom[i]
									.getText())) {

								MainWindow.phy.destroyTank(Tanks.get(a));

							}

						}
					}

				}

			}
		});

		btnImport = new JButton("");
		btnImport.setToolTipText("Importer un script");
		btnImport.setFocusable(false);
		btnImport.setRequestFocusEnabled(false);
		btnImport.setBorder(null);
		btnImport.setBorderPainted(false);
		btnImport.setRolloverEnabled(false);
		btnImport.setContentAreaFilled(false);
		btnImport.setIcon(new ImageIcon("src/ressources/Add File-50.png"));
		btnImport.setIconTextGap(0);
		btnImport.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnImport.setMargin(new Insets(0, 0, 0, 0));

		final JButton btnJeu = new JButton("");
		btnJeu.setRolloverEnabled(false);
		btnJeu.setRequestFocusEnabled(false);
		btnJeu.setBorderPainted(false);
		btnJeu.setSize(new Dimension(46, 46));
		btnJeu.setPreferredSize(new Dimension(46, 46));
		btnJeu.setMaximumSize(new Dimension(46, 46));
		btnJeu.setIconTextGap(0);
		btnJeu.setBorder(null);
		btnJeu.setContentAreaFilled(false);
		btnJeu.setIcon(new ImageIcon("src/ressources/Controller-50'.png"));
		btnJeu.setToolTipText("Mode Jeu");

		final JButton btnDev = new JButton("");
		btnDev.setRolloverEnabled(false);
		btnDev.setRequestFocusEnabled(false);
		btnDev.setBorderPainted(false);
		btnDev.setBorder(null);
		btnDev.setToolTipText("Mode D\u00E9veloppeur");
		btnDev.setContentAreaFilled(false);
		btnDev.setIconTextGap(0);
		btnDev.setIcon(new ImageIcon("src/ressources/Code-50.png"));

		final JButton btnsave = new JButton("");
		btnsave.setFocusTraversalPolicyProvider(true);
		btnsave.setContentAreaFilled(false);
		btnsave.setBorderPainted(false);
		btnsave.setRequestFocusEnabled(false);
		btnsave.setRolloverEnabled(false);
		btnsave.setToolTipText("Enregistrer");
		btnsave.setIcon(new ImageIcon("src/ressources/save.png"));
		btnsave.setBorder(null);

		btnsave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				File file = new File("src/scripts/" + slcTank.getText() + ".py");
				FileWriter outStream = null;
				try {
					outStream = new FileWriter(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outStream.write(textAreaCode.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		final JButton btnPause = new JButton("");
		btnPause.setToolTipText("Pause");
		btnPause.setRequestFocusEnabled(false);
		btnPause.setRolloverEnabled(false);
		btnPause.setContentAreaFilled(false);
		btnPause.setMargin(new Insets(0, 0, 0, 0));
		btnPause.setIcon(new ImageIcon("src/ressources/Sleep Mode-50.png"));
		btnPause.setIconTextGap(0);
		btnPause.setBorderPainted(false);

		final JButton btnDbg = new JButton("");
		btnDbg.setRolloverEnabled(false);
		btnDbg.setRequestFocusEnabled(false);
		btnDbg.setIcon(new ImageIcon("src/ressources/Bug-50.png"));
		btnDbg.setToolTipText("Mode Debug");
		btnDbg.setSelectedIcon(null);
		btnDbg.setIconTextGap(0);
		btnDbg.setBorderPainted(false);
		btnDbg.setContentAreaFilled(false);

		final JButton btnPlay = new JButton("");
		btnPlay.setContentAreaFilled(false);
		btnPlay.setToolTipText("Play");
		btnPlay.setRolloverEnabled(false);
		btnPlay.setRequestFocusEnabled(false);
		btnPlay.setName("Play");
		btnPlay.setIconTextGap(0);
		btnPlay.setBorder(null);
		btnPlay.setBorderPainted(false);
		btnPlay.setIcon(new ImageIcon("src/ressources/Start-50.png"));
		btnPlay.setMargin(new Insets(0, 0, 0, 0));

		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (stoped == 1)
					stoped = 0; // Demarrage / Redémarrage
				else if (stoped == 2)
					return;
				else
					stoped = 2;

				// btnNextStep.setVisible(!btnNextStep.isVisible());
				if (stoped == 1 || stoped == 2) {
					btnPlay.setIcon(new ImageIcon("src/ressources/Start-50.png"));
					btnPlay.setToolTipText("Jouer");
					textAreaCode.setEditable(true);
				} else {
					// checkCodeUpdates();
					btnPlay.setIcon(new ImageIcon(
							"src/ressources/Sleep Mode-50.png"));
					btnPlay.setToolTipText("Pause");
					textAreaCode.setEditable(false);
				}
				GraphicInterface.gui.setFocusable(true);
				GraphicInterface.gui.requestFocusInWindow();
			}
		});

		btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphicInterface.NextStepFlag = true;
			}
		});

		btnDev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jaune.setVisible(false);
				vert.setVisible(true);
				gris.setVisible(true);
			}
		});

		btnJeu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				jaune.setVisible(true);
				vert.setVisible(false);

			}

		});

		btnDbg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vert.setVisible(true);
				jaune.setVisible(true);
				gris.setVisible(true);

			}
		});

		rouge.setPreferredSize(new Dimension(720, 50));

		mms = new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
			
			

				int x_origin_windowGame = (int) MainWindow.getContainer()
						.getLocationOnScreen().getX();
				int y_origin_windowGame = (int) MainWindow.getContainer()
						.getLocationOnScreen().getY();
				int x_end_windowGame = x_origin_windowGame
						+ MainWindow.getContainer().getWidth();
				int y_end_windowGame = y_origin_windowGame
						+ MainWindow.getContainer().getHeight();

				int x_absolute_position = e.getXOnScreen()
						- x_origin_windowGame;
				int y_absolute_position = e.getYOnScreen()
						- y_origin_windowGame;
				MainWindow.getPanneauDessin().lastRegisteredMousePosition.x = x_absolute_position;
				MainWindow.getPanneauDessin().lastRegisteredMousePosition.y = y_absolute_position;
				/*
				 * xd=e.getX()-400; yd=e.getY()-40;
				 * setSelectedTank(MainWindow.phy.addTank( xd, yd /
				 * MainWindow.getPanneauDessin() .getTailleCase(),""));
				 * e.consume(); repaint(); if(dragging){
				 * 
				 * }
				 */

			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}

		};

		pms = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				System.out.println(e.getSource());
				for (int j = 0; j < scriptnom.length; j++) {

					String k = e.getSource().toString().split("Tank")[1]
							.split(".png")[0].substring(1);

					if (e.getSource() == tanklist[j]) {

						tankPressed = tanklist[j];
						slcTank = scriptnom[j];
						tankPane[j].setBorder(BorderFactory
								.createLineBorder(Color.black));
						textAreaCode.setEditable(true);
						textAreaCode.setFocusable(true);
						TankScript = scriptnom[j].getText();
						textAreaCode.setText("");

						Path wiki_path = Paths.get("src/scripts/"
								+ scriptnom[j].getText() + ".py");

						Charset charset = Charset.forName("ISO-8859-1");
						try {
							List<String> lines = Files.readAllLines(wiki_path,
									charset);
							String script = "";
							for (String line : lines) {
								script += line + "\n";

							}
							textAreaCode.setText(script);
						} catch (IOException e1) {
							System.out.println(e1);
						}

					}

					else {
						tankPane[j].setBorder(null);
					}

				}

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

				for (int j = 0; j < scriptnom.length; j++) {

					if (e.getSource() == tanklist[j]) {
						if (e.getButton() == MouseEvent.BUTTON3) {
							GraphicCreateTank t = new GraphicCreateTank(
									MainWindow.getInterface(), scriptnom[j], j);
						}
					}

				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		};

		ms = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

				int x_origin_windowGame = (int) MainWindow.getContainer()
						.getLocationOnScreen().getX();
				int y_origin_windowGame = (int) MainWindow.getContainer()
						.getLocationOnScreen().getY();
				int x_end_windowGame = x_origin_windowGame
						+ MainWindow.getContainer().getWidth();
				int y_end_windowGame = y_origin_windowGame
						+ MainWindow.getContainer().getHeight();

				if (TankChoice != "") {
					if (e.getXOnScreen() >= x_origin_windowGame
							&& e.getXOnScreen() <= x_end_windowGame
							&& e.getYOnScreen() >= y_origin_windowGame
							&& e.getYOnScreen() <= y_end_windowGame) {
						int x_absolute_position = e.getXOnScreen()
								- x_origin_windowGame;
						int y_absolute_position = e.getYOnScreen()
								- y_origin_windowGame;

						String filePath = TankScript;
						setSelectedTank(MainWindow.phy.addTank(
								x_absolute_position
										/ MainWindow.getPanneauDessin()
												.getTailleCase(),
								y_absolute_position
										/ MainWindow.getPanneauDessin()
												.getTailleCase(), filePath));
						File file = new File("src/scripts/" + filePath + ".py");

						Path wiki_path = Paths.get("src/scripts/" + filePath
								+ ".py");

						Charset charset = Charset.forName("ISO-8859-1");
						try {
							List<String> lines = Files.readAllLines(wiki_path,
									charset);
							String script = "";
							for (String line : lines) {
								script += line + "\n";

							}
							textAreaCode.setText(script);
						} catch (IOException e1) {
							System.out.println(e1);
						}

					}
				}

				TankChoice = "";
				dragging = false;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

				if (stoped == 0)
					return;
				TankChoice = e.getSource().toString().split("Tank")[1]
						.split(".png")[0].substring(1);

				int x_origin_windowGame = (int) MainWindow.getContainer()
						.getLocationOnScreen().getX();
				int y_origin_windowGame = (int) MainWindow.getContainer()
						.getLocationOnScreen().getY();
				int x_end_windowGame = x_origin_windowGame
						+ MainWindow.getContainer().getWidth();
				int y_end_windowGame = y_origin_windowGame
						+ MainWindow.getContainer().getHeight();
				int x_absolute_position = e.getXOnScreen()
						- x_origin_windowGame;
				int y_absolute_position = e.getYOnScreen()
						- y_origin_windowGame;
				MainWindow.getPanneauDessin().lastRegisteredMousePosition.x = x_absolute_position;
				MainWindow.getPanneauDessin().lastRegisteredMousePosition.y = y_absolute_position;

				for (int j = 0; j < scriptnom.length; j++) {

					if (e.getSource() == tanklist[j])
						TankScript = scriptnom[j].getText();

				}
				dragging = true;

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		};

		rouge.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), "Vos Tanks"));

		rouge.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 7));

		textAreaHelp = new JTextArea("noAction()\n" + "moveForward()\n"
				+ "moveBackward()\n" + "turnClockwise()\n"
				+ "turnCounterClockwise()\n" + "shoot()");
		textAreaHelp.setEditable(false);
		textAreaHelp.setBorder(BorderFactory.createEtchedBorder());

		final JButton btnDevMode = new JButton("Mode Dev");
		btnDevMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GraphicInterface.devModeActivated == false) {
					// jSplitPane1.setDividerLocation(1.0d);
					GraphicInterface.devModeActivated = true;
					btnDevMode.setText("Mode Jeu");
					textAreaHelp
							.setText("noAction(): Le Tank est en stand-by.\n"
									+ "moveForward(): Le Tank avance d'une case.\n"
									+ "moveBackward(): Le Tank recule d'une case.\n"
									+ "turnClockwise(): Le Tank tourne d'un quart de tour dans le sens de l'horloge.\n"
									+ "turnCounterClockwise(): Le Tank tourne d'un quart de tour dans le sens inverse de l'horloge.\n"
									+ "shoot(): Le Tank tire un missile.");
				} else {
					// jSplitPane1.setDividerLocation(351);
					GraphicInterface.devModeActivated = false;
					btnDevMode.setText("Mode Dev");
					textAreaHelp.setText("noAction()\n" + "moveForward()\n"
							+ "moveBackward()\n" + "turnClockwise()\n"
							+ "turnCounterClockwise()\n" + "shoot()");
				}
			}
		});

		btnImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					String s = "";
					String filePath = GraphicInterface.chooser
							.getSelectedFile().getAbsolutePath();
					System.out.println(filePath);

					try {
						List<String> lines = Files.readAllLines(
								Paths.get(filePath), Charset.defaultCharset());

						for (String line : lines) {

							s += line + "\n";

						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					textAreaCode.setText(s);

					selectedTank.getIntel().getScript().setFileName(filePath);

				}
			}

		});

		TankBCyan = new JLabel("");
		TankBCyan.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));

		TankBRed = new JLabel("");
		TankBRed.setIcon(new ImageIcon("src/ressources/TankBRed.png"));

		TankBVert = new JLabel("");
		TankBVert.setIcon(new ImageIcon("src/ressources/TankBVert.png"));

		scriptnom[0] = new JLabel("Script1");
		scriptnom[1] = new JLabel("Script2");
		scriptnom[2] = new JLabel("Script3");

		tankPane[0] = new JPanel();
		tankPane[1] = new JPanel();
		tankPane[2] = new JPanel();

		tankPane[0].setLayout(new BoxLayout(tankPane[0], BoxLayout.PAGE_AXIS));
		TankBCyan.setAlignmentX(CENTER_ALIGNMENT);
		scriptnom[0].setAlignmentX(CENTER_ALIGNMENT);
		tankPane[0].add(TankBCyan);
		tankPane[0].add(scriptnom[0]);

		tankPane[1].setLayout(new BoxLayout(tankPane[1], BoxLayout.PAGE_AXIS));
		TankBRed.setAlignmentX(CENTER_ALIGNMENT);
		scriptnom[1].setAlignmentX(CENTER_ALIGNMENT);
		tankPane[1].add(TankBRed);
		tankPane[1].add(scriptnom[1]);

		tankPane[2].setLayout(new BoxLayout(tankPane[2], BoxLayout.PAGE_AXIS));
		TankBVert.setAlignmentX(CENTER_ALIGNMENT);
		scriptnom[2].setAlignmentX(CENTER_ALIGNMENT);
		tankPane[2].add(TankBVert);
		tankPane[2].add(scriptnom[2]);

		tanklist[0] = TankBCyan;
		tanklist[1] = TankBRed;
		tanklist[2] = TankBVert;

		TankBCyan.addMouseListener(ms);
		TankBRed.addMouseListener(ms);
		TankBVert.addMouseListener(ms);

		TankBCyan.addMouseMotionListener(mms);
		TankBRed.addMouseMotionListener(mms);
		TankBVert.addMouseMotionListener(mms);

		TankBCyan.addMouseListener(pms);
		TankBRed.addMouseListener(pms);
		TankBVert.addMouseListener(pms);

		tankPane[0].addMouseListener(pms);
		tankPane[1].addMouseListener(pms);
		tankPane[2].addMouseListener(pms);

		this.rouge.add(tankPane[0]);
		this.rouge.add(tankPane[1]);
		this.rouge.add(tankPane[2]);

		this.rouge.repaint();
		this.rouge.revalidate();

		textAreaCode = new RSyntaxTextArea();
		textAreaCode.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
		// updateListTanksOnMap();

		JScrollPane textAreaCodeScrollPane = new JScrollPane(textAreaCode);
		textAreaCodeScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaCodeScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JLabel lblErrors = new JLabel("Sortie");
		lblErrors.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblHelp = new JLabel("Aide");

		JLabel lblPen = new JLabel("Pensez \u00E0 sauvegarder votre travail");
		lblPen.setAlignmentY(Component.TOP_ALIGNMENT);
		GroupLayout gl_vert = new GroupLayout(vert);
		gl_vert.setHorizontalGroup(gl_vert
				.createParallelGroup(Alignment.TRAILING)
				.addComponent(textAreaHelp, GroupLayout.DEFAULT_SIZE, 260,
						Short.MAX_VALUE)
				.addComponent(gris, Alignment.LEADING,
						GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
				.addGroup(
						gl_vert.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblPen)
								.addPreferredGap(ComponentPlacement.RELATED,
										38, Short.MAX_VALUE)
								.addComponent(btnsave).addContainerGap())
				.addComponent(textAreaCodeScrollPane, Alignment.LEADING,
						GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE));
		gl_vert.setVerticalGroup(gl_vert
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						Alignment.TRAILING,
						gl_vert.createSequentialGroup()
								.addComponent(textAreaCodeScrollPane,
										GroupLayout.DEFAULT_SIZE, 248,
										Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_vert.createParallelGroup(
												Alignment.TRAILING, false)
												.addComponent(btnsave)
												.addComponent(
														lblPen,
														GroupLayout.PREFERRED_SIZE,
														27,
														GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textAreaHelp,
										GroupLayout.PREFERRED_SIZE, 100,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(gris, GroupLayout.PREFERRED_SIZE,
										120, GroupLayout.PREFERRED_SIZE)));

		vert.setLayout(gl_vert);

		textAreaOutput = new JTextArea();

		textAreaOutput.setPreferredSize(new Dimension(506, 110));
		textAreaOutput.setMaximumSize(new Dimension(200, 2147483647));
		textAreaOutput.setEditable(false);

		JScrollPane textAreaOutputScrollPane = new JScrollPane(textAreaOutput);
		textAreaOutputScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout gl_blanc = new GroupLayout(blanc);
		gl_blanc.setHorizontalGroup(gl_blanc.createParallelGroup(
				Alignment.TRAILING)
				.addGroup(
						gl_blanc.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)

								.addComponent(btnPlay,
										GroupLayout.PREFERRED_SIZE, 57,
										GroupLayout.PREFERRED_SIZE)
								.addGap(2)
								.addComponent(btnDev,
										GroupLayout.PREFERRED_SIZE, 57,
										GroupLayout.PREFERRED_SIZE)
								.addGap(2)
								.addComponent(btnJeu,
										GroupLayout.PREFERRED_SIZE, 57,
										GroupLayout.PREFERRED_SIZE)
								.addGap(2)
								.addComponent(btnDbg,
										GroupLayout.PREFERRED_SIZE, 57,
										GroupLayout.PREFERRED_SIZE).addGap(2)
								.addGap(2)));

		gl_blanc.setVerticalGroup(gl_blanc.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_blanc.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 43,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDev, GroupLayout.PREFERRED_SIZE, 43,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnJeu, GroupLayout.PREFERRED_SIZE, 43,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDbg, GroupLayout.PREFERRED_SIZE, 43,
								GroupLayout.PREFERRED_SIZE)));

		blanc.setLayout(gl_blanc);

		GroupLayout gl_gris = new GroupLayout(gris);
		gl_gris.setHorizontalGroup(gl_gris.createParallelGroup(
				Alignment.LEADING).addComponent(textAreaOutputScrollPane,
				GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE));
		gl_gris.setVerticalGroup(gl_gris.createParallelGroup(Alignment.LEADING)
				.addComponent(textAreaOutputScrollPane,
						GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE));
		// textAreaOutputScrollPane.setSize(new Dimension(300,300) );

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.TRAILING)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		rouge,
																		GroupLayout.DEFAULT_SIZE,
																		544,
																		Short.MAX_VALUE)
																.addPreferredGap(
																		ComponentPlacement.RELATED)
																.addComponent(
																		bleu,
																		GroupLayout.PREFERRED_SIZE,
																		182,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		vert,
																		GroupLayout.DEFAULT_SIZE,
																		244,
																		Short.MAX_VALUE)
																.addPreferredGap(
																		ComponentPlacement.RELATED)
																.addComponent(
																		jaune,
																		GroupLayout.DEFAULT_SIZE,
																		482,
																		Short.MAX_VALUE))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		gris,
																		GroupLayout.DEFAULT_SIZE,
																		200,
																		Short.MAX_VALUE)
																.addPreferredGap(
																		ComponentPlacement.RELATED)
																.addComponent(
																		blanc,
																		GroupLayout.PREFERRED_SIZE,
																		400,
																		GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addContainerGap()
																.addComponent(
																		rouge,
																		GroupLayout.PREFERRED_SIZE,
																		73,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGap(19)
																.addComponent(
																		bleu,
																		GroupLayout.PREFERRED_SIZE,
																		54,
																		GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.TRAILING)
												.addComponent(
														vert,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														jaune,
														GroupLayout.DEFAULT_SIZE,
														304, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING, false)
												.addComponent(
														blanc,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														gris,
														GroupLayout.DEFAULT_SIZE,
														45, Short.MAX_VALUE))
								.addContainerGap()));

		GroupLayout gl_bleu = new GroupLayout(bleu);
		gl_bleu.setHorizontalGroup(gl_bleu.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_bleu.createSequentialGroup()
						.addGap(5)
						.addComponent(btnAdd)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE,
								40, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnImport, GroupLayout.PREFERRED_SIZE,
								46, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(12, Short.MAX_VALUE)));
		gl_bleu.setVerticalGroup(gl_bleu
				.createParallelGroup(Alignment.TRAILING)
				.addGroup(
						gl_bleu.createSequentialGroup()
								.addGroup(
										gl_bleu.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														btnAdd,
														GroupLayout.PREFERRED_SIZE,
														46, Short.MAX_VALUE)
												.addComponent(
														btnDelete,
														GroupLayout.PREFERRED_SIZE,
														40, Short.MAX_VALUE)
												.addComponent(
														btnImport,
														GroupLayout.PREFERRED_SIZE,
														46,
														GroupLayout.PREFERRED_SIZE))
								.addContainerGap()));
		bleu.setLayout(gl_bleu);
		panel.setLayout(gl_panel);

		// panel principale
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(panel,
				GroupLayout.PREFERRED_SIZE, 615, GroupLayout.PREFERRED_SIZE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(panel,
				GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE));

		this.pack();
		this.setFocusable(true);
		this.requestFocusInWindow();

	}// </editor-fold>//GEN-END:initComponents

	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel vert;
	public javax.swing.JPanel jPanel5;
	public javax.swing.JPanel rouge;
	public static javax.swing.JPanel jaune;
	public JPanel bleu, blanc, gris;
	public JPanel jSplitPane1;
	private JPanel jSplitPane3;
	public JPanel panel;
	private int xd;
	private int yd;

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		/**
		 * Suppression des fichiers temporaires à la fermeture de l'application
		 * windowClosed
		 */
		for (Tank t : MainWindow.getPanneauDessin().getPhysique().getTanks()) {
			File file = new File("src/ressources/"
					+ t.getIntel().getScript().getTmpFileName());
			file.delete();
		}
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		/* Suppression des fichiers temporaires à la fermeture de l'application */
		for (Tank t : MainWindow.getPanneauDessin().getPhysique().getTanks()) {
			File file = new File("src/ressources/"
					+ t.getIntel().getScript().getTmpFileName());
			file.delete();
		}
		System.exit(0);

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		// TODO Auto-generated method stub

	}

	public static int i = 3;

	public void addTank(JLabel tank, JLabel scriptNom) {
		this.a = tank;
		scriptnom[i] = new JLabel();
		tankPane[i] = new JPanel();

		tankPane[i].setLayout(new BoxLayout(tankPane[i], BoxLayout.PAGE_AXIS));
		scriptNom.setAlignmentX(CENTER_ALIGNMENT);
		this.a.setAlignmentX(CENTER_ALIGNMENT);
		tankPane[i].add(this.a);
		tanklist[i] = tank;
		scriptnom[i] = scriptNom;
		tankPane[i].add(scriptNom);

		// this.a.setText(scriptNom.getTefxt());

		MainWindow.getInterface().rouge.add(tankPane[i]);

		MainWindow.getInterface().a.addMouseListener(ms);
		MainWindow.getInterface().a.addMouseMotionListener(mms);
		MainWindow.getInterface().a.addMouseListener(pms);
		MainWindow.getInterface().tankPane[i].addMouseListener(pms);

		MainWindow.getInterface().rouge.revalidate();

		MainWindow.getInterface().repaint();

		i++;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (selectedTank != null && stoped == 1) {
			if (arg0.getID() == 402) // Touch suppr relachée
			{
				MainWindow.getPanneauDessin().getPhysique()
						.destroyTank(selectedTank);
				// setSelectedTank(null);
				MainWindow.getPanneauDessin().repaint();
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		// TODO Auto-generated method stub

	}

	public void ModifTank(JLabel b, JLabel scriptNom2, int j) {
		// TODO Auto-generated method stub
		this.tanklist[j].setIcon(b.getIcon());
		this.scriptnom[j].setText(scriptNom2.getText());
	}

}