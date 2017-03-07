<?php

namespace Semantikos\ClientBundle\API;

class RespuestaBuscarTerminoGenerica
{

    /**
     * @var descripcionesPerfectMatch $descripcionesPerfectMatch
     */
    protected $descripcionesPerfectMatch = null;

    /**
     * @var descripcionesNoValidas $descripcionesNoValidas
     */
    protected $descripcionesNoValidas = null;

    /**
     * @var descripcionesPendientes $descripcionesPendientes
     */
    protected $descripcionesPendientes = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return descripcionesPerfectMatch
     */
    public function getDescripcionesPerfectMatch()
    {
      return $this->descripcionesPerfectMatch;
    }

    /**
     * @param descripcionesPerfectMatch $descripcionesPerfectMatch
     * @return RespuestaBuscarTerminoGenerica
     */
    public function setDescripcionesPerfectMatch($descripcionesPerfectMatch)
    {
      $this->descripcionesPerfectMatch = $descripcionesPerfectMatch;
      return $this;
    }

    /**
     * @return descripcionesNoValidas
     */
    public function getDescripcionesNoValidas()
    {
      return $this->descripcionesNoValidas;
    }

    /**
     * @param descripcionesNoValidas $descripcionesNoValidas
     * @return RespuestaBuscarTerminoGenerica
     */
    public function setDescripcionesNoValidas($descripcionesNoValidas)
    {
      $this->descripcionesNoValidas = $descripcionesNoValidas;
      return $this;
    }

    /**
     * @return descripcionesPendientes
     */
    public function getDescripcionesPendientes()
    {
      return $this->descripcionesPendientes;
    }

    /**
     * @param descripcionesPendientes $descripcionesPendientes
     * @return RespuestaBuscarTerminoGenerica
     */
    public function setDescripcionesPendientes($descripcionesPendientes)
    {
      $this->descripcionesPendientes = $descripcionesPendientes;
      return $this;
    }

}
