# DuckDB – Attaching Databases

DuckDB can attach other databases and query their tables from the same SQL session. This demo starts with a local DuckDB file, adds a SQLite database, and then connects to PostgreSQL. `ATTACH` makes their tables available without importing them into the current database.

## Starting the Demo

Start a fresh in-memory DuckDB session from `sample-data`:

```sh
duckdb
```

Run the SQL examples below in this session. All file paths are relative to `sample-data`. After each `ATTACH`, use `USE` to select the database so subsequent statements can refer to tables by their short names.

## Attach a DuckDB File

Attach the supplied bike trip database:

```sql
ATTACH 'duckdb:bike_trips.duckdb' AS bikes (READ_ONLY);
```

The `duckdb:` prefix explicitly selects DuckDB's native database format. No additional extension or credentials are required. The prefix is optional for native DuckDB files, but makes the database type explicit in the same way as the SQLite and PostgreSQL examples below. `READ_ONLY` allows queries while preventing changes to the attached database.

The file contains the tables `rides` and `hoods`.


Source: [ATTACH and DETACH](https://duckdb.org/docs/current/sql/statements/attach)

## Attach a SQLite File

Attach a SQLite database using the `sqlite:` prefix:

```sql
ATTACH 'sqlite:demo.sqlite' AS sqlite_db;
```

The prefix selects the `sqlite` extension, which DuckDB loads automatically when needed, so a separate `TYPE sqlite` option is unnecessary. This opens the sample SQLite database file.

Source: [SQLite extension](https://duckdb.org/docs/current/core_extensions/sqlite)

## Attach PostgreSQL

PostgreSQL must be running with the sample data and persistent DuckDB secret `pg_demo` prepared. Follow the [PostgreSQL preparation guide](PostgreSQL.md) before continuing.

Connect using the secret created by the setup script:

```sql
ATTACH 'postgres:' AS pg (SECRET 'pg_demo');
```

The `postgres:` prefix selects the PostgreSQL extension, which DuckDB loads automatically when needed. `SECRET` selects the stored connection settings and credentials. `AS pg` names the attached database within this session.


Source: [PostgreSQL extension](https://duckdb.org/docs/current/core_extensions/postgres/overview), [PostgreSQL secrets](https://duckdb.org/docs/current/core_extensions/postgres/secrets)

All three databases remain attached in the same session. Switch between them with `USE bikes;`, `USE sqlite_db;` or `USE pg;` before querying their tables.

