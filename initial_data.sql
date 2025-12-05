-- Script SQL para datos iniciales de Reverso Social
-- Versión con servicios reales confirmados

-- ======================
-- USUARIOS
-- ======================
-- Contraseña para todos: password123
-- Hash BCrypt de "password123": $2a$10$xqTjXBvGQZH5.xHqN6rZ2eTjPTXLPZdUJU.pLwW2K.gKxK5M7kE5e

-- CUENTA ADMIN COMPARTIDA (Para María, Ana y Laura)
INSERT INTO users (id, full_name, email, password, phone, company_name, role, created_at, updated_at) VALUES
('a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 'Equipo Reverso Social', 
 'admin@reversosocial.com', 
 '$2a$10$xqTjXBvGQZH5.xHqN6rZ2eTjPTXLPZdUJU.pLwW2K.gKxK5M7kE5e', 
 '+34 900 000 000', 
 'Reverso Social', 
 'ADMIN', 
 NOW(), 
 NOW());

-- CUENTA EDITOR DE EJEMPLO (Para colaboradoras externas/becarias)
INSERT INTO users (id, full_name, email, password, phone, company_name, role, created_at, updated_at) VALUES
('b2c3d4e5-f6a7-4b8c-9d0e-1f2a3b4c5d6e', 
 'Colaboradora Externa', 
 'editor@reversosocial.com', 
 '$2a$10$xqTjXBvGQZH5.xHqN6rZ2eTjPTXLPZdUJU.pLwW2K.gKxK5M7kE5e', 
 '+34 900 000 001', 
 'Reverso Social', 
 'EDITOR', 
 NOW(), 
 NOW());

-- ======================
-- CATEGORÍAS DE SERVICIOS
-- ======================

INSERT INTO service_categories (id, name, description, icon_url, sort_order, active, created_at, updated_at) VALUES
('d4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a', 
 'Consultoría de Género', 
 'Planes de Igualdad, auditorías retributivas y asesoramiento especializado cumpliendo con el RD 901/2020 y 902/2020.', 
 '/icons/consulting.svg', 
 1, 
 true, 
 NOW(), 
 NOW()),

('e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', 
 'Protocolos de Actuación', 
 'Diseño e implementación de protocolos contra el acoso sexual, por razón de sexo y diversidad LGTBI+.', 
 '/icons/protocols.svg', 
 2, 
 true, 
 NOW(), 
 NOW()),

('f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', 
 'Formación Especializada', 
 'Capacitaciones adaptadas por niveles en igualdad de género, diversidad y prevención de violencias.', 
 '/icons/training.svg', 
 3, 
 true, 
 NOW(), 
 NOW()),

('a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', 
 'Proyectos Sociocomunitarios', 
 'Intervención social con perspectiva de género inclusiva y gestión de fondos del Pacto de Estado.', 
 '/icons/community.svg', 
 4, 
 true, 
 NOW(), 
 NOW());

-- ======================
-- SERVICIOS
-- ======================

-- CONSULTORÍA DE GÉNERO

