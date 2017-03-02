<?php

namespace Semantikos\ClientBundle\API;

class crossMapsDirectosPorIDDescripcionResponse
{

    /**
     * @var CrossmapSetMembersResponse $crossmapSetMember
     */
    protected $crossmapSetMember = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return CrossmapSetMembersResponse
     */
    public function getCrossmapSetMember()
    {
      return $this->crossmapSetMember;
    }

    /**
     * @param CrossmapSetMembersResponse $crossmapSetMember
     * @return crossMapsDirectosPorIDDescripcionResponse
     */
    public function setCrossmapSetMember($crossmapSetMember)
    {
      $this->crossmapSetMember = $crossmapSetMember;
      return $this;
    }

}
