package Character.Monster;

import Character.Character;
import Character.Hero.Hero;
import Character.Monster.Monster;
import GTCS1Lib_Java.Internal.Vector2;
import GTCS1Lib_Java.JavaGTCS1Base.SpriteAnimateMode;
import Tile.TileSet;

/**************************************************************************************************
 * This Monster class extends Character class.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Monster extends Character 
{
	private static final long serialVersionUID = 8542901976353394434L;

	/* Monster States */
	public enum MonsterState
	{
		Rest,
		Patrol,
		Chase,
		Auto
	}
	
	/* Constant variables */
	protected final float PATROL_SPEED = 0.2f;		// patrol speed
	protected final float PATROL_LENGTH = 20f;		// patrol distance from center
	protected final int PATROL_TICKS = 100;			// patrol tick time
	protected final int PATROL_FRAME_RATE = 5;		// patrol frame rate
	
	protected final float DIST_BEGIN_CHASE = 150f;	// distance to begin chase
	protected final float CHASE_SPEED = 1f;			// chase speed
	protected final int CHASE_FRAME_RATE = 10;		// chase frame rate
	
	/* Local variables */
	protected MonsterState mCurrentState = MonsterState.Patrol;
	
	protected Vector2 mPatrolVector;				// patrol movement
	protected Vector2 mNewPatrolPt;					// new patrol point
	protected boolean mGetPatrolPt;					// is monster patrolling?
	protected boolean mHoldPatrol;					// find new patrol point?
	protected int mPatrolTicks;						// patrol tick time

	protected Vector2 mOrigCenter;					// original monster center
	protected String mMonsterType;					// type of monster
	protected String mBattleScene;					// battle scenario
	
	/**
	 * Default Constructor.
	 * @param origin starting coordinate
	 * @param radius size of Monster
	 */
	public Monster(Vector2 origin, float radius) 
	{
		super(origin, radius);
		InitializeMonster();
	}
	
	/**
	 * Default Constructor.
	 * @param origin starting coordinate
	 * @param radius size of Monster
	 * @param walkSpeed	walk speed
	 */
	public Monster(Vector2 origin, float radius, float walkSpeed)
	{
		super(origin, radius);
		mWalkSpeed = walkSpeed;
		InitializeMonster();
	}
	
	/** Initialize Monster's local variables */
	protected void InitializeMonster()
	{
		mPatrolVector = Vector2.Zero;
		mNewPatrolPt = Vector2.Zero;
		mGetPatrolPt = true;
		mHoldPatrol = false;
		mPatrolTicks = 0;
		
		mOrigCenter = new Vector2(GetCenterX(), GetCenterY());
		mMonsterType = "";
		mBattleScene = "";
	}
	
	/** Reclaim memory space. */
	public void Unload()
	{
		RemoveFromAutoDrawSet();
	}
	
	/**
	 * Update monster with tiles and hero.
	 * @param tiles Map tiles
	 * @param target Targeted character
	 */
	public void Update(TileSet tiles, Character target)
	{
		super.UpdateUserMovement(tiles);
		this.Update(target);
	}
	
	/**
	 * Update Monster.
	 * @param target Targeted character
	 */
	public void Update(Character target) 
	{
		/* Check Monster's current state */
		switch (mCurrentState)
		{
			case Rest:
				RestState();
				break;
			case Patrol:
				PatrolState(target);
				break;
			case Chase:
				ChaseState(target);
				break;
			case Auto:
				AutoState();
				break;
		}
	}
	
	/**
	 * Monster PATROL state. Monster moves free-form.
	 */
	protected void PatrolState(Hero target)
	{
		/* If not patrolling, create a new random patrol point */
		if (mGetPatrolPt)
		{
			mMoveToCoord = new Vector2();
			mMoveToCoord.X = mOrigCenter.X + ((float)(Math.random() * 2 - 1.0) * PATROL_LENGTH);
			mMoveToCoord.Y = mOrigCenter.Y + ((float)(Math.random() * 2 - 1.0) * PATROL_LENGTH);
			CalcFacingDirection();
			mGetPatrolPt = false;
		}

		/* Keep moving until reaching patrolling point */
		if (!mHoldPatrol)
		{
			if (!GetSpriteSheetIsUsingAnimation())
			{
				SetSpriteTextureAnimationFrames(walkBeginX, mCurrDir, walkEndX, mCurrDir, PATROL_FRAME_RATE, SpriteAnimateMode.AnimateForward);
				SetSpriteSheetIsUsingAnimation(true);
			}
			SetCenter(Vector2.add(GetCenter(), Vector2.multiply(mDirVector, PATROL_SPEED)));
		}
			
		/* If reached new patrolling point, hold. */
		if (Math.abs(mMoveToCoord.X - GetCenterX()) < 0.5f || Math.abs(mMoveToCoord.Y - GetCenterY()) < 0.5f)
		{
			/* Hold for given amount of time */
			if (mPatrolTicks < PATROL_TICKS)
			{
				SetSpriteTextureAnimationFrames(restBeginX, mCurrDir, restEndX, mCurrDir, 1, SpriteAnimateMode.AnimateForward);
				mHoldPatrol = true;
				mPatrolTicks++;
			}
			/* Hold enough, start patrol again */
			else 
			{
				SetSpriteSheetIsUsingAnimation(false);
				mHoldPatrol = false;
				mGetPatrolPt = true;
				mPatrolTicks = 0;
			}
		}
		
		// Check if Monster is nearby
		Vector2 toHero = Vector2.subtract(target.GetCenter(), GetCenter());
		if (toHero.Length() < DIST_BEGIN_CHASE)
		{
			SetSpriteSheetIsUsingAnimation(false);
			mCurrentState = MonsterState.Chase;
		}
	}
	
	protected void PatrolState(Character target)
	{
		/* If not patrolling, create a new random patrol point */
		if (mGetPatrolPt)
		{
			mMoveToCoord = new Vector2();
			mMoveToCoord.X = mOrigCenter.X + ((float)(Math.random() * 2 - 1.0) * PATROL_LENGTH);
			mMoveToCoord.Y = mOrigCenter.Y + ((float)(Math.random() * 2 - 1.0) * PATROL_LENGTH);
			CalcFacingDirection();
			mGetPatrolPt = false;
		}

		/* Keep moving until reaching patrolling point */
		if (!mHoldPatrol)
		{
			if (!GetSpriteSheetIsUsingAnimation())
			{
				SetSpriteTextureAnimationFrames(walkBeginX, mCurrDir, walkEndX, mCurrDir, PATROL_FRAME_RATE, SpriteAnimateMode.AnimateForward);
				SetSpriteSheetIsUsingAnimation(true);
			}
			SetCenter(Vector2.add(GetCenter(), Vector2.multiply(mDirVector, PATROL_SPEED)));
		}
			
		/* If reached new patrolling point, hold. */
		if (Math.abs(mMoveToCoord.X - GetCenterX()) < 0.5f || Math.abs(mMoveToCoord.Y - GetCenterY()) < 0.5f)
		{
			/* Hold for given amount of time */
			if (mPatrolTicks < PATROL_TICKS)
			{
				SetSpriteTextureAnimationFrames(restBeginX, mCurrDir, restEndX, mCurrDir, 1, SpriteAnimateMode.AnimateForward);
				mHoldPatrol = true;
				mPatrolTicks++;
			}
			/* Hold enough, start patrol again */
			else 
			{
				SetSpriteSheetIsUsingAnimation(false);
				mHoldPatrol = false;
				mGetPatrolPt = true;
				mPatrolTicks = 0;
			}
		}
		
		// Check if Monster is nearby
		Vector2 toHero = Vector2.subtract(target.GetCenter(), GetCenter());
		if (toHero.Length() < DIST_BEGIN_CHASE)
		{
			SetSpriteSheetIsUsingAnimation(false);
			mCurrentState = MonsterState.Chase;
		}
	}
	
	/**
	 * Monster CHASE state. Monster chases target.
	 */
	protected void ChaseState(Hero target)
	{
		Vector2 toHero = Vector2.subtract(target.GetCenter(), GetCenter());
		
		/* Verify if Monster is in chasing distance */
		if (toHero.Length() < DIST_BEGIN_CHASE)
		{
			mMoveToCoord = new Vector2(target.GetCenterX(), target.GetCenterY());
			CalcFacingDirection();
			
			if (!GetSpriteSheetIsUsingAnimation())
			{
				SetSpriteTextureAnimationFrames(walkBeginX, mCurrDir, walkEndX, mCurrDir, CHASE_FRAME_RATE, SpriteAnimateMode.AnimateForward);
				SetSpriteSheetIsUsingAnimation(true);
			}
			
			/* Update monster movement */
			SetCenter(Vector2.add(Vector2.multiply(mDirVector, mWalkSpeed), GetCenter()));
			
			/* Check for changing direction */
			if (mPrevDir != mCurrDir)
			{
				mPrevDir = mCurrDir;
				SetSpriteSheetIsUsingAnimation(false);
			}
		}
		/* Monster runs too fast, can't chase no more! */
		else
		{
			mGetPatrolPt = true;
			mHoldPatrol = false;
			
			/* Get a new patrolling center and set Monster to patrolling state */
			mOrigCenter = new Vector2(GetCenter().X, GetCenter().Y);
			mCurrentState = MonsterState.Patrol;
		}
	}
	
	protected void ChaseState(Character target)
	{
		Vector2 toHero = Vector2.subtract(target.GetCenter(), GetCenter());
		
		/* Verify if Monster is in chasing distance */
		if (toHero.Length() < DIST_BEGIN_CHASE)
		{
			mMoveToCoord = new Vector2(target.GetCenterX(), target.GetCenterY());
			CalcFacingDirection();
			
			if (!GetSpriteSheetIsUsingAnimation())
			{
				SetSpriteTextureAnimationFrames(walkBeginX, mCurrDir, walkEndX, mCurrDir, CHASE_FRAME_RATE, SpriteAnimateMode.AnimateForward);
				SetSpriteSheetIsUsingAnimation(true);
			}
			
			/* Update monster movement */
			SetCenter(Vector2.add(Vector2.multiply(mDirVector, mWalkSpeed), GetCenter()));
			
			/* Check for changing direction */
			if (mPrevDir != mCurrDir)
			{
				mPrevDir = mCurrDir;
				SetSpriteSheetIsUsingAnimation(false);
			}
		}
		/* Monster runs too fast, can't chase no more! */
		else
		{
			mGetPatrolPt = true;
			mHoldPatrol = false;
			
			/* Get a new patrolling center and set Monster to patrolling state */
			mOrigCenter = new Vector2(GetCenter().X, GetCenter().Y);
			mCurrentState = MonsterState.Patrol;
		}
	}
	
	/**
	 * Helper function for AUTO state. Set end state to Patrol state.
	 */
	@Override
	protected void AutoStateSetEnd()
	{
		/* Set Monster to Patrol state */
		mGetPatrolPt = true;
		mHoldPatrol = false;
		mCurrentState = MonsterState.Patrol;
	}
	
	/**
	 * Set Monster's current state. <code>Rest, Patrol, Chase, Auto</code>.
	 * @param state
	 */
	public void SetState(MonsterState state)
	{
		mCurrentState = state;
	}
	
	/**
	 * Set Monster to <code>Rest</code> state. This is same as <code>Rest()</code>.
	 */
	public void SetRestState()
	{
		mCurrentState = MonsterState.Rest;
	}
	
	/**
	 * Set Monster to <code>Rest</code> state. This is same as <code>SetRestState()</code>.
	 */
	public void Rest()
	{
		mCurrentState = MonsterState.Rest;
	}
	
	/**
	 * Set Monster to <code>Patrol</code> state. This is same as <code>Patrol()</code>.
	 */
	public void SetPatrolState()
	{
		mCurrentState = MonsterState.Patrol;
	}
	
	/**
	 * Set Monster to <code>Patrol</code> state. This is same as <code>SetPatrolState()</code>.
	 */
	public void Patrol()
	{
		mCurrentState = MonsterState.Patrol;
	}
	
	/**
	 * Set Monster to <code>Auto</code> state. Monster will move to given coordinate.
	 * This is the same as <code>SetAuto()</code>.
	 * @param toCoord move to coordinate
	 */
	public void MoveTo(Vector2 toCoord)
	{
		mMoveToCoord = new Vector2(toCoord.X, toCoord.Y);
		SetSpriteSheetIsUsingAnimation(false);
		mCurrentState = MonsterState.Auto;
	}
	
	/**
	 * Set Monster to <code>Auto</code> state. Monster will move to given coordinate.
	 * This is the same as <code>SetAuto()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 */
	public void MoveTo(float coordX, float coordY)
	{
		MoveTo(new Vector2(coordX, coordY));
	}
	
	/**
	 * Set Monster to <code>Auto</code> state. Monster will move to given coordinate.
	 * This is the same as <code>MoveTo()</code>.
	 * @param toCoord move to coordinate
	 */
	public void SetAuto(Vector2 toCoord)
	{
		MoveTo(toCoord);
	}
	
	/**
	 * Set Monster to <code>Auto</code> state. Monster will move to given coordinate.
	 * This is the same as <code>MoveTo()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 */
	public void SetAuto(float coordX, float coordY)
	{
		MoveTo(coordX, coordY);
	}
	
	public void setBattleScene(String incScene)
	{
		mBattleScene = incScene;
	}
	
	public String getBattleScene()
	{
		return mBattleScene;
	}
	
	public String GetMonsterType()
	{
		return mMonsterType;
	}
}
