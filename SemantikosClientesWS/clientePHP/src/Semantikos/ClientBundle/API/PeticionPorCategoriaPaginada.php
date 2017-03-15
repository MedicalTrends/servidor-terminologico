<?php

namespace Semantikos\ClientBundle\API;

class PeticionPorCategoriaPaginada
{

    /**
     * @var string $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var int $numeroPagina
     */
    protected $numeroPagina = null;

    /**
     * @var int $tamanoPagina
     */
    protected $tamanoPagina = null;

    /**
     * @var string $idEstablecimiento
     */
    protected $idEstablecimiento = null;

    /**
     * @param string $nombreCategoria
     * @param int $numeroPagina
     * @param int $tamanoPagina
     * @param string $idEstablecimiento
     */
    public function __construct()
    {
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
     * @return PeticionPorCategoriaPaginada
     */
    public function setNombreCategoria($nombreCategoria)
    {
      $this->nombreCategoria = $nombreCategoria;
      return $this;
    }

    /**
     * @return int
     */
    public function getNumeroPagina()
    {
      return $this->numeroPagina;
    }

    /**
     * @param int $numeroPagina
     * @return PeticionPorCategoriaPaginada
     */
    public function setNumeroPagina($numeroPagina)
    {
      $this->numeroPagina = $numeroPagina;
      return $this;
    }

    /**
     * @return int
     */
    public function getTamanoPagina()
    {
      return $this->tamanoPagina;
    }

    /**
     * @param int $tamanoPagina
     * @return PeticionPorCategoriaPaginada
     */
    public function setTamanoPagina($tamanoPagina)
    {
      $this->tamanoPagina = $tamanoPagina;
      return $this;
    }

    /**
     * @return string
     */
    public function getIdEstablecimiento()
    {
      return $this->idEstablecimiento;
    }

    /**
     * @param string $idEstablecimiento
     * @return PeticionPorCategoriaPaginada
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
      $this->idEstablecimiento = $idEstablecimiento;
      return $this;
    }

}
