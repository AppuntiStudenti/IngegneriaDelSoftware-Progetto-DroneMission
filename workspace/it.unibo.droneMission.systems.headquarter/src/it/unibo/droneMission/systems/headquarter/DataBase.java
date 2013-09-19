package it.unibo.droneMission.systems.headquarter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import it.unibo.droneMission.interfaces.headquarter.CommandsStatus;
import it.unibo.droneMission.interfaces.headquarter.DataBaseTables;
import it.unibo.droneMission.interfaces.headquarter.IDataBase;
import it.unibo.droneMission.interfaces.messages.ICommand;
import it.unibo.droneMission.interfaces.messages.IFile;
import it.unibo.droneMission.interfaces.messages.INotify;
import it.unibo.droneMission.interfaces.messages.IPicturePackage;
import it.unibo.droneMission.interfaces.messages.IReply;
import it.unibo.droneMission.interfaces.messages.ISensorsData;
import it.unibo.droneMission.prototypes.messages.Command;
import it.unibo.droneMission.prototypes.messages.Factory;
import it.unibo.droneMission.prototypes.messages.File;
import it.unibo.droneMission.prototypes.messages.Notify;
import it.unibo.droneMission.prototypes.messages.PicturePackage;
import it.unibo.droneMission.prototypes.messages.SensorsData;

public abstract class DataBase extends Storage implements IDataBase {

	public final String ASC = "ASC";
	public final String DESC = "DESC";
	
	protected String dbname;
	protected String username;
	protected String password;
	protected String host;
	protected int port;
	
	protected ArrayList<String> select;
	protected ArrayList<String> from;
	protected Hashtable<String, String> where;
	protected String orderBy;
	protected String orderByDirection;
	protected int limit;
	protected int offset;
	
	protected Connection db;
	
	@Override
	public void init() {
		// TODO NEEDS IMPLEMENTATION

	}
	
