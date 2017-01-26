<?php

namespace Semantikos\ClientBundle\API;

class IndirectCrossmap
{

    /**
     * @var int $mapGroup
     */
    protected $mapGroup = null;

    /**
     * @var int $mapPriority
     */
    protected $mapPriority = null;

    /**
     * @var string $mapRule
     */
    protected $mapRule = null;

    /**
     * @var string $mapAdvice
     */
    protected $mapAdvice = null;

    /**
     * @var string $mapTarget
     */
    protected $mapTarget = null;

    /**
     * @var int $correlation
     */
    protected $correlation = null;

    /**
     * @var int $idCrossmapCategory
     */
    protected $idCrossmapCategory = null;

    /**
     * @var boolean $state
     */
    protected $state = null;

    /**
     * @param int $mapGroup
     * @param int $mapPriority
     * @param int $correlation
     * @param int $idCrossmapCategory
     * @param boolean $state
     */
    public function __construct($mapGroup, $mapPriority, $correlation, $idCrossmapCategory, $state)
    {
      $this->mapGroup = $mapGroup;
      $this->mapPriority = $mapPriority;
      $this->correlation = $correlation;
      $this->idCrossmapCategory = $idCrossmapCategory;
      $this->state = $state;
    }

    /**
     * @return int
     */
    public function getMapGroup()
    {
      return $this->mapGroup;
    }

    /**
     * @param int $mapGroup
     * @return IndirectCrossmap
     */
    public function setMapGroup($mapGroup)
    {
      $this->mapGroup = $mapGroup;
      return $this;
    }

    /**
     * @return int
     */
    public function getMapPriority()
    {
      return $this->mapPriority;
    }

    /**
     * @param int $mapPriority
     * @return IndirectCrossmap
     */
    public function setMapPriority($mapPriority)
    {
      $this->mapPriority = $mapPriority;
      return $this;
    }

    /**
     * @return string
     */
    public function getMapRule()
    {
      return $this->mapRule;
    }

    /**
     * @param string $mapRule
     * @return IndirectCrossmap
     */
    public function setMapRule($mapRule)
    {
      $this->mapRule = $mapRule;
      return $this;
    }

    /**
     * @return string
     */
    public function getMapAdvice()
    {
      return $this->mapAdvice;
    }

    /**
     * @param string $mapAdvice
     * @return IndirectCrossmap
     */
    public function setMapAdvice($mapAdvice)
    {
      $this->mapAdvice = $mapAdvice;
      return $this;
    }

    /**
     * @return string
     */
    public function getMapTarget()
    {
      return $this->mapTarget;
    }

    /**
     * @param string $mapTarget
     * @return IndirectCrossmap
     */
    public function setMapTarget($mapTarget)
    {
      $this->mapTarget = $mapTarget;
      return $this;
    }

    /**
     * @return int
     */
    public function getCorrelation()
    {
      return $this->correlation;
    }

    /**
     * @param int $correlation
     * @return IndirectCrossmap
     */
    public function setCorrelation($correlation)
    {
      $this->correlation = $correlation;
      return $this;
    }

    /**
     * @return int
     */
    public function getIdCrossmapCategory()
    {
      return $this->idCrossmapCategory;
    }

    /**
     * @param int $idCrossmapCategory
     * @return IndirectCrossmap
     */
    public function setIdCrossmapCategory($idCrossmapCategory)
    {
      $this->idCrossmapCategory = $idCrossmapCategory;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getState()
    {
      return $this->state;
    }

    /**
     * @param boolean $state
     * @return IndirectCrossmap
     */
    public function setState($state)
    {
      $this->state = $state;
      return $this;
    }

}
