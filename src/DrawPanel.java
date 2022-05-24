import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	private final int GAME_WITH = 1260;
	private final int GAME_HIGHT = 900;
	public Iterator<Bullet> iterator = null;
	private List<Stage> stages = new ArrayList<>();// 存储每一关的场景
	public Stage nowStage = null;// 当前关卡
	public List<MyTank> myTanks = new ArrayList<>();
	public List<Bullet> bullets = new ArrayList<>();
	private long temp, begin, time;// 用于计算帧率
	private Font font = new Font("微软雅黑", Font.BOLD, 18);

	public DrawPanel() {
		setPreferredSize(new Dimension(GAME_WITH, GAME_HIGHT));// 当上一级容器不是绝对布局的时候，这里最好使用setPreferredSize。
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
		Stage stage0 = new Stage(0, this);
		// Stage stage1 = new Stage(1, this);
		stages.add(stage0);
		// stages.add(stage1);
		nowStage = stages.get(0);
		myTanks.add(new MyTank(480, 840, 0, this));// 生成一辆我方坦克
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);// 调用父类构造函数，绘制出基本框架（这句必须写，且必须写在第一句）
		Color c = g.getColor();
		// System.out.println(g.hashCode());通过调用g和g2d的hashcode值我们发现是相等的，强转后指向同一对象。
		// System.out.println(g2d.hashCode());
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(nowStage.backgroundImage, 0, 0, null);// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）

		for (Iterator<MyTank> iterator = myTanks.iterator(); iterator.hasNext();) {// 遍历当前关卡我方坦克，如果还活着就画出来，否则就从集合中删除。
			MyTank myTank = iterator.next();
			myTank.paintMyself(g2d);
		}

		for (Iterator<EnemyTank> iterator = nowStage.enemyTanks.iterator(); iterator.hasNext();) {
			EnemyTank enemyTank = iterator.next();
			enemyTank.paintMyself(g2d);
		}

		// 子弹的碰撞检测。

		outer: for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext();) {
			Bullet bullet = iterator.next();
			Iterator<MyTank> iterator2 = myTanks.iterator();
			while (iterator2.hasNext()) {
				MyTank myTank = iterator2.next();
				if ((bullet.owner.equals("enemytank")) && (new Rectangle(myTank.tank_x, myTank.tank_y, 60, 60)
						.contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6)))) {
					//System.out.println("mytank!!!!");
					iterator.remove();
					iterator2.remove();
					break outer;
				}
			}

			for (Iterator<EnemyTank> iterator3 = nowStage.enemyTanks.iterator(); iterator3.hasNext();) {
				EnemyTank enemyTank = iterator3.next();
				if ((bullet.owner.equals("mytank")) && (new Rectangle(enemyTank.tank_x, enemyTank.tank_y, 60, 60)
						.contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6)))) {
					//System.out.println("enemytank!!!!");
					enemyTank.isalive = false;
					iterator.remove();
					iterator3.remove();
					break outer;
				}
			}
			for (Iterator<Obstacle> iterator4 = nowStage.obstacles.iterator(); iterator4.hasNext();) {
				Obstacle obstacle = iterator4.next();
				if (new Rectangle(obstacle.x, obstacle.y, 60, 60)
						.contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
					//System.out.println("obstacle!!!!");
					if (!obstacle.canCrossIn) {
						if (obstacle.canDisdroyed) {
							iterator4.remove();
							iterator.remove();
						} else {
							iterator.remove();
						}
						break outer;
					}

				}
			}
			if (new Rectangle(nowStage.base.x, nowStage.base.y, 60, 60)
					.contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
				//System.out.println("base!!!!");
				nowStage.base.isalive = false;
				iterator.remove();
				break outer;
			}
			if (bullet.bullet_x < 0 || bullet.bullet_x > 1260 || bullet.bullet_y < 0 || bullet.bullet_y > 900) {
				iterator.remove();
				break outer;
			}
			bullet.drawMyself(g2d);
		}

		for (Iterator<Obstacle> iterator = nowStage.obstacles.iterator(); iterator.hasNext();) {// 遍历当前关卡障碍物。
			Obstacle obstacle = iterator.next();
			g2d.drawImage(obstacle.show, obstacle.x, obstacle.y, this);
		}

		g2d.drawImage(nowStage.base.show, nowStage.base.x, nowStage.base.y, this);// 画出主基地。

		// 计算帧率并绘制字符串
		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(font);
		begin = System.currentTimeMillis();
		time = begin - temp;
		if (time != 0)
			g.drawString("fps：" + (int) (1000 / (time)), 30, 790);
		temp = begin;
		g2d.drawString("子弹数量：" + (bullets.size()), 30, 830);
		g2d.drawString("敌方坦克数量：" + nowStage.enemyTanks.size(), 30, 870);
		g.setColor(c);
	}

	// 处理dpanel接收到的keyPressed键盘事件e，并改变状态所需要控制的坦克的state，供paintMyself函数画图
	private class ControlKeyListener extends KeyAdapter {

		@Override // 用于坦克移动
		public void keyPressed(KeyEvent e) {// 键盘按下去，大约20-40毫秒触发一次
			switch (e.getKeyCode()) {// 如果用swith的话只能垂直和水平走，因为swith中有break 所以只能响应最先按下的键。如果用if-else就可以斜着走了。
			case KeyEvent.VK_W:
				myTanks.get(0).state = State.UP_MOVING;
				break;
			case KeyEvent.VK_S:
				myTanks.get(0).state = State.DOWN_MOVING;
				break;
			case KeyEvent.VK_A:
				myTanks.get(0).state = State.LEFT_MOVING;
				break;
			case KeyEvent.VK_D:
				myTanks.get(0).state = State.RIGHT_MOVING;
				break;
			default:
				break;
			}
		}

		@Override // 用于坦克恢复静止以及发射子弹
		public void keyReleased(KeyEvent e) {// 键盘抬起触发一次
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				myTanks.get(0).state = State.UP_STAY;
				break;
			case KeyEvent.VK_S:
				myTanks.get(0).state = State.DOWN_STAY;
				break;
			case KeyEvent.VK_A:
				myTanks.get(0).state = State.LEFT_STAY;
				break;
			case KeyEvent.VK_D:
				myTanks.get(0).state = State.RIGHT_STAY;
				break;
			case KeyEvent.VK_H:
				myTanks.get(0).fire();
				break;
			default:
				break;
			}
		}
	}
}
