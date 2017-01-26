<?php

namespace Semantikos\ClientBundle\Helper\Search;

use Semantikos\ClientBundle\API\PeticionBuscarTerminoSimple;
use Semantikos\ClientBundle\API\PeticionPorCategoria;
use Semantikos\ClientBundle\API\PeticionBuscarTermino;
use Semantikos\ClientBundle\API\PeticionConceptosPedibles;
use Semantikos\ClientBundle\API\PeticionRefSetsPorIdDescripcion;
use Semantikos\ClientBundle\API\PeticionConceptosPorRefSet;
use Semantikos\ClientBundle\API\descriptionIDorConceptIDRequest;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;
 

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class SearchServiceMappingHelper {
    //put your code here    
    private $container;

    public function __construct(Container $container=null)
    {        
        $this->container = $container;
    }
    
    public function mapWS001Parameters($parameters = null){  
                        
        $peticionBuscarTerminoSimple = new PeticionBuscarTerminoSimple();
        
        $peticionBuscarTerminoSimple->setTermino($parameters['termino']);
        $peticionBuscarTerminoSimple->setNombreCategoria(explode(',',$parameters['categorias']));
        $peticionBuscarTerminoSimple->setNombreRefSet(explode(',',$parameters['refSets']));                                                                                      
        
        return array( 'peticionBuscarTermino' => $peticionBuscarTerminoSimple );
    }
    
    public function mapWS002Parameters($parameters = null){                                    
        
        $peticionPorCategoria = new PeticionPorCategoria($parameters['nombreCategoria'],
                                                         $parameters['idEstablecimiento']);        
        
        return array( 'peticionConceptosPorCategoria' => $peticionPorCategoria );
    }
    
    public function mapWS004Parameters($parameters = null){                       
        
        $peticionBuscarTermino = new PeticionBuscarTermino();
        
        $peticionBuscarTermino->setTermino($parameters['termino']);
        $peticionBuscarTermino->setNombreCategoria(explode(',',$parameters['categorias']));
        $peticionBuscarTermino->setNombreRefSet(explode(',',$parameters['refSets']));
        $peticionBuscarTermino->setIdEstablecimiento($parameters['idEstablecimiento']);
        
        return array( 'peticionBuscarTermino' => $peticionBuscarTermino );
    }
    
    public function mapWS005Parameters($parameters = null){                       
        
        $peticionConceptosPedibles = new PeticionConceptosPedibles();
        
        $peticionConceptosPedibles->setPedible($parameters['pedible']);                
        $peticionConceptosPedibles->setNombreCategoria(explode(',',$parameters['categorias']));
        $peticionConceptosPedibles->setNombreRefSet(explode(',',$parameters['refSets']));
        $peticionConceptosPedibles->setIdEstablecimiento($parameters['idEstablecimiento']);
        
        return array( 'peticionObtenerTerminosPedibles' => $peticionConceptosPedibles );
    }
    
    public function mapWS007Parameters($parameters = null){                       
        
        $peticionRefSetsPorIdDescripcion = new PeticionRefSetsPorIdDescripcion();
        
        $peticionRefSetsPorIdDescripcion->setIdDescripcion($parameters['descriptionId']);                
        $peticionRefSetsPorIdDescripcion->setIncluyeEstablecimientos($parameters['incluyeEstablecimiento']);
        $peticionRefSetsPorIdDescripcion->setIdStablishment($parameters['idEstablecimiento']);        
        
        return array( 'peticionRefSetsPorIdDescripcion' => $peticionRefSetsPorIdDescripcion );
    }
    
    public function mapWS008Parameters($parameters = null){                                          
        
        return array( 'incluyeEstablecimientos' => $parameters['incluyeEstablecimiento'],
                      'idStablishment' => $parameters['idEstablecimiento']
                    );
    }
    
    public function mapWS022Parameters($parameters = null){                       
        
        $peticionConceptosPorRefSet = new PeticionConceptosPorRefSet();
        
        $peticionConceptosPorRefSet->setNombreRefSet($parameters['refSet']);        
        $peticionConceptosPorRefSet->setIdEstablecimiento($parameters['idEstablecimiento']);        
        
        return array( 'peticionConceptosPorRefSet' => $peticionConceptosPorRefSet );
    }
    
    public function mapWS026Parameters($parameters = null){                       
        
        $descriptionIDorConceptIDRequest = new descriptionIDorConceptIDRequest();
        
        $descriptionIDorConceptIDRequest->setDescription_id($parameters['descriptionId']);        
        $descriptionIDorConceptIDRequest->setConcept_id($parameters['conceptId']);
        $descriptionIDorConceptIDRequest->setStablishment_id($parameters['idEstablecimiento']);        
        
        return array( 'descripcionIDorConceptIDRequest' => $descriptionIDorConceptIDRequest );
    }
    
    public function mapWS027Parameters($parameters = null){                       
        
        $descriptionIDorConceptIDRequest = new descriptionIDorConceptIDRequest();
        
        $descriptionIDorConceptIDRequest->setDescription_id($parameters['descriptionId']);        
        $descriptionIDorConceptIDRequest->setConcept_id($parameters['conceptId']);
        $descriptionIDorConceptIDRequest->setStablishment_id($parameters['idEstablecimiento']);        
        
        return array( 'DescripcionID' => $descriptionIDorConceptIDRequest );
    }
    
    public function mapWS028Parameters($parameters = null){                                      
        
        return array( 'idDescripcion' => $parameters['descriptionId'] );
    }
        
}                
