# DuckDB – Remote Storage

DuckDB can read remote files directly from SQL without a separate download step. This demo first imports a public CSV file over HTTPS, then imports a CSV file from your own Amazon S3 bucket using AWS credentials.

## Starting the Demo

From `sample-data`, start a fresh DuckDB CLI session:

```sh
duckdb
```

Run the SQL examples in this session.

## Remote File Access over HTTPS

The `httpfs` extension provides remote file access. It supports reading over HTTP(S) and reading and writing files through the S3 API. DuckDB normally loads it automatically when needed

Read the CSV file with the HTTP protocol instead of a local filesystem.

```sql
FROM 'https://blobs.duckdb.org/stations.csv';
```

DuckDB uses the URL like a local file path and automatically detects the CSV's delimiter, header and column types.

Source: [httpfs extension](https://duckdb.org/docs/current/core_extensions/httpfs/overview), [CSV import](https://duckdb.org/docs/current/data/csv/overview)

## Remote File Access to S3

Before running this part of the demo, prepare AWS and S3 and configure DuckDB authentication with a persistent S3 secret. Follow the [AWS and S3 preparation guide](AWS.md).

Read a CSV file from S3:

```sql
FROM 's3://devoxx26-demo-bucket/sample-data/beach_clubs.csv';
```

The S3 path identifies your bucket and the object key `sample-data/beach_clubs.csv`. DuckDB selects the secret whose scope matches that path and reads the CSV.
