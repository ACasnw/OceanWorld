/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oceanworld;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
/**
 *
 * @author annie
 */

// Partícula de plancton que flota suavemente
public class Particula {

    public float x, y, z;
    public int id;

    // Límite superior del volumen de agua
    private final float suelo;

    public Particula(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
        this.id    = (int) (Math.random() * 10000);
        this.suelo = -2.5f;
    }

    // Mueve la partícula hacia arriba con deriva lateral
    public void actualizar(float time) {
        y += 0.0025f + 0.0008f * (float) Math.sin(time + id);
        x += (float) Math.sin(time * 0.42f + id) * 0.0022f;
        z += (float) Math.cos(time * 0.36f + id) * 0.0022f;

        // Si sale del rango, reaparece abajo en posición aleatoria
        if (y > suelo + 10f) {
            y = suelo + 0.05f;
            x = (float) Math.random() * 28 - 14;
            z = (float) Math.random() * 28 - 14;
        }
    }

    // Dibuja la partícula como esfera diminuta brillante
    public void dibujar(GL2 gl, GLUT glut, float time) {
        float alpha = 0.14f + 0.10f * (float) Math.sin(time * 1.6f + id * 0.55f);
        gl.glColor4f(0.42f, 0.72f, 0.88f, alpha);
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        glut.glutSolidSphere(0.016f, 4, 3);
        gl.glPopMatrix();
    }
}