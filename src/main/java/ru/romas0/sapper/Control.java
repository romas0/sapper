/**
 Класс панели управления

 @author <A HREF="mailto:serega170587@gmail.com">Sergey Romanenko</A>
 @version 0.1.0 $ $Date: 2017/08/24 4:55:00 $
 **/
package ru.romas0.sapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

/**
 * Класс панели управления
 */
public class Control extends JPanel {

    /**
     * Таблица лидеров
     */
    private LeaderBoard leaderBoard;

    /**
     * Область для информации о статусе игры
     */
    private JLabel statusMessage;

    /**
     * Область для информации о времени игры
     */
    private JLabel timeMessage;

    /**
     * Область для информации о количестве найденных бомб
     */
    private JLabel foundBombsMessage;

    /**
     * Конструктор
     */
    public Control() {
        super(new GridBagLayout());
        this.initGui();
    }

    /**
     * Добавить запись в таблицу лидеров
     */
    public void pushToLeaderBoard(double seconds, Date date) {
        this.leaderBoard.push(seconds, date);
    }

    /**
     * Установить сообщение в метку "статус игры". "You lose!" или "You win!"
     * @param message текст сообщения
     * @param color цвет текста
     */
    public void setStatusMessage(String message, Color color) {
        this.statusMessage.setText(message);
        this.statusMessage.setForeground(color);
    }

    /**
     * Установить сообщение в метку "время игры".
     * @param seconds количество секунд
     */
    public void setTimeMessage(double seconds) {
        this.timeMessage.setText(GameGui.gameTimeFormat(seconds));
    }

    /**
     * Установить сообщение в метку "количество бомб".
     * @param message количество бомб
     */
    public void setFoundBombsMessage(String message) {
        this.foundBombsMessage.setText(message);
    }

    /**
     * Инициализировать графические элементы
     */
    private void initGui() {
        GridBagConstraints gbc = new GridBagConstraints();
        this.setBorder(new EmptyBorder(0, 10, 10, 0));
        JLabel leaderBoardLabel = new JLabel("Leader Board", JLabel.CENTER );
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        gbc.weightx=100;
        this.add(leaderBoardLabel, gbc);

        this.leaderBoard = new LeaderBoard();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.01;
        this.add(this.leaderBoard, gbc);

        this.statusMessage = new JLabel("", JLabel.CENTER );
        this.statusMessage.setFont(new Font(this.statusMessage.getName(), Font.PLAIN, 25));
        this.statusMessage.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.weighty = 0.4;
        this.add(this.statusMessage, gbc);

        this.timeMessage = new JLabel("0.00", JLabel.CENTER );
        this.timeMessage.setFont(new Font(this.timeMessage.getName(), Font.PLAIN, 20));
        this.timeMessage.setForeground(Color.red);
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        this.add(this.timeMessage, gbc);

        this.foundBombsMessage = new JLabel("0 / 40", JLabel.CENTER );
        this.foundBombsMessage.setFont(new Font(this.foundBombsMessage.getName(), Font.PLAIN, 20));
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        this.add(this.foundBombsMessage, gbc);

        JButton newGameButton = new JButton("New game");
        newGameButton.setPreferredSize(new Dimension(150, 50));
        gbc.fill = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 0.1;
        this.add(newGameButton, gbc);
        newGameButton.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                GameGui.getInstance().setStatusHidden();
                GameGui.getInstance().start(4,5); // TODO
                GameGui.getInstance().refresh();
            }

            public void mousePressed(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {}
        });
    }
}
