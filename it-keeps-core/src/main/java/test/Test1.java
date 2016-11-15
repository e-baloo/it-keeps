package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> listDstNew = new ArrayList<String>();
		List<String> listDstBck = new ArrayList<String>();

		listDstNew.add("A");
		listDstNew.add("B");
		listDstNew.add("C");

		listDstNew.add("M");
		listDstNew.add("N");
		listDstNew.add("O");
		listDstBck.add("M");
		listDstBck.add("N");
		listDstBck.add("O");

		
		listDstBck.add("X");
		listDstBck.add("Y");
		listDstBck.add("Z");
		
		
		System.out.println("List listDstNew       : " + listDstNew);
		System.out.println("List listDstBck       : " + listDstBck);
		System.out.println("List listRemove       : " + listDstBck.stream().filter(
				f -> 
				!listDstNew.contains(f)
				).collect(Collectors.toList()));
		System.out.println("List listAdd          : " + listDstNew.stream().filter(
				f -> 
				!listDstBck.contains(f)
				).collect(Collectors.toList()));
		
		

	}

}
