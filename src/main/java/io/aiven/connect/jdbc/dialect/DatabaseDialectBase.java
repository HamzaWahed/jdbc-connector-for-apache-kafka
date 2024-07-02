package io.aiven.connect.jdbc.dialect;

import io.aiven.connect.jdbc.source.TimestampIncrementingCriteria;
import io.aiven.connect.jdbc.util.*;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.errors.ConnectException;

import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface DatabaseDialectBase extends ConnectionProvider {
    /**
     * Return the name of the dialect.
     *
     * @return the dialect's name; never null
     */
    String name();

    /**
     * Create a new prepared statement using the specified database connection.
     *
     * @param connection the database connection; may not be null
     * @param query      the query expression for the prepared statement; may not be null
     * @return a new prepared statement; never null
     * @throws SQLException if there is an error with the database connection
     */
    PreparedStatement createPreparedStatement(
            Connection connection,
            String query
    ) throws SQLException;

    /**
     * Parse the supplied simple name or fully qualified name for a table into a {@link TableId}.
     *
     * @param fqn the fully qualified string representation; may not be null
     * @return the table identifier; never null
     */
    TableId parseTableIdentifier(String fqn);

    /**
     * Get the identifier rules for this database.
     *
     * @return the identifier rules
     */
    IdentifierRules identifierRules();

    /**
     * Get a new {@link ExpressionBuilder} that can be used to build expressions with quoted
     * identifiers.
     *
     * @return the builder; never null
     * @see #identifierRules()
     * @see IdentifierRules#expressionBuilder(boolean)
     */
    ExpressionBuilder expressionBuilder();

    /**
     * Return current time at the database
     *
     * @param connection database connection
     * @param cal        calendar
     * @return the current time at the database
     * @throws SQLException if there is an error with the database connection
     */
    Timestamp currentTimeOnDB(
            Connection connection,
            Calendar cal
    ) throws SQLException, ConnectException;

    /**
     * Get a list of identifiers of the non-system tables in the database.
     *
     * @param connection database connection
     * @return a list of tables; never null
     * @throws SQLException if there is an error with the database connection
     */
    List<TableId> tableIds(Connection connection) throws SQLException;

    /**
     * Determine if the specified table exists in the database.
     *
     * @param connection the database connection; may not be null
     * @param tableId    the identifier of the table; may not be null
     * @return true if the table exists, or false otherwise
     * @throws SQLException if there is an error accessing the metadata
     */
    boolean tableExists(Connection connection, TableId tableId) throws SQLException;

    /**
     * Create the definition for the columns described by the database metadata using the current
     * schema and catalog patterns defined in the configuration.
     *
     * @param connection    the database connection; may not be null
     * @param tablePattern  the pattern for matching the tables; may be null
     * @param columnPattern the pattern for matching the columns; may be null
     * @return the column definitions keyed by their {@link ColumnId}; never null
     * @throws SQLException if there is an error accessing the metadata
     */
    Map<ColumnId, ColumnDefinition> describeColumns(
            Connection connection,
            String tablePattern,
            String columnPattern
    ) throws SQLException;

    /**
     * Create the definition for the columns described by the database metadata.
     *
     * @param connection     the database connection; may not be null
     * @param catalogPattern the pattern for matching the catalog; may be null
     * @param schemaPattern  the pattern for matching the schemas; may be null
     * @param tablePattern   the pattern for matching the tables; may be null
     * @param columnPattern  the pattern for matching the columns; may be null
     * @return the column definitions keyed by their {@link ColumnId}; never null
     * @throws SQLException if there is an error accessing the metadata
     */
    Map<ColumnId, ColumnDefinition> describeColumns(
            Connection connection,
            String catalogPattern,
            String schemaPattern,
            String tablePattern,
            String columnPattern
    ) throws SQLException;

    /**
     * Create the definition for the columns in the result set.
     *
     * @param rsMetadata the result set metadata; may not be null
     * @return the column definitions keyed by their {@link ColumnId} and in the same order as the
     *     result set; never null
     * @throws SQLException if there is an error accessing the result set metadata
     */
    Map<ColumnId, ColumnDefinition> describeColumns(ResultSetMetaData rsMetadata) throws SQLException;

    /**
     * Get the definition of the specified table.
     *
     * @param connection the database connection; may not be null
     * @param tableId    the identifier of the table; may not be null
     * @return the table definition; null if the table does not exist
     * @throws SQLException if there is an error accessing the metadata
     */
    TableDefinition describeTable(Connection connection, TableId tableId) throws SQLException;

    /**
     * Create the definition for the columns in the result set returned when querying the table. This
     * may not work if the table is empty.
     *
     * @param connection the database connection; may not be null
     * @param tableId    the name of the table; may be null
     * @return the column definitions keyed by their {@link ColumnId}; never null
     * @throws SQLException if there is an error accessing the result set metadata
     */
    Map<ColumnId, ColumnDefinition> describeColumnsByQuerying(
            Connection connection,
            TableId tableId
    ) throws SQLException;

    /**
     * Create a criteria generator for queries that look for changed data using timestamp and
     * incremented columns.
     *
     * @param incrementingColumn the identifier of the incremented column; may be null if there is
     *                           none
     * @param timestampColumns   the identifiers of the timestamp column; may be null if there is
     *                           none
     * @return the {@link TimestampIncrementingCriteria} implementation; never null
     */
    TimestampIncrementingCriteria criteriaFor(
            ColumnId incrementingColumn,
            List<ColumnId> timestampColumns
    );

    /**
     * Use the supplied {@link SchemaBuilder} to add a field that corresponds to the column with the
     * specified definition.
     *
     * @param column  the definition of the column; may not be null
     * @param builder the schema builder; may not be null
     * @return the name of the field, or null if no field was added
     */
    String addFieldToSchema(ColumnDefinition column, SchemaBuilder builder);
}
