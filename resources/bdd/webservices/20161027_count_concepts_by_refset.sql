
-- Function: semantikos.count_concepts_by_refset(bigint, boolean)

-- DROP FUNCTION semantikos.count_concepts_by_refset(bigint, boolean);

CREATE OR REPLACE FUNCTION semantikos.count_concepts_by_refset(
    IN refset bigint,
    IN modeled boolean)
  RETURNS TABLE(count bigint) AS
$BODY$
BEGIN

  RETURN QUERY select count(*)
  from (
  select c.id
  from semantikos.smtk_concept c
    inner join semantikos.smtk_refset_concept r
      on c.id = r.id_concept
  where r.id_refset=refset and (c.is_modeled = modeled) ) AS consulta;


END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION semantikos.count_concepts_by_refset(bigint, boolean)
  OWNER TO postgres;

