<?php

namespace Semantikos\ClientBundle\API;

class conceptosPorCategoriaResponse
{

    /**
     * @var RespuestaConceptosPorCategoria $respuestaConceptos
     */
    public $respuestaConceptos = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return RespuestaConceptosPorCategoria
     */
    public function getRespuestaConceptos()
    {
      return $this->respuestaConceptos;
    }

    /**
     * @param RespuestaConceptosPorCategoria $respuestaConceptos
     * @return conceptosPorCategoriaResponse
     */
    public function setRespuestaConceptos($respuestaConceptos)
    {
      $this->respuestaConceptos = $respuestaConceptos;
      return $this;
    }

}
