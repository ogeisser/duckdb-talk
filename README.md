# DuckDB at Devoxx

Demo material for a hands-on DuckDB talk. The numbered directories follow the talk's topic order and contain the examples and explanations for revisiting the demos afterward.

## Downloading the Sample Data

The large sample files `sample-data/taxi_trips.parquet` and `sample-data/bike_trips.duckdb` are stored using Git LFS (Large File Storage). Install Git LFS before cloning so Git downloads the actual data files rather than just their small pointer files.

On macOS with Homebrew:

```sh
brew install git-lfs
git lfs install
git clone https://github.com/ogeisser/duckdb-talk.git
```

For other platforms, see the [Git LFS installation instructions](https://github.com/git-lfs/git-lfs#installing).

If you have already cloned the repository, install and initialize Git LFS as above, then run this command from the repository directory:

```sh
git lfs pull
```

The two files require approximately 710 MiB of download. Use a Git clone with Git LFS to obtain the complete sample data.

## Demo Overview

| Topic | What it covers | Material available |
| --- | --- | --- |
| [1. Introduction](01_Intro/Intro.md) | CLI, in-memory and persistent databases, SQL scripts and the Web UI. | Guide and SQL example; the Web UI section currently contains launch commands only. |
| [2. Java](04_Languages/Java/Java.md) and [Python](04_Languages/Python/Python.md) | Embedded DuckDB using JDBC, Java's chunked result API and Python. | Guides and runnable source files. |
| [3. File types](08_Filetypes/Filetypes.md) | CSV, nested JSON, Parquet and Excel. | Guide and SQL scripts. |
| [4. Remote storage](12_Storage/Storage.md) | Reading CSV files over HTTPS and authenticated S3 access. | Guide with inline SQL. |
| [5. Attaching databases](15_Attach/Attach.md) | Querying DuckDB, SQLite and PostgreSQL databases through `ATTACH`. | Guide with inline SQL and a PostgreSQL setup script. |
| [6. DuckLake](20_Ducklake/Ducklake.md) | Local and PostgreSQL catalogs with local or S3 data files. | Guide with three inline SQL demos; builds on Storage and Attach. |
| [7. Quack](25_Quack/Quack.md) | Quack client/server connections using the DuckDB 2.0 preview. | Guide with inline SQL. |
