package com.webalcove.travelbook;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

// was FragmentActivity
public class TravelActivity extends AppCompatActivity implements TravelFragment.Callbacks {
	public static final String EXTRA_IMAGE = "extra_image";
	
	private ImagePagerAdapter mAdapter;
	private ViewPager mViewPager;
	
    private ArrayList<Travel> mTravels;
	private ArrayList<TravelImages> mTravelImages;
	
	static int mId;
	static String mPlace;
	
	static int mPlacePos;
       
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		// this code should occur in FragmentStatePagerAdapter instantiateItem call
//		FragmentManager fm = getSupportFragmentManager();
//		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
//		
//		if (fragment == null) {
//			fragment = new TravelFragment();
//			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
//		}
		
		mTravels = TravelLab.get(this).getTravels();
        
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);	
		
		//setContentView(R.layout.activity_pager);
		
		if (savedInstanceState == null) {
			mPlacePos = getIntent().getExtras().getInt("TRAVELPOS");
			mId = getIntent().getExtras().getInt("TRAVELID");
			mPlace = getIntent().getExtras().getString("TRAVELPLACE");
		}
		
		// get your data from TravelLab from array of travels
		//mTravelImages = TravelListFragment.mTravels.get(mPlacePos).loadTravelImages();
		mTravelImages = mTravels.get(mPlacePos).loadTravelImages();
		
		//mViewPager = (ViewPager)findViewById(R.id.viewPager);
				
		// set adapter to unnamed instance
		//mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mTravelImages.size());
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), TravelLab.get(this).getTravelImagesSize());
		//ViewPager mViewPager = (ViewPager)findViewById(R.id.myViewPager);
		mViewPager.setAdapter(mAdapter);

		
		// replace activity title with current travel
//		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			public void onPageScrollStateChanged(int state) { }
//			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
//			
//			public void onPageSelected(int pos) {
//				Travel travel = mTravels.get(pos);
//				if (travel.getTitle() != null) {
//					setTitle(travel.getTitle());
//				}
//			}
//		});	
	} 
	
	public void myAdapterChange() {
		//mAdapter.mSize = mTravelImages.size();
		mAdapter.mSize = TravelLab.get(this).getTravelImagesSize();		
		mAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(mAdapter.mSize);
	}
		
	// FragmentStatePagerAdapter more frugal on memory
	public static class ImagePagerAdapter extends FixedFragmentStatePagerAdapter {
	    private int mSize;

	    public ImagePagerAdapter(FragmentManager fm, int size) {
	        super(fm);
        	mSize = size;
	    }
	    
	    @Override
	    public int getItemPosition(Object object) {
	    	// reload all fragments, make notifyDataSetChanged() work   	
	    	return POSITION_NONE;
	    }

	    // can't be zero
	    @Override
	    public int getCount() {
	    	if (mSize == 0) {
	    			return 1;
	    	} else {
	    		return mSize;
	    	}
	    }

	    @Override
	    public Fragment getItem(int position) {  	
	    	// instaniate TravelFragment
	        return TravelFragment.newInstance(position, mId, mPlace, mPlacePos);
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putInt("id", mId);
	  savedInstanceState.putString("place", mPlace);	
	  savedInstanceState.putInt("placepos", mPlacePos);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  mId = savedInstanceState.getInt("id");
	  mPlace = savedInstanceState.getString("place");
	  mPlacePos = savedInstanceState.getInt("placepos");
	}  
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// see if it makes it here
    	//super.onActivityResult(requestCode, resultCode, data);
    	this.myAdapterChange();
    }	
	  
    public void onTravelUpdated(Travel travel) {
        // do nothing        
    }	   
}
