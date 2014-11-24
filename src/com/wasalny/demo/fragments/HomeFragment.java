package com.wasalny.demo.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wasalny.demo.R;
import com.wasalny.demo.apirequest.ConnectivityTask;
import com.wasalny.demo.apirequest.ConnectivityTask.OnPostExecuteListener;
import com.wasalny.demo.constant.FoursquareKyes;
import com.wasalny.demo.constant.NetworkingUtils;
import com.wasalny.demo.database.DatabaseManager;
import com.wasalny.demo.database.VenueTable;
import com.wasalny.demo.model.VenueModel;

public class HomeFragment  extends Fragment implements LocationListener{

	LocationManager mLocationMngr;
	String   provider;
	GoogleMap mGoogleMap;
	 double lat,lng;
 List<VenueModel> mList=new ArrayList<VenueModel>();
	  Bitmap imgbit = null;
		final List<Marker> markerList=new ArrayList<Marker>();
		HashMap<Marker, VenueModel> mappingK=new HashMap<Marker, VenueModel>();
SharedPreferences prefs;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 
				View  layout = inflater.inflate(R.layout.home_fragment, container,
						false);
				
		prefs=getActivity().getSharedPreferences(FoursquareKyes.SHARED_NAME, getActivity().MODE_PRIVATE);
		//GetMyLocation();
		
		
				//loginToForsquare();
				return layout;
	}
	
	
	private void initilizeMap() {
	    if (mGoogleMap == null) {
	       
	    	mGoogleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
	                 .getMap();
	         if (mGoogleMap != null) {
	          //   setUpMap();
	        	 
	             double latitude =30.0500 ;
	             double longitude =31.2333 ;
	              
	             // create marker
	             MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
	              
	             // adding marker
	             
	         /*    CameraUpdate center=
	                     CameraUpdateFactory.newLatLng(new LatLng(latitude,
	                     		longitude));
	                 CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

	                 googleMap.moveCamera(center);
	                 googleMap.animateCamera(zoom);*/
	         Marker    mMarkerOption = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng) )
	 					.icon(BitmapDescriptorFactory
	 							.fromResource(R.drawable.ic_launcher)));
	         mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng) , 10));
	     /*    mGoogleMap.setInfoWindowAdapter(new CustomInfoWindow(getActivity(),
	 					address));*/
	         //     marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
	         //     mGoogleMap.addMarker(mMarkerOption); 
	             mGoogleMap.setMyLocationEnabled(true);
	           
	             Location l=  mGoogleMap.getMyLocation();
	             
	             mGoogleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng) , 12.0f) );
	             mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick(Marker marker) {
						// TODO Auto-generated method stub
						
						String venueID=mappingK.get(marker).getId();
						 CheckIn( venueID );
					}
				});
	         }
	        // check if map is created successfully or not
	        if (mGoogleMap == null) {
	            Toast.makeText(getActivity(),
	                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
	                    .show();
	        }
	        
	  
	         
	    }
	}
	
	
	
	protected void CheckIn(String venueID) {
		// TODO Auto-generated method stub
		MultipartEntity param = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
		 	param.addPart("client_id", new StringBody(FoursquareKyes.client_ID));
			param.addPart("client_secret", new StringBody(FoursquareKyes.sectret_key)); 
			param.addPart("venueId", new StringBody(venueID));
			param.addPart("v", new StringBody("20140806"));
			param.addPart("oauth_token", new StringBody(prefs.getString(FoursquareKyes.TOKEN_KEY, "")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			ConnectivityTask task=new ConnectivityTask(FoursquareKyes.CHECKIN_ADD, param, getActivity(), new OnPostExecuteListener() {
				
				@Override
				public void onPostExecute(String result) {
					// TODO Auto-generated method stub
					JSONObject resultObj;
					try {
						resultObj = new JSONObject(result);
						JSONObject responseObj=resultObj.getJSONObject("response");
						JSONArray notificationArray=responseObj.getJSONArray("notifications");
						JSONObject messageObject=notificationArray.getJSONObject(0).getJSONObject("item");
						String message=messageObject.getString("message");
						
						Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
					} 
					
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			
			task.execute();
			if(NetworkingUtils.isNetworkConnected(getActivity()))
			{
				
			}
			else
			{
				Toast.makeText(getActivity(), "no Internet connection", Toast.LENGTH_SHORT).show();
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GetVenueList()
	{
		mList.clear();
		MultipartEntity param = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			param.addPart("client_id", new StringBody(FoursquareKyes.client_ID));
			param.addPart("client_secret", new StringBody(FoursquareKyes.sectret_key));
			param.addPart("ll", new StringBody(lat+","+lng));
			param.addPart("v", new StringBody("20140806"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try {
			ConnectivityTask task=new ConnectivityTask(FoursquareKyes.VENUE_API, param, getActivity() , new OnPostExecuteListener() {
				
				@Override
				public void onPostExecute(String result) {
					// TODO Auto-generated method stub
					try {
						JSONObject resultObj=new JSONObject(result);
					JSONObject responseObj=resultObj.getJSONObject("response");
					JSONArray venueArray=responseObj.getJSONArray("venues");
					
					for(int i=0;i<venueArray.length();i++)
					{
						VenueModel model=new VenueModel();
						model.setId( venueArray.getJSONObject(i).getString("id") );
						model.setName(venueArray.getJSONObject(i).getString("name"));
						model.setLat(venueArray.getJSONObject(i).getJSONObject("location").getString("lat"));
						model.setLng(venueArray.getJSONObject(i).getJSONObject("location").getString("lng"));
						model.setImg_url(venueArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getJSONObject("icon").getString("prefix")+"bg_32"+
								venueArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getJSONObject("icon").getString("suffix"));
					model.setCheckIn_count(venueArray.getJSONObject(i).getJSONObject("stats").getInt("checkinsCount"));
					model.setUsersCounts(venueArray.getJSONObject(i).getJSONObject("stats").getInt("usersCount"));
					model.setTipCount(venueArray.getJSONObject(i).getJSONObject("stats").getInt("tipCount"));
		
					if(VenueTable.getInstance().GetvenueByID(model.getId())!=null)
					{
						
					}
					else
					{
						VenueTable.getInstance(). insertVenue(model);
						
						mList.add(model);
						
						GetImgBitmap(model.getImg_url(),model);
						
						
					}
					
					}
					
					if(mList.size()==0)
					{
						Toast.makeText(getActivity(), "no new venues near to you ", Toast.LENGTH_SHORT).show();
					}
					//create  marker adapter 
					
					} 
					
					
					
					
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			if(NetworkingUtils.isNetworkConnected(getActivity()))
			task.execute();
			else
				Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
		} 
		
		
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private Bitmap GetImgBitmap(final String img_url,final VenueModel model)
	{
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				// TODO Auto-generated method stub
				  URL url=null;
				 
				try {
					url = new URL(img_url);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					imgbit = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							Marker marker = mGoogleMap.addMarker(new MarkerOptions().
							position(new LatLng(Double.valueOf(model.getLat()),Double.valueOf( model.getLng())))
						     .title(model.getName())	.icon(BitmapDescriptorFactory.fromBitmap(imgbit))); //...
							 
							markerList.add(marker); 
							mappingK.put(marker, model);
						}
					});
				
					Log.i("IMG", ""+imgbit);
			}
				
			 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			}
		}).start();
		
		
		return imgbit;
	}
	
	public void GetMyLocation()
	 {
		 boolean canGetLocation;
		 mLocationMngr = (LocationManager) getActivity(). getSystemService(Context.LOCATION_SERVICE);
			Location location = null ;

		 ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

         canGetLocation = true;

         // if DATA Enabled get lat/long using WIFI Services
         if (activeNetwork.isConnected()) {
             mLocationMngr.requestLocationUpdates(
                     LocationManager.NETWORK_PROVIDER,
                     400, 1, this);
             Log.d("Network", "Network");
             if (mLocationMngr != null) {
               
            	 provider=LocationManager.NETWORK_PROVIDER;
            	 location = mLocationMngr
                         .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             }
         }
		 
	 }


	 
	 public Location getLocation() {
	     
		 Location location=null;
		
		 try {
	            mLocationMngr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

	            // getting GPS status
	            boolean isGPSEnabled = mLocationMngr.isProviderEnabled(LocationManager.GPS_PROVIDER);

	            // getting network status
	            boolean isNetworkEnabled = mLocationMngr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	            if (!isGPSEnabled && !isNetworkEnabled) {
	                // no network provider is enabled
	            	startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	            } else {
	                // First get location from Network Provider
	                if (isNetworkEnabled) {
	                    mLocationMngr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,  15000, 10, this);
	                    Log.d("Network", "Network");
	                    if (mLocationMngr != null) {
	                        location = mLocationMngr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                       /* if (location != null) {
	                            lat = location.getLatitude();
	                            lng = location.getLongitude();
	                            Log.d("Loca", "lat"+lat+" lng"+lng);
	                            initilizeMap();
	                            GetVenueList();

	                        }*/
	                    }
	                }
	                //get the location by gps
	                else   if (isGPSEnabled) {
	                    if (location == null) {
	                        mLocationMngr.requestLocationUpdates(LocationManager.GPS_PROVIDER,400,1, this);
	                        Log.d("GPS Enabled", "GPS Enabled");
	                        if (mLocationMngr != null) {location = mLocationMngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                           /* if (location != null) {
	                             
	                            }*/
	                        }
	                    }
	                }
	                
	                if(location!=null)
	                {
	                	   lat = location.getLatitude();
                           lng = location.getLongitude();
                           initilizeMap();
                          
                           if(VenueTable.getInstance().GetVenueList()==null|| VenueTable.getInstance().GetVenueList().size()==0)
                           {
                        	   GetVenueList();
                           }
                           else
                           {
                        	   //get from database 
                        	   mList=VenueTable.getInstance().GetVenueList();
                        	   for(int i=0 ; i< mList.size(); i++)
                        	   {
                        		   GetImgBitmap(mList.get(i).getImg_url(),mList.get(i));
                        	   }
                        	   
                        	   GetVenueList();
                        	   
                           }
                       
	                }
	            }

	         
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return location;
	    }
@SuppressWarnings("unchecked")
@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if(prefs.contains(FoursquareKyes.TOKEN_KEY))
		
	{
		getLocation();
		
	}
	
	else
	{
		Intent intent=new Intent(getActivity(), ActivitywebView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
		
	 
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		 double lat = (double) (location.getLatitude());
		 double lng = (double) (location.getLongitude());
		 // Toast.makeText(getActivity(), "location is" +lat +" "+lng, Toast.LENGTH_SHORT).show();
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}



	 
}
