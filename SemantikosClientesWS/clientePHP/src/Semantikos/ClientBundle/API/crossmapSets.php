<?php

namespace Semantikos\ClientBundle\API;

class crossmapSets
{

    /**
     * @var CrossmapSet $crossmapSet
     */
    protected $crossmapSet = null;

    /**
     * @param CrossmapSet $crossmapSet
     */
    public function __construct($crossmapSet)
    {
      $this->crossmapSet = $crossmapSet;
    }

    /**
     * @return CrossmapSet
     */
    public function getCrossmapSet()
    {
      return $this->crossmapSet;
    }

    /**
     * @param CrossmapSet $crossmapSet
     * @return crossmapSets
     */
    public function setCrossmapSet($crossmapSet)
    {
      $this->crossmapSet = $crossmapSet;
      return $this;
    }

}
