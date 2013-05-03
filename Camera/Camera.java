package camera;

import GTCS1Lib_Java.JavaGTCS1Base;
import GTCS1Lib_Java.Internal.JavaGTCS1Color;
import GTCS1Lib_Java.Internal.JavaGTCS1Rectangle;
import GTCS1Lib_Java.Internal.Vector2;

/*****************************************************************************
 * This <code>Camera</code> class keeps a focus on the coordinate that is
 * passed in to it. It is able to produce various effects such as Chase,
 * Shake, Shift, and etc. See <code>CameraEffect</code> for a list of
 * effects implemented. The concept of <code>Camera</code> is that it centers
 * on the coordinate given, and sets world size according to width of the
 * camera. Imagine the camera does not move, instead the background moves.
 * 
 * 08/22 - Set default Encounter effect with fast zoom in.
 * 08/18 - Added Encounter effect.
 * 08/11 - Added accessors to Zoom and Shift.
 * 08/07 - Fixed minor bug where mEffectFocus not focused correctly.
 * 08/07 - Rid of Swirl effect, use Zoom in to engage battle sequence.
 * 07/30 - Refined Camera class. Removed NPCEffect.
 * 07/25 - Modified SHIFT, it works. Read <code>SetShiftEffect()</code> for how to use.
 * 07/23 - Added NPC effect.
 * 07/23 - SHIFT does not work and is disabled due to hero is tied to world bound.
 * 
 * @author Howard Lee
 * @version	08/22/2011
 * @since	07/15/2011
 * 
 *****************************************************************************/
public class Camera
{
	/* Constant variables */
	private final float WORLD_RATIO = JavaGTCS1Base.World.WorldDimension().Y / JavaGTCS1Base.World.WorldDimension().X;
	private final int REFRESH_RATE = 30;		// Game frame rate
	private final float CHASE_SPEED = 0.05f;	// Focus chasing speed
	private final float SHIFT_SPEED = 4f;		// Shift speed
	private final int FADE_SPEED = 15;			// Fade speed
	
	/* Default variables */
	private Vector2 mLowerLeft;			// Lower left corner
	private Vector2 mEffectFocus;		// Focus when effects are ON
	private float mCameraWidth;			// Camera width
	private float mCameraHeight;		// Camera height
	private float mMapWidth;			// Map width
	private float mMapHeight;			// Map height
	private String mEffectMsg;			// Current effect
	
	/* CHASE effect variables */
	private boolean mGetOrigFocus;		// Get original focus
	private boolean mChaseEffectStatus;	// Whether Chase effect is ON as well
	private boolean mChaseEffectSwitch;
	
	/* SHAKE effect variables */
	private float mShakeRadius;			// Radius of shake
	private int mShakeType;				// Type of shake
	private int mShakeDuration;			// Length of shake
	private int mShakeTimer;			// Shake timer
	private boolean mShakeEffectSwitch;
	
	/* SHIFT effect variables */
	private Vector2 mShiftFocus;		// Shift focus
	private float mShiftSpeed;			// Shift speed
	private boolean mShowShift;			// Shift to new focus or shift back
	private boolean mIsDoneShifting;		// Is done shifting?
	private boolean mShiftEffectSwitch;
	
	/* EVENT effect variables */
	JavaGTCS1Rectangle mTopEventBox;	// Top event box
	JavaGTCS1Rectangle mBottomEventBox;	// Bottom event box
	private float mMaxBoxHeight;		// Max event box height
	private float mBoxHeight;			// Event box height
	private boolean mShowEventBox;		// Show event box
	private boolean mEventEffectSwitch;	// Whether event is on or off
	
	/* ZOOM effect variables */
	private float mOrigCamWidth;		// Original camera width
	private float mPrevCamWidth;		// Previous camera width
	private float mZoomAmount;			// Amount to zoom (- or +)
	private float mZoomSpeed;			// Speed of zoom
	private boolean mShowZoom;			// Show zoom
	private boolean mIsDoneZooming;		// Is done zooming?
	private boolean mZoomEffectSwitch;	// True = ON, false = OFF
	
	/* FADE effect variables */
	private int alpha;
	private int mFadeSpeed;
	private boolean mIsFadeIn;
	private boolean mFadeEffectSwitch;
	
	/* ENCOUNTER effect variable */
	private float mPrevZoomSpeed;
	private boolean mIsDoneEncounter;
	private boolean mEncounterSwitch;
	
	/* mask variables */
	private JavaGTCS1Rectangle mMask;   // Mask
	
