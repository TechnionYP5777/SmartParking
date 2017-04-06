
/*
 * 
 * @author zahimizrahi
 * @author DavidCohen55
 * 
 */
package main.java.gui.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
import main.java.Exceptions.LoginException;
import main.java.data.members.StickersColor;

public class Register extends AbstractWindow {

	Register() {
		windowEnum = WindowEnum.REGISTER;
		window = new Stage();
		window.getIcons().add(new Image(getClass().getResourceAsStream("Smart_parking_icon.png")));
	}

	public void display(final Stage primaryStage, final WindowEnum __) {


		window = primaryStage;
		window.getIcons().add(new Image(getClass().getResourceAsStream("Smart_parking_icon.png")));
		final GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setVgap(8);
		grid.setHgap(10);
		window.setWidth(450);
		window.setHeight(580);

		// title
		final DropShadow shadow = new DropShadow();
		shadow.setOffsetY(4.0);
		shadow.setColor(Color.color(0.4f, 0.4f, 0.4f));
		final Label title = new Label();
		title.setEffect(shadow);
		title.setTextFill(Color.ROYALBLUE);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setText("Register");
		title.setFont(Font.font(null, FontWeight.BOLD, 48));
		title.getStyleClass().add("label-title");

		// user
		final Label user = new Label("Username");
		final TextField nameInput = new TextField();
		nameInput.setPromptText("username");

		// password
		final Label pass = new Label("Password");
		final PasswordField passInput = new PasswordField();
		passInput.setPromptText("password");

		// phone number
		final HBox hboxPhone = new HBox();
		final Label phoneNumber = new Label("Phone Number");
		final ChoiceBox<String> prefixNumber = new ChoiceBox<>();
		prefixNumber.getItems().addAll("050", "052", "053", "054", "057");
		prefixNumber.setValue("050");
		prefixNumber.getStyleClass().add("cb");
		final TextField phoneNumberInput = new TextField();
		phoneNumberInput.setMaxWidth(95);
		// phoneNumberInput.setMaxWidth(50);
		phoneNumberInput.setPromptText("phone number");
		hboxPhone.getChildren().addAll(prefixNumber, phoneNumberInput);

		// car number
		final Label carNumber = new Label("Car Number");
		final TextField carNumberInput = new TextField();
		carNumberInput.setPromptText("car number");

		// email
		final Label mail = new Label("E-Mail");
		final TextField mailInput = new TextField();
		mailInput.setPromptText("e-mail");

		final Label sticker = new Label("Sticker Color");
		final ChoiceBox<String> stickerColor = new ChoiceBox<>();
		stickerColor.getItems().addAll("Blue", "Green", "White", "Red", "Bordeaux", "Yellow");
		stickerColor.setValue("Blue");
		stickerColor.getStyleClass().add("cb");
		final Hyperlink wantLogin = new Hyperlink();
		wantLogin.setText("Already Registered?");
		wantLogin.setOnAction(e -> {
			window.close();
			new Login().display(primaryStage, WindowEnum.SIGN_UP);
		});

		final Button registerButton = new Button(), backButton = new Button();
		final Button muteButton = new Button();
		String image =  (AbstractWindow.mediaPlayer.isMute() ? "mute_button.png" : "unmute_button.png");
		setButtonGraphic (muteButton, image);
		setButtonGraphic (backButton, "back_button.png");
		setButtonGraphic (registerButton, "yes_button.png");
		backButton.setOnAction(e -> {
			// move to editing my details
			window.close();
			AbstractWindow.prevWindows.get(AbstractWindow.prevWindows.size() - 1).window.show();
			AbstractWindow.prevWindows.remove(AbstractWindow.prevWindows.size() - 1);
		});

		muteButton.setOnAction(e -> UtilMethods.mute(mediaPlayer, AbstractWindow.muteButtonsAL));
		muteButtonsAL.add(muteButton);
		final HBox hbox = new HBox(20);
		GridPane.setConstraints(muteButton, 3,0);
		GridPane.setConstraints(title, 0, 1);
		GridPane.setColumnSpan(title, 3);
		GridPane.setConstraints(user, 0, 2);
		GridPane.setConstraints(nameInput, 1, 2);
		GridPane.setConstraints(pass, 0, 3);
		GridPane.setConstraints(passInput, 1, 3);
		GridPane.setConstraints(hboxPhone, 0, 4);
		GridPane.setConstraints(phoneNumber, 0, 4);
		GridPane.setConstraints(hboxPhone, 1, 4);
		GridPane.setColumnSpan(hboxPhone, 2);
		GridPane.setConstraints(carNumber, 0, 5);
		GridPane.setConstraints(carNumberInput, 1, 5);
		GridPane.setConstraints(mail, 0, 6);
		GridPane.setConstraints(mailInput, 1, 6);
		GridPane.setConstraints(sticker, 0, 7);
		GridPane.setConstraints(stickerColor, 1, 7);
		GridPane.setConstraints(hbox, 1, 8);
		GridPane.setConstraints(wantLogin, 1, 9);
		GridPane.setColumnSpan(wantLogin, 2);

		hbox.getChildren().addAll(registerButton, backButton);
		grid.getChildren().addAll(muteButton , title, user, nameInput, pass, passInput, phoneNumber, hboxPhone, carNumber,
				carNumberInput, mail, mailInput, sticker, stickerColor, hbox, wantLogin);
		registerButton.setOnAction(e -> {
			try {
				final String name = nameInput.getText(), password = passInput.getText(),
						phone = prefixNumber.getSelectionModel().getSelectedItem() + phoneNumberInput.getText(),
						car = carNumberInput.getText(), eMail = mailInput.getText();
				final StickersColor type = StickersColor
						.valueOf(stickerColor.getSelectionModel().getSelectedItem().toUpperCase());
				if ("".equals(name) || "".equals(password) || phone.length() == 3 || "".equals(car) || "".equals(eMail))
					throw new LoginException("All of the fields should be full");
				login.userSignUp(name, password, phone, car, eMail, type);
				new AlertBox().display("Sign Up", "You Successfully Signed Up!");
				window.close();
				AbstractWindow.prevWindows.get(AbstractWindow.prevWindows.size() - 1).window.show();
				AbstractWindow.prevWindows.remove(AbstractWindow.prevWindows.size() - 1);
			} catch (final LoginException e1) {
				new AlertBox().display("Sign Up", e1 + "");
			}
		});
		grid.setBackground(
				new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, new Insets(2, 2, 2, 2))));
		final Scene scene = new Scene(grid);
		scene.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		window.setScene(scene);
		window.setTitle("Register");
		window.show();
	}

}