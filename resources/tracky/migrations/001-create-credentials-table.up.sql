CREATE TABLE credentials
(
  id SERIAL PRIMARY KEY,
  user_id varchar(64) NOT NULL UNIQUE,
  options JSON
);
