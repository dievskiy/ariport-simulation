package controller;

import javafx.application.Platform;
import simu.framework.IMoottori;
import simu.framework.Kello;
import simu.model.Pisteet;
import simu.model.LogMoottori;
import simu.model.Palvelupiste;
import view.ISimulaattorinUI;

public class Kontrolleri implements IKontrolleri, Pisteet {

	private final int MAX_TURVAPISTEET = 3;
	private final int MIN_TURVAPISTEET = 1;

	private IMoottori moottori;
	private ISimulaattorinUI gui;

	public Kontrolleri(ISimulaattorinUI gui) {
		this.gui = gui;
	}

	@Override
	public void kaynnistaSimulointi() {
		int asiakkaat = asiakasMaara();
		int turvaMaara = getTurvaLinjojenMaara();
		int jakaumanKeskiarvo = getJakaumanKeskiarvo();
		int todennakoisyys = getTodennakoisyys();

		int vessaPisteet = getVessaPisteidenMaara();
		int kahvilaPisteet = getVessaPisteidenMaara();
		int kauppaPisteet = getVessaPisteidenMaara();

		if (asiakkaat < 0 || turvaMaara < 0 || jakaumanKeskiarvo < 0 || todennakoisyys < 0 || kahvilaPisteet < 0
				|| kauppaPisteet < 0 || vessaPisteet < 0)
			return; 
		
		moottori = new LogMoottori(this); // luodaan uusi moottorisäie jokaista simulointia varten
		Kello.getInstance().setAika(0);
		moottori.setAsiakkaat(asiakkaat);
		gui.aktivoiUi(false);
		((Thread) moottori).start();
		
	}

	public int asiakasMaara() {
		int maara = gui.asiakasMaara();
		if (maara < 1) {
			gui.vaaraAsiakasMaara();
			return -1;
		}
		return maara;
	}

	@Override
	public int getTurvaLinjojenMaara() {
		int maara = gui.getTurvaLinjojenMaara();
		if (maara < MIN_TURVAPISTEET || maara > MAX_TURVAPISTEET) {
			gui.vaaraTurvaLinjojenMaara();
			return -1;
		}
		return maara;
	}

	@Override
	public int getJakaumanKeskiarvo() {
		int maara = gui.getJakaumanKeskiarvo();
		if (maara < 3 || maara > 100) {
			gui.vaaraJakauma();
			return -1;
		}
		return maara;
	}

	@Override
	public int getTodennakoisyys() {
		int maara = gui.getTodennakoisyys();
		if (maara < 0 || maara > 100) {
			gui.vaaraTodennakoisyys();
			return -1;
		}
		return maara;
	}

	@Override
	public int getKauppaPisteidenMaara() {
		int maara = gui.getKauppaPisteidenMaara();
		if (maara < 1 || maara > 10) {
			gui.naytaTarkistaArvot();
			return -1;
		}
		return maara;
	}

	@Override
	public int getKahvilaPisteidenMaara() {
		int maara = gui.getKahvilaPisteidenMaara();
		if (maara < 1 || maara > 10) {
			gui.naytaTarkistaArvot();
			return -1;
		}
		return maara;
	}

	@Override
	public int getVessaPisteidenMaara() {
		int maara = gui.getVessaPisteidenMaara();
		if (maara < 1 || maara > 10) {
			gui.naytaTarkistaArvot();
			return -1;
		}
		return maara;
	}

	@Override
	public void naytaPisteet(Palvelupiste[] pisteet) {
		Platform.runLater(new Runnable() {
			public void run() {
				gui.getVisualisointi().paivitaPisteet(pisteet);
			}
		});
	}

	@Override
	public void lopeta() {
		gui.aktivoiUi(true);
		
	}

}
