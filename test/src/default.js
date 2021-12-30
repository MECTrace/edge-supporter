$(document).ready(function(){

	// 등록
	$(".action_register").click(function(){
		$.register();
	});

    $(".action_send_packet").click(function(){
		var idx = $(this).attr("idx");
		$.send_packet(idx);
	});
});