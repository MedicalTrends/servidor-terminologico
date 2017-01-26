<?php

namespace Semantikos\ClientBundle\API;


class PeticionConceptosRelacionados
{

    /**
     * @var string $idConcepto
     */
    protected $idConcepto = null;

    /**
     * @var string $idDescripcion
     */
    protected $idDescripcion = null;

    
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
     * @return PeticionConceptosRelacionados
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
     * @return PeticionConceptosRelacionados
     */
    public function setIdDescripcion($idDescripcion)
    {
      $this->idDescripcion = $idDescripcion;
      return $this;
    }

}
