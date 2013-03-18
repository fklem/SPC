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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.spc.dependency.DependencyResolver;
import org.spc.dependency.TypeDependencyResolver;

public class ContainerTest {

	@Test
	public void simpleTest() throws Exception {
		Container _c = new Container();

		_c.add(new String("test_name"));
		_c.add(new TestObject());

		System.out.println(_c.getObjects());
	}

	@Test
	public void testTypeDependencyResolver() throws Exception {
		TypeDependencyResolver _tdr = new TypeDependencyResolver();

		Collection<Object> _coll = new ArrayList<Object>();

		_coll.add("testString");
		_coll.add(new TestObject());

		Object _o = _tdr.resolveDependency("java.lang.String", _coll);

		System.out.println(_o);
	}

	@Test
	public void testDependencyResolver() throws Exception {
		DependencyResolver _resolver = new DependencyResolver();
		Object _o = new TestObject();

		Map<String, Object> _map = new HashMap<String, Object>();

		_map.put("nombreTest0", "nombre de prueba");
		_map.put("nombreTest1", "nombre dos");

		_resolver.resolveDependenciesFor(_o, _map);

		System.out.println(_o);
	}

	@Test
	public void testExecutableContainer() throws Exception {
		Container _c = new Container();

		List<String> _list = new ArrayList<String>();

		_c.add("nombreTest", "nombreTestExecutable");
		_c.add(_list);

		_c.add(new TestExecutable(""));
		_c.add(new TestExecutableList());

		_c.execute();
	}

	@Test
	public void testExecutableQueue() throws Exception {
		ExecutableQueue _queue = new ExecutableQueue();

		_queue.add(new TestExecutable("uno"));
		_queue.add(new TestExecutable("dos"));
		_queue.add(new TestExecutable("tres"));

		for (ExecutableObjectDescriptor _desc : _queue) {
			System.out.println(_desc);
		}

	}
}