	/**
	 * Default Constructor for Camera class.
	 * @param focus Camera focus
	 * @param cameraWidth Camera width
	 * @param mapWidth Map width
	 * @param mapHeight Map height
	 */
	public Camera (Vector2 focus, float cameraWidth, float mapWidth, float mapHeight) 
	{
		/* Initialize DEFAULT variables */
		mLowerLeft = new Vector2();
		mEffectFocus = new Vector2(focus.X, focus.Y);
		
		mCameraWidth = cameraWidth;
		mCameraHeight = cameraWidth * WORLD_RATIO;
		
		mMapWidth = mapWidth;
		mMapHeight = mapHeight;
		mEffectMsg = "";
		
		/* Initialize CHASE effect variables */
		mGetOrigFocus = true;
		mChaseEffectSwitch = false;
		mChaseEffectStatus = false;
		
		/* Initialize SHAKE effect variables */
		mShakeRadius = 5;
		mShakeType = 2;
		mShakeDuration = 5;
		mShakeTimer = 0;
		mShakeEffectSwitch = false;
		
		/* Initialize SHIFT effect variables */
		mShiftFocus = new Vector2(0, 0);
		mShiftSpeed = SHIFT_SPEED;
		mShowShift = true;
		mIsDoneShifting = true;
		mShiftEffectSwitch = false;
		
		/* Initialize EVENT effect variables */
		mTopEventBox = new JavaGTCS1Rectangle(Vector2.Zero, mCameraWidth, 0);
		mBottomEventBox = new JavaGTCS1Rectangle(Vector2.Zero, mCameraWidth, 0);
		mTopEventBox.SetColor(new JavaGTCS1Color(0, 0, 0, 200));
		mBottomEventBox.SetColor(new JavaGTCS1Color(0, 0, 0, 200));
		mMaxBoxHeight = mCameraHeight / 8f;
		mBoxHeight = 0;
		mShowEventBox = false;
		mEventEffectSwitch = false;
		
		/* Initialize ZOOM effect variables */
		mOrigCamWidth = mCameraWidth;
		mPrevCamWidth = mCameraWidth;
		mZoomAmount = 0;
		mZoomSpeed = 3f;
		mShowZoom = false;
		mIsDoneZooming = true;
		mZoomEffectSwitch = false;
		
		/* Initialize FADE effect variables */
		alpha = 0;
		mFadeSpeed = FADE_SPEED;
		mIsFadeIn = true;
		mFadeEffectSwitch = false;
		
		/* Initialize ENCOUNTER variable */
		mPrevZoomSpeed = mZoomSpeed;
		mIsDoneEncounter = true;
		mEncounterSwitch = false;
		
		/* Initialize mask variables */
		mMask = new JavaGTCS1Rectangle(focus, mCameraWidth, mCameraHeight);
		mMask.SetColor(new JavaGTCS1Color(0, 0, 0, 0));
	}
	
	/** Load objects inside Camera */
	public void Load()
	{
		mMask.AddToAutoDrawSet();
		mTopEventBox.AddToAutoDrawSet();
		mBottomEventBox.AddToAutoDrawSet();
	}
	
	/** Unload objects inside Camera */
	public void Unload()
	{
		mMask.RemoveFromAutoDrawSet();
		mTopEventBox.RemoveFromAutoDrawSet();
		mBottomEventBox.RemoveFromAutoDrawSet();
	}
	
	/**
	 * Update camera
	 * @param focus Camera focus
	 */
    public void UpdateCamera(Vector2 focus)
    {
    	/* Check for camera effect switches, true = ON, false = OFF */
    	if (mChaseEffectSwitch) ChaseEffect(focus);
    	if (mShiftEffectSwitch) ShiftEffect(focus);
    	if (mEventEffectSwitch) EventEffect();
    	if (mZoomEffectSwitch) ZoomEffect();
    	if (mFadeEffectSwitch) FadeEffect();
    	if (mEncounterSwitch) EncounterEffect(focus);
    	
    	/* Check if Chase or Shift effects are ON */
    	if (mChaseEffectSwitch || mShiftEffectSwitch)
    	{
    		Default(mEffectFocus);
    	}
    	else
    	{
    		Default(focus);
    	}
    	
    	/* Check if shake effect switch is ON */
    	if (mShakeEffectSwitch) ShakeEffect();
    	
    	/* Update mask to play well with Loading screen */
    	mMask.SetCenterX(mLowerLeft.X + mCameraWidth/2);
    	mMask.SetCenterY(mLowerLeft.Y + mCameraHeight/2);
    	mMask.TopOfAutoDrawSet();
    	
//        JavaGTCS1Base.EchoToTopStatus("Effect: " + mEffectMsg +
//        							  " E.Focus: " + (int)mEffectFocus.X + "," + (int)mEffectFocus.Y + ")" +
//        							  " Hero (" + (int)focus.X + "," + (int)focus.Y + ")");
    }
    
