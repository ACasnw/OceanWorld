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
// Un pez del ecosistema: datos, movimiento y dibujo
public class Pez {

    // Posición y velocidad
    public float x, y, z;
    public float vx, vz;

    // Color y tamaño
    public float r, g, b;
    public float radio;

    // Identificador único para animaciones individuales
    public int tipo, id;

    public Pez(float x, float y, float z, float[] col, float radio, int tipo) {
        this.x = x; this.y = y; this.z = z;
        this.r = col[0]; this.g = col[1]; this.b = col[2];
        this.radio = radio;
        this.tipo  = tipo;
        // Velocidad inicial aleatoria pequeña
        this.vx = (float) Math.random() * 0.06f - 0.03f;
        this.vz = (float) Math.random() * 0.06f - 0.03f;
        this.id = (int) (Math.random() * 10000);
    }

    // Actualiza posición del pez cada frame
    public void actualizar(float time, float suelo) {
        // Movimiento aleatorio suave
        vx += (Math.random() - 0.5) * 0.006f;
        vz += (Math.random() - 0.5) * 0.006f;

        // Vuelve al centro si se aleja demasiado
        float dist = (float) Math.sqrt(x * x + z * z);
        if (dist > 8.5f) {
            vx -= x * 0.003f;
            vz -= z * 0.003f;
        }

        // Limita velocidad máxima
        float vel = (float) Math.sqrt(vx * vx + vz * vz);
        if (vel > 0.11f) { vx = (vx / vel) * 0.11f; vz = (vz / vel) * 0.11f; }
        if (vel < 0.018f) vx += 0.01f;

        x += vx;
        z += vz;

        // Altura oscilante sobre el suelo
        y = suelo + 1.4f + 0.45f * (float) Math.sin(time * 0.48f + id * 0.42f);
    }

    // Dibuja el pez completo con aletas, cola y ojos
    public void dibujar(GL2 gl, GLUT glut, float time) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        // Orientación según dirección de movimiento
        float ang = (float) Math.atan2(vz, vx);
        gl.glRotatef((float) Math.toDegrees(ang), 0, 1, 0);

        // Ondulación lateral al nadar
        float onda = (float) Math.sin(time * 5.8f + id * 0.9f);
        gl.glRotatef(onda * 5.5f, 0, 1, 0);

        float R = radio;

        // Cuerpo principal elongado
        gl.glColor3f(r, g, b);
        gl.glPushMatrix();
        gl.glScalef(1.85f, 0.60f, 0.65f);
        glut.glutSolidSphere(R, 14, 10);
        gl.glPopMatrix();

        // Vientre más claro
        gl.glColor3f(
            Math.min(1f, r * 0.85f + 0.18f),
            Math.min(1f, g * 0.85f + 0.18f),
            Math.min(1f, b * 0.85f + 0.25f)
        );
        gl.glPushMatrix();
        gl.glTranslatef(0, -R * 0.12f, 0);
        gl.glScalef(1.35f, 0.30f, 0.50f);
        glut.glutSolidSphere(R, 12, 8);
        gl.glPopMatrix();

        // Cabeza redondeada
        gl.glColor3f(r, g, b);
        gl.glPushMatrix();
        gl.glTranslatef(R * 1.15f, R * 0.04f, 0);
        gl.glScalef(0.68f, 0.60f, 0.62f);
        glut.glutSolidSphere(R, 12, 10);
        gl.glPopMatrix();

        // Pedúnculo caudal (estrechamiento antes de la cola)
        gl.glColor3f(r * 0.80f, g * 0.80f, b * 0.82f);
        gl.glPushMatrix();
        gl.glTranslatef(-R * 1.55f, 0, 0);
        gl.glScalef(0.42f, 0.30f, 0.30f);
        glut.glutSolidSphere(R, 10, 8);
        gl.glPopMatrix();

        // Cola animada con dos lóbulos
        float colaOnd = onda * 0.28f;
        gl.glColor3f(r * 0.72f, g * 0.72f, b * 0.76f);
        gl.glPushMatrix();
        gl.glTranslatef(-R * 1.82f, 0, 0);
        gl.glRotatef(colaOnd * 32f, 0, 1, 0);

        gl.glPushMatrix();
        gl.glTranslatef(-R * 0.1f, R * 0.34f, 0);
        gl.glScalef(0.72f, 0.62f, 0.18f);
        glut.glutSolidSphere(R * 0.68f, 8, 6);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-R * 0.1f, -R * 0.34f, 0);
        gl.glScalef(0.72f, 0.62f, 0.18f);
        glut.glutSolidSphere(R * 0.68f, 8, 6);
        gl.glPopMatrix();
        gl.glPopMatrix();

        // Aleta dorsal
        gl.glColor3f(r * 0.68f, g * 0.68f, b * 0.75f);
        gl.glPushMatrix();
        gl.glTranslatef(R * 0.15f, R * 0.60f, 0);
        gl.glScalef(1.0f, 0.55f, 0.10f);
        glut.glutSolidSphere(R * 0.68f, 8, 6);
        gl.glPopMatrix();

        // Aletas pectorales animadas
        float aAnim = 15f * (float) Math.sin(time * 5.5f + id);
        gl.glColor3f(r * 0.72f, g * 0.72f, b * 0.80f);

        gl.glPushMatrix();
        gl.glTranslatef(R * 0.30f, -R * 0.06f, R * 0.60f);
        gl.glRotatef(aAnim, 0, 0, 1);
        gl.glScalef(0.48f, 0.10f, 0.72f);
        glut.glutSolidSphere(R * 0.52f, 7, 5);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(R * 0.30f, -R * 0.06f, -R * 0.60f);
        gl.glRotatef(-aAnim, 0, 0, 1);
        gl.glScalef(0.48f, 0.10f, 0.72f);
        glut.glutSolidSphere(R * 0.52f, 7, 5);
        gl.glPopMatrix();

        // Ojos
        Utils.dibujarOjo(gl, glut, R * 0.90f, R * 0.18f,  R * 0.40f, 0.052f);
        Utils.dibujarOjo(gl, glut, R * 0.90f, R * 0.18f, -R * 0.40f, 0.052f);

        gl.glPopMatrix();
    }
}