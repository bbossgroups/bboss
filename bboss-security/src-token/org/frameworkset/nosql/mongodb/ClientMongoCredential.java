package org.frameworkset.nosql.mongodb;

public class ClientMongoCredential {
	 private String mechanism;
	    private String userName;
	    private String database;
	    private String password;
		public String getMechanism() {
			return mechanism;
		}
		public void setMechanism(String mechanism) {
			this.mechanism = mechanism;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getDatabase() {
			return database;
		}
		public void setDatabase(String database) {
			this.database = database;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

}
