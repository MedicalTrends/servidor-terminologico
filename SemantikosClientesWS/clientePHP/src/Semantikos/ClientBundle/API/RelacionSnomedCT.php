<?php

namespace Semantikos\ClientBundle\API;

class RelacionSnomedCT
{

    /**
     * @var string $tipoRelacion
     */
    protected $tipoRelacion = null;

    /**
     * @var string $idConceptoSnomedCT
     */
    protected $idConceptoSnomedCT = null;

    /**
     * @var string $descripcion
     */
    protected $descripcion = null;

    /**
     * @var string $grupo
     */
    protected $grupo = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getTipoRelacion()
    {
      return $this->tipoRelacion;
    }

    /**
     * @param string $tipoRelacion
     * @return RelacionSnomedCT
     */
    public function setTipoRelacion($tipoRelacion)
    {
      $this->tipoRelacion = $tipoRelacion;
      return $this;
    }

    /**
     * @return string
     */
    public function getIdConceptoSnomedCT()
    {
      return $this->idConceptoSnomedCT;
    }

    /**
     * @param string $idConceptoSnomedCT
     * @return RelacionSnomedCT
     */
    public function setIdConceptoSnomedCT($idConceptoSnomedCT)
    {
      $this->idConceptoSnomedCT = $idConceptoSnomedCT;
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
     * @return RelacionSnomedCT
     */
    public function setDescripcion($descripcion)
    {
      $this->descripcion = $descripcion;
      return $this;
    }

    /**
     * @return string
     */
    public function getGrupo()
    {
      return $this->grupo;
    }

    /**
     * @param string $grupo
     * @return RelacionSnomedCT
     */
    public function setGrupo($grupo)
    {
      $this->grupo = $grupo;
      return $this;
    }

}
