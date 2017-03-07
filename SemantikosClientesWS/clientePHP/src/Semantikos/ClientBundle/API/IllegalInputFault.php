<?php

namespace Semantikos\ClientBundle\API;

class IllegalInputFault
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
     * @return IllegalInputFault
     */
    public function setMessage($message)
    {
      $this->message = $message;
      return $this;
    }

}
