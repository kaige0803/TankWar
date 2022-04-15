import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtill {
	public static BufferedImage[] mytank1 = new BufferedImage[4]; 
	public static BufferedImage[] mytank2 = new BufferedImage[4]; 
	public static BufferedImage[] obstacles = new BufferedImage[4]; 
	public static BufferedImage[] enemytank1 = new BufferedImage[4]; 
	public static BufferedImage[] enemytank2 = new BufferedImage[4]; 
	public static BufferedImage[] enemytank3 = new BufferedImage[4]; 
	public static BufferedImage[] backgrounds = new BufferedImage[4]; 
	public static BufferedImage[] bullet = new BufferedImage[4]; 
	public static BufferedImage[] blasts = new BufferedImage[4];
	
	static {
		try {
			mytank1[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_up.png"));
			mytank1[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_right.png"));
			mytank1[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_down.png"));
			mytank1[3] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/mytank1/mytank1_left.png"));
			backgrounds[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/backgrounds/background1.jpg"));
			bullet[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_up.png"));
			bullet[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_right.png"));
			bullet[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_down.png"));
			bullet[3] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/bullet/bullet_left.png"));
			enemytank1[0] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank1_up.png"));
			enemytank1[1] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank1_right.png"));
			enemytank1[2] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank1_down.png"));
			enemytank1[3] = ImageIO.read(ImageUtill.class.getClassLoader().getResourceAsStream("images/enemytanks/enemytank1_left.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
