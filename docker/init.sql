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

CREATE TABLE roles
(
    role_id   UUID PRIMARY KEY,
    role_name VARCHAR(255),
    UNIQUE (role_name)
);

CREATE TABLE user_roles
(
    user_id  UUID REFERENCES members (user_id) ON DELETE CASCADE NOT NULL,
    group_id VARCHAR(255) REFERENCES groups (group_id) ON DELETE CASCADE NOT NULL,
    role_id  UUID REFERENCES roles (role_id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (user_id, group_id, role_id)
);


-- Insert sample data into the groups table
INSERT INTO groups (group_id, group_name, description, creator_id)
VALUES ('group_1', 'Developers', 'Group for developers to discuss and share knowledge.',
        'a1e8a3d2-07a8-11ec-bf63-0242ac130002'),
       ('group_2', 'Designers', 'Group for designers to collaborate on projects.',
        'a1e8a3d2-07a8-11ec-bf63-0242ac130003'),
       ('group_3', 'Managers', 'Group for managers to discuss strategies and plans.',
        'a1e8a3d2-07a8-11ec-bf63-0242ac130004');

-- Insert sample data into the members table
INSERT INTO members (user_id, group_id)
VALUES ('a1e8a3d2-07a8-11ec-bf63-0242ac130005', 'group_1'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130006', 'group_1'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130007', 'group_2'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130008', 'group_2'),
       ('a1e8a3d2-07a8-11ec-bf63-0242ac130009', 'group_3');