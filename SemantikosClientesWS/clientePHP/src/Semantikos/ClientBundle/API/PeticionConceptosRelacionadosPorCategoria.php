<?php

namespace Semantikos\ClientBundle\API;


class PeticionConceptosRelacionadosPorCategoria
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
     * @var string $categoriaRelacion
     */
    protected $categoriaRelacion = null;

    /**
     * @param string $categoriaRelacion
     */
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
     * @return PeticionConceptosRelacionadosPorCategoria
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
     * @return PeticionConceptosRelacionadosPorCategoria
     */
    public function setIdDescripcion($idDescripcion)
    {
      $this->idDescripcion = $idDescripcion;
      return $this;
    }

    /**
     * @return string
     */
    public function getCategoriaRelacion()
    {
      return $this->categoriaRelacion;
    }

    /**
     * @param string $categoriaRelacion
     * @return PeticionConceptosRelacionadosPorCategoria
     */
    public function setCategoriaRelacion($categoriaRelacion)
    {
      $this->categoriaRelacion = $categoriaRelacion;
      return $this;
    }

}
