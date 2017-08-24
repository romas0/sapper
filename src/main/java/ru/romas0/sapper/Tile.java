/**
 Класс клеточки игрового поля

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
 **/
package ru.romas0.sapper;

import javax.swing.*;
import java.awt.*;

/**
 * Класс клеточки игрового поля
 */
public class Tile extends JButton {

    public static final int STATUS_INIT          = 1;
    public static final int STATUS_NUMBER        = 2;
    public static final int STATUS_MARK_FLAG     = 3;
    public static final int STATUS_MARK_DOT      = 4;
    public static final int STATUS_MARK_BOMB     = 5;
    public static final int STATUS_EXPLODED_BOMB = 6;
    public static final int STATUS_NOTHING       = 7;

    private int status = Tile.STATUS_INIT;

    private int number=4;

    /**
     * Конструктор
     */
    public Tile() {
        super("");
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setFocusPainted(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.status == Tile.STATUS_NUMBER) {
            g.drawString(String.valueOf(this.number), 7, 16);
        } else if (this.status == Tile.STATUS_MARK_FLAG) {
            Color color = new Color(255, 0, 0);
            g.setColor(color);
            int xPoly[] = {6, 17, 6};
            int yPoly[] = {4, 7, 11};
            g.fillPolygon(new Polygon(xPoly, yPoly, xPoly.length));

            Color color2 = new Color(0, 0, 0);
            g.setColor(color2);
            g.fillRect(6, 4, 2, 14);
        } else if (this.status == Tile.STATUS_MARK_BOMB) {
            Color color = new Color(0, 0, 0);
            this.drawBomb(g, color);
        } else if (this.status == Tile.STATUS_EXPLODED_BOMB) {
            Color color = new Color(255, 0, 0);
            this.drawBomb(g, color);
        } else if (this.status == Tile.STATUS_MARK_DOT) {
            Color color = new Color(0, 0, 0);
            g.setColor(color);
            g.fillOval(9,9,3,3);
        } else if (this.status == Tile.STATUS_NOTHING) {
            g.clearRect(0,0,100,100);
        }
    }

    protected void drawBomb(Graphics g, Color color) {
        g.setColor(color);
        g.fillOval(6,6,10,10);
        g.drawLine(5, 11, 17, 11);
        g.drawLine(11, 5, 11, 17);
        g.drawLine(7, 7, 16, 16);
        g.drawLine(7, 16, 16, 7);
    }
}
