package juego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.ImageIcon;

public class BloqueRecompensa extends Obstaculo{
	private ImageIcon sprite;
	private Juego juego;
    private int x;
    private int y;
    private int ANCHO;
    private int ALTO;
    private Jugador jugador;
    private ObjetoRecompensa objetoRecompensa;
	
	public BloqueRecompensa(int x, int y, boolean moving, Juego juego) {
		super(x, y, moving);
        setAncho(40);
        setAlto(40);
		moving = false;
        this.x = x;
        this.y = y;
        this.ANCHO = 40;
        this.ALTO = 40;
		this.juego = juego;
		this.jugador= juego.getJugador();
		objetoRecompensa=new ObjetoRecompensa(x,y-ALTO,true, juego);
		tenerRecompensa();
		sprite = new ImageIcon(getClass().getResource("/imagenes/bloqueespecial.png"));
	}
	
	public void paint(Graphics g){
		g.drawImage(sprite.getImage(),getX(),getY(),getAncho(),getAlto(),null);
		//g.setColor(Color.red);
		//g.fillRect(getX(),getY()+getAlto()-getAlto()/8,getAncho(),getAlto()/5);
		choque();
		objetoRecompensa.paint(g);	
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(getX(),getY(),getAncho(),getAlto());
	}
	
	public void choque() {
		Rectangle2D jugadorReact = new Rectangle2D.Double(jugador.getX(),jugador.getY(),jugador.getAncho(),jugador.getAlto());
		Rectangle2D bloqueRecompensaReact = getBoundsGolpear();
		if(jugadorReact.intersects(bloqueRecompensaReact)) {
			objetoRecompensa.spawn();
		}
	}
}
