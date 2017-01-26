<?php
namespace Semantikos\ClientBundle\Helper\Relationship;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;

use Symfony\Component\HttpFoundation\Response;

use Semantikos\ClientBundle\API\RespuestaRefSets;

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class RelationshipServiceClientsHelper {
    //put your code here    
    private $container;
    
    private $soapClient;        
    
    private $logger;

    public function __construct(Container $container=null)
    {        
        $this->container = $container;
        $this->logger = $this->container->get('logger');
        
        $builder = $this->container->get('besimple.soap.client.builder.relationship');
        /**
         * Agregar opciones al builder
         */
        $this->soapClient = $builder->build();                    
    }          

    public function callWS006($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS006Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->sugerenciasDeDescripciones($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }      
    
    public function callWS010($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS010Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->conceptosRelacionados($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }   
    
    public function callWS010_01($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS010Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->conceptosRelacionadosLite($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }
    
    public function callWS011($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS011Parameters($params_array);                  
        
        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);                    
            $result = $this->soapClient->obtenerMedicamentoClinico($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }
    
    public function callWS011_01($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS011Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->obtenerMedicamentoClinicoLite($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }
    
    public function callWS012($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS012Parameters($params_array);                  
        
        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);             
            $result = $this->soapClient->obtenerMedicamentoBasico($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }
    
    public function callWS012_01($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS012Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->obtenerMedicamentoBasicoLite($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }
    
    public function callWS013($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS013Parameters($params_array);                  
        
        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);             
            $result = $this->soapClient->obtenerProductoComercial($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }
    
    public function callWS013_01($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS013Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->obtenerProductoComercialLite($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }    
    
}                
