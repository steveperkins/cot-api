-- 1. Create the database manually by copying and pasting this command, or use PGAdmin's wizard
CREATE DATABASE cot
  WITH ENCODING='UTF8'
       CONNECTION LIMIT=-1;
       
-- 2. Connect to the new 'cot' database and run the rest of this script
CREATE USER api WITH PASSWORD 'liuhIPUH988PO)&^hljkhlsfdWEF89@#lij)';

DROP TABLE IF EXISTS resource CASCADE;
DROP TABLE IF EXISTS repository CASCADE;
DROP TABLE IF EXISTS resource_editor CASCADE;
DROP TABLE IF EXISTS editor CASCADE;
DROP TABLE IF EXISTS resource_author CASCADE;
DROP TABLE IF EXISTS author CASCADE;
DROP TABLE IF EXISTS resource_tag CASCADE;
DROP TABLE IF EXISTS tag CASCADE;
DROP TABLE IF EXISTS sub_tag;
DROP TABLE IF EXISTS organization CASCADE;
DROP TABLE IF EXISTS reviewer CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS review_category CASCADE;;
DROP TABLE IF EXISTS chapter_review CASCADE;
DROP TABLE IF EXISTS chapter_review_score CASCADE;
DROP TABLE IF EXISTS license CASCADE;
DROP TABLE IF EXISTS resource_license CASCADE;


CREATE TABLE organization (
	id serial PRIMARY KEY, 
    name VARCHAR(255) NULL,
    url VARCHAR(255) NULL,
    logo_url VARCHAR(255) NULL,
    search_name VARCHAR(255) NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);
CREATE INDEX idx_organization_search_name ON organization(search_name);

CREATE TABLE repository (
    id serial PRIMARY KEY,
    organization_id int NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(255) NULL,
    search_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);
CREATE INDEX idx_repository_search_name ON repository(search_name);

