<?php

namespace Semantikos\ClientBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

use Symfony\Component\HttpFoundation\Request;

use Symfony\Component\HttpFoundation\Response;


class RelationshipServicesController extends Controller
{
    public function indexAction()
    {
        return $this->render('SemantikosClientBundle:RelationshipServices:index.html.twig');
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
            return;
        }
        
        switch($operation) {
            case 'ws006':
                $response = $this->container->get('client.helper.relationship_clients')->callWS006($ws_params);
                break;               
            case 'ws010':
                $response = $this->container->get('client.helper.relationship_clients')->callWS010($ws_params);
                break;              
            case 'ws010_01':
                $response = $this->container->get('client.helper.relationship_clients')->callWS010_01($ws_params);
                break;
            case 'ws011':                
                $response = $this->container->get('client.helper.relationship_clients')->callWS011($ws_params);
                break; 
            case 'ws011_01':
                $response = $this->container->get('client.helper.relationship_clients')->callWS011_01($ws_params);
                break;
            case 'ws012':                
                $response = $this->container->get('client.helper.relationship_clients')->callWS012($ws_params);
                break; 
            case 'ws012_01':
                $response = $this->container->get('client.helper.relationship_clients')->callWS012_01($ws_params);
                break;
            case 'ws013':                
                $response = $this->container->get('client.helper.relationship_clients')->callWS013($ws_params);
                break; 
            case 'ws013_01':
                $response = $this->container->get('client.helper.relationship_clients')->callWS013_01($ws_params);
                break;              
        }                        
        
        return new Response($response);
    }
}
