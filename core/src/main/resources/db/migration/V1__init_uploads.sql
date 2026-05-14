CREATE TABLE uploads
(
    id               UUID    NOT NULL,
    file_name        VARCHAR(255),
    chunk_count      INTEGER NOT NULL,
    processed_chunks INTEGER NOT NULL,
    result           TEXT,
    CONSTRAINT pk_uploads PRIMARY KEY (id)
);