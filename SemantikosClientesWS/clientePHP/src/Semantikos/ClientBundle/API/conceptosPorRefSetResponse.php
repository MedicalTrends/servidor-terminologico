<?php

namespace Semantikos\ClientBundle\API;

class conceptosPorRefSetResponse
{

    /**
     * @var RespuestaConceptosPorRefSet $respuestaConceptosPorRefSet
     */
    protected $respuestaConceptosPorRefSet = null;

    /**
     * @param RespuestaConceptosPorRefSet $respuestaConceptosPorRefSet
     */
    public function __construct($respuestaConceptosPorRefSet)
    {
      $this->respuestaConceptosPorRefSet = $respuestaConceptosPorRefSet;
    }

    /**
     * @return RespuestaConceptosPorRefSet
     */
    public function getRespuestaConceptosPorRefSet()
    {
      return $this->respuestaConceptosPorRefSet;
    }

    /**
     * @param RespuestaConceptosPorRefSet $respuestaConceptosPorRefSet
     * @return conceptosPorRefSetResponse
     */
    public function setRespuestaConceptosPorRefSet($respuestaConceptosPorRefSet)
    {
      $this->respuestaConceptosPorRefSet = $respuestaConceptosPorRefSet;
      return $this;
    }

}
