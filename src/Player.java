import java.util.LinkedList;
import java.util.Queue;

public class Player {
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
			//myTankQueue.offer(new MyTank(this.myTankType, this.name));
		}
		fightingTank = myTankQueue.poll();
		System.out.println(fightingTank);
	}
	
}
