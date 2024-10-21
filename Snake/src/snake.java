package snacke;
import java.util.*; 



public class snake {
    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.inicio();
    }
}

class Juego {

    Scanner input = new Scanner(System.in);
    public int puntos = 0;
    private final int tamano_t = 20;
    private int[] food;
    private LinkedList<int[]> snake = new LinkedList<>();
    private char dir;
    private boolean gameOver;

    public void inicio() {
        menu();
    }
    
    public static void delay(Scanner input) {
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void  err(int n) {
        if (n == 0) System.out.println("Opcion incorrecta\nSeleccione una opcion valida");
    }
    
    public void rules() {
        System.out.println("=== Reglas del Juego Snake ===");
        System.out.println("1. Controlas una serpiente que se mueve por el tablero.");
        System.out.println("2. El objetivo es comer la mayor cantidad de comida posible.");
        System.out.println("3. Cada vez que la serpiente come, se hace mï¿½s larga.");
        System.out.println("4. El juego termina si la serpiente choca contra las paredes o contra sï¿½ misma.");
        System.out.println("5. Usa las teclas de direcciï¿½n (w, a, s, d) para moverte:");
        System.out.println("   - w: Arriba");
        System.out.println("   - s: Abajo");
        System.out.println("   - a: Izquierda");
        System.out.println("   - d: Derecha");
        System.out.println("6. ï¿½Intenta obtener la mayor puntuaciï¿½n posible!");
    }

    private void menu(){
        while(true) {
            System.out.println("### Bienvenido al juego Snake ###");
            System.out.println("-Ingrese que opcion quiere seguir:\n"
                             + "\t1-Jugar\n" + "\t2-Reglas\n" + "\t3-Salir");
            int option = input.nextInt();

            if (option == 3) break;

            switch(option) {
                case 1:
                	juego();
                    break;

                case 2:
                	rules();
                	delay(input);
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
    	if(input.hasNext()) {
    		inputRecived = true;
    		char dire = input.next().toUpperCase().charAt(0);
    		dir = dire;
    	} else inputRecived = false;
        
    }

    public void Mover() {
        int[] h_snake = snake.peekFirst();
        int nRow = h_snake[0], nCol = h_snake[1]; // copiamos la direccion de la cabeza de la serpiente

        switch(dir) { // dependiendo de la direccion ingresada por el usuario cambiamos la direccion de la serpiente
            case 'W': nRow--; break;
            case 'A': nCol--; break;
            case 'S': nRow++; break;
            case 'D': nCol++; break;
        }

        snake.addFirst(new int[]{nRow,nCol});
        if(nRow == food[0] && nCol == food[1]) {
        	generateFood();
        	puntos += 10;
        } else snake.removeLast();
    }

    public void checkGame() {
        int[] head = snake.peekFirst();
        int r = head[0], c = head[1];

        if(r >= tamano_t || c >= tamano_t || r < 0 || c < 0 || isSnake(r, c)) gameOver = true;
    }
    
    private boolean inputRecived = false;

    public void juego() {
    	inputRecived = false;
    	generateFood();
        dibujarTablero();
        
        while(!gameOver) {
        	
        	inputRecived = false;
        	do {
        		getDir();
                Mover();
                dibujarTablero();
                checkGame();
                try {
                	Thread.sleep(1000);
                } catch(InterruptedException e) {
                	e.printStackTrace();
                }
        	} while (!inputRecived);
        	
        }

    }
    
    public void dibujarTablero() {
    	for(int i = 0; i < tamano_t; i++) {
    		System.out.print("|");
    		for(int j = 0; j < tamano_t; j++) {
    			if (isSnake(i,j)) {System.out.print(" - |");}
    			else if(i == food[0] && j == food[1]) {System.out.print(" * |");}
    			else {System.out.print("   |");}
    		}
    		System.out.println();
    	}
    }
}
