<?php

namespace Semantikos\ClientBundle\API;

use JMS\Serializer\Annotation\SerializedName;
use JMS\Serializer\Annotation\Type;


class PeticionCodificacionDeNuevoTermino
{

    /**
     * @var string $establecimiento
     * @Type("string")
     */
    public $establecimiento = null;

    /**
     * @var string $idConcepto
     * @Type("string")
     */
    public $idConcepto = null;

    /**
     * @var string $termino
     * @Type("string")
     */
    public $termino = null;

    /**
     * @var string $tipoDescripcion
     * @Type("string")
     */
    public $tipoDescripcion = null;

    /**
     * @var boolean $esSensibleAMayusculas
     * @Type("string")
     */
    public $esSensibleAMayusculas = null;

    /**
     * @var string $email
     * @Type("string")
     */
    public $email = null;

    /**
     * @var string $observacion
     * @Type("string")
     */
    public $observacion = null;

    /**
     * @var string $profesional
     * @Type("string")
     */
    public $profesional = null;

    /**
     * @var string $profesion
     * @Type("string")
     */
    public $profesion = null;

    /**
     * @var string $especialidad
     * @Type("string")
     */
    public $especialidad = null;

    /**
     * @var string $subespecialidad
     * @SerializedName("sub-especialidad")
     * @Type("string")
     */
    public $subespecialidad = null;

    /**
     * @var string $categoria
     * @Type("string")
     */
    public $categoria = null;

    /**
     * @param string $establecimiento
     * @param string $idConcepto
     * @param string $termino
     * @param string $subespecialidad
     * @param string $categoria
     */
    public function __construct()
    {
    }

    /**
     * @return string
     */
    public function getEstablecimiento()
    {
      return $this->establecimiento;
    }

    /**
     * @param string $establecimiento
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setEstablecimiento($establecimiento)
    {
      $this->establecimiento = $establecimiento;
      return $this;
    }

    /**
     * @return string
     */
    public function getIdConcepto()
    {
      return $this->idConcepto;
    }

    /**
     * @param string $idConcepto
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setIdConcepto($idConcepto)
    {
      $this->idConcepto = $idConcepto;
      return $this;
    }

    /**
     * @return string
     */
    public function getTermino()
    {
      return $this->termino;
    }

    /**
     * @param string $termino
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setTermino($termino)
    {
      $this->termino = $termino;
      return $this;
    }

    /**
     * @return string
     */
    public function getTipoDescripcion()
    {
      return $this->tipoDescripcion;
    }

    /**
     * @param string $tipoDescripcion
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setTipoDescripcion($tipoDescripcion)
    {
      $this->tipoDescripcion = $tipoDescripcion;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getEsSensibleAMayusculas()
    {
      return $this->esSensibleAMayusculas;
    }

    /**
     * @param boolean $esSensibleAMayusculas
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setEsSensibleAMayusculas($esSensibleAMayusculas)
    {
      $this->esSensibleAMayusculas = $esSensibleAMayusculas;
      return $this;
    }

    /**
     * @return string
     */
    public function getEmail()
    {
      return $this->email;
    }

    /**
     * @param string $email
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setEmail($email)
    {
      $this->email = $email;
      return $this;
    }

    /**
     * @return string
     */
    public function getObservacion()
    {
      return $this->observacion;
    }

    /**
     * @param string $observacion
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setObservacion($observacion)
    {
      $this->observacion = $observacion;
      return $this;
    }

    /**
     * @return string
     */
    public function getProfesional()
    {
      return $this->profesional;
    }

    /**
     * @param string $profesional
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setProfesional($profesional)
    {
      $this->profesional = $profesional;
      return $this;
    }

    /**
     * @return string
     */
    public function getProfesion()
    {
      return $this->profesion;
    }

    /**
     * @param string $profesion
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setProfesion($profesion)
    {
      $this->profesion = $profesion;
      return $this;
    }

    /**
     * @return string
     */
    public function getEspecialidad()
    {
      return $this->especialidad;
    }

    /**
     * @param string $especialidad
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setEspecialidad($especialidad)
    {
      $this->especialidad = $especialidad;
      return $this;
    }

    /**
     * @return string
     */
    public function getSubespecialidad()
    {
      return $this->subespecialidad;
    }

    /**
     * @param string $subespecialidad
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setSubespecialidad($subespecialidad)
    {
      $this->subespecialidad = $subespecialidad;
      return $this;
    }

    /**
     * @return string
     */
    public function getCategoria()
    {
      return $this->categoria;
    }

    /**
     * @param string $categoria
     * @return PeticionCodificacionDeNuevoTermino
     */
    public function setCategoria($categoria)
    {
      $this->categoria = $categoria;
      return $this;
    }

}
