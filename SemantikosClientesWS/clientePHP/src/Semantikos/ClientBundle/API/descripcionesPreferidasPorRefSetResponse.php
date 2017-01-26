<?php

namespace Semantikos\ClientBundle\API;

class descripcionesPreferidasPorRefSetResponse
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
     * @return descripcionesPreferidasPorRefSetResponse
     */
    public function setRespuestaConceptosPorRefSet($respuestaConceptosPorRefSet)
    {
      $this->respuestaConceptosPorRefSet = $respuestaConceptosPorRefSet;
      return $this;
    }

}
