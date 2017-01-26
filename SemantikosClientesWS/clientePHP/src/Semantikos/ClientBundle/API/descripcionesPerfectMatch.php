<?php

namespace Semantikos\ClientBundle\API;

class descripcionesPerfectMatch
{

    /**
     * @var DescripcionPerfectMatch $descripcionPerfectMatch
     */
    protected $descripcionPerfectMatch = null;

    /**
     * @param DescripcionPerfectMatch $descripcionPerfectMatch
     */
    public function __construct($descripcionPerfectMatch)
    {
      $this->descripcionPerfectMatch = $descripcionPerfectMatch;
    }

    /**
     * @return DescripcionPerfectMatch
     */
    public function getDescripcionPerfectMatch()
    {
      return $this->descripcionPerfectMatch;
    }

    /**
     * @param DescripcionPerfectMatch $descripcionPerfectMatch
     * @return descripcionesPerfectMatch
     */
    public function setDescripcionPerfectMatch($descripcionPerfectMatch)
    {
      $this->descripcionPerfectMatch = $descripcionPerfectMatch;
      return $this;
    }

}
