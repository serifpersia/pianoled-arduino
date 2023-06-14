package com.serifpersia.pianoled;

import com.serifpersia.pianoled.ui.GetUI;

public class ModesController {

	PianoLED pianoLED;
	static boolean BGColor = false;

	public static boolean VelocityOn = false;

	public static boolean RandomOn = false;

	public static boolean SplitOn = false;

	public static boolean GradientOn = false;

	public static boolean SplashOn = false;

	public static boolean AnimationOn = false;

	public static boolean Gradient2Side = true;

	public static boolean Gradient3Side = false;

	public static boolean Gradient4Side = false;

	public static boolean Gradient5Side = false;

	public static boolean Gradient6Side = false;

	public static boolean Gradient7Side = false;

	public static boolean Gradient8Side = false;

	public ModesController(PianoLED pianoLED) {
		this.pianoLED = pianoLED;
	}

	private void disableAllModes() {
		RandomOn = false;
		VelocityOn = false;
		AnimationOn = false;
		SplitOn = false;
		GradientOn = false;
		SplashOn = false;

	}

	private void disableGradient() {
		Gradient2Side = false;
		Gradient3Side = false;
		Gradient4Side = false;
		Gradient5Side = false;
		Gradient6Side = false;
		Gradient7Side = false;
		Gradient8Side = false;
	}

	public void modeSelect(int n) {
		if (pianoLED.getPianoController().arduino != null)
			pianoLED.getPianoController().arduino.sendCommandBlackOut();

		switch (GetUI.getModeName(n)) {
		case "Default":
			disableAllModes();
			GetUI.setDefaults(8, 255, 0);
			break;
		case "Splash":
			disableAllModes();
			GetUI.setDefaults(8, 255, 150);
			SplashOn = true;
			break;
		case "Random":
			disableAllModes();
			GetUI.setDefaults(8, 255, 0);
			RandomOn = true;
			break;
		case "Gradient":
			disableAllModes();
			GetUI.setDefaults(8, 255, 0);
			GradientOn = true;
			break;
		case "Velocity":
			disableAllModes();
			GetUI.setDefaults(8, 255, 0);
			VelocityOn = true;
			break;
		case "Split":
			disableAllModes();
			GetUI.setDefaults(8, 255, 0);
			SplitOn = true;
			break;
		case "Animation":
			disableAllModes();
			GetUI.setDefaults(8, 255, 255);
			AnimationOn = true;
			break;
		}
	}

	public void gradientSideSelect(int n) {
		if (pianoLED.getPianoController().arduino != null)
			pianoLED.getPianoController().arduino.sendCommandBlackOut();

		switch (GetUI.getGradientName(n)) {
		case "2 Side Gradient":
			disableGradient();
			Gradient2Side = true;
			break;
		case "3 Side Gradient":
			disableGradient();
			Gradient3Side = true;
			break;
		case "4 Side Gradient":
			disableGradient();
			Gradient4Side = true;
			break;
		case "5 Side Gradient":
			disableGradient();
			Gradient5Side = true;
			break;
		case "6 Side Gradient":
			disableGradient();
			Gradient6Side = true;
			break;
		case "7 Side Gradient":
			disableGradient();
			Gradient7Side = true;
			break;
		case "8 Side Gradient":
			disableGradient();
			Gradient8Side = true;
			break;
		default:
			break;
		}
	}

	public static int getGradientSideCount() {
		if (Gradient2Side) {
			return 1;
		} else if (Gradient3Side) {
			return 2;
		} else if (Gradient4Side) {
			return 3;
		} else if (Gradient5Side) {
			return 4;
		} else if (Gradient6Side) {
			return 5;
		} else if (Gradient7Side) {
			return 6;
		} else if (Gradient8Side) {
			return 7;
		} else {
			return -1; // No gradient side count selected
		}
	}

}
