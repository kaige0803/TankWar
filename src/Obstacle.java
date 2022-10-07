import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Obstacle {
	public static final int STEEL = 0, STONE = 1, GRASS = 2;
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
		if(type == STEEL) canDisdroyed = false; else canDisdroyed = true;//只有钢铁无法被摧毁。
		if(type == GRASS) canCrossIn = true; else canCrossIn = false;//只有草丛可以穿越。
		show = ResourceRepertory.obstacles[type];
		rectangle = new Rectangle(x, y, 60, 60);
	}
	
}
