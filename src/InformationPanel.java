
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class InformationPanel extends JPanel implements Runnable {
	private final int WHITH = 200;
	public int fps = 0;
	public JLabel fpsLabel = new JLabel("FPS :  ");
	public JLabel fpsData = new JLabel("0");
	public JLabel stageLabel = new JLabel("STAGE : ");
	public JLabel stageData = new JLabel();
	private JPanel enemyInformationPanel = new JPanel();
	private JPanel myInformationPanel = new JPanel();
	private JPanel gameInformationPanel = new JPanel();
	private Font font1 = new Font("微软雅黑", Font.BOLD, 14);
	private Font font2 = new Font("微软雅黑", Font.BOLD, 26);
	
	public InformationPanel() {
		super();
		fpsLabel.setFont(font2);
		fpsLabel.setForeground(Color.WHITE);
		fpsData.setFont(font2);
		fpsData.setForeground(Color.WHITE);
		stageLabel.setFont(font2);
		stageLabel.setForeground(Color.WHITE);
		stageData.setFont(font2);
		stageData.setForeground(Color.WHITE);
		
		this.setPreferredSize(new Dimension(WHITH, 900));
		this.setLayout(new BorderLayout(0, 0));
		myInformationPanel.setPreferredSize(new Dimension(WHITH,150));
		myInformationPanel.setBackground(Color.DARK_GRAY);
		myInformationPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
                                                                     "My_Tanks", 
                                                                     TitledBorder.DEFAULT_JUSTIFICATION, 
                                                                     TitledBorder.DEFAULT_POSITION, 
                                                                     font1, Color.WHITE));
		enemyInformationPanel.setPreferredSize(new Dimension(WHITH, 600));
		enemyInformationPanel.setBackground(Color.DARK_GRAY);
		enemyInformationPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
				                                                       "Enemy_Tanks", 
				                                                       TitledBorder.DEFAULT_JUSTIFICATION, 
				                                                       TitledBorder.DEFAULT_POSITION, 
				                                                       font1, Color.WHITE));
		//设置并组装gameInformationPanel面板。
		gameInformationPanel.setPreferredSize(new Dimension(WHITH, 150));
		gameInformationPanel.setBackground(Color.DARK_GRAY);
		gameInformationPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
																		"Game_Information", 
																		TitledBorder.DEFAULT_JUSTIFICATION, 
																		TitledBorder.DEFAULT_POSITION, 
																		font1, Color.WHITE));
		gameInformationPanel.add(fpsLabel, BorderLayout.CENTER);
		gameInformationPanel.add(fpsData, BorderLayout.CENTER);
		gameInformationPanel.add(stageLabel,BorderLayout.SOUTH);
		gameInformationPanel.add(stageData,BorderLayout.SOUTH);
		
		this.add(gameInformationPanel, BorderLayout.NORTH);
		this.add(enemyInformationPanel, BorderLayout.CENTER);
		this.add(myInformationPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void run() {
		while (true) {
			fpsData.setText(DrawPanel.fps + "");
			stageData.setText(DrawPanel.sort + "");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

}
