CREATE TABLE word_count
(
    id      BIGINT  NOT NULL,
    text_id UUID,
    word    VARCHAR(255),
    count   INTEGER NOT NULL,
    CONSTRAINT pk_wordcount PRIMARY KEY (id)
);