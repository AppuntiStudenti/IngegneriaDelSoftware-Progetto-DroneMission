/*
*  Generated by AN Unibo
*/
package it.unibo.contact.DroneMissionSystem;
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
import it.unibo.baseEnv.basicFrame.EnvFrame;

public abstract class DroneSupport extends Subject{
	private static Drone obj = null;
	private IMessage resCheckMsg;
	private boolean resCheck;
	/*
	* Factory method: returns a singleton
	*/
	public static Drone create(String name) throws Exception{
		if( obj == null ) obj = new Drone(name);
		return obj;
	}
	/* -------------------------------------
	* Local state of the subject
	* --------------------------------------
	*/
	protected int lastMsgNum = 0;
	
	
	//Constructor
	public DroneSupport(String name) throws Exception{
		super(name);
	 	isMultiInput=true;
	 	inputMessageList=new String[]{"command", "endSelectInput"};
	 	initLastMsgRdMemo();  //put in initGui since the name must be set
		//Singleton
		if( obj != null ) return;
		 obj = (Drone)this;
	}
	
	/* -------------------------------------
	* Init
	* --------------------------------------
	*/
	//PREPARE INPUT THREADS
	public void initInputSupports() throws Exception{
	PlatformExpert.findInSupport( getName(), "command" ,true,view);
	}
	
 	protected void initLastMsgRdMemo(){
 			lastMsgRdMemo.put("command"+getName(),0);
 	}
	protected void initGui(){
	    env = new EnvFrame( getName(), this, new java.awt.Color(151, 228, 255), java.awt.Color.black );
	    env.init();
	    env.writeOnStatusBar(getName() + " | DroneSupport working ... ",14);
	    view = env.getOutputView();
	    initLastMsgRdMemo(); //put here since the name must be set
	 }
	
	/* -------------------------------------
	* State-based Behavior
	* -------------------------------------- 
	*/ 
	protected abstract void startMission() throws Exception;
	protected abstract void endMission() throws Exception;
	protected abstract void setSpeed(java.lang.String value) throws Exception;
	protected abstract java.lang.String getDataFromSensors() throws Exception;
	protected abstract java.lang.String getDataPhoto() throws Exception;
	/* --- USER DEFINED STATE ACTIONS --- */
	/* --- USER DEFINED TASKS --- */
	/* 
		Each state acquires some input and performs some action 
		Each state is mapped into a void method 
	*/
	//Variable behavior declarations
	protected 
	String msgCommand = "";
	protected 
	String cmdName = "";
	protected 
	String cmdValue = "";
	protected 
	boolean start = false;
	protected 
	boolean stop = false;
	protected 
	boolean speed = false;
	protected 
	String sensorsDatas = null;
	protected 
	String dataPhoto = null;
	public  java.lang.String get_msgCommand(){ return msgCommand; }
	public  java.lang.String get_cmdName(){ return cmdName; }
	public  java.lang.String get_cmdValue(){ return cmdValue; }
	public  boolean get_start(){ return start; }
	public  boolean get_stop(){ return stop; }
	public  boolean get_speed(){ return speed; }
	public  java.lang.String get_sensorsDatas(){ return sensorsDatas; }
	public  java.lang.String get_dataPhoto(){ return dataPhoto; }
	
