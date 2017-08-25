/**
 Класс клеточки игрового поля

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
 **/
package ru.romas0.sapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Класс клеточки игрового поля
 */
public class Tile extends JButton {

    /**
     * Константы статусов клеточек игрового поля
     */
    public static final int STATUS_INIT          = 1; // Исходное состояние
    public static final int STATUS_NUMBER        = 2; // Число
    public static final int STATUS_MARK_FLAG     = 3; // Флаг
    public static final int STATUS_MARK_DOT      = 4; // Точка
    public static final int STATUS_MARK_BOMB     = 5; // Бомба черная (игра завершена)
    public static final int STATUS_EXPLODED_BOMB = 6; // Бомба красная (игра завершена)
    public static final int STATUS_NOTHING       = 7; // Нет бомбы, нет числа
    public static final int STATUS_BLOCK         = 8; // Нет бомбы, нет числа (игра завершена)

    /**
     * Порядковый номер клеточки
     */
    private int index;

    /**
     * Статус клеточки
     */
    private int status = Tile.STATUS_INIT;

    /**
     * Число бомб вокруг
     */
    private int number = 0;

    /**
     * Флаг "Курсор мыши над клеточкой"
     */
    private boolean mouseOver = false;

    /**
     * Конструктор
     */
    public Tile() {
        super("");
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setFocusPainted(false);
        this.addMouseListener(new ClickListener());
    }

    /**
     * Установить порядковый номер клеточки
     * @param idx номер
     */
    public void setIndex(int idx) {
        this.index = idx;
    }

    /**
     * Учтановить статус
     * @param status новый статус
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Учтановить число, означающее количество бомб вокруг
     * @param number число
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Обновить
     */
    public void refresh() {
        this.repaint();
    }

    /**
     * Рисование клеточки
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.status == Tile.STATUS_NUMBER) {
            Color bgColor = new Color(98, 255, 137);
            g.setColor(bgColor);
            g.fillRect(0,0,100,100);
            Color textColor = new Color(0, 0, 0);
            g.setColor(textColor);
            g.drawString(String.valueOf(this.number), 7, 16);
        } else if (this.status == Tile.STATUS_MARK_FLAG) {
            this.drawFlag(g);
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
            Color color = new Color(98, 255, 137);
            g.setColor(color);
            g.fillRect(0,0,100,100);
        } else if (this.status == Tile.STATUS_BLOCK) {
            Color color = new Color(187, 187, 187);
            g.setColor(color);
            g.fillRect(0,0,100,100);
        }
    }

    /**
     * Нарисовать бомбу
     * @param g
     * @param color цвет
     */
    private void drawBomb(Graphics g, Color color) {
        g.setColor(color);
        g.fillOval(6,6,10,10);
        g.drawLine(5, 11, 17, 11);
        g.drawLine(11, 5, 11, 17);
        g.drawLine(7, 7, 16, 16);
        g.drawLine(7, 16, 16, 7);
    }

    /**
     * Нарисовать флаг
     * @param g
     */
    private void drawFlag(Graphics g) {
        Color color = new Color(255, 0, 0);
        g.setColor(color);
        int xPoly[] = {6, 17, 6};
        int yPoly[] = {4, 7, 11};
        g.fillPolygon(new Polygon(xPoly, yPoly, xPoly.length));
        Color color2 = new Color(0, 0, 0);
        g.setColor(color2);
        g.fillRect(6, 4, 2, 14);
    }

    /**
     * Класс для работы с обработчиками событий мыши
     */
    private class ClickListener implements MouseListener {

        /**
         * Клик (нажал и отпустил)
         * @param e
         */
        public void mouseClicked(MouseEvent e) {}

        /**
         * Клик (только нажал, сразу срабатывает)
         * @param e
         */
        public void mousePressed(MouseEvent e) {
            if (mouseOver) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    GameGui.getInstance().rightClick(index);
                } else {
                    GameGui.getInstance().leftClick(index);
                }
                refresh();
            }
        }

        /**
         * Отпустил
         * @param e
         */
        public void mouseReleased(MouseEvent e) {}

        /**
         * Курсор появился над клеточкой
         * @param e
         */
        public void mouseEntered(MouseEvent e) {
            mouseOver = true;
        }

        /**
         * Курсор ушел из области клеточки
         * @param e
         */
        public void mouseExited(MouseEvent e) {
            mouseOver = false;
        }
    }
}
