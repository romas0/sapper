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

    /**
     * Конструктор
     */
    public Tile() {
        super("");
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setFocusPainted(false);
    }
}
