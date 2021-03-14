package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simu.model.Palvelupiste;
import simu.model.TapahtumanTyyppi;

/**
 * Visualisointi luokka, joka piirtää palvelupisteet ja päivittää jonoarvot.
 * @author ruslanp
 *
 */
public class Visualisointi extends Canvas implements IVisualisointi {

	private class Koordinaatti {
		private int X;
		private int Y;

		public Koordinaatti(int x, int y) {
			setX(x);
			setY(y);
		}

		public int getX() {
			return X;
		}

		public void setX(int x) {
			X = x;
		}

		public int getY() {
			return Y;
		}

		public void setY(int y) {
			Y = y;
		}
	}

	private GraphicsContext gc;

	private static final int PALVELUPISTE_KOKO = 60;

	private static final int MARGIN_PISTEIDEN_VALILLA = 60;

	private static final int MARGIN_VERTICAL = 20;

	// empiirisesti saatu vakio
	final int Y_MARGIN_KERROIN = 2600;

	private static int KORKEUS;

	private static int LEVEYS;

	private int pisteKorkeus;

	private final HashMap<TapahtumanTyyppi, List<Koordinaatti>> marginMap = new HashMap<>();

	private SimulaattorinGUI gui;

	public Visualisointi(int w, int h, SimulaattorinGUI gui) {
		super(w, h);
		this.gui = gui;
		LEVEYS = w;
		KORKEUS = h;
		gc = this.getGraphicsContext2D();
	}

	public void alustaAnimointi(Palvelupiste[] pisteet) {
		piirräTaustakuvat();
		piirräTekstit();
		piirräPisteet(pisteet);

	}
	
	private void piirräTaustakuvat() {
		gc.setFill(Color.rgb(80, 95, 97, 0.1));
		gc.fillRect(240, 0, 360, getHeight());
	}

