<?php

namespace Semantikos\ClientBundle\API;

class conceptosPorCategoria
{

    /**
     * @var PeticionPorCategoria $peticionConceptosPorCategoria
     */
    protected $peticionConceptosPorCategoria = null;

    /**
     * @param PeticionPorCategoria $peticionConceptosPorCategoria
     */
    public function __construct($peticionConceptosPorCategoria)
    {
      $this->peticionConceptosPorCategoria = $peticionConceptosPorCategoria;
    }

    /**
     * @return PeticionPorCategoria
     */
    public function getPeticionConceptosPorCategoria()
    {
      return $this->peticionConceptosPorCategoria;
    }

    /**
     * @param PeticionPorCategoria $peticionConceptosPorCategoria
     * @return conceptosPorCategoria
     */
    public function setPeticionConceptosPorCategoria($peticionConceptosPorCategoria)
    {
      $this->peticionConceptosPorCategoria = $peticionConceptosPorCategoria;
      return $this;
    }

}
