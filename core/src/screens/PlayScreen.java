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

		world = new World(new Vector2(0, -10), true);
		player = new Samus(world);
		b2dr = new Box2DDebugRenderer();

		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;

		// Ground

		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / SuperMetroid.PPM,
					(rect.getY() + rect.getHeight() / 2) / SuperMetroid.PPM);
			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / SuperMetroid.PPM, rect.getHeight() / 2 / SuperMetroid.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}

		// Spikes

		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / SuperMetroid.PPM,
					rect.getY() + rect.getHeight() / 2);
			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / SuperMetroid.PPM, rect.getHeight() / 2 / SuperMetroid.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}

		// MUSICA

		// music = SuperMetroid.manager.get("assets/musica/fondoDos.mp3", Music.class);
		// music.setLooping(true);
		// music.setVolume(0.3f);
		// music.play();
	}

	@Override
	public void show() {

	}

	public void handleInput(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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
		// TODO Auto-generated method stub

	}

}