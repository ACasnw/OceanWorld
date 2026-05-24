/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oceanworld;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.*;
/**
 *
 * @author annie
 */

// Renderizador principal: dibuja y actualiza todo el ecosistema
public class OceanRenderer implements GLEventListener {

    private GLU  glu;
    private GLUT glut;

    // Tiempo global para animaciones
    private float time = 0;

    // Cámara y personaje
    private Camera  camera;
    private Camaron camaron;

    // Estado de teclas (lo actualiza OceanWorld)
    public boolean teclaW, teclaA, teclaS, teclaD, teclaSpace;
    public float   camRotYObj = 0;

    // Límite del suelo del mundo
    private static final float SUELO = -2.5f;

    // Listas de criaturas y elementos del mundo
    private List<Pez>         peces      = new ArrayList<>();
    private List<Burbuja>     burbujas   = new ArrayList<>();
    private List<Coral>       corales    = new ArrayList<>();
    private List<Alga>        algas      = new ArrayList<>();
    private List<Roca>        rocas      = new ArrayList<>();
    private List<EstrellaMar> estrellas  = new ArrayList<>();
    private List<Pulpo>       pulpos     = new ArrayList<>();
    private List<Tortuga>     tortugas   = new ArrayList<>();
    private List<Medusa>      medusas    = new ArrayList<>();
    private List<Particula>   particulas = new ArrayList<>();

