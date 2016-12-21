
-- Function: semantikos.find_refsets_by_name()

-- DROP FUNCTION semantikos.find_refsets_by_name();

CREATE OR REPLACE FUNCTION semantikos.find_refsets_by_name(IN pattern CHARACTER VARYING)
  RETURNS TABLE(id bigint, name character varying, institution bigint, creation_date timestamp without time zone, validity_until timestamp without time zone) AS
$BODY$
BEGIN
  RETURN QUERY SELECT r.id, r.name_refset, r.id_institution, r.creation_date, r.validity_until
               FROM semantikos.smtk_refset as r
               WHERE r.name_refset ILIKE pattern;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100
ROWS 1000;
ALTER FUNCTION semantikos.find_refsets_by_name(character varying)
OWNER TO postgres;

