#!/usr/bin/env bash
set -euo pipefail

# Start Colima VM
colima start

# Run PostgreSQL Docker container
docker run -d \
  --name duckdb-demo-postgres \
  -p 127.0.0.1:5432:5432 \
  -e POSTGRES_USER=demo \
  -e POSTGRES_PASSWORD=demo \
  -e POSTGRES_DB=demo \
  -v duckdb-demo-postgres-data:/var/lib/postgresql \
  postgres:18

# Wait until PostgreSQL accepts connections.
for attempt in {1..60}; do
  if docker exec duckdb-demo-postgres pg_isready -U demo -d demo >/dev/null 2>&1; then
    break
  fi
  if [ "$attempt" -eq 60 ]; then
    echo "PostgreSQL did not become ready within 60 seconds." >&2
    exit 1
  fi
  sleep 1
done

# Init PostgreSQL
duckdb -c "
  INSTALL postgres;

  LOAD postgres;

  CREATE OR REPLACE PERSISTENT SECRET pg_demo (
      TYPE postgres,
      HOST '127.0.0.1',
      PORT 5432,
      DATABASE 'demo',
      USER 'demo',
      PASSWORD 'demo'
  );

  ATTACH 'postgres:' AS pg (SECRET pg_demo);

  USE pg;

  CREATE TABLE taxi_trips AS FROM read_parquet('taxi_trips.parquet');
"
