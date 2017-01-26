<?php
namespace Semantikos\ClientBundle\Helper\Relationship;

use Symfony\Component\Form\FormFactoryInterface;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\FormType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\HttpFoundation\Session\Session;

use Semantikos\ClientBundle\Entity\Category;
use Semantikos\ClientBundle\Entity\RefSet;
use Symfony\Component\Form\FormEvents;

use Doctrine\ORM\EntityManager;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;
 

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class RelationshipServiceFormsHelper {
    //put your code here
    protected $formFactory;
    protected $em;
    protected $session;
    private $container;

    public function __construct(FormFactoryInterface $formFactory, EntityManager $em, Session $session, Container $container=null)
    {
        $this->formFactory = $formFactory;
        $this->em = $em;
        $this->session = $session;
        $this->container = $container;
    }
        
    public function getWS006Form(){                                
        
        return $ws006Form = $this->formFactory->createNamedBuilder('ws006', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                    
            ->add('termino', TextType::class)            
            ->add('categorias', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "Ingrese las Categorías separadas por ','")  
            ))
            ->add('refSets', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "Ingrese los RefSets separados por ','")
            ))             
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }    
    
    public function getWS010Form(){                                
        
        return $ws010Form = $this->formFactory->createNamedBuilder('ws010', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Básico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Básico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS010_01Form(){                                
        
        return $ws010_01Form = $this->formFactory->createNamedBuilder('ws010_01', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Básico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Básico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS011Form(){                                
        
        return $ws011Form = $this->formFactory->createNamedBuilder('ws011', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Básico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Básico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS011_01Form(){                                
        
        return $ws011_01Form = $this->formFactory->createNamedBuilder('ws011_01', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Básico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Básico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS012Form(){                                                
        
        return $ws012Form = $this->formFactory->createNamedBuilder('ws012', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Clínico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Clínico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS012_01Form(){                                
        
        return $ws012_01Form = $this->formFactory->createNamedBuilder('ws012_01', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Clínico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Clínico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS013Form(){                                                
        
        return $ws013Form = $this->formFactory->createNamedBuilder('ws013', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Clínico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Clínico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS013_01Form(){                                
        
        return $ws013_01Form = $this->formFactory->createNamedBuilder('ws013_01', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Clínico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Clínico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS014Form(){                                                
        
        return $ws014Form = $this->formFactory->createNamedBuilder('ws014', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Clínico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Clínico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS014_01Form(){                                
        
        return $ws014_01Form = $this->formFactory->createNamedBuilder('ws014_01', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('relationship_call'))                
            ->setMethod('POST')                                      
            ->add('descriptionId', TextType::class, array(
                  'required' => false,                
                  'attr' => array( 'placeholder' => "ID de Descripción de Medicamento Clínico")  
            ))
            ->add('conceptId', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "ID Concepto de Medicamento Clínico")
            ))             
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    
}                
