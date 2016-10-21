import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * @author Huyen
 *
 */
public class BouncingBall extends JFrame {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	private static final int UPDATETIME = 5;
	private DrawingCanvas canvas;
	//Ball
	int x = 50, y = 50;
	int size = 30;
	int xSpeed = 1, ySpeed = 2;
	Color[] ballColorArray = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.GRAY, Color.PINK, Color.BLACK };
	Color ballColor = Color.BLACK;
	//Paddle
	int paddleWidth = 10;
	int leftPaddleY;
	int leftPaddleHeight = 100;
	int rightPaddleY;
	int rightPaddleHeight = 100;
	//Mouse
	int mouseX = 0, mouseY =0;
	
	/**
	 * Constructor
	 */
	public BouncingBall() {
		canvas = new DrawingCanvas();
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setContentPane(canvas);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Bouncing Ball");
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		ActionListener updateTask = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Update ball position
				x += xSpeed;
				y += ySpeed;
				//Reverse the direction of the ball if it hits the left or right wall
				if (x < 0 || x > WIDTH - size) {
					xSpeed = -xSpeed;
					changeBallColor();
				}
				//Reverse the direction of the ball if it hits the top or bottom wall
				if (y < 0 || y > HEIGHT - size) {
					ySpeed = -ySpeed;
					changeBallColor();
				}
				//Set fixed paddles position
				leftPaddleY = (getHeight()-40)/2 - leftPaddleHeight/2;
				rightPaddleY = (getHeight()-40)/2 - rightPaddleHeight/2;
				repaint();
			}
		};
		//Run timer
		Timer timer = new Timer(UPDATETIME, updateTask);
		timer.start();
	}

	/**
	 * Change the color of the ball to random color
	 */
	public void changeBallColor() {
		Random rnd = new Random();
		ballColor = ballColorArray[rnd.nextInt(ballColorArray.length)];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BouncingBall();
			}
		});

	}
	//Nested Class (Class bên trong class khác)
	private class DrawingCanvas extends JPanel {

		@Override
		protected void paintComponent(Graphics g) {
			long start = System.nanoTime();
			super.paintComponent(g);
			setBackground(Color.WHITE);
			//Ball
			g.setColor(ballColor);
			g.fillOval(x, y, size, size);
			//Mouse trail
			g.setColor(Color.RED);
			g.fillOval(mouseX-10, mouseY-10, 20, 20);
			
			//Left Paddle
			g.setColor(Color.BLACK);
			int leftPaddleX = 0;
			g.fillRect(leftPaddleX, leftPaddleY, paddleWidth, leftPaddleHeight);
			//Right Paddle
			int rightPaddleX = getWidth()-paddleWidth;
			g.fillRect(rightPaddleX, rightPaddleY, paddleWidth, rightPaddleHeight);
			
			//Debug
			long duration = System.nanoTime() - start;
			//System.out.println("Paint in : "+ duration+ " nano seconds");
		}
	}
}
