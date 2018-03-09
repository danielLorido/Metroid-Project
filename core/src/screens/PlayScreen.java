package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.metroidproyect.game.SuperMetroid;

import Personaje.Samus;
import Util.B2WorldCreator;
import hud.Hud;

public class PlayScreen implements Screen {
	private Hud hud;
	private SuperMetroid game;
	private OrthographicCamera camera;
	private Viewport myViewPort;
	private Samus player;

	// tiled variables
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private Music music;

	// Box2D Variables
	World world;
	Box2DDebugRenderer b2dr;

	public PlayScreen(SuperMetroid game) {
		this.game = game;
		camera = new OrthographicCamera();
		myViewPort = new FitViewport(SuperMetroid.VIRTUAL_WIDTH / SuperMetroid.PPM,
				SuperMetroid.VIRTUAL_HEIGHT / SuperMetroid.PPM, camera);
		hud = new Hud(game.batch);

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("mapa/mapabueno.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / SuperMetroid.PPM);
		camera.position.set(myViewPort.getWorldWidth() / 2, myViewPort.getWorldHeight() / 2, 0);

		world = new World(new Vector2(0, -20), true);
		player = new Samus(world);
		b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, map);

		// MUSICA

		music = Gdx.audio.newMusic(Gdx.files.internal("musica/fondoUno.mp3"));
		music.setLooping(true);
		music.setVolume(0.3f);
		music.play();
	}

	@Override
	public void show() {

	}

	public void handleInput(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.b2body.getPosition().y <= 0.7) {
			player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2) {
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2) {
			player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

		}

	}

	private void update(float delta) {
		this.handleInput(delta);
		world.step(1 / 60f, 6, 2);
		camera.position.x = player.b2body.getPosition().x;
		camera.update();
		renderer.setView(camera);
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();

		b2dr.render(world, camera.combined);

		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		// game.batch.begin();
		// game.batch.draw(texture, 0, 0);
		// game.batch.end();

	}

	@Override
	public void resize(int width, int height) {
		myViewPort.update(width, height);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}

}