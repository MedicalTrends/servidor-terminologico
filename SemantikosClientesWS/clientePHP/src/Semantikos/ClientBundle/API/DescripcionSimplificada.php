<?php

namespace Semantikos\ClientBundle\API;

class DescripcionSimplificada
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
     * @return DescripcionSimplificada
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
     * @return DescripcionSimplificada
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
     * @return DescripcionSimplificada
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
     * @return DescripcionSimplificada
     */
    public function setTipoDescripcion($tipoDescripcion)
    {
      $this->tipoDescripcion = $tipoDescripcion;
      return $this;
    }

}
