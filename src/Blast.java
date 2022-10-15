import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Blast {
	public int x, y;
	private BufferedImage[] blastsImages ;
	public int type;
	public int step = 0;
	public int sum;//爆炸图片的总张数

	public Blast(int x, int y, int type) {
		this.type = type;
		if (type == 0) {
			blastsImages = ResourceRepertory.tankBlasts;
			this.x = x - 50;
			this.y = y - 30;
		}
		if (type == 1) {
			blastsImages = ResourceRepertory.baseBlasts;
			this.x = x - 270;
			this.y = y - 500;
		}
		if (type == 2) {
			blastsImages = ResourceRepertory.steelBlasts;
			this.x = x + 15;
			this.y = y + 10;
		}
		sum = blastsImages.length;
	}

	public void drawMyself(Graphics g) {
			g.drawImage(blastsImages[step], x, y, null);
			step += 1;
	}
}
