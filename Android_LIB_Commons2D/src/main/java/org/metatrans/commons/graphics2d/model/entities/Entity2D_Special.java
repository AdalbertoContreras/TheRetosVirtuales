package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.RectF;


public abstract class Entity2D_Special extends Entity2D_Base {
	
	
	private static final long serialVersionUID = 3348242354945602637L;
	
	
	public Entity2D_Special(RectF _evelop, int _subtype) {
		
		super(null, _evelop, _subtype);
	}
	
	
	@Override
	public int getType() {
		return TYPE_SPECIAL;
	}
}
