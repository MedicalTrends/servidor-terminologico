<?php

namespace Semantikos\ClientBundle\API;

class ColumnaTablaAuxiliar
{

    /**
     * @var string $nombreColumna
     */
    protected $nombreColumna = null;

    /**
     * @var boolean $esLlavePrimaria
     */
    protected $esLlavePrimaria = null;

    /**
     * @var boolean $esBuscable
     */
    protected $esBuscable = null;

    /**
     * @var boolean $esMostrable
     */
    protected $esMostrable = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getNombreColumna()
    {
      return $this->nombreColumna;
    }

    /**
     * @param string $nombreColumna
     * @return ColumnaTablaAuxiliar
     */
    public function setNombreColumna($nombreColumna)
    {
      $this->nombreColumna = $nombreColumna;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsLlavePrimaria()
    {
      return $this->esLlavePrimaria;
    }

    /**
     * @param boolean $esLlavePrimaria
     * @return ColumnaTablaAuxiliar
     */
    public function setEsLlavePrimaria($esLlavePrimaria)
    {
      $this->esLlavePrimaria = $esLlavePrimaria;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsBuscable()
    {
      return $this->esBuscable;
    }

    /**
     * @param boolean $esBuscable
     * @return ColumnaTablaAuxiliar
     */
    public function setEsBuscable($esBuscable)
    {
      $this->esBuscable = $esBuscable;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsMostrable()
    {
      return $this->esMostrable;
    }

    /**
     * @param boolean $esMostrable
     * @return ColumnaTablaAuxiliar
     */
    public function setEsMostrable($esMostrable)
    {
      $this->esMostrable = $esMostrable;
      return $this;
    }

}
