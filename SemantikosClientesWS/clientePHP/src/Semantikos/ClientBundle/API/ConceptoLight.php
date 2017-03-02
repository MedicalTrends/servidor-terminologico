<?php

namespace Semantikos\ClientBundle\API;

class ConceptoLight
{

    /**
     * @var string $conceptID
     */
    protected $conceptID = null;

    /**
     * @var string $idDescripcionPreferida
     */
    protected $idDescripcionPreferida = null;

    /**
     * @var string $descripcionPreferida
     */
    protected $descripcionPreferida = null;

    /**
     * @var string $categoria
     */
    protected $categoria = null;

    /**
     * @var boolean $esValido
     */
    protected $esValido = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getConceptID()
    {
      return $this->conceptID;
    }

    /**
     * @param string $conceptID
     * @return ConceptoLight
     */
    public function setConceptID($conceptID)
    {
      $this->conceptID = $conceptID;
      return $this;
    }

    /**
     * @return string
     */
    public function getIdDescripcionPreferida()
    {
      return $this->idDescripcionPreferida;
    }

    /**
     * @param string $idDescripcionPreferida
     * @return ConceptoLight
     */
    public function setIdDescripcionPreferida($idDescripcionPreferida)
    {
      $this->idDescripcionPreferida = $idDescripcionPreferida;
      return $this;
    }

    /**
     * @return string
     */
    public function getDescripcionPreferida()
    {
      return $this->descripcionPreferida;
    }

    /**
     * @param string $descripcionPreferida
     * @return ConceptoLight
     */
    public function setDescripcionPreferida($descripcionPreferida)
    {
      $this->descripcionPreferida = $descripcionPreferida;
      return $this;
    }

    /**
     * @return string
     */
    public function getCategoria()
    {
      return $this->categoria;
    }

    /**
     * @param string $categoria
     * @return ConceptoLight
     */
    public function setCategoria($categoria)
    {
      $this->categoria = $categoria;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsValido()
    {
      return $this->esValido;
    }

    /**
     * @param boolean $esValido
     * @return ConceptoLight
     */
    public function setEsValido($esValido)
    {
      $this->esValido = $esValido;
      return $this;
    }

}
