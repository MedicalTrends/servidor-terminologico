<?php

namespace Semantikos\ClientBundle\API;


class descriptionIDorConceptIDRequest
{

    /**
     * @var string $description_id
     */
    protected $description_id = null;

    /**
     * @var string $concept_id
     */
    protected $concept_id = null;

    /**
     * @var string $stablishment_id
     */
    protected $stablishment_id = null;

    /**
     * @param string $description_id
     * @param string $concept_id
     * @param string $stablishment_id
     */
    public function __construct()
    {      
    }

    /**
     * @return string
     */
    public function getDescription_id()
    {
      return $this->description_id;
    }

    /**
     * @param string $description_id
     * @return descriptionIDorConceptIDRequest
     */
    public function setDescription_id($description_id)
    {
      $this->description_id = $description_id;
      return $this;
    }

    /**
     * @return string
     */
    public function getConcept_id()
    {
      return $this->concept_id;
    }

    /**
     * @param string $concept_id
     * @return descriptionIDorConceptIDRequest
     */
    public function setConcept_id($concept_id)
    {
      $this->concept_id = $concept_id;
      return $this;
    }

    /**
     * @return string
     */
    public function getStablishment_id()
    {
      return $this->stablishment_id;
    }

    /**
     * @param string $stablishment_id
     * @return descriptionIDorConceptIDRequest
     */
    public function setStablishment_id($stablishment_id)
    {
      $this->stablishment_id = $stablishment_id;
      return $this;
    }

}
