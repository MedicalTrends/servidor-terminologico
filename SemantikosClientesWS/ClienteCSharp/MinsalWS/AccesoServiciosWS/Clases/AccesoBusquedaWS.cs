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

        public string RespuestaConceptoPorRefSet(string txtNombre, string txtNumeroPagina, string txtTamañoPagina)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaConceptoPorRefSet(txtNombre, txtNumeroPagina, txtTamañoPagina);
        }

        public string RespuestaCrossMapsDirectorsPorIdDescripcion(string txtIdDescripcion)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaCrossMapsDirectorsPorIdDescripcion(txtIdDescripcion);
        }

        public string RespuestaCrossMapSetMembersDeCrossmapSet(string txtNombreAbre)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaCrossMapSetMembersDeCrossmapSet(txtNombreAbre);
        }

        public string RespuestaCrossMapsIndiectosPorIdDescripcion(string txtIdDescripcion)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaCrossMapsIndiectosPorIdDescripcion(txtIdDescripcion);
        }

        public string RespuestaDescripcionesPreferidasPorRefSet(string txtNombre, string txtNumeroPagina, string txtTamañoPagina)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaDescripcionesPreferidasPorRefSet(txtNombre, txtNumeroPagina, txtTamañoPagina);
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

        public string RespuestaListaRefSet(string txtIncluyeEstacionamiento)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaListaRefSet(txtIncluyeEstacionamiento);
        }

        public string RespuestaObtenerTerminosPedibles(string txtNombreCategoria, string txtNombreRefSet, string txtPedible)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaObtenerTerminosPedibles(txtNombreCategoria, txtNombreRefSet, txtPedible);
        }

        public string RespuestaRefSetsPorIdDescripcion(string txtIdDescripcion)
        {
            ConexionBusquedaWS conex = new ConexionBusquedaWS();
            return conex.RespuestaRefSetsPorIdDescripcion(txtIdDescripcion);
        }
    }
}
