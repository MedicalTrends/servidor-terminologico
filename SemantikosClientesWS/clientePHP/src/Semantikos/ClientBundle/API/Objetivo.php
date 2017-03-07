<?php

namespace Semantikos\ClientBundle\API;

class Objetivo
{

    /**
     * @var string $tipo
     */
    protected $tipo = null;

    /**
     * @var boolean $activo
     */
    protected $activo = null;

    /**
     * @var int $idModulo
     */
    protected $idModulo = null;

    /**
     * @var int $estadoDefinicionId
     */
    protected $estadoDefinicionId = null;

    /**
     * @var boolean $valido
     */
    protected $valido = null;

    /**
     * @var \DateTime $tiempoEfectivo
     */
    protected $tiempoEfectivo = null;

    /**
     * @var string $valor
     */
    protected $valor = null;

    /**
     * @var Concepto $conceptoSMTK
     */
    protected $conceptoSMTK = null;

    /**
     * @var Relacion $relacion
     */
    protected $relacion = null;

    /**
     * @var TablaAuxiliar $tablaAuxiliar
     */
    protected $tablaAuxiliar = null;

    /**
     * @var fields $fields
     */
    protected $fields = null;

    /**
     * @param Relacion $relacion
     * @param TablaAuxiliar $tablaAuxiliar
     * @param fields $fields
     */
    public function __construct($relacion, $tablaAuxiliar, $fields)
    {
      $this->relacion = $relacion;
      $this->tablaAuxiliar = $tablaAuxiliar;
      $this->fields = $fields;
    }

    /**
     * @return string
     */
    public function getTipo()
    {
      return $this->tipo;
    }

    /**
     * @param string $tipo
     * @return Objetivo
     */
    public function setTipo($tipo)
    {
      $this->tipo = $tipo;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getActivo()
    {
      return $this->activo;
    }

    /**
     * @param boolean $activo
     * @return Objetivo
     */
    public function setActivo($activo)
    {
      $this->activo = $activo;
      return $this;
    }

    /**
     * @return int
     */
    public function getIdModulo()
    {
      return $this->idModulo;
    }

    /**
     * @param int $idModulo
     * @return Objetivo
     */
    public function setIdModulo($idModulo)
    {
      $this->idModulo = $idModulo;
      return $this;
    }

    /**
     * @return int
     */
    public function getEstadoDefinicionId()
    {
      return $this->estadoDefinicionId;
    }

    /**
     * @param int $estadoDefinicionId
     * @return Objetivo
     */
    public function setEstadoDefinicionId($estadoDefinicionId)
    {
      $this->estadoDefinicionId = $estadoDefinicionId;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getValido()
    {
      return $this->valido;
    }

    /**
     * @param boolean $valido
     * @return Objetivo
     */
    public function setValido($valido)
    {
      $this->valido = $valido;
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getTiempoEfectivo()
    {
      if ($this->tiempoEfectivo == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->tiempoEfectivo);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $tiempoEfectivo
     * @return Objetivo
     */
    public function setTiempoEfectivo(\DateTime $tiempoEfectivo = null)
    {
      if ($tiempoEfectivo == null) {
       $this->tiempoEfectivo = null;
      } else {
        $this->tiempoEfectivo = $tiempoEfectivo->format(\DateTime::ATOM);
      }
      return $this;
    }

    /**
     * @return string
     */
    public function getValor()
    {
      return $this->valor;
    }

    /**
     * @param string $valor
     * @return Objetivo
     */
    public function setValor($valor)
    {
      $this->valor = $valor;
      return $this;
    }

    /**
     * @return Concepto
     */
    public function getConceptoSMTK()
    {
      return $this->conceptoSMTK;
    }

    /**
     * @param Concepto $conceptoSMTK
     * @return Objetivo
     */
    public function setConceptoSMTK($conceptoSMTK)
    {
      $this->conceptoSMTK = $conceptoSMTK;
      return $this;
    }

    /**
     * @return Relacion
     */
    public function getRelacion()
    {
      return $this->relacion;
    }

    /**
     * @param Relacion $relacion
     * @return Objetivo
     */
    public function setRelacion($relacion)
    {
      $this->relacion = $relacion;
      return $this;
    }

    /**
     * @return TablaAuxiliar
     */
    public function getTablaAuxiliar()
    {
      return $this->tablaAuxiliar;
    }

    /**
     * @param TablaAuxiliar $tablaAuxiliar
     * @return Objetivo
     */
    public function setTablaAuxiliar($tablaAuxiliar)
    {
      $this->tablaAuxiliar = $tablaAuxiliar;
      return $this;
    }

    /**
     * @return fields
     */
    public function getFields()
    {
      return $this->fields;
    }

    /**
     * @param fields $fields
     * @return Objetivo
     */
    public function setFields($fields)
    {
      $this->fields = $fields;
      return $this;
    }

}
