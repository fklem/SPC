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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spc.annotation.Executable;
import org.spc.util.MethodFilter;

public class ExecutableQueue implements Iterable<ExecutableObjectDescriptor> {

	private List<ExecutableObjectDescriptor> queue = new ArrayList<ExecutableObjectDescriptor>();

	public void add(Object _o) {
		List<Method> _methods = MethodFilter.annotatedMethods(_o,
				Executable.class);

		for (Method _method : _methods)
			queue.add(new ExecutableObjectDescriptor(_o, _method, _method.getAnnotation(Executable.class).generateDependency()));

	}

	public Iterator<ExecutableObjectDescriptor> iterator() {
		return queue.iterator();
	}
}
