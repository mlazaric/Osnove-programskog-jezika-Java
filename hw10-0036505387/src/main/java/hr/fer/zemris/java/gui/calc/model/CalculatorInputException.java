package hr.fer.zemris.java.gui.calc.model;

/**
 * Iznimka koja signalizira da je korisnik probao napraviti
 * nedozvoljeni unos u kalkulator (primjerice, broj koji se 
 * unosi već ima decimalnu točku, a korisnik je probao dodati
 * još jednu).
 * 
 * @author marcupic
 */
public class CalculatorInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 */
	public CalculatorInputException() {
	}

	/**
	 * Konstruktor.
	 * @param message poruka koja opisuje pogrešku
	 */
	public CalculatorInputException(String message) {
		super(message);
	}
	
}
