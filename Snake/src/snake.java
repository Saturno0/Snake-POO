import java.util.*; 



public class snake {
    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.inicio();
    }
}

class Juego {

    Scanner input = new Scanner(System.in);
    private final int tamano_t = 20;
    private int[] food;
    private LinkedList<int[]> snake;
    private char dir;
    private boolean gameOver;

    public void inicio() {
        menu();
    }
    
    public void  err(int n) {
        if (n == 0) System.out.println("Opcion incorrecta\nSeleccione una opcion valida");
    }

    public void menu(){
        while(true) {
            System.out.println("### Bienvenido al juego Snake ###");
            System.out.println("-Ingrese que opcion quiere seguir:\n"
                             + "\t1-Jugar\n" + "\t2-Reglas" + "\t3-Salir");
            int option = input.nextInt();

            if (option == 3) break;

            switch(option) {
                case 1:
                    break;

                case 2:
                    break;

                default:
                    err(0);
                    break;  
            }
        }
    }

    public void generateFood(){
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(tamano_t);
            col = rand.nextInt(tamano_t);
        } while (isSnake(row,col));

        food = new int[] {row,col};
    }

    public boolean isSnake(int row, int col){
        for (int[] segmento : snake) {
            if(segmento[0] == row && segmento[1] == col) return true;
        }
        return false;
    }

    public void getDir() {
        char dire = input.next().toUpperCase().charAt(0);
        dir = dire;
    }

    public void Mover() {
        int[] h_snake = snake.peekFirst();
        int nRow = h_snake[0], nCol = h_snake[1];

        switch(dir) {
            case 'W': nRow--;
            case 'A': nCol--;
            case 'S': nRow++;
            case 'D': nCol++;
        }

        snake.addFirst(new int[]{nRow,nCol});
        if(nRow == food[0] && nCol == food[1]) generateFood();
        else                                   snake.removeLast();
    }

    public void checkGame() {
        int[] head = snake.peekFirst();
        int r = head[0], c = head[1];

        if(r >= tamano_t || c >= tamano_t || r < 0 || c < 0 || isSnake(r, c)) gameOver = true;
    }

    public void juego() {
        dibujarTablero();
        while(!gameOver) {
            getDir();
            Mover();
            dibujarTablero();
            checkGame();
        }

    }
}
