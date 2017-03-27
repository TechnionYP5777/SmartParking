package gui.driver.app;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import logic.LoginManager;
import logic.NavigationController;

/*
 * abstract class of simple window, all other windows will inherited from this class. 
 * 
 * @author Shahar-Y
 * 
 */ 

public abstract class AbstractWindow {
	WindowEnum windowEnum;
	public Stage window;
	public static MediaPlayer mediaPlayer;
	public static Button buttonMute;
	public static boolean isLinuxOS;
	protected static ArrayList<Button> muteButtonsAL;
	protected static LoginManager login = new logic.LoginManager();
	protected static NavigationController navigate;
	protected static ArrayList<AbstractWindow> prevWindows;

	public Stage getStage() {
		return window;
	}
	
	//set button graphics for all button in class
	public void setButtonGraphic(Button b, String image) {
		b.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(image))));
		b.getStyleClass().add("button-go");	
	}

}
