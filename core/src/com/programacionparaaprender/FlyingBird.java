package com.programacionparaaprender;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

class MyController  implements InputProcessor {
	public boolean touching;
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touching = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		touching = true;
		//System.out.println("Se esta pulsando tecla. "+touching);
		return true;
	}
	//.. more methods etc

}


public class FlyingBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, bird;
	float bw,bh,sw,sh,bx,by;
	float velocity = 0.0f;
	float gravity = 0.1f;
	int state = 0;
	MyController controller;
	Texture bee1, bee2, bee3;
	float bee1x,bee1y,bee2x,bee2y,bee3x,bee3y;
	int nbees = 3;
	float[] beesx = new float[nbees];
	float[][] beesy = new float[3][nbees];
	float bee4y;
	ShapeRenderer sr;
	Circle c_bird, c_bee1[], c_bee2[], c_bee3[];

	int score = 0;
	Boolean flag = true;
	Boolean flag1 = true;
	BitmapFont font, font1;
	Sound sound;

	@Override
	public void create () {
		controller = new MyController();
		Gdx.input.setInputProcessor(controller);
		batch = new SpriteBatch();
		img = new Texture("backdrop.png");
		bird = new Texture("frame-1.png");
		bee1 = new Texture("skeleton-01_fly_00.png");
		bee2 = new Texture("skeleton-01_fly_00.png");
		bee3 = new Texture("skeleton-01_fly_00.png");
		bw = 0;
		bh = Gdx.graphics.getHeight()/11;
		sw = Gdx.graphics.getWidth()/13;
		sh = Gdx.graphics.getHeight()/11;
		bx = Gdx.graphics.getWidth()/4;
		by = Gdx.graphics.getHeight()/2;
		bee1x = Gdx.graphics.getWidth();
		bee1y = Gdx.graphics.getHeight()/2;
		bee2x = Gdx.graphics.getWidth();
		bee2y = Gdx.graphics.getHeight()/3;
		bee3x = Gdx.graphics.getWidth();
		bee3y = Gdx.graphics.getHeight()/4;
		bee4y = Gdx.graphics.getHeight();
		/*img.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {

			}
		});
		*/
		sr = new ShapeRenderer();
		c_bird = new Circle();
		c_bee1 = new Circle[nbees];
		c_bee2 = new Circle[nbees];
		c_bee3 = new Circle[nbees];

		sound = Gdx.audio.newSound(Gdx.files.internal("burro.wav"));

		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(8);

		font1 = new BitmapFont();
		font1.setColor(Color.CYAN);
		font1.getData().setScale(10);
		for(int i = 0; i < nbees; i++){
			beesx[i] = bee2x + i * bee2x / 2;
			Random r1 = new Random();
			Random r2 = new Random();
			Random r3 = new Random();
			beesy[0][i] = r1.nextFloat() * bee4y;
			beesy[1][i] = r2.nextFloat() * bee4y;
			beesy[2][i] = r3.nextFloat() * bee4y;

			c_bee1[i]=new Circle();
			c_bee2[i]=new Circle();
			c_bee3[i]=new Circle();
		}
	}

	@Override
	public void render () {
		renderCirculos();
	}

	public void renderCirculos () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(bird, bx, by, sw, sh);

		//batch.draw(bee1, bee1x, bee1y, sw, sh);
		//batch.draw(bee2, bee2x, bee2y, sw, sh);
		//batch.draw(bee3, bee3x, bee3y, sw, sh);
		for(int i = 0; i < nbees; i++){
			if(beesx[i] < 0){
				flag = true;

				beesx[i] = bee2x + i * bee2x / 2;
				Random r1 = new Random();
				Random r2 = new Random();
				Random r3 = new Random();
				beesy[0][i] = r1.nextFloat() * bee4y;
				beesy[1][i] = r2.nextFloat() * bee4y;
				beesy[2][i] = r3.nextFloat() * bee4y;
			}
			font.draw(batch,String.valueOf(score),sw-bw, bh);
			if(beesx[i]< bx && flag){
				flag = false;
				score++;
				System.out.println(score);
			}

			batch.draw(bee1, beesx[i], beesy[0][i], sw, sh);
			batch.draw(bee2, beesx[i], beesy[1][i], sw, sh);
			batch.draw(bee3, beesx[i], beesy[2][i], sw, sh);
		}

		switch(state){
			case 0:
				font1.draw(batch, "Top to screen to start!",0,sh*2);
				if(controller.touching){
					state = 1;
				}
				break;
			case 1:
				flag1 = true;
				by = by - 4;

				if(controller.touching){
					System.out.println("Se esta pulsando tecla. ");
					velocity= -40;
					by+=40;

				}
				c_bird.set(bx+sw/2, by+sh/2,sw/2);
				for(int i = 0; i < nbees; i++){
					c_bee1[i].set(beesx[i]+sw/2, beesy[0][i]+sh/2,sw/2);
					c_bee2[i].set(beesx[i]+sw/2, beesy[1][i]+sh/2,sw/2);
					c_bee3[i].set(beesx[i]+sw/2, beesy[2][i]+sh/2,sw/2);
					if(Intersector.overlaps(c_bird, c_bee1[i]) || Intersector.overlaps(c_bird, c_bee2[i]) || Intersector.overlaps(c_bird, c_bee3[i])){
						state = 2;
					}
					beesx[i] = beesx[i] - 4;

				}

				if(by < bh) {
					by = Gdx.graphics.getHeight() / 3;
					state = 2;
				}
				if(by > Gdx.graphics.getHeight()){
					by = Gdx.graphics.getHeight() - sh;
				}
				break;
			case 2:
				font1.draw(batch, "You lost! Tap to screen to try again!",0,sh*2);
				if(flag1){
					sound.play();
					flag1 = false;
				}

				if(controller.touching){
					state = 1;
					score = 0;
					bx = Gdx.graphics.getWidth()/4;
					by = Gdx.graphics.getHeight()/2;
					for(int i = 0; i < nbees; i++){
						beesx[i] = bee2x + i * bee2x / 2;
						Random r1 = new Random();
						Random r2 = new Random();
						Random r3 = new Random();
						beesy[0][i] = r1.nextFloat() * bee4y;
						beesy[1][i] = r2.nextFloat() * bee4y;
						beesy[2][i] = r3.nextFloat() * bee4y;

						c_bee1[i]=new Circle();
						c_bee2[i]=new Circle();
						c_bee3[i]=new Circle();
					}
				}
				break;
			default:
				/*if(controller.touching){
					state = 1;
				}*/
				break;
		}

		batch.end();
	}


	public void renderCirculosShapeRender () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(bird, bx, by, sw, sh);

		//batch.draw(bee1, bee1x, bee1y, sw, sh);
		//batch.draw(bee2, bee2x, bee2y, sw, sh);
		//batch.draw(bee3, bee3x, bee3y, sw, sh);
		for(int i = 0; i < nbees; i++){
			if(beesx[i] < 0){
				beesx[i] = bee2x + i * bee2x / 2;
				Random r1 = new Random();
				Random r2 = new Random();
				Random r3 = new Random();
				beesy[0][i] = r1.nextFloat() * bee4y;
				beesy[1][i] = r2.nextFloat() * bee4y;
				beesy[2][i] = r3.nextFloat() * bee4y;
			}
			batch.draw(bee1, beesx[i], beesy[0][i], sw, sh);
			batch.draw(bee2, beesx[i], beesy[1][i], sw, sh);
			batch.draw(bee3, beesx[i], beesy[2][i], sw, sh);
		}



		//batch.draw(bird, 0, 0,Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/2);
		//batch.draw(bird, 0, 0, sw, sh);

		switch(state){
			case 1:
				by = by - 4;
				//bee1y--;
				//bee2y--;
				//bee3y--;

				//bee1x--;
				//bee2x--;
				//bee3x--;
				/*by = velocity +gravity;
				by = by -velocity;
				*/
				if(controller.touching){
					System.out.println("Se esta pulsando tecla. ");
					velocity= -40;
					by+=40;
					//bee1y+=40;
					//bee2y+=40;
					//bee3y+=40;
					//user is touching do touching action
				}else{
					// user isn't touching do the non touchin action
				}

				sr.begin(ShapeRenderer.ShapeType.Filled);

				for(int i = 0; i < nbees; i++){
					sr.setColor(Color.BLUE);
					// al colocar los beesx[i]+sw/2, beesy[0][i]+sh/2 se tapa la imagen del bee
					sr.circle(beesx[i]+sw/2, beesy[0][i]+sh/2,sw/2);
					sr.circle(beesx[i]+sw/2, beesy[1][i]+sh/2,sw/2);
					sr.circle(beesx[i]+sw/2, beesy[2][i]+sh/2,sw/2);
					sr.circle(bx+sw/2, by+sh/2,sw/2);

					beesx[i] = beesx[i] - 4;

				}
				sr.end();

				/*if (Gdx.input.justTouched()) {
					velocity= -20;
				}*/

				/*if (Gdx.input.getPressure()==0){
					//do something-else;
					velocity= -20;
				}*/
				if(by < bh) {
					by = Gdx.graphics.getHeight() / 3;
					state = 2;
				}
				if(by > Gdx.graphics.getHeight()){
					by = Gdx.graphics.getHeight() - sh;
				}
				break;
			default:
				if(controller.touching){
					state = 1;
				}
				break;
		}

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
