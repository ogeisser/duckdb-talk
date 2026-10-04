# DuckDB – DuckLake

DuckLake specification: [ducklake.select](https://ducklake.select/).

DuckLake separates the metadata catalog from the data files. This demo starts locally, moves data storage to S3, and then uses PostgreSQL for the catalog. The same SQL queries work with all three configurations.

## Prerequisites

Complete these demos first:

- [Remote Storage](../12_Storage/Storage.md): configure AWS CLI access and create the persistent S3 secret `ducklake_s3` using the `ducklake-demo` profile.
- [Attach](../15_Attach/Attach.md): run the setup script to prepare PostgreSQL and create the persistent PostgreSQL secret `pg_demo`.

Keep the PostgreSQL container running. Use the same local user and DuckDB secrets directory as in those demos so the persistent secrets are available in this session. S3 access must allow reading and writing data under `s3://devoxx26-demo-bucket/ducklake-demo/`. The PostgreSQL user must be able to create and update catalog tables in the `demo` database.


Start a fresh `duckdb` session from `sample-data` and run the following SQL examples in that session.

## 1. Local Catalog and Local Data Files


```sql
ATTACH 'ducklake:local_local_catalog.duckdb' AS lake_local (
    DATA_PATH 'data_files/'
);
```

```sql
USE lake_local;
```

```sql
CREATE TABLE IF NOT EXISTS shakespeare AS FROM read_parquet('shakespeare.parquet');
```

The catalog records table definitions, snapshots and file references. DuckLake manages the Parquet files under `data_files/`; the source Parquet file remains unchanged. Small writes can be stored inside the catalog instead, as explained below.

## 2. Local Catalog and S3 Data Files


```sql
ATTACH 'ducklake:local_s3_catalog.duckdb' AS lake_s3 (
    DATA_PATH 's3://devoxx26-demo-bucket/ducklake-demo/local-catalog/'
);
```

```sql
USE lake_s3;
```

```sql
CREATE TABLE IF NOT EXISTS shakespeare AS FROM read_parquet('shakespeare.parquet');
```


DuckDB selects the existing `ducklake_s3` secret by its matching S3 scope. The catalog stays on your machine while DuckLake writes its data files to S3.


## 3. PostgreSQL Catalog and S3 Data Files


```sql
ATTACH 'ducklake:postgres:' AS lake_pg (
    META_SECRET 'pg_demo',
    METADATA_SCHEMA 'ducklake_demo',
    DATA_PATH 's3://devoxx26-demo-bucket/ducklake-demo/postgres-catalog/'
);
```

```sql
USE lake_pg;
```

```sql
CREATE TABLE IF NOT EXISTS shakespeare AS FROM read_parquet('shakespeare.parquet');
```

`META_SECRET` passes `pg_demo` to the PostgreSQL catalog connection. `METADATA_SCHEMA` keeps DuckLake's metadata in a dedicated PostgreSQL schema. S3 authentication still uses `ducklake_s3`.

After `USE lake_pg;`, the import and queries are unchanged. PostgreSQL manages the catalog while DuckDB queries the lake's data. This is separate from the ordinary PostgreSQL table created in the Attach demo.


Source: [Connecting to DuckLake](https://ducklake.select/docs/stable/duckdb/usage/connecting)

## Flushing Small Writes to Parquet

Small writes can be inlined in the catalog to avoid creating many tiny Parquet files. To materialize inlined data as Parquet, call the flush function with the lake's attachment name:

```sql
CALL ducklake_flush_inlined_data('lake_pg');
```

Use `lake_local` or `lake_s3` for the other configurations. If the import wrote directly to Parquet, there may be no inlined data left to flush. Flushing does not move the catalog or migrate data between lakes.

Source: [Data inlining](https://ducklake.select/docs/stable/duckdb/advanced_features/data_inlining)

## Running the Demo Again

Exit with `.quit`, start `duckdb` again from `sample-data`, and repeat the SQL for the desired configuration. The catalog and data files persist after the CLI exits. `CREATE TABLE IF NOT EXISTS` preserves an existing table, so rerunning does not import duplicate rows or refresh changed input data.

Each independent catalog uses its own data directory or S3 prefix. Keep those locations paired when reconnecting. For a completely fresh run, choose a new catalog file (or PostgreSQL metadata schema) and a new data path together.
