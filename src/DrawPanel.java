import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Runnable{
	private static final int GAME_WITH = 1260;
	private static final int GAME_HIGHT = 900;
	public boolean[] keyboardPressing;//记录正在按的键
	public boolean[] keyboardPressed;//当键盘释放时记录这个键
	public Stage nowStage = null;// 当前关卡
	public int sort;
	public BufferedImage backgroundImage = null;
	public List<MyTank> myTanks = new CopyOnWriteArrayList<>();
	public List<Bullet> bullets = new CopyOnWriteArrayList<>();
	public List<Blast> blasts = new CopyOnWriteArrayList<>();
	private long temp, begin, time;// 用于计算帧率
	private Font font = new Font("微软雅黑", Font.BOLD, 18);

	public DrawPanel() {
		setPreferredSize(new Dimension(GAME_WITH, GAME_HIGHT));// 当上一级容器不是绝对布局的时候，这里最好使用setPreferredSize。
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);
		keyboardPressing = new boolean[256];
		keyboardPressed = new boolean[256];
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
		//stages.add(new Stage(0, this));
		//stages.add(new Stage(1, this));
		sort = 0;
		nowStage = new Stage(0, this);
		backgroundImage = ImageUtill.backgrounds[0];//根据关卡生成该关卡的背景图片。
		myTanks.add(new MyTank(480, 840, 0, this));// 生成一辆我方坦克
		myTanks.add(new MyTank(720, 840, 1, this));// 生成一辆我方坦克
		new Thread(this).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);// 调用父类构造函数，绘制出基本框架（这句必须写，且必须写在第一句）
		Color c = g.getColor();
		// System.out.println(g.hashCode());通过调用g和g2d的hashcode值我们发现是相等的，强转后指向同一对象。
		// System.out.println(g2d.hashCode());
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroundImage, 0, 0, null);// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）

		for (Iterator<MyTank> iterator = myTanks.iterator(); iterator.hasNext();) {// 画出我方坦克。
			MyTank myTank = iterator.next();
			myTank.paintMyself(g2d);
		}

		for (Iterator<EnemyTank> iterator = nowStage.enemyTanks.iterator(); iterator.hasNext();) {//画出地方坦克。
			EnemyTank enemyTank = iterator.next();
			enemyTank.drawMyself(g2d);
		}

		// 子弹的碰撞检测后画出。
		outer: for (Bullet bullet : bullets) {
			for (MyTank myTank : myTanks) {
				if ((bullet.owner.equals("enemytank")) && (myTank.rectangle.contains(bullet.rectangle))) {
					//System.out.println("mytank!!!!");
					blasts.add(new Blast(myTank.tank_x, myTank.tank_y, 0));
					new Thread(() -> new PlayWav("audio/tank_blast.wav")).start();
					bullets.remove(bullet);
					myTanks.remove(myTank);
					break outer;
				}
			}

			for (EnemyTank enemyTank : nowStage.enemyTanks) {
				if ((bullet.owner.equals("mytank")) && (enemyTank.rectangle.contains(bullet.rectangle))) {
					//System.out.println("enemytank!!!!");
					blasts.add(new Blast(enemyTank.tank_x, enemyTank.tank_y, 0));
					new Thread(() -> new PlayWav("audio/tank_blast.wav")).start();
					enemyTank.isalive = false;
					bullets.remove(bullet);
					nowStage.enemyTanks.remove(enemyTank);
					break outer;
				}
			}
			for (Obstacle obstacle : nowStage.obstacles) {
				if (new Rectangle(obstacle.x, obstacle.y, 60, 60).intersects(bullet.rectangle)) {
					//System.out.println("obstacle!!!!");
					if (!obstacle.canCrossIn) {
						if (obstacle.canDisdroyed) {
							blasts.add(new Blast(obstacle.x, obstacle.y, 0));
							new Thread(() -> new PlayWav("audio/obstacle_blast.wav")).start();
							nowStage.obstacles.remove(obstacle);
							bullets.remove(bullet);
						} else {
							blasts.add(new Blast(obstacle.x, obstacle.y, 2));
							new Thread(() -> new PlayWav("audio/steel_blast.wav")).start();
							bullets.remove(bullet);
						}
						break outer;
					}

				}
			}
			if (nowStage.base.isalive && nowStage.base.rectangle.contains(bullet.rectangle)) {
				//System.out.println("base!!!!");
				blasts.add(new Blast(nowStage.base.x, nowStage.base.y, 1));
				new Thread(() -> new PlayWav("audio/base_blast.wav")).start();
				nowStage.base.isalive = false;
				bullets.remove(bullet);
				break outer;
			}
			if (bullet.bullet_x < 0 || bullet.bullet_x > 1260 || bullet.bullet_y < 0 || bullet.bullet_y > 900) {
				bullets.remove(bullet);
				break outer;
			}
			bullet.drawMyself(g2d);
		}

		for (Iterator<Obstacle> iterator = nowStage.obstacles.iterator(); iterator.hasNext();) {// 画出当前关卡障碍物。
			Obstacle obstacle = iterator.next();
			g2d.drawImage(obstacle.show, obstacle.x, obstacle.y, this);
		}

		g2d.drawImage(nowStage.base.getshow(), nowStage.base.x, nowStage.base.y, this);// 画出主基地。
		
		for (Blast blast : blasts) {//画出爆炸，每一帧画一张。
			if(blast.step <= blast.sum - 1) blast.drawMyself(g2d);
			else {
				blasts.remove(blast);
			}
		}

		// 计算帧率并绘制字符串
		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(font);
		g2d.drawString("爆炸数量：" + blasts.size(), 30, 770);
		begin = System.currentTimeMillis();
		time = begin - temp;
		if (time != 0)
			g.drawString("fps：" + (int) (1000 / (time)), 30, 800);
		temp = begin;
		g2d.drawString("子弹数量：" + (bullets.size()), 30, 830);
		g2d.drawString("敌方坦克数量：" + nowStage.enemyTanks.size(), 30, 860);
		g2d.drawString("我方坦克数量：" + myTanks.size(), 30, 890);
		g.setColor(c);
	}

	// 处理dpanel接收到的keyPressed键盘事件e，并改变控制坦克所需的state，供drawMyself函数画图
	private class ControlKeyListener extends KeyAdapter {

		@Override // 用于坦克移动
		public void keyPressed(KeyEvent e) {
			keyboardPressing[e.getKeyCode()] = true;
			keyboardPressed[e.getKeyCode()] = false;
		}

		@Override // 用于坦克恢复静止以及发射子弹
		public void keyReleased(KeyEvent e) {// 键盘抬起触发一次
			keyboardPressing[e.getKeyCode()] = false;
			for(int i = 0; i < 256; i++) keyboardPressed[i] = false;
			keyboardPressed[e.getKeyCode()] = true;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (true) {
			if (nowStage.base.isalive == false || myTanks.isEmpty()) {
				System.out.println("game over!!!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				nowStage.thread.stop();
				myTanks.clear();
				bullets.clear();
				blasts.clear();
				nowStage.clear();
				for(int i = 0; i < 256; i++) keyboardPressed[i] = false;
				nowStage = new Stage(sort, this);
				myTanks.add(new MyTank(480, 840, 0, this));
				myTanks.add(new MyTank(720, 840, 1, this));
			}
			if(nowStage.enemyTanks.isEmpty() && (nowStage.count == nowStage.totalEenemyTankCount)) {
				System.out.println("you win!!!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bullets.clear();
				nowStage.clear();
				for(int i = 0; i < 256; i++) keyboardPressed[i] = false;
				sort++;
				nowStage = new Stage(sort, this);
				for (MyTank myTank : myTanks) {
					if(myTank.player == 0) {
						myTank.tank_x = 480;
						myTank.tank_y = 840;
						myTank.state = State.UP_STAY;
					}
					if(myTank.player == 1) {
						myTank.tank_x = 720;
						myTank.tank_y = 840;
						myTank.state = State.UP_STAY;
					}
					
				}
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
