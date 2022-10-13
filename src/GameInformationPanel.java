import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class GameInformationPanel extends JPanel implements Runnable{

	public JLabel fpsLabel = new JLabel("<html><font size=6 color=white face=微软雅黑>FPS :</font></html>", JLabel.CENTER);
	public JLabel fpsData = new JLabel("00", JLabel.CENTER);
	public JLabel stageLabel = new JLabel("<html><font size=6 color=white face=微软雅黑>SORT :</font></html>", JLabel.CENTER);
	public JLabel stageData = new JLabel("00", JLabel.CENTER);
	private Font font = new Font("微软雅黑", Font.BOLD, 14);
	private Thread thread;
	
	public GameInformationPanel() {
		super();
		//设置并组装gameInformationPanel面板。
				setLayout(new GridLayout(2, 2));
				setPreferredSize(new Dimension(InformationPanel.WIDTH, 120));
				setBackground(Color.DARK_GRAY);
				setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.WHITE, 1, true), 
															"Game_Information", 
															TitledBorder.DEFAULT_JUSTIFICATION, 
															TitledBorder.DEFAULT_POSITION, 
															font, Color.WHITE));
				add(fpsLabel);
				add(fpsData);
				add(stageLabel);
				add(stageData);
				thread = new Thread(this);
				thread.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			//更新gameInformationPanel面板数据
			fpsData.setText("<html><font size=6 color=white face=微软雅黑>" + DrawPanel.fps + "</font></html>");
			stageData.setText("<html><font size=6 color=white face=微软雅黑>" + "0" + (DrawPanel.sort + 1) + "</font></html>");
		}		
	}
	

}
