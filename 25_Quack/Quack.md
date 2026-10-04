# DuckDB – The Quack Protocol

Quack is DuckDB's native protocol for connecting DuckDB processes over the network. The `quack` extension lets a DuckDB process serve its databases to remote clients, adding a client/server mode to the embedded database.

## How the Protocol Works

- **HTTP transport:** Quack uses HTTP or HTTPS, so it can work with standard reverse proxies and load balancers.
- **Client-driven communication:** The client sends requests; the server responds. The server does not push unsolicited messages.
- **Native serialization:** Requests and responses use `application/duckdb` and DuckDB's internal serialization. Nested values, decimals and intervals retain their types across the connection.
- **Query and result exchange:** After the connection handshake, a query uses a request–response pair. Large results are retrieved in chunks through additional `FETCH` requests, which can run in parallel.
- **Endpoint addressing:** The `quack:` URI identifies the server. The default port is `9494`, for example `quack:server.example.com:9494`.

The client sends SQL to the server, the server executes it against its databases, and the client receives the results through Quack.

Source: [Quack Remote Protocol](https://duckdb.org/docs/current/quack/overview#quack-in-a-nutshell)

## Prerequisites

This demo uses the **DuckDB 2.0 prerelease (preview)** on both server and client, with the `quack` extension available. The following SQL statements control the Quack connection. Preview behavior and syntax may change before the final release.

Install the DuckDB alpha version on both server and client:

```sh
curl https://install.duckdb.org | DUCKDB_VERSION=alpha bash
```

## 1. Start the Server

Start the server's DuckDB session from `sample-data` and run:

```sql
CALL quack_serve(token = 'my_secret_token');
```

`quack_serve` starts the Quack endpoint in the server process. The token authenticates clients. Replace the example value with your own token.

## 2. Attach from the Client

Open a separate DuckDB session from `sample-data`. For a server on another machine, replace `localhost` with its hostname. Use the same token:

```sql
ATTACH 'quack:localhost' AS qk (TOKEN 'my_secret_token');
```

The `quack:` prefix selects the protocol; `qk` names the remote attachment in the client session.

## 3. Execute SQL on the Server

`CONNECT` directs subsequent SQL statements to the attached server; results stream back to the client.

```sql
CONNECT qk;
```


## 4. Return to Local Execution

```sql
DISCONNECT;
```

`DISCONNECT` returns execution to the local DuckDB session.

Source: [A Preview of DuckDB v2.0 – Quack and CONNECT](https://duckdb.org/2026/08/17/duckdb-20-highlights#1-duckdb-as-a-server-quack-and-connect)