	private void piirräPisteet(Palvelupiste[] pisteet) {
		gc.setFill(Color.LIGHTCORAL);
		pisteKorkeus = KORKEUS / 3 + PALVELUPISTE_KOKO;

		// Tav. luovutus
		gc.fillOval(40, pisteKorkeus, PALVELUPISTE_KOKO, PALVELUPISTE_KOKO);
		marginMap.put(TapahtumanTyyppi.Baggage, new ArrayList<Koordinaatti>() {
			{
				add(new Koordinaatti(40, pisteKorkeus));
			}
		});

		// Turvatarkastus
		gc.fillOval(40 + PALVELUPISTE_KOKO + MARGIN_PISTEIDEN_VALILLA, pisteKorkeus, PALVELUPISTE_KOKO,
				PALVELUPISTE_KOKO);
		marginMap.put(TapahtumanTyyppi.Turvatarkastus, new ArrayList<Koordinaatti>() {
			{
				add(new Koordinaatti(40 + PALVELUPISTE_KOKO + MARGIN_PISTEIDEN_VALILLA, pisteKorkeus));
			}
		});

		// ei ole tarvetta muuttaa turvapisteen ympyrän kokoa, koska turvapisteitä voi
		// olla max.3
		// ja kaikki 3 mahtuu näyttöön
		for (int i = 2; i <= gui.getTurvaLinjojenMaara(); i++) {
			int sign = i % 2 == 0 ? 1 : -1;
			int x = 40 + PALVELUPISTE_KOKO + MARGIN_PISTEIDEN_VALILLA;
			int y = pisteKorkeus + (MARGIN_PISTEIDEN_VALILLA + MARGIN_VERTICAL) * sign;
			gc.fillOval(x, y, PALVELUPISTE_KOKO, PALVELUPISTE_KOKO);

			marginMap.get(TapahtumanTyyppi.Turvatarkastus).add(new Koordinaatti(x, y));
		}

		// Kauppa
		int kauppaPisteidenMaara = gui.getKauppaPisteidenMaara();
		int kokoMarginaaleilla = (int) (getHeight() / (kauppaPisteidenMaara + 2));
		int kauppaPisteidenKoko = kokoMarginaaleilla > PALVELUPISTE_KOKO ? PALVELUPISTE_KOKO : kokoMarginaaleilla;
		int yMarginKauppa = getYMargin(kauppaPisteidenMaara);
		;

		gc.fillOval(40 + PALVELUPISTE_KOKO * 2 + MARGIN_PISTEIDEN_VALILLA * 2, pisteKorkeus - yMarginKauppa,
				kauppaPisteidenKoko, kauppaPisteidenKoko);
		marginMap.put(TapahtumanTyyppi.Kauppa, new ArrayList<Koordinaatti>() {
			{
				add(new Koordinaatti(40 + PALVELUPISTE_KOKO * 2 + MARGIN_PISTEIDEN_VALILLA * 2,
						pisteKorkeus - yMarginKauppa));
			}
		});

		for (int i = 2; i <= kauppaPisteidenMaara; i++) {
			int x = 40 + PALVELUPISTE_KOKO * 2 + MARGIN_PISTEIDEN_VALILLA * 2;
			int y = pisteKorkeus + ((kauppaPisteidenKoko + kauppaPisteidenKoko / 10) * (i - 1)) - yMarginKauppa;
			gc.fillOval(x, y, kauppaPisteidenKoko, kauppaPisteidenKoko);

			marginMap.get(TapahtumanTyyppi.Kauppa).add(new Koordinaatti(x, y));
		}

		// Kahvila
		int kahvilaPisteidenMaara = gui.getKahvilaPisteidenMaara();
		kokoMarginaaleilla = (int) (getHeight() / (kahvilaPisteidenMaara + 2));
		int kahvilaPisteidenKoko = kokoMarginaaleilla > PALVELUPISTE_KOKO ? PALVELUPISTE_KOKO : kokoMarginaaleilla;
		int yMarginKahvila = getYMargin(kahvilaPisteidenMaara);
		gc.fillOval(40 + PALVELUPISTE_KOKO * 3 + MARGIN_PISTEIDEN_VALILLA * 3, pisteKorkeus - yMarginKahvila,
				kahvilaPisteidenKoko, kahvilaPisteidenKoko);
		marginMap.put(TapahtumanTyyppi.Kahvila, new ArrayList<Koordinaatti>() {
			{
				add(new Koordinaatti(40 + PALVELUPISTE_KOKO * 3 + MARGIN_PISTEIDEN_VALILLA * 3,
						pisteKorkeus - yMarginKahvila));
			}
		});

		for (int i = 2; i <= kahvilaPisteidenMaara; i++) {
			int x = 40 + PALVELUPISTE_KOKO * 3 + MARGIN_PISTEIDEN_VALILLA * 3;
			int y = pisteKorkeus + ((kahvilaPisteidenKoko + kahvilaPisteidenKoko / 10) * (i - 1)) - yMarginKahvila;
			gc.fillOval(x, y, kahvilaPisteidenKoko, kahvilaPisteidenKoko);

			marginMap.get(TapahtumanTyyppi.Kahvila).add(new Koordinaatti(x, y));
		}

		// WC
		int vessaPisteidenMaara = gui.getVessaPisteidenMaara();
		kokoMarginaaleilla = (int) (getHeight() / (vessaPisteidenMaara + 2));
		int vessaPisteidenKoko = kokoMarginaaleilla > PALVELUPISTE_KOKO ? PALVELUPISTE_KOKO : kokoMarginaaleilla;
		int yMarginVessa = getYMargin(vessaPisteidenMaara);
		gc.fillOval(40 + PALVELUPISTE_KOKO * 4 + MARGIN_PISTEIDEN_VALILLA * 4, pisteKorkeus - yMarginVessa,
				vessaPisteidenKoko, vessaPisteidenKoko);
		marginMap.put(TapahtumanTyyppi.Vessa, new ArrayList<Koordinaatti>() {
			{
				add(new Koordinaatti(40 + PALVELUPISTE_KOKO * 4 + MARGIN_PISTEIDEN_VALILLA * 4,
						pisteKorkeus - yMarginVessa));
			}
		});

		for (int i = 2; i <= vessaPisteidenMaara; i++) {
			int x = 40 + PALVELUPISTE_KOKO * 4 + MARGIN_PISTEIDEN_VALILLA * 4;
			int y = pisteKorkeus + ((vessaPisteidenKoko + vessaPisteidenKoko / 10) * (i - 1)) - yMarginVessa;
			gc.fillOval(x, y, vessaPisteidenKoko, vessaPisteidenKoko);

			marginMap.get(TapahtumanTyyppi.Vessa).add(new Koordinaatti(x, y));
		}

		// Lähtöportti
		gc.fillOval(40 + PALVELUPISTE_KOKO * 5 + MARGIN_PISTEIDEN_VALILLA * 5, pisteKorkeus, PALVELUPISTE_KOKO,
				PALVELUPISTE_KOKO);
		marginMap.put(TapahtumanTyyppi.Lahto, new ArrayList<Koordinaatti>() {
			{
				add(new Koordinaatti(40 + PALVELUPISTE_KOKO * 5 + MARGIN_PISTEIDEN_VALILLA * 5, pisteKorkeus));
			}
		});

	}

