$(function() {
	/*服务器地址*/
	var serverIp = "slide.jingkao.net";
	/*显示幻灯片列表*/
	setTimeout(
		function() {
			showSlideList();
		}, 1500
	);
	$('.slideListContent').on('click','.slideHref',function(){
		$("#slidePos").val($(this).attr("href"));
		getPresentation($(this).attr("href"));
		$(".slideList").slideUp();
		$("#shadow").css("display", "none");
		$(".welSite").css("display", "none");
		$(".currentPage").text("1");
		return false;
	});
	$(".slideHref").bind("touchend", function() {
		$("#slidePos").val($(this).attr("href"));
		getPresentation($(this).attr("href"));
		$(".slideList").slideUp();
		$("#shadow").css("display", "none");
		$(".welSite").css("display", "none");
		$("#phoneMenu").addClass("show");
		$(".controlMenu").addClass("highlight");
		return false;
	});

	function showSlideList() {
		$(".slideList").slideDown(500);
		$("#shadow").css("display", "block");
	}
	/*取得JSON文件并解析*/
	function getPresentation(srcJson) {
		current=0;
		/*清除之前的幻灯片*/
		$(".pptPage").remove();
		$(".serialNum").remove();
		$("#slideCanvas").remove();
		/*ajax调用json文件*/
		$.getJSON("http://" + serverIp + "/" + srcJson, function(data) {
			/*获取幻灯片页面信息*/
			var pptData = data;
			var pageWidth = pptData["property"]["pageWidth"] * 4.0 / 3;
			var pageHeight = pptData["property"]["pageHeight"] * 4.0 / 3;
			var pageNum = pptData["property"]["pageNum"];
			var fileTitle = pptData["property"]["fileTitle"];
			var fileName = pptData["property"]["fileName"];
			var editDate = pptData["property"]["editDate"];
			var bgColor = pptData["property"]["background"];
			presId = pptData["property"]["presId"];
			/*pptNum = pageNum;*/
			/*假如是移动端*/
			if (window.screen.width < 480) {
				$("#phoneMenu").html(fileTitle);
			}
			/*计算比例*/
			scale = window.screen.height/pageHeight;
			/*页面中输出信息*/
			$("#pageNum").html(pageNum);
			$(".topSlideMsg").html(fileName);
			$(".totalPage").html(pageNum);
			$(".fileDetail.left").html('<span class="fileName">' + fileName + '</span><br/>修改日期:<span class="editDate">' + editDate + '</span><br/>');
			$(".fileDetail.right").html('<span class="titleBar">标题:</span><span class="fileTitle">' + fileTitle + '</span><br/><span class="titleBar">页数:</span><span class="pageNum">' + pageNum + '</span>');


			/*插入幻灯片*/
			for (var i = 0; i < pageNum; i++) {
				$(".screenShow").append("<div id='pptPage" + i + "' class='pptPage step'></div>");
				j = i + 1;
				$("#pptPage" + i).append("<div class='serialNum' style='z-index:2'>" + j + "</div>");
				if (i == 0) {
					$("#pptPage0").addClass("crtPage");
				}
			}

			/*设置播放区域样式*/
			$(".screenShow").css({
				"width": pageWidth + "px",
				"height": pageHeight + "px"
			});
			/*设置幻灯片样式*/
			$(".pptPage").css({
				"background": "url(" + bgColor + ")",
				"width": pageWidth + "px",
				"height": pageHeight + "px"
			});
			
			/*获取每一张ppt中的内容*/
			$.each(pptData["pageContent"], function(i, pptItem) {
				$.each(pptItem, function(j, itemContent) {
					/*如果是文本内容*/
					if (itemContent["sign"] == 0) {
						var paddingLeft = itemContent["marginLeft"] * 4.0 / 3;
						var paddingRight = paddingLeft;
						var paddingBottom = itemContent["marginBottom"] * 4.0 / 3;
						var paddingTop = paddingBottom;
						var shapeWidth = itemContent["editWidth"] * 4.0 / 3 - paddingLeft - paddingRight;
						var shapeHeight = itemContent["editHeight"] * 4.0 / 3 - paddingTop - paddingBottom;
						var locateX = itemContent["locateX"] * 4.0 / 3;
						var locateY = itemContent["locateY"] * 4.0 / 3;

						if (itemContent["text-align"] == 1)
							align = "center";
						else if (itemContent["text-align"] == 2)
							align = "right";
						else if (itemContent["text-align"] == 0)
							align = "left";

						$("#pptPage" + i).append("<div id='" + j + i + "' class='shape'></div>");
						/*设置文本内容*/
						$("#" + j + i).append(itemContent["content"]);
						$("#" + j + i).css({
							"width": shapeWidth + "px",
							"height": shapeHeight + "px",
							"padding-left": paddingLeft + "px",
							"padding-right": paddingRight + "px",
							"padding-top": paddingTop + "px",
							"padding-bottom": paddingBottom + "px",
							"left": locateX + "px",
							"top": locateY + "px",

							"text-align": align
						});
					}
					/*如果是文本图形*/
					else if (itemContent["sign"] == 1) {
						var paddingLeft = itemContent["marginLeft"] * 4.0 / 3;
						var paddingRight = paddingLeft;
						var paddingBottom = itemContent["marginBottom"] * 4.0 / 3;
						var paddingTop = paddingBottom;
						var shapeWidth = itemContent["shapeWidth"] * 4.0 / 3 - paddingLeft - paddingRight;
						var shapeHeight = itemContent["shapeHeight"] * 4.0 / 3 - paddingTop - paddingBottom;
						var locateX = itemContent["locateX"] * 4.0 / 3;
						var locateY = itemContent["locateY"] * 4.0 / 3;
						if (itemContent["text-align"] == 1)
							align = "center";
						else if (itemContent["text-align"] == 2)
							align = "right";
						else if (itemContent["text-align"] == 0)
							align = "left";

						$("#pptPage" + i).append("<div id='" + j + i + "' class='shape'></div>");
						$("#" + j + i).append(itemContent["content"]);
						$("#" + j + i).css({
							"width": shapeWidth + "px",
							"height": shapeHeight + "px",
							"padding-left": paddingLeft + "px",
							"padding-right": paddingRight + "px",
							"padding-top": paddingTop + "px",
							"padding-bottom": paddingBottom + "px",
							"left": locateX + "px",
							"top": locateY + "px",
							"text-align": align
						});
					}
					/*如果是图表或者图片 */
					else if (itemContent["sign"] == 2 || itemContent["sign"] == 3) {
						var imgWidth = itemContent["width"] * 4.0 / 3;
						var imgHeight = itemContent["height"] * 4.0 / 3;
						var locateX = itemContent["locateX"] * 4.0 / 3;
						var locateY = itemContent["locateY"] * 4.0 / 3;
						$("#pptPage" + i).append("<img src='" + itemContent["url"] + "' style='position:absolute;width:" + imgWidth + "px;height:" + imgHeight + "px;left:" + locateX + "px;top:" + locateY + "px'/>");
					}
					/*如果是iframe*/
					else if (itemContent["sign"] == 5) {
						var iframeWidth = pageWidth;
						var iframeHeight = pageHeight;
						$("#pptPage" + i).append("<iframe id='" + j + i + "' class='shape' scrolling='yes' frameborder='no' border='0' marginwidth='0' marginheight='0' target='_parent'></iframe>");
						/*设置iframe属性*/
						$("#" + j + i).css({
							"width": iframeWidth + "px",
							"height": iframeHeight + "px"
						});
						$("#" + j + i).attr("src", itemContent["url"]);
					}
					/*如果是直线等自定义图形*/
					else if (itemContent["sign"] == 4) {

					}
				});
			});

		});
	}
});