package org.game;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Snake extends JFrame {

    public Snake() throws IOException {
        
        initUI();
    }
    
    private void initUI() throws IOException {
        
        add(new Tablero());
               
        setResizable(false);
        pack();
        
        setTitle("Snake");
        BufferedImage image = ImageIO.read(new File("src/main/resources/apple.png"));
        setIconImage(image);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex;
            try {
                ex = new Snake();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ex.setVisible(true);
        });
    }
}
