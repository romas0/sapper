/**
 Класс игры "Сапер" с графическим интерфейсом

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
 **/
package ru.romas0.sapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.*;
import java.util.Date;

/**
 * Класс игры "Сапер" с графическим интерфейсом
 */
public class GameGui extends JFrame {

    /**
     * Объект игры
     */
    public static GameGui instance = new GameGui();

    /**
     * Количество бомб
     */
    private static final int COUNT_BOMBS = 40;

    /**
     * Количество строк
     */
    private static final int FIELD_ROWS = 16;

    /**
     * Количество столбцов
     */
    private static final int FIELD_COLS = 16;

    /**
     * Контейнер
     */
    JPanel contentPanel;

    /**
     * Игровое поле
     */
    Field field;

    /**
     * Панель (справа)
     */
    Control control;

    /**
     * Конструктор
     */
    private GameGui() {
        super("Sapper");
        this.initGui();
    }

    /**
     * Добавить запись в таблицу лидеров
     * @param seconds время, затраченное на игру
     * @param date дата
     */
    private void pushToLeaderBoard(double seconds, Date date) {
        control.pushToLeaderBoard(seconds, date);
    }

    /**
     * Установить статус игры "Проиграно"
     */
    private void setStatusLose() {
        this.control.setStatusMessage("You lose!", Color.red);
    }

    /**
     * Установить статус игры "Выиграно"
     */
    private void setStatusWin() {
        this.control.setStatusMessage("You win!", Color.green);
    }

    /**
     * Скрыть статус игры
     */
    private void setStatusHidden() {
        this.control.setStatusMessage("", Color.white);
    }

    /**
     * Установить время игры
     * @param seconds количество секунд
     */
    private void setTime(double seconds) {
        control.setTimeMessage(seconds);
    }

    /**
     * Установить информацию о найденных бомбах
     * @param count количество найденных бомб
     */
    private void setFoundBombs(int count) {
        control.setFoundBombsMessage(count + " / " + COUNT_BOMBS);
    }

    /**
     * Инициализировать графический интерфейс
     */
    private void initGui() {
        this.setLayout(new GridLayout(1, 1));
        this.setSize(650,400);
        this.setResizable(false);

        this.contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        this.contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.field = new Field(GameGui.FIELD_ROWS, GameGui.FIELD_COLS);

        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = gbc.weighty = 85;
        this.contentPanel.add(this.field, gbc);

        this.control = new Control();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 15;
        gbc.insets = new Insets(2, 2, 2, 2);
        this.contentPanel.add(this.control, gbc);

        this.add(this.contentPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Форматировать время игры
     * @param seconds количество секунд
     * @return отформатированное время
     */
    public static String gameTimeFormat(double seconds) {
        String minutes = null;
        if (seconds > 60) {
            minutes = String.valueOf((int)Math.floor(seconds / 60));
            seconds = seconds % 60;
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        return (minutes != null ? minutes+" : " : "") + formatter.format(seconds);
    }
}
