<?php

namespace Semantikos\ClientBundle\API;

class refsets
{

    /**
     * @var RefSet $refset
     */
    protected $refset = null;

    /**
     * @param RefSet $refset
     */
    public function __construct($refset)
    {
      $this->refset = $refset;
    }

    /**
     * @return RefSet
     */
    public function getRefset()
    {
      return $this->refset;
    }

    /**
     * @param RefSet $refset
     * @return refsets
     */
    public function setRefset($refset)
    {
      $this->refset = $refset;
      return $this;
    }

}
