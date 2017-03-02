<?php

namespace Semantikos\ClientBundle\API;

class IndirectCrossmapsSearch
{

    /**
     * @var indirectCrossmaps $indirectCrossmaps
     */
    protected $indirectCrossmaps = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return indirectCrossmaps
     */
    public function getIndirectCrossmaps()
    {
      return $this->indirectCrossmaps;
    }

    /**
     * @param indirectCrossmaps $indirectCrossmaps
     * @return IndirectCrossmapsSearch
     */
    public function setIndirectCrossmaps($indirectCrossmaps)
    {
      $this->indirectCrossmaps = $indirectCrossmaps;
      return $this;
    }

}
