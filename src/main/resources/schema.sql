-- Enable pgcrypto / uuid-ossp for UUID generation if needed
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(30) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    status VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. datasets
CREATE TABLE IF NOT EXISTS datasets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    source_type VARCHAR(30) NOT NULL CHECK (source_type IN ('CSV', 'JSON', 'DATABASE')),
    status VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE', 'ARCHIVED')),
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. dataset_versions
CREATE TABLE IF NOT EXISTS dataset_versions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dataset_id UUID NOT NULL REFERENCES datasets(id),
    version_number INTEGER NOT NULL,
    storage_location TEXT,
    file_format VARCHAR(30) NOT NULL,
    file_size_bytes BIGINT,
    checksum VARCHAR(128),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),
    CONSTRAINT uq_dataset_version UNIQUE (dataset_id, version_number)
);

-- 4. queries
CREATE TABLE IF NOT EXISTS queries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    query_name VARCHAR(150),
    query_text TEXT NOT NULL,
    status VARCHAR(30) NOT NULL CHECK (status IN ('QUEUED', 'RUNNING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    submitted_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. query_executions
CREATE TABLE IF NOT EXISTS query_executions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    query_id UUID NOT NULL REFERENCES queries(id),
    attempt_number INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL CHECK (status IN ('QUEUED', 'RUNNING', 'CANCELLATION_REQUESTED', 'COMPLETED', 'FAILED', 'CANCELLED')),
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    cancellation_requested_at TIMESTAMPTZ,
    error_message TEXT,
    rows_returned BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_query_attempt UNIQUE (query_id, attempt_number)
);

-- 6. query_results
CREATE TABLE IF NOT EXISTS query_results (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    execution_id UUID NOT NULL UNIQUE REFERENCES query_executions(id),
    row_count BIGINT NOT NULL,
    column_metadata JSONB NOT NULL,
    storage_location TEXT,
    result_size_bytes BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ
);

-- 7. query_datasets
CREATE TABLE IF NOT EXISTS query_datasets (
    query_id UUID NOT NULL REFERENCES queries(id),
    dataset_id UUID NOT NULL REFERENCES datasets(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (query_id, dataset_id)
);

-- 8. execution_dataset_versions
CREATE TABLE IF NOT EXISTS execution_dataset_versions (
    execution_id UUID NOT NULL REFERENCES query_executions(id),
    dataset_version_id UUID NOT NULL REFERENCES dataset_versions(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (execution_id, dataset_version_id)
);
