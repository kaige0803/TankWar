
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class InformationPanel extends JPanel implements Runnable {
	private final int WHITH = 200;
	public int fps = 0;
	public JLabel fpsLabel = new JLabel("<html><font size=6 color=white face=微软雅黑>FPS :</font></html>", JLabel.CENTER);
	public JLabel fpsData = new JLabel("00", JLabel.CENTER);
	public JLabel stageLabel = new JLabel("<html><font size=6 color=white face=微软雅黑>STAGE :</font></html>", JLabel.CENTER);
	public JLabel stageData = new JLabel("00", JLabel.CENTER);
	public JLabel player1Icon = new JLabel(new ImageIcon(InformationPanel.class.getResource("images/icon/player1.png")));
	public JLabel player2Icon = new JLabel(new ImageIcon(InformationPanel.class.getResource("images/icon/player2.png")));
	public JLabel fieldPlayer = new JLabel("<html><font size=5 color=white face=微软雅黑>Player</font></html>", JLabel.CENTER);
	public JLabel fieldLifeCount = new JLabel("<html><font size=5 color=white face=微软雅黑>Life</font></html>", JLabel.CENTER);
	public JLabel fieldScore = new JLabel("<html><font size=5 color=white face=微软雅黑>Score</font></html>", JLabel.CENTER);
	public JLabel player1LifeCount = new JLabel("<html><font size=5 color=white face=微软雅黑>00</font></html>", JLabel.CENTER);
	public JLabel player2LifeCount = new JLabel("<html><font size=5 color=white face=微软雅黑>00</font></html>", JLabel.CENTER);
	public JLabel player1Score = new JLabel("<html><font size=5 color=white face=微软雅黑>00000</font></html>", JLabel.CENTER);
	public JLabel player2Score = new JLabel("<html><font size=5 color=white face=微软雅黑>00000</font></html>", JLabel.CENTER);
	private JPanel enemyInformationPanel = new JPanel();
	private JPanel myInformationPanel = new JPanel();
	private JPanel gameInformationPanel = new JPanel();
	private Font font = new Font("微软雅黑", Font.BOLD, 14);
	
	public InformationPanel() {
		super();
		this.setPreferredSize(new Dimension(WHITH, 900));
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(Color.DARK_GRAY);
		
		//设置并组装gameInformationPanel面板。
		gameInformationPanel.setLayout(new GridLayout(2, 2));
		gameInformationPanel.setPreferredSize(new Dimension(WHITH, 120));
		gameInformationPanel.setBackground(Color.DARK_GRAY);
		gameInformationPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
																		"Game_Information", 
																		TitledBorder.DEFAULT_JUSTIFICATION, 
																		TitledBorder.DEFAULT_POSITION, 
																		font, Color.WHITE));
		gameInformationPanel.add(fpsLabel);
		gameInformationPanel.add(fpsData);
		gameInformationPanel.add(stageLabel);
		gameInformationPanel.add(stageData);
		
		//设置并组装enemyInformationPanel面板。
		enemyInformationPanel.setPreferredSize(new Dimension(WHITH, 600));
		enemyInformationPanel.setBackground(Color.DARK_GRAY);
		enemyInformationPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
																		"Enemy_Tanks", 
																		TitledBorder.DEFAULT_JUSTIFICATION, 
																		TitledBorder.DEFAULT_POSITION, 
																		font, Color.WHITE));
		
		//设置并组装myInformationPanel面板。
		myInformationPanel.setLayout(new GridLayout(3, 3));
		myInformationPanel.setPreferredSize(new Dimension(WHITH,180));
		myInformationPanel.setBackground(Color.DARK_GRAY);
		myInformationPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
                                                                     "My_Tanks", 
                                                                     TitledBorder.DEFAULT_JUSTIFICATION, 
                                                                     TitledBorder.DEFAULT_POSITION, 
                                                                     font, Color.WHITE));
		myInformationPanel.add(fieldPlayer);
		myInformationPanel.add(fieldLifeCount);
		myInformationPanel.add(fieldScore);
		
		myInformationPanel.add(player1Icon);
		myInformationPanel.add(player1LifeCount);
		myInformationPanel.add(player1Score);
		
		myInformationPanel.add(player2Icon);
		myInformationPanel.add(player2LifeCount);
		myInformationPanel.add(player2Score);
		
		//组装InformationPanel面板。
		this.add(gameInformationPanel, BorderLayout.NORTH);
		this.add(enemyInformationPanel, BorderLayout.CENTER);
		this.add(myInformationPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void run() {
		while (true) {
			fpsData.setText("<html><font size=6 color=white face=微软雅黑>" + DrawPanel.fps + "</font></html>");
			stageData.setText("<html><font size=6 color=white face=微软雅黑>" + (DrawPanel.sort + 1) + "</font></html>");
			player1LifeCount.setText("<html><font size=6 color=white face=微软雅黑>" + DrawPanel.player1Count + "</font></html>");
			player2LifeCount.setText("<html><font size=6 color=white face=微软雅黑>" + DrawPanel.player2Count + "</font></html>");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

}
