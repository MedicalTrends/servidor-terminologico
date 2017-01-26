<?php

namespace Semantikos\ClientBundle\API;

class conceptoPorIdDescripcion
{

    /**
     * @var string $idDescripcion
     */
    protected $idDescripcion = null;

    /**
     * @param string $idDescripcion
     */
    public function __construct($idDescripcion)
    {
      $this->idDescripcion = $idDescripcion;
    }

    /**
     * @return string
     */
    public function getIdDescripcion()
    {
      return $this->idDescripcion;
    }

    /**
     * @param string $idDescripcion
     * @return conceptoPorIdDescripcion
     */
    public function setIdDescripcion($idDescripcion)
    {
      $this->idDescripcion = $idDescripcion;
      return $this;
    }

}