	/**
	 * laskee Y marginaali
	 * @param pisteidenMaara
	 * @return
	 */
	private int getYMargin(int pisteidenMaara) {
		if (pisteidenMaara > 3)
			return (int) (Math.sqrt(pisteidenMaara * Y_MARGIN_KERROIN));
		else if (pisteidenMaara == 3)
			return 70;
		else if (pisteidenMaara == 2)
			return 30;
		else
			return 0;
	}

	private void piirräTekstit() {
		gc.setFill(Color.rgb(10, 10, 10, 0.7));
		int y = 30;
		int maxLeveys = 80;
		gc.fillText("Tav. luovutus", 20, y, 120);
		gc.fillText("Turvatarkastus", 20 + MARGIN_PISTEIDEN_VALILLA + PALVELUPISTE_KOKO, y, maxLeveys);
		gc.fillText("Kauppa", 40 + MARGIN_PISTEIDEN_VALILLA * 2 + PALVELUPISTE_KOKO * 2, y, maxLeveys);
		gc.fillText("Kahvila", 40 + MARGIN_PISTEIDEN_VALILLA * 3 + PALVELUPISTE_KOKO * 3, y, maxLeveys);
		gc.fillText("Vessa", 40 + MARGIN_PISTEIDEN_VALILLA * 4 + PALVELUPISTE_KOKO * 4, y, maxLeveys);
		gc.fillText("Lähtöportti", 40 + MARGIN_PISTEIDEN_VALILLA * 5 + PALVELUPISTE_KOKO * 5, y, maxLeveys);
		gc.setFill(Color.BLACK);
		gc.fillText("Odotusalue", 380, 15, 120);
	}

	@Override
	public void paivitaPisteet(Palvelupiste[] pisteet) {
		gc.clearRect(0, 0, LEVEYS, KORKEUS);
		alustaAnimointi(pisteet);

		gc.setFill(Color.BLACK);

		naytaTekstit(Arrays.asList(pisteet));
	}

	/**
	 * piirtää ympyröitä palvelupisteiden viereen ja laittaa asiakkaiden määrä
	 * niiden päälle
	 * 
	 * @param pisteet Palvelupisteet
	 */
	private void naytaTekstit(List<Palvelupiste> pisteet) {
		Set<Entry<TapahtumanTyyppi, List<Koordinaatti>>> set = marginMap.entrySet();
		for (Entry<TapahtumanTyyppi, List<Koordinaatti>> e : set) {
			// filtteroi tyypin perusteella
			List<Palvelupiste> pp = pisteet.stream().filter(p -> p.getTyyppi().equals(e.getKey()))
					.collect(Collectors.toList());

			if (pp.size() == 0)
				continue;

			int pisteenKoko = getPisteenKoko(e.getKey());
			int i = 0;
			for (Koordinaatti k : e.getValue()) {
				gc.setFill(Color.BLACK);
				gc.fillText(String.valueOf(pp.get(i).getAsiakkaidenMaara()), k.getX() - 20, k.getY() + pisteenKoko / 3,
						80);
				gc.setFill(Color.BLUE);
				gc.fillOval(k.getX() - 20, k.getY() + pisteenKoko / 2, pisteenKoko / 6, pisteenKoko / 6);
				i++;
			}
		}
	}

	/**
	 * Laskee sopiva pisteen koko 
	 * @param tyyppi tapahtuman tyyppi
	 * @return
	 */
	private int getPisteenKoko(TapahtumanTyyppi tyyppi) {
		int koko = PALVELUPISTE_KOKO;
		int pisteidenMaara = 0;
		switch (tyyppi) {
		case Baggage:
			break;
		case Kahvila:
			pisteidenMaara = gui.getKahvilaPisteidenMaara();
			break;
		case Kauppa:
			pisteidenMaara = gui.getKauppaPisteidenMaara();
			break;
		case Lahto:
			break;
		case Turvatarkastus:
			break;
		case Vastaanotto:
			break;
		case Vessa:
			pisteidenMaara = gui.getVessaPisteidenMaara();
			break;
		default:
			break;
		}
		if (pisteidenMaara != 0) {
			int kokoMarginaaleilla = (int) (getHeight() / (pisteidenMaara + 2));
			koko = kokoMarginaaleilla > PALVELUPISTE_KOKO ? PALVELUPISTE_KOKO : kokoMarginaaleilla;
		}
		return koko;
	}
}