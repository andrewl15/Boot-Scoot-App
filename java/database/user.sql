-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER bootscoot_owner
WITH PASSWORD 'bootscoot';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO bootscoot_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO bootscoot_owner;

CREATE USER bootscoot_appuser
WITH PASSWORD 'bootscoot';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO bootscoot_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO bootscoot_appuser;
