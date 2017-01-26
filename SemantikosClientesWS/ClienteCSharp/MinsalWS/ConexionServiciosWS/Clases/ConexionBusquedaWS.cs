using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using busqueda = ConexionServiciosWS.ServicioDeBusquedaWS1;

namespace ConexionServiciosWS.Clases
{
    public class ConexionBusquedaWS
    {
        public string RespuestaBuscarTermino(string txtTermino, string txtNombreCat, string txtNombreRefSet)
        {
            string respuesta = string.Empty;
            String[] categoria = new String[] { txtNombreCat };
            String[] refSet = new String[] { txtNombreRefSet };

            try
            {
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.buscarTermino(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaBuscarTruncatePerfect(string txtTermino, string txtNombreCat, string txtNombreRefSet, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.buscarTruncatePerfect(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaConceptoPorCategoria(string txtNombre, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.conceptosPorCategoria(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaConceptoPorIdDescripcion(string txtIdDescripcion)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.conceptoPorIdDescripcion(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaConceptoPorRefSet(string txtNombre, string txtNumeroPagina, string txtTamañoPagina)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.conceptopor(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaCrossMapsDirectorsPorIdDescripcion(string txtIdDescripcion)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaCrossMapSetMembersDeCrossmapSet(string txtNombreAbre)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaCrossMapsIndiectosPorIdDescripcion(string txtIdDescripcion)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaDescripcionesPreferidasPorRefSet(string txtNombre, string txtNumeroPagina, string txtTamañoPagina)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaGetCrossmapSets(string txtIdInstitucion)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaListaCategorias()
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaListaRefSet(string txtIncluyeEstacionamiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerTerminosPedibles(string txtNombreCategoria, string txtNombreRefSet, string txtPedible)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaRefSetsPorIdDescripcion(string txtIdDescripcion)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }
    }
}