    /**
     * Camera default with no effect. Keep focus within map bounds.
     * @param focus Camera focus
     */
    private void Default(Vector2 focus)
    {
    	/* Determine side bounds */
    	float eastBound = mMapWidth - mCameraWidth / 2;
    	float westBound = mCameraWidth / 2;
    	float northBound = mMapHeight - mCameraHeight / 2;
    	float southBound = mCameraHeight / 2;
        
    	// Focus is inside bounds
        if (focus.X <= eastBound && focus.X >= westBound && focus.Y <= northBound && focus.Y >= southBound)
        {
        	mLowerLeft.X = focus.X - (mCameraWidth / 2f);
        	mLowerLeft.Y = focus.Y - (mCameraHeight / 2f);
        }
        // Focus out of bounds
        else
        {
	        // Reaching NE corner
	        if (focus.X > eastBound && focus.Y > northBound)
	        {
	        	mLowerLeft.X = (mMapWidth - mCameraWidth / 2f) - (mCameraWidth / 2f);
	    		mLowerLeft.Y = (mMapHeight - mCameraHeight / 2f) - (mCameraHeight / 2f);
	        }
	        // Reaching SE corner
	        else if (focus.X > eastBound && focus.Y < southBound)
	        {
	        	mLowerLeft.X = (mMapWidth - mCameraWidth / 2f) - (mCameraWidth / 2f);
	    		mLowerLeft.Y = mCameraHeight / 2f - (mCameraHeight / 2f);
	        }
	        // Reaching SW corner
	        else if (focus.X < westBound && focus.Y < southBound)
	        {
	        	mLowerLeft.X = mCameraWidth / 2f - (mCameraWidth / 2f);
	    		mLowerLeft.Y = mCameraHeight / 2f - (mCameraHeight / 2f);
	        }
	        // Reaching NW corner
	        else if (focus.X < westBound && focus.Y > northBound)
	        {
	        	mLowerLeft.X = mCameraWidth / 2f - (mCameraWidth / 2f);
	    		mLowerLeft.Y = (mMapHeight - mCameraHeight / 2f) - (mCameraHeight / 2f);
	        }
	        // Reaching east bound
	        else if (focus.X > eastBound && (focus.Y > southBound || focus.Y < northBound))
	    	{
	    		mLowerLeft.X = (mMapWidth - mCameraWidth / 2f) - (mCameraWidth / 2f);
	    		mLowerLeft.Y = focus.Y - (mCameraHeight / 2f);
	    	}
	        // Reaching west bound
	        else if (focus.X < westBound && (focus.Y > southBound || focus.Y < northBound))
	    	{
	    		mLowerLeft.X = mCameraWidth / 2f - (mCameraWidth / 2f);
	    		mLowerLeft.Y = focus.Y - (mCameraHeight / 2f);
	    	}
	        // Reaching south bound
	        else if (focus.Y < southBound && (focus.X < eastBound || focus.X > westBound))
	    	{
	    		mLowerLeft.X = focus.X - (mCameraWidth / 2f);
	    		mLowerLeft.Y = mCameraHeight / 2f - (mCameraHeight / 2f);
	    	}
	        // Reaching north bound
	        else if (focus.Y > northBound && (focus.X < eastBound || focus.X > westBound))
	    	{
	    		mLowerLeft.X = focus.X - (mCameraWidth / 2f);
	    		mLowerLeft.Y = (mMapHeight - mCameraHeight / 2f) - (mCameraHeight / 2f);
	    	}   	
        }
        
        /* Update camera position */
        JavaGTCS1Base.World.SetWorldCoordinates(mLowerLeft, mCameraWidth);
    }
    
    /**
     * Camera CHASE effect.
     * @param focus Camera focus
     */
    private void ChaseEffect(Vector2 focus)
    {
    	mEffectMsg = "Chase";
    	
    	// Copy original focus
    	if (mGetOrigFocus)
    	{
    		mEffectFocus = new Vector2(focus.X, focus.Y);
    		mGetOrigFocus = false;
    	}

    	// Gradually chase focus
    	mEffectFocus.X += (focus.X - mEffectFocus.X) * CHASE_SPEED;
    	mEffectFocus.Y += (focus.Y - mEffectFocus.Y) * CHASE_SPEED;
    }
    
