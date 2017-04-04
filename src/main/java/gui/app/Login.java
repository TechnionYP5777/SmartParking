/*
 * implementation of login screen. 
 * 
 * @author zahimizrahi
 * 
 */
package main.java.gui.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.java.logic.NavigationController;

public class Login extends AbstractWindow {

	GridPane grid;

	public Login() {
		windowEnum = WindowEnum.LOG_IN;
		window = new Stage();
		window.getIcons().add(new Image(getClass().getResourceAsStream("Smart_parking_icon.png")));
	}
	
	public void display(final Stage primaryStage, final WindowEnum __) {
		window = primaryStage;
		grid = new GridPane();
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setVgap(8);
		grid.setHgap(10);
		window.setWidth(600);
		window.setHeight(440);

		// title
		final DropShadow shadow = new DropShadow();
		shadow.setOffsetY(4.0);
		shadow.setColor(Color.color(0.4f, 0.4f, 0.4f));
		final Label title = new Label();
		title.setEffect(shadow);
		title.setTextFill(Color.ROYALBLUE);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setText("Login");
		title.setFont(Font.font(null, FontWeight.BOLD, 48));
		title.getStyleClass().add("label-title");
		// user
		final Label user = new Label("Car Number");
		final TextField nameInput = new TextField();
		nameInput.setPromptText("Car Number");

		// password
		final Label pass = new Label("Password");
		final PasswordField passInput = new PasswordField();
		passInput.setPromptText("Password");
		final Hyperlink forgotPass = new Hyperlink();
		forgotPass.setText("Forgot Password?");
		
		final Button muteButton = new Button();
		String image =  (AbstractWindow.mediaPlayer.isMute() ? "mute_button.png" : "unmute_button.png");
		setButtonGraphic (muteButton, image);
		
		forgotPass.setOnAction(e -> new GetPassByMail().display(primaryStage, WindowEnum.LOG_IN));
	
		muteButton.setOnAction(e -> UtilMethods.mute(mediaPlayer, AbstractWindow.muteButtonsAL));
		muteButtonsAL.add(muteButton);
		
		final Button loginButton = new Button(), backButton = new Button();
		setButtonGraphic (loginButton, "yes_button.png");
		setButtonGraphic (backButton, "back_button.png");
		backButton.setOnAction(e -> {
			// move to editing my details
			window.close();
			AbstractWindow.prevWindows.get(AbstractWindow.prevWindows.size() - 1).window.show();
			AbstractWindow.prevWindows.remove(AbstractWindow.prevWindows.size() - 1);
		});
		final HBox hbox = new HBox(20);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		GridPane.setConstraints(muteButton, 3,0);
		GridPane.setConstraints(title, 0, 1);
		GridPane.setColumnSpan(title, 2);
		GridPane.setConstraints(user, 0, 2);
		GridPane.setConstraints(nameInput, 1, 2);
		GridPane.setConstraints(pass, 0, 3);
		GridPane.setConstraints(passInput, 1, 3);
		hbox.getChildren().addAll(loginButton, backButton);
		GridPane.setConstraints(hbox, 1, 4);
		GridPane.setConstraints(forgotPass, 2, 5);


		grid.getChildren().addAll(muteButton, title, user, nameInput, pass, passInput, hbox, forgotPass);
		loginButton.setOnAction(e -> {
			if (!login.userLogin(nameInput.getText(), passInput.getText()))
				new AlertBox().display("Login failed", "Car Number/Password is incorrect.");
			else {
				navigate = new NavigationController(login.getUser());
				final AlertBox MB = new AlertBox();
				MB.display("Please Wait",
						"Connecting to server. \nThis might take a Few seconds. \nPlease confirm and wait patiently");
				MB.window.close();

				window.close();

				new AlertBox().display("Successful", "You have successfuly logged in");
				Opening.getCAObject(prevWindows).buttonLogin.setDisable(true);
				Opening.getCAObject(prevWindows).buttonRegister.setDisable(true);
				Opening.getCAObject(prevWindows).buttonChooseDestination.setDisable(false);
				Opening.getCAObject(prevWindows).buttonMyDetails.setDisable(false);
				Opening.getCAObject(prevWindows).buttonLogOut.setDisable(false);
				Opening.getCAObject(prevWindows).welcomeLabel.setText("Welcome " + login.getUserName());
				AbstractWindow.prevWindows.get(AbstractWindow.prevWindows.size() - 1).window.show();
				AbstractWindow.prevWindows.remove(AbstractWindow.prevWindows.size() - 1);
			}
		});
		grid.setBackground(
				new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, new Insets(2, 2, 2, 2))));
		final Scene scene = new Scene(grid);
		scene.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		window.setScene(scene);
		window.setTitle("Login");
		window.show();

	}

}
