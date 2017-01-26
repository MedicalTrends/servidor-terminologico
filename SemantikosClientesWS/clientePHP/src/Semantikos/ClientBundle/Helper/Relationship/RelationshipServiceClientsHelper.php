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

    public function callWS014($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS014Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerMedicamentoClinicoConEnvase($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS014_01($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS014Parameters($params_array);

        try {
            $result = $this->soapClient->obtenerMedicamentoClinicoConEnvaseLite($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS015($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS015Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerProductoComercialConEnvase($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS015_01($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS015Parameters($params_array);

        try {
            $result = $this->soapClient->obtenerProductoComercialConEnvaseLite($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS016($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS016Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerFamiliaProducto($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS017($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS017Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerProductoComercial($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS017_01($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS017Parameters($params_array);

        try {
            $result = $this->soapClient->obtenerProductoComercialLite($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS018($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS018Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerProductoComercialConEnvase($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS018_01($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS018Parameters($params_array);

        try {
            $result = $this->soapClient->obtenerProductoComercialConEnvaseLite($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS019($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS019Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerSustancia($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS020($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS020Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerRegistroISP($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }

    public function callWS021($params_array = null){

        $peticion = $this->container->get('client.helper.relationship_mapping')->mapWS021Parameters($params_array);

        try {
            //$result = $this->soapClient->conceptosRelacionadosChildren($peticion);
            $result = $this->soapClient->obtenerBioequivalentes($peticion);
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }

        return json_encode($result);
    }
}                