    /**
     * Camera SHAKE effect.
     * @param focus Camera focus
     */
    private void ShakeEffect()
    {
    	mEffectMsg = "Shake";
    	
    	/* Shake for given duration */
    	if (mShakeTimer < mShakeDuration * REFRESH_RATE)
    	{
	    	switch (mShakeType)
	    	{
	    	case 0:		// Left-Right shake
	    		mLowerLeft.X += (JavaGTCS1Base.RandomFloat() * mShakeRadius * 2 - mShakeRadius);
		    	break;
	    	case 1:		// Up-Down shake
	    		mLowerLeft.Y += (JavaGTCS1Base.RandomFloat() * mShakeRadius * 2 - mShakeRadius);
		    	break;
	    	case 2:		// Random shake
	    		mLowerLeft.X += (JavaGTCS1Base.RandomFloat() * mShakeRadius * 2 - mShakeRadius);
	    		mLowerLeft.Y += (JavaGTCS1Base.RandomFloat() * mShakeRadius * 2 - mShakeRadius);					
	    		break;
	    	}
	    	JavaGTCS1Base.World.SetWorldCoordinates(mLowerLeft, mCameraWidth);
	    	mShakeTimer++;
    	}
    	/* Done shaking */
    	else 
    	{
    		mShakeTimer = 0;
			mShakeEffectSwitch = false;
		}
    }
    
    /**
     * Camera SHIFT effect.
     * @param focus Camera focus
     */
    private void ShiftEffect(Vector2 focus)
    {
    	mEffectMsg = "Shift";
    	
    	/* Set a previous focus to work on */
    	if (mGetOrigFocus)
    	{
    		mEffectFocus = new Vector2(focus.X, focus.Y);
    		mGetOrigFocus = false;
    	}
    	
    	/* Shift to given coordinate */
    	if (mShowShift)
    	{
    		/* Focus has not shifted completely */
    		if (Math.abs((int)mShiftFocus.X - (int)mEffectFocus.X) > mShiftSpeed || Math.abs((int)mShiftFocus.Y - (int)mEffectFocus.Y) > mShiftSpeed) 
    		{
    	    	/* Gradually shift focus to given coordinate */
    	    	mEffectFocus = Vector2.add(Vector2.multiply(Vector2.Normalize(Vector2.subtract(mShiftFocus, mEffectFocus)), mShiftSpeed), mEffectFocus);
    		}
    		/* Focus has shifted successfully */
    		else
    		{
    			mIsDoneShifting = true;
    		}
    	}
    	/* Shift back to original focus */
    	else 
    	{
    		/* Focus has not shifted back completely */
    		if (Math.abs((int)focus.X - (int)mEffectFocus.X) > mShiftSpeed || Math.abs((int)focus.Y - (int)mEffectFocus.Y) > mShiftSpeed)
    		{
    			/* Gradually shift focus back */
    			mEffectFocus = Vector2.add(Vector2.multiply(Vector2.Normalize(Vector2.subtract(focus, mEffectFocus)), mShiftSpeed), mEffectFocus);
    		}
    		/* Focus has shifted successfully */
    		else
    		{
    			mIsDoneShifting = true;
    			
    			/* Reset variables */
    			mShiftEffectSwitch = false;
    			mGetOrigFocus = true;
    			
				/* Return Chase effect switch to original state */
				if (mChaseEffectStatus)
					mChaseEffectSwitch = true;
    		}
		}
    }
    	
	/**
	 * Camera EVENT effect. 
	 * @param focus Camera focus
	 */
	private void EventEffect()
	{
		/* Set event box center */
		mTopEventBox.SetCenter(new Vector2(mLowerLeft.X + mCameraWidth / 2, mLowerLeft.Y + mCameraHeight));
		mBottomEventBox.SetCenter(new Vector2(mLowerLeft.X + mCameraWidth / 2, mLowerLeft.Y));		

		/* Set event box height */
		mTopEventBox.setHeight(mBoxHeight);
		mBottomEventBox.setHeight(mBoxHeight);
	
		/* Event effect is ON */
		if (mShowEventBox)
		{
			mEffectMsg = "Event ON";
			
			/* Gradually increase event box height */
			if (mBoxHeight < mMaxBoxHeight * 2)
			{
				mTopEventBox.AddToAutoDrawSet();
				mBottomEventBox.AddToAutoDrawSet();
				mBoxHeight++;
			}
			else
			{
				mBoxHeight = mMaxBoxHeight * 2;
			}
		}
		/* Event effect is OFF */
		else
		{
			mEffectMsg = "Event OFF";
			
			/* Gradually decrease event box height */
			if (mBoxHeight > 0)
			{
				mBoxHeight--;
			}
			else
			{
				mTopEventBox.RemoveFromAutoDrawSet();
				mBottomEventBox.RemoveFromAutoDrawSet();
				mEventEffectSwitch = false;
			}
		}		
	}
	
