$(function() {
	var serverIp = "slide.jingkao.net:3102";
	/*当前用户*/
	var currentUser = $("#identity").val();
	/*启动websocket连接*/
	websocket();
	/*websocket*/
	var webSocket;
	/*页码与当前页*/
	/*var pptNum = ;*/
	
	/*过渡动画*/
	var transition = 0;
	/*提示信息*/
	$(".tooltips").addClass("show");
	$(".tooltips").click(function() {
		$(this).addClass("hidden");
		$(".navBtn").addClass("show");
	});
	/*菜单事件*/
	/*文件按钮*/
	$("#foBtn").click(function() {
		$("#shadow").css("display", "block");
		$(".slideList").slideDown();
		$(".fileMenuList").hide();
		return false;
	});
	$("#fdBtn").click(function(){
		$(".fileMenuList").hide();
		$.get("download?assetsSrc="+$("#slidePos").val(),function(data){
			window.location.href="http://localhost:9000/upload/demo.osl";
		});
		return false;
	});
	$(".fileMenu").click(function() {
		$(".fileMenuList").show();
	});
	$(".cancelBtn").click(function(screenShow) {
		$(".fileMenuList").hide();
	});
	/*幻灯片列表*/
	$(".slideCloseBtn").click(function() {
		$("#shadow").css("display", "none");
		$(".slideList").fadeOut();
		return false;
	});
	/*主题按钮*/
	$(".themeMenu").click(function() {
		$("#shadow").css("display", "block");
		$("#themeModal").fadeIn();
		return false;
	});
	$(".themeCloseBtn").click(function() {
		$("#shadow").css("display", "none");
		$("#themeModal").fadeOut();
		return false;
	});
	$(".themeBox").click(function() {
		$(".themeBox").removeClass("selected");
		$(this).addClass("selected");
		return false;
	});
	$(".themeConfirm").click(function() {
		var seleTheme = $(".themeBox.selected");
		if (seleTheme.attr("href") == undefined)
			alert("请选中一种主题");
		else {
			theme = seleTheme.attr("href");
			if (theme == 0) {
				$(".pptPage").css("background", "#FFF");
			} else if (theme == 1) {
				$(".pptPage").css("background", "-moz-radial-gradient(#FFFFFF 0%,#F1F1F1 50%,#CCCCCC 100%)");
				$(".pptPage").css("background","-webkit-radial-gradient(#FFFFFF 5%,#F1F1F1 25%,#D7D7D7 50%,#BDBDBD 100%)");
			} else if (theme == 2) {
				$(".pptPage").css("background-image", "url(/public/images/skulls.png)");
			} else if (theme == 3) {
				$(".pptPage").css("background-image", "url(/public/images/light_grey.png)");
			} else if (theme == 4) {
				$(".pptPage").css("background", "blue");
			} else if (theme == 5) {
				$(".pptPage").css("background", "-moz-radial-gradient(#8B8B8B 0%,#797979 50%,#444444 100%)");
				$(".pptPage").css("background", "-webkit-radial-gradient(#8B8B8B 0%,#797979 50%,#444444 100%)");
			} else if (theme == 6) {
				$(".pptPage").css("background-image", "url(/public/images/rainbow.jpg)");
			} else if (theme == 7) {
				$(".pptPage").css("background-image", "url(/public/images/gplaypattern.png)");
			}
		}
		$("#shadow").css("display", "none");
		$("#themeModal").fadeOut();
		return false;
	});
	/*动画按钮*/
	$(".tranMenu").click(function() {
		$("#shadow").css("display", "block");
		$("#transModal").fadeIn();
		return false;
	});
	$(".transCloseBtn").click(function() {
		$("#shadow").css("display", "none");
		$("#transModal").fadeOut();
		return false;
	});
	$(".transBox").click(function() {
		/*$(".transBox").css({
			"border": "2px solid #DDDDDD",
			"opacity": "0.6"
		});*/
		$(".transBox").removeClass("selected");
		/*$(this).css({
			"border": "2px solid #4A8BF5",
			"opacity": "1"
		});*/
		$(this).addClass("selected");
		return false;
	});
	$(".transConfirm").click(function() {
		var seleTrans = $(".transBox.selected");
		if (seleTrans.attr("href") == undefined)
			alert("请选中一种过渡动画");
		else
			transition = seleTrans.attr("href");
		$("#shadow").css("display", "none");
		$("#transModal").fadeOut();
		return false;
	});
	/*播放按钮*/
	$(".playMenu").click(function() {
		fullScreenPlay();
	});

	/*帮助按钮*/
	$(".helpMenu").click(function() {
		alert("SCAU HCI");
	});
	/*上一页下一页按钮*/
	$(".prevBtn").click(function() {
		slideLeft();
		return false;
	});
	$(".nextBtn").click(function() {
		slideRight();
		return false;
	});
	/*键盘监听事件*/
	$(document).keydown(function(e) {
		/*向左键*/
		if (e.keyCode == 37) {
			slideLeft();
		}
		/*向上键*/
		if (e.keyCode == 38) {
			scaleLarger();
		}
		/*向右键*/
		if (e.keyCode == 39) {
			slideRight();
		}
		/*向下键*/
		if (e.keyCode == 40) {
			scaleSmaller();
		}
		/*s键*/
		if (e.keyCode == 83) {
			$("#fileMsg").slideDown();
			$("#menuBar").slideDown();
		}
		/*n键-3D放大*/
		if(e.keyCode == 78){
			scale3DLarger();
		}
		/*n键-3D缩小*/
		if(e.keyCode == 77){
			scale3DSmaller();
		}
	});
	if (window.screen.width < 480) {
		phoneAdapt();
	}
	var scaleSize = 1;
	/*放大播放*/
	function scaleLarger(){
		scaleSize = scaleSize+0.15;
		if(scaleSize>=2){
			scaleSize = 1.9;
		}
    	$(".screenShow").css({"-moz-transform":"scale("+scaleSize+","+scaleSize+")","-webkit-transform":"scale("+scaleSize+","+scaleSize+")"});
	}
	/*3D放大*/
	function scale3DLarger(){
		scaleSize = scaleSize+0.15;
		if(scaleSize>=2){
			scaleSize = 1.9;
		}
    	$(".screenShow").css({"-moz-transform":"rotate(360deg) rotateX(360deg) rotateY(360deg) scale("+scaleSize+","+scaleSize+")","-webkit-transform":"rotate(360deg) rotateX(360deg) rotateY(360deg) scale("+scaleSize+","+scaleSize+")"});
	}
	/*缩小播放*/
	function scaleSmaller(){
		scaleSize = scaleSize-0.15;
		if(scaleSize<=0){
			scaleSize = 0.1;
		}
		$(".screenShow").css({"-moz-transform":"scale("+scaleSize+","+scaleSize+")","-webkit-transform":"scale("+scaleSize+","+scaleSize+")"});
	}
	/*3D缩小*/
	function scale3DSmaller(){
		scaleSize = scaleSize-0.15;
		if(scaleSize<=0){
			scaleSize = 0.1;
		}
		$(".screenShow").css({"-moz-transform":"rotate(-360deg) rotateX(-360deg) rotateY(-360deg) scale("+scaleSize+","+scaleSize+")","-webkit-transform":"rotate(-360deg) rotateX(-360deg) rotateY(-360deg) scale("+scaleSize+","+scaleSize+")"});
	}
	/*上一页ppt动画*/
	function slideLeft() {
		$("#slideCanvas").remove();
		if ($("#fileMsg").css("display") != "none"){
			$("#fileMsg").slideUp();
			$(".topSlideMsg").slideDown();
			$(".bottomSlideMsg").slideDown();
		}
		if ($("#menuBar").css("display") != "none"){
			$("#menuBar").slideUp();
			$(".topSlideMsg").slideDown();
			$(".bottomSlideMsg").slideDown();
		}
		if (current == 0) {
			$("#tips").html("已经是第一页了");
			$("#tips").fadeIn(1000);
			$("#tips").fadeOut(1000);
		} else {
			current = current - 1;
			$(".currentPage").html(current+1);
			if (currentUser == "1") {
				try{
					webSocket.send("jumpTo:" + current+","+presId);
				}
				catch(e){
					console.log(e);
				}
			}
			if (transition == 0) {
				$(".pptPage").css("display", "none");
				$("#pptPage" + current).css("display", "block");
			} else if (transition == 1) {
				$(".pptPage").css("display", "none");
				$("#pptPage" + current).fadeIn(1000);
			} else if (transition == 2) {
				$("#pptPage" + (current + 1)).hide("slide", {
					direction: 'right'
				}, 500, function() {
					$("#pptPage" + current).show("slide", {
						direction: 'left'
					}, 500);
				});
			} else if (transition == 3) {
				$("#pptPage" + (current + 1)).hide("fold", 500, function() {
					$("#pptPage" + current).show("fold", 500);
				});
			} else if (transition == 4) {
				$("#pptPage" + (current + 1)).css("display", "none");
				$("#pptPage" + current).show("bounce", {
					times: 3
				}, "slow");
			} else if (transition == 5) {
				$("#pptPage" + (current + 1)).hide("explode", function() {
					$("#pptPage" + current).show("explode");
				});
			} else if (transition == 6) {
				$("#pptPage" + (current + 1)).hide("pulsate", function() {
					$("#pptPage" + current).show("pulsate");
				});
			}
		}
	}

	/*下一页ppt动画*/
	function slideRight() {
		$("#slideCanvas").remove();
		if ($("#fileMsg").css("display") != "none"){
			$("#fileMsg").slideUp();
			$(".topSlideMsg").slideDown();
			$(".bottomSlideMsg").slideDown();
		}
		if ($("#menuBar").css("display") != "none"){
			$("#menuBar").slideUp();
			$(".topSlideMsg").slideDown();
			$(".bottomSlideMsg").slideDown();
		}
		if (current == $("#pageNum").text() - 1) {
			$("#tips").html("已经是最后一页了");
			$("#tips").fadeIn(1000);
			$("#tips").fadeOut(2000);
		} else {
			current = parseInt(current) + 1;
			$(".currentPage").html(current+1);
			if (currentUser == "1") {
				try{
					webSocket.send("jumpTo:" + current+","+presId);
				}
				catch(e){
					console.log(e);
				}
			}
			if (transition == 0) {
				$(".pptPage").css("display", "none");
				$("#pptPage" + current).css("display", "block");
			} else if (transition == 1) {
				$(".pptPage").css("display", "none");
				$("#pptPage" + current).fadeIn(1000);
			} else if (transition == 2) {
				$("#pptPage" + (current - 1)).hide("slide", {
					direction: 'left'
				}, 500, function() {
					$("#pptPage" + current).show("slide", {
						direction: 'right'
					}, 500);
				});
			} else if (transition == 3) {
				$("#pptPage" + (current - 1)).hide("fold", 500, function() {
					$("#pptPage" + current).show("fold", 500);
				});
			} else if (transition == 4) {
				$("#pptPage" + (current - 1)).css("display", "none");
				$("#pptPage" + current).show("bounce", {
					times: 3
				}, "slow");
			} else if (transition == 5) {
				$("#pptPage" + (current - 1)).hide("explode", function() {
					$("#pptPage" + current).show("explode");
				});
			} else if (transition == 6) {
				$("#pptPage" + (current - 1)).hide("pulsate", function() {
					$("#pptPage" + current).show("pulsate");
				});
			}
		}
	}

	/*任意一ppt动画*/
	function slideToRandom(crtPage) {
		$("#slideCanvas").remove();
		$(".currentPage").html(parseInt(crtPage)+parseInt(1));
		if ($("#fileMsg").css("display") != "none"){
			$("#fileMsg").slideUp();
			$(".topSlideMsg").slideDown();
			$(".bottomSlideMsg").slideDown();
		}
		if ($("#menuBar").css("display") != "none"){
			$("#menuBar").slideUp();
			$(".topSlideMsg").slideDown();
			$(".bottomSlideMsg").slideDown();
		}
		if (transition == 0) {
			$(".pptPage").css("display", "none");
			$("#pptPage" + crtPage).css("display", "block");

		} else if (transition == 1) {
			$(".pptPage").css("display", "none");
			$("#pptPage" + crtPage).fadeIn(1000);
		} else if (transition == 2) {
			var direction1 = 'left';
			var direction2 = 'right';
			if (crtPage < current) {
				direction1 = 'right';
				direction2 = 'left';
			}
			$("#pptPage" + current).hide("slide", {
				direction: direction1
			}, 500, function() {
				$("#pptPage" + crtPage).show("slide", {
					direction: direction2
				}, 500);
			});
		} else if (transition == 3) {
			$("#pptPage" + current).hide("fold", 500, function() {
				$("#pptPage" + crtPage).show("fold", 500);
			});
		} else if (transition == 4) {
			$("#pptPage" + current).css("display", "none");
			$("#pptPage" + crtPage).show("bounce", {
				times: 3
			}, "slow");
		} else if (transition == 5) {
			$("#pptPage" + current).hide("explode", function() {
				$("#pptPage" + crtPage).show("explode");
			});
		} else if (transition == 6) {
			$("#pptPage" + current).hide("pulsate", function() {
				$("#pptPage" + crtPage).show("pulsate");
			});
		}
		current = crtPage;
	}

	function fullScreenPlay() {
		/*设置浏览器窗口信息*/
		/*浏览器时下窗口文档的高度--$(document).height()*/
		var browserHeight = window.screen.height;
		var slideHeightStr = $(".screenShow").css("height");
		var slideHeight = slideHeightStr.substring(0, slideHeightStr.length - 2);
		var r = browserHeight / slideHeight;
		$(".screenShow").fullScreen();
		$(".screenShow").css("-webkit-transform", "scale(" + r + "," + r + ")");
		$(".screenShow").css("-moz-transform", "scale(" + r + "," + r + ")");
		return false;
	}

	function websocket() {
		var identity = currentUser;
		if (!window.WebSocket && window.MozWebSocket)
			window.WebSocket = window.MozWebSocket;
		if (!window.WebSocket) {
			alert("此浏览器不支持WebSocket");
		}
		var wsServer = 'ws://' + serverIp + '/ws?identity=' + identity;
		webSocket = new WebSocket(wsServer);
		webSocket.onopen = function(evt) {
			onOpen(evt)
		};
		webSocket.onclose = function(evt) {
			console.log(evt);
			webSocket = new WebSocket(wsServer);
		}
		webSocket.onerror = function(evt) {
			console.log(evt);
		}

		function onOpen(evt) {
			var username = $("#username").html();
			try{
				webSocket.send(username);
			}
			catch(e){
				console.log(e);
			}
		}
		webSocket.onmessage = function(evt) {
			if (evt.data.indexOf("jumpTo:") >= 0) {
				var comStr = evt.data.slice(7);
				var homeId = comStr.split(",")[1];
				if(homeId == presId){
					slideToRandom(comStr.split(",")[0]);
				}
			} else if (evt.data == "fullScreen") {
				fullScreenPlay();
			} else {
				alert("response: " + evt.data);
			}
		}
	}

	/*手机适配*/
	function phoneAdapt() {
		transition = 1;
		$(document.body).append("<div id='phoneMenu'>Web Slider On Phone</div><div id='phonePage'></div>");
		$(document.body).append("<div class='controlMenu'>");
		$(document.body).append("<a href='#' id='phonePageU' class='phoneBtn'>Prev</a>");
		$(document.body).append("<a href='#' id='phonePageD' class='phoneBtn'>Next</a>");
		$(document.body).append("<a href='#' id='phoneFullS' class='phoneBtn'>Menu</a>");
		$(document.body).append("<div class='bottomPane'>打造自己的个性幻灯片</div>");
		$(document.body).append("</div>");
		$(document.body).bind("touchstart", function(e) {
			e.preventDefault();
		});

		$("#phonePageU").bind("touchend", function(e) {
			// alert(current);
			slideLeft();
		});
		$("#phonePageD").bind("touchend", function(e) {
			// alert(current);
			slideRight();
		});
		$("#phoneFullS").bind("touchend", function(e) {
			$("#shadow").css("display", "block");
			$(".slideList").slideDown();
			$(".fileMenuList").hide();
			return false;
		});
		$(".modalCancel").bind("touchend", function(e) {
			$("#shadow").css("display", "none");
			$(".slideList").fadeOut();
		});
		$(".slideCloseBtn").bind("touchend", function(e) {
			$("#shadow").css("display", "none");
			$(".slideList").fadeOut();
		});
	}

});