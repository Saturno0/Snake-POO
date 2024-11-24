import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

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
    private static int punto_desafio;
    private static boolean desafioInicializado = false;
    private Image headUp, headDown, headLeft, headRight;
    private Image tailUp, tailDown, tailLeft, tailRight;
    private Image bodyStraightHorizontal, bodyStraightVertical;
    private Image turnLeftUp, turnLeftDown, turnRightUp, turnRightDown;

    private void loadImages() {
        headUp = new ImageIcon(getClass().getResource("/img/Cabeza_arr.png")).getImage();
        headDown = new ImageIcon(getClass().getResource("/img/Cabeza_abj.png")).getImage();
        headLeft = new ImageIcon(getClass().getResource("/img/Cabeza_izq.png")).getImage();
        headRight = new ImageIcon(getClass().getResource("/img/Cabeza_der.png")).getImage();
    
        tailUp = new ImageIcon(getClass().getResource("/img/Cola_arr.png")).getImage();
        tailDown = new ImageIcon(getClass().getResource("/img/Cola_abj.png")).getImage();
        tailLeft = new ImageIcon(getClass().getResource("/img/Cola_izq.png")).getImage();
        tailRight = new ImageIcon(getClass().getResource("/img/Cola_der.png")).getImage();
    
        bodyStraightHorizontal = new ImageIcon(getClass().getResource("/img/Cuerpo_izq-der.png")).getImage();
        bodyStraightVertical = new ImageIcon(getClass().getResource("/img/Cuerpo_arr-abj.png")).getImage();
    
        turnLeftUp = new ImageIcon(getClass().getResource("/img/Doblez_der_abj.png")).getImage();
        turnLeftDown = new ImageIcon(getClass().getResource("/img/Doblez_arr_der.png")).getImage();
        turnRightUp = new ImageIcon(getClass().getResource("/img/Doblez_izq_abj.png")).getImage();
        turnRightDown = new ImageIcon(getClass().getResource("/img/Doblez_arr_izq.png")).getImage();

    }
    
    


    public SnakeGame(int velocidad) {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        loadImages(); // Cargar imágenes aquí
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
        timer = new Timer(velocidad, this);
        timer.start();
    }

    private void initGame() {
        snake.clear();
        snake.add(new int[]{BOARD_SIZE / 2, BOARD_SIZE / 2});
        generateFood();
        gameOver = false;
        dir = ' ';
        puntos = 0;
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

        if (nRow < 0 || nCol < 0 || nRow >= BOARD_SIZE || nCol >= BOARD_SIZE || 
            (snake.size() > 1 && isSnake(nRow, nCol))) {
            gameOver = true;
            puntuar();
            return;
        }

        snake.addFirst(new int[]{nRow, nCol});
        if (nRow == food[0] && nCol == food[1]) {
            generateFood();
            puntos += 10;
        } else {
            snake.removeLast();
        }
    }

    private static void iniciateDesafio() {
        Random random = new Random();
        int r = random.nextInt(300);
        punto_desafio = r;
        
    }
    
    private void puntuar() {
        if (!desafioInicializado) {
            iniciateDesafio();
            desafioInicializado = true;
        }
        if (puntos >= punto_desafio) {
            puntos += 100;
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

        for (int i = 0; i < snake.size(); i++) {
            int[] segment = snake.get(i);
            int x = segment[1] * TILE_SIZE;
            int y = segment[0] * TILE_SIZE;
        
            if (i == 0) {
                // Cabeza
                switch (dir) {
                    case 'U': g.drawImage(headUp, x, y, TILE_SIZE, TILE_SIZE, this); break;
                    case 'D': g.drawImage(headDown, x, y, TILE_SIZE, TILE_SIZE, this); break;
                    case 'L': g.drawImage(headLeft, x, y, TILE_SIZE, TILE_SIZE, this); break;
                    case 'R': g.drawImage(headRight, x, y, TILE_SIZE, TILE_SIZE, this); break;
                }
            } else if (i == snake.size() - 1) {
                // Cola
                int[] prev = snake.get(i - 1);
                if (prev[0] < segment[0]) {
                    g.drawImage(tailUp, x, y, TILE_SIZE, TILE_SIZE, this);
                } else if (prev[0] > segment[0]) {
                    g.drawImage(tailDown, x, y, TILE_SIZE, TILE_SIZE, this);
                } else if (prev[1] < segment[1]) {
                    g.drawImage(tailLeft, x, y, TILE_SIZE, TILE_SIZE, this);
                } else if (prev[1] > segment[1]) {
                    g.drawImage(tailRight, x, y, TILE_SIZE, TILE_SIZE, this);
                }
            } else {
                // Cuerpo
                int[] prev = snake.get(i - 1);
                int[] next = snake.get(i + 1);
        
                if (prev[0] == next[0]) {
                    // Horizontal
                    g.drawImage(bodyStraightHorizontal, x, y, TILE_SIZE, TILE_SIZE, this);
                } else if (prev[1] == next[1]) {
                    // Vertical
                    g.drawImage(bodyStraightVertical, x, y, TILE_SIZE, TILE_SIZE, this);
                } else {
                    // Dobleces
                    if (prev[0] < segment[0] && next[1] > segment[1] || next[0] < segment[0] && prev[1] > segment[1]) {
                        g.drawImage(turnLeftDown, x, y, TILE_SIZE, TILE_SIZE, this);
                    } else if (prev[0] > segment[0] && next[1] > segment[1] || next[0] > segment[0] && prev[1] > segment[1]) {
                        g.drawImage(turnLeftUp, x, y, TILE_SIZE, TILE_SIZE, this);
                    } else if (prev[0] < segment[0] && next[1] < segment[1] || next[0] < segment[0] && prev[1] < segment[1]) {
                        g.drawImage(turnRightDown, x, y, TILE_SIZE, TILE_SIZE, this);
                    } else if (prev[0] > segment[0] && next[1] < segment[1] || next[0] > segment[0] && prev[1] < segment[1]) {
                        g.drawImage(turnRightUp, x, y, TILE_SIZE, TILE_SIZE, this);
                    }
                }
            }
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
        String reglas = "Reglas del Juego: \n"
                + "1. Usa las teclas W, A, S, D para mover la serpiente.\n" 
                + "2. Come la comida roja para ganar puntos.\n" 
                + "3. No choques con los bordes ni con vos mismo.\n" 
                + "4. El juego termina si la serpiente choca.\n\n" 
                + "¡Buena suerte!\n"; 
        JOptionPane.showMessageDialog(null, reglas, "Reglas del Juego", JOptionPane.INFORMATION_MESSAGE);
        showMenu(); 
    }

    private static void showChallenge() {
        String mensaje = "El desafío de hoy es obtener: " + punto_desafio + " puntos.";
        JOptionPane.showMessageDialog(null, mensaje, "Desafío del Día", JOptionPane.INFORMATION_MESSAGE);
        showMenu(); 
    }

    private static void showMenu() {
        String[] options = {"Jugar", "Ver Reglas", "Ver Desafío de Hoy", "Salir"};
        
        if (!desafioInicializado) {
            iniciateDesafio();
            desafioInicializado = true;
        }
        
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
            case 0: chooseDifficulty(); break;
            case 1: showRules(); break;
            case 2: showChallenge(); break;
            case 3: System.exit(0); break;
            default: System.exit(0);
        }
    }

    private static void chooseDifficulty() {
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
            case 0: velocidad = 150; break;
            case 1: velocidad = 100; break;
            case 2: velocidad = 50; break;
            default: return;
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