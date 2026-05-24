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


//  BURBUJA
class Burbuja {
    public float x, y, z, radio;

    public Burbuja(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
        this.radio = 0.048f + (float) Math.random() * 0.082f;
    }

    // Sube y deriva lateralmente
    public void actualizar(float time, int index) {
        y += 0.024f + radio * 0.018f;
        x += (float) Math.sin(time * 2.0f + index * 0.3f) * 0.004f;
    }

    public boolean fuera() {
        return y > 5.8f;
    }

    // Esfera translúcida con brillo
    public void dibujar(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glColor4f(0.48f, 0.74f, 0.90f, 0.42f);
        glut.glutSolidSphere(radio, 8, 7);
        gl.glColor4f(1f, 1f, 1f, 0.38f);
        gl.glTranslatef(0.026f, 0.026f, 0);
        glut.glutSolidSphere(radio * 0.28f, 4, 4);
        gl.glPopMatrix();
    }
}

//  ALGA
class Alga {
    public float x, y, z, altura;
    public int id;

    public Alga(float x, float y, float z, float altura) {
        this.x = x; this.y = y; this.z = z;
        this.altura = altura;
        this.id = (int) (Math.random() * 10000);
    }

    // Dibuja tallo segmentado + hojas curvas
    public void dibujar(GL2 gl, GLUT glut, float time) {
        gl.glPushMatrix();

        // Balanceo orgánico doble
        float sw1 = 0.18f * (float) Math.sin(time * 1.15f + id * 0.78f);
        float sw2 = 0.10f * (float) Math.cos(time * 0.82f + id * 0.52f);
        gl.glTranslatef(x + sw1, y, z + sw2);

        float vg = Utils.C_ALGA_B[1] + 0.08f * (float) Math.sin(id * 1.1f);
        float vb = Utils.C_ALGA_B[2] + 0.04f * (float) Math.cos(id * 0.8f);

        // Tallo curvo por segmentos
        int segs   = 7;
        float altS = altura / segs;
        gl.glPushMatrix();
        for (int seg = 0; seg < segs; seg++) {
            float pct    = (float) seg / segs;
            float grosor = 0.065f * (1f - pct * 0.65f);
            float curvX  = sw1 * pct * 10f;
            float curvZ  = sw2 * pct * 8f;
            gl.glColor4f(Utils.C_ALGA_B[0], vg - pct * 0.04f, vb, 0.92f);
            gl.glRotatef(curvX, 0, 0, 1);
            gl.glRotatef(curvZ, 1, 0, 0);
            glut.glutSolidCylinder(Math.max(0.018f, grosor), altS, 5, 3);
            gl.glTranslatef(0, 0, altS);
        }
        gl.glPopMatrix();

        // Hoja principal izquierda
        gl.glPushMatrix();
        gl.glTranslatef(0, altura * 0.52f, 0);
        gl.glRotatef(55f + sw1 * 30f, 0, 1, 0);
        gl.glRotatef(-32f, 1, 0, 0);
        gl.glColor4f(Utils.C_ALGA_C[0], Utils.C_ALGA_C[1], Utils.C_ALGA_C[2], 0.88f);
        gl.glScalef(0.10f, 0.80f, 0.38f);
        glut.glutSolidSphere(0.58f, 7, 6);
        gl.glPopMatrix();

        // Hoja secundaria derecha
        gl.glPushMatrix();
        gl.glTranslatef(0, altura * 0.70f, 0);
        gl.glRotatef(-48f + sw2 * 25f, 0, 1, 0);
        gl.glRotatef(-28f, 1, 0, 0);
        gl.glColor4f(Utils.C_ALGA_B[0] + 0.01f, vg + 0.04f, vb, 0.82f);
        gl.glScalef(0.09f, 0.70f, 0.32f);
        glut.glutSolidSphere(0.52f, 7, 5);
        gl.glPopMatrix();

        // Hojita en la punta
        gl.glPushMatrix();
        gl.glTranslatef(0, altura * 0.88f, 0);
        gl.glRotatef(sw1 * 20f, 0, 1, 0);
        gl.glColor4f(Utils.C_ALGA_C[0], Utils.C_ALGA_C[1] + 0.04f, Utils.C_ALGA_C[2], 0.78f);
        gl.glScalef(0.07f, 0.55f, 0.22f);
        glut.glutSolidSphere(0.45f, 6, 5);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }
}

