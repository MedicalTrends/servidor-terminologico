<?php

namespace Semantikos\ClientBundle\API;

class DefinicionRelacion
{

    /**
     * @var string $name
     */
    protected $name = null;

    /**
     * @var string $description
     */
    protected $description = null;

    /**
     * @var Multiplicidad $multiplicidad
     */
    protected $multiplicidad = null;

    /**
     * @var DefinicionObjetivo $definicionObjetivo
     */
    protected $definicionObjetivo = null;

    /**
     * @var DefinicionRelacion $definicionRelacionExcluida
     */
    protected $definicionRelacionExcluida = null;

    /**
     * @var definicionesAtributoRelacion $definicionesAtributoRelacion
     */
    protected $definicionesAtributoRelacion = null;

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
     * @return DefinicionRelacion
     */
    public function setName($name)
    {
      $this->name = $name;
      return $this;
    }

    /**
     * @return string
     */
    public function getDescription()
    {
      return $this->description;
    }

    /**
     * @param string $description
     * @return DefinicionRelacion
     */
    public function setDescription($description)
    {
      $this->description = $description;
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
     * @return DefinicionRelacion
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
     * @return DefinicionRelacion
     */
    public function setDefinicionObjetivo($definicionObjetivo)
    {
      $this->definicionObjetivo = $definicionObjetivo;
      return $this;
    }

    /**
     * @return DefinicionRelacion
     */
    public function getDefinicionRelacionExcluida()
    {
      return $this->definicionRelacionExcluida;
    }

    /**
     * @param DefinicionRelacion $definicionRelacionExcluida
     * @return DefinicionRelacion
     */
    public function setDefinicionRelacionExcluida($definicionRelacionExcluida)
    {
      $this->definicionRelacionExcluida = $definicionRelacionExcluida;
      return $this;
    }

    /**
     * @return definicionesAtributoRelacion
     */
    public function getDefinicionesAtributoRelacion()
    {
      return $this->definicionesAtributoRelacion;
    }

    /**
     * @param definicionesAtributoRelacion $definicionesAtributoRelacion
     * @return DefinicionRelacion
     */
    public function setDefinicionesAtributoRelacion($definicionesAtributoRelacion)
    {
      $this->definicionesAtributoRelacion = $definicionesAtributoRelacion;
      return $this;
    }

}
