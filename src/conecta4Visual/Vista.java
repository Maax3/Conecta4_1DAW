package conecta4Visual;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Vista extends JFrame {

	public JPanel lamina = new JPanel();
	public JPanel laminaGanadora = new JPanel();
	public JPanel laminaMenu = new JPanel();

	/*
	 * :::::::::::::::::::FUNCIONAMIENTO DEL CONSTRUCTOR::::::::::::::::::::::
	 *
	 * El constructor da formato al JFrame ya que la clase Vista hereda de JFrame. 
	 * Después formato los diferentes JPanel (laminas) por si mismo gracias a los diferentes métodos void();. 
	 * Por último, inicializa las lamina 'menu' de forma automática con el add();.
	 */

	public Vista() {

		setTitle("Juegos de Mesa");
		setBounds(0, 0, 800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		crearTablero();
		crearTableroGanador();
		crearMenu();
		add(laminaMenu);
	}

	public void crearTablero() {
		
		lamina.setLayout(new GridLayout(10, 10));
		lamina.setBackground(new Color(240, 178, 122));
		// Marron pastel: 240, 178, 122
		lamina.setBorder(BorderFactory.createLineBorder(Color.WHITE, 7));
	}

	/*
	 * El JPanel se inicia en el constructor, pero se agrega al JFrame en el
	 * actionPerformed de la clase Celda cuando una de las 2 fichas gane la partida.
	 */

	public void crearTableroGanador() {
		
		laminaGanadora.setLayout(null);
		laminaGanadora.setBackground(new Color(184, 207, 229));
		laminaGanadora.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 7));
	}

	public void crearMenu() {
		
		laminaMenu.setLayout(null);
		laminaMenu.setBackground(new Color(184, 207, 229));
		laminaMenu.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 7));
		laminaMenu.add(crearMensaje("CONECTA a 4", ""));
	}

	public Celda crearBoton() {
		
		Celda boton = new Celda();
			boton.setBounds(300, 300, 200, 150);
			boton.setText("COMENZAR");
			boton.setBackground(Color.BLACK);
			boton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			boton.setForeground(Color.WHITE);
			boton.setFont(new Font("Sans Serif", Font.BOLD, 28));
			boton.setFocusable(false);
			Movimiento.comenzarMovimiento(boton);

		return boton;
	}

	/*
	 * Se añade al contenedor en cuanto se llame al método desde la clase 'Celda en
	 * el método de actionPerformed -> victoria
	 */

	public void trofeo() {
		
		JLabel trofeo = new JLabel();
			trofeo.setIcon(new ImageIcon("img/trofeo.png"));
			trofeo.setBounds(-125, 0, 800, 800);
			Movimiento.comenzarMovimiento(-125, 600, trofeo);
			laminaGanadora.add(trofeo);
	}

	/**
	 * Metodo "reusable" que escribe un texto con JTextField tanto en el menu
	 * principal como en la pantalla final
	 */

	public static JTextField crearMensaje(String frase1, String frase2) {
		
		JTextField frase = new JTextField(frase1 + frase2);
			frase.setBounds(200, 100, 400, 70);
			frase.setBackground(Color.BLACK);
			frase.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			frase.setForeground(Color.WHITE);
			frase.setFont(new Font("Sans Serif", Font.BOLD, 28));
			frase.setEditable(false);
			frase.setHorizontalAlignment(JTextField.CENTER);
			
		return frase;
	}
}
