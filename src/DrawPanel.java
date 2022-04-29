import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	private final int GAME_WITH = 1200;
	private final int GAME_HIGHT = 900;
	private Tank myTank;
	private ArrayList<EnemyTank> enemytanks = new ArrayList<>();
	private long temp, begin, time;//用于计算帧率
	private Random r = new Random();//用于产生随机产生敌人坦克的类型和位置。
	public DrawPanel() {
		super();
		setPreferredSize(new Dimension(GAME_WITH, GAME_HIGHT));//当上一级容器不是绝对布局的时候，这里最好使用setPreferredSize。
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
		myTank = new Tank(1140, 840);
		for (int i = 0; i < 10; i++) {//生成地方坦克并加入集合
			enemytanks.add(new EnemyTank(r.nextInt(1140), r.nextInt(840), r.nextInt(3)));
		}
		for (EnemyTank enemyTank : enemytanks) {//启动地方坦克线程
			new Thread(enemyTank).start();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color c = g.getColor();
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(ImageUtill.backgrounds[0], 0, 0, null);
//		System.out.println(g.hashCode());通过调用g和g2d的hashcode值我们发现是相等的，强转后指向同一对象。
//		System.out.println(g2d.hashCode());
		
		// 绘制背景（注意：背景需要最先画，否则背景处于最上层，看不到其他图形了）
		//g2d.setColor(new Color(255, 200, 200));
		//g2d.fillRect(0, 0, getWidth(), getHeight());
		//绘制字符串
		g2d.setColor(Color.blue);
		//g2d.drawString("子弹数量："+ (myTank.getBullets().size() + enemyTank.getBullets().size()), 100, 100);
		// 将dpanel的g2d画笔传给myTank来绘制坦克和子弹
		myTank.paintMyself(g2d);
		for (EnemyTank enemyTank : enemytanks) {//遍历敌方坦克，如果还活着就画出来，否则就从集合中删除。
			if (enemyTank.isalive) {
				enemyTank.paintMyself(g2d);
			}else {
				enemytanks.remove(enemyTank);
			}
		}
		//enemyTank.paintMyself(g2d);
		
		//计算帧率
		begin = System.currentTimeMillis();
		time = begin-temp;
		if(time != 0) g.drawString("fps："+(int)(1000/(time)), 100, 50);
		temp = begin;
		
		g.setColor(c);
	}

	
	//将dpanel接收到的键盘信息都发送给坦克类的实例，由实例去实现各个键的功能。
	//为了提高效率也可以在传之前先判断一下按键信息，如果是属于这个坦克类的控制键再传递给该坦克类。
	private class ControlKeyListener extends KeyAdapter {
		
		@Override//用于坦克移动
		public void keyPressed(KeyEvent e) {// 键盘按下去，大约20-40毫秒触发一次
			myTank.setKeyPressedEvent(e);//将得到的keyEvent传给myTank
		}

		@Override//用于坦克恢复静止以及发射子弹
		public void keyReleased(KeyEvent e) {//键盘抬起触发一次
			myTank.setKeyReleasedEvent(e);//将得到的keyEvent传给myTank
		}

		
		

	}

}
