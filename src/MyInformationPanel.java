import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class MyInformationPanel extends JPanel implements Runnable {
	public JLabel player1Icon = new JLabel(new ImageIcon(ResourceRepertory.myTankIcon[0]));
	public JLabel player2Icon = new JLabel(new ImageIcon(ResourceRepertory.myTankIcon[1]));
	public JLabel fieldPlayer = new JLabel("<html><font size=5 color=white face=微软雅黑>Player</font></html>", JLabel.CENTER);
	public JLabel fieldLifeCount = new JLabel("<html><font size=5 color=white face=微软雅黑>Life</font></html>", JLabel.CENTER);
	public JLabel fieldScore = new JLabel("<html><font size=5 color=white face=微软雅黑>Score</font></html>", JLabel.CENTER);
	public JLabel player1LifeCount = new JLabel("<html><font size=5 color=white face=微软雅黑>00</font></html>", JLabel.CENTER);
	public JLabel player2LifeCount = new JLabel("<html><font size=5 color=white face=微软雅黑>00</font></html>", JLabel.CENTER);
	public JLabel player1Score = new JLabel("<html><font size=5 color=white face=微软雅黑>00000</font></html>", JLabel.CENTER);
	public JLabel player2Score = new JLabel("<html><font size=5 color=white face=微软雅黑>00000</font></html>", JLabel.CENTER);
	private Font font = new Font("微软雅黑", Font.BOLD, 14);
	private Thread thread;
	
	public MyInformationPanel() {
		super();
		//设置并组装myInformationPanel面板。
				setLayout(new GridLayout(3, 3));
				setPreferredSize(new Dimension(InformationPanel.WIDTH,180));
				setBackground(Color.DARK_GRAY);
				setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
		                                                                     "My_Tanks", 
		                                                                     TitledBorder.DEFAULT_JUSTIFICATION, 
		                                                                     TitledBorder.DEFAULT_POSITION, 
		                                                                     font, Color.WHITE));
				add(fieldPlayer);
				add(fieldLifeCount);
				add(fieldScore);
				
				add(player1Icon);
				add(player1LifeCount);
				add(player1Score);
				
				add(player2Icon);
				add(player2LifeCount);
				add(player2Score);
				thread = new Thread(this);
				thread.start();
	}

	@Override
	public void run() {
		//更新myInformationPanel面板数据
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			for (Player player : DrawPanel.players) {
				switch (player.name) {
				case "player1":
					player1LifeCount
							.setText("<html><font size=6 color=white face=微软雅黑>" + player.count + "</font></html>");
					player1Score.setText("<html><font size=6 color=white face=微软雅黑>" + player.score + "</font></html>");
					break;
				case "player2":
					player2LifeCount
							.setText("<html><font size=6 color=white face=微软雅黑>" + player.count + "</font></html>");
					player2Score.setText("<html><font size=6 color=white face=微软雅黑>" + player.score + "</font></html>");
					break;

				default:
					break;
				}
			}
		}
	}
}
