<?php

namespace Semantikos\ClientBundle\API;

class Multiplicidad
{

    /**
     * @var int $limiteInferior
     */
    protected $limiteInferior = null;

    /**
     * @var int $limiteSuperior
     */
    protected $limiteSuperior = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return int
     */
    public function getLimiteInferior()
    {
      return $this->limiteInferior;
    }

    /**
     * @param int $limiteInferior
     * @return Multiplicidad
     */
    public function setLimiteInferior($limiteInferior)
    {
      $this->limiteInferior = $limiteInferior;
      return $this;
    }

    /**
     * @return int
     */
    public function getLimiteSuperior()
    {
      return $this->limiteSuperior;
    }

    /**
     * @param int $limiteSuperior
     * @return Multiplicidad
     */
    public function setLimiteSuperior($limiteSuperior)
    {
      $this->limiteSuperior = $limiteSuperior;
      return $this;
    }

}
