<?php

namespace classes\user\datatable;

trait datatable
{
    public function dt_init($post)
//require function definition
    {
/*test*/
    $draw   = (isset($post["draw"]))?       $post["draw"]:"";

    $val = array(
        "draw"     => $draw
    );

    return $val;	
}
}