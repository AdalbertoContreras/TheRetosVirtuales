package org.metatrans.commons.graphics2d.menu;




import org.metatrans.commons.cfg.ConfigurationEntry_Base;
import org.metatrans.commons.graphics2d.R;


public class Config_Joystick_MoveLeft extends ConfigurationEntry_Base implements IConfigurationJoystick {

	
	@Override
	public int getID() {
		return MOVE_LEFTJOYSTICK;
	}

	@Override
	public int getName() {
		return R.string.menu_joystick_moveleft;
	}

	@Override
	public int getIconResID() {
		return R.drawable.joystick_transparent;
	}
}