//  ROCA
class Roca {
    public float x, y, z, s, rotY;

    public Roca(float x, float y, float z, float s) {
        this.x = x; this.y = y; this.z = z;
        this.s    = s;
        this.rotY = (float) Math.random() * 360f;
    }

    // Dibuja tres esferas superpuestas para forma orgánica
    public void dibujar(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(rotY, 0, 1, 0);

        // Masa principal aplastada
        gl.glColor3f(Utils.C_ROCA[0], Utils.C_ROCA[1], Utils.C_ROCA[2]);
        gl.glPushMatrix();
        gl.glScalef(s * 1.45f, s * 0.62f, s * 1.12f);
        glut.glutSolidSphere(1f, 10, 8);
        gl.glPopMatrix();

        // Protuberancia trasera
        gl.glColor3f(Utils.C_ROCA[0] + 0.03f, Utils.C_ROCA[1] + 0.04f, Utils.C_ROCA[2] + 0.05f);
        gl.glPushMatrix();
        gl.glTranslatef(s * 0.48f, s * 0.18f, s * 0.28f);
        gl.glScalef(s * 0.88f, s * 0.48f, s * 0.75f);
        glut.glutSolidSphere(1f, 8, 6);
        gl.glPopMatrix();

        // Detalle en la cima
        gl.glColor3f(Utils.C_ROCA_C[0], Utils.C_ROCA_C[1], Utils.C_ROCA_C[2]);
        gl.glPushMatrix();
        gl.glTranslatef(-s * 0.15f, s * 0.52f, 0);
        gl.glScalef(s * 0.45f, s * 0.32f, s * 0.45f);
        glut.glutSolidSphere(1f, 7, 6);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }
}

//  ESTRELLA DE MAR
class EstrellaMar {
    public float x, y, z, radio;
    public float[] color;
    public int id;

    public EstrellaMar(float x, float y, float z, float radio, float[] color) {
        this.x = x; this.y = y; this.z = z;
        this.radio = radio;
        this.color = color;
        this.id    = (int) (Math.random() * 10000);
    }

    // Pequeño movimiento ocasional
    public void actualizar() {
        if (Math.random() < 0.0015f) {
            x += (float) (Math.random() - 0.5) * 0.03f;
            z += (float) (Math.random() - 0.5) * 0.03f;
        }
    }

    // Centro + 5 brazos aplanados
    public void dibujar(GL2 gl, GLUT glut, float time) {
        gl.glPushMatrix();
        float wobble = 0.025f * (float) Math.sin(time * 0.38f + id);
        gl.glTranslatef(x, y + wobble, z);
        gl.glRotatef(id % 360, 0, 1, 0);
        gl.glColor3f(color[0], color[1], color[2]);

        // Centro
        gl.glPushMatrix();
        gl.glScalef(1f, 0.20f, 1f);
        glut.glutSolidSphere(radio * 0.40f, 10, 8);
        gl.glPopMatrix();

        // 5 brazos
        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glRotatef(i * 72f, 0, 1, 0);
            gl.glTranslatef(radio * 0.82f, 0, 0);
            gl.glScalef(1.05f, 0.16f, 0.36f);
            glut.glutSolidSphere(radio * 0.72f, 9, 7);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }
}

//  PULPO
class Pulpo {
    public float x, y, z;
    public float vx, vz;
    public int id;

    public Pulpo(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
        this.id = (int) (Math.random() * 10000);
    }

    // Movimiento aleatorio lento con rebote en los bordes
    public void actualizar() {
        vx += (Math.random() - 0.5) * 0.005f;
        vz += (Math.random() - 0.5) * 0.005f;
        float vel = (float) Math.sqrt(vx * vx + vz * vz);
        if (vel > 0.060f) { vx = (vx / vel) * 0.060f; vz = (vz / vel) * 0.060f; }
        x += vx; z += vz;
        if (Math.abs(x) > 9f) vx *= -1;
        if (Math.abs(z) > 9f) vz *= -1;
    }