	public boolean checkTables() {
		// TODO NEEDS IMPLEMENTATION

		try {
			DatabaseMetaData metadata = db.getMetaData();
		} catch (SQLException e) {
			System.err.println("Error trying to get metadata.");
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void disconnect() {
		try {
			db.close();
		} catch (SQLException e) {
			System.err.println("Error trying to closing database connection.");
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		try {
			return db != null && !db.isClosed();
		} catch (SQLException e) {
			System.err.println("Error trying to check if connection is closed.");
			e.printStackTrace();
		}
		return false;
	}
		@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setHostname(String hostname) {
		this.host = hostname;
	}

	@Override
	public String getHostname() {
		return host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void setDatabaseName(String dbname) {
		this.dbname = dbname;		
	}

	@Override
	public String getDatabaseName() {
		return dbname;
	}
	

	@Override
	public void storeCommand(ICommand command) {
		Hashtable<String, String> set = new Hashtable<>();
		set.put(DataBaseTables.COMMANDS_COLUMN_TYPE, "" + command.getType());
		set.put(DataBaseTables.COMMANDS_COLUMN_TIME, "" + command.getTime());
		set.put(DataBaseTables.COMMANDS_COLUMN_STATUS, "" + CommandsStatus.TO_SEND);
		set.put(DataBaseTables.COMMANDS_COLUMN_VALUE, "" + command.getValue());
		
		this.from(DataBaseTables.COMMANDS_TABLENAME);
		this.insert(set);
	}

	@Override
	public void storeCommandReply(IReply reply) {
		ResultSet cmd = _getOldestCommandToSend_SQL();
		if (cmd == null)
			return;
		
		Hashtable<String, String> set = new Hashtable<>();
		
		try {
			cmd.next();
			String cmd_id = cmd.getString(DataBaseTables.COMMANDS_COLUMN_ID);
			// update cmd status
			set.put(DataBaseTables.COMMANDS_COLUMN_STATUS, "" + CommandsStatus.REPLIED);
			this.from(DataBaseTables.COMMANDS_TABLENAME);
			this.where(DataBaseTables.COMMANDS_COLUMN_ID, cmd_id);
			this.update(set);
			
			// store reply
			set.clear();
			set.put(DataBaseTables.REPLIES_COLUMN_TYPE, "" + reply.getType());
			set.put(DataBaseTables.REPLIES_COLUMN_TIME, "" + reply.getTime());
			set.put(DataBaseTables.REPLIES_COLUMN_COMMAND, cmd_id);
			set.put(DataBaseTables.REPLIES_COLUMN_VALUE, "" + reply.getValue());
			
			this.from(DataBaseTables.REPLIES_TABLENAME);
			this.insert(set);
			
		} catch (SQLException e) {
			System.err.println("Error catching command id in storeCommandReply().");
			e.printStackTrace();
		}
		
	}
	
	private ResultSet _getOldestCommandToSend_SQL() {
		
		this.from(DataBaseTables.COMMANDS_TABLENAME);
		this.where(DataBaseTables.COMMANDS_COLUMN_STATUS, "" + CommandsStatus.TO_SEND);
		this.orderBy(DataBaseTables.COMMANDS_COLUMN_ID, this.DESC);
		this.limit(1);
		
		ResultSet cmd = this.get();
		try {
			if (cmd.isBeforeFirst())
				return cmd;
		} catch (SQLException e) {
			System.err.println("Error checking if there's data in oldest command query - getOldestCommandToSend()");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ICommand getCommandToSend() {
		ResultSet cmdSQL = _getOldestCommandToSend_SQL();
		if (cmdSQL == null)
			return null;
		
		try {
			cmdSQL.next();
			int type = cmdSQL.getInt(DataBaseTables.COMMANDS_COLUMN_TYPE);
			int value = cmdSQL.getInt(DataBaseTables.COMMANDS_COLUMN_VALUE);
			long time = cmdSQL.getLong(DataBaseTables.COMMANDS_COLUMN_TIME);
			Command cmd = new Command(type, value, time);
			return cmd;
		} catch (SQLException e) {
			System.err.println("SQL Error catching command values getCommandToSend()");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<ICommand> getLatestCommands(int n) {
		
		if (n <= 0)
			return null;
		
		this.from(DataBaseTables.COMMANDS_TABLENAME);
		this.where(DataBaseTables.COMMANDS_COLUMN_STATUS, "" + CommandsStatus.TO_SEND);
		this.orderBy(DataBaseTables.COMMANDS_COLUMN_ID, this.DESC);
		this.limit(n);
		
		ResultSet set = this.get();
		
		ArrayList<ICommand> list = new ArrayList<>();
		
		try {
			while (set.next()) {
				int type = set.getInt(DataBaseTables.COMMANDS_COLUMN_TYPE);
				int value = set.getInt(DataBaseTables.COMMANDS_COLUMN_VALUE);
				long time = set.getLong(DataBaseTables.COMMANDS_COLUMN_TIME);
				Command cmd = new Command(type, value, time);
				list.add(cmd);
			}
		} catch (SQLException e) {
			System.err.println("Error ciclying result from getLatestCommands(int n)");
			e.printStackTrace();
			return null;
		}
		
		return list;
	}

	@Override
	public void storeNotify(INotify notify) {
		Hashtable<String, String> set = new Hashtable<>();
		set.put(DataBaseTables.NOTIFIES_COLUMN_TYPE, "" + notify.getType());
		set.put(DataBaseTables.NOTIFIES_COLUMN_TIME, "" + notify.getTime());
		set.put(DataBaseTables.NOTIFIES_COLUMN_VALUE, "" + notify.getValue());
		
		this.from(DataBaseTables.NOTIFIES_TABLENAME);
		this.insert(set);

	}

	@Override
	public List<INotify> getLatestNotifies(int n) {
		if (n <= 0)
			return null;
					
		this.from(DataBaseTables.NOTIFIES_TABLENAME);
		this.orderBy(DataBaseTables.NOTIFIES_COLUMN_ID, this.DESC);
		this.limit(n);
		
		ResultSet set = this.get();
		ArrayList<INotify> list = new ArrayList<INotify>();
		
		try {
			while (set.next()) {
				int type = set.getInt(DataBaseTables.NOTIFIES_COLUMN_TYPE);
				String value = set.getString(DataBaseTables.NOTIFIES_COLUMN_VALUE);
				long time = set.getLong(DataBaseTables.NOTIFIES_COLUMN_TIME);
				Notify notify = new Notify(type, value, time);
				list.add(notify);
			}
				
		} catch (SQLException e) {
			System.err.println("Error checking if there's data in oldest notifies query - getLatestNotifies()");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void storeSensorsData(ISensorsData data) {
		_storeSensorsDataAndGetResult_SQL(data, false);
	}

	private ResultSet _storeSensorsDataAndGetResult_SQL(ISensorsData data, boolean getResult) {
		Hashtable<String, String> set = new Hashtable<>();
		set.put(DataBaseTables.SENSORS_COLUMN_DATA, "" + data.toJSON());
		set.put(DataBaseTables.SENSORS_COLUMN_TIME, "" + data.getTime());
		
		this.from(DataBaseTables.SENSORS_TABLENAME);
		
		if(this.insert(set) > 0 && getResult)
			return _getLatestSensorsData_SQL(1);
		
		return null;
	}
	
	private ResultSet _getLatestSensorsData_SQL(int n) {
		this.from(DataBaseTables.SENSORS_TABLENAME);
		this.limit(n);
		this.orderBy(DataBaseTables.SENSORS_COLUMN_ID, this.DESC);
		return this.get();
	}
	
	private ResultSet _getSensorsData_ById_SQL(int id) {
		this.from(DataBaseTables.SENSORS_TABLENAME);
		this.where(DataBaseTables.SENSORS_COLUMN_ID, "" + id);
		return this.get();
	}
	
	@Override
	public List<ISensorsData> getLatestSensorsDatas(int n) {
		ResultSet set = _getLatestSensorsData_SQL(n);
		if (set == null)
			return null;
		ArrayList<ISensorsData> list = new ArrayList<ISensorsData>();
		try {
			while (set.next()) {
				String data = set.getString(DataBaseTables.SENSORS_COLUMN_DATA);
				list.add(Factory.createSensorsData(data));
			}
			
		} catch (SQLException e) {
			System.err.println("Error catching value from getLatestSensorsDatas(int n)");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ISensorsData getSensorsData(long time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storePicturePackage(IPicturePackage pack) {
		
		ResultSet sensorsSQL = _storeSensorsDataAndGetResult_SQL(pack.getSensorsData(), true);
		int sensorsID;
		try {
			sensorsSQL.next();
			sensorsID = sensorsSQL.getInt(DataBaseTables.SENSORS_COLUMN_ID);
		} catch (SQLException e) {
			System.err.println("Error getting ID from sensors ResultSet - storePicturePackage(IPicturePackage pack)");
			e.printStackTrace();
			return;
		}
		
		IFile picture = pack.getFile();
		
		Hashtable<String, String> set = new Hashtable<>();
		set.put(DataBaseTables.PICTURES_COLUMN_FILE_TIME, "" + picture.getCreationTime());
		set.put(DataBaseTables.PICTURES_COLUMN_FILE_NAME, "" + picture.getName());
		set.put(DataBaseTables.PICTURES_COLUMN_FILE_DATA, "" + picture.getDataAsBase64());
		set.put(DataBaseTables.PICTURES_COLUMN_SENSORS, "" + sensorsID);
		
		this.from(DataBaseTables.PICTURES_TABLENAME);
		this.insert(set);

	}
	
	@Override 
	public List<IPicturePackage> getLatestPicturePackages(int n) {
		
		if(n <= 0)
			return null;
		
		File file = new File(); 
		SensorsData sensors = new SensorsData();
		
		int sensors_id = -1;
		
		this.from(DataBaseTables.PICTURES_TABLENAME);
		this.orderBy(DataBaseTables.PICTURES_COLUMN_ID, this.DESC);
		this.limit(n);
		ResultSet set = this.get();
		
		ArrayList<IPicturePackage> list = new ArrayList<>();
		
		if (set == null)
			return null;
		
		try {
			while (set.next()) {	
				try {
				
					String filename = set.getString(DataBaseTables.PICTURES_COLUMN_FILE_NAME);
					String data = set.getString(DataBaseTables.PICTURES_COLUMN_FILE_DATA);
					long time = set.getLong(DataBaseTables.PICTURES_COLUMN_FILE_TIME);
					
					sensors_id = set.getInt(DataBaseTables.PICTURES_COLUMN_SENSORS);
					
					file.setData(data);
					file.setName(filename);
					file.setCreationTime(time);
				
					
				} catch (SQLException e) {
					System.err.println("Error catching file information - getLatestPicturePackages()");
					e.printStackTrace();
					continue;
				}
				
				ResultSet setSensors = _getSensorsData_ById_SQL(sensors_id);
				
				if (setSensors == null)
					continue;
				
				try {
					setSensors.next();
					String data = setSensors.getString(DataBaseTables.SENSORS_COLUMN_DATA);
					sensors = Factory.createSensorsData(data);
				} catch (SQLException e) {
					System.err.println("Error catching sensors information - getLatestPicturePackages()");
					e.printStackTrace();
					continue;
				}
				
				list.add(new PicturePackage(sensors, file));
			}
		} catch (SQLException e) {
			System.err.println("Error cycling set in getLatestPicturePackages()");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void storeFile(IFile file) {
		// TODO Auto-generated method stub

	}

	@Override
	public IFile getFile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFile getFile(long time) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<IFile> getLatestFiles(int n) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void select(String column) {
		this.select.add(column);
	}

	@Override
	public void select(String[] columns) {
		for (String c : columns)
			this.select(c);
	}

	@Override
	public void from(String table) {
		this.from.add(table);
	}

	@Override
	public void from(String[] tables) {
		for (String t : tables)
			this.from(t);
	}

	@Override
	public void where(String key, String value) {
		this.where.put(key, value);
	}

	@Override
	public void where(Hashtable<String, String> set) {
		this.where.putAll(set);
		
	}

	@Override
	public void orderBy(String column, String direction) {
		this.orderBy = column;
		this.orderByDirection = direction;
	}

	@Override
	public void limit(int n) {
		this.limit = n;
	}

	@Override
	public void offset(int n) {
		this.offset = n;
	}
	
	protected void resetSQLParams() {
		select = new ArrayList<String>();
		from = new ArrayList<String>();
		where = new Hashtable<String, String>();
		orderBy = "";
		orderByDirection = "";
		limit = -1;
		offset = -1;
	}
	
}
