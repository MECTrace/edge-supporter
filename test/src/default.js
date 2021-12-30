$(document).ready(function(){

	// 등록
	$(".action_register").click(function(){
		$.register();
	});

    $(".action_send_packet").click(function(){
		var idx = $(this).attr("idx");
		$.send_packet(idx);
	});

    $(".action_apply_rule").click(function(){
		$.apply_rule();
	});
});

$.extend({

    //need register function

    "register": function(){

        $.ajax({
			url: "/filter/list/register",
			dataType: "html",
			type: "POST",
			success: function(res){
				$("#default-modal .modal-content").html(res);
				$("#default-modal").modal();

				// register submit
				$(".action_register_submit").click(function(){
					$.register_submit();	
				});

				// show from payload
				$("#register_payload_type").change(function(){
					$(".form_payload").hide();	
					
					var target_id = $(this).val();
					$("#"+target_id).show();
				});
				
				// popover
				$('[data-toggle="popover"]').popover({
					"placement":	"left",
					"trigger":		"focus",
				});
			},
			error: function(res){
				console.log(res);	
			}
		});
    },

    //submit 
    "register_submit": function(){
        var payload_type = $("#register_payload_type").val();
        var obj = $("#register_wrap .form_payload:visible input:text");

        var new_payload = "";
        $.each(obj, function(key, val){
			ek = $(this).attr("name");
			ev = $(this).val();

			new_payload += ek+"="+ev+"&";
		})

        new_payload += "type="+payload_type;
        if($("#register_wrap select[name='nack_code']")){
			new_payload += "&nack_code="+$("#register_wrap select[name='nack_code']").val();
		}

        //test log
        console.log(new_payload);
    },

    /*to do
    $.ajax({
        //register submit f
    })*/


    "send_packet": function(idx){
		$.ajax({
			url: "/filter/list/send_packet",
			dataType: "json",
			data: {"idx": idx},
			type: "POST",
			success: function(res){

				if(res.result == "send success"){
					alert("Success");	
				}else{
					alert("fail");	
				}
			},error: function(res){
				console.log(res);	
			}
		});
	},

    "apply_rule": function()
    {
        //add apply_rule

        dataType: "json",
        type: "POST",
        success: function(res){
            if(res.result == "success"){
                alert("Success");	
                else{
					alert("fail");	
				}
        }
    }
});
