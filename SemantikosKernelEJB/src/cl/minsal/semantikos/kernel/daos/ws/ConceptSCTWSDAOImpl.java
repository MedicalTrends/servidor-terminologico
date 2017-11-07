package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.TagDAO;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.dtos.ConceptDTO;
import cl.minsal.semantikos.model.dtos.ConceptSCTDTO;
import cl.minsal.semantikos.model.dtos.DescriptionSCTDTO;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Created by des01c7 on 03-11-17.
 */
@Stateless
public class ConceptSCTWSDAOImpl implements ConceptSCTWSDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptSCTWSDAOImpl.class);

    public ConceptSCT createConceptSCTFromDTO(ConceptSCTDTO conceptDTO) throws SQLException {

        long id = conceptDTO.getId();
        Timestamp effectiveTime = conceptDTO.getEffectiveTime();
        boolean active = conceptDTO.isActive();
        long moduleID = conceptDTO.getModuleId();
        long definitionStatusID = conceptDTO.getDefinitionStatusId();

        ConceptSCT conceptSCT = new ConceptSCT(id, effectiveTime, active, moduleID, definitionStatusID);

        conceptSCT.setId(id);

        /* Se recuperan las descripciones del concepto */
        List<DescriptionSCTDTO> descriptions = conceptDTO.getDescriptions();
        conceptSCT.setDescriptions(createDescriptionsSCTFromDTO(descriptions));

        return conceptSCT;

    }

    private List<DescriptionSCT> createDescriptionsSCTFromDTO(List<DescriptionSCTDTO> descriptions) throws SQLException {

        List<DescriptionSCT> descriptionsSCT = new ArrayList<>();

        for (DescriptionSCTDTO description : descriptions) {
            long id = description.getId();
            Timestamp effectiveTime = description.getEffectiveTime();
            boolean active = description.isActive();
            long moduleID = description.getModuleId();
            long conceptID = description.getConceptId();
            String languageCode = description.getLanguageCode();
            long typeID = description.getTypeId();
            String term = description.getTerm();
            long caseSignificanceID = description.getCaseSignificanceId();
            long acceptabilityID = description.getAcceptabilityId();

            /**
             * Identifies whether the description is an FSN, Synonym or other description type.
             * This field is set to a child of 900000000000446008 | Description type | in the Metadata hierarchy.
             */
            try {
                DescriptionSCT descriptionSCT = new DescriptionSCT(id, DescriptionSCTType.valueOf(typeID), effectiveTime, active, moduleID, conceptID, languageCode, term, caseSignificanceID);

                descriptionSCT.setFavourite(DescriptionSCTType.valueOf(acceptabilityID).equals(DescriptionSCTType.PREFERRED));

                descriptionsSCT.add(descriptionSCT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return descriptionsSCT;
    }
}
