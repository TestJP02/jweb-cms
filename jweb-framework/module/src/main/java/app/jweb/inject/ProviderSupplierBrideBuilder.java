package app.jweb.inject;

import app.jweb.ApplicationException;
import app.jweb.util.type.Types;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author chi
 */
public class ProviderSupplierBrideBuilder<T> {
    private static final AtomicInteger INDEX = new AtomicInteger();
    private final Class<? extends Provider<T>> providerClass;

    public ProviderSupplierBrideBuilder(Class<? extends Provider<T>> providerClass) {
        this.providerClass = providerClass;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Supplier<T>> build() {
        ClassPool classPool = ClassPool.getDefault();
        CtClass classBuilder = classPool.makeClass(providerClass.getCanonicalName() + "$Bride" + INDEX.incrementAndGet());
        try {
            classBuilder.addInterface(classPool.get(Supplier.class.getName()));
            ConstPool constPool = classBuilder.getClassFile().getConstPool();
            CtField field = CtField.make(String.format("%s provider;", Types.className(providerClass)), classBuilder);
            Annotation ctAnnotation = new Annotation(Inject.class.getCanonicalName(), constPool);
            AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            attr.addAnnotation(ctAnnotation);
            field.getFieldInfo().addAttribute(attr);
            classBuilder.addField(field);

            CtMethod ctMethod = CtMethod.make("public Object get() {return provider.get();}", classBuilder);
            classBuilder.addMethod(ctMethod);

            return classBuilder.toClass();
        } catch (CannotCompileException | NotFoundException e) {
            throw new ApplicationException("failed to create provider bride, providerType={}", providerClass, e);
        }
    }
}
