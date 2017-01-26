<?php

namespace Semantikos\ClientBundle\API;

class buscarTerminoResponse
{

    /**
     * @var RespuestaBuscarTerminoGenerica $respuestaBuscarTermino
     */
    protected $respuestaBuscarTermino = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return RespuestaBuscarTerminoGenerica
     */
    public function getRespuestaBuscarTermino()
    {
      return $this->respuestaBuscarTermino;
    }

    /**
     * @param RespuestaBuscarTerminoGenerica $respuestaBuscarTermino
     * @return buscarTerminoResponse
     */
    public function setRespuestaBuscarTermino($respuestaBuscarTermino)
    {
      $this->respuestaBuscarTermino = $respuestaBuscarTermino;
      return $this;
    }

}
