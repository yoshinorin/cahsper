/* This file version (V1.3.0) does not relate to the application version. */
ALTER TABLE comments ADD uuid CHAR(36) CHARACTER SET ascii NOT NULL AFTER id;
ALTER TABLE comments CHANGE COLUMN id id INT(11) UNSIGNED NOT NULL;
ALTER TABLE comments DROP PRIMARY KEY;

UPDATE comments
INNER JOIN (
  SELECT id, uuid, UUID() AS generated_uuid FROM comments
) AS X
ON X.id = comments.id
SET comments.uuid = X.generated_uuid;

ALTER TABLE comments DROP COLUMN id;
ALTER TABLE comments CHANGE COLUMN uuid id CHAR(36) CHARACTER SET ascii NOT NULL;
ALTER TABLE comments ADD PRIMARY KEY (id);
