<?php

namespace classes\user\datatable;

trait datatable
{
    public function dt_init($post)
//require function definition
    {
/*test*/ 
        $draw   = (isset($post["draw"]))?       $post["draw"]:"";
        $start  = (isset($post["start"]))?      $post["start"]:"";

        $val = array(
            "draw"     => $draw
            ,"start"    => $start
        );

    return $val;	
    }
}