	/**
	 * Camera ZOOM effect.
	 * @param focus Camera focus
	 */
	private void ZoomEffect()
	{
		/* Zoom effect is ON */
		if (mShowZoom)
		{
			/* Gradually adjust camera width outward */
			if (mZoomAmount > 0)
			{
				mEffectMsg = "Zoom OUT";
				/* Zoom out and make sure it's within map width */
				if (mCameraWidth < (mPrevCamWidth + mZoomAmount) && mCameraWidth < mMapWidth)
				{
					mCameraWidth += mZoomSpeed;
					mCameraHeight = mCameraWidth * WORLD_RATIO;
				}
				/* Done zooming out, turn Zoom switch off */
				else 
				{
					mIsDoneZooming = true;
					mZoomEffectSwitch = false;
				}
			}
			/* Gradually adjust camera width inward */
			else if (mZoomAmount < 0)
			{
				mEffectMsg = "Zoom IN";
				/* Zoom in and make sure camera width not below 100 */
				if (mCameraWidth > (mPrevCamWidth + mZoomAmount) && mCameraWidth > 100)
				{
					mCameraWidth -= mZoomSpeed;
					mCameraHeight = mCameraWidth * WORLD_RATIO;
				}
				/* Done zooming in, turn Zoom switch off */
				else 
				{
					mIsDoneZooming = true;
					mZoomEffectSwitch = false;
				}
			}
			/* No zoom in/out needed, turn Zoom switch off */
			else 
			{
				mIsDoneZooming = true;
				mZoomEffectSwitch = false;
			}
		}
		/* Zoom effect OFF, return to original camera width */
		else
		{
			/* Check if current camera width not equals original width */
			if (mCameraWidth != mOrigCamWidth)
			{
				mZoomAmount = -(mCameraWidth - mOrigCamWidth);
				mShowZoom = true;
			}
			/* No zoom in/out needed, turn Zoom switch off */
			else 
			{
				mIsDoneZooming = true;
				mZoomAmount = 0;
				mCameraWidth = mOrigCamWidth;
				mCameraHeight = mCameraWidth * WORLD_RATIO;
				mZoomEffectSwitch = false;
			}
		}
	}
	
	/**
	 * Camera FADE effect.
	 */
	private void FadeEffect()
	{
		/* Perform fade in */
		if (mIsFadeIn)
		{
			if (alpha > 0)
			{
				alpha -= mFadeSpeed;
				SetCameraMask(255, 255, 255, alpha);
			}
			else
			{
				mFadeEffectSwitch = false;
			}
		}
		/* Perform fade out */
		else
		{
			if (alpha < 255)
			{
				alpha += mFadeSpeed;
				SetCameraMask(255, 255, 255, alpha);
			}
			else
			{
				mFadeEffectSwitch = false;
			}
		}
	}
	
	/**
	 * Camera ENCOUNTER effect.
	 */
	private void EncounterEffect(Vector2 focus)
	{
		if (mIsDoneZooming)
		{
			ResetZoom();
			ResetFade();
			mZoomSpeed = mPrevZoomSpeed;	// reset zoom speed
			Default(focus);					// reset focus
			mIsDoneEncounter = true;
			mEncounterSwitch = false;
		}
	}
	
    /**
     * Set camera default with no effects.
     */
    public void SetDefault()
    {
    	/* Turn all effect switches OFF */
    	mChaseEffectSwitch = false;
    	mShakeEffectSwitch = false;
    	mShiftEffectSwitch = false;
    	mEventEffectSwitch = false;
    	mTopEventBox.RemoveFromAutoDrawSet();
    	mBottomEventBox.RemoveFromAutoDrawSet();
    	mBoxHeight = 0;
    	mZoomEffectSwitch = false;
    	mGetOrigFocus = true;
    	mCameraWidth = mOrigCamWidth;
    }
    
