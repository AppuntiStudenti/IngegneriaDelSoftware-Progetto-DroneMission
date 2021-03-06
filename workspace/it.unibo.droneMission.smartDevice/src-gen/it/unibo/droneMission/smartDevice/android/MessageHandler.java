/* ======================================================
* Generated by AN 
* University of Bologna
* ======================================================-
*/
package  it.unibo.droneMission.smartDevice.android;
import android.os.Handler;
import android.os.Message;

public class MessageHandler extends Handler {
	protected BaseActivity myActivity;
  
	public MessageHandler(BaseActivity myActivity) {
		this.myActivity = myActivity;
	}
 
	public void handleMessage(Message msg) {
	String currentValue;
		try {
			currentValue = msg.getData().getString("addOutputMsg");
			if (currentValue != null) {
				myActivity.println(currentValue); //DO PRINT !!!
 			}
 			else{ 
 				currentValue = msg.getData().getString("setOutputMsg");
			if (currentValue != null) {
				myActivity.printMsg(currentValue); //DO PRINT !!!
			}
			}
 		} catch (Exception e) {
			System.out.println("ERROR" + e);
		}
	}
 }
