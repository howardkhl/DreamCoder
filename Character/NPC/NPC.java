package Character.NPC;

import Character.Character;
import Character.Monster.Monster.MonsterState;
import GTCS1Lib_Java.Internal.Vector2;

/**************************************************************************************************
 * This NPC class extends Character class.
 * 
 * @version 7/27/2011
 * @author Howard Lee
 * 
 **************************************************************************************************/

public class NPC extends Character 
{
	private static final long serialVersionUID = -8677828800540649575L;
	
	/* NPC States */
	public enum NPCState
	{
		Rest,
		Patrol,
		Auto
	}
	
	/* Constant variables */
	
	/* Local variables */
	protected NPCState mCurrentState = NPCState.Rest;

	/**
	 * Default Constructor.
	 * @param origin starting coordinate
	 * @param radius size of NPC
	 */
	public NPC(Vector2 origin, float radius) 
	{
		super(origin, radius);
	}
	
	/**
	 * Default Constructor.
	 * @param origin starting coordinate
	 * @param radius size of NPC
	 * @param walkSpeed	walk speed
	 */
	public NPC(Vector2 origin, float radius, float walkSpeed)
	{
		super(origin, radius);
		mWalkSpeed = walkSpeed;
	}
	
	/** Reclaim memory space. */
	public void Unload()
	{
		RemoveFromAutoDrawSet();
	}
	
	/**
	 * Update NPC.
	 */
	@Override
	public void Update() 
	{
		/* Check NPC's current state */
		switch (mCurrentState)
		{
			case Rest:
				RestState();
				break;
			case Patrol:
				PatrolState();
				break;
			case Auto:
				AutoState();
				break;
		}
	}
	
	/**
	 * NPC PATROL state. NPC feels bored and decides to walk around.
	 */
	protected void PatrolState()
	{
		/* Needs to implement */
	}
	
	/**
	 * Set NPC's current state. <code>Rest, Patrol, Auto</code>.
	 * @param state
	 */
	public void SetState(NPCState state)
	{
		mCurrentState = state;
	}
	
	/**
	 * Helper function for AUTO state. Set to either Rest state or NoInput state.
	 */
	@Override
	protected void AutoStateSetEnd() 
	{
		mCurrentState = NPCState.Rest;
	}
	
	/**
	 * Set NPC to <code>Rest</code> state. This is same as <code>Rest()</code>.
	 */
	public void SetRestState()
	{
		mCurrentState = NPCState.Rest;
	}
	
	/**
	 * Set NPC to <code>Rest</code> state. This is same as <code>SetRestState()</code>.
	 */
	public void Rest()
	{
		mCurrentState = NPCState.Rest;
	}
	
	/**
	 * Set NPC to <code>Auto</code> state. NPC will move to given coordinate.
	 * This is the same as <code>SetAuto()</code>.
	 * @param toCoord move to coordinate
	 */
	public void MoveTo(Vector2 toCoord)
	{
		mMoveToCoord = new Vector2(toCoord.X, toCoord.Y);
		SetSpriteSheetIsUsingAnimation(false);
		mCurrentState = NPCState.Auto;
	}
	
	/**
	 * Set NPC to <code>Auto</code> state. NPC will move to given coordinate.
	 * This is the same as <code>SetAuto()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 */
	public void MoveTo(float coordX, float coordY)
	{
		MoveTo(new Vector2(coordX, coordY));
	}
	
	/**
	 * Set NPC to <code>Auto</code> state. NPC will move to given coordinate.
	 * This is the same as <code>MoveTo()</code>.
	 * @param toCoord move to coordinate
	 */
	public void SetAuto(Vector2 toCoord)
	{
		MoveTo(toCoord);
	}
	
	/**
	 * Set NPC to <code>Auto</code> state. NPC will move to given coordinate.
	 * This is the same as <code>MoveTo()</code>.
	 * @param coordX x-coordinate
	 * @param coordY y-coordinate
	 */
	public void SetAuto(float coordX, float coordY)
	{
		MoveTo(coordX, coordY);
	}
}
