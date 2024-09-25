package juego;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

public class Joya {
	private int x;
	private int y;
	private int ANCHO=40;
	private int ALTO=40;
	private boolean consumido=false;
	private boolean onPersonaje=false;
	private ImageIcon sprite;
	private Juego juego;
	
	public Joya (int x, int y, Juego juego) {
		this.x=x;
		this.y=y;
		this.juego=juego;
		sprite = new ImageIcon(getClass().getResource("/imagenes/collar.png"));
	}
	
	public void paint(Graphics g) {
		if(!consumido) {
			g.drawImage(sprite.getImage(),x,y,ANCHO,ALTO,null);		
			choque();
		}
	}
	
	public void choque() {
		Rectangle2D jugadorReact = juego.getJugador().getBounds();
		Rectangle2D joyaReact = getBounds();
		if(jugadorReact.intersects(joyaReact)) {
			onPersonaje=true;
		} else {
			onPersonaje=false;
		}
		//System.out.println("onPer: " + onPersonaje);
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x,y,ANCHO,ALTO);
	}
	
	public boolean getOnPersonaje() {
		return onPersonaje;
	}
	
	public boolean getConsumido() {
		return consumido;
	}
	
	public void consumir() {
		consumido=true;
	}
	
}
