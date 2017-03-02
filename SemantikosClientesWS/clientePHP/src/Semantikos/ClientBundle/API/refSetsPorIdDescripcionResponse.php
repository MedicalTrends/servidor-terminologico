<?php

namespace Semantikos\ClientBundle\API;

class refSetsPorIdDescripcionResponse
{

    /**
     * @var RespuestaRefSets $refsetResponse
     */
    protected $refsetResponse = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return RespuestaRefSets
     */
    public function getRefsetResponse()
    {
      return $this->refsetResponse;
    }

    /**
     * @param RespuestaRefSets $refsetResponse
     * @return refSetsPorIdDescripcionResponse
     */
    public function setRefsetResponse($refsetResponse)
    {
      $this->refsetResponse = $refsetResponse;
      return $this;
    }

}
