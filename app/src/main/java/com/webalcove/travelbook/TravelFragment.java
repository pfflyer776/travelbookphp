package com.webalcove.travelbook;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class TravelFragment extends Fragment {
	private static final String TAG = "TravelFragment";
	public static final String EXTRA_TRAVEL_ID = "com.webalcove.travelbook.travel_id";
	private static final String IMAGE_DATA_EXTRA = "resId";
	private static final int REQUEST_PICTURE = 10;

    TextView mTravelDesc;
    TextView mImageDesc;
    ImageView selectedImage;
    int mImageNum;  
    int mPlacePos;
    
    String mPlace;
    int mId;
    
    int mTravelSize;
    TravelImages oTravelImage;
    
    private Callbacks mCallbacks;
	
    // interface to host activities, implemented in TravelListActivity, used for updating data
    // from controls used here like EditText or CheckBox
    public interface Callbacks {
        void onTravelUpdated(Travel travel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);   
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    } 	
    
  
    // attach arguments bundle to a fragment, after fragment is created but before it is added to an activity
    public static TravelFragment newInstance(int imageNum, int mExtraId, String mExtraPlace, int mExtraPlacePos ) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_TRAVEL_ID, travelId);
        
        args.putInt(IMAGE_DATA_EXTRA, imageNum);
        args.putInt("ID", mExtraId);
        args.putString("PLACE", mExtraPlace);
        args.putInt("PLACEPOS", mExtraPlacePos);

        TravelFragment fragment = new TravelFragment();
        fragment.setArguments(args);

        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        
        // access putExtras
        if (getArguments() != null) {
        	// which image to display
        	mImageNum = getArguments().getInt(IMAGE_DATA_EXTRA);
        	
            mTravelSize = TravelLab.get(getActivity()).getTravelImagesSize();
        	
        	// used for adding picture
        	mPlace = getArguments().getString("PLACE");
        	mId = getArguments().getInt("ID");
        } else {
        	mImageNum = 0;
        	mPlace = "";
        	mId = 0;
        }
        
        // turn on options menu handling (up button)
        setHasOptionsMenu(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_travel, parent, false);
        String mDescript;
        
        mImageDesc = (TextView)v.findViewById(R.id.image_desc);

        if (mTravelSize > 0)
        	oTravelImage = TravelLab.get(getActivity()).getTravelImage(mImageNum);

        if (mTravelSize == 0) {
        	mDescript = "No Images";
        } else {
        	mDescript = oTravelImage.getDescript();
        }

        mImageDesc.setText(mDescript);
        
        selectedImage=(ImageView) v.findViewById(R.id.imageView1);    
    
        return v;
    }
    
    // this gets called twice when viewpager first created so can swipe quickly
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // not working for tablet
        if (TravelActivity.class.isInstance(getActivity())) {

        	if (mTravelSize > 0) {
        		String urlLink = oTravelImage.getURL();
	            urlLink = urlLink.replaceAll(" ", "%20");
	            Log.d("Link: ", urlLink);
	            Picasso.with(getActivity()).load(urlLink).resize(100,100).into(selectedImage);
        	}
            
        	//((TravelActivity) getActivity()).loadBitmap(fn, selectedImage);
        }
    }
    
    // inflate add button to appear
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_travel_image, menu);
    }
    
    // responding to the app icon (home) menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.menu_item_new_image:
				
				Intent i = new Intent(getActivity(), TravelAddPicActivity.class);
				i.putExtra("ID", mId);
				i.putExtra("PLACE", mPlace);
				startActivityForResult(i, REQUEST_PICTURE);
				return true;				
            case android.R.id.home:
            	if (NavUtils.getParentActivityName(getActivity()) != null) {
            		NavUtils.navigateUpFromSameTask(getActivity());
            	}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } 
    } 
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQUEST_PICTURE) {
    		 		
    	}
    }    
}
