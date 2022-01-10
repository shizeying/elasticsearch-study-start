package com.run.start.tools;


import io.vavr.control.Try;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Base64Utils {
	
	// Base64 编码与解码
	private static final Base64.Decoder DECODER_64 = Base64.getDecoder();
	private static final Base64.Encoder ENCODER_64 = Base64.getEncoder();
	// dpi越大转换后的图片越清晰，相对转换速度越慢
	private static final Integer DPI = 200;
	
	// 编码、解码格式
	private static final Charset CODE_FORMATE = StandardCharsets.UTF_8;
	
	/**
	 * 1. text明文 转 Base64字符串
	 *
	 * @param text 明文
	 * @return Base64 字符串
	 */
	public static String textToBase64Str(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		return ENCODER_64.encodeToString(text.getBytes(CODE_FORMATE));
	}
	
	/**
	 * 2. text的Base64字符串 转 明文
	 *
	 * @param base64Str text的Base64字符串
	 * @return text明文
	 */
	public static String base64StrToText(String base64Str) {
		if (StringUtils.isBlank(base64Str)) {
			return base64Str;
		}
		
		return Try.of(() -> new String(DECODER_64.decode(base64Str), CODE_FORMATE))
				.onFailure(Base64Exception::new).get();
		
		
	}
	
	/**
	 * 3. 文件（图片、pdf） 转 Base64字符串
	 *
	 * @param file 需要转Base64的文件
	 * @return Base64 字符串
	 */
	public static String fileToBase64Str(File file) throws IOException {
		String base64Str = null;
		FileInputStream fin = null;
		BufferedInputStream bin = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bout = null;
		try {
			fin = new FileInputStream(file);
			bin = new BufferedInputStream(fin);
			baos = new ByteArrayOutputStream();
			bout = new BufferedOutputStream(baos);
			// io
			byte[] buffer = new byte[1024];
			int len = bin.read(buffer);
			while (len != -1) {
				bout.write(buffer, 0, len);
				len = bin.read(buffer);
			}
			// 刷新此输出流，强制写出所有缓冲的输出字节
			bout.flush();
			byte[] bytes = baos.toByteArray();
			// Base64字符编码
			base64Str = ENCODER_64.encodeToString(bytes).trim();
		} catch (IOException e) {
			e.getMessage();
		} finally {
			try {
				if (Objects.nonNull(fin)) {
					fin.close();
				}
				if (Objects.nonNull(bin)) {
					bin.close();
				}
				if (Objects.nonNull(bout)) {
					bout.close();
				}
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return base64Str;
	}
	
	/**
	 * 4. Base64字符串 转 文件（图片、pdf） -- 多用于测试
	 *
	 * @param base64Content Base64 字符串
	 * @param filePath      存放路径
	 */
	public static void base64ContentToFile(String base64Content, String filePath) {
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// Base64解码到字符数组
			byte[] bytes = DECODER_64.decode(base64Content);
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
			bis = new BufferedInputStream(byteInputStream);
			File file = new File(filePath);
			File path = file.getParentFile();
			if (!path.exists()) {
				path.mkdirs();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			// io
			byte[] buffer = new byte[1024];
			int length = bis.read(buffer);
			while (length != -1) {
				bos.write(buffer, 0, length);
				length = bis.read(buffer);
			}
			// 刷新此输出流，强制写出所有缓冲的输出字节
			bos.flush();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (Objects.nonNull(bis)) {
					bis.close();
				}
				if (Objects.nonNull(fos)) {
					fos.close();
				}
				if (Objects.nonNull(bos)) {
					bos.close();
				}
				
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * 5. 将pdf的base64编码格式 转为 img的base64编码格式，只限最后一页
	 *
	 * @param base64Pdf pdf的base64编码
	 * @return image的base64编码
	 */
	public static String base64PdfToJpgBase64(String base64Pdf) {
		if (StringUtils.isBlank(base64Pdf)) {
			return base64Pdf;
		}
		String jpg_base64 = null;
		PDDocument doc = null;
		try {
			// Base64解码
			byte[] pdf_bytes = DECODER_64.decode(base64Pdf);
			// 利用PdfBox生成图像
			doc = PDDocument.load(pdf_bytes);
			int size = doc.getNumberOfPages();
			for (int i = 0; i < size; i++) {
				BufferedImage image = new PDFRenderer(doc).renderImageWithDPI(i, DPI, ImageType.RGB);
				// io流
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// 写入流中
				ImageIO.write(image, "jpg", baos);
				// 转换成字节
				byte[] jpg_Bytes = baos.toByteArray();
				// Base64编码
				jpg_base64 = ENCODER_64.encodeToString(jpg_Bytes).trim();
				// 删除 \r\n
				jpg_base64 = jpg_base64.replaceAll("\n|\n", "");
			}
		} catch (IOException e) {
			System.err.printf("err:%s", e.getMessage());
		} finally {
			if (doc != null) {
				try {
					doc.close();
				} catch (IOException e) {
					System.err.printf("err:%s", e.getMessage());
				}
			}
		}
		return jpg_base64;
	}
	
	/**
	 * 6. 将pdf的base64编码格式 转为 img的base64编码格式，并将所有页合成一张图片
	 *
	 * @param pdfBase64Str pdf的base64编码
	 * @return image的base64编码
	 */
	public static String base64PdfToJpgBase64ForOne(String pdfBase64Str) {
		if (StringUtils.isBlank(pdfBase64Str)) {
			return pdfBase64Str;
		}
		String jpg_base64 = null;
		PDDocument doc = new PDDocument();
		// io
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// Base64解码
			byte[] pdf_bytes = DECODER_64.decode(pdfBase64Str);
			doc = PDDocument.load(pdf_bytes);
			int size = doc.getNumberOfPages();
			/* 图像合并使用的参数 */
			// 定义宽度
			int width = 0;
			// 保存一张图片中的RGB数据
			int[] singleImgRGB;
			// 定义高度，后面用于叠加
			int shiftHeight = 0;
			// 保存每张图片的像素值
			BufferedImage imageResult = null;
			// 利用PdfBox生成图像
			PDDocument pdDocument = doc;
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			/* 根据总页数, 按照50页生成一张长图片的逻辑, 进行拆分 */
			// 每50页转成1张图片,有多少转多少
			int pageLength = size;
			// 总计循环的次数
			int totalCount = pdDocument.getNumberOfPages() / pageLength + 1;
			for (int m = 0; m < totalCount; m++) {
				for (int i = 0; i < pageLength; i++) {
					int pageIndex = i + (m * pageLength);
					if (pageIndex == pdDocument.getNumberOfPages()) {
						System.out.println("循环次数 m = " + m);
						break;
					}
					// dpi越大，则图片越清晰，图片越大，转换耗费的时间也越多
					BufferedImage image = renderer.renderImageWithDPI(pageIndex, DPI, ImageType.RGB);
					int imageHeight = image.getHeight();
					int imageWidth = image.getWidth();
					if (i == 0) {
						// 计算高度和偏移量
						// 使用第一张图片宽度;
						width = imageWidth;
						// 保存每页图片的像素值
						// 加个判断：如果m次循环后所剩的图片总数小于pageLength，则图片高度按剩余的张数绘制，否则会出现长图片下面全是黑色的情况
						if ((pdDocument.getNumberOfPages() - m * pageLength) < pageLength) {
							imageResult = new BufferedImage(width,
									imageHeight * (pdDocument.getNumberOfPages() - m * pageLength),
									BufferedImage.TYPE_INT_RGB);
						} else {
							imageResult = new BufferedImage(width, imageHeight * pageLength,
									BufferedImage.TYPE_INT_RGB);
						}
					} else {
						// 将高度不断累加
						shiftHeight += imageHeight;
					}
					singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);
					imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width);
				}
				// 这个很重要，下面会有说明
				shiftHeight = 0;
			}
			pdDocument.close();
			// 写入流中
			ImageIO.write(imageResult, "jpg", baos);
			// 转换成字节
			byte[] jpg_Bytes = baos.toByteArray();
			// 转换成base64串
			jpg_base64 = ENCODER_64.encodeToString(jpg_Bytes).trim();
			// 删除 \r\n
			jpg_base64 = jpg_base64.replaceAll("\n|\n", "");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				baos.close();
				doc.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return jpg_base64;
	}
	
	public static float[] convertBase64ToArray(String base64Str) {
		final byte[] decode = Base64.getDecoder().decode(base64Str.getBytes());
		final FloatBuffer floatBuffer = ByteBuffer.wrap(decode).asFloatBuffer();
		final float[] dims = new float[floatBuffer.capacity()];
		floatBuffer.get(dims);
		
		return dims;
	}
	
	public static String convertArrayToBase64(float[] array) {
		final int capacity = Float.BYTES * array.length;
		final ByteBuffer bb = ByteBuffer.allocate(capacity);
		for (float v : array) {
			bb.putFloat(v);
		}
		bb.rewind();
		final ByteBuffer encodedBB = Base64.getEncoder().encode(bb);
		
		return new String(encodedBB.array());
	}
	
	public static void main(String[] args) throws IOException {
		final File file = new File("C:\\Users\\shizeying\\Desktop\\工艺优化\\这十六种注塑制品的缺陷及原因分析一览表.docx");
		final String s = fileToBase64Str(file);
		System.out.println(s);
	}
}
