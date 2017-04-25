package de.unisaarland.instrumentation;

public class Instrumenter {

	// This is not visible in the DiSL Snippet for some reason..
	// private static final Logger __log = Logging.getPackageInstance();
	// public static ClassLoader loader;
	// static String curPath;

	/* Probably this might work as guard ?! */
	public static boolean isIgnoredClass(String owner) {
		// For the moment let's be conservative
		if (owner == null) {
			return true;
		}

		// Lde/unisaarland/instrumentation/DependencyInfo;
		if (owner.startsWith("L") && owner.endsWith(";")) {
			owner = owner.substring(1, owner.length() - 1);
		}

		// TODO This is too strict for the moment ...
		return owner.startsWith("java/") //
				|| owner.startsWith("sun/")//
				|| owner.startsWith("ch/usi/dag/disl")//
				|| owner.startsWith("de/unisaarland/instrumentation")//
				|| owner.startsWith("de/unisaarland/analysis")//
				// FIXME. Apparently adding JUnit to the project results in bad
				// behaviors instrumenting JUnitCore has an error (see
				// JUnitCore.trace)!
				|| owner.startsWith("org/junit")//
				|| owner.startsWith("junit/framework")//
				|| owner.startsWith("com/sun")//
		;
		// return owner.startsWith("java/lang/Object") ||
		// owner.startsWith("java/lang/Number")
		// || owner.startsWith("java/lang/Comparable") ||
		// owner.startsWith("java/lang/ref/SoftReference")
		// || owner.startsWith("java/lang/ref/Reference") ||
		// owner.startsWith("java/lang/ref/FinalizerReference")
		// || owner.startsWith("java/lang/Boolean") ||
		// owner.startsWith("java/lang/Character")
		// || owner.startsWith("java/lang/Float") ||
		// owner.startsWith("java/lang/Byte")
		// || owner.startsWith("java/lang/Short") ||
		// owner.startsWith("java/lang/Integer")
		// || owner.startsWith("java/lang/StackTraceElement") ||
		// (owner.startsWith("edu/gmu/swe/datadep"))
		// || owner.startsWith("sun/awt/image/codec/") ||
		// (owner.startsWith("sun/reflect/Reflection"))
		// || owner.equals("java/lang/reflect/Proxy")
		// || owner.startsWith("sun/reflection/annotation/AnnotationParser")
		// || owner.startsWith("sun/reflect/MethodAccessor") ||
		// owner.startsWith("sun/reflect/ConstructorAccessor")
		// || owner.startsWith("sun/reflect/SerializationConstructorAccessor")
		// || owner.startsWith("sun/reflect/GeneratedMethodAccessor")
		// || owner.startsWith("sun/reflect/GeneratedConstructorAccessor")
		// || owner.startsWith("sun/reflect/GeneratedSerializationConstructor")
		// || owner.startsWith("java/lang/reflect/Field") ||
		// owner.startsWith("sun/reflect/Unsafe")
		// || owner.startsWith("java/lang/Class") ||
		// owner.startsWith("java/lang/reflect/Method")
		// || owner.startsWith("java/lang/Double") ||
		// owner.startsWith("java/lang/Long") //
		// // Ignore string type
		// || owner.equals("java/lang/String")
		// // Ignore Instrumentation classes
		// || owner.equals("de/unisaarland/instrumentation/DependencyInfo")//
		// ;

	}

	/* Probably this might work as guard ?! */
	public static boolean isMockedClass(String owner) {
		// Note: there is a specific interface provided by Mockito to identify
		// mocked classes. We use string instead so we do not include mockito as
		// dependency to build thi project
		return owner.contains("EnhancerByMockitoWithCGLIB");
	}

	// static int n = 0;

	// @Deprecated /* This is used to instrument the JVM */
	// public static byte[] instrumentClass(String path, InputStream is, boolean
	// renameInterfaces) {
	// try {
	// n++;
	// if (n % 1000 == 0)
	// System.out.println("Processed: " + n);
	// curPath = path;
	// ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	//
	// int nRead;
	// byte[] data = new byte[16384];
	//
	// while ((nRead = is.read(data, 0, data.length)) != -1) {
	// buffer.write(data, 0, nRead);
	// }
	//
	// buffer.flush();
	// RWDependencyClassFileTransformer transformer = new
	// RWDependencyClassFileTransformer();
	// byte[] ret = transformer
	// .transform(/* Instrumenter.loader, path, null, null,
	// */buffer.toByteArray());
	// curPath = null;
	// return ret;
	// } catch (Exception ex) {
	// curPath = null;
	// ex.printStackTrace();
	// return null;
	// }
	// }

	// static Option help = new Option("help", "print this message");

