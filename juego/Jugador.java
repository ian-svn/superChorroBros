package juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class Jugador {
    private Juego juego;
    private int x, y;
    private int vx;
    private int vy;
    private int auxx=vy;
    private boolean crecido=false;
    private boolean onDoor=false;
    private boolean onJoya=false;
    private boolean onGround=false;
    private boolean left=false, right=false, jump=false, jumping=false, agachado=false;
    private String ultimoLado = "Der";
    private ImageIcon sprite;
    private boolean vivo=true;
    private int ANCHO = 30, ALTO = 50;
    private int tempSalto, temp = tempSalto;
    private int tempCorrer, tempC = tempCorrer;
    private int tempAgachado, tempA = tempAgachado;
    private int tempDaniado, tempD=tempDaniado, daniado;
    private ReproductorSonido rep;
    private boolean enmascarado=false;
    private int anchoAux=ANCHO,altoAux=ALTO,auxxx=0;
    //private int tempSuspendido = 5, tempS = tempSalto;
    
    public Jugador() {
    	
    }
    
    public Jugador(int x, int y, Juego juego) {
        this.juego = juego;
        perspectiva(juego.getNivel());
        this.x = x;
        this.y = y;
        this.anchoAux=ANCHO;
        this.altoAux=ALTO;
        this.onGround = false;
        this.rep = new ReproductorSonido();
        this.tempSalto=65; temp = tempSalto;
        this.tempCorrer=30; tempC = tempCorrer;
        this.tempAgachado=100; tempA = tempAgachado;
        this.tempDaniado=80; tempD=tempDaniado; daniado=0;
	    rep.cargarSonido("sonidos/saltoMario.wav",false);
    }

    
    
    public void paint(Graphics g) {
    	if(vivo) {
    		if(daniado==0) {

    			 if(agachado&&right){
         			sprite= new ImageIcon(getClass().getResource("/imagenes/personajeAgachadoD.png"));
             	    g.drawImage(sprite.getImage(), x, y+4, ANCHO, ALTO+2, null);
	        	    ultimoLado="Der";
 	    		} else if(agachado&&left) {
         			sprite= new ImageIcon(getClass().getResource("/imagenes/personajeAgachadoI.png"));
             	    g.drawImage(sprite.getImage(), x, y+4, ANCHO, ALTO+2, null);
            	    ultimoLado="Izq";
 	    		} else if(right) {
	    			sprite= new ImageIcon(getClass().getResource("/imagenes/personajeCorriendo1.png"));
	        	    g.drawImage(sprite.getImage(), x, y+4, ANCHO+2+ANCHO/2, ALTO+2, null);
	        	    ultimoLado="Der";
	    		} else if(left) {
        			sprite= new ImageIcon(getClass().getResource("/imagenes/personajeCorriendo2.png"));
            	    g.drawImage(sprite.getImage(), x, y+4, ANCHO+2+ANCHO/2, ALTO+2, null);
            	    ultimoLado="Izq";
	    		} else if(agachado) {
	    			if(ultimoLado=="Der") {
	         			sprite= new ImageIcon(getClass().getResource("/imagenes/personajeAgachadoD.png"));
	             	    g.drawImage(sprite.getImage(), x, y+4, ANCHO, ALTO+2, null);
		    		} else {
	         			sprite= new ImageIcon(getClass().getResource("/imagenes/personajeAgachadoI.png"));
	             	    g.drawImage(sprite.getImage(), x, y+4, ANCHO, ALTO+2, null);
	    			}
	    		}
	    		else {
	    			sprite = new ImageIcon(getClass().getResource("/imagenes/ladronquieto.png")); 
	    			g.drawImage(sprite.getImage(), x, y+4, ANCHO-ANCHO/10, ALTO+2, null);
	    		}
    		}
    		if(daniado==1) {
    			sprite=new ImageIcon(getClass().getResource("/imagenes/personajeDaniado.png"));	
    			tempD--;
    			if(tempD%2==0) {
	    			g.drawImage(sprite.getImage(), x, y+4, ANCHO-ANCHO/10, ALTO+2, null);
    			}
    			if(tempD<=0) {
    				tempD=tempDaniado;
    				daniado=0;
    			}
    		}
    	    moverse();
    	}
    	
    	//g.fillRect(x-auxAncho/2, y-110, auxAncho*3+auxAncho/3, auxAlto);
    }
    
    private int tempCaida=5,tempCaidaAux=tempCaida;
    private int auxAgachado=0;
    
    public void moverse() {
    	//System.out.println("vivo: " + vivo);
    	if(vivo) {
    		if(agachado&&jump) {
    			jump=false;
    		}
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
    			Rectangle2D obstaculoReact = obstaculo.getBounds();
    			Rectangle2D jugadorReact = new Rectangle2D.Double(x+3,y,ANCHO,ALTO);
    			if(jugadorReact.intersects(obstaculoReact)) {
    				right=false;
    				break;
    			}
    		}
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
    			Rectangle2D obstaculoReact = obstaculo.getBounds();
    			Rectangle2D jugadorReact = new Rectangle2D.Double(x-3,y,ANCHO,ALTO);
    			if(jugadorReact.intersects(obstaculoReact)) {
    				left=false;
    				break;
    			}
    		}
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
    			Rectangle2D obstaculoReactArriba = new Rectangle2D.Double(obstaculo.getX(), obstaculo.getY()-8, obstaculo.getAncho(), obstaculo.getAlto()-obstaculo.getAlto()/2-obstaculo.getAlto()/3);
    			Rectangle2D jugadorReact = getBounds();
    			
    			if(jugadorReact.intersects(obstaculoReactArriba)) {
    				onGround=true;
    				vy=auxx;
    				break;
    			} else {
    				onGround=false;
    			}
    		}
    		if(juego.getObstaculos().size()>0) {
	    		for(Obstaculo obstaculo : juego.getObstaculos()) {
	    			Rectangle2D obstaculoReactArriba = obstaculo.getBoundsGolpear();
	    			Rectangle2D jugadorReact = getBounds();
	    			
	    			if(jugadorReact.intersects(obstaculoReactArriba)) {
	    				onGround=false;
	    				jumping=false;
	    				break;
	    			}
	    		}	
    		}
    		
    		if(jumping) {
    			if(vy%2==0) {
    				vy++;		
    			}
    		}
    		
    		
    		if(!onGround&&!jumping) {
    			y+=vy;
    			tempCaida--;
    			if(tempCaida<=0) {
    				vy++;
    				tempCaida=tempCaidaAux;
    			}
    	    	//System.out.println("y: " + y);
    		}
    		
    		//System.out.println("left: " + left);
    		if(left) {
    			x-=vx;
        		//System.out.println("x: " + x);
    		}
    		
    		if(right) {
    			x+=vx;
    		}
    		

    		if (jump && onGround) {
    		    jumping = true;
    		    rep.reproducir(10);
    		}

    		if(jumping) {
    			onGround=false;
    			temp--;
    			y-=vy;
    			if(temp<=0||onGround) {
    				temp=tempSalto;
    				jump=false;
    				jumping=false;
    				vy=auxx;
    			}
    		}
    	}
    	if((x<=0-ANCHO || x>=juego.getTamanioNivelX()+ANCHO)|| y>=juego.getTamanioNivelY()-ALTO) {
    		morir();
    	}
    	
    	if(agachado) {
    		if(juego.getNivel()>=1&&juego.getNivel()<=3) {
    			ALTO=100;
    		} else {
    			ALTO=30;
    		}
    	}
    	
    	if(!onGround&&!agachado) {
        	perspectiva(juego.getNivel());
    	}
    	//System.out.println("left: " + left + " right : " + right + " jump: " + jump + " jumping= " + jumping + " onGround: " + onGround);
    	//if(left||right||jump) {
    		//System.out.println(x);		
    	//}
    	
    	tempAgachado--;
    	
    	//System.out.println("x: " + x + " y: " + y);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) left = true;
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) right = true;
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_W) {
        	jump = true;
        	desagacharse();
        }
        if (key == KeyEvent.VK_ESCAPE) juego.pausa();
        if (key == KeyEvent.VK_E&&onDoor) {
        	if(juego.getNivel()!=3) {
            	juego.siguienteNivel();
        	} else if(juego.getNivel()==3&&juego.getJoya().getConsumido()) {
            	juego.siguienteNivel();
        	}
        }
        if (key == KeyEvent.VK_E&&juego.getJoya().getOnPersonaje()) juego.getJoya().consumir();
        if (key == KeyEvent.VK_S&&tempAgachado<=0) agacharse();
        if (key == KeyEvent.VK_ESCAPE&&juego.getPerder()) ;
        if (key == KeyEvent.VK_R&&juego.getPerder()) ;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) left = false;
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) right = false;
        if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_W) jump = false;
        if (key == KeyEvent.VK_S&&onGround) desagacharse();
    }

    public void resetPosition() {
        x = 50;
        y = 340-getAlto();
        onGround = false;
        revivir();
    }
    
    public void revivir() {
    	vivo=true;
        left=false; 
        right=false; 
        jump=false; 
        jumping=false;
        agachado=false;
        perspectiva(juego.getNivel());
    }
    
    public boolean getIzq() {
    	return left;
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
    
    public int getVy() {
        return vy;
    }

    public boolean getVivo() {
    	return vivo;
    }
    
    public boolean getOnGround() {
        return onGround;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }
    
    public void setVx(int vx) {
        this.vx = vx;
    }

    public void tocaPiso() {
        onGround = true;
    }
    
    public void caer() {
        onGround = false;
    }
    
    
    public boolean getCrecer() {
    	return crecido;
    }
    
    public void morir() {
    	vivo = false;
    }
    
    public void abrir() {
    	onDoor = true;
    }
    
    public void cerrar() {
    	onDoor = false;
    }
    
    public void sobreJoya() {
    	onJoya = true;
    }
    
    public void noSobreJoya() {
    	onJoya = false;
    }
    
    public boolean getOnDoor() {
    	return onDoor;
    }
    
    public void enmascarar() {
    	enmascarado=true;
    }

    public void achicar() {
    	setAncho(30);
    	setAlto(50);
    	enmascarado=false;
    }
    
    public void daniar() {
    	daniado=1;
    }
    
    public int getDaniado() {
		return daniado;
    }
    
    public void nuevasZapas() {
    	tempSalto=40;
    }

    
    public boolean getEnmascarado() {
    	return enmascarado;
    }
    
    public void setTempSalto(int tempSalto) {
    	this.tempSalto = tempSalto;
    }
    
    public void agacharse() {
		agachado=true;
		tempAgachado=tempA;
    }
    
    public void desagacharse() {
    	if(agachado&&onGround) {
    		Rectangle2D jugadorReact = getBoundsChoque(110);
    		for(Obstaculo obstaculo : juego.getObstaculos()) {
        		Rectangle2D obstaculoReact = obstaculo.getBounds();
        		if(obstaculoReact.intersects(jugadorReact)){
        			return;
        		}
    		}
    		if(juego.getNivel()>=1&&juego.getNivel()<=3) {
    			y-=110;
    		} else {
    			y-=25;
    		}
    	}
		agachado=false;
    }
    
    public void perspectiva(int nivel) {
    	if(nivel==1||nivel==2||nivel==3) {
        	setAlto(160);
        	setAncho(80);
        	setVy(3);
        	auxx=3;
        	setTempSalto(45);
        	setVx(4);        	
    	} else if(nivel==4||nivel==5) {
        	setAlto(50);
        	setAncho(30);
        	setTempSalto(30);
        	setVy(3);
        	auxx=3;
        	setVx(4);
    	}
    }
    
    public void noMoverse(){
    	left=false;
    	right=false;
    	jump=false;
    }
    
    public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x, y, ANCHO, ALTO);
	}
    
    private int auxAncho = ANCHO;
    private int auxAlto = ALTO;
    
    public Rectangle2D getBoundsChoque(int loQueSube) {
		return new Rectangle2D.Double(x-auxAncho/2, y-110, auxAncho*3+auxAncho/3, auxAlto);
	}
}
