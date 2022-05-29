import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MyTank{
	
	public  int tank_x, tank_y;//坦克位置
	private int tank_speed = 5;//坦克速度
	private int type;
	public String name;
	private DrawPanel drawPanel = null;
	public State state = State.UP_STAY;//坦克初始状态为向上静止
	public Rectangle rectangle;
	
	public MyTank(int tank_x, int tank_y, int type, String name, DrawPanel drawPanel) {
		super();
		this.type = type;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.name = name;
		this.drawPanel = drawPanel;
		rectangle = new Rectangle(tank_x, tank_y, 60, 60);
	} 
	
//	public List<Bullet> getBullets() {
//		return bullets;
//	}

	public void paintMyself(Graphics2D g2d) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		switch (state) {
		case LEFT_MOVING:
			g2d.drawImage(ImageUtill.myTanks[type][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMoveLeft()) {// 判断坦克是否到达边界或者遇到障碍物。
				tank_x -= tank_speed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT_MOVING:
			g2d.drawImage(ImageUtill.myTanks[type][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMoveRight()) {
				tank_x += tank_speed;
				rectangle.x = tank_x;
			}
			break;
		case UP_MOVING:
			g2d.drawImage(ImageUtill.myTanks[type][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMoveUp()) {
				tank_y -= tank_speed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN_MOVING:
			g2d.drawImage(ImageUtill.myTanks[type][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMoveDown()) {
				tank_y += tank_speed;
				rectangle.y = tank_y;
			}
			break;
		case LEFT_STAY:
			g2d.drawImage(ImageUtill.myTanks[type][3], tank_x, tank_y, null);
			break;
		case RIGHT_STAY:
			g2d.drawImage(ImageUtill.myTanks[type][1], tank_x, tank_y, null);
			break;
		case UP_STAY:
			g2d.drawImage(ImageUtill.myTanks[type][0], tank_x, tank_y, null);
			break;
		case DOWN_STAY:
			g2d.drawImage(ImageUtill.myTanks[type][2], tank_x, tank_y, null);
			break;

		default:
			break;
		}
	}

	private boolean canMoveDown() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_y == obstacle.y - 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60) && (!obstacle.canCrossIn)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_y == enemyTank.tank_y - 60) && (tank_x < enemyTank.tank_x + 60) && (tank_x > enemyTank.tank_x - 60)) return false;
		}
		return true;
	}

	private boolean canMoveUp() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_y == obstacle.y + 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60) && (!obstacle.canCrossIn)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_y == enemyTank.tank_y + 60) && (tank_x < enemyTank.tank_x + 60) && (tank_x > enemyTank.tank_x - 60)) return false;
		}
		return true;
	}

	private boolean canMoveRight() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_x == obstacle.x - 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60) && (!obstacle.canCrossIn)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_x == enemyTank.tank_x - 60) && (tank_y < enemyTank.tank_y + 60) && (tank_y > enemyTank.tank_y - 60)) return false;
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_x == obstacle.x + 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60) && (!obstacle.canCrossIn)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_x == enemyTank.tank_x + 60) && (tank_y < enemyTank.tank_y + 60) && (tank_y > enemyTank.tank_y - 60)) return false;
		}
		return true;
	}

	public void fire() {
		drawPanel.bullets.add(new Bullet(tank_x, tank_y, state, "mytank"));
		new Thread(() -> new PlayWav("audio/bulletflying.wav")).start();
	}
	
}
