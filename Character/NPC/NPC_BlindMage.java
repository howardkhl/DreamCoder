package Character.NPC;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * NPC_BlindMage for Q6B_0 
 * 
 * @version 05/08/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class NPC_BlindMage extends NPC 
{
	private static final long serialVersionUID = 2173976464601362157L;

	/** Default Constructor */
	public NPC_BlindMage(Vector2 origin, float radius) 
	{
		super(origin, radius);
		SetSprite();
	}
	
	/** Default Constructor */
	public NPC_BlindMage(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		SetSprite();
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("blindmage_ss.png");
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
