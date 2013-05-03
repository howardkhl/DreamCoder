package Character.Monster;

import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This Monster_DarkWizard class extends Monster class. For demo purpose only, not intended for final design.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Monster_Wizard_Despair extends Monster 
{
	private static final long serialVersionUID = 1301296434788269554L;

	/** Default Constructor */
	public Monster_Wizard_Despair(Vector2 origin, float radius) 
	{
		super(origin, radius);
		Initialize();
	}
	
	/** Default Constructor */
	public Monster_Wizard_Despair(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		Initialize();
	}
	
	/** Initialize monster */
	private void Initialize()
	{
		SetSprite();
		mMonsterType = "Wizard_Despair";
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("wizard_despair.png");
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
