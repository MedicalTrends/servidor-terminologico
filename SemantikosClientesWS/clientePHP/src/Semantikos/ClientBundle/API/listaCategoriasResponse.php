<?php

namespace Semantikos\ClientBundle\API;

class listaCategoriasResponse
{

    /**
     * @var Categoria $categoria
     */
    protected $categoria = null;

    /**
     * @param Categoria $categoria
     */
    public function __construct($categoria)
    {
      $this->categoria = $categoria;
    }

    /**
     * @return Categoria
     */
    public function getCategoria()
    {
      return $this->categoria;
    }

    /**
     * @param Categoria $categoria
     * @return listaCategoriasResponse
     */
    public function setCategoria($categoria)
    {
      $this->categoria = $categoria;
      return $this;
    }

}
