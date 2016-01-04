package utils;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.*;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Poi {

	/* 解析ppt文件 */
	public static void analysis(String pptFile,String folder,long presId) {
		try {
			// 创建文件
			Poi.createFile(folder);
			// 获取ppt文件
			SlideShow slideShow = new SlideShow(new HSLFSlideShow(pptFile));
			Slide[] slide = slideShow.getSlides();
			// 创建文件输出流
			FileOutputStream fos = new FileOutputStream(new File("assets/"+folder+"/presentation.json"));
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(fos, "UTF-8"));
			// 写json文件
			bufferedWriter.write("{\n");
			bufferedWriter.append("\"pageContent\":[");
			for (int i = 0; i < slide.length; i++) {
				bufferedWriter.append("\n{\n");
				Shape[] sh = slide[i].getShapes();
				for (int j = 0; j < sh.length; j++) {
					java.awt.Rectangle anchor = sh[j].getAnchor();
					// 如果是文本框
					if (sh[j] instanceof TextBox) {
						TextBox text = (TextBox) sh[j];
						if (text.getText() != null) {
							if (text.getText().contains("iframe:src")) // iframe窗口
							{
								// 文本头部信息
								String textHeader = "\"shape" + j
										+ "\":{\"sign\":\"5\",";
								// 文本内容
								String textContent = "\"url\":\""
										+ text.getText().split("\\(")[1]
												.split("\\)")[0]
										+ "\",\"width\":\""
										+ text.getAnchor().getWidth()
										+ "\",\"height\":\""
										+ text.getAnchor().getHeight()
										+ "\",\"locateX\":\""
										+ text.getAnchor().getLocation().getX()
										+ "\",\"locateY\":\""
										+ text.getAnchor().getLocation().getY()
										+ "\"}";
								bufferedWriter.append(textHeader + textContent);
							} else {
								String fontWeight;
								String fontStyle;
								RichTextRun rt = text.getTextRun()
										.getRichTextRuns()[0];

								// 文本头部信息
								String textHeader = "\"shape" + j
										+ "\":{\"sign\":\"1\",";
								// 文本内容
								String textContent = "\"content\":\"";
								for (int k = 0; k < text.getTextRun()
										.getRichTextRuns().length; k++) {

									rt = text.getTextRun().getRichTextRuns()[k];
									if (rt.isBold())
										fontWeight = "bold";
									else
										fontWeight = "normal";
									if (rt.isItalic())
										fontStyle = "italic";
									else
										fontStyle = "normal";

									textContent += "<span style='font-size:"
											+ rt.getFontSize()
											+ "pt;color:"
											+ ColorConv
													.toHex(rt.getFontColor()
															.getRed(), rt
															.getFontColor()
															.getGreen(), rt
															.getFontColor()
															.getBlue())
											+ ";font-style:"
											+ fontStyle
											+ ";font-family:"
											+ rt.getFontName()
											+ ";font-weight:"
											+ fontWeight
											+ ";'>"
											+ rt.getText()
													.replaceAll("  ", " &nbsp;")
													.replaceAll("\n", "<br/>")
													.replaceAll("\"", "&quot;")
													.replaceAll("\t",
															"&nbsp;&nbsp;&nbsp;&nbsp;")
											+ "</span>";

								}
								textContent += "\",";
								// 文本尾部信息
								String textFooter = "\"text-align\":\""
										+ text.getHorizontalAlignment() + "\","
										+ "\"vertical-align\":\""
										+ text.getVerticalAlignment() + "\","
										+ "\"locateX\":" + "\""
										+ anchor.getLocation().getX() + "\","
										+ "\"locateY\":\""
										+ anchor.getLocation().getY() + "\","
										+ "\"shapeWidth\":\""
										+ anchor.getWidth() + "\","
										+ "\"shapeHeight\":\""
										+ anchor.getHeight() + "\","
										+ "\"marginTop\":\""
										+ text.getMarginTop() + "\","
										+ "\"marginBottom\":\""
										+ text.getMarginBottom() + "\","
										+ "\"marginLeft\":\""
										+ text.getMarginLeft() + "\"" + "}";
								bufferedWriter.append(textHeader + textContent
										+ textFooter);

							}
						} else
							bufferedWriter.append("\"xxx\":\"xxx\"");
						if (j < sh.length - 1) {
							bufferedWriter.append(",\n");
						}
					} else if (sh[j] instanceof TextShape) {
						TextShape text = (TextShape) sh[j];

						if (text.getText() != null) {
							if (text.getText().contains("iframe:src")) {
								// 文本头部信息
								String textHeader = "\"shape" + j
										+ "\":{\"sign\":\"5\",";
								// 文本内容
								String textContent = "\"url\":\""
										+ text.getText().split("\\(")[1]
												.split("\\)")[0]
										+ "\",\"width\":\""
										+ text.getAnchor().getWidth()
										+ "\",\"height\":\""
										+ text.getAnchor().getHeight()
										+ "\",\"locateX\":\""
										+ text.getAnchor().getLocation().getX()
										+ "\",\"locateY\":\""
										+ text.getAnchor().getLocation().getY()
										+ "\"}";
								bufferedWriter.append(textHeader + textContent);
							} else {
								RichTextRun rt = text.getTextRun()
										.getRichTextRuns()[0];
								// 文本头部信息
								String textHeader = "\"shape" + j
										+ "\":{\"sign\":\"1\",";
								// 文本内容
								String textContent = "\"content\":\"";
								for (int k = 0; k < text.getTextRun()
										.getRichTextRuns().length; k++) {
									rt = text.getTextRun().getRichTextRuns()[k];
									String fontWeight;
									String fontStyle;
									if (rt.isBold())
										fontWeight = "bold";
									else
										fontWeight = "normal";
									if (rt.isItalic())
										fontStyle = "italic";
									else
										fontStyle = "normal";

									textContent += "<span style='font-size:"
											+ rt.getFontSize()
											+ "pt;color:"
											+ ColorConv
													.toHex(rt.getFontColor()
															.getRed(), rt
															.getFontColor()
															.getGreen(), rt
															.getFontColor()
															.getBlue())
											+ ";font-style:"
											+ fontStyle
											+ ";font-family:"
											+ rt.getFontName()
											+ ";font-weight:"
											+ fontWeight
											+ ";'>"
											+ rt.getText()
													.replaceAll("  ", " &nbsp;")
													.replaceAll("\n", "<br/>")
													.replaceAll("\"", "&quot;")
													.replaceAll("\t",
															"&nbsp;&nbsp;&nbsp;&nbsp;")
											+ "</span>";

								}
								textContent += "\",";
								// 文本尾部信息
								String textFooter = "\"text-align\":\""
										+ text.getHorizontalAlignment() + "\","
										+ "\"vertical-align\":\""
										+ text.getVerticalAlignment() + "\","
										+ "\"locateX\":" + "\""
										+ anchor.getLocation().getX() + "\","
										+ "\"locateY\":\""
										+ anchor.getLocation().getY() + "\","
										+ "\"shapeWidth\":\""
										+ anchor.getWidth() + "\","
										+ "\"shapeHeight\":\""
										+ anchor.getHeight() + "\","
										+ "\"marginTop\":\""
										+ text.getMarginTop() + "\","
										+ "\"marginBottom\":\""
										+ text.getMarginBottom() + "\","
										+ "\"marginLeft\":\""
										+ text.getMarginLeft() + "\"" + "}";
								bufferedWriter.append(textHeader + textContent
										+ textFooter);

							}
						} else
							bufferedWriter.append("\"xxx\":\"xxx\"");
						if (j < sh.length - 1) {
							bufferedWriter.append(",\n");
						}
					} else if (sh[j] instanceof Picture) {
						Picture shape = (Picture) sh[j];
						if (shape.getPictureData() == null) {
							bufferedWriter.append("\"xxx\":\"xxx\"");
							if (j < sh.length - 1) {
								bufferedWriter.append(",\n");
							}
							break;
						}
						byte[] data = shape.getPictureData().getData();
						int type = shape.getPictureData().getType();
						String ext;
						switch (type) {
						case Picture.JPEG:
							ext = ".jpg";
							break;
						case Picture.PNG:
							ext = ".png";
							break;
						case Picture.WMF:
							ext = ".wmf";
							break;
						case Picture.EMF:
							ext = "emf";
							break;
						case Picture.PICT:
							ext = ".pict";
							break;
						default:
							continue;
						}
						FileOutputStream out = new FileOutputStream("assets/"+folder+"/res/pict_"
								+ i + "" + j + ext);
						out.write(data);
						out.close();
						if (ext.equals(".wmf")) {
							PicConv.transcode("pict_" + i + "" + j);
							bufferedWriter.append("\"shape" + j
									+ "\":{\"sign\":\"2\","
									+ "\"url\":\"/assets/"+folder+"/res/pict_" + i + "" + j
									+ ".jpg" + "\",\"width\":\""
									+ shape.getAnchor().width + "\","
									+ "\"height\":\""
									+ shape.getAnchor().height + "\","
									+ "\"locateX\":\""
									+ shape.getAnchor().getLocation().getX()
									+ "\"," + "\"locateY\":\""
									+ shape.getAnchor().getLocation().getY()
									+ "\"}");
						} else {
							bufferedWriter.append("\"shape" + j
									+ "\":{\"sign\":\"2\","
									+ "\"url\":\"/assets/"+folder+"/res/pict_" + i + "" + j + ext
									+ "\",\"width\":\""
									+ shape.getAnchor().width + "\","
									+ "\"height\":\""
									+ shape.getAnchor().height + "\","
									+ "\"locateX\":\""
									+ shape.getAnchor().getLocation().getX()
									+ "\"," + "\"locateY\":\""
									+ shape.getAnchor().getLocation().getY()
									+ "\"}");
						}
						if (j < sh.length - 1) {
							bufferedWriter.append(",\n");
						}
					} else if (sh[j] instanceof Table) {
						Table table = (Table) sh[j];
						for (int k = 0; k < table.getNumberOfRows(); k++)
							for (int m = 0; m < table.getNumberOfColumns(); m++) {
								if (table.getCell(k, m).getTextRun() != null) {
									table.getCell(k, m).getTextRun()
											.getRichTextRuns()[0]
											.setFontName("宋体");
								}
							}
						BufferedImage img = new BufferedImage(
								slideShow.getPageSize().width,
								slideShow.getPageSize().height,
								BufferedImage.TYPE_INT_ARGB);
						Graphics2D graphics = img.createGraphics();
                        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        graphics.setRenderingHint(RenderingHints.KEY_RENDERING , RenderingHints.VALUE_RENDER_QUALITY);
						graphics.setPaint(Color.white);
						Font font = new Font("宋体", Font.PLAIN, 12);
						graphics.setFont(font);
						graphics.fill(new Rectangle.Float(0, 0, slideShow
								.getPageSize().width,
								slideShow.getPageSize().height));

						slide[i].draw(graphics);
						FileOutputStream out = new FileOutputStream("table_"
								+ i + "" + j + ".png");
						javax.imageio.ImageIO.write(img, "png", out);
						out.close();
						BufferedImage image = (BufferedImage) ImageIO
								.read(new File("table_" + i + "" + j + ".png"));
						int x = (int) table.getAnchor().getLocation().getX();
						int y = (int) table.getAnchor().getLocation().getY();
						BufferedImage myimage = image.getSubimage(x, y,
								table.getAnchor().width,
								table.getAnchor().height);
						ImageIO.write(myimage, "png", new FileOutputStream(
								"assets/"+folder+"/res/table_" + i + "" + j + ".png"));
						File file = new File("table_" + i + "" + j + ".png");
						file.delete();
						bufferedWriter.append("\"" + "shape" + j
								+ "\":{\"sign\":\"3\"," + "\"url\":"
								+ "\"/assets/"+folder+"/res/table_" + i + "" + j + ".png\","
								+ "\"locateX\":" + "\"" + x + "\","
								+ "\"locateY\":" + "\"" + y + "\","
								+ "\"width\":\"" + table.getAnchor().width
								+ "\",\"height\":\"" + table.getAnchor().height
								+ "\"}");
						if (j < sh.length - 1) {
							bufferedWriter.append(",\n");
						}
					} else if (sh[j] instanceof Line) {
						Line line = (Line) sh[j];
						/* 头部信息 */
						String lineHeader = "\"shape" + j
								+ "\":{\"sign\":\"4\",";
						/* 直线信息 */
						String lineContent = "\"color\":\""
								+ ColorConv.toHex(line.getLineColor().getRed(),
										line.getLineColor().getGreen(), line
												.getLineColor().getBlue())
								+ "\",\"width\":\"" + line.getLineWidth()
								+ "\",";
						/* */
						if (line.getFlipHorizontal()) {
							lineContent += "\"x1\":\""
									+ (line.getAnchor().getLocation().getX() + line
											.getAnchor().getWidth())
									+ "\",\"y1\":\""
									+ (line.getAnchor().getLocation().getY())
									+ "\",\"x2\":\""
									+ (line.getAnchor().getLocation().getX())
									+ "\",\"y2\":\""
									+ (line.getAnchor().getLocation().getY() + line
											.getAnchor().getHeight()) + "\"}";
						} else {
							lineContent += "\"x1\":\""
									+ (line.getAnchor().getLocation().getX())
									+ "\",\"y1\":\""
									+ (line.getAnchor().getLocation().getY())
									+ "\",\"x2\":\""
									+ (line.getAnchor().getLocation().getX() + line
											.getAnchor().getWidth())
									+ "\",\"y2\":\""
									+ (line.getAnchor().getLocation().getY() + line
											.getAnchor().getHeight()) + "\"}";
						}
						bufferedWriter.append(lineHeader + lineContent);
						if (j < sh.length - 1) {
							bufferedWriter.append(",\n");
						}
					} else {
						bufferedWriter.append("\"xxx\":\"xxx\"");
						if (j < sh.length - 1) {
							bufferedWriter.append(",\n");
						}
					}
				}
				bufferedWriter.append("}");
				if (i != slide.length - 1)
					bufferedWriter.append(",");
			}
			/*获取背景图片*/
			SlideMaster master = slideShow.getSlidesMasters()[0];
			Background background = master.getBackground();
			Fill f = background.getFill();
			BufferedImage img = new BufferedImage(slideShow.getPageSize().width,
					slideShow.getPageSize().height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = img.createGraphics();
			graphics.setPaint(f.getBackgroundColor());
			graphics.fill(new Rectangle.Float(0, 0, slideShow.getPageSize().width,
					slideShow.getPageSize().height));
			background.draw(graphics);
			FileOutputStream out = new FileOutputStream("assets/"+folder+"/res/background.png");
			javax.imageio.ImageIO.write(img, "png", out);
			out.close();
			String backgroundUrl = "/assets/"+folder+"/res/background.png";

			bufferedWriter.append("]\n,");
			bufferedWriter.append("\"property\":"
					+ "{"
					+ "\"pageWidth\":\""
					+ slideShow.getPageSize().width
					+ "\","
					+ "\"pageHeight\":\""
					+ slideShow.getPageSize().height
					+ "\","
					+ "\"pageNum\":\""
					+ slideShow.getSlides().length
					+ "\","
					+ "\"background\":\""
					+ backgroundUrl
					+ "\","
					+ "\"fileName\":\""
					+ pptFile.substring(pptFile.lastIndexOf("/")+1)
					+ "\","
					+ "\"presId\":\""
					+ presId
					+ "\","
					+ "\"fileTitle\":\""
					+ slideShow.getSlides()[0].getTitle().replaceAll("\n", "")
							.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
							.replaceAll("  ", " &nbsp;") + "\","
					+ "\"editDate\":\"" + FileUtils.getModifiedTime(pptFile)
					+ "\"}");
			bufferedWriter.append("}");
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("create success!");
	}

    public static void prepareGraphic(Graphics2D graphics){
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

	/* 创建文件 */
	public static void createFile(String folder) {
		//json文件所在文件夹
		String jsonFolderPath = "assets/"+folder;
		// json文件
		String jsonPath = "assets/"+folder+"/presentation.json";
		File jsonFolder = new File(jsonFolderPath);
		File jsonFile = new File(jsonPath);
		// res文件夹
		String resPath = "assets/"+folder+"/res";
		File resFile = new File(resPath);
		try {
			if (!jsonFile.exists()) {
				jsonFolder.mkdirs();
				jsonFile.createNewFile();
				System.out.println("json文件已经创建!");
			}
			if (!resFile.exists()) {
				resFile.mkdirs();
				System.out.println("res文件夹已经创建!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
