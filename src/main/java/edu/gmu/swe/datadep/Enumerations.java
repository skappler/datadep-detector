package edu.gmu.swe.datadep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Enumerations {

	private static Set<String> e;

	// THIS IS THE WORST BUT CANNOT FIND ANY OTHER WAY TO LOAD THIS INFO ON
	// STARTUP SO FAR
	public synchronized static Set<String> get() {
		return e;
	}

	static {
		System.out.println("Enumerations: build enumeration set");
		e = new HashSet<String>();
		if (System.getProperty("enum-list") != null && new File(System.getProperty("enum-list")).exists()) {
			try {
				e.addAll(Files.readAllLines(new File(System.getProperty("enum-list")).toPath()));
				System.out
						.println("Enumerations READ ENUMS FROM " + new File(System.getProperty("enum-list")).toPath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else {
			System.out.println("Enumerations No Enum file specified");
		}
		///
		e.addAll(Arrays.asList(new String[] { //
				"crystal.model.DataSource$RepoKind", //
				"crystal.model.RevisionHistory$When", //
				"crystal.model.RevisionHistory$Capable", //
				"crystal.model.RevisionHistory$Ease", //
				"crystal.model.RevisionHistory$Action",
				//
				"org.junit.runners.MethodSorters", //
				//
				"org.freehg.hgkit.FileStatus$Status", //
				"org.freehg.hgkit.core.Ignore$ExistingIgnore$Syntax$1", //
				"org.freehg.hgkit.core.Ignore$ExistingIgnore$Syntax$2", //
				"org.freehg.hgkit.core.Ignore$ExistingIgnore$Syntax", //
				"crystal.model.DataSource$RepoKind", //
				"crystal.model.RevisionHistory$Action", //
				"crystal.model.RevisionHistory$Capable", //
				"crystal.model.RevisionHistory$Ease", //
				"crystal.model.RevisionHistory$When", //
				"crystal.server.AbstractLogParser$CheckpointLabels", //

		}));

		// CUT/DATA-DEP specific
		e.addAll(Arrays.asList(new String[] { //
				"org.jdom2.Content$CType", //
				"org.jdom2.output.Format$TextMode", //
				"org.jdom2.output.LineSeparator", //
				"org.jdom2.AttributeType", //
				"org.jdom2.input.sax.XMLReaders", //
				// This is tricky
				"org.jdom2.input.sax.XMLReaders$NONSingleton", //
				"org.jdom2.input.sax.XMLReaders$DTDSingleton", //
				"org.jdom2.input.sax.XMLReaders$XSDSingleton", //
				//
				"org.junit.internal.runners.rules.RuleFieldValidator", //
				//
				"java.util.zip.ZipConstants", //
				"org.apache.maven.surefire.booter.ForkedBooter", //
				"org.apache.maven.surefire.booter.BooterConstants", //
				"org.apache.maven.surefire.util.RunOrder", //
				"org.apache.maven.surefire.booter.ForkingRunListener" }));

		// JRE/JVM Level
		e.addAll(Arrays.asList(new String[] { "java.lang.Thread$State", "org.apache.derby.iapi.security.Securable",
				"com.sun.deploy.ref.AppModel$SecurityMode", "com.sun.deploy.ref.AppModel$Type",
				"com.sun.deploy.ref.AppRef$Type", "com.sun.deploy.security.ruleset.DRSResult$Type",
				"com.sun.glass.ui.EventLoop$State", "com.sun.glass.ui.mac.MacAccessible$MacAction",
				"com.sun.glass.ui.mac.MacAccessible$MacNotification",
				"com.sun.glass.ui.mac.MacAccessible$MacOrientation", "com.sun.glass.ui.mac.MacAccessible$MacRole",
				"com.sun.glass.ui.mac.MacAccessible$MacSubrole", "com.sun.glass.ui.mac.MacAccessible$MacText",
				"com.sun.javafx.css.Combinator$1", "com.sun.javafx.css.Combinator$2", "com.sun.javafx.css.Combinator",
				"com.sun.javafx.css.SizeUnits$1", "com.sun.javafx.css.SizeUnits$10", "com.sun.javafx.css.SizeUnits$11",
				"com.sun.javafx.css.SizeUnits$12", "com.sun.javafx.css.SizeUnits$13", "com.sun.javafx.css.SizeUnits$14",
				"com.sun.javafx.css.SizeUnits$15", "com.sun.javafx.css.SizeUnits$2", "com.sun.javafx.css.SizeUnits$3",
				"com.sun.javafx.css.SizeUnits$4", "com.sun.javafx.css.SizeUnits$5", "com.sun.javafx.css.SizeUnits$6",
				"com.sun.javafx.css.SizeUnits$7", "com.sun.javafx.css.SizeUnits$8", "com.sun.javafx.css.SizeUnits$9",
				"com.sun.javafx.css.SizeUnits", "com.sun.javafx.cursor.CursorType",
				"com.sun.javafx.effect.EffectDirtyBits", "com.sun.javafx.fxml.expression.Operator",
				"com.sun.javafx.geom.BaseBounds$BoundsType", "com.sun.javafx.geom.Path2D$CornerPrefix",
				"com.sun.javafx.image.AlphaType", "com.sun.javafx.print.Units", "com.sun.javafx.scene.CssFlags",
				"com.sun.javafx.scene.DirtyBits", "com.sun.javafx.scene.LayoutFlags",
				"com.sun.javafx.scene.control.behavior.OptionalBoolean",
				"com.sun.javafx.scene.control.skin.ButtonBarSkin$Spacer$1",
				"com.sun.javafx.scene.control.skin.ButtonBarSkin$Spacer$2",
				"com.sun.javafx.scene.control.skin.ComboBoxMode", "com.sun.javafx.scene.control.skin.FXVK$Type",
				"com.sun.javafx.scene.control.skin.TabPaneSkin$TabAnimationState",
				"com.sun.javafx.scene.traversal.Direction", "com.sun.javafx.sg.prism.CacheFilter$ScrollCacheState",
				"com.sun.javafx.sg.prism.NGCanvas$InitType", "com.sun.javafx.sg.prism.NGNode$DirtyFlag",
				"com.sun.javafx.tk.FileChooserType", "com.sun.javafx.tk.FocusCause",
				"com.sun.javafx.tk.quantum.RotateGestureRecognizer$RotateRecognitionState",
				"com.sun.javafx.tk.quantum.ScrollGestureRecognizer$ScrollRecognitionState",
				"com.sun.javafx.tk.quantum.ZoomGestureRecognizer$ZoomRecognitionState",
				"com.sun.media.jfxmedia.MediaError", "com.sun.media.jfxmedia.control.VideoFormat",
				"com.sun.openpisces.Dasher$LengthIterator$Side", "com.sun.prism.CompositeMode",
				"com.sun.prism.GraphicsPipeline$ShaderModel", "com.sun.prism.GraphicsPipeline$ShaderType",
				"com.sun.prism.PixelFormat", "com.sun.prism.es2.ES2PhongShader$BumpMapState",
				"com.sun.prism.es2.ES2PhongShader$DiffuseState", "com.sun.prism.es2.ES2PhongShader$SelfIllumState",
				"com.sun.prism.es2.ES2PhongShader$SpecularState", "com.sun.prism.impl.BaseMesh$FaceMembers",
				"com.sun.prism.impl.ps.BaseShaderContext$MaskType",
				"com.sun.prism.impl.ps.BaseShaderContext$SpecialShaderType",
				"com.sun.scenario.effect.impl.Renderer$RendererState", "com.sun.webkit.Timer$Mode",
				"com.sun.webkit.network.PublicSuffixes$Rule", "com.sun.webkit.network.SocketStreamHandle$State",
				"javafx.animation.PathTransition$OrientationType", "javafx.application.ConditionalFeature",
				"javafx.css.StyleOrigin", "javafx.fxml.FXMLLoader$SupportedType$1",
				"javafx.fxml.FXMLLoader$SupportedType$2", "javafx.fxml.FXMLLoader$SupportedType$3",
				"javafx.fxml.FXMLLoader$SupportedType$4", "javafx.fxml.FXMLLoader$SupportedType$5",
				"javafx.fxml.FXMLLoader$SupportedType$6", "javafx.geometry.HPos", "javafx.geometry.HorizontalDirection",
				"javafx.geometry.NodeOrientation", "javafx.geometry.Orientation", "javafx.geometry.Pos",
				"javafx.geometry.Side", "javafx.geometry.VPos", "javafx.geometry.VerticalDirection",
				"javafx.print.Collation", "javafx.print.PageOrientation", "javafx.print.PrintColor",
				"javafx.print.PrintQuality", "javafx.print.PrintSides", "javafx.print.PrinterJob$JobStatus",
				"javafx.scene.AccessibleAction", "javafx.scene.AccessibleAttribute", "javafx.scene.AccessibleRole",
				"javafx.scene.CacheHint", "javafx.scene.DepthTest", "javafx.scene.control.ContentDisplay",
				"javafx.scene.control.OverrunStyle", "javafx.scene.control.SelectionMode",
				"javafx.scene.control.TableUtil$SortEventType", "javafx.scene.control.TreeSortMode",
				"javafx.scene.effect.BlendMode", "javafx.scene.effect.BlurType",
				"javafx.scene.input.InputMethodHighlight", "javafx.scene.input.KeyCode",
				"javafx.scene.input.MouseButton", "javafx.scene.input.TransferMode",
				"javafx.scene.layout.BackgroundRepeat", "javafx.scene.layout.BorderRepeat",
				"javafx.scene.layout.Priority", "javafx.scene.paint.CycleMethod", "javafx.scene.shape.ArcType",
				"javafx.scene.shape.CullFace", "javafx.scene.shape.DrawMode", "javafx.scene.shape.FillRule",
				"javafx.scene.shape.StrokeLineCap", "javafx.scene.shape.StrokeLineJoin",
				"javafx.scene.shape.StrokeType", "javafx.scene.text.FontPosture", "javafx.scene.text.FontSmoothingType",
				"javafx.scene.text.FontWeight", "javafx.scene.text.TextAlignment", "javafx.scene.text.TextBoundsType",
				"javafx.scene.transform.MatrixType", "javafx.stage.Modality", "javafx.stage.StageStyle",
				"jdk.nashorn.internal.codegen.ClassEmitter$Flag", "jdk.nashorn.internal.codegen.CompilerConstants",
				"jdk.nashorn.internal.codegen.Condition", "jdk.nashorn.internal.objects.annotations.Where",
				"jdk.nashorn.internal.parser.TokenKind", "jdk.nashorn.internal.parser.TokenType",
				"jdk.nashorn.internal.runtime.Context$FieldMode", "jdk.nashorn.internal.runtime.JSErrorType",
				"jdk.nashorn.internal.runtime.JSType", "jdk.nashorn.internal.runtime.regexp.joni.constants.CCSTATE",
				"jdk.nashorn.internal.runtime.regexp.joni.constants.CCVALTYPE",
				"jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType", "sun.security.pkcs11.Secmod$ModuleType",
				"com.sun.jnlp.ApiDialog$DialogResult", "com.oracle.jrockit.jfr.ContentType",
				"com.oracle.jrockit.jfr.DataType", "com.oracle.jrockit.jfr.Transition", "oracle.jrockit.jfr.DCmd$Unit",
				"oracle.jrockit.jfr.MsgLevel", "sun.security.ssl.CipherSuite$KeyExchange",
				"sun.security.ssl.CipherSuite$PRF",
				"sun.security.ssl.SupportedEllipticCurvesExtension$NamedEllipticCurve",
				"sun.security.ssl.X509KeyManagerImpl$CheckResult", "sun.security.ssl.X509KeyManagerImpl$CheckType",
				"com.apple.concurrent.Dispatch$Priority", "com.oracle.webservices.internal.api.EnvelopeStyle$Style",
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType",
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingParameterStyle",
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingStyle",
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingUse",
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.WebParamMode",
				"com.sun.beans.util.Cache$Kind$1", "com.sun.beans.util.Cache$Kind$2", "com.sun.beans.util.Cache$Kind$3",
				"com.sun.corba.se.spi.protocol.RetryType",
				"com.sun.imageio.plugins.jpeg.JPEGImageReader$CallBackLock$State",
				"com.sun.imageio.plugins.jpeg.JPEGImageWriter$CallBackLock$State",
				"com.sun.java.swing.plaf.gtk.GTKConstants$ArrowType",
				"com.sun.java.swing.plaf.gtk.GTKConstants$ExpanderStyle",
				"com.sun.java.swing.plaf.gtk.GTKConstants$IconSize",
				"com.sun.java.swing.plaf.gtk.GTKConstants$Orientation",
				"com.sun.java.swing.plaf.gtk.GTKConstants$PositionType",
				"com.sun.java.swing.plaf.gtk.GTKConstants$ShadowType",
				"com.sun.java.swing.plaf.gtk.GTKConstants$StateType",
				"com.sun.java.swing.plaf.gtk.GTKConstants$TextDirection",
				"com.sun.java.swing.plaf.gtk.GTKEngine$Settings", "com.sun.java.swing.plaf.gtk.GTKEngine$WidgetType",
				"com.sun.java.swing.plaf.windows.TMSchema$Control", "com.sun.java.swing.plaf.windows.TMSchema$TypeEnum",
				"com.sun.management.VMOption$Origin", "com.sun.nio.file.ExtendedCopyOption",
				"com.sun.nio.file.ExtendedOpenOption", "com.sun.nio.file.ExtendedWatchEventModifier",
				"com.sun.nio.file.SensitivityWatchEventModifier",
				"com.sun.nio.sctp.AssociationChangeNotification$AssocChangeEvent", "com.sun.nio.sctp.HandlerResult",
				"com.sun.nio.sctp.PeerAddressChangeNotification$AddressChangeEvent",
				"com.sun.org.apache.xalan.internal.utils.FeatureManager$Feature",
				"com.sun.org.apache.xalan.internal.utils.FeatureManager$State",
				"com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase$State",
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityManager$Limit",
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityManager$NameMap",
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityManager$State",
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager$Property",
				"com.sun.org.apache.xerces.internal.util.Status",
				"com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer$NameMap",
				"com.sun.org.apache.xerces.internal.utils.XMLSecurityManager$NameMap",
				"com.sun.org.glassfish.external.arc.Stability",
				"com.sun.org.glassfish.external.probe.provider.PluginPoint", "com.sun.org.glassfish.gmbal.Impact",
				"com.sun.org.glassfish.gmbal.ManagedObjectManager$RegistrationDebugLevel",
				"com.sun.security.jgss.InquireType", "com.sun.security.ntlm.Version",
				"com.sun.tracing.dtrace.DependencyClass", "com.sun.tracing.dtrace.StabilityLevel",
				"com.sun.xml.internal.bind.Messages", "com.sun.xml.internal.bind.api.Messages",
				"com.sun.xml.internal.bind.v2.Messages", "com.sun.xml.internal.bind.v2.model.annotation.Messages",
				"com.sun.xml.internal.bind.v2.model.core.ID", "com.sun.xml.internal.bind.v2.model.core.PropertyKind",
				"com.sun.xml.internal.bind.v2.model.core.WildcardMode",
				"com.sun.xml.internal.bind.v2.model.impl.Messages", "com.sun.xml.internal.bind.v2.runtime.Messages",
				"com.sun.xml.internal.bind.v2.runtime.property.Messages",
				"com.sun.xml.internal.bind.v2.runtime.reflect.Messages",
				"com.sun.xml.internal.bind.v2.runtime.unmarshaller.Messages",
				"com.sun.xml.internal.bind.v2.schemagen.Form$1", "com.sun.xml.internal.bind.v2.schemagen.Form$2",
				"com.sun.xml.internal.bind.v2.schemagen.Form$3", "com.sun.xml.internal.bind.v2.schemagen.Form",
				"com.sun.xml.internal.bind.v2.schemagen.GroupKind", "com.sun.xml.internal.bind.v2.schemagen.Messages",
				"com.sun.xml.internal.ws.api.ComponentFeature$Target", "com.sun.xml.internal.ws.api.SOAPVersion",
				"com.sun.xml.internal.ws.api.addressing.AddressingVersion$1",
				"com.sun.xml.internal.ws.api.addressing.AddressingVersion$2",
				"com.sun.xml.internal.ws.api.addressing.AddressingVersion",
				"com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion$Setting",
				"com.sun.xml.internal.ws.api.databinding.SoapBodyStyle",
				"com.sun.xml.internal.ws.api.message.Packet$Status", "com.sun.xml.internal.ws.api.model.ExceptionType",
				"com.sun.xml.internal.ws.api.model.MEP", "com.sun.xml.internal.ws.api.model.ParameterBinding$Kind",
				"com.sun.xml.internal.ws.api.model.wsdl.WSDLDescriptorKind",
				"com.sun.xml.internal.ws.api.policy.subject.BindingSubject$WsdlMessageType",
				"com.sun.xml.internal.ws.api.policy.subject.BindingSubject$WsdlNameScope",
				"com.sun.xml.internal.ws.api.server.LazyMOMProvider$Scope",
				"com.sun.xml.internal.ws.api.server.LazyMOMProvider",
				"com.sun.xml.internal.ws.assembler.MetroTubelineAssembler$Side",
				"com.sun.xml.internal.ws.client.ContentNegotiation", "com.sun.xml.internal.ws.client.sei.ValueGetter$1",
				"com.sun.xml.internal.ws.client.sei.ValueGetter$2", "com.sun.xml.internal.ws.client.sei.ValueGetter",
				"com.sun.xml.internal.ws.handler.HandlerProcessor$RequestOrResponse",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$1",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$2",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$3",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$4",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$5",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$6",
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$7",
				"com.sun.xml.internal.ws.policy.jaxws.BuilderHandlerMessageScope$Scope",
				"com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLParserExtension$HandlerType",
				"com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion",
				"com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken",
				"com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject$WsdlNameScope",
				"com.sun.xml.internal.ws.server.sei.ValueGetter$1", "com.sun.xml.internal.ws.server.sei.ValueGetter$2",
				"com.sun.xml.internal.ws.server.sei.ValueGetter",
				"com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser$BindingMode", "java.awt.Desktop$Action",
				"java.awt.TrayIcon$MessageType", "java.awt.font.NumericShaper$Range$1",
				"java.lang.UNIXProcess$Platform", "java.lang.annotation.ElementType",
				"java.lang.annotation.RetentionPolicy", "java.lang.invoke.LambdaFormEditor$Transform$Kind",
				"java.lang.management.MemoryType", "java.lang.management.PlatformComponent", "java.math.RoundingMode",
				"java.net.StandardProtocolFamily", "java.nio.file.AccessMode", "java.nio.file.FileVisitOption",
				"java.nio.file.FileVisitResult", "java.nio.file.LinkOption", "java.nio.file.StandardCopyOption",
				"java.nio.file.StandardOpenOption", "java.nio.file.attribute.AclEntryFlag",
				"java.nio.file.attribute.AclEntryPermission", "java.nio.file.attribute.AclEntryType",
				"java.nio.file.attribute.PosixFilePermission", "java.security.CryptoPrimitive",
				"java.security.cert.CRLReason", "java.security.cert.CertPathValidatorException$BasicReason",
				"java.security.cert.PKIXReason", "java.security.cert.PKIXRevocationChecker$Option",
				"java.sql.ClientInfoStatus", "java.sql.JDBCType", "java.sql.PseudoColumnUsage",
				"java.sql.RowIdLifetime", "java.time.DayOfWeek", "java.time.Month", "java.time.chrono.HijrahEra",
				"java.time.chrono.IsoEra", "java.time.chrono.MinguoEra", "java.time.chrono.ThaiBuddhistEra",
				"java.time.format.DateTimeFormatterBuilder$SettingsParser", "java.time.format.FormatStyle",
				"java.time.format.ResolverStyle", "java.time.format.SignStyle", "java.time.format.TextStyle",
				"java.time.temporal.ChronoField", "java.time.temporal.ChronoUnit",
				"java.time.temporal.IsoFields$Field$1", "java.time.temporal.IsoFields$Field$2",
				"java.time.temporal.IsoFields$Field$3", "java.time.temporal.IsoFields$Field$4",
				"java.time.temporal.JulianFields$Field", "java.util.Comparators$NaturalOrderComparator",
				"java.util.Formatter$BigDecimalLayoutForm", "java.util.Locale$FilteringMode",
				"java.util.regex.UnicodeProp$1", "java.util.regex.UnicodeProp$10", "java.util.regex.UnicodeProp$11",
				"java.util.regex.UnicodeProp$12", "java.util.regex.UnicodeProp$13", "java.util.regex.UnicodeProp$14",
				"java.util.regex.UnicodeProp$15", "java.util.regex.UnicodeProp$16", "java.util.regex.UnicodeProp$17",
				"java.util.regex.UnicodeProp$18", "java.util.regex.UnicodeProp$19", "java.util.regex.UnicodeProp$2",
				"java.util.regex.UnicodeProp$3", "java.util.regex.UnicodeProp$4", "java.util.regex.UnicodeProp$5",
				"java.util.regex.UnicodeProp$6", "java.util.regex.UnicodeProp$7", "java.util.regex.UnicodeProp$8",
				"java.util.regex.UnicodeProp$9", "java.util.regex.UnicodeProp",
				"java.util.stream.Collector$Characteristics", "java.util.stream.StreamOpFlag",
				"java.util.stream.StreamShape", "javax.annotation.Resource$AuthenticationType",
				"javax.imageio.ImageIO$SpiInfo$1", "javax.imageio.ImageIO$SpiInfo$2", "javax.imageio.ImageIO$SpiInfo$3",
				"javax.lang.model.SourceVersion", "javax.lang.model.element.ElementKind",
				"javax.lang.model.element.Modifier", "javax.lang.model.element.NestingKind",
				"javax.lang.model.type.TypeKind", "javax.smartcardio.CardTerminals$State", "javax.swing.SortOrder",
				"javax.swing.plaf.nimbus.AbstractRegionPainter$PaintContext$CacheMode",
				"javax.swing.text.html.FormSubmitEvent$MethodType", "javax.tools.StandardLocation",
				"javax.xml.bind.annotation.XmlAccessOrder", "javax.xml.bind.annotation.XmlAccessType",
				"javax.xml.bind.annotation.XmlNsForm", "jdk.management.resource.ResourceAccuracy",
				"jdk.net.SocketFlow$Status", "org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod$Type",
				"sun.invoke.util.Wrapper", "sun.java2d.pisces.Dasher$LengthIterator$Side",
				"sun.launcher.LauncherHelper$SizePrefix", "sun.net.ftp.FtpClient$TransferType",
				"sun.net.ftp.FtpDirEntry$Permission", "sun.net.ftp.FtpDirEntry$Type", "sun.net.ftp.FtpReplyCode",
				"sun.net.httpserver.HttpConnection$State", "sun.net.httpserver.SSLStreams$BufType",
				"sun.net.sdp.SdpProvider$Action", "sun.net.www.protocol.http.AuthScheme",
				"sun.nio.ch.UnixAsynchronousSocketChannelImpl$OpType", "sun.nio.fs.AbstractWatchKey$State",
				"sun.reflect.annotation.TypeAnnotation$TypeAnnotationTarget", "sun.security.krb5.KdcComm$BpType",
				"sun.security.krb5.KrbAsReqBuilder$State", "sun.security.smartcardio.CardImpl$State",
				"sun.security.tools.keytool.Main$Command", "sun.launcher.LauncherHelper", "sun.font.EAttribute",
				"sun.awt.AppContext$State", "javax.swing.DropMode", "javax.swing.ClientPropertyKey",
				"java.util.concurrent.TimeUnit$7", "java.util.concurrent.TimeUnit$6", "java.util.concurrent.TimeUnit$5",
				"java.util.concurrent.TimeUnit$4", "java.util.concurrent.TimeUnit$3", "java.util.concurrent.TimeUnit$2",
				"java.util.concurrent.TimeUnit", "java.net.InetAddress$Cache$Type", "java.io.File$PathStatus",
				"com.apple.eawt.QuitStrategy", "apple.laf.JRSUIControl$BufferState",
				"com.oracle.tools.packager.JreUtils$Rule$Type", "com.oracle.tools.packager.RelativeFileSet$Type",
				"com.oracle.tools.packager.jnlp.JNLPBundler$Mode", "com.sun.javafx.tools.packager.PackagerLib$Filter",
				"com.sun.javafx.tools.packager.TemplatePlaceholders",
				"com.sun.javafx.tools.resource.DeployResource$Type", "com.oracle.javafx.jmx.json.JSONReader$EventType",
				"com.oracle.javafx.jmx.json.impl.JSONSymbol", "com.sun.tools.jconsole.JConsoleContext$ConnectionState",
				"sun.tools.jconsole.inspector.XNodeInfo$Type",
				"com.ibm.icu.impl.LocaleDisplayNamesImpl$CapitalizationContextUsage",
				"com.ibm.icu.impl.LocaleDisplayNamesImpl$DataTableType",
				"com.ibm.icu.impl.TimeZoneGenericNames$Pattern", "com.ibm.icu.impl.Trie2$ValueWidth",
				"com.ibm.icu.lang.UScript$ScriptUsage", "com.ibm.icu.text.AlphabeticIndex$Bucket$LabelType",
				"com.ibm.icu.text.CompactDecimalDataCache$QuoteState",
				"com.ibm.icu.text.CompactDecimalDataCache$UResFlags",
				"com.ibm.icu.text.CompactDecimalFormat$CompactStyle", "com.ibm.icu.text.DateFormat$BooleanAttribute",
				"com.ibm.icu.text.DateFormatSymbols$CapitalizationContextUsage",
				"com.ibm.icu.text.DateTimePatternGenerator$DTPGflags", "com.ibm.icu.text.DisplayContext",
				"com.ibm.icu.text.PluralRules$KeywordStatus", "com.ibm.icu.text.PluralRules$StandardPluralCategories",
				"com.ibm.icu.text.SimpleDateFormat$ContextValue",
				"com.ibm.icu.text.TimeZoneFormat$GMTOffsetPatternType", "com.ibm.icu.text.TimeZoneFormat$OffsetFields",
				"com.ibm.icu.text.TimeZoneFormat$ParseOption", "com.ibm.icu.text.UnicodeSet$ComparisonStyle",
				"com.ibm.icu.util.FormatWidth", "com.ibm.icu.util.GenderInfo$Gender",
				"com.ibm.icu.util.IslamicCalendar$CalculationType", "com.ibm.icu.util.Region$RegionType",
				"com.ibm.icu.util.StringTrieBuilder$State", "com.jrockit.mc.common.IMCAggregatedFrame$AggregationType",
				"com.jrockit.mc.common.IMCFrame$Type", "com.jrockit.mc.common.IMCStackTrace$TruncationState",
				"com.jrockit.mc.common.environment.OS$Type", "com.jrockit.mc.common.jvm.Connectable",
				"com.jrockit.mc.common.jvm.JVMArch", "com.jrockit.mc.common.jvm.JVMType",
				"com.jrockit.mc.common.persistence.LocalizedString$PlacementIdentifier",
				"com.jrockit.mc.common.unit.BinaryPrefix", "com.jrockit.mc.common.unit.DecimalPrefix",
				"com.jrockit.mc.common.util.SizeToolkit$Format", "com.jrockit.mc.common.util.TimeRangeToolkit$Format",
				"com.jrockit.mc.components.ui.contributions.ActivationPolicy",
				"com.jrockit.mc.components.ui.design.LayoutItemType",
				"com.jrockit.mc.components.ui.design.SupportedControl",
				"com.jrockit.mc.components.ui.design.actions.InsertAction$POSITION",
				"com.jrockit.mc.components.ui.settings.Orientation",
				"com.jrockit.mc.flightrecorder.controlpanel.ui.configuration.model.xml.XMLNodeType",
				"com.jrockit.mc.flightrecorder.controlpanel.ui.recordingconfiguration.PathElement$PathElementKind",
				"com.jrockit.mc.flightrecorder.ui.common.OperativeSetFilterDescription",
				"com.jrockit.mc.flightrecorder.ui.components.aggregators.AggregatorType",
				"com.jrockit.mc.flightrecorder.ui.components.chart.model.XAxisDataSource",
				"com.jrockit.mc.flightrecorder.ui.components.graph.TooltipVerbosity",
				"com.jrockit.mc.flightrecorder.ui.components.graph.TransitionLookup$Transition",
				"com.jrockit.mc.flightrecorder.ui.components.graph.renderer.coloring.EventColorType",
				"com.jrockit.mc.flightrecorder.ui.components.inputs.Role",
				"com.jrockit.mc.flightrecorder.ui.components.range.NanoTimeFormatter$Precision",
				"com.jrockit.mc.flightrecorder.RecordingPrinter$TimeFormat",
				"com.jrockit.mc.flightrecorder.RecordingPrinter$Verbosity",
				"com.jrockit.mc.flightrecorder.internal.parser.model.DataType",
				"com.jrockit.mc.flightrecorder.internal.parser.model.Transition",
				"com.jrockit.mc.flightrecorder.spi.EventOrder", "com.jrockit.mc.flightrecorder.spi.Expansion",
				"com.jrockit.mc.flightrecorder.spi.FieldType", "com.jrockit.mc.greychart.ui.model.Style",
				"com.jrockit.mc.greychart.ui.model.TickDensityName", "se.hirt.greychart.TickDensity",
				"se.hirt.greychart.data.RenderingMode", "se.hirt.greychart.impl.TimestampFormatter$Precision",
				"com.jrockit.mc.rjmx.subscription.storage.internal.AttributeStorageEventType",
				"com.jrockit.mc.ui.misc.ProgressCircle$Direction", "com.jrockit.mc.ui.model.fields.ByteUnit",
				"com.jrockit.mc.ui.model.fields.TimespanParser$TimeFactor", "javax.servlet.DispatcherType",
				"javax.servlet.SessionTrackingMode", "org.apache.commons.codec.language.bm.NameType",
				"org.apache.commons.codec.language.bm.RuleType", "org.apache.http.auth.AuthProtocolState",
				"org.apache.http.auth.ChallengeState", "org.apache.http.client.cache.CacheResponseStatus",
				"org.apache.http.entity.mime.HttpMultipartMode", "org.apache.http.impl.auth.NTLMScheme$State",
				"org.apache.http.impl.client.cache.RequestProtocolError",
				"org.apache.jasper.compiler.TagFileProcessor$TagFileDirectiveVisitor$Name",
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter$Side$1",
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter$Side$2",
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenizer$Side$1",
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenizer$Side$2",
				"org.apache.lucene.document.Field$Index$1", "org.apache.lucene.document.Field$Index$2",
				"org.apache.lucene.document.Field$Index$3", "org.apache.lucene.document.Field$Index$4",
				"org.apache.lucene.document.Field$Index$5", "org.apache.lucene.document.Field$Store$1",
				"org.apache.lucene.document.Field$Store$2", "org.apache.lucene.document.Field$TermVector$1",
				"org.apache.lucene.document.Field$TermVector$2", "org.apache.lucene.document.Field$TermVector$3",
				"org.apache.lucene.document.Field$TermVector$4", "org.apache.lucene.document.Field$TermVector$5",
				"org.apache.lucene.document.FieldSelectorResult", "org.apache.lucene.queryParser.QueryParser$Operator",
				"org.apache.lucene.search.BooleanClause$Occur$1", "org.apache.lucene.search.BooleanClause$Occur$2",
				"org.apache.lucene.search.BooleanClause$Occur$3", "org.apache.lucene.util.Version",
				"org.eclipse.e4.ui.model.application.ui.menu.ItemType",
				"org.eclipse.e4.ui.model.application.ui.SideValue", "org.eclipse.e4.ui.model.internal.Position",
				"org.eclipse.emf.ecore.change.ChangeKind",
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark$1",
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark$2",
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark$3",
				"org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl$BinaryIO$FeatureKind",
				"org.eclipse.emf.ecore.xml.namespace.SpaceType", "org.eclipse.equinox.console.common.KEYS",
				"org.eclipse.equinox.internal.p2.publisher.eclipse.ProductContentType",
				"org.eclipse.jetty.security.UserDataConstraint",
				"org.osgi.service.component.annotations.ConfigurationPolicy",
				"org.osgi.service.component.annotations.ReferenceCardinality",
				"org.osgi.service.component.annotations.ReferencePolicyOption",
				"org.osgi.service.component.annotations.ReferencePolicy",
				"org.eclipse.osgi.report.resolution.ResolutionReport$Entry$Type",
				"org.sat4j.minisat.core.LearnedConstraintsEvaluationType", "org.sat4j.minisat.core.SimplificationType",
				"org.sat4j.tools.encoding.EncodingStrategy", "oracle.jvm.hotspot.jfr.ThreadStates$States",
				"sun.jvm.hotspot.debugger.arm.ARMThreadContext$Arch", "sun.jvm.hotspot.gc_interface.G1YCType",
				"sun.jvm.hotspot.gc_interface.GCCause", "sun.jvm.hotspot.gc_interface.GCName",
				"sun.jvm.hotspot.gc_interface.GCWhen", "sun.jvm.hotspot.gc_interface.ReferenceType",
				"sun.jvm.hotspot.opto.CompilerPhaseType", "sun.jvm.hotspot.runtime.Flags",
				"sun.jvm.hotspot.runtime.VMOps", "com.sun.javadoc.LanguageVersion",
				"com.sun.source.doctree.AttributeTree$ValueKind", "com.sun.source.doctree.DocTree$Kind",
				"com.sun.source.tree.LambdaExpressionTree$BodyKind",
				"com.sun.source.tree.MemberReferenceTree$ReferenceMode", "com.sun.source.tree.Tree$Kind",
				"com.sun.source.util.TaskEvent$Kind", "com.sun.tools.classfile.AccessFlags$Kind",
				"com.sun.tools.classfile.Opcode$Set", "com.sun.tools.classfile.Opcode",
				"com.sun.tools.doclets.formats.html.markup.HtmlAttr",
				"com.sun.tools.doclets.formats.html.markup.HtmlStyle",
				"com.sun.tools.doclets.formats.html.markup.HtmlTag$EndTag",
				"com.sun.tools.doclets.formats.html.markup.HtmlTag", "com.sun.tools.doclets.formats.html.SectionName",
				"com.sun.tools.doclets.internal.toolkit.util.MethodTypes", "com.sun.tools.doclint.Checker$Flag",
				"com.sun.tools.doclint.Entity", "com.sun.tools.doclint.Env$AccessKind",
				"com.sun.tools.doclint.HtmlTag$1", "com.sun.tools.doclint.HtmlTag$10",
				"com.sun.tools.doclint.HtmlTag$2", "com.sun.tools.doclint.HtmlTag$3", "com.sun.tools.doclint.HtmlTag$4",
				"com.sun.tools.doclint.HtmlTag$5", "com.sun.tools.doclint.HtmlTag$6", "com.sun.tools.doclint.HtmlTag$7",
				"com.sun.tools.doclint.HtmlTag$8", "com.sun.tools.doclint.HtmlTag$9", "com.sun.tools.doclint.HtmlTag",
				"com.sun.tools.internal.jxc.ap.Const", "com.sun.tools.internal.jxc.ap.Messages",
				"com.sun.tools.internal.jxc.Messages",
				"com.sun.tools.internal.ws.processor.generator.GeneratorConstants",
				"com.sun.tools.internal.ws.processor.modeler.annotation.WebServiceConstants",
				"com.sun.tools.internal.ws.processor.modeler.ModelerConstants",
				"com.sun.tools.internal.ws.processor.modeler.wsdl.WSDLModeler$StyleAndUse",
				"com.sun.tools.internal.xjc.api.SpecVersion", "com.sun.tools.internal.xjc.api.util.Messages",
				"com.sun.tools.internal.xjc.generator.bean.field.Messages",
				"com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy",
				"com.sun.tools.internal.xjc.generator.bean.Messages", "com.sun.tools.internal.xjc.Language",
				"com.sun.tools.internal.xjc.outline.Aspect", "com.sun.tools.internal.xjc.reader.Messages",
				"com.sun.tools.internal.xjc.reader.RawTypeSet$Mode",
				"com.sun.tools.internal.xjc.reader.relaxng.BindStyle",
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding$UnderscoreBinding",
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.EnumMemberMode",
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.LocalScoping",
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.Messages",
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.OptionalPropertyMode",
				"com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeBindingMode",
				"com.sun.tools.internal.xjc.reader.xmlschema.ct.Messages",
				"com.sun.tools.javac.api.DiagnosticFormatter$Configuration$DiagnosticPart",
				"com.sun.tools.javac.api.DiagnosticFormatter$Configuration$MultilineLimit",
				"com.sun.tools.javac.api.DiagnosticFormatter$PositionKind",
				"com.sun.tools.javac.code.Attribute$RetentionPolicy", "com.sun.tools.javac.code.BoundKind",
				"com.sun.tools.javac.code.Flags$Flag", "com.sun.tools.javac.code.Kinds$KindName",
				"com.sun.tools.javac.code.Source", "com.sun.tools.javac.code.TargetType",
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound$1",
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound$2",
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound$3", "com.sun.tools.javac.code.TypeTag",
				"com.sun.tools.javac.comp.CompileStates$CompileState",
				"com.sun.tools.javac.comp.Flow$BaseAnalyzer$JumpKind$1",
				"com.sun.tools.javac.comp.Flow$BaseAnalyzer$JumpKind$2",
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$1", "com.sun.tools.javac.comp.Infer$BoundErrorKind$2",
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$3", "com.sun.tools.javac.comp.Infer$BoundErrorKind$4",
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$5", "com.sun.tools.javac.comp.Infer$BoundErrorKind$6",
				"com.sun.tools.javac.comp.Infer$DependencyKind", "com.sun.tools.javac.comp.Infer$GraphInferenceSteps",
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$1",
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$2",
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$3",
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$4",
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$5",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$1",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$10",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$11",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$2",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$3",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$4",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$5",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$6",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$7",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$8",
				"com.sun.tools.javac.comp.Infer$IncorporationStep$9", "com.sun.tools.javac.comp.Infer$InferenceStep$1",
				"com.sun.tools.javac.comp.Infer$InferenceStep$2", "com.sun.tools.javac.comp.Infer$InferenceStep$3",
				"com.sun.tools.javac.comp.Infer$InferenceStep$4", "com.sun.tools.javac.comp.Infer$InferenceStep$5",
				"com.sun.tools.javac.comp.Infer$InferenceStep$6", "com.sun.tools.javac.comp.Infer$LegacyInferenceSteps",
				"com.sun.tools.javac.comp.Resolve$InterfaceLookupPhase$1",
				"com.sun.tools.javac.comp.Resolve$InterfaceLookupPhase$2",
				"com.sun.tools.javac.comp.Resolve$MethodResolutionPhase$1",
				"com.sun.tools.javac.comp.Resolve$SearchResultKind",
				"com.sun.tools.javac.file.JavacFileManager$SortFiles$1",
				"com.sun.tools.javac.file.JavacFileManager$SortFiles$2", "com.sun.tools.javac.jvm.ClassFile$Version",
				"com.sun.tools.javac.jvm.Code$StackMapFormat$1", "com.sun.tools.javac.jvm.Code$StackMapFormat$2",
				"com.sun.tools.javac.jvm.Profile$1", "com.sun.tools.javac.jvm.Profile",
				"com.sun.tools.javac.jvm.Target", "com.sun.tools.javac.main.JavaCompiler$ImplicitSourcePolicy",
				"com.sun.tools.javac.main.Option$1", "com.sun.tools.javac.main.Option$10",
				"com.sun.tools.javac.main.Option$11", "com.sun.tools.javac.main.Option$12",
				"com.sun.tools.javac.main.Option$13", "com.sun.tools.javac.main.Option$14",
				"com.sun.tools.javac.main.Option$15", "com.sun.tools.javac.main.Option$16",
				"com.sun.tools.javac.main.Option$17", "com.sun.tools.javac.main.Option$18",
				"com.sun.tools.javac.main.Option$19", "com.sun.tools.javac.main.Option$2",
				"com.sun.tools.javac.main.Option$20", "com.sun.tools.javac.main.Option$21",
				"com.sun.tools.javac.main.Option$22", "com.sun.tools.javac.main.Option$23",
				"com.sun.tools.javac.main.Option$24", "com.sun.tools.javac.main.Option$25",
				"com.sun.tools.javac.main.Option$26", "com.sun.tools.javac.main.Option$27",
				"com.sun.tools.javac.main.Option$3", "com.sun.tools.javac.main.Option$4",
				"com.sun.tools.javac.main.Option$5", "com.sun.tools.javac.main.Option$6",
				"com.sun.tools.javac.main.Option$7", "com.sun.tools.javac.main.Option$8",
				"com.sun.tools.javac.main.Option$9", "com.sun.tools.javac.main.Option$ChoiceKind",
				"com.sun.tools.javac.main.Option", "com.sun.tools.javac.parser.JavacParser$BasicErrorRecoveryAction$1",
				"com.sun.tools.javac.parser.JavacParser$BasicErrorRecoveryAction$2",
				"com.sun.tools.javac.util.BasicDiagnosticFormatter$BasicConfiguration$BasicFormatKind",
				"com.sun.tools.javac.util.BasicDiagnosticFormatter$BasicConfiguration$SourcePosition",
				"com.sun.tools.javac.util.MandatoryWarningHandler$DeferredDiagnosticKind",
				"com.sun.tools.javac.util.RichDiagnosticFormatter$RichConfiguration$RichFormatterFeature",
				"com.sun.tools.javadoc.ToolOption$1", "com.sun.tools.javadoc.ToolOption$10",
				"com.sun.tools.javadoc.ToolOption$11", "com.sun.tools.javadoc.ToolOption$12",
				"com.sun.tools.javadoc.ToolOption$13", "com.sun.tools.javadoc.ToolOption$14",
				"com.sun.tools.javadoc.ToolOption$15", "com.sun.tools.javadoc.ToolOption$16",
				"com.sun.tools.javadoc.ToolOption$17", "com.sun.tools.javadoc.ToolOption$18",
				"com.sun.tools.javadoc.ToolOption$19", "com.sun.tools.javadoc.ToolOption$2",
				"com.sun.tools.javadoc.ToolOption$20", "com.sun.tools.javadoc.ToolOption$21",
				"com.sun.tools.javadoc.ToolOption$22", "com.sun.tools.javadoc.ToolOption$23",
				"com.sun.tools.javadoc.ToolOption$24", "com.sun.tools.javadoc.ToolOption$25",
				"com.sun.tools.javadoc.ToolOption$3", "com.sun.tools.javadoc.ToolOption$4",
				"com.sun.tools.javadoc.ToolOption$5", "com.sun.tools.javadoc.ToolOption$6",
				"com.sun.tools.javadoc.ToolOption$7", "com.sun.tools.javadoc.ToolOption$8",
				"com.sun.tools.javadoc.ToolOption$9", "com.sun.tools.javadoc.ToolOption",
				"com.sun.tools.javap.LocalVariableTableWriter$NoteKind$1",
				"com.sun.tools.javap.LocalVariableTableWriter$NoteKind$2",
				"com.sun.tools.javap.LocalVariableTypeTableWriter$NoteKind$1",
				"com.sun.tools.javap.LocalVariableTypeTableWriter$NoteKind$2",
				"com.sun.tools.javap.TryBlockWriter$NoteKind$1", "com.sun.tools.javap.TryBlockWriter$NoteKind$2",
				"com.sun.tools.javap.TryBlockWriter$NoteKind$3", "com.sun.tools.jdeps.Profile",
				"com.sun.tools.jdi.EventDestination", "sun.rmi.rmic.newrmic.jrmp.Constants$StubVersion",
				"javax.annotation.meta.When", "org.netbeans.api.search.SearchPattern$MatchType",
				"org.netbeans.api.search.provider.SearchFilter$FolderResult",
				"org.netbeans.modules.search.Constants$Limit", "org.netbeans.api.visual.action.ConnectorState",
				"org.netbeans.api.visual.action.ContiguousSelectEvent$SelectionType",
				"org.netbeans.api.visual.action.InplaceEditorProvider$EditorInvocationType",
				"org.netbeans.api.visual.anchor.Anchor$Direction",
				"org.netbeans.api.visual.anchor.AnchorFactory$DirectionalAnchorKind",
				"org.netbeans.api.visual.anchor.AnchorShapeFactory$ConnectionEnd",
				"org.netbeans.api.visual.export.SceneExporter$ImageType",
				"org.netbeans.api.visual.export.SceneExporter$ZoomType",
				"org.netbeans.api.visual.graph.layout.TreeGraphLayoutAlignment",
				"org.netbeans.api.visual.layout.LayoutFactory$ConnectionWidgetLayoutAlignment",
				"org.netbeans.api.visual.layout.LayoutFactory$SerialAlignment",
				"org.netbeans.api.visual.model.ObjectSceneEventType",
				"org.netbeans.api.visual.print.ScenePrinter$ScaleStrategy",
				"org.netbeans.api.visual.widget.EventProcessingType",
				"org.netbeans.modules.visual.graph.layout.hierarchicalsupport.DirectedGraph$DummyVertex$Type",
				"org.netbeans.core.output2.OutputKind",
				"org.netbeans.core.ui.options.general.GeneralOptionsModel$TestingStatus",
				"org.netbeans.core.ui.options.general.WebBrowsersOptionsModel$ChangeStatus",
				"org.netbeans.core.windows.WindowSystemEventType", "org.netbeans.lib.uihandler.InputGesture",
				"org.netbeans.modules.print.util.Macro", "org.netbeans.modules.uihandler.Installer$Submit$DialogState",
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$1",
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$2",
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$3",
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$4",
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType", "org.openide.awt.ContextSelection",
				"org.openide.text.DocumentStatus", "org.netbeans.lib.profiler.ui.threads.ViewManager$Position",
				"com.sun.tools.visualvm.modules.appui.options.NetworkOptionsModel$TestingStatus",
				//
				"org.apache.derby.iapi.security.Securable", //
				"com.sun.deploy.model.LocalApplicationProperties$Kind", //
				"com.sun.deploy.ref.AppModel$SecurityMode", //
				"com.sun.deploy.ref.AppModel$Type", //
				"com.sun.deploy.ref.AppRef$Type", //
				"com.sun.deploy.security.ValidationState$TYPE", //
				"com.sun.deploy.security.ruleset.DRSResult$Type", //
				"com.sun.deploy.util.SecurityBaseline$UpdateCheckStatus", //
				"com.sun.glass.ui.EventLoop$State", //
				"com.sun.glass.ui.GestureSupport$GestureState$StateId", //
				"com.sun.glass.ui.mac.MacAccessible$MacAction", //
				"com.sun.glass.ui.mac.MacAccessible$MacAttribute", //
				"com.sun.glass.ui.mac.MacAccessible$MacNotification", //
				"com.sun.glass.ui.mac.MacAccessible$MacOrientation", //
				"com.sun.glass.ui.mac.MacAccessible$MacRole", //
				"com.sun.glass.ui.mac.MacAccessible$MacSubrole", //
				"com.sun.glass.ui.mac.MacAccessible$MacText", //
				"com.sun.javafx.css.Combinator$1", //
				"com.sun.javafx.css.Combinator$2", //
				"com.sun.javafx.css.Combinator", //
				"com.sun.javafx.css.FontFace$FontFaceSrcType", //
				"com.sun.javafx.css.SizeUnits$1", //
				"com.sun.javafx.css.SizeUnits$10", //
				"com.sun.javafx.css.SizeUnits$11", //
				"com.sun.javafx.css.SizeUnits$12", //
				"com.sun.javafx.css.SizeUnits$13", //
				"com.sun.javafx.css.SizeUnits$14", //
				"com.sun.javafx.css.SizeUnits$15", //
				"com.sun.javafx.css.SizeUnits$2", //
				"com.sun.javafx.css.SizeUnits$3", //
				"com.sun.javafx.css.SizeUnits$4", //
				"com.sun.javafx.css.SizeUnits$5", //
				"com.sun.javafx.css.SizeUnits$6", //
				"com.sun.javafx.css.SizeUnits$7", //
				"com.sun.javafx.css.SizeUnits$8", //
				"com.sun.javafx.css.SizeUnits$9", //
				"com.sun.javafx.css.SizeUnits", //
				"com.sun.javafx.cursor.CursorType", //
				"com.sun.javafx.effect.EffectDirtyBits", //
				"com.sun.javafx.fxml.expression.Expression$Parser$TokenType", //
				"com.sun.javafx.fxml.expression.Operator", //
				"com.sun.javafx.geom.BaseBounds$BoundsType", //
				"com.sun.javafx.geom.Path2D$CornerPrefix", //
				"com.sun.javafx.geom.transform.BaseTransform$Degree", //
				"com.sun.javafx.iio.ImageStorage$ImageType", //
				"com.sun.javafx.image.AlphaType", //
				"com.sun.javafx.print.Units", //
				"com.sun.javafx.scene.CssFlags", //
				"com.sun.javafx.scene.DirtyBits", //
				"com.sun.javafx.scene.LayoutFlags", //
				"com.sun.javafx.scene.control.behavior.OptionalBoolean", //
				"com.sun.javafx.scene.control.behavior.TextFieldBehavior$TextInputTypes", //
				"com.sun.javafx.scene.control.skin.ButtonBarSkin$Spacer$1", //
				"com.sun.javafx.scene.control.skin.ButtonBarSkin$Spacer$2", //
				"com.sun.javafx.scene.control.skin.ButtonBarSkin$Spacer", //
				"com.sun.javafx.scene.control.skin.ComboBoxMode", //
				"com.sun.javafx.scene.control.skin.FXVK$Type", //
				"com.sun.javafx.scene.control.skin.TabPaneSkin$TabAnimation", //
				"com.sun.javafx.scene.control.skin.TabPaneSkin$TabAnimationState", //
				"com.sun.javafx.scene.traversal.Direction", //
				"com.sun.javafx.sg.prism.CacheFilter$ScrollCacheState", //
				"com.sun.javafx.sg.prism.NGCanvas$InitType", //
				"com.sun.javafx.sg.prism.NGNode$DirtyFlag", //
				"com.sun.javafx.sg.prism.NGNode$RenderRootResult", //
				"com.sun.javafx.sg.prism.NGShape$Mode", //
				"com.sun.javafx.sg.prism.NodeEffectInput$RenderType", //
				"com.sun.javafx.tk.FileChooserType", //
				"com.sun.javafx.tk.FocusCause", //
				"com.sun.javafx.tk.quantum.RotateGestureRecognizer$RotateRecognitionState", //
				"com.sun.javafx.tk.quantum.ScrollGestureRecognizer$ScrollRecognitionState", //
				"com.sun.javafx.tk.quantum.SwipeGestureRecognizer$SwipeRecognitionState", //
				"com.sun.javafx.tk.quantum.ZoomGestureRecognizer$ZoomRecognitionState", //
				"com.sun.javafx.webkit.prism.WCGraphicsPrismContext$Type", //
				"com.sun.javafx.webkit.theme.RenderThemeImpl$WidgetType", //
				"com.sun.media.jfxmedia.MediaError", //
				"com.sun.media.jfxmedia.control.VideoFormat", //
				"com.sun.media.jfxmedia.events.PlayerStateEvent$PlayerState", //
				"com.sun.media.jfxmedia.track.Track$Encoding", //
				"com.sun.openpisces.Dasher$LengthIterator$Side", //
				"com.sun.prism.CompositeMode", //
				"com.sun.prism.GraphicsPipeline$ShaderModel", //
				"com.sun.prism.GraphicsPipeline$ShaderType", //
				"com.sun.prism.PhongMaterial$MapType", //
				"com.sun.prism.PixelFormat$DataType", //
				"com.sun.prism.PixelFormat", //
				"com.sun.prism.Texture$Usage", //
				"com.sun.prism.Texture$WrapMode", //
				"com.sun.prism.es2.ES2PhongShader$BumpMapState", //
				"com.sun.prism.es2.ES2PhongShader$DiffuseState", //
				"com.sun.prism.es2.ES2PhongShader$SelfIllumState", //
				"com.sun.prism.es2.ES2PhongShader$SpecularState", //
				"com.sun.prism.impl.BaseMesh$FaceMembers", //
				"com.sun.prism.impl.PrismTrace$SummaryType", //
				"com.sun.prism.impl.ps.BaseShaderContext$MaskType", //
				"com.sun.prism.impl.ps.BaseShaderContext$SpecialShaderType", //
				"com.sun.prism.j2d.paint.MultipleGradientPaint$ColorSpaceType", //
				"com.sun.prism.j2d.paint.MultipleGradientPaint$CycleMethod", //
				"com.sun.prism.paint.Paint$Type", //
				"com.sun.prism.shape.ShapeRep$InvalidationType", //
				"com.sun.scenario.effect.AbstractShadow$ShadowMode", //
				"com.sun.scenario.effect.Blend$Mode", //
				"com.sun.scenario.effect.Effect$AccelType", //
				"com.sun.scenario.effect.impl.Renderer$RendererState", //
				"com.sun.scenario.effect.impl.state.LinearConvolveRenderState$PassType", //
				"com.sun.scenario.effect.impl.state.RenderState$EffectCoordinateSpace", //
				"com.sun.scenario.effect.light.Light$Type", //
				"com.sun.webkit.Timer$Mode", //
				"com.sun.webkit.network.PublicSuffixes$Rule", //
				"com.sun.webkit.network.SocketStreamHandle$State", //
				"javafx.animation.Animation$Status", //
				"javafx.animation.KeyValue$Type", //
				"javafx.animation.PathTransition$OrientationType", //
				"javafx.application.ConditionalFeature", //
				"javafx.application.Preloader$StateChangeNotification$Type", //
				"javafx.concurrent.Worker$State", //
				"javafx.css.StyleOrigin", //
				"javafx.fxml.FXMLLoader$SupportedType$1", //
				"javafx.fxml.FXMLLoader$SupportedType$2", //
				"javafx.fxml.FXMLLoader$SupportedType$3", //
				"javafx.fxml.FXMLLoader$SupportedType$4", //
				"javafx.fxml.FXMLLoader$SupportedType$5", //
				"javafx.fxml.FXMLLoader$SupportedType$6", //
				"javafx.fxml.FXMLLoader$SupportedType", //
				"javafx.geometry.HPos", //
				"javafx.geometry.HorizontalDirection", //
				"javafx.geometry.NodeOrientation", //
				"javafx.geometry.Orientation", //
				"javafx.geometry.Pos", //
				"javafx.geometry.Side", //
				"javafx.geometry.VPos", //
				"javafx.geometry.VerticalDirection", //
				"javafx.print.Collation", //
				"javafx.print.PageOrientation", //
				"javafx.print.PrintColor", //
				"javafx.print.PrintQuality", //
				"javafx.print.PrintSides", //
				"javafx.print.Printer$MarginType", //
				"javafx.print.PrinterJob$JobStatus", //
				"javafx.scene.AccessibleAction", //
				"javafx.scene.AccessibleAttribute", //
				"javafx.scene.AccessibleRole", //
				"javafx.scene.CacheHint", //
				"javafx.scene.DepthTest", //
				"javafx.scene.Scene$DirtyBits", //
				"javafx.scene.Scene$DragDetectedState", //
				"javafx.scene.SubScene$SubSceneDirtyBits", //
				"javafx.scene.chart.LineChart$SortingPolicy", //
				"javafx.scene.chart.StackedAreaChart$PartOf", //
				"javafx.scene.control.Alert$AlertType", //
				"javafx.scene.control.ButtonBar$ButtonData", //
				"javafx.scene.control.ContentDisplay", //
				"javafx.scene.control.OverrunStyle", //
				"javafx.scene.control.ScrollPane$ScrollBarPolicy", //
				"javafx.scene.control.SelectionMode", //
				"javafx.scene.control.TabPane$TabClosingPolicy", //
				"javafx.scene.control.TableColumn$SortType", //
				"javafx.scene.control.TableUtil$SortEventType", //
				"javafx.scene.control.TreeSortMode", //
				"javafx.scene.control.TreeTableColumn$SortType", //
				"javafx.scene.effect.BlendMode", //
				"javafx.scene.effect.BlurType", //
				"javafx.scene.image.PixelFormat$Type", //
				"javafx.scene.input.InputMethodHighlight", //
				"javafx.scene.input.KeyCode", //
				"javafx.scene.input.KeyCombination$ModifierValue", //
				"javafx.scene.input.MouseButton", //
				"javafx.scene.input.ScrollEvent$HorizontalTextScrollUnits", //
				"javafx.scene.input.ScrollEvent$VerticalTextScrollUnits", //
				"javafx.scene.input.TouchPoint$State", //
				"javafx.scene.input.TransferMode", //
				"javafx.scene.layout.BackgroundRepeat", //
				"javafx.scene.layout.BorderRepeat", //
				"javafx.scene.layout.Priority", //
				"javafx.scene.media.MediaException$Type", //
				"javafx.scene.media.MediaPlayer$Status", //
				"javafx.scene.paint.CycleMethod", //
				"javafx.scene.shape.ArcType", //
				"javafx.scene.shape.CullFace", //
				"javafx.scene.shape.DrawMode", //
				"javafx.scene.shape.FillRule", //
				"javafx.scene.shape.StrokeLineCap", //
				"javafx.scene.shape.StrokeLineJoin", //
				"javafx.scene.shape.StrokeType", //
				"javafx.scene.text.FontPosture", //
				"javafx.scene.text.FontSmoothingType", //
				"javafx.scene.text.FontWeight", //
				"javafx.scene.text.TextAlignment", //
				"javafx.scene.text.TextBoundsType", //
				"javafx.scene.transform.MatrixType", //
				"javafx.stage.Modality", //
				"javafx.stage.PopupWindow$AnchorLocation", //
				"javafx.stage.StageStyle", //
				"jdk.internal.dynalink.beans.BeanLinker$CollectionType", //
				"jdk.internal.dynalink.beans.GuardedInvocationComponent$ValidationType", //
				"jdk.internal.dynalink.linker.ConversionComparator$Comparison", //
				"jdk.nashorn.internal.codegen.ClassEmitter$Flag", //
				"jdk.nashorn.internal.codegen.CompilerConstants", //
				"jdk.nashorn.internal.codegen.Condition", //
				"jdk.nashorn.internal.codegen.LocalVariableTypesCalculator$LvarType", //
				"jdk.nashorn.internal.ir.FunctionNode$Kind", //
				"jdk.nashorn.internal.ir.RuntimeNode$Request", //
				"jdk.nashorn.internal.objects.annotations.Where", //
				"jdk.nashorn.internal.parser.DateParser$Token", //
				"jdk.nashorn.internal.parser.TokenKind", //
				"jdk.nashorn.internal.parser.TokenType", //
				"jdk.nashorn.internal.runtime.Context$FieldMode", //
				"jdk.nashorn.internal.runtime.JSErrorType", //
				"jdk.nashorn.internal.runtime.JSType", //
				"jdk.nashorn.internal.runtime.linker.AdaptationResult$Outcome", //
				"jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode$ReduceType", //
				"jdk.nashorn.internal.runtime.regexp.joni.constants.CCSTATE", //
				"jdk.nashorn.internal.runtime.regexp.joni.constants.CCVALTYPE", //
				"jdk.nashorn.internal.runtime.regexp.joni.constants.TokenType", //
				"jdk.nashorn.internal.runtime.ScriptEnvironment$FunctionStatementBehavior", //
				"sun.security.pkcs11.Secmod$ModuleType", //
				"sun.security.pkcs11.Secmod$TrustType", //
				"sun.security.pkcs11.Secmod$DbMode", //
				"com.sun.nio.zipfs.ZipFileAttributeView$AttrID", //
				"com.sun.jnlp.ApiDialog$DialogResult", //
				"com.oracle.jrockit.jfr.ContentType", //
				"com.oracle.jrockit.jfr.DataType", //
				"com.oracle.jrockit.jfr.Transition", //
				"oracle.jrockit.jfr.DCmd$Unit", //
				"oracle.jrockit.jfr.MsgLevel", //
				"sun.security.ssl.CipherSuite$CipherType", //
				"sun.security.ssl.CipherSuite$KeyExchange", //
				"sun.security.ssl.CipherSuite$PRF", //
				"sun.security.ssl.SignatureAndHashAlgorithm$HashAlgorithm", //
				"sun.security.ssl.SignatureAndHashAlgorithm$SignatureAlgorithm", //
				"sun.security.ssl.SupportedEllipticCurvesExtension$NamedEllipticCurve", //
				"sun.security.ssl.X509KeyManagerImpl$CheckResult", //
				"sun.security.ssl.X509KeyManagerImpl$CheckType", //
				"com.apple.concurrent.Dispatch$Priority", //
				"com.oracle.webservices.internal.api.EnvelopeStyle$Style", //
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType", //
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingParameterStyle", //
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingStyle", //
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingUse", //
				"com.oracle.xmlns.internal.webservices.jaxws_databinding.WebParamMode", //
				"com.sun.awt.AWTUtilities$Translucency", //
				"com.sun.beans.util.Cache$Kind$1", //
				"com.sun.beans.util.Cache$Kind$2", //
				"com.sun.beans.util.Cache$Kind$3", //
				"com.sun.beans.util.Cache$Kind", //
				"com.sun.corba.se.spi.protocol.RetryType", //
				"com.sun.imageio.plugins.jpeg.JPEGImageReader$CallBackLock$State", //
				"com.sun.imageio.plugins.jpeg.JPEGImageWriter$CallBackLock$State", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$ArrowType", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$ExpanderStyle", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$IconSize", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$Orientation", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$PositionType", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$ShadowType", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$StateType", //
				"com.sun.java.swing.plaf.gtk.GTKConstants$TextDirection", //
				"com.sun.java.swing.plaf.gtk.GTKEngine$Settings", //
				"com.sun.java.swing.plaf.gtk.GTKEngine$WidgetType", //
				"com.sun.java.swing.plaf.windows.TMSchema$Control", //
				"com.sun.java.swing.plaf.windows.TMSchema$Part", //
				"com.sun.java.swing.plaf.windows.TMSchema$Prop", //
				"com.sun.java.swing.plaf.windows.TMSchema$State", //
				"com.sun.java.swing.plaf.windows.TMSchema$TypeEnum", //
				"com.sun.jmx.remote.security.MBeanServerFileAccessController$AccessType", //
				"com.sun.management.VMOption$Origin", //
				"com.sun.nio.file.ExtendedCopyOption", //
				"com.sun.nio.file.ExtendedOpenOption", //
				"com.sun.nio.file.ExtendedWatchEventModifier", //
				"com.sun.nio.file.SensitivityWatchEventModifier", //
				"com.sun.nio.sctp.AssociationChangeNotification$AssocChangeEvent", //
				"com.sun.nio.sctp.HandlerResult", //
				"com.sun.nio.sctp.PeerAddressChangeNotification$AddressChangeEvent", //
				"com.sun.org.apache.xalan.internal.utils.FeatureManager$Feature", //
				"com.sun.org.apache.xalan.internal.utils.FeatureManager$State", //
				"com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase$State", //
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityManager$Limit", //
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityManager$NameMap", //
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityManager$State", //
				"com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager$Property", //
				"com.sun.org.apache.xerces.internal.impl.XMLScanner$NameType", //
				"com.sun.org.apache.xerces.internal.util.Status", //
				"com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer$NameMap", //
				"com.sun.org.apache.xerces.internal.utils.XMLSecurityManager$Limit", //
				"com.sun.org.apache.xerces.internal.utils.XMLSecurityManager$NameMap", //
				"com.sun.org.apache.xerces.internal.utils.XMLSecurityManager$State", //
				"com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager$Property", //
				"com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager$State", //
				"com.sun.org.glassfish.external.arc.Stability", //
				"com.sun.org.glassfish.external.probe.provider.PluginPoint", //
				"com.sun.org.glassfish.gmbal.Impact", //
				"com.sun.org.glassfish.gmbal.ManagedObjectManager$RegistrationDebugLevel", //
				"com.sun.security.jgss.InquireType", //
				"com.sun.security.ntlm.Version", //
				"com.sun.tracing.dtrace.DependencyClass", //
				"com.sun.tracing.dtrace.StabilityLevel", //
				"com.sun.xml.internal.bind.Messages", //
				"com.sun.xml.internal.bind.api.Messages", //
				"com.sun.xml.internal.bind.v2.Messages", //
				"com.sun.xml.internal.bind.v2.model.annotation.Messages", //
				"com.sun.xml.internal.bind.v2.model.core.ID", //
				"com.sun.xml.internal.bind.v2.model.core.PropertyKind", //
				"com.sun.xml.internal.bind.v2.model.core.WildcardMode", //
				"com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl$PropertyGroup", //
				"com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl$SecondaryAnnotation", //
				"com.sun.xml.internal.bind.v2.model.impl.Messages", //
				"com.sun.xml.internal.bind.v2.runtime.Messages", //
				"com.sun.xml.internal.bind.v2.runtime.property.Messages", //
				"com.sun.xml.internal.bind.v2.runtime.reflect.Messages", //
				"com.sun.xml.internal.bind.v2.runtime.unmarshaller.Messages", //
				"com.sun.xml.internal.bind.v2.schemagen.Form$1", //
				"com.sun.xml.internal.bind.v2.schemagen.Form$2", //
				"com.sun.xml.internal.bind.v2.schemagen.Form$3", //
				"com.sun.xml.internal.bind.v2.schemagen.Form", //
				"com.sun.xml.internal.bind.v2.schemagen.GroupKind", //
				"com.sun.xml.internal.bind.v2.schemagen.Messages", //
				"com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent$EVENT_TYPE", //
				"com.sun.xml.internal.org.jvnet.mimepull.MIMEParser$STATE", //
				"com.sun.xml.internal.ws.api.ComponentFeature$Target", //
				"com.sun.xml.internal.ws.api.SOAPVersion", //
				"com.sun.xml.internal.ws.api.addressing.AddressingVersion$1", //
				"com.sun.xml.internal.ws.api.addressing.AddressingVersion$2", //
				"com.sun.xml.internal.ws.api.addressing.AddressingVersion", //
				"com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion$Setting", //
				"com.sun.xml.internal.ws.api.databinding.SoapBodyStyle", //
				"com.sun.xml.internal.ws.api.message.Packet$State", //
				"com.sun.xml.internal.ws.api.message.Packet$Status", //
				"com.sun.xml.internal.ws.api.model.ExceptionType", //
				"com.sun.xml.internal.ws.api.model.MEP", //
				"com.sun.xml.internal.ws.api.model.ParameterBinding$Kind", //
				"com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation$ANONYMOUS", //
				"com.sun.xml.internal.ws.api.model.wsdl.WSDLDescriptorKind", //
				"com.sun.xml.internal.ws.api.policy.subject.BindingSubject$WsdlMessageType", //
				"com.sun.xml.internal.ws.api.policy.subject.BindingSubject$WsdlNameScope", //
				"com.sun.xml.internal.ws.api.server.LazyMOMProvider$Scope", //
				"com.sun.xml.internal.ws.api.server.LazyMOMProvider", //
				"com.sun.xml.internal.ws.assembler.MetroTubelineAssembler$Side", //
				"com.sun.xml.internal.ws.client.ContentNegotiation", //
				"com.sun.xml.internal.ws.client.sei.ValueGetter$1", //
				"com.sun.xml.internal.ws.client.sei.ValueGetter$2", //
				"com.sun.xml.internal.ws.client.sei.ValueGetter", //
				"com.sun.xml.internal.ws.developer.MemberSubmissionAddressing$Validation", //
				"com.sun.xml.internal.ws.dump.LoggingDumpTube$Position", //
				"com.sun.xml.internal.ws.dump.MessageDumper$MessageType", //
				"com.sun.xml.internal.ws.dump.MessageDumper$ProcessingState", //
				"com.sun.xml.internal.ws.handler.HandlerProcessor$Direction", //
				"com.sun.xml.internal.ws.handler.HandlerProcessor$RequestOrResponse", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$1", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$2", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$3", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$4", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$5", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$6", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness$7", //
				"com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector$AlternativeFitness", //
				"com.sun.xml.internal.ws.policy.PolicyIntersector$CompatibilityMode", //
				"com.sun.xml.internal.ws.policy.PolicyMap$ScopeType", //
				"com.sun.xml.internal.ws.policy.jaxws.BuilderHandlerMessageScope$Scope", //
				"com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLGeneratorExtension$ScopeType", //
				"com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLParserExtension$HandlerType", //
				"com.sun.xml.internal.ws.policy.sourcemodel.ModelNode$Type", //
				"com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion", //
				"com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken", //
				"com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator$Fitness", //
				"com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject$WsdlMessageType", //
				"com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject$WsdlNameScope", //
				"com.sun.xml.internal.ws.server.sei.ValueGetter$1", //
				"com.sun.xml.internal.ws.server.sei.ValueGetter$2", //
				"com.sun.xml.internal.ws.server.sei.ValueGetter", //
				"com.sun.xml.internal.ws.util.xml.XMLReaderComposite$State", //
				"com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser$BindingMode", //
				"java.awt.Component$BaselineResizeBehavior", //
				"java.awt.Desktop$Action", //
				"java.awt.GraphicsDevice$WindowTranslucency", //
				"java.awt.MultipleGradientPaint$ColorSpaceType", //
				"java.awt.MultipleGradientPaint$CycleMethod", //
				"java.awt.TrayIcon$MessageType", //
				"java.awt.font.NumericShaper$Range$1", //
				"java.awt.font.NumericShaper$Range", //
				"java.lang.ProcessBuilder$Redirect$Type", //
				"java.lang.Thread$State", //
				"java.lang.UNIXProcess$Platform", //
				"java.lang.annotation.ElementType", //
				"java.lang.annotation.RetentionPolicy", //
				"java.lang.invoke.LambdaForm$BasicType", //
				"java.lang.invoke.LambdaFormEditor$Transform$Kind", //
				"java.lang.invoke.MethodHandleImpl$Intrinsic", //
				"java.lang.management.MemoryType", //
				"java.lang.management.PlatformComponent", //
				"java.math.RoundingMode", //
				"java.net.Authenticator$RequestorType", //
				"java.net.StandardProtocolFamily", //
				"java.nio.file.AccessMode", //
				"java.nio.file.FileTreeWalker$EventType", //
				"java.nio.file.FileVisitOption", //
				"java.nio.file.FileVisitResult", //
				"java.nio.file.LinkOption", //
				"java.nio.file.StandardCopyOption", //
				"java.nio.file.StandardOpenOption", //
				"java.nio.file.attribute.AclEntryFlag", //
				"java.nio.file.attribute.AclEntryPermission", //
				"java.nio.file.attribute.AclEntryType", //
				"java.nio.file.attribute.PosixFilePermission", //
				"java.security.CryptoPrimitive", //
				"java.security.KeyRep$Type", //
				"java.security.cert.CRLReason", //
				"java.security.cert.CertPathValidatorException$BasicReason", //
				"java.security.cert.PKIXReason", //
				"java.security.cert.PKIXRevocationChecker$Option", //
				"java.sql.ClientInfoStatus", //
				"java.sql.JDBCType", //
				"java.sql.PseudoColumnUsage", //
				"java.sql.RowIdLifetime", //
				"java.text.Normalizer$Form", //
				"java.time.DayOfWeek", //
				"java.time.Month", //
				"java.time.chrono.HijrahEra", //
				"java.time.chrono.IsoEra", //
				"java.time.chrono.MinguoEra", //
				"java.time.chrono.ThaiBuddhistEra", //
				"java.time.format.DateTimeFormatterBuilder$SettingsParser", //
				"java.time.format.FormatStyle", //
				"java.time.format.ResolverStyle", //
				"java.time.format.SignStyle", //
				"java.time.format.TextStyle", //
				"java.time.temporal.ChronoField", //
				"java.time.temporal.ChronoUnit", //
				"java.time.temporal.IsoFields$Field$1", //
				"java.time.temporal.IsoFields$Field$2", //
				"java.time.temporal.IsoFields$Field$3", //
				"java.time.temporal.IsoFields$Field$4", //
				"java.time.temporal.IsoFields$Field", //
				"java.time.temporal.IsoFields$Unit", //
				"java.time.temporal.JulianFields$Field", //
				"java.time.zone.ZoneOffsetTransitionRule$TimeDefinition", //
				"java.util.Comparators$NaturalOrderComparator", //
				"java.util.Formatter$BigDecimalLayoutForm", //
				"java.util.Locale$FilteringMode", //
				"java.util.regex.UnicodeProp$1", //
				"java.util.regex.UnicodeProp$10", //
				"java.util.regex.UnicodeProp$11", //
				"java.util.regex.UnicodeProp$12", //
				"java.util.regex.UnicodeProp$13", //
				"java.util.regex.UnicodeProp$14", //
				"java.util.regex.UnicodeProp$15", //
				"java.util.regex.UnicodeProp$16", //
				"java.util.regex.UnicodeProp$17", //
				"java.util.regex.UnicodeProp$18", //
				"java.util.regex.UnicodeProp$19", //
				"java.util.regex.UnicodeProp$2", //
				"java.util.regex.UnicodeProp$3", //
				"java.util.regex.UnicodeProp$4", //
				"java.util.regex.UnicodeProp$5", //
				"java.util.regex.UnicodeProp$6", //
				"java.util.regex.UnicodeProp$7", //
				"java.util.regex.UnicodeProp$8", //
				"java.util.regex.UnicodeProp$9", //
				"java.util.regex.UnicodeProp", //
				"java.util.stream.Collector$Characteristics", //
				"java.util.stream.MatchOps$MatchKind", //
				"java.util.stream.StreamOpFlag$Type", //
				"java.util.stream.StreamOpFlag", //
				"java.util.stream.StreamShape", //
				"java.util.stream.StreamSpliterators$UnorderedSliceSpliterator$PermitStatus", //
				"javax.annotation.Resource$AuthenticationType", //
				"javax.imageio.ImageIO$SpiInfo$1", //
				"javax.imageio.ImageIO$SpiInfo$2", //
				"javax.imageio.ImageIO$SpiInfo$3", //
				"javax.imageio.ImageIO$SpiInfo", //
				"javax.jws.WebParam$Mode", //
				"javax.jws.soap.SOAPBinding$ParameterStyle", //
				"javax.jws.soap.SOAPBinding$Style", //
				"javax.jws.soap.SOAPBinding$Use", //
				"javax.lang.model.SourceVersion", //
				"javax.lang.model.element.ElementKind", //
				"javax.lang.model.element.Modifier", //
				"javax.lang.model.element.NestingKind", //
				"javax.lang.model.type.TypeKind", //
				"javax.management.monitor.Monitor$NumericalType", //
				"javax.net.ssl.SSLEngineResult$HandshakeStatus", //
				"javax.net.ssl.SSLEngineResult$Status", //
				"javax.smartcardio.CardTerminals$State", //
				"javax.swing.GroupLayout$Alignment", //
				"javax.swing.JTable$PrintMode", //
				"javax.swing.LayoutStyle$ComponentPlacement", //
				"javax.swing.MultiUIDefaults$MultiUIDefaultsEnumerator$Type", //
				"javax.swing.RowFilter$ComparisonType", //
				"javax.swing.SortOrder", //
				"javax.swing.SwingWorker$StateValue", //
				"javax.swing.event.RowSorterEvent$Type", //
				"javax.swing.plaf.nimbus.AbstractRegionPainter$PaintContext$CacheMode", //
				"javax.swing.plaf.nimbus.Effect$EffectType", //
				"javax.swing.plaf.nimbus.ImageScalingHelper$PaintType", //
				"javax.swing.text.html.FormSubmitEvent$MethodType", //
				"javax.swing.text.html.StyleSheet$BoxPainter$HorizontalMargin", //
				"javax.tools.Diagnostic$Kind", //
				"javax.tools.DocumentationTool$Location", //
				"javax.tools.JavaFileObject$Kind", //
				"javax.tools.StandardLocation", //
				"javax.xml.bind.annotation.XmlAccessOrder", //
				"javax.xml.bind.annotation.XmlAccessType", //
				"javax.xml.bind.annotation.XmlNsForm", //
				"javax.xml.ws.Service$Mode", //
				"javax.xml.ws.handler.MessageContext$Scope", //
				"javax.xml.ws.soap.AddressingFeature$Responses", //
				"jdk.management.resource.ResourceAccuracy", //
				"jdk.net.SocketFlow$Status", //
				"org.jcp.xml.dsig.internal.dom.AbstractDOMSignatureMethod$Type", //
				"sun.font.LayoutPathImpl$EndType", //
				"sun.invoke.util.Wrapper", //
				"sun.java2d.cmm.lcms.LCMSImageLayout$BandOrder", //
				"sun.java2d.pipe.hw.ExtendedBufferCapabilities$VSyncType", //
				"sun.java2d.pisces.Dasher$LengthIterator$Side", //
				"sun.java2d.pisces.PiscesRenderingEngine$NormMode", //
				"sun.launcher.LauncherHelper$SizePrefix", //
				"sun.misc.FormattedFloatingDecimal$Form", //
				"sun.misc.ObjectInputFilter$Status", //
				"sun.net.ProgressSource$State", //
				"sun.net.ftp.FtpClient$TransferType", //
				"sun.net.ftp.FtpDirEntry$Permission", //
				"sun.net.ftp.FtpDirEntry$Type", //
				"sun.net.ftp.FtpReplyCode", //
				"sun.net.httpserver.HttpConnection$State", //
				"sun.net.httpserver.SSLStreams$BufType", //
				"sun.net.sdp.SdpProvider$Action", //
				"sun.net.www.protocol.http.AuthCacheValue$Type", //
				"sun.net.www.protocol.http.AuthScheme", //
				"sun.net.www.protocol.http.HttpURLConnection$TunnelState", //
				"sun.nio.ch.UnixAsynchronousSocketChannelImpl$OpType", //
				"sun.nio.fs.AbstractPoller$RequestType", //
				"sun.nio.fs.AbstractWatchKey$State", //
				"sun.nio.fs.UnixFileStore$FeatureStatus", //
				"sun.reflect.annotation.TypeAnnotation$TypeAnnotationTarget", //
				"sun.security.jgss.spnego.SpNegoToken$NegoResult", //
				"sun.security.krb5.KdcComm$BpType", //
				"sun.security.krb5.KrbAsReqBuilder$State", //
				"sun.security.provider.NativePRNG$Variant", //
				"sun.security.provider.certpath.OCSP$RevocationStatus$CertStatus", //
				"sun.security.provider.certpath.OCSPResponse$ResponseStatus", //
				"sun.security.provider.certpath.RevocationChecker$Mode", //
				"sun.security.smartcardio.CardImpl$State", //
				"sun.security.tools.keytool.Main$Command", //
				"sun.security.tools.keytool.Main$Option", //
				"sun.security.util.DisabledAlgorithmConstraints$Constraint$Operator", //
				"sun.swing.SwingUtilities2$Section", //
				"sun.swing.plaf.synth.Paint9Painter$PaintType", //
				"sun.util.logging.PlatformLogger$Level", //
				"sun.util.locale.provider.LocaleProviderAdapter$Type", //
				"sun.lwawt.LWWindowPeer$PeerType", //
				"sun.launcher.LauncherHelper", //
				"sun.java2d.StateTrackable$State", //
				"sun.font.EAttribute", //
				"sun.awt.OSInfo$OSType", //
				"sun.awt.CausedFocusEvent$Cause", //
				"sun.awt.AppContext$State", //
				"javax.swing.DropMode", //
				"javax.swing.ClientPropertyKey", //
				"java.util.concurrent.TimeUnit$7", //
				"java.util.concurrent.TimeUnit$6", //
				"java.util.concurrent.TimeUnit$5", //
				"java.util.concurrent.TimeUnit$4", //
				"java.util.concurrent.TimeUnit$3", //
				"java.util.concurrent.TimeUnit$2", //
				"java.util.concurrent.TimeUnit$1", //
				"java.util.concurrent.TimeUnit", //
				"java.util.Locale$Category", //
				"java.net.Proxy$Type", //
				"java.net.InetAddress$Cache$Type", //
				"java.lang.UNIXProcess$LaunchMechanism", //
				"java.io.File$PathStatus", //
				"java.awt.Window$Type", //
				"java.awt.EventFilter$FilterAction", //
				"java.awt.Dialog$ModalityType", //
				"java.awt.Dialog$ModalExclusionType", //
				"com.apple.eawt.QuitStrategy", //
				"apple.laf.JRSUIControl$BufferState", //
				"com.oracle.tools.packager.JreUtils$Rule$Type", //
				"com.oracle.tools.packager.RelativeFileSet$Type", //
				"com.oracle.tools.packager.jnlp.JNLPBundler$Mode", //
				"com.sun.javafx.tools.packager.DeployParams$RunMode", //
				"com.sun.javafx.tools.packager.PackagerLib$Filter", //
				"com.sun.javafx.tools.packager.TemplatePlaceholders", //
				"com.sun.javafx.tools.packager.bundlers.Bundler$BundleType", //
				"com.sun.javafx.tools.resource.DeployResource$Type", //
				"com.oracle.javafx.jmx.json.JSONDocument$Type", //
				"com.oracle.javafx.jmx.json.JSONReader$EventType", //
				"com.oracle.javafx.jmx.json.JSONWriter$ContainerType", //
				"com.oracle.javafx.jmx.json.impl.JSONSymbol", //
				"com.sun.tools.jconsole.JConsoleContext$ConnectionState", //
				"sun.tools.jconsole.inspector.XNodeInfo$Type", //
				"sun.tools.jconsole.Plotter$Unit", //
				"com.ibm.icu.impl.LocaleDisplayNamesImpl$CapitalizationContextUsage", //
				"com.ibm.icu.impl.LocaleDisplayNamesImpl$DataTableType", //
				"com.ibm.icu.impl.TimeZoneGenericNames$GenericNameType", //
				"com.ibm.icu.impl.TimeZoneGenericNames$Pattern", //
				"com.ibm.icu.impl.Trie2$ValueWidth", //
				"com.ibm.icu.lang.UScript$ScriptUsage", //
				"com.ibm.icu.text.AlphabeticIndex$Bucket$LabelType", //
				"com.ibm.icu.text.CompactDecimalDataCache$QuoteState", //
				"com.ibm.icu.text.CompactDecimalDataCache$UResFlags", //
				"com.ibm.icu.text.CompactDecimalFormat$CompactStyle", //
				"com.ibm.icu.text.DateFormat$BooleanAttribute", //
				"com.ibm.icu.text.DateFormatSymbols$CapitalizationContextUsage", //
				"com.ibm.icu.text.DateTimePatternGenerator$DTPGflags", //
				"com.ibm.icu.text.DisplayContext$Type", //
				"com.ibm.icu.text.DisplayContext", //
				"com.ibm.icu.text.IDNA$Error", //
				"com.ibm.icu.text.ListFormatter$Style", //
				"com.ibm.icu.text.LocaleDisplayNames$DialectHandling", //
				"com.ibm.icu.text.MessagePattern$ApostropheMode", //
				"com.ibm.icu.text.MessagePattern$ArgType", //
				"com.ibm.icu.text.MessagePattern$Part$Type", //
				"com.ibm.icu.text.MessagePatternUtil$MessageContentsNode$Type", //
				"com.ibm.icu.text.Normalizer2$Mode", //
				"com.ibm.icu.text.PluralRules$KeywordStatus", //
				"com.ibm.icu.text.PluralRules$Operand", //
				"com.ibm.icu.text.PluralRules$PluralType", //
				"com.ibm.icu.text.PluralRules$SampleType", //
				"com.ibm.icu.text.PluralRules$StandardPluralCategories", //
				"com.ibm.icu.text.SimpleDateFormat$ContextValue", //
				"com.ibm.icu.text.SpoofChecker$RestrictionLevel", //
				"com.ibm.icu.text.TimeZoneFormat$GMTOffsetPatternType", //
				"com.ibm.icu.text.TimeZoneFormat$OffsetFields", //
				"com.ibm.icu.text.TimeZoneFormat$ParseOption", //
				"com.ibm.icu.text.TimeZoneFormat$Style", //
				"com.ibm.icu.text.TimeZoneFormat$TimeType", //
				"com.ibm.icu.text.TimeZoneNames$NameType", //
				"com.ibm.icu.text.UnicodeSet$ComparisonStyle", //
				"com.ibm.icu.text.UnicodeSet$SpanCondition", //
				"com.ibm.icu.util.BytesTrie$Result", //
				"com.ibm.icu.util.FormatWidth", //
				"com.ibm.icu.util.GenderInfo$Gender", //
				"com.ibm.icu.util.GenderInfo$ListGenderStyle", //
				"com.ibm.icu.util.IslamicCalendar$CalculationType", //
				"com.ibm.icu.util.LocaleMatcher$Level", //
				"com.ibm.icu.util.Region$RegionType", //
				"com.ibm.icu.util.StringTrieBuilder$Option", //
				"com.ibm.icu.util.StringTrieBuilder$State", //
				"com.ibm.icu.util.TimeZone$SystemTimeZoneType", //
				"com.ibm.icu.util.ULocale$Category", //
				"com.jrockit.mc.common.IMCAggregatedFrame$AggregationType", //
				"com.jrockit.mc.common.IMCFrame$Type", //
				"com.jrockit.mc.common.IMCStackTrace$TruncationState", //
				"com.jrockit.mc.common.environment.OS$Type", //
				"com.jrockit.mc.common.jvm.Connectable", //
				"com.jrockit.mc.common.jvm.JVMArch", //
				"com.jrockit.mc.common.jvm.JVMType", //
				"com.jrockit.mc.common.persistence.LocalizedString$PlacementIdentifier", //
				"com.jrockit.mc.common.unit.BinaryPrefix", //
				"com.jrockit.mc.common.unit.DecimalPrefix", //
				"com.jrockit.mc.common.unit.QuantityConversionException$Problem", //
				"com.jrockit.mc.common.util.SizeToolkit$Format", //
				"com.jrockit.mc.common.util.TimeRangeToolkit$Format", //
				"com.jrockit.mc.components.ui.contributions.ActivationPolicy", //
				"com.jrockit.mc.components.ui.design.LayoutItemType", //
				"com.jrockit.mc.components.ui.design.SupportedControl", //
				"com.jrockit.mc.components.ui.design.actions.InsertAction$POSITION", //
				"com.jrockit.mc.components.ui.settings.IconEditor$Tool", //
				"com.jrockit.mc.components.ui.settings.Orientation", //
				"com.jrockit.mc.flightrecorder.controlpanel.ui.configuration.model.xml.XMLNodeType", //
				"com.jrockit.mc.flightrecorder.controlpanel.ui.recordingconfiguration.PathElement$PathElementKind", //
				"com.jrockit.mc.flightrecorder.ui.common.OperativeSetFilterDescription", //
				"com.jrockit.mc.flightrecorder.ui.components.aggregators.AggregatorType", //
				"com.jrockit.mc.flightrecorder.ui.components.chart.model.XAxisDataSource", //
				"com.jrockit.mc.flightrecorder.ui.components.graph.TooltipVerbosity", //
				"com.jrockit.mc.flightrecorder.ui.components.graph.TransitionLookup$Transition", //
				"com.jrockit.mc.flightrecorder.ui.components.graph.renderer.coloring.EventColorType", //
				"com.jrockit.mc.flightrecorder.ui.components.inputs.Role", //
				"com.jrockit.mc.flightrecorder.ui.components.range.NanoTimeFormatter$Precision", //
				"com.jrockit.mc.flightrecorder.RecordingPrinter$TimeFormat", //
				"com.jrockit.mc.flightrecorder.RecordingPrinter$Verbosity", //
				"com.jrockit.mc.flightrecorder.internal.parser.model.DataType", //
				"com.jrockit.mc.flightrecorder.internal.parser.model.Transition", //
				"com.jrockit.mc.flightrecorder.spi.EventOrder", //
				"com.jrockit.mc.flightrecorder.spi.Expansion", //
				"com.jrockit.mc.flightrecorder.spi.FieldType", //
				"com.jrockit.mc.greychart.ui.model.Style", //
				"com.jrockit.mc.greychart.ui.model.TickDensityName", //
				"se.hirt.greychart.ChartChangeEvent$ChangeType", //
				"se.hirt.greychart.TickDensity", //
				"se.hirt.greychart.YAxis$Position", //
				"se.hirt.greychart.data.RenderingMode", //
				"se.hirt.greychart.impl.TimestampFormatter$Precision", //
				"com.jrockit.mc.jdp.client.DiscoveryEvent$Kind", //
				"com.jrockit.mc.rjmx.ui.internal.ChartModel$AxisRange", //
				"com.jrockit.mc.rjmx.IServerHandle$State", //
				"com.jrockit.mc.rjmx.services.IOperation$OperationImpact", //
				"com.jrockit.mc.rjmx.services.flr.IRecordingDescriptor$RecordingState", //
				"com.jrockit.mc.rjmx.subscription.MRI$Type", //
				"com.jrockit.mc.rjmx.subscription.internal.IMRISubscriptionDebugInformation$SubscriptionState", //
				"com.jrockit.mc.rjmx.subscription.storage.internal.AttributeStorageEventType", //
				"com.jrockit.mc.ui.fields.FilterMatcher$Where", //
				"com.jrockit.mc.ui.misc.ProgressCircle$Direction", //
				"com.jrockit.mc.ui.model.fields.ByteUnit", //
				"com.jrockit.mc.ui.model.fields.TimespanParser$TimeFactor", //
				"javax.annotation.Resource$AuthenticationType", //
				"javax.servlet.DispatcherType", //
				"javax.servlet.SessionTrackingMode", //
				"javax.servlet.annotation.ServletSecurity$EmptyRoleSemantic", //
				"javax.servlet.annotation.ServletSecurity$TransportGuarantee", //
				"org.apache.commons.codec.language.bm.NameType", //
				"org.apache.commons.codec.language.bm.RuleType", //
				"org.apache.felix.gogo.runtime.Tokenizer$Type", //
				"org.apache.http.auth.AuthProtocolState", //
				"org.apache.http.auth.ChallengeState", //
				"org.apache.http.client.cache.CacheResponseStatus", //
				"org.apache.http.conn.routing.RouteInfo$LayerType", //
				"org.apache.http.conn.routing.RouteInfo$TunnelType", //
				"org.apache.http.entity.mime.HttpMultipartMode", //
				"org.apache.http.impl.auth.GGSSchemeBase$State", //
				"org.apache.http.impl.auth.NTLMScheme$State", //
				"org.apache.http.impl.client.cache.RequestProtocolError", //
				"org.apache.jasper.compiler.TagFileProcessor$TagFileDirectiveVisitor$Name", //
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter$Side$1", //
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter$Side$2", //
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter$Side", //
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenizer$Side$1", //
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenizer$Side$2", //
				"org.apache.lucene.analysis.ngram.EdgeNGramTokenizer$Side", //
				"org.apache.lucene.document.DateTools$Resolution", //
				"org.apache.lucene.document.Field$Index$1", //
				"org.apache.lucene.document.Field$Index$2", //
				"org.apache.lucene.document.Field$Index$3", //
				"org.apache.lucene.document.Field$Index$4", //
				"org.apache.lucene.document.Field$Index$5", //
				"org.apache.lucene.document.Field$Index", //
				"org.apache.lucene.document.Field$Store$1", //
				"org.apache.lucene.document.Field$Store$2", //
				"org.apache.lucene.document.Field$Store", //
				"org.apache.lucene.document.Field$TermVector$1", //
				"org.apache.lucene.document.Field$TermVector$2", //
				"org.apache.lucene.document.Field$TermVector$3", //
				"org.apache.lucene.document.Field$TermVector$4", //
				"org.apache.lucene.document.Field$TermVector$5", //
				"org.apache.lucene.document.Field$TermVector", //
				"org.apache.lucene.document.FieldSelectorResult", //
				"org.apache.lucene.document.NumericField$DataType", //
				"org.apache.lucene.index.FieldInfo$IndexOptions", //
				"org.apache.lucene.index.IndexReader$FieldOption", //
				"org.apache.lucene.index.IndexWriterConfig$OpenMode", //
				"org.apache.lucene.queryParser.QueryParser$Operator", //
				"org.apache.lucene.search.BooleanClause$Occur$1", //
				"org.apache.lucene.search.BooleanClause$Occur$2", //
				"org.apache.lucene.search.BooleanClause$Occur$3", //
				"org.apache.lucene.search.BooleanClause$Occur", //
				"org.apache.lucene.search.CachingWrapperFilter$DeletesMode", //
				"org.apache.lucene.search.spans.SpanPositionCheckQuery$AcceptStatus", //
				"org.apache.lucene.util.Version", //
				"org.apache.lucene.util.fst.FST$INPUT_TYPE", //
				"org.eclipse.e4.core.internal.contexts.IEclipseContextDebugger$EventType", //
				"org.eclipse.e4.core.services.nls.Message$ReferenceType", //
				"org.eclipse.e4.ui.model.application.ui.menu.ItemType", //
				"org.eclipse.e4.ui.model.application.ui.SideValue", //
				"org.eclipse.e4.ui.model.internal.Position", //
				"org.eclipse.e4.ui.workbench.modeling.EPartService$PartState", //
				"org.eclipse.e4.ui.workbench.modeling.ISaveHandler$Save", //
				"org.eclipse.emf.ecore.change.ChangeKind", //
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark$1", //
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark$2", //
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark$3", //
				"org.eclipse.emf.ecore.resource.ContentHandler$ByteOrderMark", //
				"org.eclipse.emf.ecore.resource.ContentHandler$Validity", //
				"org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl$BinaryIO$FeatureKind", //
				"org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl$BinaryIO$Version", //
				"org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl$EObjectOutputStream$Check", //
				"org.eclipse.emf.ecore.xml.namespace.SpaceType", //
				"org.eclipse.equinox.console.common.KEYS", //
				"org.eclipse.equinox.internal.p2.publisher.eclipse.ProductContentType", //
				"org.eclipse.jetty.http.HttpStatus$Code", //
				"org.eclipse.jetty.io.Buffers$Type", //
				"org.eclipse.jetty.security.UserDataConstraint", //
				"org.eclipse.jetty.servlet.Holder$Source", //
				"org.eclipse.jetty.util.Scanner$Notification", //
				"org.osgi.service.component.annotations.ConfigurationPolicy", //
				"org.osgi.service.component.annotations.ReferenceCardinality", //
				"org.osgi.service.component.annotations.ReferencePolicyOption", //
				"org.osgi.service.component.annotations.ReferencePolicy", //
				"org.eclipse.osgi.report.resolution.ResolutionReport$Entry$Type", //
				"org.eclipse.osgi.container.Module$StopOptions", //
				"org.eclipse.osgi.container.Module$Settings", //
				"org.eclipse.osgi.container.Module$State", //
				"org.eclipse.osgi.container.ModuleContainerAdaptor$ModuleEvent", //
				"org.eclipse.osgi.container.Module$StartOptions", //
				"org.eclipse.osgi.container.ModuleDatabase$Sort", //
				"org.eclipse.osgi.container.ModuleContainerAdaptor$ContainerEvent", //
				"org.sat4j.minisat.core.LearnedConstraintsEvaluationType", //
				"org.sat4j.minisat.core.SimplificationType", //
				"org.sat4j.tools.encoding.EncodingStrategy", //
				"oracle.jvm.hotspot.jfr.ThreadStates$States", //
				"sun.jvm.hotspot.debugger.arm.ARMThreadContext$Arch", //
				"sun.jvm.hotspot.gc_interface.G1YCType", //
				"sun.jvm.hotspot.gc_interface.GCCause", //
				"sun.jvm.hotspot.gc_interface.GCName", //
				"sun.jvm.hotspot.gc_interface.GCWhen", //
				"sun.jvm.hotspot.gc_interface.ReferenceType", //
				"sun.jvm.hotspot.memory.Universe$NARROW_OOP_MODE", //
				"sun.jvm.hotspot.opto.CompilerPhaseType", //
				"sun.jvm.hotspot.runtime.Flags", //
				"sun.jvm.hotspot.runtime.VMOps", //
				"com.sun.codemodel.internal.JFormatter$Mode", //
				"com.sun.javadoc.LanguageVersion", //
				"com.sun.source.doctree.AttributeTree$ValueKind", //
				"com.sun.source.doctree.DocTree$Kind", //
				"com.sun.source.tree.LambdaExpressionTree$BodyKind", //
				"com.sun.source.tree.MemberReferenceTree$ReferenceMode", //
				"com.sun.source.tree.Tree$Kind", //
				"com.sun.source.util.TaskEvent$Kind", //
				"com.sun.tools.classfile.AccessFlags$Kind", //
				"com.sun.tools.classfile.ConstantPool$RefKind", //
				"com.sun.tools.classfile.Instruction$Kind", //
				"com.sun.tools.classfile.Instruction$TypeKind", //
				"com.sun.tools.classfile.Opcode$Set", //
				"com.sun.tools.classfile.Opcode", //
				"com.sun.tools.classfile.Type$WildcardType$Kind", //
				"com.sun.tools.classfile.TypeAnnotation$Position$TypePathEntryKind", //
				"com.sun.tools.classfile.TypeAnnotation$TargetType", //
				"com.sun.tools.doclets.formats.html.LinkInfoImpl$Kind", //
				"com.sun.tools.doclets.formats.html.markup.HtmlAttr", //
				"com.sun.tools.doclets.formats.html.markup.HtmlStyle", //
				"com.sun.tools.doclets.formats.html.markup.HtmlTag$BlockType", //
				"com.sun.tools.doclets.formats.html.markup.HtmlTag$EndTag", //
				"com.sun.tools.doclets.formats.html.markup.HtmlTag", //
				"com.sun.tools.doclets.formats.html.markup.RawHtml$State", //
				"com.sun.tools.doclets.formats.html.SectionName", //
				"com.sun.tools.doclets.internal.toolkit.util.MethodTypes", //
				"com.sun.tools.doclint.Checker$Flag", //
				"com.sun.tools.doclint.Entity", //
				"com.sun.tools.doclint.Env$AccessKind", //
				"com.sun.tools.doclint.HtmlTag$1", //
				"com.sun.tools.doclint.HtmlTag$10", //
				"com.sun.tools.doclint.HtmlTag$2", //
				"com.sun.tools.doclint.HtmlTag$3", //
				"com.sun.tools.doclint.HtmlTag$4", //
				"com.sun.tools.doclint.HtmlTag$5", //
				"com.sun.tools.doclint.HtmlTag$6", //
				"com.sun.tools.doclint.HtmlTag$7", //
				"com.sun.tools.doclint.HtmlTag$8", //
				"com.sun.tools.doclint.HtmlTag$9", //
				"com.sun.tools.doclint.HtmlTag$Attr", //
				"com.sun.tools.doclint.HtmlTag$AttrKind", //
				"com.sun.tools.doclint.HtmlTag$BlockType", //
				"com.sun.tools.doclint.HtmlTag$EndKind", //
				"com.sun.tools.doclint.HtmlTag$Flag", //
				"com.sun.tools.doclint.HtmlTag", //
				"com.sun.tools.doclint.Messages$Group", //
				"com.sun.tools.internal.jxc.ap.Const", //
				"com.sun.tools.internal.jxc.ap.Messages", //
				"com.sun.tools.internal.jxc.Messages", //
				"com.sun.tools.internal.ws.processor.generator.GeneratorConstants", //
				"com.sun.tools.internal.ws.processor.modeler.annotation.WebServiceConstants", //
				"com.sun.tools.internal.ws.processor.modeler.ModelerConstants", //
				"com.sun.tools.internal.ws.processor.modeler.wsdl.WSDLModeler$StyleAndUse", //
				"com.sun.tools.internal.ws.wscompile.Options$Target", //
				"com.sun.tools.internal.xjc.addon.episode.PluginImpl$OutlineAdaptor$OutlineType", //
				"com.sun.tools.internal.xjc.api.SpecVersion", //
				"com.sun.tools.internal.xjc.api.util.Messages", //
				"com.sun.tools.internal.xjc.Driver$Mode", //
				"com.sun.tools.internal.xjc.generator.bean.field.Messages", //
				"com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy$1", //
				"com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy$2", //
				"com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy", //
				"com.sun.tools.internal.xjc.generator.bean.Messages", //
				"com.sun.tools.internal.xjc.Language", //
				"com.sun.tools.internal.xjc.model.CElementPropertyInfo$CollectionMode", //
				"com.sun.tools.internal.xjc.outline.Aspect", //
				"com.sun.tools.internal.xjc.reader.dtd.ModelGroup$Kind", //
				"com.sun.tools.internal.xjc.reader.Messages", //
				"com.sun.tools.internal.xjc.reader.RawTypeSet$Mode", //
				"com.sun.tools.internal.xjc.reader.relaxng.BindStyle", //
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIGlobalBinding$UnderscoreBinding", //
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.EnumMemberMode", //
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.LocalScoping", //
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.Messages", //
				"com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.OptionalPropertyMode", //
				"com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeBindingMode", //
				"com.sun.tools.internal.xjc.reader.xmlschema.ct.Messages", //
				"com.sun.tools.javac.api.DiagnosticFormatter$Configuration$DiagnosticPart", //
				"com.sun.tools.javac.api.DiagnosticFormatter$Configuration$MultilineLimit", //
				"com.sun.tools.javac.api.DiagnosticFormatter$PositionKind", //
				"com.sun.tools.javac.code.Attribute$RetentionPolicy", //
				"com.sun.tools.javac.code.BoundKind", //
				"com.sun.tools.javac.code.Flags$Flag", //
				"com.sun.tools.javac.code.Kinds$KindName", //
				"com.sun.tools.javac.code.Lint$LintCategory", //
				"com.sun.tools.javac.code.Source", //
				"com.sun.tools.javac.code.TargetType", //
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound$1", //
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound$2", //
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound$3", //
				"com.sun.tools.javac.code.Type$UndetVar$InferenceBound", //
				"com.sun.tools.javac.code.TypeAnnotationPosition$TypePathEntryKind", //
				"com.sun.tools.javac.code.TypeAnnotations$AnnotationType", //
				"com.sun.tools.javac.code.TypeTag", //
				"com.sun.tools.javac.comp.CompileStates$CompileState", //
				"com.sun.tools.javac.comp.DeferredAttr$ArgumentExpressionKind", //
				"com.sun.tools.javac.comp.DeferredAttr$AttrMode", //
				"com.sun.tools.javac.comp.Flow$BaseAnalyzer$JumpKind$1", //
				"com.sun.tools.javac.comp.Flow$BaseAnalyzer$JumpKind$2", //
				"com.sun.tools.javac.comp.Flow$BaseAnalyzer$JumpKind", //
				"com.sun.tools.javac.comp.Flow$FlowKind", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$1", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$2", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$3", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$4", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$5", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind$6", //
				"com.sun.tools.javac.comp.Infer$BoundErrorKind", //
				"com.sun.tools.javac.comp.Infer$DependencyKind", //
				"com.sun.tools.javac.comp.Infer$GraphInferenceSteps", //
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$1", //
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$2", //
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$3", //
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$4", //
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind$5", //
				"com.sun.tools.javac.comp.Infer$IncorporationBinaryOpKind", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$1", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$10", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$11", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$2", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$3", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$4", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$5", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$6", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$7", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$8", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep$9", //
				"com.sun.tools.javac.comp.Infer$IncorporationStep", //
				"com.sun.tools.javac.comp.Infer$InferenceStep$1", //
				"com.sun.tools.javac.comp.Infer$InferenceStep$2", //
				"com.sun.tools.javac.comp.Infer$InferenceStep$3", //
				"com.sun.tools.javac.comp.Infer$InferenceStep$4", //
				"com.sun.tools.javac.comp.Infer$InferenceStep$5", //
				"com.sun.tools.javac.comp.Infer$InferenceStep$6", //
				"com.sun.tools.javac.comp.Infer$InferenceStep", //
				"com.sun.tools.javac.comp.Infer$LegacyInferenceSteps", //
				"com.sun.tools.javac.comp.LambdaToMethod$LambdaSymbolKind", //
				"com.sun.tools.javac.comp.Resolve$InterfaceLookupPhase$1", //
				"com.sun.tools.javac.comp.Resolve$InterfaceLookupPhase$2", //
				"com.sun.tools.javac.comp.Resolve$InterfaceLookupPhase", //
				"com.sun.tools.javac.comp.Resolve$MethodCheckDiag", //
				"com.sun.tools.javac.comp.Resolve$MethodResolutionPhase$1", //
				"com.sun.tools.javac.comp.Resolve$MethodResolutionPhase", //
				"com.sun.tools.javac.comp.Resolve$SearchResultKind", //
				"com.sun.tools.javac.comp.Resolve$VerboseResolutionMode", //
				"com.sun.tools.javac.file.JavacFileManager$SortFiles$1", //
				"com.sun.tools.javac.file.JavacFileManager$SortFiles$2", //
				"com.sun.tools.javac.file.JavacFileManager$SortFiles", //
				"com.sun.tools.javac.jvm.ClassFile$Version", //
				"com.sun.tools.javac.jvm.ClassReader$AttributeKind", //
				"com.sun.tools.javac.jvm.Code$StackMapFormat$1", //
				"com.sun.tools.javac.jvm.Code$StackMapFormat$2", //
				"com.sun.tools.javac.jvm.Code$StackMapFormat", //
				"com.sun.tools.javac.jvm.Profile$1", //
				"com.sun.tools.javac.jvm.Profile", //
				"com.sun.tools.javac.jvm.Target", //
				"com.sun.tools.javac.main.JavaCompiler$CompilePolicy", //
				"com.sun.tools.javac.main.JavaCompiler$ImplicitSourcePolicy", //
				"com.sun.tools.javac.main.Main$Result", //
				"com.sun.tools.javac.main.Option$1", //
				"com.sun.tools.javac.main.Option$10", //
				"com.sun.tools.javac.main.Option$11", //
				"com.sun.tools.javac.main.Option$12", //
				"com.sun.tools.javac.main.Option$13", //
				"com.sun.tools.javac.main.Option$14", //
				"com.sun.tools.javac.main.Option$15", //
				"com.sun.tools.javac.main.Option$16", //
				"com.sun.tools.javac.main.Option$17", //
				"com.sun.tools.javac.main.Option$18", //
				"com.sun.tools.javac.main.Option$19", //
				"com.sun.tools.javac.main.Option$2", //
				"com.sun.tools.javac.main.Option$20", //
				"com.sun.tools.javac.main.Option$21", //
				"com.sun.tools.javac.main.Option$22", //
				"com.sun.tools.javac.main.Option$23", //
				"com.sun.tools.javac.main.Option$24", //
				"com.sun.tools.javac.main.Option$25", //
				"com.sun.tools.javac.main.Option$26", //
				"com.sun.tools.javac.main.Option$27", //
				"com.sun.tools.javac.main.Option$3", //
				"com.sun.tools.javac.main.Option$4", //
				"com.sun.tools.javac.main.Option$5", //
				"com.sun.tools.javac.main.Option$6", //
				"com.sun.tools.javac.main.Option$7", //
				"com.sun.tools.javac.main.Option$8", //
				"com.sun.tools.javac.main.Option$9", //
				"com.sun.tools.javac.main.Option$ChoiceKind", //
				"com.sun.tools.javac.main.Option$OptionGroup", //
				"com.sun.tools.javac.main.Option$OptionKind", //
				"com.sun.tools.javac.main.Option$PkgInfo", //
				"com.sun.tools.javac.main.Option", //
				"com.sun.tools.javac.parser.DocCommentParser$TagParser$Kind", //
				"com.sun.tools.javac.parser.JavacParser$BasicErrorRecoveryAction$1", //
				"com.sun.tools.javac.parser.JavacParser$BasicErrorRecoveryAction$2", //
				"com.sun.tools.javac.parser.JavacParser$BasicErrorRecoveryAction", //
				"com.sun.tools.javac.parser.JavacParser$ParensResult", //
				"com.sun.tools.javac.parser.Tokens$Comment$CommentStyle", //
				"com.sun.tools.javac.parser.Tokens$Token$Tag", //
				"com.sun.tools.javac.parser.Tokens$TokenKind", //
				"com.sun.tools.javac.tree.JCTree$JCLambda$ParameterKind", //
				"com.sun.tools.javac.tree.JCTree$JCMemberReference$OverloadKind", //
				"com.sun.tools.javac.tree.JCTree$JCMemberReference$ReferenceKind", //
				"com.sun.tools.javac.tree.JCTree$JCPolyExpression$PolyKind", //
				"com.sun.tools.javac.tree.JCTree$Tag", //
				"com.sun.tools.javac.util.BasicDiagnosticFormatter$BasicConfiguration$BasicFormatKind", //
				"com.sun.tools.javac.util.BasicDiagnosticFormatter$BasicConfiguration$SourcePosition", //
				"com.sun.tools.javac.util.Bits$BitsState", //
				"com.sun.tools.javac.util.JCDiagnostic$DiagnosticFlag", //
				"com.sun.tools.javac.util.JCDiagnostic$DiagnosticType", //
				"com.sun.tools.javac.util.Log$PrefixKind", //
				"com.sun.tools.javac.util.Log$WriterKind", //
				"com.sun.tools.javac.util.MandatoryWarningHandler$DeferredDiagnosticKind", //
				"com.sun.tools.javac.util.RichDiagnosticFormatter$RichConfiguration$RichFormatterFeature", //
				"com.sun.tools.javac.util.RichDiagnosticFormatter$WhereClauseKind", //
				"com.sun.tools.javadoc.JavaScriptScanner$TagParser$Kind", //
				"com.sun.tools.javadoc.JavaScriptScanner$WhitespaceRetentionPolicy", //
				"com.sun.tools.javadoc.ToolOption$1", //
				"com.sun.tools.javadoc.ToolOption$10", //
				"com.sun.tools.javadoc.ToolOption$11", //
				"com.sun.tools.javadoc.ToolOption$12", //
				"com.sun.tools.javadoc.ToolOption$13", //
				"com.sun.tools.javadoc.ToolOption$14", //
				"com.sun.tools.javadoc.ToolOption$15", //
				"com.sun.tools.javadoc.ToolOption$16", //
				"com.sun.tools.javadoc.ToolOption$17", //
				"com.sun.tools.javadoc.ToolOption$18", //
				"com.sun.tools.javadoc.ToolOption$19", //
				"com.sun.tools.javadoc.ToolOption$2", //
				"com.sun.tools.javadoc.ToolOption$20", //
				"com.sun.tools.javadoc.ToolOption$21", //
				"com.sun.tools.javadoc.ToolOption$22", //
				"com.sun.tools.javadoc.ToolOption$23", //
				"com.sun.tools.javadoc.ToolOption$24", //
				"com.sun.tools.javadoc.ToolOption$25", //
				"com.sun.tools.javadoc.ToolOption$3", //
				"com.sun.tools.javadoc.ToolOption$4", //
				"com.sun.tools.javadoc.ToolOption$5", //
				"com.sun.tools.javadoc.ToolOption$6", //
				"com.sun.tools.javadoc.ToolOption$7", //
				"com.sun.tools.javadoc.ToolOption$8", //
				"com.sun.tools.javadoc.ToolOption$9", //
				"com.sun.tools.javadoc.ToolOption", //
				"com.sun.tools.javap.InstructionDetailWriter$Kind", //
				"com.sun.tools.javap.LocalVariableTableWriter$NoteKind$1", //
				"com.sun.tools.javap.LocalVariableTableWriter$NoteKind$2", //
				"com.sun.tools.javap.LocalVariableTableWriter$NoteKind", //
				"com.sun.tools.javap.LocalVariableTypeTableWriter$NoteKind$1", //
				"com.sun.tools.javap.LocalVariableTypeTableWriter$NoteKind$2", //
				"com.sun.tools.javap.LocalVariableTypeTableWriter$NoteKind", //
				"com.sun.tools.javap.TryBlockWriter$NoteKind$1", //
				"com.sun.tools.javap.TryBlockWriter$NoteKind$2", //
				"com.sun.tools.javap.TryBlockWriter$NoteKind$3", //
				"com.sun.tools.javap.TryBlockWriter$NoteKind", //
				"com.sun.tools.javap.TypeAnnotationWriter$NoteKind", //
				"com.sun.tools.jdeps.Analyzer$Type", //
				"com.sun.tools.jdeps.Profile", //
				"com.sun.tools.jdi.EventDestination", //
				"com.sun.xml.internal.xsom.XSModelGroup$Compositor", //
				"sun.rmi.rmic.newrmic.jrmp.Constants$StubVersion", //
				"org.netbeans.modules.openide.filesystems.FileFilterSupport$FilterElement$ComparisonResult", //
				"org.openide.filesystems.FCLSupport$Op", //
				"javax.annotation.meta.When", //
				"org.netbeans.api.search.SearchPattern$MatchType", //
				"org.netbeans.api.search.provider.SearchFilter$FolderResult", //
				"org.netbeans.api.search.ui.SearchPatternController$Option", //
				"org.netbeans.modules.search.Constants$Limit", //
				"org.netbeans.modules.search.GraphicalSearchListener$EventType", //
				"org.netbeans.modules.search.IgnoreListPanel$ItemType", //
				"org.netbeans.modules.search.MatchingObject$InvalidityStatus", //
				"org.netbeans.modules.search.PatternSandbox$TextPatternSandbox$LineEnding", //
				"org.netbeans.modules.search.ReplaceTask$ResultStatus", //
				"org.netbeans.modules.search.matcher.MultiLineMappedMatcherBig$State", //
				"org.netbeans.spi.search.SearchFilterDefinition$FolderResult", //
				"org.netbeans.api.visual.action.ConnectorState", //
				"org.netbeans.api.visual.action.ContiguousSelectEvent$SelectionType", //
				"org.netbeans.api.visual.action.InplaceEditorProvider$EditorInvocationType", //
				"org.netbeans.api.visual.action.InplaceEditorProvider$ExpansionDirection", //
				"org.netbeans.api.visual.action.ResizeProvider$ControlPoint", //
				"org.netbeans.api.visual.anchor.Anchor$Direction", //
				"org.netbeans.api.visual.anchor.AnchorFactory$DirectionalAnchorKind", //
				"org.netbeans.api.visual.anchor.AnchorShapeFactory$ConnectionEnd", //
				"org.netbeans.api.visual.export.SceneExporter$ImageType", //
				"org.netbeans.api.visual.export.SceneExporter$ZoomType", //
				"org.netbeans.api.visual.graph.layout.TreeGraphLayoutAlignment", //
				"org.netbeans.api.visual.layout.LayoutFactory$ConnectionWidgetLayoutAlignment", //
				"org.netbeans.api.visual.layout.LayoutFactory$SerialAlignment", //
				"org.netbeans.api.visual.model.ObjectSceneEventType", //
				"org.netbeans.api.visual.print.ScenePrinter$ScaleStrategy", //
				"org.netbeans.api.visual.widget.ConnectionWidget$RoutingPolicy", //
				"org.netbeans.api.visual.widget.EventProcessingType", //
				"org.netbeans.api.visual.widget.LabelWidget$Alignment", //
				"org.netbeans.api.visual.widget.LabelWidget$Orientation", //
				"org.netbeans.api.visual.widget.LabelWidget$VerticalAlignment", //
				"org.netbeans.api.visual.widget.ScrollWidget$SliderWidget$Part", //
				"org.netbeans.api.visual.widget.SeparatorWidget$Orientation", //
				"org.netbeans.api.visual.widget.general.IconNodeWidget$TextOrientation", //
				"org.netbeans.modules.visual.graph.layout.hierarchicalsupport.DirectedGraph$DummyVertex$Type", //
				"org.netbeans.modules.visual.graph.layout.orthogonalsupport.MGraph$DummyVertex$Type", //
				"org.netbeans.modules.visual.graph.layout.orthogonalsupport.MGraph$Edge$Direction", //
				"org.netbeans.core.output2.OutputKind", //
				"org.netbeans.core.output2.OutputTab$ACTION", //
				"org.netbeans.core.output2.options.OutputOptions$LinkStyle", //
				"org.netbeans.core.ui.options.general.GeneralOptionsModel$TestingStatus", //
				"org.netbeans.core.ui.options.general.WebBrowsersOptionsModel$ChangeStatus", //
				"org.netbeans.core.windows.WindowSystemEventType", //
				"org.netbeans.core.windows.view.dnd.KeyboardDnd$Side", //
				"org.netbeans.core.windows.view.ui.toolbars.ToolbarConstraints$Align", //
				"org.netbeans.lib.uihandler.InputGesture", //
				"org.netbeans.lib.uihandler.LogRecords$Parser$Elem", //
				"org.netbeans.api.autoupdate.OperationException$ERROR_TYPE", //
				"org.netbeans.api.autoupdate.UpdateManager$TYPE", //
				"org.netbeans.api.autoupdate.UpdateUnitProvider$CATEGORY", //
				"org.netbeans.modules.autoupdate.services.InstallSupportImpl$STEP", //
				"org.netbeans.modules.autoupdate.services.OperationContainerImpl$OperationType", //
				"org.netbeans.modules.autoupdate.updateprovider.AutoupdateCatalogParser$ELEMENTS", //
				"org.netbeans.modules.autoupdate.updateprovider.AutoupdateInfoParser$ELEMENTS", //
				"org.netbeans.modules.autoupdate.ui.UnitCategoryTableModel$Type", //
				"org.netbeans.modules.autoupdate.ui.wizards.OperationWizardModel$OperationType", //
				"org.netbeans.modules.masterfs.filebasedfs.fileobjects.FileObjectFactory$Caller", //
				"org.netbeans.modules.masterfs.filebasedfs.naming.NamingFactory$FileType", //
				"org.netbeans.modules.options.export.OptionsChooserPanel$PanelType", //
				"org.netbeans.modules.options.export.OptionsExportModel$ParserState", //
				"org.netbeans.modules.options.export.OptionsExportModel$State", //
				"org.netbeans.modules.print.util.Macro", //
				"org.netbeans.api.queries.SharabilityQuery$Sharability", //
				"org.netbeans.modules.sendopts.DefaultProcessor$Type", //
				"org.netbeans.modules.uihandler.ConnectionErrorDlg$SegmentKind", //
				"org.netbeans.modules.uihandler.Installer$Button", //
				"org.netbeans.modules.uihandler.Installer$DataType", //
				"org.netbeans.modules.uihandler.Installer$Submit$DialogState", //
				"org.netbeans.swing.etable.ETable$ColumnSelection", //
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$1", //
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$2", //
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$3", //
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType$4", //
				"org.netbeans.swing.tabcontrol.customtabs.TabbedType", //
				"org.openide.awt.ContextSelection", //
				"org.openide.awt.HtmlRendererImpl$Type", //
				"org.openide.awt.NotificationDisplayer$Priority", //
				"org.openide.awt.QuickSearch$QS_FIRE", //
				"org.openide.windows.IOColors$OutputType", //
				"org.openide.windows.IOSelect$AdditionalOperation", //
				"org.openide.loaders.FolderChildren$RefreshMode", //
				"org.openide.text.CloneableEditorInitializer$Phase", //
				"org.openide.text.DocumentStatus", //
				"org.openide.text.Line$ShowOpenType", //
				"org.openide.text.Line$ShowVisibilityType", //
				"org.openide.text.PrintPreferences$Alignment", //
				"org.netbeans.lib.profiler.ui.threads.ViewManager$Position", //
				"org.netbeans.lib.profiler.classfile.ClassInfo$StackMapFrame$FrameType", //
				"org.netbeans.modules.profiler.api.java.SourcePackageInfo$Scope", //
				"org.netbeans.modules.profiler.spi.LoadGenPlugin$Result", //
				"org.netbeans.modules.profiler.heapwalk.details.basic.ArrayValueView$Type", //
				"org.netbeans.modules.profiler.snaptracer.logs.LogRecords$Parser$Elem", //
				"com.sun.tools.visualvm.core.ui.DataSourceView$Alert", //
				"com.sun.tools.visualvm.modules.appui.options.NetworkOptionsModel$TestingStatus", //
				"com.sun.tools.visualvm.sampler.SamplerImpl$State", //
				"com.sun.tools.visualvm.tools.jmx.JmxModel$ConnectionState"

		}));
	}

}
