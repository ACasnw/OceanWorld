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


// Una medusa translúcida que flota dentro del agua
public class Medusa {

    public float x, y, z;
    public float radio;
    public float[] col;
    public int id;

    public Medusa(float x, float y, float z, float radio, float[] col) {
        this.x = x; this.y = y; this.z = z;
        this.radio = radio;
        this.col   = col;
        this.id    = (int) (Math.random() * 10000);
    }

    // Flotación pulsada: sube al contraer, baja al relajar
    public void actualizar(float time) {
        float fase = (float) Math.abs(Math.sin(time * 1.55f + id * 0.9f));
        y += (fase > 0.55f) ? 0.009f : -0.004f;

        // Deriva lateral muy suave
        x += (float) Math.sin(time * 0.35f + id * 0.65f) * 0.006f;
        z += (float) Math.cos(time * 0.30f + id * 0.52f) * 0.006f;

        // Límites dentro del agua (no salen a la superficie)
        if (y > 3.8f) y = 3.8f;
        if (y < 0.4f) y = 0.4f;
        if (Math.abs(x) > 10f) x *= -0.5f;
        if (Math.abs(z) > 10f) z *= -0.5f;
    }

    // Dibuja campana, glow bioluminiscente y tentáculos
    public void dibujar(GL2 gl, GLUT glut, float time) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        // Pulsación de la campana
        float pulso    = 0.65f + 0.35f * (float) Math.abs(Math.sin(time * 1.55f + id * 0.9f));
        float alpCamp  = 0.32f + 0.14f * (float) Math.sin(time * 1.55f + id * 0.9f);

        // Campana translúcida
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(col[0], col[1], col[2], alpCamp + 0.08f);
        gl.glPushMatrix();
        gl.glScalef(1.0f, pulso * 0.52f, 1.0f);
        glut.glutSolidSphere(radio, 18, 16);
        gl.glPopMatrix();

        // Borde inferior de la campana
        gl.glColor4f(col[0] + 0.08f, col[1] + 0.08f, col[2] + 0.10f, alpCamp * 0.55f);
        gl.glPushMatrix();
        gl.glTranslatef(0, -radio * pulso * 0.46f, 0);
        gl.glScalef(1.10f, 0.16f, 1.10f);
        glut.glutSolidSphere(radio, 14, 8);
        gl.glPopMatrix();

        // Núcleo bioluminiscente (glow aditivo)
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
        float glowIntens = 0.12f + 0.08f * (float) Math.sin(time * 2.0f + id);
        gl.glColor4f(col[0], col[1] + 0.12f, col[2] + 0.08f, glowIntens);
        gl.glPushMatrix();
        gl.glScalef(0.50f, pulso * 0.22f, 0.50f);
        glut.glutSolidSphere(radio, 10, 8);
        gl.glPopMatrix();

        // Tentáculos: 10, fluidos con deriva
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        for (int t = 0; t < 10; t++) {
            gl.glPushMatrix();
            double angT = t * Math.PI * 2 / 10;
            float tx = (float) Math.cos(angT) * radio * 0.68f;
            float tz = (float) Math.sin(angT) * radio * 0.68f;
            gl.glTranslatef(tx, -radio * pulso * 0.48f, tz);

            // Cada tentáculo tiene 10 segmentos decrecientes
            for (int seg = 0; seg < 10; seg++) {
                float pct  = (float) seg / 10f;
                float derX = 0.045f * (float) Math.sin(time * 2.1f + t * 0.95f + seg * 0.55f);
                float derY = -0.13f;
                float derZ = 0.035f * (float) Math.cos(time * 1.85f + t * 0.70f + seg * 0.48f);
                gl.glTranslatef(derX, derY, derZ);
                float alpha = 0.40f - pct * 0.25f;
                float gr    = Math.max(0.008f, 0.038f * (1f - pct * 0.80f));
                gl.glColor4f(col[0], col[1], col[2] + 0.05f, alpha);
                glut.glutSolidSphere(gr, 4, 4);
            }
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }
}