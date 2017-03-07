<?php

namespace Semantikos\ClientBundle\API;

class definicionesAtributoRelacion
{

    /**
     * @var DefinicionRelacionAtributo $definicionAtributoRelacion
     */
    protected $definicionAtributoRelacion = null;

    /**
     * @param DefinicionRelacionAtributo $definicionAtributoRelacion
     */
    public function __construct($definicionAtributoRelacion)
    {
      $this->definicionAtributoRelacion = $definicionAtributoRelacion;
    }

    /**
     * @return DefinicionRelacionAtributo
     */
    public function getDefinicionAtributoRelacion()
    {
      return $this->definicionAtributoRelacion;
    }

    /**
     * @param DefinicionRelacionAtributo $definicionAtributoRelacion
     * @return definicionesAtributoRelacion
     */
    public function setDefinicionAtributoRelacion($definicionAtributoRelacion)
    {
      $this->definicionAtributoRelacion = $definicionAtributoRelacion;
      return $this;
    }

}
