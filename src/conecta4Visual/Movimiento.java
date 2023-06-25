package conecta4Visual;

import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class Movimiento {

	public static TimerTask tarea;
	private static int valorTrofeo = 0;
	private static int posX = 300;
	private static int posY = 300;
	private static int a, b, c, d, e, f;
	private static int turno = 0;
	private static int aux = 3;

	/**
	 * Este método usa la clase timer y la interfaz TimerTask de java.util para
	 * simular el desplazamiento del trofeo en el ejeY
	 * 
	 * Cuanto más pequeño sea la variable valor mas 'natural' se verá el
	 * desplazamiento. El if tiene un tope 'maxPos' que sirve para parar la imagen
	 * en el lugar que quieras.
	 */

	public static void comenzarMovimiento(int ejeX, int ejeY, JLabel trofeo) {

		Timer desplazamiento = new Timer();
		TimerTask tarea = new TimerTask() {
			public void run() {

				trofeo.setLocation(ejeX, ejeY - valorTrofeo);
				valorTrofeo += 3; // cambia el valor del eje Y

				if (valorTrofeo >= 650) {
					desplazamiento.cancel();
				}
			}
		};// programa la tarea, que se repite cada '13' milisegundos
		desplazamiento.schedule(tarea, 0, 13);
	}

	/*
	 * Genera el color aleatorio del boton
	 */

	private static int random(int num) {
		Random rng = new Random();
		return rng.nextInt(num);
	}

	public static void comenzarMovimiento(Celda boton) {

		Timer desplazamiento = new Timer();
		tarea = new TimerTask() {
			public void run() {

				// -aux -aux diagonal sup izq
				if (a <= 293) {
					boton.setLocation(posX -= aux, posY -= aux);
					a += 3;
				}
				// -0 -aux baja
				else if (b <= 300) {
					boton.setLocation(posX, posY += aux);
					b += 3;
				}
				// +aux +aux diagonal inf derecha
				else if (c <= 293) {
					boton.setLocation(posX += aux, posY += aux);
					c += 3;
				}
				// +aux -aux diagonal sup derecha
				else if (d <= 293) {
					boton.setLocation(posX += aux, posY -= aux);
					d += 3;
				}

				else if (e <= 300) {
					boton.setLocation(posX -= aux, posY -= aux);
					e += 3;
				}

				else if (f <= 300) {
					boton.setLocation(posX, posY += aux);
					f += 3;

				} else {
					if (turno % 2 == 0) {
						aux = -3;
						turno++;
					} else {
						aux = 3;
						turno++;
					}
					a = 0;
					b = 0;
					c = 0;
					d = 0;
					e = 0;
					f = 0;
					boton.setBackground(new Color(random(255), random(255), random(255)));
				}
			}
		};// programa la tarea, con delay 1500 y que se repite cada '17' milisegundos
		desplazamiento.schedule(tarea, 1500, 17);
	}

}
