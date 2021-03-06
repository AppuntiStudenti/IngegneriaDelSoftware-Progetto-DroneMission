/* ======================================================
* Generated by AN 
* University of Bologna
* ======================================================-
*/
package it.unibo.droneMission.smartDevice.android;
	import android.os.Bundle;
	import android.content.Intent;
	import it.unibo.droneMission.smartDevice.android.R;
	 //Layout Packages
	import android.widget.*;
	
public abstract class SmartDashboardSupport extends BaseActivity{
	protected String inputValue  = "TODO";	//Data value associated to an action
	protected String outputValue = "TODO";	//Data value to be sent
	protected String MY_FILE_PATH = "/data/data/it.unibo.droneMission.smartDevice.android/files/";	
	protected SmartDashboard dummyObj = null; //To stimulate the user to define a class
	protected boolean activatedByGui = false;
	//LifeCycle methods
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout);
			
	        
	        
	        }//onCreate
	@Override
	public void println(String msg) {
		if (output == null){ 
			output = (TextView) findViewById(R.id.output);
		}
		output.append(msg+"\n");
	}
	
	
	protected void onResume() {
		super.onResume();
		lookAtInput();
	}//onResume
	protected void onDestroy() {
		super.onDestroy();
	}//onDestroy
	//Actions
	protected abstract void startMission(String inputValue,Bundle b) throws Exception;
	protected abstract void endMission(String inputValue,Bundle b) throws Exception;
	protected abstract void updateValues(String inputValue,Bundle b) throws Exception;
	protected abstract void notifyReceived(String inputValue,Bundle b) throws Exception;
	//Interactions
	/*
	*	INPUT
	*/
		//Look at the activity input in order to decide what to do
		protected void lookAtInput() {
			Intent sourceIntent = getIntent();
			if(sourceIntent != null && sourceIntent.getAction() != null)
			analyseInput(sourceIntent);
		}
	
		//Check the nature of the activity input
		protected void analyseInput(Intent i) {
			if (i.getAction().contains("action.MAIN")) {
				activatedByGui = true;
				doJob();
			} else execAction(i);
		}
		
		public boolean isActivatedByGUI() {
			return activatedByGui;
		}
		
		//Perform some local job
		protected abstract void doJob();
		
		//Execute some action
		protected void execAction(Intent input) {
		try {
			//println("SmartDashboard execAction "+input.getAction()+" "+input.getData()+" "+input.getExtras()); //4
			 if(input.getAction().endsWith("startMission")){
				String inputValue = input.getData().getLastPathSegment();
				startMission(inputValue,input.getExtras());
				}
			 else if(input.getAction().endsWith("endMission")){
				String inputValue = input.getData().getLastPathSegment();
				endMission(inputValue,input.getExtras());
				}
			 else if(input.getAction().endsWith("updateValues")){
				String inputValue = input.getData().getLastPathSegment();
				updateValues(inputValue,input.getExtras());
				}
			 else if(input.getAction().endsWith("notifyReceived")){
				String inputValue = input.getData().getLastPathSegment();
				notifyReceived(inputValue,input.getExtras());
				}
		} catch(Exception e) {}
		}	
}//SmartDashboardSupport
