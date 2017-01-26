<?php

namespace Semantikos\ClientBundle\API;

class conceptosPorRefSet
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
     * @return conceptosPorRefSet
     */
    public function setPeticionConceptosPorRefSet($peticionConceptosPorRefSet)
    {
      $this->peticionConceptosPorRefSet = $peticionConceptosPorRefSet;
      return $this;
    }

}
