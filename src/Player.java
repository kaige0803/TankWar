import java.util.LinkedList;
import java.util.Queue;

public class Player {
	public static int totalCount;
	public int score = 0;//总得分
	public int count = 3;//坦克数量
	public int myTankType;
	public String name;
	public MyTank fightingTank;//正在运行的坦克
	public Queue<MyTank> myTankQueue = new LinkedList<>();//坦克队列
	
	public Player(int myTankType, String name) {
		super();
		this.myTankType = myTankType;
		this.name = name;
		for(int i = 0; i < count; i++) {
			myTankQueue.offer(new MyTank(this.myTankType, this.name));
			totalCount++;
		}
		fightingTank = myTankQueue.poll();
		fightingTank.keyboardThread.start();
	}
	
	public boolean fightTankDestroyed() {
		if (myTankQueue.size() != 0) {
			count -= 1;
			totalCount -= 1;
			fightingTank = null;
			return false;
		}else {
			return true;
		}
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
