<?php

namespace Semantikos\ClientBundle\API;

class Descripcion
{

    /**
     * @var string $id
     */
    protected $id = null;

    /**
     * @var string $tipo
     */
    protected $tipo = null;

    /**
     * @var string $termino
     */
    protected $termino = null;

    /**
     * @var boolean $valida
     */
    protected $valida = null;

    /**
     * @var boolean $esSensibleAMayusculas
     */
    protected $esSensibleAMayusculas = null;

    /**
     * @var boolean $nombreAutgenerado
     */
    protected $nombreAutgenerado = null;

    /**
     * @var boolean $publicado
     */
    protected $publicado = null;

    /**
     * @var boolean $modelado
     */
    protected $modelado = null;

    /**
     * @var \DateTime $validaHasta
     */
    protected $validaHasta = null;

    /**
     * @var \DateTime $creado
     */
    protected $creado = null;

    /**
     * @var int $usos
     */
    protected $usos = null;

    /**
     * @var Usuario $usuarioCreador
     */
    protected $usuarioCreador = null;

    /**
     * @var Concepto $concepto
     */
    protected $concepto = null;

    /**
     * @param Concepto $concepto
     */
    public function __construct($concepto)
    {
      $this->concepto = $concepto;
    }

    /**
     * @return string
     */
    public function getId()
    {
      return $this->id;
    }

    /**
     * @param string $id
     * @return Descripcion
     */
    public function setId($id)
    {
      $this->id = $id;
      return $this;
    }

    /**
     * @return string
     */
    public function getTipo()
    {
      return $this->tipo;
    }

    /**
     * @param string $tipo
     * @return Descripcion
     */
    public function setTipo($tipo)
    {
      $this->tipo = $tipo;
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
     * @return Descripcion
     */
    public function setTermino($termino)
    {
      $this->termino = $termino;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getValida()
    {
      return $this->valida;
    }

    /**
     * @param boolean $valida
     * @return Descripcion
     */
    public function setValida($valida)
    {
      $this->valida = $valida;
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
     * @return Descripcion
     */
    public function setEsSensibleAMayusculas($esSensibleAMayusculas)
    {
      $this->esSensibleAMayusculas = $esSensibleAMayusculas;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getNombreAutgenerado()
    {
      return $this->nombreAutgenerado;
    }

    /**
     * @param boolean $nombreAutgenerado
     * @return Descripcion
     */
    public function setNombreAutgenerado($nombreAutgenerado)
    {
      $this->nombreAutgenerado = $nombreAutgenerado;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getPublicado()
    {
      return $this->publicado;
    }

    /**
     * @param boolean $publicado
     * @return Descripcion
     */
    public function setPublicado($publicado)
    {
      $this->publicado = $publicado;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getModelado()
    {
      return $this->modelado;
    }

    /**
     * @param boolean $modelado
     * @return Descripcion
     */
    public function setModelado($modelado)
    {
      $this->modelado = $modelado;
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getValidaHasta()
    {
      if ($this->validaHasta == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->validaHasta);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $validaHasta
     * @return Descripcion
     */
    public function setValidaHasta(\DateTime $validaHasta = null)
    {
      if ($validaHasta == null) {
       $this->validaHasta = null;
      } else {
        $this->validaHasta = $validaHasta->format(\DateTime::ATOM);
      }
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getCreado()
    {
      if ($this->creado == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->creado);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $creado
     * @return Descripcion
     */
    public function setCreado(\DateTime $creado = null)
    {
      if ($creado == null) {
       $this->creado = null;
      } else {
        $this->creado = $creado->format(\DateTime::ATOM);
      }
      return $this;
    }

    /**
     * @return int
     */
    public function getUsos()
    {
      return $this->usos;
    }

    /**
     * @param int $usos
     * @return Descripcion
     */
    public function setUsos($usos)
    {
      $this->usos = $usos;
      return $this;
    }

    /**
     * @return Usuario
     */
    public function getUsuarioCreador()
    {
      return $this->usuarioCreador;
    }

    /**
     * @param Usuario $usuarioCreador
     * @return Descripcion
     */
    public function setUsuarioCreador($usuarioCreador)
    {
      $this->usuarioCreador = $usuarioCreador;
      return $this;
    }

    /**
     * @return Concepto
     */
    public function getConcepto()
    {
      return $this->concepto;
    }

    /**
     * @param Concepto $concepto
     * @return Descripcion
     */
    public function setConcepto($concepto)
    {
      $this->concepto = $concepto;
      return $this;
    }

}
