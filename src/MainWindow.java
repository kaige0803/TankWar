import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

public final class MainWindow extends JFrame {
	private final int GAME_WITH = 800;
	private final int GAME_HIGHT = 600;
	private DrawPanel dPanel = new DrawPanel();

	public MainWindow() throws HeadlessException {
		setSize(GAME_WITH, GAME_HIGHT);
		setLocation(600, 200);
		setResizable(false);
		setTitle("坦克大战");
		dPanel.setBackground(Color.BLUE);
		getContentPane().add(dPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
