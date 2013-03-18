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

import java.util.List;

import org.spc.annotation.DependsOn;
import org.spc.annotation.Executable;

public class TestExecutable {
	private String name;
	private String id;

	public TestExecutable(String _id) {
		id = _id;
	}

	public void setName(String _name) {
		name = _name;
	}

	@Executable(generateDependency="listaResultado")
	public List<String> doSomeStuff(@DependsOn(id="nombreTest") String _param, List<String> _param2) {
		System.out.println("doing some stuff! " + _param);
		_param2.add(_param);
		
		return _param2;
	}

	@Override
	public String toString() {
		return "TestExecutable [name=" + name + ", id=" + id + "]";
	}

}
