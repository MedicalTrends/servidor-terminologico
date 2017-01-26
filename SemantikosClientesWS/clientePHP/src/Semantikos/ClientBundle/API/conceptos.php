<?php

namespace Semantikos\ClientBundle\API;

class conceptos
{

    /**
     * @var ConceptoLight $concepto
     */
    protected $concepto = null;

    /**
     * @param ConceptoLight $concepto
     */
    public function __construct($concepto)
    {
      $this->concepto = $concepto;
    }

    /**
     * @return ConceptoLight
     */
    public function getConcepto()
    {
      return $this->concepto;
    }

    /**
     * @param ConceptoLight $concepto
     * @return conceptos
     */
    public function setConcepto($concepto)
    {
      $this->concepto = $concepto;
      return $this;
    }

}
