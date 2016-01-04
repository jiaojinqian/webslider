/*AJAX文件上传*/
$(function(){
	$("#pptFile").click(function(){
		$.upload({
			url:'upload',
			fileName:'pptFile',
			params:{name:'pxblog'},
			dataType:'json',
			onSend:function(){
				return true;
			},
			onComplate:function(data){
			$(".slideList  table").append("<tr><td><a href='"+data["presPath"]+"' class='slideHref'>"+data["presName"]+"<div class='editDate'>"+data["uploadAt"]+"</div></a></td></tr>");
			}
		});
		return false;
	});
});