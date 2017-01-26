using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ingreso = ConexionServiciosWS.ServicioDeIngresoWS1;

namespace ConexionServiciosWS.Clases
{
    public class ConexionIngresoWS
    {
        public string RespuestaCodificacionNuevoTermino(string txtEstablecimiento, string txtIdConcepto, string txtTermino, string txtTipoDescripcion, string txtMayusculas, string txtEmail, string txtObservacion, string txtProfesional, string txtProfesion, string txtEspecialidad, string txtSubEspecialidad, string txtCategoria)
        {
            string respuesta = string.Empty;
            try
            {
                ingreso.ServicioDeIngreso clientIngreso = new ingreso.ServicioDeIngreso();                
                ingreso.PeticionCodificacionDeNuevoTermino peti = new ingreso.PeticionCodificacionDeNuevoTermino();

                peti.establecimiento = txtEstablecimiento;
                peti.idConcepto = txtIdConcepto;
                peti.termino = txtTermino;
                peti.tipoDescripcion = txtTipoDescripcion;
                peti.esSensibleAMayusculas = Convert.ToBoolean(txtMayusculas);
                peti.email = txtEmail;
                peti.observacion = txtObservacion;
                peti.profesional = txtProfesional;
                peti.profesion = txtProfesion;
                peti.especialidad = txtEspecialidad;
                peti.subespecialidad = txtSubEspecialidad;
                peti.categoria = txtCategoria;
                var resultado = clientIngreso.codificacionDeNuevoTermino(peti);
                respuesta = resultado.ToString();
            }
            catch (Exception ex)
            {
                return ex.Message;
            }

            return respuesta;
        }

        public string RespuestaIncrementarContador(string txtIdDescripcion)
        {
            try
            {
                string respuesta = string.Empty;
                ingreso.ServicioDeIngreso clientIngreso = new ingreso.ServicioDeIngreso();
                respuesta = clientIngreso.incrementarContadorDescripcionConsumida(txtIdDescripcion).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }
    }
}
