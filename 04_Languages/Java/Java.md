# DuckDB with Java and JBang

These two standalone Java examples show how to query DuckDB using standard JDBC
and DuckDB's chunked result API. [JBang](https://www.jbang.dev/) handles Java,
the JDBC driver, and compilation. Each example runs from a single source file
without a Maven/Gradle project or database server.

## Setup

Install JBang (macOS/Linux):

```sh
curl -fsSL https://sh.jbang.dev | bash -s - app setup
```

Then open a new terminal. From the project root, change into the Java directory.
Run the examples using their local paths. All commands below assume this directory:

```sh
jbang run Demo.java
jbang run DemoChunked.java
```

On the first run, JBang downloads Java and dependencies as needed and caches
them. Both source files declare the same configuration:

```java
//JAVA 25
//JAVA_OPTIONS --enable-native-access=ALL-UNNAMED
//DEPS org.duckdb:duckdb_jdbc:1.5.6.0
```

`//JAVA` selects Java 25, and `//DEPS` pins the JDBC driver version.
`//JAVA_OPTIONS` enables native access for classpath libraries, including the
driver, which loads the native DuckDB engine. This avoids Java's restricted
native-access warning.

After running each example successfully with an internet connection, it can also
be run offline:

```sh
jbang run --offline Demo.java
jbang run --offline DemoChunked.java
```

## Standard JDBC: `Demo.java`

`DriverManager.getConnection("jdbc:duckdb::memory:")` opens an in-memory database
inside the Java process. `prepareStatement()` prepares the SQL query, and
`executeQuery()` returns a JDBC `ResultSet`.

The query generates five rows:

```sql
SELECT * FROM range(5)
```

The loop advances through the result with `next()` and reads the first column
with `getLong(1)`. JDBC column indexes start at **1**. The output is:

```text
0
1
2
3
4
```

## Chunked Results: `DemoChunked.java`

This example uses the same database and query and produces the same output.
It accesses DuckDB's columnar results through its extended API:

1. `unwrap(DuckDBConnection.class)` exposes DuckDB-specific connection methods.
2. `prepare()` creates a `DuckDBPreparedStatement`, whose `query()` method
   returns a `DuckDBChunkedResult`.
3. `nextChunk()` fetches the next chunk, and `chunk()` provides a
   `DuckDBDataChunkReader`.
4. `vector(0)` accesses the first column as a `DuckDBReadableVector`.
   `getLong(row)` reads each value within that vector.

Both column and row indexes start at **0** in the chunk API. The inner loop uses
`chunk.rowCount()` because chunks can differ in size. Process the chunk and its
vectors before advancing to the next chunk.

To inspect multiple chunks, change `range(5)` to `range(5000)` and replace the
inner value-printing loop with `System.out.println(chunk.rowCount())`.

The chunk API reduces JDBC row-access overhead for large results. This small
example demonstrates the API, not a speedup: printing every value would dominate
a performance comparison.

## Resource Management

Both examples use try-with-resources for the connection, prepared statement, and
result. Java closes them in reverse order when the block ends: result, statement,
then connection. This also happens when an exception leaves the block.

## Further Reading

- [DuckDB JDBC](https://duckdb.org/docs/current/clients/java/overview)
- [Chunked Query Results in the DuckDB Java Driver](https://duckdb.org/2026/08/21/chunked-query-results-java-driver#reading-chunks-with-duckdbchunkedresult)
- [JBang Quick Start](https://www.jbang.dev/documentation/jbang/latest/quickstart.html)
- [JBang Installation](https://www.jbang.dev/documentation/jbang/latest/installation.html)
