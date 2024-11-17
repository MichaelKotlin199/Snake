package org.example.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private boolean gameOver = false;
    private Timer timer;
    private List<Point> snake;
    private Point food;
    private int direction = KeyEvent.VK_RIGHT;
    private int score = 0;

    public Game() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) direction = KeyEvent.VK_UP;
                if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) direction = KeyEvent.VK_DOWN;
                if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) direction = KeyEvent.VK_LEFT;
                if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) direction = KeyEvent.VK_RIGHT;
            }
        });

        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        generateFood();

        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
            checkCollision();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case KeyEvent.VK_UP:
                head.y--;
                break;
            case KeyEvent.VK_DOWN:
                head.y++;
                break;
            case KeyEvent.VK_LEFT:
                head.x--;
                break;
            case KeyEvent.VK_RIGHT:
                head.x++;
                break;
        }
        head.x = (head.x + WIDTH) % WIDTH;
        head.y = (head.y + HEIGHT) % HEIGHT;

        snake.add(0, head);

        if (head.equals(food)) {
            score++;
            generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                break;
            }
        }
    }

    private void generateFood() {
        Random random = new Random();
        food = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        for (Point segment : snake) {
            if (segment.equals(food)) {
                generateFood();
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Game Over! Press 'R' to restart.", 20, HEIGHT * TILE_SIZE / 2);
        } else {
            g.setColor(Color.GREEN);
            for (Point segment : snake) {
                g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Score: " + score, 10, 20);
        }
    }

    public void restart() {
        gameOver = false;
        snake.clear();
        snake.add(new Point(5, 5));
        direction = KeyEvent.VK_RIGHT;
        score = 0;
        generateFood();
        timer.start();
        repaint();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
