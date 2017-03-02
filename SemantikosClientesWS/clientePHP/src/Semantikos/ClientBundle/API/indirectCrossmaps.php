<?php

namespace Semantikos\ClientBundle\API;

class indirectCrossmaps
{

    /**
     * @var IndirectCrossmap $indirectCrossmap
     */
    protected $indirectCrossmap = null;

    /**
     * @param IndirectCrossmap $indirectCrossmap
     */
    public function __construct($indirectCrossmap)
    {
      $this->indirectCrossmap = $indirectCrossmap;
    }

    /**
     * @return IndirectCrossmap
     */
    public function getIndirectCrossmap()
    {
      return $this->indirectCrossmap;
    }

    /**
     * @param IndirectCrossmap $indirectCrossmap
     * @return indirectCrossmaps
     */
    public function setIndirectCrossmap($indirectCrossmap)
    {
      $this->indirectCrossmap = $indirectCrossmap;
      return $this;
    }

}
