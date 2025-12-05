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
 'Cumplimiento del RD 901/2020 y 902/2020 con auditoría, diseño de medidas, negociación, registro en REGCON y seguimiento anual.', 
 'Servicio integral que incluye: auditoría previa de diagnóstico de situación, diseño de medidas adaptadas a la realidad de la empresa, negociación con la representación legal de las personas trabajadoras y apoyo en los procesos negociadores, redacción del Plan de Igualdad, registro en REGCON, formación para la Comisión Paritaria de Negociación y a la plantilla, seguimiento y evaluación anual.', 
 '/icons/equality-plan.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'd4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a', 
 'Igualdad Retributiva', 
 'Auditoría retributiva según RD 902/2020: registros retributivos, auditoría salarial, clasificación profesional igualitaria y corrección de brechas.', 
 'Realizamos informes de Auditoría Retributiva cumpliendo con el marco normativo establecido por el Real Decreto 902/2020. Incluye elaboración de registros retributivos, auditoría salarial con perspectiva de género, diseño de sistemas de clasificación profesional igualitarios y acompañamiento para corregir brechas salariales detectadas.', 
 '/icons/salary-audit.svg', 
 2, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('d0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 
 'd4e5f6a7-b8c9-4d0e-1f2a-3b4c5d6e7f8a', 
 'Consultoría en Igualdad', 
 'Investigación en estudios de género, sistemas predictivos con IA, informes de impacto de género, organización de jornadas y asesoramiento en RSC.', 
 'Servicios de investigación: estudios de género e impacto social con perspectiva de género. Sistemas predictivos a través de IA en materia de Violencia de género (I+D). Informes con impacto de género e informes y estudios sobre brechas de género. Organización de jornadas, seminarios, encuentros, desayunos y cualquier otra actividad enmarcada. Integración de la igualdad en políticas de RSC y asesoramiento en RSC con Perspectiva de Género.', 
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
 'Protocolos contra el Acoso Sexual y por Razón de Sexo', 
 'Redacción, negociación y seguimiento de protocolos. Formación a la Comisión y provisión de Servicio Técnico especializado.', 
 'Realizamos Protocolos contra el Acoso Sexual y por razón de sexo cumpliendo con la normativa actualmente vigente. Incluye redacción, negociación y seguimiento del protocolo, formación a la Comisión designada por la empresa de Prevención e Intervención en caso de acoso sexual y/o por razón de sexo, y provisión en caso necesario de Servicio Técnico de Técnica de Igualdad especializada en Violencias Machistas y Sexuales, así como de Psicóloga especializada en Violencia de Género.', 
 '/icons/harassment-protocol.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 
 'e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', 
 'Protocolos y Planes LGTBI+', 
 'Diseño según Ley 4/2023 con diagnóstico, evaluación de políticas, medidas específicas, protocolos anti-LGTBIfobia y formación especializada.', 
 'Realizamos Protocolos y Planes LGTBI+ de acuerdo con la Ley 4/2023 y en línea con los principios de inclusión y no discriminación. Obligatorio para empresas de más de 50 personas trabajadoras. Incluye: diagnóstico de situación (informe de diversidad), evaluación de políticas y protocolos existentes, diseño de medidas y objetivos específicos, protocolos de prevención y actuación frente a la LGTBIfobia, registro y comunicación interna del protocolo, y formación especializada para plantilla y dirección.', 
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
 'Capacitaciones totalmente adaptadas por niveles: igualdad, protocolos, conciliación, liderazgo, diversidad LGTBI, IA con perspectiva de género.', 
 'Realizamos todo tipo de formación relacionada con igualdad ajustándonos al 100% a las necesidades de su entidad. Temas incluyen: formación en materia de igualdad y normativa actualizada, protocolo de prevención y actuación frente al acoso sexual y/o por razón de sexo, conciliación y corresponsabilidad, lenguaje y comunicación inclusiva, igualdad en selección y promoción, liderazgo eficaz de equipos con perspectiva de género, resolución de conflictos y toma de decisiones, diversidad sexual y de género LGTBI en el entorno laboral, IA con perspectiva de género en entornos laborales, y cualquier otra formación necesaria. Capacitaciones adaptadas por niveles (dirección/cargos políticos/cuadros sindicales, mandos superiores, mandos intermedios, personal técnico, resto de plantilla). Diseño personalizado según tamaño, sector y cultura empresarial. Formaciones presenciales, online, virtuales y/o híbridas, utilizando la pedagogía activa como motor educativo.', 
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
 'Proyectos Sociales con Perspectiva de Género', 
 'Intervención social con colectivos vulnerables: soledad no deseada, empoderamiento femenino, inserción sociolaboral, lucha contra el acoso escolar, salud e IA con perspectiva de género.', 
 'Desarrollamos, gestionamos y evaluamos proyectos sociales y comunitarios dirigidos a administración pública y colaboraciones institucionales con perspectiva de género inclusiva. Incluye: proyectos en el marco de la soledad no deseada con distintos colectivos (población mayor, otros colectivos vulnerables), procesos sociocomunitarios inclusivos de colectivos en situación de vulnerabilidad social, intervención con mujeres (empoderamiento femenino e inserción sociolaboral), lucha contra el acoso escolar por razón de diversidad afectivo-sexual, intercultural, funcional o cualesquiera (combatir el odio a la juventud diversa desde la prevención), salud con perspectiva de género, e IA con perspectiva de género e impacto en la juventud.', 
 '/icons/social-projects.svg', 
 1, 
 true, 
 'a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 
 NOW(), 
 NOW()),

