package thinktank.javabot.intelligences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import thinktank.javabot.graphics.GraphicInterface;
import thinktank.javabot.physics.Tank;

/**
 * 
 * @author Gabriel
 */

public class Script {

	private String instructions = "";
	private String filename;
	private int currentLine = 0;
	private String tmpFileName;
	private String tmpFileContent = "";
	/**
	 * @see Intelligence
	 */
	private Intelligence intelligence;

	/**
	 * Contient les instructions du fichier Python
	 * 
	 * @param filename
	 *            , intelligence
	 */
	public Script(String filename, Intelligence intelligence) {

		this.filename = filename;

		this.intelligence = intelligence;
		importInstructionsFromFile(this.filename);
		generateTmpFileName();
		// System.out.println("generated file: "+tmpFileName);
		// System.out.println("Content:  "+tmpFileContent);
		writeTmpFile();

	}

	public synchronized void setFileName(String filename)
	/**
	 * Attribue le chemin du fichier de script sélectionné
	 * 
	 * @param filename
	 */
	{
		this.filename = "a.py";

		instructions = "";
		importInstructionsFromFile(this.filename);
		writeTmpFile();
		updateInstructions(instructions);
	}

	public synchronized String getInstructions()
	/**
	 * Récupère les instructions du fichier de script
	 * 
	 * @return instructions
	 */
	{
		return instructions;
	}

	public synchronized void setCurrentLine(int line)
	/**
	 * Attribue la ligne en cours
	 * 
	 * @param line
	 */
	{
		currentLine = line;
	}

	public synchronized int getCurrentLine()
	/**
	 * Retourne la ligne en cours
	 * 
	 * @return currentLine
	 */
	{
		return currentLine;
	}

	public synchronized String getTmpFileName()
	/**
	 * Retourne le nom du fichier temporaire du script
	 * 
	 * @return tmpFileName
	 */
	{
		return tmpFileName;
	}

	public synchronized int startPositionLine(int line)

	/**
	 * Retourne la position du premier caractère de la ligne à mettre en
	 * surbrillance
	 * 
	 * @param line
	 * @return line number,
	 */
	{
		int i = 0;
		String[] lines = instructions.split("\n");
		try {
			for (int j = 0; j < line - 1; j++) {
				i += lines[j].length() + 1;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}

		return i;
	}

	public synchronized int endPositionLine(int line)

	/**
	 * Retourne la position du dernier caractère de la ligne à mettre en
	 * surbrillance
	 * 
	 * @param line
	 * @return line number
	 */
	{
		int i = 0;
		String[] lines = instructions.split("\n");
		try {
			for (int j = 0; j < line; j++) {
				i += lines[j].length() + 1;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}

		return i;
	}

	private synchronized void generateTmpFileName()
	/**
	 * Génére un nom aléatoire pour le fichier .py temporaire
	 */
	{
		String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		tmpFileName = "."; /* Fichier caché sur les systèmes UNIX */
		for (int i = 0; i < 50; i++) {
			tmpFileName += charset.charAt((int) (Math.random() * charset
					.length()));
		}
	}

	/* Ecrit dans le fichier temporaire */
	public synchronized void writeTmpFile()
	/**
	 * Ecrit dans le fichier temporaire
	 */
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"src/ressources/" + tmpFileName)));
			writer.write(tmpFileContent);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private synchronized String addLayer(String instr)
	/**
	 * Retourne la string contenant le script python avec la surcouche
	 * 
	 * @param instr
	 * @return tmpFileContentLayer
	 */
	{
		String tmpFileContentLayer = instr;
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"doNothing\\(\\)", "tank.doNothing(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"moveForward\\(\\)", "tank.moveForward(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"moveBackward\\(\\)", "tank.moveBackward(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"turnClockwise\\(\\)", "tank.turnClockwise(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"turnCounterClockwise\\(\\)",
				"tank.turnCounterClockwise(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"shoot\\(\\)", "tank.shoot(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"radar\\(\\)", "tank.radar(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"distanceOfForwardObstacle\\(\\)",
				"tank.distanceOfForwardObstacle(lineno())");
		tmpFileContentLayer = tmpFileContentLayer.replaceAll(
				"typeOfForwardObstacle\\(\\)",
				"tank.typeOfForwardObstacle(lineno())");
		return tmpFileContentLayer;
	}

	public synchronized void updateInstructions(String newInstructions)
	/**
	 * Applique la surcouche au script Python
	 * 
	 * @param newInstructions
	 */
	{

		instructions = newInstructions;
		tmpFileContent = addLayer(instructions);
		writeTmpFile();
		Tank tmp = intelligence.getTankR().tankPhy;
		intelligence = new Intelligence(filename, intelligence.getIntels(),
				tmp, this);
		tmp.setIntelligence(intelligence);

	}

	public synchronized void importInstructionsFromFile(String path)

	/**
	 * chemin du script pour l'import de l'instruction
	 * 
	 * @param path
	 */

	{
		if (path == null) {
			instructions = "print \"Hello World !\"";
			tmpFileContent = addLayer(instructions);
			GraphicInterface.textAreaCode.setText("path nul");
			System.out.print("dskljdfskljfkdlk");
			return;
		}
		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(path));

			while ((sCurrentLine = br.readLine()) != null) {
				instructions += sCurrentLine + '\n';
				// tmpFileContent +=
				// "print str(inspect.currentframe().f_back.f_lineno)";

				tmpFileContent = addLayer(instructions);

			}
			System.out.println(tmpFileContent);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	
	

}