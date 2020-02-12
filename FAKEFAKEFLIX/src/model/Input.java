package model;

import java.util.Scanner;

/**
 * 
 * @author David. Clase para recogida de datos
 *
 */

public class Input {

	public Input() {

	}

	/**
	 * @author David.
	 * @return int recogido por teclado.
	 */
	public static int readInt() {
		int inNum = 0;
		try {
			inNum = new Scanner(System.in).nextInt();
		} catch (InputMismatchException m) {
			System.out.println("ese no es un número válido. ");
		}
		return inNum;
	}

	/**
	 * @author David.
	 * @return String recogido por teclado.
	 */
	public static String readString() {
		String inText = "";
		try {
			inText = new Scanner(System.in).nextLine();
		} catch (Exception e) {
			System.out.println(e);
		}
		return inText;
	}
}
