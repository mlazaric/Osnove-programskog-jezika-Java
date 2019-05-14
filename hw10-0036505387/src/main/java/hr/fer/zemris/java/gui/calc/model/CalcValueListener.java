package hr.fer.zemris.java.gui.calc.model;

/**
 * Model promatrača koji je zainteresiran za dojavu o
 * promjenama vrijednosti upisane u kalkulator.
 * 
 * @author marcupic
 */
public interface CalcValueListener {
	/**
	 * Metoda koja se poziva kao rezultat promjene
	 * vrijednosti zapisane u kalkulator. 
	 * 
	 * @param model model kalkulatora u kojemu je došlo do promjene; ne može biti <code>null</code>
	 */
	void valueChanged(CalcModel model);
}
