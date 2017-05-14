 CREATE TABLE IF NOT EXISTS beer_comment (
  id INT AUTO_INCREMENT NOT NULL,
  user_id INT NOT NULL,
  beer_id INT NOT NULL,
  comment TEXT NOT NULL,
  date DATE NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES user(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (beer_id) REFERENCES beer(id) ON UPDATE CASCADE ON DELETE CASCADE
);
