package juego;

import java.io.File;
import javax.sound.sampled.*;

import java.io.IOException;

public class ReproductorSonido {
    private Clip clip;
    private FloatControl volumenControl; 
    private boolean loop;
    private String rutas;
    File archivo;
    
    public void cargarSonido(String ruta, boolean loop) {
        try {
        	rutas=ruta;
        	archivo = new File(ruta);
        	
            if (!archivo.exists()) {
                throw new RuntimeException("El archivo de sonido no existe: " + ruta);
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivo);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if(loop) {
            	if (clip != null) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
	            } else {
	                System.err.println("El sonido no ha sido cargado correctamente.");
	            }
            }

            
        } catch (UnsupportedAudioFileException e) {
            System.err.println("El formato del archivo de sonido no es compatible: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de sonido: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("No se pudo abrir la línea de audio: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error desconocido al cargar el sonido: " + e.getMessage());
        }
        
    }

    public void reproducir(int nuevoVolumen) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            ajustarVolumen((float)(-nuevoVolumen));
        } else {
            System.err.println("El sonido no ha sido cargado correctamente.");
        }
    }

    public void detener() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public void ajustarVolumen(float nuevoVolumen) {
        if (volumenControl != null) {
            volumenControl.setValue(nuevoVolumen);
        }
    }
}
