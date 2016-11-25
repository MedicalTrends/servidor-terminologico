-- Function: semantikos.get_concept_by_conceptid(character varying)

-- DROP FUNCTION semantikos.get_concept_by_conceptid(character varying);

CREATE OR REPLACE FUNCTION semantikos.get_concept_by_conceptid(IN c_id character varying)
  RETURNS TABLE(id bigint, conceptid character varying, id_category bigint, is_modeled boolean, is_to_be_reviewed boolean, is_to_be_consultated boolean, is_fully_defined boolean, is_published boolean, id_tag_smtk bigint, observation character varying) AS
$BODY$
BEGIN
  RETURN QUERY select
        c.id,
        c.conceptid,
        c.id_category,
        c.is_modeled,
        c.is_to_be_reviewed ,
        c.is_to_be_consultated ,
        c.is_fully_defined ,
        c.is_published,
        c.id_tag_smtk,
        c.observation
  from semantikos.smtk_concept c
  where c.conceptid= c_id
  group by c.id
  order by c.id;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION semantikos.get_concept_by_conceptid(character varying)
  OWNER TO postgres;
