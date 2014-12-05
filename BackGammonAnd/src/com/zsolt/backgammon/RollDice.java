package com.zsolt.backgammon;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RollDice extends Sprite implements IOnAreaTouchListener {
	private int dice1ConsumedCount = 2;
	private int dice2ConsumedCount = 2;
	ArrayList<Dice> dices = new ArrayList<Dice>(2);
	private int finishedCount =0;
	private TableActivity table;

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final ITouchArea pTouchArea, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()) {
			if (table.getTableState() == TableActivity.DICE_TURN)
			{
				
				this.rollDices();
				
				return true;
			}
		}

		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void rollDices() {
		finishedCount = 0;
		final Body faceBody = (Body) dices.get(0).getUserData();
		final Body faceBody2 = (Body) dices.get(1).getUserData();
		Random randomGenerator = new Random();
		
		int i;
		i= randomGenerator.nextInt(6);
		int j;
		j= randomGenerator.nextInt(6);
		Vector2 velocity = Vector2Pool.obtain((i - 3) * 50, (j - 3) * 50);
		faceBody.setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
		velocity = Vector2Pool.obtain((j - 3) * 50, (i - 3) * 50);
		faceBody2.setLinearVelocity(velocity);

		Dice dice1 = dices.get(0);
		Dice dice2 = dices.get(1);
		AnimatedSprite.IAnimationListener listener = (IAnimationListener) new DiceAnimationListener(this);
		dice1.animate(new long[] { 30, 30, 30, 30, 30, 30 },
				new int[] { i, (i + 1) % 6, (i + 2) % 6, (i + 3) % 6,
				(i + 4) % 6, (i + 5) % 6 }, 13, listener);
		dice2.animate(new long[] { 30, 30, 30, 30, 30, 30 },
				new int[] { j, (j + 1) % 6, (j + 2) % 6, (j + 3) % 6,
				(j + 4) % 6, (j + 5) % 6 }, 13,listener);
		
		dice1ConsumedCount = 0;
		dice2ConsumedCount = 0;
		//while(dice2.isAnimationRunning());
		Vector2Pool.recycle(velocity);
	}

	public RollDice(float pX, float pY, 
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, TableActivity table) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.table = table;
	}

	public void addDices(Dice d1, Dice d2) {
		dices.clear();
		dices.add(d1);
		dices.add(d2);
	}

	public int getDice1Value() {
		if ((dice1ConsumedCount<1)||(dice1ConsumedCount+dice2ConsumedCount<3 && isDouble()))
			return dices.get(0).getValue();
		else
			return 0;
	}

	public int getDice2Value() {
		if ((dice2ConsumedCount<1)||(dice1ConsumedCount+dice2ConsumedCount<3 && isDouble()))
			return dices.get(1).getValue();
		else
			return 0;
	}

	public void consumeDice1() {
		dice1ConsumedCount ++;
		if (noRemainingMoves())
		{
			this.table.onDicesConsumed();
		}
	}
	public void consumeAllDices()
	{
		dice1ConsumedCount = 2;
		dice2ConsumedCount = 2;
		this.table.onDicesConsumed();
	}
	public boolean isDouble()
	{
		if (dices.get(0).getValue() == dices.get(1).getValue())
			return true;
		else return false;
	}
	public void consumeDice2() {
		dice2ConsumedCount ++;
		if (noRemainingMoves())
		{
			this.table.onDicesConsumed();
		}
	}

	public boolean noRemainingMoves() {
		boolean doubl = isDouble();
		if ((dice1ConsumedCount ==1 && dice2ConsumedCount ==1 && !doubl) ||
				(dice1ConsumedCount + dice2ConsumedCount>3 && doubl))
				{
					return true;
				}
		else
		{
			return false;
		}
	}
	public void onAnimationFinished()
	{
		finishedCount ++;
		if (finishedCount == 2)
		{
			table.onDiceRolled();
		}
	}

}
