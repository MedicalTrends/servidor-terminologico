<?php

namespace Semantikos\ClientBundle\API;

class Stub {
    
  function Stub($func_args) {
      
    $iCount = count($func_args);
    
    for ($i = 0; $i < $iCount; $i++) {
      eval('$this->arg'.$i.' = $func_args[$i];');
    }
    
  }
  
}
