
package simu.model;

import simu.framework.Kello;

/**
 * Tulostaa suureet konsoliin
 * @author jarnopk
 * @version 1.0
 */

public class TulostenKasittelija {

	private Palvelupiste[] palvelupisteet;
	private int palvellutAsiakkaat;
	private int asiakkaat;

	private SuureLaskeminen suureet;
	private double kokonasaikaPalvelupisteessa;

	public TulostenKasittelija(Palvelupiste[] palvelupisteet, int asiakkaat, int palvellutAsiakkaat,
			double kokonasaikaPalvelupisteessa) {
		this.asiakkaat = asiakkaat;
		this.palvelupisteet = palvelupisteet;
		this.palvellutAsiakkaat = palvellutAsiakkaat;
		this.kokonasaikaPalvelupisteessa = kokonasaikaPalvelupisteessa;
		suureet = new SuureLaskeminen(asiakkaat, palvellutAsiakkaat, kokonasaikaPalvelupisteessa);

	}
	/**
	 * Tulostaa kaikki tiedot
	 */

	public void tulokset() {

		System.out.printf("Saapuneet asiakkaat: %d\n", asiakkaat);
		System.out.printf("Palvellut asiakkaat: %d\n", palvellutAsiakkaat);
		printPalvelupisteet();
		System.out.printf("Kokonaisaika palvelupisteessä: %.2f \n", kokonasaikaPalvelupisteessa);
		keskimaarainenJononpituus();
		suoritusteho();
		keskimaarainenlapimeno();

	}
	
	/**
	 * Käy läpi palvelupisteet ja tulostaa keskimääräisen palveluajan ja suoritus tehon jokaiselle pisteelle
	 */
	private void printPalvelupisteet() {
		StringBuffer buffer = new StringBuffer();
		double kokonaisAika = Kello.getInstance().getAika();

		for (Palvelupiste p : palvelupisteet) {

			double keskPalveluaika = (p.getPalvellutAsiakkaat() == 0) ? 0
					: (p.getAktiiviaika() / p.getPalvellutAsiakkaat());
			double suoritusteho = p.getPalvellutAsiakkaat() / kokonaisAika;

			buffer.append(String.format(
					"Palvelupisteen %s aktiiviaika on %.1f, keskimääräinen palveluaika on %.1f ja suoritusteho on %.3f \n",
					p.getTyyppi().toString(), p.getAktiiviaika(), keskPalveluaika, suoritusteho));
		}
		System.out.println(buffer.toString());
	}

	private void keskimaarainenJononpituus() {
		System.out.printf("Keskimääräinen jonopituus oli: %.1f \n", suureet.getKeskijononPituus());
	}

	private void suoritusteho() {

		System.out.printf("Suoritusteho oli: %.3f \n", suureet.getSuoritusteho());
	}

	private void keskimaarainenlapimeno() {

		System.out.printf("Keskimääräinen läpimeno oli: %.1f \n", suureet.getKeskimaarainenlapimeno());
	}
	
}
