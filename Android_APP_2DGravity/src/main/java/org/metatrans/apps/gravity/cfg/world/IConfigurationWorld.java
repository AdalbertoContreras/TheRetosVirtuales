package org.metatrans.apps.gravity.cfg.world;


import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;


public interface IConfigurationWorld extends IConfigurationDifficulty {
	
	public float getSpaceMultiplier();
	public float getMaxSpeed();
	
	public String getName_String();
	public String getDescription_String();
	
}
