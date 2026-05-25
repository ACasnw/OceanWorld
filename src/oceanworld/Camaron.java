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

// Camarón del jugador
public class Camaron {

    // Posición
    public float x, y, z;

    // Velocidad
    private float velX, velY, velZ;

    // Rotación
    public float rotY, rotYObj;

    // Física
    private static final float GRAVEDAD = 0.015f;
    private static final float VEL_MOV = 0.15f;
    private static final float VEL_SALTO = 0.40f;
    private static final float FRICCION = 0.86f;

    // Suelo
    private final float suelo;

    private boolean enSuelo = false;

    // Colores
    private static final float[] C_CAMARON = {0.880f, 0.340f, 0.240f};
    private static final float[] C_CAMARON_C = {0.960f, 0.580f, 0.420f};

    public Camaron(float suelo) {

        this.suelo = suelo;

        x = 0;
        y = 0;
        z = 0;
    }

    // Movimiento
    public void actualizar(boolean w, boolean a, boolean s, boolean d, boolean space) {

        // Gravedad
        if (y > suelo) {

            velY -= GRAVEDAD;

            enSuelo = false;

        } else {

            y = suelo;

            velY = 0;

            enSuelo = true;
        }

        // Movimiento
        float vx = 0;
        float vz = 0;

        // Adelante
        if (w) {

            vx += (float) Math.sin(Math.toRadians(rotY)) * VEL_MOV;
            vz += (float) Math.cos(Math.toRadians(rotY)) * VEL_MOV;
        }

        // Atrás
        if (s) {

            vx -= (float) Math.sin(Math.toRadians(rotY)) * VEL_MOV;
            vz -= (float) Math.cos(Math.toRadians(rotY)) * VEL_MOV;
        }

        // Izquierda
        if (a) {

            vx += (float) Math.sin(Math.toRadians(rotY + 90f)) * VEL_MOV;
            vz += (float) Math.cos(Math.toRadians(rotY + 90f)) * VEL_MOV;
        }

        // Derecha
        if (d) {

            vx += (float) Math.sin(Math.toRadians(rotY - 90f)) * VEL_MOV;
            vz += (float) Math.cos(Math.toRadians(rotY - 90f)) * VEL_MOV;
        }

        // Salto
        if (space && enSuelo) {

            velY = VEL_SALTO;

            enSuelo = false;
        }

        // Giro suave
        rotY += (rotYObj - rotY) * 0.08f;

        // Movimiento suave
        velX = velX * FRICCION + vx * (1f - FRICCION);
        velZ = velZ * FRICCION + vz * (1f - FRICCION);

        x += velX;
        y += velY;
        z += velZ;

        // Movimiento submarino
        y += Math.sin(System.currentTimeMillis() * 0.002) * 0.002f;

        // Límites del mundo
        if (x > 11f) {
            x = 11f;
            velX *= -0.4f;
        }

        if (x < -11f) {
            x = -11f;
            velX *= -0.4f;
        }

        if (z > 11f) {
            z = 11f;
            velZ *= -0.4f;
        }

        if (z < -11f) {
            z = -11f;
            velZ *= -0.4f;
        }
    }

    // Dibuja el camarón
    public void dibujar(GL2 gl, GLUT glut, float time) {

        gl.glPushMatrix();

        gl.glTranslatef(x, y, z);

        gl.glRotatef(rotY, 0, 1, 0);

        // Movimiento del cuerpo
        gl.glRotatef((float) Math.sin(time * 3f) * 3f, 0, 0, 1);

        dibujarCuerpo(gl, glut, time);
        dibujarCabeza(gl, glut, time);
        dibujarAbdomen(gl, glut, time);
        gl.glRotatef(-4f, 0, 0, 1);
        dibujarPatas(gl, glut, time);
        dibujarCola(gl, glut);
        dibujarAntenas(gl, glut, time);

        gl.glPopMatrix();
    }

