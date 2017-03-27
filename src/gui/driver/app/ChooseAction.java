package gui.driver.app;

/*
 * main window of the GUI. 
 * 
 * @author zahimizrahi & Shahar-Y
 * 
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.LoginManager;

import java.net.URL;
import java.util.ArrayList;

import gui.map.DriverMap;

public class ChooseAction extends AbstractWindow {
	public Button buttonAbout;
	Button buttonLogin;
	Button buttonMyDetails;
	Button buttonRegister;
	Button buttonChooseDestination;
	Button buttonMap;
	Button buttonLogOut;
	Button buttonMute;
	Label welcomeLabel;

	public ChooseAction() {
		windowEnum = WindowEnum.CHOOSE_ACTION;
		muteButtonsAL = new ArrayList<Button>();
		window = new Stage();
		buttonMute = new Button();
		window.getIcons().add(new Image(getClass().getResourceAsStream("Smart_parking_icon.png")));
		window.initModality(Modality.APPLICATION_MODAL);
	}
	
/*
	//set button graphics for all button in class
	public void setButtonGraphic(Button b, String image) {
		b.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(image))));
		b.getStyleClass().add("button-go");	
	}
	*/
	
	public void display(final Stage primaryStage, final WindowEnum prevWindow) {
		window.setTitle("What Would you like to do?");
		window.setMinWidth(750);
		final VBox vbox = new VBox(8);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		isLinuxOS = "Linux".equals(System.getProperty("os.name"));

		final URL resource = getClass().getResource("sound.mp3");
		mediaPlayer = new MediaPlayer(new Media(resource + ""));
		mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
		mediaPlayer.play();
		buttonMute = new Button();
		buttonMute.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("unmute_button.png"))));
		buttonMute.getStyleClass().add("button-go");
		// buttonMute.setDisable(false);
		// buttonMute.getStyleClass().clear();

		buttonMute.setOnAction(e -> UtilMethods.mute(mediaPlayer, AbstractWindow.muteButtonsAL));
		muteButtonsAL.add(buttonMute);
		// buttonMute.getStyleClass().add("button-muteOFF");

		// TODO: get a better way to check logged in
		final HBox mute = new HBox(1);
		welcomeLabel = new Label();
		try {
			welcomeLabel.setText("Welcome " + login.getUserName() + "!");
		} catch (final Exception e) {
			welcomeLabel.setText("Welcome! You are not logged in");
		}
		welcomeLabel.getStyleClass().add("label-welcome");
		
		
		buttonAbout = new Button("About");
		setButtonGraphic (buttonAbout, "about_button.png"); 
		buttonAbout.setOnAction(λ -> {
			window.close();
			prevWindows.add(this);
			new About().display(primaryStage, WindowEnum.CHOOSE_ACTION, buttonMute);
		});

		buttonLogin = new Button("Login");
		setButtonGraphic (buttonLogin, "login_button.png");;
		buttonLogin.setOnAction(e -> {
			window.close();
			final Login login = new Login();
			AbstractWindow.prevWindows.add(this);
			login.display(primaryStage, WindowEnum.CHOOSE_ACTION);
		});

		buttonRegister = new Button("Register");
		setButtonGraphic (buttonRegister, "register_button.png"); 
		buttonRegister.setOnAction(λ -> {
			window.close();
			AbstractWindow.prevWindows.add(this);
			new Register().display(primaryStage, WindowEnum.CHOOSE_ACTION);
		});


		buttonMap = new Button("View Map");
		setButtonGraphic (buttonMap, "map_button.png"); 
		buttonMap.setOnAction(λ -> {
			window.close();
			AbstractWindow.prevWindows.add(this);
			new DriverMap("32.777789, 35.022054", "32.761565, 35.019438").display(primaryStage);
		});


		buttonMyDetails = new Button("My Details");
		setButtonGraphic (buttonMyDetails, "details_button.png"); 
		buttonMyDetails.setOnAction(e -> {
			window.close();
			final MyDetails MD = new MyDetails();
			AbstractWindow.prevWindows.add(this);
			MD.display(primaryStage, WindowEnum.CHOOSE_ACTION, null, null);

		});
		buttonMyDetails.setDisable(true);

		buttonChooseDestination = new Button("Choose Destination");
		setButtonGraphic (buttonChooseDestination, "choose_destination_button.png"); 
		buttonChooseDestination.setOnAction(e -> {
			window.close();
			final ChooseDestination CD = new ChooseDestination();
			AbstractWindow.prevWindows.add(this);
			CD.display(primaryStage);

		});
		buttonChooseDestination.setDisable(true);

		final Button buttonClose = new Button("Exit");
		setButtonGraphic (buttonClose, "exit_button.png"); 
		buttonClose.setOnAction(λ -> {
			if (prevWindow == WindowEnum.NONE
					&& new ConfirmBox().display("Confirmation", "Are you sure you want to exit?"))
				window.close();
		});

		buttonLogOut = new Button("Log Out");
		setButtonGraphic (buttonLogOut, "logout_button.png"); 
		buttonLogOut.setOnAction(λ -> {
			if (prevWindow == WindowEnum.NONE
					&& new ConfirmBox().display("Confirmation", "Are you sure you want to log out?")) {
				welcomeLabel.setText("Welcome. You are not logged in");
				login = new LoginManager();
				setButtonsDefaultValues();
				// window.close();
			}
		});
		buttonLogOut.setDisable(true);
		// buttonLogOut.getStyleClass().add("button-menu");

		if (!isLinuxOS)
			mute.getChildren().add(buttonMute);
		mute.setAlignment(Pos.TOP_RIGHT);

		final HBox hbox1 = new HBox(5), hbox2 = new HBox(5);
		hbox1.getChildren().addAll(buttonAbout, buttonLogin, buttonRegister, buttonChooseDestination);
		hbox2.getChildren().addAll(buttonMap, buttonMyDetails, buttonLogOut, buttonClose);
		vbox.getChildren().addAll(mute, welcomeLabel, hbox1, hbox2);
		vbox.setAlignment(Pos.CENTER);
		final Scene scene = new Scene(vbox);
		scene.getStylesheets().add(

				getClass().getResource("mainStyle.css").toExternalForm());
		window.setScene(scene);
		window.showAndWait();
	}

	public void setButtonsDefaultValues() {
		buttonLogin.setDisable(false);
		buttonMute.setDisable(false);
		buttonMyDetails.setDisable(true);
		buttonRegister.setDisable(false);
		buttonChooseDestination.setDisable(true);
		buttonMap.setDisable(false);
		buttonLogOut.setDisable(true);
	}

}
