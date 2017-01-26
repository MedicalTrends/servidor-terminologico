<?php

namespace Semantikos\ClientBundle\API;

class crossmapSetMembersDeCrossmapSet
{

    /**
     * @var string $nombreAbreviadoCrossmapSet
     */
    protected $nombreAbreviadoCrossmapSet = null;

    /**
     * @param string $nombreAbreviadoCrossmapSet
     */
    public function __construct($nombreAbreviadoCrossmapSet)
    {
      $this->nombreAbreviadoCrossmapSet = $nombreAbreviadoCrossmapSet;
    }

    /**
     * @return string
     */
    public function getNombreAbreviadoCrossmapSet()
    {
      return $this->nombreAbreviadoCrossmapSet;
    }

    /**
     * @param string $nombreAbreviadoCrossmapSet
     * @return crossmapSetMembersDeCrossmapSet
     */
    public function setNombreAbreviadoCrossmapSet($nombreAbreviadoCrossmapSet)
    {
      $this->nombreAbreviadoCrossmapSet = $nombreAbreviadoCrossmapSet;
      return $this;
    }

}
