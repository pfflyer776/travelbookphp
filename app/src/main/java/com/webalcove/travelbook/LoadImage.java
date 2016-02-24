package com.webalcove.travelbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class LoadImage {

	public String myfilename;
	private Context mContext;
	
	LoadImage(Context context) {
		mContext = context;
	}	
	
	public Bitmap getImage(String url) {
		
		Bitmap bitmap;
		File cacheDir;
		
	    bitmap = null;		
        
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"YourDirectorName");
        else
            cacheDir=mContext.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
        
        String fn = String.valueOf(url.hashCode());
        File f = new File(cacheDir, fn);
        
      	try {
      		URL myurl = new URL(url);
      		//bitmap = BitmapFactory.decodeStream((InputStream)myurl.getContent());
      		
      		HttpURLConnection conn = (HttpURLConnection)myurl.openConnection();
      		conn.setDoInput(true);
      		conn.connect();
      		
      		InputStream is = conn.getInputStream();
      		
      		final BitmapFactory.Options options = new BitmapFactory.Options();
      		
      		BufferedInputStream bis = new BufferedInputStream(is, 4*1024);
      		
      		ByteArrayBuffer baf = new ByteArrayBuffer(50);
      		int current = 0;
      		while ((current = bis.read()) != -1) {
      			baf.append((byte)current);
      		}
      		byte[] imageData = baf.toByteArray();
      		
      		BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
      		options.inJustDecodeBounds = true;
      		options.inSampleSize = 2; // calculateInSampleSize(options, 128, 128);
      		options.inJustDecodeBounds = true;
      		bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
      		writeFile(bitmap, f);
      		this.myfilename = f.toString();
      		
      		bis.close();
      		is.close();
      		
      		Log.d("Got bitmap","hi");
      		
      	} catch (Exception e) {
      		Log.d("Error=", e.toString());
      	}
        return bitmap;
    }
	
    private void writeFile(Bitmap bmp, File f) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex) {
            }
        }
    }
}
