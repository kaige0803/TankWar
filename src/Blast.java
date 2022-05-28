import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Blast {
	public int x, y;
	private BufferedImage[] tank_blasts_images = ImageUtill.tank_blasts;
	public int step = 0;
	
	public Blast(int x, int y) {
		super();
		this.x = x - 50;
		this.y = y - 30;
	}

	public void drawMyself(Graphics2D g2d) {
			g2d.drawImage(tank_blasts_images[step], x, y, null);
			step += 1;
	}
}
