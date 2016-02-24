package com.webalcove.travelbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TravelAddActivity extends SingleFragmentActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }		

	@Override
	protected Fragment createFragment() {
	//	return new TravelAddFragment();
		// since fragment is serialized need to start fragment differently
		//UUID travelId = (UUID) getIntent().getSerializableExtra(TravelAddFragment.EXTRA_TRAVEL_ID);
		//int travelId = getIntent().getExtras().getInt("TRAVELID");
		int arrayPos = getIntent().getExtras().getInt("ARRAYPOS");
		
		return TravelAddFragment.newInstance(arrayPos);
	}

}
