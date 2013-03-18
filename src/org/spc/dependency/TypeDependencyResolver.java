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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/*
 * REESCRIBIR ESTA CLASE 
 */
public class TypeDependencyResolver {
	private Map<String, Object> typeMap = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public Object resolveDependency(String _type, Collection<Object> _objects)
			throws UnresolvedDependencyException {
		Object _o = typeMap.get(_type);

		if (_o == null) {
			Class<Object> _c = null;
			try {
				_c = (Class<Object>) Class.forName(_type);
			} catch (ClassNotFoundException _e) {
				return null;
			}

			for (Object _object : _objects) {
				if (_c.isInstance(_object)) {
					typeMap.put(_type, _object);
					_o = _object;
					break;
				}
			}
		}

		return _o;
	}

	public Object resolveDependency(Class<Object> _class, Collection<Object> _objects)
			throws UnresolvedDependencyException {
		Object _o = typeMap.get(_class.getCanonicalName());

		if (_o == null) {
			for (Object _object : _objects) {
				if (_class.isInstance(_object)) {
					typeMap.put(_class.getCanonicalName(), _object);
					_o = _object;
					break;
				}
			}
		}

		return _o;
	}
}
