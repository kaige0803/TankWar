import java.util.LinkedList;
import java.util.Queue;

public class Player {
	public static int totalCount;
	public int score = 0;//总得分
	public int count = 3;//初始坦克数量
	public int start_x, start_y;// 坦克初始位置
	public int myTankType;
	public String name;
	public MyTank fightingTank;//正在运行的坦克
	public Queue<MyTank> myTankQueue = new LinkedList<>();//坦克队列
	
	public Player(int myTankType, String name) {
		super();
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
		for(int i = 0; i < count; i++) {
			myTankQueue.offer(new MyTank(start_x, start_y, this.myTankType, this.name));
			totalCount++;
		}
		fightingTank = myTankQueue.poll();
		fightingTank.keyboardThread.start();
	}
	
	public void fightTankDestroyed() {
			totalCount -= 1;
			fightingTank.isAlive = false;
			fightingTank = null;
			System.out.println(myTankQueue.size());
			System.out.println(totalCount);
	}
	
	public void creatFightTank() {
		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(DrawPanel.nowStage.base.isalive) {
				fightingTank = myTankQueue.poll();
				fightingTank.keyboardThread.start();
			}
		}).start();
		
	}
	
	
	
}
