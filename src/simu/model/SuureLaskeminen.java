package simu.model;


import simu.framework.Kello;

/**
 * Laskee suureet
 * 
 * @author jarnopk & ruslanp
 * @version 1.0
 */

public class SuureLaskeminen {

	private int palvellutAsiakkaat;
	private double kokonasaikaPalvelupisteessa;

	public SuureLaskeminen(int asiakkaat, int palvellutAsiakkaat,
			double kokonasaikaPalvelupisteessa) {
		this.palvellutAsiakkaat = palvellutAsiakkaat;
		this.kokonasaikaPalvelupisteessa = kokonasaikaPalvelupisteessa;

	}
	
	/**
	 * 
	 * @return Palauttaa keskijonon pituuden
	 */

	public double getKeskijononPituus() {
		double keskijononpituus = kokonasaikaPalvelupisteessa / Kello.getInstance().getAika();

		return keskijononpituus;
	}
	/**
	 * 
	 * @return palauttaa suoritustehon
	 */
	public double getSuoritusteho() {
		double suoritusteho = palvellutAsiakkaat / Kello.getInstance().getAika();
		return suoritusteho;

	}
	/**
	 * 
	 * @return palauttaa keskimääräisen läpimenoajan
	 */

	public double getKeskimaarainenlapimeno() {
		double keskilapimeno = kokonasaikaPalvelupisteessa / palvellutAsiakkaat;
		return keskilapimeno;
	}

}
