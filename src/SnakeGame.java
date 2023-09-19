import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    // Constants
    private static final int SCREEN_WIDTH = 400;
    private static final int SCREEN_HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int DELAY = 200; // Adjusted delay to 200 milliseconds

    private final ArrayList<Integer> snakeX = new ArrayList<>();
    private final ArrayList<Integer> snakeY = new ArrayList<>();
    private int appleX;
    private int appleY;
    private int score;
    private boolean isRunning;
    private char direction;
    private Timer timer; // Timer variable

    public SnakeGame() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snakeX.clear();
        snakeY.clear();
        snakeX.add(SCREEN_WIDTH / 2);
        snakeY.add(SCREEN_HEIGHT / 2);

        spawnApple();
        score = 0;
        direction = 'R';
        isRunning = true;

        // Stop the Timer if it's running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // Create a new Timer with a 200ms delay and start it
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnApple() {
        Random random = new Random(System.currentTimeMillis());
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        int headX = snakeX.get(0);
        int headY = snakeY.get(0);

        if (direction == 'U') headY -= UNIT_SIZE;
        if (direction == 'D') headY += UNIT_SIZE;
        if (direction == 'L') headX -= UNIT_SIZE;
        if (direction == 'R') headX += UNIT_SIZE;

        snakeX.add(0, headX);
        snakeY.add(0, headY);

        // Check for apple
        if (headX == appleX && headY == appleY) {
            score++;
            spawnApple();
        } else {
            snakeX.remove(snakeX.size() - 1);
            snakeY.remove(snakeY.size() - 1);
        }
    }

    public void checkCollision() {
        int headX = snakeX.get(0);
        int headY = snakeY.get(0);

        // Check for collisions with the walls
        if (headX < 0 || headX >= SCREEN_WIDTH || headY < 0 || headY >= SCREEN_HEIGHT) {
            isRunning = false;
            gameOver();
            return;
        }

        // Check for collisions with itself
        for (int i = 1; i < snakeX.size(); i++) {
            if (headX == snakeX.get(i) && headY == snakeY.get(i)) {
                isRunning = false;
                gameOver();
                return;
            }
        }
    }

    public void gameOver() {
        isRunning = false;
        String message = "Game Over\nYour Score: " + score + "\nPlay Again?";
        int choice = JOptionPane.showConfirmDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            startGame(); // Restart the game
        } else {
            System.exit(0); // Exit the game
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    public void draw(Graphics g) {
        if (isRunning) {
            // Draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snakeX.size(); i++) {
                g.setColor(Color.green);
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, 10, 30);
        } else {
            // Game over message
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", 150, SCREEN_HEIGHT / 2);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, 10, 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkCollision();
        }

        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            }
            if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            }
            if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            }
            if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
