using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AccesoServiciosWS.Clases;

namespace MinsalWS.Pages.Servicio2
{
    public partial class BuscarTruncatePerfect : System.Web.UI.Page
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
            else
            {
                string script = @"<script type='text/javascript'> alert('Error al procesar el servicio, contacte al administrador. ');</script>";
                ScriptManager.RegisterStartupScript(this, typeof(Page), "alerta", script, false);
            }
        }

        #endregion

        #region METODOS

        public bool ConsumirServicio()
        {
            try
            {
                string respuesta = string.Empty;
                AccesoBusquedaWS ingreso = new AccesoBusquedaWS();
                respuesta = ingreso.RespuestaBuscarTruncatePerfect(txtTermino.Text, txtNombreCat.Text, txtNombreRefSet.Text, txtIdEstablecimiento.Text);
                if (respuesta != "False")
                {
                    TextArea1.Text = respuesta;
                    return true;
                }
                else
                {
                    TextArea1.Text = respuesta;
                    return false;
                }
            }
            catch (Exception)
            {
                return false;
            }
        }

        #endregion
    }
}