    // Manto + tentáculos con movimiento orgánico
    public void dibujar(GL2 gl, GLUT glut, float time) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        // Manto ovalado
        gl.glColor3f(Utils.C_PULPO[0], Utils.C_PULPO[1], Utils.C_PULPO[2]);
        gl.glPushMatrix();
        gl.glScalef(0.88f, 1.30f, 0.88f);
        glut.glutSolidSphere(0.50f, 14, 12);
        gl.glPopMatrix();

        // Base del manto más ancha
        gl.glPushMatrix();
        gl.glTranslatef(0, -0.30f, 0);
        gl.glScalef(1.18f, 0.52f, 1.18f);
        glut.glutSolidSphere(0.50f, 12, 10);
        gl.glPopMatrix();

        // Coloración clara en el vientre
        gl.glColor3f(Utils.C_PULPO[0] + 0.12f, Utils.C_PULPO[1] + 0.06f, Utils.C_PULPO[2] + 0.10f);
        gl.glPushMatrix();
        gl.glTranslatef(0.15f, -0.12f, 0);
        gl.glScalef(0.65f, 0.45f, 0.80f);
        glut.glutSolidSphere(0.40f, 10, 8);
        gl.glPopMatrix();

        // Ojos expresivos con pedúnculo
        for (int oj = 0; oj < 2; oj++) {
            float oz = (oj == 0) ? 0.32f : -0.32f;
            gl.glPushMatrix();
            gl.glTranslatef(0.35f, 0.18f, oz);
            gl.glColor3f(Utils.C_PULPO[0] + 0.05f, Utils.C_PULPO[1] + 0.02f, Utils.C_PULPO[2] + 0.05f);
            gl.glScalef(0.9f, 0.9f, 0.9f);
            glut.glutSolidSphere(0.10f, 7, 6);
            gl.glTranslatef(0.06f, 0.06f, oz * 0.15f);
            gl.glColor3f(0.04f, 0.04f, 0.06f);
            glut.glutSolidSphere(0.085f, 8, 7);
            gl.glColor3f(0.90f, 0.90f, 0.98f);
            gl.glTranslatef(0.03f, 0.03f, 0);
            glut.glutSolidSphere(0.03f, 4, 4);
            gl.glPopMatrix();
        }

        // 8 tentáculos con 5 segmentos ondulantes cada uno
        for (int t = 0; t < 8; t++) {
            gl.glPushMatrix();
            double angT = t * Math.PI * 2 / 8;
            float bx = (float) Math.cos(angT) * 0.42f;
            float bz = (float) Math.sin(angT) * 0.42f;
            gl.glTranslatef(bx, -0.52f, bz);
            gl.glRotatef((float) Math.toDegrees(angT), 0, 1, 0);

            float grosorBase = 0.095f;
            for (int seg = 0; seg < 5; seg++) {
                float pct  = (float) seg / 5f;
                float ondX = 0.12f * (float) Math.sin(time * 2.0f + t * 1.1f + seg * 0.65f);
                float ondY = 0.08f * (float) Math.cos(time * 1.7f + t * 0.8f + seg * 0.50f);
                float ondZ = 0.08f * (float) Math.sin(time * 1.5f + t * 0.6f + seg * 0.40f);
                gl.glTranslatef(ondX, ondY - 0.22f, ondZ);
                float gr = Math.max(0.015f, grosorBase * (1f - pct * 0.75f));
                gl.glColor3f(
                    Utils.C_PULPO[0] * (1f - pct * 0.18f),
                    Utils.C_PULPO[1] * (1f - pct * 0.10f),
                    Utils.C_PULPO[2] * (1f - pct * 0.18f)
                );
                glut.glutSolidSphere(gr, 6, 5);
            }

            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }
}

//  TORTUGA
class Tortuga {
    public float x, y, z, radio, velX, velZ;
    public int id;

