package cl.minsal.semantikos.ws;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Andrés Farías on 16-01-17.
 */
public class ParameterValidatorTest {

    /** La instancia a probar */
    private ParameterValidator parameterValidator = new ParameterValidator();

    @Test(expected = IllegalArgumentException.class)
    public void required001() throws Exception {
        parameterValidator.required(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void required002() throws Exception {
        parameterValidator.required("");
    }

    @Test()
    public void required003() throws Exception {
        parameterValidator.required("Some value");
    }

    @Test
    public void atLeastOneRequired001() throws Exception {
        parameterValidator.atLeastOneRequired("Some value", "");
    }

    @Test
    public void atLeastOneRequired002() throws Exception {
        parameterValidator.atLeastOneRequired(null, "Something");
    }

    @Test(expected = IllegalArgumentException.class)
    public void atLeastOneRequired003() throws Exception {
        parameterValidator.atLeastOneRequired(null, "");
    }

    @Test
    public void isProvided() throws Exception {
        boolean provided = parameterValidator.isProvided(null, "");

        assertFalse(provided);
    }

}