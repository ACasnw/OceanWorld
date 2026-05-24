/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oceanworld;
import com.jogamp.opengl.glu.GLU;
/**
 *
 * @author annie
 */

// Cámara principal que sigue al camarón desde atrás
public class Camera {

    // Distancia y altura de la cámara respecto al personaje
    private static final float DIST_CAM = 5.0f;
    private static final float ALT_CAM  = 2.8f;

    // Posición del ojo calculada cada frame
    private float eyeX, eyeY, eyeZ;

    // Aplica la cámara siguiendo al camarón
    public void aplicar(GLU glu, Camaron camaron) {
        float rot = camaron.rotY;

        // Posición del ojo detrás y arriba del camarón
        eyeX = camaron.x - (float) Math.sin(Math.toRadians(rot)) * DIST_CAM;
        eyeY = camaron.y + ALT_CAM;
        eyeZ = camaron.z + (float) Math.cos(Math.toRadians(rot)) * DIST_CAM;

        // Mira ligeramente por encima del camarón
        glu.gluLookAt(
            eyeX, eyeY, eyeZ,
            camaron.x, camaron.y + 0.5f, camaron.z,
            0, 1, 0
        );
    }
}