/*
 * This screen will show basic information about the project and its contributors.  
 * 
 * @author zahimizrahi
 * 
 */

package gui.driver.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class About extends AbstractWindow {

	private static final String TEXT = "Smart Parking system is used for finding free parking slots within The Technion. \n" 
							 + "Given the location and the customer details, the system will provide location of the nearest available \n" 
							 + "parking slot and will update in real time in case of nearer parking slot became available. The system \n" 
							 + "will present a map of the Technion with mapping of the parking slots and navigates the customer toward \n" 
							 + "the chosen parking slot, in addition there will be a possibility to communicate with other car owners. \n"
							 + "This project is made under the course 'Yearly Project in Software' in Technion Institute of Technology \n"
							 + "by the skilled students: David Cohen, Zahi Mizrahi, Shahar Yair, Sefi Albo and Shay Segal. \n" ;
	
	public static final String IMAGE = "https://camo.githubusercontent.com/6110675e5337a30faa1f5dddf6120a8c37ce4250/687474703a2f2f6936372e74696e797069632e636f6d2f6e6d7a7770772e706e67";

	public About() {
		windowEnum = WindowEnum.ABOUT;
		window = new Stage();
		window.getIcons().add(new Image(getClass().getResourceAsStream("Smart_parking_icon.png")));
	}

	public void display(final Stage primaryStage, final WindowEnum __, final Button buttonMute) {
		window = primaryStage;
		window.setTitle("About");
		window.setWidth(1000);
		window.setHeight(650);
		final Label label = new Label(TEXT);
		label.setAlignment(Pos.CENTER);
		final ImageView iv = new ImageView (new Image (IMAGE)); 
		label.setStyle("-fx-background-color: #458AE5;\n" + "-fx-text-fill: white ;\n" + "-fx-background-radius: 10px;\n"
				+ "-fx-padding: 10px;\n" + "-fx-graphic-text-gap: 10px;\n" + "-fx-font-family: 'Arial';\n"
				+ "-fx-font-size: 14px;");
		final VBox vbox = new VBox(10);
		// System.out.println("HERE IS BUTTONMUTE: "+ buttonMute.toString());
		
		final Button muteButton = new Button();
		String image =  (AbstractWindow.mediaPlayer.isMute() ? "mute_button.png" : "unmute_button.png");
		setButtonGraphic (muteButton, image);
		muteButton.setOnAction(e -> UtilMethods.mute(mediaPlayer, AbstractWindow.muteButtonsAL));
		muteButtonsAL.add(muteButton);
		final HBox hbox = new HBox ();
		 hbox.setPadding(new Insets(10, 10, 10, 10));
		 final Pane spacer = new Pane();
		 HBox.setHgrow(spacer, Priority.ALWAYS);
		 spacer.setMinSize(80, 1);
		
	/*	if (!isLinuxOS) {
			final Button AboutMute = UtilMethods.clone(buttonMute);
			muteButtonsAL.add(AboutMute);
			vbox.getChildren().add(AboutMute);
		}
		*/
		 hbox.getChildren().addAll(spacer, muteButton);
		final Button backButton = new Button();
		backButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("back_button.png"))));
		backButton.getStyleClass().add("button-go");
		backButton.setOnAction(e -> {
			window.close();
			prevWindows.get(prevWindows.size() - 1).window.show();
			prevWindows.remove(prevWindows.size() - 1);
		});
		vbox.getChildren().addAll(hbox, iv, label, backButton);
		vbox.setAlignment(Pos.CENTER);
		final Scene scene = new Scene(vbox);
		vbox.setBackground(
				new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, new Insets(2, 2, 2, 2))));
		scene.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		window.setScene(scene);
		window.show();
	}
}
