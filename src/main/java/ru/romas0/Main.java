/**
 Класс - точка входа приложения

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
**/
package ru.romas0;

import ru.romas0.sapper.GameGui;

/**
 * Класс - точка входа программы
 */
public class Main {

    /**
     * Метод - точка входа
     * @param args аргументы
     */
    public static void main(String[] args) {
         GameGui.getInstance();
    }
}
