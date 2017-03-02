<?php

namespace Semantikos\ClientBundle\API;

class Categoria
{

    /**
     * @var string $nombre
     */
    protected $nombre = null;

    /**
     * @var string $nombreAbreviado
     */
    protected $nombreAbreviado = null;

    /**
     * @var boolean $restringida
     */
    protected $restringida = null;

    
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
     * @return Categoria
     */
    public function setNombre($nombre)
    {
      $this->nombre = $nombre;
      return $this;
    }

    /**
     * @return string
     */
    public function getNombreAbreviado()
    {
      return $this->nombreAbreviado;
    }

    /**
     * @param string $nombreAbreviado
     * @return Categoria
     */
    public function setNombreAbreviado($nombreAbreviado)
    {
      $this->nombreAbreviado = $nombreAbreviado;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getRestringida()
    {
      return $this->restringida;
    }

    /**
     * @param boolean $restringida
     * @return Categoria
     */
    public function setRestringida($restringida)
    {
      $this->restringida = $restringida;
      return $this;
    }

}
