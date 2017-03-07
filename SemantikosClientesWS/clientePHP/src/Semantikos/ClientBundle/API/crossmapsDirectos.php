<?php

namespace Semantikos\ClientBundle\API;

class crossmapsDirectos
{

    /**
     * @var CrossmapSetMember $crossmapDirecto
     */
    protected $crossmapDirecto = null;

    /**
     * @param CrossmapSetMember $crossmapDirecto
     */
    public function __construct($crossmapDirecto)
    {
      $this->crossmapDirecto = $crossmapDirecto;
    }

    /**
     * @return CrossmapSetMember
     */
    public function getCrossmapDirecto()
    {
      return $this->crossmapDirecto;
    }

    /**
     * @param CrossmapSetMember $crossmapDirecto
     * @return crossmapsDirectos
     */
    public function setCrossmapDirecto($crossmapDirecto)
    {
      $this->crossmapDirecto = $crossmapDirecto;
      return $this;
    }

}
