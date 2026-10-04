# PostgreSQL Preparation

Prepare PostgreSQL and DuckDB authentication before running the PostgreSQL examples in the Attach and DuckLake demos.

## Run the Setup Script

The PostgreSQL setup requires DuckDB, Docker and Colima. Run the setup script from `sample-data`:

```sh
bash ../15_Attach/init_pg.sh
```

[init_pg.sh](init_pg.sh) prepares PostgreSQL, loads the sample data and creates the persistent secret `pg_demo`. Keep PostgreSQL running for the Attach demo and the subsequent [DuckLake demo](../20_Ducklake/Ducklake.md).
