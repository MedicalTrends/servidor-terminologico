package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Andrés Farías
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConceptDTO.class, name = "ConceptDTO"),
        @JsonSubTypes.Type(value = ConceptSCTDTO.class, name = "ConceptSCTDTO"),
        @JsonSubTypes.Type(value = BasicTypeValueDTO.class, name = "BasicTypeValueDTO"),
        @JsonSubTypes.Type(value = HelperTableRowDTO.class, name = "HelperTableRowDTO"),
        @JsonSubTypes.Type(value = CrossmapSetMemberDTO.class, name = "CrossmapSetMemberDTO")
})
public interface TargetDTO {


}