    /**
     * Set camera default with no effects.
     */
    public void Reset()
    {
    	SetDefault();
    }
    
    /**
     * Set camera with CHASE effect. Camera slowly chasing target's focus.
     * @param chaseSwitch true = ON, false = OFF
     */
    public void SetChaseEffect(boolean effectSwitch)
    {
    	mChaseEffectSwitch = effectSwitch;
    }
    
    /**
     * Turn CHASE effect ON.
     */
    public void TurnChaseON()
    {
    	mChaseEffectSwitch = true;
    }
    
    /**
     * Turn CHASE effect OFF.
     */
    public void TurnChaseOFF()
    {
    	mChaseEffectSwitch = false;
    }
    
    /**
     * Set camera with SHAKE effect. Shake Left-Right, Up-Down, or Random (recommended).
     * @param type 0 = Left-Right, 1 = Up-Down, 2 = Random
     * @param duration Duration of shake (in seconds)
     * @param radius Radius of shake (recommended 10f)
     */
    public void SetShakeEffect(int type, int duration, float radius)
    {
    	mShakeEffectSwitch = true;
    	
    	/* Set shake type, duration, and radius */
    	if (type >= 0 && type <= 2)
    		mShakeType = type;
    	mShakeDuration = duration;
    	mShakeRadius = radius;
    }
    
    /**
     * Turn SHAKE effect ON. Shake Left-Right, Up-Down, or Random (recommended).
     * @param type 0 = Left-Right, 1 = Up-Down, 2 = Random
     * @param duration Duration of shake (in seconds)
     * @param radius Radius of shake (recommended 10f)
     */
    public void TurnShakeON(int type, int duration, float radius)
    {
    	SetShakeEffect(type, duration, radius);
    }
    
    /**
     * Turn SHAKE effect ON, with random shake, 10 sec duration, and 10f radius.
     * @param duration
     */
    public void TurnShakeON(int duration)
    {
    	SetShakeEffect(2, duration, 10f);
    }
    
    /**
     * Turn SHAKE effect ON, with random shake, max duration, and 10f radius.
     */
    public void TurnShakeON()
    {
    	SetShakeEffect(2, Integer.MAX_VALUE, 10f);
    }
    
    /**
     * Turn SHAKE effect off.
     */
    public void TurnShakeOFF()
    {
    	mShakeEffectSwitch = false;
    }
    
    /**
     * Set camera with SHIFT effect. When set to <code>true</code>, camera focus slowly 
     * shifts to the coordinate given. When set to <code>false</code>, camera focus slowly
     * shifts back to original target. Chase effect is temporarily turned OFF while 
     * shifting (if it was ON).
     * @param effectSwitch true = ON, false = OFF (When OFF, shiftFocus is not needed 
     * 					   and is set to Vector2.Zero).
     * @param shiftFocus The coordinate to shift to
     * @param shiftSpeed Speed of shift (recommended at 2f)
     */
    public void SetShiftEffect(boolean effectSwitch, Vector2 shiftFocus, float shiftSpeed)
    {
    	if (effectSwitch)
    	{
	    	mShiftEffectSwitch = true;
	    	mShowShift = true;
	    	mShiftFocus = new Vector2(shiftFocus.X, shiftFocus.Y);
	    	mShiftSpeed = shiftSpeed;
			
	    	/* Turn off Chase effect temporarily if is currently ON */
			if (mChaseEffectSwitch)
			{
				mChaseEffectStatus = true;
				mChaseEffectSwitch = false;
			}
    	}
    	else
    	{
    		mShowShift = false;
    		mShiftFocus = Vector2.Zero;
    	}
    	mIsDoneShifting = false;
    }
    
    /**
     * Set Camera's shift speed.
     * @param shiftSpeed
     */
    public void SetShiftSpeed(float shiftSpeed)
    {
    	mShiftSpeed = shiftSpeed;
    }
    
    /**
     * Turn SHIFT effect ON.
     * @param shiftFocus focus to shift
     */
    public void TurnShiftON(Vector2 shiftFocus)
    {
    	mShiftEffectSwitch = true;
    	mShowShift = true;
    	mShiftFocus = new Vector2(shiftFocus.X, shiftFocus.Y);
    	mIsDoneShifting = false;
    	
    	/* Turn off Chase effect temporarily if is currently ON */
		if (mChaseEffectSwitch)
		{
			mChaseEffectStatus = true;
			mChaseEffectSwitch = false;
		}
    }
    
