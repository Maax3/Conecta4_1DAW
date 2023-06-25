package conecta4Visual;

public class Conecta4_APP {

	public static void main(String[] args) {
		// Delay para el splashArt
		try {
		    Thread.sleep(3000); 
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

		Sonido.encenderCancion();
		Vista tablero = Celda.tablero;
		Celda start = new Celda();
		start.crearBotonPrincipal();
		start.crearCeldas();
		tablero.setVisible(true);
		
	}
}
