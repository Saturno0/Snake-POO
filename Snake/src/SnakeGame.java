import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int TILE_SIZE = 20;
    private final int BOARD_SIZE = 30;
    private final int SCREEN_SIZE = TILE_SIZE * BOARD_SIZE;
    private Timer timer;
    private int[] food;
    private LinkedList<int[]> snake = new LinkedList<>();
    private char dir;
    private int puntos = 0;
    private boolean gameOver = false;

    public SnakeGame(int velocidad) {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() { // con esto podemos "escuchar" las teclas que presiona el usuario y asi saber cual va a ser la direccion
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:  if (dir != 'D') dir = 'U'; break;
                    case KeyEvent.VK_S:  if (dir != 'U') dir = 'D'; break;
                    case KeyEvent.VK_A:  if (dir != 'R') dir = 'L'; break;
                    case KeyEvent.VK_D:  if (dir != 'L') dir = 'R'; break;
                }
            }
        });
        initGame();
        timer = new Timer(velocidad, this); // esto define el tiempo en el que se va actualizando la pagina
        timer.start();
    }

    private void initGame() { // iniciamos la posicion de la serpiente
        snake.clear();
        snake.add(new int[]{BOARD_SIZE / 2, BOARD_SIZE / 2});
        generateFood();
        gameOver = false;
        dir = ' '; // Dirección inicial
        puntos = 0; // Reiniciar puntos
    }

    private void generateFood() { // generamos la posicion de la comida de forma aleatoria
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(BOARD_SIZE);
            col = rand.nextInt(BOARD_SIZE);
        } while (isSnake(row, col));
        food = new int[]{row, col};
    }

    private boolean isSnake(int row, int col) { // esta funcion es para saber si las cordenadas que le estamos pasando son parte de la serpiente
        for (int[] segment : snake) {
            if (segment[0] == row && segment[1] == col) return true;
        }
        return false;
    }

    private void move() { // con esta funcion vamos actualizando el movimiento y la posicion de la cabeza de la serpiente
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
        if (nRow < 0 || nCol < 0 || nRow >= BOARD_SIZE || nCol >= BOARD_SIZE || 
            (snake.size() > 1 && isSnake(nRow, nCol))) {
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
            g.drawString("Game Over! Puntos: " + puntos, SCREEN_SIZE / 2, SCREEN_SIZE / 2);
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

    private static void showRules() {
        String reglas = "Reglas del Juego:\n"
              + "1. Usa las teclas W, A, S, D para mover la serpiente.\n"
              + "2. Come la comida roja para ganar puntos.\n"
              + "3. No choques con los bordes ni con vos mismo.\n"
              + "4. El juego termina si la serpiente choca.\n\n"
              + "¡Buena suerte!";
        JOptionPane.showMessageDialog(null, reglas, "Reglas del Juego", JOptionPane.INFORMATION_MESSAGE);
        showMenu(); // Volver al menú principal
    }

    private static void showMenu() {
        String[] options = {"Jugar", "Ver Reglas", "Salir"};
        int choice = JOptionPane.showOptionDialog(
                null, 
                "Bienvenido a Snake Game", 
                "Menú Principal", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                options, 
                options[0]);

        switch (choice) {
            case 0: chooseDifficulty(); break; // Elegir dificultad y empezar juego
            case 1: showRules(); break; // Ver Reglas
            case 2: System.exit(0); break; // Salir
            default: System.exit(0); break; // Salir en caso de cierre del diálogo
        }
    }

    private static void chooseDifficulty() { // seteamos la velocidad en la que se va a ir actualizando la pagina dependiendo de que dificultad haya elegido el usuario
        String[] difficulties = {"Fácil", "Media", "Difícil"};
        int choice = JOptionPane.showOptionDialog(
                null, 
                "Elige la Dificultad", 
                "Dificultad", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                difficulties, 
                difficulties[0]);

        int velocidad;
        switch (choice) {
            case 0: velocidad = 150; break; // Fácil
            case 1: velocidad = 100; break; // Media
            case 2: velocidad = 50;  break; // Difícil
            default: { return; } // Cancelado, volver al menú
        }

        startGame(velocidad);
    }

    private static void startGame(int velocidad) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame(velocidad);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showMenu();
    }
}
