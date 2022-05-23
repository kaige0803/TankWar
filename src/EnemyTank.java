import java.awt.Graphics2D;
import java.util.Random;

public class EnemyTank implements Runnable{
	
	public int tank_x, tank_y;//坦克位置
	private int tank_speed = 5;//坦克速度
	private int type;
	private DrawPanel drawPanel = null;
	private Random r = new Random();//用于产生随机方向和随机的时间间隔。
	private State state = State.DOWN_MOVING;//初始方向向下运动。
	public boolean isalive = true;
	public EnemyTank(int tank_x, int tank_y, int type, DrawPanel drawPanel) {
		super();
		this.type = type;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.drawPanel = drawPanel;
		new Thread(this).start();//开启地方坦克线程。
	}

//	public List<Bullet> getBullets() {
//		return bullets;
//	}

	public void paintMyself(Graphics2D g2d) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		switch (state) {
		case LEFT_MOVING:
			g2d.drawImage(ImageUtill.enemyTank[type][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMoveLeft())
				tank_x -= tank_speed;
			break;
		case RIGHT_MOVING:
			g2d.drawImage(ImageUtill.enemyTank[type][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMoveRight())
				tank_x += tank_speed;
			break;
		case UP_MOVING:
			g2d.drawImage(ImageUtill.enemyTank[type][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMoveUp())
				tank_y -= tank_speed;
			break;
		case DOWN_MOVING:
			g2d.drawImage(ImageUtill.enemyTank[type][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMoveDown())
				tank_y += tank_speed;
			break;
		case LEFT_STAY:
			g2d.drawImage(ImageUtill.enemyTank[type][3], tank_x, tank_y, null);
			break;
		case RIGHT_STAY:
			g2d.drawImage(ImageUtill.enemyTank[type][1], tank_x, tank_y, null);
			break;
		case UP_STAY:
			g2d.drawImage(ImageUtill.enemyTank[type][0], tank_x, tank_y, null);
			break;
		case DOWN_STAY:
			g2d.drawImage(ImageUtill.enemyTank[type][2], tank_x, tank_y, null);
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
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_y == myTank.tank_y - 60) && (tank_x < myTank.tank_x + 60) && (tank_x > myTank.tank_x - 60)) return false;
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
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_y == myTank.tank_y + 60) && (tank_x < myTank.tank_x + 60) && (tank_x > myTank.tank_x - 60)) return false;
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
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_x == myTank.tank_x - 60) && (tank_y < myTank.tank_y + 60) && (tank_y > myTank.tank_y - 60)) return false;
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
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_x == myTank.tank_x + 60) && (tank_y < myTank.tank_y + 60) && (tank_y > myTank.tank_y - 60)) return false;
		}
		return true;
	}
	
	@Override
	public void run() {
		while(isalive) {
			drawPanel.bullets.add(new Bullet(tank_x, tank_y, state, "enemytank"));
			try {//随机加入子弹
				Thread.sleep(500 + r.nextInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//随机生成坦克状态
			state = State.values()[r.nextInt(8)];
			try {
				Thread.sleep(1000 + r.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
