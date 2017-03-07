<?php

namespace Semantikos\ClientBundle\API;

class descripcionesNoValidas
{

    /**
     * @var DescripcionNoValida $descripcionNoValida
     */
    protected $descripcionNoValida = null;

    /**
     * @param DescripcionNoValida $descripcionNoValida
     */
    public function __construct($descripcionNoValida)
    {
      $this->descripcionNoValida = $descripcionNoValida;
    }

    /**
     * @return DescripcionNoValida
     */
    public function getDescripcionNoValida()
    {
      return $this->descripcionNoValida;
    }

    /**
     * @param DescripcionNoValida $descripcionNoValida
     * @return descripcionesNoValidas
     */
    public function setDescripcionNoValida($descripcionNoValida)
    {
      $this->descripcionNoValida = $descripcionNoValida;
      return $this;
    }

}
