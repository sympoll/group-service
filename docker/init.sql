-- Group Service Schema
CREATE TABLE groups
(
    group_id            VARCHAR(255) PRIMARY KEY,
    group_name          VARCHAR(255),
    description         TEXT,
    creator_id          UUID,
    time_created        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE members
(
    user_id         uuid NOT NULL,
    group_id        VARCHAR(255) REFERENCES groups(group_id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (group_id, user_id)
);