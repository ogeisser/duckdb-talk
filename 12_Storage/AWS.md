# AWS and S3 Preparation

Prepare your AWS credentials, S3 bucket, sample data and DuckDB authentication before running the S3 examples in the remote storage and DuckLake demos.

## Prepare Your AWS Profile and Bucket

This part assumes basic familiarity with S3 buckets, objects and access permissions. Before continuing, install the AWS CLI and configure a local AWS profile with valid credentials. Create your own S3 bucket and give the profile permissions to list the bucket and read, upload and delete objects used by the demo.

The commands below use these example values:

| Setting | Example value | What to use |
| --- | --- | --- |
| S3 bucket | `devoxx26-demo-bucket` | Your own bucket with a unique name. The example bucket is not available for participants to use. |
| AWS profile | `ducklake-demo` | Any profile name you choose, including an existing configured profile. |
| AWS region | `eu-central-1` | The region of your bucket. |

Replace the bucket name in every S3 path, including the secret's `SCOPE`. Replace the profile name in both the AWS CLI's `--profile` arguments and the secret's `PROFILE` option, and set `REGION` to your bucket's region. Use the same values when continuing with the [DuckLake demo](../20_Ducklake/Ducklake.md).

## Check Access and Upload the Sample Data

Run these AWS CLI commands in a terminal from `sample-data`. Use your bucket and profile names:

```sh
aws s3 ls s3://devoxx26-demo-bucket/ --profile ducklake-demo
aws s3 cp ../README.md s3://devoxx26-demo-bucket/README.md --profile ducklake-demo
aws s3 rm s3://devoxx26-demo-bucket/README.md --profile ducklake-demo
```

These commands list the bucket contents, upload the local README and delete the uploaded object. The upload replaces any existing object at that key; the local README remains unchanged.

Next, upload the CSV used by the DuckDB query:

```sh
aws s3 cp beach_clubs.csv s3://devoxx26-demo-bucket/sample-data/beach_clubs.csv --profile ducklake-demo
```

This creates the object `sample-data/beach_clubs.csv` in your bucket. The S3 query in the remote storage demo will check that DuckDB can read it using the same profile.

## Configure DuckDB Authentication

For S3, `httpfs` handles file access. The `aws` extension retrieves credentials through the AWS SDK. In this demo, it uses the `credential_chain` provider to read credentials from a configured AWS profile.

Start a DuckDB CLI session from the terminal if one is not already open:

```sh
duckdb
```

In the DuckDB session, install and load the required extensions:

```sql
INSTALL httpfs;
LOAD httpfs;
INSTALL aws;
LOAD aws;
```

Create a secret using your profile, region and bucket name:

```sql
CREATE OR REPLACE PERSISTENT SECRET ducklake_s3 (
    TYPE s3,
    PROVIDER credential_chain,
    CHAIN 'config',
    PROFILE 'ducklake-demo',
    REGION 'eu-central-1',
    SCOPE 's3://devoxx26-demo-bucket/'
);
```

`ducklake_s3` is the DuckDB secret's name, separate from the AWS profile name. Keep this secret name for the subsequent DuckLake demo; choosing a different AWS profile does not require renaming the secret.

| Option | Purpose |
| --- | --- |
| `TYPE s3` | Creates a secret for S3 access. |
| `PROVIDER credential_chain` | Retrieves credentials through the AWS SDK. |
| `CHAIN 'config'` | Uses the AWS configuration/profile credential source. |
| `PROFILE` | Selects your configured local AWS profile. |
| `REGION` | Sets the region for requests. |
| `SCOPE` | Associates this secret with matching S3 path prefixes. AWS permissions determine access. |

`OR REPLACE` lets you update the secret by running the statement again. `PERSISTENT` makes it available in later DuckDB sessions, including the DuckLake demo, independently of the current database. By default, DuckDB stores persistent secrets unencrypted in `~/.duckdb/stored_secrets`.

Source: [AWS extension](https://duckdb.org/docs/current/core_extensions/aws), [Secrets manager](https://duckdb.org/docs/current/configuration/secrets_manager)
