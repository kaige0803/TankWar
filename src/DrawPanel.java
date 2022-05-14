import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.Random;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Runnable {
	private final int GAME_WITH = 1260;
	private final int GAME_HIGHT = 900;
	public Iterator<Bullet> iterator = null;
	private List<Stage> stages = new ArrayList<>();//存储每一关的场景
	public Stage nowStage = null;//当前关卡
	public List<MyTank> myTanks = new ArrayList<>();
	private long temp, begin, time;//用于计算帧率
	//private Random r = new Random();//用于产生随机产生敌人坦克的类型和位置。
	private  Thread checkCrashThread  = null;//用于检测子弹和坦克的碰撞
	public DrawPanel() {
		setPreferredSize(new Dimension(GAME_WITH, GAME_HIGHT));//当上一级容器不是绝对布局的时候，这里最好使用setPreferredSize。
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);  
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
		
		Stage stage0 = new Stage(0, this);
		Stage stage1 = new Stage(1, this);
		stages.add(stage0);
		stages.add(stage1);
		nowStage = stages.get(0);
		myTanks.add(new MyTank(480, 840, 0, this));//生成一辆我方坦克
		checkCrashThread = new Thread(this);//创建检测线程
		checkCrashThread.start();//启动检测线程
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);//调用父类构造函数，绘制出基本框架（这句必须写，且必须写在第一句）
		Color c = g.getColor();
//		System.out.println(g.hashCode());通过调用g和g2d的hashcode值我们发现是相等的，强转后指向同一对象。
//		System.out.println(g2d.hashCode());
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(nowStage.backgroundImage, 0, 0, null);// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）
		//g2d.drawString("子弹数量："+ (myTanks.get(0).getBullets().size() + nowStage.enemyTanks.getBullets().size()), 100, 100);
		// 将dpanel的g2d画笔传给myTank来绘制坦克和子弹
		
		for (Iterator<MyTank> iterator = myTanks.iterator(); iterator.hasNext();) {//遍历当前关卡我方坦克，如果还活着就画出来，否则就从集合中删除。
			MyTank myTank = iterator.next();
			if(myTank.isalive) {
				myTank.paintMyself(g2d);
			}else {
				iterator.remove();
			}
		}
		
		for(Iterator<EnemyTank> iterator = nowStage.enemyTanks.iterator(); iterator.hasNext();) {
			EnemyTank enemyTank = iterator.next();
			if (enemyTank.isalive) {
				enemyTank.paintMyself(g2d);
			}else {
				iterator.remove();
			}
		}
		
		for (Iterator<Obstacle> iterator = nowStage.obstacles.iterator(); iterator.hasNext();) {//遍历当前关卡障碍物，如果还活着就画出来，否则就从集合中删除。
			Obstacle obstacle = iterator.next();
			if (obstacle.isalive) {
			g2d.drawImage(obstacle.show, obstacle.x, obstacle.y, this);
			}else {
				iterator.remove();
			}
		}
		
		g2d.drawImage(nowStage.base.show, nowStage.base.x, nowStage.base.y, this);//画出主基地。
		
		//计算帧率并绘制字符串
		g2d.setColor(Color.blue);
		begin = System.currentTimeMillis();
		time = begin-temp;
		if(time != 0) g.drawString("fps："+(int)(1000/(time)), 100, 50);
		temp = begin;
		
		g.setColor(c);
	}

	//处理dpanel接收到的keyPressed键盘事件e，并改变状态所需要控制的坦克的state，供paintMyself函数画图
	private class ControlKeyListener extends KeyAdapter {
		
		@Override//用于坦克移动
		public void keyPressed(KeyEvent e) {// 键盘按下去，大约20-40毫秒触发一次
			switch (e.getKeyCode()) {//如果用swith的话只能垂直和水平走，因为swith中有break 所以只能响应最先按下的键。如果用if-else就可以斜着走了。
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
		
		@Override//用于坦克恢复静止以及发射子弹
		public void keyReleased(KeyEvent e) {//键盘抬起触发一次
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

	@Override
	public void run() {//用于检测子弹和坦克的碰撞并刷新dpanel。
		while(true) {
			repaint();
			
			//检测我方坦克的子弹是否遇到敌方方坦克、主基地或者障碍物。
			for (MyTank myTank : myTanks) {
				iterator = myTank.bullets.iterator();
				outer:
				while(iterator.hasNext()) {
					Bullet bullet = (Bullet) iterator.next();
					//是否碰到敌方坦克？
					for (EnemyTank enemyTank : nowStage.enemyTanks) {
						if(new Rectangle(enemyTank.tank_x, enemyTank.tank_y, 60, 60).contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
System.out.println("enemytank!!!!");
						//显示爆炸和音效
						//将我方子弹和敌方坦克从列表中去除
							enemyTank.isalive = false;
							bullet.isalve = false;
							break outer;
						}
					}
					//是否碰到障碍物？
					for (Obstacle obstacle : nowStage.obstacles) {
						if(new Rectangle(obstacle.x, obstacle.y, 60, 60).contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
System.out.println("obstacle!!!!");
							obstacle.isalive = false;
							bullet.isalve = false;
							break outer;
						}
					}
					//是否碰到主基地？
					if(new Rectangle(nowStage.base.x, nowStage.base.y, 60, 60).contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
System.out.println("base!!!!");
						nowStage.base.isalive = false;
						bullet.isalve = false;
						break outer;
					}
				}
			}
			
			
			
			//检测敌方坦克子弹是否遇到我方坦克、主基地或者障碍物。
			for (EnemyTank enemyTank : nowStage.enemyTanks) {
				iterator = enemyTank.bullets.iterator();
				outer:
				while(iterator.hasNext()) {
					Bullet bullet = (Bullet) iterator.next();
					//是否碰到我方坦克？
					for (MyTank myTank : myTanks) {
						if(new Rectangle(myTank.tank_x, myTank.tank_y, 60, 60).contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
System.out.println("mytank!!!!");
							myTank.isalive = false;
							bullet.isalve = false;
							break outer;
						}
					}
					//是否碰到障碍物？
					for (Obstacle obstacle : nowStage.obstacles) {
						if(new Rectangle(obstacle.x, obstacle.y, 60, 60).contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
System.out.println("obstacle!!!!");
							obstacle.isalive = false;
							bullet.isalve = false;
							break outer;
						}
					}
					//是否碰到主基地？
					if(new Rectangle(nowStage.base.x, nowStage.base.y, 60, 60).contains(new Rectangle(bullet.bullet_x, bullet.bullet_y, 6, 6))) {
System.out.println("base!!!!");
                        nowStage.base.isalive = false;
                        bullet.isalve = false;
                        break outer;
					}
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
