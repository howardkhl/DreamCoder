package Character.Hero;

import screen_Manager.ScreenManager;
import Character.Character;
import GTCS1Lib_Java.JavaGTCS1Base;
import GTCS1Lib_Java.Internal.Vector2;
import GTCS1Lib_Java.JavaGTCS1Base.SpriteAnimateMode;

/**************************************************************************************************
 * This Hero class extends Hero class.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Hero extends Character 
{
	private static final long serialVersionUID = -4586962566753668490L;

	/* Hero States */
	public enum HeroState
	{
		Rest,
		Walk,
		Auto
	}
	
	/* Constant variables */
	
	/* Local variables */
	protected HeroState mCurrentState = HeroState.Rest;
	protected boolean isUserControl = true;
	
	/**
	 * Default Constructor.
	 * @param origin starting coordinate
	 * @param radius size of Hero
	 */
	public Hero(Vector2 origin, float radius) 
	{
		super(origin, radius);
	}
	
	/**
	 * Default Constructor.
	 * @param origin starting coordinate
	 * @param radius size of Hero
	 * @param walkSpeed	walk speed
	 */
	public Hero(Vector2 origin, float radius, float walkSpeed)
	{
		super(origin, radius);
		mWalkSpeed = walkSpeed;
	}
	
	/** Reclaim memory space. */
	public void Unload()
	{
		RemoveFromAutoDrawSet();
	}
	
	/**
	 * Update Hero.
	 */
	@Override
	public void Update() 
	{
		/* Check Hero's current state */
		switch (mCurrentState)
		{
			case Rest:
				RestState();
				break;
			case Walk:
				WalkState();
				break;
			case Auto:
				AutoState();
				break;
		}
		
//		JavaGTCS1Base.EchoToTopStatus("Hero: (" + (int)GetCenterX() + "," + (int)GetCenterY() + ")" +
//									  " Speed: " + (int)mWalkSpeed);
	}
	
	/**
	 * Hero REST state. Hero stands still and awaits user input. When Hero finishes
	 * walking it will return to this state. Alternatively, use <code>SetRestState()</code> 
	 * function to set to this state.
	 */
	@Override
	protected void RestState()
	{
		super.RestState();
		
		/* Check if Hero is moving and user has control of Hero */
		if ((JavaGTCS1Base.LeftThumbstick().X != 0f || JavaGTCS1Base.LeftThumbstick().Y != 0f) && isUserControl)
		{
			mCurrentState = HeroState.Walk;
		}
	}
	
	/**
	 * Hero WALK state. Hero moves free-form according to user input.
	 */
	protected void WalkState()
	{
		/* Get facing direction */
		CalcFacingDirection();
		
		/* Animate walk */
		if (!GetSpriteSheetIsUsingAnimation())
		{
			SetSpriteTextureAnimationFrames(walkBeginX, mCurrDir, walkEndX, mCurrDir, WALK_FRAME_RATE, SpriteAnimateMode.AnimateForward);
			SetSpriteSheetIsUsingAnimation(true);
		}
		
		/* Check if moving direction changes */
		if (mPrevDir != mCurrDir)
		{
			SetSpriteSheetIsUsingAnimation(false);
			mPrevDir = mCurrDir;
		}
		
		/* Check if Hero stopped moving, go to Rest state */
		if (JavaGTCS1Base.LeftThumbstick().X == 0f &&
			JavaGTCS1Base.LeftThumbstick().Y == 0f)
		{
			mCurrentState = HeroState.Rest;
		}

		/* Update Hero's center */
		SetCenter(Vector2.add(this.GetCenter(), Vector2.multiply(JavaGTCS1Base.LeftThumbstick(), mWalkSpeed)));
	}
	
	/**
	 * Hero NOINPUT state. Hero does not take input from user. Use <code>SetNoInput</code> 
	 * function to set to this state. 
	 */
	protected void NoInputState() 
	{
		/* Set Rest sprite */
		SetSpriteTextureAnimationFrames(restBeginX, mCurrDir, restEndX, mCurrDir, 1, SpriteAnimateMode.AnimateForward);
		SetSpriteSheetIsUsingAnimation(false);
	}
	
	/**
	 * Helper function for AUTO state. Set end state to NoInput state.
	 */
	@Override
	protected void AutoStateSetEnd()
	{
		/* Set Hero to Rest state */
		mCurrentState = HeroState.Rest;
	}
	
	/**
	 * Helper function to determine direction vector. Overridden to receive input from left thumbstick.
	 */
	@Override
	protected void CalcDirectionVector()
	{
		if (mCurrentState == HeroState.Walk)
		{
			/* Get input from left thumbstick */
			mDirVector = JavaGTCS1Base.LeftThumbstick();
		}
		else
		{
			/* Calculate direction vector from "MoveTo" coordinate */
			mDirVector = Vector2.Normalize(Vector2.subtract(mMoveToCoord, GetCenter()));
		}
	}
	
	/**
	 * Set Hero's current state. States are: <code>Rest, Walk, Auto</code>.
	 * @param state state to set Hero in
	 */
	public void SetState(HeroState state)
	{
		mCurrentState = state;
	}

	/**
	 * Set Hero to <code>Rest</code> state. This is the same as <code>Manual()</code>.
	 */
	public void SetRestState()
	{
		mCurrentState = HeroState.Rest;
		isUserControl = true;
	}
	
	/**
	 * Set Hero to <code>Rest</code> state. This is the same as <code>SetRestState()</code>.
	 */
	public void Manual()
	{
		mCurrentState = HeroState.Rest;
		isUserControl = true;
	}
	
	/**
	 * Set user control of Hero.
	 * @param userControl true = player controls hero, false = player loses control of hero
	 */
	public void SetUserControl(boolean userControl)
	{
		if (userControl)
		{
			isUserControl = true;
		}
		else 
		{
			isUserControl = false;
			mCurrentState = HeroState.Rest;
		}
	}
	
	/**
	 * Set Hero to <code>NoInput</code> state. This is the same as <code>NoInput()</code>.
	 */
	public void SetNoInputState()
	{
		SetUserControl(false);
	}
	
	/**
	 * Set Hero to <code>NoInput</code> state.
	 */
	public void SetNoInput()
	{
		SetUserControl(false);
	}
	
	/**
	 * Set Hero to <code>NoInput</code> state. This is the same as <code>SetNoInputState()</code>.
	 */
	public void NoInput()
	{
		SetUserControl(false);
	}
	
	/**
	 * Set Hero to <code>Auto</code> state. Hero will move to given coordinate.
	 * This is the same as <code>SetAuto()</code>.
	 * @param toCoord move to coordinate
	 */
	public void MoveTo(Vector2 toCoord)
	{
		mMoveToCoord = new Vector2(toCoord.X, toCoord.Y);
		SetSpriteSheetIsUsingAnimation(false);
		mCurrentState = HeroState.Auto;
	}
	
	/**
	 * Set Hero to <code>Auto</code> state. Hero will move to given coordinate.
	 * This is the same as <code>SetAuto()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 */
	public void MoveTo(float coordX, float coordY)
	{
		MoveTo(new Vector2(coordX, coordY));
	}
	
	/**
	 * Set Hero to <code>Auto</code> state. Hero will move to given coordinate.
	 * This is the same as <code>MoveTo()</code>.
	 * @param toCoord move to coordinate
	 */
	public void SetAuto(Vector2 toCoord)
	{
		MoveTo(toCoord);
	}
	
	/**
	 * Set Hero to <code>Auto</code> state. Hero will move to given coordinate.
	 * This is the same as <code>MoveTo()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 */
	public void SetAuto(float coordX, float coordY)
	{
		MoveTo(coordX, coordY);
	}
	
	/**
	 * Return Hero's current walk speed.
	 * @return walk speed
	 */
	public float GetWalkSpeed()
	{
		return mWalkSpeed;
	}
	
	public Vector2 getMoveTo()
	{
		return mMoveToCoord;
	}
}
