/**
 Класс исключения логики игры

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/26 1:30:00 $
 **/
package ru.romas0.sapper;

/**
 * Класс исключения логики игры
 */
public class GameLogicException extends Exception {

    /**
     * Конструктор
     */
    public GameLogicException() {
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public GameLogicException(String message) {
        super(message);
    }
}
