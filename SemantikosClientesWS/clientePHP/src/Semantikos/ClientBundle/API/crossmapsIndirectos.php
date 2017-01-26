<?php

namespace Semantikos\ClientBundle\API;

class crossmapsIndirectos
{

    /**
     * @var IndirectCrossmap $crossmapIndirecto
     */
    protected $crossmapIndirecto = null;

    /**
     * @param IndirectCrossmap $crossmapIndirecto
     */
    public function __construct($crossmapIndirecto)
    {
      $this->crossmapIndirecto = $crossmapIndirecto;
    }

    /**
     * @return IndirectCrossmap
     */
    public function getCrossmapIndirecto()
    {
      return $this->crossmapIndirecto;
    }

    /**
     * @param IndirectCrossmap $crossmapIndirecto
     * @return crossmapsIndirectos
     */
    public function setCrossmapIndirecto($crossmapIndirecto)
    {
      $this->crossmapIndirecto = $crossmapIndirecto;
      return $this;
    }

}
