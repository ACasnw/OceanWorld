/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oceanworld;

/**
 *
 * @author annie
 */
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class OceanRenderer implements GLEventListener {
 
  @Override
  public void init(GLAutoDrawable drawable){
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
  
  }
  
  @Override
  public void display(GLAutoDrawable drawable) {
    GL2 gl= drawable.getGL().getGL2();
    //Color del océano
    gl.glClearColor(0.0f,0.5f,0.8f,1.0f);
    //Limpiar pantalla
    gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    
    
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
  }
}
