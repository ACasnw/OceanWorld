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

// Funciones de utilidad compartidas por varias clases
public class Utils {

    // Colores globales del ecosistema — todos vienen de aquí
    public static final float[] C_ABISMO   = {0.008f, 0.030f, 0.120f};
    public static final float[] C_PROFUNDO = {0.012f, 0.055f, 0.175f};
    public static final float[] C_MEDIO    = {0.018f, 0.095f, 0.240f};

    public static final float[] C_ARENA    = {0.580f, 0.520f, 0.360f};
    public static final float[] C_ARENA_O  = {0.420f, 0.375f, 0.255f};
    public static final float[] C_ROCA     = {0.195f, 0.215f, 0.235f};
    public static final float[] C_ROCA_C   = {0.260f, 0.285f, 0.305f};

    public static final float[] C_ALGA_B   = {0.048f, 0.340f, 0.245f};
    public static final float[] C_ALGA_C   = {0.065f, 0.460f, 0.320f};
    public static final float[] C_CORAL_R  = {0.680f, 0.255f, 0.195f};
    public static final float[] C_CORAL_P  = {0.720f, 0.330f, 0.420f};
    public static final float[] C_CORAL_O  = {0.640f, 0.420f, 0.165f};
    public static final float[] C_CORAL_M  = {0.490f, 0.195f, 0.420f};

    public static final float[] C_PEZ_ORO  = {0.820f, 0.650f, 0.075f};
    public static final float[] C_PEZ_TUR  = {0.065f, 0.520f, 0.620f};
    public static final float[] C_PEZ_AZU  = {0.075f, 0.210f, 0.680f};
    public static final float[] C_PEZ_COR  = {0.840f, 0.395f, 0.095f};
    public static final float[] C_PULPO    = {0.360f, 0.105f, 0.390f};
    public static final float[] C_TORT     = {0.175f, 0.410f, 0.210f};
    public static final float[] C_CAMARON  = {0.880f, 0.340f, 0.240f};

    // Dibuja un ojo con iris oscuro y punto de brillo
    public static void dibujarOjo(GL2 gl, GLUT glut, float ox, float oy, float oz, float r) {
        gl.glPushMatrix();
        gl.glTranslatef(ox, oy, oz);
        gl.glColor3f(0.04f, 0.04f, 0.06f);
        glut.glutSolidSphere(r, 7, 6);
        gl.glColor3f(0.88f, 0.88f, 0.96f);
        gl.glTranslatef(r * 0.32f, r * 0.32f, r * 0.12f);
        glut.glutSolidSphere(r * 0.28f, 4, 4);
        gl.glPopMatrix();
    }

    // Dibuja un círculo plano sobre el suelo (para sombras y brillos)
    public static void circuloSuelo(GL2 gl, float x, float z, float r, float suelo) {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex3f(x, suelo + 0.02f, z);
        for (int i = 0; i <= 18; i++) {
            double a = i * Math.PI * 2 / 18;
            gl.glVertex3f(x + (float) Math.cos(a) * r, suelo + 0.02f, z + (float) Math.sin(a) * r);
        }
        gl.glEnd();
    }
}