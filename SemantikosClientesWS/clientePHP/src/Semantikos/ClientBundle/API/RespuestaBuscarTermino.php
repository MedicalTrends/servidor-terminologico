<?php

namespace Semantikos\ClientBundle\API;

class RespuestaBuscarTermino
{

    /**
     * @var conceptos $conceptos
     */
    protected $conceptos = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return conceptos
     */
    public function getConceptos()
    {
      return $this->conceptos;
    }

    /**
     * @param conceptos $conceptos
     * @return RespuestaBuscarTermino
     */
    public function setConceptos($conceptos)
    {
      $this->conceptos = $conceptos;
      return $this;
    }

}
