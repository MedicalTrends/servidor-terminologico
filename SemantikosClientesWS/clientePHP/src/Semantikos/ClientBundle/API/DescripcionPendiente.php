<?php

namespace Semantikos\ClientBundle\API;

class DescripcionPendiente
{

    /**
     * @var string $terminoPreferido
     */
    protected $terminoPreferido = null;

    /**
     * @var string $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var boolean $pendienteCodificacion
     */
    protected $pendienteCodificacion = null;

    /**
     * @var boolean $validez
     */
    protected $validez = null;

    /**
     * @var descripciones $descripciones
     */
    protected $descripciones = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getTerminoPreferido()
    {
      return $this->terminoPreferido;
    }

    /**
     * @param string $terminoPreferido
     * @return DescripcionPendiente
     */
    public function setTerminoPreferido($terminoPreferido)
    {
      $this->terminoPreferido = $terminoPreferido;
      return $this;
    }

    /**
     * @return string
     */
    public function getNombreCategoria()
    {
      return $this->nombreCategoria;
    }

    /**
     * @param string $nombreCategoria
     * @return DescripcionPendiente
     */
    public function setNombreCategoria($nombreCategoria)
    {
      $this->nombreCategoria = $nombreCategoria;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getPendienteCodificacion()
    {
      return $this->pendienteCodificacion;
    }

    /**
     * @param boolean $pendienteCodificacion
     * @return DescripcionPendiente
     */
    public function setPendienteCodificacion($pendienteCodificacion)
    {
      $this->pendienteCodificacion = $pendienteCodificacion;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getValidez()
    {
      return $this->validez;
    }

    /**
     * @param boolean $validez
     * @return DescripcionPendiente
     */
    public function setValidez($validez)
    {
      $this->validez = $validez;
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
     * @return DescripcionPendiente
     */
    public function setDescripciones($descripciones)
    {
      $this->descripciones = $descripciones;
      return $this;
    }

}
