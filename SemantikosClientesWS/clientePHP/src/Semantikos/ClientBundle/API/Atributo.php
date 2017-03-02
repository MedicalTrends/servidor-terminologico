<?php

namespace Semantikos\ClientBundle\API;

class Atributo
{

    /**
     * @var string $tipo
     */
    protected $tipo = null;

    /**
     * @var string $nombre
     */
    protected $nombre = null;

    /**
     * @var string $valor
     */
    protected $valor = null;

    
    public function __construct()
    {
    
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
     * @return Atributo
     */
    public function setTipo($tipo)
    {
      $this->tipo = $tipo;
      return $this;
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
     * @return Atributo
     */
    public function setNombre($nombre)
    {
      $this->nombre = $nombre;
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
     * @return Atributo
     */
    public function setValor($valor)
    {
      $this->valor = $valor;
      return $this;
    }

}
