using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using relacionados = ConexionServiciosWS.ServicioDeRelacionadosWS1;

namespace ConexionServiciosWS.Clases
{
    public class ConexionRelacionadosWS
    {
        public string RespuestaConceptosRelacionados(string txtConcepto, string txtDescripcionId, string txtCategoriaRelacion)
        {
            string respuesta = string.Empty;

            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionadosPorCategoria conceptos = new relacionados.PeticionConceptosRelacionadosPorCategoria();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                conceptos.categoriaRelacion = txtCategoriaRelacion;
                respuesta = clientRelacionados.conceptosRelacionados(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaConceptosRelacionadosLite(string txtConcepto, string txtDescripcionId, string txtCategoriaRelacion)
        {
            string respuesta = string.Empty;

            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionadosPorCategoria conceptos = new relacionados.PeticionConceptosRelacionadosPorCategoria();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                conceptos.categoriaRelacion = txtCategoriaRelacion;
                respuesta = clientRelacionados.conceptosRelacionadosLite(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerBioequivalentes(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerBioequivalentes(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerFamiliaProducto(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerFamiliaProducto(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerMedicamentoBasico(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerMedicamentoBasico(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerMedicamentoBasicoLite(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerMedicamentoBasicoLite(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerMedicamentoClinico(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerMedicamentoClinico(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerMedicamentoClinicoConEnvase(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerMedicamentoClinicoConEnvase(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerMedicamentoClinicoConEnvaseLite(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerMedicamentoClinicoConEnvaseLite(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerMedicamentoClinicoLite(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerMedicamentoClinicoLite(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerProductoComercial(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerProductoComercial(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerProductoComercialConEnvase(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerProductoComercialConEnvase(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerProductoComercialLite(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerProductoComercialLite(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerProductoComercialConEnvaseLite(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerProductoComercialConEnvaseLite(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerRegistroISP(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerRegistroISP(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerSustancia(string txtConcepto, string txtDescripcionId)
        {
            string respuesta = string.Empty;
            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionConceptosRelacionados conceptos = new relacionados.PeticionConceptosRelacionados();
                conceptos.idConcepto = txtConcepto;
                conceptos.idDescripcion = txtDescripcionId;
                respuesta = clientRelacionados.obtenerSustancia(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaSugerenciasDeDescripciones(string txtTermino, string txtNombreCat, string txtNombreRefSet)
        {
            string respuesta = string.Empty;
            String[] categoria = new String[] { txtNombreCat };
            String[] refSet = new String[] { txtNombreRefSet };

            try
            {
                relacionados.ServicioDeRelacionados clientRelacionados = new relacionados.ServicioDeRelacionados();
                relacionados.PeticionSugerenciasDeDescripciones sugerencias = new relacionados.PeticionSugerenciasDeDescripciones();
                sugerencias.termino = txtTermino;
                sugerencias.nombreCategoria = categoria;
                sugerencias.nombreRefSet = refSet;
                respuesta = clientRelacionados.sugerenciasDeDescripciones(sugerencias).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }
    }
}