INSERT INTO services (id, category_id, name, short_description, full_description, icon_url, sort_order, active, created_by_user_id, created_at, updated_at) VALUES
('b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 
 'd4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a', 
 'Planes de Igualdad', 
 'Cumplimiento del RD 901/2020 y 902/2020 con auditoría, diseño de medidas y registro en REGCON.', 
 'Servicio integral que incluye: auditoría previa de diagnóstico de situación, diseño de medidas adaptadas a la realidad de la empresa, negociación con la representación legal de las personas trabajadoras, redacción del Plan de Igualdad, registro en REGCON, formación para la Comisión Paritaria y seguimiento anual.', 
 '/icons/equality-plan.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'd4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a', 
 'Asesoramiento en Igualdad Retributiva', 
 'Elaboración de registros retributivos, auditoría salarial y corrección de brechas salariales.', 
 'Cumplimos con el marco normativo del RD 901/2020 y 902/2020. Incluye elaboración de registros retributivos, auditoría salarial con perspectiva de género, diseño de sistemas de clasificación profesional igualitarios y acompañamiento para corregir brechas salariales detectadas.', 
 '/icons/salary-audit.svg', 
 2, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('d0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 
 'd4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a', 
 'Consultoría en Igualdad', 
 'Investigación, estudios de género, organización de jornadas y asesoramiento en RSC con perspectiva de género.', 
 'Servicios de investigación en estudios de género e impacto social, organización de jornadas y seminarios, integración de la igualdad en políticas de RSC y asesoramiento integral en Responsabilidad Social Corporativa con Perspectiva de Género.', 
 '/icons/consulting-general.svg', 
 3, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW());

-- PROTOCOLOS DE ACTUACIÓN

INSERT INTO services (id, category_id, name, short_description, full_description, icon_url, sort_order, active, created_by_user_id, created_at, updated_at) VALUES
('e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 
 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', 
 'Protocolos contra el Acoso Sexual', 
 'Redacción, negociación y seguimiento de protocolos contra el acoso sexual y por razón de sexo.', 
 'Incluye redacción y negociación del protocolo, formación a la Comisión designada para prevención e intervención, y provisión de Servicio Técnico con técnica de igualdad especializada en violencias machistas y psicóloga especializada en violencia de género.', 
 '/icons/harassment-protocol.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 
 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', 
 'Protocolos y Planes LGTBI+', 
 'Diseño e implantación según Ley 4/2023 con diagnóstico, medidas específicas y formación especializada.', 
 'Obligatorio para empresas de más de 50 personas trabajadoras según Ley 4/2023. Incluye diagnóstico de situación, evaluación de políticas existentes, diseño de medidas específicas, protocolos de prevención contra la LGTBIfobia, registro y comunicación interna, y formación especializada para plantilla y dirección.', 
 '/icons/lgtbi-protocol.svg', 
 2, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW());

-- FORMACIÓN ESPECIALIZADA

INSERT INTO services (id, category_id, name, short_description, full_description, icon_url, sort_order, active, created_by_user_id, created_at, updated_at) VALUES
('a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', 
 'f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', 
 'Formación en Igualdad de Género', 
 'Capacitaciones adaptadas por niveles: lenguaje inclusivo, conciliación, liderazgo y diversidad.', 
 'Formaciones totalmente adaptadas a las necesidades de la entidad. Temas: lenguaje inclusivo, protocolos de prevención del acoso, conciliación y corresponsabilidad, formación básica en igualdad, igualdad en selección y promoción, liderazgo con perspectiva de género, resolución de conflictos, diversidad LGTBI e IA con perspectiva de género. Metodología interactiva con casos prácticos y materiales audiovisuales.', 
 '/icons/gender-training.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW());

-- PROYECTOS SOCIOCOMUNITARIOS

INSERT INTO services (id, category_id, name, short_description, full_description, icon_url, sort_order, active, created_by_user_id, created_at, updated_at) VALUES
('b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', 
 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', 
 'Proyectos con Perspectiva de Género', 
 'Intervención social con colectivos vulnerables, empoderamiento femenino e inserción sociolaboral.', 
 'Proyectos dirigidos a administración pública en soledad no deseada, procesos sociocomunitarios inclusivos, intervención con mujeres (empoderamiento e inserción sociolaboral), lucha contra el acoso escolar por diversidad, e IA con perspectiva de género e impacto en la juventud.', 
 '/icons/social-projects.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 
 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', 
 'Gestión de Fondos Pacto de Estado', 
 'Campañas de sensibilización, coeducación, protocolos municipales y formación especializada contra violencias machistas.', 
 'Gestión integral de fondos del Pacto de Estado contra la Violencia de Género. Incluye campañas publicitarias de sensibilización, actividades socioeducativas, coeducación y prevención, protocolos de coordinación municipales, talleres especializados y sistemas predictivos a través de IA en materia de violencia de género (I+D).', 
 '/icons/state-pact.svg', 
 2, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW());

-- ======================
-- CARACTERÍSTICAS DE SERVICIOS
-- ======================

-- Planes de Igualdad
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('d6e7f8a9-b0c1-4d2e-3f4a-5b6c7d8e9f0a', 
 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 
 'Auditoría de Diagnóstico', 
 'Análisis exhaustivo de la situación actual de igualdad en la organización.', 
 1, 
 NOW(), 
 NOW()),

('e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', 
 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 
 'Negociación con RLT', 
 'Apoyo en el proceso negociador con la representación legal de trabajadores/as.', 
 2, 
 NOW(), 
 NOW()),

('f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2c', 
 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 
 'Registro en REGCON', 
 'Gestión del registro oficial del Plan de Igualdad.', 
 3, 
 NOW(), 
 NOW()),

('a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', 
 'b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 
 'Seguimiento Anual', 
 'Evaluación y seguimiento continuo de la implementación del plan.', 
 4, 
 NOW(), 
 NOW());

-- Asesoramiento en Igualdad Retributiva
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('b0c1d2e3-f4a5-4b6c-7d8e-9f0a1b2c3d4e', 
 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'Auditoría Salarial', 
 'Análisis detallado de la estructura retributiva con perspectiva de género.', 
 1, 
 NOW(), 
 NOW()),

('c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', 
 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'Corrección de Brechas', 
 'Acompañamiento en la implementación de medidas correctoras.', 
 2, 
 NOW(), 
 NOW());

-- Protocolos contra el Acoso Sexual
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('d2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', 
 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 
 'Formación a la Comisión', 
 'Capacitación especializada para la Comisión de Prevención e Intervención.', 
 1, 
 NOW(), 
 NOW()),

('e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', 
 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 
 'Servicio Técnico Especializado', 
 'Provisión de técnica en igualdad y psicóloga especializada en violencia de género.', 
 2, 
 NOW(), 
 NOW());

-- Protocolos LGTBI+
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('f4a5b6c7-d8e9-4f0a-1b2c-3d4e5f6a7b8c', 
 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 
 'Diagnóstico de Diversidad', 
 'Informe completo sobre la situación de diversidad en la organización.', 
 1, 
 NOW(), 
 NOW()),

('a5b6c7d8-e9f0-4a1b-2c3d-4e5f6a7b8c9d', 
 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 
 'Protocolos Anti-LGTBIfobia', 
 'Diseño de protocolos de prevención y actuación frente a la discriminación.', 
 2, 
 NOW(), 
 NOW());

-- ======================
-- RECURSOS
-- ======================

INSERT INTO resources (id, title, description, type, file_url, preview_image_url, public, user_id, created_at, updated_at) VALUES
('d2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', 
 'Guía Básica de Igualdad de Género', 
 'Conceptos fundamentales sobre igualdad de género, discriminación y buenas prácticas en el ámbito laboral.', 
 'GUIDE', 
 '/files/guia-basica-igualdad.pdf', 
 '/images/guia-igualdad-preview.jpg', 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', 
 'Informe: Brecha Salarial en España 2024', 
 'Análisis actualizado de la brecha salarial de género en España por sectores y comunidades autónomas.', 
 'REPORT', 
 '/files/informe-brecha-salarial-2024.pdf', 
 '/images/brecha-salarial-preview.jpg', 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('f4a5b6c7-d8e9-4f0a-1b2c-3d4e5f6a7b8c', 
 'Protocolo Modelo contra el Acoso Sexual', 
 'Plantilla de protocolo adaptable para empresas de diferentes tamaños y sectores.', 
 'GUIDE', 
 '/files/protocolo-modelo-acoso.pdf', 
 '/images/protocolo-preview.jpg', 
 false, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW());

-- ======================
-- CONTACTOS DE EJEMPLO
-- ======================

INSERT INTO contacts (id, full_name, email, message, accepts_privacy, status, user_id, created_at, updated_at) VALUES
('a5b6c7d8-e9f0-4a1b-2c3d-4e5f6a7b8c9d', 
 'Carmen Rodríguez', 
 'carmen.rodriguez@empresa.com', 
 'Hola, estamos interesados en implementar un Plan de Igualdad en nuestra empresa de 150 trabajadores/as. ¿Podrían enviarnos información sobre los servicios y presupuesto?', 
 true, 
 'PENDING', 
 NULL, 
 NOW(), 
 NOW()),

('b6c7d8e9-f0a1-4b2c-3d4e-5f6a7b8c9d0e', 
 'Juan López', 
 'juan.lopez@ayuntamiento.es', 
 'Desde el ayuntamiento queremos organizar una formación sobre prevención de violencias machistas para el equipo de servicios sociales. ¿Tienen disponibilidad para el próximo trimestre?', 
 true, 
 'IN_PROGRESS', 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW());

-- ======================
-- BLOG POSTS
-- ======================

INSERT INTO blog_posts 
(id, title, subtitle, slug, content, category, cover_image_url, status, created_at, updated_at, published_at, author_id)
VALUES
(
  '11111111-aaaa-bbbb-cccc-000000000001',
  'El Impacto de la Igualdad en 2024',
  'Cómo las nuevas políticas transforman el entorno laboral y social',
  'impacto-igualdad-2024',
  'En 2024 hemos visto avances significativos en políticas de igualdad. Este artículo explora los hitos clave y su impacto en comunidades locales.',
  'Actualidad',
  '/images/blog/igualdad-2024.jpg',
  'PUBLISHED',
  NOW(), NOW(), NOW(),
  'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d'
),

(
  '11111111-aaaa-bbbb-cccc-000000000002',
  'Rompiendo la Brecha Salarial',
  'Un análisis profundo sobre la equidad financiera real',
  'rompiendo-brecha-salarial',
  'La brecha salarial es uno de los desafíos estructurales más persistentes. Revisamos los pasos clave para avanzar hacia una igualdad efectiva.',
  'Economía',
  '/images/blog/brecha-salarial.jpg',
  'PUBLISHED',
  NOW(), NOW(), NOW(),
  'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d'
),

(
  '11111111-aaaa-bbbb-cccc-000000000003',
  'Nuestra Visión Feminista',
  'Los valores que mueven el trabajo de Reverso Social',
  'vision-feminista-reverso',
  'En este artículo abordamos los principios que guían nuestro enfoque de trabajo: transversalidad, inclusión y compromiso político.',
  'Institucional',
  '/images/blog/vision-feminista.jpg',
  'PUBLISHED',
  NOW(), NOW(), NOW(),
  'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d'
),

(
  '11111111-aaaa-bbbb-cccc-000000000004',
  'Proyectos de Inclusión Social',
  'Un repaso por nuestras iniciativas del último trimestre',
  'proyectos-inclusion-social',
  'Hemos impulsado nuevas acciones orientadas a reducir desigualdades, fortalecer redes comunitarias y promover liderazgos femeninos.',
  'Proyectos',
  '/images/blog/inclusion-social.jpg',
  'PUBLISHED',
  NOW(), NOW(), NOW(),
  'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d'
),

(
  '11111111-aaaa-bbbb-cccc-000000000005',
  'Economía Feminista: Retos y Horizontes',
  'Una mirada crítica a los modelos económicos actuales',
  'economia-feminista-retos',
  'La economía feminista ofrece herramientas para repensar la producción, los cuidados y la redistribución. Aquí exploramos su impacto práctico.',
  'Economía',
  '/images/blog/economia-feminista.jpg',
  'PUBLISHED',
  NOW(), NOW(), NOW(),
  'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d'
);

-- ======================
-- FIN DEL SCRIPT
-- ======================