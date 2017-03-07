<?php

namespace Semantikos\ClientBundle\EventListener;

use Symfony\Component\HttpKernel\Event\FilterControllerEvent;
use Symfony\Component\HttpKernel\HttpKernel;
use Symfony\Component\HttpKernel\Controller\TraceableControllerResolver;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Router;
use Symfony\Component\Security\Core\SecurityContext;
use Symfony\Component\Security\Core\Authorization\AuthorizationChecker;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorage;
use Symfony\Component\HttpFoundation\Session\Session;

class ControllerListener
{    
        
    protected $router;
    protected $security;        
    //protected $resolver;
    protected $tokenStorage;
    protected $session;


    public function __construct(Router $router, AuthorizationChecker $security, /*TraceableControllerResolver $resolver,*/
                                TokenStorage $tokenStorage = null, Session $session) {
        $this->router = $router;		
        $this->security = $security;                
        //$this->resolver = $resolver;
        $this->tokenStorage = $tokenStorage;
        $this->session = $session;
    }
        
    public function onKernelController(FilterControllerEvent $event)
    {        	
        $request = $event->getRequest();                                        
        $routeName = $request->getPathInfo('_route');  
        
        $tokens=explode('/',$routeName);
        $route=end($tokens);                    
        
        switch ($route){
            case 'SearchServices':
                $this->session->set('active', 'search_index');
                break;            
            case 'EntranceServices':
                $this->session->set('active', 'entrance_index');                                                                                    
                break;            
            case 'RelationshipServices':                
                $this->session->set('active', 'relationship_index');                                
                break;            
        }
        
        /*
        if ($this->tokenStorage->getToken() != null && explode('/',$routeName)[1] != "login" &&
            $this->security->isGranted('IS_AUTHENTICATED_FULLY')) {
                        
            $event->setController($this->resolver->getController($request));						
            $request->attributes->set('_controller', 'FrameworkBundle:Redirect:Redirect');
            $request->attributes->set('_route', $this->router->generate('fos_user_security_login'));
            $event->setController($this->resolver->getController($request));						                        
        }
        */        
        
        //$_SESSION['routes'][]=explode('/',$routeName)[1];  
        
        $tokens=explode('/',$routeName);
        $route=end($tokens);
        
        switch ($route){
            case 'body':                
                //Obtener parámetros de filtros
                $componente= $request->query->get('componente');
                $nombreComponente= $request->query->get('nombreComponente');
                $anio= $request->query->get('anio');
                $mes= $request->query->get('mes');
                $estado= $request->query->get('estado');                
                $resetFiltros = $request->query->get('resetFiltros');                
                //////////////////  
                //Setear filtros en la sesion        
                // Si se estan reseteando los filtros limpiar las variables de la sesion                               
                if($resetFiltros){
                    $this->session->set('filtroComponente',null);    
                    $this->session->set('nombreComponente','SIGGES');    
                    $this->session->set('filtroAnio',null);
                    $this->session->set('filtroMes',null);
                    $this->session->set('filtroEstado',null);        
                }
                else{            
                    $this->session->set('filtroComponente',$componente);        
                    $this->session->set('nombreComponente',$nombreComponente);    
                    $this->session->set('filtroAnio',$anio);
                    $this->session->set('filtroMes',$mes);
                    $this->session->set('filtroEstado',$estado);        
                }
                ////////////////////////////
                break;
            case 'dashboard':
                $this->session->set('active', 'dashboard_index');
                break;            
            case 'incidencia':
                $this->session->set('active', 'incidencia_index');                                                                                    
                break;            
            case 'mantencion':                
                $this->session->set('active', 'mantencion_index');                                
                break;            
        }
        
        if ($this->tokenStorage->getToken() != null && explode('/',$routeName)[1] == "login" &&
            $this->security->isGranted('IS_AUTHENTICATED_FULLY')) {
            /*          
            $event->setController($this->resolver->getController($request));						
            $request->attributes->set('_controller', 'MonitorBundle:Dashboard:index');
            $request->attributes->set('_route', $this->router->generate('dashboard_index'));
            $event->setController($this->resolver->getController($request));	
            */
            $this->tokenStorage->setToken(null);
            $request->getSession()->invalidate();
        }
          
        /*
        if ($this->tokenStorage->getToken() != null && in_array(explode('/',$routeName)[1],["servicio","new","assign","edit"]) &&
            !$this->security->isGranted('ROLE_ADMIN')) {
            $this->tokenStorage->setToken(null);
            $request->getSession()->invalidate();
        }
        
        if ($this->tokenStorage->getToken() != null && in_array(explode('/',$routeName)[1],["dashboard"]) &&
            $this->security->isGranted('ROLE_ADMIN')) {
            $this->tokenStorage->setToken(null);
            $request->getSession()->invalidate();
        }
        */
    }
}