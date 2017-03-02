<?php

namespace Semantikos\ClientBundle\API;

class NotFoundFault
{

    /**
     * @var string $message
     */
    protected $message = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getMessage()
    {
      return $this->message;
    }

    /**
     * @param string $message
     * @return NotFoundFault
     */
    public function setMessage($message)
    {
      $this->message = $message;
      return $this;
    }

}
