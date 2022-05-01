import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtill {
	public static BufferedImage[] myTank1 = new BufferedImage[4]; 
	public static BufferedImage[] myTank2 = new BufferedImage[4]; 
	public static BufferedImage[] obstacles = new BufferedImage[4]; 
	public static BufferedImage[][] enemyTank = new BufferedImage[3][4]; 
	public static BufferedImage[] backgrounds = new BufferedImage[4]; 
	public static BufferedImage[] bullet = new BufferedImage[4]; 
	public static BufferedImage[] blasts = new BufferedImage[4];
	
	static {
		try {
			myTank1[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_up.png"));
			myTank1[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_right.png"));
			myTank1[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_down.png"));
			myTank1[3] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_left.png"));
			backgrounds[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/backgrounds/background1.jpg"));
			bullet[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_up.png"));
			bullet[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_right.png"));
			bullet[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_down.png"));
			bullet[3] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_left.png"));
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) {
					enemyTank[i][j] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank" + i + j + ".png"));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
