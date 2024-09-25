package juego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

public class Enemigo{
	
	ImageIcon sprite;
	Juego juego;
	Rectangle2D enemigoReact = getBoundsAplastar();
    private boolean onGround=false;
    private boolean left, right=false;
    private int x, y;
    private int ANCHO, ALTO;
    private int tempPasos=10,temp=tempPasos,aux=0;
    private boolean vivo=true;
    private TipoEnemigo tipoEnemigo;
	
    public Enemigo() {
	}
    
	public Enemigo(int x, int y, TipoEnemigo tipoEnemigo, boolean left, Juego juego) {
		this.juego = juego;
		this.x=x;
		this.y=y;
        perspectiva(juego.getNivel());
		this.tipoEnemigo=tipoEnemigo;
		if(tipoEnemigo==tipoEnemigo.ANCIANA) {
			this.sprite = new ImageIcon(getClass().getResource("/imagenes/vieja-1.png"));
		} else if(tipoEnemigo==tipoEnemigo.POLICIA||tipoEnemigo==tipoEnemigo.POLICIACORRIENDO) {
			this.sprite = new ImageIcon(getClass().getResource("/imagenes/policiaD.png"));
		} /*else if(tipoEnemigo==tipoEnemigo.JEFE) {
			this.sprite = new ImageIcon(getClass().getResource("/imagenes/vieja-1.png")); //jefe
		}*/
		this.left=left;
		if(!left) {
			right=true;
		} else {
			right=false;
		}
		
	}
	
	public void paint(Graphics2D g){
		if(temp<=0&&aux==0) {
			if(tipoEnemigo==tipoEnemigo.ANCIANA) {
				sprite = new ImageIcon(getClass().getResource("/imagenes/vieja-1.png"));	
			} 
			if(tipoEnemigo==tipoEnemigo.POLICIA || tipoEnemigo==tipoEnemigo.POLICIACORRIENDO) {
				sprite = new ImageIcon(getClass().getResource("/imagenes/policiaD.png"));	
			} /*else if(tipoEnemigo==tipoEnemigo.JEFE) {sprite = new ImageIcon(getClass().getResource("/imagenes/vieja-3.png"));	}*/
			aux=1;
			temp=tempPasos;
		} else if(temp<=0&&aux==1){
			if(tipoEnemigo==tipoEnemigo.ANCIANA) {
				sprite = new ImageIcon(getClass().getResource("/imagenes/vieja-2.png"));	
			}
			if(tipoEnemigo==tipoEnemigo.POLICIA || tipoEnemigo==tipoEnemigo.POLICIACORRIENDO) {
				sprite = new ImageIcon(getClass().getResource("/imagenes/policiaD.png"));	
			} /*else if(tipoEnemigo==tipoEnemigo.JEFE) {sprite = new ImageIcon(getClass().getResource("/imagenes/vieja-3.png"));	}*/
			aux=0;
			temp=tempPasos;
		}
		temp--;
		if(vivo&&!juego.getMenu()) {
			g.drawImage(sprite.getImage(), x, y, ANCHO, ALTO,null);
			choque();
			moverse();
	        //g.setColor(Color.BLUE);
	        //g.fillRect(x+ANCHO/5, y-ALTO/8, ANCHO-ANCHO/3, ALTO-ALTO/2);
	        //g.fillRect(x, y, ANCHO, ALTO);
		}
	}
	
	public void choque() {
		if(vivo) {
			Rectangle2D enemigoReact = getBounds();
			Rectangle2D enemigoReactAplas = getBoundsAplastar();
			Rectangle2D protaReact = juego.getJugador().getBounds();
			
			if(enemigoReactAplas.intersects(protaReact)&&juego.getJugador().getVivo()&&!juego.getJugador().getOnGround()) {
				vivo=false;
			} else {

				if(!(juego.getJugador().getDaniado()==1)) {
					if(enemigoReact.intersects(protaReact)&&juego.getJugador().getVivo()) {
						if(!(juego.getJugador().getEnmascarado())) {
							juego.getJugador().morir();
						} else {
							morir();
							juego.getJugador().achicar();
							juego.getJugador().daniar();
						}
					}
				}
			}
		}
	}
	
	public void moverse() {
    	if(vivo) {
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
    		
    		for(Enemigo enemigo : juego.getEnemigos()) {
    			Rectangle2D obstaculoReact = enemigo.getBounds();
    			Rectangle2D enemigoReact = new Rectangle2D.Double(x+2,y,ANCHO,ALTO);
    			if(enemigoReact.intersects(obstaculoReact)&&enemigo!=this&&enemigo.getVivo()) {
    				voltearse();
        			enemigo.voltearse();	
    				break;
    			}
    		}
    		for(Enemigo enemigo : juego.getEnemigos()) {
    			Rectangle2D obstaculoReact = enemigo.getBounds();
    			Rectangle2D enemigoReact = new Rectangle2D.Double(x-2,y,ANCHO,ALTO);
    			if(enemigoReact.intersects(obstaculoReact)&&enemigo!=this&&enemigo.getVivo()) {
	    			voltearse();
	    			enemigo.voltearse();
	    			break;
    			}
    		}

    		if(!onGround) {
    			y+=4;
    	    	//System.out.println("y: " + y);
    		}
    		
    		//System.out.println("left: " + left);
    		if(left) {
    			if(tipoEnemigo==tipoEnemigo.POLICIACORRIENDO) {
        			x-=3;
    			}
    			x-=1;
        		//System.out.println("x: " + x);
    		}
    		
    		if(right) {
    			if(tipoEnemigo==tipoEnemigo.POLICIACORRIENDO) {
    				x+=3;
    			}
    			x+=1;
    		}
        	choque();
    	}
    	//System.out.println("left: " + left + " right : " + right + " jump: " + jump + " jumping= " + jumping + " onGround: " + onGround);
	}
	
	public void perspectiva(int nivel) {
    	if(nivel==1||nivel==2||nivel==3) {
        	setAlto(120);
        	setAncho(100);
    	} else if(nivel==4||nivel==5) {
        	setAlto(50);
        	setAncho(40);
    	}
    }
	
	public void voltearse() {
		if(!left) {
			right=false;
			left=true;		
		} else {
			right=true;
			left=false;	
		}
	}
	
	public void morir() {
		vivo=false;
	}
	
	public boolean getVivo() {
		return vivo;
	}
	
	public boolean getLeft() {
		return left;
	}
	
	public boolean getRight() {
		return right;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getAncho() {
		return ANCHO;
	}
	
	public int getAlto() {
		return ALTO;
	}
	
	public void setAncho(int ANCHO) {
		this.ANCHO=ANCHO;
	}
	
	public void setAlto(int ALTO) {
		this.ALTO=ALTO;
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x, y, ANCHO, ALTO);
	}
	
	
	public Rectangle2D getBoundsAplastar() {
		return new Rectangle2D.Double(x+ANCHO/5, y-ALTO/8, ANCHO-ANCHO/3, ALTO-ALTO/2);
	}

	public void drawInvertedImage(Graphics2D g2d, Image img, int x, int y, int width, int height) {
	    // Crear una transformación para invertir la imagen horizontalmente
	    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
	    // Mover la imagen a la posición correcta después de la inversión
	    tx.translate(-x - width, y);

	    // Dibujar la imagen usando la transformación
	    g2d.drawImage(img, tx, null);
	}

}
