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

// Un coral del arrecife: cerebro, rama o abanico
public class Coral {

    public float x, y, z;
    public float r, g, b;
    public float radio;
    public float rotY;
    public int tipo; // 0 = cerebro, 1 = rama, 2 = abanico

    public Coral(float x, float y, float z, float[] col, float radio, int tipo) {
        this.x = x; this.y = y; this.z = z;
        this.r = col[0]; this.g = col[1]; this.b = col[2];
        this.radio = radio;
        this.tipo  = tipo;
        this.rotY  = (float) Math.random() * 360f;
    }

    // Dibuja el coral según su tipo
    public void dibujar(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(rotY, 0, 1, 0);
        gl.glColor3f(r, g, b);

        switch (tipo) {
            case 0: dibujarCerebro(gl, glut); break;
            case 1: dibujarRama(gl, glut);    break;
            case 2: dibujarAbanico(gl, glut); break;
        }

        gl.glPopMatrix();
    }

    // Coral cerebro: esfera con surcos
    private void dibujarCerebro(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glScalef(1f, 0.72f, 1f);
        glut.glutSolidSphere(radio, 14, 12);
        gl.glPopMatrix();

        // Surcos más oscuros
        gl.glColor3f(r * 0.72f, g * 0.72f, b * 0.72f);
        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glRotatef(i * 36f, 0, 1, 0);
            gl.glTranslatef(0, radio * 0.45f, 0);
            gl.glScalef(0.13f, 0.18f, 0.75f);
            glut.glutSolidSphere(radio * 0.75f, 6, 5);
            gl.glPopMatrix();
        }
    }

    // Coral rama: tronco + 4 ramas con ramitas
    private void dibujarRama(GL2 gl, GLUT glut) {
        glut.glutSolidCylinder(radio * 0.18f, radio * 0.7f, 5, 4);

        for (int r2 = 0; r2 < 4; r2++) {
            gl.glPushMatrix();
            gl.glRotatef(r2 * 90f + 22f, 0, 1, 0);
            gl.glRotatef(-58f + r2 * 4f, 1, 0, 0);
            glut.glutSolidCylinder(radio * 0.13f, radio * 2.0f, 5, 4);
            gl.glTranslatef(0, 0, radio * 2.0f);

            // Punta de la rama
            glut.glutSolidSphere(radio * 0.20f, 8, 6);

            // Ramita lateral
            gl.glPushMatrix();
            gl.glRotatef(45f, 1, 0, 0);
            glut.glutSolidCylinder(radio * 0.08f, radio * 0.9f, 4, 3);
            gl.glTranslatef(0, 0, radio * 0.9f);
            glut.glutSolidSphere(radio * 0.13f, 6, 5);
            gl.glPopMatrix();

            gl.glPopMatrix();
        }
    }

    // Coral abanico: tallo + aro con radios internos
    private void dibujarAbanico(GL2 gl, GLUT glut) {
        glut.glutSolidCylinder(radio * 0.10f, radio * 0.55f, 5, 4);
        gl.glTranslatef(0, 0, radio * 0.55f);

        // Aro exterior (orientado verticalmente)
        gl.glPushMatrix();
        gl.glRotatef(90f, 1, 0, 0);
        for (int seg = 0; seg < 20; seg++) {
            gl.glPushMatrix();
            double a = seg * Math.PI * 2 / 20;
            float ax = (float) Math.cos(a) * radio;
            float az = (float) Math.sin(a) * radio;
            gl.glTranslatef(ax, az, 0);
            gl.glScalef(0.08f, 0.08f, 0.18f);
            glut.glutSolidSphere(radio * 0.12f, 5, 4);
            gl.glPopMatrix();
        }
        gl.glPopMatrix();

        // Radios internos del abanico
        for (int ri = 0; ri < 7; ri++) {
            gl.glPushMatrix();
            gl.glRotatef(-90f + ri * 30f, 0, 0, 1);
            gl.glScalef(0.04f, radio * 1.1f, 0.04f);
            glut.glutSolidSphere(0.8f, 4, 3);
            gl.glPopMatrix();
        }
    }
}