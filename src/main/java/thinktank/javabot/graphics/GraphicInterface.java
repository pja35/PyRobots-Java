/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinktank.javabot.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.runner.notification.StoppedByUserException;

import helper.GhostComponentAdapter;
import helper.GhostDropListener;
import helper.GhostDropManagerDemo;
import helper.GhostGlassPane;
import helper.GhostMotionAdapter;
import helper.GhostPictureAdapter;
import helper.UIHelper;
import thinktank.javabot.intelligences.Script;
import thinktank.javabot.physics.Direction;
import thinktank.javabot.physics.Tank;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import javax.swing.border.TitledBorder;

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
	public static JFileChooser chooser = new JFileChooser("src/scripts");
	private static Tank selectedTank;
	public String mode = "Debug";
	private GhostGlassPane glassPane;
	/*
	 * Ces attributs permettent de sauvegarder l'état du jeu à un moment donné
	 */
	private ArrayList<Tank> listeTanksSauv = new ArrayList<>();
	private ArrayList<Integer> listX = new ArrayList<>();
	private ArrayList<Integer> listY = new ArrayList<>();
	private ArrayList<Integer> listPV = new ArrayList<>();
	private ArrayList<Integer> listDirecX = new ArrayList<>();
	private ArrayList<Integer> listDirecY = new ArrayList<>();

	/*
	 * Plusieurs valeurs possibles pour stoped: 0: Le jeu est en exÃ©cution 1: Le
	 * jeu est arrÃªtÃ© 2: L'arrÃªt est en cours et sera effectif Ã  la fin des
	 * itÃ©rations de fluiditÃ©
	 */
	public static int stoped = 1;
	public static boolean NextStepFlag = false;
	public static RSyntaxTextArea textAreaCode;
	public static Highlighter currentLineExecution;
	public static StringWriter outPut = new StringWriter();
	public JButton btnImport;
	public JLabel TankBBrun;
	public JLabel TankBCyan;
	public JLabel TankBJaune;
	public JLabel TankBRose;
	public JLabel TankBVert;
	private JLabel TankBViolet;
	public JLabel a;
	public GroupLayout gl_box;
	private MouseMotionListener mms;
	private MouseListener ms;
	private MouseListener pms;
	public static GraphicInterface gui;
	public static boolean devModeActivated = false;
	public static JLabel slcTank=null;
	public static JLabel tankPressed;
	private String TankScript;
	private JLabel TankBRed;
	public static JPanel[] tankPane;
	public static JLabel[] scriptnom;
	public static JLabel[] tanklist;
	public boolean dragging;
	GhostPictureAdapter pictureAdapter;
	GhostComponentAdapter componentAdapter;
	GhostDropListener listener;
	JList list_1; //Utilisé pour faire le menu d'aide
	JLabel lblNew;
	/*
	 * Pour les undo/redo nous avons utilisé la classe UndoManager de Java
	 * Problème de la classe, les retours (annulation d'action) se font caractère par caractère (à revoir)
	 */
	UndoManager manager = new UndoManager();
	public String script;
	public static StyledDocument doc = (StyledDocument) new DefaultStyledDocument();
	public static JTextPane textAreaOutput = new JTextPane(doc);
	JPanel gap = new JPanel();



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
	 * rÃ©cupÃ¨re le tank sÃ©lectionnÃ©
	 * 
	 * @return selectedTank
	 */
	public static Tank getSelectedTank() {
		return selectedTank;
	}

	/**
	 * met Ã  jour le tank sÃ©lectionnÃ©
	 * 
	 * @param tank
	 */
	public static void setSelectedTank(Tank t) {
		selectedTank = t;

		if (selectedTank == null) {

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
		 * On vÃ©rifie si il faut mettre Ã  jour la liste d'instructions. Y-a-t-il
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
		}
	}

	/**
	 * update Output Area
	 */
	public static void updateOutputArea(Style style) {

		try {
			doc.insertString(doc.getLength(), outPut.toString(), style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outPut.getBuffer().setLength(0);
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
		glassPane = new GhostGlassPane();
		setGlassPane(glassPane);

		jLabel1 = new javax.swing.JLabel();
		jLabel1.setText("jLabel1");
		panel = new javax.swing.JPanel();

		vert = new JPanel();
		vert.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), "Editeur de code"));
		jaune = new GhostGlassPane();
		jaune.setBackground(Color.YELLOW);
		jaune.setLayout(new GridLayout());
		listener = new GhostDropManagerDemo(jaune);
		jaune.setMinimumSize(new Dimension(662,485));
		jaune.setMaximumSize(new Dimension(662,485));
		gap.setPreferredSize(new Dimension(0, 0));
		gap.setMinimumSize(new Dimension(300, 0));
		gap.setMaximumSize(new Dimension(300, 0));
		
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

		box = Box.createHorizontalBox();
		box.setLocation(new Point(2, 2));

		bleu = new JPanel();
		bleu.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), "Commandes Tanks"));
		blanc = new JPanel();

		tankPane = new JPanel[100];
		scriptnom = new JLabel[100];
		tanklist = new JLabel[100];

		final JPanel gris = new JPanel();
		gris.setMaximumSize(new Dimension(200, 32767));
		gris.setAlignmentX(Component.LEFT_ALIGNMENT);

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

						MainWindow.getInterface().box.remove(tankPane[i]);
						System.out.println("ztiktiktiktitktik :"+slcTank.getText());
						File f = new File("src/scripts/"+slcTank.getText()+".py");
						f.delete();
						MainWindow.getInterface().box.repaint();
						MainWindow.getInterface().validate();
						File inputFile = new File("src/scripts/persistance.txt");
						File tempFile = new File("src/scripts/tmp.txt");

						try {
							BufferedReader reader = new BufferedReader(new FileReader(inputFile));
							BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
							String lineToRemove = scriptnom[i].getText();
							String currentLine;
							String currentLine2;
							while((currentLine = reader.readLine()) != null && (currentLine2 = reader.readLine()) != null) {
								String trimmedLine = currentLine.trim();
								if(trimmedLine.equals(lineToRemove)) continue;
								writer.write(currentLine + System.getProperty("line.separator"));
								writer.write(currentLine2 + System.getProperty("line.separator"));
							}
							writer.close(); 
							reader.close(); 
							inputFile.delete();
							boolean successful = tempFile.renameTo(inputFile);
							System.out.println("successful : "+successful);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
				textAreaCode.setText("");
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
				if(slcTank==null)
					JOptionPane.showMessageDialog(MainWindow.getInterface(), "Selectionnez un tank pour sauvegarder","Erreur",JOptionPane.ERROR_MESSAGE, null);	
				else{
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
						for (int j=0;j<MainWindow.phy.getTanks().size();j++) {
							Tank tank=MainWindow.phy.getTanks().get(j);
							if(GraphicInterface.slcTank!=null){
								if(tank.filep.equals(GraphicInterface.slcTank.getText()))
								{
									int x=tank.getCoordX();
									int y=tank.getCoordY();
									int pdv=tank.getPointsDeVie();
									int direct_x=tank.getDirection().getDx();
									int direct_y=tank.getDirection().getDy();
									String tc = tank.tc;
									MainWindow.phy.getMap().erase(x, y);
									Tank t = new Tank(x, y, MainWindow.phy.getMap(), "src/scripts/" + slcTank.getText() + ".py",
											MainWindow.phy, tc , slcTank.getText());
									MainWindow.phy.getMap().addObjetTT(x, y, t);
									t.setPointsDeVie(pdv);
									t.getDirection().setDx(direct_x);
									t.getDirection().setDy(direct_y);
									MainWindow.phy.getTanks().set(j, t);
									switch (tc)
									{
									  case "Vert":
									    t.setColor(Color.green);
									    break;  
									  case "Jaune":
										    t.setColor(Color.YELLOW);
										    break;  
									  case "Cyan":
										    t.setColor(Color.CYAN);
										    break;  
									  case "Red":
										    t.setColor(Color.RED);
										    break;  
									  case "Violet":
										    t.setColor(new Color(255, 0, 255));
										    break;  
									  case "Rose":
										    t.setColor(Color.PINK);
										    break;  
									}
								}
							}
						}
						btnsave.setIcon(new ImageIcon("src/ressources/save.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}});

		/*
		 * Button pour l'annulation d'un action sur l'éditeur du texte
		 */
		final JButton btnundo = new JButton("");
		btnundo.setFocusTraversalPolicyProvider(true);
		btnundo.setContentAreaFilled(false);
		btnundo.setBorderPainted(false);
		btnundo.setRequestFocusEnabled(false);
		btnundo.setRolloverEnabled(false);
		btnundo.setToolTipText("Retour en arriï¿½re");
		btnundo.setIcon(new ImageIcon("src/ressources/undo.png"));
		btnundo.setBorder(null);

		btnundo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				manager.undo();
				btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));

			}
		});

		/*
		 * Button pour refaire une action sur l'éditeur du texte
		 */
		final JButton btnredo = new JButton("");
		btnredo.setFocusTraversalPolicyProvider(true);
		btnredo.setContentAreaFilled(false);
		btnredo.setBorderPainted(false);
		btnredo.setRequestFocusEnabled(false);
		btnredo.setRolloverEnabled(false);
		btnredo.setToolTipText("Retour en arrière");
		btnredo.setIcon(new ImageIcon("src/ressources/redo.png"));
		btnredo.setBorder(null);

		btnredo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				manager.redo();
				btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
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
		final JButton btnSauvegarder = new JButton("");
		btnSauvegarder.setRolloverEnabled(false);
		btnSauvegarder.setRequestFocusEnabled(false);
		btnSauvegarder.setIcon(new ImageIcon("src/ressources/Screenshot.png"));
		btnSauvegarder.setToolTipText("Sauvgarder cette état");
		btnSauvegarder.setSelectedIcon(null);
		btnSauvegarder.setIconTextGap(0);
		btnSauvegarder.setBorderPainted(false);
		btnSauvegarder.setContentAreaFilled(false);

		final JButton btnReinitialiser = new JButton("");
		btnReinitialiser.setRolloverEnabled(false);
		btnReinitialiser.setRequestFocusEnabled(false);
		btnReinitialiser.setIcon(new ImageIcon("src/ressources/Reset.png"));
		btnReinitialiser.setToolTipText("Récupérer la dernière état sauvegardée");
		btnReinitialiser.setSelectedIcon(null);
		btnReinitialiser.setIconTextGap(0);
		btnReinitialiser.setBorderPainted(false);
		btnReinitialiser.setContentAreaFilled(false);

		final JButton btnGenerator = new JButton("");
		btnGenerator.setRolloverEnabled(false);
		btnGenerator.setRequestFocusEnabled(false);
		btnGenerator.setIcon(new ImageIcon("src/ressources/map_generator.png"));
		btnGenerator.setToolTipText("Générer une nouvelle dispostion des mûrs");
		btnGenerator.setSelectedIcon(null);
		btnGenerator.setIconTextGap(0);
		btnGenerator.setBorderPainted(false);
		btnGenerator.setContentAreaFilled(false);


		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (stoped == 1)
					stoped = 0; // Demarrage / RedÃ©marrage
				else if (stoped == 2)
					return;
				else
					stoped = 2;

				if (stoped == 1 || stoped == 2) {
					btnPlay.setIcon(new ImageIcon("src/ressources/Start-50.png"));
					btnPlay.setToolTipText("Jouer");
					textAreaCode.setEditable(true);
					btnReinitialiser.setEnabled(true);
					btnSauvegarder.setEnabled(true);
				} else {
					btnPlay.setIcon(new ImageIcon(
							"src/ressources/Sleep Mode-50.png"));
					btnPlay.setToolTipText("Pause");
					textAreaCode.setEditable(false);
					btnReinitialiser.setEnabled(false);
					btnSauvegarder.setEnabled(false);
					slcTank=null;
					selectedTank=null;
					for(int j=0;j<i;j++)
					{
						tankPane[j].setBorder(null);
					}

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
				gap.setPreferredSize(new Dimension(0, 0));
				
				System.out.println("X :"+blanc.getX());
				if(!(MainWindow.getInterface().getExtendedState() == 6))
				{
					MainWindow.getInterface().setSize(1368, 676);
				}
				
			}
		});


		btnJeu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jaune.setVisible(true);
				vert.setVisible(false);
				gap.setPreferredSize(new Dimension(300, 0));
				//blanc.setLocation(blanc.getX()-340, blanc.getY());
				if(!(MainWindow.getInterface().getExtendedState() == 6))
					MainWindow.getInterface().setSize(1368, 676);			
			}
		});

		btnDbg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vert.setVisible(true);
				jaune.setVisible(true);
				gap.setPreferredSize(new Dimension(0, 0));
				if(!(MainWindow.getInterface().getExtendedState() == 6))
				{
					MainWindow.getInterface().setSize(1368, 676);
				}
			}
		});


		box.setPreferredSize(new Dimension(720, 50));

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
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}

		};

		pms = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				System.out.println(e.getSource()==TankBCyan);
				System.out.println(e.getSource().toString().split("Tank")[1]
						.split(".png")[0].substring(1));
				for (int j = 0; j < scriptnom.length; j++) {

					String k = e.getSource().toString().split("Tank")[1]
							.split(".png")[0].substring(1);

					if (e.getSource() == tanklist[j]) {
						System.out.println("Entrï¿½ !!!");
						selectedTank=null;
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
							btnsave.setIcon(new ImageIcon("src/ressources/save.png"));
							MainWindow.getInterface().script=script;
						} catch (IOException e1) {
							System.out.println(e1);
						}

					}

					else {
						tankPane[j].setBorder(BorderFactory.createEmptyBorder());
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
									MainWindow.getInterface(), scriptnom[j], j, scriptnom[j].getText());
						}
					}

				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		};

		/*
		 * Listener qui permet de faire de Drag n'Drop
		 * Inspiré depuis le menu Drag n'Drop de Romain Guy disponible sur internet
		 * La classe glassPane (package helper) permet de creer un deuxième panel transparent une fois qu'on click sur un tank
		 */
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

		box.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), "Vos Tanks"));

		final JButton btnDevMode = new JButton("Mode Dev");
		btnDevMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GraphicInterface.devModeActivated == false) {
					GraphicInterface.devModeActivated = true;
					btnDevMode.setText("Mode Jeu");

				} else {
					GraphicInterface.devModeActivated = false;
					btnDevMode.setText("Mode Dev");
				}
			}
		});

		btnImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO Auto-generated method stub
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
		
		/*
		 * Initialisation du panel 'Vos Tanks' avec les 3 tanks et script par défaut (Script 1, Script 2, Script3)
		 */

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
		//
		tanklist[0] = TankBCyan;
		tanklist[1] = TankBRed;
		tanklist[2] = TankBVert;

		TankBCyan.addMouseListener(ms);
		TankBRed.addMouseListener(ms);
		TankBVert.addMouseListener(ms);

		TankBCyan.addMouseListener(pms);
		TankBRed.addMouseListener(pms);
		TankBVert.addMouseListener(pms);

		tankPane[0].addMouseListener(pms);
		tankPane[1].addMouseListener(pms);
		tankPane[2].addMouseListener(pms);

		this.box.add(tankPane[0]);
		this.box.add(tankPane[1]);
		this.box.add(tankPane[2]);
		TankBCyan.addMouseListener(pictureAdapter = new GhostPictureAdapter(glassPane, "TankBCyan", "../ressources/TankBCyan.png"));
		pictureAdapter.addGhostDropListener(listener);
		TankBCyan.addMouseMotionListener(new GhostMotionAdapter(glassPane));

		TankBRed.addMouseListener(pictureAdapter = new GhostPictureAdapter(glassPane, "TankBRed", "../ressources/TankBRed.png"));
		pictureAdapter.addGhostDropListener(listener);
		TankBRed.addMouseMotionListener(new GhostMotionAdapter(glassPane));

		TankBVert.addMouseListener(pictureAdapter = new GhostPictureAdapter(glassPane, "TankBVert", "../ressources/TankBVert.png"));
		pictureAdapter.addGhostDropListener(listener);
		TankBVert.addMouseMotionListener(new GhostMotionAdapter(glassPane));

		this.box.repaint();
		this.box.revalidate();

		textAreaCode = new RSyntaxTextArea();
		textAreaCode.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);

		JScrollPane textAreaCodeScrollPane = new JScrollPane(textAreaCode);
		textAreaCodeScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaCodeScrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);



		JLabel lblErrors = new JLabel("Sortie");
		lblErrors.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblHelp = new JLabel("Aide");

		/*
		 * Initialisation du Menu d'aide
		 */
		DefaultListModel<String> listModel = new DefaultListModel();
		listModel.addElement("MoveForward");
		listModel.addElement("MoveBackward");
		listModel.addElement("TurnClockwise");
		listModel.addElement("TurnCounterClockwise");
		listModel.addElement("Shoot");
		listModel.addElement("NoAction");
		listModel.addElement("--------------------------------------------------------");
		listModel.addElement("----------------------    Aide    ----------------------");




		JPanel help_pane=new JPanel(new FlowLayout(FlowLayout.LEFT));
		help_pane.setPreferredSize(new Dimension(500,110));
		list_1 = new JList(listModel);
		/*
		 * Evenement permettant d'ajouter l'instruction après un double click sur le menu d'aide
		 */
		list_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				i=list_1.getSelectedIndex();
				switch (i) {
				case 0:
					lblNew.setText("\t Le tank avance d'une case.");
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					break;
				case 1:
					lblNew.setText("\t Le tank recule d'une case.");
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					break;
				case 2:
					lblNew.setText("<html>\t Le tank tourne d'un quart de tour <br> dans le sens de l'horloge.</html>");
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					break;
				case 3:
					lblNew.setText("<html>\t Le tank tourne d'un quart de tour <br> dans le sens inverse de l'horloge.</html>");
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					break;
				case 4:
					lblNew.setText("\t Le tank tire un missile.");
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					break;
				case 5:
					lblNew.setText("\t Le tank est en standby");
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					break;
				default:
					lblNew.setText("");
					break;
				}
				if(e.getClickCount()==2){
					switch (i) {
					case 0:
						textAreaCode.insert("moveForward()", textAreaCode.getCaretPosition());
						break;
					case 1:
						textAreaCode.insert("moveBackward()", textAreaCode.getCaretPosition());
						break;
					case 2:
						textAreaCode.insert("turnClockwise()", textAreaCode.getCaretPosition());
						break;
					case 3:
						textAreaCode.insert("turnCounterClockwise()", textAreaCode.getCaretPosition());
						break;
					case 4:
						textAreaCode.insert("shoot()", textAreaCode.getCaretPosition());
						break;
					case 5:
						textAreaCode.insert("doNothing()", textAreaCode.getCaretPosition());
						break;
					default:
						textAreaCode.setText(textAreaCode.getText());
						break;
					}
				}

			}
		});
		help_pane.setPreferredSize(new Dimension(351,141));
		help_pane.setMaximumSize(new Dimension(351,141));
		help_pane.setMinimumSize(new Dimension(351,141));

		list_1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list_1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		lblNew = new JLabel("");
		help_pane.setOpaque(true);
		help_pane.setBackground(Color.WHITE);
		lblNew.setHorizontalAlignment(SwingConstants.CENTER);
		lblNew.setBackground(Color.white);
		help_pane.add(list_1);
		help_pane.add(lblNew);
		lblNew.setAlignmentX(Component.RIGHT_ALIGNMENT);
		help_pane.setBorder(BorderFactory.createLineBorder(Color.gray));
		lblNew.setBackground(Color.WHITE);

		JScrollPane textAreaOutputScrollPane = new JScrollPane();
		textAreaOutputScrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), "Console"));
		GroupLayout gl_vert = new GroupLayout(vert);
		gl_vert.setHorizontalGroup(
				gl_vert.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_vert.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_vert.createParallelGroup(Alignment.TRAILING)
								.addComponent(textAreaOutputScrollPane, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
								.addGroup(gl_vert.createSequentialGroup()
										.addComponent(textAreaCodeScrollPane, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_vert.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_vert.createSequentialGroup()
														.addGap(14)
														.addComponent(btnsave)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(btnundo)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(btnredo))
												.addGroup(gl_vert.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(help_pane, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)))))
						.addContainerGap())
				);
		gl_vert.setVerticalGroup(
				gl_vert.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_vert.createSequentialGroup()
						.addGroup(gl_vert.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_vert.createSequentialGroup()
										.addGroup(gl_vert.createParallelGroup(Alignment.LEADING)
												.addComponent(btnsave)
												.addComponent(btnundo)
												.addComponent(btnredo))
										.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
										.addComponent(help_pane, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
								.addComponent(textAreaCodeScrollPane, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
						.addGap(18)
						.addComponent(textAreaOutputScrollPane, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);


		textAreaOutputScrollPane.setViewportView(textAreaOutput);

		vert.setLayout(gl_vert);

		/*
		 * Sauvegarder un état
		 * On sauvegarde les positions (listX et listY), les points de vie (listPV) et les directions (listDirecX et listDirecY)
		 * de tout les tanks présent sur le plateau de jeu
		 */
		btnSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				listeTanksSauv = MainWindow.phy.getTanks();
				listX.clear();
				listY.clear();
				listPV.clear();
				listDirecX.clear();
				listDirecY.clear();
				for (Tank tank : listeTanksSauv)
				{
					System.out.println(tank.getDirection().getDx() + " " + tank.getDirection().getDy());
					listX.add(tank.getCoordX());
					listY.add(tank.getCoordY());
					listPV.add(tank.getPointsDeVie());
					listDirecX.add(tank.getDirection().getDx());
					listDirecY.add(tank.getDirection().getDy());
				}
				btnSauvegarder.setEnabled(false);
			}
		});

		/*
		 *Reinitialiser d'un état
		 *Le principe est qu'on supprime tout les tanks présent sur le plateau du jeu
		 *puis on recrée des tanks suivants les positions, nombre de points de vie et direction sauvegardés précedamment
		 */
		btnReinitialiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> listX_old = new ArrayList<>();
				ArrayList<Integer> listY_old = new ArrayList<>();
				for (Tank tank : listeTanksSauv)
				{
					listX_old.add(tank.getCoordX());
					listY_old.add(tank.getCoordY());
				}
				for (int i = 0; i < listeTanksSauv.size() ; i++)
				{				
					Tank t = new Tank(listX.get(i), listY.get(i), MainWindow.phy.getMap(), "src/scripts/" + listeTanksSauv.get(i).filep + ".py",
							MainWindow.phy, listeTanksSauv.get(i).tc , listeTanksSauv.get(i).filep);
					MainWindow.phy.getMap().addObjetTT(listX.get(i), listY.get(i), t);
					t.setPointsDeVie(listPV.get(i));
					t.getDirection().setDx(listDirecX.get(i));
					t.getDirection().setDy(listDirecY.get(i));
					MainWindow.phy.getTanks().set(i, t);
				}
				for(int j = 0; j < listX_old.size() ; j++)
				{
					if(listX_old.get(j) != listX.get(j) || listY_old.get(j) != listY.get(j))
						MainWindow.phy.getMap().erase(listX_old.get(j), listY_old.get(j));
				}
			}
		});

		btnGenerator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stoped==1 || stoped==2)
				{
					MainWindow.generateNewMap();
				}
			}
		});


		GroupLayout gl_blanc = new GroupLayout(blanc);
		gl_blanc.setHorizontalGroup(
				gl_blanc.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_blanc.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnGenerator, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(2)
						.addComponent(btnSauvegarder, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(2)
						.addComponent(btnReinitialiser, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)		
						.addGap(2)
						.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(2)
						.addComponent(btnDev, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(2)
						.addComponent(btnJeu, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(2)
						.addComponent(btnDbg, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGap(2))
				);
		gl_blanc.setVerticalGroup(
				gl_blanc.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_blanc.createSequentialGroup()
						.addGroup(gl_blanc.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_blanc.createParallelGroup(Alignment.BASELINE, false)
										.addComponent(btnGenerator, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnSauvegarder, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnReinitialiser, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnPlay, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnDev, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnJeu, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnDbg, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))));



		blanc.setLayout(gl_blanc);
		

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(box, GroupLayout.DEFAULT_SIZE, 1178, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(bleu, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(vert, GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(gap, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jaune, GroupLayout.PREFERRED_SIZE, 662, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(462)
							.addComponent(blanc, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(box, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(bleu, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(jaune, GroupLayout.PREFERRED_SIZE, 485, GroupLayout.PREFERRED_SIZE)
								.addComponent(vert, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(46)
							.addComponent(gap, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(451)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(blanc, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
					.addContainerGap())
		);

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
		textAreaCode.getDocument().addUndoableEditListener(manager);
		textAreaCode.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!script.equals(textAreaCode.getText())){
					btnsave.setIcon(new ImageIcon("src/ressources/firesave.png"));
					System.out.println("dï¿½tectï¿½ !");}
				else
					btnsave.setIcon(new ImageIcon("src/ressources/save.png"));
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		this.setSize(new Dimension(1750,661));

	}// </editor-fold>//GEN-END:initComponents

	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel vert;
	public javax.swing.JPanel jPanel5;
	public Box box;
	public static javax.swing.JPanel jaune;
	public JPanel bleu, blanc, gris;
	public JPanel jSplitPane1;
	private JPanel jSplitPane3;
	public JPanel panel;
	private int xd;
	private int yd;

	/*
	 * La fonction permet au démarrage du jeu de recharger les tanks qu'on avait créer pendant d'ancienne session de jeu.
	 * 
	 */
	protected void persistance()
	{
		try (BufferedReader br = new BufferedReader(new FileReader("src/scripts/persistance.txt")))
		{

			String sCurrentLine;
			String sCurrentLine2;

			while ((sCurrentLine = br.readLine()) != null && (sCurrentLine2 = br.readLine()) != null) {
				int color;
				color=Integer.parseInt(sCurrentLine2);
				JLabel a = new JLabel();
				a.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));
				JPanel tankPanel = new JPanel();
				tankPanel.setLayout(new BorderLayout(0, 0));
				JLabel scriptNom = new JLabel(sCurrentLine);
				scriptNom.setEnabled(true);
				scriptNom.setFocusable(false);
				scriptNom.setHorizontalAlignment(SwingConstants.CENTER);
				scriptNom.setHorizontalTextPosition(SwingConstants.CENTER);

				if (color == 0) {

					a.setIcon(new ImageIcon("src/ressources/TankBViolet.png"));

				} else if (color == 1) {
					a.setIcon(new ImageIcon("src/ressources/TankBRed.png"));

				} else if (color == 2) {
					a.setIcon(new ImageIcon("src/ressources/TankBRose.png"));

				} else if (color == 3) {
					a.setIcon(new ImageIcon("src/ressources/TankBVert.png"));

				} else if (color == 4) {
					a.setIcon(new ImageIcon("src/ressources/TankBJaune.png"));

				} else if (color == 5) {

					a.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));

				}

				this.addTankPersistance(a, scriptNom);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		/**
		 * Suppression des fichiers temporaires Ã  la fermeture de l'application
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
		/* Suppression des fichiers temporaires Ã  la fermeture de l'application */
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

	/*
	 * La fonction permet d'ajouter un tank au panel "Vos Tanks"
	 */
	public void addTank(JLabel tank, JLabel scriptNom) {
		this.a = tank;
		scriptnom[i] = new JLabel();
		tankPane[i] = new JPanel();

		tankPane[i].setLayout(new BoxLayout(tankPane[i], BoxLayout.PAGE_AXIS));
		scriptNom.setAlignmentX(CENTER_ALIGNMENT);
		scriptNom.setMaximumSize(scriptnom[0].getMaximumSize());
		scriptNom.setMinimumSize(scriptnom[0].getMinimumSize());
		scriptNom.setPreferredSize(scriptnom[0].getPreferredSize());
		this.a.setAlignmentX(CENTER_ALIGNMENT);
		tankPane[i].add(this.a);
		tanklist[i] = tank;
		scriptnom[i] = scriptNom;
		tankPane[i].add(scriptNom);

		MainWindow.getInterface().box.add(tankPane[i]);
		MainWindow.getInterface().a.addMouseListener(ms);
		System.out.println(".."+tank.getIcon().toString().substring(3));
		MainWindow.getInterface().a.addMouseListener(MainWindow.getInterface().pictureAdapter = new GhostPictureAdapter(glassPane, "", ".."+tank.getIcon().toString().substring(3)));
		pictureAdapter.addGhostDropListener(listener);
		MainWindow.getInterface().a.addMouseListener(pms);
		MainWindow.getInterface().a.addMouseMotionListener(new GhostMotionAdapter(glassPane));
		MainWindow.getInterface().tankPane[i].addMouseListener(pms);

		MainWindow.getInterface().box.revalidate();

		MainWindow.getInterface().repaint();
		for (int j = 0; j < i; j++) {
			tankPane[j].setBorder(BorderFactory.createEmptyBorder());		
		}

		tankPane[i].setBorder(BorderFactory
				.createLineBorder(Color.black));
		slcTank=scriptnom[i];
		//Modifs 13/05 Affichage Script Tank Crée
		textAreaCode.setText("");

		Path wiki_path = Paths.get("src/scripts/"
				+ scriptnom[i].getText() + ".py");

		Charset charset = Charset.forName("ISO-8859-1");
		try {
			List<String> lines = Files.readAllLines(wiki_path,
					charset);
			String script = "";
			for (String line : lines) {
				script += line + "\n";

			}
			textAreaCode.setText(script);
			MainWindow.getInterface().script=script;
		} catch (IOException e1) {
			System.out.println(e1);
		}
		i++;
	}

	/*
	 * Cet évement permet de modifier l'orientation des tanks via les touches directionnelles du clavier
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if(stoped==1 || stoped==2){
			int c = e.getKeyCode ();
			if (c==KeyEvent.VK_UP) {                
				selectedTank.getDirection().setDx(0);
				selectedTank.getDirection().setDy(-1);
			} else if(c==KeyEvent.VK_DOWN) {                
				selectedTank.getDirection().setDx(0);
				selectedTank.getDirection().setDy(1);   
			} else if(c==KeyEvent.VK_LEFT) {                
				selectedTank.getDirection().setDx(-1);
				selectedTank.getDirection().setDy(0);
			} else if(c==KeyEvent.VK_RIGHT) {                
				selectedTank.getDirection().setDx(1);
				selectedTank.getDirection().setDy(0);
			}}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		// TODO Auto-generated method stub

	}

	public void ModifTank(JLabel b, JLabel scriptNom2, int j) {
		// TODO Auto-generated method stub
		this.tanklist[j].setIcon(b.getIcon());
		this.scriptnom[j].setText(scriptNom2.getText());
		File file=new File("src/scripts/"+scriptnom[j].getText()+".py");
	}

	public static void SetBorder()
	{
		for (int j=0;j<i;j++) {
			if(scriptnom[j].getText().equals(selectedTank.filep))
			{
				tankPane[j].setBorder(BorderFactory
						.createLineBorder(Color.black));
				slcTank=scriptnom[j];
			}
			else
				tankPane[j].setBorder(null);
		}
	}



	public void modifierNomScript(String text, String toremove) {
		System.out.println("new : "+text+" old : "+toremove);
		File inputFile = new File("src/scripts/persistance.txt");
		File tempFile = new File("src/scripts/tmp.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			String lineToRemove = toremove;
			String currentLine;
			String currentLine2;
			while((currentLine = reader.readLine()) != null && (currentLine2 = reader.readLine()) != null) {
				String trimmedLine = currentLine.trim();
				if(trimmedLine.equals(lineToRemove)) 
				{
					System.out.println("found");
					File file1=new File("src/scripts/"+toremove+".py");
					File file2=new File("src/scripts/"+text+".py");
					file1.renameTo(file2);
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
				writer.write(currentLine2 + System.getProperty("line.separator"));
			}
			writer.close(); 
			reader.close(); 
			boolean successful = tempFile.renameTo(inputFile);
			System.out.println("successful : "+successful);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * La fonction garde le même principe AddTank mais ne sélectionne plus le tank ajouté au panel
	 * La fonction est utilisée uniquement dans la fonction persistance()
	 */
	public void addTankPersistance(JLabel tank, JLabel scriptNom)
	{
		this.a = tank;
		scriptnom[i] = new JLabel();
		tankPane[i] = new JPanel();

		tankPane[i].setLayout(new BoxLayout(tankPane[i], BoxLayout.PAGE_AXIS));
		scriptNom.setAlignmentX(CENTER_ALIGNMENT);
		scriptNom.setMaximumSize(scriptnom[0].getMaximumSize());
		scriptNom.setMinimumSize(scriptnom[0].getMinimumSize());
		scriptNom.setPreferredSize(scriptnom[0].getPreferredSize());
		this.a.setAlignmentX(CENTER_ALIGNMENT);
		tankPane[i].add(this.a);
		tanklist[i] = tank;
		scriptnom[i] = scriptNom;
		tankPane[i].add(scriptNom);

		MainWindow.getInterface().box.add(tankPane[i]);
		MainWindow.getInterface().a.addMouseListener(ms);
		System.out.println(".."+tank.getIcon().toString().substring(3));
		MainWindow.getInterface().a.addMouseListener(MainWindow.getInterface().pictureAdapter = new GhostPictureAdapter(glassPane, "", ".."+tank.getIcon().toString().substring(3)));
		pictureAdapter.addGhostDropListener(listener);
		MainWindow.getInterface().a.addMouseListener(pms);
		MainWindow.getInterface().a.addMouseMotionListener(new GhostMotionAdapter(glassPane));
		MainWindow.getInterface().tankPane[i].addMouseListener(pms);

		MainWindow.getInterface().box.revalidate();

		MainWindow.getInterface().repaint();
		for (int j = 0; j < i; j++) {
			tankPane[j].setBorder(BorderFactory.createEmptyBorder());		
		}

		i++;
	}
}