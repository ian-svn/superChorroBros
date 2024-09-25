package juego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

public class Obstaculo {
    private int x, y, ANCHO, ALTO, nivel;
    private boolean moving;
    private int direction;
    private int moveRange;
    private int startX;
    private boolean destruido=false;
    private boolean recompensa=false;
    private ImageIcon sprite;
    public Rectangle2D bloqueReactArriba;

    public Obstaculo(int x, int y, boolean moving, int nivel) {
        this.x = x;
        this.y = y;
        this.ANCHO = 40;
        this.ALTO = 40;
        this.moving = moving;
        this.nivel = nivel;
        this.direction = 1;
        this.startX = x;
        this.moveRange = 100;
        if(this.nivel==1) {
            this.sprite = new ImageIcon(getClass().getResource("/imagenes/maderapisonivel1.png"));    	
        } else if(nivel==2) {
            this.sprite = new ImageIcon(getClass().getResource("/imagenes/maderapisonivel1.png"));    	        	
        } else if(nivel==3) {
            this.sprite = new ImageIcon(getClass().getResource("/imagenes/maderapisonivel1.png"));    	
        } else if(nivel==4) {
            this.sprite = new ImageIcon(getClass().getResource("/imagenes/asfaltopisonivel2.png"));    	
        } else if(nivel==5) {
            this.sprite = new ImageIcon(getClass().getResource("/imagenes/asfaltopisonivel2.png"));    	
        }
    }

    public Obstaculo(int x, int y, boolean moving) {
        this.x = x;
        this.y = y;
        this.ANCHO = 40;
        this.ALTO = 40;
        this.moving = moving;
    }
    
    public void update() {
        if (moving) {
            x += 2 * direction;
            if (x < startX || x > startX + moveRange) {
                direction *= -1; // Reverse direction when hitting range limits
            }
        }
    }

    public void paint(Graphics g) {
    	if(!destruido) {
    		g.drawImage(sprite.getImage(), x, y, ANCHO, ALTO,null);
    		update();
            //g.setColor(Color.RED);
            //g.fillRect(getX(), getY()-getAlto()/8, getAncho(), getAlto()-getAlto()/2-getAlto()/3);
    	}
    }

    public void choque(Jugador jugador) {
    	/*
    	Rectangle2D jugadorReactIzq = new Rectangle2D.Double(player.getX(),player.getY(),player.getAncho()+2,player.getAlto());
    	Rectangle2D jugadorReactDer = new Rectangle2D.Double(player.getX(),player.getY(),player.getAncho()-2,player.getAlto());
    	Rectangle2D obstaculoReact = getBounds();
    	if(jugadorReactAbajo.intersects(jugadorReactDer)) {
    		player.fAbj();
    		player.slt();
    	}
		if(jugadorReactDer.intersects(obstaculoReact)) {
			player.fIzq();
		}
		if(jugadorReactIzq.intersects(obstaculoReact)) {
			player.fDer();
		}
		*/
		//System.out.println("no, der: " + player.getDer());
		//System.out.println("no, izq: " + player.getIzq());
	}
    
    public Rectangle2D getBoundsAplastar() {
		return new Rectangle2D.Double(getX(), getY()-2, getAncho(), getAlto()-getAlto()/2-getAlto()/3);
	}
    
    public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x, y, ANCHO, ALTO-ALTO/10);
	}
	
	public Rectangle2D getBoundsGolpear() {
		return new Rectangle2D.Double(getX(),getY()+getAlto()-getAlto()/8,getAncho(),getAlto()/5);
	}
	
	public void spawRecompensa() {
		
	}

    // Getters for obstacle dimensions
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x=x;
    }

    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y=y;
    }

    public int getAncho() {
        return ANCHO;
    }

    public int getAlto() {
        return ALTO;
    }
    
    public void setAncho(int ANCHO) {
        this.ANCHO = ANCHO;
    }

    public void setAlto(int ALTO) {
        this.ALTO = ALTO;
    }
    
    public void destruirse() {
    	destruido=true;
    }
    
    public boolean getDestruido() {
    	return destruido;
    }
    
    public boolean getRecompensa() {
    	return recompensa;
    }
    
    public void tenerRecompensa() {
    	recompensa=true;
    }
}