	protected boolean endStateControl = false;
	protected String curstate ="st_Drone_init";
	protected void stateControl( ) throws Exception{
		boolean debugMode = System.getProperty("debugMode" ) != null;
	 		while( ! endStateControl ){
	 			//DEBUG 
	 			if(debugMode) debugNextState(); 
	 			//END DEBUG
			/* REQUIRES Java Compiler 1.7
			switch( curstate ){
				case "st_Drone_init" : st_Drone_init(); break; 
				case "st_Drone_ready" : st_Drone_ready(); break; 
				case "st_Drone_startMission" : st_Drone_startMission(); break; 
				case "st_Drone_setspeed" : st_Drone_setspeed(); break; 
				case "st_Drone_onMission" : st_Drone_onMission(); break; 
				case "st_Drone_commandHandler" : st_Drone_commandHandler(); break; 
				case "st_Drone_endMission" : st_Drone_endMission(); break; 
			}//switch	
			*/
			if( curstate.equals("st_Drone_init")){ st_Drone_init(); }
			else if( curstate.equals("st_Drone_ready")){ st_Drone_ready(); }
			else if( curstate.equals("st_Drone_startMission")){ st_Drone_startMission(); }
			else if( curstate.equals("st_Drone_setspeed")){ st_Drone_setspeed(); }
			else if( curstate.equals("st_Drone_onMission")){ st_Drone_onMission(); }
			else if( curstate.equals("st_Drone_commandHandler")){ st_Drone_commandHandler(); }
			else if( curstate.equals("st_Drone_endMission")){ st_Drone_endMission(); }
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
	
	protected void st_Drone_ready()  throws Exception{
		
		showMsg("----- Waiting setSpeed -----");
		 curRequest=hl_drone_grant_command();
		 curInputMsg= curRequest.getReceivedMessage();
		 curInputMsgContent = curInputMsg.msgContent();
		msgCommand =curInputMsg.msgContent() ;
		cmdName =Drone.getCommandName(msgCommand) ;
		start =cmdName.contains("setspeed") ;
		
		{//XBlockcode
		boolean _start = start;
		expXabseResult=_start;
		}//XBlockcode
		if(  (Boolean)expXabseResult ){ //cond
		curRequest.replyToCaller( "OK" ); 
		curstate = "st_Drone_startMission"; 
		//resetCurVars(); //leave the current values on
		return;
		}//if cond
		curRequest.replyToCaller( "FAIL" ); 
		showMsg("ERROR: expected 'setspeed' command to start. Received: "+cmdName);
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Drone_startMission()  throws Exception{
		
		startMission(  );hl_drone_emit_notify( "start" );
		curstate = "st_Drone_setspeed"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Drone_setspeed()  throws Exception{
		
		cmdValue =Drone.getCommandValue(curInputMsgContent) ;
		setSpeed( cmdValue );curstate = "st_Drone_onMission"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Drone_onMission()  throws Exception{
		
		sensorsDatas =getDataFromSensors(  ) ;
		hl_drone_emit_sensorsData( sensorsDatas );
		dataPhoto =getDataPhoto(  ) ;
		hl_drone_forward_photo_headQuarter(dataPhoto );
		//[it.unibo.indigo.contact.impl.SignalImpl@4189ffa7 (name: sensorsData) (var: null), it.unibo.indigo.contact.impl.SignalImpl@4aef4e8e (name: notify) (var: null)] | command isSignal=false
		resCheck = checkForMsg(getName(),"command",null);
		if(resCheck){
			curstate = "st_Drone_commandHandler";
			return;}
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Drone_commandHandler()  throws Exception{
		
		 curRequest=hl_drone_grant_command();
		 curInputMsg= curRequest.getReceivedMessage();
		 curInputMsgContent = curInputMsg.msgContent();
		cmdName =Drone.getCommandName(curInputMsgContent) ;
		stop =cmdName.contains("stop") ;
		speed =cmdName.contains("setspeed") ;
		
		{//XBlockcode
		boolean _operator_or = false;
		boolean _stop = stop;
		if (_stop) {
		  _operator_or = true;
		} else {
		  boolean _speed = speed;
		  _operator_or = BooleanExtensions.operator_or(_stop, _speed);
		}
		expXabseResult=_operator_or;
		}//XBlockcode
		if(  (Boolean)expXabseResult ){ //cond
		curRequest.replyToCaller( "OK" ); 
		
		{//XBlockcode
		boolean _stop = stop;
		expXabseResult=_stop;
		}//XBlockcode
		if(  (Boolean)expXabseResult ){ //cond
		curstate = "st_Drone_endMission"; 
		//resetCurVars(); //leave the current values on
		return;
		}//if cond
		
		{//XBlockcode
		boolean _speed = speed;
		expXabseResult=_speed;
		}//XBlockcode
		if(  (Boolean)expXabseResult ){ //cond
		curstate = "st_Drone_setspeed"; 
		//resetCurVars(); //leave the current values on
		return;
		}//if cond
		}//if cond
		curRequest.replyToCaller( "FAIL" ); 
		curstate = "st_Drone_onMission"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	protected void st_Drone_endMission()  throws Exception{
		
		endMission(  );hl_drone_emit_notify( "end" );
		/* --- TRANSITION TO NEXT STATE --- */
		resetCurVars();
		do_terminationState();
		endStateControl=true;
	}
	protected void st_Drone_init()  throws Exception{
		
		showMsg("----- Drone Initialized -----");
		curstate = "st_Drone_ready"; 
		//resetCurVars(); //leave the current values on
		return;
		/* --- TRANSITION TO NEXT STATE --- */
	}
	
   	
 	/* -------------------------------------
	* COMMUNICATION CORE OPERATIONS for drone
	* --------------------------------------
	*/
 
	protected void hl_drone_forward_photo_headQuarter( String M  ) throws Exception {
	M = MsgUtil.putInEnvelope(M);
	IMessage m = new Message("headQuarter_photo("+getName()+",photo,"+M+","+msgNum+")");
	comSup.outOnly( "headQuarter","photo",getName() , m );
	msgNum++;
	
	}
	
	protected void hl_drone_emit_sensorsData( String M  ) throws Exception {
	M = MsgUtil.putInEnvelope(M);
	IMessage m = new Message("signal("+getName()+",sensorsData,"+M+","+msgNum+")");
	comSup.outOnly( getName() ,"sensorsData",  m );
	msgNum++;
	
	}
	
	protected void hl_drone_emit_notify( String M  ) throws Exception {
	M = MsgUtil.putInEnvelope(M);
	IMessage m = new Message("signal("+getName()+",notify,"+M+","+msgNum+")");
	comSup.outOnly( getName() ,"notify",  m );
	msgNum++;
	
	}
	
	protected IMessageAndContext hl_drone_grant_command(   ) throws Exception {
	//EXPERT for COMPOSED drone_grant_command isInput=true withAnswer=true applVisible=true
	IMessageAndContext answer = comSup.inOut(
	getName() ,"command", 
	"drone_command(ANYx1y2,command,M,N)" ); 
	return answer;
	
	}
	
	
 	/* -------------------------------------
	* CONNECTION OPERATIONS for drone
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
	  if( msgId.equals("command")){
	  	curRequest = hl_drone_grant_command();
	  	m = curRequest.getReceivedMessage();
	  	return m;		
	  }
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
		droneForward_photo_headQuarterEnd();
		droneEmit_sensorsDataEnd();
		droneEmit_notifyEnd();
		droneGrant_commandEnd();
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
	protected void droneForward_photo_headQuarterEnd() throws Exception{
		PlatformExpert.findOutSupportToEnd("drone","photo",getName(),view );
		//showMsg("terminate droneForward_photo_headQuarter");
	}
	protected void droneEmit_sensorsDataEnd() throws Exception{
		//No operation is done at subject level. The SenseRemote threads are terminates 
		//when the main application is closed
	//		PlatformExpert.findOutSupportToEnd("space","coreCmd","coreToDSpace", view);		
	//		showMsg("terminate signal support");
	}
	protected void droneEmit_notifyEnd() throws Exception{
		//No operation is done at subject level. The SenseRemote threads are terminates 
		//when the main application is closed
	//		PlatformExpert.findOutSupportToEnd("space","coreCmd","coreToDSpace", view);		
	//		showMsg("terminate signal support");
	}
	protected void droneGrant_commandEnd() throws Exception{
	 	PlatformExpert.findInSupportToEnd(getName(),"command",view );
		//showMsg("terminate droneGrant_command");
	}
}//DroneSupport
