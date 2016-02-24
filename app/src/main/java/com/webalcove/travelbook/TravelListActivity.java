package com.webalcove.travelbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

// onCreate in SingleFragmentActivity, extends FragmentActivity
public class TravelListActivity extends AppCompatActivity implements TravelListFragment.Callbacks, TravelFragment.Callbacks {
	
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(getLayoutResId());
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			fragment = new TravelListFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}	
	
	public void onTravelSelected(Travel travel, int pos) {
	    // start an instance of TravelActivity
        Intent i = new Intent(this, TravelActivity.class);
            
        // putExtra is retrieved in TravelFragment using getActivity() during onCreate
        //i.putExtra(TravelFragment.EXTRA_TRAVEL_ID, travel.getId());
		int myId = travel.getId();
        i.putExtra("TRAVELID",  myId);
        i.putExtra("TRAVELPOS", pos);
            
        // place
        i.putExtra("TRAVELPLACE", travel.getTitle());
        startActivityForResult(i, 0);
	}
	
	// re-load list for tablet when save changes to travel
    public void onTravelUpdated(Travel travel) {
        FragmentManager fm = getSupportFragmentManager();
        TravelListFragment listFragment = (TravelListFragment)
                fm.findFragmentById(R.id.fragmentContainer);
        if (listFragment != null) {
        	listFragment.updateUI();
        }
    }


	@Override
	public void onPause() {
		super.onPause();
		//	((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
    @Override
    public void onResume() {
    	super.onResume();
    //	((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

}
