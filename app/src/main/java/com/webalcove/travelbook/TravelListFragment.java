package com.webalcove.travelbook;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ListFragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TravelListFragment extends ListFragment {
	private ArrayList<Travel> mTravels;
	//private ArrayList<Travel> mMyTravels;
	
    private Callbacks mCallbacks;
    
    TravelAdapter mAdapter;

	private ProgressDialog pDialog;

	private static String url_remove_location = BuildConfig.URLREMOVE;
	JSONParser jsonParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
    
    // onTravelSelected implemented in TravelListActivity
    public interface Callbacks {
        void onTravelSelected(Travel travel, int position);
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
    
    // update list on tablet when changes made to travel
    public void updateUI() {
        ((TravelAdapter)getListAdapter()).notifyDataSetChanged();
    }    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		// keep fragment across activity recreation
		setRetainInstance(true);
        
        // override title of manifest for this activity
        getActivity().setTitle(R.string.travel_title);
               
        // create an empty adapter
        mAdapter = new TravelAdapter(getActivity());
        setListAdapter(mAdapter);
        
        mTravels = TravelLab.get(getActivity()).getTravels();
    }
    
    // inflate internal android list and register it for ContextMenu
    // create view
    @TargetApi(11)
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_list, container, false);

		// needed for delete to work
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		registerForContextMenu(listView);
    	
    	return v;
	}

    @Override
    public void onStart() {
        super.onStart();
        TravelLab.get(getActivity()).clearTravels();
        LoadUrl myURL = new LoadUrl();
        myURL.execute();
    }

    // open fragment when click on list
	public void onListItemClick(ListView l, View v, int position, long id) {
        // get the Travel from the adapter
        Travel t = ((TravelAdapter)getListAdapter()).getItem(position);
        
        // load TravelImages here !!!
		TravelLab.get(getActivity()).clearImages();
        ArrayList<TravelImages> mTravelImages = t.loadTravelImages();
        for (int i = 0; i < mTravelImages.size(); i++) {
        	TravelLab.get(getActivity()).addTravelImages(mTravelImages.get(i));
        }
        
        // start TravelActivity (in cell phone) or create fragment on tablet
        // call in TraveListActivity
        mCallbacks.onTravelSelected(t, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// refresh list after add location
    	if (resultCode == Activity.RESULT_OK)
    		// refresh list for tablet
    		((TravelAdapter)getListAdapter()).notifyDataSetChanged();
    	else {
    		// update of add place didn't occur
    	}
    		
    }
    
    // inflate delete menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	getActivity().getMenuInflater().inflate(R.menu.travel_list_item_context, menu);
    }

    public class TravelAdapter extends ArrayAdapter<Travel> {
    	private ArrayList<Travel> mEntries = new ArrayList<Travel>();
    	 
        private LayoutInflater mLayoutInflater;  
      
        public TravelAdapter(Context context) {
        	super(context, android.R.layout.simple_selectable_list_item);
        	mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        @Override
        public int getCount() {
           return mEntries.size();
        }
  
        @Override
        public Travel getItem(int position) {
           return mEntries.get(position);
        }
  
        @Override
        public long getItemId(int position) {
           return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {                                           
	        // if we weren't given a view, inflate one
	        if (null == convertView) {
	        	convertView = mLayoutInflater.inflate(R.layout.list_item_travel, null);
	        }
	
	        // configure the view for this Travel
	        Travel t = mEntries.get(position);
	
	        // update text in list
	        TextView titleTextView =
	            (TextView)convertView.findViewById(R.id.travel_list_item_titleTextView);
	        titleTextView.setText(t.getTitle());
	
	        return convertView;
        }
        
        public void upDateEntries(ArrayList<Travel> mTravels) {
        	mEntries = mTravels;
        	notifyDataSetChanged();
        }
               
    }

	@Override
	public void onPause() {
		super.onPause();
	}
    
    // make list show update made after it was modified
    @Override
    public void onResume() {
    	super.onResume();
    	((TravelAdapter)getListAdapter()).notifyDataSetChanged();
    }
    
    // inflate add button to appear
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_travel_list, menu);
    }

    // when push add button
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Travel mTravel;
		
		switch (item.getItemId()) {
		case R.id.menu_item_new_travel:
			
			// will need to get last id and increment it
			int nPos = 0;
			
			int lastPos = mTravels.size()-1;
			if (lastPos != 0) {
				mTravel = mTravels.get(lastPos);
				nPos = mTravel.getId();
				nPos++;
			}
			
			mTravel = new Travel();
			mTravel.setId(nPos);

			TravelLab.get(getActivity()).addTravel(mTravel);
			lastPos = TravelLab.get(getActivity()).getTravelSize() - 1;
			Intent i = new Intent(getActivity(), TravelAddActivity.class);
			i.putExtra("ARRAYPOS", lastPos);
			// when returns will call onActivityResult()
			startActivityForResult(i, 0);
		
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// when push delete button
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;

        Travel travel = mAdapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_travel:

            // remove from TravelLab
			TravelLab.get(getActivity()).deleteTravel(travel);
            mAdapter.upDateEntries(mTravels);

            // remove from Cloud
			RemoveLocation locRemove = new RemoveLocation();
			int myId = travel.getId();
			locRemove.execute(Integer.toString(myId));

			return true;
		}
		return super.onContextItemSelected(item);
	}   
    
	public class LoadUrl extends AsyncTask<Object, String, String> {
		
	    // url to get all images list
		private String url_places = BuildConfig.URLPLACES;
	    private String url_images = BuildConfig.URLIMAGES;
	    
	    // JSON Node names
	    private static final String TAG_SUCCESS = "success";
	    private static final String TAG_IMAGES = "images";
	    private static final String TAG_ID = "id";
	    private static final String TAG_PLACES = "places";
	    private static final String TAG_PLACE = "place";
	    private static final String TAG_LINKIMAGE = "linkimage";
	    private static final String TAG_DESCRIPT = "description";    
		
	    // Creating JSON Parser object
	    JSONParser jParser = new JSONParser();	
	    
	    // images JSONArray
	    JSONArray images = null;    
	    JSONArray places = null;

		String errMessage = "";
		
	    /**
	     * Before starting background thread Show Progress Dialog
	     * */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	    }
	    
	    /**
	     * getting All images from url
	     * @param mTravels 
	     * */
	    protected String doInBackground(Object... args) {

			HashMap<String, String> params = new HashMap<>();

	        // getting JSON string from URL
	        JSONObject json = jParser.makeHttpRequest(url_places, "GET", params);
            if (!jParser.errMessage.isEmpty())
                errMessage = jParser.errMessage;
	        JSONObject jImage = jParser.makeHttpRequest(url_images, "GET", params);
            if (!jParser.errMessage.isEmpty())
                errMessage = jParser.errMessage;
	        
	        Travel mTravel;

	        // Check your log cat for JSON response
            if (json != null) {
                Log.d("All Places: ", json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int successPlace = json.getInt(TAG_SUCCESS);

                    if (successPlace == 1) {
                        // locations found
                        // Getting Array of Images
                        places = json.getJSONArray(TAG_PLACES);
                        images = jImage.getJSONArray(TAG_IMAGES);
                        Log.d("length array: ", new Integer(places.length()).toString());

                        // looping through All places
                        for (int i = 0; i < places.length(); i++) {
                            JSONObject p = places.getJSONObject(i);

                            // Storing each json item in variable
                            int id = p.getInt(TAG_ID);
                            String place = p.getString(TAG_PLACE);

                            mTravel = new Travel();
                            mTravel.setId(id);
                            mTravel.setTitle(place);

                            // getting images for current place
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject c = images.getJSONObject(j);

                                int imageId = c.getInt(TAG_ID);
                                if (imageId == id) {
                                    String image = c.getString(TAG_LINKIMAGE);
                                    String descript = c.getString(TAG_DESCRIPT);
                                    mTravel.addImage(image, descript);
                                }
                            }
                            TravelLab.get(getActivity()).addTravel(mTravel);

                        }
                    } else {
                        errMessage = "Location not found.";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

	        // type is available in onPostExecute in parameter (not used here)
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(String mNotUsed) {
            if (!errMessage.isEmpty())
                Toast.makeText(getActivity(),"Error: "+errMessage, Toast.LENGTH_LONG).show();

	    	mAdapter.upDateEntries(mTravels);
	    }	
	}

	public class RemoveLocation extends AsyncTask<String, String, String> {
		/**
		 * Creating location
		 * */
		protected String doInBackground(String... args) {
			String id = args[0];

			HashMap<String, String> params = new HashMap<>();
			params.put("id", id);

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_remove_location,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created location
					//getActivity().setResult(Activity.RESULT_OK);
					//getActivity().finish();
				} else {
					// failed to create product
					//getActivity().setResult(Activity.RESULT_CANCELED);
					//getActivity().finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

        @Override
        protected void onPostExecute(String mNotUsed) {
            mAdapter.notifyDataSetChanged();
        }
	}
}