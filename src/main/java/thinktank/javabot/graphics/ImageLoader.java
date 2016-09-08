package thinktank.javabot.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	BufferedImage img[] = new BufferedImage[8];

	public enum SpiriteName {
		MUR, MISSILE, SOL
	}

	public ImageLoader() {

		try {
			img[SpiriteName.SOL.ordinal()] = toBufferedImage(ImageIO
					.read(new File("src/ressources/sol.png")));

		} catch (IOException e) {
			System.out.println("probléme d'ouverture des images");
		}

	}

	private BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}

	public BufferedImage getSpirite(int spirite) {

		return img[spirite];
	}

}
