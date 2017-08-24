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
     * Конструктор
     * @param countRows количество строк
     * @param countCols количество колонок
     */
    public Field(int countRows, int countCols) {
        super(new GridLayout(countRows, countCols, 0, 0));
        this.tiles = new Tile[countRows * countCols];
        for (int i=0; i<countRows * countCols; i++ ) {
            this.tiles[i] = new Tile();
            this.add(tiles[i]);
        }
    }
}
