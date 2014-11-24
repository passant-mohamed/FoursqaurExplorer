package com.wasalny.demo.apirequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
public class ConnectivityTask extends AsyncTask<String, Integer, String> {

	String uri;
	private OnPostExecuteListener mPostExecuteListener = null;
	private MultipartEntity mParams = new MultipartEntity(
			HttpMultipartMode.BROWSER_COMPATIBLE);
	public static String Msg_dialog = "loading...";

	  Context context;
	// private ProgressDialog dialog;
	public Dialog dialog;

	static AsyncTask<String, Integer, String> act_task;

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		publishProgress(0);
		StringBuilder s = new StringBuilder();
		
		try {

			// HttpClient httpClient = new DefaultHttpClient();

			HttpPost postRequest = new HttpPost(uri);

			// mParams= new
			// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			HttpParams httpParameters = new BasicHttpParams();

			 
			   int timeoutConnection = 30000;
			   HttpConnectionParams.setConnectionTimeout(httpParameters,
			  timeoutConnection); // Set the default socket timeout
			 // (SO_TIMEOUT) // in milliseconds which is the timeout for waiting
			 int timeoutSocket =40000;
			  
			  HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			 
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			// HttpResponse response = httpClient.execute(httpGet);

			if (mParams != null) {
			
				postRequest.setEntity(mParams);
			}
			HttpResponse response = httpClient.execute(postRequest);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}

			Log.i("Result", s.toString());
		}

		/*catch (SocketTimeoutException ste) {

			Log.i("Timeout Exception: ", ste.toString());

			if ((dialog != null && dialog.isShowing())) {

				dialog.dismiss();
			}

		} */
		
		catch(ConnectTimeoutException e){
            
 			Log.i("Timeout Exception: ", e.toString());
 			
 			 if ( dialog!=null && dialog.isShowing()) 
			 {
		       
				 dialog.dismiss();
			 }
 			  
 				 ((Activity) context).runOnUiThread(new Runnable() {
 					 public void run() {
 						 //Your code here
 						 AlertDialog.Builder dialog=new AlertDialog.Builder(context);
 						 dialog.setTitle("Error").setMessage("Time out exception !");
 						/* dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
 							 
 							 @Override
 							 public void onClick(DialogInterface dialog, int which) {
 								 
 								try {
									act_task=new ConnectivityTask(context, uri, mParams, mPostExecuteListener);
								} 
 								 catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
 							 }
 						 });*/
 						 
 						 dialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
 						
 						 if(context==null)
 						 {
 							 
 						 }
 						 else 
 						 {
 							 
 							 dialog.create();
 							 dialog.show();
 						 }
 					 }
 				 });
 			 
 			  
        } 
		
		catch(SocketTimeoutException ste){ 
		
			Log.i("Timeout Exception: ", ste.toString());
			
			 if (( dialog!=null && dialog.isShowing()) ) 
			 {
		       
				 dialog.dismiss();
			 }
           
       	  
			  
 				 ((Activity) context).runOnUiThread(new Runnable() {
 					 public void run() {
 						 //Your code here
 						 AlertDialog.Builder dialog=new AlertDialog.Builder(context);
 						 dialog.setTitle("Error").setMessage("Time out exception !");
                       /*  dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
 							 
 							 @Override
 							 public void onClick(DialogInterface dialog, int which) {
 								 
 								 try {
									act_task=new ConnectivityTask(context, uri, mParams, active, mPostExecuteListener);
								} 
 								 catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
 							  
 							 }
 						 });
 						 */
 						 dialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
 						 if(context==null)
 						 {
 							 
 						 }
 						 else 
 						 {
 							 
 							 dialog.create();
 							 dialog.show();
 						 }
 					 }
 				 });
 			  
			  
        }  
		catch (Exception e) {

			Log.i("log_tag", "Error in http connection " + e.toString());
		}
		return s.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

		if (dialog == null) {
			if (mPostExecuteListener != null) {
				try {
					// JSONArray jArray = new JSONArray(result);
					mPostExecuteListener.onPostExecute(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
		 	if (dialog.isShowing()) {

				// got an error here

				dialog.dismiss();
			} 

			if (mPostExecuteListener != null) {
				try {
					// JSONArray jArray = new JSONArray(result);
					mPostExecuteListener.onPostExecute(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);

		if (values[0] == 0) {

			if (dialog == null) {

			}
			else {
				//dialog = new Dialog(context);
				/*Window window = dialog.getWindow();
				window.setBackgroundDrawableResource(android.R.color.transparent);
				window.requestFeature(window.FEATURE_NO_TITLE);
			 //	dialog.setContentView(R.layout.loading);
				dialog.setCancelable(true);
				dialog.show();*/
				Msg_dialog="loading...";//context.getResources().getString("Loading");
				dialog.setCancelable(true);
				dialog = ProgressDialog.show(context, null, 
    					Msg_dialog, true);
			}

		}

	}

	public static interface OnPostExecuteListener {

		void onPostExecute(String result);

	}

	public ConnectivityTask(String uri, MultipartEntity mParams,
			Context context, OnPostExecuteListener mPostExecuteListener)
			throws Exception {
		super();
		this.uri = uri;
		this.mPostExecuteListener = mPostExecuteListener;
		this.mParams = mParams;

		this.context = context;

		if (context == null) {
			dialog = null;
		} else {
			dialog = new ProgressDialog(this.context);
		}

		if (mPostExecuteListener == null)
			throw new Exception("Param cannot be null.");
	}

}
