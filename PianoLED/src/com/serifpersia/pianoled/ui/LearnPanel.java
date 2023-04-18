package com.serifpersia.pianoled.ui;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.serifpersia.pianoled.PianoController;
import com.serifpersia.pianoled.learn.MidiPlayer;
import com.serifpersia.pianoled.learn.MidiPlayerConsumer;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class LearnPanel extends JPanel implements MidiPlayerConsumer {

	public static JComboBox<?> MidiOutList;
	protected MidiPlayer midiPlayer;

	JButton lbPlayMidi = new JButton("⏯");

	public LearnPanel() {
		setBackground(new Color(21, 25, 28));
		setLayout(new BorderLayout(0, 0));

		JPanel slideControlsPane = new JPanel();
		slideControlsPane.setBackground(Color.RED);
		slideControlsPane.setVisible(false);
		add(slideControlsPane, BorderLayout.EAST);
		slideControlsPane.setLayout(new BorderLayout(0, 0));
		// Add mouse listener to slideControlsPane
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int width = getWidth();
				if (width - x <= 150) {
					// Mouse is near the right border of LearnPanel
					slideControlsPane.setVisible(true);
				} else {
					// Mouse is not near the right border of LearnPanel
					slideControlsPane.setVisible(false);
				}
			}
		});

		slideControlsPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int width = slideControlsPane.getWidth();
				if (x <= 0 || x >= width || e.getY() <= 0 || e.getY() >= slideControlsPane.getHeight()) {
					// Mouse is out of slideControlsPane
					slideControlsPane.setVisible(false);
				}
			}
		});

		// Add hotkey to toggle visibility of slideControlsPane
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK),
				"toggleControls");
		getActionMap().put("toggleControls", new AbstractAction() {
			boolean controlsVisible = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				controlsVisible = !controlsVisible;
				slideControlsPane.setVisible(controlsVisible);
				if (controlsVisible) {
					System.out.println("Ctrl+C(Controls_Visible)");
				} else {
					System.out.println("Ctrl+C(Controls_Hidden)");
				}
			}
		});

		JLabel lbControls = new JLabel("Controls");
		lbControls.setHorizontalAlignment(SwingConstants.CENTER);
		lbControls.setForeground(Color.WHITE);
		lbControls.setFont(new Font("Tahoma", Font.BOLD, 40));
		slideControlsPane.add(lbControls, BorderLayout.NORTH);

		JPanel controlsPane = new JPanel();
		controlsPane.setBackground(Color.BLACK);
		slideControlsPane.add(controlsPane, BorderLayout.EAST);
		GridBagLayout gbl_controlsPane = new GridBagLayout();
		gbl_controlsPane.columnWidths = new int[] { 25, 0, 0, 0, 0 };
		gbl_controlsPane.rowHeights = new int[] { 209, 0, 0, 209, 209, 0 };
		gbl_controlsPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_controlsPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		controlsPane.setLayout(gbl_controlsPane);

		JLabel midiFileName = addMidiFileNameLabel(controlsPane, 1);
		addLoadMidiFileControl(controlsPane, 0, midiFileName);
		addMidiOutListControl(controlsPane, 2);
		addPlaybackControls(controlsPane, 3);
	}

	private void addLoadMidiFileControl(JPanel controlsPane, int gridy, JLabel midiFileName) {
		JButton lbLoadMidi = new JButton("Load Midi File");
		GridBagConstraints gbc_lbLoadMidi = new GridBagConstraints();
		gbc_lbLoadMidi.gridwidth = 4;
		gbc_lbLoadMidi.insets = new Insets(0, 0, 0, 0);
		gbc_lbLoadMidi.gridx = 0;
		gbc_lbLoadMidi.gridy = gridy;
		controlsPane.add(lbLoadMidi, gbc_lbLoadMidi);
		lbLoadMidi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				// Show the file chooser dialog and check the user's choice
				int returnValue = fileChooser.showOpenDialog(LearnPanel.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					midiFileName.setText(selectedFile.getName());
					String deviceName = (String) MidiOutList.getSelectedItem();
					midiPlayer = new MidiPlayer(new File(selectedFile.getAbsolutePath()),
							getMidiDeviceByName(deviceName));
					midiPlayer.setConsumer(LearnPanel.this);
				}
			}
		});
	}

	private JLabel addMidiFileNameLabel(JPanel controlsPane, int gridy) {
		JLabel midiFileName = new JLabel("");
		GridBagConstraints gbc_lbMidiFileName = new GridBagConstraints();
		gbc_lbMidiFileName.gridwidth = 4;
		gbc_lbMidiFileName.insets = new Insets(0, 0, 5, 0);
		gbc_lbMidiFileName.gridx = 0;
		gbc_lbMidiFileName.gridy = gridy;
		midiFileName.setFont(new Font("Montserrat", Font.PLAIN, 12));
		midiFileName.setForeground(Color.WHITE);
		controlsPane.add(midiFileName, gbc_lbMidiFileName);
		return midiFileName;
	}

	private int addMidiOutListControl(JPanel controlsPane, int gridy) {
		MidiOutList = new JComboBox<Object>(PianoController.getMidiOutDevices());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 4;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = gridy;
		controlsPane.add(MidiOutList, gbc_comboBox);
		MidiOutList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMidiPlayerOutputDevice();
			}

		});
		return gridy;
	}

	private void setMidiPlayerOutputDevice() {
		String deviceName = (String) MidiOutList.getSelectedItem();
		System.out.println("Midi Out Device: " + deviceName);
		try {
			if (midiPlayer != null)
				midiPlayer.setOutputDevice(getMidiDeviceByName(deviceName));
		} catch (MidiUnavailableException e1) {
			System.out.println("Error connecting to Midi Out Device: " + deviceName);
			e1.printStackTrace();
		}
	}

	private MidiDevice getMidiDeviceByName(String deviceName) {
		MidiDevice.Info[] deviceInfo = MidiSystem.getMidiDeviceInfo();
		for (Info info : deviceInfo) {
			if (info.getName().equalsIgnoreCase(deviceName)) {
				try {
					return MidiSystem.getMidiDevice(info);
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private void addPlaybackControls(JPanel controlsPane, int gridy) {
		Font midiControlsFont = new Font("Montserrat", Font.BOLD, 16);

		JButton lbStart = new JButton("⏮");
		GridBagConstraints gbc_lbStart = new GridBagConstraints();
		gbc_lbStart.anchor = GridBagConstraints.NORTH;
		gbc_lbStart.insets = new Insets(0, 0, 5, 5);
		gbc_lbStart.gridx = 0;
		gbc_lbStart.gridy = gridy;
		lbStart.setFont(midiControlsFont);
		controlsPane.add(lbStart, gbc_lbStart);
		lbStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				midiPlayer.rewind();
			}
		});

		JButton lbGoBack = new JButton("⏪");
		GridBagConstraints gbc_lbGoBack = new GridBagConstraints();
		gbc_lbGoBack.anchor = GridBagConstraints.NORTH;
		gbc_lbGoBack.insets = new Insets(0, 0, 5, 5);
		gbc_lbGoBack.gridx = 1;
		gbc_lbGoBack.gridy = gridy;
		lbGoBack.setFont(midiControlsFont);
		controlsPane.add(lbGoBack, gbc_lbGoBack);
		lbGoBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				midiPlayer.rewind(-5);
			}
		});

		GridBagConstraints gbc_lbPlayMidi = new GridBagConstraints();
		gbc_lbPlayMidi.anchor = GridBagConstraints.NORTH;
		gbc_lbPlayMidi.insets = new Insets(0, 0, 5, 5);
		gbc_lbPlayMidi.gridx = 2;
		gbc_lbPlayMidi.gridy = gridy;
		lbPlayMidi.setFont(midiControlsFont);
		controlsPane.add(lbPlayMidi, gbc_lbPlayMidi);
		lbPlayMidi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				midiPlayer.playPause();
				if (midiPlayer.isPaused()) {
					setPlaybackButtonStop();
				} else {
					setPlaybackButtonPlay();
				}
			}

		});

		JButton lbGoForwards = new JButton("⏩");
		GridBagConstraints gbc_lbGoForwards = new GridBagConstraints();
		gbc_lbGoForwards.anchor = GridBagConstraints.NORTH;
		gbc_lbGoForwards.insets = new Insets(0, 0, 5, 0);
		gbc_lbGoForwards.gridx = 3;
		gbc_lbGoForwards.gridy = gridy;
		lbGoForwards.setFont(midiControlsFont);
		controlsPane.add(lbGoForwards, gbc_lbGoForwards);
		lbGoForwards.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				midiPlayer.rewind(5);
			}
		});
	}

	private void setPlaybackButtonStop() {
		lbPlayMidi.setText("⏯");
	}

	private void setPlaybackButtonPlay() {
		lbPlayMidi.setText("⏸");
	}

	@Override
	public void onPlaybackFinished() {
		setPlaybackButtonStop();
	}
}