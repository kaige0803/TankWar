import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Blast {
	public int x, y;
	private BufferedImage[] blasts_images ;
	public int type;
	public int step = 0;
	public int sum;//爆炸图片的总张数
	public Blast(int x, int y, int type) {
		super();
		this.type = type;
		if(type == 0) {
			blasts_images = ImageUtill.tank_blasts;
			this.x = x - 50;
			this.y = y - 30;
			}
		if(type == 1) {
			blasts_images = ImageUtill.base_blasts;
			this.x = x - 270;
			this.y = y - 500;
			}
		if(type == 2) {
			blasts_images = ImageUtill.steel_blasts;
			this.x = x + 15;
			this.y = y + 10;
		}
		sum = blasts_images.length;
	}

	public void drawMyself(Graphics g) {
			g.drawImage(blasts_images[step], x, y, null);
			step += 1;
	}
}
