package cl.minsal.semantikos.model;

/**
 * Created by root on 08-07-16.
 */
public class Extern {
    private long idExtern;
    private Long idExternTableName;
    private Long pkValueInAccesoryTable;

    public long getIdExtern() {
        return idExtern;
    }

    public void setIdExtern(long idExtern) {
        this.idExtern = idExtern;
    }

    public Long getIdExternTableName() {
        return idExternTableName;
    }

    public void setIdExternTableName(Long idExternTableName) {
        this.idExternTableName = idExternTableName;
    }

    public Long getPkValueInAccesoryTable() {
        return pkValueInAccesoryTable;
    }

    public void setPkValueInAccesoryTable(Long pkValueInAccesoryTable) {
        this.pkValueInAccesoryTable = pkValueInAccesoryTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Extern that = (Extern) o;

        if (idExtern != that.idExtern) return false;
        if (idExternTableName != null ? !idExternTableName.equals(that.idExternTableName) : that.idExternTableName != null)
            return false;
        if (pkValueInAccesoryTable != null ? !pkValueInAccesoryTable.equals(that.pkValueInAccesoryTable) : that.pkValueInAccesoryTable != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idExtern ^ (idExtern >>> 32));
        result = 31 * result + (idExternTableName != null ? idExternTableName.hashCode() : 0);
        result = 31 * result + (pkValueInAccesoryTable != null ? pkValueInAccesoryTable.hashCode() : 0);
        return result;
    }
}
