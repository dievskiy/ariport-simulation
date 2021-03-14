package controller;


public interface IKontrolleri {
	// Rajapinta, joka tarjotaan  käyttöliittymälle:
	
	/**
	 * Käynnistetään simulointi
	 */
	public void kaynnistaSimulointi();
	

	// Rajapinta, joka tarjotaan moottorille:
	
	public int getTurvaLinjojenMaara();
	
	public int getJakaumanKeskiarvo();
	
	public int getTodennakoisyys();

	public int getKauppaPisteidenMaara();

	public int getKahvilaPisteidenMaara();

	public int getVessaPisteidenMaara();

	public void lopeta();

}
