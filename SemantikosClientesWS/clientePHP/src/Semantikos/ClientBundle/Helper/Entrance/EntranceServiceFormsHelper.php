<?php
namespace Semantikos\ClientBundle\Helper\Entrance;

use Symfony\Component\Form\FormFactoryInterface;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FormType;
use Symfony\Component\HttpFoundation\Session\Session;

use Doctrine\ORM\EntityManager;

use Symfony\Component\DependencyInjection\ContainerInterface as Container;
 

/**
 * Description of FiltersHelper
 *
 * @author diego
 */
class EntranceServiceFormsHelper {
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
        
    public function getWS030Form(){                                
        
        return $ws030Form = $this->formFactory->createNamedBuilder('ws030', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('entrance_call'))                
            ->setMethod('POST')                    
            ->add('descriptionId', TextType::class)                        
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }    
    
    public function getWS031Form(){                                
        
        return $ws031Form = $this->formFactory->createNamedBuilder('ws031', FormType::class, null)                      
            ->setAction($this->container->get('router')->generate('entrance_call'))                
            ->setMethod('POST')                    
            ->add('termino', TextType::class)                        
            ->add('categoriaPropuesta', TextType::class)
            ->add('nombreProfesional', TextType::class)
            ->add('mail', EmailType::class)
            ->add('profesion', TextType::class)
            ->add('especialidad', TextType::class)
            ->add('subespecialidad', TextType::class)
            ->add('establecimiento', TextType::class)
            ->add('sensibleMayusculas', ChoiceType::class, array(
                  'choices' => array('Si' => true, 'No' => false), 
                  'choices_as_values' => true, 
                  'expanded' => true,
                  'data' => false                  
            ))                   
            ->add('observacion', TextareaType::class)
            ->add('tipoDescripcionPropuesta', TextType::class)
            ->add('call', SubmitType::class, array('label' => 'Invocar WS', 'attr' => array('class' => 'btn btn-primary')))                                  
            ->getForm()->createView();                    
    }       
    
}                
