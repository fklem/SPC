/*
Copyright (c) 2013 Federico Ricca

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
OTHER DEALINGS IN THE SOFTWARE.
 */
package org.spc.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.spc.annotation.DependsOn;
import org.spc.dependency.DependencyNameBuilder;
import org.spc.dependency.DependencyResolver;
import org.spc.dependency.UninjectableMethodException;
import org.spc.dependency.UnresolvedDependencyException;

public class Container {
	private Map<String, Object> objects = new HashMap<String, Object>();
	private DependencyResolver dependencyResolver = new DependencyResolver();
	private ExecutableQueue executableQueue = new ExecutableQueue();

	public void add(Object _o) throws UnresolvedDependencyException,
			UninjectableMethodException {
		this.add("#unnamedObject-" + _o.hashCode(), _o);
	}

	public void add(String _id, Object _o)
			throws UnresolvedDependencyException, UninjectableMethodException {
		if (objects.containsKey(_id))
			return;

		objects.put(_id, _o);

		dependencyResolver.resolveDependenciesFor(_o, objects);

		executableQueue.add(_o);
	}

	public Collection<Object> getObjects() {
		return objects.values();
	}

	public void execute() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			UnresolvedDependencyException, UninjectableMethodException {
		for (ExecutableObjectDescriptor _desc : executableQueue) {
			Method _method = _desc.getMethod();

			Annotation[][] _annotationParams = _method
					.getParameterAnnotations();
			Class<?>[] _paramTypes = _method.getParameterTypes();

			Object[] _params = new Object[_paramTypes.length];

			for (int _i = 0; _i < _paramTypes.length; _i++) {
				Class<?> _class = _paramTypes[_i];
				String _id = "";
				String _type = "";

				Annotation[] _annotations = _annotationParams[_i];

				if (_annotations.length > 0) {
					// check reference id
					Annotation _a = _annotations[0];

					if (_a instanceof DependsOn) {
						_id = ((DependsOn) _a).id();
						_type = ((DependsOn) _a).type();
					}
				}

				if ((_type == null) || _type.isEmpty())
					_type = _class.getCanonicalName();

				_params[_i] = dependencyResolver.findDependency(
						DependencyNameBuilder.buildName(_desc
								.getExecutableObject().getClass()
								.getCanonicalName(), _method.getName()
								+ "#param:" + _i, _id, _type), _id, _type,
						_class, objects);
			}

			Object _result = _method.invoke(_desc.getExecutableObject(),
					_params);

			if (_result != null)
				this.add(_desc.getGeneratedDependency(), _result);
		}
	}

}
