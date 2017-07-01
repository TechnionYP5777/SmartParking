package main.java.data.members;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

/* dbMember class - class representing single parseObject in Parse server. 
@author DavidCohen55
@since 2017-03-27
*/


public abstract class dbMember {
	protected ParseObject parseObject;
	protected String objectId;

	public String getObjectId() {
		return objectId;
	}

	public ParseObject getParseObject() {
		return parseObject;
	}

	public void setParseObject(final String tableName) {
		parseObject = new ParseObject(tableName);
	}

	public void setObjectId() {
		objectId = parseObject.getObjectId();
	}

	public void deleteParseObject() throws ParseException {
		parseObject.delete();
		objectId = "";
	}
}
