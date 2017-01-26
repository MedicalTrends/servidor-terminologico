<?php

namespace Semantikos\ClientBundle\EventListener;

use Symfony\Component\Security\Http\Authentication\AuthenticationSuccessHandlerInterface;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\SecurityContext;
use Symfony\Component\Security\Core\Authorization\AuthorizationChecker;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorage;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Router;

class LoginSuccessHandler implements AuthenticationSuccessHandlerInterface
{
	
	protected $router;
	protected $security;
        protected $tokenStorage;
	
	public function __construct(Router $router, AuthorizationChecker $security, TokenStorage $tokenStorage)
	{
            $this->router = $router;
            $this->security = $security;
            $this->tokenStorage = $tokenStorage;
	}
	
	public function onAuthenticationSuccess(Request $request, TokenInterface $token)
	{                                           
            
            //session_start();            
                        
            if ($this->security->isGranted('ROLE_SUPER_ADMIN'))
            {
                    // $response = new RedirectResponse($this->router->generate('algo'));
                    $response = new Response("NO PERMITIDO");
                    //SE DESLOGEA
                    //$this->security->setToken(null);
                    $this->tokenStorage->setToken(null);
                    $request->getSession()->invalidate();
            }
            else if ($this->security->isGranted('ROLE_USER') && !$this->security->isGranted('ROLE_ADMIN'))
            {                                    
                $response = new RedirectResponse($this->router->generate('search_index'));
            } 
            
            return $response;
	}
	
}