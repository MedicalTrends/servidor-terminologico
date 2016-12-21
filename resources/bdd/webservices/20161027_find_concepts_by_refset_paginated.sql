
-- Function: semantikos.find_concepts_by_refset_paginated(bigint, integer, integer, boolean)

-- DROP FUNCTION semantikos.find_concepts_by_refset_paginated(bigint, integer, integer, boolean);

CREATE OR REPLACE FUNCTION semantikos.find_concepts_by_refset_paginated(
  IN refset bigint,
  IN page integer,
  IN page_size integer,
  IN modeled boolean)
  RETURNS TABLE(id bigint, conceptid character varying, id_category bigint, is_modeled boolean, is_to_be_reviewed boolean, is_to_be_consultated boolean, is_fully_defined boolean, is_published boolean, id_tag_smtk bigint, observation character varying) AS
$BODY$
BEGIN
  RETURN QUERY select
                 c.id,
                 c.conceptid,
                 c.id_category,
                 c.is_modeled,
                 c.is_to_be_reviewed,
                 c.is_to_be_consultated,
                 c.is_fully_defined,
                 c.is_published,
                 c.id_tag_smtk,
                 c.observation
               from semantikos.smtk_concept c
                 inner join semantikos.smtk_refset_concept r
                   on c.id = r.id_concept
               where r.id_refset=refset and (c.is_modeled = modeled)
               order by c.id
               limit page_size offset (page);

END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100
ROWS 1000;
ALTER FUNCTION semantikos.find_concepts_by_refset_paginated(bigint, integer, integer, boolean)
OWNER TO postgres;

