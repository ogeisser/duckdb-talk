# DuckDB – Data Import and Export

The examples demonstrate data import from CSV, Parquet, Excel and JSON files, direct file queries, and data export to files.

## Starting the Demo

Start an in-memory database from `sample-data`:

```sh
duckdb
```

Run the examples in the same CLI session. DuckDB resolves relative file paths from its working directory, so the SQL uses only the data file names.

## CSV Import

Import the beach club data from a CSV file into a table. DuckDB automatically detects the delimiter, header and column types:

```sql
CREATE OR REPLACE TABLE beach_clubs AS
  SELECT * FROM read_csv('beach_clubs.csv');
```

Source: [CSV import](https://duckdb.org/docs/current/data/csv/overview)

## Parquet Import

Import the taxi trips from a Parquet file into a table. Parquet stores typed data in a column-oriented format, so DuckDB reads the schema directly from the file:

```sql
CREATE OR REPLACE TABLE taxi_trips AS
  FROM read_parquet('taxi_trips.parquet');
```

Source: [Reading and writing Parquet files](https://duckdb.org/docs/current/data/parquet/overview)

## Directly Reading Files

Query supported file formats directly by placing a quoted file path in the `FROM` clause. DuckDB selects the appropriate reader based on the file extension: for example, `.csv` uses the CSV reader and `.parquet` uses the Parquet reader.


```sql
FROM 'beach_clubs.csv';
```

```sql
FROM 'taxi_trips.parquet';
```


Use an explicit reader function when you need to pass options, such as a CSV delimiter or column types, or when the file extension does not identify the format correctly.

Source: [Directly reading files](https://duckdb.org/docs/current/guides/file_formats/read_file)

## Excel Import

Import data from the `Customers` sheet of the Northwind Excel workbook:

```sql
FROM read_xlsx('northwind.xlsx', sheet='Customers', all_varchar=true);
```

`sheet` selects the worksheet. `all_varchar=true` reads all columns as text, which is useful for identifiers and postal codes.

Source: [Excel extension](https://duckdb.org/docs/current/core_extensions/excel)

## JSON Import

Example: [json.sql](../sample-data/json.sql)

The sample contains a saved GitHub issue search response: a top-level object with search metadata and an `items` array. Import the JSON into a table, expanding `items` into one row per issue and selecting the desired fields in a single statement, without creating an intermediate table:

```sql
.read json.sql
```

`read_json()` reads the top-level object as one row with an `items` column containing a `LIST` of `STRUCT` values. The `issues` CTE uses `unnest(items)` to produce one row per list element without creating an intermediate table. Each element is a struct named `issue`. Dot notation accesses fields such as `issue.number` and nested fields such as `issue.user.login` and `issue.reactions.total_count`.

Source: [Loading JSON](https://duckdb.org/docs/current/data/json/loading_json), [Unnesting](https://duckdb.org/docs/current/sql/query_syntax/unnest)

## Data Export

DuckDB supports data export to all the formats shown above: CSV, Parquet, JSON and Excel (`.xlsx`, via the `excel` extension). Use `COPY ... TO` to export a table or query result.

As an example, export the `beach_clubs` table created in the CSV import example to a Parquet file:

```sql
COPY beach_clubs TO 'beach_clubs.parquet' (FORMAT PARQUET);
```

The exported file is saved in the current working directory (`sample-data` in this demo).

Source: [COPY statement](https://duckdb.org/docs/current/sql/statements/copy), [Excel export](https://duckdb.org/docs/current/guides/file_formats/excel_export), [Reading and writing Parquet files](https://duckdb.org/docs/current/data/parquet/overview)
