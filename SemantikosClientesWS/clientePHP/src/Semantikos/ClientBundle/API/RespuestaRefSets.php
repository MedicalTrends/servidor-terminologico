<?php

namespace Semantikos\ClientBundle\API;

class RespuestaRefSets
{

    /**
     * @var refsets $refsets
     */
    protected $refsets = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return refsets
     */
    public function getRefsets()
    {
      return $this->refsets;
    }

    /**
     * @param refsets $refsets
     * @return RespuestaRefSets
     */
    public function setRefsets($refsets)
    {
      $this->refsets = $refsets;
      return $this;
    }            

}