    public Tortuga(float x, float y, float z, int id) {
        this.x = x; this.y = y; this.z = z;
        this.radio = 0.60f;
        this.id    = id;
    }

    // Nado sinusoidal tranquilo
    public void actualizar(float time) {
        velX = (float) Math.sin(time * 0.24f + id * 2.0f) * 0.052f;
        velZ = (float) Math.cos(time * 0.20f + id * 2.0f) * 0.052f;
        x += velX; z += velZ;
        // Envuelve el mundo (sale por un lado, entra por el otro)
        if (x < -10f) x = 10f; if (x > 10f) x = -10f;
        if (z < -10f) z = 10f; if (z > 10f) z = -10f;
    }

    // Caparazón segmentado + cabeza + 4 aletas
    public void dibujar(GL2 gl, GLUT glut, float time) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        float ang = (float) Math.atan2(velZ, velX);
        gl.glRotatef((float) Math.toDegrees(ang), 0, 1, 0);

        // Balanceo suave al nadar
        float rock = 3.5f * (float) Math.sin(time * 1.1f + id);
        gl.glRotatef(rock, 0, 0, 1);

        float R = radio;

        // Caparazón aplastado
        gl.glColor3f(Utils.C_TORT[0], Utils.C_TORT[1], Utils.C_TORT[2]);
        gl.glPushMatrix();
        gl.glScalef(1.18f, 0.55f, 0.95f);
        glut.glutSolidSphere(R, 16, 12);
        gl.glPopMatrix();

        // Placas del caparazón
        gl.glColor3f(Utils.C_TORT[0] + 0.05f, Utils.C_TORT[1] + 0.08f, Utils.C_TORT[2] + 0.04f);
        int[][] grid = {{0,0},{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{1,-1},{-1,1}};
        for (int[] g : grid) {
            gl.glPushMatrix();
            gl.glTranslatef(g[0] * R * 0.40f, R * 0.36f, g[1] * R * 0.38f);
            gl.glScalef(0.52f, 0.18f, 0.52f);
            glut.glutSolidSphere(R * 0.38f, 8, 5);
            gl.glPopMatrix();
        }

        // Cuello y cabeza
        gl.glColor3f(Utils.C_TORT[0] + 0.08f, Utils.C_TORT[1] + 0.12f, Utils.C_TORT[2] + 0.06f);
        gl.glPushMatrix();
        gl.glTranslatef(R * 1.08f, 0, 0);
        gl.glRotatef(-8f, 0, 0, 1);
        gl.glScalef(0.42f, 0.33f, 0.33f);
        glut.glutSolidSphere(R * 0.78f, 9, 7);
        gl.glTranslatef(R * 0.62f, 0.04f, 0);
        gl.glScalef(1.55f, 1.38f, 1.42f);
        glut.glutSolidSphere(R * 0.30f, 12, 10);
        Utils.dibujarOjo(gl, glut, R * 0.18f, R * 0.10f,  R * 0.20f, 0.048f);
        Utils.dibujarOjo(gl, glut, R * 0.18f, R * 0.10f, -R * 0.20f, 0.048f);
        gl.glPopMatrix();

        // 4 aletas animadas
        gl.glColor3f(Utils.C_TORT[0] + 0.06f, Utils.C_TORT[1] + 0.10f, Utils.C_TORT[2] + 0.04f);
        float aAnim = 18f * (float) Math.sin(time * 1.5f + id);
        float[][] aPos = {{0.62f,-0.10f,0.68f},{0.62f,-0.10f,-0.68f},
                          {-0.48f,-0.10f,0.65f},{-0.48f,-0.10f,-0.65f}};
        float[] aDir = {aAnim, -aAnim, -aAnim, aAnim};
        for (int ai = 0; ai < 4; ai++) {
            gl.glPushMatrix();
            gl.glTranslatef(aPos[ai][0] * R, aPos[ai][1] * R, aPos[ai][2] * R);
            gl.glRotatef(aDir[ai], 0, 0, 1);
            gl.glScalef(0.25f, 0.10f, 0.65f);
            glut.glutSolidSphere(R * 0.72f, 9, 6);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }
}