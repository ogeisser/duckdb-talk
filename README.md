# DuckDB at Devoxx

Demo material for a hands-on DuckDB talk. The numbered directories follow the talk's topic order and contain the examples and explanations for revisiting the demos afterward.

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
