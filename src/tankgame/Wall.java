package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Wall extends JComponent implements Drawable{

    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage image;
    Rectangle bounds;

    public Wall (int x, int y) {

        this.x = x;
        this.y = y;
        init();
        this.width = image.getWidth();
        this.height = image.getHeight();
        bounds = new Rectangle(x, y, width, height);
    }

    private void init() {

        try {
            image = ImageIO.read(getClass().getResource("resources/images/Wall1.png"));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void drawImage(Graphics g) {

        paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(image, x, y, this);

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }

    public Rectangle getBounds() {

        return bounds;
    }
}