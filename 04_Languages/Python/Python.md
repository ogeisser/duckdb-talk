# DuckDB with Python and uv

This standalone Python example queries DuckDB and prints the results.
[uv](https://docs.astral.sh/uv/) manages Python and dependencies directly from
the script's inline metadata. No separate project configuration, manual virtual
environment setup, or database server is required.

## Setup

Install uv (macOS/Linux):

```sh
curl -LsSf https://astral.sh/uv/install.sh | sh
```

Then open a new terminal. From the project root, change into the Python directory.
All commands below assume this directory. Run the example using its local path:

```sh
uv run demo.py
```

On the first run, uv downloads a compatible Python interpreter if needed and
installs DuckDB into an isolated environment. The script declares its requirements
using inline script metadata (PEP 723):

```python
# /// script
# requires-python = ">=3.14,<3.15"
# dependencies = ["duckdb==1.5.6"]
# ///
```

The Python requirement selects the 3.14 release series, allowing patch updates.
The DuckDB package version is pinned to 1.5.6.

After a successful initial run with an internet connection, the cached
interpreter and dependencies allow offline execution:

```sh
uv run --offline demo.py
```

## How It Works

`duckdb.connect(":memory:")` opens an in-memory database inside the Python
process. The `with` block closes the connection automatically, including when
an exception leaves the block.

`connection.execute()` runs the same query as the Java examples:

```sql
SELECT * FROM range(5)
```

`fetchall()` returns the rows as a list of tuples. The loop prints `row[0]`, the
first column of each row. Python indexes start at **0**. The output is:

```text
0
1
2
3
4
```

`fetchall()` loads the entire result into Python memory, which keeps this small
example simple. Larger results can be consumed in batches with `fetchmany()`.

## Further Reading

- [DuckDB Python API](https://duckdb.org/docs/current/clients/python/overview)
- [Running Scripts with uv](https://docs.astral.sh/uv/guides/scripts/)
- [uv Installation](https://docs.astral.sh/uv/getting-started/installation/)
