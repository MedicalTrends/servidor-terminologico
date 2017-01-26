<?php

namespace Semantikos\ClientBundle\API;

class descripcionesSugeridas
{

    /**
     * @var DescripcionPerfectMatch $descripcionSugerida
     */
    protected $descripcionSugerida = null;

    /**
     * @param DescripcionPerfectMatch $descripcionSugerida
     */
    public function __construct($descripcionSugerida)
    {
      $this->descripcionSugerida = $descripcionSugerida;
    }

    /**
     * @return DescripcionPerfectMatch
     */
    public function getDescripcionSugerida()
    {
      return $this->descripcionSugerida;
    }

    /**
     * @param DescripcionPerfectMatch $descripcionSugerida
     * @return descripcionesSugeridas
     */
    public function setDescripcionSugerida($descripcionSugerida)
    {
      $this->descripcionSugerida = $descripcionSugerida;
      return $this;
    }

}
