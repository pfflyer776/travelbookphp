package com.webalcove.travelbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class TravelAddPicActivity extends FragmentActivity {
	
	private TravelActivity mOther;
    
	protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment,"PICTURE").commit();
		}        
    }
    
    protected Fragment createFragment() {
    	    	
    	// instead of new TravelAddPicFragment() could do Fragemnt.newInstance to pass extras
        return new TravelAddPicFragment();
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    	// this calls the fragments onActivityRequest
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	// see where it goes
    }
}
