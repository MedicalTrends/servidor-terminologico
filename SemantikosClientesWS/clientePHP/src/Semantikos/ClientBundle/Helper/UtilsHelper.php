<?php
namespace Semantikos\ClientBundle\Helper;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;

use Symfony\Component\HttpFoundation\JsonResponse;

use Semantikos\ClientBundle\Entity\RequestStatus;
 

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class UtilsHelper {
    //put your code here    
    private $container;

    public function __construct(Container $container=null)
    {        
        $this->container = $container;
    }         
    
    public function decodeParameters($parameters = null){            
        
        $ws_params = array();
        $regex= '/\[(.*?)\]/';
        $last = sizeof($parameters)-1;        
        
        foreach($parameters as $key => $value) {   
            if($key === $last)
                continue;            
            preg_match('/\[(.*?)\]/', $value['name'], $match);
            $name = str_replace(array( '[', ']' ), '', $match[0]);
            $ws_params[$name] = $value['value'];            
        }
        
        return $ws_params;
    }
    
    public function serializeParametersURL($params_array = null){            
        
        $params_url='';
        
        foreach($params_array as $key => $value){                
            $params_url = $params_url.$key.'='.$value.'&';
        }  
        
        return substr($params_url, 0, -1);
    }
    
    public function validate($operation = null, $params_array = null){                            
        
        $error = false;
        $message = '';
        
        $requestStatus = new RequestStatus();
        
        switch($operation) {            
            case 'ws004':
                if($params_array['categorias'] == "" && $params_array['refSets'] == "") {
                    $error = true;
                    $message = "Debe ingresar al menos una 'Categoría' o un 'RefSet'";
                }                                
                break; 
            case 'ws026':
            case 'ws027':
            case 'ws010':
                if($params_array['descriptionId'] == "" && $params_array['conceptId'] == "") {
                    $error = true;
                    $message = "Debe ingresar al menos un 'descriptionId' o un 'conceptId'";
                }                                
                break;             
        }        
        
        $requestStatus->setError($error);
        $requestStatus->setMessage($message);
        
        return $requestStatus;            
    }
    
}                
