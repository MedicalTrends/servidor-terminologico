using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AccesoServiciosWS.Clases;

namespace MinsalWS.Pages
{
    public partial class IncrementarContadorDescripcion : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        #region EVENTOS

        protected void btnInvocar_Click(object sender, EventArgs e)
        {
            if (this.ConsumirServicio())
            {
                //string script = @"<script type='text/javascript'> alert('Operación ejecutada correctacmente!.');</script>";
                //ScriptManager.RegisterStartupScript(this, typeof(Page), "alerta", script, false);
            }
        }

        #endregion

        #region METODOS

        public bool ConsumirServicio()
        {
            try
            {
                string respuesta = string.Empty;
                AccesoIngresoWS ingreso = new AccesoIngresoWS();
                respuesta = ingreso.RespuestaIncrementarContador(txtIdDescripcion.Text);
                TextArea1.Text = respuesta;
                return true;
            }
            catch (Exception)
            {
                string script = @"<script type='text/javascript'> alert('Error al procesar el servicio, contacte al administrador. ');</script>";
                ScriptManager.RegisterStartupScript(this, typeof(Page), "alerta", script, false);
                return false;
            }
        }

        #endregion
    }
}