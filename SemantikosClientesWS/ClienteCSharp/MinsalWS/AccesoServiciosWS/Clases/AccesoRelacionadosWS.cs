using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ConexionServiciosWS.Clases;

namespace AccesoServiciosWS.Clases
{
    public class AccesoRelacionadosWS
    {
        public string RespuestaConceptosRelacionados(string txtConcepto, string txtDescripcionId, string txtCategoriaRelacion)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaConceptosRelacionados(txtConcepto, txtDescripcionId, txtCategoriaRelacion);
        }

        public string RespuestaConceptosRelacionadosLite(string txtConcepto, string txtDescripcionId, string txtCategoriaRelacion)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaConceptosRelacionadosLite(txtConcepto, txtDescripcionId, txtCategoriaRelacion);
        }

        public string RespuestaObtenerBioequivalentes(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerBioequivalentes(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerFamiliaProducto(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerFamiliaProducto(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerMedicamentoBasico(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerMedicamentoBasico(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerMedicamentoBasicoLite(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerMedicamentoBasicoLite(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerMedicamentoClinico(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerMedicamentoClinico(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerMedicamentoClinicoConEnvase(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerMedicamentoClinicoConEnvase(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerMedicamentoClinicoConEnvaseLite(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerMedicamentoClinicoConEnvaseLite(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerMedicamentoClinicoLite(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerMedicamentoClinicoLite(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerProductoComercial(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerProductoComercial(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerProductoComercialConEnvase(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerProductoComercialConEnvase(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerProductoComercialLite(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerProductoComercialLite(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerProductoComercialConEnvaseLite(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerProductoComercialConEnvaseLite(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerRegistroISP(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerRegistroISP(txtConcepto, txtDescripcionId);
        }

        public string RespuestaObtenerSustancia(string txtConcepto, string txtDescripcionId)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaObtenerSustancia(txtConcepto, txtDescripcionId);
        }

        public string RespuestaSugerenciasDeDescripciones(string txtTermino, string txtNombreCat, string txtNombreRefSet)
        {
            ConexionRelacionadosWS conex = new ConexionRelacionadosWS();
            return conex.RespuestaSugerenciasDeDescripciones(txtTermino, txtNombreCat, txtNombreRefSet);
        }
    }
}
