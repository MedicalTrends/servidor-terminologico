<?php

namespace Semantikos\ClientBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * RequestStatus  
 */
class RequestStatus
{
    /**
     * @var boolean
     *        
     */
    protected $error;
        
    /**
     * @var string
     *     
     */
    protected $message;


    /**
     * Get error
     *
     * @return boolean
     */
    public function isError()
    {
        return $this->error;
    }
    
    /**
     * Set error
     * 
     * @param boolean $error
     * 
     * @return RequestStatus   
     */
    public function setError($error = false)
    {
        $this->error = $error;
        
        return $this;
    }
    
    /**
     * Get message
     *
     * @return string
     */
    public function getMessage()
    {
        return $this->message;
    }
    
    /**
     * Set message
     *     
     * @param string $message
     * 
     * @return RequestStatus   
     */
    public function setMessage($message = false)
    {
        $this->message = $message;
        
        return $this;
    }
}

