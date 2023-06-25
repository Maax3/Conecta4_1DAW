package conecta4Visual;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Celda extends JButton implements MouseListener, ActionListener {

	public int fil;
	public int col;
	private ImageIcon negras = new ImageIcon("img/negro.png");
	private ImageIcon rojas = new ImageIcon("img/rojo.png");
	private static int turno = numAleatorio(); // negras o blancas de forma aleatoria al inciar la partida
	private Celda[][] matriz = new Celda[10][10];
	public static Vista tablero = new Vista();

	public Celda() {
		super();
	}

	public Celda(int fil, int col) {

		setFilasColumnas(fil, col);
		setBackground(null);
		setFocusable(false);
	}

	/*
	 * Metodo para añadir posiciones [x][y] al constructor
	 */
	public void setFilasColumnas(int fil, int col) {
		this.fil = fil;
		this.col = col;
	}

	/*
	 * Uno de los metodos más importantes que crea una matriz de botones 'Celdas' y
	 * la rellena con sus propias instancias. Se crean 100 botones(o un tablero
	 * 10x10). Todos las instancias implementan un actionListener y un
	 * MouseListener.
	 * 
	 * En la mayoría de los siguientes métodos usaremos esas instancias para crear
	 * condiciones de victoria, añadir efectos, evitar superposiciones etc...
	 * 
	 */

	public void crearCeldas() {

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				matriz[i][j] = new Celda(i, j); // se crea una nueva instancia de la clase celda
					tablero.lamina.add(matriz[i][j]); // se añade a la lamina
						matriz[i][j].addMouseListener(this); // se añade a cada celda un Action de this (él mismo)
							matriz[i][j].addActionListener(this);
			}
		}
	}
	
	
	public void crearBotonPrincipal() {
		
		Celda c = tablero.crearBoton();
		c.setActionCommand("BOTON COMENZAR");
		c.addActionListener(this);
		tablero.laminaMenu.add(c);
	}

	public void cambiarLamina(Celda c) {

		if (c.getActionCommand().equals("BOTON COMENZAR")) {
			Sonido.apagarCancion();
			Movimiento.tarea.cancel(); // se elimina el movimiento del boton para que no se siga reproduciendo internamente
			tablero.laminaMenu.setVisible(false);
			tablero.add(tablero.lamina);
		}
	}

	/*
	 * Metodo principal que registra todos los posibles eventos y actua según las
	 * distintas condiciones impuestas gracias al metodo e.getSource(). Al hacer un
	 * casting a nuestra clase botones podemos obtener en cada momento el boton que
	 * ha desencadenado el 'evento'. Esto también permite obtener las coordenadas de
	 * la matriz y diseñar la logica del juego.
	 */

	public void actionPerformed(ActionEvent e) {
		Celda c = (Celda) e.getSource();

		cambiarLamina(c);

		if (isAdyacente(c) && !isSuperpuesta(c)) {
			if (turno % 2 == 0) {
				c.setIcon(negras);
				Sonido.audio("sonido/ficha1.wav");
				turno++;

			} else {
				c.setIcon(rojas);
				Sonido.audio("sonido/ficha2.wav");
				turno++;
			}
		}

		if (isVictoria(c)) {

			tablero.lamina.setVisible(false);
			tablero.add(tablero.laminaGanadora);
			turno++;

			if (turno % 2 == 0) {
				tablero.laminaGanadora.add(Vista.crearMensaje("HAN GANADO", " NEGRAS"));
			} else {
				tablero.laminaGanadora.add(Vista.crearMensaje("HAN GANADO", " ROJAS"));
			}
			tablero.trofeo();
			Sonido.audio("sonido/victoria.wav");
		}
	}

	/*
	 * Metodo que compone todas las posibles victorias. 
	 * Se comprueban filas, columnas y diagonales.
	 */

	private boolean isVictoria(Celda c) {
		return isFila(c) || isColumna(c) || isDiagonal();
	}

	private boolean isDiagonal() {
		return isDiagonalSuperiorDerecha() || isDiagonalInferiorIzquierda() || isDiagonalSuperiorIzquierda()
				|| isDiagonalInferiorDerecha();
	}

	/*
	 * Limita la posicion de las fichas a donde corresponda (fila 9 y fichas
	 * adyacentes)
	 */

	private boolean isAdyacente(Celda c) {
		boolean comprobar;

		if (c.fil == 9) // con esto desbloqueamos la última fila
			comprobar = true;

		else if (matriz[c.fil + 1][c.col].getIcon() == rojas || matriz[c.fil + 1][c.col].getIcon() == negras) {

			comprobar = true;
		} else
			comprobar = false;

		return comprobar;
	}
	/*
	 * Método que evita la superposicion de fichas
	 */

	public boolean isSuperpuesta(Celda c) {

		return matriz[c.fil][c.col].getIcon() == rojas || matriz[c.fil][c.col].getIcon() == negras;
	}

	private boolean isFila(Celda c) {

		boolean victory = false;
		int fichasRojas = 0;
		int fichasNegras = 0;
		int fila = c.fil; // fijamos la fila

		for (int columna = 0; columna < matriz.length; columna++) {

			// :::CONDICIONES PARA ROJAS:::
			if (matriz[fila][columna].getIcon() == rojas) {

				fichasRojas++;
				if (fichasRojas == 4)
					victory = true;
			} else
				fichasRojas = 0;

			// :::CONDICIONES PARA NEGRAS:::
			if (matriz[fila][columna].getIcon() == negras) {

				fichasNegras++;
				if (fichasNegras == 4)
					victory = true;
			} else
				fichasNegras = 0;

		} // fin del for
		return victory;
	}

	/*
	 * Se recorre la columna seleccionada y se buscan las imagenes de las fichas. Si
	 * hace match 4 veces seguidas, el jugador de ficha en curso gana.
	 */

	private boolean isColumna(Celda c) {

		boolean victory = false;
		int fichasRojas = 0;
		int fichasNegras = 0;
		int columna = c.col; // fijamos la columna

		for (int fila = 0; fila < matriz.length; fila++) {

			// :::CONDICIONES PARA ROJAS:::

			if (matriz[fila][columna].getIcon() == rojas) {

				fichasRojas++;
				if (fichasRojas == 4)
					victory = true;
			} else
				fichasRojas = 0;

			// :::CONDICIONES PARA NEGRAS:::

			if (matriz[fila][columna].getIcon() == negras) {

				fichasNegras++;
				if (fichasNegras == 4)
					victory = true;
			} else
				fichasNegras = 0;

		} // fin del for
		return victory;

	}

	// :::::::::::::CONCEPTO CLAVE PARA TODAS LAS DIAGONALES::::::::::::
	/*
	 * Puede darse el problema de que poniendo 3 fichas en una diagonal y 1 ficha
	 * del mismo color en la siguiente diagonal, los métodos devuelvan true en lugar
	 * de false, esto es así porque el método leía de forma sucesiva todas las
	 * diagonales: https://i.gyazo.com/74300060b87ba9f1b3f5764f662186e3.png
	 * 
	 * Para evitar esto,es imprescindible resetear las variables de las fichas a 0
	 * cada vez que acabe el bucle for anidado. De esa forma, cuando el metodo
	 * comience a buscar en la siguiente línea, solo contará las fichas encontradas
	 * en esa línea.
	 */

	

	/*
	 * Para poder recorrer la matriz en diagonal: --> Se fija la fila en 0 y se
	 * resetea en cada fin del bucle anidado --> Se aumenta la columna en +1 para el
	 * siguiente bucle anidado --> Se aumenta la fila SOLO dentro del bucle anidado
	 * 
	 * De tal forma, la secuencia seria:
	 * [0,0][1,1][2,2][3,3][4,4][5,5][6,6][7,7][8,8][9,9]....
	 * [0,1][1,2][2,3][3,4][4,5][5,6][6,7][7,8][8,9]....
	 * [0,2][1,3][2,4][3,5][4,6][5,7][6,8][7,9]....
	 *
	 * RECORRIDO: 				 ⚪⚫
	 * DIAGONAL SUPERIOR DERECHA ⚪⚪
	 */

	private boolean isDiagonalSuperiorDerecha() {

		boolean victory = false;
		int fichasRojas = 0;
		int fichasNegras = 0;
		int fila = 0;

		// i simplemente marca las veces que se tiene que repetir el bucle anidado
		for (int i = 0; i < 7; i++) {

			for (int columna = 0 + i; columna < 10; columna++, fila++) {

				// :::CONDICIONES PARA ROJAS:::
				if (matriz[fila][columna].getIcon() == rojas) {

					fichasRojas++;

					if (fichasRojas == 4) {
						victory = true;
					}
				} else {
					fichasRojas = 0;
				}
				// :::CONDICIONES PARA NEGRAS:::
				if (matriz[fila][columna].getIcon() == negras) {

					fichasNegras++;

					if (fichasNegras == 4) {
						victory = true;
					}
				} else {
					fichasNegras = 0;
				}
			} // fin del for anidado

			fila = 0;
			fichasRojas = 0;
			fichasNegras = 0;
		}

		return victory;

	}


	/*
	 * Para poder recorrer la matriz en diagonal: --> Se fija la fila en 9 y se
	 * resetea en cada fin del bucle anidado --> Se disminuye la columna y la fila
	 * en 1 dentro del bucle anidado
	 * 
	 * De tal forma, la secuencia seria: [9,0]... [9,1][8,0].... [9,2][8,1][7,0]....
	 * 
	 * RECORRIDO: 				   ⚪⚪
	 * DIAGONAL INFERIOR IZQUIERDA ⚫⚪
	 */

	private boolean isDiagonalInferiorIzquierda() {

		boolean victory = false;
		int fichasRojas = 0;
		int fichasNegras = 0;
		int fila = 9;

		// i simplemente marca las veces que se tiene que repetir el bucle anidado
		for (int i = 3; i < 9; i++) {

			for (int columna = 0 + i; columna >= 0; columna--, fila--) {

				// :::CONDICIONES PARA ROJAS:::
				if (matriz[fila][columna].getIcon() == rojas) {

					fichasRojas++;

					if (fichasRojas == 4) {
						victory = true;
					}
				} else {
					fichasRojas = 0;
				}
				// :::CONDICIONES PARA NEGRAS:::
				if (matriz[fila][columna].getIcon() == negras) {

					fichasNegras++;

					if (fichasNegras == 4) {
						victory = true;
					}
				} else {
					fichasNegras = 0;
				}
			} // fin del for anidado

			fila = 9;
			fichasRojas = 0;
			fichasNegras = 0;
		}

		return victory;

	}

	/*
	 * Para poder recorrer la matriz en diagonal: --> Se fija la fila en 0 y se
	 * resetea en cada fin del bucle anidado en 1 --> Restamos -1 a la fila y
	 * sumamos +1 a la columna para el siguiente bucle anidado --> Se aumenta
	 * resetea la fila en 1 porque sino con fila-- quedaría en negativo (-1) en la
	 * primera vuelta
	 * 
	 * De tal forma, la secuencia seria: [0,0]... [1,0][0,1]... [2,0][1,1][0,2]...
	 * 
	 * RECORRIDO: 				   ⚫⚪
	 * DIAGONAL SUPERIOR IZQUIERDA ⚪⚪
	 */

	private boolean isDiagonalSuperiorIzquierda() {

		boolean victory = false;
		int fichasRojas = 0;
		int fichasNegras = 0;
		int fila = 0;

		// i simplemente marca las veces que se tiene que repetir el bucle anidado
		for (int i = 2; i < 10; i++) {

			for (int columna = 0; fila >= 0; columna++, fila--) {

				// :::CONDICIONES PARA ROJAS:::
				if (matriz[fila][columna].getIcon() == rojas) {

					fichasRojas++;

					if (fichasRojas == 4) {
						victory = true;
					}
				} else {
					fichasRojas = 0;
				}
				// :::CONDICIONES PARA NEGRAS:::
				if (matriz[fila][columna].getIcon() == negras) {

					fichasNegras++;

					if (fichasNegras == 4) {
						victory = true;
					}
				} else {
					fichasNegras = 0;
				}
			} // fin del for anidado
			fila = 1 + i;
			fichasRojas = 0;
			fichasNegras = 0;
		}

		return victory;

	}

	/*
	 * Para poder recorrer la matriz en diagonal: --> Se fija la fila en 9 y se
	 * resetea en cada fin del bucle anidado a 9 --> Se aumenta la columna en +1
	 * para el siguiente bucle anidado --> Pero se disminuye en 1 el estado inicial
	 * de la columna (9,8,7...)
	 * 
	 * De tal forma, la secuencia seria: [9,9].... [9,8][8,9] [9,7][8,8][7,9]....
	 * 
	 * RECORRIDO: 				 ⚪⚪
	 * DIAGONAL INFERIOR DERECHA ⚪⚫
	 */

	private boolean isDiagonalInferiorDerecha() {

		boolean victory = false;
		int fichasRojas = 0;
		int fichasNegras = 0;
		int fila = 9;

		// i simplemente marca las veces que se tiene que repetir el bucle anidado
		for (int i = 3; i < 9; i++) {

			for (int columna = 9 - i; columna < 10; columna++, fila--) {

				// :::CONDICIONES PARA ROJAS:::
				if (matriz[fila][columna].getIcon() == rojas) {

					fichasRojas++;
					if (fichasRojas == 4) {
						victory = true;
					}
				} else {
					fichasRojas = 0;
				}
				// :::CONDICIONES PARA NEGRAS:::
				if (matriz[fila][columna].getIcon() == negras) {

					fichasNegras++;

					if (fichasNegras == 4) {
						victory = true;
					}
				} else {
					fichasNegras = 0;
				}
			} // fin del for anidado

			fila = 9;
			fichasRojas = 0;
			fichasNegras = 0;
		}

		return victory;

	}

	/**
	 * Permite de forma aleatoria empezar con rojas o negras
	 */

	private static int numAleatorio() {
		Random rng = new Random();
		int num = 1;
		
		if (rng.nextInt(100) > 50)
			num = 0;

		return num;
	}

	// ::::METODOS DE LA INTERFAZ MOUSE LISTENER::::

	/*
	 * Crea un :hover sobre la casilla adyacente disponible. Es decir, según el
	 * turno tu turno y las fichas del tablero señala donde puedes colocar 1 ficha.
	 */

	public void mouseEntered(MouseEvent e) {
		
		Celda c = (Celda) e.getComponent();
		c.setCursor(new Cursor(12)); // cambia el cursor al cursor de la 'mano'

		if (isAdyacente(c) && !isSuperpuesta(c)) {
			if (turno % 2 == 0) {
				c.setBackground(new Color(66, 66, 66)); // :hover de color negro
			} else
				c.setBackground(new Color(244, 65, 51)); // :hover de color rojo
		} 
	}
	/*
	 * Devuelve el color original a la celda (boton). 
	 * Es decir, pasa de rojo || negro a transparente.
	 */

	public void mouseExited(MouseEvent e) {
		
		Celda c = (Celda) e.getComponent();
		if (!c.getActionCommand().equals("BOTON COMENZAR"))
			c.setBackground(null);
	}

	// Quita el overlay (fondo) que existe x defecto al presionar y mantener pulsado un boton.
	public void mousePressed(MouseEvent e) {
		
		Celda c = (Celda) e.getComponent();
		if (!c.getActionCommand().equals("BOTON COMENZAR"))
			c.setContentAreaFilled(false);

	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {
		/*
		 * Funciona exactamente igual que el actionPerformed, pero por alguna razon
		 * presenta un delay entre el click-accion y hace que ciertas acciones como
		 * pulsar un boton tengan un retardo o no se registren correctamente
		 */
	}

}
