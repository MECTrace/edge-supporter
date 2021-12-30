<?php
return array(
//add routes
    "routes" => array(

        "route" => "/issued",

        "constraints" => array(
            "module",
            "controller",
            "action",
            "arg1",
            "arg2",
            "arg3",
        ),

        "defaults" => array(
            "controller"    => "index",
            "action"        => "index",
        ),
)