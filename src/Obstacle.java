import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Obstacle {
	public static final int STEEL = 0, STONE = 1, GRASS = 2;
	public int x, y;
	public int type;//0:钢铁  1：石头 2：草丛
	public BufferedImage show;
	public boolean canDisdroyed;
	public boolean canCrossIn;
	public int width, height;
	public Rectangle rectangle;
	
	public Obstacle(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		switch (this.type) {
		case STEEL:
			this.canCrossIn = false;
			this.canDisdroyed = false;
			break;
		case STONE:
			this.canCrossIn = false;
			this.canDisdroyed = true;
			break;
		case GRASS:
			this.canCrossIn = true;
			this.canDisdroyed = false;
			break;

		default:
			break;
		}
		show = ResourceRepertory.obstacles[type];
		width = ResourceRepertory.obstacles[type].getWidth();
		height = ResourceRepertory.obstacles[type].getHeight();
		rectangle = new Rectangle(x, y, width, height);
	}
	
}
