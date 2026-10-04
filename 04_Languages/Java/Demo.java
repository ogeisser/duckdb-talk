//JAVA 25
//JAVA_OPTIONS --enable-native-access=ALL-UNNAMED
//DEPS org.duckdb:duckdb_jdbc:1.5.6.0

import java.sql.DriverManager;

public class Demo {
    public static void main(String[] args) throws Exception {

        // Try-with-resources manages all three resources
        try (
            // Open an in-memory DuckDB database inside this Java process.
            var connection = DriverManager.getConnection("jdbc:duckdb::memory:");

            // Prepare the SQL using a standard JDBC PreparedStatement.
            var statement = connection.prepareStatement("SELECT * FROM range(5)");

            // Run the query: range(5) produces the numbers 0 through 4.
            var result = statement.executeQuery()

        ) {

            // Move to the next row; JDBC column indexes start at 1.
            while (result.next()) {
                System.out.println(result.getLong(1));
            }
        }
        // Resources close in reverse order: result, statement, connection.
        // This also happens if an exception leaves the try block.
    }
}
