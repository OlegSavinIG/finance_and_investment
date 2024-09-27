-- V1__init_schema.sql
CREATE TABLE IF NOT EXISTS statistic_entity (
    id UInt64,
    ticker String,
    type String,
    result Int32,
    creationTime DateTime,
    closedTime DateTime,
    userId UInt64
) ENGINE = MergeTree()
ORDER BY (id);
