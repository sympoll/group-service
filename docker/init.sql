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
    user_id         UUID NOT NULL,
    group_id        VARCHAR(255) REFERENCES groups(group_id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (group_id, user_id)
);

CREATE TABLE roles
(
    role_name VARCHAR(63) PRIMARY KEY
);

CREATE TABLE user_roles
(
    user_id  UUID NOT NULL,
    group_id VARCHAR(255) NOT NULL,
    role_name  VARCHAR(63) REFERENCES roles (role_name) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (user_id, group_id, role_name),
    FOREIGN KEY (user_id, group_id) REFERENCES members (user_id, group_id) ON DELETE CASCADE
);

-- Insert sample data into the groups table
INSERT INTO groups (group_id, group_name, description, creator_id, time_created) VALUES
                                                                                     ('group1', 'Developers Group', 'Group for software developers.', 'c50b79b4-4bb2-4de6-9c3c-1d6cda63d672', '2024-08-14 10:00:00'),
                                                                                     ('group2', 'Designers Group', 'Group for graphic and UI/UX designers.', 'c06b7b23-3e5a-4b64-8df3-f2cb4b4b2c6a', '2024-08-14 10:05:00'),
                                                                                     ('group3', 'Product Managers Group', 'Group for product management professionals.', 'efbdcf0e-232f-47ba-93de-97284b71ad34', '2024-08-14 10:10:00'),
                                                                                     ('group4', 'Marketing Group', 'Group for marketing professionals.', 'ba8c56f4-6b9d-41a6-9e78-9982f6fa7d48', '2024-08-14 10:15:00'),
                                                                                     ('group5', 'Sales Group', 'Group for sales professionals.', 'd7d2179a-3dcb-4237-b8c4-39dc1f73df17', '2024-08-14 10:20:00');

-- Insert sample data into the members table
INSERT INTO members (user_id, group_id) VALUES
                                            ('b1f8e925-2129-473d-bc09-b3a2a331f839', 'group1'),
                                            ('bcdbbd68-8975-41d9-bd55-4a30a9f8e5f7', 'group1'),
                                            ('ca98fcb8-28b3-4708-becd-9114c9bba4b3', 'group1'),
                                            ('9edc3d7f-b5cb-4e8b-9a9b-3b4b4d91bf0b', 'group2'),
                                            ('3fb27438-8e71-4513-95d2-7c2c18b72b4a', 'group2'),
                                            ('68839b0c-7e14-4b65-a95c-873edc8f31c9', 'group2'),
                                            ('e63a9f56-3d7b-4a62-bf10-77b8f45b7ed6', 'group3'),
                                            ('c19b9b3b-d60c-46fc-97e1-3c47c00c5727', 'group3'),
                                            ('11c17138-839f-4189-8103-9d4031cf3765', 'group4'),
                                            ('3d0d216a-3bf6-4078-b0b1-3b72c4b0a4a7', 'group4'),
                                            ('823dbe18-c1ad-423e-a9f1-1c5b1931c095', 'group4'),
                                            ('9bc8a7db-7f14-41a3-bf7e-d0a0078cfbab', 'group5'),
                                            ('debfdbd5-bf68-46e2-bfc3-21d6fa4ddfe1', 'group5');

-- Insert sample data into the roles table
INSERT INTO roles (role_name) VALUES
                                  ('Group Moderator'),
                                  ('Group Admin');

-- Insert sample data into the user_roles table
INSERT INTO user_roles (user_id, group_id, role_name) VALUES
                                                          ('b1f8e925-2129-473d-bc09-b3a2a331f839', 'group1', Group Admin),
                                                          ('bcdbbd68-8975-41d9-bd55-4a30a9f8e5f7', 'group1', Group Moderator),
                                                          ('ca98fcb8-28b3-4708-becd-9114c9bba4b3', 'group1', Group Moderator);