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
package org.spc.dependency;

import java.lang.reflect.Method;
import java.util.Map;

import org.spc.annotation.DependsOn;

public class DependencyResolver {

	private TypeDependencyResolver typeDependencyResolver = new TypeDependencyResolver();
	private DependencyInjector dependencyInjector = new DependencyInjector();

	public void resolveDependenciesFor(Object _o, Map<String, Object> _objects)
			throws UnresolvedDependencyException, UninjectableMethodException {
		Method[] _methods = _o.getClass().getMethods();

		for (Method _method : _methods) {
			if (_method.isAnnotationPresent(DependsOn.class)) {
				DependsOn _d = _method.getAnnotation(DependsOn.class);

				Object _dependency = null;

				if ((!_d.id().isEmpty()) && (_objects.containsKey(_d.id()))) {
					_dependency = _objects.get(_d.id());
				} else if (!_d.type().isEmpty()) {
					_dependency = typeDependencyResolver.resolveDependency(
							_d.type(), _objects.values());
				}

				String _dependencyName = DependencyNameBuilder.buildName(_o
						.getClass().getCanonicalName(), _method.getName(), _d
						.id(), _d.type());

				if (_dependency == null)
					throw new UnresolvedDependencyException(_dependencyName);

				dependencyInjector.injectDependency(_dependencyName, _o,
						_method, _dependency);
			}
		}
	}

	public Object findDependency(String _dependencyName, String _id,
			String _type, Class<?> _class, Map<String, Object> _objects)
			throws UnresolvedDependencyException {
		Object _dependency = null;

		if ((_id != null) && !_id.isEmpty()) {
			_dependency = _objects.get(_id);
		} else if ((_type != null) && !_type.isEmpty()) {
			_dependency = typeDependencyResolver.resolveDependency(_type,
					_objects.values());
		} else {
			_dependency = typeDependencyResolver.resolveDependency(
					_class.getCanonicalName(), _objects.values());

		}

		if (_dependency == null)
			throw new UnresolvedDependencyException(_dependencyName);

		return _dependency;
	}
}
