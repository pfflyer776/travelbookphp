package com.webalcove.travelbook;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TravelAddPicFragment  extends Fragment {
	
    //private TravelActivity mOther;
    String mPlace;
    int mId;    
    
    String filename = null;
    
    File destination;
    String imagePath;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = "http://www.webalcove.com/places/uploadImage.php";	
	
	private static final int REQUEST_PHOTO = 1;
	EditText picCaption;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mId = getActivity().getIntent().getExtras().getInt("ID");
		mPlace = getActivity().getIntent().getExtras().getString("PLACE");
	}
	
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_travel_addpic, parent, false);
        
        picCaption = (EditText) v.findViewById(R.id.etCaption);
        
        Button takePictureButton = (Button)v.findViewById(R.id.btnPicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				Intent i = new Intent(getActivity(), TravelCameraActivity.class);

				// when returns will call onActivityResult()
				startActivityForResult(i, REQUEST_PHOTO);
            } 
        });
        
		return v;	
    }   
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode != Activity.RESULT_OK) return;
    	if (requestCode == REQUEST_PHOTO) {
            // create a new Photo object and attach it to the crime
            filename = data.getStringExtra(TravelCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
            	
            	imagePath = getActivity().getFileStreamPath(filename).getAbsolutePath();
                Thread t = new Thread(new Runnable() {
                    public void run() {        
                        uploadFile(imagePath);
                    }
                });
                t.start();
                try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                // thread above may not be done yet need join to make it happen first
                String mUrl = "http://www.webalcove.com/places/" + mPlace + "/images/" + filename;
        		TravelImages mTravelImage = new TravelImages();
        		mTravelImage.setURL(mUrl);
        		mTravelImage.setDescript(picCaption.getText().toString());
                //TravelActivity.mTravelImages.add(mTravelImage);
        		TravelLab.get(getActivity()).addTravelImages(mTravelImage);		

                getActivity().finish();
                //mOther.myAdapterChange();

            }
        }   	
    }	
    
    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 

        if (!sourceFile.isFile()) {
            //dialog.dismiss(); 
            Log.e("uploadFile", "Source File not exist :" +filename);
            return 0;
        }
        else
        {
            try { 

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection(); 
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                
                // use post method
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName); 

                dos = new DataOutputStream(conn.getOutputStream());
                
                // send parameter # 1
                dos.writeBytes(twoHyphens + boundary + lineEnd); 
                dos.writeBytes("Content-Disposition: form-data; name=\"param1\"" + lineEnd + lineEnd);
                dos.writeBytes(mId + lineEnd);
                
                // send parameter # 2
                dos.writeBytes(twoHyphens + boundary + lineEnd); 
                dos.writeBytes("Content-Disposition: form-data; name=\"param2\"" + lineEnd + lineEnd);
                dos.writeBytes(mPlace + lineEnd);
                
                // send parameter # 3
                dos.writeBytes(twoHyphens + boundary + lineEnd); 
                dos.writeBytes("Content-Disposition: form-data; name=\"param3\"" + lineEnd + lineEnd);
                dos.writeBytes(picCaption.getText().toString() + lineEnd);                

                // send binary file
                dos.writeBytes(twoHyphens + boundary + lineEnd); 
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename="+ fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available(); 

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

// shouldn't do ui work in thread
//                	getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            Toast.makeText(getActivity(), "File Upload Complete.", 
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });                
                }    

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                
            } catch (MalformedURLException ex) {

                //dialog.dismiss();  
                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "MalformedURLException", 
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {

                //dialog.dismiss();  
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "Got Exception : see logcat ", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);  
            }
            //dialog.dismiss(); 
            
            return serverResponseCode; 

        } // End else block 
    }     
}
