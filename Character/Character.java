package Character;

import ExpressBubble.ButtonABubble;
import ExpressBubble.SupriseBubble;
import GTCS1Lib_Java.JavaGTCS1Base.SpriteAnimateMode;
import GTCS1Lib_Java.Internal.Vector2;
import MapObjects.FreeMovingMapObject;

/**************************************************************************************************
 * This Character class is the base class for Character, NPC, Monster, and etc.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Character extends FreeMovingMapObject 
{
	private static final long serialVersionUID = 3072458068954917427L;

	/* Character states */
	protected enum CharaterState
	{
		Rest,
		Auto
	}
	
	/* Character facing */
	public enum CharacterFacing
	{
		South,
		East,
		North,
		West
	}
	
	/* Constant variables */
	protected final float WALK_SPEED = 5f;		// walk speed
	protected final int WALK_FRAME_RATE = 20;	// walk frame rate
	
	/* Local variables */
	protected CharaterState mCurrentState;		// Current state
	protected int mCurrDir;						// Direction: S=0, E=1, N=2, W=3
	protected int mPrevDir;						// Previous direction
	protected Vector2 mDirVector;				// Direction vector
	protected Vector2 mMoveToCoord;				// Coordinate for auto-advancing
	protected float mWalkSpeed;					// Character's walk speed
	
	/* Express Bubble */
	private SupriseBubble mSupriseBubble;
	private ButtonABubble mButtonABubble;
	
	/* Sprite variables */
	protected int restBeginX = 0;					// Rest sprite: begin column
	protected int restEndX = 0;						// Rest sprite: end column
	protected int walkBeginX = 0;					// Walk sprite: begin column
	protected int walkEndX = 0;						// Walk sprite: end column
	protected int rowFacingSouth = 0;				// Sprite's row facing south
	protected int rowFacingEast = 0;				// Sprite's row facing east
	protected int rowFacingNorth = 0;				// Sprite's row facing north
	protected int rowFacingWest = 0;				// Sprite's row facing west
	
	/**
	 * Default Constructor
	 * @param origin starting coordinate
	 * @param radius size of Character
	 */
	public Character(Vector2 origin, float radius) 
	{
		super(origin, radius);
		Initialize(origin);
	}
	
	/** Initialize local variables. */
	protected void Initialize(Vector2 origin)
	{
		mCurrentState = CharaterState.Rest;
		mCurrDir = 0;							// Initial direction facing South
		mPrevDir = mCurrDir;					// Set previous direction to current direction
		mMoveToCoord = Vector2.Zero;			// Initialize to zero
		mDirVector = Vector2.Zero;				// Initialize to zero
		mWalkSpeed = WALK_SPEED;				// Set to default walk speed
		
		/* Instantiate Express Bubble */
		mSupriseBubble = new SupriseBubble(origin);
		mButtonABubble = new ButtonABubble(origin);
	}
	
	/** Reclaim memory space. */
	public void Unload()
	{
		RemoveFromAutoDrawSet();
	}
	
	public void UpdateUserMovement(Tile.TileSet tiles)
	{
		super.UpdateUserMovement(tiles);
		this.UpdateBubble();
		this.Update();
	}
	
	/** Base update function */
	protected void Update() 
	{
		/* Check current state */
		switch (mCurrentState)
		{
			case Rest:
				RestState();
				break;
			case Auto:
				AutoState();
				break;
		}
	}
	
	/** Update Express Bubble */
	protected void UpdateBubble()
	{
		mSupriseBubble.Update(new Vector2(GetCenterX(), GetCenterY() + GetRadius()/2 + mSupriseBubble.GetHeight()));
		mButtonABubble.Update(new Vector2(GetCenterX(), GetCenterY() + GetRadius()/2 + mSupriseBubble.GetHeight()));
	}
	
	/**
	 * Character REST state. Character stands still and do nothing.
	 */
	protected void RestState()
	{
		/* Set Rest sprite according to facing direction */
		SetSpriteTextureAnimationFrames(restBeginX, mCurrDir, restEndX, mCurrDir, 1, SpriteAnimateMode.AnimateForward);
		SetSpriteSheetIsUsingAnimation(false);
	}
	
	/**
	 * Character AUTO state. Character auto-advances to a given coordinate.
	 * Use <code>SetAuto</code> function to change Character's state and set coordinate.
	 */
	protected void AutoState()
	{
		/* Get facing direction */
		CalcFacingDirection();
		
		/* Animate Character walk sprite */
		if (!GetSpriteSheetIsUsingAnimation())
		{
			SetSpriteTextureAnimationFrames(walkBeginX, mCurrDir, walkEndX, mCurrDir, WALK_FRAME_RATE, SpriteAnimateMode.AnimateForward);
			SetSpriteSheetIsUsingAnimation(true);
		}
		
		/* Gradually move Character to given coordinate */
		SetCenter(Vector2.add(GetCenter(), Vector2.multiply(mDirVector, mWalkSpeed)));
		
		/* Check if Character reached designated coordinate */
		if (Math.abs(mMoveToCoord.X - GetCenterX()) < mWalkSpeed && Math.abs(mMoveToCoord.Y - GetCenterY()) < mWalkSpeed)
		{
			AutoStateSetEnd();
		}
	}
	
	/**
	 * Helper function for AUTO state. Inherited classes must override to set to its own state.
	 */
	protected void AutoStateSetEnd() 
	{
		mCurrentState = CharaterState.Rest;
	}
	
	/**
	 * Helper function to determine facing direction.
	 */
	protected void CalcFacingDirection()
	{
		/* Calculate direction vector */
		CalcDirectionVector();
		
		/* Determine facing direction */
		if (mDirVector.Y < -Math.abs(mDirVector.X)) mCurrDir = rowFacingSouth;	// South
		if (mDirVector.X > Math.abs(mDirVector.Y)) mCurrDir = rowFacingEast;	// East
		if (mDirVector.Y > Math.abs(mDirVector.X)) mCurrDir = rowFacingNorth;	// North
		if (mDirVector.X < -Math.abs(mDirVector.Y)) mCurrDir = rowFacingWest;	// West
	}
	
	/**
	 * Helper function to determine direction vector.
	 */
	protected void CalcDirectionVector()
	{
		/* Calculate direction vector from "MoveTo" coordinate */
		mDirVector = Vector2.Normalize(Vector2.subtract(mMoveToCoord, GetCenter()));
	}
	
	/**
	 * Set Character's walk speed.
	 * @param speed walk speed
	 */
	public void SetWalkSpeed(float speed)
	{
		mWalkSpeed = speed;
	}
	
	/**
	 * Set Character's facing direction.
	 * @param direction 0 = south, 1 = east, 2 = north, 3 = west
	 */
	public void SetFacing(int direction)
	{
		mCurrDir = direction;
	}
	
	/**
	 * Set Character's facing direction.
	 * @param direction 0 = south, 1 = east, 2 = north, 3 = west
	 */
	public void SetFacing(CharacterFacing facingDir)
	{
		if (facingDir == CharacterFacing.South)
			mCurrDir = rowFacingSouth;
		else if (facingDir == CharacterFacing.East)
			mCurrDir = rowFacingEast;
		else if (facingDir == CharacterFacing.North)
			mCurrDir = rowFacingNorth;
		else if (facingDir == CharacterFacing.West)
			mCurrDir = rowFacingWest;
	}
	
	/**
	 * Get Character's facing direction.
	 * @return facing direction
	 */
	public int GetFacing()
	{
		return mCurrDir;
	}
	
	/**
	 * Compare current location with given X and Y coordinate. Same as <code>CompareCoordinate()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 * @return
	 */
	public boolean HasReached(float coordX, float coordY)
	{
		if (Math.abs(GetCenterX() - coordX) < mWalkSpeed && Math.abs(GetCenterY() - coordY) < mWalkSpeed)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/**
	 * Compare current location with given X and Y coordinate. Same as <code>CompareCoordinate()</code>.
	 * @param coord comparing coordinate
	 * @return
	 */
	public boolean HasReached(Vector2 coord)
	{
		if (Math.abs(GetCenterX() - coord.X) < mWalkSpeed && Math.abs(GetCenterY() - coord.Y) < mWalkSpeed)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/**
	 * Show Express Bubble.
	 * @param i 0 = show surprise bubble, 1 = show button bubble
	 */
	public void ShowExpress(int i)
	{
		switch (i) {
			case 0:
				mSupriseBubble.Show();
				break;
			case 1:
				mButtonABubble.Show();
				break;
		}
	}
	
	/**
	 * Hide Express Bubble.
	 * @param i 0 = hide surprise bubble, 1 = hide button bubble
	 */
	public void HideExpress(int i)
	{
		switch (i) {
			case 0:
				mSupriseBubble.Hide();
				break;
			case 1:
				mButtonABubble.Hide();
				break;
		}
	}
}
