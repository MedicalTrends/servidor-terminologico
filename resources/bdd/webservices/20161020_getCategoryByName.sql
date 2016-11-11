-- Function: semantikos.get_category_by_name(character varying)

-- DROP FUNCTION semantikos.get_category_by_name(character varying);

CREATE OR REPLACE FUNCTION semantikos.get_category_by_name(IN p_name character varying)
  RETURNS TABLE(idcategory bigint, namecategory character varying, nameabbreviated character varying, restriction boolean, active boolean, tag bigint, color character varying) AS
$BODY$
begin
   RETURN QUERY select
	c.id,
	c.name,
	c.name_abreviated,
	c.restriction,
	c.valid,
	c.tag_semantikos,
	c.color
    from semantikos.smtk_category c
    where c.name ILIKE p_name;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION semantikos.get_category_by_name(IN p_name character varying)
  OWNER TO postgres;
