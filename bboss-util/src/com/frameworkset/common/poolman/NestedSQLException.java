package com.frameworkset.common.poolman;

import java.sql.SQLException;

/**
 * Class to allow passing an Exception with the original SQLException 
 */
public class NestedSQLException extends SQLException {

  /**
   * Constructor from java.sql.SQLException
   * @see java.sql.SQLException
   * @param msg - the message for the exception
   */
  public NestedSQLException(String msg) {
    super(msg);
  }

  /**
   * Constructor from java.sql.SQLException
   * @see java.sql.SQLException
   * @param reason - the reason for the exception
   * @param SQLState - the SQLState
   */
  public NestedSQLException(String reason, String SQLState) {
    super(reason, SQLState);
  }

  /**
   * Constructor from java.sql.SQLException
   * @see java.sql.SQLException
   * @param reason - the reason for the exception
   * @param SQLState - the SQLState
   * @param vendorCode - a vendor supplied code to go w/ the message
   */
  public NestedSQLException(String reason, String SQLState, int vendorCode) {
    super(reason, SQLState, vendorCode);
  }

  /**
   * Constructor from java.sql.SQLException with added nested exception
   * @param msg - the message for the exception
   * @param cause - the cause of the exception
   */
  public NestedSQLException(String msg, Throwable cause) {
    super(msg);
    initCause(cause);
  }

  /**
   * Constructor from java.sql.SQLException with added nested exception
   * @see java.sql.SQLException
   * @param reason - the reason for the exception
   * @param SQLState - the SQLState
   * @param cause - the cause of the exception
   */
  public NestedSQLException(String reason, String SQLState, Throwable cause) {
    super(reason, SQLState);
    initCause(cause);
  }

  /**
   * Constructor from java.sql.SQLException with added nested exception
   * @param reason - the reason for the exception
   * @param SQLState - the SQLState
   * @param vendorCode - a vendor supplied code to go w/ the message
   * @param cause - the cause of the exception
   */
  public NestedSQLException(String reason, String SQLState, int vendorCode, Throwable cause) {
    super(reason, SQLState, vendorCode);
    initCause(cause);
  }

  /**
   * Constructor from java.sql.SQLException with added nested exception
   * @param cause - the cause of the exception
   */
  public NestedSQLException(Throwable cause) {
    super();
    initCause(cause);
  }
}
