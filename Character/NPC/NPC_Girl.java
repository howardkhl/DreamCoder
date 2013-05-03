package Character.NPC;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This NPC_Girl class extends NPC class. For demo purpose only, not intended for final design.
 * Note: Little Girl art is a free and open domain!
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class NPC_Girl extends NPC 
{
	private static final long serialVersionUID = 2173976464601362157L;

	/** Default Constructor */
	public NPC_Girl(Vector2 origin, float radius) 
	{
		super(origin, radius);
		SetSprite();
	}
	
	/** Default Constructor */
	public NPC_Girl(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		SetSprite();
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("littlegirl.png");
		SetSpriteSheetTexture(1, 1, 0);
		SetSpriteSheetUsed(true);
		
		restBeginX = 0;
		restEndX = 0;
		walkBeginX = 0;
		walkEndX = 0;
		rowFacingSouth = 0;
		rowFacingEast = 0;
		rowFacingNorth = 0;
		rowFacingWest = 0;
	}
}
