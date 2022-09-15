import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Base {
	public int x, y;
	public boolean isalive;
	public BufferedImage show = null;
	public Rectangle rectangle;
	
	public Base(int x, int y) {
		isalive = true;
		this.x = x;
		this.y = y;
		rectangle = new Rectangle(x, y, 60, 60);
	}

	public Image getshow() {
		if(isalive) {
			return ResourceRepertory.base[0];
		}else {
			return ResourceRepertory.base[1];
		}
	}
	
}
