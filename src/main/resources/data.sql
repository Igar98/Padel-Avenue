-- Limpiar tablas existentes (en orden correcto por foreign keys)
DELETE FROM match;
DELETE FROM player;

-- Insertar jugadores (usando UUIDs fijos para facilitar testing)
INSERT INTO player (id, name, phone, is_admin, points, is_active) VALUES 
('123e4567-e89b-12d3-a456-426614174000', 'Juan Admin', '+34600000001', true, 0, true),
('123e4567-e89b-12d3-a456-426614174001', 'Pedro García', '+34600000002', false, 0, true),
('123e4567-e89b-12d3-a456-426614174002', 'Ana Martínez', '+34600000003', false, 0, true),
('123e4567-e89b-12d3-a456-426614174003', 'Luis Rodríguez', '+34600000004', false, 0, true),
('123e4567-e89b-12d3-a456-426614174004', 'María López', '+34600000005', false, 0, true),
('123e4567-e89b-12d3-a456-426614174005', 'Carlos Sánchez', '+34600000006', false, 0, true),
('123e4567-e89b-12d3-a456-426614174006', 'Sofia Ruiz', '+34600000007', false, 0, true),
('123e4567-e89b-12d3-a456-426614174007', 'Roberto Torres', '+34600000008', true, 0, true),
('123e4567-e89b-12d3-a456-426614174008', 'Elena Castro', '+34600000009', false, 0, true),
('123e4567-e89b-12d3-a456-426614174009', 'David Moreno', '+34600000010', false, 0, true);

-- Insertar algunos partidos históricos de ejemplo
INSERT INTO match (id, played_at, team1player1_id, team1player2_id, team2player1_id, team2player2_id, team1won) VALUES
('223e4567-e89b-12d3-a456-426614174000', '2024-03-20 10:00:00', 
 '123e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174001',
 '123e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174003',
 true);

-- Actualizar puntos basados en partidos históricos
UPDATE player 
SET points = points + 1 
WHERE id IN ('123e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174001');