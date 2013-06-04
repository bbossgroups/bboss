package com.frameworkset.common.poolman.sql;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

public class PoolManDatabaseMetaData implements DatabaseMetaData,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private boolean _allProceduresAreCallable;

	private boolean _allTablesAreSelectable;

	private String _URL;

	private String _userName;

	private boolean _readOnly;

	private boolean _nullsAreSortedHigh;

	private boolean _nullsAreSortedLow;

	private boolean _nullsAreSortedAtStart;

	private boolean _nullsAreSortedAtEnd;

	private String _databaseProductName;

	private String _databaseProductVersion;
	
	private String _driverName;

	private String _driverVersion;

	private int _driverMajorVersion;

	private int _driverMinorVersion;

	private boolean _usesLocalFiles;

	private boolean _usesLocalFilePerTable;

	private boolean _supportsMixedCaseIdentifiers;

	private boolean _storesUpperCaseIdentifiers;

	private boolean _storesLowerCaseIdentifiers;

	private boolean _storesMixedCaseIdentifiers;

	private boolean _supportsMixedCaseQuotedIdentifiers;

	private boolean _storesUpperCaseQuotedIdentifiers;

	private boolean _storesLowerCaseQuotedIdentifiers;

	private boolean _storesMixedCaseQuotedIdentifiers;

	private String _identifierQuoteString;

	private String _SQLKeywords;

	private String _numericFunctions;

	private String _stringFunctions;

	private String _systemFunctions;

	private String _timeDateFunctions;

	private String _searchStringEscape;

	private String _extraNameCharacters;

	private boolean _supportsAlterTableWithAddColumn;

	private boolean _supportsAlterTableWithDropColumn;

	private boolean _supportsColumnAliasing;

	private boolean _nullPlusNonNullIsNull;

	private boolean _supportsConvert;

	

	private boolean _supportsTableCorrelationNames;

	private boolean _supportsDifferentTableCorrelationNames;

	private boolean _supportsExpressionsInOrderBy;

	private boolean _supportsOrderByUnrelated;

	private boolean _supportsGroupBy;

	private boolean _supportsGroupByUnrelated;

	private boolean _supportsGroupByBeyondSelect;

	private boolean _supportsLikeEscapeClause;

	private boolean _supportsMultipleResultSets;

	private boolean _supportsMultipleTransactions;

	private boolean _supportsNonNullableColumns;

	private boolean _supportsMinimumSQLGrammar;

	private boolean _supportsCoreSQLGrammar;

	private boolean _supportsExtendedSQLGrammar;

	private boolean _supportsANSI92EntryLevelSQL;

	private boolean _supportsANSI92IntermediateSQL;

	private boolean _supportsANSI92FullSQL;

	private boolean _supportsIntegrityEnhancementFacility;

	private boolean _supportsOuterJoins;

	private boolean _supportsFullOuterJoins;

	private boolean _supportsLimitedOuterJoins;

	private String _schemaTerm;

	private String _procedureTerm;

	private String _patalogTerm;

	private boolean _catalogAtStart;

	private String _catalogSeparator;

	private boolean _supportsSchemasInDataManipulation;

	private boolean _supportsSchemasInProcedureCalls;

	private boolean _supportsSchemasInTableDefinitions;

	private boolean _supportsSchemasInIndexDefinitions;

	private boolean _supportsSchemasInPrivilegeDefinitions;

	private boolean _supportsCatalogsInDataManipulation;

	private boolean _supportsCatalogsInProcedureCalls;

	private boolean _supportsCatalogsInTableDefinitions;

	private boolean _supportsCatalogsInIndexDefinitions;

	private boolean _supportsCatalogsInPrivilegeDefinitions;

	private boolean _supportsPositionedDelete;

	private boolean _supportsPositionedUpdate;

	private boolean _supportsSelectForUpdate;

	private boolean _supportsStoredProcedures;

	private boolean _supportsSubqueriesInComparisons;

	private boolean _supportsSubqueriesInExists;

	private boolean _supportsSubqueriesInIns;

	private boolean _supportsSubqueriesInQuantifieds;

	private boolean _supportsCorrelatedSubqueries;

	private boolean _supportsUnion;

	private boolean _supportsUnionAll;

	private boolean _supportsOpenCursorsAcrossCommit;

	private boolean _supportsOpenCursorsAcrossRollback;

	private boolean _supportsOpenStatementsAcrossCommit;

	private boolean _supportsOpenStatementsAcrossRollback;

	private int _MaxBinaryLiteralLength;

	private int _MaxCharLiteralLength;

	private int _MaxColumnNameLength;

	private int _MaxColumnsInGroupBy;

	private int _MaxColumnsInIndex;

	private int _MaxColumnsInOrderBy;

	private int _MaxColumnsInSelect;

	private int _MaxColumnsInTable;

	private int _MaxConnections;

	private int _MaxCursorNameLength;

	private int _MaxIndexLength;

	private int _MaxSchemaNameLength;

	private int _MaxProcedureNameLength;

	private int _MaxCatalogNameLength;

	private int _MaxRowSize;

	private boolean _doesMaxRowSizeIncludeBlobs;

	private int _MaxStatementLength;

	private int _MaxStatements;

	private int _MaxTableNameLength;

	private int _MaxTablesInSelect;

	private int _MaxUserNameLength;

	private int _DefaultTransactionIsolation;

	private boolean _supportsTransactions;

	private boolean _supportsTransactionIsolationLevel;
	private boolean _supportsDataDefinitionAndDataManipulationTransactions;

	private boolean _supportsDataManipulationTransactionsOnly;

	private boolean _dataDefinitionCausesTransactionCommit;

	private boolean _dataDefinitionIgnoredInTransactions;

	private ResultSet _Procedures;

	private ResultSet _ProcedureColumns;

	private ResultSet _Tables;

	private ResultSet _Schemas;

	private ResultSet _Catalogs;

	private ResultSet _TableTypes;

	private ResultSet _Columns;

	private ResultSet _ColumnPrivileges;

	private ResultSet _TablePrivileges;

	private ResultSet _BestRowIdentifier;

	private ResultSet _VersionColumns;

	private ResultSet _PrimaryKeys;

	private ResultSet _ImportedKeys;

	private ResultSet _ExportedKeys;

	private ResultSet _CrossReference;

	private ResultSet _TypeInfo;

	private ResultSet _IndexInfo;

	private boolean _supportsResultSetType;

	private boolean _supportsResultSetConcurrency;

	private boolean _ownUpdatesAreVisible;
	private boolean _ownDeletesAreVisible;
	private boolean _ownInsertsAreVisible;
	private boolean _othersUpdatesAreVisible;
	private boolean _othersDeletesAreVisible;
	private boolean _othersInsertsAreVisible;
	private boolean _updatesAreDetected;
	private boolean _deletesAreDetected;
	private boolean _insertsAreDetected;
	private boolean _supportsBatchUpdates;

	private ResultSet _UDTs;

	public Connection getConnection;

	private boolean _supportsSavepoints;

	private boolean _supportsNamedParameters;

	private boolean _supportsMultipleOpenResults;

	private boolean _supportsGetGeneratedKeys;

	private ResultSet _SuperTypes;

	private ResultSet _SuperTables;

	private ResultSet _Attributes;

	private boolean _supportsResultSetHoldability;

	private int _ResultSetHoldability;

	private int _DatabaseMajorVersion;

	private int _databaseMinorVersion;

	private int _JDBCMajorVersion;

	private int _JDBCMinorVersion;

	private int _SQLStateType;

	private boolean _locatorsUpdateCopy;

	private boolean _supportsStatementPooling;

	
	
	public DatabaseMetaData getCopy(DatabaseMetaData original) throws java.sql.SQLException {
		if(original instanceof PoolManDatabaseMetaData)
			return original;
		else
		{
			return new PoolManDatabaseMetaData(original);
		}
	}
	
	public PoolManDatabaseMetaData(DatabaseMetaData original)
	{
		
	}

	public boolean allProceduresAreCallable() throws SQLException {
		
		return false;
	}

	public boolean allTablesAreSelectable() throws SQLException {
		
		return false;
	}

	public String getURL() throws SQLException {
		
		return null;
	}

	public String getUserName() throws SQLException {
		
		return null;
	}

	public boolean isReadOnly() throws SQLException {
		
		return false;
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		
		return false;
	}

	public boolean nullsAreSortedLow() throws SQLException {
		
		return false;
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		
		return false;
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		
		return false;
	}

	public String getDatabaseProductName() throws SQLException {
		
		return null;
	}

	public String getDatabaseProductVersion() throws SQLException {
		
		return null;
	}

	public String getDriverName() throws SQLException {
		
		return null;
	}

	public String getDriverVersion() throws SQLException {
		
		return null;
	}

	public int getDriverMajorVersion() {
		
		return 0;
	}

	public int getDriverMinorVersion() {
		
		return 0;
	}

	public boolean usesLocalFiles() throws SQLException {
		
		return false;
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		
		return false;
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		
		return false;
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		
		return false;
	}

	public String getIdentifierQuoteString() throws SQLException {
		
		return null;
	}

	public String getSQLKeywords() throws SQLException {
		
		return null;
	}

	public String getNumericFunctions() throws SQLException {
		
		return null;
	}

	public String getStringFunctions() throws SQLException {
		
		return null;
	}

	public String getSystemFunctions() throws SQLException {
		
		return null;
	}

	public String getTimeDateFunctions() throws SQLException {
		
		return null;
	}

	public String getSearchStringEscape() throws SQLException {
		
		return null;
	}

	public String getExtraNameCharacters() throws SQLException {
		
		return null;
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		
		return false;
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		
		return false;
	}

	public boolean supportsColumnAliasing() throws SQLException {
		
		return false;
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		
		return false;
	}

	public boolean supportsConvert() throws SQLException {
		
		return false;
	}

	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		
		return false;
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		
		return false;
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		
		return false;
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		
		return false;
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		
		return false;
	}

	public boolean supportsGroupBy() throws SQLException {
		
		return false;
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		
		return false;
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		
		return false;
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		
		return false;
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		
		return false;
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		
		return false;
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		
		return false;
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		
		return false;
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		
		return false;
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		
		return false;
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		
		return false;
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		
		return false;
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		
		return false;
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		
		return false;
	}

	public boolean supportsOuterJoins() throws SQLException {
		
		return false;
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		
		return false;
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		
		return false;
	}

	public String getSchemaTerm() throws SQLException {
		
		return null;
	}

	public String getProcedureTerm() throws SQLException {
		
		return null;
	}

	public String getCatalogTerm() throws SQLException {
		
		return null;
	}

	public boolean isCatalogAtStart() throws SQLException {
		
		return false;
	}

	public String getCatalogSeparator() throws SQLException {
		
		return null;
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		
		return false;
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		
		return false;
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		
		return false;
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		
		return false;
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		
		return false;
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		
		return false;
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		
		return false;
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		
		return false;
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		
		return false;
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		
		return false;
	}

	public boolean supportsPositionedDelete() throws SQLException {
		
		return false;
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		
		return false;
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		
		return false;
	}

	public boolean supportsStoredProcedures() throws SQLException {
		
		return false;
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		
		return false;
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		
		return false;
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		
		return false;
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		
		return false;
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		
		return false;
	}

	public boolean supportsUnion() throws SQLException {
		
		return false;
	}

	public boolean supportsUnionAll() throws SQLException {
		
		return false;
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		
		return false;
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		
		return false;
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		// 
		return false;
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		
		return false;
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		
		return 0;
	}

	public int getMaxCharLiteralLength() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInIndex() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInSelect() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInTable() throws SQLException {
		
		return 0;
	}

	public int getMaxConnections() throws SQLException {
		
		return 0;
	}

	public int getMaxCursorNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxIndexLength() throws SQLException {
		
		return 0;
	}

	public int getMaxSchemaNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxProcedureNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxCatalogNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxRowSize() throws SQLException {
		
		return 0;
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		
		return false;
	}

	public int getMaxStatementLength() throws SQLException {
		
		return 0;
	}

	public int getMaxStatements() throws SQLException {
		
		return 0;
	}

	public int getMaxTableNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxTablesInSelect() throws SQLException {
		
		return 0;
	}

	public int getMaxUserNameLength() throws SQLException {
		
		return 0;
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		
		return 0;
	}

	public boolean supportsTransactions() throws SQLException {
		
		return false;
	}

	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		
		return false;
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		
		return false;
	}

	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		
		return false;
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		
		return false;
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		
		return false;
	}

	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		
		return null;
	}

	public ResultSet getSchemas() throws SQLException {
		
		return null;
	}

	public ResultSet getCatalogs() throws SQLException {
		
		return null;
	}

	public ResultSet getTableTypes() throws SQLException {
		
		return null;
	}

	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
		
		return null;
	}

	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		
		return null;
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		
		return null;
	}

	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		
		return null;
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		
		return null;
	}

	public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		
		return null;
	}

	public ResultSet getTypeInfo() throws SQLException {
		
		return null;
	}

	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
		
		return null;
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		
		return false;
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		
		return false;
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		
		return false;
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		
		return false;
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		
		return false;
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		
		return false;
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		
		return false;
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		
		return false;
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		
		return false;
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		
		return false;
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		
		return false;
	}

	public boolean supportsBatchUpdates() throws SQLException {
		
		return false;
	}

	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
		
		return null;
	}

	public Connection getConnection() throws SQLException {
		
		return null;
	}

	public boolean supportsSavepoints() throws SQLException {
		
		return false;
	}

	public boolean supportsNamedParameters() throws SQLException {
		
		return false;
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		
		return false;
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		
		return false;
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		
		return null;
	}

	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
		
		return null;
	}

	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		
		return false;
	}

	public int getResultSetHoldability() throws SQLException {
		
		return 0;
	}

	public int getDatabaseMajorVersion() throws SQLException {
		
		return 0;
	}

	public int getDatabaseMinorVersion() throws SQLException {
		
		return 0;
	}

	public int getJDBCMajorVersion() throws SQLException {
		
		return 0;
	}

	public int getJDBCMinorVersion() throws SQLException {
		
		return 0;
	}

	public int getSQLStateType() throws SQLException {
		
		return 0;
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		
		return false;
	}

	public boolean supportsStatementPooling() throws SQLException {
		
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public RowIdLifetime getRowIdLifetime() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getSchemas(String catalog, String schemaPattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public ResultSet getClientInfoProperties() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getFunctions(String catalog, String schemaPattern,
			String functionNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getFunctionColumns(String catalog, String schemaPattern,
			String functionNamePattern, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	public ResultSet getPseudoColumns(String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean generatedKeyAlwaysReturned() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
