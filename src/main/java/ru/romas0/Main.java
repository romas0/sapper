/**
 Класс - точка входа приложения

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
**/
package ru.romas0;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("ru.romas0.sapper.GameGui");
        } catch (ClassNotFoundException e) {
            System.exit(1);
        }
    }
}
