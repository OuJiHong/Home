

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.Expression;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.JarURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDemo <T>{
	
	private static final Logger log = Logger.getLogger(TestDemo.class.getName());
	
	public enum  To{
		one,
		two
	}
	
	
	public int getAge(){
		return 12;
	}
	
	public String getName(){
		return "hello";
	}
	
	/**
	 * test run 
	 * @param args
	 */
	public static void main_001(String[] args) {
		System.out.println("hello world!!");
		System.out.println(Arrays.toString("|sd|f|sd|f|sf|".split("\\|",-1)));
		
		
		
		Method[] methods = TestDemo.class.getDeclaredMethods();
		for(Method method : methods){
//			Type[] types = method.getGenericParameterTypes();
			Type[] types = method.getParameterTypes();
			for(Type t : types){
				System.out.println(Arrays.toString(t.getClass().getInterfaces()));
			}
		}
		
		try{
			Class<?> clazz = Class.forName("com.cq.test.Test$To");
			System.out.println("out:" + clazz.getName());
			System.out.println("out:" + clazz.getCanonicalName());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		PropertyEditor propertyEditor = PropertyEditorManager.findEditor(Long.class);
		System.out.println(propertyEditor);
		propertyEditor.setAsText("234234");
		System.out.println("输出类型：" + propertyEditor.getValue().getClass());
		
		
		//动态执行方法: Expression 是 Statement 的子类
		TestDemo t = new TestDemo();
		Expression expression = new Expression(t, "hello", new Object[]{new ArrayList<String>()});
		try {
			//expression.execute();
			System.out.println(expression.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * test
	 * @param bb
	 */
	public String hello(List<String> bb){
		System.out.println("hello:--" + bb);
		return "success";
	}
	
	
	/**
	 * @param args
	 */
	public static void main_002(String[] args){
		
		Iterator<Class<?>> categories = IIORegistry.getDefaultInstance().getCategories();
		
		while(categories.hasNext()){
			Class<?> clazz = categories.next();
			System.out.println(clazz);
		}
		
		try{
			URL url = new  URL("jar:file:C:\\Users\\OJH\\Desktop\\propertiesLoading.jar!/org");
			JarURLConnection connection = (JarURLConnection)url.openConnection();
			if(connection != null){
				System.out.println("out:" + connection.getJarEntry());
			}else{
				System.out.println("not found");
			}
			
			JarFile jarFile = connection.getJarFile();
			Enumeration<JarEntry> enumeration = jarFile.entries();
			while(enumeration.hasMoreElements()){
				JarEntry jarEntry = enumeration.nextElement();
				System.out.println(jarEntry.getName() + ":" + jarEntry.getComment());
			}
			
			log.info("日志信息输出了！！！");
			
			log.info(System.getProperty("java.io.tmpdir"));
			log.info(File.createTempFile("att", ".gg").getAbsolutePath());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * test image
	 * @param args
	 */
	public static void main__003(String[] args) throws Exception{
		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.drawString("hello", 30, 30);
		graphics.setColor(Color.RED);
		graphics.drawRect(20, 20, 40, 40);
		graphics.fillArc(50, 50, 60, 60, 0, 360);
		graphics.setColor(Color.GREEN);
		graphics.fillRoundRect(30, 30, 40, 60, 8, 8);
		graphics.setColor(Color.BLUE);
		graphics.fill3DRect(60, 60, 40, 40, true);
		graphics.dispose();
		
		File file = new File("C:\\Users\\OJH\\Desktop\\hello.jpg");
		ImageOutputStream output = new FileImageOutputStream(file);
		
		Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter imageWriter = imageWriterIterator.next();
		imageWriter.setOutput(output);
		
		
		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imageWriteParam.setCompressionQuality(0.4f);
		imageWriter.write(null, new IIOImage(image, null, null), imageWriteParam);
		
		log.info("write end");
	
	}
	
	/**
	 * run
	 * @param args
	 */
	public static void main_004(String[] args) throws Exception{
		byte[] data = "hello word".getBytes();
		Signature signature = Signature.getInstance("MD5withRSA");
		KeySpec keySpec = new PKCS8EncodedKeySpec(new byte[]{});
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
//		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		signature.initSign(privateKey);
		signature.update(data);
		byte[] signData = signature.sign();
		System.out.println(Arrays.toString(signData));
		
		//verify
		
		Signature verifySignature = Signature.getInstance("MD5withRSA");
		verifySignature.initVerify(publicKey);
		verifySignature.update(data);
		boolean verifyResult = verifySignature.verify(signData);
		System.out.println(verifyResult);
		
		
		//解开
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decryptData = cipher.doFinal(signData);
		System.out.println("解开：" + Arrays.toString(decryptData));
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] md5Data = messageDigest.digest(data);
		System.out.println("MD5值：" + Arrays.toString(md5Data));
	}
	
	
	/**
	 * @param args
	 */
	public static void main_005(String[] args) throws Exception{
		TestDemo tt = new TestDemo();
		PropertyDescriptor[] des = PropertyUtils.getPropertyDescriptors(tt);
		for(PropertyDescriptor d : des){
			System.out.println(d);
		}
		Map<String,String> mm = BeanUtils.describe(tt);
		System.out.println("out=================" + mm);
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main_006(String[] args) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		Map<String,Object>  map = new HashMap<String,Object>();
		map.put("Name", "张三");
		map.put("age", 32);
//		map.put("Name", new String[]{"bb","cc"});
		
		String jsonStr = objectMapper.writeValueAsString(map);
		System.out.println(jsonStr);
		
		DemoBean demoBeanCC = objectMapper.readValue(jsonStr, DemoBean.class);
		System.out.println("demoBeanCC:" + demoBeanCC);
		System.out.println("convert to map:" + BeanUtils.describe(demoBeanCC));
		
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(DemoBean.class);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor p : propertyDescriptors){
				System.out.println(p.getName() + ":" + p.getWriteMethod());
			}
			
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DemoBean demoBean = new DemoBean();
		try {
			BeanUtils.copyProperties(demoBean, map);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(demoBean);
	}
	
	
	/**
	 * @param args
	 */
	public static void main_007(String[] args) {
		Integer a = 128;
		BigDecimal dd = new BigDecimal("128.01");
		double b = dd.doubleValue();
		
		System.out.println(dd.multiply(new BigDecimal(0.1)).setScale(3, RoundingMode.HALF_EVEN));
		System.out.println(b * 0.1);
		
		System.out.println(a == b);
		
		BigDecimal amount = new BigDecimal("0.30123");
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		System.out.println(decimalFormat.format(amount));
		
		System.out.println(Currency.getInstance(Locale.CHINA).getDefaultFractionDigits());
		
		//因为标度不同，所以比较两者的值也不同. 所以比较数值时必须使用compareTo
		BigDecimal before = BigDecimal.valueOf(23.01);
		BigDecimal after = new BigDecimal("23.010");
		System.out.println(before.compareTo(after));
		
		System.out.println(NumberUtils.toDouble("234.2d"));
	}
	
	
	
}
