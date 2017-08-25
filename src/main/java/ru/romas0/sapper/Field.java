/**
 Класс игрового поля

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
 **/
package ru.romas0.sapper;

import javax.swing.*;
import java.awt.*;

/**
 * Класс игрового поля
 */
public class Field extends JPanel {

    /**
     * Массив клеточек
     */
    private Tile[] tiles;

    /**
     * Количечтво строчек игрового поля
     */
    private int countRows;

    /**
     * Количество колонок игрового поля
     */
    private int countCols;

    /**
     * Конструктор
     * @param countRows количество строк
     * @param countCols количество колонок
     */
    public Field(int countRows, int countCols) {
        super(new GridLayout(countRows, countCols, 0, 0));
        this.countRows = countRows;
        this.countCols = countCols;
        this.tiles = new Tile[countRows * countCols];
        for (int i=0; i<countRows * countCols; i++ ) {
            this.tiles[i] = new Tile();
            this.tiles[i].setIndex(i);
            this.add(tiles[i]);
        }
    }

    /**
     * Получить Объект клеточки игрового поля
     * @param row номер строчки
     * @param col номер колонки
     * @return
     */
    public Tile getTile(int row, int col) {
        return this.tiles[row*this.countCols + col];
    }

    /**
     * Обновить игровое поле
     */
    public void refresh() {
        for (Tile tile: this.tiles) {
            tile.refresh();
            if (GameGui.getInstance().getGameLogic().getStatus()==GameLogic.STATUS_LOSE) {
                tile.setEnabled(false);
            } else {
                tile.setEnabled(true);
            }
        }
    }
}
