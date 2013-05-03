package Character.NPC;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * NPC_Elder for Q6B_0 
 * 
 * @version 05/08/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class NPC_Elder extends NPC 
{
	private static final long serialVersionUID = -4059795815562972638L;

	/** Default Constructor */
	public NPC_Elder(Vector2 origin, float radius) 
	{
		super(origin, radius);
		SetSprite();
	}
	
	/** Default Constructor */
	public NPC_Elder(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		SetSprite();
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("elder_sprite.png");
		SetSpriteSheetTexture(1, 2, 0);
		SetSpriteSheetUsed(true);
		
		restBeginX = 0;
		restEndX = 0;
		walkBeginX = 0;
		walkEndX = 0;
		rowFacingSouth = 0;
		rowFacingEast = 1;
		rowFacingNorth = 1;
		rowFacingWest = 0;
	}
}
