<?php

namespace Semantikos\ClientBundle\API;

class PeticionSugerenciasDeDescripciones
{

    /**
     * @var string $termino
     */
    protected $termino = null;

    /**
     * @var string[] $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var string[] $nombreRefSet
     */
    protected $nombreRefSet = null;

    /**
     * @param string $termino
     */
    public function __construct()
    {
      
    }

    /**
     * @return string
     */
    public function getTermino()
    {
      return $this->termino;
    }

    /**
     * @param string $termino
     * @return PeticionSugerenciasDeDescripciones
     */
    public function setTermino($termino)
    {
      $this->termino = $termino;
      return $this;
    }

    /**
     * @return string[]
     */
    public function getNombreCategoria()
    {
      return $this->nombreCategoria;
    }

    /**
     * @param string[] $nombreCategoria
     * @return PeticionSugerenciasDeDescripciones
     */
    public function setNombreCategoria(array $nombreCategoria = null)
    {
      $this->nombreCategoria = $nombreCategoria;
      return $this;
    }

    /**
     * @return string[]
     */
    public function getNombreRefSet()
    {
      return $this->nombreRefSet;
    }

    /**
     * @param string[] $nombreRefSet
     * @return PeticionSugerenciasDeDescripciones
     */
    public function setNombreRefSet(array $nombreRefSet = null)
    {
      $this->nombreRefSet = $nombreRefSet;
      return $this;
    }

}
