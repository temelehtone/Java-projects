import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int WIDTH = 600;
	static final int HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
	static final int DELAY = 80;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int appleEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {
		if (running) {
			// for (int i=0; i < HEIGHT / UNIT_SIZE; i++) {
			// g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, HEIGHT);
			// g.drawLine(0, i*UNIT_SIZE, WIDTH, i*UNIT_SIZE);
			// }
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}

			}
			g.setColor(Color.blue);
			g.setFont(new Font("Ink Free", Font.BOLD, 30));
			g.drawString("Score: " + appleEaten, (UNIT_SIZE / 2), g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			newApple();
			bodyParts++;
			appleEaten++;
		}
	}

	public void checkCollision() {
		// Checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// Checks if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		// Checks if head touches right border
		if (x[0] > WIDTH) {
			running = false;
		}
		// Checks if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		// Checks if head touches bottom border
		if (y[0] > HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (WIDTH - metrics.stringWidth("Game Over!")) / 2, HEIGHT / 2);
		// Score text
		g.setColor(Color.blue);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + appleEaten, (WIDTH - metrics2.stringWidth("Score: " + appleEaten)) / 2,
				g.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L') {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D') {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U') {
						direction = 'D';
					}
					break;
			}
		}
	}
}
