/**
 Класс таблицы лидеров

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
**/
package ru.romas0.sapper;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Класс таблицы лидеров
 */
public class LeaderBoard extends JTable {

    /**
     * Для форматирования дат
     */
    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("d.MM.yyyy H:mm:ss");

    /**
     * Список лидеров
     */
    private List<Record> leaders;

    /**
     * Для сортировки списка лидеров
     */
    private Comparator recordComparator = new RecordComparator();

    /**
     * Конструктор
     */
    public LeaderBoard() {
        super(10, 3);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.leaders = new ArrayList();
        this.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.getColumnModel().getColumn(1).setPreferredWidth(55);
        this.getColumnModel().getColumn(2).setPreferredWidth(125);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        this.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        this.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
    }

    /**
     * Добавить запись о лидере
     * @param seconds количество секунд
     * @param date дата
     */
    public void push(double seconds, Date date) {
        this.leaders.add(new Record(seconds, this.dateFormat.format(date)));
        Collections.sort(this.leaders, this.recordComparator);
        this.refresh();
    }

    /**
     * Обновить список лидеров
     */
    private void refresh() {
        DefaultTableModel model = (DefaultTableModel)this.getModel();
        int counter=0;
        for (Record r: leaders) {
            if (counter>=10) break;
            model.setValueAt(counter+1, counter, 0);
            model.setValueAt(GameGui.gameTimeFormat(r.seconds), counter, 1);
            model.setValueAt(r.getDate(), counter, 2);
            counter++;
        }
    }

    /**
     * Для того, чтобы нельзя было редактировать таблицу
     * @param row строка
     * @param column колонка
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Класс записей таблицы лидеров
     */
    private class Record {

        /**
         * Количество секунд, затраченных на игру
         */
        private double seconds;

        /**
         * Дата
         */
        private String date;

        /**
         * Конструктор
         * @param seconds количество секунд, затраченных на игру
         * @param date дата
         */
        Record(double seconds, String date) {
            this.seconds = seconds;
            this.date = date;
        }

        /**
         * Получить количество секунд, затраченных на игру
         * @return
         */
        public double getSeconds() {
            return seconds;
        }

        /**
         * Получить дату игры
         * @return
         */
        public String getDate() {
            return date;
        }
    }

    /**
     * Класс для сортировки записей в таблице лидеров
     */
    private class RecordComparator implements Comparator<Record> {
        public int compare(Record r1, Record r2) {
            if (r1.getSeconds() > r2.getSeconds()) {
                return 1;
            } else if (r1.getSeconds() < r2.getSeconds()) {
                return -1;
            }
            return 0;
        }
    }
}
