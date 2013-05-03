package Character.Hero;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This Hero_Riku class extends Hero class. For demo purpose only, not intended for final design.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Hero_Riku extends Hero 
{
	private static final long serialVersionUID = 1301296434788269554L;

	/** Default Constructor */
	public Hero_Riku(Vector2 origin, float radius) 
	{
		super(origin, radius);
		SetSprite();
	}
	
	/** Default Constructor */
	public Hero_Riku(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		SetSprite();
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("Riku.png");
		SetSpriteSheetTexture(14, 8, 0);
		SetSpriteSheetUsed(true);
		
		restBeginX = 0;
		restEndX = 0;
		walkBeginX = 1;
		walkEndX = 8;
		rowFacingSouth = 0;
		rowFacingEast = 2;
		rowFacingNorth = 4;
		rowFacingWest = 6;
	}
}
