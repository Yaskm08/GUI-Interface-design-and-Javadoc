// Yassine Kraiem.
// CSC 240 Programming Activity 4: Exploring GUI Interface Design and Using Javadoc
// 12 November 2024.

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiniMusicPlayer {

    Sequencer sequencer;
    Sequence sequence;
    Track track;

    public static void main(String[] args) {
        MiniMusicPlayer player = new MiniMusicPlayer();
        player.buildGUI();
    }

    /**
     * Builds the main GUI for the MIDI sequencer.
     */
    public void buildGUI() {
        JFrame frame = new JFrame("MIDI Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        JPanel panel = new JPanel();
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartListener());

        panel.add(startButton);
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        frame.setVisible(true);

        setUpMidi();
    }

    /**
     * Sets up the MIDI sequencer and adds notes to the track for a more noticeable melody.
     */
    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setSequence(sequence);

            // Adding a sequence of notes for a more noticeable sound pattern
            int[] melodyNotes = {
                    60, 62, 64, 65, 67, 69, 71, 72, // Ascending scale
                    72, 71, 69, 67, 65, 64, 62, 60  // Descending scale
            };

            int tick = 1;
            for (int note : melodyNotes) {
                // Note on
                track.add(makeEvent(144, 1, note, 100, tick));
                // Note off after a short duration
                track.add(makeEvent(128, 1, note, 100, tick + 2));
                tick += 4; // Advance tick for the next note
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inner class to handle the start button action, allowing replay from the beginning.
     */
    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            if (sequencer != null && sequencer.isRunning()) {
                sequencer.stop(); // Stop the sequencer if it's already running
            }
            sequencer.setTickPosition(0); // Rewind to the beginning
            sequencer.start(); // Start playing from the beginning
            System.out.println("Playing...");
        }
    }

    /**
     * Utility method to make a MIDI event.
     *
     * @param com The command (144 for note on, 128 for note off)
     * @param chan The channel (1 in this case)
     * @param one The note number
     * @param two The velocity
     * @param tick The timing for the event
     * @return The MIDI event created
     */
    public static MidiEvent makeEvent(int com, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(com, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}
