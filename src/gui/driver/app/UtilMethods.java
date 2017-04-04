package gui.driver.app;

import java.util.ArrayList;

import data.members.StickersColor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;

public class UtilMethods {

	public static String getStickerColor(final StickersColor color) {
		switch (color) {
		case BLUE:
			return "Blue";
		case BORDEAUX:
			return "Bordeaux";
		case GREEN:
			return "Green";
		case RED:
			return "Red";
		case WHITE:
			return "White";
		case YELLOW:
			return "Yellow";
		default:
			return "";
		}
	}

	// onEntry checks if you just entered the window or you clicked on the mute
	// button
	public static void mute(final MediaPlayer p, final ArrayList<Button> muteButtonsAL) {
		for (final Button currButton : muteButtonsAL)
			currButton.setGraphic(new ImageView(new Image(
					UtilMethods.class.getResourceAsStream(!p.isMute() ? "mute_button.png" : "unmute_button.png"))));
		p.setMute(!p.isMute());
	}

	public static Button clone(final Button firstButton) {
		final Button button = new Button();
		button.setText(firstButton.getText());
		button.getStyleClass().addAll(firstButton.getStyleClass());
		button.setOnAction(e -> UtilMethods.mute(AbstractWindow.mediaPlayer, AbstractWindow.muteButtonsAL));
		return button;
	}

}
