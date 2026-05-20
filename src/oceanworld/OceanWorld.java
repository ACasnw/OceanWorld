/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package oceanworld;

/**
 *
 * @author annie
 */
import javax.swing.JFrame;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;

public class OceanWorld {

  public static void main(String[] args) {
 GLProfile profile= GLProfile.get(GLProfile.GL2);
 GLCapabilities capabilities = new GLCapabilities(profile);
 GLJPanel panel= new GLJPanel(capabilities);
 JFrame frame= new JFrame("Ocean World");
 frame.add(panel);
 frame.setSize(1400,900);
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setLocationRelativeTo(null);
 frame.setVisible(true);
  }
  
}
