import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public final class MainWindow extends JFrame {

	private DrawPanel dPanel; // 绘图首选JPane类，实现了双缓冲
	private InformationPanel iPanel;

	public MainWindow() {
		//this.setUndecorated(true);// 去掉窗口所有的装饰，这句最好写在最前。
		this.setResizable(false);// 窗口不可调整。
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("坦克大战");
		this.dPanel = new DrawPanel();
		this.iPanel = new InformationPanel();
		this.getContentPane().add(dPanel, BorderLayout.CENTER);// 把dpanel添加到窗体的内容面板。
		this.getContentPane().add(iPanel, BorderLayout.EAST);// 把ipanel添加到窗体的内容面板。
		this.setIconImage(ResourceRepertory.myTanks[0][0]);
		this.pack();// 一定要加这句。
		this.setLocationRelativeTo(null);// 窗口居中显示。这句要加在窗口添加完所有组件并打包pack()之后。
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowIconified(WindowEvent e) {
				dPanel.gameStop();
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				dPanel.gameResume();
			}

			
		});
	}
}
