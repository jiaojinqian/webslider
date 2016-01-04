$(function(){
	document.onmousemove = function(e){
		if(window.screen.width<480)
			return false;
		if(e.clientY <= 70 || e.clientY >= (window.screen.height-70)){
			if ($("#fileMsg").css("display") == "none"){
				$("#fileMsg").slideDown();
				$("#menuBar").slideDown();
			}
		}
	}  
});