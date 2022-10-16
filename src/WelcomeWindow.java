import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class WelcomeWindow extends JWindow implements Runnable {
	
	public static int readedFileCount = 0;
	public static final int TOTAL_FILES = 129;
	public JProgressBar progressbar;
	public JLabel label;
	public WelcomeWindow() {
		label = new JLabel();
		label.setIcon(new ImageIcon(WelcomeWindow.class.getResource("images/Splash/Splash.jpg")));
		progressbar = new JProgressBar(0, TOTAL_FILES);
		progressbar.setBorderPainted(false);
		progressbar.setStringPainted(true);
		progressbar.setForeground(Color.DARK_GRAY);
		progressbar.setFont(new Font("微软雅黑", Font.BOLD, 18));
		this.add(label, BorderLayout.CENTER);
		this.add(progressbar,BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Thread(new WelcomeWindow()).start());
		try {
			Class.forName("ResourceRepertory");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(readedFileCount <= TOTAL_FILES) {
			this.progressbar.setValue(readedFileCount);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		SwingUtilities.invokeLater(() -> new MainWindow());
		this.dispose();
	}

}