    //  INIT: configura OpenGL y crea el mundo
    @Override
    public void init(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        glu  = new GLU();
        glut = new GLUT();

        camera  = new Camera();
        camaron = new Camaron(SUELO);

        // Configuración básica de OpenGL
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Niebla submarina que oculta los bordes del mundo
        float[] niebla = {Utils.C_ABISMO[0], Utils.C_ABISMO[1], Utils.C_ABISMO[2], 1f};
        gl.glEnable(GL2.GL_FOG);
        gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP2);
        gl.glFogfv(GL2.GL_FOG_COLOR, niebla, 0);
        gl.glFogf(GL2.GL_FOG_DENSITY, 0.030f);
        gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);

        poblarMundo();
    }

    //  RESHAPE: ajusta perspectiva al cambiar tamaño de ventana
    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        GL2 gl = d.getGL().getGL2();
        if (h <= 0) h = 1;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(70, (float) w / h, 0.1, 250);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    
    //  DISPLAY: dibuja un frame completo
    @Override
    public void display(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        gl.glClearColor(Utils.C_ABISMO[0], Utils.C_ABISMO[1], Utils.C_ABISMO[2], 1f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Sincroniza la rotación objetivo desde el teclado
        camaron.rotYObj = camRotYObj;

        // Física del camarón
        camaron.actualizar(teclaW, teclaA, teclaS, teclaD, teclaSpace);

        // Cámara detrás del camarón
        camera.aplicar(glu, camaron);

        // Configura iluminación
        aplicarLuz(gl);

        // Dibuja opacos primero, transparentes al final
        dibujarAtmosfera(gl);
        dibujarSuelo(gl);
        for (Roca r    : rocas)     r.dibujar(gl, glut);
        for (EstrellaMar e : estrellas) e.dibujar(gl, glut, time);
        for (Coral c   : corales)   c.dibujar(gl, glut);
        for (Alga a    : algas)     a.dibujar(gl, glut, time);
        for (Pulpo p   : pulpos)    p.dibujar(gl, glut, time);
        for (Tortuga t : tortugas)  t.dibujar(gl, glut, time);
        for (Pez p     : peces)     p.dibujar(gl, glut, time);
        camaron.dibujar(gl, glut, time);

        // Transparentes al final (blend correcto)
        dibujarMedusas(gl);
        dibujarParticulas(gl);
        dibujarRayosLuz(gl);
        dibujarBurbujas(gl);

        // Actualiza lógica de todos los objetos
        for (Pez p     : peces)     p.actualizar(time, SUELO);
        for (EstrellaMar e : estrellas) e.actualizar();
        for (Medusa m  : medusas)   m.actualizar(time);
        for (Particula p : particulas) p.actualizar(time);
        for (Pulpo p   : pulpos)    p.actualizar();
        for (Tortuga t : tortugas)  t.actualizar(time);
        actualizarBurbujas();

        time += 0.016f;
    }

    @Override
    public void dispose(GLAutoDrawable d) {}

    //  LUZ SUBMARINA: sol desde arriba + relleno desde abajo
    private void aplicarLuz(GL2 gl) {
        // Luz principal: tono azul cian oscilante
        float[] pos0 = {5 * (float) Math.sin(time * 0.22f), 9f, 4 * (float) Math.cos(time * 0.16f), 1f};
        float[] dif0 = {0.32f, 0.58f, 0.82f, 1f};
        float[] amb0 = {0.06f, 0.12f, 0.22f, 1f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  dif0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  amb0, 0);

        // Luz de relleno: reflejo del suelo
        float[] pos1 = {-3f, SUELO + 1.5f, 2f, 1f};
        float[] dif1 = {0.05f, 0.18f, 0.24f, 1f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos1, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE,  dif1, 0);
    }

    //  ATMÓSFERA: fondo de abismo + superficie del agua
    private void dibujarAtmosfera(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);

        // Pared de abismo al fondo
        gl.glColor4f(Utils.C_ABISMO[0], Utils.C_ABISMO[1], Utils.C_ABISMO[2], 1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-150, -20, -150); gl.glVertex3f(150, -20, -150);
        gl.glVertex3f( 150,  14, -150); gl.glVertex3f(-150, 14, -150);
        gl.glEnd();

        // Superficie del agua con ondas suaves
        for (int fila = 0; fila < 6; fila++) {
            float ow  = 0.06f * (float) Math.sin(time * 0.45f + fila * 0.9f);
            float alp = 0.08f + 0.03f * (float) Math.sin(time * 0.38f + fila);
            gl.glColor4f(0.18f, 0.52f, 0.78f, alp);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-60f, 5.0f + ow, -60f + fila * 20f);
            gl.glVertex3f( 60f, 5.0f + ow, -60f + fila * 20f);
            gl.glVertex3f( 60f, 5.0f + ow, -40f + fila * 20f);
            gl.glVertex3f(-60f, 5.0f + ow, -40f + fila * 20f);
            gl.glEnd();
        }

        gl.glEnable(GL2.GL_LIGHTING);
    }

    //  SUELO DE ARENA con sombras y brillos de luz
    private void dibujarSuelo(GL2 gl) {
        // Arena base
        gl.glColor3f(Utils.C_ARENA[0], Utils.C_ARENA[1], Utils.C_ARENA[2]);
        gl.glBegin(GL2.GL_QUADS);
        gl.glNormal3f(0f, 1f, 0f);
        gl.glVertex3f(-22f, SUELO, -22f); gl.glVertex3f(22f, SUELO, -22f);
        gl.glVertex3f( 22f, SUELO,  22f); gl.glVertex3f(-22f, SUELO,  22f);
        gl.glEnd();

        gl.glDisable(GL2.GL_LIGHTING);

        // Parches de sombra para dar sensación de relieve
        float[][] sombras = {{-4,2,2.0f},{3,-5,1.7f},{-7,-3,1.3f},{6,6,2.2f},
                             {-2,7,1.4f},{8,-1,1.1f},{-6,6,1.8f},{1,-8,2.0f},{5,3,1.6f}};
        for (float[] p : sombras) {
            gl.glColor4f(Utils.C_ARENA_O[0], Utils.C_ARENA_O[1], Utils.C_ARENA_O[2], 0.50f);
            Utils.circuloSuelo(gl, p[0], p[1], p[2], SUELO);
        }

        // Brillos de luz solar pulsantes
        float[][] brillos = {{0,0,1.4f},{-3,-4,1.0f},{4,2,1.2f},{-5,5,0.85f},{7,-3,0.9f}};
        for (float[] p : brillos) {
            float pulso = 0.28f + 0.12f * (float) Math.sin(time * 0.7f + p[0]);
            gl.glColor4f(0.72f, 0.66f, 0.47f, pulso);
            Utils.circuloSuelo(gl, p[0], p[1], p[2], SUELO);
        }

        gl.glEnable(GL2.GL_LIGHTING);
    }

    // Dibuja medusas con iluminación especial y blending aditivo
    private void dibujarMedusas(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_BLEND);
        for (Medusa m : medusas) m.dibujar(gl, glut, time);
        // Restaura blending normal
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    // Dibuja partículas de plancton con blending aditivo
    private void dibujarParticulas(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
        for (Particula p : particulas) p.dibujar(gl, glut, time);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    // Rayos de luz solar que bajan desde la superficie
    private void dibujarRayosLuz(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
        for (int i = 0; i < 7; i++) {
            float angR  = i * 52f + time * 4.5f;
            float rx    = (float) Math.sin(Math.toRadians(angR)) * 2.8f;
            float rz    = (float) Math.cos(Math.toRadians(angR)) * 2.8f;
            float inten = 0.022f + 0.010f * (float) Math.sin(time * 1.0f + i * 0.8f);
            gl.glColor4f(0.38f, 0.62f, 0.90f, inten);
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex3f(rx, 5f, rz);
            gl.glVertex3f(rx + 2.0f, -6f, rz + 2.0f);
            gl.glVertex3f(rx - 2.0f, -6f, rz - 2.0f);
            gl.glEnd();
        }
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    // Dibuja burbujas
    private void dibujarBurbujas(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        for (Burbuja b : burbujas) b.dibujar(gl, glut);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    // Genera nuevas burbujas y elimina las que salen del agua
    private void actualizarBurbujas() {
        if (Math.random() < 0.09f)
            burbujas.add(new Burbuja(
                (float) Math.random() * 14 - 7, SUELO + 0.2f,
                (float) Math.random() * 14 - 7
            ));
        for (int i = burbujas.size() - 1; i >= 0; i--) {
            Burbuja b = burbujas.get(i);
            b.actualizar(time, i);
            if (b.fuera()) burbujas.remove(i);
        }
    }

    //  POBLAR MUNDO: distribución artística por zonas
    private void poblarMundo() {
        // Corales en zonas del arrecife
        agregarGrupoCorales( 5.5f, -1f,  Utils.C_CORAL_R, 3);
        agregarGrupoCorales( 7.0f,  4f,  Utils.C_CORAL_P, 2);
        agregarGrupoCorales( 4.0f,  6f,  Utils.C_CORAL_O, 2);
        agregarGrupoCorales(-6.5f, -2f,  Utils.C_CORAL_M, 3);
        agregarGrupoCorales(-5.0f,  5f,  Utils.C_CORAL_R, 2);
        corales.add(new Coral( 2f, SUELO, -7f, Utils.C_CORAL_P, 0.30f, 0));
        corales.add(new Coral(-3f, SUELO,  8f, Utils.C_CORAL_O, 0.38f, 1));
        corales.add(new Coral( 9f, SUELO, -4f, Utils.C_CORAL_R, 0.25f, 2));

        // Jardín de algas (izquierda y centro)
        float[][] zonasAlga = {
            {-4f,3f},{-3f,1f},{-5f,5f},{-2f,6f},{-6f,2f},
            {0f,4f},{1f,7f},{-1f,2f},{2f,-3f},{-4f,-5f},
            {3f,-6f},{-7f,0f},{-2f,-2f},{5f,-8f},{-8f,4f},
            {1f,-8f},{4f,2f},{-3f,7f},{6f,-5f},{-5f,-3f},
        };
        float[] alturas = {1.2f,1.8f,0.9f,2.1f,1.5f,0.7f,2.3f,1.1f,1.7f,0.8f,
                           1.4f,1.9f,0.6f,2.0f,1.3f,1.6f,0.85f,1.75f,1.05f,2.2f};
        for (int i = 0; i < zonasAlga.length; i++) {
            for (int k = 0; k < 2 + (i % 2); k++) {
                float jx   = (float) (Math.random() * 0.7 - 0.35);
                float jz   = (float) (Math.random() * 0.7 - 0.35);
                float hVar = alturas[i] * (0.7f + (float) Math.random() * 0.5f);
                algas.add(new Alga(zonasAlga[i][0] + jx, SUELO, zonasAlga[i][1] + jz, hVar));
            }
        }

        // Rocas en los bordes del mundo
        float[][] posRocas = {
            {-9,-8,1.1f},{9,-7,0.85f},{-10,2,0.70f},{10,3,0.95f},
            {-8,7,0.90f},{8,8,1.20f},{-10,-3,0.60f},{10,-5,0.75f},
            {3,-10,0.80f},{-4,10,0.65f},{7,-10,1.00f},{-7,9,0.88f},
            {-5,-9,0.72f},{5,9,0.55f},{0,-11,0.95f},{0,11,0.70f},
        };
        for (float[] r : posRocas) rocas.add(new Roca(r[0], SUELO, r[1], r[2]));

        // Estrellas de mar sobre el suelo
        float[][] posEst = {{2,3},{-4,-2},{6,-5},{-7,4},{1,-6},{5,7},{-2,6},{8,2},{-1,-4},{4,-1}};
        float[][] colEst = {Utils.C_CORAL_R,Utils.C_CORAL_O,Utils.C_CORAL_P,Utils.C_CORAL_R,Utils.C_CORAL_O,
                            Utils.C_CORAL_P,Utils.C_CORAL_R,Utils.C_CORAL_O,Utils.C_CORAL_P,Utils.C_CORAL_R};
        for (int i = 0; i < posEst.length; i++)
            estrellas.add(new EstrellaMar(posEst[i][0], SUELO + 0.04f, posEst[i][1],
                          0.28f + (float) Math.sin(i * 1.3f) * 0.10f, colEst[i]));

        // Banco de peces dorados
        for (int i = 0; i < 18; i++)
            peces.add(new Pez(2f+(float)Math.random()*4-2, 1.2f+(float)Math.random()*1.2f-0.6f, 1f+(float)Math.random()*4-2, Utils.C_PEZ_ORO, 0.17f, 0));
        // Peces turquesa
        for (int i = 0; i < 8; i++)
            peces.add(new Pez((float)Math.random()*12-6, 0.8f+(float)Math.random()*1.8f, (float)Math.random()*12-6, Utils.C_PEZ_TUR, 0.24f, 1));
        // Peces azul profundo
        for (int i = 0; i < 4; i++)
            peces.add(new Pez((float)Math.random()*10-5, 1.2f+(float)Math.random()*1.5f, (float)Math.random()*10-5, Utils.C_PEZ_AZU, 0.34f, 2));
        // Peces coral-naranja
        for (int i = 0; i < 7; i++)
            peces.add(new Pez((float)Math.random()*12-6, 0.5f+(float)Math.random()*2f, (float)Math.random()*12-6, Utils.C_PEZ_COR, 0.21f, 3));

        // Pulpos
        pulpos.add(new Pulpo(-4.5f, SUELO + 0.5f,  5f));
        pulpos.add(new Pulpo( 6.5f, SUELO + 0.5f, -4f));
        pulpos.add(new Pulpo(-2.0f, SUELO + 0.5f, -6f));

        // Tortugas
        tortugas.add(new Tortuga(-5f, -1.2f,  4f, 0));
        tortugas.add(new Tortuga( 7f, -1.5f, -5f, 1));

        // Medusas dentro del agua
        float[][] datosMed = {
            {-3f,1.5f,2f,0.42f},{4f,2.2f,-3f,0.38f},{-6f,1.8f,-5f,0.50f},
            {7f,2.5f,4f,0.35f},{2f,3.0f,6f,0.44f},{-5f,1.2f,7f,0.40f},
        };
        float[][] colMed = {
            {0.520f,0.720f,0.920f},{0.720f,0.520f,0.840f},{0.500f,0.860f,0.820f},
            {0.860f,0.620f,0.720f},{0.620f,0.760f,0.960f},{0.750f,0.860f,0.680f},
        };
        for (int i = 0; i < datosMed.length; i++) {
            float[] dd = datosMed[i];
            medusas.add(new Medusa(dd[0], dd[1], dd[2], dd[3], colMed[i]));
        }

        // Partículas de plancton
        for (int i = 0; i < 200; i++)
            particulas.add(new Particula(
                (float) Math.random() * 28 - 14,
                SUELO + (float) Math.random() * 9f,
                (float) Math.random() * 28 - 14
            ));
    }

    // Agrega un grupo de corales alrededor de un punto
    private void agregarGrupoCorales(float cx, float cz, float[] col, int cantidad) {
        int tipo = (int) (Math.random() * 3);
        for (int i = 0; i < cantidad; i++) {
            float jx    = (float) Math.random() * 2.2f - 1.1f;
            float jz    = (float) Math.random() * 2.2f - 1.1f;
            float radio = 0.28f + (float) Math.random() * 0.22f;
            corales.add(new Coral(cx + jx, SUELO, cz + jz, col, radio, (tipo + i) % 3));
        }
    }
}