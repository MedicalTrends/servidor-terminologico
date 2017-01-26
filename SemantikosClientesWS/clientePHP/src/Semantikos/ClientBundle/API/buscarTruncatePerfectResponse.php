<?php

namespace Semantikos\ClientBundle\API;

class buscarTruncatePerfectResponse
{

    /**
     * @var RespuestaConceptosPorCategoria $respuestaConceptos
     */
    protected $respuestaConceptos = null;

    
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
     * @return buscarTruncatePerfectResponse
     */
    public function setRespuestaConceptos($respuestaConceptos)
    {
      $this->respuestaConceptos = $respuestaConceptos;
      return $this;
    }

}
