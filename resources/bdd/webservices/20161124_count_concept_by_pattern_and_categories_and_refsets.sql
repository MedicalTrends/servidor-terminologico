-- Function: semantikos.count_concept_by_pattern_and_categories_and_refsets(integer[], integer[], text[], boolean)

-- DROP FUNCTION semantikos.count_concept_by_pattern_and_categories_and_refsets(integer[], integer[], text[], boolean);

CREATE OR REPLACE FUNCTION semantikos.count_concept_by_pattern_and_categories_and_refsets(
    IN categories integer[],
    IN refsets integer[],
    IN patterns text[],
    IN modeled boolean)
  RETURNS TABLE(count bigint) AS
$BODY$
BEGIN
  RETURN QUERY select count(*)
    from (select c.id
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
      ) as consulta;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION semantikos.count_concept_by_pattern_and_categories_and_refsets(integer[], integer[], text[], boolean)
  OWNER TO postgres;
