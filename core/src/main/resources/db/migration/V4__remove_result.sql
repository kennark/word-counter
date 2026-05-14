ALTER TABLE uploads
    DROP COLUMN result;

CREATE SEQUENCE IF NOT EXISTS word_count_id_seq;
ALTER TABLE word_count
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE word_count
    ALTER COLUMN id SET DEFAULT nextval('word_count_id_seq');

ALTER SEQUENCE word_count_id_seq OWNED BY word_count.id;