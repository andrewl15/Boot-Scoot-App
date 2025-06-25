-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER linedance_owner
WITH PASSWORD 'linedance';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO linedance_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO linedance_owner;

CREATE USER linedance_appuser
WITH PASSWORD 'linedance';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO linedance_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO linedance_appuser;
