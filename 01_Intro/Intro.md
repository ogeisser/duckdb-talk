# DuckDB – Intro

DuckDB is a SQL database for analytical queries that runs without a separate database server.

Website: [duckdb.org](https://duckdb.org/)

## Installing the CLI

DuckDB installation [Documentation](https://duckdb.org/install/)

### curl / Shell (macOS and Linux)

The official installation script installs the DuckDB CLI:

```sh
curl https://install.duckdb.org | bash
```

After installation, add DuckDB to your `PATH` for the current shell session:

```sh
export PATH="$HOME/.duckdb/cli/latest:$PATH"
```

For a permanent setup, also add the `export` line to `~/.zshrc` or `~/.bashrc`.

Source: [DuckDB installation script](https://duckdb.org/docs/current/operations_manual/installing_duckdb/install_script)

### Homebrew (Alternative)

With Homebrew installed:

```sh
brew install duckdb
```

Source: [DuckDB on Homebrew](https://formulae.brew.sh/formula/duckdb)

Verify the installation:

```sh
duckdb --version
```

## Starting the CLI

From `sample-data`, open or create a DuckDB database file:

```sh
duckdb demo.duckdb
```

Without a database file, DuckDB uses an in-memory database: tables and data exist only for the current session and are lost when you exit. When you specify a database file such as `demo.duckdb`, DuckDB stores the tables and data on disk so they remain available across sessions.

## CLI Query

Enter SQL in the open DuckDB CLI. A semicolon ends the statement:

```sql
SELECT 'Hello DuckDB!' AS message;
```

```sql
create table numbers(v INTEGER);
```

```sql
insert into numbers values (0);
```

```sql
INSERT INTO numbers (v) FROM range(1, 5);
```

```sql
SELECT sum(v) AS total FROM numbers;
```

The table contains the numbers 0 through 4; their sum is 10.

Source: [CLI overview](https://duckdb.org/docs/current/clients/cli/overview)

## CLI Dot Commands

Dot commands control the CLI. They start with a dot at the beginning of a line and are entered without a semicolon.

| Command | Effect |
| --- | --- |
| `.help` | Show available commands |
| `.databases` | List databases |
| `.tables` | List tables |
| `.schema numbers` | Show the definition of the `numbers` table |
| `.mode csv` | Output results as CSV |
| `.mode duckbox` | Return to the default table format |
| `.sh` | Execute shell command |
| `.cd` | Change working directory |
| `.read file` | Read file and execute as SQL (or dot commands) |
| `.quit` | Exit the CLI |


Source: [Dot commands](https://duckdb.org/docs/current/clients/cli/dot_commands)

## Reading SQL from a File

```text
.read example.sql
```

The CLI stays open afterward. The file can contain both SQL and dot commands. Relative file paths are resolved from the working directory where DuckDB was started.

Source: [Reading SQL from a file](https://duckdb.org/docs/current/clients/cli/overview#reading-sql-from-a-file)

## CLI with `-c <command>`

Run a query directly from the terminal and exit DuckDB afterward:

```sh
duckdb -c "SELECT 'Hello DuckDB!' AS message;"
```

You can also run multiple SQL statements:

```sh
duckdb -c "SELECT 42 AS answer; SELECT current_date AS today;"
```

## CLI with `-f <file>`

Run the supplied SQL file from the terminal and exit DuckDB afterward:

```sh
duckdb -f example.sql
```

Optionally, use a persistent database file:

```sh
duckdb demo.duckdb -f example.sql
```

Source for `-c` and `-f`: [CLI arguments](https://duckdb.org/docs/current/clients/cli/arguments)

## Executable SQL

On macOS and Linux, you can make a SQL file directly executable using a shebang. Prerequisites: `duckdb` is in your `PATH` and `/usr/bin/env` supports `-S`.

```sh
./example.sql
```

## Web UI

```sh
duckdb --ui
```

```sql
call start_ui();
```
