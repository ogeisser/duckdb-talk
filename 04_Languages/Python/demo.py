# /// script
# requires-python = ">=3.14,<3.15"
# dependencies = ["duckdb==1.5.6"]
# ///

import duckdb

# Open an in-memory database; the context manager closes the connection.
with duckdb.connect(":memory:") as connection:
    # Run the same query as the Java examples: numbers 0 through 4.
    result = connection.execute("SELECT * FROM range(5)")

    # Fetch the small result as tuples; Python column indexes start at 0.
    for row in result.fetchall():
        print(row[0])
