<?php

namespace Semantikos\ClientBundle\API;

class CrossmapSetMember
{

    /**
     * @var int $idCrossmapSetMember
     */
    protected $idCrossmapSetMember = null;

    /**
     * @var CrossmapSet $crossmapSet
     */
    protected $crossmapSet = null;

    /**
     * @var string $code
     */
    protected $code = null;

    /**
     * @var string $gloss
     */
    protected $gloss = null;

    /**
     * @param int $idCrossmapSetMember
     * @param CrossmapSet $crossmapSet
     */
    public function __construct($idCrossmapSetMember, $crossmapSet)
    {
      $this->idCrossmapSetMember = $idCrossmapSetMember;
      $this->crossmapSet = $crossmapSet;
    }

    /**
     * @return int
     */
    public function getIdCrossmapSetMember()
    {
      return $this->idCrossmapSetMember;
    }

    /**
     * @param int $idCrossmapSetMember
     * @return CrossmapSetMember
     */
    public function setIdCrossmapSetMember($idCrossmapSetMember)
    {
      $this->idCrossmapSetMember = $idCrossmapSetMember;
      return $this;
    }

    /**
     * @return CrossmapSet
     */
    public function getCrossmapSet()
    {
      return $this->crossmapSet;
    }

    /**
     * @param CrossmapSet $crossmapSet
     * @return CrossmapSetMember
     */
    public function setCrossmapSet($crossmapSet)
    {
      $this->crossmapSet = $crossmapSet;
      return $this;
    }

    /**
     * @return string
     */
    public function getCode()
    {
      return $this->code;
    }

    /**
     * @param string $code
     * @return CrossmapSetMember
     */
    public function setCode($code)
    {
      $this->code = $code;
      return $this;
    }

    /**
     * @return string
     */
    public function getGloss()
    {
      return $this->gloss;
    }

    /**
     * @param string $gloss
     * @return CrossmapSetMember
     */
    public function setGloss($gloss)
    {
      $this->gloss = $gloss;
      return $this;
    }

}
