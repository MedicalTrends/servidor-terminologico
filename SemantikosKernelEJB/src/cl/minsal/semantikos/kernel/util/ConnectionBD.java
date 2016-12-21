package cl.minsal.semantikos.kernel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Francisco Mendez on 29-06-16.
 */
public class ConnectionBD {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionBD.class);

    private javax.naming.InitialContext ctx;
    private javax.sql.DataSource ds;

    public ConnectionBD() {
        try {
            ctx = new javax.naming.InitialContext();
            ds = (javax.sql.DataSource) ctx.lookup("java:jboss/PostgresDS");
        } catch (NamingException e) {
            logger.error("Error al buscar Datasource en Jboss", e);
        }
    }

    public Connection getConnection() {
        Connection connection;
        try {
            ctx = new javax.naming.InitialContext();
            ds = (javax.sql.DataSource) ctx.lookup("java:jboss/PostgresDS");
            connection = ds.getConnection();
        } catch (NamingException e) {
            String errorMessage = "Error al buscar Datasource en Jboss";
            logger.error(errorMessage, e);
            throw new EJBException(errorMessage, e);
        } catch (SQLException e) {
            String errorMessage = "Error al conectarse a BD";
            logger.error("Error al conectarse a BD", e);
            throw new EJBException(errorMessage, e);
        }

        return connection;
    }
}
