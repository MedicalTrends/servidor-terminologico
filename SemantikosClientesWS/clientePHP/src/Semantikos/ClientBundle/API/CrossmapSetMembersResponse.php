<?php

namespace Semantikos\ClientBundle\API;

class CrossmapSetMembersResponse
{

    /**
     * @var crossmapSetMembers $crossmapSetMembers
     */
    protected $crossmapSetMembers = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return crossmapSetMembers
     */
    public function getCrossmapSetMembers()
    {
      return $this->crossmapSetMembers;
    }

    /**
     * @param crossmapSetMembers $crossmapSetMembers
     * @return CrossmapSetMembersResponse
     */
    public function setCrossmapSetMembers($crossmapSetMembers)
    {
      $this->crossmapSetMembers = $crossmapSetMembers;
      return $this;
    }

}
