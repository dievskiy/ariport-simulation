package simu.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import controller.IKontrolleri;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;
import simu.framework.Trace;

public class OmaMoottori extends Moottori {

	private double kokonasaikaPalvelupisteessa = 0;

	private Saapumisprosessi saapumisprosessi;

	private int turvaLinjojenMaara = 1;

	private int kauppapisteidenMaara = 1;

	private int kahvilapisteidenMaara = 1;

	private int vessapisteidenMaara = 1;

	private int todennakoisyys = 50;

	// Map, jossa avaimena on asiakkaan saapumisaika ja arvona nykyisen
	// palvelupisteen indeksi
	private Map<Double, Integer> turvaIndeksit = new HashMap<>();

	private Map<Double, Integer> kauppaIndeksit = new HashMap<>();

	private Map<Double, Integer> kahvilaIndeksit = new HashMap<>();

	private Map<Double, Integer> vessaIndeksit = new HashMap<>();

	@Override
	protected boolean simuloidaan() {
		Trace.out(Trace.Level.INFO, "Kello on: " + Kello.getInstance().getAika());
		return palvellut < asiakkaat;

	}

	public OmaMoottori(IKontrolleri kontrolleri) {
		super(kontrolleri);

		int jakaumanKeskiarvo = kontrolleri.getJakaumanKeskiarvo();
		turvaLinjojenMaara = kontrolleri.getTurvaLinjojenMaara();
		todennakoisyys = kontrolleri.getTodennakoisyys();
		kauppapisteidenMaara = kontrolleri.getKauppaPisteidenMaara();
		kahvilapisteidenMaara = kontrolleri.getKahvilaPisteidenMaara();
		vessapisteidenMaara = kontrolleri.getVessaPisteidenMaara();

		palvelupisteet = new Palvelupiste[6 + turvaLinjojenMaara - 1 + kauppapisteidenMaara - 1 + kahvilapisteidenMaara
				- 1 + vessapisteidenMaara - 1];

		palvelupisteet[0] = new Palvelupiste(new Normal(10, 10), tapahtumalista, TapahtumanTyyppi.Baggage);
		palvelupisteet[1] = new Palvelupiste(new Normal(7, 2), tapahtumalista, TapahtumanTyyppi.Turvatarkastus);

		palvelupisteet[2] = new Palvelupiste(new Negexp(jakaumanKeskiarvo), tapahtumalista, TapahtumanTyyppi.Kauppa);
		palvelupisteet[3] = new Palvelupiste(new Negexp(jakaumanKeskiarvo), tapahtumalista, TapahtumanTyyppi.Kahvila);
		palvelupisteet[4] = new Palvelupiste(new Negexp(jakaumanKeskiarvo), tapahtumalista, TapahtumanTyyppi.Vessa);
		palvelupisteet[5] = new Palvelupiste(new Normal(5, 3), tapahtumalista, TapahtumanTyyppi.Lahto);

		int indeksi = 0;
		for (int i = 1; i < turvaLinjojenMaara; i++) {
			palvelupisteet[5 + i] = new Palvelupiste(new Normal(7, 1), tapahtumalista, TapahtumanTyyppi.Turvatarkastus);
		}

		ContinuousGenerator negexp = new Negexp(jakaumanKeskiarvo);
		
		for (int i = 1; i < kauppapisteidenMaara; i++) {
			indeksi = 5 + i + turvaLinjojenMaara - 1;
			palvelupisteet[indeksi] = new Palvelupiste(negexp, tapahtumalista, TapahtumanTyyppi.Kauppa);

		}
		for (int i = 1; i < kahvilapisteidenMaara; i++) {
			indeksi = 5 + i + turvaLinjojenMaara - 1 + kauppapisteidenMaara - 1;
			palvelupisteet[indeksi] = new Palvelupiste(negexp, tapahtumalista, TapahtumanTyyppi.Kahvila);
		}

		for (int i = 1; i < vessapisteidenMaara; i++) {
			indeksi = 5 + i + turvaLinjojenMaara - 1 + kauppapisteidenMaara - 1 + kahvilapisteidenMaara - 1;
			palvelupisteet[indeksi] = new Palvelupiste(negexp, tapahtumalista, TapahtumanTyyppi.Vessa);
		}

		saapumisprosessi = new Saapumisprosessi(new Negexp(10, 3), tapahtumalista, TapahtumanTyyppi.Vastaanotto);

	}

