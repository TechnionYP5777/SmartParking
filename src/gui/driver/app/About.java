/*
 * This screen will show basic information about the project and its contributors.  
 * 
 * @author zahimizrahi
 * 
 */

package gui.driver.app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
		label.setStyle("-fx-background-color: white;\n" + "-fx-text-fill: black;\n" + "-fx-background-radius: 10px;\n"
				+ "-fx-padding: 10px;\n" + "-fx-graphic-text-gap: 10px;\n" + "-fx-font-family: 'Arial';\n"
				+ "-fx-font-size: 14px;");
		final VBox vbox = new VBox(10);
		vbox.setStyle("-fx-background-color: null; -fx-padding: 10px;");
		// System.out.println("HERE IS BUTTONMUTE: "+ buttonMute.toString());

		if (!isLinuxOS) {
			final Button AboutMute = StaticMethods.cloneButton(buttonMute);
			muteButtonsAL.add(AboutMute);
			vbox.getChildren().add(AboutMute);
		}

		final Button backButton = new Button();
		backButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("back_button.png"))));
		backButton.getStyleClass().add("button-go");
		backButton.setOnAction(e -> {
			window.close();
			prevWindows.get(prevWindows.size() - 1).window.show();
			prevWindows.remove(prevWindows.size() - 1);
		});
		vbox.getChildren().addAll(iv, label, backButton);
		vbox.setAlignment(Pos.CENTER);
		final Scene scene = new Scene(vbox);
		window.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		window.show();
	}
}
