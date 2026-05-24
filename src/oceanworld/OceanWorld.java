/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package oceanworld;
import javax.swing.*;
import java.awt.event.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
/**
 *
 * @author annie
 */


// Ventana principal
public class OceanWorld extends GLJPanel implements KeyListener {

    // Renderizador principal
    private final OceanRenderer renderer;

    public OceanWorld() {

        renderer = new OceanRenderer();

        addGLEventListener(renderer);
        addKeyListener(this);

        setFocusable(true);
    }

    // Teclas presionadas
    @Override
    public void keyPressed(KeyEvent e) {

        int k = e.getKeyCode();

        if (k == KeyEvent.VK_W) renderer.teclaW = true;
        if (k == KeyEvent.VK_S) renderer.teclaS = true;
        if (k == KeyEvent.VK_A) renderer.teclaA = true;
        if (k == KeyEvent.VK_D) renderer.teclaD = true;

        if (k == KeyEvent.VK_SPACE) {
            renderer.teclaSpace = true;
        }

        if (k == KeyEvent.VK_LEFT) {
            renderer.camRotYObj -= 4f;
        }

        if (k == KeyEvent.VK_RIGHT) {
            renderer.camRotYObj += 4f;
        }
    }

    // Teclas soltadas
    @Override
    public void keyReleased(KeyEvent e) {

        int k = e.getKeyCode();

        if (k == KeyEvent.VK_W) renderer.teclaW = false;
        if (k == KeyEvent.VK_S) renderer.teclaS = false;
        if (k == KeyEvent.VK_A) renderer.teclaA = false;
        if (k == KeyEvent.VK_D) renderer.teclaD = false;

        if (k == KeyEvent.VK_SPACE) {
            renderer.teclaSpace = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Main
    public static void main(String[] args) {

        JFrame frame = new JFrame("Ocean World");
        OceanWorld panel = new OceanWorld();
        frame.add(panel);
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Animación
        FPSAnimator animator = new FPSAnimator(panel, 60);
        animator.start();
        System.out.println("Ocean World iniciado");
    }
}