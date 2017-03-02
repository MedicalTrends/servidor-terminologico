<?php

namespace Semantikos\ClientBundle\API;

class TablaAuxiliar
{

    /**
     * @var string $nombre
     */
    protected $nombre = null;

    /**
     * @var string $descripcion
     */
    protected $descripcion = null;

    /**
     * @var string $nombreTabla
     */
    protected $nombreTabla = null;

    /**
     * @var columnaTablaAuxiliar $columnaTablaAuxiliar
     */
    protected $columnaTablaAuxiliar = null;

    
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
     * @return TablaAuxiliar
     */
    public function setNombre($nombre)
    {
      $this->nombre = $nombre;
      return $this;
    }

    /**
     * @return string
     */
    public function getDescripcion()
    {
      return $this->descripcion;
    }

    /**
     * @param string $descripcion
     * @return TablaAuxiliar
     */
    public function setDescripcion($descripcion)
    {
      $this->descripcion = $descripcion;
      return $this;
    }

    /**
     * @return string
     */
    public function getNombreTabla()
    {
      return $this->nombreTabla;
    }

    /**
     * @param string $nombreTabla
     * @return TablaAuxiliar
     */
    public function setNombreTabla($nombreTabla)
    {
      $this->nombreTabla = $nombreTabla;
      return $this;
    }

    /**
     * @return columnaTablaAuxiliar
     */
    public function getColumnaTablaAuxiliar()
    {
      return $this->columnaTablaAuxiliar;
    }

    /**
     * @param columnaTablaAuxiliar $columnaTablaAuxiliar
     * @return TablaAuxiliar
     */
    public function setColumnaTablaAuxiliar($columnaTablaAuxiliar)
    {
      $this->columnaTablaAuxiliar = $columnaTablaAuxiliar;
      return $this;
    }

}
