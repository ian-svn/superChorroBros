package juego;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

public class ObjetoRecompensa {
	private int x;
	private int y;
	private int ANCHO=40;
	private int ALTO=40;
	private ImageIcon sprite;
	private boolean spawneado=false;
	private boolean consumido;
	private Juego juego;
	private Jugador jugador;
	private int numeroRandom;
	private boolean left;
	private boolean right;
	private boolean onGround;
	
	public ObjetoRecompensa(int x,int y, boolean left, Juego juego) {
		this.x=x;
		this.y=y;
		this.juego=juego;
		this.jugador=juego.getJugador();
		this.consumido = false;
		this.left=left;
		this.numeroRandom = (int)(Math.random() * 10);
		//this.numeroRandom = 5;
		if(numeroRandom>=0&&numeroRandom<=3) {
			ANCHO=40;//morir
			ALTO=40;
			sprite=new ImageIcon(getClass().getResource("/imagenes/bloqueespecial.png"));
		} else if (numeroRandom>=4&&numeroRandom<=8) {
			ANCHO=40;//mascara
			ALTO=30;
			sprite = new ImageIcon(getClass().getResource("/imagenes/mascara.png"));
		} else if (numeroRandom>=9&&numeroRandom<=10) {
			ANCHO=30;//zapas
			ALTO=30;
			sprite=new ImageIcon(getClass().getResource("/imagenes/zapatillas.png"));
		}
		
		if(!left) {
			right=true;
		} else {
			right=false;
		}
	}
	
	public void paint(Graphics g) {
		if(!consumido&&spawneado) {
			g.drawImage(sprite.getImage(),x,y,ANCHO,ALTO,null);		
			//sSystem.out.println("holas");
			choque();
		}
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x,y,ANCHO,ALTO);
	}
	
	public boolean getConsumido() {
		return consumido;
	}
	
	public void consumir() {
		consumido=false;
	}
	
	public void choque() {
		if(!consumido&&spawneado) {
			Rectangle2D jugadorReact = jugador.getBounds();
			Rectangle2D bloqueRecompensaReact = getBounds();
			if(jugadorReact.intersects(bloqueRecompensaReact)) {
				if(numeroRandom>=0&&numeroRandom<=3) {
					juego.getJugador().morir();
				} else if (numeroRandom>=4&&numeroRandom<=8) {
					juego.getJugador().enmascarar();
				} else if (numeroRandom>=9&&numeroRandom<=10) {
					juego.getJugador().nuevasZapas();
				}
				consumido=true;
				despawn();
			}
			moverse();
			//System.out.println("num}: " + numeroRandom);
		}
		
	}
	
	public void moverse() {
		//System.out.println("numeror: " + numeroRandom);
		if(!consumido&&spawneado) {
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
    			Rectangle2D obstaculoReact = obstaculo.getBounds();
    			Rectangle2D enemigoReact = new Rectangle2D.Double(x+3,y,ANCHO,ALTO);
    			if(enemigoReact.intersects(obstaculoReact)&&!obstaculo.getDestruido()) {
    				right=false;
    				left=true;
    				break;
    			}
    		}
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
    			Rectangle2D obstaculoReact = obstaculo.getBounds();
    			Rectangle2D enemigoReact = new Rectangle2D.Double(x-3,y,ANCHO,ALTO);
    			if(enemigoReact.intersects(obstaculoReact)&&!obstaculo.getDestruido()) {
    				right=true;
    				left=false;
    				break;
    			}
    		}
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
    			Rectangle2D obstaculoReactArriba = obstaculo.getBoundsAplastar();//new Rectangle2D.Double(getX(), getY()-8, getAncho(), getAlto()-getAlto()/2-getAlto()/3
    			Rectangle2D enemigoReact = getBounds();
    			if(enemigoReact.intersects(obstaculoReactArriba)&&!obstaculo.getDestruido()) {
    				onGround=true;
    				break;
    			} else {
    				onGround=false;
    			}
    		}

    		if(!onGround) {
    			y+=4;
    	    	//System.out.println("y: " + y);
    		}
    		
    		//System.out.println("left: " + left);
    		if(left) {
    			x-=1;
        		//System.out.println("x: " + x);
    		}
    		
    		if(right) {
    			x+=1;
    		}
		}
	}
	
	public void spawn() {
		spawneado=true;
	}
	public void despawn() {
		spawneado=false;
	}
	
	public boolean getSpawn() {
		return spawneado;
	}
	
}
