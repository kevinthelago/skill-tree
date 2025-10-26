-- Initialize admin user
INSERT INTO users (username, email, password, role, created_at, updated_at)
VALUES ('kevin', 'kevinthelago@gmail.com', '$2a$12$pDDNxZ.FEx2TUVNk3WfCy.I2yjS6QnefWv/l.T3V/oSYrVkWyBfFO', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
