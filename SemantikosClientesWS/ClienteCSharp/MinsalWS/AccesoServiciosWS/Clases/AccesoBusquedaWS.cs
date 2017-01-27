using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ConexionServiciosWS.Clases;

namespace AccesoServiciosWS.Clases
{
    public class AccesoBusquedaWS
    {
        public string RespuestaBuscarDescripcionExacta(string txtIdEstableccimiento, string txtPattern)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaBuscarDescripcionExacta(txtIdEstableccimiento, txtPattern);
        }

        public string RespuestaBuscarTermino(string txtTermino, string txtNombreCat, string txtNombreRefSet)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaBuscarTermino(txtTermino, txtNombreCat, txtNombreRefSet);
        }

        public string RespuestaBuscarTruncatePerfect(string txtTermino, string txtNombreCat, string txtNombreRefSet, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaBuscarTruncatePerfect(txtTermino, txtNombreCat, txtNombreRefSet, txtIdEstablecimiento);
        }

        public string RespuestaConceptoPorCategoria(string txtNombre, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaConceptoPorCategoria(txtNombre, txtIdEstablecimiento);
        }

        public string RespuestaConceptoPorIdDescripcion(string txtIdDescripcion)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaConceptoPorIdDescripcion(txtIdDescripcion);
        }

        public string RespuestaConceptoPorRefSet(string txtNombre, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaConceptoPorRefSet(txtNombre, txtIdEstablecimiento);
        }

        public string RespuestaCrossMapsDirectorsPorIdDescripcion(string txtIdDescripcion, string txtIdConcepto, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaCrossMapsDirectorsPorIdDescripcion(txtIdDescripcion, txtIdConcepto, txtIdEstablecimiento);
        }

        public string RespuestaCrossMapSetMembersDeCrossmapSet(string txtNombreAbre)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaCrossMapSetMembersDeCrossmapSet(txtNombreAbre);
        }

        public string RespuestaCrossMapsIndiectosPorIdDescripcionIDorConceptoID(string txtIdDescripcion, string txtIdConcepto, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaCrossMapsIndiectosPorIdDescripcionIDorConceptoID(txtIdDescripcion, txtIdConcepto, txtIdEstablecimiento);
        }

        public string RespuestaDescripcionesPreferidasPorRefSet(string txtNombre, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaDescripcionesPreferidasPorRefSet(txtNombre, txtIdEstablecimiento);
        }

        public string RespuestaGetCrossmapSets(string txtIdInstitucion)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaGetCrossmapSets(txtIdInstitucion);
        }

        public string RespuestaListaCategorias()
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaListaCategorias();
        }

        public string RespuestaListaRefSet(bool txtIncluyeEstacionamiento, string txtEstablecimientoNombre)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaListaRefSet(Convert.ToBoolean(txtIncluyeEstacionamiento), txtEstablecimientoNombre);
        }

        public string RespuestaObtenerTerminosPedibles(string txtNombreCategoria, string txtIdEstablecimiento, string txtNombreRefSet, string txtPedible)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaObtenerTerminosPedibles(txtNombreCategoria, txtIdEstablecimiento, txtNombreRefSet, txtPedible);
        }

        public string RespuestaRefSetsPorIdDescripcion(string txtIdDescripcion, string txtIdEstablecimiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaRefSetsPorIdDescripcion(txtIdDescripcion, txtIdEstablecimiento);
        }
    }
}
