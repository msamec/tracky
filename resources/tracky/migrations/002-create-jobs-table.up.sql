CREATE SCHEMA IF NOT EXISTS proletarian;
--;;
CREATE TABLE IF NOT EXISTS proletarian.job (
    job_id      UUID PRIMARY KEY,
    queue       TEXT      NOT NULL,
    job_type    TEXT      NOT NULL,
    payload     TEXT      NOT NULL,
    attempts    INTEGER   NOT NULL,
    enqueued_at TIMESTAMP NOT NULL,
    process_at  TIMESTAMP NOT NULL
);
--;;
CREATE TABLE IF NOT EXISTS proletarian.archived_job (
    job_id      UUID PRIMARY KEY,
    queue       TEXT      NOT NULL,
    job_type    TEXT      NOT NULL,
    payload     TEXT      NOT NULL,
    attempts    INTEGER   NOT NULL,
    enqueued_at TIMESTAMP NOT NULL,
    process_at  TIMESTAMP NOT NULL,
    status      TEXT      NOT NULL,
    finished_at TIMESTAMP NOT NULL
);
--;;
DROP INDEX IF EXISTS proletarian.job_queue_process_at;
--;;
CREATE INDEX job_queue_process_at ON proletarian.job (queue, process_at);
