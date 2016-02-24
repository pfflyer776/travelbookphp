package com.webalcove.travelbook;

import java.util.ArrayList;

import android.content.Context;

public class TravelLab {

	private Context mAppContext;
	
    private ArrayList<Travel> mTravels;
	private ArrayList<TravelImages> mTravelImages;
	private static TravelLab sTravelLab;
	private static boolean gotData;
	
	private TravelLab(Context appContext) {
		mAppContext = appContext;
		
		mTravels = new ArrayList<Travel>();
		mTravelImages = new ArrayList<TravelImages>();
	}
	
	public static TravelLab get(Context c) {
		// use application context so exists for life of application
		if (sTravelLab == null) {
			sTravelLab = new TravelLab(c.getApplicationContext());
		}
		return sTravelLab;			
	}
	
	// will add travelimages after select which travel location
	public void addTravel(Travel t) {
		mTravels.add(t);
	}
	
	public void addTravelImages(TravelImages t) {
		mTravelImages.add(t);
	}
	
	public void deleteTravel(Travel t) {
		mTravels.remove(t);
	}
	
	public ArrayList<Travel> getTravels() {
		return mTravels;
	}
	
	public void clearTravels() {
		mTravels.clear();
		mTravelImages.clear();
	}

	public static void setData() {
		gotData = true;
	}

    public static boolean getData() {
        return gotData;
    }

	public void clearImages() {
		mTravelImages.clear();
	}
	
	public int getTravelSize() {
		return mTravels.size();
	}
	
	public TravelImages getTravelImage(int pos) {
		TravelImages t = mTravelImages.get(pos);
		return t;
	}
	
	public int getTravelImagesSize() {
		return mTravelImages.size();
	}
	
	public Travel getTravel(int id) {
		for (Travel t : mTravels) {
			if (t.getId() == id)
				return t;
		}
		return null;
	}
}
