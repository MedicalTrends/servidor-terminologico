

-- Function: semantikos.find_refsets_by_concept()

-- DROP FUNCTION semantikos.find_refsets_by_concept();

CREATE OR REPLACE FUNCTION semantikos.find_refsets_by_concept(IN concept bigint)
  RETURNS TABLE(id bigint, name character varying, institution bigint, creation_date timestamp without time zone, validity_until timestamp without time zone) AS
$BODY$
BEGIN
  RETURN QUERY SELECT r.id, r.name_refset, r.id_institution, r.creation_date, r.validity_until
               FROM semantikos.smtk_refset as r
                 inner join semantikos.smtk_refset_concept rc
                   on r.id = rc.id_refset
               WHERE rc.id_concept = concept;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100
ROWS 1000;
ALTER FUNCTION semantikos.find_refsets_by_concept(bigint)
OWNER TO postgres;


