//JAVA 25
//JAVA_OPTIONS --enable-native-access=ALL-UNNAMED
//DEPS org.duckdb:duckdb_jdbc:1.5.6.0

import java.sql.DriverManager;
import org.duckdb.DuckDBConnection;

public class DemoChunked {
    public static void main(String[] args) throws Exception {

        // Try-with-resources manages all three resources.
        try (
            // Unwrap the JDBC connection to access DuckDB-specific methods.
            var connection = DriverManager.getConnection("jdbc:duckdb::memory:")
                .unwrap(DuckDBConnection.class);

            // prepare() returns a DuckDBPreparedStatement.
            var statement = connection.prepare("SELECT * FROM range(5)");

            // query() returns a DuckDBChunkedResult instead of a JDBC ResultSet.
            var result = statement.query()
        ) {
            // Fetch one chunk at a time, then read its first column vector.
            while (result.nextChunk()) {
                var chunk = result.chunk();       // DuckDBDataChunkReader
                var numbers = chunk.vector(0);    // DuckDBReadableVector

                // Chunk column and row indexes start at 0; range returns BIGINT.
                for (long row = 0; row < chunk.rowCount(); row++) {
                    System.out.println(numbers.getLong(row));
                }
                // Consume the chunk and its vectors before advancing.
            }
        }
        // Resources close in reverse order: result, statement, connection.
    }
}
