package org.metatrans.commons.cfg.publishedapp;


import org.metatrans.commons.R;
import org.metatrans.commons.cfg.appstore.IAppStore;


public class PublishedApplication_BagaturChess extends PublishedApplication_Base {
	
	
	public PublishedApplication_BagaturChess(IAppStore _store) {
		super(_store);
	}
	
	
	public PublishedApplication_BagaturChess(IAppStore _store, String _app_storeID) {
		super(_store, null, _app_storeID);
	}
	
	
	public PublishedApplication_BagaturChess(IAppStore _store, String _app_storeID, IPublishedApplication _socialVersion) {
		super(_store, _socialVersion, _app_storeID);
	}
	
	
	public PublishedApplication_BagaturChess(IAppStore _store, IPublishedApplication _socialVersion) {
		super(_store, _socialVersion, null);
	}
	
	
	@Override
	public String getPackage() {
		return "com.bagaturchess";
	}
	
	
	@Override
	public int getName() {
		return R.string.app_bagaturchess_name;
	}
	
	
	@Override
	public int getIconResID() {
		return R.drawable.ic_logo_bagaturchess;
	}
	
	
	@Override
	public int getDescription_Line1() {
		return R.string.app_bagaturchess_advertising1;
	}
	
	
	@Override
	public int getDescription_Line2() {
		return R.string.app_bagaturchess_advertising2;
	}
}
