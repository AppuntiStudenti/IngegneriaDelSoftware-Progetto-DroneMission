/*
*  Generated by AN Unibo
*/
package it.unibo.contact.HeadQuarterAnalysis;
//Import generated by the contact spec
//Other Import
import it.unibo.contact.platformuv.*;
import it.unibo.is.interfaces.*;
import it.unibo.is.interfaces.platforms.*;
//import org.eclipse.xtext.xbase.lib.*;
//import org.eclipse.xtext.xbase.lib.Functions.*;
import java.util.Vector;
import it.unibo.contact.platformuv.LindaLike;
import it.unibo.is.interfaces.protocols.IConnInteraction;
//import java.awt.Color;
//For Xbase code 
import org.eclipse.xtext.xbase.lib.Functions.*;
import org.eclipse.xtext.xbase.lib.*;

public abstract class SmartdeviceSupport extends Subject{
	private static Smartdevice obj = null;
	private IMessage resCheckMsg;
	private boolean resCheck;
	/*
	* Factory method: returns a singleton
	*/
	public static Smartdevice create(String name) throws Exception{
		if( obj == null ) obj = new Smartdevice(name);
		return obj;
	}
	/* -------------------------------------
	* Local state of the subject
	* --------------------------------------
	*/
	protected int lastMsgNum = 0;
	
	
	//Constructor
	public SmartdeviceSupport(String name) throws Exception{
		super(name);
	 	isMultiInput=true;
	 	inputMessageList=new String[]{"sensorsData","notify", "endSelectInput"};
	 	initLastMsgRdMemo();  //put in initGui since the name must be set
		//Singleton
		if( obj != null ) return;
		 obj = (Smartdevice)this;
	}
	
	/* -------------------------------------
	* Init
	* --------------------------------------
	*/
	//PREPARE INPUT THREADS
	public void initInputSupports() throws Exception{
	}
	
 	protected void initLastMsgRdMemo(){
 			lastMsgRdMemo.put("sensorsData"+getName(),0);
 			lastMsgRdMemo.put("notify"+getName(),0);
 	}
	protected void initGui(){
		if( env != null ) view = env.getOutputView();
	    initLastMsgRdMemo(); //put here since the name must be set
	}
	
	/* -------------------------------------
	* State-based Behavior
	* -------------------------------------- 
	*/ 
	protected abstract void notifyUserMissionStarted() throws Exception;
	protected abstract void updateGauges(java.lang.String data) throws Exception;
	protected abstract void missionFinished() throws Exception;
	/* --- USER DEFINED STATE ACTIONS --- */
	/* --- USER DEFINED TASKS --- */
	/* 
		Each state acquires some input and performs some action 
		Each state is mapped into a void method 
	*/
	//Variable behavior declarations
	protected 
	String notifyContent = null;
	protected 
	String dataDroneReceived = null;
	public  java.lang.String get_notifyContent(){ return notifyContent; }
	public  java.lang.String get_dataDroneReceived(){ return dataDroneReceived; }
	
	protected boolean endStateControl = false;
	protected String curstate ="st_Smartdevice_init";
	protected void stateControl( ) throws Exception{
		boolean debugMode = System.getProperty("debugMode" ) != null;
	 		while( ! endStateControl ){
	 			//DEBUG 
	 			if(debugMode) debugNextState(); 
	 			//END DEBUG
			/* REQUIRES Java Compiler 1.7
			switch( curstate ){
				case "st_Smartdevice_init" : st_Smartdevice_init(); break; 
				case "st_Smartdevice_missionStart" : st_Smartdevice_missionStart(); break; 
				case "st_Smartdevice_waitingForData" : st_Smartdevice_waitingForData(); break; 
				case "st_Smartdevice_receivedData" : st_Smartdevice_receivedData(); break; 
				case "st_Smartdevice_notifyHandler" : st_Smartdevice_notifyHandler(); break; 
				case "st_Smartdevice_endMission" : st_Smartdevice_endMission(); break; 
			}//switch	
			*/
			if( curstate.equals("st_Smartdevice_init")){ st_Smartdevice_init(); }
			else if( curstate.equals("st_Smartdevice_missionStart")){ st_Smartdevice_missionStart(); }
			else if( curstate.equals("st_Smartdevice_waitingForData")){ st_Smartdevice_waitingForData(); }
			else if( curstate.equals("st_Smartdevice_receivedData")){ st_Smartdevice_receivedData(); }
			else if( curstate.equals("st_Smartdevice_notifyHandler")){ st_Smartdevice_notifyHandler(); }
			else if( curstate.equals("st_Smartdevice_endMission")){ st_Smartdevice_endMission(); }
		}//while
		//DEBUG 
		//if( synch != null ) synch.add(getName()+" reached the end of stateControl loop"  );
	 	}
	 	protected void selectInput(boolean mostRecent, Vector<String> tempList) throws Exception{
		Vector<IMessage> queries=comSup.prepareInput(mostRecent,getName(),
				SysKb.getSyskb(),tempList.toArray(),InteractPolicy.nopolicy() );
		//showMsg("*** queries" + queries);
		curInputMsg = selectOneInput(mostRecent,queries);	
		curInputMsgContent = curInputMsg.msgContent();	
	}
	
