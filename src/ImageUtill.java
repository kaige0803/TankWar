import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtill {
	public static BufferedImage[][] myTanks = new BufferedImage[2][4]; 
	public static BufferedImage[] obstacles = new BufferedImage[4]; 
	public static BufferedImage[][] enemyTank = new BufferedImage[3][4]; 
	public static BufferedImage[] backgrounds = new BufferedImage[10]; 
	public static BufferedImage[] bullet = new BufferedImage[4]; 
	public static BufferedImage[] tank_blasts = new BufferedImage[11];
	public static BufferedImage base = null;
	public static BufferedImage destroyed = null;
	
	 
	static {
		try {
			backgrounds[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/backgrounds/background1.jpg"));
			bullet[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_up.png"));
			bullet[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_right.png"));
			bullet[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_down.png"));
			bullet[3] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_left.png"));
			obstacles[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/obstacles/steel.png"));
			obstacles[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/obstacles/stone.png"));
			obstacles[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/obstacles/grass.png"));
			base = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/base/base.png"));
			destroyed = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/destroyed/destroyed.png"));
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) { 
					enemyTank[i][j] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank" + i + j + ".png"));
				}
			}
			
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					myTanks[i][j] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytanks/mytank" + i + j + ".png"));
				}
			}
			for(int i = 0; i < 11; i++) {
				tank_blasts[i] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/blasts/tank_blast" + i + ".gif"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
