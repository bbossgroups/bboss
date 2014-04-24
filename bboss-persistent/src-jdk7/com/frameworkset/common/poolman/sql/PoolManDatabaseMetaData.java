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

	private DatabaseMetaData original;
	
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
		this.original = original;
	}

	public boolean allProceduresAreCallable() throws SQLException {
		
		return original.allProceduresAreCallable();
	}

	public boolean allTablesAreSelectable() throws SQLException {
		
		return original.allTablesAreSelectable();
	}

	public String getURL() throws SQLException {
		
		return original.getURL();
	}

	public String getUserName() throws SQLException {
		
		return original.getUserName();
	}

	public boolean isReadOnly() throws SQLException {
		
		return original.isReadOnly();
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		
		return original.nullsAreSortedHigh();
	}

	public boolean nullsAreSortedLow() throws SQLException {
		
		return original.nullsAreSortedLow();
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		
		return original.nullsAreSortedAtStart();
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		
		return original.nullsAreSortedAtEnd();
	}

	public String getDatabaseProductName() throws SQLException {
		
		return original.getDatabaseProductName();
	}

	public String getDatabaseProductVersion() throws SQLException {
		
		return original.getDatabaseProductVersion();
	}

	public String getDriverName() throws SQLException {
		
		return original.getDriverName();
	}

	public String getDriverVersion() throws SQLException {
		
		return original.getDriverVersion();
	}

	public int getDriverMajorVersion() {
		
		return original.getDriverMajorVersion();
	}

	public int getDriverMinorVersion() {
		
		return original.getDriverMinorVersion();
	}

	public boolean usesLocalFiles() throws SQLException {
		
		return original.usesLocalFiles();
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		
		return original.usesLocalFilePerTable();
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		
		return original.supportsMixedCaseIdentifiers();
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		
		return original.storesUpperCaseIdentifiers();
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		
		return original.storesLowerCaseIdentifiers();
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		
		return original.storesMixedCaseIdentifiers();
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		
		return original.supportsMixedCaseQuotedIdentifiers();
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		
		return original.storesUpperCaseQuotedIdentifiers();
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		
		return original.storesLowerCaseQuotedIdentifiers();
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		
		return original.storesMixedCaseQuotedIdentifiers();
	}

	public String getIdentifierQuoteString() throws SQLException {
		
		return original.getIdentifierQuoteString();
	}

	public String getSQLKeywords() throws SQLException {
		
		return original.getSQLKeywords();
	}

	public String getNumericFunctions() throws SQLException {
		
		return original.getNumericFunctions();
	}

	public String getStringFunctions() throws SQLException {
		
		return original.getStringFunctions();
	}

	public String getSystemFunctions() throws SQLException {
		
		return original.getSystemFunctions() ;
	}

	public String getTimeDateFunctions() throws SQLException {
		
		return original.getTimeDateFunctions();
	}

	public String getSearchStringEscape() throws SQLException {
		
		return original.getSearchStringEscape();
	}

	public String getExtraNameCharacters() throws SQLException {
		
		return original.getExtraNameCharacters();
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		
		return original.supportsAlterTableWithAddColumn();
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		
		return original.supportsAlterTableWithDropColumn();
	}

	public boolean supportsColumnAliasing() throws SQLException {
		
		return original.supportsColumnAliasing();
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		
		return original.nullPlusNonNullIsNull();
	}

	public boolean supportsConvert() throws SQLException {
		
		return original.supportsConvert();
	}

	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		
		return original.supportsConvert( fromType,  toType);
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		
		return original.supportsTableCorrelationNames();
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		
		return original.supportsDifferentTableCorrelationNames();
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		
		return original.supportsExpressionsInOrderBy();
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		
		return original.supportsOrderByUnrelated();
	}

	public boolean supportsGroupBy() throws SQLException {
		
		return original.supportsGroupBy();
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		
		return original.supportsGroupByUnrelated();
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		
		return original.supportsGroupByBeyondSelect();
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		
		return original.supportsLikeEscapeClause();
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		
		return original.supportsMultipleResultSets();
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		
		return original.supportsMultipleTransactions();
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		
		return original.supportsNonNullableColumns();
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		
		return original.supportsMinimumSQLGrammar();
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		
		return original.supportsCoreSQLGrammar();
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		
		return original.supportsExtendedSQLGrammar();
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		
		return original.supportsANSI92EntryLevelSQL();
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		
		return original.supportsANSI92IntermediateSQL();
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		
		return original.supportsANSI92FullSQL();
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		
		return original.supportsIntegrityEnhancementFacility();
	}

	public boolean supportsOuterJoins() throws SQLException {
		
		return original.supportsOuterJoins();
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		
		return original.supportsFullOuterJoins();
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		
		return original.supportsLimitedOuterJoins();
	}

	public String getSchemaTerm() throws SQLException {
		
		return original.getSchemaTerm();
	}

	public String getProcedureTerm() throws SQLException {
		
		return original.getProcedureTerm();
	}

	public String getCatalogTerm() throws SQLException {
		
		return original.getCatalogTerm();
	}

	public boolean isCatalogAtStart() throws SQLException {
		
		return original.isCatalogAtStart();
	}

	public String getCatalogSeparator() throws SQLException {
		
		return original.getCatalogSeparator();
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		
		return original.supportsSchemasInDataManipulation();
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		
		return original.supportsSchemasInProcedureCalls();
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		
		return original.supportsSchemasInTableDefinitions();
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		
		return original.supportsSchemasInIndexDefinitions();
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		
		return original.supportsSchemasInPrivilegeDefinitions();
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		
		return original.supportsCatalogsInDataManipulation();
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		
		return original.supportsCatalogsInProcedureCalls();
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		
		return original.supportsCatalogsInTableDefinitions();
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		
		return original.supportsCatalogsInIndexDefinitions();
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		
		return original.supportsCatalogsInPrivilegeDefinitions();
	}

	public boolean supportsPositionedDelete() throws SQLException {
		
		return original.supportsPositionedDelete();
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		
		return original.supportsPositionedUpdate();
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		
		return original.supportsSelectForUpdate();
	}

	public boolean supportsStoredProcedures() throws SQLException {
		
		return original.supportsStoredProcedures();
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		
		return original.supportsSubqueriesInComparisons();
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		
		return original.supportsSubqueriesInExists();
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		
		return original.supportsSubqueriesInIns();
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		
		return original.supportsSubqueriesInQuantifieds();
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		
		return original.supportsCorrelatedSubqueries();
	}

	public boolean supportsUnion() throws SQLException {
		
		return original.supportsUnion();
	}

	public boolean supportsUnionAll() throws SQLException {
		
		return original.supportsUnionAll();
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		
		return original.supportsOpenCursorsAcrossCommit();
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		
		return original.supportsOpenCursorsAcrossRollback();
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		// 
		return original.supportsOpenStatementsAcrossCommit();
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		
		return original.supportsOpenStatementsAcrossRollback();
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		
		return original.getMaxBinaryLiteralLength();
	}

	public int getMaxCharLiteralLength() throws SQLException {
		
		return original.getMaxCharLiteralLength();
	}

	public int getMaxColumnNameLength() throws SQLException {
		
		return original.getMaxColumnNameLength();
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		
		return original.getMaxColumnsInGroupBy();
	}

	public int getMaxColumnsInIndex() throws SQLException {
		
		return original.getMaxColumnsInIndex();
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		
		return original.getMaxColumnsInOrderBy();
	}

	public int getMaxColumnsInSelect() throws SQLException {
		
		return original.getMaxColumnsInSelect();
	}

	public int getMaxColumnsInTable() throws SQLException {
		
		return original.getMaxColumnsInTable();
	}

	public int getMaxConnections() throws SQLException {
		
		return original.getMaxConnections();
	}

	public int getMaxCursorNameLength() throws SQLException {
		
		return original.getMaxCursorNameLength();
	}

	public int getMaxIndexLength() throws SQLException {
		
		return original.getMaxIndexLength();
	}

	public int getMaxSchemaNameLength() throws SQLException {
		
		return original.getMaxSchemaNameLength();
	}

	public int getMaxProcedureNameLength() throws SQLException {
		
		return original.getMaxProcedureNameLength();
	}

	public int getMaxCatalogNameLength() throws SQLException {
		
		return original.getMaxCatalogNameLength();
	}

	public int getMaxRowSize() throws SQLException {
		
		return original. getMaxRowSize();
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		
		return original.doesMaxRowSizeIncludeBlobs();
	}

	public int getMaxStatementLength() throws SQLException {
		
		return original.getMaxStatementLength();
	}

	public int getMaxStatements() throws SQLException {
		
		return original.getMaxStatements();
	}

	public int getMaxTableNameLength() throws SQLException {
		
		return original.getMaxTableNameLength();
	}

	public int getMaxTablesInSelect() throws SQLException {
		
		return original.getMaxTablesInSelect();
	}

	public int getMaxUserNameLength() throws SQLException {
		
		return original. getMaxUserNameLength();
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		
		return original.getDefaultTransactionIsolation();
	}

	public boolean supportsTransactions() throws SQLException {
		
		return original.supportsTransactions();
	}

	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		
		return original.supportsTransactionIsolationLevel( level);
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		
		return original.supportsDataDefinitionAndDataManipulationTransactions();
	}

	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		
		return original.supportsDataManipulationTransactionsOnly();
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		
		return original.dataDefinitionCausesTransactionCommit();
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		
		return original.dataDefinitionIgnoredInTransactions();
	}

	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
		
		return original.getProcedures( catalog,  schemaPattern,  procedureNamePattern);
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
		
		return original.getProcedureColumns( catalog,  schemaPattern,  procedureNamePattern,  columnNamePattern);
	}

	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		
		return original. getTables( catalog,  schemaPattern,  tableNamePattern,  types);
	}

	public ResultSet getSchemas() throws SQLException {
		
		return original.getSchemas();
	}

	public ResultSet getCatalogs() throws SQLException {
		
		return original.getCatalogs() ;
	}

	public ResultSet getTableTypes() throws SQLException {
		
		return original.getTableTypes();
	}

	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		
		return original.getColumns( catalog,  schemaPattern,  tableNamePattern,  columnNamePattern);
	}

	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
		
		return original.getColumnPrivileges( catalog,  schema,  table,  columnNamePattern) ;
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		
		return original.getTablePrivileges( catalog,  schemaPattern,  tableNamePattern);
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
		
		return original.getBestRowIdentifier( catalog,  schema,  table,  scope,  nullable);
	}

	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		
		return original.getVersionColumns( catalog,  schema,  table);
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		
		return original.getPrimaryKeys( catalog,  schema,  table);
	}

	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		
		return original.getImportedKeys( catalog,  schema,  table);
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		
		return original.getExportedKeys( catalog,  schema,  table);
	}

	public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		
		return original.getCrossReference( primaryCatalog,  primarySchema,  primaryTable,  foreignCatalog,  foreignSchema,  foreignTable);
	}

	public ResultSet getTypeInfo() throws SQLException {
		
		return original.getTypeInfo();
	}

	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
		
		return original.getIndexInfo( catalog,  schema,  table,  unique,  approximate);
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		
		return original.supportsResultSetType( type);
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		
		return original.supportsResultSetConcurrency( type,  concurrency);
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		
		return original.ownUpdatesAreVisible( type);
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		
		return original.ownDeletesAreVisible( type);
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		
		return original.ownInsertsAreVisible( type);
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		
		return original.othersUpdatesAreVisible( type);
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		
		return original.othersDeletesAreVisible( type);
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		
		return original.othersInsertsAreVisible( type);
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		
		return original.updatesAreDetected( type);
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		
		return original.deletesAreDetected( type) ;
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		
		return original.insertsAreDetected( type);
	}

	public boolean supportsBatchUpdates() throws SQLException {
		
		return original.supportsBatchUpdates();
	}

	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
		
		return original.getUDTs( catalog,  schemaPattern,  typeNamePattern,  types);
	}

	public Connection getConnection() throws SQLException {
		
		return original.getConnection();
	}

	public boolean supportsSavepoints() throws SQLException {
		
		return original.supportsSavepoints();
	}

	public boolean supportsNamedParameters() throws SQLException {
		
		return original.supportsNamedParameters();
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		
		return original.supportsMultipleOpenResults();
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		
		return original.supportsGetGeneratedKeys();
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		
		return original.getSuperTypes( catalog,  schemaPattern,  typeNamePattern);
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		
		return original.getSuperTables( catalog,  schemaPattern,  tableNamePattern);
	}

	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
		
		return original.getAttributes( catalog,  schemaPattern,  typeNamePattern,  attributeNamePattern);
	}

	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		
		return original.supportsResultSetHoldability( holdability);
	}

	public int getResultSetHoldability() throws SQLException {
		
		return original.getResultSetHoldability();
	}

	public int getDatabaseMajorVersion() throws SQLException {
		
		return original.getDatabaseMajorVersion();
	}

	public int getDatabaseMinorVersion() throws SQLException {
		
		return original.getDatabaseMinorVersion();
	}

	public int getJDBCMajorVersion() throws SQLException {
		
		return original.getJDBCMajorVersion();
	}

	public int getJDBCMinorVersion() throws SQLException {
		
		return original.getJDBCMinorVersion();
	}

	public int getSQLStateType() throws SQLException {
		
		return original.getSQLStateType();
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		
		return original.locatorsUpdateCopy();
	}

	public boolean supportsStatementPooling() throws SQLException {
		
		return original.supportsStatementPooling();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return original.unwrap( iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return original.isWrapperFor( iface);
	}

	public RowIdLifetime getRowIdLifetime() throws SQLException {
		// TODO Auto-generated method stub
		return original.getRowIdLifetime() ;
	}

	public ResultSet getSchemas(String catalog, String schemaPattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return original.getSchemas( catalog,  schemaPattern);
	}

	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		// TODO Auto-generated method stub
		return original.supportsStoredFunctionsUsingCallSyntax();
	}

	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return original.autoCommitFailureClosesAllResultSets();
	}

	public ResultSet getClientInfoProperties() throws SQLException {
		// TODO Auto-generated method stub
		return original.getClientInfoProperties();
	}

	public ResultSet getFunctions(String catalog, String schemaPattern,
			String functionNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return original.getFunctions( catalog,  schemaPattern,
				 functionNamePattern);
	}

	public ResultSet getFunctionColumns(String catalog, String schemaPattern,
			String functionNamePattern, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return original.getFunctionColumns( catalog,  schemaPattern,
				 functionNamePattern,  columnNamePattern);
	}

	@Override
	public ResultSet getPseudoColumns(String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return original.getPseudoColumns( catalog,  schemaPattern,
				 tableNamePattern,  columnNamePattern);
	}

	@Override
	public boolean generatedKeyAlwaysReturned() throws SQLException {
		// TODO Auto-generated method stub
		return original.generatedKeyAlwaysReturned();
	}

}
