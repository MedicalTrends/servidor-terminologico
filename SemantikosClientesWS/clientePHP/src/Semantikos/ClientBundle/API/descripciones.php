<?php

namespace Semantikos\ClientBundle\API;

class descripciones
{

    /**
     * @var DescripcionSimplificada $descripcion
     */
    protected $descripcion = null;

    /**
     * @param DescripcionSimplificada $descripcion
     */
    public function __construct($descripcion)
    {
      $this->descripcion = $descripcion;
    }

    /**
     * @return DescripcionSimplificada
     */
    public function getDescripcion()
    {
      return $this->descripcion;
    }

    /**
     * @param DescripcionSimplificada $descripcion
     * @return descripciones
     */
    public function setDescripcion($descripcion)
    {
      $this->descripcion = $descripcion;
      return $this;
    }

}
