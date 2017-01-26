<?php

namespace Semantikos\ClientBundle\API;

class RefSet
{

    /**
     * @var string $nombre
     */
    protected $nombre = null;

    /**
     * @var boolean $valido
     */
    protected $valido = null;

    /**
     * @var \DateTime $validoHasta
     */
    protected $validoHasta = null;

    /**
     * @var \DateTime $creadoEn
     */
    protected $creadoEn = null;

    /**
     * @var string $institucion
     */
    protected $institucion = null;

    /**
     * @var conceptos $conceptos
     */
    protected $conceptos = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getNombre()
    {
      return $this->nombre;
    }

    /**
     * @param string $nombre
     * @return RefSet
     */
    public function setNombre($nombre)
    {
      $this->nombre = $nombre;
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
     * @return RefSet
     */
    public function setValido($valido)
    {
      $this->valido = $valido;
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getValidoHasta()
    {
      if ($this->validoHasta == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->validoHasta);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $validoHasta
     * @return RefSet
     */
    public function setValidoHasta(\DateTime $validoHasta = null)
    {
      if ($validoHasta == null) {
       $this->validoHasta = null;
      } else {
        $this->validoHasta = $validoHasta->format(\DateTime::ATOM);
      }
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getCreadoEn()
    {
      if ($this->creadoEn == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->creadoEn);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $creadoEn
     * @return RefSet
     */
    public function setCreadoEn(\DateTime $creadoEn = null)
    {
      if ($creadoEn == null) {
       $this->creadoEn = null;
      } else {
        $this->creadoEn = $creadoEn->format(\DateTime::ATOM);
      }
      return $this;
    }

    /**
     * @return string
     */
    public function getInstitucion()
    {
      return $this->institucion;
    }

    /**
     * @param string $institucion
     * @return RefSet
     */
    public function setInstitucion($institucion)
    {
      $this->institucion = $institucion;
      return $this;
    }

    /**
     * @return conceptos
     */
    public function getConceptos()
    {
      return $this->conceptos;
    }

    /**
     * @param conceptos $conceptos
     * @return RefSet
     */
    public function setConceptos($conceptos)
    {
      $this->conceptos = $conceptos;
      return $this;
    }

}
