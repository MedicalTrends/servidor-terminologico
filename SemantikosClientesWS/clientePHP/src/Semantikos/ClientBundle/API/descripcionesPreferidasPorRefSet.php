<?php

namespace Semantikos\ClientBundle\API;

class descripcionesPreferidasPorRefSet
{

    /**
     * @var PeticionConceptosPorRefSet $peticionConceptosPorRefSet
     */
    protected $peticionConceptosPorRefSet = null;

    /**
     * @param PeticionConceptosPorRefSet $peticionConceptosPorRefSet
     */
    public function __construct($peticionConceptosPorRefSet)
    {
      $this->peticionConceptosPorRefSet = $peticionConceptosPorRefSet;
    }

    /**
     * @return PeticionConceptosPorRefSet
     */
    public function getPeticionConceptosPorRefSet()
    {
      return $this->peticionConceptosPorRefSet;
    }

    /**
     * @param PeticionConceptosPorRefSet $peticionConceptosPorRefSet
     * @return descripcionesPreferidasPorRefSet
     */
    public function setPeticionConceptosPorRefSet($peticionConceptosPorRefSet)
    {
      $this->peticionConceptosPorRefSet = $peticionConceptosPorRefSet;
      return $this;
    }

}
