package cl.minsal.semantikos.model;

import cl.minsal.semantikos.kernel.daos.TagDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías on 8/26/16.
 */
@Singleton
public class TagFactory {

    /** El logger de esta clase */
    private static final Logger logger = LoggerFactory.getLogger(TagFactory.class);

    @EJB
    private TagDAO tagDAO;

    /**
     * Este método es responsable de crear un objeto Tag a partir de una expresión JSON de la forma:
     * <code>[{"id":1,"name":"tag etiqueta ","letter_color":null,"background_color":null,"id_parent_tag":null},
     * {"id":2,"name":"tag update","letter_color":null,"background_color":null,"id_parent_tag":null}]</code>
     *
     * @param jsonExpression La expresión JSon a partir de la cual se crea el Tag.
     *
     * @return El objeto Tag.
     */
    public List<Tag> createTagsFromJSON(String jsonExpression) {

        /* Si JSON es nulo, se retorna una lista vacía */
        if (jsonExpression == null) {
            return new ArrayList<>();
        }

        /* Se parsea la expresión JSON */
        ObjectMapper mapper = new ObjectMapper();
        TagDTO[] tagsDTO;
        try {
            tagsDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), TagDTO[].class);
        } catch (IOException e) {
            String errorMsg = "Error when parsing a JSON a Relationships";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        /* Se retorna como una lista */
        return createTagFromDTOs(tagsDTO);
    }

    public List<Tag> createChildrenTagsFromJSON(Tag parent, String jsonExpression) {

        /* Si JSON es nulo, se retorna una lista vacía */
        if (jsonExpression == null) {
            return new ArrayList<>();
        }

        /* Se parsea la expresión JSON */
        ObjectMapper mapper = new ObjectMapper();
        TagDTO[] tagsDTO;
        try {
            tagsDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), TagDTO[].class);
        } catch (IOException e) {
            String errorMsg = "Error when parsing a JSON a Relationships";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        /* Se retorna como una lista */
        return createChildrenTagFromDTOs(parent,tagsDTO);
    }




    /**
     * Este método es responsable de transformar el arreglo de DTO's a objetos del modelo.
     *
     * @param tagsDTO El arreglo de DTO.
     *
     * @return Una lista de objetos de negocio Tag.
     */
    private List<Tag> createTagFromDTOs(TagDTO[] tagsDTO) {
        List<Tag> tags = new ArrayList<>();
        for (TagDTO tagDTO : tagsDTO) {
            tags.add(createTagFromDTO(tagDTO));
        }

        return tags;
    }

    private List<Tag> createChildrenTagFromDTOs(Tag parent,TagDTO[] tagsDTO) {
        List<Tag> tags = new ArrayList<>();
        for (TagDTO tagDTO : tagsDTO) {
            tags.add(createTagChildren(parent,tagDTO));
        }

        return tags;
    }



    /**
     * Este método es responsable de crear un objeto de negocio <code>Tag</code> a partir de un DTO.
     *
     * @param tagDTO El DTO a partir del cual se creará el objeto de negocio.
     *
     * @return El objeto de negocio Tag.
     */
    private Tag createTagFromDTO(TagDTO tagDTO) {

        Tag parentTag = tagDAO.findTagByID(tagDTO.getIdParentTag());
        List<Tag> children;
        Tag tag = new Tag(tagDTO.getId(), tagDTO.getName(), tagDTO.getBackgroundColor(), tagDTO.getLetterColor(), parentTag);
        children = tagDAO.getChildrenOf(tag);
        tag.setSon(children);

        return tag;
    }

    private Tag createTagChildren(Tag tagParent, TagDTO tagChild){

        Tag tag =new Tag(tagChild.getId(), tagChild.getName(), tagChild.getBackgroundColor(), tagChild.getLetterColor(),  tagParent);
        List<Tag> children = tagDAO.getChildrenOf(tag);
        tag.setSon(children);
        return tag;
    }

    /**
     * Este método es responsable de crear un objeto Tag a partir de una expresión JSON de la forma:
     * <code>{"id":1,"name":"tag etiqueta ","letter_color":null,"background_color":null,"id_parent_tag":null},
     * {"id":2,"name":"tag update","letter_color":null,"background_color":null,"id_parent_tag":null}</code>
     *
     * @param jsonExpression La expresión JSon a partir de la cual se crea el Tag.
     *
     * @return El objeto Tag.
     */
    public Tag createTagFromJSON(@NotNull String jsonExpression) {

        /* Se parsea la expresión JSON */
        ObjectMapper mapper = new ObjectMapper();
        TagDTO tagDTO;
        try {
            tagDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), TagDTO.class);
        } catch (IOException e) {
            String errorMsg = "Error when parsing a JSON a Relationships";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        /* Se retorna como una lista */
        return createTagFromDTO(tagDTO);
    }
}

class TagDTO {

    private long id;
    private String name;
    private String letterColor;
    private String backgroundColor;
    private long idParentTag;

    public TagDTO(){

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetterColor() {
        return letterColor;
    }

    public void setLetterColor(String letterColor) {
        this.letterColor = letterColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public long getIdParentTag() {
        return idParentTag;
    }

    public void setIdParentTag(long idParentTag) {
        this.idParentTag = idParentTag;
    }
}
