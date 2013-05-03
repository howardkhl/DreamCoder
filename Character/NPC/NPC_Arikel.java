package Character.NPC;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This NPC_Arikel class extends NPC class. For demo purpose only, not intended for final design.
 * Note: Arikel art is a free and open domain!
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class NPC_Arikel extends NPC 
{
	private static final long serialVersionUID = 561457278042244983L;

	/** Default Constructor */
	public NPC_Arikel(Vector2 origin, float radius) 
	{
		super(origin, radius);
		SetSprite();
	}
	
	/** Default Constructor */
	public NPC_Arikel(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		SetSprite();
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("Arikel.png");
		SetSpriteSheetTexture(8, 4, 0);
		SetSpriteSheetUsed(true);
		
		restBeginX = 0;
		restEndX = 0;
		walkBeginX = 1;
		walkEndX = 7;
		rowFacingSouth = 0;
		rowFacingEast = 3;
		rowFacingNorth = 1;
		rowFacingWest = 2;
	}
}
