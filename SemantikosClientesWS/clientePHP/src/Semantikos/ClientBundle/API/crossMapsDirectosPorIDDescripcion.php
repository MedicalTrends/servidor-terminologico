<?php

namespace Semantikos\ClientBundle\API;

class crossMapsDirectosPorIDDescripcion
{

    /**
     * @var string $DescripcionID
     */
    protected $DescripcionID = null;

    /**
     * @param string $DescripcionID
     */
    public function __construct($DescripcionID)
    {
      $this->DescripcionID = $DescripcionID;
    }

    /**
     * @return string
     */
    public function getDescripcionID()
    {
      return $this->DescripcionID;
    }

    /**
     * @param string $DescripcionID
     * @return crossMapsDirectosPorIDDescripcion
     */
    public function setDescripcionID($DescripcionID)
    {
      $this->DescripcionID = $DescripcionID;
      return $this;
    }

}
