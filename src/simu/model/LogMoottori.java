package simu.model;

import controller.IKontrolleri;
import simu.framework.Tapahtuma;

/**
 * Luokka, joka lisää visualosinti toiminnallisuutta OmaMoottori luokalle
 * Decorator suunnittelumallilla. Oikea nimi olisi VisualisointiMoottori
 * @author ruslanp
 */
public class LogMoottori extends OmaMoottori {

	private static final int VIIVE_MS = 100;

	public LogMoottori(IKontrolleri kontrolleri) {
		super(kontrolleri);
	}

	@Override
	public void suoritaTapahtuma(Tapahtuma t) {
		super.suoritaTapahtuma(t);
		((Pisteet) kontrolleri).naytaPisteet(palvelupisteet);
		try {
			Thread.sleep(VIIVE_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
