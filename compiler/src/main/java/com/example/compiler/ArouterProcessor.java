package com.example.compiler;

import com.example.annotation.Arouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

/**
 * java 控制台乱码问题 在对应module的gradle中加入一下代码 tasks.withType(JavaCompile) { options.encoding = "UTF-8" }
 *
 * @author : Chengjs
 * @date : 2020-01-03 14:29
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.example.annotation.Arouter"})/*全类名*/
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("content")/* 接收从app传递过来的参数 app gradle需要配置
javaCompileOptions{
            annotationProcessorOptions{
                arguments=[content:'hello apt']
            }
        } 保证在defaultConfig节点之下
        key要保持一致    从init方法中获取processingEnv.getOptions().get("content")*/
public class ArouterProcessor extends AbstractProcessor {

    //操作element工具类  javax包下  以下字段类同
    private Elements elementUtils;
    //type（类信息）工具类
    private Types typeUtils;
    //用来输出警告、错误等日志
    private Messager messager;
    //文件生成器
    private Filer filer;

    /**
     * 可用@SupportedAnnotationTypes注解代替  获取处理的注解类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    /**
     * 可用注解@SupportedSourceVersion代替
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    /**
     * 可用注解代替
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }


    /**
     * 关键方法  返回true 表示处理成功 false 处理失败
     *
     * @param annotations 使用了该处理器对应注解的节点集合
     * @param roundEnv 当前或者之前的运行环境 可以通过该对象查找找到的注解
     * @return true 表示后续处理器不会再处理（已经处理完成）
     */
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        if (annotations.isEmpty()) {
//            return false;
//        }
//        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Arouter.class);
//        for (Element element : elementsAnnotatedWith) {
//            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
//            String className = element.getSimpleName().toString();
//            messager.printMessage(Kind.NOTE, "被注解的类————" + className);
//            String finalName = className.concat("$$Arouter");
//            try {
//                JavaFileObject sourceFile = filer.createSourceFile(packageName.concat(".").concat(finalName));
//
//                /**
//                 *  public static Class findTarget(String path) {
//                 *         if (path.equalsIgnoreCase("/app/MainActicity")) {
//                 *             return MainActivity.class;
//                 *         }
//                 *         return null;
//                 *     }
//                 * }
//                 * */
//                Writer writer = sourceFile.openWriter();
//                writer.write("package " + packageName + ";\n");
//                writer.write("public class " + finalName + "{\n");
//                writer.write(" public static Class findTarget(String path) { \n");
//                Arouter annotation = element.getAnnotation(Arouter.class);
//                writer.write(" if (path.equalsIgnoreCase(\"" + annotation.path() + "\")) {\n");
//                writer.write("return " + className + ".class;\n}\n");
//                writer.write("return null;\n}\n}");
//                writer.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return true;
//    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Arouter.class);
        for (Element element : elementsAnnotatedWith) {
            Arouter annotation = element.getAnnotation(Arouter.class);
            String className=element.getSimpleName().toString();
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String finalName=className.concat("$$Arouter");
            MethodSpec methodSpec = MethodSpec.methodBuilder("findTarget")
                    .addParameter(String.class, "path")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(Class.class)
                    .addStatement(" return path.equalsIgnoreCase($S) ? $T.class:null", annotation.path(),
                            ClassName.get((TypeElement) element)).build();
            TypeSpec typeSpec = TypeSpec.classBuilder(finalName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpec).build();
            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        String content = processingEnv.getOptions().get("content");
        messager.printMessage(Kind.NOTE, content);
    }
}
