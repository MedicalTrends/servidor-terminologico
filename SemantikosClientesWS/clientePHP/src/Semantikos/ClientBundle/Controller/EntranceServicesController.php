<?php

namespace Semantikos\ClientBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

use Symfony\Component\HttpFoundation\Request;

use Symfony\Component\HttpFoundation\Response;


class EntranceServicesController extends Controller
{
    public function indexAction()
    {
        return $this->render('SemantikosClientBundle:EntranceServices:index.html.twig');
    }    
    
    public function callAction(Request $request)
    {        
        $operation = $request->request->get('operation');
        $parameters = $request->request->get('parameters');  
        $response;                
        
        $ws_params = $this->container->get('client.helper.utils')->decodeParameters($parameters);                                
        
        switch($operation) {
            case 'ws030':
                $response = $this->container->get('client.helper.entrance_clients')->callWS030($ws_params);
                break;   
            case 'ws031':
                $response = $this->container->get('client.helper.entrance_clients')->callWS031($ws_params);
                break; 
        }                        
        
        return new Response($response);
    }    
}
