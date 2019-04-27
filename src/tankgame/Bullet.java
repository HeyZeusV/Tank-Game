package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends JComponent implements Drawable {

    private int x;
    private int y;
    private final int r = 6;
    private int vx;
    private int vy;
    private short angle;
    private int width;
    private int height;
    private boolean visible;
    private BufferedImage image;
    private final int BOARD_WIDTH = 2569;
    private final int BOARD_HEIGHT = 896;
    private Rectangle bounds;

    public Bullet(int x, int y, short angle) {

        this.x = x;
        this.y = y;
        this.angle = angle;
        this.visible = true;
        init();
        this.width = image.getWidth();
        this.height = image.getHeight();
        bounds = new Rectangle(x, y, width, height);
    }
    private void init() {

        try {
            image = ImageIO.read(getClass().getResource("resources/images/Bullet.png"));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // used the same method you had in your tank example
    public void drawImage(Graphics g) {

        this.paintComponent(g);
        paintComponent(g);
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), image.getWidth() / 2, image.getHeight() / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(image, rotation, null);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }

    public BufferedImage getImage() {

        return image;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public short getAngle() {
        return angle;
    }
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {

        return bounds;
    }

    public int getHeight() {

        return height;
    }
    public int getWidth() {
        return width;
    }

    // same method in your example but tweaked to make sure bullets traveled faster than tanks
    public void move() {

        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x += 3 * vx;
        y += 3 * vy;
        // stops drawing bullet if offscreen
        if ( 0 > x || x > BOARD_WIDTH || y < 0 || y > BOARD_HEIGHT) {

            this.visible = false;
        }
        bounds.setLocation(x, y);
        repaint();
    }
}
