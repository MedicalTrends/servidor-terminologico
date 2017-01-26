<?php

namespace Semantikos\ClientBundle\API;

class Usuario
{

    /**
     * @var string $nombreUsuario
     */
    protected $nombreUsuario = null;

    /**
     * @var string $nombre
     */
    protected $nombre = null;

    /**
     * @var string $apellido
     */
    protected $apellido = null;

    /**
     * @var string $segundoApellido
     */
    protected $segundoApellido = null;

    /**
     * @var string $email
     */
    protected $email = null;

    /**
     * @var string $rut
     */
    protected $rut = null;

    /**
     * @var \DateTime $ultimoLogin
     */
    protected $ultimoLogin = null;

    /**
     * @var \DateTime $ultimoCambioPassword
     */
    protected $ultimoCambioPassword = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return string
     */
    public function getNombreUsuario()
    {
      return $this->nombreUsuario;
    }

    /**
     * @param string $nombreUsuario
     * @return Usuario
     */
    public function setNombreUsuario($nombreUsuario)
    {
      $this->nombreUsuario = $nombreUsuario;
      return $this;
    }

    /**
     * @return string
     */
    public function getNombre()
    {
      return $this->nombre;
    }

    /**
     * @param string $nombre
     * @return Usuario
     */
    public function setNombre($nombre)
    {
      $this->nombre = $nombre;
      return $this;
    }

    /**
     * @return string
     */
    public function getApellido()
    {
      return $this->apellido;
    }

    /**
     * @param string $apellido
     * @return Usuario
     */
    public function setApellido($apellido)
    {
      $this->apellido = $apellido;
      return $this;
    }

    /**
     * @return string
     */
    public function getSegundoApellido()
    {
      return $this->segundoApellido;
    }

    /**
     * @param string $segundoApellido
     * @return Usuario
     */
    public function setSegundoApellido($segundoApellido)
    {
      $this->segundoApellido = $segundoApellido;
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
     * @return Usuario
     */
    public function setEmail($email)
    {
      $this->email = $email;
      return $this;
    }

    /**
     * @return string
     */
    public function getRut()
    {
      return $this->rut;
    }

    /**
     * @param string $rut
     * @return Usuario
     */
    public function setRut($rut)
    {
      $this->rut = $rut;
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getUltimoLogin()
    {
      if ($this->ultimoLogin == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->ultimoLogin);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $ultimoLogin
     * @return Usuario
     */
    public function setUltimoLogin(\DateTime $ultimoLogin = null)
    {
      if ($ultimoLogin == null) {
       $this->ultimoLogin = null;
      } else {
        $this->ultimoLogin = $ultimoLogin->format(\DateTime::ATOM);
      }
      return $this;
    }

    /**
     * @return \DateTime
     */
    public function getUltimoCambioPassword()
    {
      if ($this->ultimoCambioPassword == null) {
        return null;
      } else {
        try {
          return new \DateTime($this->ultimoCambioPassword);
        } catch (\Exception $e) {
          return false;
        }
      }
    }

    /**
     * @param \DateTime $ultimoCambioPassword
     * @return Usuario
     */
    public function setUltimoCambioPassword(\DateTime $ultimoCambioPassword = null)
    {
      if ($ultimoCambioPassword == null) {
       $this->ultimoCambioPassword = null;
      } else {
        $this->ultimoCambioPassword = $ultimoCambioPassword->format(\DateTime::ATOM);
      }
      return $this;
    }

}
