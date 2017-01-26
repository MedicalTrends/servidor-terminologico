<?php
namespace Semantikos\ClientBundle\Helper\Entrance;

use Semantikos\ClientBundle\API\PeticionCodificacionDeNuevoTermino;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;
 

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class EntranceServiceMappingHelper {
    //put your code here    
    private $container;

    public function __construct(Container $container=null)
    {        
        $this->container = $container;
    }         
    
    public function mapWS030Parameters($parameters = null){                       
                        
        return array( 'descriptionID' => $parameters['descriptionId'] );
    }  
    
    public function mapWS031Parameters($parameters = null){                       
        
        $peticionCodificacionDeNuevoTermino = new PeticionCodificacionDeNuevoTermino();
        
        $peticionCodificacionDeNuevoTermino->setTermino($parameters['termino']);
        $peticionCodificacionDeNuevoTermino->setCategoria($parameters['categoriaPropuesta']);
        $peticionCodificacionDeNuevoTermino->setProfesional($parameters['nombreProfesional']);
        $peticionCodificacionDeNuevoTermino->setEmail($parameters['mail']);
        $peticionCodificacionDeNuevoTermino->setProfesion($parameters['profesion']);
        $peticionCodificacionDeNuevoTermino->setEspecialidad($parameters['especialidad']);
        $peticionCodificacionDeNuevoTermino->setSubespecialidad($parameters['subespecialidad']);
        $peticionCodificacionDeNuevoTermino->setEstablecimiento($parameters['establecimiento']);
        $peticionCodificacionDeNuevoTermino->setEsSensibleAMayusculas($parameters['sensibleMayusculas']);
        $peticionCodificacionDeNuevoTermino->setObservacion($parameters['observacion']);
        $peticionCodificacionDeNuevoTermino->setTipoDescripcion($parameters['tipoDescripcionPropuesta']);
                        
        return array( 'peticionCodificacionDeNuevoTermino' => $peticionCodificacionDeNuevoTermino );
    }
        
}                
