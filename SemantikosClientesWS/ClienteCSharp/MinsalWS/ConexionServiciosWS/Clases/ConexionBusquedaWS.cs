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
        public string RespuestaBuscarDescripcionExacta(string txtIdEstableccimiento, string txtPattern)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PatronDeBusqueda patron = new busqueda.PatronDeBusqueda();
                patron.idEstablecimiento = txtIdEstableccimiento;
                patron.pattern = txtPattern;
                respuesta = clientbusqueda.buscarDescripcionExacta(patron).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaBuscarTermino(string txtTermino, string txtNombreCat, string txtNombreRefSet)
        {
            string respuesta = string.Empty;
            String[] categoria = new String[] { txtNombreCat };
            String[] refSet = new String[] { txtNombreRefSet };

            try
            {
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PeticionBuscarTerminoSimple termino = new busqueda.PeticionBuscarTerminoSimple();
                termino.termino = txtTermino;
                termino.nombreCategoria = categoria;
                termino.nombreRefSet = refSet;
                respuesta = clientbusqueda.buscarTermino(termino).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaBuscarTruncatePerfect(string txtTermino, string txtNombreCat, string txtNombreRefSet, string txtIdEstablecimiento)
        {
            string respuesta = string.Empty;
            String[] categoria = new String[] { txtNombreCat };
            String[] refSet = new String[] { txtNombreRefSet };

            try
            {
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PeticionBuscarTermino termino = new busqueda.PeticionBuscarTermino();
                termino.termino = txtTermino;
                termino.nombreCategoria = categoria;
                termino.nombreRefSet = refSet;
                termino.idEstablecimiento = txtIdEstablecimiento;
                respuesta = clientbusqueda.buscarTruncatePerfect(termino).ToString();
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
                busqueda.PeticionPorCategoria categoria = new busqueda.PeticionPorCategoria();
                categoria.nombreCategoria = txtNombre;
                categoria.idEstablecimiento = txtIdEstablecimiento;
                respuesta = clientbusqueda.conceptosPorCategoria(categoria).ToString();
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

        public string RespuestaConceptoPorRefSet(string txtNombre, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PeticionConceptosPorRefSet conceptos = new busqueda.PeticionConceptosPorRefSet();
                conceptos.idEstablecimiento = txtIdEstablecimiento;
                conceptos.nombreRefSet = txtNombre;
                respuesta = clientbusqueda.conceptosPorRefSet(conceptos).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaCrossMapsDirectorsPorIdDescripcion(string txtIdDescripcion, string txtIdConcepto, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.descriptionIDorConceptIDRequest maps = new busqueda.descriptionIDorConceptIDRequest();
                maps.description_id = txtIdDescripcion;
                maps.concept_id = txtIdConcepto;
                maps.stablishment_id = txtIdEstablecimiento;
                respuesta = clientbusqueda.crossMapsDirectosPorIDDescripcion(maps).ToString();
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
                respuesta = clientbusqueda.crossmapSetMembersDeCrossmapSet(txtNombreAbre).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaCrossMapsIndiectosPorIdDescripcionIDorConceptoID(string txtIdDescripcion, string txtIdConcepto, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.descriptionIDorConceptIDRequest concept = new busqueda.descriptionIDorConceptIDRequest();
                concept.description_id = txtIdDescripcion;
                concept.concept_id = txtIdConcepto;
                concept.stablishment_id = txtIdEstablecimiento;
                respuesta = clientbusqueda.crossMapsIndirectosPorDescripcionIDorConceptID(concept).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaDescripcionesPreferidasPorRefSet(string txtNombre, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PeticionConceptosPorRefSet conceptos = new busqueda.PeticionConceptosPorRefSet();
                conceptos.nombreRefSet = txtNombre; ;
                conceptos.idEstablecimiento = txtIdEstablecimiento;
                respuesta = clientbusqueda.descripcionesPreferidasPorRefSet(conceptos).ToString();
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
                respuesta = clientbusqueda.getCrossmapSets(txtIdInstitucion).ToString();
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
                respuesta = clientbusqueda.listaCategorias().ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaListaRefSet(bool establecimiento, string txtIncluyeEstacionamiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                respuesta = clientbusqueda.listaRefSet(establecimiento, txtIncluyeEstacionamiento).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaObtenerTerminosPedibles(string txtNombreCategoria, string txtIdEstablecimiento, string txtNombreRefSet, string txtPedible)
        {
            String[] categoria = new String[] { txtNombreCategoria };
            String[] nombreRef = new String[] { txtNombreRefSet };

            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PeticionConceptosPedibles pedibles = new busqueda.PeticionConceptosPedibles();
                pedibles.nombreCategoria = categoria;
                pedibles.idEstablecimiento = txtIdEstablecimiento;
                pedibles.nombreRefSet = nombreRef;
                pedibles.pedible = Convert.ToBoolean(txtPedible);
                
                respuesta = clientbusqueda.obtenerTerminosPedibles(pedibles).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }

        public string RespuestaRefSetsPorIdDescripcion(string txtIdDescripcion, string txtIdEstablecimiento)
        {
            try
            {
                string respuesta = string.Empty;
                busqueda.ServicioDeBusqueda clientbusqueda = new busqueda.ServicioDeBusqueda();
                busqueda.PeticionRefSetsPorIdDescripcion refSet = new busqueda.PeticionRefSetsPorIdDescripcion();
                refSet.idDescripcion = txtIdDescripcion;
                refSet.idStablishment = txtIdEstablecimiento;
                respuesta = clientbusqueda.refSetsPorIdDescripcion(refSet).ToString();
                return respuesta;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }
    }
}
