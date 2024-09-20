package juego;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

public class Meta{
	private Integer x;
	private Integer y;
	private int ANCHO;
	private int ALTO;
	private Juego juego;
	private ImageIcon sprite;
	private ImageIcon flecha;
	private boolean destruido;
	
	public Meta(int x, int y,Juego juego) {
		this.x = x;
		this.y = y;
		this.juego = juego;
        perspectiva(juego.getNivel());
        
        if(juego.getNivel()==3) {
        	sprite = new ImageIcon(getClass().getResource("/imagenes/puertaparaelpiso2alpiso3.png"));
        } else {
            sprite=new ImageIcon(getClass().getResource("/imagenes/escalera.png"));
        }
        
		flecha=new ImageIcon(getClass().getResource("/gifs/flecha.gif"));
	}
	
	public void paint(Graphics g) {
		if(!destruido) {
			g.drawImage(sprite.getImage(),x,y,ANCHO,ALTO, null);
			if(juego.getJugador().getOnDoor()){
				g.drawImage(flecha.getImage(),x-ANCHO/4,y-ALTO,ANCHO+ANCHO/2,ALTO, null);
			}
			//System.out.println(juego.getJugador().getOnDoor());
		}
		choque(juego.getJugador());
	}
	
	
	public void destruirse() {
		destruido=false;
	}
	
	public int getAlto() {
		return ALTO;
	}
	
	public int getAncho() {
		return ALTO;
	}
	
	public void setAlto(int ALTO) {
		this.ALTO=ALTO;
	}
	
	public void setAncho(int ANCHO) {
		this.ANCHO=ANCHO;
	}
	
	public void choque(Jugador jugador) {
		Rectangle2D jugadorReact = jugador.getBounds();
		Rectangle2D metaReact = getBounds();
		if(jugadorReact.intersects(metaReact)) {
			jugador.abrir();
		} else {
			jugador.cerrar();
		}
	}
	
	public void perspectiva(int nivel) {
    	if(nivel==1||nivel==2||nivel==3) {
        	setAncho(200);
        	setAlto(250);
    	} else if(nivel==4||nivel==5) {
        	setAlto(50);
        	setAncho(40);
    	}
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x,y,ANCHO,ALTO);
	}
}