	// static File rootOutputDir;

	// // I suspect this is used to instrument all the jars/classes offline
	// public static void _main(String[] args) {
	//
	// String outputFolder = args[1];
	// rootOutputDir = new File(outputFolder);
	// if (!rootOutputDir.exists())
	// rootOutputDir.mkdir();
	// String inputFolder = args[0];
	// // Setup the class loader
	// final ArrayList<URL> urls = new ArrayList<URL>();
	// Path input = FileSystems.getDefault().getPath(args[0]);
	// try {
	// if (Files.isDirectory(input))
	// Files.walkFileTree(input, new FileVisitor<Path>() {
	// public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes
	// attrs) throws IOException {
	// return FileVisitResult.CONTINUE;
	// }
	//
	// public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	// throws IOException {
	// if (file.getFileName().toString().endsWith(".jar"))
	// urls.add(file.toUri().toURL());
	// return FileVisitResult.CONTINUE;
	// }
	//
	// public FileVisitResult visitFileFailed(Path file, IOException exc) throws
	// IOException {
	// return FileVisitResult.CONTINUE;
	// }
	//
	// public FileVisitResult postVisitDirectory(Path dir, IOException exc)
	// throws IOException {
	// return FileVisitResult.CONTINUE;
	// }
	// });
	// else if (inputFolder.endsWith(".jar"))
	// urls.add(new File(inputFolder).toURI().toURL());
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// try {
	// urls.add(new File(inputFolder).toURI().toURL());
	// } catch (MalformedURLException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// File f = new File(inputFolder);
	// if (!f.exists()) {
	// System.err.println("Unable to read path " + inputFolder);
	// System.exit(-1);
	// }
	// if (f.isDirectory())
	// processDirectory(f, rootOutputDir, true);
	// else if (inputFolder.endsWith(".jar") || inputFolder.endsWith(".war"))
	// processJar(f, rootOutputDir);
	// else if (inputFolder.endsWith(".class"))
	// try {
	// processClass(f.getName(), new FileInputStream(f), rootOutputDir);
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// else if (inputFolder.endsWith(".zip")) {
	// processZip(f, rootOutputDir);
	// } else {
	// System.err.println("Unknown type for path " + inputFolder);
	// System.exit(-1);
	// }
	// }

	// static String lastInstrumentedClass;

	// private static void processClass(String name, InputStream is, File
	// outputDir) {
	//
	// try {
	// FileOutputStream fos = new FileOutputStream(outputDir.getPath() +
	// File.separator + name);
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// lastInstrumentedClass = outputDir.getPath() + File.separator + name;
	//
	// byte[] c = instrumentClass(outputDir.getAbsolutePath(), is, true);
	// bos.write(c);
	// bos.writeTo(fos);
	// fos.close();
	// is.close();
	//
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	// private static void processDirectory(File f, File parentOutputDir,
	// boolean isFirstLevel) {
	// if (f.getName().equals(".AppleDouble"))
	// return;
	// File thisOutputDir;
	// if (isFirstLevel) {
	// thisOutputDir = parentOutputDir;
	// } else {
	// thisOutputDir = new File(parentOutputDir.getAbsolutePath() +
	// File.separator + f.getName());
	// thisOutputDir.mkdir();
	// }
	// for (File fi : f.listFiles()) {
	// if (fi.isDirectory())
	// processDirectory(fi, thisOutputDir, false);
	// else if (fi.getName().endsWith(".class"))
	// try {
	// processClass(fi.getName(), new FileInputStream(fi), thisOutputDir);
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// else if (fi.getName().endsWith(".jar") || fi.getName().endsWith(".war"))
	// // try {
	// // FileOutputStream fos = new
	// // FileOutputStream(thisOutputDir.getPath() + File.separator +
	// // f.getName());
	// processJar(fi, thisOutputDir);
	// // fos.close();
	// // } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// // e1.printStackTrace();
	// // }
	// else if (fi.getName().endsWith(".zip"))
	// processZip(fi, thisOutputDir);
	// else {
	// File dest = new File(thisOutputDir.getPath() + File.separator +
	// fi.getName());
	// FileChannel source = null;
	// FileChannel destination = null;
	//
	// try {
	// source = new FileInputStream(fi).getChannel();
	// destination = new FileOutputStream(dest).getChannel();
	// destination.transferFrom(source, 0, source.size());
	// } catch (Exception ex) {
	// System.err.println("error copying file " + fi);
	// ex.printStackTrace();
	// // logger.log(Level.SEVERE, "Unable to copy file " + fi,
	// // ex);
	// // System.exit(-1);
	// } finally {
	// if (source != null) {
	// try {
	// source.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// if (destination != null) {
	// try {
	// destination.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// }
	// }
	//
	// }

