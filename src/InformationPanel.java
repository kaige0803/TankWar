import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InformationPanel extends JPanel {
	private static final int WHITH = 200;
	public JPanel enemyInformationPanel = new EnemyInformationPanel();
	private JPanel myInformationPanel = new MyInformationPanel();
	private JPanel gameInformationPanel = new GameInformationPanel();
	
	public InformationPanel() {
		this.setPreferredSize(new Dimension(WHITH, 900));
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(Color.DARK_GRAY);
		//组装InformationPanel面板。
		this.add(gameInformationPanel, BorderLayout.NORTH);
		this.add(enemyInformationPanel, BorderLayout.CENTER);
		this.add(myInformationPanel, BorderLayout.SOUTH);
	}
}
