<?php

namespace Semantikos\ClientBundle\API;

class obtenerTerminosPediblesResponse
{

    /**
     * @var RespuestaBuscarTermino $respuestaBuscarTermino
     */
    protected $respuestaBuscarTermino = null;

    /**
     * @param RespuestaBuscarTermino $respuestaBuscarTermino
     */
    public function __construct($respuestaBuscarTermino)
    {
      $this->respuestaBuscarTermino = $respuestaBuscarTermino;
    }

    /**
     * @return RespuestaBuscarTermino
     */
    public function getRespuestaBuscarTermino()
    {
      return $this->respuestaBuscarTermino;
    }

    /**
     * @param RespuestaBuscarTermino $respuestaBuscarTermino
     * @return obtenerTerminosPediblesResponse
     */
    public function setRespuestaBuscarTermino($respuestaBuscarTermino)
    {
      $this->respuestaBuscarTermino = $respuestaBuscarTermino;
      return $this;
    }

}
