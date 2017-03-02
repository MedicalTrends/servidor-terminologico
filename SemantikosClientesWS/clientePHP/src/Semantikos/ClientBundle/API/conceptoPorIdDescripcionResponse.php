<?php

namespace Semantikos\ClientBundle\API;

class conceptoPorIdDescripcionResponse
{

    /**
     * @var Concepto $concepto
     */
    protected $concepto = null;

    /**
     * @param Concepto $concepto
     */
    public function __construct($concepto)
    {
      $this->concepto = $concepto;
    }

    /**
     * @return Concepto
     */
    public function getConcepto()
    {
      return $this->concepto;
    }

    /**
     * @param Concepto $concepto
     * @return conceptoPorIdDescripcionResponse
     */
    public function setConcepto($concepto)
    {
      $this->concepto = $concepto;
      return $this;
    }

}
