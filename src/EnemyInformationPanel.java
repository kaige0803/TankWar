import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class EnemyInformationPanel extends JPanel implements Runnable{
	
	public Font font = new Font("微软雅黑", Font.BOLD, 14);
	public List<EnemyTank> CopyOfEnemyQueue;
	public Thread thread;
	public EnemyInformationPanel() {
		this.setPreferredSize(new Dimension(InformationPanel.WIDTH, 600));
		this.setBackground(Color.DARK_GRAY);
		this.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
														"Enemy_Tanks", 
														TitledBorder.DEFAULT_JUSTIFICATION, 
														TitledBorder.DEFAULT_POSITION, 
														font, Color.WHITE));
		thread = new Thread(this);
		thread.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		int x = 0, y = 0;
		super.paintComponent(g);
		for(EnemyTank enemyTank : DrawPanel.nowStage.queueOfEnemyTanks) {
			g.drawImage(ResourceRepertory.enemyTankIcon[enemyTank.type], 30 + x*50, 40 + y*50, null);
			x += 1;
			if(x % 3 == 0) {
				x = 0;
				y += 1;
			}
			
		}
	}

	@Override
	public void run() {
		//更新enemyInformationPanel面板数据
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	
}


