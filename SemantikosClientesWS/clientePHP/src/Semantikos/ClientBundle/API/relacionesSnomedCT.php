<?php

namespace Semantikos\ClientBundle\API;

class relacionesSnomedCT
{

    /**
     * @var RelacionSnomedCT $relacionSnomedCT
     */
    protected $relacionSnomedCT = null;

    /**
     * @param RelacionSnomedCT $relacionSnomedCT
     */
    public function __construct($relacionSnomedCT)
    {
      $this->relacionSnomedCT = $relacionSnomedCT;
    }

    /**
     * @return RelacionSnomedCT
     */
    public function getRelacionSnomedCT()
    {
      return $this->relacionSnomedCT;
    }

    /**
     * @param RelacionSnomedCT $relacionSnomedCT
     * @return relacionesSnomedCT
     */
    public function setRelacionSnomedCT($relacionSnomedCT)
    {
      $this->relacionSnomedCT = $relacionSnomedCT;
      return $this;
    }

}
