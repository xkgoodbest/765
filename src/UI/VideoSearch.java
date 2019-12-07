package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
//import org.opencv.core.Core;

public class VideoSearch {
    private JFrame frame;
    Player qPlayer;
    Player rPlayer;

    public VideoSearch() {
        frame = new JFrame("baseFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
    }

    public void addComponentsToPane(Container pane) {


        BufferedImage imgOne = new BufferedImage(352, 288, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgTwo = new BufferedImage(352, 40, BufferedImage.TYPE_INT_RGB);
        JLabel qPlayImg = new JLabel(new ImageIcon(imgOne));
        JLabel rPlayImg = new JLabel(new ImageIcon(imgOne));
        JLabel sim = new JLabel(new ImageIcon(imgTwo));

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;

        JTextField queryBox = new JTextField("/Users/xkgoodbest/Programs/576_project/query/first");
        queryBox.setPreferredSize( new Dimension( 200, 24 ) );
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(queryBox, c);
        queryBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = queryBox.getText();
                qPlayer = new Player(path, "Query", qPlayImg);
            }
        });


        JPanel resultPanel = new JPanel(new GridBagLayout());
        c.ipady = 40;
        c.gridx = 1;
        c.gridy = 0;
        pane.add(resultPanel, c);

        ButtonGroup bG = new ButtonGroup();
        JRadioButton[] matches = new JRadioButton[3];
        matches[0] = new JRadioButton("/Users/xkgoodbest/Programs/576_project/database_videos/movie");
        matches[1] = new JRadioButton("/Users/xkgoodbest/Programs/576_project/database_videos/starcraft");
        matches[2] = new JRadioButton("/Users/xkgoodbest/Programs/576_project/database_videos/sports");


        c.ipady = 0;
        c.gridx = 0;
        c.ipadx = 200;
        for(int i = 0; i < matches.length; i++) {
            bG.add(matches[i]);
            c.gridy = i;
            resultPanel.add(matches[i], c);
        }
        ActionListener matchesListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton aButton = (AbstractButton) actionEvent.getSource();
                System.out.println(aButton.getText());
                String path = aButton.getText();
                rPlayer = new Player(path, "Result", rPlayImg);
            }
        };
        matches[0].addActionListener(matchesListener);
        matches[1].addActionListener(matchesListener);
        matches[2].addActionListener(matchesListener);

        JPanel placeTaker = new JPanel(new GridBagLayout());
        c.ipadx = 200;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(placeTaker, c);

        JPanel slidePanel = new JPanel(new GridBagLayout());
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(slidePanel, c);

        JSlider slider = new JSlider(JSlider.HORIZONTAL);
        slider.setValue(0);
        c.ipadx = 0;
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 0;
        slidePanel.add(sim, c);
        c.gridy = 1;
        slidePanel.add(slider, c);
        slider.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rPlayer.adjustTimeLine(slider.getValue()*6);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        final Timer increaseValue = new Timer(0, new ActionListener() {// 50 ms interval in each increase.
            public void actionPerformed(ActionEvent e) {
                if (slider.getMaximum() != slider.getValue()) {
                    if(rPlayer.pv.currentFrame % 6 == 0)
                    {
                        slider.setValue(rPlayer.pv.currentFrame/6);
                    }

                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });


        JPanel queryPlayerPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 2;
        pane.add(queryPlayerPanel, c);
        queryPlayerPanel.add(qPlayImg);

        JPanel resultPlayerPanel = new JPanel(new GridBagLayout());
        c.gridx = 1;
        c.gridy = 2;
        pane.add(resultPlayerPanel, c);
        resultPlayerPanel.add(rPlayImg);

        JPanel qControlers = new JPanel(new GridBagLayout());
        c.ipady = 40;
        c.ipadx = 300;
        c.gridx = 0;
        c.gridy = 3;
        pane.add(qControlers, c);

        JButton qPlay = new JButton("Play");

        JButton qPause = new JButton("Pause");
        JButton qStop = new JButton("Stop");
        qControlers.add(qPlay);
        qControlers.add(qPause);
        qControlers.add(qStop);
        
        qPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qPlayer.play();
            }
        });
        qPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qPlayer.pause();
            }
        });
        qStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qPlayer.stop();
            }
        });

        JPanel rControlers = new JPanel(new GridBagLayout());
        c.ipady = 40;
        c.ipadx = 300;
        c.gridx = 1;
        c.gridy = 3;
        pane.add(rControlers, c);

        JButton rPlay = new JButton("Play");
        JButton rPause = new JButton("Pause");
        JButton rStop = new JButton("Stop");
        rControlers.add(rPlay);
        rControlers.add(rPause);
        rControlers.add(rStop);

        rPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rPlayer.play();
                increaseValue.start();
            }
        });
        rPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rPlayer.pause();
            }
        });
        rStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rPlayer.stop();
            }
        });
    }

    public static void main(String[] args) {
        VideoSearch vs = new VideoSearch();
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
