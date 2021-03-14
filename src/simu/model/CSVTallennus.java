package simu.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import simu.framework.Kello;

/**
 * 
 * Luo CSV tiedoston ja kirjoittaa tarjolla olevat suureet
 * 
 * @author jarnopk
 * @version 1.0
 */

public class CSVTallennus {
	SuureLaskeminen suureet;

	public CSVTallennus() {
	}

	/**
	 * Metodi luo lentokentta.csv tiedoston, jos on olemassa jo niin kirjoittaa jo
	 * olemassa olevan perään.
	 * 
	 * @param asiakaslkm                   Ohjelmaan syötetty asiakasmäärä
	 * @param luku                         Keskijononpituus
	 * @param suoriteTeho                  Suoriteho
	 * @param kokonaisaikaPalvelupisteessa Paljonko on vietetty kokonaisuudessaan
	 *                                     palvelupisteessa
	 * @param keskilapi                    Keskimääräinen simulaattorin läpimenoaika
	 */
	public void csvTiedosto(int asiakaslkm, double luku, double suoriteTeho, double kokonaisaikaPalvelupisteessa,
			double keskilapi) {
		try {
			FileWriter fw = new FileWriter("lentokentta.csv", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			BufferedReader br = new BufferedReader(new FileReader("lentokentta.csv"));
			String otsikko = br.readLine();
			if (otsikko == null) { // jos dokumentista ei löydy tietoa niin kirjoittaa alla olevan

				String header = String
						.format("Asiakasmäärä; Keskijononpituus; Suoritusteho; Kokonaisaika; Keskiläpimeno;\n");

				pw.write(header);
			}

			pw.println(
					asiakaslkm + ";" + luku + ";" + suoriteTeho + ";" + kokonaisaikaPalvelupisteessa + ";" + keskilapi);
			pw.flush();
			pw.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	/**
	 * Luo yksittäisen tiedoston jossa on jokainen käytössä ollut palvelupiste
	 * eritelty
	 * 
	 * @param palvelupisteet Saa parametrina käytössä olevat palvelupisteet
	 */

	public void csvPalvelupiste(Palvelupiste[] palvelupisteet) {
		try {
			FileWriter fw = new FileWriter("Palvelupiste.csv");
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			BufferedReader br = new BufferedReader(new FileReader("Palvelupiste.csv"));
			String otsikko = br.readLine();

			if (otsikko == null) {
				StringBuffer header = new StringBuffer("");
				header.append(String.format(";" + "Aktiiviaika; KeskPalveluaika; Suoritusteho;\n"));
				pw.write(header.toString());
				header.append("\n");
				for (Palvelupiste p : palvelupisteet) {
					double kokonaisAika = Kello.getInstance().getAika();
					double keskPalveluaika = p.getPalvellutAsiakkaat() == 0 ? 0
							: p.getAktiiviaika() / p.getPalvellutAsiakkaat();
					double suoritusteho = p.getPalvellutAsiakkaat() / kokonaisAika;
					String rivi = String.format("%s;%f;%f;%f\n", p.getTyyppi().toString(), p.getAktiiviaika(),
							keskPalveluaika, suoritusteho);
					pw.write(rivi);
				}
			}

			pw.println();
			pw.flush();
			pw.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

}
