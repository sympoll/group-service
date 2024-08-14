-- Create the tables
CREATE TABLE groups
(
    group_id     VARCHAR(255) PRIMARY KEY,
    group_name   VARCHAR(255),
    description  TEXT,
    creator_id   UUID,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE members
(
    user_id  UUID                                                        NOT NULL,
    group_id VARCHAR(255) REFERENCES groups (group_id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (group_id, user_id)
);

-- Insert sample data into the groups table
INSERT INTO groups (group_id, group_name, description, creator_id)
VALUES ('group1', 'Developers', 'Group for developers to discuss and share knowledge.',
        'a1e8a3d2-07a8-11ec-bf63-0242ac130002'),
       ('group2', 'Designers', 'Group for designers to collaborate on projects.',
        'a1e8a3d2-07a8-11ec-bf63-0242ac130003'),
       ('group3', 'Managers', 'Group for managers to discuss strategies and plans.',
        'a1e8a3d2-07a8-11ec-bf63-0242ac130004');

-- Insert sample data into the members table
INSERT INTO members (user_id, group_id)
VALUES ('a1e8a3d2-07a8-11ec-bf63-0242ac130005', 'group1'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130006', 'group1'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130007', 'group2'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130008', 'group2'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130009', 'group3');