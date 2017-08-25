/**
 Класс логики игры

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/26 1:30:00 $
 **/
package ru.romas0.sapper;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс логики игры
 */
public class GameLogic {

    /**
     * Константы статусов игры
     */
    public static final int STATUS_READY   = 1; // Игра готова к началу
    public static final int STATUS_ON      = 2; // Идет игра
    public static final int STATUS_WIN     = 3; // Выиграл
    public static final int STATUS_LOSE    = 4; // Проиграл

    /**
     * Статус игры
     */
    private int gameStatus = GameLogic.STATUS_READY;

    /**
     * Количество строк на поле игры
     */
    private int countRows;

    /**
     * Количество колонок на поле игры
     */
    private int countCols;

    /**
     * Количество бомб на поле игры
     */
    private int countBombs;

    /**
     * Количество флагов, которые поставил игрок на поле
     */
    private int countFlags = 0;

    /**
     * Клеточки игрового поля
     */
    private Cell tiles[][];

    /**
     * Unix timestamp начала игры
     */
    private Long start;

    /**
     * Unix timestamp завершения игры
     */
    private Long end = null;

    /**
     * Конструктор
     * @param cRows количество строк в игровом поле
     * @param cCols количество колонок в игровом поле
     * @param cBombs количество бомб
     * @param startY y координата начала игры
     * @param startX x координата начала игры
     */
    public GameLogic(int cRows, int cCols, int cBombs, int startY, int startX) throws GameLogicException {
        if (cRows<3 || cCols<3) {
            throw new GameLogicException("Попытка инициализации поля игры размером менее 3x3");
        }
        if (startX >= cCols || startY >= cRows) {
            throw new GameLogicException("Попытка начала игры вне поля.");
        }
        this.countRows = cRows;
        this.countCols = cCols;
        this.countBombs = cBombs;
        this.generateBombs(startX, startY);
        this.calculateNumbers();
        this.gameStatus = GameLogic.STATUS_READY;
    }

    /**
     * Начать игру
     */
    public void start() {
        this.start = System.currentTimeMillis();
        this.end = null;
    }

    /**
     * Получить статус игры
     * @return
     */
    public int getStatus() {
        return gameStatus;
    }

    /**
     * Получить клеточку игры
     * @param row номер строки
     * @param col номер колонки
     * @return
     */
    public Cell getCell(int row, int col) {
        return this.tiles[row][col];
    }

    /**
     * Генерирует (расставляет) случайным образом по полю бомбы
     * @param startY номер строки начала игры
     * @param startX номер колонки начала игры
     * @throws GameLogicException
     */
    private void generateBombs(int startY, int startX) throws GameLogicException {
        if (this.countBombs > this.countRows * this.countCols -1) {
            throw new GameLogicException("Недостаточно полей для размещения всех бомб");
        }
        this.tiles = new Cell[this.countRows][countCols];
        for (int i=0; i<this.countRows; i++) {
            for (int j=0; j<this.countCols; j++) {
                this.tiles[i][j] = new Cell(i, j);
            }
        }

        this.tiles[startY][startX].setNumber(0);

        /** Счетчик сгенерированных бомб */
        int bombs=this.countBombs;
        while (bombs != 0) {
            int randY = ThreadLocalRandom.current().nextInt(0, this.countRows);
            int randX = ThreadLocalRandom.current().nextInt(0, this.countCols);
            if (!this.tiles[randY][randX].hasBomb() && randX != startX && randY != startY) {
                this.tiles[randY][randX].putBomb();
                bombs--;
            }
        }
    }

    /**
     * Вычисляет для каждой клеточки сколько бомб ее окружают
     */
    private void calculateNumbers() {
        for (int i=0; i<this.countRows; i++) {
            for (int j=0; j<this.countCols; j++) {
                this.tiles[i][j].setNumber(this.getBombsAround(i, j));
            }
        }
    }

    /**
     * Получить количество бомб окружающих клеточку
     * @param row номер строки
     * @param col номер колонки
     * @return
     */
    private int getBombsAround(int row, int col) {
        int bombs = 0;
        for (int i=row-1; i<=row+1; i++) {
            for (int j=col-1; j<=col+1; j++) {
                if (i<0 || j<0 || i>=this.countRows || j>=this.countCols || (i==row && j==col)) {
                    continue;
                }
                if (this.tiles[i][j].hasBomb) {
                    bombs++;
                }
            }
        }
        return bombs;
    }

    /**
     * Получить количество флагов, которые поставил игрок
     * @return
     */
    public int getCountFlags() {
        return this.countFlags;
    }

