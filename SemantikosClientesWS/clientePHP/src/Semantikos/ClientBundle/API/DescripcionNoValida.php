<?php

namespace Semantikos\ClientBundle\API;

class DescripcionNoValida
{

    /**
     * @var string $razonNoValido
     */
    protected $razonNoValido = null;

    /**
     * @var boolean $validez
     */
    protected $validez = null;

    /**
     * @var descripcionesSugeridas $descripcionesSugeridas
     */
    protected $descripcionesSugeridas = null;

    /**
     * @var int $cantidadRegistros
     */
    protected $cantidadRegistros = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getRazonNoValido()
    {
      return $this->razonNoValido;
    }

    /**
     * @param string $razonNoValido
     * @return DescripcionNoValida
     */
    public function setRazonNoValido($razonNoValido)
    {
      $this->razonNoValido = $razonNoValido;
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
     * @return DescripcionNoValida
     */
    public function setValidez($validez)
    {
      $this->validez = $validez;
      return $this;
    }

    /**
     * @return descripcionesSugeridas
     */
    public function getDescripcionesSugeridas()
    {
      return $this->descripcionesSugeridas;
    }

    /**
     * @param descripcionesSugeridas $descripcionesSugeridas
     * @return DescripcionNoValida
     */
    public function setDescripcionesSugeridas($descripcionesSugeridas)
    {
      $this->descripcionesSugeridas = $descripcionesSugeridas;
      return $this;
    }

    /**
     * @return int
     */
    public function getCantidadRegistros()
    {
      return $this->cantidadRegistros;
    }

    /**
     * @param int $cantidadRegistros
     * @return DescripcionNoValida
     */
    public function setCantidadRegistros($cantidadRegistros)
    {
      $this->cantidadRegistros = $cantidadRegistros;
      return $this;
    }

}
