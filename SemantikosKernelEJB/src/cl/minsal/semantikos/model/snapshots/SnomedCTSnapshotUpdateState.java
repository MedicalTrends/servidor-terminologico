package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.users.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Diego Soto
 */
public class SnomedCTSnapshotUpdateState extends PersistentEntity {

    private boolean conceptsProcessed;

    private long conceptsFileLine;

    private boolean descriptionsProcessed;

    private long descriptionsFileLine;

    private boolean relationshipsProcessed;

    private long relationshipsFileLine;


}