	// public static void processJar(File f, File outputDir) {
	// try {
	// // @SuppressWarnings("resource")
	// // System.out.println("File: " + f.getName());
	// JarFile jar = new JarFile(f);
	// JarOutputStream jos = null;
	// jos = new JarOutputStream(new FileOutputStream(outputDir.getPath() +
	// File.separator + f.getName()));
	// Enumeration<JarEntry> entries = jar.entries();
	// while (entries.hasMoreElements()) {
	// JarEntry e = entries.nextElement();
	// if (e.getName().endsWith(".class")) {
	// {
	//
	// try {
	// JarEntry outEntry = new JarEntry(e.getName());
	// jos.putNextEntry(outEntry);
	// byte[] clazz = instrumentClass(f.getAbsolutePath(),
	// jar.getInputStream(e), true);
	// if (clazz == null) {
	// System.out.println("Failed to instrument " + e.getName() + " in " +
	// f.getName());
	// InputStream is = jar.getInputStream(e);
	// byte[] buffer = new byte[1024];
	// while (true) {
	// int count = is.read(buffer);
	// if (count == -1)
	// break;
	// jos.write(buffer, 0, count);
	// }
	// } else {
	// jos.write(clazz);
	// }
	// jos.closeEntry();
	// } catch (ZipException ex) {
	// ex.printStackTrace();
	// continue;
	// }
	//
	// }
	//
	// } else {
	// JarEntry outEntry = new JarEntry(e.getName());
	// if (e.isDirectory()) {
	// try {
	// jos.putNextEntry(outEntry);
	// jos.closeEntry();
	// } catch (ZipException exxx) {
	// System.out.println("Ignoring exception: " + exxx);
	// }
	// } else if (e.getName().startsWith("META-INF")
	// && (e.getName().endsWith(".SF") || e.getName().endsWith(".RSA"))) {
	// // don't copy this
	// } else if (e.getName().equals("META-INF/MANIFEST.MF")) {
	// Scanner s = new Scanner(jar.getInputStream(e));
	// jos.putNextEntry(outEntry);
	//
	// String curPair = "";
	// while (s.hasNextLine()) {
	// String line = s.nextLine();
	// if (line.equals("")) {
	// curPair += "\n";
	// if (!curPair.contains("SHA1-Digest:"))
	// jos.write(curPair.getBytes());
	// curPair = "";
	// } else {
	// curPair += line + "\n";
	// }
	// }
	// s.close();
	// // jos.write("\n".getBytes());
	// jos.closeEntry();
	// } else {
	// try {
	// jos.putNextEntry(outEntry);
	// InputStream is = jar.getInputStream(e);
	// byte[] buffer = new byte[1024];
	// while (true) {
	// int count = is.read(buffer);
	// if (count == -1)
	// break;
	// jos.write(buffer, 0, count);
	// }
	// jos.closeEntry();
	// } catch (ZipException ex) {
	// if (!ex.getMessage().contains("duplicate entry")) {
	// ex.printStackTrace();
	// System.out.println("Ignoring above warning from improper source zip...");
	// }
	// }
	// }
	//
	// }
	//
	// }
	// if (jos != null) {
	// jos.close();
	//
	// }
	// jar.close();
	// } catch (Exception e) {
	// System.err.println("Unable to process jar: " + f.getAbsolutePath());
	// e.printStackTrace();
	// // logger.log(Level.SEVERE, "Unable to process jar: " +
	// // f.getAbsolutePath(), e);
	// File dest = new File(outputDir.getPath() + File.separator + f.getName());
	// FileChannel source = null;
	// FileChannel destination = null;
	//
	// try {
	// source = new FileInputStream(f).getChannel();
	// destination = new FileOutputStream(dest).getChannel();
	// destination.transferFrom(source, 0, source.size());
	// } catch (Exception ex) {
	// System.err.println("Unable to copy file: " + f.getAbsolutePath());
	// ex.printStackTrace();
	// // System.exit(-1);
	// } finally {
	// if (source != null) {
	// try {
	// source.close();
	// } catch (IOException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// }
	// }
	// if (destination != null) {
	// try {
	// destination.close();
	// } catch (IOException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// }
	// }
	// }
	// // System.exit(-1);
	// }
	//
	// }

