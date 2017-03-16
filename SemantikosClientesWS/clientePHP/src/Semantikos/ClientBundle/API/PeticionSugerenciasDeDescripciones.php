<?php

namespace Semantikos\ClientBundle\API;

class PeticionSugerenciasDeDescripciones
{

    /**
     * @var string $termino
     */
    protected $termino = null;

    /**
     * @var string[] $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var string $idEstablecimiento
     */
    protected $idEstablecimiento = null;

    /**
     * @param string $termino
     * @param string $idEstablecimiento
     */
    public function __construct()
    {
    }

    /**
     * @return string
     */
    public function getTermino()
    {
        return $this->termino;
    }

    /**
     * @param string $termino
     * @return PeticionSugerenciasDeDescripciones
     */
    public function setTermino($termino)
    {
        $this->termino = $termino;
        return $this;
    }

    /**
     * @return string[]
     */
    public function getNombreCategoria()
    {
        return $this->nombreCategoria;
    }

    /**
     * @param string[] $nombreCategoria
     * @return PeticionSugerenciasDeDescripciones
     */
    public function setNombreCategoria(array $nombreCategoria = null)
    {
        $this->nombreCategoria = $nombreCategoria;
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
     * @return PeticionSugerenciasDeDescripciones
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
        $this->idEstablecimiento = $idEstablecimiento;
        return $this;
    }

}
