<?php

namespace Semantikos\ClientBundle\API;

class crossmapSetMembersDeCrossmapSetResponse
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
     * @return crossmapSetMembersDeCrossmapSetResponse
     */
    public function setCrossmapSetMember($crossmapSetMember)
    {
      $this->crossmapSetMember = $crossmapSetMember;
      return $this;
    }

}
