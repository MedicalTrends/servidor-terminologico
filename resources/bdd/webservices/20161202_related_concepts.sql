-- Function: semantikos.get_related_concept(bigint)

-- DROP FUNCTION semantikos.get_related_concept(bigint);

CREATE OR REPLACE FUNCTION semantikos.get_related_concept(IN c_id bigint)
  RETURNS TABLE(id bigint, conceptid character varying, id_category bigint, is_modeled boolean, is_to_be_reviewed boolean, is_to_be_consultated boolean, is_fully_defined boolean, is_published boolean, observation character varying, id_tag_smtk bigint) AS
$BODY$
BEGIN
  RETURN QUERY  
  SELECT c.id, c.conceptid, c.id_category, c.is_modeled, c.is_to_be_reviewed ,c.is_to_be_consultated ,c.is_fully_defined, c.is_published, c.observation, c.id_tag_smtk
  FROM semantikos.smtk_target t
  INNER JOIN semantikos.smtk_relationship r ON t.id= r.id_target
  INNER JOIN semantikos.smtk_concept c on c.id= r.id_source_concept
  WHERE t.id_concept_stk=c_id;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION semantikos.get_related_concept(bigint)
  OWNER TO postgres;
