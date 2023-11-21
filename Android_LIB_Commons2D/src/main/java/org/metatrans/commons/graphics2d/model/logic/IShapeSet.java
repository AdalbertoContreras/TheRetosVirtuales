package org.metatrans.commons.graphics2d.model.logic;


import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.entities.IEntity2D;

import java.util.List;


public interface IShapeSet {
	
	public void intersect(List<IEntity2D> result, RectF test, boolean stop_after_first);
}
