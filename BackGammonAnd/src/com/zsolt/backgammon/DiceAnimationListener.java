package com.zsolt.backgammon;


import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;

public class DiceAnimationListener implements IAnimationListener {
	private int maxCount = 150;
	private RollDice parent;

	public DiceAnimationListener(RollDice parent)
	{
		super();
		this.parent = parent;
	}
	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
		// TODO Auto-generated method stub
		/*
		if (--maxCount <=0)
		{
			Random randomGenerator = new Random();
			int i = randomGenerator.nextInt(6);
			pAnimatedSprite.stopAnimation(i);
		}*/
	}

	@Override
	public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
		((Dice)pAnimatedSprite).onAnimationFinished((pAnimatedSprite.getCurrentTileIndex() + 1));
		parent.onAnimationFinished();
	}

	
}
