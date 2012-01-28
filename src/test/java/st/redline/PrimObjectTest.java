/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PrimObjectTest {

	@Test
	public void shouldDelegatePackageLookupToClass() {
		PrimObject aClass = mock(PrimObject.class);
		PrimObject primObject = new PrimObject();
		primObject.cls(aClass);
		primObject.packageFor("SomeClass");
		verify(aClass).packageFor("SomeClass");
	}

	@Test
	public void shouldLookupImportsForFullyQualifiedClassNameWhenResolvingObjects() {
		PrimObject aClass = mock(PrimObject.class);
		PrimObject primObject = new PrimObject();
		PrimObject spy = spy(primObject);
		when(spy.packageFor("Thing")).thenReturn("st.redline.Thing");
		when(spy.resolveObject("st.redline.Thing")).thenReturn(aClass);
		assertEquals(aClass, spy.resolveObject("Thing"));
	}

	@Test
	public void resolveObjectShouldReturnClassFromClassesRegistryIfFound() {
		PrimObject aClass = mock(PrimObject.class);
		PrimObject.classes.put("Thing", aClass);
		PrimObject primObject = new PrimObject();
		assertEquals(aClass, primObject.resolveObject("Thing"));
	}

	@Test
	public void variableAtShouldCallResolveObject() {
		PrimObject primObject = new PrimObject();
		PrimObject spy = spy(primObject);
		spy.variableAt("Thing");
		verify(spy).resolveObject("Thing");
	}

	@Test
	public void shouldHaveClassesRegistry() {
		assertNotNull(PrimObject.classes);
	}

	@Test
	public void shouldAddSlotToAttributesForClass() {
		PrimObject object = new PrimObject(5);
		assertEquals(object.attributes.length, 6);
	}

	@Test
	public void shouldInitializeAllAttributesToPrimitiveNil() {
		PrimObject object = new PrimObject(5);
		for (int i = 0; i < 5; i++)
			assertEquals(object.attributes[i], PrimObject.PRIM_NIL);
	}

	@Test
	public void shouldAddSlotForClassInAttributes() {
		PrimObject object = new PrimObject(3);
		assertEquals(object.attributes.length, 4);
	}

	@Test
	public void shouldInitializeItselfAsSuperclass() {
		PrimObject object = new PrimObject();
		assertEquals(object.superclass(), object);
	}

	@Test
	public void shouldAnswerDoesNotUnderstandAsMethodForAnySelector() {
		PrimObject object = new PrimObject();
		assertEquals(object.methodFor("foo"), PrimObject.BASIC_DOES_NOT_UNDERSTAND);
		assertEquals(object.methodFor("bar"), PrimObject.BASIC_DOES_NOT_UNDERSTAND);
	}

	@Test
	public void shouldAnswerTrueToIncludesSelector() {
		PrimObject object = new PrimObject();
		assertTrue(object.includesSelector("foo"));
		assertTrue(object.includesSelector("bar"));
	}

	@Test
	public void performShouldAnswerPrimitiveNilWhenNoClassSet() {
		PrimObject object = new PrimObject();
		assertEquals(object.perform("anything"), PrimObject.PRIM_NIL);
	}

	@Test
	public void invokeShouldAnswerSelfWhenNoClassSet() {
		PrimObject object = new PrimObject();
		assertEquals(object, object.invoke(object, new PrimContext(object, null, "anything")));
	}

	@Test
	public void shouldCreateWithStringJavaValue() {
		String javaValue = "string";
		PrimObject object = PrimObject.string(javaValue);
		assertEquals(object.javaValue(), javaValue);
	}

	@Test
	public void shouldProvideAccessToClass() {
		PrimObject object = new PrimObject();
		assertEquals(object.cls(), PrimObject.PRIM_NIL);
	}

	@Test
	public void shouldProvideMutateOfClass() {
		PrimObject object = new PrimObject();
		PrimObject aClass = new PrimObject();
		object.cls(aClass);
		assertEquals(object.cls(), aClass);
	}
}