    /**
     * Turn SHIFT effect OFF.
     */
    public void TurnShiftOFF()
    {
		mShowShift = false;
		mShiftFocus = Vector2.Zero;
		mIsDoneShifting = false;
    }
    
    /**
     * @return True = done shifting, False = not done shifting
     */
    public boolean IsDoneShifting()
    {
    	return mIsDoneShifting;
    }
    
    /**
     * Set camera with EVENT effect. This adds black transparent borders on Top and Bottom.
     * @param eventSwitch true = ON, false = OFF
     */
	public void SetEventEffect(boolean effectSwitch)
	{
		if (effectSwitch) {
			mEventEffectSwitch = true;
			mShowEventBox = true;
		}
		else {
			mShowEventBox = false;
		}
	}
	
	/**
	 * Turn EVENT effect ON.
	 */
	public void TurnEventON()
	{
		mEventEffectSwitch = true;
		mShowEventBox = true;
	}
	
	/**
	 * Turn EVENT effect OFF.
	 */
	public void TurnEventOFF()
	{
		mShowEventBox = false;
	}
	
	/**
	 * Hide event boxes completely.
	 */
	public void HideEventBox()
	{
		mTopEventBox.RemoveFromAutoDrawSet();
		mBottomEventBox.RemoveFromAutoDrawSet();
		mEventEffectSwitch = false;
		mShowEventBox = false;
	}
	
	/**
	 * To determine whether or not the camera has event mode on.
	 * @return Boolean of whether or not it is in event mode.
	 */
	public boolean IsEventMode( )
	{
		return mShowEventBox;
	}
	
	/**
	 * Set camera with ZOOM effect. Camera is able to Zoom IN, OUT, and OFF.
	 * @param zoomSwitch true = ON, false = OFF (OFF will return camera to original width and height)
	 * @param zoomAmt Amount to zoom (positive to zoom out, negative to zoom in, 0 when <code>zoomSwitch</code> is OFF)
	 * @param zoomSpeed Speed of zoom (larger the faster, recommend 3f)
	 */
	public void SetZoomEffect(boolean effectSwitch, float zoomAmt, float zoomSpeed)
	{
		if (effectSwitch) {
			mZoomEffectSwitch = effectSwitch;
			mShowZoom = true;
		}
		else {
			mZoomEffectSwitch = true;
			mShowZoom = false;
		}
		mPrevCamWidth = mCameraWidth;
		if (effectSwitch == true)
			mZoomAmount = zoomAmt;
		else 
			mZoomAmount = 0;
		if (zoomSpeed < 0)
			zoomSpeed = 0;
		mZoomSpeed = zoomSpeed;
		mIsDoneZooming = false;
	}
	
	/**
	 * Set Camera's zoom speed.
	 * @param zoomSpeed
	 */
	public void SetZoomSpeed(float zoomSpeed)
	{
		mZoomSpeed = zoomSpeed;
	}
	
	/**
	 * Turn ZOOM effect ON.
	 * @param zoomAmt amount of zoom (negative to zoom in, positive to zoom out)
	 */
	public void TurnZoomON(float zoomAmt)
	{
		mZoomEffectSwitch = true;
		mShowZoom = true;
		mPrevCamWidth = mCameraWidth;
		mZoomAmount = zoomAmt;
		mIsDoneZooming = false;
	}
	
	/**
	 * Turn ZOOM effect OFF. Camera returns to original size.
	 */
	public void TurnZoomOFF()
	{
		mZoomEffectSwitch = true;
		mShowZoom = false;
		mPrevCamWidth = mCameraWidth;
		mZoomAmount = 0;
		mIsDoneZooming = false;
	}
	
	public void ResetZoom()
	{
		mZoomEffectSwitch = false;
		mIsDoneZooming = true;
		mCameraWidth = mOrigCamWidth;
		mCameraHeight = mCameraWidth * WORLD_RATIO;
	}
	
	/**
	 * @return True = done zooming, False = not done zooming
	 */
	public boolean IsDoneZooming()
	{
		return mIsDoneZooming;
	}
	
	/**
	 * Camera fades in gradually.
	 */
	public void FadeOut()
	{
		alpha = 0;
		mIsFadeIn = false;
		mFadeEffectSwitch = true;
	}
	
	/**
	 * Camera fades out gradually.
	 */
	public void FadeIn()
	{
		alpha = 255;
		mIsFadeIn = true;
		mFadeEffectSwitch = true;
	}
	