('c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 
 'a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', 
 'Gestión de Fondos Pacto de Estado', 
 'Gestión integral de fondos contra la Violencia de Género: campañas de sensibilización, coeducación, protocolos municipales, talleres especializados y sistemas predictivos con IA.', 
 'Ayudamos a su entidad a gestionar y desarrollar proyectos dentro de los Fondos del Pacto de Estado contra la Violencia de Género de nuestro país. Incluye: campañas publicitarias de sensibilización y concienciación, coeducación (actividades socioeducativas de sensibilización y concienciación de las Violencias Machistas, el acoso sexual, por razón de sexo, intercultural y/o LGTBI), protocolos de coordinación y/o prevención y actuación municipales o mancomunados contra las Violencias Machistas, talleres y formación especializada tanto para colectivos como para profesionales, y sistemas predictivos a través de IA en materia de violencia de género (I+D).', 
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

-- Igualdad Retributiva
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('b0c1d2e3-f4a5-4b6c-7d8e-9f0a1b2c3d4e', 
 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'Registros Retributivos', 
 'Elaboración completa de registros retributivos según normativa.', 
 1, 
 NOW(), 
 NOW()),

('c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', 
 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'Auditoría Salarial', 
 'Análisis detallado de la estructura retributiva con perspectiva de género.', 
 2, 
 NOW(), 
 NOW()),

('d1e2f3a4-b5c6-4d7e-8f9a-0b1c2d3e4f5a', 
 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'Clasificación Profesional', 
 'Diseño de sistemas de clasificación profesional igualitarios.', 
 3, 
 NOW(), 
 NOW()),

('e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5c', 
 'c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 
 'Corrección de Brechas', 
 'Acompañamiento en la implementación de medidas correctoras.', 
 4, 
 NOW(), 
 NOW());

-- Consultoría en Igualdad
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('f1a2b3c4-d5e6-4f7a-8b9c-0d1e2f3a4b5c', 
 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 
 'Investigación y Estudios', 
 'Estudios de género e impacto social con perspectiva de género.', 
 1, 
 NOW(), 
 NOW()),

('a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5e', 
 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 
 'Sistemas Predictivos con IA', 
 'Desarrollo de sistemas predictivos en materia de Violencia de Género (I+D).', 
 2, 
 NOW(), 
 NOW()),

('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5e', 
 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 
 'Organización de Eventos', 
 'Jornadas, seminarios, encuentros y actividades especializadas.', 
 3, 
 NOW(), 
 NOW()),

('c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5a', 
 'd0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 
 'RSC con Perspectiva de Género', 
 'Integración de la igualdad en políticas de RSC y asesoramiento especializado.', 
 4, 
 NOW(), 
 NOW());

-- Protocolos contra el Acoso Sexual
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('d2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', 
 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 
 'Redacción y Negociación', 
 'Diseño completo del protocolo y apoyo en negociación con la empresa.', 
 1, 
 NOW(), 
 NOW()),

('e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', 
 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 
 'Formación a la Comisión', 
 'Capacitación especializada para la Comisión de Prevención e Intervención.', 
 2, 
 NOW(), 
 NOW()),

('f3a4b5c6-d7e8-4f9a-0b1c-2d3e4f5a6b7c', 
 'e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 
 'Servicio Técnico Especializado', 
 'Provisión de técnica en igualdad y psicóloga especializada en violencia de género.', 
 3, 
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
 'Evaluación de Políticas', 
 'Análisis de políticas y protocolos existentes relacionados con diversidad.', 
 2, 
 NOW(), 
 NOW()),

('b5c6d7e8-f9a0-4b1c-2d3e-4f5a6b7c8d9e', 
 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 
 'Protocolos Anti-LGTBIfobia', 
 'Diseño de protocolos de prevención y actuación frente a la discriminación.', 
 3, 
 NOW(), 
 NOW()),

('c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9a', 
 'f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 
 'Formación Especializada', 
 'Capacitación para plantilla y dirección en diversidad LGTBI+.', 
 4, 
 NOW(), 
 NOW());

-- Formación en Igualdad
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('d6e7f8a9-b0c1-4d2e-3f4a-5b6c7d8e9f0b', 
 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', 
 'Adaptación por Niveles', 
 'Formaciones personalizadas según dirección, mandos o resto de plantilla.', 
 1, 
 NOW(), 
 NOW()),

('e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1c', 
 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', 
 'Metodología Activa', 
 'Pedagogía activa con casos prácticos y materiales audiovisuales.', 
 2, 
 NOW(), 
 NOW()),

('f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2d', 
 'a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', 
 'Modalidades Flexibles', 
 'Formaciones presenciales, online, virtuales y/o híbridas.', 
 3, 
 NOW(), 
 NOW());

-- Proyectos Sociales
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3e', 
 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', 
 'Soledad No Deseada', 
 'Proyectos con población mayor y otros colectivos vulnerables.', 
 1, 
 NOW(), 
 NOW()),