CREATE TABLE resource (
    id serial PRIMARY KEY,
    repository_id int NOT NULL REFERENCES repository(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    ancillaries_url VARCHAR(255) NULL,
    external_review_url VARCHAR(255) NULL,
    search_title VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);
CREATE INDEX idx_resource_search_title ON resource(search_title);

CREATE TABLE author (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    search_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT null
);
CREATE INDEX idx_author_search_name ON author(search_name);

CREATE TABLE editor (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    search_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT null
);
CREATE INDEX idx_editor_search_name ON editor(search_name);

CREATE TABLE resource_author (
    resource_id int NOT NULL REFERENCES resource(id),
    author_id int NOT NULL REFERENCES author(id),
    PRIMARY KEY(resource_id, author_id)
);

CREATE TABLE resource_editor (
    resource_id int NOT NULL REFERENCES resource(id),
    editor_id int NOT NULL REFERENCES editor(id),
    PRIMARY KEY(resource_id, editor_id)
);


CREATE TABLE tag (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    tag_type VARCHAR(255) NOT NULL DEFAULT 'GENERAL',
    parent_tag_id int NULL,
    search_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_tag_parent_tag_id ON tag(parent_tag_id);
CREATE INDEX idx_tag_tag_type ON tag(tag_type);
CREATE INDEX idx_tag_search_name ON tag(search_name);

CREATE TABLE resource_tag (
    resource_id int NOT NULL REFERENCES resource(id) ON DELETE CASCADE,
    tag_id int NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(resource_id, tag_id)
);

CREATE TABLE sub_tag (
    tag_id int NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
    parent_tag_id int NULL REFERENCES tag(id) ON DELETE CASCADE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (parent_tag_id, tag_id)
);

CREATE TABLE reviewer (
	id serial PRIMARY KEY, 
    organization_id int NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NULL,
    biography TEXT NULL,
    search_name VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);
CREATE INDEX idx_reviewer_search_name ON reviewer(search_name);

CREATE TABLE review (
	id serial PRIMARY KEY, 
    resource_id int NOT NULL REFERENCES resource(id) ON DELETE CASCADE,
    reviewer_id int NOT NULL REFERENCES reviewer(id),
    review_type VARCHAR(255) NULL,
    score decimal NOT NULL,
    chart_url VARCHAR(255) NULL,
    comments TEXT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);

CREATE TABLE review_category (
	id serial PRIMARY KEY, 
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	review_type VARCHAR(255) NOT NULL,
	sort_order int NOT NULL,
	min_score decimal NOT NULL DEFAULT 1,
	max_score decimal NOT NULL DEFAULT 5,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);

CREATE TABLE chapter_review (
	id serial PRIMARY KEY, 
    review_id int NOT NULL REFERENCES review(id) ON DELETE CASCADE,
    chapter int NOT NULL,
    comments VARCHAR(255) NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);

CREATE TABLE chapter_review_score (
	id serial PRIMARY KEY, 
    chapter_review_id int NOT NULL REFERENCES chapter_review(id) ON DELETE CASCADE,
    review_category_id int NOT NULL REFERENCES review_category(id) ON DELETE CASCADE,
    score decimal NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT NULL
);

CREATE TABLE license (
    id VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE resource_license (
    resource_id int NOT NULL REFERENCES resource(id) ON DELETE CASCADE,
    license_id VARCHAR(255) NOT NULL REFERENCES license(id) ON DELETE CASCADE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(resource_id, license_id)
);

CREATE OR REPLACE FUNCTION set_dates_trigger_fn()
 RETURNS trigger AS $$
BEGIN
 IF TG_OP = 'INSERT' THEN
  NEW.created_date := CURRENT_TIMESTAMP;
  ELSE IF TG_OP = 'UPDATE' AND NEW.updated_date IS NULL THEN
   NEW.updated_date := CURRENT_TIMESTAMP;
  END IF;
 END IF;
RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION set_created_date_trigger_fn()
 RETURNS trigger AS $$
BEGIN
 IF NEW.created_date IS NULL THEN
  NEW.created_date := CURRENT_TIMESTAMP;
 END IF;
RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS set_dates_trigger ON author;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON author
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_dates_trigger ON repository;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON repository
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();

DROP TRIGGER IF EXISTS set_dates_trigger ON resource;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON resource
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();

DROP TRIGGER IF EXISTS set_dates_trigger ON author;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON author
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();

DROP TRIGGER IF EXISTS set_dates_trigger ON editor;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON editor
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_dates_trigger ON reviewer;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON reviewer
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_dates_trigger ON review;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON review
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_dates_trigger ON review_category;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON review_category
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_dates_trigger ON chapter_review;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON chapter_review
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_dates_trigger ON chapter_review_score;
CREATE TRIGGER set_dates_trigger
	BEFORE INSERT OR UPDATE ON chapter_review_score
	FOR EACH ROW
	EXECUTE PROCEDURE set_dates_trigger_fn();
	
DROP TRIGGER IF EXISTS set_created_date_trigger ON tag;
CREATE TRIGGER set_created_date_trigger
	BEFORE INSERT ON tag
	FOR EACH ROW
	EXECUTE PROCEDURE set_created_date_trigger_fn();
	
DROP TRIGGER IF EXISTS set_created_date_trigger ON resource_tag;
CREATE TRIGGER set_created_date_trigger
	BEFORE INSERT ON resource_tag
	FOR EACH ROW
	EXECUTE PROCEDURE set_created_date_trigger_fn();
	
DROP TRIGGER IF EXISTS set_created_date_trigger ON sub_tag;
CREATE TRIGGER set_created_date_trigger
	BEFORE INSERT ON sub_tag
	FOR EACH ROW
	EXECUTE PROCEDURE set_created_date_trigger_fn();

DROP TRIGGER IF EXISTS set_created_date_trigger ON license;
CREATE TRIGGER set_created_date_trigger
	BEFORE INSERT ON license
	FOR EACH ROW
	EXECUTE PROCEDURE set_created_date_trigger_fn();

DROP TRIGGER IF EXISTS set_created_date_trigger ON resource_license;
CREATE TRIGGER set_created_date_trigger
	BEFORE INSERT ON resource_license
	FOR EACH ROW
	EXECUTE PROCEDURE set_created_date_trigger_fn();
	
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO api;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO api;

INSERT INTO tag(name, search_name, tag_type) VALUES
('Featured', 'featured', 'GENERAL'),
('Applied Sciences', 'applied sciences', 'DISCIPLINE'),
('Business', 'business', 'DISCIPLINE'),
('Humanities', 'humanities', 'DISCIPLINE'),
('Natural Sciences', 'natural sciences', 'DISCIPLINE'),
('Social Sciences', 'social sciences', 'DISCIPLINE');

INSERT INTO tag(name, search_name, tag_type, parent_tag_id) VALUES
('Computer and Information Science', 'computer and information science', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Applied Sciences')),
('Engineering and Electronics', 'engineering and electronics', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Applied Sciences')),
('Health and Nursing', 'health and nursing', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Applied Sciences')),
('Accounting and Finance', 'accounting and finance', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Business')),
('General Business', 'general business', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Business')),
('Education', 'education', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('English and Composition', 'english and composition', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('Fine Arts', 'fine arts', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('History', 'history', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('Languages and Communication', 'languages and communication', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('Literature', 'literature', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('Philosophy', 'philosophy', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Humanities')),
('Biology and Genetics', 'biology and genetics', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Natural Sciences')),
('Chemistry', 'chemistry', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Natural Sciences')),
('General Sciences', 'general sciences', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Natural Sciences')),
('Mathematics', 'mathematics', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Natural Sciences')),
('Physics', 'physics', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Natural Sciences')),
('Statistics and Probability', 'statistics and probability', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Natural Sciences')),
('Anthropology and Archaeology', 'anthropology and archaeology', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Social Sciences')),
('Economics', 'economics', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Social Sciences')),
('Law', 'law', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Social Sciences')),
('Political Science', 'political science', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Social Sciences')),
('Psychology', 'psychology', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Social Sciences')),
('Sociology', 'sociology', 'DISCIPLINE', (SELECT id FROM tag WHERE name='Social Sciences'))
;

INSERT INTO review_category (name, description, review_type, sort_order)
	VALUES('Clarity and comprehensibility', '', 'CONTENT', 1),
	('Clarity and comprehensibility', '', 'CONTENT', 2),
	('Accuracy', '', 'CONTENT', 3),
	('Readability', '', 'CONTENT', 4),
	('Consistency', '', 'CONTENT', 5),
	('Appropriateness', '', 'CONTENT', 6),
	('Interface', '', 'CONTENT', 7),
	('Content usefulness', '', 'CONTENT', 8),
	('Modularity', '', 'CONTENT', 9),
	('Content errors', '', 'CONTENT', 10),
	('Reading level', '', 'CONTENT', 11),
	('Cultural relevance', '', 'CONTENT', 12)
;

	
INSERT INTO license(id, description) VALUES
	('BY', 'Open with Attribution'),
	('CC', 'Creative Commons'),
	('GFDL', 'GNU Free Documentation License'),
	('GGPL', 'GNU General Public License'),
	('NC', 'Non-Commercial'),
	('PD', 'Public Domain'),
	('SA', 'Share Alike'),
	('CC0', 'Public Domain'),
	('ND', 'No Derivatives')
;

INSERT INTO organization(name, url, logo_url, search_name, created_date) VALUES
('Florida Virtual Campus', 'https://www.floridashines.org/orange-grove', 'https://www.floridashines.org/floridaShines.org-theme/images/flvc.png', 'florida virtual campus', CURRENT_TIMESTAMP);

INSERT INTO repository(organization_id, name, url, search_name, created_date) VALUES
((SELECT id FROM organization WHERE search_name='florida virtual campus'), 'Orange Grove', 'https://florida.theorangegrove.org/og/oai', 'orange grove', CURRENT_TIMESTAMP);