    /**
     * Обновляет статус игры
     */
    private void updateGameStatus() {
        if (this.gameStatus==GameLogic.STATUS_LOSE || this.gameStatus==GameLogic.STATUS_WIN) {
            for (int i=0; i<countRows; i++) {
                for (int j=0; j<countCols; j++) {
                    if (this.tiles[i][j].getStatus()==Cell.STATUS_INIT) {
                        if (this.tiles[i][j].hasBomb()) {
                            this.tiles[i][j].status = Cell.STATUS_MARK_BOMB;
                        } else if (this.tiles[i][j].getNumber()>0) {
                            this.tiles[i][j].status = Cell.STATUS_NUMBER;
                        } else {
                            this.tiles[i][j].status = Cell.STATUS_BLOCK;
                        }
                    }
                }
            }
            this.end = System.currentTimeMillis();
            return;
        } else {
            int hiddenTiles = countRows * countCols;
            for (int i=0; i<countRows; i++) {
                for (int j=0; j<countCols; j++) {
                    if (this.tiles[i][j].getStatus() == Cell.STATUS_NUMBER || this.tiles[i][j].getStatus() == Cell.STATUS_NOTHING) {
                        hiddenTiles--;
                    }
                }
            }
            if (hiddenTiles == countBombs) {
                this.gameStatus=GameLogic.STATUS_WIN;
                this.updateGameStatus();
            }
        }
    }

    /**
     * Получить продолжительность игры
     * @return
     */
    public double getGameDuration() {
        if (this.end == null) {
            Long now = System.currentTimeMillis();
            return now - this.start;
        }
        return this.end - this.start;
    }

    /**
     * Класс клеточки игрового поля
     */
    public class Cell {

        /**
         * Константы статусов клеточки игрового поля
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
         * Статус клеточки
         */
        private int status = Cell.STATUS_INIT;

        /**
         * Флаг "Есть бомба"
         */
        private boolean hasBomb = false;

        /**
         * Число бомб вокруг
         */
        private int number;

        /**
         * Номер строчки
         */
        private int iRow;

        /**
         * Номер колонки
         */
        private int iCol;

        /**
         * Конструктор
         * @param iRow номер строчки
         * @param iCol номер колонки
         */
        public Cell(int iRow, int iCol) {
            this.iRow = iRow;
            this.iCol = iCol;
        }

        /**
         * Получить статус клеточки
         * @return
         */
        public int getStatus() {
            return this.status;
        }

        /**
         * Получить количество бомб, окружающих эту клетку
         * @return
         */
        public int getNumber() {
            return this.number;
        }

        /**
         * Клик левой кнопкой мыши
         * @return
         */
        public boolean leftClick() {
            if (this.status == Cell.STATUS_INIT || this.status == Cell.STATUS_MARK_DOT) {
                if (this.hasBomb()) {
                    this.status = Cell.STATUS_EXPLODED_BOMB;
                    gameStatus=GameLogic.STATUS_LOSE;
                } else if (this.getNumber() == 0) {
                    this.status = Cell.STATUS_NOTHING;
                    this.clickAround(this.iRow, this.iCol);
                } else {
                    this.status = Cell.STATUS_NUMBER;
                }
                updateGameStatus();
                return true;
            }
            return false;
        }

        /**
         * Клик правой кнопкой мыши
         * @return
         */
        public boolean rightClick() {
            if (this.status == Cell.STATUS_INIT) {
                this.status = Cell.STATUS_MARK_FLAG;
                countFlags++;
                return true;
            } else if (this.status == Cell.STATUS_MARK_FLAG) {
                this.status = Cell.STATUS_MARK_DOT;
                countFlags--;
                return true;
            } else if (this.status == Cell.STATUS_MARK_DOT) {
                this.status = Cell.STATUS_INIT;
                return true;
            }
            return false;
        }

        /**
         * Клик левой кнопкой мыши по всем пустым окружающим клеткам (рекурсивно)
         * @param clickRow номер строки
         * @param clickCol номер колонки
         */
        private void clickAround(int clickRow, int clickCol) {
            for (int i=clickRow-1; i<=clickRow+1; i++) {
                for (int j=clickCol-1; j<=clickCol+1; j++) {
                    if (i<0 || j<0 || i>=countRows || j>=countCols) {
                        continue;
                    }
                    if (tiles[i][j].status == Cell.STATUS_INIT && !tiles[i][j].hasBomb()) {
                        if (tiles[i][j].getNumber()==0) {
                            tiles[i][j].status=Cell.STATUS_NOTHING;
                            clickAround(i, j);
                        } else {
                            tiles[i][j].status=Cell.STATUS_NUMBER;
                        }
                    }
                }
            }
        }

        /**
         * Есть ли бомба?
         * @return
         */
        public boolean hasBomb() {
            return this.hasBomb;
        }

        /**
         * Поместить бомбу
         */
        public void putBomb() {
            this.hasBomb = true;
        }

        /**
         * Установить число, означающее количество бомб вокруг
         * @param number число
         */
        public void setNumber(int number) {
            this.number = number;
        }
    }
}
