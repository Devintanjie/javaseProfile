package com.taobao.bird.common.utils.compile;

import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import com.taobao.bird.common.utils.compile.exception.CompileExprException;
import com.taobao.bird.common.utils.compile.exception.JdkCompileException;

public class JdkCompiler implements JavaSourceCompiler {

    private List<String> options;

    public JdkCompiler(){
        options = new ArrayList<String>();
        // options.add("-target");
        // options.add("1.6");
    }

    @SuppressWarnings("rawtypes")
    public Class compile(String sourceString) {
        JavaSource source = new JavaSource(sourceString);
        return compile(source);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Class compile(JavaSource javaSource) {
        try {
            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
            JdkCompileTask compileTask = new JdkCompileTask(new JdkCompilerClassLoader(this.getClass().getClassLoader()),
                options);
            String fullName = javaSource.getPackageName() + "." + javaSource.getClassName();
            Class newClass = compileTask.compile(fullName, javaSource.getSource(), errs);
            return newClass;
        } catch (JdkCompileException ex) {
            DiagnosticCollector<JavaFileObject> diagnostics = ex.getDiagnostics();
            throw new CompileExprException("compile error, source : \n" + javaSource + ", "
                                           + diagnostics.getDiagnostics(), ex);
        } catch (Exception ex) {
            throw new CompileExprException("compile error, source : \n" + javaSource, ex);
        }

    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