('b9c0d1e2-f3a4-4b5c-6d7e-8f9a0b1c2d3f', 
 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', 
 'Empoderamiento Femenino', 
 'Intervención con mujeres: empoderamiento e inserción sociolaboral.', 
 2, 
 NOW(), 
 NOW()),

('c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3a', 
 'b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', 
 'Prevención de Acoso Escolar', 
 'Combatir el odio a la juventud diversa desde la prevención.', 
 3, 
 NOW(), 
 NOW());

-- Gestión Fondos Pacto de Estado
INSERT INTO service_features (id, service_id, title, description, sort_order, created_at, updated_at) VALUES
('d0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4b', 
 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 
 'Campañas de Sensibilización', 
 'Campañas publicitarias de sensibilización y concienciación.', 
 1, 
 NOW(), 
 NOW()),

('e0f1a2b3-c4d5-4e6f-7a8b-9c0d1e2f3a4c', 
 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 
 'Coeducación', 
 'Actividades socioeducativas contra las Violencias Machistas y acoso.', 
 2, 
 NOW(), 
 NOW()),

('f0a1b2c3-d4e5-4f6a-7b8c-9d0e1f2a3b4d', 
 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 
 'Protocolos Municipales', 
 'Protocolos de coordinación y/o prevención y actuación municipales.', 
 3, 
 NOW(), 
 NOW()),

('a0b1c2d3-e4f5-4a6b-7c8d-9e0f1a2b3c4e', 
 'c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 
 'Formación Especializada', 
 'Talleres para colectivos y profesionales especializados.', 
 4, 
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
-- FIN DEL SCRIPT
-- ======================