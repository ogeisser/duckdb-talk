-- Source: https://api.github.com/search/issues?q=repo%3Aduckdb%2Fduckdb%20is%3Aissue&sort=created&order=desc&per_page=10
-- Read JSON, unnest items and select issue fields without an intermediate table.

CREATE OR REPLACE TABLE duckdb_issues AS

WITH issues AS (
    SELECT unnest(items) AS issue FROM read_json('duckdb_issues.json')
)

SELECT
    issue.number AS issue_number,
    issue.title AS title,
    issue.state AS state,
    issue.user.login AS author,
    issue.reactions.total_count AS reactions,
    issue.created_at AS created_at
FROM issues;
