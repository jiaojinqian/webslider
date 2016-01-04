$(function() {
	/*canvas绘图*/
	var canvas;
	var context;
	var clickX = new Array();
	var clickY = new Array();
	var clickDrag = new Array();
	var flag;
	var point = {};
	var offsetlef;
	var offsettop;

	point.notFirst = false;

	$("#fdrawBtn").click(function() {
		var pageWidth = $(".pptPage").css("width");
		var pageHeight = $(".pptPage").css("height");
		/*scale = window.screen.height/pageHeight;*/
		$("#slideCanvas").remove();
		$(".screenShow").append("<canvas id='slideCanvas' width='" + pageWidth + "' height='" + pageHeight + "'>不支持画板！</canvas>");
		/*设置Canvas画板样式*/
			$("#slideCanvas").css({
				"position": "absolute",
				"left": "0",
				"top": "0"
			});
		canvas = document.getElementById('slideCanvas');
		context = canvas.getContext('2d');
		draw();
	});

	function draw() {
		canvas.onmousedown = function(e) {
			flag = true;
			offsetleft = $(".screenShow").offset().left;
			offsettop = $(".screenShow").offset().top;
			addClick(e.screenX - offsetleft, e.screenY - offsettop);
			redraw();
		};

		canvas.onmousemove = function(e) {
			if (flag) {
				addClick(e.screenX - offsetleft, e.screenY - offsettop, true);
				redraw();
			}
		};

		canvas.onmouseup = function(e) {
			flag = false;
		}

		canvas.onmouseout = function(e) {
			flag = false;
		}
	}

	function addClick(x, y, dragging) {
		clickX.push(x);
		clickY.push(y);
		clickDrag.push(dragging);
	}

	function redraw() {
		context.strokeStyle = '#df4b26';
		context.lineJoin = 'round';
		context.lineWidth = 5;
		while (clickX.length > 0) {
			point.bx = point.x;
			point.by = point.y;
			point.x = clickX.pop()/scale;
			point.y = clickY.pop()/scale;
			point.drag = clickDrag.pop();
			context.beginPath();
			if (point.drag && point.notFirst) {
				context.moveTo(point.bx, point.by);
			} else {
				point.notFirst = true;
				context.moveTo(point.x - 1, point.y);
			}
			context.lineTo(point.x, point.y);
			context.closePath();
			context.stroke();
		}
	}
});