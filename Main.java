import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    PlayPanel playPanel;

    public Main() {
        setTitle("<Tetris>");
        setLayout(new BorderLayout(10, 10));

        playPanel = new PlayPanel();
        add("Center", playPanel);


        JButton startPauseButton, exitButton;
        startPauseButton = new JButton("Pause");
        exitButton = new JButton("Exit");


        JPanel gridPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        gridPanel.add(startPauseButton);
        gridPanel.add(exitButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(gridPanel);
        add("South", buttonPanel);


        startPauseButton.addActionListener(e -> {
            JButton button = (JButton) e.getSource();
            if (playPanel.isPause()) {
                button.setText("Pause");
                playPanel.resume();
                playPanel.requestFocus();
            } else {
                button.setText("Start");
                playPanel.pause();
            }
        });
        exitButton.addActionListener(e->{
            startPauseButton.setText("Start");
            playPanel.pause();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "really?", "tetris", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) (PlayPanel.mGetWidth() * 1.2), (int) (PlayPanel.mGetHeight() * 1.2));
        System.out.println(getSize());
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();

    }

}
