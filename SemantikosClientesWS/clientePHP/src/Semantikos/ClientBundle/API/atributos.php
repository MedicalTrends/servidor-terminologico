<?php

namespace Semantikos\ClientBundle\API;

class atributos
{

    /**
     * @var AtributoRelacion $atributo
     */
    protected $atributo = null;

    /**
     * @param AtributoRelacion $atributo
     */
    public function __construct($atributo)
    {
      $this->atributo = $atributo;
    }

    /**
     * @return AtributoRelacion
     */
    public function getAtributo()
    {
      return $this->atributo;
    }

    /**
     * @param AtributoRelacion $atributo
     * @return atributos
     */
    public function setAtributo($atributo)
    {
      $this->atributo = $atributo;
      return $this;
    }

}
