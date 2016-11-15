package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> listA = new ArrayList<String>();
		List<String> listB = new ArrayList<String>();

		listA.add("A");
		listA.add("B");
		listA.add("C");

		listA.add("M");
		listA.add("N");
		listA.add("O");
		listB.add("M");
		listB.add("N");
		listB.add("O");

		
		listB.add("X");
		listB.add("Y");
		listB.add("Z");
		
		
		System.out.println("List A       : " + listA);
		System.out.println("List A       : " + listB);
		System.out.println("List A (I) B : " + listA.stream().filter(listB::contains).collect(Collectors.toList()));
		System.out.println("List A (-) B : " + listA.stream().filter(
				f -> 
					!listB.contains(f)
				
				
				).collect(Collectors.toList()));
		
		

	}

}
