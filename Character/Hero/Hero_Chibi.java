package Character.Hero;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This Hero_Riku class extends Hero class. For demo purpose only, not intended for final design.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Hero_Chibi extends Hero 
{
	private static final long serialVersionUID = 1301296434788269554L;

	/** Default Constructor */
	public Hero_Chibi(Vector2 origin, float radius) 
	{
		super(origin, radius);
		SetSprite();
	}
	
	/** Default Constructor */
	public Hero_Chibi(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		SetSprite();
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("hero.png");
		SetSpriteSheetTexture(4, 4, 0);
		SetSpriteSheetUsed(true);
		
		restBeginX = 0;
		restEndX = 0;
		walkBeginX = 1;
		walkEndX = 3;
		rowFacingSouth = 2;
		rowFacingEast = 0;
		rowFacingNorth = 3;
		rowFacingWest = 1;
	}
}
