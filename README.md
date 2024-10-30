# Snake-POO

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGameGUI extends JPanel implements ActionListener {

    private final int TILE_SIZE = 20;
    private final int BOARD_SIZE = 20;
    private final int SCREEN_SIZE = TILE_SIZE * BOARD_SIZE;
    private final Timer timer;
    private int[] food;
    private LinkedList<int[]> snake = new LinkedList<>();
    private char dir = 'R';
    private int puntos = 0;
    private boolean gameOver = false;

    public SnakeGameGUI() {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: if (dir != 'D') dir = 'U'; break;
                    case KeyEvent.VK_S: if (dir != 'U') dir = 'D'; break;
                    case KeyEvent.VK_A: if (dir != 'R') dir = 'L'; break;
                    case KeyEvent.VK_D: if (dir != 'L') dir = 'R'; break;
                }
            }
        });
        initGame();
        timer = new Timer(150, this);
        timer.start();
    }

    private void initGame() {
        snake.clear();
        snake.add(new int[]{BOARD_SIZE / 2, BOARD_SIZE / 2});
        generateFood();
        gameOver = false;
    }

    private void generateFood() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(BOARD_SIZE);
            col = rand.nextInt(BOARD_SIZE);
        } while (isSnake(row, col));
        food = new int[]{row, col};
    }

    private boolean isSnake(int row, int col) {
        for (int[] segment : snake) {
            if (segment[0] == row && segment[1] == col) return true;
        }
        return false;
    }

    private void move() {
        int[] head = snake.peekFirst();
        int nRow = head[0];
        int nCol = head[1];
        
        switch (dir) {
            case 'U': nRow--; break;
            case 'D': nRow++; break;
            case 'L': nCol--; break;
            case 'R': nCol++; break;
        }

        // Verificar colisiones
        if (nRow < 0 || nCol < 0 || nRow >= BOARD_SIZE || nCol >= BOARD_SIZE || (snake.size() > 1 && isSnake(nRow, nCol))) {
            gameOver = true;
            return;
        }

        // Comer comida
        snake.addFirst(new int[]{nRow, nCol});
        if (nRow == food[0] && nCol == food[1]) {
            generateFood();
            puntos += 10;
        } else {
            snake.removeLast();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over! Puntos: " + puntos, SCREEN_SIZE / 4, SCREEN_SIZE / 2);
            return;
        }

        g.setColor(Color.RED);
        g.fillRect(food[1] * TILE_SIZE, food[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        g.setColor(Color.GREEN);
        for (int[] segment : snake) {
            g.fillRect(segment[1] * TILE_SIZE, segment[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGameGUI game = new SnakeGameGUI();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
