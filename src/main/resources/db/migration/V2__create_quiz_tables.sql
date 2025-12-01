-- Flyway migration to add quiz tables, user_points and leaderboard_snapshot
CREATE TABLE IF NOT EXISTS quiz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  quiz_date DATE NOT NULL UNIQUE,
  title VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quiz_question (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  quiz_id BIGINT NOT NULL,
  question_text TEXT NOT NULL,
  options TEXT,
  correct_index INT,
  points INT DEFAULT 10,
  CONSTRAINT fk_quiz_question_quiz FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_attempt (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  quiz_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  answers TEXT,
  score INT NOT NULL DEFAULT 0,
  attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_quiz_attempt_quiz FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,
  UNIQUE KEY uq_quiz_user (quiz_id, user_id)
);

CREATE TABLE IF NOT EXISTS user_points (
  user_id BIGINT PRIMARY KEY,
  total_points BIGINT NOT NULL DEFAULT 0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS leaderboard_snapshot (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  snapshot_date DATE NOT NULL,
  user_id BIGINT NOT NULL,
  rank INT NOT NULL,
  tier VARCHAR(64) NOT NULL,
  UNIQUE KEY uq_snapshot_user_date (snapshot_date, user_id)
);

CREATE TABLE IF NOT EXISTS leaderboard_tier (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  capacity INT NOT NULL,
  sort_index INT NOT NULL
);

-- Insert sample tiers
INSERT INTO leaderboard_tier (name, capacity, sort_index) VALUES
('Radiant', 10, 1),
('Immortal', 25, 2),
('Guardian', 50, 3),
('Participant', 999999, 99);
