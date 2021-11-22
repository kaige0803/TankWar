import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.nio.CharBuffer;

import javax.swing.JButton;
import javax.swing.JFrame;

public final class MainWindow extends JFrame{
	private final int GAME_WITH = 800;
	private final int GAME_HIGHT = 600;
	DrawPanel dPanel = new DrawPanel();
	JButton jButton = new JButton("sakldfaj;sdfa;sdf");
	public MainWindow() throws HeadlessException {
		setSize(GAME_WITH, GAME_HIGHT);
		//setLayout(new FlowLayout());
		setResizable(false);
		setLocation(600, 200);
		add(dPanel,BorderLayout.CENTER);
		add(jButton,BorderLayout.SOUTH);
		setTitle("坦克大战");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//new Thread(dPanel).start();
		
	}
	
	

	
}
	



