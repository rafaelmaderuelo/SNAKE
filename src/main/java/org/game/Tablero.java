package org.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tablero extends JPanel implements ActionListener {

    private final int Ancho_Tablero = 500;
    private final int Altura_Tablero = 500;
    private final int PIXEL_SIZE = 10;
    private final int ALL_DOTS = 2500;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int pixels;
    private int apple_x;
    private int apple_y;

    private boolean izquierda = false;
    private boolean derecha = true;
    private boolean arriba = false;
    private boolean abajo = false;
    private boolean inGame = true;

    private Timer timer;
    private Image cuerpoSnake;
    private Image apple;
    private Image cabezaSnake;

    public Tablero() {
        initTablero();
    }
    
    private void initTablero() {
        addKeyListener(new LectorTeclado());     // lectura de teclado
        setBackground(Color.black);         // fondo negro
        setFocusable(true);

        setPreferredSize(new Dimension(Ancho_Tablero, Altura_Tablero));
        cargaImg();
        initGame();
    }

    private void cargaImg() {
        ImageIcon cuerpo = new ImageIcon("src/main/resources/dot.png");
        cuerpoSnake = cuerpo.getImage();

        ImageIcon manzana = new ImageIcon("src/main/resources/apple.png");
        apple = manzana.getImage();

        ImageIcon cabeza = new ImageIcon("src/main/resources/head.png");
        cabezaSnake = cabeza.getImage();
    }

    private void initGame() {
        pixels = 3;

        for (int z = 0; z < pixels; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        pintar(g);
    }

    //pinta cada componente del juego en su posicion
    private void pintar(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < pixels; z++) {
                if (z == 0) {
                    g.drawImage(cabezaSnake, x[z], y[z], this);
                } else {
                    g.drawImage(cuerpoSnake, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }        
    }

    //fin del juego + añadir boton reset
    //TODO
    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (Ancho_Tablero - metr.stringWidth(msg)) / 2, Altura_Tablero / 2);
    }

    // comprueba si se come la manzana y le suma un pixel al cuerpo de la manzana
    // colocando otra manzana despues
    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            pixels++;
            locateApple();
        }
    }

    //logica de "movimiento" de la serpiente asignando la posicion anterior al punto de delante del cuerpo
    private void mover() {
        for (int z = pixels; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (izquierda) x[0] -= PIXEL_SIZE;
        if (derecha) x[0] += PIXEL_SIZE;
        if (arriba) y[0] -= PIXEL_SIZE;
        if (abajo) y[0] += PIXEL_SIZE;

    }

    //comprobar que los valores de la cabeza de la serpiente no salen fuera de los limites del tablero
    private void checkColision() {
        for (int z = pixels; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= Altura_Tablero) inGame = false;
        if (y[0] < 0) inGame = false;
        if (x[0] >= Ancho_Tablero) inGame = false;
        if (x[0] < 0) inGame = false;
        if (!inGame) timer.stop();
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * PIXEL_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * PIXEL_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkColision();
            mover();
        }

        repaint();
    }

    private class LectorTeclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // se impide dar la vuelta 180º
            if ((key == KeyEvent.VK_LEFT) && (!derecha)) {
                izquierda = true;
                arriba = false;
                abajo = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!izquierda)) {
                derecha = true;
                arriba = false;
                abajo = false;
            }

            if ((key == KeyEvent.VK_UP) && (!abajo)) {
                arriba = true;
                derecha = false;
                izquierda = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!arriba)) {
                abajo = true;
                derecha = false;
                izquierda = false;
            }
        }
    }
}