    // Cuerpo principal
    private void dibujarCuerpo(GL2 gl, GLUT glut, float time) {

        gl.glColor3f(C_CAMARON[0], C_CAMARON[1], C_CAMARON[2]);

        gl.glPushMatrix();

        gl.glScalef(1.0f, 0.68f, 0.68f);

        glut.glutSolidSphere(0.42f, 14, 12);

        gl.glPopMatrix();

        // Parte inferior
        gl.glColor3f(C_CAMARON_C[0], C_CAMARON_C[1], C_CAMARON_C[2]);

        gl.glPushMatrix();

        gl.glTranslatef(0, -0.18f, 0);

        gl.glScalef(0.85f, 0.30f, 0.58f);

        glut.glutSolidSphere(0.42f, 10, 8);

        gl.glPopMatrix();
    }

    // Cabeza
    private void dibujarCabeza(GL2 gl, GLUT glut, float time) {

        gl.glColor3f(C_CAMARON[0], C_CAMARON[1], C_CAMARON[2]);

        gl.glPushMatrix();

        gl.glTranslatef(0.36f, 0.14f, 0);

        gl.glScalef(0.72f, 0.65f, 0.65f);

        glut.glutSolidSphere(0.36f, 14, 12);

        gl.glPopMatrix();
    }

    // Abdomen
    private void dibujarAbdomen(GL2 gl, GLUT glut, float time) {

        gl.glColor3f(C_CAMARON[0], C_CAMARON[1], C_CAMARON[2]);

        float[] radios = {0.26f, 0.24f, 0.22f, 0.20f, 0.18f, 0.15f};

        for (int i = 0; i < 6; i++) {

            gl.glPushMatrix();

            float dx = -0.20f - i * 0.12f;

            gl.glTranslatef(dx, -0.008f * i, 0);

            float onda = 8f * (float) Math.sin(time * 3.5f + i * 0.7f);

            gl.glRotatef(onda, 0, 0, 1);

           gl.glScalef(1.0f, 0.9f, 0.9f);

            glut.glutSolidSphere(radios[i], 10, 8);

            gl.glPopMatrix();
        }
    }

    // Patas
    private void dibujarPatas(GL2 gl, GLUT glut, float time) {

        gl.glColor3f(C_CAMARON_C[0], C_CAMARON_C[1], C_CAMARON_C[2]);

        for (int i = 0; i < 5; i++) {

            for (int lado = 0; lado < 2; lado++) {

                gl.glPushMatrix();

                float zPos = (lado == 0) ? 0.25f : -0.25f;

                gl.glTranslatef(-0.10f - i * 0.15f, -0.10f, zPos);

                float anim = 8f * (float) Math.sin(time * 3.8f + i);

                gl.glRotatef(anim, 1, 0, 0);

                glut.glutSolidCylinder(0.012f, 0.30f, 4, 3);

                gl.glPopMatrix();
            }
        }
    }

    // Cola
    private void dibujarCola(GL2 gl, GLUT glut) {

        gl.glColor3f(C_CAMARON[0], C_CAMARON[1], C_CAMARON[2]);

        gl.glPushMatrix();

        gl.glTranslatef(-0.95f, -0.12f, 0);

        for (int i = 0; i < 5; i++) {

            gl.glPushMatrix();

            gl.glRotatef(-48f + i * 24f, 0, 0, 1);

            gl.glScalef(0.10f, 0.55f, 0.06f);

            glut.glutSolidSphere(0.25f, 6, 4);

            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }

    // Antenas
    private void dibujarAntenas(GL2 gl, GLUT glut, float time) {

        gl.glColor3f(1.0f, 0.78f, 0.65f);

        for (int i = 0; i < 2; i++) {

            gl.glPushMatrix();

            float zPos = (i == 0) ? 0.14f : -0.14f;

            gl.glTranslatef(0.54f, 0.28f, zPos);

            float anim = (float) Math.sin(time * 1.3f + i) * 10f;

            gl.glRotatef(anim, 0, 1, 0);

            glut.glutSolidCylinder(0.005f, 0.45f, 5, 4);

            gl.glPopMatrix();
        }
    }
}