<?php

namespace Semantikos\ClientBundle\EventListener;

use Symfony\Component\HttpKernel\Event\GetResponseEvent;
use Symfony\Component\HttpKernel\HttpKernel;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Router;
use Symfony\Component\Security\Core\SecurityContext;
use Symfony\Component\Security\Core\Authorization\AuthorizationChecker;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorage;

use Symfony\Component\Security\Core\Authentication\AuthenticationTrustResolver;

use Symfony\Component\Security\Core\Authorization\Voter\AuthenticatedVoter;


class RequestListener
{
    protected $router;
    protected $security;    
    protected $tokenStorage;
 
    public function __construct(Router $router, AuthorizationChecker $security, TokenStorage $tokenStorage = null) {
        $this->router = $router;
        $this->security = $security;   
        $this->tokenStorage = $tokenStorage;
    }
        
    public function onKernelRequest(GetResponseEvent $event)
    {                        
        
        if (HttpKernel::MASTER_REQUEST != $event->getRequestType()) {
            // don't do anything if it's not the master request
            return;
        }        
                                
        $request = $event->getRequest();
        $routeName = $request->attributes->get('_route');                    
        
        /*
        if( $this->tokenStorage->getToken() == null && $routeName != "fos_user_security_login"){
            $url = $this->router->generate('fos_user_security_login');
            $event->setResponse(new RedirectResponse($url));
        }
                        
        if ($this->tokenStorage->getToken() != null && $routeName != "fos_user_security_login" && 
            !$this->security->isGranted('IS_AUTHENTICATED_FULLY')) {
            $url = $this->router->generate('fos_user_security_login');
            $event->setResponse(new RedirectResponse($url));
        }
        */                
        
        if($routeName === "fos_user_security_login" && $this->security->isGranted('ROLE_USER') 
            && !$this->security->isGranted('ROLE_ADMIN')){
            $url = $this->router->generate('search_index');
            $event->setResponse(new RedirectResponse($url));
        }
        
    }
}