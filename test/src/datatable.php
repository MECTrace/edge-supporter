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
        $length = (isset($post["length"]))?     $post["length"]:"";
        $order  = (isset($post["order"][0]))?   $post["order"][0]:"";
        $search = (isset($post["search"]))?     $post["search"]:"";

        $val = array(
            "draw"     => $draw
            ,"start"    => $start
            ,"length"   => $length
            ,"order"    => $order
            ,"search"   => $search
        );

    return $val;	
    }
}

public function registerAction()
{


    $val = array(
//			"payload_type" => $payload_type
    );

    echo only_contents($val);
    die();

}