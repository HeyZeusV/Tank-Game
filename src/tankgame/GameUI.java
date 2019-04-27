package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameUI extends JComponent {

    private Tank t1;
    private Tank t2;
    private BufferedImage image;

    public GameUI(Tank t1, Tank t2) {

        this.t1 = t1;
        this.t2 = t2;

        init();
    }

    private void init() {

        try {
            image = ImageIO.read(getClass().getResource("resources/images/Heart.png"));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public void drawImage(Graphics g) {

        paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        int distancep1 = 10;
        int distancep2 = 10;
        // outputs the hearts
        for (int i = 0; i < t1.getLives(); i++) {
            g2d.drawImage(image, t1.getX() + distancep1, t1.getY() - 20, this);
            distancep1 += 12;
        }
        for (int i = 0; i < t2.getLives(); i++) {
            g2d.drawImage(image, t2.getX() + distancep2, t2.getY() - 20, this);
            distancep2 += 12;
        }
        // outputs the healthbars
        g2d.setColor(Color.WHITE);
        g2d.drawRect(t1.getX() - 3, t1.getY() + 55, 60, 8);
        g2d.fillRect(t1.getX() - 3, t1.getY() + 55, t1.getHealth(), 8);
        g2d.drawRect(t2.getX() - 3, t2.getY() + 55, 60, 8);
        g2d.fillRect(t2.getX() - 3, t2.getY() + 55, t2.getHealth(), 8);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }

}


