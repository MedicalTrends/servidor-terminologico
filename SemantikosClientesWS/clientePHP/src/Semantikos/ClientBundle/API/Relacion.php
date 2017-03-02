<?php

namespace Semantikos\ClientBundle\API;

class Relacion
{

    /**
     * @var Objetivo $objetivo
     */
    protected $objetivo = null;

    /**
     * @var DefinicionRelacion $definicion
     */
    protected $definicion = null;

    /**
     * @var Concepto $concepto
     */
    protected $concepto = null;

    /**
     * @var atributos $atributos
     */
    protected $atributos = null;

    /**
     * @param Objetivo $objetivo
     * @param Concepto $concepto
     */
    public function __construct($objetivo, $concepto)
    {
      $this->objetivo = $objetivo;
      $this->concepto = $concepto;
    }

    /**
     * @return Objetivo
     */
    public function getObjetivo()
    {
      return $this->objetivo;
    }

    /**
     * @param Objetivo $objetivo
     * @return Relacion
     */
    public function setObjetivo($objetivo)
    {
      $this->objetivo = $objetivo;
      return $this;
    }

    /**
     * @return DefinicionRelacion
     */
    public function getDefinicion()
    {
      return $this->definicion;
    }

    /**
     * @param DefinicionRelacion $definicion
     * @return Relacion
     */
    public function setDefinicion($definicion)
    {
      $this->definicion = $definicion;
      return $this;
    }

    /**
     * @return Concepto
     */
    public function getConcepto()
    {
      return $this->concepto;
    }

    /**
     * @param Concepto $concepto
     * @return Relacion
     */
    public function setConcepto($concepto)
    {
      $this->concepto = $concepto;
      return $this;
    }

    /**
     * @return atributos
     */
    public function getAtributos()
    {
      return $this->atributos;
    }

    /**
     * @param atributos $atributos
     * @return Relacion
     */
    public function setAtributos($atributos)
    {
      $this->atributos = $atributos;
      return $this;
    }

}
