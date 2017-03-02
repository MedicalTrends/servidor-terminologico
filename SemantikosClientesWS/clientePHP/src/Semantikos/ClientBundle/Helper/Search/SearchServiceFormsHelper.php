<?php
namespace Semantikos\ClientBundle\Helper\Search;

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
class SearchServiceFormsHelper {
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
        
    public function getWS001Form(){                                
        
        return $ws001Form = $this->formFactory->createNamedBuilder('ws001', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('search_call'))                
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
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }
    
    public function getWS002Form(){                                
        
        return $ws002Form = $this->formFactory->createNamedBuilder('ws002',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')            
            ->add('nombreCategoria', TextType::class)            
            ->add('idEstablecimiento', NumberType::class)                              
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))
            ->getForm()->createView();                    
    }
    
    public function getWS004Form(){                                
        
        return $ws004Form = $this->formFactory->createNamedBuilder('ws004',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
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
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS005Form(){                                
        
        return $ws005Form = $this->formFactory->createNamedBuilder('ws005',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')                    
            ->add('pedible', ChoiceType::class, array(
                  'choices' => array('Si' => 'Si', 'No' => 'No'), 
                  'choices_as_values' => true, 
                  'expanded' => true,
                  'data' => 'Si'                  
            ))
            ->add('categorias', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "Ingrese las Categorías separadas por ','")
            ))
            ->add('refSets', TextType::class, array(
                  'required' => false,
                  'attr' => array( 'placeholder' => "Ingrese los RefSets separados por ','")
            )) 
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS007Form(){                                
        
        return $ws004Form = $this->formFactory->createNamedBuilder('ws007',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')                    
            ->add('descriptionId', TextType::class, array('required' => false))
            ->add('incluyeEstablecimiento', ChoiceType::class, array(
                  'choices' => array('Si' => true, 'No' => false), 
                  'choices_as_values' => true, 
                  'expanded' => true,
                  'data' => true                  
            ))            
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS008Form(){                                
        
        return $ws004Form = $this->formFactory->createNamedBuilder('ws008',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')                                
            ->add('incluyeEstablecimiento', ChoiceType::class, array(
                  'choices' => array('Si' => true, 'No' => false), 
                  'choices_as_values' => true, 
                  'expanded' => true,
                  'data' => true                  
            ))            
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS009Form(){                                
        
        return $ws004Form = $this->formFactory->createNamedBuilder('ws009',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('descriptionIds', TextType::class, array( 
                  'attr' => array( 'placeholder' => "Ingrese las DescriptionIDs separados por ','") 
            ))
            ->add('incluyeEstablecimiento', ChoiceType::class, array(
                  'choices' => array('Si' => true, 'No' => false), 
                  'choices_as_values' => true, 
                  'expanded' => true,
                  'data' => true                  
            ))            
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS022Form(){                                
        
        return $ws022Form = $this->formFactory->createNamedBuilder('ws022',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('refSet', TextType::class)                           
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS023Form(){                                
        
        return $ws023Form = $this->formFactory->createNamedBuilder('ws023',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('refSet', TextType::class)                           
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS024Form(){                                
        
        return $ws024Form = $this->formFactory->createNamedBuilder('ws024',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')                     
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS025Form(){                                
        
        return $ws024Form = $this->formFactory->createNamedBuilder('ws025',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('nombreAbreviadoCrossmapSet', TextType::class)    
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
    
    public function getWS026Form(){                                
        
        return $ws026Form = $this->formFactory->createNamedBuilder('ws026',FormType::class, null)
            
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('descriptionId', TextType::class, array( 
                  'required' => false           
            ))    
            ->add('conceptId', TextType::class, array( 
                  'required' => false
            ))
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))              
            ->getForm()->createView();                    
    }
    
    public function getWS027Form(){                                
        
        return $ws027Form = $this->formFactory->createNamedBuilder('ws027',FormType::class, null)
            
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('descriptionId', TextType::class, array( 
                  'required' => false           
            ))    
            ->add('conceptId', TextType::class, array( 
                  'required' => false
            ))
            ->add('idEstablecimiento', NumberType::class)            
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))              
            ->getForm()->createView();                    
    }
    
    public function getWS028Form(){                                
        
        return $ws028Form = $this->formFactory->createNamedBuilder('ws028',FormType::class, null)
            ->setAction($this->container->get('router')->generate('search_call'))
            ->setMethod('POST')         
            ->add('descriptionId', TextType::class)    
            ->add('idEstablecimiento', NumberType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))  
            ->getForm()->createView();                    
    }
}                
