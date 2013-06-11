package it.unibo.droneMission.interfaces;
/**
 * @model 
 */
public interface ILocTracker extends IGaugeVariant{
	/**
	 * @model 
	 */
	public static final double MAX=DefaultValues.MAXLOC;	
	/**
	 * @model 
	 */
	public static final double MIN=DefaultValues.MINLOC;
	
	public static final double INITLAT = DefaultValues.INITLAT;
	
	public static final double INITLON = DefaultValues.INITLON;
	
	void setVal(IGaugeValue lat, IGaugeValue lon);
}
