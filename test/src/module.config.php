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

        "view_manager" => array(
            //"layout"    	=> __DIR__ . DS . "view" . DS . "layout" . DS . "layout.phtml",
            "html_path"   	=> __DIR__ . DS . "view" . DS . "html",
            //"error"     => __DIR__ . DS . "view" . DS . "error" . DS . "error.phtml",
        ),
)