<?php

namespace Semantikos\ClientBundle\API;

class Concepto
{

    /**
     * @var string $id
     */
    protected $id = null;

    /**
     * @var boolean $aSerRevisado
     */
    protected $aSerRevisado = null;

    /**
     * @var boolean $aSerConsultado
     */
    protected $aSerConsultado = null;

    /**
     * @var boolean $modelado
     */
    protected $modelado = null;

    /**
     * @var boolean $completamenteDefinido
     */
    protected $completamenteDefinido = null;

    /**
     * @var boolean $publicado
     */
    protected $publicado = null;

    /**
     * @var boolean $valido
     */
    protected $valido = null;

    /**
     * @var \DateTime $validoHasta
     */
    protected $validoHasta = null;

    /**
     * @var string $observacion
     */
    protected $observacion = null;

    /**
     * @var Categoria $categoria
     */
    protected $categoria = null;

    /**
     * @var refSets $refSets
     */
    protected $refSets = null;

    /**
     * @var descripciones $descripciones
     */
    protected $descripciones = null;

    /**
     * @var atributos $atributos
     */
    protected $atributos = null;

    /**
     * @var relaciones $relaciones
     */
    protected $relaciones = null;

    /**
     * @var relacionesSnomedCT $relacionesSnomedCT
     */
    protected $relacionesSnomedCT = null;

    /**
     * @var crossmapsIndirectos $crossmapsIndirectos
     */
    protected $crossmapsIndirectos = null;

    /**
     * @var crossmapsDirectos $crossmapsDirectos
     */
    protected $crossmapsDirectos = null;

    /**
     * @param Categoria $categoria
     */
    public function __construct($categoria)
    {
      $this->categoria = $categoria;
    }

    /**
     * @return string
     */
    public function getId()
    {
      return $this->id;
    }

    /**
     * @param string $id
     * @return Concepto
     */
    public function setId($id)
    {
      $this->id = $id;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getASerRevisado()
    {
      return $this->aSerRevisado;
    }

    /**
     * @param boolean $aSerRevisado
     * @return Concepto
     */
    public function setASerRevisado($aSerRevisado)
    {
      $this->aSerRevisado = $aSerRevisado;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getASerConsultado()
    {
      return $this->aSerConsultado;
    }

    /**
     * @param boolean $aSerConsultado
     * @return Concepto
     */
    public function setASerConsultado($aSerConsultado)
    {
      $this->aSerConsultado = $aSerConsultado;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getModelado()
    {
      return $this->modelado;
    }

    /**
     * @param boolean $modelado
     * @return Concepto
     */
    public function setModelado($modelado)
    {
      $this->modelado = $modelado;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getCompletamenteDefinido()
    {
      return $this->completamenteDefinido;
    }

    /**
     * @param boolean $completamenteDefinido
     * @return Concepto
     */
    public function setCompletamenteDefinido($completamenteDefinido)
    {
      $this->completamenteDefinido = $completamenteDefinido;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getPublicado()
    {
      return $this->publicado;
    }

    /**
     * @param boolean $publicado
     * @return Concepto
     */
    public function setPublicado($publicado)
    {
      $this->publicado = $publicado;
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
     * @return Concepto
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
     * @return Concepto
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
     * @return string
     */
    public function getObservacion()
    {
      return $this->observacion;
    }

    /**
     * @param string $observacion
     * @return Concepto
     */
    public function setObservacion($observacion)
    {
      $this->observacion = $observacion;
      return $this;
    }

    /**
     * @return Categoria
     */
    public function getCategoria()
    {
      return $this->categoria;
    }

    /**
     * @param Categoria $categoria
     * @return Concepto
     */
    public function setCategoria($categoria)
    {
      $this->categoria = $categoria;
      return $this;
    }

    /**
     * @return refSets
     */
    public function getRefSets()
    {
      return $this->refSets;
    }

    /**
     * @param refSets $refSets
     * @return Concepto
     */
    public function setRefSets($refSets)
    {
      $this->refSets = $refSets;
      return $this;
    }

    /**
     * @return descripciones
     */
    public function getDescripciones()
    {
      return $this->descripciones;
    }

    /**
     * @param descripciones $descripciones
     * @return Concepto
     */
    public function setDescripciones($descripciones)
    {
      $this->descripciones = $descripciones;
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
     * @return Concepto
     */
    public function setAtributos($atributos)
    {
      $this->atributos = $atributos;
      return $this;
    }

    /**
     * @return relaciones
     */
    public function getRelaciones()
    {
      return $this->relaciones;
    }

    /**
     * @param relaciones $relaciones
     * @return Concepto
     */
    public function setRelaciones($relaciones)
    {
      $this->relaciones = $relaciones;
      return $this;
    }

    /**
     * @return relacionesSnomedCT
     */
    public function getRelacionesSnomedCT()
    {
      return $this->relacionesSnomedCT;
    }

    /**
     * @param relacionesSnomedCT $relacionesSnomedCT
     * @return Concepto
     */
    public function setRelacionesSnomedCT($relacionesSnomedCT)
    {
      $this->relacionesSnomedCT = $relacionesSnomedCT;
      return $this;
    }

    /**
     * @return crossmapsIndirectos
     */
    public function getCrossmapsIndirectos()
    {
      return $this->crossmapsIndirectos;
    }

    /**
     * @param crossmapsIndirectos $crossmapsIndirectos
     * @return Concepto
     */
    public function setCrossmapsIndirectos($crossmapsIndirectos)
    {
      $this->crossmapsIndirectos = $crossmapsIndirectos;
      return $this;
    }

    /**
     * @return crossmapsDirectos
     */
    public function getCrossmapsDirectos()
    {
      return $this->crossmapsDirectos;
    }

    /**
     * @param crossmapsDirectos $crossmapsDirectos
     * @return Concepto
     */
    public function setCrossmapsDirectos($crossmapsDirectos)
    {
      $this->crossmapsDirectos = $crossmapsDirectos;
      return $this;
    }

}
