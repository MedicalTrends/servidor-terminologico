<?php

namespace Semantikos\ClientBundle\API;

class crossMapsIndirectosPorIDDescripcionResponse
{

    /**
     * @var IndirectCrossmapsSearch $indirectCrossmaps
     */
    protected $indirectCrossmaps = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return IndirectCrossmapsSearch
     */
    public function getIndirectCrossmaps()
    {
      return $this->indirectCrossmaps;
    }

    /**
     * @param IndirectCrossmapsSearch $indirectCrossmaps
     * @return crossMapsIndirectosPorIDDescripcionResponse
     */
    public function setIndirectCrossmaps($indirectCrossmaps)
    {
      $this->indirectCrossmaps = $indirectCrossmaps;
      return $this;
    }

}
