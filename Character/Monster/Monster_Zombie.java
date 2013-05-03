package Character.Monster;

import monsterGenerator.MonsterGenerator.MonsterType;
import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This Monster_DarkWizard class extends Monster class. For demo purpose only, not intended for final design.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class Monster_Zombie extends Monster 
{
	private static final long serialVersionUID = 6257439508519656758L;

	/** Default Constructor */
	public Monster_Zombie(Vector2 origin, float radius) 
	{
		super(origin, radius);
		Initialize();
	}
	
	/** Default Constructor */
	public Monster_Zombie(Vector2 origin, float radius, float walkSpeed) 
	{
		super(origin, radius, walkSpeed);
		mWalkSpeed = walkSpeed;
		Initialize();
	}
	
	/** Initialize monster */
	private void Initialize()
	{
		SetSprite();
		mMonsterType = "Zombie";
	}

	/** Sprite stuff */
	private void SetSprite()
	{
		SetTexture("zombie_sprite.png");
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
