package view;

public interface ISimulaattorinUI {
	public int asiakasMaara();
	
	public int getTurvaLinjojenMaara();
	
	public int getJakaumanKeskiarvo();

	public int getTodennakoisyys();
	
	public void vaaraTurvaLinjojenMaara();

	public void vaaraAsiakasMaara();

	public void vaaraJakauma();

	public void vaaraTodennakoisyys();

	public int getKauppaPisteidenMaara();

	public int getKahvilaPisteidenMaara();

	public int getVessaPisteidenMaara();

	public void naytaTarkistaArvot();
	
	public IVisualisointi getVisualisointi();

	public void aktivoiUi(boolean aktivoi);
}

