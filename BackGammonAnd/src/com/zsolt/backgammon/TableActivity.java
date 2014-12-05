package com.zsolt.backgammon;


import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class TableActivity extends SimpleBaseGameActivity{


	private static final int LAYER_COUNT = 5;
	private static final int LAYER_PHYSYCS = 0;
	private static final int LAYER_BACKGROUND = 1;
	private static final int LAYER_STONE = LAYER_BACKGROUND + 1;
	private static final int LAYER_DICE = 4;

	public static final int DICE_TURN = 0;
	public static final int BLACK_TURN = 1;
	public static final int WHITE_TURN = -1;

	int tableState = DICE_TURN;
	private int oldState = BLACK_TURN;
	private Camera mCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mBitmapTextureAtlas2;
	private BitmapTextureAtlas mBitmapTextureAtlas3;
	private BitmapTextureAtlas mBitmapTextureAtlas4;
	private BitmapTextureAtlas mBitmapTextureAtlas5;
	private BitmapTextureAtlas mBitmapTextureAtlas6;
	private TiledTextureRegion mWhiteStoneTextureRegion;
	private TiledTextureRegion mBlackStoneTextureRegion;
	private TiledTextureRegion mMarkedWhiteStoneTextureRegion;
	private TiledTextureRegion mMarkedBlackStoneTextureRegion;
	private TiledTextureRegion mDiceTextureRegion;
	private TiledTextureRegion mDiceRollTextureRegion;
	private BitmapTextureAtlas mBackgroundTexture;
	private ITextureRegion mBackgroundTextureRegion;

	private ColumnList columnList;


	private PhysicsWorld mPhysicsWorld;
	private Dice dice1, dice2;
	private RollDice rollDice;

	private Scene mScene;
	private Player player1, player2;


	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, Constants.CAMERA_WIDTH,
				Constants.CAMERA_HEIGHT);

		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT),
						this.mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	protected void onCreateResources() {

		// TODO Auto-generated method stub
		/* Load all the textures this game needs. */
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);
		this.mBitmapTextureAtlas2 = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);
		this.mBitmapTextureAtlas5 = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);
		this.mBitmapTextureAtlas6 = new BitmapTextureAtlas(
				this.getTextureManager(), 128, 128);
		this.mBitmapTextureAtlas3 = new BitmapTextureAtlas(
				this.getTextureManager(), 1000, 1000);
		this.mBitmapTextureAtlas4 = new BitmapTextureAtlas(
				this.getTextureManager(), 1000, 1000);
		this.mBlackStoneTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"stoneBlack.png", 0, 0, 1, 1);
		this.mWhiteStoneTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas2, this,
						"stoneBig.png", 0, 0, 1, 1);
		this.mMarkedWhiteStoneTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas5, this,
						"stoneBig_transparent.png", 0, 0, 1, 1);
		this.mMarkedBlackStoneTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas6, this,
						"stoneBlack_transparent.png", 0, 0, 1, 1);
		this.mDiceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas3, this,
						"feherkocka.png", 0, 0, 6, 1);
		this.mDiceRollTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas4, this,
						"next.png", 0, 0, 1, 1);

		this.mBitmapTextureAtlas.load();
		this.mBitmapTextureAtlas2.load();
		this.mBitmapTextureAtlas3.load();
		this.mBitmapTextureAtlas4.load();
		this.mBitmapTextureAtlas5.load();
		this.mBitmapTextureAtlas6.load();
		this.mBackgroundTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 2000, 2000);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTexture, this, "Tabla.bmp",
						0, 0);
		this.mBackgroundTexture.load();
	}

	protected Scene onCreateScene() {
		// TODO move this in Table.java
		VertexBufferObjectManager pVertexBufferObjectMAnager = this.getVertexBufferObjectManager();
		this.mScene = new Scene();
		for (int i = 0; i < LAYER_COUNT; i++) {
			this.mScene.attachChild(new Entity());
		}

		/* No background color needed as we have a fullscreen background sprite. */
		this.mScene.setBackgroundEnabled(false);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(
				new Sprite(0, 0, this.mBackgroundTextureRegion,
						pVertexBufferObjectMAnager));
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_JUPITER), false);

		final VertexBufferObjectManager vertexBufferObjectManager = pVertexBufferObjectMAnager;
		final Rectangle ground = new Rectangle(0, Constants.CAMERA_HEIGHT - 2,
				Constants.CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, Constants.CAMERA_WIDTH, 2,
				vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, Constants.CAMERA_HEIGHT,
				vertexBufferObjectManager);
		final Rectangle right = new Rectangle(Constants.CAMERA_WIDTH - 2, 0, 2,
				Constants.CAMERA_WIDTH, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0.5f,
				0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right,
				BodyType.StaticBody, wallFixtureDef);

		this.mScene.getChildByIndex(LAYER_PHYSYCS).attachChild(ground);
		this.mScene.getChildByIndex(LAYER_PHYSYCS).attachChild(roof);
		this.mScene.getChildByIndex(LAYER_PHYSYCS).attachChild(left);
		this.mScene.getChildByIndex(LAYER_PHYSYCS).attachChild(right);

		this.mScene.registerUpdateHandler(this.mPhysicsWorld);


		dice1 = new Dice(100f, 100f, this.mDiceTextureRegion,
				pVertexBufferObjectMAnager);

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1,
				0.5f, 0.5f);
		final Body body = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
				dice1, BodyType.DynamicBody, objectFixtureDef);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dice1,
				body, true, true));
		dice1.setUserData(body);
		this.mScene.getChildByIndex(LAYER_DICE).attachChild(dice1);

		dice2 = new Dice(100f, 100f, this.mDiceTextureRegion,
				pVertexBufferObjectMAnager);
		final FixtureDef objectFixtureDef2 = PhysicsFactory.createFixtureDef(1,
				0.5f, 0.5f);
		final Body body2 = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
				dice2, BodyType.DynamicBody, objectFixtureDef2);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dice2,
				body2, true, true));
		dice2.setUserData(body2);

		this.mScene.getChildByIndex(LAYER_DICE).attachChild(dice2);

		rollDice = new RollDice(0f, 190f, this.mDiceRollTextureRegion,
				pVertexBufferObjectMAnager, this);
		rollDice.addDices(dice1, dice2);
		this.mScene.registerTouchArea(rollDice);
		this.mScene.setOnAreaTouchListener(rollDice);
		this.mScene.getChildByIndex(LAYER_DICE).attachChild(rollDice);


		ArrayList<Stone> markerStones = new ArrayList<Stone>(52);
		int i;
		for (i = 0; i < 26; i++) {
			Stone stone = new MarkerStone(player1, Constants.colorType.COLOR_WHITE, 0,
					0, this.mMarkedWhiteStoneTextureRegion,
					pVertexBufferObjectMAnager);
			this.mScene.getChildByIndex(LAYER_STONE).attachChild(stone);
			stone.setVisible(false);
			markerStones.add(stone);
		}
		for (i = 0; i < 26; i++) {
			Stone stone = new MarkerStone(player2, Constants.colorType.COLOR_WHITE, 0,
					0, this.mMarkedBlackStoneTextureRegion,
					pVertexBufferObjectMAnager);
			this.mScene.getChildByIndex(LAYER_STONE).attachChild(stone);
			stone.setVisible(false);
			markerStones.add(stone);
		}
		columnList = new ColumnList(markerStones);

		this.player1 = new HumanPlayer(this,columnList,rollDice,Constants.colorType.COLOR_WHITE);
		this.player2 = new ComputerPlayer(this,columnList,rollDice,Constants.colorType.COLOR_BLACK);

		Stone WhiteStoneVekt[];
		Stone BlackStoneVekt[];

		WhiteStoneVekt = player1.StoneFactory(mScene, mWhiteStoneTextureRegion, pVertexBufferObjectMAnager, LAYER_STONE, Constants.colorType.COLOR_WHITE);
		BlackStoneVekt = player2.StoneFactory(mScene, mBlackStoneTextureRegion, pVertexBufferObjectMAnager, LAYER_STONE, Constants.colorType.COLOR_BLACK);
		
		// TODO: abstractize this
		for (i = 0; i < 5; i++) {
			this.addStone(11, BlackStoneVekt[i]);
			this.addStone(12, WhiteStoneVekt[i]);
		}
		for (i = 5; i < 8; i++) {
			this.addStone(16, BlackStoneVekt[i]);
			this.addStone(7, WhiteStoneVekt[i]);
		}
		for (i = 8; i < 13; i++) {
			this.addStone(18, BlackStoneVekt[i]);
			this.addStone(5, WhiteStoneVekt[i]);
		}
		for (i = 13; i < 15; i++) {
			this.addStone(0, BlackStoneVekt[i]);
			this.addStone(23, WhiteStoneVekt[i]);
		}

		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);


		return this.mScene;

	}

	public TableActivity() {
	}

	public void addStone(int columnID, Stone stone) {
		columnList.get(columnID).addStone(stone);
	}




	public int getTableState()
	{
		return tableState;
	}
	public void onDiceRolled()
	{
		if (this.oldState == WHITE_TURN)
		{
			changeTableStateBlack();
			player2.onDiceRolled();
		}
		else
		{
			changeTableStateWhite();
			player1.onDiceRolled();
		}

	}

	public void onDicesConsumed()
	{
		this.oldState = this.tableState;
		this.tableState = DICE_TURN;
		if(oldState == WHITE_TURN)
		{
			this.toast("Black player, please roll the dice");
			player2.onDicesConsumed();
		}
		else 
		{
			this.toast("White player, please roll the dice");
			player1.onDicesConsumed();
		}
	}
	private void changeTableStateBlack()
	{
		this.tableState = BLACK_TURN;
		this.toast("Black player turn");



		//if (rollDice.isDouble() )
		//		computer.move(4);
		//	else
		//	computer.move(2);
	}
	private void changeTableStateWhite()
	{
		this.tableState = WHITE_TURN;
		this.toast("White player turn");
	}



	private void log(final String pMessage) {
		Debug.d(pMessage);
	}
	public void toast(final String pMessage) {
		this.log(pMessage);
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(TableActivity.this, pMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void waitSeconds(final Player player,float sec)
	{
		mScene.registerUpdateHandler(new TimerHandler( sec, false, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				player.onTimePassed();
			}
		}));
	}

}
