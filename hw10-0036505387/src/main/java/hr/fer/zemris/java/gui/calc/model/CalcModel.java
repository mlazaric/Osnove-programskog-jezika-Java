package hr.fer.zemris.java.gui.calc.model;

import java.util.function.DoubleBinaryOperator;

/**
 * Sučelje specificira model jednog jednostavnog kalkulatora.
 * 
 * @author marcupic
 */
public interface CalcModel {
	/**
	 * Prijava promatrača koje treba obavijestiti kada se
	 * promijeni vrijednost pohranjena u kalkulatoru.
	 * 
	 * @param l promatrač; ne smije biti <code>null</code>
	 * 
	 * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
	 */
	void addCalcValueListener(CalcValueListener l);

	/**
	 * Odjava promatrača s popisa promatrača koje treba 
	 * obavijestiti kada se promijeni vrijednost 
	 * pohranjena u kalkulatoru.
	 * 
	 * @param l promatrač; ne smije biti <code>null</code>
	 * 
	 * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
	 */
	void removeCalcValueListener(CalcValueListener l);
	
	/**
	 * Vraća tekst koji treba prikazati na zaslonu kalkulatora.
	 * Detaljnija specifikacija dana je u uputi za domaću zadaću.
	 * 
	 * @return tekst za prikaz na zaslonu kalkulatora
	 */
	String toString();
	
	/**
	 * Vraća trenutnu vrijednost koja je pohranjena u kalkulatoru.
	 * 
	 * @return vrijednost pohranjena u kalkulatoru
	 */
	double getValue();

	/**
	 * Upisuje decimalnu vrijednost u kalkulator. Vrijednost smije
	 * biti i beskonačno odnosno NaN. Po upisu kalkulator 
	 * postaje needitabilan.
	 * 
	 * @param value vrijednost koju treba upisati
	 */
	void setValue(double value);
	
	/**
	 * Vraća informaciju je li kalkulator editabilan (drugim riječima,
	 * smije li korisnik pozivati metode {@link #swapSign()},
	 * {@link #insertDecimalPoint()} te {@link #insertDigit(int)}).
	 * @return <code>true</code> ako je model editabilan, <code>false</code> inače
	 */
	boolean isEditable();

	/**
	 * Resetira trenutnu vrijednost na neunesenu i vraća kalkulator u
	 * editabilno stanje.
	 */
	void clear();

	/**
	 * Obavlja sve što i {@link #clear()}, te dodatno uklanja aktivni
	 * operand i zakazanu operaciju.
	 */
	void clearAll();

	/**
	 * Mijenja predznak unesenog broja.
	 * 
	 * @throws CalculatorInputException ako kalkulator nije editabilan
	 */
	void swapSign() throws CalculatorInputException;
	
	/**
	 * Dodaje na kraj trenutnog broja decimalnu točku.
	 * 
	 * @throws CalculatorInputException ako nije još unesena niti jedna znamenka broja,
	 *         ako broj već sadrži decimalnu točku ili ako kalkulator nije editabilan
	 */
	void insertDecimalPoint() throws CalculatorInputException;

	/**
	 * U broj koji se trenutno upisuje na kraj dodaje poslanu znamenku.
	 * Ako je trenutni broj "0", dodavanje još jedne nule se potiho
	 * ignorira.
	 * 
	 * @param digit znamenka koju treba dodati
	 * @throws CalculatorInputException ako bi dodavanjem predane znamenke broj postao prevelik za konačan prikaz u tipu {@link Double}, ili ako kalkulator nije editabilan.
	 * @throws IllegalArgumentException ako je <code>digit &lt; 0</code> ili <code>digit &gt; 9</code>
	 */
	void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException;

	/**
	 * Provjera je li upisan aktivni operand.
	 * 
	 * @return <code>true</code> ako je aktivani operand upisan, <code>false</code> inače
	 */
	boolean isActiveOperandSet();
	
	/**
	 * Dohvat aktivnog operanda.
	 * 
	 * @return aktivni operand
	 * 
	 * @throws IllegalStateException ako aktivni operand nije postavljen
	 */
	double getActiveOperand() throws IllegalStateException;

	/**
	 * Metoda postavlja aktivni operand na predanu vrijednost. 
	 * Ako kalkulator već ima postavljen aktivni operand, predana
	 * vrijednost ga nadjačava.
	 * 
	 * @param activeOperand vrijednost koju treba pohraniti kao aktivni operand
	 */
	void setActiveOperand(double activeOperand);
	
	/**
	 * Uklanjanje zapisanog aktivnog operanda.
	 */
	void clearActiveOperand();
	
	/**
	 * Dohvat zakazane operacije.
	 * 
	 * @return zakazanu operaciju, ili <code>null</code> ako nema zakazane operacije
	 */
	DoubleBinaryOperator getPendingBinaryOperation();

	/**
	 * Postavljanje zakazane operacije. Ako zakazana operacija već
	 * postoji, ovaj je poziv nadjačava predanom vrijednošću.
	 * 
	 * @param op zakazana operacija koju treba postaviti; smije biti <code>null</code>
	 */
	void setPendingBinaryOperation(DoubleBinaryOperator op);
	
}
