package thinktank.javabot.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GraphicCreateTank extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultListModel<String> model = new DefaultListModel<String>();
	JList<String> list = new JList<String>(model);
	JPanel panel = new JPanel();
	JLabel labelForImages = new JLabel();
	HashMap<String, String> data = new HashMap<String, String>();

	private JTextField textField;
	JButton btnNewButton;

	JLabel lblpy;
	public GraphicInterface window;
	private JLabel b;


//	
	public GraphicCreateTank(GraphicInterface graphicInterface) {
		super("Nouveau Tank");
		this.window = graphicInterface;

		lblpy = new JLabel(".py");
		lblpy.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setColumns(5);
		btnNewButton = new JButton("");
		btnNewButton.setRolloverEnabled(false);
		btnNewButton.setRequestFocusEnabled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setSize(new Dimension(46, 46));
		btnNewButton.setPreferredSize(new Dimension(46, 46));
		btnNewButton.setMaximumSize(new Dimension(46, 46));
		btnNewButton.setIconTextGap(0);
		btnNewButton.setBorder(null);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(new ImageIcon("src/ressources/agree.png"));
		btnNewButton.setToolTipText("Ajouter Le Tank ");
		btnNewButton.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel tankPanel = new JPanel();
				tankPanel.setLayout(new BorderLayout(0, 0));
				JLabel scriptNom = new JLabel(textField.getText());
				JLabel a = new JLabel();
				a.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));
				scriptNom.setEnabled(true);
				scriptNom.setFocusable(false);
				scriptNom.setHorizontalAlignment(SwingConstants.CENTER);
				scriptNom.setHorizontalTextPosition(SwingConstants.CENTER);

				if (list.getSelectedIndex() == 0) {

					a.setIcon(new ImageIcon("src/ressources/TankBViolet.png"));

				} else if (list.getSelectedIndex() == 1) {
					a.setIcon(new ImageIcon("src/ressources/TankBRed.png"));

				} else if (list.getSelectedIndex() == 2) {
					a.setIcon(new ImageIcon("src/ressources/TankBRose.png"));

				} else if (list.getSelectedIndex() == 3) {
					a.setIcon(new ImageIcon("src/ressources/TankBVert.png"));

				} else if (list.getSelectedIndex() == 4) {
					a.setIcon(new ImageIcon("src/ressources/TankBJaune.png"));

				} else if (list.getSelectedIndex() == 5) {

					a.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));

				}

				window.addTank(a, scriptNom);

				/*
				 * � chaque ajout d'un tank, on �crit dans le fichier persistance le nom du script et le num�ro de la couleur
				 * du tank
				 */
				File varTmpDir = new File("src/scripts/" + scriptNom.getText()
						+ ".py");
				File persistance=new File("src/scripts/persistance.txt");
				System.out.println("existance:"+persistance.exists());
				boolean exists = varTmpDir.exists();
				if (!exists) {

					try {
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(new File("src/scripts/"
										+ scriptNom.getText() + ".py"),true));

						writer.write("");

						writer.close();
							
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
				try {
				if(!persistance.exists())
				{
					persistance.createNewFile();
				}
				FileWriter fw = new FileWriter(persistance.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(scriptNom.getText()+"\n"+list.getSelectedIndex()+"\n");
				bw.close();
				}
				catch (IOException e2) {
					e2.printStackTrace();
				}
				fermer();
			}
		});

		data.put("a", "        ");
		data.put("b", "        ");
		data.put("c", "        ");
		data.put("d", "        ");
		data.put("j", "        ");
		data.put("h", "        ");

		for (Object x : data.keySet()) {
			model.addElement(data.get(x));
		}
		this.add(new JScrollPane(list));

		labelForImages.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));
		panel.add(labelForImages);
		this.add(panel);
		repaint();

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {

					if (list.getSelectedIndex() == 0) {

						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBViolet.png"));
						panel.add(labelForImages);
						b = new JLabel();
						b = labelForImages;

						repaint();

					} else if (list.getSelectedIndex() == 1) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBRed.png"));
						panel.add(labelForImages);
						b = labelForImages;
						repaint();
						b = new JLabel();
						b = labelForImages;

					} else if (list.getSelectedIndex() == 2) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBRose.png"));
						panel.add(labelForImages);
						b = new JLabel();
						repaint();

						b = labelForImages;

					} else if (list.getSelectedIndex() == 3) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBVert.png"));
						panel.add(labelForImages);
						b = new JLabel();
						repaint();
						b = labelForImages;

					} else if (list.getSelectedIndex() == 4) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBJaune.png"));
						panel.add(labelForImages);
						repaint();
						b = new JLabel();
						b = labelForImages;

					} else if (list.getSelectedIndex() == 5) {

						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBCyan.png"));
						panel.add(labelForImages);
						b = new JLabel();
						repaint();
						b = labelForImages;

					}

				}

			}

		});

		list.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				// On appele la méthode parent qui initialise tout bien :
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);

				// Le paramètre 'value' correspond à la valeur de la ligne
				// courante
				// On s'en sert pour déterminer la couleur de fond à appliquer

				if (index == 0)
					setBackground(Color.MAGENTA);
				else if (index == 1)
					setBackground(Color.RED);
				else if (index == 2)
					setBackground(Color.PINK);
				else if (index == 3)
					setBackground(Color.GREEN);
				else if (index == 4)
					setBackground(Color.YELLOW);
				else if (index == 5)
					setBackground(Color.CYAN);

				return this;
			}

		});

		this.add(textField);
		this.add(lblpy);

		this.add(btnNewButton);
		this.setLayout(new FlowLayout());
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(254, 185);
		this.setResizable(false);
		this.setVisible(true);
	}

	public void setPanel(JPanel panel) {
		this.add(panel);
	}

	public GraphicCreateTank(GraphicInterface interfaceModif,
			final JLabel scriptnom, final int j,final String toremove) {
		// TODO Auto-generated constructor stub
		super("Modifier votre Tank");
		this.window = interfaceModif;

		lblpy = new JLabel(".py");
		lblpy.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setColumns(5);
		btnNewButton = new JButton("");
		btnNewButton.setRolloverEnabled(false);
		btnNewButton.setRequestFocusEnabled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setSize(new Dimension(46, 46));
		btnNewButton.setPreferredSize(new Dimension(46, 46));
		btnNewButton.setMaximumSize(new Dimension(46, 46));
		btnNewButton.setIconTextGap(0);
		btnNewButton.setBorder(null);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(new ImageIcon("src/ressources/Edit-Image-50.png"));
		btnNewButton.setToolTipText("Modifier Le Tank ");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel tankPanel = new JPanel();
				tankPanel.setLayout(new BorderLayout(0, 0));

				JLabel scriptNom;
				if (textField.getText() != "")
					scriptNom = new JLabel(textField.getText());
				else
					scriptNom = scriptnom;
				/*
				 * Suppression du tank qu'on a voulu modifier
				 */
				File inputFile = new File("src/scripts/persistance.txt");
				File tempFile = new File("src/scripts/tmp.txt");
					BufferedReader reader;
					try {
						reader = new BufferedReader(new FileReader(inputFile));
						BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
						String lineToRemove = toremove;
						System.out.println("to remove : "+lineToRemove+" to add : "+textField.getText());
						String currentLine;
						String currentLine2;
						while((currentLine = reader.readLine()) != null && (currentLine2 = reader.readLine()) != null) {
						    String trimmedLine = currentLine.trim();
						    if(trimmedLine.equals(lineToRemove)) 
						    	{
						    	System.out.println("found");
								File file1=new File("src/scripts/"+toremove+".py");
								File file2=new File("src/scripts/"+textField.getText()+".py");
								file1.renameTo(file2);
						    	continue;
						    	}
						    writer.write(currentLine + System.getProperty("line.separator"));
						    writer.write(currentLine2 + System.getProperty("line.separator"));
						}
//							writer.write(textField.getText() + System.getProperty("line.separator"));
//							writer.write(list.getSelectedIndex() + System.getProperty("line.separator"));
						writer.close(); 
						reader.close(); 
						forceRename(tempFile, inputFile);
						//System.out.println("successful : "+successful);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				
				//MainWindow.getInterface().modifierNomScript(scriptNom.getText(),toremove);

				for (Object x : data.keySet()) {
					if (list.getSelectedValue().equals(x)) {
						b.setIcon(new ImageIcon(data.get(x)));

						repaint();
					}
				}
				JLabel a = new JLabel();
				a.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));
				scriptNom.setEnabled(true);
				scriptNom.setFocusable(false);
				scriptNom.setHorizontalAlignment(SwingConstants.CENTER);
				scriptNom.setHorizontalTextPosition(SwingConstants.CENTER);

				if (list.getSelectedIndex() == 0) {

					a.setIcon(new ImageIcon("src/ressources/TankBViolet.png"));

				} else if (list.getSelectedIndex() == 1) {
					a.setIcon(new ImageIcon("src/ressources/TankBRed.png"));

				} else if (list.getSelectedIndex() == 2) {
					a.setIcon(new ImageIcon("src/ressources/TankBRose.png"));

				} else if (list.getSelectedIndex() == 3) {
					a.setIcon(new ImageIcon("src/ressources/TankBVert.png"));

				} else if (list.getSelectedIndex() == 4) {
					a.setIcon(new ImageIcon("src/ressources/TankBJaune.png"));

				} else if (list.getSelectedIndex() == 5) {

					a.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));

				}

				window.ModifTank(a, scriptNom, j);
				/*
				 * Ajout du nouveau nom du script et la nouvelle couleur dans le fichier persistance
				 */
				File varTmpDir = new File("src/scripts/" + scriptNom.getText()
				+ ".py");
				File persistance=new File("src/scripts/persistance.txt");
				System.out.println("existance:"+persistance.exists());
				boolean exists = varTmpDir.exists();
				try {
				if(!persistance.exists())
				{
					persistance.createNewFile();
				}
				FileWriter fw = new FileWriter(persistance.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(scriptNom.getText()+"\n"+list.getSelectedIndex()+"\n");
				bw.close();
				}
				catch (IOException e2) {
					e2.printStackTrace();
				}
				fermer();
			}
		});

		data.put("a", "        ");
		data.put("b", "        ");
		data.put("c", "        ");
		data.put("d", "        ");
		data.put("j", "        ");
		data.put("h", "        ");

		for (Object x : data.keySet()) {
			model.addElement(data.get(x));
		}

		this.add(new JScrollPane(list));
		labelForImages.setIcon(new ImageIcon("src/ressources/TankBCyan.png"));
		panel.add(labelForImages);
		this.add(panel);
		repaint();
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {

					if (list.getSelectedIndex() == 0) {

						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBViolet.png"));
						panel.add(labelForImages);
						b = new JLabel();
						b = labelForImages;

						repaint();

					} else if (list.getSelectedIndex() == 1) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBRed.png"));
						panel.add(labelForImages);
						b = labelForImages;
						repaint();
						b = new JLabel();
						b = labelForImages;

					} else if (list.getSelectedIndex() == 2) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBRose.png"));
						panel.add(labelForImages);
						b = new JLabel();
						repaint();

						b = labelForImages;

					} else if (list.getSelectedIndex() == 3) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBVert.png"));
						panel.add(labelForImages);
						b = new JLabel();
						repaint();
						b = labelForImages;

					} else if (list.getSelectedIndex() == 4) {
						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBJaune.png"));
						panel.add(labelForImages);
						repaint();
						b = new JLabel();
						b = labelForImages;

					} else if (list.getSelectedIndex() == 5) {

						labelForImages.setIcon(new ImageIcon(
								"src/ressources/TankBCyan.png"));
						panel.add(labelForImages);
						b = new JLabel();
						repaint();
						b = labelForImages;

					}
				}
			}

		});
		list.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				// On appele la méthode parent qui initialise tout bien :
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);

				// Le paramètre 'value' correspond à la valeur de la ligne
				// courante
				// On s'en sert pour déterminer la couleur de fond à appliquer

				if (index == 0)
					setBackground(Color.MAGENTA);
				else if (index == 1)
					setBackground(Color.RED);
				else if (index == 2)
					setBackground(Color.PINK);
				else if (index == 3)
					setBackground(Color.GREEN);
				else if (index == 4)
					setBackground(Color.YELLOW);
				else if (index == 5)
					setBackground(Color.CYAN);
				
				return this;
			}

		});
		this.add(textField);
		this.add(lblpy);

		this.add(btnNewButton);
		this.setLayout(new FlowLayout());
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(254, 185);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	public void fermer()
	{
		this.setVisible(false);
	}
	
	public void forceRename(File source, File target) throws IOException
	{
	   if (target.exists()) target.delete();
	   source.renameTo(target);
	}

}
