-- Function: semantikos.find_concept_by_pattern_and_categories_and_refsets(integer[], integer[], text[], integer, integer, boolean)

-- DROP FUNCTION semantikos.find_concept_by_pattern_and_categories_and_refsets(integer[], integer[], text[], integer, integer, boolean);

CREATE OR REPLACE FUNCTION semantikos.find_concept_by_pattern_and_categories_and_refsets(
    IN categories integer[],
    IN refsets integer[],
    IN patterns text[],
    IN page integer,
    IN page_size integer,
    IN modeled boolean)
  RETURNS TABLE(id bigint, conceptid character varying, id_category bigint, is_modeled boolean, is_to_be_reviewed boolean, is_to_be_consultated boolean, is_fully_defined boolean, is_published boolean, id_tag_smtk bigint, observation character varying, is_inherited boolean) AS
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
	c.observation,
  c.is_inherited
from semantikos.smtk_category ca
  inner join semantikos.smtk_concept c
    on ca.id = c.id_category and c.id_category = any (categories::int[])
  inner join semantikos.smtk_refset_concept rc
    on rc.id_concept=c.id and rc.id_refset = any (refsets::int[])
  inner join semantikos.smtk_description d
    on c.id = d.id_concept and unaccent(d.term) ilike all (string_to_array('%'||array_to_string(patterns,'%,%')||'%',','))
  where c.is_modeled = modeled
  group by c.id
  order by c.id
  limit page_size offset (page);

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION semantikos.find_concept_by_pattern_and_categories_and_refsets(integer[], integer[], text[], integer, integer, boolean)
  OWNER TO postgres;
