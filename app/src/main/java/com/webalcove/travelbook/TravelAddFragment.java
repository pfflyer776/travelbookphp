package com.webalcove.travelbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TravelAddFragment extends Fragment {
	private static final String TAG = "TravelAddFragment";
	public static final String EXTRA_TRAVEL_ID = "com.webalcove.travelbook.travel_id";
	
	private ArrayList<Travel> mTravels;

    Travel mTravel;
    Button mDone;
    EditText mTravelField;
    int mTravelId;
    String mTravelContent;
    
    JSONParser jsonParser = new JSONParser();
    
    //url to create new product
    private static String url_create_location = "http://www.webalcove.com/places/create_location.php";
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    
    // attach arguments bundle to a fragment, after fragment is created but before it is added to an activity
    public static TravelAddFragment newInstance(int arrayPos) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_TRAVEL_ID, travelId);
        args.putInt("ARRAYPOS", arrayPos);

        TravelAddFragment fragment = new TravelAddFragment();
        fragment.setArguments(args);

        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // access fragments arguments
        //UUID travelId = (UUID)getArguments().getSerializable(EXTRA_TRAVEL_ID);
        //int mPos = getActivity().getIntent().getExtras().getInt("ARRAYPOS");
        int mPos = getArguments().getInt("ARRAYPOS");
        
        //mTravel = TravelListFragment.mTravels.get(mPos);
        mTravels = TravelLab.get(getActivity()).getTravels();
        mTravel = mTravels.get(mPos);
        mTravelId = mTravel.getId();
        
        //mTravel = TravelLab.getTravel(travelId);
        
        // turn on options menu handling (up button)
        setHasOptionsMenu(true);
    }
    
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, parent, false);
        
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//        	if (NavUtils.getParentActivityName(getActivity()) != null) {
//        		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//        	}
//        }
        
        mTravelField = (EditText)v.findViewById(R.id.travel_location);
        //mTravelField.setText(mTravel.getTitle());
        mTravelField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mTravel.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        
        mDone = (Button)v.findViewById(R.id.bDone);
        mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// add location here to web site
                mTravelContent = mTravelField.getText().toString();
				CreateLocation loc = new CreateLocation();
				loc.execute();
			}
		});        
    
        return v;
    }
    
    public class CreateLocation extends AsyncTask<String, String, String> {
    	/**
         * Creating location
         * */
        protected String doInBackground(String... args) {
            String id = Integer.toString(mTravelId);
            String place = mTravelContent;
 
            // Building Parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("id", id));
//            params.add(new BasicNameValuePair("place", place));

            HashMap<String, String> params = new HashMap<>();
            params.put("id",id);
            params.put("place",place);
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_location,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created location
    				getActivity().setResult(Activity.RESULT_OK);
    				getActivity().finish();
                } else {
                    // failed to create product
    				getActivity().setResult(Activity.RESULT_CANCELED);
    				getActivity().finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }        	
        	return null;
        }	
    }
	
    @Override
    public void onPause() {
        super.onPause();
        //TravelLab.get(getActivity()).saveTravels();
    } 
}
