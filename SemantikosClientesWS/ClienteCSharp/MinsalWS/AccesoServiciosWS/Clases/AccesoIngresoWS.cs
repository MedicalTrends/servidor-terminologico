using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ConexionServiciosWS.Clases;

namespace AccesoServiciosWS.Clases
{
    public class AccesoIngresoWS
    {
        public string RespuestaCodificacionNuevoTermino(string txtEstablecimiento, string txtIdConcepto, string txtTermino, string txtTipoDescripcion, string txtMayusculas, string txtEmail, string txtObservacion, string txtProfesional, string txtProfesion, string txtEspecialidad)
        {
            ConexionIngresoWS conex = new ConexionIngresoWS();
            return conex.RespuestaCodificacionNuevoTermino(txtEstablecimiento, txtIdConcepto, txtTermino, txtTipoDescripcion, txtMayusculas, txtEmail, txtObservacion, txtProfesional, txtProfesion, txtEspecialidad);
        }

        public string RespuestaIncrementarContador(string txtIdDescripcion)
        {
            ConexionIngresoWS conex = new ConexionIngresoWS();
            return conex.RespuestaIncrementarContador(txtIdDescripcion);
        }
    }
}
