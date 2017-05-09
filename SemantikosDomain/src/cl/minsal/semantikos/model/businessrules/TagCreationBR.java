package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.Tag;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

/**
 * @author Andrés Farías on 9/2/16.
 */
public class TagCreationBR {

    public void applyRules(Tag tag) {
        br001DeepnessLevel(tag);
    }

    private void br001DeepnessLevel(Tag tag) {
        br001_3LevelDeep(tag);
    }

    /**
     * Esta BR valida que la etiqueta no tenga más de 3 niveles de anidación hacia los hijos.
     *
     * @param tag La etiqueta que se desea validar.
     */
    protected void br001_3LevelDeep(Tag tag) {

        Tag rootTag;
        if (tag.getParentTag() == null) {
            rootTag = tag;
        } else {
            Tag middleTag = tag;
            while (middleTag.getParentTag() != null) {
                middleTag = middleTag.getParentTag();
            }
            rootTag = middleTag;
        }

        if (rootTag.deepnessLevel() > 3) {
            throw new BusinessRuleException("BR-UNK", "Un tag no puede tener biz-biz nietos");
        }
    }
}