	// private static void processZip(File f, File outputDir) {
	// try {
	// // @SuppressWarnings("resource")
	// ZipFile zip = new ZipFile(f);
	// ZipOutputStream zos = null;
	// zos = new ZipOutputStream(new FileOutputStream(outputDir.getPath() +
	// File.separator + f.getName()));
	// Enumeration<? extends ZipEntry> entries = zip.entries();
	// while (entries.hasMoreElements()) {
	// ZipEntry e = entries.nextElement();
	//
	// if (e.getName().endsWith(".class")) {
	// {
	// ZipEntry outEntry = new ZipEntry(e.getName());
	// zos.putNextEntry(outEntry);
	//
	// byte[] clazz = instrumentClass(f.getAbsolutePath(),
	// zip.getInputStream(e), true);
	// if (clazz == null) {
	// InputStream is = zip.getInputStream(e);
	// byte[] buffer = new byte[1024];
	// while (true) {
	// int count = is.read(buffer);
	// if (count == -1)
	// break;
	// zos.write(buffer, 0, count);
	// }
	// } else
	// zos.write(clazz);
	// zos.closeEntry();
	//
	// }
	//
	// } else if (e.getName().endsWith(".jar")) {
	// ZipEntry outEntry = new ZipEntry(e.getName());
	// // jos.putNextEntry(outEntry);
	// // try {
	// // processJar(jar.getInputStream(e), jos);
	// // jos.closeEntry();
	// // } catch (FileNotFoundException e1) {
	// // // TODO Auto-generated catch block
	// // e1.printStackTrace();
	// // }
	//
	// File tmp = new File("/tmp/classfile");
	// if (tmp.exists())
	// tmp.delete();
	// FileOutputStream fos = new FileOutputStream(tmp);
	// byte buf[] = new byte[1024];
	// int len;
	// InputStream is = zip.getInputStream(e);
	// while ((len = is.read(buf)) > 0) {
	// fos.write(buf, 0, len);
	// }
	// is.close();
	// fos.close();
	// // System.out.println("Done reading");
	// File tmp2 = new File("tmp2");
	// if (!tmp2.exists())
	// tmp2.mkdir();
	// processJar(tmp, new File("tmp2"));
	//
	// zos.putNextEntry(outEntry);
	// is = new FileInputStream("tmp2/classfile");
	// byte[] buffer = new byte[1024];
	// while (true) {
	// int count = is.read(buffer);
	// if (count == -1)
	// break;
	// zos.write(buffer, 0, count);
	// }
	// is.close();
	// zos.closeEntry();
	// // jos.closeEntry();
	// } else {
	// ZipEntry outEntry = new ZipEntry(e.getName());
	// if (e.isDirectory()) {
	// try {
	// zos.putNextEntry(outEntry);
	// zos.closeEntry();
	// } catch (ZipException exxxx) {
	// System.out.println("Ignoring exception: " + exxxx.getMessage());
	// }
	// } else if (e.getName().startsWith("META-INF")
	// && (e.getName().endsWith(".SF") || e.getName().endsWith(".RSA"))) {
	// // don't copy this
	// } else if (e.getName().equals("META-INF/MANIFEST.MF")) {
	// Scanner s = new Scanner(zip.getInputStream(e));
	// zos.putNextEntry(outEntry);
	//
	// String curPair = "";
	// while (s.hasNextLine()) {
	// String line = s.nextLine();
	// if (line.equals("")) {
	// curPair += "\n";
	// if (!curPair.contains("SHA1-Digest:"))
	// zos.write(curPair.getBytes());
	// curPair = "";
	// } else {
	// curPair += line + "\n";
	// }
	// }
	// s.close();
	// zos.write("\n".getBytes());
	// zos.closeEntry();
	// } else {
	// zos.putNextEntry(outEntry);
	// InputStream is = zip.getInputStream(e);
	// byte[] buffer = new byte[1024];
	// while (true) {
	// int count = is.read(buffer);
	// if (count == -1)
	// break;
	// zos.write(buffer, 0, count);
	// }
	// zos.closeEntry();
	// }
	// }
	//
	// }
	// zos.close();
	// zip.close();
	// } catch (Exception e) {
	// System.err.println("Unable to process zip: " + f.getAbsolutePath());
	// e.printStackTrace();
	// File dest = new File(outputDir.getPath() + File.separator + f.getName());
	// FileChannel source = null;
	// FileChannel destination = null;
	//
	// try {
	// source = new FileInputStream(f).getChannel();
	// destination = new FileOutputStream(dest).getChannel();
	// destination.transferFrom(source, 0, source.size());
	// } catch (Exception ex) {
	// System.err.println("Unable to copy zip: " + f.getAbsolutePath());
	// ex.printStackTrace();
	// // System.exit(-1);
	// } finally {
	// if (source != null) {
	// try {
	// source.close();
	// } catch (IOException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// }
	// }
	// if (destination != null) {
	// try {
	// destination.close();
	// } catch (IOException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// }

}