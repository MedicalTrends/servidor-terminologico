using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using busqueda = ConexionServiciosWS.ServicioDeBusquedaWS;

namespace ConexionServiciosWS.Clases
{
    public class ConexionBusquedaWS
    {
        public string RespuestaBuscarTermino(string txtTermino, string txtNombreCat, string txtNombreRefSet)
        {
            string respuesta = string.Empty;
            String[] categoria = new String[] { txtNombreCat };
            String[] refSet = new String[] { txtNombreRefSet };

            busqueda.SearchServiceClient clientBusqueda = new busqueda.SearchServiceClient();
            //busqueda.buscarTermino termino = new busqueda.buscarTermino();
            busqueda.PeticionBuscarTermino termino = new busqueda.PeticionBuscarTermino();
            termino.termino = txtTermino;
            termino.nombreCategoria = categoria;
            termino.nombreRefSet = refSet;
            respuesta = clientBusqueda.buscarTermino(termino);
            
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaBuscarTruncatePerfect(string txtTermino, string txtNombreCat, string txtNombreRefSet, string txtIdEstablecimiento)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaConceptoPorCategoria(string txtNombre, string txtIdEstablecimiento)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaConceptoPorIdDescripcion(string txtIdDescripcion)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaConceptoPorRefSet(string txtNombre, string txtNumeroPagina, string txtTamañoPagina)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaCrossMapsDirectorsPorIdDescripcion(string txtIdDescripcion)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaCrossMapSetMembersDeCrossmapSet(string txtNombreAbre)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtNombreAbre;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtNombreAbre).ToString();
            return respuesta;
        }

        public string RespuestaCrossMapsIndiectosPorIdDescripcion(string txtIdDescripcion)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }

        public string RespuestaDescripcionesPreferidasPorRefSet(string txtNombre, string txtNumeroPagina, string txtTamañoPagina)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtNombre;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtNombre).ToString();
            return respuesta;
        }

        public string RespuestaGetCrossmapSets(string txtIdInstitucion)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdInstitucion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdInstitucion).ToString();
            return respuesta;
        }

        public string RespuestaListaCategorias()
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = "";
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida("").ToString();
            return respuesta;
        }

        public string RespuestaListaRefSet(string txtIncluyeEstacionamiento)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIncluyeEstacionamiento;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIncluyeEstacionamiento).ToString();
            return respuesta;
        }

        public string RespuestaObtenerTerminosPedibles(string txtNombreCategoria, string txtNombreRefSet, string txtPedible)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtNombreCategoria;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtNombreCategoria).ToString();
            return respuesta;
        }

        public string RespuestaRefSetsPorIdDescripcion(string txtIdDescripcion)
        {
            string respuesta = string.Empty;

            busqueda.ServicioDeIngresoClient clientIngreso = new busqueda.ServicioDeIngresoClient();
            busqueda.incrementarContadorDescripcionConsumida incre = new busqueda.incrementarContadorDescripcionConsumida();
            incre.idDescripcion = txtIdDescripcion;
            respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
            return respuesta;
        }
    }
}
