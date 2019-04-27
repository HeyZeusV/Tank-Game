package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BreakableWall extends JComponent implements Drawable {

    private int x;
    private int y;
    private int width;
    private int height;
    private int health = 20;
    private boolean visible;
    private BufferedImage image;
    Rectangle bounds;

    public BreakableWall (int x, int y) {

        this.x = x;
        this.y = y;
        init();
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.visible = true;
        bounds = new Rectangle(x, y, width, height);
    }

    private void init() {

        try {
            image = ImageIO.read(getClass().getResource("resources/images/Wall2.png"));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void drawImage(Graphics g) {

        paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(image, x, y, this);
        if (this.health == 0) {

            this.visible = false;
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }

    public Rectangle getBounds() {

        return bounds;
    }

    public void setHealth(int health) {

        this.health += health;
    }

    public boolean isVisible() {

        return visible;
    }

    @Override
    public void setVisible(boolean visible) {

        this.visible = visible;
    }
}
