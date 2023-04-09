package ui;

import java.awt.Color;

import com.serifpersia.pianoled.Arduino;

public class ModesController {
	private Arduino arduino;

	static boolean BGColor = false;

	public static boolean VelocityOn = false;

	public static boolean RandomOn = false;

	public static boolean SplitOn = false;

	public static boolean GradientOn = false;

	public static boolean SplashOn = false;

	public static boolean AnimationOn = false;

	private void disableAllModes() {
		RandomOn = false;
		VelocityOn = false;
		AnimationOn = false;
		SplitOn = false;
		GradientOn = false;
		SplashOn = false;
	}

	public void modeSelect(int n) {
		if (arduino != null)
			arduino.sendCommandBlackOut();

		switch (GetUI.getModeName(n)) {
		case "Default":
			disableAllModes();
			// GetUI.hideAllControls();
			// GetUI.showDefaultControls();
			GetUI.setDefaults(255, 255);
			break;
		case "Splash":
			disableAllModes();
			GetUI.setSplashDefaults(255, 80, 8);
			ControlsPanel.selectedColor = Color.BLACK;
			SplashOn = true;
			break;
		case "Random":
			disableAllModes();
			GetUI.setDefaults(255, 255);
			RandomOn = true;
			break;
		case "Gradient":
			disableAllModes();
			GetUI.setDefaults(255, 255);
			GradientOn = true;
			break;
		case "Velocity":
			disableAllModes();
			GetUI.setDefaults(255, 255);
			VelocityOn = true;
			break;
		case "Split":
			disableAllModes();
			GetUI.setDefaults(255, 255);
			SplitOn = true;
			break;
		case "Animation":
			disableAllModes();
			GetUI.setDefaults(255, 255);
			AnimationOn = true;
			break;
		}
		System.out.println("Selected mode: " + GetUI.getModeName(n));
	}
}
