
-- 1. Users
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ
);

-- 2. Datasets
CREATE TABLE datasets (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    source_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_dataset_user
        FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT uq_dataset_owner_name
        UNIQUE (created_by, name)
);

-- 3. Dataset versions
CREATE TABLE dataset_versions (
    id UUID PRIMARY KEY,
    dataset_id UUID NOT NULL,
    version_number INTEGER NOT NULL,
    storage_location VARCHAR(1000),
    file_format VARCHAR(30) NOT NULL,
    file_size_bytes BIGINT,
    checksum VARCHAR(128),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    created_by UUID,
    CONSTRAINT fk_version_dataset
        FOREIGN KEY (dataset_id) REFERENCES datasets(id),
    CONSTRAINT fk_version_user
        FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT uq_dataset_version
        UNIQUE (dataset_id, version_number),
    CONSTRAINT uq_version_id_dataset
        UNIQUE (id, dataset_id),
    CONSTRAINT chk_version_number
        CHECK (version_number > 0),
    CONSTRAINT chk_file_size
        CHECK (file_size_bytes IS NULL OR file_size_bytes >= 0)
);

-- 4. Queries
CREATE TABLE queries (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    query_name VARCHAR(150),
    query_text TEXT NOT NULL,
    submitted_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_query_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 5. Query executions
CREATE TABLE query_executions (
    id UUID PRIMARY KEY,
    query_id UUID NOT NULL,
    attempt_number INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    cancellation_requested_at TIMESTAMPTZ,
    error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_execution_query
        FOREIGN KEY (query_id) REFERENCES queries(id),
    CONSTRAINT uq_query_attempt
        UNIQUE (query_id, attempt_number),
    CONSTRAINT chk_attempt_number
        CHECK (attempt_number > 0)
);

-- 6. Query results
CREATE TABLE query_results (
    id UUID PRIMARY KEY,
    execution_id UUID NOT NULL UNIQUE,
    row_count BIGINT NOT NULL,
    column_metadata JSONB,
    storage_location VARCHAR(1000),
    result_size_bytes BIGINT,
    created_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ,
    CONSTRAINT fk_result_execution
        FOREIGN KEY (execution_id) REFERENCES query_executions(id),
    CONSTRAINT chk_result_row_count
        CHECK (row_count >= 0),
    CONSTRAINT chk_result_size
        CHECK (result_size_bytes IS NULL OR result_size_bytes >= 0)
);

-- 7. Query-Dataset relationship
CREATE TABLE query_datasets (
    id UUID PRIMARY KEY,
    query_id UUID NOT NULL,
    dataset_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_query_dataset_query
        FOREIGN KEY (query_id) REFERENCES queries(id),
    CONSTRAINT fk_query_dataset_dataset
        FOREIGN KEY (dataset_id) REFERENCES datasets(id),
    CONSTRAINT uq_query_dataset
        UNIQUE (query_id, dataset_id)
);

-- 8. Execution-Dataset-Version relationship
CREATE TABLE execution_dataset_versions (
    id UUID PRIMARY KEY,
    execution_id UUID NOT NULL,
    dataset_id UUID NOT NULL,
    dataset_version_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_edv_execution
        FOREIGN KEY (execution_id) REFERENCES query_executions(id),
    CONSTRAINT fk_edv_dataset_version
        FOREIGN KEY (dataset_version_id, dataset_id)
        REFERENCES dataset_versions(id, dataset_id),
    CONSTRAINT uq_execution_dataset
        UNIQUE (execution_id, dataset_id)
);

-- Useful indexes
CREATE INDEX idx_queries_user_id
    ON queries(user_id);

CREATE INDEX idx_executions_query_id
    ON query_executions(query_id);

CREATE INDEX idx_executions_status
    ON query_executions(status);

CREATE INDEX idx_datasets_created_by
    ON datasets(created_by);

CREATE INDEX idx_dataset_versions_dataset_id
    ON dataset_versions(dataset_id);

CREATE INDEX idx_query_datasets_dataset_id
    ON query_datasets(dataset_id);
