package it.unibo.droneMission.gauge;

import it.unibo.droneMission.interfaces.IGaugeDisplay;
import it.unibo.droneMission.interfaces.IGaugeValue;

public class LocTrackerWithDisplay extends LocTracker {
	protected IGaugeDisplay DisLat;
	protected IGaugeDisplay DisLon;
	protected String displayedLat;
	protected String displayedLon;
	
	
	public LocTrackerWithDisplay(IGaugeDisplay D1,IGaugeDisplay D2){
		super();
		DisLat = D1;
		DisLat = D2;
		DisLat.update(new GaugeValueDouble(INITLAT));
		DisLon.update(new GaugeValueDouble(INITLON));
		displayedLat=DisLon.getCurVal();
		displayedLon=DisLon.getCurVal();
	}
	
	@Override
	public void update() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public IGaugeValue getVal() {
		// TODO Auto-generated method stub
		return super.getVal();
	}

	@Override
	public String getCurValRepDisplayed() {
		// TODO Auto-generated method stub
		return displayedLat+" "+displayedLon;
	}

	@Override
	public void setVal(IGaugeValue lat, IGaugeValue lon) {
		// TODO Auto-generated method stub
		super.setVal(lat, lon);
		if((check(lat))&&(check(lon))){
			DisLat.update(lat);
			DisLat.getCurVal();
			DisLon.update(lon);
			DisLon.getCurVal();
		}
	}
}
