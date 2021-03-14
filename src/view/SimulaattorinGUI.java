package view;

import controller.IKontrolleri;
import controller.Kontrolleri;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simu.framework.Trace;
import simu.framework.Trace.Level;

public class SimulaattorinGUI extends Application implements ISimulaattorinUI {
	private IKontrolleri kontrolleri;
	private Button kaynnistaButton;

	private TextField asiakaslkm;
	private TextField turvaPisteet;
	private TextField jakaumanKeskiarvo;
	private TextField todennakoisyys;

	private TextField kauppaPisteet;
	private TextField kahvilaPisteet;
	private TextField vessaPisteet;

	private IVisualisointi visualisointi;

	@Override
	public void init() {

		Trace.setTraceLevel(Level.INFO);

		kontrolleri = new Kontrolleri(this);
	}

	@Override
	public void start(Stage primaryStage) {
		// Käyttöliittymän rakentaminen
		try {

			visualisointi = new Visualisointi(800, 400, this);

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});
			primaryStage.setTitle("Simulaattori");

			Text asiakaslkmHint = new Text("Syötä asiakasmäärä");
			asiakaslkm = new TextField("420");

			Text turvaPisteetHint = new Text("Truvatarkastuspisteet (1-3)");
			turvaPisteet = new TextField("3");

			Text kauppaPisteetHint = new Text("Kauppapisteiden määrä (1-10)");
			kauppaPisteet = new TextField("1");

			Text kahvilaPisteetHint = new Text("Kahvilapisteiden määrä (1-10)");
			kahvilaPisteet = new TextField("1");

			Text vessaPisteetHint = new Text("Vessapisteiden määrä (1-10)");
			vessaPisteet = new TextField("1");

			Text jakaumanKeskiarvoHint = new Text("Eksponenttijakauman keskiarvo kauppajonoissa (3-100)");
			jakaumanKeskiarvo = new TextField("7");

			Text todennakoisyysHint = new Text("Todennäköisyys, että asiakas menee odotusalueeseen (0-100)");
			todennakoisyys = new TextField("50");

			kaynnistaButton = new Button();
			kaynnistaButton.setText("Käynnistä simulointi");
			kaynnistaButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					kontrolleri.kaynnistaSimulointi();
				}
			});

			VBox hBox = new VBox();
			hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylä, oikea, ala, vasen
			hBox.setSpacing(10); // noodien välimatka 10 pikseliä

			hBox.getChildren().addAll(kaynnistaButton, asiakaslkmHint, asiakaslkm, turvaPisteetHint, turvaPisteet,
					kauppaPisteetHint, kauppaPisteet, kahvilaPisteetHint, kahvilaPisteet, vessaPisteetHint,
					vessaPisteet, jakaumanKeskiarvoHint, jakaumanKeskiarvo, todennakoisyysHint, todennakoisyys);

			BorderPane bp = new BorderPane();

			bp.setLeft(hBox);
			bp.setCenter((Canvas) visualisointi);
			Scene scene = new Scene(bp);
			primaryStage.setScene(scene);

			primaryStage.setResizable(false);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int asiakasMaara() {
		int maara = -1;
		try {
			maara = Integer.parseInt(asiakaslkm.getText());

		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public int getTurvaLinjojenMaara() {
		int maara = -1;
		try {
			maara = Integer.parseInt(turvaPisteet.getText());

		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public void aktivoiUi(boolean aktivoi) {
		aktivoi = !aktivoi;
		kaynnistaButton.setDisable(aktivoi);
		asiakaslkm.setDisable(aktivoi);
		turvaPisteet.setDisable(aktivoi);
		jakaumanKeskiarvo.setDisable(aktivoi);
		todennakoisyys.setDisable(aktivoi);
		kauppaPisteet.setDisable(aktivoi);
		kahvilaPisteet.setDisable(aktivoi);
		vessaPisteet.setDisable(aktivoi);
	}

	private void naytaVirheDialogi(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Virhe");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void vaaraTurvaLinjojenMaara() {
		naytaVirheDialogi("Turvatarkastuslinjojen lukumäärä voi olla välillä 1-3");
	}

	// JavaFX-sovelluksen (käyttöliittymän) käynnistäminen

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void vaaraAsiakasMaara() {
		naytaVirheDialogi(" asiakkaat < 0!!!!");

	}

	@Override
	public int getJakaumanKeskiarvo() {
		int maara = -1;
		try {
			maara = Integer.parseInt(jakaumanKeskiarvo.getText());

		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public void vaaraJakauma() {
		naytaVirheDialogi("Jakauman keskiarvon on oltava välillä 3-100");
	}

	/**
	 * Tödennäköisyys, että asiakas menee odotusalueeseen
	 */
	@Override
	public int getTodennakoisyys() {
		int maara = -1;
		try {
			maara = Integer.parseInt(todennakoisyys.getText());

		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public void vaaraTodennakoisyys() {
		naytaVirheDialogi("todennakoisyys 0-100");
	}

	@Override
	public int getKauppaPisteidenMaara() {
		int maara = -1;
		try {
			maara = Integer.parseInt(kauppaPisteet.getText());
		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public int getKahvilaPisteidenMaara() {
		int maara = -1;
		try {
			maara = Integer.parseInt(kahvilaPisteet.getText());
		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public int getVessaPisteidenMaara() {
		int maara = -1;
		try {
			maara = Integer.parseInt(vessaPisteet.getText());
		} catch (NumberFormatException e) {
		}
		return maara;
	}

	@Override
	public void naytaTarkistaArvot() {
		naytaVirheDialogi("Tarkista arvot!");
	}

	@Override
	public IVisualisointi getVisualisointi() {
		return visualisointi;
	}

}
