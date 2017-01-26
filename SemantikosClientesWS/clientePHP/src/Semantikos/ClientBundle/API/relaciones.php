<?php

namespace Semantikos\ClientBundle\API;

class relaciones
{

    /**
     * @var Relacion $relacion
     */
    protected $relacion = null;

    /**
     * @param Relacion $relacion
     */
    public function __construct($relacion)
    {
      $this->relacion = $relacion;
    }

    /**
     * @return Relacion
     */
    public function getRelacion()
    {
      return $this->relacion;
    }

    /**
     * @param Relacion $relacion
     * @return relaciones
     */
    public function setRelacion($relacion)
    {
      $this->relacion = $relacion;
      return $this;
    }

}
