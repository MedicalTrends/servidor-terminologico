<?php
namespace Semantikos\ClientBundle\Helper\Relationship;

use Semantikos\ClientBundle\API\PeticionSugerenciasDeDescripciones;
use Semantikos\ClientBundle\API\PeticionConceptosRelacionados;
use Semantikos\ClientBundle\API\PeticionConceptosRelacionadosPorCategoria;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;
 

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class RelationshipServiceMappingHelper {
    //put your code here    
    private $container;

    public function __construct(Container $container=null)
    {        
        $this->container = $container;
    }         
    
    public function mapWS006Parameters($parameters = null){                       
                        
        $peticionSugerenciaDeDescripciones = new PeticionSugerenciasDeDescripciones();
        
        $peticionSugerenciaDeDescripciones->setTermino($parameters['termino']);
        $peticionSugerenciaDeDescripciones->setNombreCategoria(explode(',',$parameters['categorias']));        
        $peticionSugerenciaDeDescripciones->setIdEstablecimiento($parameters['idEstablecimiento']);
        
        return array( 'peticionSugerenciasDeDescripciones' => $peticionSugerenciaDeDescripciones );
    }   
    
    public function mapWS010Parameters($parameters = null){                       
                        
        $peticionConceptosRelacionadosPorCategoria = new PeticionConceptosRelacionadosPorCategoria();
                
        $peticionConceptosRelacionadosPorCategoria->setCategoriaRelacion('Fármacos - Medicamento Clínico');
        $peticionConceptosRelacionadosPorCategoria->setIdConcepto($parameters['conceptId']);        
        $peticionConceptosRelacionadosPorCategoria->setIdDescripcion($parameters['descriptionId']);                
        
        return array( 'peticionConceptosRelacionadosPorCategoria' => $peticionConceptosRelacionadosPorCategoria );
    } 
    
    public function mapWS011Parameters($parameters = null){                       
                        
        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();
                
        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);        
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);                
        
        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }
    
    public function mapWS012Parameters($parameters = null){                       
                        
        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();
                
        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);        
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);                
        
        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }
    
    public function mapWS013Parameters($parameters = null){                       
                        
        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();
                
        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);        
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);                
        
        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS014Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS015Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS016Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS017Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS018Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS019Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS020Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }

    public function mapWS021Parameters($parameters = null){

        $peticionConceptosRelacionados = new PeticionConceptosRelacionados();

        $peticionConceptosRelacionados->setIdConcepto($parameters['conceptId']);
        $peticionConceptosRelacionados->setIdDescripcion($parameters['descriptionId']);

        return array( 'peticionConceptosRelacionados' => $peticionConceptosRelacionados );
    }
}                
