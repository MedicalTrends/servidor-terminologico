<?php

namespace Semantikos\ClientBundle\API;

class DefinicionRelacionAtributo
{

    /**
     * @var string $name
     */
    protected $name = null;

    /**
     * @var Multiplicidad $multiplicidad
     */
    protected $multiplicidad = null;

    /**
     * @var DefinicionObjetivo $definicionObjetivo
     */
    protected $definicionObjetivo = null;

    /**
     * @param Multiplicidad $multiplicidad
     * @param DefinicionObjetivo $definicionObjetivo
     */
    public function __construct($multiplicidad, $definicionObjetivo)
    {
      $this->multiplicidad = $multiplicidad;
      $this->definicionObjetivo = $definicionObjetivo;
    }

    /**
     * @return string
     */
    public function getName()
    {
      return $this->name;
    }

    /**
     * @param string $name
     * @return DefinicionRelacionAtributo
     */
    public function setName($name)
    {
      $this->name = $name;
      return $this;
    }

    /**
     * @return Multiplicidad
     */
    public function getMultiplicidad()
    {
      return $this->multiplicidad;
    }

    /**
     * @param Multiplicidad $multiplicidad
     * @return DefinicionRelacionAtributo
     */
    public function setMultiplicidad($multiplicidad)
    {
      $this->multiplicidad = $multiplicidad;
      return $this;
    }

    /**
     * @return DefinicionObjetivo
     */
    public function getDefinicionObjetivo()
    {
      return $this->definicionObjetivo;
    }

    /**
     * @param DefinicionObjetivo $definicionObjetivo
     * @return DefinicionRelacionAtributo
     */
    public function setDefinicionObjetivo($definicionObjetivo)
    {
      $this->definicionObjetivo = $definicionObjetivo;
      return $this;
    }

}
