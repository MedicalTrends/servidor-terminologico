<?php

namespace Semantikos\ClientBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

use Symfony\Component\HttpFoundation\Request;

use Symfony\Component\HttpFoundation\Response;


class SearchServicesController extends Controller
{
    public function indexAction()
    {
        return $this->render('SemantikosClientBundle:SearchServices:index.html.twig');
    }    
    
    public function callAction(Request $request)
    {        
        $operation = $request->request->get('operation');
        $parameters = $request->request->get('parameters');  
        $response;                              
        
        $ws_params = $this->container->get('client.helper.utils')->decodeParameters($parameters);                
        
        $requestStatus = $this->container->get('client.helper.utils')->validate($operation, $ws_params);                                
        
        if($requestStatus->isError()) {            
            return new Response($requestStatus->getMessage(), 500);            
        }
                        
        switch($operation) {
            case 'ws001':                
                //return $this->container->get('client.helper.search_clients')->callWS001($ws_params);                
                $response = $this->container->get('client.helper.search_clients')->callWS001($ws_params);
                break;     
            case 'ws002':
                $response = $this->container->get('client.helper.search_clients')->callWS002($ws_params);
                break;  
            case 'ws004':
                $response = $this->container->get('client.helper.search_clients')->callWS004($ws_params);
                break;  
            case 'ws005':
                $response = $this->container->get('client.helper.search_clients')->callWS005($ws_params);
                break;     
            case 'ws007':
                $response = $this->container->get('client.helper.search_clients')->callWS007($ws_params);
                break;             
            case 'ws008':
                $response = $this->container->get('client.helper.search_clients')->callWS008($ws_params);
                break;  
            case 'ws009':
                $response = $this->container->get('client.helper.search_clients')->callWS009($ws_params);
                break;  
            case 'ws022':
                $response = $this->container->get('client.helper.search_clients')->callWS022($ws_params);
                break; 
            case 'ws023':
                $response = $this->container->get('client.helper.search_clients')->callWS023($ws_params);
                break;   
            case 'ws024':
                $response = $this->container->get('client.helper.search_clients')->callWS024($ws_params);
                break;              
            case 'ws025':
                $response = $this->container->get('client.helper.search_clients')->callWS025($ws_params);
                break;       
            case 'ws026':
                $response = $this->container->get('client.helper.search_clients')->callWS026($ws_params);
                break;                   
            case 'ws027':
                $response = $this->container->get('client.helper.search_clients')->callWS027($ws_params);
                break;                               
            case 'ws028':
                $response = $this->container->get('client.helper.search_clients')->callWS028($ws_params);
                break;              
        }                                                
        
        return new Response($response);
    }    
    
}
