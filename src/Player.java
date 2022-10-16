import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class Player {
	public static int totalCount;//玩家坦克总数量
	private static Properties controlKeysProperty;// 控制键配置信息
	private int[] controlKeys = new int[5];//设置五个控制键，前四个是方向，最后一个是发射子弹。
	public int score = 0;//总得分
	public int count = 3;//初始坦克数量
	public int start_x, start_y;// 坦克初始位置
	public int myTankType;//所拥有的坦克类型
	public String name;
	public MyTank fightingTank;//正在运行的坦克
	public Queue<MyTank> myTankQueue = new LinkedList<>();//坦克队列
	
	static {
		controlKeysProperty = new Properties();
		try {
			controlKeysProperty.load(
					Player.class.getClassLoader().getResourceAsStream("controlkeys.properies"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Player(int myTankType, String name) {
		String propertyValue;
		String[] propertyValues;
		this.myTankType = myTankType;
		this.name = name;
		switch (myTankType) {
		case 0:
			this.start_x = 480;
			this.start_y = 840;
			break;
		case 1:
			this.start_x = 720;
			this.start_y = 840;
			break;
		default:
			break;
		}
		propertyValue = controlKeysProperty.getProperty(name);
		propertyValues = propertyValue.split(",");
		for (int i = 0; i < propertyValues.length; i++) {
			controlKeys[i] = Integer.parseInt(propertyValues[i]);
		}
		for(int i = 0; i < count; i++) {
			myTankQueue.offer(new MyTank(start_x, start_y, this.myTankType, this.name, this.controlKeys));
			totalCount++;
		}
		fightingTank = myTankQueue.poll();
		fightingTank.keyboardThread.start();
	}
	
	public void fightTankDestroyed() {
			totalCount -= 1;
			count -= 1;
			fightingTank.isAlive = false;
			fightingTank = null;
	}
	
	public void creatFightTank() {
		new Thread(() -> {
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(DrawPanel.nowStage.base.isalive && myTankQueue.size() != 0) {
				fightingTank = myTankQueue.poll();
				fightingTank.keyboardThread.start();
			}
		}).start();
	}
}
