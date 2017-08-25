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
import java.util.TimerTask;

/**
 * Класс игры "Сапер" с графическим интерфейсом
 */
public class GameGui extends JFrame {

    /**
     * Константы статусов игры
     */
    public static final int  STATUS_ON  = 1; // Игра идет
    public static final int  STATUS_OFF = 2; // Нет игры

    /**
     * Статус игры
     */
    private int status = GameGui.STATUS_OFF;

    /**
     * Объект игры
     */
    public static GameGui instance;

    /**
     * Объект лигики игры
     */
    private GameLogic gameLogic;

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
     * Таймер
     */
    private java.util.Timer timer = new java.util.Timer(false);

    /**
     * Конструктор
     */
    private GameGui() {
        super("Sapper");
        this.initGui();

        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getStatus() == GameGui.STATUS_ON) {
                    double gameDuration = gameLogic.getGameDuration()/1000;
                    setTime(gameDuration);
                }
            }
        }, 0, 10);
    }

    /**
     * Получить объект игры
     * @return
     */
    public static GameGui getInstance() {
        if (GameGui.instance == null) {
            GameGui.instance = new GameGui();
        }
        return GameGui.instance;
    }

    /**
     * Получить статус игры
     * @return
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * Начать игру
     * @param y номер строчки
     * @param x номер колонки
     */
    public void start(int y, int x) {
        try {
            this.gameLogic = new GameLogic(GameGui.FIELD_ROWS, GameGui.FIELD_COLS, GameGui.COUNT_BOMBS, y, x);
            this.gameLogic.start();
            this.status = GameGui.STATUS_ON;
        } catch (GameLogicException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Обновить игру (синхронизировать с объектом логики игры)
     */
    public void refresh() {
        for (int i=0; i<GameGui.FIELD_ROWS; i++) {
            for (int j=0; j<GameGui.FIELD_COLS; j++) {
                GameLogic.Cell cell = this.gameLogic.getCell(i, j);
                Tile tile = this.field.getTile(i, j);

                if (cell.getStatus() == GameLogic.Cell.STATUS_INIT) {
                    tile.setStatus(Tile.STATUS_INIT);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_NUMBER) {
                    tile.setNumber(cell.getNumber());
                    tile.setStatus(Tile.STATUS_NUMBER);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_EXPLODED_BOMB) {
                    tile.setStatus(Tile.STATUS_EXPLODED_BOMB);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_MARK_BOMB) {
                    tile.setStatus(Tile.STATUS_MARK_BOMB);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_MARK_DOT) {
                    tile.setStatus(Tile.STATUS_MARK_DOT);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_MARK_FLAG) {
                    tile.setStatus(Tile.STATUS_MARK_FLAG);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_NOTHING) {
                    tile.setStatus(Tile.STATUS_NOTHING);
                } else if (cell.getStatus() == GameLogic.Cell.STATUS_BLOCK) {
                    tile.setStatus(Tile.STATUS_BLOCK);
                }
            }
        }
        this.field.refresh();
        this.setFoundBombs(this.gameLogic.getCountFlags());
        if (this.gameLogic.getStatus() == GameLogic.STATUS_WIN) {
            this.setStatusWin();
            this.setFoundBombs(GameGui.COUNT_BOMBS);
        } else if (this.gameLogic.getStatus() == GameLogic.STATUS_LOSE) {
            this.setStatusLose();
        }
        double gameDuration = this.gameLogic.getGameDuration()/1000;
        this.setTime(gameDuration);
        if (this.getStatus() == GameGui.STATUS_ON && this.gameLogic.getStatus()==GameLogic.STATUS_WIN) {
            this.pushToLeaderBoard(this.gameLogic.getGameDuration()/1000, new Date());
            this.status = GameGui.STATUS_OFF;
        }
    }

    /**
     * Клик левой кнопкой мыши
     * @param index порядковый номер клеточки
     */
    public void leftClick(int index) {
        int clickRow = (int)Math.floor(index/GameGui.FIELD_COLS);
        int clickCol = index - clickRow*GameGui.FIELD_COLS;

        if (this.gameLogic==null) {
            this.start(clickRow, clickCol);
        }

        if (this.gameLogic.getStatus() != GameLogic.STATUS_LOSE &&
                this.gameLogic.getStatus() != GameLogic.STATUS_WIN &&
                this.gameLogic.getCell(clickRow, clickCol).leftClick()) {
            this.refresh();
        }
    }

    /**
     * Клик правой кнопкой мыши
     * @param index порядковый номер клеточки
     */
    public void rightClick(int index) {
        if (this.gameLogic==null) {
            return;
        }

        int clickRow = (int)Math.floor(index/GameGui.FIELD_COLS);
        int clickCol = index - clickRow*GameGui.FIELD_COLS;

        if (this.gameLogic.getStatus() != GameLogic.STATUS_LOSE &&
                this.gameLogic.getStatus() != GameLogic.STATUS_WIN &&
                this.gameLogic.getCell(clickRow, clickCol).rightClick()) {
            this.refresh();
        }
    }

    /**
     * Получить объект логики игры
     * @return
     */
    public GameLogic getGameLogic() {
        return this.gameLogic;
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
    public void setStatusLose() {
        this.control.setStatusMessage("You lose!", new Color(187, 0,0));
    }

    /**
     * Установить статус игры "Выиграно"
     */
    public void setStatusWin() {
        this.control.setStatusMessage("You win!", new Color(0, 153,0));
    }

    /**
     * Скрыть статус игры
     */
    public void setStatusHidden() {
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
        NumberFormat formatter;
        if (minutes != null) {
            formatter = new DecimalFormat("#00.00");
        } else {
            formatter = new DecimalFormat("#0.00");
        }
        return (minutes != null ? minutes+" : " : "") + formatter.format(seconds);
    }
}
