<?php

namespace Semantikos\ClientBundle\API;

class CrossmapSet
{

    /**
     * @var string $abbreviatedName
     */
    protected $abbreviatedName = null;

    /**
     * @var string $name
     */
    protected $name = null;

    /**
     * @var int $version
     */
    protected $version = null;

    /**
     * @param int $version
     */
    public function __construct($version)
    {
      $this->version = $version;
    }

    /**
     * @return string
     */
    public function getAbbreviatedName()
    {
      return $this->abbreviatedName;
    }

    /**
     * @param string $abbreviatedName
     * @return CrossmapSet
     */
    public function setAbbreviatedName($abbreviatedName)
    {
      $this->abbreviatedName = $abbreviatedName;
      return $this;
    }

    /**
     * @return string
     */
    public function getName()
    {
      return $this->name;
    }

    /**
     * @param string $name
     * @return CrossmapSet
     */
    public function setName($name)
    {
      $this->name = $name;
      return $this;
    }

    /**
     * @return int
     */
    public function getVersion()
    {
      return $this->version;
    }

    /**
     * @param int $version
     * @return CrossmapSet
     */
    public function setVersion($version)
    {
      $this->version = $version;
      return $this;
    }

}
