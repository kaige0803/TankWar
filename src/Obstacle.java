import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Obstacle {
	public int x, y;
	public int type;//0:钢铁  1：石头 2：草丛
	public BufferedImage show;
	public boolean canDisdroyed;
	public boolean canCrossIn;
	public Rectangle rectangle;
	
	public Obstacle(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		if(type == 0) canDisdroyed = false; else canDisdroyed = true;//只有钢铁无法被摧毁。
		if(type == 2) canCrossIn = true; else canCrossIn = false;//只有草丛可以穿越。
		show = ImageUtill.obstacles[type];
		rectangle = new Rectangle(x, y, 60, 60);
	}
	
}
