/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oceanworld;

/**
 *
 * @author annie
 */
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class OceanRenderer implements GLEventListener {
  @Override
  public void init(GLAutoDrawable drawable){
    GL2 gl= drawable.getGL().getGL2();
    gl.glEnable(GL.GL_DEPTH_TEST);
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
  
  }
  @Override
  public void display(GLAutoDrawable drawable) {
    GL2 gl= drawable.getGL().getGL2();
    gl.glClearColor(0.0f,0.1f,0.3f,1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    
    
  }

  @Override
  public void reshape(GLAutoDrawable drawable, 
          int x, 
          int y, 
          int width,
          int height) {
  }
}
