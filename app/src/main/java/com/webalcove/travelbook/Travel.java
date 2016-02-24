package com.webalcove.travelbook;

import java.util.ArrayList;

public class Travel {
	
	private int mId;
	private String mTitle;
	
	private ArrayList<TravelImages> mTravelImages;
	
	public Travel() {
		mId = 0;
		mTitle = "";

		mTravelImages = new ArrayList<TravelImages>();
		
//		TravelImages mTravelImage = new TravelImages();
//		mTravelImage.setURL("http://www.webalcove.com/places/grandcanyon/images/image19.jpg");
//		mTravelImages.add(mTravelImage);
//		
//		mTravelImage = new TravelImages();
//		mTravelImage.setURL("http://www.webalcove.com/places/grandcanyon/images/image20.jpg");
//		mTravelImages.add(mTravelImage);
//		
//		mTravelImage = new TravelImages();
//		mTravelImage.setURL("http://www.webalcove.com/places/grandcanyon/images/image21.jpg");
//		mTravelImages.add(mTravelImage);
//		
//		mTravelImage = new TravelImages();
//		mTravelImage.setURL("http://www.webalcove.com/places/grandcanyon/images/image22.jpg");
//		mTravelImages.add(mTravelImage);
	}
	
	@Override
	public String toString() {
		return mTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public int getId() {
		return mId;
	}
	
	public void setId(int mId) {
		this.mId = mId;
	}
	
	public void addTravel(TravelImages t) {
		mTravelImages.add(t);
	}
	
	public void addImage(String mUrl, String mDescript) {
		TravelImages mTravelImage = new TravelImages();
		mTravelImage.setURL(mUrl);
		mTravelImage.setDescript(mDescript);
		mTravelImages.add(mTravelImage);		
	}
	
	public ArrayList<TravelImages> loadTravelImages() {
		return mTravelImages;
	}

}
