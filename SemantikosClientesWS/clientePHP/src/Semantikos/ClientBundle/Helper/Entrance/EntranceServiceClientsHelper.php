<?php
namespace Semantikos\ClientBundle\Helper\Entrance;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;

use Symfony\Component\HttpFoundation\Response;

use Semantikos\ClientBundle\API\RespuestaRefSets;
 
/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class EntranceServiceClientsHelper {
    //put your code here    
    private $container;
    
    private $soapClient;        
    
    private $logger;

    public function __construct(Container $container=null)
    {        
        $this->container = $container;
        $this->logger = $this->container->get('logger');
        
        $builder = $this->container->get('besimple.soap.client.builder.entrance');
        /**
         * Agregar opciones al builder
         */
        $this->soapClient = $builder->build();                    
    }          

    public function callWS030($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.entrance_mapping')->mapWS030Parameters($params_array);                  
        
        try {
            $result = $this->soapClient->incrementarContadorDescripcionConsumida($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }      
    
    public function callWS031($params_array = null){                                                                                                                                                                                                                                                                                                                           
        
        $peticion = $this->container->get('client.helper.entrance_mapping')->mapWS031Parameters($params_array);                                                                                              
        
        try {            
            $result = $this->soapClient->codificacionDeNuevoTermino($peticion);                    
        } catch (\SoapFault $soapFault) {
            return json_encode($soapFault);
        }
                
        return json_encode($result);
    }      
            
    
    /*
    public function callWS001($params_array = null){
        
        //$url = "http://www.minsal.cl/semantikos/ws/ServicioDeBusqueda?";
        
        $url = "http://192.168.0.226/semantikos/ws/ServicioDeBusqueda?";
        
        $response;
        
        $url= $url.$this->container->get('client.helper.utils')->serializeParametersURL($params_array);                                  
        
        $restClient = $this->container->get('circle.restclient');
        
        try {
            $response = $restClient->get($url);            
        } catch (Exception $ex) {
            $logger->error('An error occurred');
        }        
        
        var_dump($url);
        
        return new Response($response);        
    }
    */
}                
