import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Base {
	public int x, y;
	public int width, height;
	public boolean isalive;
	public BufferedImage show = null;
	public Rectangle rectangle;
	
	public Base(int x, int y) {
		isalive = true;
		this.x = x;
		this.y = y;
		this.width = ResourceRepertory.base[0].getWidth();
		this.height = ResourceRepertory.base[0].getHeight();
		rectangle = new Rectangle(x, y, width, height);
	}

	public Image getshow() {
		if(isalive) {
			return ResourceRepertory.base[0];
		}else {
			return ResourceRepertory.base[1];
		}
	}
	
}