	@Override
	protected void alustukset() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void suoritaTapahtuma(Tapahtuma t) { // B-vaiheen tapahtumat
		Asiakas a;
		switch (t.getTyyppi()) {

		case Vastaanotto:
			palvelupisteet[0].lisaaJonoon(new Asiakas());
			saapumisprosessi.generoiSeuraava();
			break;
		case Baggage:
			a = palvelupisteet[0].otaJonosta();

			int turvaIndeksi = getRandomIndeksi(TapahtumanTyyppi.Turvatarkastus);
			palvelupisteet[turvaIndeksi].lisaaJonoon(a);
			turvaIndeksit.put(a.getSaapumisaika(), turvaIndeksi);

			break;
		case Turvatarkastus:
			double minimiAika = turvaIndeksit.keySet().stream().min(Double::compare).get();
			int indeksi = turvaIndeksit.remove(minimiAika);

			a = palvelupisteet[indeksi].otaJonosta();
			int meneeko = (int) (Math.random() * 100) + 1;

			if (meneeko < todennakoisyys) {
				int myymala = (int) (Math.random() * 100) + 1;
				if (myymala > 0 && myymala <= 33) {
					// menee kauppaan
					int kauppaIndeksi = getRandomIndeksi(TapahtumanTyyppi.Kauppa);
					palvelupisteet[kauppaIndeksi].lisaaJonoon(a);
					kauppaIndeksit.put(a.getSaapumisaika(), kauppaIndeksi);
				} else if (myymala > 33 && myymala <= 66) {
					// menee kahvilaan
					int kahvilaIndeksi = getRandomIndeksi(TapahtumanTyyppi.Kahvila);
					palvelupisteet[kahvilaIndeksi].lisaaJonoon(a);
					kahvilaIndeksit.put(a.getSaapumisaika(), kahvilaIndeksi);
				} else {
					// vessaan
					int vessaIndeksi = getRandomIndeksi(TapahtumanTyyppi.Vessa);
					palvelupisteet[vessaIndeksi].lisaaJonoon(a);
					vessaIndeksit.put(a.getSaapumisaika(), vessaIndeksi);
				}
			} else {
				// menee suoraan lähtöporttiin
				palvelupisteet[5].lisaaJonoon(a);
			}

			break;
		case Kauppa:
			double aika = kauppaIndeksit.keySet().stream().min(Double::compare).get();
			int kauppaIndeksi = kauppaIndeksit.remove(aika);

			a = palvelupisteet[kauppaIndeksi].otaJonosta();
			palvelupisteet[5].lisaaJonoon(a);
			break;
		case Kahvila:
			aika = kahvilaIndeksit.keySet().stream().min(Double::compare).get();
			int kahvilaIndeksi = kahvilaIndeksit.remove(aika);

			a = palvelupisteet[kahvilaIndeksi].otaJonosta();
			palvelupisteet[5].lisaaJonoon(a);
			break;
		case Vessa:
			aika = vessaIndeksit.keySet().stream().min(Double::compare).get();
			int vessaIndeksi = vessaIndeksit.remove(aika);

			a = palvelupisteet[vessaIndeksi].otaJonosta();
			palvelupisteet[5].lisaaJonoon(a);
			break;
		case Lahto:
			a = palvelupisteet[5].otaJonosta();
			a.setPoistumisaika(Kello.getInstance().getAika());
			a.raportti();
			kokonasaikaPalvelupisteessa += a.getPoistumisaika() - a.getSaapumisaika();
			palvellut++;
			break;
		}
	}

	/**
	 * Laskee indeksi tietylle tapahtuman tyypille randomisti.
	 * 
	 * @param t TapahtumanTyyppi
	 * @return palvelupisteen indeksi
	 */
	private int getRandomIndeksi(TapahtumanTyyppi t) {
		int pisteidenMaara = 0;
		int alaraja = 0;
		int oletusIndeksi = 0;

		switch (t) {
		case Kahvila:
			oletusIndeksi = 3;
			if (kahvilapisteidenMaara == 1)
				return oletusIndeksi;
			pisteidenMaara = kahvilapisteidenMaara;
			alaraja = (6 + turvaLinjojenMaara - 1 + kauppapisteidenMaara - 1);
			break;
		case Baggage:
			break;
		case Kauppa:
			oletusIndeksi = 2;
			if (kauppapisteidenMaara == 1)
				return oletusIndeksi;
			pisteidenMaara = kauppapisteidenMaara;
			alaraja = (6 + turvaLinjojenMaara - 1);
			break;
		case Lahto:
			break;
		case Turvatarkastus:
			oletusIndeksi = 1;
			if (turvaLinjojenMaara == 1)
				return oletusIndeksi;
			pisteidenMaara = turvaLinjojenMaara;
			alaraja = 6;
			break;
		case Vastaanotto:
			break;
		case Vessa:
			oletusIndeksi = 4;
			if (vessapisteidenMaara == 1)
				return oletusIndeksi;
			pisteidenMaara = vessapisteidenMaara;
			alaraja = (6 + turvaLinjojenMaara - 1 + kauppapisteidenMaara - 1 + kahvilapisteidenMaara - 1);
			break;
		default:
			break;
		}

		Random ran = new Random();
		int randomIndeksi = ran.nextInt(pisteidenMaara - 1) + alaraja;

		// ottaa varteen oletuspisteetkin
		if ((int) (Math.random() * 100 + 1) > (100 - (100 / pisteidenMaara)))
			randomIndeksi = oletusIndeksi;

		return randomIndeksi;
	}

	@Override
	protected void tulokset() {
		kontrolleri.lopeta();
		System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());
		SuureLaskeminen suureet = new SuureLaskeminen(asiakkaat, palvellut, kokonasaikaPalvelupisteessa);

		// Vie tiedot CSV luokkaan tallennusta varten
		CSVTallennus tallennus = new CSVTallennus();
		tallennus.csvTiedosto(asiakkaat, suureet.getKeskijononPituus(), suureet.getSuoritusteho(),
				kokonasaikaPalvelupisteessa, suureet.getKeskimaarainenlapimeno());
		tallennus.csvPalvelupiste(palvelupisteet);
		(new TulostenKasittelija(palvelupisteet, asiakkaat, palvellut, kokonasaikaPalvelupisteessa)).tulokset();

	}

}