	/**
	 * Remove and reset Camera fade effect.
	 */
	public void ResetFade()
	{
		SetCameraMask(0, 0, 0, 0);
		mFadeEffectSwitch = false;
	}
	
	/**
	 * Set Camera's fade speed
	 * @param fadeSpeed Fade speed
	 */
	public void SetFadeSpeed(int fadeSpeed)
	{
		mFadeSpeed = fadeSpeed;
	}
	
	/**
	 * Turn Camera's ENCOUNTER effect ON. Camera will zoom in and do fade in effect.
	 * Use {@link #IsDoneEncounter()} to determine if effect is done.
	 */
	public void TurnEncounterON()
	{
		mPrevZoomSpeed = mZoomSpeed;	// keep a record of current zoom speed
		mZoomSpeed = 30f;				// set for fast zoom in effect
		TurnZoomON(Integer.MIN_VALUE);
		FadeOut();
		
		mIsDoneEncounter = false;
		mEncounterSwitch = true;
	}
	
	public void TurnEncounterON(int i)
	{
		mPrevZoomSpeed = mZoomSpeed;	// keep a record of current zoom speed
		mZoomSpeed = 30f;				// set for fast zoom in effect
		TurnZoomON(i);
		FadeOut();
		
		mIsDoneEncounter = false;
		mEncounterSwitch = true;
	}
	
	/**
	 * @return True = encounter effect done, False = not done
	 */
	public boolean IsDoneEncounter()
	{
		return mIsDoneEncounter;
	}
	
    /**
     * Set width of the camera.
     * @param cameraWidth Camera width
     */
    public void SetCameraWidth(float cameraWidth)
    {
    	mCameraWidth = cameraWidth;
    	mCameraHeight = cameraWidth * WORLD_RATIO;
    }
    
    /**
     * Set width of the map.
     * @param mapWidth Map width
     */
    public void SetMapWidth(float mapWidth)
    {
    	mMapWidth = mapWidth;
    }
    
    /**
     * Set height of the map.
     * @param mapHeight	Map height
     */
    public void SetMapHeight(float mapHeight)
    {
    	mMapHeight = mapHeight;
    }
    
    /**
     * Set camera mask (need only alpha color).
     * @param Alpha Alpha color
     */
	public void SetCameraMaskAlpha(int Alpha)
	{
		mMask.SetColor(new JavaGTCS1Color(0, 0, 0, Alpha));
	}
	
	public void SetCameraMaskR(int R)
	{
		mMask.SetColor(new JavaGTCS1Color(R, 0, 0, 0));
	}
	
	public void SetCameraMaskG(int G)
	{
		mMask.SetColor(new JavaGTCS1Color(0, G, 0, 0));
	}
	
	public void SetCameraMaskB(int B)
	{
		mMask.SetColor(new JavaGTCS1Color(0, 0, B, 0));
	}
	
	public void SetCameraMask(int R, int G, int B, int Alpha)
	{
		mMask.SetColor(new JavaGTCS1Color(R, G, B, Alpha));
	}
	
	/**
	 * Set label for camera mask.
	 * @param text Label
	 */
	public void SetCameraMaskLabel(String text)
	{
		mMask.SetLabel(text);
	}
	
	/**
	 * Set label color for camera mask.
	 * @param color Label color
	 */
	public void SetCameraMaskLabelColor(JavaGTCS1Color color)
	{
		mMask.SetLabelColor(color);
	}
	
	/**
	 * @return Returns camera's lower left coordinate
	 */
	public Vector2 GetLowerLeft()
	{
		return mLowerLeft;
	}
	
	/**
	 * @return Returns camera's width.
	 */
	public float GetCameraWidth()
	{
		return mCameraWidth;
	}
	
	/**
	 * @return Returns camera's height.
	 */
	public float GetCameraHeight()
	{
		return mCameraHeight;
	}
	
	public float GetMapWidth()
	{
		return mMapWidth;
	}
	
	public float GetMapHeight()
	{
		return mMapHeight;
	}
	
	public Vector2 GetCenter()
	{
		return Vector2.add(mLowerLeft, new Vector2(mCameraWidth/2, mCameraHeight/2));
	}
	
	public float GetTopX()
	{
		return mLowerLeft.X + mCameraWidth;
	}
	
	public float GetTopY()
	{
		return mLowerLeft.Y + mCameraHeight;
	}

	public void SetMaskLabelSize(float size)
	{
		mMask.SetLabelSize(size);
	}
}
