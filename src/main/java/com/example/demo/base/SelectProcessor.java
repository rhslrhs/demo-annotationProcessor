package com.example.demo.base;

import com.example.demo.base.Select;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("com.example.demo.base.Select")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class SelectProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Select.class)) {
            // 필드 이름 가져오기
            String fieldName = element.getSimpleName().toString();

            // 클래스 정보 가져오기
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String className = enclosingElement.getSimpleName().toString();
            PackageElement packageElement = (PackageElement) enclosingElement.getEnclosingElement();
            String packageName = packageElement.getQualifiedName().toString();

            // 생성될 클래스 이름
            String generatedClassName = className + "Generated";

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing field: " + fieldName + " in class: " + className);

            try {
                // 새로운 소스 파일 생성
                JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + generatedClassName);
                try (Writer writer = builderFile.openWriter()) {
                    writer.write("package " + packageName + ";\n");
                    writer.write("public class " + generatedClassName + " {\n");
                    writer.write("    public static String select_" + fieldName + "() {\n");
                    writer.write("        return \"" + fieldName + "\";\n");
                    writer.write("    }\n");
                    writer.write("}\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
