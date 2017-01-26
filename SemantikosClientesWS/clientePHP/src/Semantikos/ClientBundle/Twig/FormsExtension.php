<?php
namespace Semantikos\ClientBundle\Twig;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;
use Semantikos\ClientBundle\Helper\SearchServiceFormsHelper;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of GlobalFiltersExtension
 *
 * @author diego
 */
class FormsExtension extends \Twig_Extension {
    //put your code here
    private $container;
    public function __construct(Container $container=null)
    {
        $this->container = $container;
        //var_dump ($container); exit; //  prints null !!!
    } 
    
     protected function get($service)
    {
        return $this->container->get($service);
    }
        
    public function getGlobals()
    {
        return array(
            "SearchForms" => array( "ws001" => $this->container->get("client.helper.search_forms")->getWS001Form(),
                                    "ws002" => $this->container->get("client.helper.search_forms")->getWS002Form(),
                                    "ws004" => $this->container->get("client.helper.search_forms")->getWS004Form(), 
                                    "ws005" => $this->container->get("client.helper.search_forms")->getWS005Form(), 
                                    "ws007" => $this->container->get("client.helper.search_forms")->getWS007Form(),
                                    "ws008" => $this->container->get("client.helper.search_forms")->getWS008Form(),
                                    "ws009" => $this->container->get("client.helper.search_forms")->getWS009Form(),
                                    "ws022" => $this->container->get("client.helper.search_forms")->getWS022Form(),
                                    "ws023" => $this->container->get("client.helper.search_forms")->getWS023Form(),
                                    "ws024" => $this->container->get("client.helper.search_forms")->getWS024Form(),
                                    "ws025" => $this->container->get("client.helper.search_forms")->getWS025Form(),
                                    "ws026" => $this->container->get("client.helper.search_forms")->getWS026Form(),
                                    "ws027" => $this->container->get("client.helper.search_forms")->getWS027Form(),
                                    "ws028" => $this->container->get("client.helper.search_forms")->getWS028Form(),
                            ),
            "EntranceForms" => array( "ws030" => $this->container->get("client.helper.entrance_forms")->getWS030Form(),
                                      "ws031" => $this->container->get("client.helper.entrance_forms")->getWS031Form(),
            ),
            "RelationshipForms" => array( "ws006" => $this->container->get("client.helper.relationship_forms")->getWS006Form(),                                      
                                          "ws010" => $this->container->get("client.helper.relationship_forms")->getWS010Form(),
                                          "ws010_01" => $this->container->get("client.helper.relationship_forms")->getWS010_01Form(),
                                          "ws011" => $this->container->get("client.helper.relationship_forms")->getWS011Form(),
                                          "ws011_01" => $this->container->get("client.helper.relationship_forms")->getWS011_01Form(),
                                          "ws012" => $this->container->get("client.helper.relationship_forms")->getWS012Form(),
                                          "ws012_01" => $this->container->get("client.helper.relationship_forms")->getWS012_01Form(),
                                          "ws013" => $this->container->get("client.helper.relationship_forms")->getWS013Form(),
                                          "ws013_01" => $this->container->get("client.helper.relationship_forms")->getWS013_01Form(),
                                          "ws014" => $this->container->get("client.helper.relationship_forms")->getWS013Form(),
                                          "ws014_01" => $this->container->get("client.helper.relationship_forms")->getWS013_01Form(),
            )
        );
    }

    public function getName()
    {
        return 'Forms_extension';
    }
    
     public function getForms()
     {
        return array(
            new \Twig_Filter_Function('SearchForms', array($this, 'SearchForms')),
            new \Twig_Filter_Function('EntranceForms', array($this, 'EntranceForms')),
            new \Twig_Filter_Function('RelationshipForms', array($this, 'RelationshipForms')),
        );
    } 
}
