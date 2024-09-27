-- V1__init_schema.sql
CREATE TABLE IF NOT EXISTS statistic_entity (
    ticker String,
    type Enum8('LONG' = 1, 'SHORT' = 2),
    result Int32,
    creationTime DateTime,
    closedTime DateTime,
    programCreationTime DateTime,
    userId UInt64
) ENGINE = MergeTree()
ORDER BY (userId, programCreationTime)
PARTITION BY toYYYYMM(closedTime)
SETTINGS index_granularity = 4096;