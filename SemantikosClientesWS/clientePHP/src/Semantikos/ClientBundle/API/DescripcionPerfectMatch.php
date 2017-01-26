<?php

namespace Semantikos\ClientBundle\API;

class DescripcionPerfectMatch
{

    /**
     * @var string $idConcepto
     */
    protected $idConcepto = null;

    /**
     * @var string $idDescripcion
     */
    protected $idDescripcion = null;

    /**
     * @var string $termino
     */
    protected $termino = null;

    /**
     * @var string $tipoDescripcion
     */
    protected $tipoDescripcion = null;

    /**
     * @var string $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var string $terminoPreferido
     */
    protected $terminoPreferido = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getIdConcepto()
    {
      return $this->idConcepto;
    }

    /**
     * @param string $idConcepto
     * @return DescripcionPerfectMatch
     */
    public function setIdConcepto($idConcepto)
    {
      $this->idConcepto = $idConcepto;
      return $this;
    }

    /**
     * @return string
     */
    public function getIdDescripcion()
    {
      return $this->idDescripcion;
    }

    /**
     * @param string $idDescripcion
     * @return DescripcionPerfectMatch
     */
    public function setIdDescripcion($idDescripcion)
    {
      $this->idDescripcion = $idDescripcion;
      return $this;
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
     * @return DescripcionPerfectMatch
     */
    public function setTermino($termino)
    {
      $this->termino = $termino;
      return $this;
    }

    /**
     * @return string
     */
    public function getTipoDescripcion()
    {
      return $this->tipoDescripcion;
    }

    /**
     * @param string $tipoDescripcion
     * @return DescripcionPerfectMatch
     */
    public function setTipoDescripcion($tipoDescripcion)
    {
      $this->tipoDescripcion = $tipoDescripcion;
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
     * @return DescripcionPerfectMatch
     */
    public function setNombreCategoria($nombreCategoria)
    {
      $this->nombreCategoria = $nombreCategoria;
      return $this;
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
     * @return DescripcionPerfectMatch
     */
    public function setTerminoPreferido($terminoPreferido)
    {
      $this->terminoPreferido = $terminoPreferido;
      return $this;
    }

}
