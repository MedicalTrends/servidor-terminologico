<?php
// src/AppBundle/DataFixtures/ORM/LoadUserData.php

namespace AppBundle\DataFixtures\ORM;

use Doctrine\Common\DataFixtures\FixtureInterface;
use Doctrine\Common\Persistence\ObjectManager;
use Semantikos\ClientBundle\Entity\Category;
use Semantikos\ClientBundle\Entity\RefSet;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

use Symfony\Component\Finder\Finder;

class LoadFixtures extends Controller implements FixtureInterface
{
    public function load(ObjectManager $manager)
    {        
        $generator = new \Wsdl2PhpGenerator\Generator();
        $generator->generate(
            new \Wsdl2PhpGenerator\Config(array(
                'inputFile' => 'http://192.168.0.226:8080/ws/ServicioDeRelacionados?wsdl',
                'outputDir' => 'C:\xampp\htdocs\output',
                'soapClientClass' => '\BeSimple\SoapClient\SoapClient'
                //'namespaceName' => 'http://service.ws.semantikos.minsal.cl/'
            ))
        );
    }
}