package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.TagDAO;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.tags.Tag;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class TagMapper {

    @EJB
    TagDAO tagDAO;

    public Tag createTagFromResultSet(ResultSet rs, Tag parent) {

        try {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String backgroundColor =  rs.getString("background_color");
            String letterColor = rs.getString("letter_color");

            if(parent == null) {
                parent = tagDAO.findTagByID(rs.getLong("id_parent_tag"));
            }
            List<Tag> children = new ArrayList<>();
            Tag tag = new Tag(id, name, backgroundColor, letterColor, parent);

            children = tagDAO.getChildrenOf(tag);
            tag.setSon(children);

            return tag;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