	protected void st_Smartdevice_missionStart()  throws Exception{
		
		notifyUserMissionStarted();curstate = "st_Smartdevice_waitingForData"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Smartdevice_waitingForData()  throws Exception{
		
		//[it.unibo.indigo.contact.impl.SignalImpl@32f25aa8 (name: sensorsData) (var: null), it.unibo.indigo.contact.impl.SignalImpl@2dd4f822 (name: notify) (var: null)] | sensorsData isSignal=true
		resCheckMsg = checkSignal("ANY","sensorsData",false);
		if(resCheckMsg != null){
			curstate = "st_Smartdevice_receivedData";
			return;}
		//[it.unibo.indigo.contact.impl.SignalImpl@32f25aa8 (name: sensorsData) (var: null), it.unibo.indigo.contact.impl.SignalImpl@2dd4f822 (name: notify) (var: null)] | notify isSignal=true
		resCheckMsg = checkSignal("ANY","notify",false);
		if(resCheckMsg != null){
			curstate = "st_Smartdevice_endMission";
			return;}
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Smartdevice_receivedData()  throws Exception{
		
		inputMessageList=new String[]{  "sensorsData"  };
		curInputMsg=selectWithPriority(false, inputMessageList);
		curInputMsgContent = curInputMsg.msgContent();
		dataDroneReceived =curInputMsgContent;
		showMsg(dataDroneReceived);
		updateGauges(dataDroneReceived);curstate = "st_Smartdevice_waitingForData"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Smartdevice_notifyHandler()  throws Exception{
		
		inputMessageList=new String[]{  "notify"  };
		curInputMsg=selectWithPriority(false, inputMessageList);
		curInputMsgContent = curInputMsg.msgContent();
		notifyContent =curInputMsgContent;
		
		{//XBlockcode
		String _notifyContent = notifyContent;
		boolean _operator_equals = ObjectExtensions.operator_equals(_notifyContent, "start");
		expXabseResult=_operator_equals;
		}//XBlockcode
		if(  (Boolean)expXabseResult ){ //cond
		curstate = "st_Smartdevice_missionStart"; 
		//resetCurVars(); //leave the current values on
		return;
		}//if cond
		
		{//XBlockcode
		String _notifyContent = notifyContent;
		boolean _operator_equals = ObjectExtensions.operator_equals(_notifyContent, "end");
		expXabseResult=_operator_equals;
		}//XBlockcode
		if(  (Boolean)expXabseResult ){ //cond
		curstate = "st_Smartdevice_endMission"; 
		//resetCurVars(); //leave the current values on
		return;
		}//if cond
		curstate = "st_Smartdevice_waitingForData"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Smartdevice_endMission()  throws Exception{
		
		missionFinished();/* --- TRANSITION TO NEXT STATE --- */
		resetCurVars();
		do_terminationState();
		endStateControl=true;
	}
	protected void st_Smartdevice_init()  throws Exception{
		
		/* --- TRANSITION TO NEXT STATE --- */
		Vector<String> tempList=new Vector<String>();
		tempList.add("notify");
		 		if( tempList.size()==0){
					resetCurVars();
					do_terminationState();
					endStateControl=true;
					return;
				}
		selectInput(false,tempList);
		if(curInputMsg.msgId().equals("notify")){ 
		curstate = "st_Smartdevice_missionStart";
		return;
		}//if curInputMsg notify
	}
	
   	
 	/* -------------------------------------
	* COMMUNICATION CORE OPERATIONS for smartdevice
	* --------------------------------------
	*/
 
	protected IMessage hl_smartdevice_sense_sensorsData(   ) throws Exception {
	IMessage m = new Message("signal(ANYx1y2,sensorsData,M,N)");
	IMessage inMsg = comSup.rdw( getName() ,"sensorsData",  lastMsgRdMemo,m );
		return inMsg;
	
	}
	protected IMessage hl_smartdevice_sense_sensorsData( boolean mostRecent  ) throws Exception {
	if( ! mostRecent) return hl_smartdevice_sense_sensorsData ();
	else{
	IMessage m = new Message("signal(ANYx1y2,sensorsData,M,N)");
	IMessage inMsg = comSup.rdwMostRecent(getName() ,"sensorsData",  lastMsgRdMemo,m );
		return inMsg;
	}
	
	}
	
	protected IMessage hl_smartdevice_sense_notify(   ) throws Exception {
	IMessage m = new Message("signal(ANYx1y2,notify,M,N)");
	IMessage inMsg = comSup.rdw( getName() ,"notify",  lastMsgRdMemo,m );
		return inMsg;
	
	}
	protected IMessage hl_smartdevice_sense_notify( boolean mostRecent  ) throws Exception {
	if( ! mostRecent) return hl_smartdevice_sense_notify ();
	else{
	IMessage m = new Message("signal(ANYx1y2,notify,M,N)");
	IMessage inMsg = comSup.rdwMostRecent(getName() ,"notify",  lastMsgRdMemo,m );
		return inMsg;
	}
	
	}
	
	
 	/* -------------------------------------
	* CONNECTION OPERATIONS for smartdevice
	* --------------------------------------
	*/
	
	/* -------------------------------------
	* Local body of the subject
	* --------------------------------------
	*/
	
		public void doJob() throws Exception{ stateControl(); }
	 	//INSERTED FOR DEBUG
		protected boolean nextStep = false;
		protected String stateBreakpoint = null;
		protected Vector<String> synch;
		protected synchronized void debugNextState() throws Exception{
			if( stateBreakpoint != null && ! curstate.equals(stateBreakpoint) ) return;
			while( stateBreakpoint != null && curstate.equals(stateBreakpoint) ){
				showMsg(" stateBreakpoint reached "  +  stateBreakpoint);
				synch.add("stateBreakpoint reached " + stateBreakpoint);
				//showMsg("wait");
	 			wait();			
			}
	//		if( stateBreakpoint != null   ) { //resumed!
	// 	 	stateBreakpoint = null;
	//			return;
			}
	//		while( ! nextStep ) wait();
	//		if( stateBreakpoint != null ) debugNextState();
	//		else{
	//			showMsg("resume nextStep");
	//			synch.add("nextStep done");
	//			nextStep = false;
	//		}
	//	}
	//	public synchronized void nextStateStep(Vector<String> synch) throws Exception{
	//		showMsg("nextStateStep " + curstate );
	//		this.synch = synch;
	//		nextStep = true;
	//		notifyAll();
	//	}
		public synchronized void nextStateStep(String state, Vector<String> synch) throws Exception{
			this.synch = synch;
			stateBreakpoint = state;
			nextStep = true;
			showMsg("nextStateStep stateBreakpoint=" + stateBreakpoint );
	 		notifyAll();
		}
		//END INSERTED FOR DEBUG
			
		protected void do_terminationState() throws Exception {
			//showMsg(  " ---- END STATE LOOP ---- " );
		}
	
	protected IMessage acquire(String msgId) throws Exception{
	  //showMsg("acquire "  +  msgId ); 
	  IMessage m;
	  //USER MESSAGES
	 if( msgId.equals("endSelectInput")){
	  String ms = MsgUtil.bm(MsgUtil.channelInWithPolicy(InteractPolicy.nopolicy(),getName(), "endSelectInput"), 
	    getName(), "endSelectInput", "ANYx1y2", "N");
	  //Serve the auto-dispatch
	  IMessage min = core.in(new Message(ms).toString());
	  return min;
	 }
	  throw new Exception("Wrong msgId:"+  msgId);
	}//acquire	
	
	/* -------------------------------------
	* Operations (from Java)
	* --------------------------------------
	*/
	
		
	/* -------------------------------------
	* Termination
	* --------------------------------------
	*/
	public void terminate() throws Exception{ //by EndSubjectConnections
	 			 //Auto-forward a dispatch to finish selectInput operations
	 		    String ms =
	 		      MsgUtil.bm(MsgUtil.channelInWithPolicy(InteractPolicy.nopolicy(),getName(), "endSelectInput"), 
	 		       getName(), "endSelectInput", "endSelectInput", "0");
	 		    core.out(ms);
	if( synch != null ){
		synch.add(getName()+" reached the end of loop"  );
	}
	obj = null;
	//System.out.println(getName() + " terminated");
	}	
	// Teminate operations
}//SmartdeviceSupport
