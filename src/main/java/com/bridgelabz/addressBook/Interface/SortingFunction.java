package com.bridgelabz.addressBook.Interface;

import java.util.stream.Stream;

@FunctionalInterface
public interface SortingFunction {

	Stream<Contacts> sort(Stream<Contacts> input);
}
