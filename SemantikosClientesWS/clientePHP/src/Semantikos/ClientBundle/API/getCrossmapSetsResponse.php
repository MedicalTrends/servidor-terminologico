<?php

namespace Semantikos\ClientBundle\API;

class getCrossmapSetsResponse
{

    /**
     * @var CrossmapSetsResponse $crossmapSetResponse
     */
    protected $crossmapSetResponse = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return CrossmapSetsResponse
     */
    public function getCrossmapSetResponse()
    {
      return $this->crossmapSetResponse;
    }

    /**
     * @param CrossmapSetsResponse $crossmapSetResponse
     * @return getCrossmapSetsResponse
     */
    public function setCrossmapSetResponse($crossmapSetResponse)
    {
      $this->crossmapSetResponse = $crossmapSetResponse;
      return $this;
    }

}
