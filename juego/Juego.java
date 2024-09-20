package juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Juego extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Jugador jugador;
    private List<Obstaculo> obstaculos;
    private List<ObjetoRecompensa> objetosRec;
    private List<Enemigo> enemigos;
    private Camara camera;
    private int tamanioNivelX;
    private int tamanioNivelY;
    private int nivel=1;
    private int niveles=5;
    private int aux=0;
    private ImageIcon fondo;
    private ReproductorSonido rep;
    private Meta meta=new Meta(1000,10,this);
    private boolean pausa=false;
    private boolean menu=true;
    private boolean perder=false;
    private Font fuenteRetro;
    private static Font retro;
    private static Font retroTitulo;
    private static Font retroPerder;
    private static Font retroChi;
    private int tiempo=0;

    public Juego() {
        this.setFocusable(true);
        this.addKeyListener(this);
        this.tamanioNivelX = 2400; 
        this.tamanioNivelY = 450;
        Jugador ju=new Jugador();
        this.jugador = new Jugador(tamanioNivelX / 100, 380-ju.getAlto(), this);
        this.camera = new Camara(0, 0, 800, 400, tamanioNivelX);
        this.obstaculos = new ArrayList<>();
        this.objetosRec = new ArrayList<>();
        this.enemigos= new ArrayList<>();
        //this.fondo = new ImageIcon(getClass().getResource("/imagenes/escenariodeciudadnivel2.png"));
        this.rep = new ReproductorSonido();
        timer = new Timer(5, this);
        timer.start();
        inicializarFuentes();
    }

    @Override
    public void paintComponent(Graphics g) {
    	tiempo++;
    	g.setFont(fuenteRetro);
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        Image fondoImg = fondo.getImage();
        int fondoWidth = fondoImg.getWidth(null);
        int fondoHeight = fondoImg.getHeight(null);

        int cameraX = camera.getX();
        int cameraY = 0; 
        int viewWidth = camera.getViewWidth(); 
        int viewHeight = camera.getViewHeight();

        int numRepeticionesX = (int) Math.ceil((double) tamanioNivelX / fondoWidth);

        for (int i = 0; i < numRepeticionesX; i++) {
            int x = i * fondoWidth - cameraX;
            g.drawImage(fondoImg, x, cameraY, fondoWidth, viewHeight, this);
        }
        
        g2d.translate(-camera.getX(), 0); 


        perder(g);
        
        meta.paint(g);
        
        jugador.paint(g2d);
        for (Obstaculo obstaculo : obstaculos) {
            obstaculo.paint(g2d);
        }
        for (ObjetoRecompensa objetoRec : objetosRec) {
        	objetoRec.paint(g2d);
        }
        for (Enemigo enemigo : enemigos) {
            enemigo.paint(g2d);
        }

        g.setFont(retro);
        
        if(menu) {
        	int size = 30;
            //g.drawString(" Presione cualquier tecla para iniciar ", camera.getViewHeight()/5,camera.getViewHeight()/2+camera.getViewHeight()/5);
            drawTextWithOutline(g, "Presione cualquier tecla para iniciar. " , ((camera.getViewWidth()/3-size*7))+camera.getX(), (camera.getViewHeight()/2+camera.getViewHeight()/3), Color.BLACK, Color.YELLOW,retroChi);
        }
        
        //if(getJugador().getX()>=tamanioNivelX&&nivel>0&&nivel<niveles) {
        	//siguienteNivel();
        //}
        
    }
    private boolean auxi=true;
    @Override
    public void actionPerformed(ActionEvent e) {
        camera.update(jugador.getX());
        for(int x=0;x<obstaculos.size();x++) {
        	obstaculos.get(x).choque(jugador);
        }
        loadWorld();
        repaint();
    }

    public void siguienteNivel() {
    	nivel++;
    	getJugador().revivir();
    	aux=0;
    	resetGame();
    }
    
    private void resetGame() {
        jugador.resetPosition();
        camera.reset();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
        menu=false;
        if(key == KeyEvent.VK_R&&!jugador.getVivo()) {
        	aux=0;
        	nivel=1;
        	jugador.revivir();
        }
        if(key == KeyEvent.VK_ESCAPE&&!jugador.getVivo()) {
        	System.exit(0);
        }
        jugador.keyPressed(e);
        
        //System.out.println(menu);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        jugador.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public List<Obstaculo> getObstaculos() {
        return obstaculos;
    }
    
    public List<Enemigo> getEnemigos() {
        return enemigos;
    }
    
    
    
    public Jugador getJugador() {
        return jugador;
    }
    
    private boolean ganar=false;
    
    public void perder(Graphics g) {
    	
        g.setFont(retro);
    	
        if(getJugador().getX()>=tamanioNivelX&&nivel==niveles) {
        	
        	int size = 30;
        	
        	getJugador().noMoverse();
        	
        	if(!ganar) {
        		
        		ganar=true;
	        	rep.detener();
	        	rep.cargarSonido("sonidos/ganar.wav", false);
	        	rep.reproducir(30);
	        	
        	}
        	
            g.setColor(Color.GREEN);
            drawTextWithOutline(g, "¡ Ganaste !" , ((camera.getViewWidth()/3-size*7))+camera.getX(), (camera.getViewHeight()/3), Color.BLACK, Color.GREEN,retroTitulo);
        
            drawTextWithOutline(g, "Presione [ESC] para cerrar el juego." , ((camera.getViewWidth()/3-size*6))+camera.getX(), camera.getViewHeight()/2+camera.getViewHeight()/3
            		, Color.BLACK, Color.GREEN,retroChi);
            drawTextWithOutline(g, "Presione [R] para reiniciar el juego." , ((camera.getViewWidth()/3-size*6))+camera.getX(), camera.getViewHeight()/2+camera.getViewHeight()/5, Color.BLACK, Color.GREEN,retroChi);
        
        }
        
        if(nivel!=0&&!menu&&!ganar) {
	        g.setColor(Color.white);
	        g.drawString("Nivel " + nivel,100,150);
	        drawTextWithOutline(g, "Nivel " + nivel ,100,150, Color.BLACK, Color.YELLOW,retro);
	        
        }
        
        if(!getJugador().getVivo()&&nivel!=niveles+1&&!ganar) {
        	
        	//System.out.println("vivo: " + jugador.getVivo() + "nivel: " + nivel);
        	obstaculos.clear();
        	enemigos.clear();
        	meta.destruirse();
        	
        	int size = 84;
        	//g.setColor(Color.RED);
            //g.drawString("¡ Perdiste ! ", ((camera.getViewWidth()/2-size*2)-size/3)+camera.getX(), camera.getViewHeight()/4);
            size=30;
            //g.drawString("Presione [ESC] para volver al menu.", ((camera.getViewWidth()/3-size*2+size/2)-size/3)+camera.getX(), camera.getViewHeight()/2+camera.getViewHeight()/6);
            drawTextWithOutline(g, "¡ Perdiste !" , ((camera.getViewWidth()/3-size*8))+camera.getX(), (camera.getViewHeight()/3), Color.BLACK, Color.RED, retroTitulo);
            drawTextWithOutline(g, "Presione [ESC] para cerrar el juego." , ((camera.getViewWidth()/3-size*6))+camera.getX(), camera.getViewHeight()/2+camera.getViewHeight()/3
            		, Color.BLACK, Color.RED,retroChi);
            drawTextWithOutline(g, "Presione [R] para reiniciar el juego." , ((camera.getViewWidth()/3-size*6))+camera.getX(), camera.getViewHeight()/2+camera.getViewHeight()/5, Color.BLACK, Color.RED,retroChi);
        }

        
    	if(!getJugador().getVivo()&&auxi&&!ganar) {
        	rep.detener();
            rep.cargarSonido("sonidos/gameOver.wav", true);
            rep.reproducir(15);
            auxi=false;
    	}
    	
    	
    }
    
    public void pausa() {
    	if(pausa) {
    		pausa=false;
    	} else {
    		pausa=true;
    	}
    }
    
    public boolean getPerder() {
    	return perder;
    }
    
    public int getTamanioNivelX() {
    	return tamanioNivelX;
    }
    
    public int getTamanioNivelY() {
    	return tamanioNivelY;
    }
    
    public boolean getMenu() {
    	return menu;
    }
    
    public int getNivel() {
    	return nivel;
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Super Chorro Bros");
        Juego game = new Juego();

        frame.setSize(800, 430);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private int unaVez=0;
    
    private void loadWorld() {
    	
    	if(nivel==1&&aux==0) {
    		inicializarNivel("/imagenes/escenarionivel1piso1.png",1080);

    		obstaculos.add(new Obstaculo(-50, 30, false, nivel));
    		obstaculos.add(new Obstaculo(-50, 110, false, nivel));
    		obstaculos.add(new Obstaculo(-50, 210, false, nivel));
    		obstaculos.add(new Obstaculo(-50, 310, false, nivel));
    		obstaculos.add(new Obstaculo(-50, 410, false, nivel));
    		obstaculos.add(new Obstaculo(-50, 510, false, nivel));
    		
    		for(int x=0;x<tamanioNivelX/40;x++) {
    			obstaculos.add(new Obstaculo(x*40,380,false,nivel));
    		}
    		
    		obstaculos.add(new Obstaculo(1070, 30, false, nivel));
    		obstaculos.add(new Obstaculo(1070, 110, false, nivel));
    		obstaculos.add(new Obstaculo(1070, 210, false, nivel));
    		obstaculos.add(new Obstaculo(1070, 310, false, nivel));
    		obstaculos.add(new Obstaculo(1070, 410, false, nivel));
    		obstaculos.add(new Obstaculo(1070, 510, false, nivel));
    		
    		enemigos.add(new Enemigo(300, 200, TipoEnemigo.ANCIANA, false, this)); 
            enemigos.add(new Enemigo(500, 200, TipoEnemigo.ANCIANA, true, this));
            enemigos.add(new Enemigo(700, 200, TipoEnemigo.ANCIANA, false, this));
            enemigos.add(new Enemigo(900, 200, TipoEnemigo.ANCIANA, true, this));
            
    		obstaculos.add(new Obstaculo(200, 340, false,nivel));
    		//enemigos.add(new Enemigo(500, 330, true,  this));
    		//enemigos.add(new Enemigo(620, 330, false, this));
    		//obstaculos.add(new Obstaculo(960, 340, false,nivel));

    		meta = new Meta(760,150,this);
    		
    		
    		for(int x=0;x<enemigos.size();x++) {
    			enemigos.get(x);
    		}
    		
    	
    	} else if(nivel==2&&aux==0) {
    		
    		
    		inicializarNivel("/imagenes/escenarionivel1piso2.png", 1080);
    		for(int x=0;x<tamanioNivelX/40;x++) {
    			obstaculos.add(new Obstaculo(x*40,380,false,nivel));
    		}
    		meta = new Meta(760,150,this);
    		
    		
    	} else if(nivel==3&&aux==0) {
    		
    		
    		inicializarNivel("/imagenes/escenarionivel1piso3.png", 1080);
    		for(int x=0;x<tamanioNivelX/40;x++) {
    			obstaculos.add(new Obstaculo(x*40,380,false,nivel));
    		}
    		meta = new Meta(760,150,this);
    		
    		
    	}
    	else if(nivel==4&&aux==0) {

    		
    		inicializarNivel("/imagenes/escenariodeciudadnivel2.png",4000);
    		
    		for(int x=0;x<tamanioNivelX/40;x++) {
    			if(x==10||x==11||x==12||x==25||x==26||x==27||x==28||x==29||x>60) {
    				
    			} else {
    				obstaculos.add(new Obstaculo(x*40,380,false,nivel));		
    			}
    			if(x==5) {
    				obstaculos.add(new BloqueRecompensa(x*10,240,true,this));
    			}
    		}
    		for(int x=1;x<4;x++) {
				obstaculos.add(new Obstaculo(-40,340-x*40,false,nivel));	
    		}
    		
    		obstaculos.add(new Obstaculo(520, 340, false,nivel));
    		enemigos.add(new Enemigo(580, 310, TipoEnemigo.POLICIA, true, this));
    		obstaculos.add(new Obstaculo(720, 340, false,nivel));

    		obstaculos.add(new Obstaculo(1400, 340, false,nivel));
    		enemigos.add(new Enemigo(1440, 330, TipoEnemigo.POLICIA, true,  this));
    		enemigos.add(new Enemigo(1620, 330, TipoEnemigo.POLICIA, true, this));
    		obstaculos.add(new Obstaculo(1960, 340, false,nivel));
    		
    		for(int x=0;x<3;x++) {
    			for(int y=0;y<3;y++) {
    				if((y==2&&x==1)||(y==2&&x==0)||(y==1&&x==0)) {
    					
    				} else {
    					obstaculos.add(new Obstaculo(880+x*40,340-y*40,false,nivel));						
    				}
    			}
    		}
    		
    		for(int x=0;x<3;x++) {
    			for(int y=0;y<3;y++) {
    				if((x==1&&y==0)||(x==2&&y==0)||(x==2&&y==1)) {
    					obstaculos.add(new Obstaculo(2320+x*40,340-y*40,false,nivel));	
    				}
    			}
    		}

			obstaculos.add(new Obstaculo(2320+4*40,260,false,nivel));	
			obstaculos.add(new Obstaculo(2320+5*40,260,false,nivel));
			obstaculos.add(new Obstaculo(2320+8*40,200,false,nivel));
			
			obstaculos.add(new Obstaculo(2320+11*40,260,false,nivel));
			obstaculos.add(new Obstaculo(2320+12*40,260,false,nivel));
			
			
			obstaculos.add(new Obstaculo(2320+16*40,260,false,nivel));
			
			obstaculos.add(new Obstaculo(2320+18*40,300,false,nivel));
			obstaculos.add(new Obstaculo(2320+19*40,300,false,nivel));
			
			obstaculos.add(new Obstaculo(2320+25*40,300,false,nivel));
			obstaculos.add(new Obstaculo(2320+26*40,300,false,nivel));
			
			for(int x=30;x<=38;x++) {
				if(x==30||x==38) {
					obstaculos.add(new Obstaculo(2320+x*40,280,false,nivel));
				}
				obstaculos.add(new Obstaculo(2320+x*40,320,false,nivel));
			}
			
			Enemigo enemi = new Enemigo();
			enemigos.add(new Enemigo(2320+35*40,280+enemi.getAlto(), TipoEnemigo.POLICIA, true,this));
			enemigos.add(new Enemigo(2320+35*40,300+enemi.getAlto(), TipoEnemigo.POLICIA, true,this));
			enemigos.add(new Enemigo(2320+35*40,320+enemi.getAlto(), TipoEnemigo.POLICIA, true,this));

			meta = new Meta(2320+38*36,380-meta.getAlto(),this);
    		
            //enemigos.add(new Enemigo(700, 310, false, this));
            //enemigos.add(new Enemigo(1500, 310, false, this));
            //enemigos.add(new Enemigo(2000, 310, false, this));

    		//System.out.println("x: " + jugador.getX() + "y: " + jugador.getY());

    	} else if(nivel==5&&aux==0) {

    		inicializarNivel("/imagenes/escenariodeciudadnivel2.png",3000);

    		for(int x=0;x<tamanioNivelX/40;x++) {
    			obstaculos.add(new Obstaculo(x*40,380,false,nivel));
    		}
    		enemigos.add(new Enemigo(400, 310, TipoEnemigo.ANCIANA, true, this));  
            enemigos.add(new Enemigo(700, 310, TipoEnemigo.ANCIANA, true, this)); 
            enemigos.add(new Enemigo(1500, 310, TipoEnemigo.ANCIANA, true, this));
            enemigos.add(new Enemigo(2000, 310, TipoEnemigo.ANCIANA, true, this));
            
    	}
    	
    	if(unaVez==0&&nivel==2&&getJugador().getVivo()&&getJugador().getX()>2100) {
    		enemigos.add(new Enemigo(2480,190, TipoEnemigo.POLICIA, true,this));
    		unaVez=1;
    	}
    	
    	aux=1;
    }
    
    private void drawTextWithOutline(Graphics g, String text, int x, int y, Color outlineColor, Color textColor, Font font) {
        
        Color originalColor = g.getColor();

        g.setFont(font);
        
        g.setColor(outlineColor);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    g.drawString(text, x + i, y + j);
                }
            }
        }


        g.setColor(textColor);
        g.drawString(text, x, y);


        g.setColor(originalColor);
    }
    
    
    
    public void inicializarFuentes() {

    	try {
            InputStream is = getClass().getResourceAsStream("/fuentes/PressStart2P-Regular.ttf");
            if (is == null) {
                throw new IOException("Fuente no encontrada");
            }
            retro = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(30f); 
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retro = new Font("Monospaced", Font.PLAIN, 16); 
        }
    	
    	try {
            InputStream is = getClass().getResourceAsStream("/fuentes/PressStart2P-Regular.ttf");
            if (is == null) {
                throw new IOException("Fuente no encontrada");
            }
            retroChi = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f); 
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retroChi = new Font("Monospaced", Font.PLAIN, 16); 
        }
    	

    	try {
            InputStream is = getClass().getResourceAsStream("/fuentes/PressStart2P-Regular.ttf");
            if (is == null) {
                throw new IOException("Fuente no encontrada");
            }
            retroTitulo = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(60f); 
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retroTitulo = new Font("Monospaced", Font.PLAIN, 16); 
        }
    	

    	try {
            InputStream is = getClass().getResourceAsStream("/fuentes/PressStart2P-Regular.ttf");
            if (is == null) {
                throw new IOException("Fuente no encontrada");
            }
            retroPerder = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f); 
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retroPerder = new Font("Monospaced", Font.BOLD, 16); 
        }
    }
    
    public void inicializarNivel(String path, int tamanioNivel) {
		if(nivel==1) {
    		rep.detener();
            rep.cargarSonido("sonidos/soundtrack.wav",true);
            rep.reproducir(15);
		}
    	jugador.resetPosition();
		fondo = new ImageIcon(getClass().getResource(path));
		unaVez=0;
        auxi=true;
		obstaculos.clear();
		enemigos.clear();
		objetosRec.clear();
    	tamanioNivelX=tamanioNivel;
		camera = new Camara(0, 0, 800, 400, tamanioNivelX);
    }
}
