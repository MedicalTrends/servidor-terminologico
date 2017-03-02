<?php

namespace Semantikos\ClientBundle\API;

class descripcionesPendientes
{

    /**
     * @var DescripcionPendiente $descripcionPendiente
     */
    protected $descripcionPendiente = null;

    /**
     * @param DescripcionPendiente $descripcionPendiente
     */
    public function __construct($descripcionPendiente)
    {
      $this->descripcionPendiente = $descripcionPendiente;
    }

    /**
     * @return DescripcionPendiente
     */
    public function getDescripcionPendiente()
    {
      return $this->descripcionPendiente;
    }

    /**
     * @param DescripcionPendiente $descripcionPendiente
     * @return descripcionesPendientes
     */
    public function setDescripcionPendiente($descripcionPendiente)
    {
      $this->descripcionPendiente = $descripcionPendiente;
      return $this;
    }

}
