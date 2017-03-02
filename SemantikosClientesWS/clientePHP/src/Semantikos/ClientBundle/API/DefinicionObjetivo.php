<?php

namespace Semantikos\ClientBundle\API;

class DefinicionObjetivo
{

    /**
     * @var boolean $esTipoBasico
     */
    protected $esTipoBasico = null;

    /**
     * @var boolean $esTipoSMTK
     */
    protected $esTipoSMTK = null;

    /**
     * @var boolean $esTablaAuxiliar
     */
    protected $esTablaAuxiliar = null;

    /**
     * @var boolean $esTipoSnomedCT
     */
    protected $esTipoSnomedCT = null;

    /**
     * @var boolean $esTipoCrossMap
     */
    protected $esTipoCrossMap = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return boolean
     */
    public function getEsTipoBasico()
    {
      return $this->esTipoBasico;
    }

    /**
     * @param boolean $esTipoBasico
     * @return DefinicionObjetivo
     */
    public function setEsTipoBasico($esTipoBasico)
    {
      $this->esTipoBasico = $esTipoBasico;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsTipoSMTK()
    {
      return $this->esTipoSMTK;
    }

    /**
     * @param boolean $esTipoSMTK
     * @return DefinicionObjetivo
     */
    public function setEsTipoSMTK($esTipoSMTK)
    {
      $this->esTipoSMTK = $esTipoSMTK;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsTablaAuxiliar()
    {
      return $this->esTablaAuxiliar;
    }

    /**
     * @param boolean $esTablaAuxiliar
     * @return DefinicionObjetivo
     */
    public function setEsTablaAuxiliar($esTablaAuxiliar)
    {
      $this->esTablaAuxiliar = $esTablaAuxiliar;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsTipoSnomedCT()
    {
      return $this->esTipoSnomedCT;
    }

    /**
     * @param boolean $esTipoSnomedCT
     * @return DefinicionObjetivo
     */
    public function setEsTipoSnomedCT($esTipoSnomedCT)
    {
      $this->esTipoSnomedCT = $esTipoSnomedCT;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsTipoCrossMap()
    {
      return $this->esTipoCrossMap;
    }

    /**
     * @param boolean $esTipoCrossMap
     * @return DefinicionObjetivo
     */
    public function setEsTipoCrossMap($esTipoCrossMap)
    {
      $this->esTipoCrossMap = $esTipoCrossMap;
      return $this;
    }

}
