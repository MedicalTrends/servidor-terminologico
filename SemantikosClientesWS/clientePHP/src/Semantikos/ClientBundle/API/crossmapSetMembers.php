<?php

namespace Semantikos\ClientBundle\API;

class crossmapSetMembers
{

    /**
     * @var CrossmapSetMember $crossmapSetMember
     */
    protected $crossmapSetMember = null;

    /**
     * @param CrossmapSetMember $crossmapSetMember
     */
    public function __construct($crossmapSetMember)
    {
      $this->crossmapSetMember = $crossmapSetMember;
    }

    /**
     * @return CrossmapSetMember
     */
    public function getCrossmapSetMember()
    {
      return $this->crossmapSetMember;
    }

    /**
     * @param CrossmapSetMember $crossmapSetMember
     * @return crossmapSetMembers
     */
    public function setCrossmapSetMember($crossmapSetMember)
    {
      $this->crossmapSetMember = $crossmapSetMember;
      return $this;
    }

}
